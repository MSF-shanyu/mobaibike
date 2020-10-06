package com.aa.ibike.ibike.service;

public interface LogService {
	
	/**
	 * 保存操作日志
	 */
	public void save(String log);
	
	/**
	 * 充值日志
	 */
	public void savePayLog(String log);
	
	/**
	 * 骑行日志
	 */
	public void saveBillingLog(String log);
	
	/**
	 * 报修日志
	 */
	public void saveRepairLog(String log);
}
