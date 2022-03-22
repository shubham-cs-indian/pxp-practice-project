package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.klass.IGetAllKlassesService;
import com.cs.core.config.strategy.usecase.klass.IGetAllKlassesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllKlassesService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<IKlassInformationModel>>
    implements IGetAllKlassesService {
  
  @Autowired
  IGetAllKlassesStrategy getAllKlassesStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllKlassesStrategy.execute(dataModel);
  }
}
