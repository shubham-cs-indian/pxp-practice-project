package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.relationship.IBulkSaveRelationshipsService;

@Service
public class BulkSaveRelationships
    extends AbstractSaveConfigInteractor<IListModel<IBulkSaveRelationshipsModel>, IBulkSaveRelationshipsResponseModel>
    implements IBulkSaveRelationships {
  
  @Autowired
  protected IBulkSaveRelationshipsService bulkSaveRelationshipsService;
  
  @Override
  public IBulkSaveRelationshipsResponseModel executeInternal(IListModel<IBulkSaveRelationshipsModel> dataModel) throws Exception
  {
    return bulkSaveRelationshipsService.execute(dataModel);
  }
}
