package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSupplierStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSupplierService extends AbstractGetConfigService<IIdParameterModel, ISupplierModel>
    implements IGetSupplierService {
  
  @Autowired
  IGetSupplierStrategy getSupplierStrategy;
  
  @Override
  public ISupplierModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierStrategy.execute(idModel);
  }
}
