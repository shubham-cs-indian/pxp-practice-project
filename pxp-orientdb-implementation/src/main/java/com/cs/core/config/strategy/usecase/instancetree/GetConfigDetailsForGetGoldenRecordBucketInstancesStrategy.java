package com.cs.core.config.strategy.usecase.instancetree;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetGoldenRecordBucketInstancesStrategy;

@Component
public class GetConfigDetailsForGetGoldenRecordBucketInstancesStrategy extends OrientDBBaseStrategy 
implements IGetConfigDetailsForGetGoldenRecordBucketInstancesStrategy {

  @Override
  public IConfigDetailsForGetNewInstanceTreeModel execute(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_GET_GOLDEN_RECORD_BUCKET_INSTANCES, model, ConfigDetailsForGetNewInstanceTreeModel.class);
  }

}
