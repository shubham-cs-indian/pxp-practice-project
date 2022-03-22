package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.ISaveAssetInstanceForOverviewTab;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.ISaveAssetInstanceForOverviewTabService;

@Service
public class SaveAssetInstanceForOverviewTab extends AbstractRuntimeInteractor<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveAssetInstanceForOverviewTab {
  
  @Autowired
  protected ISaveAssetInstanceForOverviewTabService saveAssetInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    return saveAssetInstanceForOverviewTabService.execute(klassInstancesModel);
  }
  
}
