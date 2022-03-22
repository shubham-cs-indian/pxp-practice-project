package com.cs.core.config.interactor.model.relationship;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = GetRelationshipModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class GetRelationshipModel extends ConfigResponseWithAuditLogModel implements IGetRelationshipModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected IRelationship                      entity;
  protected IConfigDetailsForRelationshipModel configDetails;
  
  @Override
  public IRelationship getEntity()
  {
    return entity;
  }
  
  @Override
  @JsonDeserialize(as = Relationship.class)
  public void setEntity(IRelationship entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IConfigDetailsForRelationshipModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForRelationshipModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForRelationshipModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
