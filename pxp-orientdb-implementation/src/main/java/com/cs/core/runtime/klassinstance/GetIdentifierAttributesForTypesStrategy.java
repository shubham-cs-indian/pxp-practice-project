package com.cs.core.runtime.klassinstance;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.ConfigDetailsForDataTransferModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetIdentifierAttributesForTypesStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetIdentifierAttributesForTypesStrategy extends OrientDBBaseStrategy
    implements IGetIdentifierAttributesForTypesStrategy {
  
  @Override
  @SuppressWarnings("unchecked")
  public IConfigDetailsForDataTransferModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_IDENTIFIER_ATTRIBUTES_FOR_TYPES, model,
        ConfigDetailsForDataTransferModel.class);
  }
}
