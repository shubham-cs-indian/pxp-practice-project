package com.cs.dam.runtime.interactor.usecase.assetinstance;

import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.ICreateAssetInstance;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.dam.runtime.assetinstance.ICreateAssetInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAssetInstance extends AbstractCreateInstance<ICreateAssetInstanceModel, IKlassInstanceInformationModel>
    implements ICreateAssetInstance {

  @Autowired
  protected ICreateAssetInstanceService createAssetInstanceService;

  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateAssetInstanceModel model)
      throws Exception
  {
      return createAssetInstanceService.execute(model);
  }

}
