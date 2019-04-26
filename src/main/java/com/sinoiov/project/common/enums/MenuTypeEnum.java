package com.sinoiov.project.common.enums;
/**
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月24日
 * 描    述：
*/
public enum MenuTypeEnum {
	
	S("系统"),
	M("目录"),
	C("菜单"),
	F("按钮");
	public String type;
	MenuTypeEnum(String type){
		this.type=type;
	}

}
