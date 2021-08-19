package com.ruidev.framework.web.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.json.DefaultJSONWriter;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import com.ruidev.admin.conf.util.ConfigurationUtil;
import com.ruidev.framework.bo.GenericBo;
import com.ruidev.framework.constant.ErrorType;
import com.ruidev.framework.constant.ListSortBy;
import com.ruidev.framework.constant.PageConstant;
import com.ruidev.framework.entity.AbsEntity;
import com.ruidev.framework.exception.BizException;
import com.ruidev.framework.util.CommonUtil;
import com.ruidev.framework.util.DateTimeUtil;
import com.ruidev.framework.util.LoginContext;
import com.ruidev.framework.util.RequestContext;
import com.thoughtworks.xstream.XStream;

/**
 * 基础控制类 定义通用的action
 *
 * @author: 锐开科技
 * @Copyright: www.ruidev.com All rights reserved.
 */
@ParentPackage("ruidev-default")
public abstract class AbsCrudAction<BO extends GenericBo> extends BaseAction {
	protected static final long serialVersionUID = 201305152104L;

	/**
	 * 短提示信息
	 */
	protected String tip;
	/**
	 * 验证码
	 */
	protected String challange;
	protected String jsonpCallback = "jsonpCallback";
	protected String jsonData;
	protected List<?> objects;
	protected AbsEntity object;
	protected volatile BO bo;
	protected Object returnObject;
	protected String dataType;
	protected int showPageInfo = 0;
	protected int showDetailInfo = 0;
	protected Integer size;
	protected Integer index;
	protected String target;
	protected Map<String, Object> errorMsg;
	protected Collection<Pattern> includeProperties;
	protected Collection<Pattern> excludeProperties;
	protected String requestMethod;
	protected String sortby;

	protected String SUCCESS = "success";
	protected String TARGET = "target";
	protected String LOGIN = "login";
	protected String JSONP = "jsonp";
	protected String JSON = "json";
	protected String REDIRECT = "redirect";
	protected String DOWNLOAD = "download";
	protected String XML = "xml";
	protected String YML = "xml";
	protected String YAML = "xml";
	protected String TIP = "tip";

	// protected static JSONUtil JSON_UTIL = new JSONUtil();
	protected static XStream XSTREAM = new XStream();
	protected static Yaml _YAML = new Yaml();

	public void setResultCode(String code) {
		SUCCESS = code;
		LOGIN = code;
		JSONP = code;
		JSON = code;
		DOWNLOAD = code;
		REDIRECT = code;
		XML = code;
		YML = code;
		YAML = code;
	}

	protected InputStream inputStream;

	/**
	 * 下载的物理文件
	 */
	protected File downloadFile;

	/**
	 * 上传的物理文件
	 */
	protected File uploadFile;
	/**
	 * 上传的物理文件
	 */
	protected File[] uploadFiles;
	/**
	 * 上传的物理文件2
	 */
	protected File uploadFile2;
	/**
	 * 上传的物理文件2
	 */
	protected File[] uploadFile2s;
	/**
	 * 上传的物理文件3
	 */
	protected File uploadFile3;
	/**
	 * 上传的物理文件3
	 */
	protected File[] uploadFile3s;

	/**
	 * 下载的文件名
	 */
	protected String downloadFileName;

	/**
	 * 上传的文件名
	 */
	protected String uploadFileFileName;
	protected String uploadFileContentType;
	/**
	 * 上传的文件名
	 */
	protected String[] uploadFilesFileName;
	/**
	 * 上传的文件2名
	 */
	protected String uploadFile2FileName;
	/**
	 * 上传的文件2名
	 */
	protected String[] uploadFile2sFileName;
	/**
	 * 上传的文件3名
	 */
	protected String uploadFile3FileName;
	/**
	 * 上传的文件3名
	 */
	protected String[] uploadFile3sFileName;

	@Action("index")
	public String index() throws Exception {
		return SUCCESS;
	}

