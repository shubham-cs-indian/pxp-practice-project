package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.goldenrecord.IBulkSaveGoldenRecordRuleService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IBulkSaveGoldenRecordRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveGoldenRecordRule
    extends AbstractSaveConfigInteractor<IListModel<IIdLabelCodeModel>, IBulkSaveGoldenRecordRuleResponseModel>
    implements IBulkSaveGoldenRecordRule {
  
  @Autowired
  protected IBulkSaveGoldenRecordRuleService bulkSaveGoldenRecordRuleService;
  
  @Override
  public IBulkSaveGoldenRecordRuleResponseModel executeInternal(IListModel<IIdLabelCodeModel> ruleModel)
      throws Exception
  {
    return bulkSaveGoldenRecordRuleService.execute(ruleModel);
  }
}
