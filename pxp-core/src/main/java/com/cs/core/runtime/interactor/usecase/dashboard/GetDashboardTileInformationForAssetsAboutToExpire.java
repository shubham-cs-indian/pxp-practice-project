package com.cs.core.runtime.interactor.usecase.dashboard;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.dashboard.IGetDashboardTileInformationForAssetAboutToExpireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardTileInformationForAssetsAboutToExpire extends AbstractRuntimeInteractor<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileInformationForAssetAboutToExpire {
  
  @Autowired
  protected IGetDashboardTileInformationForAssetAboutToExpireService getDashboardTileInformationForAssetAboutToExpireService;
  
  @Override
  protected IDashboardInformationResponseModel executeInternal(
      IDashboardInformationRequestModel dashboardInformationRequestModel) throws Exception
  {
      return getDashboardTileInformationForAssetAboutToExpireService.execute(dashboardInformationRequestModel);
  }
}
