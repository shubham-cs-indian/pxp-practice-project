package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedVariantContextModel extends IVariantContext, IModel {
  
  public static final String TAGS                   = "tags";
  public static final String PROPERTY_COLLECTIONS   = "propertyCollections";
  public static final String REFERENCED_STATUS_TAGS = "referencedStatusTags";
  public static final String CHILDREN               = "children";
  public static final String CAN_EDIT               = "canEdit";
  public static final String CAN_DELETE             = "canDelete";
  public static final String CAN_CREATE             = "canCreate";
  public static final String CONTEXT_KLASS_ID       = "contextKlassId";
  public static final String ENTITY_IDS             = "entityIds";
  
  public List<IIdParameterModel> getPropertyCollections();
  
  public void setPropertyCollections(List<IIdParameterModel> propertyCollections);
  
  public List<IReferencedVariantContextTagsModel> getTags();
  
  public void setTags(List<IReferencedVariantContextTagsModel> tagValueIds);
  
  public List<String> getReferencedStatusTags();
  
  public void setReferencedStatusTags(List<String> referencedStatusTags);
  
  public List<String> getChildren();
  
  public void setChildren(List<String> children);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
  
  public List<String> getEntityIds();
  
  public void setEntityIds(List<String> entityIds);
}
