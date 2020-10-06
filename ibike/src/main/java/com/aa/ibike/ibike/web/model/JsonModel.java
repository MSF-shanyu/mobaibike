package com.aa.ibike.ibike.web.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@ApiModel(value="结果信息实体",description="所有的REST调用得到的json结果")
public class JsonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3507935333537528017L;
	@ApiModelProperty(value="操作响应码，一般 1 表示成功 其他为失败 ",required=true)
	private Integer code;
	@ApiModelProperty(value="操作的响应信息，如code为0 则为异常信息")
	private String msg;
	@ApiModelProperty(value="操作的结果，如code为1 则为结果值")
	private Object obj;
	@ApiModelProperty(value="本操作执行完后，下一步重定向的地址")
	private String url;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "JsonModel [code=" + code + ", msg=" + msg + ", obj=" + obj + ", url=" + url + "]";
	}

}
