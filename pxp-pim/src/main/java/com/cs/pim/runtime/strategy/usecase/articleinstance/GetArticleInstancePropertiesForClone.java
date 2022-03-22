package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstancePropertiesForCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetArticleInstancePropertiesForClone;

@Service
public class GetArticleInstancePropertiesForClone extends AbstractRuntimeInteractor<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel>
    implements IGetArticleInstancePropertiesForClone {

  @Autowired
  protected IGetArticleInstancePropertiesForCloneService getArticleInstancePropertiesForCloneService;

  @Override
  protected IGetKlassInstancePropertiesForCloneModel executeInternal(IGetCloneWizardRequestModel dataModel) throws Exception
  {
    return getArticleInstancePropertiesForCloneService.execute(dataModel);
  }
  
}