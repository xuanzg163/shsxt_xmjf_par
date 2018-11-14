package com.shsxt.xmjf.server.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import com.shsxt.xmjf.server.db.dao.BasItemMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 * @author zhangxuan
 * @date 2018/11/14
 * @time 18:52
 */
@Service
public class BasItemServiceImpl implements IBasItemService {

    @Resource
    private BasItemMapper basItemMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;

    @Override
    public PageInfo<Map<String, Object>> queryItemsByParams(BasItemQuery basItemQuery) {
        Page<Map<String,Object>> vals = null;
        String key = "itemList::pageNum::" + basItemQuery.getPageNum()
                + "::pageSize::" + basItemQuery.getPageSize()
                + "::itemCycle::" + basItemQuery.getItemCycle()
                + "::itemType::" + basItemQuery.getItemType()
                + "::isHistory::" + basItemQuery.getIsHistory();
         vals = (Page<Map<String, Object>>) valueOperations.get(key);
         if (null == vals){
             PageHelper.startPage(basItemQuery.getPageNum(),basItemQuery.getPageSize());
             vals = (Page<Map<String, Object>>) basItemMapper.queryItemsByParams(basItemQuery);
             if (!CollectionUtils.isEmpty(vals)){
                 valueOperations.set(key,vals);
             }
         }

        return new PageInfo<Map<String, Object>>(vals);
    }
}
