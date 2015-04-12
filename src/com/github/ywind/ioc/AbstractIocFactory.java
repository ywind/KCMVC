package com.github.ywind.ioc;

import java.util.List;

import javax.servlet.ServletContext;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:47:13
 * 类说明
 * 
 */
public abstract class AbstractIocFactory implements IocFactory {

	@Override
	public void init(ServletContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Object> getControllers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getInterceptors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getOthers() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
