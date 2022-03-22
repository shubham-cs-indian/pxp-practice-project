package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.klass.IInheritanceDataModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class InheritanceDataModel implements IInheritanceDataModel {
  
  private static final long                             serialVersionUID       = 1L;
  
  protected String                                      sourceContentId;
  protected String                                      targetContentId;
  protected String                                      baseType;
  protected List<IRelationshipPropertiesToInheritModel> relationshipProperties = new ArrayList<>();
  protected Boolean                                     isMerged               = false;
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public String getTargetContentId()
  {
    return targetContentId;
  }
  
  @Override
  public void setTargetContentId(String targetContentId)
  {
    this.targetContentId = targetContentId;
  }
  
  @Override
  public List<IRelationshipPropertiesToInheritModel> getRelationshipProperties()
  {
    return relationshipProperties;
  }
  
  @JsonDeserialize(contentAs = RelationshipPropertiesToInheritModel.class)
  @Override
  public void setRelationshipProperties(
      List<IRelationshipPropertiesToInheritModel> relationshipProperties)
  {
    this.relationshipProperties = relationshipProperties;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Boolean getIsMerged()
  {
    return isMerged;
  }
  
  @Override
  public void setIsMerged(Boolean isMerged)
  {
    this.isMerged = isMerged;
  }
}
