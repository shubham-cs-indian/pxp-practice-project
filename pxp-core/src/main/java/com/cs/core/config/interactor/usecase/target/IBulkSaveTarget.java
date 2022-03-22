package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;

public interface IBulkSaveTarget
    extends ISaveConfigInteractor<IListModel<ITargetKlassSaveModel>, IPluginSummaryModel> {
  
}
