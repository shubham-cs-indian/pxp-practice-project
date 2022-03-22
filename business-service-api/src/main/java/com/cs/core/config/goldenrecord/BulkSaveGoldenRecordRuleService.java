package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkSaveGoldenRecordRuleResponseModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IBulkSaveGoldenRecordRuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkSaveGoldenRecordRuleService extends AbstractSaveConfigService<IListModel<IIdLabelCodeModel>, IBulkSaveGoldenRecordRuleResponseModel>
    implements IBulkSaveGoldenRecordRuleService {
  
  @Autowired
  protected IBulkSaveGoldenRecordRuleStrategy bulkSaveGoldenRecordRuleStrategy;
  
  @Override
  public IBulkSaveGoldenRecordRuleResponseModel executeInternal(IListModel<IIdLabelCodeModel> ruleModel)
      throws Exception
  {
    return bulkSaveGoldenRecordRuleStrategy.execute(ruleModel);
  }
}
