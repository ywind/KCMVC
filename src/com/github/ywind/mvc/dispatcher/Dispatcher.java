package com.github.ywind.mvc.dispatcher;

import helper.IocFactoryHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ywind.annotation.Controller;
import com.github.ywind.annotation.InterceptorURI;
import com.github.ywind.annotation.RequestURI;
import com.github.ywind.handler.HandlerAction;
import com.github.ywind.handler.HandlerExecutionChain;
import com.github.ywind.interceptor.Interceptor;
import com.github.ywind.ioc.IocFactory;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月8日 下午4:12:52
 * 类说明
 * 
 */
public class Dispatcher extends HttpServlet {
	private Map<String, List<Interceptor>> interceptorMap;
	private Map<String, HandlerAction> handlerMap;
	
	/*
	 * 
	 */
	public void initBeans() throws Exception {
		initControllerHander();
		initInterceptor();
	}
	
	private void initInterceptor() {
		interceptorMap = new HashMap<String, List<Interceptor>>();
		List<Object> interceptors = IocFactoryHelper.getIocFactory().getInterceptors();
		for (Object object : interceptors) {
			Class<? extends Object> cls=object.getClass();
			if(cls.isAnnotationPresent(InterceptorURI.class))
			{
				if (interceptorMap.containsKey(cls.getAnnotation(InterceptorURI.class).iurl())){
					interceptorMap.get(cls.getAnnotation(InterceptorURI.class).iurl()).add((Interceptor)object);
				}else {
					List<Interceptor> i = new LinkedList<Interceptor>();
					i.add((Interceptor)object);
					interceptorMap.put(cls.getAnnotation(InterceptorURI.class).iurl(), i);
				}
			}
		}
	}

	private void initControllerHander() throws Exception {
		List<Object> controllers = IocFactoryHelper.getIocFactory().getControllers();
		handlerMap = new HashMap<String, HandlerAction>();
		for (Object object : controllers) {
			initHandler(object);
		}
	}
	private void initHandler(Object obj){
		Class<? extends Object> cls=obj.getClass();
		Method[] methods=cls.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(RequestURI.class)) {
				handlerMap.put(method.getAnnotation(RequestURI.class).rurl(),new HandlerAction(obj,method));
			}
		}
	}
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HandlerExecutionChain handlerExecutionChain = getHandlerExecutionChain(request.getRequestURI());
		handlerExecutionChain.execute(request,response);
	}
	
	private HandlerExecutionChain getHandlerExecutionChain(String uri){
		
		HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();
		handlerExecutionChain.setHandlerAction(getHandlerAction(uri));
		handlerExecutionChain.setInterceptors(getInterceptors(uri));
		return handlerExecutionChain;
		
	}
	
	private List<Interceptor> getInterceptors(String uri){
		return interceptorMap.get(uri);
		
	}
	private HandlerAction getHandlerAction(String uri){
		return handlerMap.get(uri);
		
	}
}
