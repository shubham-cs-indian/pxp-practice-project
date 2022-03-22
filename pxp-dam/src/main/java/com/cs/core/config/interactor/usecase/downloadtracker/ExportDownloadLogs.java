package com.cs.core.config.interactor.usecase.downloadtracker;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.cs.core.config.downloadtracker.IExportDownloadLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.entity.downloadtracker.IAssetInstanceDownloadLogsEntity;
import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;
import com.cs.rdbms.utils.PostgresQueryToCSV;
import com.cs.runtime.interactor.model.downloadtracker.ExportDownloadLogResponseModel;
import com.cs.runtime.interactor.model.downloadtracker.IExportDownloadLogResponseModel;

@Service
public class ExportDownloadLogs extends
    AbstractGetConfigInteractor<IGetDownloadLogsRequestModel, IExportDownloadLogResponseModel>
    implements IExportDownloadLogs {

  @Autowired
  protected IExportDownloadLogsService exportDownloadLogsService;

  @Override
  protected IExportDownloadLogResponseModel executeInternal(IGetDownloadLogsRequestModel model)
      throws Exception
  {
    return exportDownloadLogsService.execute(model);
  }
}
