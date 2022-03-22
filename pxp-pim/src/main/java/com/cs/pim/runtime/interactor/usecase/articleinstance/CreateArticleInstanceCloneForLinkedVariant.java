package com.cs.pim.runtime.interactor.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstanceSingleClone;
import com.cs.pim.runtime.articleinstance.ICreateArticleInstanceCloneForLinkedVariantService;

@Service
public class CreateArticleInstanceCloneForLinkedVariant extends
    AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateArticleInstanceCloneForLinkedVariant {

  @Autowired
  protected ICreateArticleInstanceCloneForLinkedVariantService createArticleInstanceCloneForLinkedVariantService;

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return createArticleInstanceCloneForLinkedVariantService.execute(dataModel);
  }

}
