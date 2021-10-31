package com.kcbs.webforum.model.dao;

import com.kcbs.webforum.model.pojo.Category;
import com.kcbs.webforum.model.vo.PostVO;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long categoryId);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Long categoryId);

    Category selectByName(String categoryName);

    List<Category> selectAll();

    int selectPostNum(Long id);

    List<PostVO> selectList();

    List<PostVO> selectListOrderByHot();

    List<PostVO> selectEssencesList();

    List<PostVO> selectALLEssencesList();

    List<PostVO> selectEssencesListByCategoryId(Long categoryId);

    List<PostVO> selectListByCategoryOrderByHot(Long categoryId);

    int selectCommentNum(Long id);

    List<PostVO> selectRollBackList();

    List<PostVO> selectListByCategory(Long categoryId);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<PostVO> searchListByContent(String s);

    int deleteCategory(Long categoryId);
}