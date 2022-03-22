package com.cs.core.runtime.interactor.usecase.dashboard;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.dashboard.IGetDashboardTileInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardTileInformation extends AbstractRuntimeInteractor<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileInformation {

  @Autowired
  protected IGetDashboardTileInformationService getDashboardTileInformationService;

  @Override
  protected IDashboardInformationResponseModel executeInternal(IDashboardInformationRequestModel dashboardInformationRequestModel)
      throws Exception
  {
    return getDashboardTileInformationService.execute(dashboardInformationRequestModel);
  }
}
