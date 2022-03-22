package com.cs.dam.config.strategy.usecase.downloadtracker;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGetLabelsByIdsRequestModel;

public interface IGetLabelsByIdsStrategy extends IConfigStrategy<IGetLabelsByIdsRequestModel, IListModel<IIdLabelCodeModel>> {

}
