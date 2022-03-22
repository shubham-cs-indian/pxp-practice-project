package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkCreateRelationshipsStrategy
    extends IConfigStrategy<IListModel<IRelationshipModel>, IPluginSummaryModel> {
  
}
