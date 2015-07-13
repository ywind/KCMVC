package com.github.ywind.ioc;

import java.util.List;

import javax.servlet.ServletContext;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月12日 下午4:46:00
 * 类说明
 * 
 */
public interface IocFactory {
		/**
		 *初始化
		 */
		void init(ServletContext context);
		
		
		/**
		 * destory ioc
		 */
		void destroy();
		
		
		/**
		 * 得到controlers
		 * @return
		 */
		List<Object> getControllers()throws Exception;
		
		/**
		 * 得到interpcetors
		 * @return
		 */
		List<Object> getInterceptors();
		
		/**
		 * 得到其他
		 * @return
		 */
		List<Object> getOthers();
}
