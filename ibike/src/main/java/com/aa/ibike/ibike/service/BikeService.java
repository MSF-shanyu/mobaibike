package com.aa.ibike.ibike.service;

import java.util.List;

import com.aa.ibike.ibike.bean.Bike;

public interface BikeService {

	/**
	 * 开锁：1、必须有bid 2、根据bid查车   3、车的状态
	 */
	public void open(Bike bike);
	
	/**
	 * 根据bid查车
	 */
	public Bike findByBid(String bid);
	
	/**
	 * 新车上架：必须生成bid,且根据bid生成二维码
	 */
	public Bike addNewBike(Bike bike);
	
	/**
	 * 查找附近单车
	 */
	public List<Bike> findNearAll(Bike bike);
	
	/**
	 * 报修处理
	 * @param bike
	 */
	public void reportMantinant(Bike bike);
	
	/**
	 * 查找所有的单车 
	 * 一次查1000条
	 */
	public List<Bike> bikeList(Bike bike);
}
