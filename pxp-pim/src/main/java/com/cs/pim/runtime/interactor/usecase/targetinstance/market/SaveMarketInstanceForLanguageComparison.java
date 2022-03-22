package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class SaveMarketInstanceForLanguageComparison extends AbstractRuntimeInteractor<IMarketInstanceSaveModel, IGetKlassInstanceModel> implements ISaveMarketInstanceForLanguageComparison {
  
  @Autowired
  protected ISaveMarketInstanceForLanguageComparison saveMarketInstanceForLanguageComparison;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IMarketInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveMarketInstanceForLanguageComparison.execute(klassInstancesModel);
  }
}
