package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveTargetStrategy
    extends IConfigStrategy<IListModel<ITargetKlassSaveModel>, IPluginSummaryModel> {
  
}
