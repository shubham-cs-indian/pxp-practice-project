package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.ISaveAssetInstanceForTabsService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveAssetInstanceForTabs;

@Service
public class SaveAssetInstanceForTabs extends AbstractRuntimeInteractor<IAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveAssetInstanceForTabs {
  
  @Autowired
  protected ISaveAssetInstanceForTabsService saveAssetInstanceForTabsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    return saveAssetInstanceForTabsService.execute(klassInstancesModel);
  }
}
