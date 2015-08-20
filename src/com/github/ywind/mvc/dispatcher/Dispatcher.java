package com.github.ywind.mvc.dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.ywind.annotation.InterceptorURI;
import com.github.ywind.annotation.RequestURI;
import com.github.ywind.convert.TypeConverter;
import com.github.ywind.handler.HandlerAction;
import com.github.ywind.handler.HandlerExecutionChain;
import com.github.ywind.helper.ActionContext;
import com.github.ywind.helper.IocFactoryHelper;
import com.github.ywind.helper.RegexHelper;
import com.github.ywind.interceptor.Interceptor;
import com.github.ywind.interceptor.InterceptorChain;
import com.github.ywind.view.JSPView;
import com.github.ywind.view.TextView;
import com.github.ywind.view.View;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年4月8日 下午4:12:52 类说明
 * 
 */
public class Dispatcher extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Log log = LogFactory.getLog(getClass());
	private Map<String, List<Interceptor>> interceptorMap;
	private Map<String, HandlerAction> handlerMap;

	/*
	 * 初始化控制器和拦截器
	 */
	@Override
	public void init() throws ServletException {
		log.debug("init...");
		IocFactoryHelper.getIocFactory().init(getServletContext());
		try {
			initControllerHandler();
		} catch (Exception e) {
			throw new ServletException();
		}
		initInterceptor();
	}

	/*
	 * 拦截器按照权重值进行排序
	 */
	private void initInterceptor() {
		log.debug("initInterceptor...");
		interceptorMap = new HashMap<String, List<Interceptor>>();
		List<Object> interceptors = IocFactoryHelper.getIocFactory()
				.getInterceptors();
		for (Object object : interceptors) {
			Class<? extends Object> cls = object.getClass();
			if (cls.isAnnotationPresent(InterceptorURI.class)) {
				if (interceptorMap.containsKey(cls.getAnnotation(
						InterceptorURI.class).iurl())) {
					interceptorMap.get(
							cls.getAnnotation(InterceptorURI.class).iurl())
							.add((Interceptor) object);
				} else {
					List<Interceptor> i = new ArrayList<Interceptor>();
					((Interceptor) object).setWeight(cls.getAnnotation(
							InterceptorURI.class).weight());
					i.add((Interceptor) object);
					interceptorMap.put(cls.getAnnotation(InterceptorURI.class)
							.iurl(), i);
				}
			}
		}

		for (String uri : interceptorMap.keySet()) {
			Collections.sort(interceptorMap.get(uri),
					new Comparator<Interceptor>() {
						@Override
						public int compare(Interceptor o1, Interceptor o2) {
							if (o1.getWeight() > o2.getWeight())
								return 1;
							if (o1.getWeight() < o2.getWeight()) {
								return -1;
							}
							return 0;
						}
					});

		}
	}

	/*
	 * 初始化控制器
	 */
	private void initControllerHandler() throws Exception {
		List<Object> controllers = IocFactoryHelper.getIocFactory()
				.getControllers();
		handlerMap = new HashMap<String, HandlerAction>();
		for (Object object : controllers) {
			initHandler(object);
		}
	}

	private void initHandler(Object obj) {
		Class<? extends Object> cls = obj.getClass();
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(RequestURI.class)) {
				handlerMap.put(method.getAnnotation(RequestURI.class).rurl(),
						new HandlerAction(obj, method));
			}
		}
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ActionContext.setActionContext(getServletContext(), request, response);
		log.debug("do service");
		HandlerExecutionChain handlerExecutionChain = getHandlerExecutionChain(request
				.getServletPath());
		Object result = null;
		Map<String, String[]> parametersMap = request.getParameterMap();
		try {
			if (parametersMap.size() == 0) {// 如果参数为空
				result = handlerExecutionChain.execute(request, response);
			} else {
				Method method = handlerExecutionChain.getHandlerAction()
						.getMethod();
				Class<?>[] clazz = method.getParameterTypes();
				List<String> parametersNames = null;

				try {
					parametersNames = getMethodParametersName(method);
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}

				Object[] parameters = new Object[clazz.length];

				for (int i = 0; i < parametersNames.size(); i++) {
					try {
						parameters[i] = TypeConverter.convert(clazz[i],
								parametersMap.get(parametersNames.get(i))[0]);
					} catch (ParseException e) {
						response.sendError(400);
					}
				}
				result = handlerExecutionChain.execute(request, response,
						parameters);
			}

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ActionContext.rmActionContext();
		}

		if (result == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if (result instanceof String) {
			View v = null;
			if (RegexHelper.regexURL(((String) result))) {
				v = new JSPView((String) result);
			} else {
				v = new TextView((String) result);
			}

			v.render(null, request, response);
		}
		return;
	}

	private HandlerExecutionChain getHandlerExecutionChain(String uri) {
		log.debug("getHandlerExecutionChain...");
		HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();
		handlerExecutionChain.setHandlerAction(getHandlerAction(uri));
		handlerExecutionChain.setInterceptorChain(new InterceptorChain(
				getInterceptors(uri)));
		;
		return handlerExecutionChain;

	}

	/*
	 * 根据uri最近匹配拦截器和控制器
	 */
	private List<Interceptor> getInterceptors(String uri) {
		int index = uri.length();
		List<Interceptor> interceptors = new ArrayList<Interceptor>();
		while (true) {
			if (index == -1)
				break;
			if (interceptorMap.get(uri) != null) {
				interceptors.addAll(interceptorMap.get(uri));
			}
			if (index == uri.length() - 2)
				break;
			index = uri.lastIndexOf("/", index);
			uri = uri.substring(0, index + 1) + "*";
		}

		return interceptors;
	}

	private HandlerAction getHandlerAction(String uri) {
		int index = uri.length();
		while (true) {
			if (handlerMap.get(uri) != null || index == 0 || index == -1) {
				break;
			}
			index = uri.lastIndexOf("/", index);
			uri = uri.substring(0, index + 1) + "*";
		}

		return handlerMap.get(uri);
	}

	/*
	 * 获取方法的形参名字
	 */
	private List<String> getMethodParametersName(Method method)
			throws Exception {
		List<String> name = new LinkedList<String>();

		try {
			Class<?> clazz = method.getDeclaringClass();
			String methodName = method.getName();
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(clazz));
			CtClass cc = pool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(methodName);
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++)
				name.add(attr.variableName(i + pos));

		} catch (Exception e) {
			throw e;
		}

		return name;
	}
}
