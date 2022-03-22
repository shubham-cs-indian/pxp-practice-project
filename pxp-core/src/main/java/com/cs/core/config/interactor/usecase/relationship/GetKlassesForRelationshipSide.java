package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.interactor.usecase.klass.IGetKlassesForRelationshipSide;
import com.cs.core.config.klass.IGetKlassesForRelationshipSideService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassesForRelationshipSide
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassesForRelationshipModel>
    implements IGetKlassesForRelationshipSide {
  
  @Autowired
  protected IGetKlassesForRelationshipSideService getKlassesForRelationshipSideService;
  
  @Override
  public IGetKlassesForRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    
    return getKlassesForRelationshipSideService.execute(dataModel);
  }
}
