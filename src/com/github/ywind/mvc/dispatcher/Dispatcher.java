package com.github.ywind.mvc.dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ywind.annotation.InterceptorURI;
import com.github.ywind.annotation.RequestURI;
import com.github.ywind.handler.HandlerAction;
import com.github.ywind.handler.HandlerExecutionChain;
import com.github.ywind.helper.ActionContext;
import com.github.ywind.helper.IocFactoryHelper;
import com.github.ywind.interceptor.Interceptor;
import com.github.ywind.view.TextView;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月8日 下午4:12:52
 * 类说明
 * 
 */
public class Dispatcher extends HttpServlet {
	//private ServletContext servletContext;
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
					List<Interceptor> i = new ArrayList<Interceptor>();
					((Interceptor)object).setWeight(cls.getAnnotation(InterceptorURI.class).weight());
					i.add((Interceptor)object);
					interceptorMap.put(cls.getAnnotation(InterceptorURI.class).iurl(), i);
				}
			}
		}
		
		for (String uri : interceptorMap.keySet()) {
			interceptorMap.get(uri);
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
		ActionContext.setActionContext(getServletContext(), request, response);
		log(request.getRequestURI());
		HandlerExecutionChain handlerExecutionChain = getHandlerExecutionChain(request.getServletPath());
		Object result=null;
		try {
			result = handlerExecutionChain.execute(request,response);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ActionContext.rmActionContext();
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
	/*
	 * 根据uri最近匹配拦截器和控制器
	 */
	private List<Interceptor> getInterceptors(String uri){
		int index=uri.length();
		while(true){
			if (interceptorMap.get(uri)!=null||index==0||index==-1) {
				break;
			}
			index=uri.lastIndexOf("/", index);
			uri = uri.substring(0, index+1)+"*";
			log(uri);
		}
		return interceptorMap.get(uri);
		
	}
	private HandlerAction getHandlerAction(String uri){
		int index=uri.length();
		while(true){
			if (handlerMap.get(uri)!=null||index==0||index==-1) {
				break;
			}
			index=uri.lastIndexOf("/", index);
			uri = uri.substring(0, index+1)+"*";
			log(uri);
		}
		
		return handlerMap.get(uri);
	}
}
