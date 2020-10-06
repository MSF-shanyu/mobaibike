package com.aa.ibike.ibike;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aa.ibike.ibike.bean.Bike;
import com.aa.ibike.ibike.config.AppConfig;
import com.aa.ibike.ibike.dao.BikeDao;
import com.aa.ibike.ibike.dao.impl.BikeDaoImpl;
import com.aa.ibike.ibike.service.BikeService;
import com.aa.ibike.ibike.service.UserService;

import junit.framework.TestCase;
import junit.framework.TestSuite;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
public class AppTest extends TestCase{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BikeDao bikeDao;
	
	@Autowired
	private BikeService bikeService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testMongoTemplate() {
		System.out.println( mongoTemplate.getDb().getName() );
		System.out.println(  mongoTemplate.getCollectionNames()  );
	}
	
	@Test
	public void testGenVerifyCode(){
		try {
			userService.genVerifyCode("86", "13037602338");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRedisTemplate(){
		System.out.println(redisTemplate);
	}
	
	@Test
	public void testNearBike(){
		Bike b=new Bike();
		b.setLatitude(28.189132);
		b.setLongitude(112.943868);
		List<Bike> list=bikeService.findNearAll(b);
		System.out.println(list);
	}
	
	@Test
	public void testMongo(){
		System.out.println(mongoTemplate.getDb());
		System.out.println(mongoTemplate.getCollectionNames());
	}
	
	@Test
	public void testDataSource() throws SQLException{
		assertNotNull(dataSource);
		assertNotNull(dataSource.getConnection());
	}
   
	@Test
	public void testAddNewBike(){
		Bike b=new Bike();
		Bike result=bikeDao.addBike(b);
		assertNotNull(result);
		System.out.println(result.getBid());
	}

	@Test
	public void testUpdateBike(){
		Bike b=bikeDao.findBike(1L+"");
		b.setLatitude(22.2);
		b.setLongitude(33.3);
		b.setStatus(2);
		bikeDao.updateBike(b);
	}
	
	@Test
	public void testFindBike(){
		Bike b=bikeDao.findBike(1L+"");
		assertNotNull(b);
	}
	
	@Test
	public void testServiceOpen(){
		Bike b=bikeService.findByBid(1L+"");
		bikeService.open(b);
	}
	
	@Test
	public void testServiceAddNewBike(){
		Bike b=new Bike();
		Bike result =bikeService.addNewBike(b);
		System.out.println(result.getQrcode());
	}
	
}
