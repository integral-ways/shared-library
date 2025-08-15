package com.itways.dtos;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseApiRs implements Serializable {
	private String requestId;
	private String note;

}
