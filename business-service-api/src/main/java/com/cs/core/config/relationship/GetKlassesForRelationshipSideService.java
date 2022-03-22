package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.klass.IGetKlassesForRelationshipSideService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesForRelationshipSideStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassesForRelationshipSideService extends AbstractGetConfigService<IIdParameterModel, IGetKlassesForRelationshipModel>
    implements IGetKlassesForRelationshipSideService {
  
  @Autowired
  protected IGetKlassesForRelationshipSideStrategy getKlassesForRelationshipSideStrategy;
  
  @Override
  public IGetKlassesForRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getKlassesForRelationshipSideStrategy.execute(dataModel);
  }
}
