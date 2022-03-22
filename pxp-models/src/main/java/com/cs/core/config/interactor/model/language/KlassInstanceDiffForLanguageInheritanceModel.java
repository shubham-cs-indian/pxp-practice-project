package com.cs.core.config.interactor.model.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceDiffForLanguageInheritanceModel
    implements IKlassInstanceDiffForLanguageInheritanceModel {
  
  private static final long           serialVersionUID = 1L;
  protected String                    contentId;
  protected String                    baseType;
  protected Map<String, List<String>> addedAttributes;
  protected Map<String, List<String>> modifiedAttributes;
  
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
  
  @Override
  public Map<String, List<String>> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new HashMap<>();
    }
    return addedAttributes;
  }
  
  @Override
  public void setAddedAttributes(Map<String, List<String>> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public Map<String, List<String>> getModifiedAttributes()
  {
    if (modifiedAttributes == null) {
      modifiedAttributes = new HashMap<>();
    }
    return modifiedAttributes;
  }
  
  @Override
  public void setModifiedAttributes(Map<String, List<String>> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
}
