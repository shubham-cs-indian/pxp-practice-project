package com.cs.core.config.interactor.usecase.downloadtracker;

import com.cs.core.config.downloadtracker.IGetDownloadLogsService;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDownloadLogs extends AbstractGetDownloadLogs<IGetDownloadLogsRequestModel, IGetDownloadLogListResponseModel>
    implements IGetDownloadLogs {

  @Autowired
  protected IGetDownloadLogsService getDownloadLogsService;

  @Override protected IGetDownloadLogListResponseModel executeInternal(IGetDownloadLogsRequestModel model) throws Exception
  {
    return getDownloadLogsService.execute(model);
  }
}