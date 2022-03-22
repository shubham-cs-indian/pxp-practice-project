package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllowedTagValuesForMasterTaxonomy
    extends IGetConfigInteractor<IIdParameterModel, IListModel<ITagModel>> {
  
}
