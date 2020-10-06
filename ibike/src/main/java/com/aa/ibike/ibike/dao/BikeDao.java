package com.aa.ibike.ibike.dao;

import com.aa.ibike.ibike.bean.Bike;

/**
 * 单车的操作
 * @author root
 *
 */
public interface BikeDao {
	
	/**
	 * 新增一辆新车入库，并自动生成这辆车的bid,然后在业务层调用生成二维码的功能
	 * @param bike
	 * @return
	 */
	public Bike addBike(Bike bike);
	
	/**
	 * 更新操作(对应业务的入库、上线、上锁)
	 * @param bike
	 * @return
	 */
	public void updateBike(Bike bike);
	
	/**
	 * 根据bid查车
	 */
	public Bike findBike(String bid);
}
