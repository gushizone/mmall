package org.mmall.controller.backend;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.Product;
import org.mmall.service.IFileService;
import org.mmall.service.IProductService;
import org.mmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Apple on 2017/6/14.
 * 后台产品管理
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 产品保存（add + update）
     * 对外共用一个接口
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(Product product){
        return iProductService.saveOrUpdateProduct(product);
    }

    /** 产品上下架啊 */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Integer productId, Integer status){
        return iProductService.setSaleStatus(productId, status);
    }

    /** 获取产品详情 */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        return iProductService.manageProductDetail(productId);
    }

    /** 获取产品列表 */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }

    /** 产品搜索 */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName, Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    /** 文件上传 */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");// webapp下
        String targetFileName = iFileService.upload(file, path);//  ftp下文件名
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;// 可获得图片的路径

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);

    }

    /**
     * 富文本中图片上传（只针对simditor插件）
     *
     * 富文本中使用simditor：http://simditor.tower.im/docs/doc-config.html
     * {
     *     "success": true/false,
     *         "msg": "error message", # optional
     *     "file_path": "[real file path]"
     * }
     * @param file
     * @param request
     * @param response
     * @return Map 因为使用simditor，对于json的返回对象有要求，这里不使用ServerResponse
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        // simditor只会处理上传成功的响应
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;

    }

}
