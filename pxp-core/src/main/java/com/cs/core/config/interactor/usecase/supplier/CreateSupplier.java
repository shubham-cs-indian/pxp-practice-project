package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.supplier.ICreateSupplierService;

@Service
public class CreateSupplier extends
    AbstractCreateConfigInteractor<ISupplierModel, IGetKlassEntityWithoutKPModel> implements ICreateSupplier {
  
  @Autowired
  protected ICreateSupplierService createSupplierService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(ISupplierModel klassModel) throws Exception
  {
    return createSupplierService.execute(klassModel);
  }
}
