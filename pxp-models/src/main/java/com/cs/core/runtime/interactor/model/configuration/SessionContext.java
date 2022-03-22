package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("springSessionContext")
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class SessionContext implements ISessionContext {
  
  protected String              userId;
  protected String              userName;
  protected UUID                requestId;
  protected String              requestURI;
  protected String              theme;
  protected Map<String, String> idFilePathMap = new HashMap<>();
  protected Set<String>         ids           = new HashSet<>();
  protected String              fileId;
  protected String              fileName;
  protected String              sessionId;
  protected IUserSessionDTO     userSessionDTO;
  protected String              clientIPAddress;
  
  @Override
  public Map<String, String> getIdFilePathMap()
  {
    return idFilePathMap;
  }
  
  @Override
  public void setIdFilePathMap(Map<String, String> idFilePathMap)
  {
    this.idFilePathMap = idFilePathMap;
  }
  
  @Override
  public void removeFromIdFilePathMap(String id)
  {
    idFilePathMap.remove(id);
  }
  
  @Override
  public void putInIdFilePathMap(String id, String value)
  {
    idFilePathMap.put(id, value);
  }
  
  @Override
  public String getFromIdFilePathMap(String id)
  {
    return idFilePathMap.get(id);
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public UUID getRequestId()
  {
    return requestId;
  }
  
  @Override
  public void setRequestId(UUID requestId)
  {
    this.requestId = requestId;
  }
  
  @Override
  public String getTheme()
  {
    return theme;
  }
  
  @Override
  public void setTheme(String theme)
  {
    this.theme = theme;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String toString()
  {
    return "Context [userId=" + userId + ", requestId=" + requestId + ", requestURI=" + requestURI
        + "]";
  }
  
  @Override
  public Set<String> getImportedIds()
  {
    if (ids == null) {
      ids = new HashSet<>();
    }
    return ids;
  }
  
  @Override
  public void setImportedIds(Set<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public String getFileId()
  {
    return fileId;
  }
  
  @Override
  public void setFileId(String fileId)
  {
    this.fileId = fileId;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  public String getSessionId()
  {
    return sessionId;
  }
  
  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }
  
  @Override
  public IUserSessionDTO getUserSessionDTO()
  {
    return userSessionDTO;
  }
  
  @Override
  public void setUserSessionDTO(IUserSessionDTO userSessionDTO)
  {
    this.userSessionDTO = userSessionDTO;
  }
  
  @Override
  public String getClientIPAddress()
  {
    return clientIPAddress;
  }

  @Override
  public void setClientIPAddress(String clientIPAddress)
  {
    this.clientIPAddress = clientIPAddress;
  }
}
