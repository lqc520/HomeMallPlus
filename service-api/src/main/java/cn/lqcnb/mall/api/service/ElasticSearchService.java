package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.ElasticEntity;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lqc520
 * @Description: es服务
 * @date 2020/4/5 23:18
 * @see cn.lqcnb.mall.api.service
 */
@Service
@Slf4j
public class ElasticSearchService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    
    public void createIndex(String indexName,String indexSQL){
        try {
            if (!this.indexExist(indexName)) {
                log.error(" indexName={} 已经存在,indexSql={}",indexName,indexSQL);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            buildSetting(request);
            request.mapping(indexSQL, XContentType.JSON);
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    public boolean indexExist(String indexName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(indexName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    public void buildSetting(CreateIndexRequest request){
        request.settings(Settings.builder().put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));
    }

    public void insertOrUpdateOne(String indexName, ElasticEntity entity) {
        IndexRequest request = new IndexRequest(indexName);
        log.error("Data : id={},entity={}",entity.getId(),JSON.toJSONString(entity.getData()));
        request.id(entity.getId());
//        request.source(entity.getData(), XContentType.JSON);
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOne(String indexName, ElasticEntity entity) {
        DeleteRequest request = new DeleteRequest(indexName);
        request.id(entity.getId());
        try {
            restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public boolean isExistsIndex(String indexName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(indexName),RequestOptions.DEFAULT);
    }

    public void insertBatch(String indexName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(indexName).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 批量添加
     * @param indexName 索引名
     * @param list 数据
     */
    public void insertBatchTrueObj(String indexName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(indexName).id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 批量删除
     * @param indexName 索引名
     * @param idList 删除的id列表
     * @param <T> 类型
     */
    public <T> void deleteBatch(String indexName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(indexName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询
     * @param indexName 索引名
     * @param builder 查询参数
     * @param c 结果类对象
     * @param <T> 类型
     * @return List<T>
     */
    public <T> List<T> search(String indexName, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(indexName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIndex(String indexName) {
        try {
            if (!this.indexExist(indexName)) {
                log.error(" indexName={} 不存在",indexName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除
     * @param indexName 索引名
     * @param builder 条件
     */
    public void deleteByQuery(String indexName, QueryBuilder builder) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     *  设置分词器
     *
     * @param index
     * @return boolean
     * @Excpiton Exception
     */
    public boolean setMapping(String index) throws Exception{
        CreateIndexRequest indexRequest = new CreateIndexRequest(index);
        restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        PutMappingRequest request = new PutMappingRequest(index);
        //创建mapping,设置分词器
        request.source(getSObjectMapping());
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(request, RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * 构造分词对象
     * @return XContentBuilder
     */
    public  XContentBuilder getSObjectMapping() {
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {

                    builder.startObject("name");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "ik_max_word");
                    }
                    builder.endObject();

//                    builder.startObject("userNikckName");
//                    {
//                        builder.field("type", "text");
//                        builder.field("analyzer", "ik_max_word");
//                    }
//                    builder.endObject();
                }
                builder.endObject();

            }
            builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(builder.toString());
        return builder;
    }


}
