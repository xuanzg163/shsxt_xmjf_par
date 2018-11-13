package com.shsxt.xmjf.api.querys;

import com.shsxt.xmjf.api.constants.XmjfConstant;

import java.io.Serializable;

/**
 * @author zhangxuan
 * @date 2018/11/13
 * @time 20:41
 */
public class BasItemQuery implements Serializable {
    private static final long serialVersionUID = -7826088859401808602L;
    // 项目期限 null-所有 1-0~30  2-30~90  3-90~
    private Integer itemCycle;
    // 项目类型
    private Integer itemType;
    // 0-可投项目 1-历史项目
    private Integer isHistory;
    private Integer pageNum = XmjfConstant.PAGE_NUM;
    private Integer pageSize = XmjfConstant.PAGE_SIZE;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getItemCycle() {
        return itemCycle;
    }

    public void setItemCycle(Integer itemCycle) {
        this.itemCycle = itemCycle;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
