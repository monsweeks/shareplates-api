package com.giant.mindplates.biz.file.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UploadFileResponse {
	
	private String fileName;
    private String result;

}
