package com.cs.core.config.interactor.model.articleimportcomponent;

import java.io.Serializable;
import java.util.Set;

public interface IProcessContext extends Serializable {
  
  public static String USER_ID                  = "userId";
  public static String IMPORTED_IDS             = "importedIds";
  public static String IMPORTED_IDS_TO_TRANSFER = "importedIdsToTransfer";
  public static String INDEX_NAME               = "indexName";
  public static String FILE_ID                  = "fileId";
  public final String  DATA                     = "data";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Set<String> getImportedIds();
  
  public void setImportedIds(Set<String> importedIds);
  
  public String getIndexName();
  
  public void setIndexName(String indexName);
  
  public String getFileId();
  
  public void setFileId(String fileId);
  
  public Set<String> getImportedIdsToTransfer();
  
  public void setImportedIdsToTransfer(Set<String> importedIdsToTransfer);
  
  public Object getData();
  
  public void setData(Object data);
}
