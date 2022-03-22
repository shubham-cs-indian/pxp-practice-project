package com.cs.core.config.strategy.usecase.concatenatedAttribute;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.model.attribute.ConfigDetailsForCalculatedAttributesResponseModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributeRequestModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributesResponseModel;

@Component
public class GetConfigDetailsForCalculatedAttributeMigrationStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForCalculatedAttributeMigrationStrategy {
  
  public static final String useCase = "GetConfigDetailsForCalculatedAttributeMigration";
  
  @Override
  public IConfigDetailsForCalculatedAttributesResponseModel execute(IConfigDetailsForCalculatedAttributeRequestModel model) throws Exception
  {
    return super.execute(useCase, model, ConfigDetailsForCalculatedAttributesResponseModel.class);
  }
}