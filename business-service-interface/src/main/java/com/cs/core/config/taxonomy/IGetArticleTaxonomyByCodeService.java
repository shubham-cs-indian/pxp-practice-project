package com.cs.core.config.taxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetArticleTaxonomyByCodeService
    extends IGetConfigService<IIdParameterModel, IListModel<IConfigEntityTreeInformationModel>> {
}
