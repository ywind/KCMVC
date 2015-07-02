package com.github.ywind.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.github.ywind.interceptor.Interceptor;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:22:41
 * 类说明
 * 
 */
public class HandlerExecutionChain {
	private List<Interceptor> interceptors;
	private HandlerAction handlerAction;
	public List<Interceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	public HandlerAction getHandlerAction() {
		return handlerAction;
	}
	public void setHandlerAction(HandlerAction handlerAction) {
		this.handlerAction = handlerAction;
	}
	
	public Object execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (interceptors!=null) {
			for (Interceptor interceptor : interceptors) {
				interceptor.doInterceptor(request, response);
			}
		}
		if (handlerAction==null) {
			return null;
		}
		
		return handlerAction.getMethod().invoke(handlerAction.getObject());
	}
}
