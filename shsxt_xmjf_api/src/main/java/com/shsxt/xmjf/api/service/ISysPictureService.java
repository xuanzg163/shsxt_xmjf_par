package com.shsxt.xmjf.api.service;

import java.util.List;
import java.util.Map;

/**
 * 图片查询接口
 */
public interface ISysPictureService {
    public List<Map<String,Object>> querySysPicturesByItemId(Integer itemId);
}
