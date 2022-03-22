package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSuppliersByIdsStrategy;
import com.cs.core.config.supplier.IGetSuppliersByIdsService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSuppliersByIds
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetSuppliersByIds {
  
  @Autowired
  IGetSuppliersByIdsService getSuppliersByIdsService;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getSuppliersByIdsService.execute(dataModel);
  }
}
