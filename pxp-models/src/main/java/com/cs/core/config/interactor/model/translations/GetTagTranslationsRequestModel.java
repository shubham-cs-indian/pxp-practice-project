package com.cs.core.config.interactor.model.translations;

public class GetTagTranslationsRequestModel extends GetTranslationsRequestModel
    implements IGetTagTranslationsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          parentId;
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
}
