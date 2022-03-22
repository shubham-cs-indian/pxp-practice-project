package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.klassinstance.AbstractGetKlassInstanceForVersionTabService;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceCustomTabStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceForVersionTabService extends AbstractGetKlassInstanceForVersionTabService<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetAssetInstanceForVersionTabService {
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceCustomTabStrategy  getConfigDetailsForAssetInstanceCustomTabStrategy;
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception
  {
    
    return getConfigDetailsForAssetInstanceCustomTabStrategy.execute(idParameterModel);
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }
  
  @Override
  protected Boolean getIsArchive() throws Exception
  {
    return false;
  }
  
}
