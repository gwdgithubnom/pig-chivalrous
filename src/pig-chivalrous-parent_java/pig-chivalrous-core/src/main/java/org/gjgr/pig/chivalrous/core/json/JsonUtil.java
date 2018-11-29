/*
package org.gjgr.tools.Json;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import IORuntimeException;
import FileReader;
import JsonArray;
import JsonObject;
import ArrayUtil;
import ObjectUtil;
import StrUtil;

*/
/**
 * Json工具类
 *
 * @author Looly
 * <p>
 * <p>
 * 创建JsonObject
 * @return JsonObject
 * <p>
 * 创建 JsonArray
 * @return JsonArray
 * <p>
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 * <p>
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 * <p>
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 * <p>
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 * @param schema
 * ： 模式名
 * @param name
 * ： 名称
 * @param type
 * ： 数据源类型
 * <p>
 * Description:
 * </p>
 * <p>
 * 对象转换成Json字符串
 * @param obj
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 * <p>
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 * <p>
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 * <p>
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 * <p>
 * 读取Json
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 * <p>
 * 读取JsonObject
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 * <p>
 * 读取JsonArray
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 * <p>
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 * <p>
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 * <p>
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 * <p>
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * <li>array or collection -> JsonArray</li>
 * <li>beanMapWithHashSetValue -> JsonObject</li>
 * <li>standard property (Double, String, et al) -> 原对象</li>
 * <li>来自于java包 -> 字符串</li>
 * <li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 * @param object The object to wrap
 * @return The wrapped value
 * <p>
 * 创建JsonObject
 * @return JsonObject
 * <p>
 * 创建 JsonArray
 * @return JsonArray
 * <p>
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 * <p>
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 * <p>
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 * <p>
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 * @param schema
 * ： 模式名
 * @param name
 * ： 名称
 * @param type
 * ： 数据源类型
 * <p>
 * Description:
 * </p>
 * <p>
 * 对象转换成Json字符串
 * @param obj
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 * <p>
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 * <p>
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 * <p>
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 * <p>
 * 读取Json
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 * <p>
 * 读取JsonObject
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 * <p>
 * 读取JsonArray
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 * <p>
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 * <p>
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 * <p>
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 * <p>
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * <li>array or collection -> JsonArray</li>
 * <li>beanMapWithHashSetValue -> JsonObject</li>
 * <li>standard property (Double, String, et al) -> 原对象</li>
 * <li>来自于java包 -> 字符串</li>
 * <li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 * @param object The object to wrap
 * @return The wrapped value
 * <p>
 * 创建JsonObject
 * @return JsonObject
 * <p>
 * 创建 JsonArray
 * @return JsonArray
 * <p>
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 * <p>
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 * <p>
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 * <p>
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 * @param schema
 * ： 模式名
 * @param name
 * ： 名称
 * @param type
 * ： 数据源类型
 * <p>
 * Description:
 * </p>
 * <p>
 * 对象转换成Json字符串
 * @param obj
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 * <p>
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 * <p>
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 * <p>
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 * <p>
 * 读取Json
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 * <p>
 * 读取JsonObject
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 * <p>
 * 读取JsonArray
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 * <p>
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 * <p>
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 * <p>
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 * <p>
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * <li>array or collection -> JsonArray</li>
 * <li>beanMapWithHashSetValue -> JsonObject</li>
 * <li>standard property (Double, String, et al) -> 原对象</li>
 * <li>来自于java包 -> 字符串</li>
 * <li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 * @param object The object to wrap
 * @return The wrapped value
 * <p>
 * 创建JsonObject
 * @return JsonObject
 * <p>
 * 创建 JsonArray
 * @return JsonArray
 * <p>
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 * <p>
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 * <p>
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 * <p>
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 * @param schema
 * ： 模式名
 * @param name
 * ： 名称
 * @param type
 * ： 数据源类型
 * <p>
 * Description:
 * </p>
 * <p>
 * 对象转换成Json字符串
 * @param obj
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 * <p>
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 * <p>
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 * <p>
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 * <p>
 * 读取Json
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 * <p>
 * 读取JsonObject
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 * <p>
 * 读取JsonArray
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 * <p>
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 * <p>
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 * <p>
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 * <p>
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 * <p>
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 * <p>
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * <li>array or collection -> JsonArray</li>
 * <li>beanMapWithHashSetValue -> JsonObject</li>
 * <li>standard property (Double, String, et al) -> 原对象</li>
 * <li>来自于java包 -> 字符串</li>
 * <li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 * @param object The object to wrap
 * @return The wrapped value
 *
 * 创建JsonObject
 * @return JsonObject
 *
 * 创建 JsonArray
 * @return JsonArray
 *
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 *
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 *
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 *
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 * @param schema
 * ： 模式名
 * @param name
 * ： 名称
 * @param type
 * ： 数据源类型
 * <p>
 * Description:
 * </p>
 *
 * 对象转换成Json字符串
 * @param obj
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return Json字符串转成对象
 * @param str
 * @param type
 * @return 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 *
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 *
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 *
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 *
 * 读取Json
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 *
 * 读取JsonObject
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 *
 * 读取JsonArray
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 *
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 *
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 *
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 *
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 *
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 *
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 *
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 *
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 *
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * <li>array or collection -> JsonArray</li>
 * <li>beanMapWithHashSetValue -> JsonObject</li>
 * <li>standard property (Double, String, et al) -> 原对象</li>
 * <li>来自于java包 -> 字符串</li>
 * <li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 * @param object The object to wrap
 * @return The wrapped value
 *//*

public final class JsonUtil {
	
	private JsonUtil() {}
	
	//-------------------------------------------------------------------- Pause start
	*/
