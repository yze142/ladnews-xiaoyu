package com.heima.model.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: heima-leadnews
 * @description: 敏感词实体类
 * @author: hello.xaioyu
 * @create: 2022-06-16 18:54
 **/
@Data
@TableName("wm_sensitive")
public class AdSensitive {

    @TableField("id")
    private Integer id;

    @TableField("sensitives")
    private String sensitives;

    @TableField("created_time")
    private Date createdTime;


}
