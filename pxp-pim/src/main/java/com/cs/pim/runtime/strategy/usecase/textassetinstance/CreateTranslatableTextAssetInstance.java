package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ICreateTranslatableTextAssetInstance;
import com.cs.pim.runtime.textassetinstance.ICreateTranslatableTextAssetInstanceService;


@Service
public class CreateTranslatableTextAssetInstance
    extends AbstractRuntimeInteractor<ITextAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableTextAssetInstance {
  
  @Autowired
  protected ICreateTranslatableTextAssetInstanceService createTranslatableTextAssetInstanceService;

  @Override
  protected IGetKlassInstanceModel executeInternal(ITextAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    return createTranslatableTextAssetInstanceService.execute(klassInstancesModel);
  }
}
