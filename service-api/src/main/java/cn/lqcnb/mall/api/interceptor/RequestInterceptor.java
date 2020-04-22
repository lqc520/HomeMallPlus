package cn.lqcnb.mall.api.interceptor;

import cn.lqcnb.mall.api.entity.VisitReqDTO;
import cn.lqcnb.mall.api.service.SiteVisitFacade;
import cn.lqcnb.mall.api.vo.BrowserVO;
import cn.lqcnb.mall.api.vo.SiteVisitDTO;
import cn.lqcnb.mall.common.utils.IpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author lqc520
 * @Description: 请求拦截器 获取访问者信息
 * @date 2020/3/10 13:58
 */
@Async
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private SiteVisitFacade siteVisitFacade;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IpUtils ipUtils;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Async
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ipUtils == null) {
            System.out.println("ipUtils is null!!!");
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            ipUtils = (IpUtils) factory.getBean("ipUtils");
        }
        VisitReqDTO reqDTO = new VisitReqDTO();
        reqDTO.setIp(ipUtils.getIpAddr());
        reqDTO.setApp("家居商城");
        reqDTO.setUri(ipUtils.getUrI());
        //解决service为null无法注入问题
        if (siteVisitFacade == null) {
            System.out.println("siteVisitFacade is null!!!");
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            siteVisitFacade = (SiteVisitFacade) factory.getBean("siteVisitFacade");
        }
        SiteVisitDTO visit = siteVisitFacade.visit(reqDTO);
//        logger.info(visit.toString());


        String browser = getBrowser(request);
        logger.debug("browser:"+getBrowser(request));


        if (stringRedisTemplate == null) {
            System.out.println("stringRedisTemplate is null!!!");
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            stringRedisTemplate = (StringRedisTemplate) factory.getBean("stringRedisTemplate");
        }
        if(!StringUtils.isEmpty(browser)){

            if( stringRedisTemplate.opsForHash().hasKey("browser",browser)){
                Object o = stringRedisTemplate.opsForHash().get("browser", browser);
                BrowserVO browserVO = JSON.parseObject(o.toString(), new TypeReference<BrowserVO>() {
                });
                browserVO.setValue(browserVO.getValue()+1);
               stringRedisTemplate.opsForHash().put("browser",browser, JSON.toJSONString(browserVO));
//             stringRedisTemplate.opsForHash().increment("browser",browser,1);
            }else{
                BrowserVO browserVO = new BrowserVO();
                browserVO.setName(browser);
                browserVO.setValue(1);
                stringRedisTemplate.opsForHash().put("browser",browser, JSON.toJSONString(browserVO));
            }

        }


        return true;

    }


    //    获取浏览器信息
    private String getBrowser(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent") ;
        String user = userAgent.toLowerCase();
        String browser="";
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  ||
                (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) ||
                (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return browser.split("-")[0];
    }

}
