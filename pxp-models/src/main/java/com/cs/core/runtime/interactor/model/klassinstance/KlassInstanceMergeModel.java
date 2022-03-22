package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstanceMergeModel implements IKlassInstanceMergeModel {
  
  private static final long                                          serialVersionUID = 1L;
  
  protected String                                                   id;
  
  protected Map<String, String>                                      resolved         = new HashMap<>();
  
  protected Map<String, ? extends IKlassInstanceStructureMergeModel> structureChildren;
  
  protected Long                                                     latestVersionId;
  
  protected Long                                                     workingVersionId;
  
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
  public Map<String, String> getResolved()
  {
    return resolved;
  }
  
  @Override
  public void setResolved(Map<String, String> resolved)
  {
    this.resolved = resolved;
  }
  
  @Override
  public Map<String, ? extends IKlassInstanceStructureMergeModel> getStructureChildren()
  {
    return structureChildren;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceStructureMergeModel.class)
  @Override
  public void setStructureChildren(
      Map<String, ? extends IKlassInstanceStructureMergeModel> structureChildren)
  {
    this.structureChildren = structureChildren;
  }
  
  @Override
  public Long getLatestVersionId()
  {
    return latestVersionId;
  }
  
  @Override
  public void setLatestVersionId(Long latestVersionId)
  {
    this.latestVersionId = latestVersionId;
  }
  
  @Override
  public Long getWorkingVersionId()
  {
    return workingVersionId;
  }
  
  @Override
  public void setWorkingVersionId(Long workingVersionId)
  {
    this.workingVersionId = workingVersionId;
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
