/*
 * package org.gjgr.tools.model;
 * 
 * public class RequestBeanUtil {
 * //---------------------------------------------------------------------------------------------
 * fillBeanWithRequestParam
 */
/**
 * ServletRequest 参数转Bean
 *
 * @param request ServletRequest
 * @param bean Bean
 * @param copyOptions 注入时的设置
 * @return Bean
 * @param request ServletRequest
 * @param bean Bean
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 *         <p>
 *         ServletRequest 参数转Bean
 * @param request ServletRequest
 * @param beanClass Bean Class
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 * @param request ServletRequest
 * @param bean Bean
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 *         <p>
 *         ServletRequest 参数转Bean
 * @param request ServletRequest
 * @param beanClass Bean Class
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 * @param request ServletRequest
 * @param bean Bean
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 *         <p>
 *         ServletRequest 参数转Bean
 * @param request ServletRequest
 * @param beanClass Bean Class
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 * @since 3.0.4
 *        <p>
 *        ServletRequest 参数转Bean
 *        <p>
 *        ServletRequest 参数转Bean
 *        <p>
 *        ServletRequest 参数转Bean
 *//*
    * 
    * public static <T> T fillBeanWithRequestParam(final javax.servlet.ServletRequest request, T bean, CopyOptions
    * copyOptions) { final String beanName = StringCommand.lowerFirst(bean.getClass().getSimpleName()); return
    * fillBean(bean, new ValueProvider<String>(){
    * 
    * @Override public Object value(String key, Class<?> valueType) { String value = request.getParameter(key); if
    * (StringCommand.isEmpty(value)) { // 使用类名前缀尝试查找值 value = request.getParameter(beanName + StringCommand.DOT + key);
    * if (StringCommand.isEmpty(value)) { // 此处取得的值为空时跳过，包括null和"" value = null; } } return value; }
    * 
    * @Override public boolean containsKey(String key) { //对于Servlet来说，返回值null意味着无此参数 return null !=
    * request.getParameter(key); } }, copyOptions); }
    * 
    */
/**
 * ServletRequest 参数转Bean
 *
 * @param request ServletRequest
 * @param bean Bean
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 *//*
    * 
    * public static <T> T fillBeanWithRequestParam(final javax.servlet.ServletRequest request, T bean, final boolean
    * isIgnoreError) { return fillBeanWithRequestParam(request, bean,
    * CopyOptions.create().setIgnoreError(isIgnoreError)); }
    * 
    */
/**
 * ServletRequest 参数转Bean
 *
 * @param request ServletRequest
 * @param beanClass Bean Class
 * @param isIgnoreError 是否忽略注入错误
 * @return Bean
 *//*
    * 
    * public static <T> T requestParamToBean(javax.servlet.ServletRequest request, Class<T> beanClass, boolean
    * isIgnoreError) { return fillBeanWithRequestParam(request, ClassCommand.newInstance(beanClass), isIgnoreError); }
    * 
    * //--------------------------------------------------------------------------------------------- fillBean
    * 
    * }
    */
