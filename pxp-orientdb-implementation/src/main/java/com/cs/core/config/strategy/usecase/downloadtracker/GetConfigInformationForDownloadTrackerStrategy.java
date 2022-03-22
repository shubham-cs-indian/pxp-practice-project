package com.cs.core.config.strategy.usecase.downloadtracker;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetConfigInformationForDownloadTrackerRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetConfigInformationForDownloadTrackerStrategy extends OrientDBBaseStrategy
    implements IGetConfigInformationForDownloadTrackerStrategy {
  
  @Override
  public IListModel<IIdLabelModel> execute(IGetConfigInformationForDownloadTrackerRequestModel model) throws Exception
  {
     return execute(GET_CONFIG_INFORMATION_FOR_DOWNLOAD_TRACKER, model, 
        new TypeReference<ListModel<IdLabelModel>>() {});
  }
  
}
