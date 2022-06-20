package com.heima.mapper;

import com.heima.pojo.SearchArticleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApArticleMapper {

           public  List<SearchArticleVo>  selectAll();


}
