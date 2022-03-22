package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ICreateTextAssetInstance;
import com.cs.pim.runtime.textassetinstance.ICreateTextAssetInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateTextAssetInstance extends AbstractCreateInstance<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateTextAssetInstance {
  
  @Autowired
  protected ICreateTextAssetInstanceService createTextAssetInstanceService;

  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
  {
    return createTextAssetInstanceService.execute(model);
  }
}
