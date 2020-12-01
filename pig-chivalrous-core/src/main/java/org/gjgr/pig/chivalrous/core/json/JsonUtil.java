/*
 * package org.gjgr.tools.Json;
 *
 * import java.io.File; import java.io.IOException; import java.io.StringWriter; import java.io.Writer; import
 * java.lang.reflect.Type; import java.nio.charset.Charset; import java.util.Collection; import java.util.Enumeration;
 * import java.util.Map; import java.util.ResourceBundle;
 *
 * import com.google.gson.Gson; import IORuntimeException; import FileReader; import JsonArray; import JsonObject;
 * import ArrayCommand; import ObjectUtil; import StringCommand;
 *
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
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
 *//*
 *
 * public final class JsonUtil {
 *
 * private JsonUtil() {}
 *
 * //-------------------------------------------------------------------- Pause start
 */
/**
 * 创建JsonObject
 *
 * @return JsonObject
 *//*
 *
 * public static JsonObject createObj(){ return new JsonObject(); }
 *
 */
/**
 * 创建 JsonArray
 *
 * @return JsonArray
 *//*
 *
 * public static JsonArray createArray(){ return new JsonArray(); }
 *
 */
/**
 * Json字符串转JsonObject对象
 *
 * @param JsonStr Json字符串
 * @return JsonObject
 *//*
 *
 * public static JsonObject jsonObject(String JsonStr) { return new JsonObject(JsonStr); }
 *
 */
/**
 * Json字符串转JsonObject对象
 *
 * @param obj Bean对象或者Map
 * @return JsonObject
 *//*
 *
 * public static JsonObject jsonObject(Object obj) { return new JsonObject(obj); }
 *
 */
/**
 * Json字符串转JsonArray
 *
 * @param JsonStr Json字符串
 * @return JsonArray
 *//*
 *
 * public static JsonArray newJsonArray(String JsonStr) { return new JsonArray(JsonStr); }
 *
 *
 */
/**
 * Description : 带参数构造函数, 初始化模式名,名称和数据源类型
 *
 * @param schema ： 模式名
 * @param name ： 名称
 * @param type ： 数据源类型
 *            <p>
 *            Description:
 *            </p>
 *//*
 *
 *
 *
 * private static Gson gson = null;
 *
 * static { if (gson == null) { gson = new Gson(); } }
 *
 */
/**
 * 对象转换成Json字符串
 *
 * @param obj
 * @return
 *//*
 *
 * public static String toJson(Object obj) { gson = new Gson(); return gson.toJson(obj); }
 *
 */
/**
 * Json字符串转成对象
 *
 * @param str
 * @param type
 * @return
 *//*
 *
 * public static <T> T fromJson(String str, Type type) { gson = new Gson(); return gson.fromJson(str, type); }
 *
 */
/**
 * Json字符串转成对象
 *
 * @param str
 * @param type
 * @return
 *//*
 *
 * public static <T> T fromJson(String str, Class<T> type) { gson = new Gson(); return gson.fromJson(str, type); }
 */
/**
 * 转换对象为Json<br>
 * 支持的对象：<br>
 * String: 转换为相应的对象<br>
 * Array Collection：转换为JsonArray<br>
 * Bean对象：转为JsonObject
 *
 * @param obj 对象
 * @return Json
 *//*
 *
 * public static Json newJson(Object obj){ if(null == obj){ return null; }
 *
 * Json Json = null; if(obj instanceof Json){ Json = (Json) obj; }else if(obj instanceof String){ String JsonStr =
 * ((String)obj).trim(); if(JsonStr.startsWith("[")){ Json = newJsonArray(JsonStr); }else{ Json =
 * jsonObject(JsonStr); } }else if(obj instanceof Collection || obj.getClass().isArray()){//列表 Json = new
 * JsonArray(obj); }else{//对象 Json = new JsonObject(obj); }
 *
 * return Json; }
 *
 */
/**
 * XML字符串转为JsonObject
 *
 * @param xmlStr XML字符串
 * @return JsonObject
 *//*
 *
 * public static JsonObject fromXml(String xmlStr){ return XML.toJsonObject(xmlStr); }
 *
 */
/**
 * Map转化为JsonObject
 *
 * @param beanMapWithHashSetValue {@link Map}
 * @return JsonObject
 *//*
 *
 * public static JsonObject fromMap(Map<?, ?> beanMapWithHashSetValue){ return new
 * JsonObject(beanMapWithHashSetValue); }
 *
 */
/**
 * ResourceBundle转化为JsonObject
 *
 * @param bundle ResourceBundle文件
 * @return JsonObject
 *//*
 *
 * public static JsonObject fromResourceBundle(ResourceBundle bundle){ JsonObject JsonObject = new JsonObject();
 * Enumeration<String> keys = bundle.getKeys(); while (keys.hasMoreElements()) { String key = keys.nextElement(); if
 * (key != null) { InternalJsonUtil.propertyPut(JsonObject, key, bundle.getString(key)); } } return JsonObject; }
 * //-------------------------------------------------------------------- Pause end
 *
 * //-------------------------------------------------------------------- Read start
 */
/**
 * 读取Json
 *
 * @param file Json文件
 * @param charset 编码
 * @return Json（包括JsonObject和JsonArray）
 * @throws IORuntimeException
 *//*
 *
 * public static Json readJson(File file, Charset charset) throws IORuntimeException { return
 * newJson(FileReader.create(file, charset).readString()); }
 *
 */
/**
 * 读取JsonObject
 *
 * @param file Json文件
 * @param charset 编码
 * @return JsonObject
 * @throws IORuntimeException
 *//*
 *
 * public static JsonObject readJsonObject(File file, Charset charset) throws IORuntimeException { return
 * jsonObject(FileReader.create(file, charset).readString()); }
 *
 */
