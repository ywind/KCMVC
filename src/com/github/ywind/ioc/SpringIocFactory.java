package com.github.ywind.ioc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.github.ywind.annotation.Controller;
import com.github.ywind.annotation.InterceptorURI;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:48:29 类说明
 * 
 */
public class SpringIocFactory extends AbstractIocFactory {

	private ApplicationContext applicationContext;
	private List<Object> controlBeans;
	private List<Object> interceptorBeans;
	private List<Object> otherBeans;

	@Override
	public void init(ServletContext context) {
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		initBeans();
	}

	private void initBeans() {
		controlBeans = new ArrayList<Object>();
		interceptorBeans = new ArrayList<Object>();
		otherBeans = new ArrayList<Object>();
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String string : beanNames) {
			if (applicationContext.getBean(string).getClass()
					.isAnnotationPresent(Controller.class)) {
				controlBeans.add(applicationContext.getBean(string));
			} else if (applicationContext.getBean(string).getClass()
					.isAnnotationPresent(InterceptorURI.class)) {
				interceptorBeans.add(applicationContext.getBean(string));
			} else {
				otherBeans.add(applicationContext.getBean(string));
			}
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public List<Object> getControllers() throws Exception {
		// TODO Auto-generated method stub
		return this.controlBeans;
	}

	@Override
	public List<Object> getInterceptors() {
		// TODO Auto-generated method stub
		return this.interceptorBeans;
	}

	@Override
	public List<Object> getOthers() {
		// TODO Auto-generated method stub
		return this.otherBeans;
	}

}
