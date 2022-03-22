package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCreatePropertyCollectionsStrategy
    extends IConfigStrategy<IListModel<IPropertyCollectionModel>, IPluginSummaryModel> {
  
}
