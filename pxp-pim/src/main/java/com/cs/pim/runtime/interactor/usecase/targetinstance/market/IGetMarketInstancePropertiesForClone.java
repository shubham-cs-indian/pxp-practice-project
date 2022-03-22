package com.cs.pim.runtime.interactor.usecase.targetinstance.market;


import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetMarketInstancePropertiesForClone extends
    IRuntimeInteractor<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel> {
  
}
