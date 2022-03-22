package com.cs.core.config.businessapi.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.attribute.IGetAllAttributesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllAttributesService extends AbstractGetConfigService<IIdParameterModel, IListModel<IAttributeModel>>
    implements IGetAllAttributesService {
  
  @Autowired
  IGetAllAttributesStrategy neo4jGetAttributesStrategy;
  
  @Override
  public IListModel<IAttributeModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return neo4jGetAttributesStrategy.execute(dataModel);
  }
}
