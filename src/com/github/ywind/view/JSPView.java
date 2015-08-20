package com.github.ywind.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月12日 下午10:54:41 类说明
 * 
 */
public class JSPView extends AbstractView {

	private String path;
	private ForwardType type;

	public JSPView(String path) {
		this.path = path;
		type = ForwardType.Forward;
	}

	public JSPView(String path, ForwardType type) {
		this.path = path;
		this.type = type;
	}

	@Override
	public void render(Map<String, ?> models, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (type == ForwardType.Forward) {
				request.getRequestDispatcher(path).forward(request, response);
			} else if (type == ForwardType.Redirect)
				response.sendRedirect(path);

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
