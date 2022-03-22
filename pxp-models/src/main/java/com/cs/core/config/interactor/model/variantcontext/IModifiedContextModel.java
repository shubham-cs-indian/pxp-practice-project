package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedContextModel extends IVariantContext, IModel {
  
  public static final String ADDED_TAGS       = "addedTags";
  public static final String MODIFIED_TAGS    = "modifiedTags";
  public static final String DELETED_TAGS     = "deletedTags";
  public static final String ADDED_ENTITIES   = "addedEntities";
  public static final String DELETED_ENTITIES = "deletedEntities";
  
  public List<IAddedVariantContextTagsModel> getAddedTags();
  
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags);
  
  public List<IVariantContextModifiedTagsModel> getModifiedTags();
  
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<String> getAddedEntities();
  
  public void setAddedEntities(List<String> addedEntities);
  
  public List<String> getDeletedEntities();
  
  public void setDeletedEntities(List<String> deletedEntities);
}
