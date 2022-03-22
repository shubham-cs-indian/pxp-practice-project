package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.context.IContentInfoForContextualValueInheritanceModel;
import com.cs.core.config.interactor.model.context.IContextualPropertiesToInheritAttributeModel;
import com.cs.core.config.interactor.model.context.IContextualPropertiesToInheritTagModel;
import com.cs.core.runtime.interactor.model.context.ContextualPropertiesToInheritAttributeModel;
import com.cs.core.runtime.interactor.model.context.ContextualPropertiesToInheritTagModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ContentInfoForContextualValueInheritanceModel
    implements IContentInfoForContextualValueInheritanceModel {
  
  private static final long                                           serialVersionUID = 1L;
  
  protected String                                                    contentId;
  protected String                                                    baseType;
  protected Map<String, IContextualPropertiesToInheritAttributeModel> addedAttributes;
  protected Map<String, IContextualPropertiesToInheritAttributeModel> modifiedAttributes;
  protected Map<String, IContextualPropertiesToInheritAttributeModel> deletedAttributes;
  protected Map<String, IContextualPropertiesToInheritTagModel>       addedTags;
  protected Map<String, IContextualPropertiesToInheritTagModel>       modifiedTags;
  protected Map<String, IContextualPropertiesToInheritTagModel>       deletedTags;
  
  @Override
  public Map<String, IContextualPropertiesToInheritAttributeModel> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new HashMap<>();
    }
    return addedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritAttributeModel.class)
  public void setAddedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public Map<String, IContextualPropertiesToInheritAttributeModel> getModifiedAttributes()
  {
    if (modifiedAttributes == null) {
      modifiedAttributes = new HashMap<>();
    }
    return modifiedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritAttributeModel.class)
  public void setModifiedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public Map<String, IContextualPropertiesToInheritAttributeModel> getDeletedAttributes()
  {
    if (deletedAttributes == null) {
      deletedAttributes = new HashMap<>();
    }
    return deletedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritAttributeModel.class)
  public void setDeletedAttributes(
      Map<String, IContextualPropertiesToInheritAttributeModel> deletedAttributess)
  {
    this.deletedAttributes = deletedAttributess;
  }
  
  @Override
  public Map<String, IContextualPropertiesToInheritTagModel> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new HashMap<>();
    }
    return addedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritTagModel.class)
  public void setAddedTags(Map<String, IContextualPropertiesToInheritTagModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public Map<String, IContextualPropertiesToInheritTagModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new HashMap<>();
    }
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritTagModel.class)
  public void setModifiedTags(Map<String, IContextualPropertiesToInheritTagModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public Map<String, IContextualPropertiesToInheritTagModel> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new HashMap<>();
    }
    return deletedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextualPropertiesToInheritTagModel.class)
  public void setDeletedTags(Map<String, IContextualPropertiesToInheritTagModel> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
