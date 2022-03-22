package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import org.springframework.stereotype.Component;

@Component
public class SaveArticleTagInstanceStrategy implements ISaveArticleTagInstanceStrategy {
  
  @Override
  public VoidModel execute(IKlassInstanceSaveModel klassInstance) throws Exception
  {
    
    return null;
  }
}
