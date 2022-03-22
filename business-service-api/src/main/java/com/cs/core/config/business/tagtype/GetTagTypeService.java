package com.cs.core.config.business.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.strategy.usecase.tagtype.IGetTagTypeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTagTypeService extends AbstractGetConfigService<IIdParameterModel, ITagTypeModel>
    implements IGetTagTypeService {
  
  @Autowired
  IGetTagTypeStrategy neo4jGetTagTypeStrategy;
  
  @Override
  public ITagTypeModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return neo4jGetTagTypeStrategy.execute(dataModel);
  }
}
