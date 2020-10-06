package com.aa.ibike.ibike.web.Controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aa.ibike.ibike.bean.Bike;
import com.aa.ibike.ibike.service.BikeService;
import com.aa.ibike.ibike.web.model.JsonModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(value="一辆单车的操作接口鸭",tags={"单车信息","控制层"})
public class BikeController {
	private Logger logger=LogManager.getLogger();
	
	@Autowired
	private BikeService bikeService;
	
	/**
	 * 报修
	 */
	@PostMapping(value="/repair")
	public @ResponseBody JsonModel repair(@ApiIgnore JsonModel jsonModel ,Bike bike){
		try {
			this.bikeService.reportMantinant(  bike );
			jsonModel.setCode(1);
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);;
			jsonModel.setMsg(  e.getMessage() );
		}
		return jsonModel;
	}
	
	/**
	 * 扫码开锁
	 * @param jsonModel 返回值的必须部分
	 * @param bike 必须的参数   bid 经度 纬度
	 * @return
	 */
	@RequestMapping(value="/open",method={RequestMethod.POST})
	@ApiOperation(value="用户开锁操作",notes="给指定的单车开锁，参数以json格式穿过来")
	public @ResponseBody JsonModel open(@ApiIgnore JsonModel jsonModel ,@RequestBody Bike bike){
		logger.info("请求参数:"+bike);
		try {
			bikeService.open(bike);
			jsonModel.setCode(1);
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return jsonModel;
	}
	
	@RequestMapping(value="/findNearAll",method={RequestMethod.POST})
	@ApiOperation(value="查找附近的单车",notes="查找最近的40部单车")
	public @ResponseBody JsonModel findNearAll(@ApiIgnore JsonModel jsonModel,@RequestBody Bike bike){
		List<Bike> list=bikeService.findNearAll(bike);
		jsonModel.setCode(1);
		jsonModel.setObj(list);
		return jsonModel;
	}
	
	@RequestMapping(value="/bikeList",method={RequestMethod.POST})
	@ApiOperation(value="查找所有的单车")
	public @ResponseBody JsonModel bikeList(@ApiIgnore JsonModel jsonModel){
		List<Bike> list=bikeService.bikeList(null);
		jsonModel.setCode(1);
		jsonModel.setObj(list);
		return jsonModel;
	}
}
