package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.SysPicture;
import com.shsxt.xmjf.server.base.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SysPictureMapper extends BaseMapper<SysPicture> {

    public List<Map<String,Object>> querySysPicturesByItemId(Integer itemId);
}