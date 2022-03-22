package com.cs.core.config.attributiontaxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllowedTagValuesForMasterTaxonomyService
    extends IGetConfigService<IIdParameterModel, IListModel<ITagModel>> {
  
}
