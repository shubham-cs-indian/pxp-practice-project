package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetAllDataRulesResponseModel extends IModel {
  
  public static final String LIST           = "list";
  public static final String COUNT          = "count";
  public static final String LANGUAGES_INFO = "languagesInfo";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IBulkSaveDataRuleModel> getList();
  
  public void setList(List<IBulkSaveDataRuleModel> list);
  
  public Map<String, IIdLabelCodeModel> getlanguagesInfo();
  
  public void setLanguagesInfo(Map<String, IIdLabelCodeModel> languagesInfo);
}
