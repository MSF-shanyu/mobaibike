package com.aa.ibike.ibike.service.Impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aa.ibike.ibike.bean.Bike;
import com.aa.ibike.ibike.dao.BikeDao;
import com.aa.ibike.ibike.service.BikeService;

@Service
@Transactional
public class BikeServiceImpl implements BikeService {
	
	@Autowired
	private BikeDao bikeDao;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	private Logger longer=LogManager.getLogger();
	
	@Override
	public void open(Bike bike) {
		if( bike.getBid() == null){
			throw new RuntimeException("缺少待开单车编号");
		}
		Bike b=findByBid(bike.getBid());
		if(b==null){
			throw new RuntimeException("查无此车");
		}
		switch (b.getStatus()) {
		case Bike.UNACTIVE:
			throw new RuntimeException("此车暂未启用，请换车");
		case Bike.USING:
			throw new RuntimeException("此车正在被使用中，请换车");
		case Bike.INTROUBLE:
			throw new RuntimeException("此车已损坏，请换车");
		}
		bike.setStatus(Bike.USING);
		bikeDao.updateBike(bike);
	}

	@Override
	@Transactional(readOnly=true ,propagation=Propagation.SUPPORTS)
	public Bike findByBid(String bid) {
		Bike b=null;
		try{
			b=bikeDao.findBike(bid);
		}catch(Exception ex){
			//ex.printStackTrace();
			longer.error(ex.getMessage());
		}
		//复杂业务
		return b;
	}

	@Override
	public Bike addNewBike(Bike bike) {
		Bike b=bikeDao.addBike(bike);
		String bid=b.getBid();
		bike=findByBid(bid);
		//TODO 根据bid生成二维码
		String qrcode =bid+"";
		bike.setQrcode(qrcode);
		bikeDao.updateBike(bike);
		return bike;
	}
	
//	public List<Bike> findNearAll(Bike bike){
//		// bike.find({loc:{$near:[x,y]},status:1})
//		Query query=new Query();
//		query.addCriteria(Criteria.where("status").is(bike.getStatus()))
//			.addCriteria(Criteria.where("loc").near(new Point(bike.getLongitude(),bike.getLatitude())))
//			.limit(10);
//		
//		//查出来的json结构
//		List<Bike> list=this.mongoTemplate.find(query, Bike.class,"bike");
//		
//		for(Bike b:list){
//			b.setBid(b.getId());
//			b.setId(null);
//			b.setLatitude(b.getLoc()[0]);
//			b.setLongitude(b.getLoc()[1]);
//			b.setLoc(null);
//		}
//		return list;	
//	}
	
	@Override
	public List<Bike> findNearAll(Bike bike) {
		Point point = new Point(bike.getLatitude(), bike.getLongitude());
		Query q=new Query(   
				    Criteria.where("loc").near(point)
				            .maxDistance(20)
				         )
				            .limit(10);
		
		List<Bike> list = mongoTemplate.find(  q, Bike.class);
		for (Bike b : list) {
			b.setBid(b.getId());
			b.setLatitude(b.getLoc()[0]);
			b.setLongitude(b.getLoc()[1]);
		}

		return list;
	}

	@Override
	public void reportMantinant(Bike bike) {
		//根据bid查出车的状态, 要报修的车不能是行驶状态 
		Query q=new Query();
		q.addCriteria(Criteria.where("id").is(bike.getBid()));
//		Bike torepair=this.mongoTemplate.findOne(q, Bike.class,"bike");
		Bike torepair=mongoTemplate.findById(bike.getBid(), Bike.class,"bike");
		if(torepair==null){
			throw new RuntimeException("查无此车登记"+bike.getBid());
		}
		if(torepair.getStatus()==2){
			throw new RuntimeException("正在报修的车:"+ bike.getBid()+"正在行驶状态，为了您的安全,请锁车后再报修");
		}
		//2. 将此信息存入到  mongo中，并加入一个状态  handleStatus: 0 暂未处理  1已经处理 
		//TODO: 根据经纬度查询具体地址. ,存到  mongo 的 torepairbikes
		this.mongoTemplate.insert(bike, "torepairbikes");
		//      以后处理完了，要加入  handler 处理人   handleTime 处理时间
		//3. 将此车的状态在  bike collection中更改为 3
		Update u=new Update();
		u.set("status", 3);
		this.mongoTemplate.updateFirst(q, u, Bike.class,"bike");
	}

	@Override
	public List<Bike> bikeList(Bike bike) {
		List<Bike> list=null;
		if(bike==null){
			//无条件查询
			list=this.mongoTemplate.findAll(Bike.class, "bike");
		}else{
			//有条件查询
		}
		for(Bike b:list){
			b.setLatitude(b.getLoc()[0]);
			b.setLongitude(b.getLoc()[1]);
			b.setLoc(null);
		}
		return list;
	}

}
