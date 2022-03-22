package com.cs.core.runtime.interactor.model.taxonomy;

public class GetTaxonomyTreeForVariantLinkedInstancesQuicklistModel extends GetTaxonomyTreeModel
    implements IGetTaxonomyTreeForVariantLinkedInstancesQuicklistModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          entityId;
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
}
