package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAvailableMasterTagsForArticleTaxonomyStrategy extends
    IConfigStrategy<IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel, IListModel<IGetEntityModel>> {
  
}
