package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCreateAttributeStrategy
    extends IConfigStrategy<IListModel<IAttributeModel>, IPluginSummaryModel> {
  
}
