package com.github.ywind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月8日 下午4:36:09
 * 类说明
 * 
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptorURI {
	public String iurl();
}
