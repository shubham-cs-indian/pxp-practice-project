package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstanceVersionModel extends IModel {
  
  public static final String SUMMARY          = "summary";
  public static final String VERSION_NUMBER   = "versionNumber";
  public static final String MESSAGE          = "message";
  public static final String CREATED_BY       = "createdBy";
  public static final String LAST_MODIFIED_BY = "lastModifiedBy";
  public static final String LAST_MODIFIED    = "lastModified";
  public static final String SAVE_COMMENT     = "saveComment";
  
  public String getMessage();
  
  public void setMessage(String message);
  
  public Integer getVersionNumber();
  
  public void setVersionNumber(Integer versionNumber);
  
  public IKlassInstanceVersionSummaryModel getSummary();
  
  public void setSummary(IKlassInstanceVersionSummaryModel summery);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public String getLastModifiedBy();
  
  public void setLastModifiedBy(String lastModifiedBy);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
}
