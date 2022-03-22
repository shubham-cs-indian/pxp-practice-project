package com.cs.core.config.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.dam.config.strategy.usecase.iconlibrary.IBulkDeleteIconsStrategy;

/**
 * This is service class for bulk icon deletion.
 * 
 * @author jamil.ahmad *
 */

@Service
public class BulkDeleteIconsService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IBulkDeleteIconsService {
  
  @Autowired
  protected IBulkDeleteIconsStrategy bulkDeleteIconsStrategy;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    // Call Orient strategy and return model
    return bulkDeleteIconsStrategy.execute(model);
  }
  
}
