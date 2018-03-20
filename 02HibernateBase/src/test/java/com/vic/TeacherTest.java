package com.vic;

import com.vic.pojo.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TeacherTest {
    Transaction transaction=null;
    Session session=null;

    @Before
    public void before(){
        //01.读取核心配置文件 configure()底层就是加载了/hibernate.cfg.xml
        Configuration configuration=new Configuration().configure();
        //02.创建会话工厂 sessionFactory
        SessionFactory factory=configuration.buildSessionFactory();
        //03.创建会话 session
       session=factory.openSession();
       //04.开启事务
        transaction=session.beginTransaction();
    }

    @After
    public void after(){
        //07.提交事务
        transaction.commit();   //assigned 产生sql语句
        //08.关闭会话
        session.close();
    }

    /**
     * 新增教师信息
     *
     * Hibernate的核心 ：一个类和五个接口
     *
     * 一个类  Configuration：读取核心配置文件
     *
     * 五个接口：
     * 01.SessionFactory:   负责初始化Hibernate.cfg.xml文件中所有配置信息
     *                      在程序中一个就够了，使用单例模式
     * 02.Session   ：   是hibernate中用来创建事务以及对对象的增删改查操作
     * 03.Transaction:   事务的管理
     * 04：Query     ：  sql,hql
     * 05.Criteria   ：  完全面向对象
     */
    @Test
    public void addTeacher(){
        //05.创建新增对象
        Teacher teacher=new Teacher();
        teacher.setId(2);
        teacher.setName("大白222");
        //06.持久化操作
        System.out.println("*************************************");
        session.save(teacher);  //identity产生sql语句
        System.out.println("*************************************");
    }

    /**
     * 数据库分两步执行删除操作
     *
     * 01.首先根据对象的id 去数据库中查询是否有此记录
     * 02.如果存在      根据id删除指定的信息     会产生2条sql语句
     *    如果不存在    只执行查询操作      只有1条sql语句
     */
    @Test
    public void deleteTeacher(){
        //创建需要删除的对象
        Teacher teacher=new Teacher();
        teacher.setId(2);
        System.out.println("***************************************");
        session.delete(teacher);    //执行删除
        System.out.println("***************************************");
    }

    /**
     * 修改
     * 一步到位，直接执行update的语句，不会先进行查询操作
     */
    @Test
    public void updateTeacher(){
        //创建需要修改的对象
        Teacher teacher=new Teacher();
        teacher.setId(1);
        teacher.setName("超人666");
        System.out.println("**************************************");
        session.update(teacher);
        System.out.println("***************************************");
    }

    /**
     * get和load的异同
     *   相同点：都支持缓存
     *      首先会去hibernate的一级缓存（session）中查询是否有对应数据
     *      如果有，直接返回，不会再去访问数据库。
     *      如果没有，则会去我们配置的二级缓存（在已配置的情况下）去查询
     *      如果二级缓存也没有，才会访问数据库
     *   不同点：
     *      get
     *          01.调用get()方法之后，立即产生sql语句
     *          02.如果数据库中有对应数据，直接返回
     *              如果数据库中没有对应的数据，则返回null
     *      load
     *          01.支持懒加载，可以在用户需要时才生成sql语句
     *          02.如果数据库中有对应数据，直接返回
     *              如果数据库中没事对应的数据，则抛出ObjectNotFoundException异常
     *
     */

    /**
     * sql语句在分界线上，说明是立即加载
     */
    @Test
    public void getTeacher(){
        Teacher teacher=(Teacher) session.get(Teacher.class,2);
        System.out.println("******************************************");
        System.out.println(teacher);
    }

    /**
     * sql语句在分界线下，说明是懒加载，及调用的时候生成sql语句
     * （前提是开启懒加载，即lazy="true",默认是开启的）
     */
    @Test
    public void loadTeacher(){
        Teacher teacher=(Teacher) session.load(Teacher.class,1);
        System.out.println("*****************************************");
        System.out.println(teacher);
    }

    /**
     * load也可以立即加载，只需关闭懒加载
     * 在对用的hbm,xml文件中的class节点上，增加lazy="false"属性
     * 关闭后，sql语句生成在分解线上方
     */
    @Test
    public void loadTeacher2(){
        Teacher teacher=(Teacher) session.load(Teacher.class,1);
        System.out.println("********************************************");
        System.out.println(teacher);
    }
    /**
     * 验证get会把查询到的数据放进session的缓存中
     * 控制台只会显示一条sql语句
     */
    @Test
    public void getTeacher2(){
        System.out.println("***********************************");
        Teacher teacher1=(Teacher) session.get(Teacher.class,2);
        Teacher teacher2=(Teacher) session.get(Teacher.class,2);
        System.out.println("***********************************");
    }

    /**
     * evict
     * 从session中清楚指定的对象
     * 再次查询id为1的Teacher是会生成新的sql语句
     */
    @Test
    public void evictTeacher(){
        Teacher teacher1=(Teacher) session.get(Teacher.class,1);
        Teacher teacher2=(Teacher) session.get(Teacher.class,2);
        System.out.println("****************************************************");
        //清楚teacher1    再次查询
        session.evict(teacher1);
        Teacher teacher3=(Teacher) session.get(Teacher.class,1);//控制台显示sql语句
        //teacher2=(Teacher) session.get(Teacher.class,2);
    }

    /**
     * clear
     * 从session中清空所有对象
     * 一共生成了4条sql语句
     */
    @Test
    public void clearTeacher(){
        Teacher teacher1=(Teacher) session.get(Teacher.class,1);
        Teacher teacher2=(Teacher) session.get(Teacher.class,2);
        System.out.println("****************************************************");
        //清空所有对象    再次查询
        session.clear();
        teacher1=(Teacher) session.get(Teacher.class,1);
        teacher2=(Teacher) session.get(Teacher.class,2);

    }

}
