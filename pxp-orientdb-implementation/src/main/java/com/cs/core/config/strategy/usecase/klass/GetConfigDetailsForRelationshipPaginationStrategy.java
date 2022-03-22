package com.cs.core.config.strategy.usecase.klass;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.ConfigDetailsForRelationshipPaginationModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForRelationshipPaginationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForRelationshipPaginationStrategy;

@Component
public class GetConfigDetailsForRelationshipPaginationStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForRelationshipPaginationStrategy {
  
  public static final String useCase = "GetConfigDetailsForRelationshipPagination";
  
  @Override
  public IConfigDetailsForRelationshipPaginationModel execute(
      IMulticlassificationRequestForRelationshipsModel model) throws Exception
  {
    return execute(useCase, model, ConfigDetailsForRelationshipPaginationModel.class);
  }
}
