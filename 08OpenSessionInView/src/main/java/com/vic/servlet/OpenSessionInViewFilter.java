package com.vic.servlet;

import com.vic.util.SessionFactoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.LogRecord;

@WebFilter("/*")
public class OpenSessionInViewFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public boolean isLoggable(LogRecord record) {
        return false;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Session session= SessionFactoryUtil.getCurrentSession();  //获取session
        Transaction transaction= null;

        try{
            transaction=session.beginTransaction();  //开启事务
            System.out.println("过滤器中的======》"+session.hashCode());
            filterChain.doFilter(servletRequest,servletResponse);
            transaction.commit();
        }catch (HibernateException e){
            e.printStackTrace();
            if(transaction!=null){
                transaction.rollback();
            }
        }

    }

    public void destroy() {

    }
}
