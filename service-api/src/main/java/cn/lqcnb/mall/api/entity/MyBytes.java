package cn.lqcnb.mall.api.entity;

/**
 * @author lqc520
 * @Description: 处理信息
 * @date 2020/3/7 17:28
 */
public class MyBytes {
    public static String substring(String src, int start_idx, int end_idx){
        byte[] b = src.getBytes();
        String tgt = "";
        for(int i=start_idx; i<=end_idx; i++){
            tgt +=(char)b[i];
        }
        return tgt;
    }
}
