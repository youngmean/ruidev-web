package com.ruidev.framework.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.ruidev.framework.constant.BaseConstants;
import com.ruidev.framework.entity.AbsEntity;
import com.ruidev.framework.exception.SysException;

/**
 * 系统公用方法
 */
public class CommonUtil {

    private static final Logger log = LogManager.getLogger(CommonUtil.class);

    /**
     * 判断Collection是否为空
     * 
     * @param collection
     *            需要判断的Collection
     * @return boolean
     */
    public static boolean collectionIsEmpty(Collection<?> collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 将多string合一
     * @param strings
     * @return
     */
    public static String combineStrings(Object... objs) {
    	StringBuffer buffer = new StringBuffer();
    	for(Object obj : objs){
    		if(obj != null) {
    			buffer.append(obj.toString());
    		}
    	}
    	return buffer.toString();
    }
    
    /**
     * 转换字符串，如何是日期类型的字段，则转换成标准日期格式
     * 
     * @param 需要转换的对象
     * @return 转换字符串
     */
    public static String formatNumber(Object o, String pattern) {
        try {
            DecimalFormat df = new DecimalFormat(pattern);
            if (o != null) {
                return df.format(o);
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("格式化数字错误：" + pattern, e);
            return o.toString();
        }
    }

    /**
     * whether source separated by separator contains the whole word
     * 
     * @param source
     * @param word
     * @param separator
     * @return
     */
    public static boolean containsWholeWord(String source, String word,
            String separator) {
        if (source == null || word == null || separator == null) {
            return false;
        }
        String newSource = source.toString()
                .replaceAll("(.*)" + separator + "$", "$1")
                .replaceAll("^" + separator + "(.*)", "$1");
        String result = newSource.replaceAll(word, "").replaceAll(
                separator + separator, separator);
        return newSource.equals(word)
                || result.startsWith(separator)
                || result.endsWith(separator)
                || source.indexOf(word) > -1
                && result.split(separator).length == newSource.split(separator).length - 1;
    }

    /**
     * 判断Map是否为空
     * 
     * @param map
     *            需要判断的Collection
     * @return boolean
     */
    public static boolean collectionIsEmpty(Map<?, ?> map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象数组是否为空
     * 
     * @param objs
     *            需要判断的对象数组
     * @return boolean
     */
    public static boolean arrayIsEmpty(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 读取一个属性文件
     * 
     * @param propName
     *            属性文件名称
     * @return 返回属性文件
     */
    public static Properties getProperties(String propName) throws SysException {
        Properties p = null;
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(propName);
            if (in == null) {
                in = CommonUtil.class.getResourceAsStream(propName);
            }
            if (in == null) {
                in = CommonUtil.class.getClassLoader().getResourceAsStream(
                        propName);
            }
            p = getProperties(in);
        } catch (Exception ex) {
            log.error("文件不存在：", ex);
            throw new SysException(1);
        }
        return p;
    }

    /**
     * 读取一个属性文件
     * 
     * @param propName
     *            属性文件名称
     * @return 返回属性文件
     */
    public static Properties getPropertiesByContext(String propName)
            throws Exception {
        log.debug("加载属性文件：" + propName);
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream(propName);
        return getProperties(in);
    }

    /**
     * 读取一个属性文件
     * 
     * @param in
     *            文件流
     * @return 返回属性文件
     */
    private static Properties getProperties(InputStream in) throws Exception {
        Properties p = new Properties();
        if (in != null) {
            p.load(in);
            in.close();
        }
        log.debug("属性文件：" + p);
        return p;
    }

    /**
     * 替换路径当中的空格转义符号
     * 
     * @param p
     *            文件的物理路径名称
     * @return 物理路径名称
     */
    public static String replacePathSpace(String p) throws Exception {
        if (p != null && p.contains("%20")) {
            p = p.replaceAll("%20", " ");
        }
        return p;
    }
    
    /**
     * 按指定大小生成缩放图片并保存
     * @param file
     * @param width
     * @param height
     * @param targetFile
     * @throws Exception
     */
    public static void createScaleImgFile(File file, int width, int height, File targetFile) throws Exception {
        BufferedImage srcImage = ImageIO.read(file);  
        int hints = BufferedImage.TYPE_INT_RGB;
        String type = targetFile.getName().substring(targetFile.getName().lastIndexOf(".") + 1);
        if("png".equals(type)){
        	hints = BufferedImage.TYPE_4BYTE_ABGR_PRE;
        }
        int _width = srcImage.getWidth();
        int _height = srcImage.getHeight();
        int destWidth = width;
        int destHeight = height;
        Double scaleWidth = width / (_width * 1.0);
        if(scaleWidth * _height < destHeight){
        	Double scaleHeight = height / (_height * 1.0);
        	destWidth = (int) (scaleHeight * _width);
        }else{
        	destHeight = (int) (scaleWidth * _height);
        }
        Image destImage = srcImage.getScaledInstance(destWidth, destHeight, hints);  
        BufferedImage tag = new BufferedImage(width, height, hints);     
        Graphics2D g = tag.createGraphics();   
        g.drawImage(destImage, 0, 0, null);
        g.dispose();
        File dir = targetFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ImageIO.write(tag, type, targetFile);  
    }

    /**
     * 文件复制
     * 
     * @param srcFile
     * @param targetFile
     */
    public static void copyOrCreateFile(File srcFile, File targetFile) {
        try {
            InputStream in = null;
            OutputStream out = null;
            File dir = targetFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                in = new BufferedInputStream(new FileInputStream(srcFile),
                        BaseConstants.BUFFER_SIZE);
                out = new BufferedOutputStream(
                        new FileOutputStream(targetFile),
                        BaseConstants.BUFFER_SIZE);
                byte[] buffer = new byte[BaseConstants.BUFFER_SIZE];
                int len = in.read(buffer);
                while (len > 0) {
                    out.write(buffer, 0, len);
                    len = in.read(buffer);
                }
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
            log.info("Copy file to: " + targetFile.getAbsolutePath());
        } catch (Exception e) {
        	log.error("Copy file failed: "+e.getMessage() + "," + targetFile.getAbsolutePath());
        }
    }
    
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target, String ...ignoreProperties){
    	String[] fields = getNullPropertyNames(src);
    	int propsLen = ignoreProperties.length;
    	if(propsLen > 0){
    		int fieldsLen = fields.length;
    		String[] allFields = new String[fieldsLen + propsLen];
    		for(int i = 0,ilen = fieldsLen;i<ilen;i++){
    			allFields[i] = fields[i];
    		}
    		for(int i = 0,ilen = propsLen;i<ilen;i++){
    			allFields[fieldsLen + i] = ignoreProperties[i];
    		}
    		org.springframework.beans.BeanUtils.copyProperties(src, target, allFields);
    	}else{
    		org.springframework.beans.BeanUtils.copyProperties(src, target, fields);
    	}
    }
    
    public static Object copyPropertiesBatch(List srcList, List targetList, String ...properties) throws Exception{
    	if(srcList == null)return null;
    	if(targetList == null) {
    		targetList = new ArrayList<Object>(); 
    	}else {
    		targetList.clear();
    	}
    	for(Object src : srcList) {
    		targetList.add(copyProperties(src, null, properties));
    	}
    	return targetList;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object copyProperties(Object src, Object target, String ...properties) throws Exception{
    	Class<?> srcClass = src.getClass();
    	if(target == null) {
    		target = srcClass.newInstance();
    	}
    	if(properties.length == 0) {
    		org.springframework.beans.BeanUtils.copyProperties(src, target);
    		return target;
    	}
    	for(String prop : properties) {
    		if(StringUtils.isEmpty(prop.trim()))continue;
    		if(src instanceof Map) {
    			((Map)target).put(prop, ((Map)src).get(prop));
    			continue;
    		}
    		String methodName = CommonUtil.combineStrings(prop.substring(0, 1).toUpperCase(), prop.substring(1));
    		Method srcGet = null;
			try {
				srcGet = srcClass.getMethod(CommonUtil.combineStrings("get", methodName));
			} catch (Exception e) {
				log.error("Faile to copy prop: {}", e.getMessage());
			}
			if(srcGet == null)continue;
    		Object val = srcGet.invoke(src);
    		if(val != null) {
    			BeanUtils.copyProperty(target, prop, val);
    		}
    	}
    	return target;
    }

    /**
     * 生成图片缩略图
     * @param filePath
     * @param thumbPath
     * @param width
     * @param height
     * @param scaleMode 0:contain 1:stretch
     * @throws Exception
     */
    public static String toSmallImg(String filePath, String thumbPath, float width, float height, int scaleMode)
            throws Exception {
        String newurl = thumbPath;
        java.awt.Image bigJpg = javax.imageio.ImageIO.read(new java.io.File(
                filePath));
        float tagsize = width > height ? height : width;
        int old_w = bigJpg.getWidth(null);
        int old_h = bigJpg.getHeight(null);
        int new_w = 0;
        int new_h = 0;
        float tempdoubleW = 1;
        float tempdoubleH = 1;
        switch(scaleMode){
        case 0:
            tempdoubleW = tempdoubleH = old_w > old_h ? old_w / tagsize : old_h / tagsize;
            break;
        case 1:
            tempdoubleW = old_w / width;
            tempdoubleH = old_h / height;
        }
        new_w = Math.round(old_w / tempdoubleW);
        new_h = Math.round(old_h / tempdoubleH);
        java.awt.image.BufferedImage tag = new java.awt.image.BufferedImage(
                new_w, new_h, java.awt.image.BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(bigJpg, 0, 0, new_w, new_h, null);
        File newFile = new File(newurl);
        File dir = newFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream newimage = new FileOutputStream(newFile);
        com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.JPEGCodec
                .createJPEGEncoder(newimage);
        encoder.encode(tag);
        newimage.close();
        return thumbPath;
    }

    /**
     * 获取文件的后缀名
     * 
     * @param fileName
     * @return
     */
    public static String getExtentsName(String fileName) {
        if (fileName.contains(".")) {
            int pos = fileName.lastIndexOf(".");
            return fileName.substring(pos);
        }
        return "";
    }

    /**
     * 覆盖PropertyUtils.getProperty()方法，防止空指针异常
     * 
     * @param o
     * @param prop
     * @return
     */
    public static Object getProperty(Object o, String prop) {
        Object result = null;
        try {
            result = PropertyUtils.getProperty(o, prop);
        } catch (NestedNullException nne) {
            log.warn("属性：" + prop + "值为空", nne);
        } catch (Exception e) {
            log.warn("属性：" + prop + "转换出错！", e);
        }
        return result;
    }

    /**
     * 判断一个List中的对象数据是否有与传入的样本有不一样的
     * 
     * @param listValue
     *            需要判断的List ,sample 样本
     * @return true:相同,false:不同
     */
    public static boolean existValueInList(List<?> list, Object value) throws Exception {
        boolean mark = true;
        for (Object oo : list) {
            if (!oo.equals(value)) {
                mark = false;
                break;
            }
        }
        return mark;
    }

    /**
     * 如果List中不存在这个元素，则增加，反之不增加
     * 
     * @param list
     * @param obj
     * @return 增加了返回true，否则返回false
     */
    public static boolean addToListInNotExist(List<Object> list, Object obj) {
        if (list.contains(obj) || obj == null) {
            return false;
        } else {
            list.add(obj);
            return true;
        }
    }
    
    private static MessageDigest mdTemp;
    private static MessageDigest getMd5MessageDigest() throws Exception {
    	if(mdTemp == null) {
    		mdTemp = MessageDigest.getInstance("MD5");
    	}
    	return mdTemp;
    }

    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = getMd5MessageDigest();
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反射获取所有数据库字段和值,导出成标准xml格式 继承IdEntity
     * 
     * @param object
     * @param orderTag
     * @throws Exception
     */
    public static void createObjectXmlChildNodes(AbsEntity object,
            Element orderTag) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(
                BaseConstants.BASE_IMPEXP_DATE_FORMAT);
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Column.class)) {
                Column column = method.getAnnotation(Column.class);
                String dbColumn = column.name();
                Element dbElement = orderTag.addElement(dbColumn);
                Object o = method.invoke(object);
                String text = "";
                if (o != null) {
                    if (o instanceof Date) {
                        text = df.format(o);
                    } else {
                        text = o.toString();
                    }
                }
                dbElement.setText(text);
            }
        }
    }

