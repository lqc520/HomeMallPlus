package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.Member;
import cn.lqcnb.mall.api.mapper.MemberMapper;
import cn.lqcnb.mall.common.service.AbstractService;
import cn.lqcnb.mall.common.utils.EmailUtils;
import cn.lqcnb.mall.common.utils.redisUtils.RedisSMSUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class MemberService extends AbstractService<Member> {
    @Autowired
    private MemberMapper memberMapper;

    @Value("${cn.lqcnb.mall.base-url}")
    private String baseUrl;

    @Autowired
    private RedisSMSUtil redisSMSUtil;

    @Autowired
    private EmailUtils emailUtils;

    @Resource
    private HttpServletResponse response;


    public void updateByEmail(String code, String emailCode, String email) throws IOException {
        if(emailCode.equals(code)) {
            Example example =new Example(Member.class);
            example.createCriteria().andEqualTo("email",email);
            Member member = new Member();
            member.setIsActivate(0);
            member.setIsActivate(0);
//            return memberMapper.updateByExampleSelective(member,example)>0;
        }
        response.sendRedirect(baseUrl);
    }

    /**
     * 发送邮件
     * @param email 邮箱地址
     */
    public void sentEmail(String email){
        String code = UUID.randomUUID().toString().replace("-", "");
        redisSMSUtil.setEmailCodeTime(email,code,60*60);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>")
                .append("<body style='text-align:center'>")
                .append("<img src='https://lqcblog.oss-cn-shenzhen.aliyuncs.com/mall/favicon.png'>")
                .append("<div>")
                .append("<p>你好</p>")
                .append("<p>感谢你成为<span style='font-size: 24px'> 家居商城 </span> 会员</p>")
                .append("<p>你的邮箱为：<span style='color: #3FDD86;font-size: 24px'>"+email+"</span> 请点击下面的链接激活</p>")
                .append("<p>如果以上链接无法点击，请将下面的地址复制到你的浏览器地址。</p>")
                .append("<p style='color:#3FDD86;font-size: 24px'>" + baseUrl + "api/member/state/" + code + "/" + email+"</p>")
                .append("<p style='color:lightseagreen;font-size: 30px'>如果你没有激活邮箱 请忽略此条邮件。</p>")
                .append("<div>")
                .append("</body>")
                .append("</html>");

        emailUtils.sendHtmlMail(email,"家居商城",sb.toString());
//            老方法
//            String content = "<html><head></head><body><h1>这是一封激活邮件 有效期,激活请点击以下链接</h1><h3><a href='"+baseUrl+"api/member/state/"
//                    + code +"/"+member.getEmail()+ "'>"+ baseUrl +"api/member/state/" + code+"/"+member.getEmail()
//                    + "</href></h3></body></html>";
//            new Thread(new MailUtils(member.getEmail(),content)).start();
//            href='"+baseUrl+"api/member/state/" + code +"/"+member.getEmail()+"'
    }

    /**
     * 效验是否已经绑定邮箱
     * @param email 邮箱
     * @return boolean
     */
    public boolean cheEmail(String email){
        Member member = new Member();
        member.setEmail(email);
        return findList(member).size()<=1;
    }

    /**
     * 获取分页多条件查询数据
     * @param pageNum 当前页
     * @param pageSize 大小
     * @param member 查询条件
     * @return List<Member>
     */
    public List<Member> getPageList(Integer pageNum, Integer pageSize,Member member){
        if(pageNum==null||pageNum<0){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(memberMapper.getList(member)).getList();
    }

    /**
     * 获取用户增量
     * @param data 时间
     * @return Map
     */
    public List<Map> getIncrement(Integer data,Integer limit){
        return memberMapper.getIncrement(data,limit);
    }

}
