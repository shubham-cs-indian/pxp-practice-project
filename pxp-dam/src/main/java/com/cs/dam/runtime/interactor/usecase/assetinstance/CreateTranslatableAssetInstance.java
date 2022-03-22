package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.ICreateTranslatableAssetInstanceService;

@Component
public class CreateTranslatableAssetInstance extends AbstractRuntimeInteractor<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableAssetInstance {
  
  @Autowired
  protected ICreateTranslatableAssetInstanceService createTranslatableAssetInstanceService;
  
  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    return createTranslatableAssetInstanceService.execute(klassInstancesModel);
  }
  
}