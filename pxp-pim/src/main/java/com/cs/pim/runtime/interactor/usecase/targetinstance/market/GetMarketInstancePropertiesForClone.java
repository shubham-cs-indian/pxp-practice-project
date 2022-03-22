package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.targetinstance.market.IGetMarketInstancePropertiesForCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstancePropertiesForClone extends AbstractRuntimeInteractor<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel>
    implements IGetMarketInstancePropertiesForClone {

  @Autowired
  protected IGetMarketInstancePropertiesForCloneService getMarketInstancePropertiesForCloneService;

  @Override
  protected IGetKlassInstancePropertiesForCloneModel executeInternal(IGetCloneWizardRequestModel dataModel) throws Exception
  {
    return getMarketInstancePropertiesForCloneService.execute(dataModel);
  }
  
}