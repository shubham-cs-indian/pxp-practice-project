package com.cs.core.config.downloadtracker;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.config.interactor.entity.downloadtracker.IAssetInstanceDownloadLogsEntity;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;
import com.cs.rdbms.utils.PostgresQueryToCSV;
import com.cs.runtime.interactor.model.downloadtracker.ExportDownloadLogResponseModel;
import com.cs.runtime.interactor.model.downloadtracker.IExportDownloadLogResponseModel;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Service
public class ExportDownloadLogsService extends AbstractGetConfigService<IGetDownloadLogsRequestModel, IExportDownloadLogResponseModel>
    implements IExportDownloadLogsService {
  
  @Override
  protected IExportDownloadLogResponseModel executeInternal(IGetDownloadLogsRequestModel model)
      throws Exception
  {
    IExportDownloadLogResponseModel returnModel = new ExportDownloadLogResponseModel();
    List<String> idList = model.getPrimaryKeys();
    List<Map<String, Object>> gridViewFilterModel = GetDownloadLogsService.getFilters(model.getGridViewFilterModel());
    ISortModel sortOptions = model.getSortOptions();
    String sortfield = sortOptions.getSortField()!= null ? sortOptions.getSortField() : IAssetInstanceDownloadLogsEntity.TIMESTAMP;
    String sortOrder = sortOptions.getSortOrder()!=null ? sortOptions.getSortOrder() : "DESC";
    IDownloadTrackerDAO downloadTrackerDAO = DownloadTrackerDAO.getInstance();
    StringBuilder exportLogsQuery = downloadTrackerDAO.getExportLogsQuery(sortfield, sortOrder, idList, gridViewFilterModel);
    
    // Get sql connection
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    Connection connection = driver.getDataSource().getConnection();
    byte[] osBytes = PostgresQueryToCSV.getCSVBytesOfQueryResult(connection, exportLogsQuery.toString());
    returnModel.setCsvBytes(osBytes);
    return returnModel;
  }
}
