package com.cs.core.config.interactor.model.translations;

public class GetTagTranslationsModel extends GetPropertyTranslationsModel
    implements IGetTagTranslationsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          tagType;
  
  @Override
  public String getTagType()
  {
    return tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
}
