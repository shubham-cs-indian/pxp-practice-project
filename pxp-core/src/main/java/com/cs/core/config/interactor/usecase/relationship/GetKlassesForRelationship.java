package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassesForRelationshipModel;
import com.cs.core.config.interactor.usecase.klass.IGetKlassesForRelationship;
import com.cs.core.config.klass.IGetKlassesForRelationshipService;

@Service
public class GetKlassesForRelationship extends
    AbstractGetConfigInteractor<IGetKlassesForRelationshipModel, IGetKlassesForRelationshipModel>
    implements IGetKlassesForRelationship {
  
  @Autowired
  protected IGetKlassesForRelationshipService getAllKlassesForRelationshipService;
  
  @Override
  public IGetKlassesForRelationshipModel executeInternal(IGetKlassesForRelationshipModel dataModel)
      throws Exception
  {
    return getAllKlassesForRelationshipService.execute(dataModel);
  }
}
