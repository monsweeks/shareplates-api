package com.msws.shareplates.biz.statistic.controller;

import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.framework.annotation.DisableLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RequestMapping("/api/stats")
@RestController
public class StatController {
	
	@Autowired
	private StatService statService;

	@DisableLogin
	@RequestMapping( path="/shares/{shareId}", method = RequestMethod.GET, produces = "application/json")
	public List<UserAccessCount> stats(@PathVariable Long shareId, @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime startDate, @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate ) {
		// TODO timeunit은 startDate, endDate 차이에 따라서 서버에서 결정, 최대 조회되는 시간 포인트가 50이 넘지않도록? 8시간의 경우 10분 단위로 48개, 24시간의 경우, 30분 단위로 48개 정도로~
		return null;
	}
	
	
	@DisableLogin
	@RequestMapping( path="/get", method = RequestMethod.GET, produces = "application/json")
	public List<UserAccessCount> get(
			              @RequestParam(value = "timeunit", required = true) TimeUnit timeunit,
						  @RequestParam(value = "amount", defaultValue = "1", required = true) int amount,
						  @RequestParam(value = "timespan", defaultValue = "1m", required = true) String timespan,
			              @RequestParam(value = "search_key", required = false) String search_key,
			              @RequestParam(value = "search_value", required = false) String search_value ) {
		
		
		return statService.getData(search_key, search_value, timeunit, amount, timespan);
 
	}
	
	@DisableLogin
	@RequestMapping(path="/getDetail", method = RequestMethod.GET ,   produces = "application/json")
	public List<UserAccessCount> getDetail(
			              @RequestParam(value = "timeunit", required = true) TimeUnit timeunit,
						  @RequestParam(value = "amount", defaultValue = "1", required = true) int amount,
			              @RequestParam(value = "search_key", required = false) String search_key,
			              @RequestParam(value = "search_value", required = false) String search_value ) {
		
		
		return statService.getDetailData(search_key, search_value, timeunit, amount);
 
	}
	

}
