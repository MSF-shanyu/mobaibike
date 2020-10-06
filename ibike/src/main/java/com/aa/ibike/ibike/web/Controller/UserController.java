package com.aa.ibike.ibike.web.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aa.ibike.ibike.bean.User;
import com.aa.ibike.ibike.bean.WeixinResponse;
import com.aa.ibike.ibike.service.UserService;
import com.aa.ibike.ibike.utils.HttpClientUtil;
import com.aa.ibike.ibike.web.model.JsonModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import springfox.documentation.annotations.ApiIgnore;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(UserController.class);
	
	private final String SECRET = "c87a9b640f92409c2d8c1fa98367bee1";
	private final String APPID = "wx539f0585ff96ba5e";
	private final String WXSERVER = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID + "&secret=" + SECRET + "&grant_type=authorization_code";
	
	@PostMapping("/userList")
	public @ResponseBody JsonModel MemberList(@ApiIgnore JsonModel jsonModel,User user){
		try {
			List<User> list=userService.MemberList(null);
			jsonModel.setCode(1);
			jsonModel.setObj(list);
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		} 
		
		return jsonModel;
	}
	
	
	@PostMapping("/onLogin")
	public @ResponseBody JsonModel onLogin(JsonModel jm, String jscode) {
		String wxurl = WXSERVER + "&js_code=" + jscode;
		logger.info("访问后台微信的code2session:" + wxurl);
		String ret = HttpClientUtil.sendHttpPost(wxurl);
		logger.info("微信返回的结果 " + ret);  //  { session_key:xxx, openid:xxx}
		if (ret == null || "".equals(ret)) {
			jm.setCode(0);
			logger.info("网络超时");
			jm.setMsg("网络超时");
			return jm;
		}
		//   spring mvc自带了  jackson的json解析器. 这个ObjectMapper就是它里面的核心类
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 逆序列化 ，将字符串中的有效信息取出  session_key,   openid
			WeixinResponse weixinResponse = objectMapper.readValue(ret, WeixinResponse.class);
			String session_key = weixinResponse.getSession_key();// 如果解密encryptData获取unionId，会用的到
			String openId = weixinResponse.getOpenid();// 微信小程序 用户唯一标识
			// 先查询mongo中这个openId存在不存在，存在不入库，不存在就入库
			List<User> memberList = userService.selectMember(openId);
			User u = null;
			if (memberList != null && memberList.size() > 0) {
				u = memberList.get(0);   // u中有  status
				logger.info("openId:" + openId + "在mongo中已经存在，不需要插入,信息为:" + u);
			} else {
				//如果没有这个用户的openid记录，则说明这次操作是一次注册
				u = new User();
				u.setOpenId(openId); // 新增一个openid属性
				u.setStatus(0);
				userService.addMember(u);
				logger.info("openId:" + openId + "对应的mongo不存在，插入数据库");
			}
			String rsession = userService.redisSessionKey(openId, session_key);
			// (7) 把新的sessionKey返回给小程序
			jm.setCode(1);
			Map<String,String> m=new HashMap<>();
			m.put("uuid",rsession);
			m.put("openid",openId);
			m.put("status",   u.getStatus() +""   );
			m.put("phoneNum", u.getPhoneNum());
			jm.setObj(m);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setMsg("微信返回的错误码" + e.getMessage());
			return jm;
		}
		return jm;
	}
	
	@PostMapping("/recharge")
	public @ResponseBody JsonModel recharge(@ApiIgnore JsonModel jsonModel,double balance, String phoneNum){
		boolean b=false;
		try {
			b=userService.recharge(balance,phoneNum);
			if( b ) {
				jsonModel.setCode(1);
			}else {
				jsonModel.setCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(   e.getMessage() );
		}
		return jsonModel;
	}
	
	@PostMapping("/identity")
	public @ResponseBody JsonModel identity(@ApiIgnore JsonModel jsonModel,User user){
		boolean flag=userService.identity(user);
		if(flag){
			jsonModel.setCode(1);
		}else{
			jsonModel.setCode(0);
		}
		return jsonModel;
	}
	
	@PostMapping("/deposit")
	public @ResponseBody JsonModel deposit(@ApiIgnore JsonModel jsonModel,User user){
		boolean flag=userService.deposit(user);
		if(flag){
			jsonModel.setCode(1);
		}else{
			jsonModel.setCode(0);
		}
		return jsonModel;
	}
	
	@PostMapping("/genCode")
	public @ResponseBody JsonModel genSMSCode(@ApiIgnore JsonModel jsonModel,String nationCode, String phoneNum){
		String msg="true";
		try {
			// 生成4位随机数 -> 调用短信接口发送验证码 -> 将手机号对应的验证码保存到redis中，并且设置这个key的有效时长
			userService.genVerifyCode(nationCode, phoneNum);
			jsonModel.setCode(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return jsonModel;
	}
	
	@PostMapping("/verify")
	public @ResponseBody JsonModel verify(@ApiIgnore JsonModel jsonModel,User user){
		boolean flag=false;
		try {
			flag=userService.verify(user);
			if(flag){
				jsonModel.setCode(1);
			}else{
				jsonModel.setCode(0);
				jsonModel.setMsg("效验码错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonModel.setCode(0);
			jsonModel.setMsg(e.getMessage());
		}
		return jsonModel;
	}
	
}
