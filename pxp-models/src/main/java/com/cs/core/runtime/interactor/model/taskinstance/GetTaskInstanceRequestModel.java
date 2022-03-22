package com.cs.core.runtime.interactor.model.taskinstance;

public class GetTaskInstanceRequestModel implements IGetTaskInstanceRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          contentId;
  protected String          variantId;
  protected String          elementId;
  protected String          baseType;
  protected Boolean         getAll;
  protected Boolean         count;
  
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
  public String getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getElementId()
  {
    return elementId;
  }
  
  @Override
  public void setElementId(String elementId)
  {
    this.elementId = elementId;
  }
  
  @Override
  public Boolean getGetAll()
  {
    return getAll;
  }
  
  @Override
  public void setGetAll(Boolean getAll)
  {
    this.getAll = getAll;
  }
  
  @Override
  public Boolean getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Boolean count)
  {
    this.count = count;
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
