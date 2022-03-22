package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.supplier.IGetSupplierService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSupplier extends AbstractGetConfigInteractor<IIdParameterModel, ISupplierModel>
    implements IGetSupplier {
  
  @Autowired
  IGetSupplierService getSupplierService;
  
  @Override
  public ISupplierModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierService.execute(idModel);
  }
}
