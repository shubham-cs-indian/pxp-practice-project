package com.cs.pim.runtime.textassetinstance;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

public interface IGetTextAssetInstancePropertiesForCloneService extends
    IRuntimeService<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel> {
  
}
