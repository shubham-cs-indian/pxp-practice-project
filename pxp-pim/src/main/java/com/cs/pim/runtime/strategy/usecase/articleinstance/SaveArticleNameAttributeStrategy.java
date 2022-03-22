package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import org.springframework.stereotype.Component;

@Component
public class SaveArticleNameAttributeStrategy implements ISaveArticleNameAttributeStrategy {
  
  @Override
  public VoidModel execute(IModifiedContentAttributeInstanceModel model) throws Exception
  {
    
    return null;
  }
}
