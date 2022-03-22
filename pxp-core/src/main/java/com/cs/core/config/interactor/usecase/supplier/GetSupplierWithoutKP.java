package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSupplierWithoutKPStrategy;
import com.cs.core.config.supplier.IGetSupplierWithoutKPService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierWithoutKP
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetSupplierWithoutKP {
  
  @Autowired
  protected IGetSupplierWithoutKPService getSupplierWithoutKPService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getSupplierWithoutKPService.execute(idModel);
  }
}
