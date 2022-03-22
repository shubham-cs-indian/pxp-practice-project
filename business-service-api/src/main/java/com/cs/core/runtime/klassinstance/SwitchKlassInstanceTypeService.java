package com.cs.core.runtime.klassinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveCreatePermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConifgDetailsForTypeSwitchOfInstanceStrategy;
import com.cs.core.runtime.interactor.usecase.taxonomy.NewSwitchInstanceTypeService;
import com.cs.core.runtime.strategy.usecase.templating.IGetArticleInstanceForCustomTabStrategy;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;

@Service
public class SwitchKlassInstanceTypeService
    extends NewSwitchInstanceTypeService<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchKlassInstanceTypeService {
  
  @Autowired
  protected IGetConifgDetailsForTypeSwitchOfInstanceStrategy getConifgDetailsForTypeSwitchOfInstanceStrategy;
  
  @Autowired
  protected IGetArticleInstanceForCustomTabStrategy          getArticleInstanceForCustomTabStrategy;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel)
      throws Exception
  {
    try {
      return super.executeInternal(typeSwitchModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForArticle(e);
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
      // orient db plugin
      throw new ArticleKlassNotFoundException(e);
    }
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getMultiClassificationDetails(
      IConfigDetailsForSwitchTypeRequestModel requestModel) throws Exception
  {
    return getConifgDetailsForTypeSwitchOfInstanceStrategy.execute(requestModel);
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
      return WorkflowUtils.UseCases.CLASSIFICATIONSAVE; //SWITCHTYPE in old PXP
  }

}
