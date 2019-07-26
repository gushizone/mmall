package org.mmall.controller.backend;

import org.mmall.common.ServerResponse;
import org.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Apple on 2017/6/13.
 *  后台：品类管理
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /** 增加分类 */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        return iCategoryService.addCategory(categoryName,parentId);
    }

    /** 更新品类名 */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(Integer categoryId, String categoryName){
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /** 获得平级子节点 */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        return iCategoryService.getChildrenParallelCategory(categoryId);

    }

    /** 获取当前分类id和递归子节点id */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        return iCategoryService.selectCategoryAndChildrenById(categoryId);

    }


}