/**
 * 创建JsonObject
 * @return JsonObject
 *//*

	public static JsonObject createObj(){
		return new JsonObject();
	}
	
	*/
/**
 * 创建 JsonArray
 * @return JsonArray
 *//*

	public static JsonArray createArray(){
		return new JsonArray();
	}
	
	*/
/**
 * Json字符串转JsonObject对象
 * @param JsonStr Json字符串
 * @return JsonObject
 *//*

	public static JsonObject jsonObject(String JsonStr) {
		return new JsonObject(JsonStr);
	}
	
	*/
/**
 * Json字符串转JsonObject对象
 * @param obj Bean对象或者Map
 * @return JsonObject
 *//*

	public static JsonObject jsonObject(Object obj) {
		return new JsonObject(obj);
	}

	*/
/**
 * Json字符串转JsonArray
 * @param JsonStr Json字符串
 * @return JsonArray
 *//*

	public static JsonArray newJsonArray(String JsonStr) {
		return new JsonArray(JsonStr);
	}


	*/
/**
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 *
 * @param schema
 *            ： 模式名
 * @param name
 *            ： 名称
 * @param type
 *            ： 数据源类型
 *            <p>
 *            Description:
 *            </p>
 *//*



	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	*/
/**
 * 对象转换成Json字符串
 * @param obj
 * @return
 *//*

	public static String toJson(Object obj) {
		gson = new Gson();
		return gson.toJson(obj);
	}

	*/
/**
 * Json字符串转成对象
 * @param str
 * @param type
 * @return
 *//*

	public static <T> T fromJson(String str, Type type) {
		gson = new Gson();
		return gson.fromJson(str, type);
	}

	*/
/**
 * Json字符串转成对象
 * @param str
 * @param type
 * @return
 *//*

	public static <T> T fromJson(String str, Class<T> type) {
		gson = new Gson();
		return gson.fromJson(str, type);
	}
	*/
/**
 * 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 * @param obj 对象
 * @return Json
 *//*

	public static Json newJson(Object obj){
		if(null == obj){
			return null;
		}
		
		Json Json = null;
		if(obj instanceof Json){
			Json = (Json) obj;
		}else if(obj instanceof String){
			String JsonStr = ((String)obj).trim();
			if(JsonStr.startsWith("[")){
				Json = newJsonArray(JsonStr);
			}else{
				Json = jsonObject(JsonStr);
			}
		}else if(obj instanceof Collection || obj.getClass().isArray()){//列表
			Json = new JsonArray(obj);
		}else{//对象
			Json = new JsonObject(obj);
		}
		
		return Json;
	}
	
	*/
/**
 * XML字符串转为JsonObject
 * @param xmlStr XML字符串
 * @return JsonObject
 *//*

	public static JsonObject fromXml(String xmlStr){
		return XML.toJsonObject(xmlStr);
	}
	
	*/
/**
 * Map转化为JsonObject
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 *//*

	public static JsonObject fromMap(Map<?, ?> beanMapWithHashSetValue){
		return new JsonObject(beanMapWithHashSetValue);
	}
	
	*/
/**
 * ResourceBundle转化为JsonObject
 * @param bundle ResourceBundle文件
 * @return JsonObject
 *//*

	public static JsonObject fromResourceBundle(ResourceBundle bundle){
		JsonObject JsonObject = new JsonObject();
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key != null) {
				InternalJsonUtil.propertyPut(JsonObject, key, bundle.getString(key));
			}
		}
		return JsonObject;
	}
	//-------------------------------------------------------------------- Pause end
	
	//-------------------------------------------------------------------- Read start
	*/
/**
 * 读取Json
 *
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 *//*

	public static Json readJson(File file, Charset charset) throws IORuntimeException {
		return newJson(FileReader.create(file, charset).readString());
	}
	
	*/
/**
 * 读取JsonObject
 *
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 *//*

	public static JsonObject readJsonObject(File file, Charset charset) throws IORuntimeException {
		return jsonObject(FileReader.create(file, charset).readString());
	}
	
	*/
