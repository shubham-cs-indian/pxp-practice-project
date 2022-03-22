package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;

public interface IGetAvailableMasterTagsForArticleTaxonomyService
    extends IGetConfigService<IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel, IListModel<IGetEntityModel>> {
  
}
