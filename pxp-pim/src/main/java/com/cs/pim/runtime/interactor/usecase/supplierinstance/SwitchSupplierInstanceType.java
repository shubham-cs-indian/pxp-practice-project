package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.ISwitchSupplierInstanceTypeService;

@Service
public class SwitchSupplierInstanceType extends AbstractRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchSupplierInstanceType {
  
  @Autowired
  protected ISwitchSupplierInstanceTypeService switchSupplierInstanceTypeService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    return switchSupplierInstanceTypeService.execute(typeSwitchModel);
  }
}