	/**
	 * 下载 return DOWNLOAD
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream getInputStream() throws FileNotFoundException {
		if (inputStream == null && downloadFile != null) {
			inputStream = new FileInputStream(downloadFile);
		}
		return inputStream;
	}

	public String getDownloadFileName() {
		if (downloadFileName == null && downloadFile != null) {
			downloadFileName = downloadFile.getName();
		}
		return downloadFileName;
	}

	@SuppressWarnings({ "rawtypes" })
	protected ReturnData getReturnData() {
		boolean _success = true;
		Map<String, List<String>> fieldErrors = getFieldErrors();
		if (fieldErrors != null && fieldErrors.size() > 0) {
			_success = false;
			setError(ErrorType.INVALID_INPUT, ErrorType.INVALID_INPUT_MSG);
			for (String field : fieldErrors.keySet()) {
				addDetail(field, fieldErrors.get(field).get(0));
			}
		}
		if (getErrorMsg() != null) {
			returnObject = getErrorMsg();
			_success = false;
		} else {
			returnObject = getReturnObject();
		}
		List<String> onlyFetchProps = RequestContext.getOnlyFetchProperties();
		if (RequestContext.getTransformerClass() == null && onlyFetchProps != null) {
			if (returnObject != null) {
				if (returnObject instanceof List) {
					try {
						returnObject = CommonUtil.copyPropertiesBatch((List) returnObject, null,
								onlyFetchProps.toArray(new String[] {}));
					} catch (Exception e) {
						log.error("Faile to copy props: {}", e.getMessage());
					}
				} else {
					try {
						returnObject = CommonUtil.copyProperties(returnObject, null,
								onlyFetchProps.toArray(new String[] {}));
					} catch (Exception e) {
						log.error("Faile to copy props: {}", e.getMessage());
					}
				}
			}
		}
		if (showDetailInfo == 1) {
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("success", _success);
			if (_success) {
				int total = returnObject == null ? 0
						: (returnObject instanceof List) ? ((List<?>) returnObject).size() : 1;
				result.put("total", total);
			}
			result.put("data", returnObject);
			if (!StringUtils.isEmpty(tip)) {
				result.put("tip", tip);
			}
			if (showPageInfo == 1) {
				Map<String, Object> page = new HashMap<String, Object>();
				page.put("index", RequestContext.getIndex());
				page.put("size", RequestContext.getSize());
				page.put("pages", Math.round(Math.ceil(RequestContext.getTotal() / (RequestContext.getSize() * 1.0))));
				page.put("total", RequestContext.getTotal());
				result.put("page", page);
			}
			returnObject = result;
		}
		ReturnData returnData = new ReturnData();
		returnData.object = returnObject;
		returnData.success = _success;
		return returnData;
	}

	/**
	 * 将数据以xml格式返回, 反解析object,objects,errorMsg
	 * 
	 * @return
	 */
	public String getXmlData() {
		ReturnData data = getReturnData();
		returnObject = data.object;
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		try {
			return XSTREAM.toXML(returnObject);
		} catch (Exception e) {
		}
		return null;
	}

