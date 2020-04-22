package cn.lqcnb.mall.api.entity;

import lombok.Data;


@Data
public class VisitReqDTO {
    /**
     * 应用区分
     */
    private String app;

    /**
     * 访问者ip
     */
    private String ip;

    /**
     * 访问的URI
     */
    private String uri;
}
