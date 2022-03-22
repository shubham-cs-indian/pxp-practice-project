package com.cs.core.config.interactor.usecase.relationship;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;

public interface IBulkSaveRelationships extends
    ISaveConfigInteractor<IListModel<IBulkSaveRelationshipsModel>, IBulkSaveRelationshipsResponseModel> {
  
}
