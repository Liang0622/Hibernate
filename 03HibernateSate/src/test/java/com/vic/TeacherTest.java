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
        //01.读取核心配置文件
        Configuration configuration=new Configuration().configure();
        //02.创建会话工厂
        SessionFactory sf=configuration.buildSessionFactory();
        //03.创建会话
        session=sf.openSession();
        //04.开启事务
        transaction=session.beginTransaction();
    }
    @After
    public void after(){
        //08.关闭会话
        session.close();

    }

    @Test
    public void selectTest(){
        Teacher teacher=(Teacher) session.get(Teacher.class,2);
        System.out.println(teacher);
    }

    /**
     *   Hibernate对象的三种状态
     *   1.瞬时态（临时态，自由态）
     *     我们通过new关键字创建出一个类的实例对象， 这个对象和hibernate没有任何关系！
     *   2.持久态
     *     对象被session管理。就会产生一个OID（主键标识符）！这个对象和hibernate有关系！
     *   3.游离态（脱管态）
     *     曾经被session管理过！和瞬时态的区别在于，是否存在OID!
     *
     */


    @Test
    public void addTeacher(){
        Teacher teacher=new Teacher(4,"Teacher4");//瞬时状态（也叫临时状态，主键和数据库无关，一级缓存session中也没有此对象）
        session.save(teacher);//转为持久状态  （向项数据库执行insert语句，并且将此对象加载到一级缓存session中进行管理）
        session.evict(teacher);//转为游离态，从一级缓存session中清除， 不再被管理
        /*session.clear();//清除一级缓存中所有对象
        Teacher teacher1=(Teacher) session.load(Teacher.class,4);//持久状态，从数据库获得的对象会保存在一级缓存session中
        System.out.println("配置中关闭了懒加载，看看是否能执行到此");//经测试不执行。后面的语句一改不执行，因为抛异常了吗
        System.out.println(teacher1);//开启懒加载的话，会在此处才抛异常
        System.out.println("上面是否显示了Teacher的相关信息");*/
        /*
        因为没有teacher为游离态，所以缓存中没有id为4的对象，并且此时还没有commit，所以数据库中没有id为4的对象
        此时get()会抛出异常，那么之后的语句不再执行，就无法commit了，所以数据库中没有数据
        如果把上面的session.evict(teacher);注释掉，load()会在一级缓存中找到相应数据，后面语句会正常执行，即可以commit，数据库会添加此对象
        */
        Teacher teacher2=(Teacher) session.get(Teacher.class,4);//持久状态，从数据库获得的对象会保存在一级缓存session中
        System.out.println(teacher2);//如果用get，不会抛异常，返回一个null，但是仍然没有提交，因为缓存是空的
        transaction.commit();//commit就是将缓存中的脏对象提交到数据库，显然我们的缓存被清空了s

    }

    @Test
    public void addTeacher2(){
        Teacher  teacher=new Teacher(5,"老师5"); //瞬时态
        teacher.setName("changed");//瞬时态
        session.save(teacher);  //持久态开始 才被session管理
        System.out.println("*****************************");
        transaction.commit();//commit后只有一条insert语句
    }
    @Test
    public void addTeacher3(){
        Teacher  teacher=new Teacher(6,"老师6"); //瞬时态
        session.save(teacher);  //持久态开始 才被session管理
        teacher.setName("changed6");//瞬时态
        System.out.println("*****************************");
        transaction.commit();//commit后生成一条insert语句和一条update语句
    }

    @Test
    public void addTeacher4(){
        Teacher  teacher=new Teacher(7,"老师7"); //瞬时态
        session.save(teacher);  //持久态
        teacher.setName("老师5");
        teacher.setName("老师6");
        teacher.setName("老师7");
        System.out.println("*****************************");
        transaction.commit();//只有一条insert语句，因为最终的name还是"老师7"，不论修改多少次
    }
    @Test
    public void addTeacher5(){
        Teacher  teacher=new Teacher(8,"老师8"); //瞬时态
        session.save(teacher);  //持久态
        teacher.setName("老师5");
        teacher.setName("老师6");
        teacher.setName("老师老师");
        System.out.println("*****************************");
        transaction.commit();//一条insert,一条update
        // 不论改变多少次，以flush时的最终状态和save时创建的副本进行比较，执行一次或者不执行update
    }

    @Test
    public void addTeacher6(){
        Teacher  teacher= (Teacher) session.get(Teacher.class,1); //持久化状态
        teacher.setName("新教师1");//Teacher,变成了脏对象
        /**
         * 因为teacher已经是持久态  所以不需要save或者update
         */
        transaction.commit();
    }

    /**
     * saveOrUpdate（）
     * 会有3种情况：
     *  01.数据库中没有id为4的对象        select + insert
     *  02.数据库中有id为4的对象，但是name不同    select + update
     *  03.数据库中有id为4的对象，同时name相同    select
     */
    @Test
    public  void test06(){
        Teacher  teacher=new Teacher(4,"老师5"); //瞬时态
        session.saveOrUpdate(teacher); //持久态
        System.out.println("=============================================");
        transaction.commit();

    }

    /**
     *   saveOrUpdate（）：
     *     删除数据库中id为4的记录
     *     如果在savaOrUpdate只后更改了数据的属性！
     *     01.select
     *     02.insert
     *     03.update
     */
    @Test
    public  void test08(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        session.saveOrUpdate(teacher); //持久态
        teacher.setName("老师5");
        System.out.println("=============================================");
        transaction.commit();
    }

    /**
     *   merge
     *   验证一：
     *   数据库没有对应的数据
     *     01.select语句
     *     02.insert
     */
    @Test
    public  void test09(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        session.merge(teacher);
        transaction.commit();
    }

    /**
     *   merge
     *   验证二：
     *   数据库有对应的数据
     *       只有一条select语句
     */
    @Test
    public  void test10(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        session.merge(teacher);
        transaction.commit();
    }

    /**
     *   merge
     *   验证三：
     *   数据库有对应的数据，但是对数据不能完全对应
     *       只有一条select语句
     *       01.select
     *       02.update
     */
    @Test
    public  void test11(){
        Teacher  teacher=new Teacher(4,"老师5"); //瞬时态
        session.merge(teacher);
        transaction.commit();
    }

    /**
     *   merge
     *   验证四：
     *   数据库没有对应的数据，创建对象之后 对数据进行修改
     *       01.select
     *       02.insert
     *       发现并没有完成修改
     */
    @Test
    public  void test12(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        session.merge(teacher);
        teacher.setName("老师5");
        transaction.commit();//提交后数据库中的name字段显示为“老师4”
    }

    /**
     *   merge
     *   验证五：
     *   那是不是可以通过update方法实现更改
     *   然而发现并不能，而且报错了
     */
    @Test
    public  void test13(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        session.merge(teacher);  //不会改变对象的状态
        teacher.setName("老师5");  //瞬时态
        session.update(teacher); //报错
        teacher.setName("老");
        transaction.commit();
    }

    /**
     *   merge
     *   验证六：
     *   我就一定要用merge实现这样的更改     Ok，没问题。
     *   merge其实带有返回值，返回这个一级缓存中的对象
     *       01.select
     *       02.insert
     *       03.update
     *
     */
    @Test
    public  void test14(){
        Teacher  teacher=new Teacher(4,"老师4"); //瞬时态
        teacher=(Teacher)session.merge(teacher);
        teacher.setName("老师5");
        transaction.commit();//提交后数据库中的name字段显示为“老师4”
    }

    /**
     *
     * 瞬时态和游离态的区别就是是否拥有OID！
     * OID怎么来的？只要曾经被session管理过的对象都有OID！
     *
     *  save():   把瞬时态转换成持久态
     *  update(): 把游离态转换成持久态
     *  saveOrUpdate():
     *           会根据对象是否有OID来判断执行save还是update（前提是先执行一条select语句）
     *           如果有oid  执行update
     *           如果没有oid  执行save
     *  merge()： 产生的sql语句和saveOrUpdate有点类似，
     *            但是！！！！！
     *            01.merge不会改变对象的状态
     *                      如果一定要有，那么接受这个返回值
     *            02.当我们的对象处于瞬时状态时，会将对象复制一份到session的缓存中，
     *              然后执行save方法，执行insert
     */

    /**
     *  数据在数据库中不存在
     */
    @Test
    public   void  test15(){
        Teacher  teacher=new Teacher(4,"哈哈哈");//瞬时状态
        session.save(teacher);  //持久态
        session.evict(teacher);  // 游离态
        Teacher t=(Teacher) session.get(Teacher.class,4);
        System.out.println(t);
        //session.update(teacher);  // 持久态
        transaction.commit();
    }
    @Test
    public   void  test16(){
        Teacher  teacher=new Teacher(4,"哈哈哈");//瞬时状态
        session.save(teacher);  //持久态
        session.evict(teacher);  // 游离态
        Teacher t=(Teacher) session.get(Teacher.class,4);
        System.out.println(t);//输出仍为null
        session.update(teacher);  // 持久态
        transaction.commit();//sql语句顺序  select--->insert--->update
    }

    /**
     *  数据在数据库中存在！会把id  转换成 oid 让这个对象变成游离态
     */
    @Test
    public   void  test17(){
        Teacher  teacher=new Teacher(4,"哈哈哈");//游离态 数据库中有id为4的记录
        session.update(teacher); // 把游离态转换成持久化状态   update语句
        transaction.commit();
    }

    @Test
    public   void  test18(){
        Teacher  teacher=new Teacher(5,"哈哈哈55");//瞬时状态  数据库中没有id为5的记录
        session.save(teacher); // 把瞬时状态转换成持久化状态   insert语句
        transaction.commit();
    }


    /**
     *  数据在数据库中不存在！
     */
    @Test
    public   void  test19(){
        Teacher  teacher=new Teacher(4,"哈哈哈");//瞬时状态    瞬时态不能用update转化为持久状态
        session.update(teacher); // 把游离态转换成持久化状态     报错
        transaction.commit();
    }

    @Test
    public   void  test20(){
        Teacher  teacher=new Teacher(3,"嘿嘿嘿");//游离状态    数据库中有id为3的记录
        session.update(teacher); // 把游离态转换成持久化状态
        transaction.commit();
    }

    /**
     * commit 和flush的区别
     * 相同点：
     *     都会同步到数据库！
     * 不同点：
     *   commit:提交数据到数据库，会永久保存
     *   flush: 暂时保存，不一定会持久化！
     *
     *   commit在执行的时候，默认回执行flush操作，
     *   在执行flush的时候会清理缓存，
     *   清理缓存的时候执行脏检查！
     *
     *   脏检查：
     *      在我们的对象被session管理的时候，
     *      session会在缓存中创建对象的一个副本（快照）来保存对象现在的一种状态！
     *      在清理缓存的时候会拿现在的对象状态和之前的副本进行比较，
     *      如果现在的对象属性发生了变化，这个对象就是脏对象！
     *      flush会把脏对象同步到数据库。
     *      如果没有commit，数据只是暂时的保存在数据库中！
     *      之后commit才能永久保存！
     */
    @Test
    public void  test21(){
        //从数据库中获取id为1的老师信息
        Teacher teacher= (Teacher) session.get(Teacher.class,1); //持久态
        teacher.setName("小黑");  //如果删除这条语句 就不会出现update  证明没有脏对象
        System.out.println("**********************");
        session.flush();  //产生了update语句  证明同步到了数据库
    }

    @Test
    public void  test22(){
        //从数据库中获取id为1的老师信息
        Teacher teacher= (Teacher) session.get(Teacher.class,1); //持久态
        teacher.setName("小黑");  //如果删除这条语句 就不会出现update  证明没有脏对象
        System.out.println("**********************");
        session.flush();  //产生了update语句  证明同步到了数据库
        session.evict(teacher);  //清除指定对象 证明我们的数据不是来自缓存
        Teacher teacher2= (Teacher) session.get(Teacher.class,1); //持久态
        System.out.println(teacher2.getName());//显示为小黑。数据库中id为1的对象name原始数据并不是“小黑”
    }


}
