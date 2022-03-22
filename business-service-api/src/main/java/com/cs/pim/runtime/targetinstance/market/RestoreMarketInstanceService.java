package com.cs.pim.runtime.targetinstance.market;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class RestoreMarketInstanceService extends AbstractRestoreInstanceService<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreMarketInstanceService {
  
  @Override
  protected String getBaseType()
  {
    return Constants.MARKET_INSTANCE_BASE_TYPE;
  }
  
}