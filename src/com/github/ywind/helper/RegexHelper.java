package com.github.ywind.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ywind E-mail:guoshukang@vip.qq.com
 * @version 创建时间：2015年7月13日 下午9:30:10 类说明
 * 
 */
public class RegexHelper {

	// Matcher matcher;
	public static boolean regexURL(String url) {
		Pattern pattern = Pattern
				.compile("^((((\\.){2}/)*|/)?|(/))?(([a-z])+(/))*(([a-z])+(\\.jsp)?)$");
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
}
