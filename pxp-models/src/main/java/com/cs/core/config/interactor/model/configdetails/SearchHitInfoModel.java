package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SearchHitInfoModel extends ConfigEntityTreeInformationModel
    implements ISearchHitInfoModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected List<ISearchHitInfoModel> values;
  protected String                    contextInstanceId;
  
  @Override
  public List<ISearchHitInfoModel> getValues()
  {
    return values;
  }
  
  @JsonDeserialize(contentAs = SearchHitInfoModel.class)
  @Override
  public void setValues(List<ISearchHitInfoModel> values)
  {
    this.values = values;
  }
  
  @Override
  public String getContextInstanceId()
  {
    return contextInstanceId;
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    this.contextInstanceId = contextInstanceId;
  }
}
