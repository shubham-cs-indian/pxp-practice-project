package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;

import java.util.List;

public interface ISaveAttributeModel extends IAttributeModel {
  
  public static final String ADDED_TAGS    = "addedTags";
  public static final String MODIFIED_TAGS = "modifiedTags";
  public static final String DELETED_TAGS  = "deletedTags";
  
  public List<IAddedVariantContextTagsModel> getAddedTags();
  
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags);
  
  public List<IVariantContextModifiedTagsModel> getModifiedTags();
  
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
}
