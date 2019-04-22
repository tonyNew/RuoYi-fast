package com.sinoiov.project.subsystem.subsystem.domain;

import javax.validation.constraints.NotEmpty;

import com.sinoiov.common.domain.BaseEntity;

/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月15日
 * 描    述：
*/
public class SubSystem extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@NotEmpty(message="系统名称必填")
	private String name;
	@NotEmpty(message="系统地址必填")
	private String url;
	private String desc;
//	@NotEmpty(message="系统图片地址必填")
	private String img;
	private Integer visible;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Integer getVisible() {
		return visible;
	}
	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
}
