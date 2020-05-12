package com.msws.shareplates.biz.share.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareSearchConditions {
	
	private String order;
	private String direction;
	private String searchWord;
}
