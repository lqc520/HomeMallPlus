package cn.lqcnb.mall.api.controller;

import cn.lqcnb.mall.api.annotation.PassToken;
import cn.lqcnb.mall.api.annotation.UserLoginToken;
import cn.lqcnb.mall.api.entity.Card;
import cn.lqcnb.mall.api.entity.Member;
import cn.lqcnb.mall.api.service.CardService;
import cn.lqcnb.mall.api.service.MemberService;
import cn.lqcnb.mall.common.entity.LayUI;
import cn.lqcnb.mall.common.entity.R;
import cn.lqcnb.mall.common.utils.EmailUtils;
import cn.lqcnb.mall.common.utils.TencentSMSUtils.SMSUtils;
import cn.lqcnb.mall.common.utils.TokenUtil;
import cn.lqcnb.mall.common.utils.UploadUtil;
import cn.lqcnb.mall.common.utils.redisUtils.RedisSMSUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * Author: Lin QiCheng
 * Date: 2019/8/25 11:34
 * To change this template use File | Settings | File Templates.
 * Description: 用户管理
 * Modify by:
 */

@RestController
@Api(tags = "用户管理")
@CrossOrigin
@RequestMapping("api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisSMSUtil redisSMSUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private CardService cardService;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${cn.lqcnb.mall.base-url}")
    private String baseUrl;

    Logger logger = LoggerFactory.getLogger(getClass());


    //后期把业务逻辑放到service
    @PassToken
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true),
            @ApiImplicitParam(name = "code" ,value = "验证码",paramType="query",required = true)
    })
    @PostMapping("login")
    public R login(String mobile, String code, @CookieValue(required = false) String cards,@ApiParam(hidden =true) HttpServletResponse response){
        String SMSCode = redisSMSUtil.getCode(mobile);
        if(code.equals(SMSCode)){
            //登录效验
            Member param = new Member();
            param.setMobile(mobile);
            Member user = memberService.findOne(param);
            //登录成功
            if(user != null){
                //前台购物车有商品
                if(!StringUtils.isEmpty(cards)){
                    //解析数据 获取商品id和数量
                    List<Card> maps = JSONArray.parseObject(cards, new TypeReference<List<Card>>() {
                    });
                    String ids="",nums="";
                    System.out.println(maps);
                    for (Card m : maps){
                        //goodsId
                        ids += m.getGoodsId()+",";
                        //number
                        nums += m.getNumber()+",";
                    }
                    logger.info("用户未登录时的商品数据","商品id:"+ids,"对应数量",nums);

                    // 从数据库查询商品信息 goods表的 （通过id获取商品数据）
                    List<Card> cardsList = cardService.getCards(ids.split(","));
                    logger.info("商品信息",cardsList.toString());
                    int i=0;
                    String[] num = nums.split(",");
                    //修改商品的数量

                    for (Card card : cardsList){
                        //商品数量 检测库存
                        card.setNumber(String.valueOf(Integer.parseInt(num[i]) < card.getStock() ? Integer.parseInt(num[i]) : card.getStock()));
                        card.setMemberId(user.getId());

                        //查询前台购物车商品是否在数据库里面 购物车有这个id???
                        Card cheGoods= new Card();
                        cheGoods.setGoodsId(card.getGoodsId());
                        Card checkGood = cardService.findOne(cheGoods);

                        //在数据查询到商品  效验库存  数量相加
                        if(checkGood != null){
                            Card upt = new Card();
                            upt.setId(checkGood.getId());
//                              upt.setNumber(String.valueOf(Integer.parseInt(num[i])+Integer.parseInt(checkGood.getNumber()) <card.getStock()? Integer.parseInt(num[i])+Integer.parseInt(checkGood.getNumber()):card.getStock()));
                            upt.setNumber(String.valueOf(Integer.parseInt(checkGood.getNumber()) + Integer.parseInt(card.getNumber()) < card.getStock() ? Integer.parseInt(checkGood.getNumber()) + Integer.parseInt(card.getNumber()) : card.getStock()));
                            cardService.update(upt);


                        }else{ //没有添加
                            //数据库环节 后期改redis
                            card.setStock(null);
                            cardService.add(card);
                        }
                        i++;
                    }
                    logger.info("合并后商品信息",cardsList.toString());
                    //更新前台cookie购物车 为当前数据库最新数据
                    Card params =new Card();
                    params.setMemberId(user.getId());
                    List<Card> updateCookie = cardService.findList(params);

                    logger.info("更新cookie",JSONArray.toJSONString(updateCookie));
                    try {
                        String encode = URLEncoder.encode(JSONArray.toJSONString(updateCookie), "UTF-8");
                        Cookie newCard = new Cookie("cards", encode);
                        newCard.setPath("/");
                        newCard.setMaxAge(60 * 60 * 24 * 7);
                        response.addCookie(newCard);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
//                session.setAttribute("user", JSON.toJSON(user));
                //测试放回数据 上线关闭
                String token= TokenUtil.getToken(user);
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("user",user);
                jsonUser.put("token",token);
                //添加cookie
                Cookie tokenCookie = new Cookie("token",token);
                tokenCookie.setPath("/");
                //tokenCookie.setMaxAge(7 * 24 * 60 * 60);
                //tokenCookie.setDomain("mall.lqcnb.cn");
                response.addCookie(tokenCookie);
                return R.ok("登录成功",jsonUser);
            }else{
                return R.error("请先注册");
            }

        }else{
            return R.error("验证码错误");
        }
    }


    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true),
            @ApiImplicitParam(name = "code" ,value = "验证码",paramType="query",required = true)
    })
    @PostMapping("register")
    public R register(String mobile,String code){

        String SMSCode = redisSMSUtil.getCode(mobile);
        if(code.equals(SMSCode)){
            Member param = new Member();
            param.setMobile(mobile);
            boolean update = memberService.add(param);
            logger.info("用户："+mobile+"注册成功",update);
            return R.ok("注册成功",update);
        }else{
            return R.error("验证码错误");
        }

    }


    @ApiOperation(value = "检测手机号是否注册")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="path")
    @GetMapping("checkMobile/{mobile}")
    public R checkMobile(@PathVariable String mobile){
        Member param = new Member();
        param.setMobile(mobile);
        Member one = memberService.findOne(param);
        if(one==null){
            return R.error("您的手机号未注册 将进行注册");
        }else{
            return R.ok();
        }
    }


    @ApiOperation(value = "获取腾讯云短信注册验证码")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="path",required = true)
    @GetMapping("getRegisterCode/{mobile}")
    public R getRegisterCode(@PathVariable String mobile){
        String code = SMSUtils.getRegisterCode(mobile);
        if (code!=null){
            redisSMSUtil.setCodeTime(mobile,code,300);
            return R.ok("验证码已发送 5分钟内有效");
        }
        return R.error("获取短信验证码错误");
    }


    @ApiOperation(value = "获取腾讯云短信登录验证码")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="path",required = true)
    @GetMapping("getLoginCode/{mobile}")
    public R getLoginCode(@PathVariable String mobile){
        String code = SMSUtils.getLoginCode(mobile);
        if (code!=null){
            redisSMSUtil.setCodeTime(mobile,code,300);
            return R.ok("验证码已发送 5分钟内有效");
        }
        return R.error("获取短信验证码错误");
    }


    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file){
        if(file.isEmpty()){
            return R.error();
        }
        //阿里云 linux云端部署上传到阿里云oss
        String AliPath = UploadUtil.upload(file,"mall");
        //win10 本地部署 上传到本地资源
//        String winPath = UploadLocalUtil.addImg(file);
        return R.ok("ok", AliPath);
    }


    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(name = "member" ,value = "用户信息",paramType="query",required = true)
    @PatchMapping("/update")
    public R update(Member member){
        Member cur = memberService.getById(member.getId());
        logger.info("效验用户是否激活过邮箱:"+cur.getIsActivate());

//      邮箱激活
//        if(!member.getEmail().isEmpty()){
//            if(memberService.cheEmail(member.getEmail())){
//                if(cur.getIsActivate()==1 ||! cur.getEmail().equals(member.getEmail())){
//                    memberService.sentEmail(member.getEmail());
//                }
//            }else{
//                return R.error("邮箱已被绑定 请换个邮箱");
//            }
//
//        }


        if(memberService.update(member)){
            return R.ok("修改成功");
        }
        return R.error();
    }


