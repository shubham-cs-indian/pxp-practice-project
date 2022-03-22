package com.cs.core.config.interactor.usecase.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.downloadtracker.IGetDownloadLogsFilterDataService;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataResponseModel;

/**
 * This service is responsible for getting download logs filter data
 * @author vannya.kalani
 *
 */
@Service
public class GetDownloadLogsFilterData extends AbstractGetConfigInteractor<IGetDownloadLogsFilterDataRequestModel, IGetDownloadLogsFilterDataResponseModel>
implements IGetDownloadLogsFilterData {

  @Autowired
  IGetDownloadLogsFilterDataService getDownloadLogsFilterDataService;

  @Override
  protected IGetDownloadLogsFilterDataResponseModel executeInternal(IGetDownloadLogsFilterDataRequestModel model)
      throws Exception
  {
    return getDownloadLogsFilterDataService.execute(model);
  }

}
