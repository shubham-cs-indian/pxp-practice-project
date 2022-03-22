package com.cs.core.config.strategy.usecase.downloadtracker;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetConfigInformationForDownloadTrackerRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

public interface IGetConfigInformationForDownloadTrackerStrategy extends IConfigStrategy<IGetConfigInformationForDownloadTrackerRequestModel, IListModel<IIdLabelModel>> {
  
}
