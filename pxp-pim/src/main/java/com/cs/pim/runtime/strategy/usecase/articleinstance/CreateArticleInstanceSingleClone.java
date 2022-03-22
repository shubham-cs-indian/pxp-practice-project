package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstanceSingleClone;
import com.cs.pim.runtime.articleinstance.ICreateArticleInstanceSingleCloneService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstanceSingleClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstanceSingleClone extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateArticleInstanceSingleClone {

  @Autowired
  protected ICreateArticleInstanceSingleCloneService createArticleInstanceSingleCloneService;

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return createArticleInstanceSingleCloneService.execute(dataModel);
  }

}
