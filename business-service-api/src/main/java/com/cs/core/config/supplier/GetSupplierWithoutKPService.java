package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSupplierWithoutKPStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetSupplierWithoutKPService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetSupplierWithoutKPService {
  
  @Autowired
  protected IGetSupplierWithoutKPStrategy getSupplierWithoutKPStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierWithoutKPStrategy.execute(idModel);
  }
}
