package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.Goods;
import cn.lqcnb.mall.api.mapper.GoodsMapper;
import cn.lqcnb.mall.common.entity.R;
import cn.lqcnb.mall.common.service.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class GoodsService extends AbstractService<Goods> {
    @Resource
    private GoodsMapper goodsMapper;

    /**
     * 获取分页数据
     * @param map 查询条件
     * @param pageNum 当前页
     * @param pageSize 数据条数
     * @return PageInfo<>(pageList)
     */
    public PageInfo<Goods> getPageList(Map map, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Goods> pageList = goodsMapper.getPageList(map);
        System.out.println(pageList);
        return new PageInfo<>(pageList);
    }

    /**
     * 获取随机商品数据
     * @param limit 条数
     * @return List<Goods>
     */
    public List<Goods> getRandList(Integer limit){
       return goodsMapper.getRandList(limit);
    }

    /**
     * 获取分页数据
     * @param pageNum  当前页
     * @param pageSize 数据条数
     * @param goods 查询条件
     * @return List<Map>
     */
    public  List<Map> getList(Integer pageNum, Integer pageSize,Goods goods){
        if(pageNum==null||pageNum<0){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Map> list = goodsMapper.getList(goods);
        return new PageInfo<>(list).getList();
//        return goodsMapper.getList();
    }

    /**
     * 添加商品图片
     * @param file 图片文件
     * @return   "/common/img/"+fileName;
     */
    public String addImg(MultipartFile file){
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize()/1024/1024;
        System.out.println(fileName + "-->" + size+"Mb");

        String path = "E:/Study/Junior/JavaEE/idea_Program/mall/src/main/resources/static/common/img" ;
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "/common/img/"+fileName;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 搜索商品
     * @param name 商品名称
     * @return List<Goods>
     */
    public List<Goods> search(String name){
        return goodsMapper.search(name);
    }

    /**
     * 效验库存
     * @param goodsId 商品id
     * @param number 数量
     * @return boolean
     */
    public boolean cheStock(Integer goodsId,Integer number){
        Goods goods = getById(goodsId);
        if(goods.getStock()>number){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取库存
     * @param goodsId 商品id
     * @return boolean
     */
    public Integer getStock(Integer goodsId){
        return getById(goodsId).getStock();
    }






}
