package com.ming;

import com.ming.mapper.OrdersMapper;
import com.ming.mapper.UserMapper;
import com.ming.po.Orders;
import com.ming.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 一二级缓存适用场景：
 * 1. 经常查询的数据
 * 2. 相对来说不重要的数据
 * 3. 对实时性要求不高的数据
 * 4. 在多表关联中，最好存放在单表的数据在缓存中（否则容易产生幻读）
 */
public class MyBatisTest {


    /**
     * APIs of the Mybatis
     * SqlSessionFactory  thread safe
     * SqlSession         thread not-safe------> {xxxxx} : 所以写在方法体中
     */
    private SqlSessionFactory sqlSessionFactory = null;


    @Before
    public void testInit() {
        String file = "sqlMapConfig.xml";
        try {
            InputStream is = null;
            is = Resources.getResourceAsStream(file);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //测试一级缓存
    @Test
    public void testQueryUserById_Level_I() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //第一次查询（先到sqlSession中看看有没有需要的记录，如果没有，去DB中查，发出sql语句）
            /**
             * 当执行queryUserById这个方法的时候，就会写入一级缓存sqlSession
             * sqlSession就是一级缓存，HashMap(Key---Value)
             */
            User user = userMapper.queryUserById(1);
            System.out.println(user);


            //清空一级缓存
            user.setUserId(1);
            user.setUserName("TTTTT");
            user.setUserAddr("ttttt");
            userMapper.modifyUserById(user);//发出update语句
            sqlSession.commit();//清空一级缓存


            //第二次查询（先到sqlSession中看看有没有需要的记录，如果有，不再去DB中查，不发出sql语句，直接从缓存中取数据）
            user = userMapper.queryUserById(1);//从缓存中取数据
            System.out.println(user);
            /***
             * 一级缓存的特点：
             *  （1） 直接用，无需配置
             *  （2） 不得不用，无法剔除
             *  （3） 无法管理一级缓存
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    //测试二级缓存
    @Test
    public void testQueryUserById_II() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        SqlSession sqlSession3 = sqlSessionFactory.openSession();
        try {

            /**
             * 二级缓存的特点：
             * 1,需要配置
             * 2,可以管理(开启，关闭，清空，使用)
             *
             */

            /**
             *  一级和二级缓存适用的场景：
             *   1， 被经常查询的数据【适用】
             *   2， 相对来说不重要的数据【适用】
             *   3， 对实时性要求不高的数据【适用】
             *   4,  在多表关联中，最好是存放单表的数据在缓存中【适用】
             *   5,
             *   6,
             * */
            UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
            User user1 = userMapper1.queryUserById(1);
            System.out.println(user1);
            sqlSession1.close();//一级缓存关闭，只有关闭，才能写入二级缓存区域中去

            //udpate,delete,insert清空二级缓存[待定]
            UserMapper userMapper3 = sqlSession3.getMapper(UserMapper.class);
            User user3 = userMapper3.queryUserById(1);
            userMapper3.modifyUserById(user3);
            sqlSession3.commit();//清空二级缓存

            UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
            User user2 = userMapper2.queryUserById(1);//是否发出sql语句？
            System.out.println(user2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //sqlSession.close();
        }
    }

    //测试延迟加载
    /**
     * 延迟加载的适用规则
     * 1，当数据特别多的时候
     * 2，当关联查询特别频繁的时候
     * 3，当需要讲求效率的时候
     *
     */
    @Test
    public void testQueryUserById_LazyLoading() {
        SqlSession sqlSession = sqlSessionFactory.openSession();;
        try {
            OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
            //此时发出sql语句（select * from orders）
            List<Orders> ordersList = ordersMapper.findorderuserlazyload();

            //需求是：【根据orders查询user】所以延迟发出第二条sql语句，实现按需求加载信息
            //也就是我们说的延迟加载
            for (Orders orders: ordersList) {
                System.out.println("username = " + orders.getUser().getUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
