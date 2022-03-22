package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.ISaveSupplierInstanceForTabsService;

@Service
public class SaveSupplierInstanceForTabs extends AbstractRuntimeInteractor<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceForTabs {
  
  @Autowired
  protected ISaveSupplierInstanceForTabsService saveSupplierInstanceForTabsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveSupplierInstanceForTabsService.execute(klassInstancesModel);
  }
}