/**
 * 读取JsonArray
 *
 * @param file Json文件
 * @param charset 编码
 * @return JsonArray
 * @throws IORuntimeException
 *//*
 *
 * public static JsonArray readJsonArray(File file, Charset charset) throws IORuntimeException { return
 * newJsonArray(FileReader.create(file, charset).readString()); }
 * //-------------------------------------------------------------------- Read end
 *
 * //-------------------------------------------------------------------- toString start
 */
/**
 * 转为Json字符串
 *
 * @param Json Json
 * @param indentFactor 每一级别的缩进
 * @return Json字符串
 *//*
 *
 * public static String string(Json Json, int indentFactor) { return Json.toJsonString(indentFactor); }
 *
 */
/**
 * 转为Json字符串
 *
 * @param Json Json
 * @return Json字符串
 *//*
 *
 * public static String string(Json Json) { return Json.toJsonString(0); }
 *
 */
/**
 * 转为Json字符串
 *
 * @param Json Json
 * @return Json字符串
 *//*
 *
 * public static String stringPretty(Json Json) { return Json.toJsonString(4); }
 *
 */
/**
 * 转换为Json字符串
 *
 * @param obj 被转为Json的对象
 * @return Json字符串
 *//*
 *
 * public static String string(Object obj){ return string(newJson(obj)); }
 *
 */
/**
 * 转换为格式化后的Json字符串
 *
 * @param obj Bean对象
 * @return Json字符串
 *//*
 *
 * public static String stringPretty(Object obj){ return stringPretty(newJson(obj)); }
 *
 */
/**
 * 转换为XML字符串
 *
 * @param Json Json
 * @return XML字符串
 *//*
 *
 * public static String stringXml(Json Json){ return XML.toString(Json); }
 * //-------------------------------------------------------------------- toString end
 *
 * //-------------------------------------------------------------------- bean start
 */
/**
 * 转为实体类对象，转换异常将被抛出
 *
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *//*
 *
 * public static <T> T bean(JsonObject Json, Class<T> beanClass) { return bean(Json, beanClass, false); }
 *
 */
/**
 * 转为实体类对象
 *
 * @param Json JsonObject
 * @param beanClass 实体类对象
 * @return 实体类对象
 *//*
 *
 * public static <T> T bean(JsonObject Json, Class<T> beanClass, boolean ignoreError) { return null == Json ? null :
 * Json.bean(beanClass, ignoreError); } //-------------------------------------------------------------------- bean
 * end
 *
 */
/**
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 *
 * @param string A String
 * @return A String correctly formatted for insertion in a Json text.
 *//*
 *
 * public static String quote(String string) { StringWriter sw = new StringWriter(); synchronized (sw.getBuffer()) {
 * try { return quote(string, sw).toString(); } catch (IOException ignored) { // will never happen - we are writing
 * to a string writer return ""; } } }
 *
 */
/**
 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
 * Json字符串中不能包含控制字符和未经转义的引号和反斜杠
 *
 * @param string A String
 * @param writer Writer
 * @return A String correctly formatted for insertion in a Json text.
 * @throws IOException
 *//*
 *
 * public static Writer quote(String string, Writer writer) throws IOException { if (StringCommand.isEmpty(string)) {
 * writer.write("\"\""); return writer; }
 *
 * char b; //back char char c = 0; //current char String hhhh; int i; int len = string.length();
 *
 * writer.write('"'); for (i = 0; i < len; i++) { b = c; c = string.charAt(i); switch (c) { case '\\': case '"':
 * writer.write('\\'); writer.write(c); break; case '/': if (b == '<') { writer.write('\\'); } writer.write(c);
 * break; case '\b': writer.write("\\b"); break; case '\t': writer.write("\\t"); break; case '\n':
 * writer.write("\\n"); break; case '\f': writer.write("\\f"); break; case '\r': writer.write("\\r"); break; default:
 * if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) { writer.write("\\u"); hhhh =
 * Integer.toHexString(c); writer.write("0000", 0, 4 - hhhh.length()); writer.write(hhhh); } else { writer.write(c);
 * } } } writer.write('"'); return writer; }
 *
 */
/**
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
 *
 * @param object The object to wrap
 * @return The wrapped value
 *//*
 *
 * public static Object wrap(Object object) { try { if (object == null) { return JsonNull.NULL; } if (object
 * instanceof Json || JsonNull.NULL.equals(object) || object instanceof JsonString || object instanceof CharSequence
 * || object instanceof Number || ObjectUtil.isBasicType(object)) { return object; }
 *
 * if (object instanceof Collection) { Collection<?> coll = (Collection<?>) object; return new JsonArray(coll); } if
 * (ArrayCommand.isArray(object)) { return new JsonArray(object); } if (object instanceof Map) { Map<?, ?>
 * beanMapWithHashSetValue = (Map<?, ?>) object; return new JsonObject(beanMapWithHashSetValue); } Package
 * objectPackage = object.getClass().getPackage(); String objectPackageName = objectPackage != null ?
 * objectPackage.getName() : ""; if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.")
 * || object.getClass().getClassLoader() == null) { return object.toString(); } return new JsonObject(object); }
 * catch (Exception exception) { return null; } } }
 */

/**

 /**
 * This class is used for ... ClassName: JSONHelper
 *
 * @author 龚文东 root
 * @version Nov 19, 2015 8:16:14 AM
 * @Description: TODO
 */
