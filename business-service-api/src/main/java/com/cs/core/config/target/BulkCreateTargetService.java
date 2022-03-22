package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.usecase.target.IBulkCreateTargetStrategy;

@Component
public class BulkCreateTargetService
    extends AbstractCreateConfigService<IListModel<ITargetModel>, IPluginSummaryModel>
    implements IBulkCreateTargetService {
  
  @Autowired
  IBulkCreateTargetStrategy bulkCreateTargetStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITargetModel> klassListModel) throws Exception
  {
    return bulkCreateTargetStrategy.execute(klassListModel);
  }
}
