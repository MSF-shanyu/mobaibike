package com.aa.ibike.ibike.dao.impl;


import java.sql.PreparedStatement;

import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.aa.ibike.ibike.bean.Bike;
import com.aa.ibike.ibike.dao.BikeDao;

@Repository //用spring容器来托管这个bean
public class BikeDaoImpl implements BikeDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private void setDataSource(DataSource dataSource){
		this.jdbcTemplate=new JdbcTemplate(dataSource);
	}

	@Override
	public Bike addBike(Bike bike) {
		String sql="insert into bike(latitude,longitude,status,qrcode) values (0.0,0.0,0,'')";
		KeyHolder keyHolder=new GeneratedKeyHolder();
		this.jdbcTemplate.update(connection->{
			PreparedStatement ps=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			return ps;
		},keyHolder);
		bike.setBid(keyHolder.getKey().longValue()+"");
		return bike;
	}

	@Override
	public void updateBike(Bike bike) {
		Query q=new Query();
		q.addCriteria(Criteria.where("id").is(bike.getBid()));
		Update u=new Update();
		u.set("status", bike.getStatus());
		u.set("latitude", bike.getLatitude());
		u.set("longitude", bike.getLongitude());
		u.set("qrcode", bike.getQrcode());
		this.mongoTemplate.updateFirst(q, u, Bike.class,"bike");
	}

	@Override
	public Bike findBike(String bid) {
		Bike bike =mongoTemplate.findById(bid, Bike.class,"bike");
		return bike;
	}

//	@Override
//	public void updateBike(Bike bike) {
//		String sql="update bike set latitude=?,longitude=?,status=?,qrcode=? where bid=?";
//		this.jdbcTemplate.update(connection -> {
//			PreparedStatement ps=connection.prepareStatement(sql);
//			ps.setDouble(1, bike.getLatitude());
//			ps.setDouble(2, bike.getLongitude());
//			ps.setInt(3, bike.getStatus());
//			ps.setString(4, bike.getQrcode());
//			ps.setString(5, bike.getBid());
//			return ps;
//		});
//	}

//	@Override
//	public Bike findBike(String bid) {
//		String sql="select * from bike where bid = "+bid;
//		return (Bike)this.jdbcTemplate.queryForObject(sql, (resultSet,rowNum)->{
//			Bike bike=new Bike();
//			bike.setBid(resultSet.getString("bid"));
//			bike.setLatitude(resultSet.getDouble("latitude"));
//			bike.setLongitude(resultSet.getDouble("longitude"));
//			bike.setQrcode(resultSet.getString("qrcode"));
//			bike.setStatus(resultSet.getInt("status"));
//			return bike;
//		});
//	}
	
	

}
