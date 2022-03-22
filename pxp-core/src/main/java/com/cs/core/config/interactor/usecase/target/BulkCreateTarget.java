package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.target.IBulkCreateTargetService;

@Component
public class BulkCreateTarget
    extends AbstractCreateConfigInteractor<IListModel<ITargetModel>, IPluginSummaryModel>
    implements IBulkCreateTarget {
  
  @Autowired
  IBulkCreateTargetService bulkCreateTargetService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITargetModel> klassListModel) throws Exception
  {
    return bulkCreateTargetService.execute(klassListModel);
  }
}
