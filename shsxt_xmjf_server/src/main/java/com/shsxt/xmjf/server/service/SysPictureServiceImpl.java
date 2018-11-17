package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.service.ISysPictureService;
import com.shsxt.xmjf.server.db.dao.SysPictureMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 11:20
 */

@Service
public class SysPictureServiceImpl implements ISysPictureService {

    @Resource
    private SysPictureMapper sysPictureMapper;
    /**
     * 查询商品图片url
     * @param itemId
     * @return
     */
    @Override
    public List<Map<String, Object>> querySysPicturesByItemId(Integer itemId) {
        return sysPictureMapper.querySysPicturesByItemId(itemId);
    }
}
