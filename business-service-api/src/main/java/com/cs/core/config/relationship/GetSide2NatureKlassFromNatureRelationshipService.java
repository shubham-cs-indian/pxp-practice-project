package com.cs.core.config.relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.strategy.usecase.relationship.IGetSide2NatureKlassFromNatureRelationshipStrategy;
import com.cs.core.runtime.interactor.model.configdetails.ISide2NatureKlassFromNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class GetSide2NatureKlassFromNatureRelationshipService
    extends AbstractGetConfigService<IIdParameterModel, ISide2NatureKlassFromNatureRelationshipModel>
    implements IGetSide2NatureKlassFromNatureRelationshipService {
  
  @Autowired
  protected IGetSide2NatureKlassFromNatureRelationshipStrategy getSide2NatureKlassFromFromNatureRelationshipStrategy;
  
  @Override
  protected ISide2NatureKlassFromNatureRelationshipModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    dataModel.setCurrentUserId(context.getUserId());
    return getSide2NatureKlassFromFromNatureRelationshipStrategy.execute(dataModel);
  }
  
}