/**
public class JsonCommand {

    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Logger logger = LoggerFactory.getLogger(JsonCommand.class);
    private static ThreadLocal<Gson> gsonThreadLocal = ThreadLocal.withInitial(new Supplier<Gson>() {
        @Override
        public Gson get() {
            return new Gson();
        }
    });

    static {
        gsonBuilder.serializeNulls();
    }

    public JsonCommand() {
        // TODO Auto-generated constructor stub
    }

    public static JsonElement parseJsonElement(String str) {
        return new JsonParser().parse(str);
    }

    public static <T> T parse(String str, TypeToken typeToken) {
        return new Gson().fromJson(str, typeToken.getType());
    }

    public static <T> T parse(String str, Type type) {
        if (type instanceof ParameterizedType) {
            // Collection Map<String, Object> map Class<?> Holder<String>
        } else if (type instanceof TypeVariable) {
            // T \ E
        } else if (type instanceof GenericArrayType) {
            // List<>[], T[] array
            return (T) Arrays.asList(gsonThreadLocal.get().fromJson(str, type));
        } else if (type instanceof WildcardType) {
            // ?super T
        }
        return null;
    }

    public static com.google.gson.JsonElement convert(MapJson mapJson) {
        JsonElement jsonElement = mapJson.gson();
        return jsonElement;
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
/**
    public static String toJson(Object obj) {
        Gson gson = gsonThreadLocal.get();
        return gson.toJson(obj);
    }

    public static String json(Object object) {
        Gson gson = gsonThreadLocal.get();
        return gson.toJson(object);
    }

    public static JsonElement jsonElement(Object object) {
        String string = json(object);
        JsonElement jsonElement = jsonElement(string);
        return jsonElement;
    }

    public static JsonElement jsonElement(String str) {
        return new JsonParser().parse(str);
    }

    public static com.google.gson.JsonArray jsonArray(Object object) {
        String string = json(object);
        com.google.gson.JsonArray jsonElement = jsonArray(string);
        return jsonElement;
    }

    public static com.google.gson.JsonArray listStringJsonArray(List<String> strings) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        if (strings != null) {
            for (String s : strings) {
                jsonArray.add(new JsonPrimitive(s));
            }
            return jsonArray;
        } else {
            return jsonArray;
        }
    }

    public static com.google.gson.JsonArray listStringJsonArray(Set<String> strings) {
        return listStringJsonArray(new LinkedList<>(strings));
    }

    public static com.google.gson.JsonArray jsonArray(String str) {
        JsonElement jsonElement = jsonElement(str);
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        } else {
            return new com.google.gson.JsonArray();
        }
    }

    public static com.google.gson.JsonObject jsonObject(Object object) {
        String string = json(object);
        com.google.gson.JsonObject jsonElement = jsonObject(string);
        return jsonElement;
    }

    public static com.google.gson.JsonObject jsonObject(String str) {
        JsonElement jsonElement = jsonElement(str);
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        } else {
            return new com.google.gson.JsonObject();
        }
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     *
     * @param ts
     * @return
     */
/***
    public static String serializer(Object ts,
                                    final String dateFormat) {
        String jsonStr = null;
        Gson gson = null;
        if (dateFormat == null) {
            gson = gsonThreadLocal.get();
        } else {
            gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class,
                    new JsonSerializer<Date>() {
                        @Override
                        public JsonElement serialize(Date src,
                                                     Type typeOfSrc,
                                                     JsonSerializationContext context) {
                            SimpleDateFormat format = new SimpleDateFormat(
                                dateFormat);
                            return new JsonPrimitive(format.format(src));
                        }
                    })
                .setDateFormat(dateFormat).create();
            gsonThreadLocal.set(gson);
        }
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }


    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @param cl
     * @return
     */
/**
    @SuppressWarnings("unchecked")
    public static <T> T serializer(String jsonStr, Class<T> cl,
                                   final String pattern) {
        Object obj = null;
        Gson gson = null;
        if (pattern == null) {
            gson = gsonThreadLocal.get();
        } else {
            gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                        throws JsonParseException {
                        SimpleDateFormat format = new SimpleDateFormat(pattern);
                        String dateStr = json.getAsString();
                        try {
                            return format.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).setDateFormat(pattern).create();
            gsonThreadLocal.set(gson);
        }
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return (T) obj;
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
/**
    public static <T> T fromJson(String str, Class<T> type) {
        if (type.getName().equalsIgnoreCase(com.google.gson.JsonObject.class.getName())
            || type.getName().equalsIgnoreCase(com.google.gson.JsonArray.class.getName())
            || type.getName().equalsIgnoreCase(com.google.gson.JsonElement.class.getName())) {
            return (T) type.cast(fromJson(str));
        } else {
            return (T) gsonThreadLocal.get().fromJson(str, type);
        }
    }

    public static <T> T fromObject(Object object, Class<T> clazz) {
        String json = JsonCommand.toJson(object);
        return to(json, clazz);
    }

    public static <T> List<T> toArrayList(String json, Class<T> clazz) {
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 0);
        array = gsonThreadLocal.get().fromJson(json, array.getClass());
        List<T> list = new ArrayList<T>();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                list.add(clazz.cast(array[i]));
            }
        }
        return list;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        // ArrayList<T> data= (ArrayList<T>) fromJson(json,
        // new TypeToken<ArrayList<T>>() {
        // }.getType());

        return toArrayList(json, clazz);
    }

    public static <T> T[] toArray(String json, Class<T> clazz) {
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, 0);
        array = gsonThreadLocal.get().fromJson(json, array.getClass());
        return (T[]) array;
    }

    public static <T> List<T> toArrayList(JsonElement jsonElement, Class<T> clazz) {
        return toArrayList(toJson(jsonElement), clazz);
    }

    public static <T> T to(String str, Class<T> clazz) {
        if (clazz.isArray()) {

        }
        return fromJson(str, clazz);
    }

    public static <T> T to(JsonElement jsonElement, Class<T> clazz) {
        String string = toJson(jsonElement);
        return to(string, clazz);
    }

    public static JsonElement fromJson(String str) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(str);
    }

    /**
     * XML字符串转为JSONObject
     *
     * @param xmlStr XML字符串
     * @return JsonObject
     */
