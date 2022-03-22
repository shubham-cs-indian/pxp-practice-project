package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.klassinstance.AbstractNewGetVariantInstancesInTableViewService;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticleVariantInstancesInTableViewService extends AbstractNewGetVariantInstancesInTableViewService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetArticleVariantInstancesInTableViewService {
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy getArticleInstanceTypeStrategy;
  
  @Override
  protected IGetVariantInstancesInTableViewModel executeInternal(
      IGetVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForArticle();
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException();
    }
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ARTICLE_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected String getModuleId()
  {
    return Constants.PIM_MODULE;
  }
}
