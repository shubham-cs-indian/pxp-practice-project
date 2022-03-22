package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.dataintegration.IRelationshipsInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.RelationshipsInfoModel;
import org.springframework.stereotype.Component;

@Component
public class OrientDBGetRootRelationshipsByIdsForDiImportStrategy extends OrientDBBaseStrategy
    implements IGetRootRelationshipsByIdsForDiImportStrategy {
  
  @Override
  public IRelationshipsInfoModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(GET_ROOT_RELATIONSHIPS_BY_IDS_FOR_DI_IMPORT, model,
        RelationshipsInfoModel.class);
  }
}
