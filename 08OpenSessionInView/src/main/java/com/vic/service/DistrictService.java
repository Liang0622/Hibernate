package com.vic.service;

import com.vic.pojo.District;

import java.io.Serializable;

public interface DistrictService {
    //根据id查询出指定的区县
    District getDistrictById(Serializable id);
}
