package com.cs.core.runtime.interactor.usecase.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ICreateTranslatableArticleInstance;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.ICreateTranslatableArticleInstanceService;

@Service
public class CreateTranslatableArticleInstance extends AbstractRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableArticleInstance {
  
  @Autowired
  protected ICreateTranslatableArticleInstanceService createTranslatableArticleInstanceService;

  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel) throws Exception
  {
    return createTranslatableArticleInstanceService.execute(klassInstancesModel);
  }

}
