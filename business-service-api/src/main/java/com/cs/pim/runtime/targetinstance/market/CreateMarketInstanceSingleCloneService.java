package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.interactor.exception.marketinstance.UserNotHaveCreatePermissionForMarket;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceSingleClone;
import org.springframework.stereotype.Service;

@Service
public class CreateMarketInstanceSingleCloneService
    extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateMarketInstanceSingleCloneService {

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForMarket();
  }

}
