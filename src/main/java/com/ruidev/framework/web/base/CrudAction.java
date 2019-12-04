package com.ruidev.framework.web.base;

import java.util.Map;

public interface CrudAction {

    public void setActionPath(String p);

    public void setActionName(String name);
    
    public String getActionPath();

    public String getToken();

    public void setToken(String token);
    
    public void setResultCode(String code);

    public String getDataType();
    
    public Integer getIndex();
    
    public Integer getSize();
    
    public Object getObject();
    
    public void setErrorMsg(Map<String, Object> errors);
    
    public void addErrorMsg(String key, Object content);
    
    public void addDetail(String key, Object content);
    
    public void setError(String code, String msg);
    
    public void setError(int code, String msg);
    
    public Map<String, Object> getErrorMsg();
    
    public String getChallange();
}