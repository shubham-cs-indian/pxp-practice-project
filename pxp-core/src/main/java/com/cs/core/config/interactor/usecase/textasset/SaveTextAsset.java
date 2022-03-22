package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.textasset.ISaveTextAssetService;

@Service
public class SaveTextAsset extends AbstractSaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveTextAsset {
  
  @Autowired
  ISaveTextAssetService saveTextAssetService;
  
  
  @Override
  protected IGetKlassEntityWithoutKPModel executeInternal(
      IKlassSaveModel klassModel) throws Exception
  {
    return saveTextAssetService.execute(klassModel);
  }
 
}
