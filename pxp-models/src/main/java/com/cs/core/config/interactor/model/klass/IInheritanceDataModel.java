package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IInheritanceDataModel extends IModel {
  
  public static final String TARGET_CONTENT_ID       = "targetContentId";
  public static final String RELATIONSHIP_PROPERTIES = "relationshipProperties";
  public static final String BASE_TYPE               = "baseType";
  public static final String SOURCE_CONTENT_ID       = "sourceContentId";
  public static final String IS_MERGED               = "isMerged";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getTargetContentId();
  
  public void setTargetContentId(String targetContentId);
  
  public List<IRelationshipPropertiesToInheritModel> getRelationshipProperties();
  
  public void setRelationshipProperties(
      List<IRelationshipPropertiesToInheritModel> relationshipProperties);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getIsMerged();
  
  public void setIsMerged(Boolean isMerged);
}
