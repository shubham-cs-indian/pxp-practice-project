package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.relationship.ISaveRelationshipService;

@Service
public class SaveRelationship extends AbstractSaveConfigInteractor<ISaveRelationshipModel, IGetRelationshipModel>
    implements ISaveRelationship {
  
  @Autowired
  protected ISaveRelationshipService saveRelationshipService;
  
  @Override
  public IGetRelationshipModel executeInternal(ISaveRelationshipModel dataModel) throws Exception
  {
    return saveRelationshipService.execute(dataModel);
  }
}
