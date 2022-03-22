package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.supplier.IGetSuppliersByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetSuppliersByIdsService
    extends AbstractGetConfigService<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetSuppliersByIdsService {
  
  @Autowired
  IGetSuppliersByIdsStrategy getSuppliersByIdsStrategy;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getSuppliersByIdsStrategy.execute(dataModel);
  }
}
