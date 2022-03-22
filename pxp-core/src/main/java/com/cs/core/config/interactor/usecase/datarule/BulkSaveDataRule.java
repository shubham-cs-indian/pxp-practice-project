package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.datarule.IBulkSaveDataRuleService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveDataRule extends AbstractSaveConfigInteractor<IListModel<IBulkSaveDataRuleModel>, IBulkSaveDataRuleResponseModel>
    implements IBulkSaveDataRule {
  
  @Autowired
  protected IBulkSaveDataRuleService bulkSaveDataRuleService;
  
  @Override
  public IBulkSaveDataRuleResponseModel executeInternal(IListModel<IBulkSaveDataRuleModel> ruleModel)
      throws Exception
  {
    return bulkSaveDataRuleService.execute(ruleModel);
  }
}
