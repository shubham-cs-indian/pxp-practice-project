package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ConfigDetailsForGetRelationshipDataModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetRelationshipDataModel;

@Component
public class GetConfigDetailsForGetRelationshipDataFromSourcesStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy {
  
  @Override
  public IConfigDetailsForGetRelationshipDataModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_RELATIONSHIP_DATA, model, ConfigDetailsForGetRelationshipDataModel.class);
  }
}