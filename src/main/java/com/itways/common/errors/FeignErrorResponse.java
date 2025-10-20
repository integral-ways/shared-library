package com.itways.common.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignErrorResponse {
    private long timestamp;
    private int status;
    private MessageCode message;
}
