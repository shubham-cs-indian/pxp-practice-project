package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.klass.IGetKlassService;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassService extends AbstractGetConfigService<IIdParameterModel, IGetKlassModel>
    implements IGetKlassService {
  
  @Autowired
  IGetKlassStrategy      neo4jGetKlassStrategy;
  
  @Autowired
  IGetKlassesStrategy    neo4jGetKlassesByIdsStrategy;
  
  @Autowired
  IGetAttributesStrategy neo4jGetAttributesByIdsStrategy;
  
  @Override
  public IGetKlassModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return neo4jGetKlassStrategy.execute(klassModel);
  }
}
