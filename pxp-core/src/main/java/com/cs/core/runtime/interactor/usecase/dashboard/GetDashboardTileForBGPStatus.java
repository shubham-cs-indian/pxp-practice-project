package com.cs.core.runtime.interactor.usecase.dashboard;

import com.cs.core.runtime.dashboard.IGetDashboardTileForBGPStatusService;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDashboardTileForBGPStatus extends AbstractRuntimeInteractor<IDashboardBGPStatusRequestModel, IDashboardBGPStatusResponseModel>
    implements IGetDashboardTileForBGPStatus {

  @Autowired
  IGetDashboardTileForBGPStatusService getDashboardTileForBGPStatusService;

  @Override
  protected IDashboardBGPStatusResponseModel executeInternal(IDashboardBGPStatusRequestModel requestData)
      throws Exception
  {
    return getDashboardTileForBGPStatusService.execute(requestData);
  }


}
