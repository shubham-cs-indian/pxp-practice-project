package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISwitchTextAssetInstanceType;
import com.cs.pim.runtime.textassetinstance.ISwitchTextAssetInstanceTypeService;

@Service
public class SwitchTextAssetInstanceType extends AbstractRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchTextAssetInstanceType {
  
  @Autowired
  protected ISwitchTextAssetInstanceTypeService switchTextAssetInstanceTypeService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    return switchTextAssetInstanceTypeService.execute(typeSwitchModel);
  }
}
