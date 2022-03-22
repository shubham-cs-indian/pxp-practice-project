package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface ITaxonomyConflictingSource extends IRuntimeEntity{
  
  public static final String SOURCE_CONTENT_ID               = "sourceContentId";
  public static final String SOURCE_CONTENT_BASE_TYPE        = "sourceContentBaseType";
  public static final String RELATIONSHIP_ID                 = "relationshipId";
  public static final String RELATIONHSIP_SIDE_ID            = "relationshipSideId";
  
  public String getSourceContentId();
  public void setSourceContentId(String sourceContentId);
  
  public String getSourceContentBaseType();
  public void setSourceContentBaseType(String sourceContentBaseType);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipSideId();
  public void setRelationshipSideId(String relationshipSideId);

}