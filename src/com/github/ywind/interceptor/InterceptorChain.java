package com.github.ywind.interceptor;

import java.util.List;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月3日 下午7:06:39
 * 类说明
 * 
 */
public class InterceptorChain extends AbstractInterceptor {

	private List<Interceptor> interceptors;
    
	public InterceptorChain(List<Interceptor> interceptors) {
		super();
		this.interceptors=interceptors;
	}

	@Override
	public boolean doInterceptor() {
		boolean flag=true;
		for (Interceptor interceptor : interceptors) {
			if(!interceptor.doInterceptor())
				{
					flag=false;
					break;
				}
		}
		return flag;
	}
}
