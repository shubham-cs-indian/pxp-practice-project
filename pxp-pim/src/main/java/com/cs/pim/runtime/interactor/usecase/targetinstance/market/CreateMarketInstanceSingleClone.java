package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstanceSingleClone;
import com.cs.pim.runtime.targetinstance.market.ICreateMarketInstanceSingleCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateMarketInstanceSingleClone extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateMarketInstanceSingleClone {

  @Autowired
  protected ICreateMarketInstanceSingleCloneService createMarketInstanceBulkCloneService;

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return createMarketInstanceBulkCloneService.execute(dataModel);
  }

}
