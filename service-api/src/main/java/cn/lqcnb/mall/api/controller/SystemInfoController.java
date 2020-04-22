package cn.lqcnb.mall.api.controller;

import cn.lqcnb.mall.api.entity.VisitReqDTO;
import cn.lqcnb.mall.api.service.SiteVisitFacade;
import cn.lqcnb.mall.api.vo.SiteVisitDTO;
import cn.lqcnb.mall.common.utils.IpUtils;
import cn.lqcnb.mall.common.utils.OkHttpUtils;
import cn.lqcnb.mall.common.utils.SystemInfoUtils;
import cn.lqcnb.mall.common.utils.WindowsInfoUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.sun.management.UnixOperatingSystemMXBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lqc520
 * @Description: 系统信息
 * @date 2020/3/7 17:06
 */
@RestController
@Api(tags = "系统信息")
@CrossOrigin
@RequestMapping("api/system")
public class SystemInfoController {

    @Autowired
    private OkHttpUtils okHttpUtils;
    @Autowired
    private SystemInfoUtils systemInfoUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SiteVisitFacade siteVisitFacade;
    @Autowired
    private IpUtils ipUtils;

    @Value("${cn.lqcnb.mall.base-url}")
    private String baseUrl;

    Logger logger = LoggerFactory.getLogger(getClass());


    @ApiOperation("获取jvm信息")
    @GetMapping("getSystemInfo")
    public Map getSystemInfo()
    {
        String used = okHttpUtils.doGet(baseUrl + "actuator/metrics/jvm.memory.used");
        Map usedMap = JSONArray.parseObject(used, new TypeReference<Map>() {
        });
        String userMeasurements = usedMap.get("measurements").toString();
        logger.info("userMeasurements"+userMeasurements);
        List<Map> userMeasurementsMap = JSONArray.parseObject(userMeasurements, new TypeReference<List<Map>>() {
        });
        String usedValue = userMeasurementsMap.get(0).get("value").toString();


        String max = okHttpUtils.doGet(baseUrl + "actuator/metrics/jvm.memory.max");
        Map maxMap = JSONArray.parseObject(max, new TypeReference<Map>() {
        });
        String maxMeasurements = maxMap.get("measurements").toString();
        logger.info(maxMeasurements);
        List<Map> maxMeasurementsMap = JSONArray.parseObject(maxMeasurements, new TypeReference<List<Map>>() {
        });
        String maxValue = maxMeasurementsMap.get(0).get("value").toString();

        logger.info(usedValue,maxValue);


        String cpu = okHttpUtils.doGet(baseUrl + "actuator/metrics/system.cpu.usage");
        Map cpuMap = JSONArray.parseObject(cpu, new TypeReference<Map>() {
        });
        String cpuMeasurements = cpuMap.get("measurements").toString();
        logger.info(cpuMeasurements);
        List<Map> cpuMeasurementsMap = JSONArray.parseObject(cpuMeasurements, new TypeReference<List<Map>>() {
        });
        String cpuValue = cpuMeasurementsMap.get(0).get("value").toString();
        double u = Double.parseDouble(usedValue);
        double m = Double.parseDouble(maxValue);
        double c = Double.parseDouble(cpuValue);
        logger.info(u+","+m+","+c);
        double memory=u/m;
        logger.info(c+","+memory);


        Map map = new HashMap();

        BigDecimal cpuDecimal = NumberUtils.toScaledBigDecimal(c);
        BigDecimal memoryDecimal = NumberUtils.toScaledBigDecimal(memory);

        map.put("cpu",cpuDecimal);
        map.put("memory",memoryDecimal);
        return map;

    }

    @ApiOperation("获取磁盘信息")
    @GetMapping("getDiskInfo")
    public String getDiskInfo(){
        return  systemInfoUtils.getDiskInfo();
    }


    @ApiOperation("获取网络信息")
    @GetMapping("getNetWork")
    public String getNetWork(){
        return  systemInfoUtils.getNetWork();
    }

