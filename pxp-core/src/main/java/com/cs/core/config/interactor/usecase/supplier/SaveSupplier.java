package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.supplier.ISaveSupplierService;

@Service
public class SaveSupplier extends AbstractSaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveSupplier {
  
  @Autowired
  ISaveSupplierService saveSupplierService;
  
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IKlassSaveModel klassModel) throws Exception
  {
    return saveSupplierService.execute(klassModel);
  }
  
}
