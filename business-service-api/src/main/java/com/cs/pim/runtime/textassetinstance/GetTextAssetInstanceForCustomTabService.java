package com.cs.pim.runtime.textassetinstance;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveReadPermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForCustomTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceForCustomTabService extends AbstractGetInstanceForCustomTabService<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel>
    implements IGetTextAssetInstanceForCustomTabService {
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy         getConfigDetailsForCustomTab;
  
  @Override
  protected IGetKlassInstanceCustomTabModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForAsset();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new TextAssetKlassNotFoundException();
    }
    
  }
  
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel model) throws Exception
  {
    return getConfigDetailsForCustomTab.execute(model);
  }
  
}
