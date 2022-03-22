package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.klassinstance.AbstractGetPropertiesVariantInstancesInTableViewService;
import org.springframework.stereotype.Service;

@Service
public class GetMarketPropertiesVariantInstancesInTableViewService extends AbstractGetPropertiesVariantInstancesInTableViewService<IGetPropertiesVariantInstanceInTableViewRequestModel, IGetPropertiesVariantInstancesInTableViewModel>
    implements IGetMarketPropertiesVariantInstancesInTableViewService {
  
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
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    return new KlassInstanceTypeModel();
  }
}