/**
    public static com.google.gson.JsonObject fromXml(String xmlStr) {
        JsonElement jsonElement = XmlBetweenJsonObject.toJSONObject(xmlStr).gson();
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return new com.google.gson.JsonObject();
    }

    // -------------------------------------------------------------------- Pause start

    /**
     * Map转化为JSONObject
     *
     * @param map {@link Map}
     * @return JsonObject
     */
/**
    public static com.google.gson.JsonObject fromMap(Map<?, ?> map) {
        JsonElement jsonElement = new MapJson(map).gson();
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return new com.google.gson.JsonObject();
    }

    /**
     * ResourceBundle转化为JSONObject
     *
     * @param bundle ResourceBundle文件
     * @return JsonObject
     */
/**
    public static MapJson fromResourceBundle(ResourceBundle bundle) {
        MapJson mapJson = new MapJson();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key != null) {
                InternalJsonUtil.propertyPut(mapJson, key, bundle.getString(key));
            }
        }
        return mapJson;
    }

    /**
     * 创建JSONObject
     *
     * @return JsonObject
     */
/**
    private static MapJson newJsonObject() {
        return new MapJson();
    }

    /**
     * 创建 JsonArray
     *
     * @return JsonArray
     */
/**
    private static ListJson newJsonArray() {
        return new ListJson();
    }

    /**
     * JSON字符串转JSONObject对象
     *
     * @param jsonStr JSON字符串
     * @return JsonObject
     */
/**
    private static MapJson newJsonObject(String jsonStr) {
        return new MapJson(jsonStr);
    }

    /**
     * JSON字符串转JSONObject对象
     *
     * @param obj Bean对象或者Map
     * @return JsonObject
     */
/**
    private static MapJson newJsonObject(Object obj) {
        return new MapJson(obj);
    }

    /**
     * JSON字符串转JSONArray
     *
     * @param jsonStr JSON字符串
     * @return JsonArray
     */
/**
    public static ListJson newJsonArray(String jsonStr) {
        return new ListJson(jsonStr);
    }

    /**
     * 转换对象为JSON<br>
     * 支持的对象：<br>
     * String: 转换为相应的对象<br>
     * Array Collection：转换为JSONArray<br>
     * Bean对象：转为JSONObject
     *
     * @param obj 对象
     * @return Json
     */
/**
    public static Json newJson(Object obj) {
        if (null == obj) {
            return null;
        }

        Json json = null;
        if (obj instanceof Json) {
            json = (Json) obj;
        } else if (obj instanceof String) {
            String jsonStr = ((String) obj).trim();
            if (jsonStr.startsWith("[")) {
                json = newJsonArray(jsonStr);
            } else {
                json = newJsonObject(jsonStr);
            }
        } else if (obj instanceof Collection || obj.getClass().isArray()) {
            // 列表
            json = new ListJson(obj);
        } else {
            // 对象
            json = new MapJson(obj);
        }

        return json;
    }

    /**
     * 读取JSON
     *
     * @param file JSON文件
     * @param charset 编码
     * @return Json（包括JSONObject和JSONArray）
     * @throws IORuntimeException
     */
/**
    public static Json readJSON(File file, Charset charset) throws IORuntimeException {
        return newJson(FileReader.create(file, charset).readString());
    }

    /**
     * 读取JSONObject
     *
     * @param file JSON文件
     * @param charset 编码
     * @return JsonObject
     * @throws IORuntimeException
     */
/**
    public static MapJson readJSONObject(File file, Charset charset) throws IORuntimeException {
        return newJsonObject(FileReader.create(file, charset).readString());
    }

    /**
     * 读取JSONArray
     *
     * @param file JSON文件
     * @param charset 编码
     * @return JsonArray
     * @throws IORuntimeException
     */
/**
    public static ListJson readJSONArray(File file, Charset charset) throws IORuntimeException {
        return newJsonArray(FileReader.create(file, charset).readString());
    }

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @param indentFactor 每一级别的缩进
     * @return JSON字符串
     */
/**
    public static String string(Json json, int indentFactor) {
        return json.toJSONString(indentFactor);
    }

    // -------------------------------------------------------------------- Pause end

    // -------------------------------------------------------------------- Read start

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @return JSON字符串
     */
/**
    public static String string(Json json) {
        return json.toJSONString(0);
    }

    /**
     * 转为JSON字符串
     *
     * @param json Json
     * @return JSON字符串
     */
/**
    public static String stringPretty(Json json) {
        return json.toJSONString(4);
    }

    /**
     * 转换为JSON字符串
     *
     * @param obj 被转为JSON的对象
     * @return JSON字符串
     */
/**
    public static String string(Object obj) {
        String str = null;
        Gson gson = gsonThreadLocal.get();
        try {
            str = gson.toJson(obj);
        } catch (Exception e) {
            str = string(newJson(obj));
        }
        return str;
    }
    // -------------------------------------------------------------------- Read end

    // -------------------------------------------------------------------- toString start

    /**
     * 转换为格式化后的JSON字符串
     *
     * @param obj Bean对象
     * @return JSON字符串
     */
/**
    public static String stringPretty(Object obj) {
        return stringPretty(newJson(obj));
    }

    /**
     * 转换为XML字符串
     *
     * @param json Json
     * @return XML字符串
     */
/**
    public static String stringXml(Json json) {
        return XmlBetweenJsonObject.toString(json);
    }

    /**
     * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
     * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
     * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
     *
     * @param string A String
     * @return A String correctly formatted for insertion in a Json text.
     */
