package com.cs.core.config.downloadtracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.downloadtracker.GetDownloadLogsFilterDataResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsFilterDataResponseModel;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.dam.config.interactor.model.downloadtracker.GetLabelsByIdsRequestModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGetLabelsByIdsRequestModel;
import com.cs.dam.config.strategy.usecase.downloadtracker.IGetLabelsByIdsStrategy;
import com.cs.dam.rdbms.downloadtracker.dto.DownloadTrackerDTO;
import com.cs.dam.rdbms.downloadtracker.idto.IDownloadTrackerDTO;

/**
 * This service is responsible for getting download logs filter data
 * @author vannya.kalani
 *
 */

@Service
public class GetDownloadLogsFilterDataService
extends AbstractGetConfigService<IGetDownloadLogsFilterDataRequestModel, IGetDownloadLogsFilterDataResponseModel>
implements IGetDownloadLogsFilterDataService {

  @Autowired
  IGetLabelsByIdsStrategy getLabelsByIdsStrategy;

  @Override
  protected IGetDownloadLogsFilterDataResponseModel executeInternal(IGetDownloadLogsFilterDataRequestModel model)
      throws Exception
  {
    IDownloadTrackerDAO downloadTrackerDAO = RDBMSUtils.newUserSessionDAO().newDownloadTrackerDAO();
    List<String> idList = new ArrayList<>();
    String columnName = model.getColumnName();
    String searchText = model.getSearchText();

    List<IDownloadTrackerDTO> downloadTrackerDTOs = downloadTrackerDAO.fetchDownloadLogFilterData(columnName, searchText,
        model.getSortOrder(), model.getSize(), model.getFrom());
    List<IIdLabelCodeModel> filterDataList = new ArrayList<>();
    for (IDownloadTrackerDTO downloadTrackerDTO : downloadTrackerDTOs) {
      switch (columnName) {
        case DownloadTrackerDTO.USER_ID :
          IIdLabelCodeModel idLabelCodeModel = new IdLabelCodeModel();
          idLabelCodeModel.setId(downloadTrackerDTO.getUserId());
          idLabelCodeModel.setCode(downloadTrackerDTO.getUserId());
          idLabelCodeModel.setLabel(downloadTrackerDTO.getUserName());
          filterDataList.add(idLabelCodeModel);
          break;

        case DownloadTrackerDTO.ASSET_INSTANCE_CLASS_ID :
          idList.add(downloadTrackerDTO.getAssetInstanceClassId());
          break;

        case DownloadTrackerDTO.RENDITION_INSTANCE_CLASS_ID :
          idList.add(downloadTrackerDTO.getRenditionInstanceClassId());
          break;
      }
    }

    //Get label of asset class ids and rendition class ids from orientDB
    if (!DownloadTrackerDTO.USER_ID.equals(columnName)) {
      getLabelFromIds(idList, filterDataList, model);
    }

    Map<String, Object> downloadLogFilterDataMap = new HashMap<>();
    downloadLogFilterDataMap.put(CommonConstants.LIST_PROPERTY, filterDataList);

    Long count = downloadTrackerDAO.getDownloadLogFilterDataCount(columnName, searchText);
    IGetDownloadLogsFilterDataResponseModel response = new GetDownloadLogsFilterDataResponseModel();
    response.setDownloadLogFilterDataList(downloadLogFilterDataMap);
    response.setCount(count);
    return response;
  }

  /**
   * Get Label of passed ids (@param idList) from OrientDb
   * @param idList list of ids for which label needs to be fetched
   * @param filterDataList needs to be populated with the IdLabelCodeModel
   * @param model
   * @throws Exception
   */
  private void getLabelFromIds(List<String> idList, List<IIdLabelCodeModel> filterDataList,
      IGetDownloadLogsFilterDataRequestModel model) throws Exception
  {
    IGetLabelsByIdsRequestModel requestModel = new GetLabelsByIdsRequestModel();
    requestModel.setEntityType(VertexLabelConstants.ENTITY_TYPE_ASSET);
    requestModel.setIds(idList);
    requestModel.setSearchText(model.getSearchText());
    requestModel.setFrom(model.getFrom());
    requestModel.setSize(model.getSize());
    requestModel.setSortOrder(model.getSortOrder());
    IListModel<IIdLabelCodeModel> listModel = getLabelsByIdsStrategy.execute(requestModel);
    filterDataList.addAll(listModel.getList());
  }

}
