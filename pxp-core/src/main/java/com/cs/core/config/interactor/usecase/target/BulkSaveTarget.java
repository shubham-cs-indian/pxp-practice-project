package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;
import com.cs.core.config.target.IBulkSaveTargetService;

@Component
public class BulkSaveTarget
    extends AbstractSaveConfigInteractor<IListModel<ITargetKlassSaveModel>, IPluginSummaryModel>
    implements IBulkSaveTarget {
  
  @Autowired
  IBulkSaveTargetService bulkSaveTargetService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITargetKlassSaveModel> klassListModel)
      throws Exception
  {
    return bulkSaveTargetService.execute(klassListModel);
  }
}
