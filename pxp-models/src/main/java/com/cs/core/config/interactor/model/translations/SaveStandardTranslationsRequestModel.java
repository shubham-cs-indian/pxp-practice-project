package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveStandardTranslationsRequestModel extends AbstractSaveTranslationsRequestModel
    implements ISaveStandardTranslationsRequestModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<ISaveStandardTranslationModel> data;
  
  @Override
  public List<ISaveStandardTranslationModel> getData()
  {
    return data;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveTranslationModel.class)
  public void setData(List<ISaveStandardTranslationModel> data)
  {
    if (data == null) {
      data = new ArrayList<>();
    }
    this.data = data;
  }
}
