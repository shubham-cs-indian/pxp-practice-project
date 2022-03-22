package com.cs.core.runtime.interactor.model.purge;

public class PurgeRequestModel implements IPurgeRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          docType;
  protected Long            purgeTime;
  
  @Override
  public String getDocType()
  {
    return docType;
  }
  
  @Override
  public void setDocType(String docType)
  {
    this.docType = docType;
  }
  
  @Override
  public Long getPurgeTime()
  {
    return purgeTime;
  }
  
  @Override
  public void setPurgeTime(Long purgeTime)
  {
    this.purgeTime = purgeTime;
  }
}
