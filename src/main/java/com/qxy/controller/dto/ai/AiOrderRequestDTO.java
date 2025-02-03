// com.qxy.service.dto.AiOrderRequestDTO.java
package com.qxy.controller.dto.ai;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author water
 * @description: AI下单请求DTO
 * @date 2025/2/3 20:15
 * @version 1.0
 */
@Data
public class AiOrderRequestDTO {
    @NotBlank(message = "用户消息不能为空")
    private String message;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;
}