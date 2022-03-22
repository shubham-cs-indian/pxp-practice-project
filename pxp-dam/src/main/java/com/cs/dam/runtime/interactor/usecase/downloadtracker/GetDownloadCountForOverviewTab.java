package com.cs.dam.runtime.interactor.usecase.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountResponseModel;

@Service
public class GetDownloadCountForOverviewTab
    extends AbstractRuntimeInteractor<IGetDownloadCountRequestModel, IGetDownloadCountResponseModel>
    implements IGetDownloadCountForOverviewTab {
  
  @Autowired
  protected IGetDownloadCountForOverviewTabService getDownloadCountForOverviewTabService;
  @Override
  protected IGetDownloadCountResponseModel executeInternal(IGetDownloadCountRequestModel model)
      throws Exception
  {
    return getDownloadCountForOverviewTabService.execute(model);
  }
}
