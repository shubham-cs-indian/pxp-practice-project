package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IEntityRelationshipInfoModel;
import java.util.List;

public interface IRelationshipInheritanceInfoModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID        = "sourceContentId";
  public static final String SOURCE_CONTENT_BASE_TYPE = "sourceContentBaseType";
  public static final String RELATIONSHIP_INFO        = "relationshipInfo";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getSourceContentBaseType();
  
  public void setSourceContentBaseType(String sourceContentBaseType);
  
  public List<IEntityRelationshipInfoModel> getRelationshipInfo();
  
  public void setRelationshipInfo(List<IEntityRelationshipInfoModel> relationshipInfo);
}
