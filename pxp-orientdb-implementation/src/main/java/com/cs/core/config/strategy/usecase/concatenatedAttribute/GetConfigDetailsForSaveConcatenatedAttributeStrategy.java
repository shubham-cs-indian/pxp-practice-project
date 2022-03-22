package com.cs.core.config.strategy.usecase.concatenatedAttribute;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.model.attribute.ConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

@Component
public class GetConfigDetailsForSaveConcatenatedAttributeStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForSaveConcatenatedAttributeStrategy {

  public static final String useCase = "GetConfigDetailsForSaveConcatenatedAttribute";
  
  @Override
  public IConfigDetailsForSaveConcatenatedAttributeModel execute(IMulticlassificationRequestModel model) throws Exception
  {
    return super.execute(useCase, model, ConfigDetailsForSaveConcatenatedAttributeModel.class);
  }
}
