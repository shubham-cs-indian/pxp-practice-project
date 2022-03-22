package com.cs.core.config.interactor.model.relationship;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.relationship.IRelationship;

public interface IGetRelationshipModel extends IConfigResponseWithAuditLogModel {
  
  public static final String CONFIG_DETAILS = "configDetails";
  public static final String ENTITY         = "entity";
  
  public IConfigDetailsForRelationshipModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForRelationshipModel configDetails);
  
  public IRelationship getEntity();
  
  public void setEntity(IRelationship entity);
}
