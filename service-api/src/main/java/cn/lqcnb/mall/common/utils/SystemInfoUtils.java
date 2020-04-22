package cn.lqcnb.mall.common.utils;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

/**
 * @author lqc520
 * @Description: 获取系统信息
 * @date 2020/3/9 19:51
 */
@Component
public class SystemInfoUtils {



    String baseUrl = "http://lqcnb.cn:8888/system?action=";
    private OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    Logger logger = LoggerFactory.getLogger(getClass());


    //https://www.bt.cn/api-doc.pdf
    public enum Data{
//    system CentOS Linux 7.5.1804 (Core) 操作系统信息
//    version 6.8.2 面板版本
//    time 0 天 23 小时 45 分钟 上次开机到现在经过的时间
//    cpuNum 2 CPU 核心数
//    cpuRealUsed 2.01 CPU 使用率 (百分比)
//    memTotal 1024 物理内存容量 (MB)
//    memRealUsed 300 已使用的物理内存 (MB)
//    memFree 724 可用的物理内存 (MB)
//    memCached 700 缓存化的内存 (MB)
//    memBuffers 100 系统缓冲 (MB)
        GetSystemTotal,
//字段 字段值示例 说明
//[].path / 分区挂载点
//[].inodes ["8675328", "148216", "8527112", "2%"] 分区 Inode 使用信息 [总量,已使用,可用,使用率]
//[].size ["8.3G", "4.0G", "4.3G", "49%"] 分区容量使用信息 [总量,已使用,可用,使用率]
        GetDiskInfo,
//        字段 字段值示例 说明
//downTotal 446326699 总接收 (字节数)
//upTotal 77630707 总发送 (字节数)
//downPackets 1519428 总收包 (个)
//upPackets 175326 总发包 (个)
//down 36.22 下行流量 (KB)
//up 72.81 上行流量 (KB)
//cpu [1.87, 6] CPU 实时信息 [使用率,核心数]
//mem {memFree: 189, memTotal: 1741, memCached:
//722, memBuffers: 139, memRealUsed: 691}
//内存实时信息
//load {max: 12, safe: 9, one: 0, five: 0.01, limit: 12, fifteen: 0.05}
//负载实时信息 one: 1 分钟 five: 5 分钟 fifteen: 10 分
        GetNetWork
    }
    public String getJson(){
        try {
            String btSign = "tn4mlszQx6c0Eb6ANQ7ph6dqh3Yn2g19";
            String timestamp = (System.currentTimeMillis()+"");
            String md5Sign = getMd5(btSign);
            String temp = timestamp+md5Sign;
            String token = getMd5(temp);
            String json = "request_time="+timestamp+"&request_token="+token;
            return json;
        }catch (Exception e) {
            e.printStackTrace();
        }
            return null;
    }
    public String getSystemInfo(){
        String responseText = sendPost(baseUrl+Data.GetSystemTotal, getJson());
        logger.info("系统信息"+responseText);
        return responseText;
    }
    public String getDiskInfo(){
        String responseText = sendPost(baseUrl+Data.GetDiskInfo, getJson());
        logger.info("磁盘信息"+responseText);
        return responseText;
    }
    public String getNetWork(){
        String responseText = sendPost(baseUrl+Data.GetNetWork, getJson());
        logger.info("网络信息"+responseText);
        return responseText;
    }

    public  int cpuLoad() {
        double cpuLoad = osmxb.getSystemCpuLoad();
        int percentCpuLoad = (int) (cpuLoad * 100);
        return percentCpuLoad;
    }

    public  int memoryLoad() {
        double totalvirtualMemory = osmxb.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize/totalvirtualMemory;
        int percentMemoryLoad = (int) ((1-value)*100);
        return percentMemoryLoad;
    }



    public  String getMd5(String str) throws Exception
    {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误，"+e.toString());
        }
    }
    public  String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "text/xml,text/javascript,text/html,application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}
