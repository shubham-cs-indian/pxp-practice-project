package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetReferencedRelationshipPropertiesStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetReferencedRelationshipPropertiesStrategy extends OrientDBBaseStrategy
    implements IGetReferencedRelationshipPropertiesStrategy {
  
  public static final String useCase = "GetReferencedRelationshipProperties";
  
  @Override
  @SuppressWarnings("unchecked")
  public IGetReferencedRelationshipPropertiesModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(useCase, model, GetReferencedRelationshipPropertiesModel.class);
  }
}
