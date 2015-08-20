package com.github.ywind.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月12日 下午10:11:20 类说明
 * 
 */
public interface View {

	public void render(Map<String, ?> models, HttpServletRequest request,
			HttpServletResponse response);

}
