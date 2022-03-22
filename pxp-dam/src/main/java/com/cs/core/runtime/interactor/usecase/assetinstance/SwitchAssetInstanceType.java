package com.cs.core.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.assetinstance.ISwitchAssetInstanceTypeService;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class SwitchAssetInstanceType extends AbstractRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchAssetInstanceType {
  
  @Autowired
  protected ISwitchAssetInstanceTypeService switchAssetInstanceTypeService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    return switchAssetInstanceTypeService.execute(typeSwitchModel);
  }
}
