package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class RestoreSupplierInstanceService extends AbstractRestoreInstanceService<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreSupplierInstanceService {
  
  @Override
  protected String getBaseType()
  {
    return Constants.SUPPLIER_INSTANCE_BASE_TYPE;
  }
  
}
