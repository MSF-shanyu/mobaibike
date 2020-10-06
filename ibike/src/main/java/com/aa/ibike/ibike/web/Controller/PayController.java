package com.aa.ibike.ibike.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aa.ibike.ibike.bean.PayModel;
import com.aa.ibike.ibike.service.PayService;
import com.aa.ibike.ibike.web.model.JsonModel;

import springfox.documentation.annotations.ApiIgnore;

@Controller
public class PayController {
	
	@Autowired
	private PayService payService;
	
	@PostMapping(value="/pay")
	public @ResponseBody JsonModel pay(@ApiIgnore JsonModel jsonModel,PayModel payModel){
		try {
			int paymoney=payService.pay(payModel);
			jsonModel.setCode(1);
			jsonModel.setObj(paymoney);
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return jsonModel;
	}
}
