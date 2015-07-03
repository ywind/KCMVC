package com.github.ywind.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月3日 下午7:57:41
 * 类说明
 * 
 */
public class JSP {

	private String path;
	
	public JSP(String path) {
		this.path = path;
	}
	
	public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getRequestDispatcher(path).forward(req, resp);
	}
	
}
