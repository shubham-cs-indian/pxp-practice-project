package com.cs.core.config.downloadtracker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.downloadtracker.GetConfigInformationForDownloadTrackerRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetConfigInformationForDownloadTrackerRequestModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogListResponseModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetDownloadLogsRequestModel;
import com.cs.core.config.strategy.usecase.downloadtracker.IGetConfigInformationForDownloadTrackerStrategy;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetDownloadLogsService<P extends IGetDownloadLogsRequestModel, R extends IGetDownloadLogListResponseModel>
    extends AbstractGetConfigService<P, R> {
  
  @Autowired
  protected IGetConfigInformationForDownloadTrackerStrategy getConfigInformationForDownloadTracker;
  
  protected abstract IGetDownloadLogListResponseModel getDownloadLogs(P getDownloadLogsRequestModel) throws Exception;
  
  @Override
  protected R executeInternal(P getDownloadLogsRequestModel) throws Exception
  {
    Map<String, String> idVsLabelMap = new HashMap<String, String>();
    
    IGetDownloadLogListResponseModel returnModel = getDownloadLogs(getDownloadLogsRequestModel);
    List<IGetDownloadLogsModel>  logModel = returnModel.getDownloadLogList();
    
    getLanguageDependentInformation(idVsLabelMap, logModel);
    
    logModel.forEach(model -> {
      model.setAssetInstanceClassName(idVsLabelMap.get(model.getAssetInstanceClassId()));
      if(model.getRenditionInstanceClassId()!=null){
        model.setRenditionInstanceClassName(idVsLabelMap.get(model.getRenditionInstanceClassId()));
      }
    });
    
    return (R) returnModel;
  }

  private void getLanguageDependentInformation(Map<String, String> idVsLabelMap,
      List<IGetDownloadLogsModel> logModel) throws Exception
  {
    Set<String> klassIds = new HashSet<>();
    logModel.forEach(model -> {
      klassIds.add(model.getAssetInstanceClassId());
      if(model.getRenditionInstanceClassId()!=null){
        klassIds.add(model.getRenditionInstanceClassId());
      }
    });
    
    IGetConfigInformationForDownloadTrackerRequestModel configRequestModel = new GetConfigInformationForDownloadTrackerRequestModel();
    configRequestModel.getAssetClassIds().addAll(klassIds);
    IListModel<IIdLabelModel> configInfoModel = getConfigInformationForDownloadTracker.execute(configRequestModel);
    List<IIdLabelModel> configInfoModelList = (List<IIdLabelModel>) configInfoModel.getList();
    
    configInfoModelList.forEach(labelCodeModel->{
      idVsLabelMap.put(labelCodeModel.getId(), labelCodeModel.getLabel());
    });
  }
}