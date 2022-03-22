package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.usecase.supplier.IGetAllSuppliersStrategy;

@Service
public class GetAllSuppliersService
    extends AbstractGetConfigService<ISupplierModel, IListModel<IKlassInformationModel>>
    implements IGetAllSuppliersService {
  
  @Autowired
  IGetAllSuppliersStrategy getAllSuppliersStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ISupplierModel model) throws Exception
  {
    return getAllSuppliersStrategy.execute(model);
  }
}
