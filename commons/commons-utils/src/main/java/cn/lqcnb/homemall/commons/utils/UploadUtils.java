package cn.lqcnb.homemall.commons.utils;

import com.aliyun.oss.OSSClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lqc520
 * @Description: 上传工具类
 * @date 2020/3/15 22:00
 */
public class UploadUtils {

    /**
     * 上传到阿里云
     * @param file 文件
     * @param Catalog 目录
     * @return 地址
     */
    public static String upLoadOSS(MultipartFile file, String Catalog) {
        try {
            // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
            // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
            String accessKeyId = "xxx";
            String accessKeySecret = "xxxxx";
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            String uploadFilename = file.getOriginalFilename();
            // 生成新的文件名
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String newFilename =Catalog+"/"+uuid + uploadFilename.substring(uploadFilename.lastIndexOf("."));
            ossClient.putObject("lqcblog", newFilename, file.getInputStream());
            // 关闭OSSClient。
            ossClient.shutdown();

            return "https://lqcblog.oss-cn-shenzhen.aliyuncs.com/"+ newFilename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传到本地
     * @param file 文件
     * @return 地址
     */
    public static String uploadLocal(MultipartFile file){
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize()/1024/1024;
        System.out.println(fileName + "-->" + size+"Mb");

        String path = "E:/Study/Junior/JavaEE/idea_Program/mall/src/main/resources/static/common/img" ;
        File dest = new File(path + "/" + fileName);
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
        try {
            //保存文件
            file.transferTo(dest);
            return "/common/img/"+fileName;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
