package com.cs.core.runtime.strategy.usecase.goldenrecord;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.ConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForComparisonRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetConfigDetailsForGetKlassInstancesToMergeStrategy  extends OrientDBBaseStrategy 
implements IGetConfigDetailsForGetKlassInstancesToMergeStrategy {
  
  @Override
  public IConfigDetailsForGetKlassInstancesToMergeModel execute(IGetConfigDetailsForComparisonRequestModel model)
      throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_KLASS_INSTANCES_TO_MERGE, model, ConfigDetailsForGetKlassInstancesToMergeModel.class);
  }
}
