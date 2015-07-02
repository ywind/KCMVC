package com.github.ywind.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:16:24
 * 类说明
 * 
 */
public abstract class AbstractInterceptor implements Interceptor {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doInterceptor(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

}
