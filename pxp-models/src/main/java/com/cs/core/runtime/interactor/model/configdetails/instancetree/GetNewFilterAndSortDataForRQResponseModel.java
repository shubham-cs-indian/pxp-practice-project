package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForRQResponseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetNewFilterAndSortDataForRQResponseModel extends GetNewFilterAndSortDataResponseModel
    implements IGetNewFilterAndSortDataForRQResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    targetIds;
  protected Boolean         isTargetTaxonomy = false;
  protected IRelationship   relationshipConfig;
  
  @Override
  @JsonIgnore
  public List<String> getTargetIds()
  {
    if (targetIds == null) {
      targetIds = new ArrayList<>();
    }
    return targetIds;
  }
  
  @Override
  @JsonProperty
  public void setTargetIds(List<String> targetIds)
  {
    this.targetIds = targetIds;
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsTargetTaxonomy()
  {
    return isTargetTaxonomy;
  }
  
  @Override
  @JsonProperty
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy)
  {
    this.isTargetTaxonomy = isTargetTaxonomy;
  }
  
  @Override
  public IRelationship getRelationshipConfig()
  {
    return this.relationshipConfig;
  }
  
  @JsonDeserialize(as = Relationship.class)
  @Override
  public void setRelationshipConfig(IRelationship relationshipConfig)
  {
    this.relationshipConfig = relationshipConfig;
  }
}
