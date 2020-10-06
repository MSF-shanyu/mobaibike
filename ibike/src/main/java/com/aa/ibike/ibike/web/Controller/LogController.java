package com.aa.ibike.ibike.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aa.ibike.ibike.service.LogService;
import com.aa.ibike.ibike.web.model.JsonModel;

@Controller
public class LogController {
	
	@Autowired
	private LogService logService;
	
	@PostMapping("/log/savelog")
	public @ResponseBody JsonModel readly(JsonModel jsonModel,@RequestBody String log){
		logService.save(log);
		jsonModel.setCode(1);
		return jsonModel;
	}
	
	@PostMapping("/log/addPayLog")
	public @ResponseBody JsonModel addPayLog(JsonModel jsonModel,@RequestBody String log){
		logService.savePayLog(log);
		jsonModel.setCode(1);
		return jsonModel;
	}
	
	@PostMapping("/log/addRepairLog")
	public @ResponseBody JsonModel addRepairLog(JsonModel jsonModel,@RequestBody String log){
		logService.saveRepairLog(log);;
		jsonModel.setCode(1);
		return jsonModel;
	}
	
	@PostMapping("/log/addBillingLog")
	public @ResponseBody JsonModel addBillingLog(JsonModel jsonModel,@RequestBody String log){
		logService.saveBillingLog(log);;
		jsonModel.setCode(1);
		return jsonModel;
	}
}	
