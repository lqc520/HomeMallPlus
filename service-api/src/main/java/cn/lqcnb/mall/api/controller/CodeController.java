package cn.lqcnb.mall.api.controller;


import cn.lqcnb.mall.common.entity.R;
import cn.lqcnb.mall.common.utils.CodeUtils;
import cn.lqcnb.mall.common.utils.IpUtils;
import cn.lqcnb.mall.common.utils.redisUtils.RedisSMSUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * @author lqc520
 * @Description: 验证码
 * @date 2020/3/5 20:37
 */
@Controller
@CrossOrigin
@RequestMapping("api/code")
public class CodeController {
    @Autowired(required = false)
    private Producer captchaProducer = null;
    @Autowired
    HttpSession session;
    @Resource
    HttpServletResponse response;
    @Autowired
    CodeUtils codeUtils;
    @Autowired
    RedisSMSUtil redisSMSUtil;
    @Autowired
    IpUtils ipUtils;
    @Autowired
    HttpServletRequest request;

    Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/kaptcha")
    public void getKaptchaImage() throws Exception {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
//        session.setAttribute("capText", capText);

//        redis存取验证码
        redisSMSUtil.setIpCode(ipUtils.getIpAddr(),capText,60*15);
        logger.info("ip为: "+ipUtils.getIpAddr()+" 生成验证码："+capText);


        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @GetMapping("cheCode/{code}")
    @ResponseBody
    public R cheCode(@PathVariable String code){
        logger.info("ip:"+ipUtils.getIpAddr()+"  code:"+code);
        String key = redisSMSUtil.getIpCode(ipUtils.getIpAddr());
        logger.info("key:"+key);
        if(StringUtils.isEmpty(key)||StringUtils.isEmpty(code)){
            return R.error("验证码为空");
        } else if (key.equals(code.trim())) {
            return R.ok("验证码正确");
        }else{
            return R.error("验证码错误");
        }
//        if(codeUtils.checkVerifyCode(code.trim())){
//            return R.ok("验证码正确");
//        }
//        return R.error("验证码错误");
    }
}