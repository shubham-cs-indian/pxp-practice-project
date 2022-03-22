package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ISessionContext {
  
  public static String REQUEST_ID   = "requestId";
  public static String USER_ID      = "userId";
  public static String THEME        = "theme";
  public static String USER_NAME    = "userName";
  public static String IMPORTED_IDS = "importedIds";
  public static String FILE_ID      = "fileId";
  public static String FILE_NAME    = "fileName";
  public static String SESSION_ID   = "sessionId";
  public static String USER_SESSION_DTO   = "userSessionDTO";
  public static String CLIENT_IP_ADDRESS  = "clientIPAddress";
  
  public UUID getRequestId();
  
  public void setRequestId(UUID requestId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getTheme();
  
  public void setTheme(String theme);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public Map<String, String> getIdFilePathMap();
  
  public void setIdFilePathMap(Map<String, String> idFilePathMap);
  
  public String getFromIdFilePathMap(String id);
  
  public void putInIdFilePathMap(String id, String value);
  
  public void removeFromIdFilePathMap(String id);
  
  public Set<String> getImportedIds();
  
  public void setImportedIds(Set<String> importedIds);
  
  public String getFileId();
  
  public void setFileId(String fileId);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String getSessionId();
  
  public void setSessionId(String sessionId);
  
  public IUserSessionDTO getUserSessionDTO();
  
  public void setUserSessionDTO(IUserSessionDTO userSessionDTO);
  
  public String getClientIPAddress();
  
  public void setClientIPAddress(String clientIPAddress);
}
