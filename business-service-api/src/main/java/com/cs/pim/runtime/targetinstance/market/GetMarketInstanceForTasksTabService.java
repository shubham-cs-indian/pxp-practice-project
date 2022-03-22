package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForTaskTabService;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceForTasksTabService
    extends AbstractGetInstanceForTaskTabService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
    implements IGetMarketInstanceForTasksTabService {
  
  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException(e);
    }
    
  }
  
  @Override
  protected IGetTaskInstanceResponseModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
}
