package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.AbstractGetConfigDataForCreateVariantInTableView;

@Service
public class GetConfigDetailsForCreateAssetVariantInstance extends
    AbstractGetConfigDataForCreateVariantInTableView<IIdParameterModel, IGetConfigDetailsForCustomTabModel>
    implements IGetConfigDetailsForCreateAssetVariantInstance {
  
  @Override
  protected IGetConfigDetailsForCustomTabModel executeInternal(IIdParameterModel model)
      throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForArticle();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new AssetKlassNotFoundException();
    }
  }
}
