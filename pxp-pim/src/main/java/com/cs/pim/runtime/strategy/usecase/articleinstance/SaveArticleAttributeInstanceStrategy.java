package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import org.springframework.stereotype.Component;

@Component
public class SaveArticleAttributeInstanceStrategy implements ISaveArticleAttributeInstanceStrategy {
  
  @Override
  public VoidModel execute(IKlassInstanceSaveModel klassInstance) throws Exception
  {
    // saveModifiedAttributes(klassInstance);
    return null;
  }
}
