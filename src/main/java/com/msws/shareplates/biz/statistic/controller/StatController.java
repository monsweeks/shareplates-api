package com.msws.shareplates.biz.statistic.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.framework.annotation.DisableLogin;


@RequestMapping("/api/stats")
@RestController
public class StatController {
	
	@Autowired
	private StatService statService;

	@DisableLogin
	@RequestMapping( path="/shares/{shareId}", method = RequestMethod.GET, produces = "application/json")
	public List<UserAccessCount> stats(@PathVariable Long shareId, 
			                           @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime startDate, 
			                           @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate ) {
		// TODO timeunit은 startDate, endDate 차이에 따라서 서버에서 결정, 최대 조회되는 시간 포인트가 50이 넘지않도록? 8시간의 경우 10분 단위로 48개, 24시간의 경우, 30분 단위로 48개 정도로~
		return statService.getDataBetweenSpecificTime(shareId, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
	}
	

}
