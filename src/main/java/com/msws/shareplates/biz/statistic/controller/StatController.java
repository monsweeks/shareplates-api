package com.msws.shareplates.biz.statistic.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.service.StatServiceIF;
import com.msws.shareplates.common.exception.StatDBException;
import com.msws.shareplates.framework.annotation.DisableLogin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("stat")
@RestController
public class StatController {
	
	private final StatServiceIF<?> mainService;
	
	@Autowired
	public StatController(List<StatServiceIF<?>> services, @Value("${stat.database}") Stat_database database) {
		
		log.debug("same result {}", database == Stat_database.influxdb);
		this.mainService = services.stream().filter( e -> e.getName() == database)
				.findFirst()
				.orElseThrow( () ->
						new StatDBException("no stat database selected")
				);
 
	}


	@DisableLogin
	@GetMapping(path="/test/get")
	public Object testget(@RequestParam(value = "tag_name", required = false)String tag_name) {
		
		if(tag_name == null)
			return mainService.getData(TimeUnit.DAYS, 1);
		else
			return mainService.getData(tag_name, TimeUnit.DAYS, 1);

 
	}
	
	@DisableLogin
	@GetMapping(path="/test/set")
	public void testset(@RequestParam(value = "tag_name", required = true)String tag_name) {
		
		mainService.setData(tag_name);

	}

}
