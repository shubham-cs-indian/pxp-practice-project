package com.cs.core.config.downloadtracker;

import com.cs.core.config.interactor.model.downloadtracker.*;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.DbUtils;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewFilterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewListFilterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewTextFilterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewTimeRangeFilterModel;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetDownloadLogsService extends AbstractGetDownloadLogsService<IGetDownloadLogsRequestModel, IGetDownloadLogListResponseModel>
    implements IGetDownloadLogsService {
  
  public static final String GRID_VIEW_LIST_FILTER_MODEL       = "com.cs.dam.config.interactor.model.downloadtracker.GridViewListFilterModel";
  public static final String GRID_VIEW_TEXT_FILTER_MODEL       = "com.cs.dam.config.interactor.model.downloadtracker.GridViewTextFilterModel";
  public static final String GRID_VIEW_TIME_RANGE_FILTER_MODEL = "com.cs.dam.config.interactor.model.downloadtracker.GridViewTimeRangeFilterModel";
  
  @Override
  protected IGetDownloadLogListResponseModel getDownloadLogs(
      IGetDownloadLogsRequestModel getDownloadLogsRequestModel) throws Exception
  {
    IGetDownloadLogListResponseModel returnModel = new GetDownloadLogListResponseModel();
    IDownloadTrackerDAO downloadTrackerDAO = RDBMSUtils.newUserSessionDAO()
        .newDownloadTrackerDAO();
    ISortModel sortOptions = getDownloadLogsRequestModel.getSortOptions();
    String sortfield = sortOptions.getSortField()!= null ? sortOptions.getSortField() : IGetDownloadLogsModel.TIMESTAMP;
    String sortOrder = sortOptions.getSortOrder()!=null ? sortOptions.getSortOrder() : "DESC";
    Long size = getDownloadLogsRequestModel.getSize();
    Long from = getDownloadLogsRequestModel.getFrom();
    
    List<Map<String, Object>> filters = getFilters(getDownloadLogsRequestModel.getGridViewFilterModel());
    List<IDownloadTrackerDTO> listOfDownloadLogDTO = downloadTrackerDAO.fetchDownloadLogs(sortfield, sortOrder, size, from, filters);
    List<IGetDownloadLogsModel> downloadLogList = new ArrayList<>();
    getDownloadLogList(listOfDownloadLogDTO, downloadLogList);
    
    returnModel.setFrom(from);
    returnModel.setDownloadLogList(downloadLogList);
    returnModel.setSize(Long.valueOf(downloadLogList.size()));
    returnModel.setTotalCount(downloadTrackerDAO.getDownloadLogCount(filters));
    
    return returnModel;
  }

  private void getDownloadLogList(List<IDownloadTrackerDTO> listOfDownloadLogDTO,
      List<IGetDownloadLogsModel> downloadLogList)
  {
    for(IDownloadTrackerDTO downloadLogDTO: listOfDownloadLogDTO) {
      IGetDownloadLogsModel downloadLogRecord = new GetDownloadLogsModel();
      downloadLogRecord.setPrimaryId(downloadLogDTO.getPrimaryKey());
      downloadLogRecord.setUserName(downloadLogDTO.getUserName());
      downloadLogRecord.setComment(downloadLogDTO.getComment());
      downloadLogRecord.setTimestamp(downloadLogDTO.getTimeStamp());
      downloadLogRecord.setUserId(downloadLogDTO.getUserId());
      downloadLogRecord.setAssetFileName(downloadLogDTO.getAssetFileName());
      downloadLogRecord.setAssetInstanceClassCode(downloadLogDTO.getAssetInstanceClassCode());
      downloadLogRecord.setAssetInstanceClassId(downloadLogDTO.getAssetInstanceClassId());
      downloadLogRecord.setAssetInstanceIId(downloadLogDTO.getAssetInstanceIId());
      downloadLogRecord.setAssetInstanceName(downloadLogDTO.getAssetInstanceName());
      downloadLogRecord.setRenditionFileName(downloadLogDTO.getRenditionFileName());
      downloadLogRecord.setRenditionInstanceClassCode(downloadLogDTO.getRenditionInstanceClassCode());
      downloadLogRecord.setRenditionInstanceClassId(downloadLogDTO.getRenditionInstanceClassId());
      downloadLogRecord.setRenditionInstanceIId(downloadLogDTO.getRenditionInstanceIId());
      downloadLogRecord.setRenditionInstanceName(downloadLogDTO.getRenditionInstanceName());
      downloadLogRecord.setDownloadedBy(downloadLogDTO.getUserName());
      
      downloadLogList.add(downloadLogRecord);
    }
  }
  
  // TODO: Try to move below method to utility class
  public static List<Map<String, Object>> getFilters(List<? extends IGridViewFilterModel> filtersModel)
  {
    List<Map<String, Object>> filters = new ArrayList<>();
    for(IGridViewFilterModel filterModel : filtersModel) {
      Map<String, Object> filter = new HashMap<>();
      String filterType = filterModel.getFilterType();
      String filterField = getTableColumnName(filterModel.getFilterField());
      String filterValues = null;
      switch(filterType) {
        case GRID_VIEW_LIST_FILTER_MODEL :
          List<String> filterValuesList = ((IGridViewListFilterModel)filterModel).getFilterValues();
          if (filterValuesList.isEmpty()) {
            continue;
          }
          filterValues = DbUtils.quoteIt(filterValuesList);
          break;
        case GRID_VIEW_TEXT_FILTER_MODEL :
          filterValues = ((IGridViewTextFilterModel)filterModel).getFilterValues();
          break;
        case GRID_VIEW_TIME_RANGE_FILTER_MODEL :
          filterValues = ((IGridViewTimeRangeFilterModel)filterModel).getFilterValues().getStartTime() 
              + " AND " + ((IGridViewTimeRangeFilterModel)filterModel).getFilterValues().getEndTime();
          break;
        default:
          filterValues = ((IGridViewTextFilterModel)filterModel).getFilterValues();
      }
      filter.put("filterType", filterType);
      filter.put("filterField", filterField);
      filter.put("filterValues", filterValues);
      filters.add(filter);
    }
    return filters;
  }
  
  private static String getTableColumnName(String requestFieldName){
    switch(requestFieldName){
      case IGetDownloadLogsModel.ASSET_INSTANCE_CLASS_NAME :
        return IDownloadTrackerDTO.ASSET_INSTANCE_CLASS_ID;
      case IGetDownloadLogsModel.RENDITION_INSTANCE_CLASS_NAME :
        return IDownloadTrackerDTO.RENDITION_INSTANCE_CLASS_ID;
      case IGetDownloadLogsModel.DOWNLOADED_BY :
        return IDownloadTrackerDTO.USER_ID;
      case IGetDownloadLogsModel.ASSET_INSTANCE_NAME :
        return IDownloadTrackerDTO.ASSET_INSTANCE_NAME;
      case IGetDownloadLogsModel.RENDITION_INSTANCE_NAME :
        return IDownloadTrackerDTO.RENDITION_INSTANCE_NAME;
      case IGetDownloadLogsModel.TIMESTAMP :
        return IDownloadTrackerDTO.TIME_STAMP;
      case IGetDownloadLogsModel.COMMENT :
        return IDownloadTrackerDTO.COMMENT;
      case IGetDownloadLogsModel.RENDITION_FILE_NAME:
        return IDownloadTrackerDTO.RENDITION_FILE_NAME;
      case IGetDownloadLogsModel.ASSET_FILE_NAME:
        return IDownloadTrackerDTO.ASSET_FILE_NAME;
      case IGetDownloadLogsModel.USER_NAME:
        return IDownloadTrackerDTO.USER_NAME;
      default :
        return requestFieldName;
    }
  }
  
}