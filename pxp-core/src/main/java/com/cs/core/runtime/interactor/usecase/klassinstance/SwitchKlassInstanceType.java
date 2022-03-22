package com.cs.core.runtime.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.ISwitchKlassInstanceType;
import com.cs.core.runtime.klassinstance.ISwitchKlassInstanceTypeService;

@Service
public class SwitchKlassInstanceType extends AbstractRuntimeInteractor<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchKlassInstanceType {
  
  @Autowired
  protected ISwitchKlassInstanceTypeService switchKlassInstanceTypeService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    return switchKlassInstanceTypeService.execute(typeSwitchModel);
  }
}
