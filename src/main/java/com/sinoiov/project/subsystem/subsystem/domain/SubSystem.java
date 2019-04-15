package com.sinoiov.project.subsystem.subsystem.domain;

import com.sinoiov.common.domain.BaseEntity;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月15日
 * 描    述：
*/
public class SubSystem extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private String url;
	private String desc;
	private String img;
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	protected String getUrl() {
		return url;
	}
	protected void setUrl(String url) {
		this.url = url;
	}
	protected String getDesc() {
		return desc;
	}
	protected void setDesc(String desc) {
		this.desc = desc;
	}
	protected String getImg() {
		return img;
	}
	protected void setImg(String img) {
		this.img = img;
	}
	
}
