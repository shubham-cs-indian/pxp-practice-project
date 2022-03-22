package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.klass.AbstractDeleteKlassesService;
import com.cs.core.config.strategy.usecase.supplier.IDeleteSuppliersStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteSupplierService
    extends AbstractDeleteKlassesService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteSupplierService {
  
  @Autowired
  protected IDeleteSuppliersStrategy deleteTargetsStrategy;
  
  @Override
  protected String getKlassType()
  {
    return Constants.SUPPLIER_KLASS_TYPE;
  }
  
  @Override
  protected IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception
  {
    return deleteTargetsStrategy.execute(model);
  }
}
