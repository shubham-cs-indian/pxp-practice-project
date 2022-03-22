package com.cs.core.config.businessapi.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAttributeService extends AbstractGetConfigService<IIdParameterModel, IAttributeModel>
    implements IGetAttributeService {
  
  @Autowired
  IGetAttributeStrategy neo4jGetAttributeStrategy;
  
  @Override
  public IAttributeModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return neo4jGetAttributeStrategy.execute(dataModel);
  }
}
