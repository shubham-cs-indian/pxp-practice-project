package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface IRelationshipConflictingSource extends IRuntimeEntity {
  
  public static final String SOURCE_CONTENT_ID        = "sourceContentId";
  public static final String SOURCE_CONTENT_BASE_TYPE = "sourceContentBaseType";
  public static final String RELATIONSHIP_ID          = "relationshipId";
  public static final String RELATIONHSIP_SIDE_ID     = "relationshipSideId";
  public static final String COUPLING_TYPE            = "couplingType";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getSourceContentBaseType();
  
  public void setSourceContentBaseType(String sourceContentBaseType);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipSideId();
  
  public void setRelationshipSideId(String relationshipSideId);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
}
