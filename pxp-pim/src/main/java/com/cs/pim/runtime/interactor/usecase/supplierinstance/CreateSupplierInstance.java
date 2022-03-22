package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.core.runtime.interactor.usecase.supplierinstance.ICreateSupplierInstance;
import com.cs.pim.runtime.supplierinstance.ICreateSupplierInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSupplierInstance extends AbstractCreateInstance<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateSupplierInstance {
  
  @Autowired
  protected ICreateSupplierInstanceService createSupplierInstanceService;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
  {
      return createSupplierInstanceService.execute(model);
  }
}
