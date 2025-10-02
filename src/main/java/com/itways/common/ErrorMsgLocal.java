package com.itways.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsgLocal implements Serializable {

	private String ar;
	private String en;
	
	private String titleAr;
	private String titleEn;
	
	private String category;
	
	private String application;
}
