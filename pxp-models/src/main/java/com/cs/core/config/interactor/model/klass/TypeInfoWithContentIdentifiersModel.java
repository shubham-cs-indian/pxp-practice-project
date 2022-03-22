package com.cs.core.config.interactor.model.klass;

public class TypeInfoWithContentIdentifiersModel extends TypesListModel
    implements ITypeInfoWithContentIdentifiersModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contentId;
  protected String          baseType;
  protected String          name;
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
}
