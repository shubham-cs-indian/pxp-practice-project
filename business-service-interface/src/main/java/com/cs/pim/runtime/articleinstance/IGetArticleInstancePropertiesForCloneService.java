package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

public interface IGetArticleInstancePropertiesForCloneService extends
    IRuntimeService<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel> {
  
}
