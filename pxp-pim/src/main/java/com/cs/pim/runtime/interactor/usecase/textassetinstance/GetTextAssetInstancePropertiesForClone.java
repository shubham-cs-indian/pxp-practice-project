package com.cs.pim.runtime.interactor.usecase.textassetinstance;


import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstancePropertiesForCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

@Service
public class GetTextAssetInstancePropertiesForClone extends AbstractRuntimeInteractor<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel>
    implements IGetTextAssetInstancePropertiesForClone {

  @Autowired
  protected IGetTextAssetInstancePropertiesForCloneService getTextAssetInstancePropertiesForCloneService;
  @Override
  protected IGetKlassInstancePropertiesForCloneModel executeInternal(IGetCloneWizardRequestModel dataModel) throws Exception
  {
    return getTextAssetInstancePropertiesForCloneService.execute(dataModel);
  }
  
}