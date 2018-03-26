package com.vic.service;

import com.vic.dao.DistrictDao;
import com.vic.dao.DistrictDaoImpl;
import com.vic.pojo.District;
import com.vic.util.SessionFactoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.io.Serializable;

public class DistrictServiceImpl implements DistrictService {
    public District getDistrictById(Serializable id) {
        DistrictDao dao=new DistrictDaoImpl();
        //使用OpenSessionInView来控制事务，这里不能再有事务控制的代码了
        /*Transaction transaction= null;
        District district=null;
        try {
            transaction= SessionFactoryUtil.getCurrentSession().beginTransaction();
            district=dao.getDistrictById(id);
            transaction.commit();
        }catch (HibernateException e){
            e.printStackTrace();
            if(transaction!=null){
                transaction.rollback();
            }
        }
        return district;*/
        return dao.getDistrictById(id);
    }
}
