package tk.mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author lqc520
 * @Description: 通用mapper
 * @date 2020/3/14 15:08
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
