package com.github.ywind.interceptor;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:16:24 类说明
 * 
 */
public abstract class AbstractInterceptor implements Interceptor {
	/*
	 * 权重大的拦截器先执行
	 */
	private int weight;

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doInterceptor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postInterceptor() {
		// TODO Auto-generated method stub

	}

}
