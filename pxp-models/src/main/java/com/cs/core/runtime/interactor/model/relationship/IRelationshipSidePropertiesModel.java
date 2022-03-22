package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRelationshipSidePropertiesModel extends IModel {
  
  public static final String KLASS_IDS            = "klassIds";
  public static final String TARGET_TYPE          = "targetType";
  public static final String ATTRIBUTES           = "attributes";
  public static final String TAGS                 = "tags";
  public static final String SIDE_ID              = "sideId";
  public static final String DEPENDENT_ATTRIBUTES = "dependentAttributes";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public String getTargetType();
  
  public void setTargetType(String targetType);
  
  public List<IReferencedRelationshipProperty> getAttributes();
  
  public void setAttributes(List<IReferencedRelationshipProperty> attributes);
  
  public List<IReferencedRelationshipProperty> getTags();
  
  public void setTags(List<IReferencedRelationshipProperty> tags);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public List<IReferencedRelationshipProperty> getDependentAttributes();
  
  public void setDependentAttributes(List<IReferencedRelationshipProperty> dependentAttributes);
}
