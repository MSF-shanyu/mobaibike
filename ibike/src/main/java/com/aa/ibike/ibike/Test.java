package com.aa.ibike.ibike;

import com.aa.ibike.ibike.utils.SmsSendUtil;

public class Test {
	public static void main(String[] args) {
		String result = SmsSendUtil.sendSms("1234", new String[] { "8615095689660" });
		System.out.println(result);
	}
}
