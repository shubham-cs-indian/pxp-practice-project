package com.cs.core.config.interactor.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IContentInfoForContextualValueInheritanceModel extends IModel {
  
  public static final String CONTENT_ID          = "contentId";
  public static final String BASE_TYPE           = "baseType";
  public static final String ADDED_ATTRIBUTES    = "addedAttributes";
  public static final String MODIFIED_ATTRIBUTES = "modifiedAttributes";
  public static final String DELETED_ATTRIBUTES  = "deletedAttributes";
  public static final String ADDED_TAGS          = "addedTags";
  public static final String MODIFIED_TAGS       = "modifiedTags";
  public static final String DELETED_TAGS        = "deletedTags";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Map<String, IContextualPropertiesToInheritAttributeModel> getAddedAttributes();
  
  public void setAddedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> addedAttributes);
  
  public Map<String, IContextualPropertiesToInheritAttributeModel> getModifiedAttributes();
  
  public void setModifiedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> modifiedAttributes);
  
  public Map<String, IContextualPropertiesToInheritAttributeModel> getDeletedAttributes();
  
  public void setDeletedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> deletedAttributess);
  
  public Map<String, IContextualPropertiesToInheritTagModel> getAddedTags();
  
  public void setAddedTags(Map<String, IContextualPropertiesToInheritTagModel> addedTags);
  
  public Map<String, IContextualPropertiesToInheritTagModel> getModifiedTags();
  
  public void setModifiedTags(Map<String, IContextualPropertiesToInheritTagModel> modifiedTags);
  
  public Map<String, IContextualPropertiesToInheritTagModel> getDeletedTags();
  
  public void setDeletedTags(Map<String, IContextualPropertiesToInheritTagModel> deletedTags);
}
