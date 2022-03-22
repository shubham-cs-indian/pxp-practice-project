package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.pim.runtime.targetinstance.market.ICreateMarketInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateMarketInstance extends AbstractCreateInstance<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateMarketInstance {
  
  @Autowired
  protected ICreateMarketInstanceService createMarketInstanceService;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model) throws Exception
  {
    return createMarketInstanceService.execute(model);
  }
}
