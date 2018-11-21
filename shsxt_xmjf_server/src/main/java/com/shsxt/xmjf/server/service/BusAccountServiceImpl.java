package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.server.db.dao.BusAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/15
 * @time 23:16
 */

@Service
public class BusAccountServiceImpl implements IAccountService {

    @Resource
    private BusAccountMapper busAccountMapper;

    /**
     * 查用用户账户记录
     * 剩余金额，可用金额
     * @param userId
     * @return
     */
    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
        return busAccountMapper.queryBusAccountByUserId(userId);
    }

    /**
     * 查询账户资产信息
     * @param userId 用户id
     * @return
     */
    @Override
    public Map<String, Object> countBusAccountInfoByUserId(Integer userId) {
        // List<Map<String,Object>>  总金额:23421321
        Map<String,Object> result=new HashMap<String,Object>();
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> accountMap= busAccountMapper.countBusAccountInfoByUserId(userId);

        /**
         * 数据格式化，转换成JS识别数据
         */
        for(Map.Entry<String,Object> entry:accountMap.entrySet()){
            if(!(entry.getKey().equals("总资产"))){
                Map<String,Object> temp=new HashMap<String,Object>();
                temp.put("name",entry.getKey());
                temp.put("y",entry.getValue());
                list.add(temp);
            }else{
                result.put("data1",entry.getValue());
            }
        }
        result.put("data2",list);
        return result;
    }
}
