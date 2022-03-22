package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.variants.GetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.klassinstance.AbstractGetPropertiesVariantInstancesInTableViewService;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetArticlePropertiesVariantInstancesInTableViewService extends AbstractGetPropertiesVariantInstancesInTableViewService<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetArticlePropertiesVariantInstancesInTableViewService {
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy getArticleInstanceTypeStrategy;
  
  @Override
  protected IGetPropertiesVariantInstancesInTableViewModel executeInternal(
      IGetPropertiesVariantInstanceInTableViewRequestModel getKlassInstanceTreeStrategyModel)
      throws Exception
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
  protected IGetPropertiesVariantInstancesInTableViewModel executeGetVariantInstances(
      IGetPropertiesVariantInstancesInTableViewRequestStrategyModel requestModel) throws Exception
  {
    return new GetPropertiesVariantInstancesInTableViewModel();
    // return
    // getArticlePropertiesVariantInstancesInTableViewStrategy.execute(requestModel);
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return getArticleInstanceTypeStrategy.execute(idParameterModel);
  }
}
