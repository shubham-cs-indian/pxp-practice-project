package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.relationship.IGetRelationshipService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetRelationship
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetRelationshipModel>
    implements IGetRelationship {
  
  @Autowired
  protected IGetRelationshipService getRelationshipService;
  
  @Override
  public IGetRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getRelationshipService.execute(dataModel);
  }
}
