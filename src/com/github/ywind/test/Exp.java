package com.github.ywind.test;

import com.github.ywind.annotation.Controller;
import com.github.ywind.annotation.RequestURI;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月2日 下午3:28:18
 * 类说明
 * 
 */
@Controller
public class Exp {
	@RequestURI(rurl = "/render.do")
	public String test() {
		return "ss";
	}
	
	@RequestURI(rurl = "/test/*")
	public String pipei() {
		return "pipei";
	}
}
