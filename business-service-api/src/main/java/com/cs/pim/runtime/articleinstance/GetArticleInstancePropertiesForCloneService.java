package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstancePropertiesForCloneService;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstancePropertiesForCloneService extends AbstractGetInstancePropertiesForCloneService<IGetCloneWizardRequestModel, IGetKlassInstancePropertiesForCloneModel>
    implements IGetArticleInstancePropertiesForCloneService {
  
  @Override
  protected IGetKlassInstancePropertiesForCloneModel executeInternal(IGetCloneWizardRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
}