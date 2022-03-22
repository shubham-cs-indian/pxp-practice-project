package com.cs.core.config.interactor.usecase.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.relationship.IGetSide2NatureKlassFromNatureRelationshipService;
import com.cs.core.runtime.interactor.model.configdetails.ISide2NatureKlassFromNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetSide2NatureKlassFromNatureRelationship extends AbstractGetConfigInteractor<IIdParameterModel, ISide2NatureKlassFromNatureRelationshipModel>
    implements IGetSide2NatureKlassFromNatureRelationship {
  
  @Autowired
  protected IGetSide2NatureKlassFromNatureRelationshipService getSide2NatureKlassFromFromNatureRelationshipService;
  
  @Override
  protected ISide2NatureKlassFromNatureRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    dataModel.setCurrentUserId(context.getUserId());
    return getSide2NatureKlassFromFromNatureRelationshipService.execute(dataModel);
  }
  
}
