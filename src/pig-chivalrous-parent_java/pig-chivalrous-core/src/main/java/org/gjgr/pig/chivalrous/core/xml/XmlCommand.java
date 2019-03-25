package org.gjgr.pig.chivalrous.core.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.io.IoCommand;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.nio.CharsetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XmlCommand {

    /**
     * 在XML中无效的字符 正则
     */
    public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
    private static Logger logger = LoggerFactory.getLogger(XmlCommand.class);

    // -------------------------------------------------------------------------------------- Read

    /**
     * 读取解析XML文件
     *
     * @param file XML文件
     * @return XML文档对象
     */
    public static org.w3c.dom.Document readXML(File file) {
        if (file == null) {
            throw new NullPointerException("Xml file is null !");
        }
        if (false == file.exists()) {
            throw new UtilException("File [{}] not a isExist!", file.getAbsolutePath());
        }
        if (false == file.isFile()) {
            throw new UtilException("[{}] not a file!", file.getAbsolutePath());
        }

        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
        }

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(file);
        } catch (Exception e) {
            throw new UtilException("Parse xml file [" + file.getAbsolutePath() + "] error!", e);
        }
    }

    /**
     * 读取解析XML文件
     *
     * @param absoluteFilePath XML文件绝对路径
     * @return XML文档对象
     */
    public static org.w3c.dom.Document readXML(String absoluteFilePath) {
        return readXML(new File(absoluteFilePath));
    }

    /**
     * 将String类型的XML转换为XML文档
     *
     * @param xmlStr XML字符串
     * @return XML文档
     */
    public static org.w3c.dom.Document parseXml(String xmlStr) {
        if (StringCommand.isBlank(xmlStr)) {
            throw new UtilException("Xml content string is empty !");
        }
        xmlStr = cleanInvalid(xmlStr);

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(new InputSource(StringCommand.getReader(xmlStr)));
        } catch (Exception e) {
            throw new UtilException("Parse xml file [" + xmlStr + "] error!", e);
        }
    }

    // -------------------------------------------------------------------------------------- Write

    /**
     * 将XML文档转换为String
     *
     * @param doc XML文档
     * @return XML字符串
     */
    public static String toStr(org.w3c.dom.Document doc) {
        return toStr(doc, CharsetCommand.UTF_8);
    }

    /**
     * 将XML文档转换为String<br>
     * 此方法会修改Document中的字符集
     *
     * @param doc XML文档
     * @param charset 自定义XML的字符集
     * @return XML字符串
     */
    public static String toStr(org.w3c.dom.Document doc, String charset) {
        try {
            StringWriter writer = StringCommand.getWriter();

            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            throw new UtilException("Trans xml document to string error!", e);
        }
    }

    /**
     * 将XML文档写入到文件<br>
     * 使用Document中的编码
     *
     * @param doc XML文档
     * @param absolutePath 文件绝对路径，不存在会自动创建
     */
    public static void toFile(org.w3c.dom.Document doc, String absolutePath) {
        toFile(doc, absolutePath, null);
    }

    /**
     * 将XML文档写入到文件<br>
     *
     * @param doc XML文档
     * @param absolutePath 文件绝对路径，不存在会自动创建
     * @param charset 自定义XML文件的编码
     */
    public static void toFile(org.w3c.dom.Document doc, String absolutePath, String charset) {
        if (StringCommand.isBlank(charset)) {
            charset = doc.getXmlEncoding();
        }
        if (StringCommand.isBlank(charset)) {
            charset = CharsetCommand.UTF_8;
        }

        BufferedWriter writer = null;
        try {
            writer = FileCommand.bufferedWriter(absolutePath, charset, false);
            Source source = new DOMSource(doc);
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, new StreamResult(writer));
        } catch (Exception e) {
            throw new UtilException("Trans xml document to string error!", e);
        } finally {
            IoCommand.close(writer);
        }
    }

    // -------------------------------------------------------------------------------------- Create

    /**
     * 创建XML文档<br>
     * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码
     *
     * @param rootElementName 根节点名称
     * @return XML文档
     */
    public static org.w3c.dom.Document createXml(String rootElementName) {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new UtilException("Create xml document error!", e);
        }
        org.w3c.dom.Document doc = builder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));

        return doc;
    }

    // -------------------------------------------------------------------------------------- Function

    /**
     * 去除XML文本中的无效字符
     *
     * @param xmlContent XML文本
     * @return 当传入为null时返回null
     */
    public static String cleanInvalid(String xmlContent) {
        if (xmlContent == null) {
            return null;
        }
        return xmlContent.replaceAll(INVALID_REGEX, "");
    }

    /**
     * 根据节点名获得子节点列表
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点列表
     */
    public static List<org.w3c.dom.Element> getElements(org.w3c.dom.Element element, String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        return transElements(element, nodeList);
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点
     */
    public static org.w3c.dom.Element getElement(org.w3c.dom.Element element, String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList == null || nodeList.getLength() < 1) {
            return null;
        }
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            org.w3c.dom.Element childEle = (org.w3c.dom.Element) nodeList.item(i);
            if (childEle == null || childEle.getParentNode() == element) {
                return childEle;
            }
        }
        return null;
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点中的值
     */
    public static String elementText(org.w3c.dom.Element element, String tagName) {
        org.w3c.dom.Element child = getElement(element, tagName);
        return child == null ? null : child.getTextContent();
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点中的值
     */
    public static String elementText(org.w3c.dom.Element element, String tagName, String defaultValue) {
        org.w3c.dom.Element child = getElement(element, tagName);
        return child == null ? defaultValue : child.getTextContent();
    }

    /**
     * 将NodeList转换为Element列表
     *
     * @param nodeList NodeList
     * @return Element列表
     */
    public static List<org.w3c.dom.Element> transElements(NodeList nodeList) {
        return transElements(null, nodeList);
    }

    /**
     * 将NodeList转换为Element列表
     *
     * @param parentEle 父节点，如果指定将返回此节点的所有直接子节点，nul返回所有就节点
     * @param nodeList NodeList
     * @return Element列表
     */
    public static List<org.w3c.dom.Element> transElements(org.w3c.dom.Element parentEle, NodeList nodeList) {
        final ArrayList<org.w3c.dom.Element> elements = new ArrayList<org.w3c.dom.Element>();
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) nodeList.item(i);
            if (parentEle == null || element.getParentNode() == parentEle) {
                elements.add(element);
            }
        }

        return elements;
    }

    /**
     * 将可序列化的对象转换为XML写入文件，已经存在的文件将被覆盖<br>
     * Writes serializable object to a XML file. Existing file will be overwritten
     *
     * @param <T>
     * @param dest 目标文件
     * @param t 对象
     * @throws IOException
     */
    public static <T> void writeObjectAsXml(File dest, T t) throws IOException {
        FileOutputStream fos = null;
        XMLEncoder xmlenc = null;
        try {
            fos = new FileOutputStream(dest);
            xmlenc = new XMLEncoder(new BufferedOutputStream(fos));
            xmlenc.writeObject(t);
        } finally {
            IoCommand.close(fos);
            if (xmlenc != null) {
                xmlenc.close();
            }
        }
    }

    /**
     * 从XML中读取对象 Reads serialized object from the XML file.
     *
     * @param <T>
     * @param source XML文件
     * @return 对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromXml(File source) throws IOException {
        Object result = null;
        FileInputStream fis = null;
        XMLDecoder xmldec = null;
        try {
            fis = new FileInputStream(source);
            xmldec = new XMLDecoder(new BufferedInputStream(fis));
            result = xmldec.readObject();
        } finally {
            IoCommand.close(fis);
            if (xmldec != null) {
                xmldec.close();
            }
        }
        return (T) result;
    }

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
            // FileWriter fw = null;
            OutputStreamWriter isr = null;
            try {
                // fw = new FileWriter(path);
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
    public static <T> T convertXmlStrToObject(Class clazz, String xmlStr) {
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
        return (T) xmlObject;
    }

    public static <T> T convertStreamFileToObject(Class clazz, InputStream inputStream) {
        Object xmlObject = null;
        if (inputStream == null) {
            return null;
        }
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
                logger.error("convert fail!" + e.getMessage());
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
        // System.out.println("this is the content xml正文内容:");
        // System.out.println(XmlCommand.convertToXml(xmlObject));
        return (T) xmlObject;
    }

    @SuppressWarnings("unchecked")
    /**
     * 将file类型的xml转换成对象
     */
    public static <T> T convertXmlFileToObject(Class clazz, String xmlPath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(xmlPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return (T) convertStreamFileToObject(clazz, fileInputStream);
    }

    /**
     * 获取XStream对象
     *
     * @return
     */
    public static XStream getXStream() {
        // 在文本前后加上<![CDATA[和]]>
        DomDriver domDriver = new DomDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (text.startsWith("<![CDATA[") && text.endsWith("]]>")) {
                            writer.write(text);
                        } else {
                            // super.writeText(writer, text);
                            super.writeText(writer, "<![CDATA[" + text + "]]>");
                        }
                    }
                };
            }

            ;
        };

        // 去除XML属性在JavaBean中映射不到属性值的异常
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
            for (Iterator iterTemp = root.elementIterator(); iterTemp.hasNext();) {
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
