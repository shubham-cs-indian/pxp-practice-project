package com.cs.core.config.relationship;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;

public interface IBulkSaveRelationshipsService
    extends ISaveConfigService<IListModel<IBulkSaveRelationshipsModel>, IBulkSaveRelationshipsResponseModel> {
  
}
