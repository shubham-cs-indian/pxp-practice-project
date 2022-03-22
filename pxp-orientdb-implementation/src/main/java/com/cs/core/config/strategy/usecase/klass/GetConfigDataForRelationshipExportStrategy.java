package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetConfigDataForRelationshipExportModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetConfigDataForRelationshipExportStrategy;

@Component
public class GetConfigDataForRelationshipExportStrategy extends OrientDBBaseStrategy
    implements IGetConfigDataForRelationshipExportStrategy {
  
  @Override
  public IGetConfigDataResponseModel execute(IGetConfigDataForRelationshipExportModel dataModel)
      throws Exception
  {
    return execute(GET_CONFIG_DATA_FOR_RELATIONSHIP_EXPORT, dataModel, GetConfigDataResponseModel.class);
  }
}