/**
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

    /**
     * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
     * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
     * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
     *
     * @param string A String
     * @param writer Writer
     * @return A String correctly formatted for insertion in a Json text.
     * @throws IOException
     */
/**
    public static Writer quote(String string, Writer writer) throws IOException {
        if (StringCommand.isEmpty(string)) {
            writer.write("\"\"");
            return writer;
        }

        char b; // back char
        char c = 0; // current char
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

    /**
     * 在需要的时候包装对象<br>
     * 包装包括：
     * <ul>
     * <li><code>null</code> -> <code>JsonNull.NULL</code></li>
     * <li>array or collection -> JsonArray</li>
     * <li>beanMapWithHashSetValue -> JsonObject</li>
     * <li>standard property (Double, String, et al) -> 原对象</li>
     * <li>来自于java包 -> 字符串</li>
     * <li>其它 -> 尝试包装为JSONObject，否则返回<code>null</code></li>
     * </ul>
     *
     * @param object The object to wrap
     * @return The wrapped value
     */
/**
    public static Object wrap(Object object) {
        try {
            if (object == null) {
                return NullJson.NULL;
            }
            if (object instanceof Json
                || NullJson.NULL.equals(object)
                || object instanceof StringJson
                || object instanceof CharSequence
                || object instanceof Number
                || ObjectCommand.isBasicType(object)) {
                return object;
            }

            if (object instanceof Collection) {
                Collection<?> coll = (Collection<?>) object;
                return new ListJson(coll);
            }
            if (ArrayCommand.isArray(object)) {
                return new ListJson(object);
            }
            if (object instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) object;
                return new MapJson(map);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.")
                || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new MapJson(object);
        } catch (Exception exception) {
            return null;
        }
    }

    public static com.google.gson.JsonPrimitive replace(Map map, JsonPrimitive jsonPrimitive) {
        JsonPrimitive data = null;
        if (jsonPrimitive.isString()) {
            String key = jsonPrimitive.getAsString();
            if (map.containsKey(key)) {
                data = new JsonPrimitive(map.get(jsonPrimitive.getAsString()).toString());
            }
        } else {
            String key = jsonPrimitive.getAsString();
            if (map.containsKey(key)) {
                data = new JsonPrimitive(map.get(jsonPrimitive.toString()).toString());
            }
        }
        return data;
    }

    public static com.google.gson.JsonObject replace(YmlNode ymlNode, JsonElement jsonElement, String to,
                                                     com.google.gson.JsonObject data) {
        try {
            Map map = ymlNode.map();
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive temp = jsonElement.getAsJsonPrimitive();
                data.add(to, replace(map, temp));
            } else if (jsonElement.isJsonArray()) {
                com.google.gson.JsonArray temp = jsonElement.getAsJsonArray();
                com.google.gson.JsonArray newTemp = new com.google.gson.JsonArray();
                temp.forEach(element -> {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive cache = element.getAsJsonPrimitive();
                        JsonPrimitive newCache = replace(map, cache);
                        if (newCache != null) {
                            newTemp.add(newCache);
                        } else {
                            newTemp.add(cache);
                        }
                    }
                });
                data.add(to, newTemp);
            } else if (jsonElement == null || jsonElement.isJsonNull()) {
                logger.debug("skip null field about element", data);
            } else {
                logger.error("could not hand replace operation. about:{}", jsonElement);
            }

        } catch (Exception e) {
            String string = ymlNode.string();
            if (string != null) {
                data.addProperty(to, string);
            }
        }

        return data;
    }

    public static com.google.gson.JsonArray arrayFormat(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        if (ymlNode.get("source").list() != null) {
            List list = ymlNode.get("source").list();
            list.forEach(s -> {
                try {
                    com.google.gson.JsonArray array = (com.google.gson.JsonArray) jsonObject.get(s.toString());
                    array.forEach(element -> {
                        try {
                            com.google.gson.JsonObject data = format(ymlNode, (com.google.gson.JsonObject) element);
                            jsonArray.add(data);
                        } catch (Exception e) {
                            logger.error("should be a jsonObject in {} JsonArray", s, e);
                        }
                    });
                } catch (Exception e) {
                    logger.error("must be JsonArray format.", e);
                }
            });
            return jsonArray;
        } else {
            logger.error("should input a array refer about jsonobject");
            return null;
        }
    }

    public static com.google.gson.JsonObject format(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonObject messageData = new com.google.gson.JsonObject();
        com.google.gson.JsonObject source;
        Map scheme = ymlNode.get("data").map();
        Map map = ymlNode.get("source").map();
        if (map != null && map.size() > 0) {
            source = new com.google.gson.JsonObject();
            map.forEach((key, value) -> {
                if (key.equals(value)) {
                    com.google.gson.JsonElement tempSource = jsonObject.get(key.toString());
                    if (tempSource.isJsonObject()) {
                        com.google.gson.JsonObject referSource = (com.google.gson.JsonObject) tempSource;
                        referSource.entrySet().forEach((json) -> {
                            source.add(json.getKey(), json.getValue());
                        });
                    }
                } else {
                    source.add(value.toString(), jsonObject.get(key.toString()));
                }
            });
        } else {
            source = jsonObject;
        }
        scheme.forEach((key, value) -> {
            if (value instanceof String) {
                Object oo = getEntity(source, value.toString());
                if (oo != null) {
                    messageData.addProperty(key.toString(), oo.toString());
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            } else if (value instanceof List) {
                com.google.gson.JsonArray array = (com.google.gson.JsonArray) getEntity(source, value);
                if (array != null && array.size() > 0) {
                    messageData.add(key.toString(), array);
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            } else if (value instanceof Map) {
                com.google.gson.JsonObject object = (com.google.gson.JsonObject) getEntity(source, value);
                if (object != null && object.size() > 0) {
                    messageData.add(key.toString(), object);
                } else {
                    messageData.add(key.toString(), com.google.gson.JsonNull.INSTANCE);
                }
            }
        });
        map = ymlNode.get("mapData").map();
        if (map != null && map.size() > 0) {
            List mapScheme = ymlNode.get("mapScheme").list();
            mapScheme.forEach(s -> {
                JsonElement jsonElement = messageData.get(s.toString());
                YmlNode newValue = ymlNode.get("mapData").get(s.toString());
                replace(newValue, jsonElement, s.toString(), messageData);
            });
        }
        List listScheme = ymlNode.get("set").list();
        if (listScheme != null && listScheme.size() > 0) {
            listScheme.forEach(l -> {
                JsonElement jsonElement = messageData.get(l.toString());
                if (jsonElement != null) {
                    HashSet<JsonPrimitive> data = new HashSet<>();
                    if (jsonElement.isJsonArray()) {
                        jsonElement.getAsJsonArray().forEach(e -> {
                            if (e.isJsonPrimitive()) {
                                data.add(e.getAsJsonPrimitive());
                            }
                        });
                        if (data.size() > 0) {
                            try {
                                messageData.remove(l.toString());
                                com.google.gson.JsonArray covert = new com.google.gson.JsonArray();
                                data.forEach(value -> {
                                    covert.add(value);
                                });
                                messageData.add(l.toString(), covert);
                            } catch (Exception e) {
                                messageData.add(l.toString(), jsonElement);
                                logger.debug("could not set the field. reset the value to default.");
                            }

                        }
                    }
                }
            });
        }
        return messageData;
    }

    private static JsonElement getJsonEntity(com.google.gson.JsonObject data, String key) {
        JsonElement oo = null;
        if (data.has(key)) {
            if (data.get(key).isJsonPrimitive()) {
                oo = data.get(key).getAsJsonPrimitive();
            } else if (data.get(key).isJsonArray()) {
                oo = data.get(key).getAsJsonArray();
            } else if (data.get(key).isJsonObject()) {
                oo = data.get(key).getAsJsonObject();
            } else {
                oo = null;
            }
        } else {
            oo = com.google.gson.JsonNull.INSTANCE;
        }
        return oo;
    }

    private static Object getEntity(com.google.gson.JsonObject data, Object key) {
        Object oo;
        if (key instanceof String) {
            if (data.has(key.toString())) {
                if (data.get(key.toString()).isJsonPrimitive()) {
                    JsonPrimitive jsonPrimitive = data.get(key.toString()).getAsJsonPrimitive();
                    if (jsonPrimitive.isString()) {
                        oo = jsonPrimitive.getAsString();
                    } else if (jsonPrimitive.isBoolean()) {
                        oo = jsonPrimitive.getAsBoolean();
                    } else if (jsonPrimitive.isNumber()) {
                        oo = jsonPrimitive.getAsNumber();
                    } else {
                        oo = jsonPrimitive.getAsString();
                    }
                } else if (data.get(key.toString()).isJsonArray()) {
                    com.google.gson.JsonArray jsonElements = data.get(key.toString()).getAsJsonArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    jsonElements.forEach(element -> {
                        stringBuilder.append(element.toString());
                    });
                    oo = stringBuilder.toString();
                } else if (data.get(key.toString()).isJsonObject()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    com.google.gson.JsonObject jsonObject = data.get(key.toString()).getAsJsonObject();
                    jsonObject.entrySet().forEach(entry -> {
                        stringBuilder.append(getEntity(data, entry.getKey()));
                    });
                    oo = stringBuilder.toString();
                } else {
                    oo = null;
                }
            } else {
                oo = null;
            }
        } else if (key instanceof List) {
            com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
            List list = (List) key;
            list.forEach(l -> {
                if (l instanceof String) {
                    JsonElement ooo = getJsonEntity(data, l.toString());
                    if (ooo instanceof com.google.gson.JsonArray) {
                        jsonArray.addAll((com.google.gson.JsonArray) ooo);
                    } else {
                        jsonArray.add(ooo);
                    }

                } else {
                    Object ooo = getEntity(data, l);
                    if (ooo != null) {
                        if (ooo instanceof JsonElement) {
                            jsonArray.add((JsonElement) ooo);
                        } else {
                            jsonArray.add(ooo.toString());
                        }
                    }
                }
            });
            oo = jsonArray;
        } else if (key instanceof Map) {
            com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
            Map map = (Map) key;
            map.forEach((k, v) -> {
                if (v instanceof String) {
                    JsonElement ooo = getJsonEntity(data, v.toString());
                    jsonObject.add(k.toString(), ooo);
                } else {
                    Object ooo = getEntity(data, v);
                    jsonObject.addProperty(k.toString(), ooo.toString());
                }
            });
            oo = jsonObject;
        } else {
            logger.warn("did not support this type:{} for yml config in the map config file.", key);
            return null;
        }
        return oo;
    }

    public static com.google.gson.JsonArray formatToArray(YmlNode ymlNode, com.google.gson.JsonObject jsonObject) {
        com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
        String index = ymlNode.get("index").string();
        if (index == null) {
            logger.error("config error could not covert the data to array.");
        } else {
            try {
                int size = jsonObject.get(index).getAsJsonArray().size();
                for (int i = 0; i < size; i++) {
                    com.google.gson.JsonObject covert = new com.google.gson.JsonObject();
                    List scheme = ymlNode.get("scheme").list();
                    int[] j = new int[1];
                    j[0] = i;
                    scheme.forEach(s -> {
                        if (jsonObject.has(s.toString()) && jsonObject.get(s.toString()) != null) {
                            if (jsonObject.get(s.toString()).isJsonArray()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonArray().get(j[0]));
                            } else if (jsonObject.get(s.toString()).isJsonPrimitive()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonPrimitive());
                            } else if (jsonObject.get(s.toString()).isJsonObject()) {
                                covert.add(s.toString(), jsonObject.get(s.toString()).getAsJsonObject());
                            } else {
                                covert.add(s.toString(), com.google.gson.JsonNull.INSTANCE);
                            }
                        } else {
                            covert.add(s.toString(), null);
                        }
                    });
                    com.google.gson.JsonObject result = format(ymlNode, covert);
                    jsonArray.add(result);
                }
            } catch (Exception e) {
                logger.error("type not correct {}. cause by {}", jsonObject, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public static com.google.gson.JsonObject merge(com.google.gson.JsonObject left, com.google.gson.JsonObject right) {
        Set<Map.Entry<String, JsonElement>> ss = right.entrySet();
        for (Map.Entry<String, JsonElement> sss : ss) {
            if (left.has(sss.getKey())) {
                throw new UnsupportedOperationException(
                    "left jsonObject and right jsonObject has conflict key:" + sss.getKey());
            }
            left.add(sss.getKey(), sss.getValue());
        }
        return left;
    }

    public static com.google.gson.JsonObject mergeInLeft(com.google.gson.JsonObject left,
                                                         com.google.gson.JsonObject right) {
        Set<Map.Entry<String, JsonElement>> ss = right.entrySet();
        for (Map.Entry<String, JsonElement> sss : ss) {
            if (left.has(sss.getKey())) {
                continue;
            }
            left.add(sss.getKey(), sss.getValue());
        }
        return left;
    } **/




