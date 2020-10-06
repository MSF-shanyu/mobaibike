package com.aa.ibike.ibike.service;

import com.aa.ibike.ibike.bean.PayModel;

public interface PayService {
	
	/**
	 * 1. 计算金额
	 * 2. 将数据保存到mongo的  payLog      (  uuid,phoneNum,openId, 结账时间(年月日小时)  起(经纬),时间, 止(经纬) ,时间, 花费)
	 * 3. 修改单车的经纬度, 状态为1
	 * 4. 用户态: status    , balance-花费.   
	 * @param payModel
	 */
	public int pay(  PayModel payModel);
}
