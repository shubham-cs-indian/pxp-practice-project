package com.cs.pim.runtime.targetinstance.market;


import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

public interface IGetMarketInstancePropertiesForCloneService extends
    IRuntimeService<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel> {
  
}
