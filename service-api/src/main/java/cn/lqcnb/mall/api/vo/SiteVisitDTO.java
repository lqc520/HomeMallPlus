package cn.lqcnb.mall.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteVisitDTO {

    /**
     * 站点访问统计
     */
    private VisitVO siteVO;

    /**
     * 页面访问统计
     */
    private VisitVO uriVO;

}
