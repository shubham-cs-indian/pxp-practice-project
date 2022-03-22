package com.cs.dam.config.strategy.usecase.downloadtracker;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IDownloadTrackerConfigurationResponseModel;


public interface IGetDownloadTrackerConfigurationStrategy extends
    IConfigStrategy<IIdsListParameterModel, IListModel<IDownloadTrackerConfigurationResponseModel>> {
  
}
