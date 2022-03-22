package com.cs.core.runtime.interactor.model.configuration;

public class ProcessInstanceFileModel implements IProcessInstanceFileModel {
  
  private static final long serialVersionUID   = 1L;
  protected String          id;
  protected Boolean         downloadIndividual = false;
  protected String          filePath;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getfilePath()
  {
    return filePath;
  }
  
  @Override
  public void setfilePath(String filePath)
  {
    this.filePath = filePath;
    
  }
  
  @Override
  public void setdownloadIndividual(Boolean downloadIndividual)
  {
    this.downloadIndividual = downloadIndividual;
    
  }
  
  @Override
  public boolean getIsDownloadIndividual()
  {
    return downloadIndividual;
  }
  
}
