package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import org.springframework.stereotype.Component;

@Component
public class SaveArticleInstanceStrategy implements ISaveArticleInstanceStrategy {
  
  @Override
  public VoidModel execute(IKlassInstanceSaveModel model) throws Exception
  {
    return null;
  }
}
