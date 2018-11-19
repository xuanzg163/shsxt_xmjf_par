package com.shsxt.xmjf.api.enums;

/**
 * 充值类型
 */
public enum RechaggeType {
    APP(1),
    ADMIN(2),
    PC(3),
    WX(4);
    private Integer type;

    RechaggeType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
