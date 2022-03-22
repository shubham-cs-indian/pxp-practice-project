package com.cs.core.config.target;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;

public interface IBulkSaveTargetService
    extends ISaveConfigService<IListModel<ITargetKlassSaveModel>, IPluginSummaryModel> {
  
}
