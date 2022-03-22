package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;

public class GetKlassTaxonomyTreeForRelationshipRequestModel extends GetKlassTaxonomyTreeRequestModel 
      implements IGetKlassTaxonomyTreeForRelationshipRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          relationshipId;
  protected String          sideId;
  protected String          klassId;
  protected String          type;
  protected String          instanceId;
  protected List<String>    targetIds;
  protected List<String>    moduleEntities;
  
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationShipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }

  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }

  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
  }

  @Override
  public List<String> getTargetIds()
  {
    if(targetIds == null) {
      targetIds = new ArrayList<>();
    }
    return targetIds;
  }

  @Override
  public void setTargetIds(List<String> targetIds)
  {
    this.targetIds = targetIds;
  }
 
  @Override
  public List<String> getModuleEntities()
  {
    if(moduleEntities == null) {
      moduleEntities = new ArrayList<>();
    }
    return moduleEntities;
  }

  @Override
  public void setModuleEntities(List<String> moduleEntities)
  {
    this.moduleEntities = moduleEntities;
  }

}
