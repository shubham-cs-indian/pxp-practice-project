package com.cs.core.config.interactor.model.translations;

public interface IGetTagTranslationsModel extends IGetPropertyTranslationsModel {
  
  public static final String TAG_TYPE = "tagType";
  
  public String getTagType();
  
  public void setTagType(String tagType);
}
