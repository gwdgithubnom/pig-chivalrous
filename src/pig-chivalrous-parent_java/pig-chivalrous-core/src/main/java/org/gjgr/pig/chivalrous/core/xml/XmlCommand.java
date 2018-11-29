package org.gjgr.pig.chivalrous.core.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlCommand {

    private static Logger logger = LoggerFactory.getLogger(XmlCommand.class);

    /**
     * 将对象直接转换成String类型的 XML输出
     *
     * @param obj
     * @return
     */

    public static String convertToXml(Object obj) {
        // 创建输出流
        StringWriter sw = new StringWriter();
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());

            Marshaller marshaller = context.createMarshaller();
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            // 将对象转换成输出流形式的xml
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    /**
     * 将对象根据路径转换成xml文件
     *
     * @param obj
     * @param path
     * @return
     */
    public static void convertToXml(Object obj, String path) {
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());

            Marshaller marshaller = context.createMarshaller();
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            // 将对象转换成输出流形式的xml
            // 创建输出流
            //	FileWriter fw = null;
            OutputStreamWriter isr = null;
            try {
                //	fw = new FileWriter(path);
                isr = new OutputStreamWriter(new FileOutputStream(path),
                        "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.debug("covert Object:{} to xml:{} success. ", obj.getClass(), path);
            marshaller.marshal(obj, isr);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * 将String类型的xml转换成对象
     */
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
            logger.error("JAXBException did not covert success. {}", clazz);
            return null;
        }
        logger.debug("covert xml str  to object success.{}", clazz);
        return xmlObject;
    }

    public static Object convertStreamFileToObject(Class clazz, InputStream inputStream) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FileReader fr = null;
            InputStreamReader isr = null;
            try {
                // fr = new FileReader(xmlPath);
                isr = new InputStreamReader(inputStream, "UTF-8");
                xmlObject = unmarshaller.unmarshal(isr);
            } catch (UnsupportedEncodingException e) {
                logger.error("the Encode does not isExist!" + e.getMessage());
                return null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error("the file does not isExist!" + e.getMessage());
                return null;
            }

        } catch (JAXBException e) {
            e.printStackTrace();
            logger.error("covert xml to object fail.");
            return null;
        }
        if (xmlObject == null) {
            logger.info("Xml did not newJson succss.{},{}", inputStream, clazz);
        } else {
            logger.debug("Xml:{} to object:{} success.", inputStream, clazz);
        }
        //	System.out.println("this is the content xml正文内容:");
        //System.out.println(XmlCommand.convertToXml(xmlObject));
        return xmlObject;
    }

    @SuppressWarnings("unchecked")
    /**
     * 将file类型的xml转换成对象
     */
    public static Object convertXmlFileToObject(Class clazz, String xmlPath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(xmlPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return convertStreamFileToObject(clazz, fileInputStream);
    }

    /**
     * 获取XStream对象
     *
     * @return
     */
    public static XStream getXStream() {
        //在文本前后加上<![CDATA[和]]>
        DomDriver domDriver = new DomDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (text.startsWith("<![CDATA[") && text.endsWith("]]>")) {
                            writer.write(text);
                        } else {
                            //super.writeText(writer, text);
                            super.writeText(writer, "<![CDATA[" + text + "]]>");
                        }
                    }
                };
            }

            ;
        };

        //去除XML属性在JavaBean中映射不到属性值的异常
        XStream xStream = new XStream(domDriver) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    @SuppressWarnings("rawtypes")
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return this.realClass(fieldName) != null;
                            } catch (Exception e) {
                                return false;
                            }
                        } else {
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    }
                };
            }
        };

        return xStream;
    }

    /**
     * 获取xml一级节点文本值，不区分元素名称大小写
     *
     * @param xml type:String
     * @param elementName type:String
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getStairText(String xml, String elementName) {
        elementName = elementName.toLowerCase();
        String result = null;
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            for (Iterator iterTemp = root.elementIterator(); iterTemp.hasNext(); ) {
                Element element = (Element) iterTemp.next();
                if (element.getName().toLowerCase().equals(elementName)) {
                    result = element.getText();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把xml转bean对象
     *
     * @param xml
     * @param map
     * @return
     */
    public static Object xmlToBean(String xml, Map<String, Class<?>> map) {
        XStream xStream = getXStream();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            xStream.alias(key, map.get(key));
        }
        return xStream.fromXML(xml);
    }

    /**
     * bean对象转xml
     *
     * @param bean
     * @param rootClass 根节点名称转换
     * @return
     */
    public static String beanToXml(Object bean, Class<?> rootClass) {
        XStream xStream = getXStream();
        xStream.alias("xml", rootClass);
        String content = xStream.toXML(bean);
        content = content.replaceAll("&lt;", "<");// <
        content = content.replaceAll("&gt;", ">");// >
        return content;
    }
}
