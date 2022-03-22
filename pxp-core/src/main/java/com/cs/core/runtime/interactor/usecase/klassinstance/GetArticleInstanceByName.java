package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import org.springframework.stereotype.Component;

@Component
public class GetArticleInstanceByName implements IGetInstanceTreeStrategy {
  
  @Override
  public IGetKlassInstanceTreeModel execute(IGetKlassInstanceTreeStrategyModel model)
      throws Exception
  {
    
    IGetKlassInstanceTreeModel responseModel = new GetKlassInstanceTreeModel();
    
    return responseModel;
  }
}
