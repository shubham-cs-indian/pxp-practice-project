package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.relationship.ICreateRelationshipService;

@Service
public class CreateRelationship
    extends AbstractCreateConfigInteractor<ICreateRelationshipModel, IGetRelationshipModel>
    implements ICreateRelationship {
  
  @Autowired
  protected ICreateRelationshipService createRelationshipService;
  
  @Override
  public IGetRelationshipModel executeInternal(ICreateRelationshipModel dataModel) throws Exception
  {
    return createRelationshipService.execute(dataModel);
  }
}
