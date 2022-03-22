package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISaveTextAssetInstanceForTabs;
import com.cs.pim.runtime.textassetinstance.ISaveTextAssetInstanceForTabsService;

@Service
public class SaveTextAssetInstanceForTabs extends AbstractRuntimeInteractor<ITextAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveTextAssetInstanceForTabs {
  
  @Autowired
  protected ISaveTextAssetInstanceForTabsService saveTextAssetInstanceForTabsService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ITextAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveTextAssetInstanceForTabsService.execute(klassInstancesModel);
  }
}