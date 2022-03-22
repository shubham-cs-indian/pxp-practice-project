package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveRelationshipsStrategy extends
    IConfigStrategy<IListModel<IBulkSaveRelationshipsModel>, IBulkSaveRelationshipsResponseModel> {
  
}
