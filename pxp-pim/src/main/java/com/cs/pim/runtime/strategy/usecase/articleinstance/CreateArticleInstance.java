package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.pim.runtime.articleinstance.ICreateArticleInstanceService;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstance;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstance extends AbstractCreateInstance<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateArticleInstance {

  @Autowired
  protected ICreateArticleInstanceService createArticleInstanceService;

  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel klassInstancesModel) throws Exception
  {
    return createArticleInstanceService.execute(klassInstancesModel);
  }

}
