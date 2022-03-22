package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractRestoreInstance;
import com.cs.pim.runtime.supplierinstance.IRestoreSupplierInstanceService;

@Service
public class RestoreSupplierInstance extends AbstractRestoreInstance<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreSupplierInstance {

  @Autowired
  protected IRestoreSupplierInstanceService restoreSupplierInstanceService;
  
  @Override
  protected IBulkResponseModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return restoreSupplierInstanceService.execute(model);
  }
  
}
