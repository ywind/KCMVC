package com.github.ywind.helper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月2日 下午9:08:03
 * 
 * 利用ThreadLocal类将线程和对应的上下文绑定
 */
public class ActionContext {
	private static ThreadLocal<ActionContext> actionContextThreadLocal = new ThreadLocal<ActionContext>();
			
	private ServletContext servletContext;
	private HttpServletResponse response;
	private HttpServletRequest request;

	public ServletContext getServletContext() {
		return this.servletContext;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
	}
	
	
    public HttpServletResponse getResponse() {
		return response;
	}



	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}



	public HttpServletRequest getRequest() {
		return request;
	}



	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	

	public static ActionContext getActionContext() {
        return actionContextThreadLocal.get();
    }
	
	public static void setActionContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
		ActionContext actionContext=new ActionContext();
		actionContext.setRequest(request);
		actionContext.setResponse(response);
		actionContext.setServletContext(context);
		actionContextThreadLocal.set(actionContext);
	}
	
	public static void rmActionContext() {
		actionContextThreadLocal.remove();
	}
}
