package com.cs.core.config.strategy.usecase.variant;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.variantcontext.IGetConfigDetailsForGetLinkedInstancesQuicklistStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.variants.ConfigDetailsForGetVariantLinkedInstancesQuicklistModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantLinkedInstancesQuicklistModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForGetLinkedInstancesQuicklistStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetLinkedInstancesQuicklistStrategy {
  
  public static final String useCase = "GetConfigDetailsForGetLinkedInstancesQuicklist";
  
  @Override
  public IConfigDetailsForGetVariantLinkedInstancesQuicklistModel execute(IIdParameterModel model)
      throws Exception
  {
    return execute(useCase, model, ConfigDetailsForGetVariantLinkedInstancesQuicklistModel.class);
  }
}
