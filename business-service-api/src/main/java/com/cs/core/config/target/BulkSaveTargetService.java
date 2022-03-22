package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;
import com.cs.core.config.strategy.usecase.target.IBulkSaveTargetStrategy;

@Component
public class BulkSaveTargetService
    extends AbstractSaveConfigService<IListModel<ITargetKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveTargetService {
  
  @Autowired
  IBulkSaveTargetStrategy bulkSaveTargetStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITargetKlassSaveModel> klassListModel)
      throws Exception
  {
    return bulkSaveTargetStrategy.execute(klassListModel);
  }
}
