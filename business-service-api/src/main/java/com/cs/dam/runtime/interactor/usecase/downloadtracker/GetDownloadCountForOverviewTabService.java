package com.cs.dam.runtime.interactor.usecase.downloadtracker;

import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;
import com.cs.runtime.interactor.model.downloadtracker.GetDownloadCountResponseModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountRequestModel;
import com.cs.runtime.interactor.model.downloadtracker.IGetDownloadCountResponseModel;

@Service
public class GetDownloadCountForOverviewTabService
    extends AbstractRuntimeService<IGetDownloadCountRequestModel, IGetDownloadCountResponseModel>
    implements IGetDownloadCountForOverviewTabService {

  @Override
  protected IGetDownloadCountResponseModel executeInternal(IGetDownloadCountRequestModel model) throws Exception
  {
    IGetDownloadCountResponseModel responceModel = new GetDownloadCountResponseModel();
    
    // Fetch total count from asset_misc table
    IAssetMiscDAO assetMiscDao = AssetMiscDAO.getInstance();
    long totalDownloadCount = assetMiscDao.getTotalDownloadCount(Long.parseLong(model.getAssetInstanceId()));
    responceModel.setTotalDownloadCount(totalDownloadCount);
    
    // Fetch download count according to time range from downloadloginformation table
    IDownloadTrackerDAO downloadTrackerDAO = DownloadTrackerDAO.getInstance();
    ITimeRange timeRange = model.getTimeRange();
    long startTime = timeRange.getStartTime();
    long endTime = timeRange.getEndTime();
    long downloadCountWithinRange = downloadTrackerDAO.getDownloadCountWithinRange(startTime,
        endTime, Long.parseLong(model.getAssetInstanceId()));
    responceModel.setDownloadCountWithinRange(downloadCountWithinRange);
    
    return responceModel;
  }
}
