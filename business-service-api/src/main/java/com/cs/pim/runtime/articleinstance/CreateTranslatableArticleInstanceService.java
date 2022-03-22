package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.abstrct.AbstractCreateTranslatableInstanceService;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTranslatableArticleInstanceService
    extends AbstractCreateTranslatableInstanceService<IArticleInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableArticleInstanceService {

  @Autowired
  protected Long                                    assetKlassCounter;

  protected IGetKlassInstanceModel executeInternal(IArticleInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }

  @Override
  protected Long getCounter()
  {
    return assetKlassCounter++;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.SAVEARTIKLE;
  }
}