/**
 * 持有原始数据的List
 */
/**
private final ArrayList<Object> rawArrayList;

// --------------------------------------------------------------------------------------------------------------------
// Constructor start

/**
 * 构造
 */
/**
public JsonArray() {
    this.rawArrayList = new ArrayList<Object>();
    }

/**
 * 使用 {@link JsonTokener} 做为参数构造
 *
 * @param x A {@link JsonTokener}
 * @throws JsonException If there is a syntax error.
 */
/**
public JsonArray(JsonTokener x) throws JsonException {
    this();
    if (x.nextClean() != '[') {
    throw x.syntaxError("A JsonArray text must start with '['");
    }
    if (x.nextClean() != ']') {
    x.back();
    for (; ; ) {
    if (x.nextClean() == ',') {
    x.back();
    this.rawArrayList.add(JsonNull.NULL);
    } else {
    x.back();
    this.rawArrayList.add(x.nextValue());
    }
    switch (x.nextClean()) {
    case ',':
    if (x.nextClean() == ']') {
    return;
    }
    x.back();
    break;
    case ']':
    return;
default:
    throw x.syntaxError("Expected a ',' or ']'");
    }
    }
    }
    }

/**
 * 从String构造（JSONArray字符串）
 *
 * @param source JSON数组字符串
 * @throws JsonException If there is a syntax error.
 */
