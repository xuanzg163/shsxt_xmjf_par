package com.shsxt.xmjf.server.service.db;

import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.server.db.dao.BasItemMapper;
import com.shsxt.xmjf.server.service.TestBase;
import com.shsxt.xmjf.server.service.TestBase02;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 测试投资查询接口
 * @author zhangxuan
 * @date 2018/11/13
 * @time 20:56
 */

public class TestBasItemQuery extends TestBase02 {

    @Resource
    private BasItemMapper basItemMapper;

    @Test
    public void test(){
        BasItemQuery basItemQuery = new BasItemQuery();
        basItemQuery.setIsHistory(1);
        basItemQuery.setItemCycle(2);
        basItemQuery.setItemType(3);

        List<Map<String, Object>> vals = basItemMapper.queryItemsByParams(basItemQuery);
        if (!CollectionUtils.isEmpty(vals)){
            for (Map<String,Object> map:
                vals ) {
                for (Map.Entry<String,Object> entry:map.entrySet()
                     ) {
                    System.out.println(entry.getKey() + "-------" + entry.getValue());
                }
                System.out.println("=========");
            }
        }
    }
}
