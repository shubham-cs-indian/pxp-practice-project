package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISaveTextAssetInstanceForOverviewTab;
import com.cs.pim.runtime.textassetinstance.ISaveTextAssetInstanceForOverviewTabService;

@Service
public class SaveTextAssetInstanceForOverviewTab extends AbstractRuntimeInteractor<ITextAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveTextAssetInstanceForOverviewTab {
  
  @Autowired
  protected ISaveTextAssetInstanceForOverviewTabService saveTextAssetInstanceForOverviewTabService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ITextAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveTextAssetInstanceForOverviewTabService.execute(klassInstancesModel);
  }
  
}
