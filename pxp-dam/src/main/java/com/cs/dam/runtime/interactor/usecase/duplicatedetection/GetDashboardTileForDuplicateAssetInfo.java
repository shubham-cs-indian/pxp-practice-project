package com.cs.dam.runtime.interactor.usecase.duplicatedetection;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.dashboard.IGetDashboardTileForDuplicateAssetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Get duplicate assets for dam dashboard
 * @author mrunali.dhenge
 *
 */

@Service("getDashboardTileForDuplicateAssetInfo")
public class GetDashboardTileForDuplicateAssetInfo extends AbstractRuntimeInteractor<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileForDuplicateAssetInfo {
  
  @Autowired
  protected IGetDashboardTileForDuplicateAssetInfoService getDashboardTileForDuplicateAssetInfoService;
  

  @Override
  protected IDashboardInformationResponseModel executeInternal(
      IDashboardInformationRequestModel model) throws Exception
  {
    return getDashboardTileForDuplicateAssetInfoService.execute(model);
  }

}
