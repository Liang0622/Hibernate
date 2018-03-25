package com.vic;

import com.vic.pojo.Teacher;
import com.vic.util.SessionFactoryUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PageTest {
    Session session = null;
    Transaction transaction = null;

    @Before
    public void before() {
        session = SessionFactoryUtil.getCurrentSession();
        transaction = session.beginTransaction();
    }

    @Test
    public  void  test08(){
        //查询总记录数
        String  hql="select  count(*) from  Teacher";  // 会返回Long
        int counts= ((Long)session.createQuery(hql).uniqueResult()).intValue();
        //页大小
        int pageSize=3;
        //总页数
        int totalPages=(counts%pageSize==0)?(counts/pageSize):(counts/pageSize+1);
        // 显示第2页的内容
        int  pageIndex=2;
        hql="from Teacher";
        Query query= session.createQuery(hql);
        //设置从那一条数据开始查询
        query.setFirstResult((pageIndex-1)*pageSize);
        //设置页大小
        query.setMaxResults(pageSize);
        List<Teacher> teachers=query.list();
        for (Teacher t:teachers){
            System.out.println(t);
        }
    }
}
