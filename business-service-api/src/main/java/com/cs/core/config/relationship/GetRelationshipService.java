package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.usecase.relationship.IGetRelationshipStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetRelationshipService extends AbstractGetConfigService<IIdParameterModel, IGetRelationshipModel>
    implements IGetRelationshipService {
  
  @Autowired
  protected IGetRelationshipStrategy fileGetRelationshipStrategy;
  
  @Override
  public IGetRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return fileGetRelationshipStrategy.execute(dataModel);
  }
}
