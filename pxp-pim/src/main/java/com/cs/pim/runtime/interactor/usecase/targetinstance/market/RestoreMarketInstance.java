package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractRestoreInstance;
import com.cs.pim.runtime.targetinstance.market.IRestoreMarketInstanceService;

@Service
public class RestoreMarketInstance extends AbstractRestoreInstance<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreMarketInstance {
  
  @Autowired
  protected IRestoreMarketInstanceService restoreMarketInstanceService;
  
  @Override
  protected IBulkResponseModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return restoreMarketInstanceService.execute(model);
  }
  
}
