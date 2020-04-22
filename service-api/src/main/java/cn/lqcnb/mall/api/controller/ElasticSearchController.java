package cn.lqcnb.mall.api.controller;

import cn.lqcnb.mall.api.entity.ElasticEntity;
import cn.lqcnb.mall.api.entity.Goods;
import cn.lqcnb.mall.api.service.ElasticSearchService;
import cn.lqcnb.mall.api.service.GoodsService;
import cn.lqcnb.mall.api.vo.QueryVo;
import cn.lqcnb.mall.common.entity.R;
import cn.lqcnb.mall.common.utils.ElasticUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author lqc520
 * @Description: es测试
 * @date 2020/4/4 19:33
 * @see cn.lqcnb.mall.api.controller
 */
@RestController
@RequestMapping("api/es")
@Api(tags = "搜索引擎控制器")
@Slf4j
public class ElasticSearchController {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private GoodsService goodsService;

    @Resource
    private ElasticSearchService elasticSearchService;

    private String indexName = "goods";


    @GetMapping(value = "/get")
    public R get(@RequestBody QueryVo queryVo){

        if(!StringUtils.isNotEmpty(queryVo.getIdxName())){

            log.warn("索引为空");
            return  R.error();
        }

        try {
            Class<?> clazz = ElasticUtil.getClazz(queryVo.getClassName());
            Map<String,Object> params = queryVo.getQuery().get("match");
            Set<String> keys = params.keySet();
            MatchQueryBuilder queryBuilders=null;
            for(String ke:keys){
                queryBuilders = QueryBuilders.matchQuery(ke, params.get(ke));
            }
            if(null!=queryBuilders){
                SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(queryBuilders);
                List<?> data = elasticSearchService.search(queryVo.getIdxName(),searchSourceBuilder,clazz);
                return  R.ok(data);
            }
        } catch (Exception e) {

            log.error("查询数据异常，metadataVo={},异常信息={}", queryVo.toString(),e.getMessage());
        }
        return R.error();
    }



//    ============================================

    @ApiOperation(value = "创建索引接口", notes = "创建索引接口")
    @PostMapping(value = "/create/index")
    public R createIndex(String indexName) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                        .field("properties")
                        .startObject()
                            .field("name").startObject().field("index", "true").field("type", "text").field("analyzer", "ik_max_word").endObject()
//                            .field("createTime").startObject().field("index", "true").field("type", "date").field("format","yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis").endObject()
//                            .field("updateTime").startObject().field("index", "true").field("type", "date").field("format","yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis").endObject()
//
                    .endObject()
                    .endObject();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.mapping(builder);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged) {
                return  R.ok();
            } else {
                return R.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation("批量添加")
    @GetMapping("batAdd")
    public void batAdd() {
        List<Goods> goodsList = goodsService.findAll();
        goodsList.forEach(goods -> {
            try {
                add(goods, goods.getId().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @ApiOperation(value = "删除索引接口", notes = "删除索引接口")
    @DeleteMapping(value = "/delete/id")
    public R deleteById(String id){
        elasticSearchService.deleteOne(indexName,new ElasticEntity(id,null));
        return R.ok();
    }

    @ApiOperation(value = "删除索引接口", notes = "删除索引接口")
    @DeleteMapping(value = "/delete/index")
    public R delete(){
        elasticSearchService.deleteIndex(indexName);
        return R.ok();
    }

    @ApiOperation(value = "查询商品信息", notes = "查询商品信息")
    @PostMapping(value = "/getName")
    public R searchGoods(String name){
        SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(QueryBuilders.matchPhraseQuery("name",name));
        List<Goods> goodsList = elasticSearchService.search(indexName, searchSourceBuilder, Goods.class);

        return R.ok("获取数据成功",goodsList);
    }

    @ApiOperation(value = "查询商品信息", notes = "查询商品信息")
    @GetMapping(value = "/get/searchAll")
    public R searchAll(){
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<Map> mapList = new LinkedList<>();
            for (SearchHit hit : hits) {
                Map<String, Object> map = hit.getSourceAsMap();
                mapList.add(map);
            }
            return R.ok(mapList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error();
    }


    @ApiOperation(value = "修改商品信息", notes = "修改商品信息")
    @GetMapping(value = "/update/goods")
    public R insertOrUpdateOne(String id,Goods goods){
        elasticSearchService.insertOrUpdateOne(indexName,new ElasticEntity(id,goods));
        return R.ok();
    }





    /**
     * 添加
     * @param goods
     * @param indexId
     * @return
     * @throws IOException
     */
    private boolean add(Goods goods, String indexId) throws IOException {
        IndexRequest request = new IndexRequest(indexName).id(indexId).source(JSON.toJSONString(goods), XContentType.JSON);
         restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return true;
    }

    /**
     * 查询
     * @param indexId
     * @return
     * @throws IOException
     */
    @GetMapping("query")
    private Map<String, Object> query(String indexId) throws IOException {
        GetRequest request = new GetRequest(indexName, indexId);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);

        return response.getSource();
    }

    @GetMapping("searchTest")
    public void searchTest(){
        try {
            elasticSearchService.setMapping("lqc");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @GetMapping("batAddTest")
    public void batAddTest() {
        BulkRequest request = new BulkRequest();
        List<Goods> goodsList = goodsService.findAll();
        goodsList.forEach(goods -> {
                request.add(new IndexRequest("goods")
                        .id(goods.getId().toString())
                        .source(goods,XContentType.JSON)
                );
        });
        try {
            restHighLevelClient.bulk(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> search() throws IOException {
        // 构建搜索
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询条件很多,如果做搜索时有些选项可填可不填,可空克不空
        // 创建条件查询
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // 条件一
        boolBuilder.must(QueryBuilders.matchPhraseQuery("字段1", "xx"));
        // 条件二
        boolBuilder.must(QueryBuilders.matchPhraseQuery("字段2", "xx"));
        // 带范围的
        // searchSourceBuilder.postFilter(QueryBuilders.rangeQuery("某个是数的字段").gte("gte大于等于").lte("lte小于等于"));
        // 排个序
        // searchSourceBuilder.sort("排序字段", SortOrder.DESC);
        // 分个页(from从第几条开始,size每页显示几条)
        // searchSourceBuilder.from(0).size(10);
        // 将条件添加进构造的搜索
        searchSourceBuilder.query(boolBuilder);
        // 查询所有(这条和上面这条只能生效一个,有条件就不能匹配所有)
        // searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        // 设置索引
        searchRequest.indices("logstash-apigatewaytracelogger-dev-2019.05.10");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        System.out.println("查到数目:" + searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        ArrayList<String> list = new ArrayList<String>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            list.add(json);
        }

        return list;
    }

}
