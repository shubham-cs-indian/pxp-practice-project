package com.cs.core.config.strategy.usecase.variant;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForAutoCreateVariantInstanceStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForAutoCreateVariantInstanceStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForAutoCreateVariantInstanceStrategy {
  
  @Override
  public IGetConfigDetailsForCreateVariantModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_AUTO_CREATE_VARIANT_INSTANCE, model,
        GetConfigDetailsForCreateVariantModel.class);
  }
}
