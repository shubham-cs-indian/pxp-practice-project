package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;

public class GetRelationshipQuicklistRequestModel extends GetInstanceTreeRequestModel
    implements IGetRelationshipQuicklistRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Boolean         isTargetTaxonomy;
  protected String          relationshipId;
  protected String          type;
  protected String          sideId;
  protected List<String>    targetIds;
  
  @Override
  public String getInstanceId()
  {
    return id;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.id = instanceId;
  }
  
  @Override
  public Boolean getIsTargetTaxonomy()
  {
    return isTargetTaxonomy;
  }
  
  @Override
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy)
  {
    this.isTargetTaxonomy = isTargetTaxonomy;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
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
  
}
