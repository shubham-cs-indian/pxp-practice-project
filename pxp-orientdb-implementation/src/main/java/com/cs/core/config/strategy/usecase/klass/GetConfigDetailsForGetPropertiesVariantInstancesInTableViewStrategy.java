package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.variants.ConfigDetailsForGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategy
    extends OrientDBBaseStrategy
    implements IGetConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategy {
  
  @Override
  public IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel execute(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_PROPERTIES_VARIANT_INSTANCES_IN_TABLE_VIEW, model,
        ConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.class);
  }
}
