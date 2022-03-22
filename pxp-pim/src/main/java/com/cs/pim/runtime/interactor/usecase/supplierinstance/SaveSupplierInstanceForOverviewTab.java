package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.ISaveSupplierInstanceForOverviewTabService;

@Service
public class SaveSupplierInstanceForOverviewTab extends AbstractRuntimeInteractor<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceForOverviewTab {
  
  @Autowired
  protected ISaveSupplierInstanceForOverviewTabService saveSupplierInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel) throws Exception
  {
    return saveSupplierInstanceForOverviewTabService.execute(klassInstancesModel);
  }
  
}
