package com.cs.core.runtime.interactor.model.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;

@Component("context")
public class SessionContextCustomProxy implements ISessionContext {
  
  @Autowired
  protected ISessionContext      springSessionContext;
  
  @Autowired
  protected TransactionThreadData transactionThreadData;
  
  protected ThreadLocal<String>  userId    = new ThreadLocal<>();
  
  protected ThreadLocal<String>  indexName = new ThreadLocal<>();

  protected ThreadLocal<Boolean> sessionId = new ThreadLocal<>();
  
  protected ThreadLocal<IUserSessionDTO> userSessionDTO = new ThreadLocal<>();

  @Override
  public Map<String, String> getIdFilePathMap()
  {
    return springSessionContext.getIdFilePathMap();
  }
  
  @Override
  public void setIdFilePathMap(Map<String, String> idFilePathMap)
  {
    springSessionContext.setIdFilePathMap(idFilePathMap);
  }
  
  public UUID getRequestId()
  {
    try {
      UUID requestId = springSessionContext.getRequestId();
      return requestId;
    }
    catch (Throwable e) {
      return null;
    }
  }
  
  public void setRequestId(UUID requestId)
  {
    try {
      springSessionContext.setRequestId(requestId);
    }
    catch (Throwable e) {
    }
  }
  
  public String getUserId()
  {
    try {
      String userId = springSessionContext.getUserId();
      return userId;
    }
    catch (Throwable e) {     
      return transactionThreadData.getTransactionData().getUserId();
    }
  }
  
  public void setUserId(String userId)
  {
    try {
      springSessionContext.setUserId(userId);
    }
    catch (Throwable e) {
      this.userId.set(userId);
    }
  }
  
  public String getTheme()
  {
    try {
      String theme = springSessionContext.getTheme();
      return theme;
    }
    catch (Throwable e) {
      return null;
    }
  }
  
  public void setTheme(String theme)
  {
    try {
      springSessionContext.setTheme(theme);
    }
    catch (Throwable e) {
    }
  }
  
  public String getUserName()
  {
    try {
      String userName = springSessionContext.getUserName();
      return userName;
    }
    catch (Throwable e) {
      return transactionThreadData.getTransactionData().getUserName();
    }
  }
  
  public void setUserName(String userName)
  {
    try {
      springSessionContext.setUserName(userName);
    }
    catch (Throwable e) {
    }
  }
  
  @Override
  public String getFromIdFilePathMap(String id)
  {
    return springSessionContext.getFromIdFilePathMap(id);
  }
  
  @Override
  public void putInIdFilePathMap(String id, String value)
  {
    springSessionContext.putInIdFilePathMap(id, value);
  }
  
  @Override
  public void removeFromIdFilePathMap(String id)
  {
    springSessionContext.removeFromIdFilePathMap(id);
  }
  
  @Override
  public Set<String> getImportedIds()
  {
    if (springSessionContext.getImportedIds() == null) {
      springSessionContext.setImportedIds(new HashSet<>());
    }
    return springSessionContext.getImportedIds();
  }
  
  @Override
  public void setImportedIds(Set<String> ids)
  {
    springSessionContext.setImportedIds(ids);
  }
  
  @Override
  public String getFileId()
  {
    try {
      return springSessionContext.getFileId();
    }
    catch (Throwable e) {
      return null;
    }
  }
  
  @Override
  public void setFileId(String fileId)
  {
    springSessionContext.setFileId(fileId);
  }
  
  @Override
  public String getFileName()
  {
    try {
      return springSessionContext.getFileName();
    }
    catch (Exception e) {
      return null;
    }
  }
  
  @Override
  public void setFileName(String fileName)
  {
    springSessionContext.setFileName(fileName);
  }
  
  public String getSessionId()
  {
    try {
      return springSessionContext.getSessionId();
    }
    catch (Exception e) {
      return null;
    }
  }
  
  public void setSessionId(String sessionId)
  {
    springSessionContext.setSessionId(sessionId);
  }
  
  @Override
  public IUserSessionDTO getUserSessionDTO() {
    try {
      return springSessionContext.getUserSessionDTO();
    }catch(Exception e) {
      /* return new UserSessionDTO(null, new UserDTO(10, "admin"), null, 0, null);*/
      return userSessionDTO.get();
    }
  }
  
  @Override
  public void setUserSessionDTO(IUserSessionDTO userSessionDTO)
  {
    springSessionContext.setUserSessionDTO(userSessionDTO);
  }
  
  public void setUserSessionDTOInThreadLocal(IUserSessionDTO userSessionDTO)
  {
     this.userSessionDTO.set(userSessionDTO);
  }
  
  @Override
  public String getClientIPAddress()
  {
    try {
      return springSessionContext.getClientIPAddress();
    }
    catch (Exception e) {
      return null;
    }
  }

  @Override
  public void setClientIPAddress(String clientIPAddress)
  {
    springSessionContext.setClientIPAddress(clientIPAddress);
  }

}