/**
 * 读取JsonArray
 *
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 *//*

	public static JsonArray readJsonArray(File file, Charset charset) throws IORuntimeException {
		return newJsonArray(FileReader.create(file, charset).readString());
	}
	//-------------------------------------------------------------------- Read end

	//-------------------------------------------------------------------- toString start
	*/
/**
 * 转为Json字符串
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 *//*

	public static String string(Json Json, int indentFactor) {
		return Json.toJsonString(indentFactor);
	}
	
	*/
/**
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 *//*

	public static String string(Json Json) {
		return Json.toJsonString(0);
	}
	
	*/
/**
 * 转为Json字符串
 * @param Json Json
 * @return Json字符串
 *//*

	public static String stringPretty(Json Json) {
		return Json.toJsonString(4);
	}
	
	*/
/**
 * 转换为Json字符串
 * @param obj 被转为Json的对象
 * @return Json字符串
 *//*

	public static String string(Object obj){
		return string(newJson(obj));
	}
	
	*/
/**
 * 转换为格式化后的Json字符串
 * @param obj Bean对象
 * @return Json字符串
 *//*

	public static String stringPretty(Object obj){
		return stringPretty(newJson(obj));
	}
	
	*/
/**
 * 转换为XML字符串
 * @param Json Json
 * @return XML字符串
 *//*

	public static String stringXml(Json Json){
		return XML.toString(Json);
	}
	//-------------------------------------------------------------------- toString end
	
	//-------------------------------------------------------------------- bean start
	*/
/**
 * 转为实体类对象，转换异常将被抛出
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *//*

	public static <T> T bean(JsonObject Json, Class<T> beanClass) {
		return bean(Json, beanClass, false);
	}
	
	*/
/**
 * 转为实体类对象
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *//*

	public static <T> T bean(JsonObject Json, Class<T> beanClass, boolean ignoreError) {
		return null == Json ? null : Json.bean(beanClass, ignoreError);
	}
	//-------------------------------------------------------------------- bean end

	*/
/**
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 *
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 *//*

	public static String quote(String string) {
		StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			try {
				return quote(string, sw).toString();
			} catch (IOException ignored) {
				// will never happen - we are writing to a string writer
				return "";
			}
		}
	}

	*/
/**
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 *  为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 *  Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 *//*

	public static Writer quote(String string, Writer writer) throws IOException {
		if (StrUtil.isEmpty(string)) {
			writer.write("\"\"");
			return writer;
		}

		char b;		//back char
		char c = 0; //current char
		String hhhh;
		int i;
		int len = string.length();

		writer.write('"');
		for (i = 0; i < len; i++) {
			b = c;
			c = string.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					writer.write('\\');
					writer.write(c);
					break;
				case '/':
					if (b == '<') {
						writer.write('\\');
					}
					writer.write(c);
					break;
				case '\b':
					writer.write("\\b");
					break;
				case '\t':
					writer.write("\\t");
					break;
				case '\n':
					writer.write("\\n");
					break;
				case '\f':
					writer.write("\\f");
					break;
				case '\r':
					writer.write("\\r");
					break;
				default:
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
						writer.write("\\u");
						hhhh = Integer.toHexString(c);
						writer.write("0000", 0, 4 - hhhh.length());
						writer.write(hhhh);
					} else {
						writer.write(c);
					}
			}
		}
		writer.write('"');
		return writer;
	}
	
	*/
/**
 * 在需要的时候包装对象<br>
 * 包装包括：
 * <ul>
 * 	<li><code>null</code> -> <code>JsonNull.NULL</code></li>
 * 	<li>array or collection -> JsonArray</li>
 * 	<li>beanMapWithHashSetValue -> JsonObject</li>
 * 	<li>standard property (Double, String, et al) -> 原对象</li>
 * 	<li>来自于java包 -> 字符串</li>
 * 	<li>其它 -> 尝试包装为JsonObject，否则返回<code>null</code></li>
 * </ul>
 *
 * @param object The object to wrap
 * @return The wrapped value
 *//*

	public static Object wrap(Object object) {
		try {
			if (object == null) {
				return JsonNull.NULL;
			}
			if (object instanceof Json
					|| JsonNull.NULL.equals(object) 
					|| object instanceof JsonString 
					|| object instanceof CharSequence
					|| object instanceof Number
					|| ObjectUtil.isBasicType(object)) {
				return object;
			}

			if (object instanceof Collection) {
				Collection<?> coll = (Collection<?>) object;
				return new JsonArray(coll);
			}
			if (ArrayUtil.isArray(object)) {
				return new JsonArray(object);
			}
			if (object instanceof Map) {
				Map<?, ?> beanMapWithHashSetValue = (Map<?, ?>) object;
				return new JsonObject(beanMapWithHashSetValue);
			}
			Package objectPackage = object.getClass().getPackage();
			String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
			if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
				return object.toString();
			}
			return new JsonObject(object);
		} catch (Exception exception) {
			return null;
		}
	}
}
*/
