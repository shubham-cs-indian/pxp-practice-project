package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.ISwitchMarketInstanceTypeService;

@Service
public class SwitchMarketInstanceType
    extends AbstractRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchMarketInstanceType {
  
  @Autowired
  protected ISwitchMarketInstanceTypeService switchMarketInstanceTypeService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel)
      throws Exception
  {
    return switchMarketInstanceTypeService.execute(typeSwitchModel);
  }
}
