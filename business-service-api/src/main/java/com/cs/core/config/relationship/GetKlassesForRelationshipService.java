package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.klass.IGetKlassesForRelationshipService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesForRelationshipStrategy;

@Service
public class GetKlassesForRelationshipService
    extends AbstractGetConfigService<IGetKlassesForRelationshipModel, IGetKlassesForRelationshipModel>
    implements IGetKlassesForRelationshipService {
  
  @Autowired
  protected IGetKlassesForRelationshipStrategy getAllKlassesForRelationshipStrategy;
  
  @Override
  public IGetKlassesForRelationshipModel executeInternal(IGetKlassesForRelationshipModel dataModel) throws Exception
  {
    return getAllKlassesForRelationshipStrategy.execute(dataModel);
  }
}
