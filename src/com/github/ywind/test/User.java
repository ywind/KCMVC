package com.github.ywind.test;
/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月3日 下午8:12:42
 * 类说明
 * 
 */
public class User {
	public String nameString;
	public String getNameString() {
		return nameString;
	}
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	public User(String name) {
		this.nameString=name;
	}
}
