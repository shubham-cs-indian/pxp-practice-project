package com.cs.core.runtime.interactor.model.version;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstanceVersionModel implements IKlassInstanceVersionModel {
  
  private static final long                   serialVersionUID = 1L;
  protected Integer                           versionNumber;
  protected IKlassInstanceVersionSummaryModel summary;
  protected String                            createdBy;
  protected String                            message;
  protected String                            lastModifiedBy;
  protected Long                              lastModified;
  
  @Override
  public Integer getVersionNumber()
  {
    return versionNumber;
  }
  
  @Override
  public void setVersionNumber(Integer versionNumber)
  {
    this.versionNumber = versionNumber;
  }
  
  @Override
  public String getMessage()
  {
    return message;
  }
  
  @Override
  public void setMessage(String messages)
  {
    this.message = messages;
  }
  
  @Override
  public IKlassInstanceVersionSummaryModel getSummary()
  {
    if(summary == null)
      summary = new KlassInstanceVersionSummaryModel();
    return summary;
  }
  
  @Override
  @JsonDeserialize(as = KlassInstanceVersionSummaryModel.class)
  public void setSummary(IKlassInstanceVersionSummaryModel summery)
  {
    this.summary = summery;
  }
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
}
