package tk.mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author lqc520
 * 特别注意，该接口不能被扫描到，否则会出错
 * @version 1.0
 * @date 2019/10/22 10:53
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}