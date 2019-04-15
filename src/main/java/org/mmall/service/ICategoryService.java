package org.mmall.service;

import org.mmall.common.ServerResponse;
import org.mmall.pojo.Category;

import java.util.List;

/**
 * Created by Apple on 2017/6/13.
 * 分类业务
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
