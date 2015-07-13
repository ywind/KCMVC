package com.github.ywind.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月13日 下午9:39:25
 * 类说明
 * 
 */
public class TextView implements View {
	private String string;
	private String contentType;
	private String characterEncoding;
	
	public TextView(String string) {
		this.string=string;
	}
	
	public TextView(String string,String contentType) {
		this.string=string;
		this.contentType=contentType;
	}
	
	public TextView(String string,String contentType,String characterEncoding) {
		this.string=string;
		this.contentType=contentType;
		this.characterEncoding=characterEncoding;
	}
	
	@Override
	public void render(Map<String, ?> models, HttpServletRequest request,
			HttpServletResponse response) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(contentType==null ? "text/html" : contentType)
          .append(";charset=").append(characterEncoding==null ? "UTF-8" : characterEncoding);
        response.setContentType(sb.toString());
        PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(string);
	        pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
