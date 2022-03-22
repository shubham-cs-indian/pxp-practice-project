package com.cs.core.runtime.interactor.entity.contentidentifier;

public class ContextStructureInfo implements IContextStructureInfo {
  
  private static final long serialVersionUID = 1L;
  
  protected String          structureId;
  
  protected String          rootStructureId;
  
  @Override
  public String getStructureId()
  {
    return structureId;
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    this.structureId = structureId;
  }
  
  @Override
  public String getRootStructureId()
  {
    return rootStructureId;
  }
  
  @Override
  public void setRootStructureId(String rootStructureId)
  {
    this.rootStructureId = rootStructureId;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
