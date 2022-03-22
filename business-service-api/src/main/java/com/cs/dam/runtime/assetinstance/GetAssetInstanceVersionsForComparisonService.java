package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.klassinstance.AbstractGetKlassInstanceVersionsForComparisonService;
import com.cs.dam.runtime.strategy.usecase.assetinstance.IGetConfigDetailsForAssetInstanceTimelineTabStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceVersionsForComparisonService extends AbstractGetKlassInstanceVersionsForComparisonService<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetAssetInstanceVersionsForComparisonService {
  
  @Autowired
  protected IGetConfigDetailsForAssetInstanceTimelineTabStrategy getConfigDetailsForAssetInstanceTimelineTab;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception
  {
    
    return getConfigDetailsForAssetInstanceTimelineTab.execute(idParameterModel);
  }
  
  @Override
  protected String getBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IContentInstance getContentInstance()
  {
    return new AssetInstance();
  }
  
}
