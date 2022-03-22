package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceBulkClone;
import org.springframework.stereotype.Service;

@Service
public class CreateMarketInstanceBulkCloneService
    extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateMarketInstanceBulkCloneService {

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForMarket();
  }
}