/**
public JsonArray(String source) throws JsonException {
    this(new JsonTokener(source));
    }

/**
 * 从数组或{@link Collection}对象构造
 *
 * @throws JsonException 非数组或集合
 */
/**
public JsonArray(Object arrayOrCollection) throws JsonException {
    this();
    if (arrayOrCollection.getClass().isArray()) {
    // 数组
    int length = Array.getLength(arrayOrCollection);
    for (int i = 0; i < length; i += 1) {
    this.put(JsonCommand.wrap(Array.get(arrayOrCollection, i)));
    }
    } else if (arrayOrCollection instanceof Iterable<?>) {
    // Iterable
    for (Object o : (Collection<?>) arrayOrCollection) {
    this.add(o);
    }
    } else {
    throw new JsonException("JsonArray initial value should be a string or collection or array.");
    }
    }
// --------------------------------------------------------------------------------------------------------------------
// Constructor start

/**
 * 值是否为<code>null</code>
 *
 * @param index 值所在序列
 * @return true if the value at the index is null, or if there is no value.
 */
/**
public boolean isNull(int index) {
    return JsonNull.NULL.equals(this.getObj(index));
    }

/**
 * JSONArray转为以<code>separator</code>为分界符的字符串
 *
 * @param separator 分界符
 * @return a string.
 * @throws JsonException If the array contains an invalid number.
 */
/**
public String join(String separator) throws JsonException {
    int len = this.rawArrayList.size();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i += 1) {
    if (i > 0) {
    sb.append(separator);
    }
    sb.append(InternalJsonUtil.valueToString(this.rawArrayList.get(i)));
    }
    return sb.toString();
    }

@Override
public Object getObj(Integer index, Object defaultValue) {
    return (index < 0 || index >= this.size()) ? defaultValue : this.rawArrayList.get(index);
    }

/**
 * Append an object value. This increases the array's length by one. 加入元素，数组长度+1，等同于 {@link JsonArray#add(Object)}
 *
 * @param value 值，可以是： Boolean, Double, Integer, JsonArray, JsonObject, Long, or String, or the JsonNull.NULL。
 * @return this.
 */
/**
public JsonArray put(Object value) {
    this.add(value);
    return this;
    }

/**
 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
 *
 * @param index 位置
 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JsonArray, JsonObject, Long, String, or the JsonNull.NULL.
 * @return this.
 * @throws JsonException index < 0 或者非有限的数字
 */
/**
public JsonArray put(int index, Object value) throws JsonException {
    this.add(index, value);
    return this;
    }

/**
 * 是否与其它对象相等，其它对象也必须是JSONArray，对象元素相等且元素顺序一致
 *
 * @param other The other JsonArray
 * @return true if they are equal
 */
