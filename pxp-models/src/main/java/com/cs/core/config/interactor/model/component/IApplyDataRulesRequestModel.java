package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IApplyDataRulesRequestModel extends IModel, IIdsListParameterModel {
  
  public static final String DATA_RULES              = "dataRules";
  public static final String DATA_RULE_IDS_TO_REMOVE = "dataRuleIdsToRemove";
  
  public IListModel<IDataRuleModel> getDataRules();
  
  public void setDataRules(IListModel<IDataRuleModel> dataRules);
  
  public List<String> getDataRuleIdsToRemove();
  
  public void setDataRuleIdsToRemove(List<String> dataRuleIdsToRemove);
}
