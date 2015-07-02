package com.github.ywind.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月2日 上午11:29:48
 * 类说明
 * 
 */
public class TextView {
	private String textString;

	public TextView(String result) {
		this.textString = result;
	}
	
	public void render(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		StringBuilder sb = new StringBuilder(64);
        sb.append("text/html;charset=")
          .append("UTF-8");
        resp.setContentType(sb.toString());
        PrintWriter pw = resp.getWriter();
        pw.write(textString);
        pw.flush();
	}
}
