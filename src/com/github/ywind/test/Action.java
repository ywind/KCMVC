package com.github.ywind.test;

import com.github.ywind.annotation.Controller;
import com.github.ywind.annotation.RequestURI;
import com.github.ywind.helper.ActionContext;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月2日 下午3:28:18
 * 类说明
 * 
 */
@Controller
public class Action {
	@RequestURI(rurl = "/render.do")
	public String test() {
		return "index";
	}
	
	@RequestURI(rurl = "/test/*")
	public String pipei() {
		return "index";
	}
	
	@RequestURI(rurl = "/test/convert.do")
	public String convert(int i) {
		ActionContext.getActionContext().getRequest().setAttribute("user",new User("test"));
		return "index.jsp";
	}
}
