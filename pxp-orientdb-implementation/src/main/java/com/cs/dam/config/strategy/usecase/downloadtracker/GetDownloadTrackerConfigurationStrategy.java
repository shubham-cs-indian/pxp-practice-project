package com.cs.dam.config.strategy.usecase.downloadtracker;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.dam.config.interactor.model.downloadtracker.DownloadTrackerConfigurationResponseModel;
import com.cs.dam.config.interactor.model.downloadtracker.IDownloadTrackerConfigurationResponseModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetDownloadTrackerConfigurationStrategy extends OrientDBBaseStrategy
    implements IGetDownloadTrackerConfigurationStrategy {
  
  @Override
  public IListModel<IDownloadTrackerConfigurationResponseModel> execute(
      IIdsListParameterModel model) throws Exception
  {
    return execute(GET_DOWNLOAD_TRACKER_CONFIGURATION, model, 
        new TypeReference<ListModel<DownloadTrackerConfigurationResponseModel>>() {});
  }
}