//    @ApiOperation(value = "激活邮箱")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code" ,value = "激活码",paramType="path",required = true),
//            @ApiImplicitParam(name = "email" ,value = "邮箱",paramType="path",required = true)
//    })
//    @GetMapping("/state/{code}/{email}")
//    public String update(@PathVariable String code, @PathVariable String email){
//
//        String emailCode = redisSMSUtil.getEmailCode(email);
//        logger.info("email："+email);
//        logger.info("code："+ code);
//        logger.info("emailCode："+emailCode);
//        if(emailCode.equals(code)){
//            memberService.updateByEmail(email);
//            return "激活成功";
//        }
//        return "激活失败";
//    }
        @ApiOperation(value = "激活邮箱")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "code" ,value = "激活码",paramType="path",required = true),
                @ApiImplicitParam(name = "email" ,value = "邮箱",paramType="path",required = true)
        })
        @GetMapping("/state/{code}/{email}")
        public void updateByEmail(@PathVariable String code, @PathVariable String email){
            String emailCode = redisSMSUtil.getEmailCode(email);
            logger.info("email："+email,"code："+ code,"emailCode："+emailCode);
            if(emailCode.equals(code)){
                try {
                    memberService.updateByEmail(code,emailCode,email);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }


    @UserLoginToken
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(name = "token" ,value = "令牌",paramType="header",required = true)
    @GetMapping("/get")
    public R get(@RequestHeader String token){
        Member user = memberService.getById(Integer.parseInt(TokenUtil.getUserId(token)));
        if(user!=null){
            return R.ok(user);
        }
        return R.error();
    }


    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public LayUI list(){
        List<Member> userList = memberService.findAll();
        if(userList.size()!=0){
            return LayUI.ok(String.valueOf(userList.size()),userList);
        }
        return LayUI.error();
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/getPageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页",paramType = "query"),
            @ApiImplicitParam(name = "limit",value = "分页条数",paramType = "query"),
            @ApiImplicitParam(name = "member",value = "查询条件",paramType = "query")
    })
    public LayUI getPageList(Integer page, Integer limit,Member member){
        List<Member> userList = memberService.getPageList(page,limit,member);
        if(userList.size()!=0){
            return LayUI.ok(String.valueOf(userList.size()),userList);
        }
        return LayUI.error();
    }


    @ApiOperation(value = "添加用户")
    @ApiImplicitParam(name = "member" ,value = "用户",paramType="query",required = true)
    @GetMapping("/add")
    public R add(Member member){
         if(memberService.add(member)){
             return R.ok();
         }
         return R.error();
    }

    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(name = "id" ,value = "id",paramType="path",required = true)
    @DeleteMapping("/delete/{id}")
    public R add(@PathVariable Integer id){
        if(memberService.deleteById(id)){
            return R.ok();
        }
        return R.error();
    }



    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

    @GetMapping("/getIncrement")
    @ApiOperation(value = "获取用户增量")
    public Map getIncrement(){
//        LocalDate now = LocalDate.now();
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd");
//        String data = df.format(now);
//        logger.info(data);
        List date = new LinkedList();
        List count = new LinkedList();
        List<Map> increment = memberService.getIncrement(360,10);
        for(Map m : increment){
            date.add(m.get("日期"));
            count.add(m.get("人数"));
        }
        Map map = new HashMap();
        map.put("memberDate",date);
        map.put("memberCount",count);
        List<Object> browserValue = stringRedisTemplate.opsForHash().values("browser");
        Set<Object> browserName = stringRedisTemplate.opsForHash().keys("browser");
//        Object browserJson = JSONArray.toJSON(browser);
        logger.info(browserValue.toString());
        map.put("browserValue",browserValue);
//        map.put("browserValue",browserValue);
        map.put("browserName",browserName);

        return map;
    }

}
