package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.klass.IGetKlassesByIdsService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetKlassesByIdsService
    extends AbstractGetConfigService<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetKlassesByIdsService {
  
  @Autowired
  IGetKlassesStrategy neo4jGetKlassesByIdsStrategy;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return neo4jGetKlassesByIdsStrategy.execute(dataModel);
  }
}
