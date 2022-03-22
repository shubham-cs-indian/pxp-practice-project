package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetAllDataRulesResponseModel implements IGetAllDataRulesResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IBulkSaveDataRuleModel>   list;
  protected Long                           count;
  protected Map<String, IIdLabelCodeModel> languagesInfo;
  
  @Override
  public List<IBulkSaveDataRuleModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = BulkSaveDataRuleModel.class)
  public void setList(List<IBulkSaveDataRuleModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getlanguagesInfo()
  {
    return languagesInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setLanguagesInfo(Map<String, IIdLabelCodeModel> languagesInfo)
  {
    this.languagesInfo = languagesInfo;
  }
}