/**
@Override
public boolean equals(Object other) {
    if (!(other instanceof JsonArray)) {
    return false;
    }
    int len = this.size();
    if (len != ((JsonArray) other).size()) {
    return false;
    }
    for (int i = 0; i < len; i += 1) {
    Object valueThis = this.getObj(i);
    Object valueOther = ((JsonArray) other).getObj(i);
    if (valueThis instanceof JsonObject) {
    if (!((JsonObject) valueThis).equals(valueOther)) {
    return false;
    }
    } else if (valueThis instanceof JsonArray) {
    if (!((JsonArray) valueThis).equals(valueOther)) {
    return false;
    }
    } else if (!valueThis.equals(valueOther)) {
    return false;
    }
    }
    return true;
    }

/**
 * 转为JSON字符串，无缩进
 *
 * @return JSONArray字符串
 */
/**
@Override
public String toString() {
    try {
    return this.toJSONString(0);
    } catch (Exception e) {
    return null;
    }
    }

/**
 * 根据给定名列表，与其位置对应的值组成JSONObject
 *
 * @param names 名列表，位置与JSONArray中的值位置对应
 * @return A JsonObject，无名或值返回null
 * @throws JsonException 如果任何一个名为null
 */
/**
public JsonObject toJSONObject(JsonArray names) throws JsonException {
    if (names == null || names.size() == 0 || this.size() == 0) {
    return null;
    }
    JsonObject jo = new JsonObject();
    for (int i = 0; i < names.size(); i += 1) {
    jo.put(names.getStr(i), this.getObj(i));
    }
    return jo;
    }

@Override
public Writer write(Writer writer) throws JsonException {
    return this.write(writer, 0, 0);
    }

@Override
public Writer write(Writer writer, int indentFactor, int indent) throws JsonException {
    try {
    boolean commanate = false;
    int length = this.size();
    writer.write('[');

    if (length == 1) {
    InternalJsonUtil.writeValue(writer, this.rawArrayList.get(0), indentFactor, indent);
    } else if (length != 0) {
final int newindent = indent + indentFactor;

    for (int i = 0; i < length; i += 1) {
    if (commanate) {
    writer.write(',');
    }
    if (indentFactor > 0) {
    writer.write('\n');
    }
    InternalJsonUtil.indent(writer, newindent);
    InternalJsonUtil.writeValue(writer, this.rawArrayList.get(i), indentFactor, newindent);
    commanate = true;
    }
    if (indentFactor > 0) {
    writer.write('\n');
    }
    InternalJsonUtil.indent(writer, indent);
    }
    writer.write(']');
    return writer;
    } catch (IOException e) {
    throw new JsonException(e);
    }
    }

/**
 * 转为JSON字符串，指定缩进值
 *
 * @param indentFactor 缩进值，既缩进空格数
 * @return JSON字符串
 * @throws JsonException
 */
/**
@Override
public String toJSONString(int indentFactor) throws JsonException {
    StringWriter sw = new StringWriter();
synchronized (sw.getBuffer()) {
    return this.write(sw, indentFactor, 0).toString();
    }
    }

@Override
public int size() {
    return rawArrayList.size();
    }

@Override
public boolean isEmpty() {
    return rawArrayList.isEmpty();
    }

@Override
public boolean contains(Object o) {
    return rawArrayList.contains(o);
    }

@Override
public Iterator<Object> iterator() {
    return rawArrayList.iterator();
    }

@Override
public Object[] toArray() {
    return rawArrayList.toArray();
    }

@Override
public <T> T[] toArray(T[] a) {
    return rawArrayList.toArray(a);
    }

@Override
public boolean add(Object e) {
    return this.rawArrayList.add(JsonCommand.wrap(e));
    }

@Override
public boolean remove(Object o) {
    return rawArrayList.remove(o);
    }

@Override
public boolean containsAll(Collection<?> c) {
    return rawArrayList.containsAll(c);
    }

@Override
public boolean addAll(Collection<? extends Object> c) {
    for (Object obj : c) {
    this.add(obj);
    }
    return rawArrayList.addAll(c);
    }

@Override
public boolean addAll(int index, Collection<? extends Object> c) {
    return rawArrayList.addAll(index, c);
    }

@Override
public boolean removeAll(Collection<?> c) {
    return this.rawArrayList.removeAll(c);
    }

@Override
public boolean retainAll(Collection<?> c) {
    return this.rawArrayList.retainAll(c);
    }

@Override
public void clear() {
    this.rawArrayList.clear();

    }

@Override
public Object get(int index) {
    return this.rawArrayList.get(index);
    }

@Override
public Object set(int index, Object element) {
    return this.rawArrayList.set(index, JsonCommand.wrap(element));
    }

@Override
public void add(int index, Object element) {
    if (index < 0) {
    throw new JsonException("JsonArray[" + index + "] not found.");
    }
    if (index < this.size()) {
    InternalJsonUtil.testValidity(element);
    this.rawArrayList.set(index, JsonCommand.wrap(element));
    } else {
    while (index != this.size()) {
    this.add(JsonNull.NULL);
    }
    this.put(element);
    }

    }

@Override
public Object remove(int index) {
    return index >= 0 && index < this.size() ? this.rawArrayList.remove(index) : null;
    }

@Override
public int indexOf(Object o) {
    return this.rawArrayList.indexOf(o);
    }

@Override
public int lastIndexOf(Object o) {
    return this.rawArrayList.lastIndexOf(o);
    }

@Override
public ListIterator<Object> listIterator() {
    return this.rawArrayList.listIterator();
    }

@Override
public ListIterator<Object> listIterator(int index) {
    return this.rawArrayList.listIterator(index);
    }


@Override
public List<Object> subList(int fromIndex, int toIndex) {
    return this.rawArrayList.subList(fromIndex, toIndex);
    } **/