    @ApiOperation("获取系统信息")
    @GetMapping("getSystem")
    public Map getSystem(){
        Map map = JSONArray.parseObject(systemInfoUtils.getSystemInfo(), new TypeReference<Map>() {

        });
        String cpuRealUsed = map.get("cpuRealUsed").toString();
        Double memTotal = Double.parseDouble(map.get("memTotal").toString());
        Double memRealUsed = Double.parseDouble(map.get("memRealUsed").toString());
        BigDecimal bigDecimal = NumberUtils.toScaledBigDecimal(memRealUsed / memTotal);
        logger.info(bigDecimal.toString()+" cpu:"+cpuRealUsed);
        return map;
    }


    @ApiOperation("获取linux系统信息")
    @GetMapping("getInfo")
    public void getInfo(){
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        if (os instanceof UnixOperatingSystemMXBean) {
            UnixOperatingSystemMXBean unixOs = (UnixOperatingSystemMXBean) os; //10240
            System.out.println(unixOs.getMaxFileDescriptorCount()); //37
            System.out.println(unixOs.getOpenFileDescriptorCount()); //8
            System.out.println(unixOs.getArch()); //x86_64
            System.out.println(unixOs.getAvailableProcessors()); //8
            System.out.println(unixOs.getName()); //Mac OS X
            System.out.println(unixOs.getObjectName()); //java.lang:type=OperatingSystem
            System.out.println(unixOs.getSystemLoadAverage()); //2.658203125
            System.out.println(unixOs.getVersion()); //10.13.3
            System.out.println(unixOs.getCommittedVirtualMemorySize()); //10345111552
            System.out.println(unixOs.getFreePhysicalMemorySize()); //2892271616
            System.out.println(unixOs.getProcessCpuLoad()); //0.0
            System.out.println(unixOs.getTotalSwapSpaceSize()); //8589934592
            System.out.println(unixOs.getTotalPhysicalMemorySize()); //17179869184
            System.out.println(unixOs.getSystemCpuLoad()); //0.0
        }
    }


    @ApiOperation("获取浏览器访问量信息")
    @GetMapping("getBrowser")
    public Map getBrowser(){
        Map<Object, Object> browser = stringRedisTemplate.opsForHash().entries("browser");
        Map map = new HashMap();
        map.put("browserName",browser.keySet());
        map.put("browserValue",browser.values());
//        List<Object> browserValue = stringRedisTemplate.opsForHash().values("browser");
//        Set<Object> browserName = stringRedisTemplate.opsForHash().keys("browser");
////        Object browserJson = JSONArray.toJSON(browser);
//        logger.info(browserValue.toString());
////        map.put("browserValue",JSONArray.toJSONString(browserValue));
//        map.put("browserValue",browserValue);
//        map.put("browserName",browserName);

        return map;
    }

    @ApiOperation("获取浏览器访问量信息")
    @GetMapping("getBrowserValue")
    public List<Object> test(){
         return stringRedisTemplate.opsForHash().values("browser");
    }

    @ApiOperation("获取数据")
    @GetMapping(path = "visit")
    public SiteVisitDTO visit() {
        VisitReqDTO reqDTO = new VisitReqDTO();
        reqDTO.setIp(ipUtils.getIpAddr());
        reqDTO.setApp("家居商城");
        reqDTO.setUri(ipUtils.getUrI());
        return siteVisitFacade.visit(reqDTO);
    }

    @ApiOperation("获取Windows信息")
    @GetMapping("getWinInfo")
    public Map getWinInfo(){
        int memory = systemInfoUtils.memoryLoad();
        int cpu = systemInfoUtils.cpuLoad();

        List<String> disk = WindowsInfoUtil.getDisk();
        String memery1 = WindowsInfoUtil.getMemery();
        Map map = new HashMap();
        map.put("memory",memory);
        map.put("cpu",cpu);

        map.put("disk",disk);
        map.put("memery1",memery1);

         return map;
    }

    @ApiOperation("获取CpuMemory已使用信息")
    @GetMapping("getCpuMemory")
    public Map getCpuMemory(){
        Map map = new HashMap();
        map.put("memory",systemInfoUtils.memoryLoad());
        map.put("cpu",systemInfoUtils.cpuLoad());
        return map;
    }

}



