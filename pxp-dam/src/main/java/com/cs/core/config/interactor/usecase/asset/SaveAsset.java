package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.asset.ISaveAssetService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

@Service
public class SaveAsset extends AbstractSaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveAsset {
  
  @Autowired
  ISaveAssetService saveAssetService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IKlassSaveModel klassModel) throws Exception
  {
    return saveAssetService.execute(klassModel);
  }
  
}
