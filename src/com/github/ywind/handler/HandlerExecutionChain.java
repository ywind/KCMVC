package com.github.ywind.handler;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ywind.interceptor.Interceptor;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:22:41 类说明
 * 
 */
public class HandlerExecutionChain {
	private Interceptor interceptorChain;
	private HandlerAction handlerAction;

	public Interceptor getInterceptorChain() {
		return interceptorChain;
	}

	public void setInterceptorChain(Interceptor interceptorChain) {
		this.interceptorChain = interceptorChain;
	}

	public HandlerAction getHandlerAction() {
		return handlerAction;
	}

	public void setHandlerAction(HandlerAction handlerAction) {
		this.handlerAction = handlerAction;
	}

	public Object execute(HttpServletRequest request,
			HttpServletResponse response, Object... args)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (handlerAction == null) {
			return null;
		}

		if (!interceptorChain.doInterceptor())
			return null;

		return handlerAction.excute(args);
	}

	public void postHander(HttpServletRequest request,
			HttpServletResponse response, Object... args)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		interceptorChain.postInterceptor();

	}
}
