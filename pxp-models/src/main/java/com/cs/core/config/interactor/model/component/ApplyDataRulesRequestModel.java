package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ApplyDataRulesRequestModel extends IdsListParameterModel
    implements IApplyDataRulesRequestModel {
  
  private static final long            serialVersionUID    = 1L;
  protected IListModel<IDataRuleModel> dataRules;
  protected List<String>               dataRuleIdsToRemove = new ArrayList<>();
  
  @Override
  public IListModel<IDataRuleModel> getDataRules()
  {
    if (dataRules == null) {
      dataRules = new ListModel<>();
    }
    return dataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setDataRules(IListModel<IDataRuleModel> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public List<String> getDataRuleIdsToRemove()
  {
    return dataRuleIdsToRemove;
  }
  
  @Override
  public void setDataRuleIdsToRemove(List<String> dataRuleIdsToRemove)
  {
    this.dataRuleIdsToRemove = dataRuleIdsToRemove;
  }
}
