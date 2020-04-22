package cn.lqcnb.homemall.service.cache.api;

/**
 * @author lqc520
 * @Description: 缓存接口
 * @date 2020/3/23 11:45
 * @see cn.lqcnb.homemall.service.cache.api
 */
public interface RedisService {
    /**
     * 设置缓存
     * @param key
     * @param val
     */
    default void put(String key, Object val) {

    }

    /**
     *
     * @param key
     * @param val
     * @param seconds 超时时间
     */
    default void put(String key, Object val, int seconds) {

    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    default Object get(String key) {
        return null;
    }

    /**
     * 删除缓存
     * @param key
     */
    default void remove(String key) {

    }
}
