package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.entity.variantcontext.VariantContext;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ReferencedVariantContextModel extends VariantContext
    implements IReferencedVariantContextModel {
  
  private static final long                          serialVersionUID    = 1L;
  
  protected List<IReferencedVariantContextTagsModel> tags                = new ArrayList<>();
  protected List<IIdParameterModel>                  propertyCollections = new ArrayList<>();
  protected List<String>                             referencedStatusTags;
  protected List<String>                             children;
  protected Boolean                                  canEdit             = false;
  protected Boolean                                  canDelete           = false;
  protected Boolean                                  canCreate           = false;
  protected String                                   contextKlassId;
  protected List<String>                             entityIds;
  
  protected IVariantContext                          entity;
  
  public ReferencedVariantContextModel()
  {
    this.entity = new VariantContext();
  }
  
  @Override
  public List<String> getEntityIds()
  {
    return entityIds;
  }
  
  @Override
  public void setEntityIds(List<String> entityIds)
  {
    this.entityIds = entityIds;
  }
  
  @Override
  public List<String> getReferencedStatusTags()
  {
    if (referencedStatusTags == null) {
      referencedStatusTags = new ArrayList<>();
    }
    return referencedStatusTags;
  }
  
  @Override
  public void setReferencedStatusTags(List<String> referencedStatusTags)
  {
    this.referencedStatusTags = referencedStatusTags;
  }
  
  @Override
  public List<IReferencedVariantContextTagsModel> getTags()
  {
    
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextTagsModel.class)
  public void setTags(List<IReferencedVariantContextTagsModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IIdParameterModel> getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdParameterModel.class)
  public void setPropertyCollections(List<IIdParameterModel> sections)
  {
    this.propertyCollections = sections;
  }
  
  @Override
  public List<String> getChildren()
  {
    if (children == null) {
      return new ArrayList<String>();
    }
    return children;
  }
  
  @Override
  public void setChildren(List<String> children)
  {
    this.children = children;
  }
  
  @Override
  public Boolean getCanEdit()
  {
    return canEdit;
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    this.canEdit = canEdit;
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return canDelete;
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    this.canDelete = canDelete;
  }
  
  @Override
  public Boolean getCanCreate()
  {
    return canCreate;
  }
  
  @Override
  public void setCanCreate(Boolean canCreate)
  {
    this.canCreate = canCreate;
  }
  
  @Override
  public String getContextKlassId()
  {
    return contextKlassId;
  }
  
  @Override
  public void setContextKlassId(String contextKlassId)
  {
    this.contextKlassId = contextKlassId;
  }
}
