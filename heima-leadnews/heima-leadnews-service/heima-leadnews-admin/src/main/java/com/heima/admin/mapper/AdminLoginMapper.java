package com.heima.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.AdUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminLoginMapper extends BaseMapper<AdUser> {
}
