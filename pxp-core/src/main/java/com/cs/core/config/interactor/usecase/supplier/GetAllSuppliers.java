package com.cs.core.config.interactor.usecase.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.supplier.IGetAllSuppliersService;

@Service
public class GetAllSuppliers
    extends AbstractGetConfigInteractor<ISupplierModel, IListModel<IKlassInformationModel>>
    implements IGetAllSuppliers {
  
  @Autowired
  IGetAllSuppliersService getAllSuppliersService;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(ISupplierModel model) throws Exception
  {
    return getAllSuppliersService.execute(model);
  }
}
