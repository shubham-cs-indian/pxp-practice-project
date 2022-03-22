package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveKlassStrategy
    extends IConfigStrategy<IListModel<IKlassSaveModel>, IPluginSummaryModel> {
  
}
