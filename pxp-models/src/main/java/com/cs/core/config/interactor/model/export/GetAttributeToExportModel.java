package com.cs.core.config.interactor.model.export;

public class GetAttributeToExportModel implements IGetAttributeToExportModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          tagId;
  protected String          lastExportedTimeStamp;
  
  @Override
  public String getTagId()
  {
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public String getLastExportedTimeStampKey()
  {
    return lastExportedTimeStamp;
  }
  
  @Override
  public void setLastExportedTimeStampKey(String lastExportedTimeStamp)
  {
    this.lastExportedTimeStamp = lastExportedTimeStamp;
  }
}
