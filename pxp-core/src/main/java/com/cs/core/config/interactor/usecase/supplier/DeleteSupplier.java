package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.supplier.IDeleteSupplierService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteSupplier
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSupplier {
  
  @Autowired
  protected IDeleteSupplierService deleteSupplierService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    return deleteSupplierService.execute(model);
  }
}
