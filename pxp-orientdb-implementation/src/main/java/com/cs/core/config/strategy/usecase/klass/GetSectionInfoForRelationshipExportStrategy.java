package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.GetSectionInfoForRelationshipExportResponseModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetSectionInfoForRelationshipExportStrategy extends OrientDBBaseStrategy
    implements IGetSectionInfoForRelationshipExportStrategy {
  
  @Override
  public IGetSectionInfoForRelationshipExportResponseModel execute(IGetSectionInfoForRelationshipExportModel model) throws Exception
  {
    return execute(GET_SECTION_INFO_FOR_RELATIONSHIP_EXPORT, model, GetSectionInfoForRelationshipExportResponseModel.class);
  }
  
}