package com.cs.core.runtime.interactor.model.taxonomy;

public interface IGetTaxonomyTreeForVariantLinkedInstancesQuicklistModel
    extends IGetTaxonomyTreeModel {
  
  public static final String ENTITY_ID = "entityId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
}
