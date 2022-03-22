package com.cs.core.config.taxonomy;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;

public interface IGetSectionInfoForTaxonomyService
    extends IGetConfigService<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel> {
  
}
