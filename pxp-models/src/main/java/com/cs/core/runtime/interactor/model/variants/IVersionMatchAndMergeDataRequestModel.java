package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IVersionMatchAndMergeDataRequestModel extends IModel {
  
  public static final String ID                          = "id";
  public static final String PROPAGABLE_PROPERTY_IDS     = "propagablePropertyIds";
  public static final String VARIANT_CONTEXT_IDS         = "variantContextIds";
  public static final String REFERENCED_RELATIONSHIP_IDS = "referencedRelationshipIds";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getPropagablePropertyIds();
  
  public void setPropagablePropertyIds(List<String> propagablePropertyIds);
  
  public List<String> getVariantContextIds();
  
  public void setVariantContextIds(List<String> variantContextIds);
  
  public List<String> getReferencedRelationshipIds();
  
  public void setReferencedRelationshipIds(List<String> referencedRelationshipIds);
}
