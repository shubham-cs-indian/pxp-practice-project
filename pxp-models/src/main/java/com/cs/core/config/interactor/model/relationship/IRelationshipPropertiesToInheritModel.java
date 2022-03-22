package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRelationshipPropertiesToInheritModel extends IModel {
  
  public static final String RELATIONSHIPID = "relationshipId";
  public static final String ATTRIBUTES     = "attributes";
  public static final String TAGS           = "tags";
  public static final String VARIANT_ID     = "variantId";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<IDefaultValueChangeModel> getAttributes();
  
  public void setAttributes(List<IDefaultValueChangeModel> attributes);
  
  public List<IDefaultValueChangeModel> getTags();
  
  public void setTags(List<IDefaultValueChangeModel> tags);
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
}
