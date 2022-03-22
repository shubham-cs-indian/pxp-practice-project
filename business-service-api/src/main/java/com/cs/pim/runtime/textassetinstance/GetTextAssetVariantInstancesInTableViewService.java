package com.cs.pim.runtime.textassetinstance;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.klassinstance.AbstractNewGetVariantInstancesInTableViewService;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetVariantInstancesInTableViewService extends AbstractNewGetVariantInstancesInTableViewService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel>
    implements IGetTextAssetVariantInstancesInTableViewService {
  
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
      throw new TextAssetKlassNotFoundException();
    }
    
  }

  @Override
  protected String getBaseType()
  {
    return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected String getModuleId()
  {
    return Constants.TEXT_ASSET_MODULE;
  }
}
