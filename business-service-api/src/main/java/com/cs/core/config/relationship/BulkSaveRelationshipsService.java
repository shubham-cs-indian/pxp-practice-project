package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.strategy.usecase.relationship.IBulkSaveRelationshipsStrategy;

@Service
public class BulkSaveRelationshipsService
    extends AbstractSaveConfigService<IListModel<IBulkSaveRelationshipsModel>, IBulkSaveRelationshipsResponseModel>
    implements IBulkSaveRelationshipsService {
  
  @Autowired
  protected IBulkSaveRelationshipsStrategy bulkSaveRelationshipsStrategy;
  
  @Override
  public IBulkSaveRelationshipsResponseModel executeInternal(IListModel<IBulkSaveRelationshipsModel> dataModel) throws Exception
  {
    return bulkSaveRelationshipsStrategy.execute(dataModel);
  }
}
