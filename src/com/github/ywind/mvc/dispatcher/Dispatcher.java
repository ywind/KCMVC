package com.github.ywind.mvc.dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ywind.annotation.Controller;
import com.github.ywind.annotation.InterceptorURI;
import com.github.ywind.annotation.RequestURI;
import com.github.ywind.handler.HandlerAction;
import com.github.ywind.handler.HandlerExecutionChain;
import com.github.ywind.helper.IocFactoryHelper;
import com.github.ywind.interceptor.Interceptor;
import com.github.ywind.ioc.IocFactory;
import com.github.ywind.view.TextView;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月8日 下午4:12:52
 * 类说明
 * 
 */
public class Dispatcher extends HttpServlet {
	private ServletContext servletContext;
	private Map<String, List<Interceptor>> interceptorMap;
	private Map<String, HandlerAction> handlerMap;
	
	/*
	 * 
	 */
	@Override
	public void init() throws ServletException{
		
		IocFactoryHelper.getIocFactory().init(getServletContext());
		try {
			initControllerHander();
		} catch (Exception e) {
			throw new ServletException();
		}
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
		log(request.getRequestURI());
		HandlerExecutionChain handlerExecutionChain = getHandlerExecutionChain(request.getServletPath());
		Object result=null;
		try {
			result = handlerExecutionChain.execute(request,response);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result==null) response.sendError(HttpServletResponse.SC_NOT_FOUND);
		if(result instanceof String)
		{
			new TextView((String)result).render(request, response);
			//return;
		}
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