	public String getYmlData() {
		ReturnData data = getReturnData();
		returnObject = data.object;
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		try {
			Tag rootTag = new Tag(data.success ? "success" : "error");
			return _YAML.dumpAs(returnObject, rootTag, FlowStyle.BLOCK);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 将json数据再次包装成jsonP格式
	 * 
	 * @return
	 */
	public String getJsonpData() {
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		if (StringUtils.isEmpty(jsonpCallback)) {
			jsonpCallback = "_";
		}
		return CommonUtil.combineStrings(jsonpCallback, "&&", jsonpCallback, "(", getJsonData(), ")");
	}

	/**
	 * 将数据以json格式返回, 反解析object,objects,errorMsg
	 * 
	 * @return
	 */
	public String getJsonData() {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (StringUtils.isEmpty(jsonData)) {
			ReturnData data = getReturnData();
			returnObject = data.object;
			if (returnObject != null) {
				_addExcludeProperty(".*user", ".*tenant", ".*password", ".*application", ".*updater", ".*createBy",
						".*updateBy", ".*applicationId", ".*dataFlag", ".*handler", ".*hibernateLazyInitializer");
			}
			try {
				JSONUtil JSON_UTIL = new JSONUtil();
				DefaultJSONWriter w = new DefaultJSONWriter();
				w.setDateFormatter("yyyy-MM-dd HH:mm:ss");
				JSON_UTIL.setWriter(w);
				jsonData = JSON_UTIL.serialize(returnObject, excludeProperties, includeProperties, false, true);
			} catch (JSONException e) {
				Throwable cause = ExceptionUtils.getRootCause(e);
				e.printStackTrace();
				HashMap<String, Object> errorMsg = new HashMap<String, Object>();
				errorMsg.put("error", e.getMessage());
				if (cause != null) {
					errorMsg.put("cause", cause.getMessage());
				}
				returnObject = null;
				object = null;
				objects = null;
				excludeProperties = null;
				includeProperties = null;
				setErrorMsg(errorMsg);
				jsonData = getJsonData();
			} catch (EmptyStackException e) {
				HashMap<String, Object> errorMsg = new HashMap<String, Object>();
				errorMsg.put("error", "request too frequently");
				returnObject = null;
				object = null;
				objects = null;
				excludeProperties = null;
				includeProperties = null;
				setErrorMsg(errorMsg);
				jsonData = getJsonData();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("error: " + e.getMessage());
			}
		}
		return jsonData;
	}

	/**
	 * 以正则的方式添加排除字段
	 * 
	 * @param exprs
	 */
	protected void addExcludeProperty(String... exprs) {
		String actionName = ServletActionContext.getActionMapping().getName();
		boolean isList = actionName.toLowerCase().contains("list") || actionName.toLowerCase().contains("search")
				|| (returnObject != null && returnObject instanceof Collection);
		for (String expr : exprs) {
			if (showDetailInfo == 1) {
				if (isList) {
					_addExcludeProperty("data\\[\\d*\\]\\." + expr);
				} else {
					_addExcludeProperty("data\\." + expr);
				}
			} else {
				if (isList) {
					_addExcludeProperty("\\[\\d*\\]\\." + expr);
				} else {
					_addExcludeProperty(expr);
				}
			}
		}
	}

	protected void _addExcludeProperty(String... exprs) {
		if (excludeProperties == null) {
			excludeProperties = new ArrayList<Pattern>();
		}
		for (String expr : exprs) {
			excludeProperties.add(Pattern.compile(expr));
		}
	}

	protected void _addIncludeProperty(String... exprs) {
		if (includeProperties == null) {
			includeProperties = new ArrayList<Pattern>();
		}
		for (String expr : exprs) {
			includeProperties.add(Pattern.compile(expr));
		}
	}

	/**
	 * 以正则的方式添加包含字段(注:需要在returnObject不为空的情况下使用)
	 * 
	 * @param exprs
	 */
	protected void addIncludeProperty(String... exprs) {
		Object rtnObj = getReturnObject();
		if (includeProperties == null) {
			includeProperties = new ArrayList<Pattern>();
			if (showDetailInfo == 1) {
				includeProperties.add(Pattern.compile("success"));
				includeProperties.add(Pattern.compile("data(\\[\\d*\\])?"));
				includeProperties.add(Pattern.compile("total"));
				if (showPageInfo == 1) {
					includeProperties.add(Pattern.compile("page"));
					includeProperties.add(Pattern.compile("page.*"));
				}
			} else {
				if (rtnObj instanceof Collection<?>) {
					includeProperties.add(Pattern.compile("(\\[\\d*\\])?"));
				}
			}
		}
		for (String expr : exprs) {
			if (showDetailInfo == 1) {
				if (rtnObj instanceof Collection<?>) {
					includeProperties.add(Pattern.compile("data\\[\\d*\\]\\." + expr));
				} else {
					includeProperties.add(Pattern.compile("data\\." + expr));
				}
				if (showPageInfo == 1) {
					includeProperties.add(Pattern.compile("page"));
					includeProperties.add(Pattern.compile("page.*"));
				}
			} else {
				if (rtnObj instanceof Collection<?>) {
					includeProperties.add(Pattern.compile("\\[\\d*\\]\\." + expr));
				} else {
					includeProperties.add(Pattern.compile(expr));
				}
			}
		}
	}

	public Object getReturnObject() {
		if (returnObject == null) {
			returnObject = object == null ? objects : object;
		}
		return returnObject;
	}

	@SuppressWarnings("unchecked")
	protected void setReturnObjectAttribute(String key, Object value) {
		if (returnObject == null) {
			returnObject = new HashMap<String, Object>();
		}
		if (!(returnObject instanceof HashMap)) {
			return;
		}
		((HashMap<String, Object>) returnObject).put(key, value);
	}

	@SuppressWarnings("unchecked")
	protected Object getReturnObjectAttribute(String key) {
		if (returnObject != null && returnObject instanceof HashMap) {
			return ((HashMap<String, Object>) returnObject).get(key);
		}
		return null;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getJsonpCallback() {
		return jsonpCallback;
	}

	public void setJsonpCallback(String jsonpCallback) {
		this.jsonpCallback = jsonpCallback;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@Autowired
	public void setBo(BO bo) {
		this.bo = bo;
	}

	/**
	 * 2.分页列表显示.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		objects = bo.getListPageData("from " + getClass().getMethod("getObject").getReturnType().getName());
		return SUCCESS;
	}

	/**
	 * 3.读取一条记录.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		object = bo.getData(object.getClass(), object.getId());
		return SUCCESS;
	}

	/**
	 * 4.为添加准备数据.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		return SUCCESS;
	}

	/**
	 * 5.为更新准备显示的数据.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		object = bo.getData(object.getClass(), object.getId());
		return SUCCESS;
	}

	/**
	 * 6.公共保存方法.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		object = bo.saveData(object);
		target = "list";
		return REDIRECT;
	}

	/**
	 * 7.公共删除记录.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		bo.deleteData(object.getClass(), ((AbsEntity) object).getId());
		target = "list";
		return REDIRECT;
	}

	/**
	 * @return the bo
	 */
	public BO getBo() {
		return bo;
	}

	/**
	 * @return the objects
	 * @Column(name = "objects")
	 */
	public List<?> getObjects() {
		return objects;
	}

	/**
	 * @param objects - List
	 */
	public void setObjects(List<?> objects) {
		this.objects = objects;
	}

	public AbsEntity getObject() {
		return object;
	}

	protected <T> void setSessionAttribute(String attr, T value) {
		request.getSession().setAttribute(attr, value);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getSessionAttribute(String attr) {
		return (T) request.getSession(false).getAttribute(attr);
	}

	protected void removeSessionAttribute(String attr) {
		request.getSession().removeAttribute(attr);
	}

	@Override
	protected void finalize() throws Throwable {
		SUCCESS = null;
		LOGIN = null;
		JSONP = null;
		JSON = null;
		DOWNLOAD = null;
		REDIRECT = null;
		XML = null;
		super.finalize();
	}

	public String getDataType() {
		if (!PageConstant.isValidDataType(dataType)) {
			if (this.getFieldErrors() != null && this.getFieldErrors().size() > 0) {
				this.dataType = "input";
			}
		}
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getShowPageInfo() {
		return showPageInfo;
	}

	public void setShowPageInfo(int showPageInfo) {
		this.showPageInfo = showPageInfo;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		if (size > PageConstant.MAX_PAGE_SIZE) {
			size = PageConstant.MAX_PAGE_SIZE;
		}
		RequestContext.setSize(size);
		this.size = size;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		RequestContext.setIndex(index);
		this.index = index;
	}

	public int getShowDetailInfo() {
		return showDetailInfo;
	}

	public void setShowDetailInfo(int showDetailInfo) {
		this.showDetailInfo = showDetailInfo;
	}

	public Map<String, Object> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(Map<String, Object> errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void addErrorMsg(String key, Object content) {
		if (this.errorMsg == null) {
			this.errorMsg = new HashMap<String, Object>();
		}
		this.errorMsg.put(key, content);
	}

	public void addDetail(String key, Object content) {
		if (this.errorMsg == null) {
			this.errorMsg = new HashMap<String, Object>();
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detail = (HashMap<String, Object>) errorMsg.get("detail");
		if (detail == null) {
			detail = new HashMap<String, Object>();
		}
		detail.put(key, content);
		this.errorMsg.put("detail", detail);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param code 错误编码
	 * @param msg  错误信息
	 * @param tip  引导提示
	 */
	public void setError(String code, String msg, String tip) {
		if (this.errorMsg == null) {
			this.errorMsg = new HashMap<String, Object>();
		}
		this.errorMsg.put("tip", tip);
		this.errorMsg.put("code", code);
		this.errorMsg.put("msg", msg);
		this.errorMsg.put("message", msg);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param code 错误编码
	 * @param msg  错误信息
	 */
	public void setError(String code, String msg) {
		setError(code, msg, null);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param code 错误编码
	 * @param msg  错误信息
	 */
	public void setError(int code, String msg) {
		setError(code + "", msg, null);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param code 错误编码
	 * @param msg  错误信息
	 * @param tip  引导提示
	 */
	public void setError(int code, String msg, String tip) {
		setError(code + "", msg, tip);
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	protected String saveUploadFile(String fileName, File uploadFile) {
		String savePath = getSavePath();
		String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, fileName, uploadFile);
		CommonUtil.copyOrCreateFile(uploadFile, new File(realPath));
		String path = realPath.substring(savePath.length() - uploadPath.length());
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return path;
	}

	protected String getPathByActionPathAndUserTenantUploadFile(String baseSavePath, String uploadFileFileName,
			File file) {
		return getPathAndMd5ByActionPathAndUserTenantUploadFile(baseSavePath, uploadFileFileName, file).get("path");
	}

	protected Map<String, String> getPathAndMd5ByActionPathAndUserTenantUploadFile(String baseSavePath,
			String uploadFileFileName, File file) {
		Map<String, String> pathAndMd5 = new HashMap<String, String>();
		String actionPath = getActionPath();
		String user = LoginContext.getCurrentLoginUserId() == null ? "u"
				: LoginContext.getCurrentLoginUserId().toString();
		String tenant = LoginContext.getCurrentLoginUserTenantId() == null ? "t"
				: LoginContext.getCurrentLoginUserTenantId().toString();
		int lastIndex = uploadFileFileName.lastIndexOf(".");
		String name = uploadFileFileName.substring(0, lastIndex == -1 ? uploadFileFileName.length() : lastIndex);
		String type = uploadFileFileName.substring(name.length());
		String fileMd5 = name;
		if (file != null) {
			try {
				fileMd5 = CommonUtil.getFileMD5String(file);
			} catch (Exception e) {
				fileMd5 = CommonUtil.MD5(name);
			}
		}
		StringBuffer buffer = new StringBuffer(baseSavePath)
				.append(actionPath.substring(0, actionPath.lastIndexOf("/") + 1)).append(user).append("_")
				.append(tenant).append("/").append(DateTimeUtil.getCurrentDateStr()).append("/").append(fileMd5)
				.append(type);
		String path = buffer.toString().replace("\\", "/").replace("//", "/");
		pathAndMd5.put("path", path);
		pathAndMd5.put("md5", fileMd5);
		return pathAndMd5;
	}

	protected String getPathByActionPathAndUserTenantUploadFile(String baseSavePath, String uploadFileFileName,
			File file, String size) {
		String actionPath = getActionPath();
		String user = LoginContext.getCurrentLoginUserId() == null ? "u"
				: LoginContext.getCurrentLoginUserId().toString();
		String tenant = LoginContext.getCurrentLoginUserTenantId() == null ? "t"
				: LoginContext.getCurrentLoginUserTenantId().toString();
		int lastIndex = uploadFileFileName.lastIndexOf(".");
		String name = uploadFileFileName.substring(0, lastIndex == -1 ? uploadFileFileName.length() : lastIndex);
		String type = uploadFileFileName.substring(name.length());
		String fileMd5 = name;
		try {
			fileMd5 = CommonUtil.getFileMD5String(file);
		} catch (Exception e) {
			e.printStackTrace();
			fileMd5 = CommonUtil.MD5(name);
		}
		fileMd5 = CommonUtil.combineStrings(fileMd5, "_", size);
		StringBuffer buffer = new StringBuffer(baseSavePath)
				.append(actionPath.substring(0, actionPath.lastIndexOf("/") + 1)).append(user).append("_")
				.append(tenant).append("/").append(DateTimeUtil.getCurrentDateStr()).append("/").append(fileMd5)
				.append(type);
		return buffer.toString().replace("\\", "/").replace("//", "/");
	}

	protected String uploadPath = "/resources/files/";

	protected String getSavePath() {
		return getResourceDiskSavePath() + uploadPath;
	}

	protected String getResourceDiskSavePath() {
		String savePath = ConfigurationUtil.getConfigurationValue("DISK_RESOURCE_PATH");
		if (!StringUtils.isEmpty(savePath)) {
			savePath = savePath.replaceAll("\\\\/$", "");
		} else {
			savePath = ServletActionContext.getServletContext().getRealPath("");
		}
		return savePath;
	}

	protected String getUploadFileRealPath(String fileName, File file) {
		if (StringUtils.isEmpty(fileName)) {
			return null;
		}
		String savePath = getSavePath();
		String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, fileName, file);
		return realPath;
	}

	protected String saveScaledImgUploadFile(int width, int height) throws Exception {
		if (uploadFile != null) {
			String savePath = getSavePath();
			String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFileFileName, uploadFile,
					width + "_" + height);
			CommonUtil.createScaleImgFile(uploadFile, width, height, new File(realPath));
			String url = realPath.substring(savePath.length() - uploadPath.length());
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
			return url;
		}
		return null;
	}

	protected String saveUploadFile() {
		if (uploadFile != null) {
			String savePath = getSavePath();
			String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFileFileName, uploadFile);
			CommonUtil.copyOrCreateFile(uploadFile, new File(realPath));
			String url = realPath.substring(savePath.length() - uploadPath.length());
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
			return url.replaceAll("/+", "/");
		}
		return null;
	}

	protected String saveUploadFile(File file, String fileName) {
		if (file != null) {
			String savePath = getSavePath();
			String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, fileName, file);
			CommonUtil.copyOrCreateFile(file, new File(realPath));
			String url = realPath.substring(savePath.length() - uploadPath.length());
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
			return url.replaceAll("/+", "/");
		}
		return null;
	}

	protected String[] saveUploadFiles() {
		if (uploadFiles != null && uploadFilesFileName != null) {
			String[] paths = new String[uploadFiles.length];
			int index = 0;
			for (File uploadFile : uploadFiles) {
				if (uploadFile == null) {
					paths[index] = null;
				}
				String savePath = getSavePath();
				String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFilesFileName[index],
						uploadFile);
				CommonUtil.copyOrCreateFile(uploadFile, new File(realPath));
				paths[index] = realPath.substring(savePath.length() - uploadPath.length());
				if (!paths[index].startsWith("/")) {
					paths[index] = "/" + paths[index];
				}
				index++;
			}
			return paths;
		}
		return null;
	}

	protected String saveUploadFile2() {
		if (uploadFile2 != null) {
			String savePath = getSavePath();
			String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFile2FileName, uploadFile2);
			CommonUtil.copyOrCreateFile(uploadFile2, new File(realPath));
			return realPath.substring(savePath.length() - uploadPath.length());
		}
		return null;
	}

	protected String[] saveUploadFile2s() {
		if (uploadFile2s != null) {
			String[] paths = new String[uploadFile2s.length];
			int index = 0;
			for (File uploadFile2 : uploadFile2s) {
				String savePath = getSavePath();
				String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFile2sFileName[index],
						uploadFile2);
				CommonUtil.copyOrCreateFile(uploadFile2, new File(realPath));
				paths[index] = realPath.substring(savePath.length() - uploadPath.length());
				index++;
			}
			return paths;
		}
		return null;
	}

	protected String saveUploadFile3() {
		if (uploadFile3 != null) {
			String savePath = getSavePath();
			String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFile3FileName, uploadFile3);
			CommonUtil.copyOrCreateFile(uploadFile3, new File(realPath));
			return realPath.substring(savePath.length() - uploadPath.length());
		}
		return null;
	}

	protected String[] saveUploadFile3s() {
		if (uploadFile3s != null) {
			String[] paths = new String[uploadFile3s.length];
			int index = 0;
			for (File uploadFile3 : uploadFile3s) {
				String savePath = getSavePath();
				String realPath = getPathByActionPathAndUserTenantUploadFile(savePath, uploadFile3sFileName[index],
						uploadFile3);
				CommonUtil.copyOrCreateFile(uploadFile3, new File(realPath));
				paths[index] = realPath.substring(savePath.length() - uploadPath.length());
				index++;
			}
			return paths;
		}
		return null;
	}

	public File getUploadFile2() {
		return uploadFile2;
	}

	public void setUploadFile2(File uploadFile2) {
		this.uploadFile2 = uploadFile2;
	}

	public String getUploadFile2FileName() {
		return uploadFile2FileName;
	}

	public void setUploadFile2FileName(String uploadFile2FileName) {
		this.uploadFile2FileName = uploadFile2FileName;
	}

	public File[] getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(File[] uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public File[] getUploadFile2s() {
		return uploadFile2s;
	}

	public void setUploadFile2s(File[] uploadFile2s) {
		this.uploadFile2s = uploadFile2s;
	}

	public String[] getUploadFilesFileName() {
		return uploadFilesFileName;
	}

	public void setUploadFilesFileName(String[] uploadFilesFileName) {
		this.uploadFilesFileName = uploadFilesFileName;
	}

	public String[] getUploadFile2sFileName() {
		return uploadFile2sFileName;
	}

	public void setUploadFile2sFileName(String[] uploadFile2sFileName) {
		this.uploadFile2sFileName = uploadFile2sFileName;
	}

	public File getUploadFile3() {
		return uploadFile3;
	}

	public void setUploadFile3(File uploadFile3) {
		this.uploadFile3 = uploadFile3;
	}

	public File[] getUploadFile3s() {
		return uploadFile3s;
	}

	public void setUploadFile3s(File[] uploadFile3s) {
		this.uploadFile3s = uploadFile3s;
	}

	public String getUploadFile3FileName() {
		return uploadFile3FileName;
	}

	public void setUploadFile3FileName(String uploadFile3FileName) {
		this.uploadFile3FileName = uploadFile3FileName;
	}

	public String[] getUploadFile3sFileName() {
		return uploadFile3sFileName;
	}

	public void setUploadFile3sFileName(String[] uploadFile3sFileName) {
		this.uploadFile3sFileName = uploadFile3sFileName;
	}

	public String getChallange() {
		return challange;
	}

	public void setChallange(String challange) {
		this.challange = challange;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUploadFileContentType() {
		return uploadFileContentType;
	}

	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}

	public void assertNull(Object param, String paramName) throws Exception {
		if (param == null) {
			throw new BizException(ConfigurationUtil.getInstance().i18n("req.paramrequired", paramName),
					ErrorType.INVALID_INPUT);// CommonUtil.combineStrings("参数", paramName, "不能为空"));
		}
	}

	public void assertNullOrEmpty(Object param, String paramName) throws Exception {
		if (param == null) {
			throw new BizException(ConfigurationUtil.getInstance().i18n("req.paramrequired", paramName),
					ErrorType.INVALID_INPUT);
		} else {
			if (param instanceof String) {
				if (StringUtils.isEmpty(param.toString())) {
					throw new BizException(ConfigurationUtil.getInstance().i18n("req.paramrequired", paramName),
							ErrorType.INVALID_INPUT);
				}
			}
		}
	}

	public void assertNullOrEmptyError(Object param, String error) throws Exception {
		if (param == null) {
			throw new BizException(error, ErrorType.INVALID_INPUT);
		} else {
			if (param instanceof String) {
				if (StringUtils.isEmpty(param.toString())) {
					throw new BizException(error, ErrorType.INVALID_INPUT);
				}
			}
		}
	}

	public void assertNullOrEmptyErrorWithCode(int errorcode, Object param, String error) throws Exception {
		if (param == null) {
			throw new BizException(error, errorcode);
		} else {
			if (param instanceof String) {
				if (StringUtils.isEmpty(param.toString())) {
					throw new BizException(error, errorcode);
				}
			}
		}
	}

	public void assertNullOrEmpty(Object param, String paramName, int maxLen) throws Exception {
		assertNullOrEmpty(param, paramName);
		if (param.toString().length() > maxLen) {
			throw new BizException(CommonUtil.combineStrings("参数【", paramName, "】长度不能超过", maxLen + "", "位"),
					ErrorType.INVALID_INPUT);
		}
	}

	public void assertNullOrEmpty(Object param, String paramName, int minLen, int maxLen) throws Exception {
		assertNullOrEmpty(param, paramName);
		if (param.toString().length() < minLen) {
			throw new BizException(CommonUtil.combineStrings("参数【", paramName, "】长度不能低于", minLen + "", "位"),
					ErrorType.INVALID_INPUT);
		}
		if (param.toString().length() > maxLen) {
			throw new BizException(CommonUtil.combineStrings("参数【", paramName, "】长度不能超过", maxLen + "", "位"),
					ErrorType.INVALID_INPUT);
		}
	}

	public void assertNullOrEmptyError(Object param, String error, int minLen, int maxLen) throws Exception {
		assertNullOrEmptyError(param, error);
		if (param.toString().length() < minLen) {
			throw new BizException(CommonUtil.combineStrings(error, " 长度不能低于", minLen + "", "位"),
					ErrorType.INVALID_INPUT);
		}
		if (param.toString().length() > maxLen) {
			throw new BizException(CommonUtil.combineStrings(error, " 长度不能超过", maxLen + "", "位"),
					ErrorType.INVALID_INPUT);
		}
	}

	public void assertNullOrEmptyErrorWithCode(int errorcode, Object param, String error, int minLen, int maxLen)
			throws Exception {
		assertNullOrEmptyErrorWithCode(errorcode, param, error);
		if (param.toString().length() < minLen) {
			throw new BizException(CommonUtil.combineStrings(error, " 长度不能低于", minLen + "", "位"), errorcode);
		}
		if (param.toString().length() > maxLen) {
			throw new BizException(CommonUtil.combineStrings(error, " 长度不能超过", maxLen + "", "位"), errorcode);
		}
	}

	public void throwBizException(Object... strings) throws BizException {
		throw new BizException(CommonUtil.combineStrings(strings));
	}

	public void throwBizExceptionWithCode(int errorId, Object... strings) throws BizException {
		BizException e = new BizException(CommonUtil.combineStrings(strings));
		e.setErrorId(errorId);
		throw e;
	}

	public String getRequestMethod() {
		requestMethod = request.getMethod().toUpperCase();
		return requestMethod;
	}

	public boolean isGetRequest() {
		return "GET".equals(getRequestMethod());
	}

	public boolean isPostRequest() {
		return "POST".equals(getRequestMethod());
	}

	public String getSortby() {
		return sortby;
	}

	public void setSortby(String sortby) {
		this.sortby = sortby;
	}

	public List<ListSortBy> getSortBys(String... sorts) {
		if (StringUtils.isEmpty(sortby))
			return null;
		Map<String, Boolean> sortsLimit = new HashMap<String, Boolean>();
		for (String s : sorts) {
			sortsLimit.put(s, true);
		}
		String[] _sorts = sortby.split(",");
		List<ListSortBy> bys = new ArrayList<ListSortBy>();
		for (String _sort : _sorts) {
			String[] sort = _sort.split("=");
			String orderField = sort[0];
			if (!sortsLimit.containsKey(orderField))
				continue;
			String orderby = sort[1];
			ListSortBy sortBy = ConfigurationUtil.getInstance().getEnum(ListSortBy.class, orderby);
			if (sortBy == null)
				continue;
			sortBy.setField(orderField);
			bys.add(sortBy);
			RequestContext.addOrderBy(orderField, sortBy.getName());
		}
		return bys;
	}

	public List<ListSortBy> getSortBys(ListSortBy defaultSortBy, String... sorts) {
		List<ListSortBy> bys = getSortBys(sorts);
		if (bys == null) {
			RequestContext.addOrderBy(defaultSortBy.getField(), defaultSortBy.getName());
			bys = Arrays.asList(defaultSortBy);
		}
		return bys;
	}

	public List<ListSortBy> getSortBys(List<ListSortBy> defaultSortBys, String... sorts) {
		List<ListSortBy> bys = getSortBys(sorts);
		if (bys == null && defaultSortBys != null) {
			for (ListSortBy by : defaultSortBys) {
				RequestContext.addOrderBy(by.getField(), by.getName());
			}
			bys = defaultSortBys;
		}
		return bys;
	}
}

class ReturnData {
	public boolean success;
	public Object object;
}