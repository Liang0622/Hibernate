package com.vic.dao;

import com.vic.pojo.District;
import com.vic.util.SessionFactoryUtil;
import org.hibernate.Session;

import java.io.Serializable;

public class DistrictDaoImpl implements DistrictDao {
    public District getDistrictById(Serializable id) {
        Session session= SessionFactoryUtil.getCurrentSession();
        District district= (District) session.load(District.class,id);
        System.out.println("daoImpl中的=======》"+session.hashCode());
        //System.out.println(district);
        return district;
    }
}
