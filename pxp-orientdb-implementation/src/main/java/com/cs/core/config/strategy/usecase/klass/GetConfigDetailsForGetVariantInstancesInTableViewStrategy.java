package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.variants.ConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForGetVariantInstancesInTableViewStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetVariantInstancesInTableViewStrategy {
  
  public static final String useCase = GET_CONFIG_DETAILS_FOR_GET_VARIANT_INSTANCES_IN_TABLE_VIEW;
  
  @Override
  public IConfigDetailsForGetVariantInstancesInTableViewModel execute(
      IConfigDetailsForGetVariantInstancesInTableViewRequestModel model) throws Exception
  {
    return execute(useCase, model, ConfigDetailsForGetVariantInstancesInTableViewModel.class);
  }
}
