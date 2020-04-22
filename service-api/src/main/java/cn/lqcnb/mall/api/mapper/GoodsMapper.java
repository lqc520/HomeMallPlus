package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Goods;
import org.apache.ibatis.annotations.Select;
//import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends Mapper<Goods> {
    /**
     * 获取分页商品信息
     * @param map 查询条件
     * @return List<Goods>
     */
    public List<Goods> getPageList(Map map);

    /**
     * 随机获取商品信息
     * @param limit 商品个数
     * @return List<Goods>
     */
    @Select("SELECT * FROM `mall_goods` ORDER BY  RAND() LIMIT #{limit}")
    public List<Goods> getRandList(Integer limit);

    /**
     * 获取商品列表
     * @param goods 查询信息
     * @return List<Map>
     */
    public List<Map> getList(Goods goods);

    /**
     * 搜索商品信息
     * @param name 商品名称
     * @return List<Goods>
     */
    public List<Goods> search(String name);
}