package com.msws.shareplates.biz.statistic.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.framework.annotation.DisableLogin;


@RequestMapping("stat")
@RestController
public class StatController {
	
	@Autowired
	private StatService statService;
	
	
	@DisableLogin
	@GetMapping(path="/get")
	public List<UserAccessCount> get(
			              @RequestParam(value = "timeunit", required = true) TimeUnit timeunit,
						  @RequestParam(value = "amount", defaultValue = "1", required = true) int amount,
			              @RequestParam(value = "search_key", required = false) String search_key,
			              @RequestParam(value = "search_value", required = false) String search_value ) {
		
		
		return statService.getData(search_key, search_value, timeunit, amount);
 
	}
	
	@DisableLogin
	@GetMapping(path="/getDetail")
	public List<UserAccessCount> getDetail(
			              @RequestParam(value = "timeunit", required = true) TimeUnit timeunit,
						  @RequestParam(value = "amount", defaultValue = "1", required = true) int amount,
			              @RequestParam(value = "search_key", required = false) String search_key,
			              @RequestParam(value = "search_value", required = false) String search_value ) {
		
		
		return statService.getDetailData(search_key, search_value, timeunit, amount);
 
	}
	

}
