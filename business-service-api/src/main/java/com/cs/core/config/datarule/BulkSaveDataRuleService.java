package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleResponseModel;
import com.cs.core.config.strategy.usecase.datarule.IBulkSaveDataRuleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveDataRuleService extends AbstractSaveConfigService<IListModel<IBulkSaveDataRuleModel>, IBulkSaveDataRuleResponseModel>
    implements IBulkSaveDataRuleService {
  
  @Autowired
  protected IBulkSaveDataRuleStrategy bulkSaveDataRuleStrategy;
  
  @Override
  public IBulkSaveDataRuleResponseModel executeInternal(IListModel<IBulkSaveDataRuleModel> ruleModel)
      throws Exception
  {
    return bulkSaveDataRuleStrategy.execute(ruleModel);
  }
}