    /**
     * 根据标准xml格式中数据库字段和值,给一个IdEntity赋值 继承IdEntity
     * 
     * @param object
     * @param orderTag
     * @throws Exception
     */
    public static void importObjectPropertiesByXml(AbsEntity object,
            Element parentNode, String[] ignoreProperties) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(
                BaseConstants.BASE_IMPEXP_DATE_FORMAT);
        List<String> ignorePros = new ArrayList<String>();
        if (object == null) {
            throw new Exception("赋值对象为空");
        }
        if (ignoreProperties != null) {
            for (String str : ignoreProperties) {
                ignorePros.add(str);
            }
        }
        List<Element> childNodes = parentNode.elements();
        Map<String, Method> colMethodMaps = createColumnKeyGetMethodValueMap(object);
        for (Element child : childNodes) {
            if (!ignorePros.contains(child.getName())
                    && colMethodMaps.containsKey(child.getName())) {
                Method getterMethod = colMethodMaps.get(child.getName());
                String setterName = "set" + getterMethod.getName().substring(3);
                Class<?> classType = getterMethod.getReturnType();
                Method setterMethod = object.getClass().getMethod(setterName,
                        classType);
                String childValue = child.getTextTrim();
                if (!StringUtils.isEmpty(childValue)) {
                    if (classType.toString().toLowerCase().contains("date")) {
                        setterMethod.invoke(object, df.parse(childValue));
                    } else if (classType.toString().toLowerCase()
                            .contains("long")) {
                        setterMethod.invoke(object, Long.valueOf(childValue));
                    } else if (classType.toString().toLowerCase()
                            .contains("double")) {
                        setterMethod.invoke(object, Double.valueOf(childValue));
                    } else {
                        setterMethod.invoke(object, childValue);
                    }
                }
            }
        }
    }

    /**
     * 获取IdEntity 数据库字段 和 get方法名对应的Map
     * 
     * @param object
     * @return
     */
    private static Map<String, Method> createColumnKeyGetMethodValueMap(
            AbsEntity object) {
        Map<String, Method> result = new HashMap<String, Method>();
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(Column.class)) {
                Column column = method.getAnnotation(Column.class);
                String dbColumn = column.name();
                result.put(dbColumn, method);
            }
        }
        return result;
    }

    /**
     * 判断两个对象中给定的属性值是否都相同
     * @param fstObj
     * @param scdObj
     * @param properties
     * @return
     * @throws Exception
     */
    public static boolean isPropertiesEquals(Object fstObj, Object scdObj,
            String... properties) throws Exception {
        if (fstObj == null || scdObj == null) {
            return false;
        }
        for (String property : properties) {
            Object fst = BeanUtils.getProperty(fstObj, property);
            Object scd = BeanUtils.getProperty(scdObj, property);
            if (fst == null) {
                if (scd != null) {
                    return false;
                }
            } else {
                if (!fst.equals(scd)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 判断两个对象中给定的属性值是否都相同
     * @param fstObj
     * @param scdObj
     * @param properties
     * @return
     * @throws Exception
     */
    public static boolean isPropertiesEquals(Object fstObj, Object scdObj,
            String properties) throws Exception {
        if (fstObj == null || scdObj == null) {
            return false;
        }
        String[] _properties = properties.split(",");
        for (String property : _properties) {
            Object fst = BeanUtils.getProperty(fstObj, property);
            Object scd = BeanUtils.getProperty(scdObj, property);
            if (fst == null) {
                if (scd != null) {
                    return false;
                }
            } else {
                if (!fst.equals(scd)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 数据库中读取的Object类型，如果不为null,转换成Double型并返回
     * 
     * @param object
     * @return
     */
    public static Double getDoubleValue(Object object) {
        if (object == null) {
            return 0.0;
        } else {
            if (StringUtils.isEmpty(object.toString())) {
                return 0.0;
            }
            return Double.valueOf(object.toString());
        }
    }

    /**
     * 数据库中读取的Object类型，如果不为null,转换成Long型并返回
     * 
     * @param object
     * @return
     */
    public static Long getLongValue(Object object) {
        if (object == null) {
            return null;
        } else {
            if (StringUtils.isEmpty(object.toString())) {
                return null;
            }
            String longStr = object.toString();
            if (longStr.contains(".")) {
                return Long.valueOf(longStr.substring(0, longStr.indexOf(".")));
            }
            return Long.valueOf(longStr);
        }
    }

    public static void main(String[] args) throws Exception {

    }

    /**
     * 获取真实ip
     * 
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
    	if (request == null)
			return "";
		String ip = request.getHeader("X-Requested-For");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
    }

    /**
     * 获取Excel读取的数字型字符
     * 
     * @param strObj
     * @return
     */
    public static String getExcelCharValue(String strObj) {
        if (strObj == null) {
            return strObj;
        }
        if (strObj.contains(".")) {
            while (strObj.endsWith("0")) {
                strObj = strObj.substring(0, strObj.length() - 1);
            }
            if (strObj.endsWith(".")) {
                strObj = strObj.substring(0, strObj.length() - 1);
            }
        }
        return strObj;
    }

    public static boolean writeFile(byte[] data, String dirPath, String name)
            throws IOException {
        if (data == null || name == null || name.equals("")) {
            return false;
        }
        File file = new File(dirPath + name);
        if(!file.getParentFile().exists()){
        	file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        if (data != null) {
            fos.write(data, 0, data.length);
            fos.close();
        }
        return true;
    }
    
    /*** 
     * 计算SHA1码 
     *  
     * @return String 适用于上G大的文件 
     * @throws NoSuchAlgorithmException 
     * */  
    public static String getFileSha1String(File file) throws OutOfMemoryError,  
            IOException, NoSuchAlgorithmException {  
        messagedigest = MessageDigest.getInstance("SHA-1");  
        FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
        messagedigest.update(byteBuffer);
        in.close();
        return bufferToHex(messagedigest.digest());  
    }  
    
    public static String getFileMD5String(File file) throws Exception {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
				file.length());
		messagedigest.update(byteBuffer);
		ch.close();
		in.close();
		return bufferToHex(messagedigest.digest());
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
	}
}
