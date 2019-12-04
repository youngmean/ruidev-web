package com.ruidev.framework.util;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

/**
 * Object XML 转换工具
 * @author 明
 *
 */
public class JaxbUtil {

	/**
	 * 将List转换为xml
	 * @param objects
	 * @return
	 * @throws Exception 
	 */
	public static String toXML(List<?> objects, String root, String elementRoot, boolean containXmlDeclare, boolean containRoot) throws Exception{
		if(StringUtils.isEmpty(root)){
			root = "objects";
		}
		if(StringUtils.isEmpty(elementRoot)){
			elementRoot = "object";
		}
		StringBuffer buffer = new StringBuffer("");
		if(containRoot){
			if(containXmlDeclare){
				buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			}
			buffer.append("<").append(root).append(">");
		}
		for(Object obj : objects){
			String objType = obj.getClass().getSimpleName().toLowerCase();
			buffer.append("<").append(objType).append(">");
			buffer.append(toXML(obj, root, false, false));
			buffer.append("</").append(objType).append(">");
		}
		if(containRoot){
			buffer.append("</").append(root).append(">");
		}
		return buffer.toString();
	}
	
	/**
	 * 将Map转换为xml
	 * @param objects
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public static String toXML(Map<?, ?> map, String root, boolean containXmlDeclare, boolean containRoot) throws Exception{
		if(StringUtils.isEmpty(root)){
			root = "result";
		}
		StringBuffer buffer = new StringBuffer("");
		if(containRoot){
			if(containXmlDeclare){
				buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			}
			buffer.append("<").append(root).append(">");
		}
		Set<?> keys = map.keySet();
		for(Object key : keys){
			Object obj = map.get(key);
			if(obj == null || key == null){
				continue;
			}
			if(obj instanceof List){
				buffer.append(toXML((List)obj, key.toString(), "object", false, true));
			}else{
				buffer.append(toXML(obj, key.toString(), false, true));
			}
		}
		if(containRoot){
			buffer.append("</").append(root).append(">");
		}
		return buffer.toString();
	}
	
	/**
	 * 对象转化为xml
	 * @param obj
	 * @param root
	 * @param containXmlDeclare
	 * @param containRoot
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String toXML(Object obj, String root, boolean containXmlDeclare, boolean containRoot) throws Exception{
		if(StringUtils.isEmpty(root)){
			root = "object";
		}
		StringBuffer buffer = new StringBuffer("");
		if(obj instanceof String || obj instanceof Double || obj instanceof Integer){
			if(containRoot){
				if(containXmlDeclare){
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				}
				buffer.append("<").append(root).append(">");
			}
			buffer.append(obj.toString());
			if(containRoot){
				buffer.append("</").append(root).append(">");
			}
			return buffer.toString();
		}else if(obj instanceof Map){
			if(containRoot){
				if(containXmlDeclare){
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				}
				buffer.append("<").append(root).append(">");
			}
			Map map = (Map)obj;
			for(Object key : map.keySet()){
				buffer.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
			}
			if(containRoot){
				buffer.append("</").append(root).append(">");
			}
			return buffer.toString();
		}
		ByteArrayOutputStream baos;
		JAXBElement<?> element = null;
		JAXBContext ctx = null;
		Marshaller m = null;
		baos = new ByteArrayOutputStream();
		ctx = JAXBContext.newInstance(obj.getClass());
		m = ctx.createMarshaller();
		
		/**
		if(obj instanceof CrudEntity){
			CrudEntity entity = (CrudEntity) obj;
			entity.setUser(null);
			entity.setUpdater(null);
		}
		**/
		element = new JAXBElement(new QName("", root), obj.getClass(), obj);
		try {
			m.marshal(element, baos);
		} catch (MarshalException e) {
			e.printStackTrace();
			return "";
		}
		String _result = baos.toString("UTF-8");
		if(!containRoot){
			_result = _result.substring(_result.indexOf(">")+1);
			int len = root.length() + 2;
			_result = _result.substring(len, _result.length() - len - 1);
		}else if(!containXmlDeclare){
			_result = _result.substring(_result.indexOf(">")+1);
		}
		buffer.append(_result);
		return buffer.toString();
	}
}
