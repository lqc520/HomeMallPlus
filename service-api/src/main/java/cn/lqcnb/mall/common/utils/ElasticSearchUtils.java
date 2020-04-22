package cn.lqcnb.mall.common.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lqc520
 * @Description: es
 * @date 2020/4/4 22:14
 * @see cn.lqcnb.mall.common.utils
 */
public class ElasticSearchUtils {

    /**
     * 新增或者修改，修改的时候直接覆盖之前的数据
     *
     * @param index 索引，对应数据库的库
     * @param map 实际要存储的数据

     */
    public static void insertOrUpdate(String ip, Integer port, String index,Map map) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        IndexRequest indexRequest = new IndexRequest(index).source(map);
        client.index(indexRequest,RequestOptions.DEFAULT);
        client.close();
    }

    /**
     * 删除数据
     *
     * @param index 索引
     * @param id

     */
    public static void delete(String ip,Integer port,String index,String id) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status() + "~" + delete.getResult());
        client.close();
    }

    /**
     * 更新数据
     *
     * 这个更新不会覆盖之前的数据，如果之前存在一个key（aaa），你这次更新没有key（aaa）。那么这个key（aaa）的数据不会被覆盖
     * @param index 索引
     * @param id
     * @param map 更新的数据

     */
    public static void update(String ip,Integer port,String index,String id,Map map) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        UpdateRequest request = new UpdateRequest(index, id).doc(map);
        client.update(request, RequestOptions.DEFAULT);
        client.close();
    }

    /**
     * 根据ID 获取数据
     */
    public static void getById(String ip,Integer port,String index,String id) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        GetRequest posts = new GetRequest(index, id);
        GetResponse response = client.get(posts, RequestOptions.DEFAULT);
        System.out.println(response);
        client.close();
    }

    /**
     * 根据index 获取数据
     */
    public static void getByIndex(String ip,Integer port,String index) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        SearchRequest searchRequest = new SearchRequest(index);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(search);
        client.close();
    }


    /**
     * 高级查询 模板
     */
    public static void getHighQuery(String ip,Integer port, String index) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder().numOfFragments(0).fragmentSize(500);;

        /*👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇自定义部分👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇*/

        // 两种查询选一个
        // 模糊查询 text 字段中 包含 三 的
        QueryBuilder matchQueryBuilder =  QueryBuilders.matchQuery("text", "三").analyzer("ik_max_word");

        // 条件查询 在bamTitle 和 bamDesc 字段都进行模糊匹配关键字是 "三" 的
//        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder("三");
//        queryBuilder.field("bamTitle").field("bamDesc");
//        searchSourceBuilder.query(queryBuilder);
//        highlightBuilder.field("bamTitle").field("bamDesc");


        /**
         * 高亮查询 一
         * 最简单的高亮查询
         *
         * 每一个field 都表示 字段名， 高亮部分为上面的条件搜索 三
         */
        highlightBuilder.field("text").field("name");
        /**
         * 高亮查询 二
         * 自定义高亮标签
         *
         * 每一个field 都表示 字段名， 高亮部分为上面的条件搜索 三
         */
//        highlightBuilder.preTags("<h2>");
//        highlightBuilder.postTags("</h2>");
//        highlightBuilder.field("text").field("name");
        /**
         * 高亮查询 三
         * 对每一个字段进行单独设置，highlighterType 里面都有那些值，我暂时没有研究，可以自行研究
         *
         * 每一个field 都表示 字段名， 高亮部分为上面的条件搜索 三
         */
//        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("text");
//        highlightTitle.highlighterType("unified");
//        highlightBuilder.field(highlightTitle);

        /**
         * 分页查询
         *
         * from 起始页
         * size 每页条数
         */
//        searchSourceBuilder.from(1);
//        searchSourceBuilder.size(1);

        /**
         * 根据 _id 排序 _id 字段名
         */
//        searchSourceBuilder.sort(new FieldSortBuilder("text").order(SortOrder.ASC));

        /**
         * 选择性的查询字段
         * includeFields 要查询的字段名称
         * excludeFields 不查询的字段名称
         */
        String[] includeFields = new String[] {"text","name"};
        String[] excludeFields = new String[] {"id"};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);

        /**
         * 设置超时时间
         */
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));


        /*👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆自定义部分👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆*/

        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(search);
        client.close();
    }

}
