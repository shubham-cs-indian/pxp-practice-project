package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveBasicTranslationsRequestModel extends AbstractSaveTranslationsRequestModel
    implements ISaveBasicTranslationRequestModel {
  
  private static final long                  serialVersionUID = 1L;
  protected List<ISaveBasicTranslationModel> data;
  
  @Override
  public List<ISaveBasicTranslationModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveBasicTranslationModel.class)
  public void setData(List<ISaveBasicTranslationModel> data)
  {
    if (data == null) {
      data = new ArrayList<>();
    }
    this.data = data;
  }
}
