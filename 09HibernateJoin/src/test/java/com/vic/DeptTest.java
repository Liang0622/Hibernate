package com.vic;

import com.vic.pojo.Dept;
import com.vic.pojo.Emp;
import com.vic.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DeptTest {
    Session session=null;
    Transaction transaction=null;

    @Before
    public  void  before(){
        session= SessionFactoryUtil.getCurrentSession();
        transaction= session.beginTransaction();
    }

    @Test
    public   void addTest(){
        /**
         * 新增部门的同时 新增员工
         * 因为我们设置了cascade=all
         */
        Dept dept=new Dept();
        dept.setDeptId(1);
        dept.setName("财务部");
        dept.setLocation("1楼");
        //创建部门对应的员工
        Emp emp1=new Emp(1,"员工1","财务猿1",10000.0,dept);
        Emp emp2=new Emp(2,"员工2","财务猿2",5000.0,dept);
        Emp emp3=new Emp(3,"员工3","财务猿3",6000.0,dept);
        //将员工放进集合中
        dept.getEmps().add(emp1);
        dept.getEmps().add(emp2);
        dept.getEmps().add(emp3);
        session.save(dept);
        transaction.commit(); //提交事务

    }

    /**
     * 使用普通内连接查询  部门和员工的信息
     *
     连接查询
     1.内连接 ：查询两张表中的，相同的数据
     01.隐式内连接
     select  studentName，gradeName from  student,grade
     where  student.gradeId=grade.gradeId
     02.显式内连接
     select  studentName，gradeName from  student
     inner join grade  on  student.gradeId=grade.gradeId
     */
    @Test
    public  void  innerJoin1(){
        String hql="from Emp e inner join e.dept";  //e.dept就是Emp类中的域属性
        List<Object[]> list= session.createQuery(hql).list();
        for (Object[]  o:list){
            System.out.println(o[0]);  //Emp对象
            System.out.println(o[1]);  //Dept对象
        }
    }

    /**
     * 迫切内连接
     * 直接把Dept封装到Emp的dept属性中
     */
    @Test
    public  void  innerJoinFetch(){
        String hql="from Emp e inner join fetch e.dept";  //e.dept就是Emp类中的域属性
        List<Emp> list= session.createQuery(hql).list();
        for (Emp  o:list){
            System.out.println(o);  //Emp对象
        }
    }
    /**
     * 使用左外连接
     * 以左表为准，右表中无值用null填充
     */
    @Test
    public  void  leftJoin1(){
        String hql="from Emp e left join  e.dept";  //e.dept就是Emp类中的域属性
        List<Object[]> list= session.createQuery(hql).list();
        for (Object[]  o:list){
            System.out.println(o[0]);//Emp对象
            System.out.println(o[1]);//Dept对象
        }
    }

    /**
     * 使用迫切左外连接
     * 没有迫切右外连接，因为返回的对象总是左表，
     * 左表用null填充，null又没有dept属性的，右表没有位置可放
     */
    @Test
    public  void  leftJoinFetch(){
        String hql="from Emp e left join fetch e.dept";  //e.dept就是Emp类中的域属性
        List<Emp> list= session.createQuery(hql).list();
        for (Emp  o:list){
            System.out.println(o);//Emp对象
        }
    }




}
