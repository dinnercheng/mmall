package com.mmall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    ShippingMapper shippingMapper;
    public ServerResponse add(Integer userId, Shipping shipping){

        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map map = Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("Create Address Success",map);
        }

        return  ServerResponse.createByErrorMessage("Create Address Failed");
    }

    public ServerResponse<String> del(Integer userId,Integer shippingId){

        int resultCount = shippingMapper.deleteShippingIdByUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("Delete Address Success");
        }
        return  ServerResponse.createByErrorMessage("Delete Address Failed");
    }

    public ServerResponse update(Integer userId, Shipping shipping){

        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("Update Address Success");
        }

        return  ServerResponse.createByErrorMessage("Update Address Failed");
    }

    public ServerResponse<Shipping> select(Integer userId,Integer shippingId){

        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("Not find shipping");
        }
        return  ServerResponse.createBySuccess(shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return  ServerResponse.createBySuccess(pageInfo);
    }



}
