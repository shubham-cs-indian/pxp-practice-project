package com.cs.core.config.interactor.model.articleimportcomponent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("processContext")
@Scope("prototype")
public class ProcessContext implements IProcessContext {
  
  protected String      userId;
  protected Set<String> importedIds           = new HashSet<>();
  protected Set<String> importedIdsToTransfer = new HashSet<>();
  protected String      indexName;
  protected String      fileId;
  protected Object      data;
  
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
  public Set<String> getImportedIds()
  {
    if (importedIds == null) {
      importedIds = new HashSet<>();
    }
    return importedIds;
  }
  
  @Override
  public void setImportedIds(Set<String> ids)
  {
    this.importedIds = ids;
  }
  
  @Override
  public String getIndexName()
  {
    
    return indexName;
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
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
  public Set<String> getImportedIdsToTransfer()
  {
    return importedIdsToTransfer;
  }
  
  @Override
  public void setImportedIdsToTransfer(Set<String> importedIdsToTransfer)
  {
    this.importedIdsToTransfer = importedIdsToTransfer;
  }
  
  @Override
  public Object getData()
  {
    return this.data;
  }
  
  @Override
  public void setData(Object data)
  {
    this.data = data;
  }
}
