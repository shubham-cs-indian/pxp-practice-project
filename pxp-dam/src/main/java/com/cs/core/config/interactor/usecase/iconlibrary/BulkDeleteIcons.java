package com.cs.core.config.interactor.usecase.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.iconlibrary.IBulkDeleteIconsService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

/**
 * This is service class for bulk icon deletion.
 * 
 * @author jamil.ahmad *
 */

@Service
public class BulkDeleteIcons extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IBulkDeleteIcons {
  
  @Autowired
  protected IBulkDeleteIconsService bulkDeleteIconsService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return bulkDeleteIconsService.execute(model);
  }
}
