package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.klass.AbstractCreateKlassService;
import com.cs.core.config.strategy.usecase.supplier.ICreateSupplierStrategy;

@Service
public class CreateSupplierService extends
    AbstractCreateKlassService<ISupplierModel, IGetKlassEntityWithoutKPModel> implements ICreateSupplierService {
  
  @Autowired
  protected ICreateSupplierStrategy orientdbCreateSupplierStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeCreateKlass(ISupplierModel klassModel) throws Exception
  {
    return orientdbCreateSupplierStrategy.execute(klassModel);
  }
}
