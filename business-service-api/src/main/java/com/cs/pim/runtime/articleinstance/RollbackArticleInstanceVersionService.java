package com.cs.pim.runtime.articleinstance;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.abstrct.AbstractRollbackInstanceVersionService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

@Component
public class RollbackArticleInstanceVersionService extends 
  AbstractRollbackInstanceVersionService<IKlassInstanceVersionRollbackModel,IGetKlassInstanceModel> implements IRollbackArticleInstanceVersionService{
   
  @Override
  public IGetKlassInstanceModel execute(IKlassInstanceVersionRollbackModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }
}
