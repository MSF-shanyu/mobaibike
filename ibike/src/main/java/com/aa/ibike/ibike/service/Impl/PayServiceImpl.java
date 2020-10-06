package com.aa.ibike.ibike.service.Impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aa.ibike.ibike.bean.Bike;
import com.aa.ibike.ibike.bean.PayModel;
import com.aa.ibike.ibike.bean.User;
import com.aa.ibike.ibike.service.BikeService;
import com.aa.ibike.ibike.service.PayService;
import com.aa.ibike.ibike.service.UserService;

@Service
@Transactional
public class PayServiceImpl implements PayService {
	
	@Autowired
	private BikeService bikeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Logger logger =LogManager.getLogger();
	
	public static final int MONEYPERHOUR=4;   //每小时价格
	
	@Override
	public int pay(PayModel payModel) {
		//1、计算金额
		Long startTime=payModel.getStartTime();
		Long endTime=payModel.getEndTime();
		Long spendTime=endTime-startTime;
		double hours=(Double.parseDouble(spendTime+""))/(60*60);
		Integer h=(int)Math.ceil(hours);
		int payMoney=h*MONEYPERHOUR;
		payModel.setPayMoney(payMoney);
		payModel.setLogTime(new Date().toLocaleString());
		//2. 将数据保存到mongo的 payLog ( uuid,phoneNum,openId, 结账时间(年月日小时) 起(经纬),时间, 止(经纬) ,时间, 花费) 
		this.mongoTemplate.insert(payModel, "paylog");
		//3. 修改单车的经纬度, 状态为1 
		Query q=new Query();
		q.addCriteria(Criteria.where("id").is(payModel.getBid()));
		Update u=new Update();
		Double[] loc=new Double[] {payModel.getLatitude(),payModel.getLongitude()};
		u.set("loc", loc).set("status", 1);
		this.mongoTemplate.updateFirst(q, u, Bike.class	,"bike");
		//4. 修改用户态: status , balance-花费.
		Query qu=new Query();
		qu.addCriteria(Criteria.where("phoneNum").is(payModel.getPhoneNum()));
		User user=this.mongoTemplate.findOne(qu, User.class,"users");
		Update uu=new Update();
		uu.set("status", 3);
		uu.set("balance", user.getBalance()-payMoney);
		mongoTemplate.updateFirst(qu, uu, User.class,"users");
		logger.info( "结账成功" );
		return payMoney;
	}
}
