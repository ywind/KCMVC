package com.github.ywind.test;

import com.github.ywind.annotation.InterceptorURI;
import com.github.ywind.helper.ActionContext;
import com.github.ywind.interceptor.AbstractInterceptor;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月3日 下午6:46:27 类说明
 * 
 */
@InterceptorURI(iurl = "/test/convert.do", weight = 0)
public class Interceptor2 extends AbstractInterceptor {

	public boolean doInterceptor() {
		ActionContext.getActionContext().getRequest()
				.setAttribute("interceptor2", "interceptor2");
		return true;
	}

	@Override
	public void postInterceptor() {
		ActionContext.getActionContext().getRequest()
				.setAttribute("interceptor4", "interceptor4");
	}

}
