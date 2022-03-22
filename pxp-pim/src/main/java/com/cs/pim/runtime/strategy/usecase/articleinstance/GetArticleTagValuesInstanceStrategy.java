package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.tag.GetTagValuesInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.tag.IGetTagValuesInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.tag.IIdAndListInstanceModel;
import org.springframework.stereotype.Component;

@Component
public class GetArticleTagValuesInstanceStrategy implements IGetArticleTagValuesInstanceStrategy {
  
  @Override
  public IGetTagValuesInstanceStrategyModel execute(IIdAndListInstanceModel model) throws Exception
  {
    
    return new GetTagValuesInstanceStrategyModel();
  }
}
