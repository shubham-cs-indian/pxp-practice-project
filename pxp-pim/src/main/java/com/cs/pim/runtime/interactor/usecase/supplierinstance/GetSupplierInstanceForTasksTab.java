package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IGetSupplierInstanceForTasksTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierInstanceForTasksTab
    extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
    implements IGetSupplierInstanceForTasksTab {

  @Autowired
  protected IGetSupplierInstanceForTasksTabService getSupplierInstanceForTasksTabService;

  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
      return getSupplierInstanceForTasksTabService.execute(getKlassInstanceTreeStrategyModel);
  }

}
