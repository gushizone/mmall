package org.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.mmall.common.Const;
import org.mmall.common.ResponseCode;
import org.mmall.common.ServerResponse;
import org.mmall.dao.CategoryMapper;
import org.mmall.dao.ProductMapper;
import org.mmall.pojo.Category;
import org.mmall.pojo.Product;
import org.mmall.service.ICategoryService;
import org.mmall.service.IProductService;
import org.mmall.util.DateTimeUtil;
import org.mmall.util.PropertiesUtil;
import org.mmall.vo.ProductDetailVo;
import org.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apple on 2017/6/14.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    /** 保存或更新产品 */
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null){
//            将第一个子图设置为主图
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
//            约定有id为更新，无为新增
            if(product.getId() != null){
                int count = productMapper.updateByPrimaryKey(product);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
            }else {
                int count = productMapper.insert(product);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }

        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数失败");
    }

    /** 设置产品售卖状态 */
    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return  ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    /** 获取产品详情 */
    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
//       TODO 组装vo对象--value object
        //pojo->ob(business object)->vo(value object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /** VO组装 */
    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStatus(product.getStatus());
//        imageHost（允许默认值）
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.imooc.com/"));
//        parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

//      对时间类型（毫秒数）进行格式化（利用joda-time开源库）
//        createTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
//        updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /** 获取产品列表 */
    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
//        TODO myBatis-pageHelper : 使用PageInfo的分页方式
        //1.startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //2.填写自己的sql查询逻辑
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVOList = Lists.newArrayList();
        for(Product productItem : productList){//   组装VO
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVOList.add(productListVo);
        }
        //3.pageHelper--收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);//VO的变相分页
        return ServerResponse.createBySuccess(pageResult);
    }

    /** VO组装 */
    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.imooc.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /** 搜索产品 */
    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){//TODO 拼接模糊查询
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVOList = Lists.newArrayList();
        for(Product productItem : productList){//   VO组装
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVOList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 获取产品详情（// TODO 提供给前台的查询，已下架的产品不予显示）
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);// 组装VO
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /** 通过关键字/分类获取产品列表（搜索，允许关键字/分类单个为空） */
    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<Integer>();
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            // 没有查到该分组且关键字为空，就返回一个空的结果集（依然需要分页），不报错
            if(category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);//  如果集合没有变化，直接返回即可
                return  ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();//获取所以子节点及本身
        }
        // 模糊查询
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
//        排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                // 使用PageHelper的排序（参数要求：price desc）
                PageHelper.orderBy(orderByArray[0] +" "+ orderByArray[1]);
            }
        }
//        因为Mybatis中是判断是不是null,之前由对categoryIdList进行了空赋值，所以避免不当查询需要进行归null处理
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVOList = Lists.newArrayList();
        for(Product product: productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVOList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
