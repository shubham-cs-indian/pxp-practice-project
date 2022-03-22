package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;

public interface IGetSupplierInstanceForTasksTabService extends IRuntimeService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel> {
  
}
