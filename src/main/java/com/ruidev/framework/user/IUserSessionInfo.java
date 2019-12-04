package com.ruidev.framework.user;

import java.io.Serializable;

/**
 * 用户登陆后的SessionInfo内容
 */
public interface IUserSessionInfo extends Serializable{
    /**
     * 用户信息
     * @return
     */
    IUser getUser();

    /**
     * @param user
     *            the user to set
     */
    void setUser(IUser user);

    /**
     * session
     * @return
     */
    String getSessionId();

    void setSessionId(String sessionId);

    /**
     * Ip地址
     * @return
     */
    String getRemoteIp();

    void setRemoteIp(String remoteIp);
    /**
     * 用户身份
     * @return
     */
    String getPrincipal();
    
    boolean hasPermission(String uri);
}
