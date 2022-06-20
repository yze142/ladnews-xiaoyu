package com.heima.wemedia.service;

import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface WmSensitiveService {
    /**
     * 删除
     * @param id
     * @return
     */

    public ResponseResult delSensitive( Integer id);

    /**
     * 查询列表
     * @param sensitiveDto
     * @return
     */
    public ResponseResult selectSensutiveList( SensitiveDto sensitiveDto);

}
