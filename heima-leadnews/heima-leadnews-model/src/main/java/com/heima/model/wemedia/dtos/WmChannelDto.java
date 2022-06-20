package com.heima.model.wemedia.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: heima-leadnews
 * @description: 分页查询频道
 * @author: hello.xaioyu
 * @create: 2022-06-17 14:47
 **/
@Data
@NoArgsConstructor
public class WmChannelDto {

    private String name;
    private Integer page;
    private Integer size;

}
