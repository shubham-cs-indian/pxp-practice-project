package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOrganizeTreeDataResponseModel;

public interface IGetConfigDetailForOrganizeTreeDataStrategy extends
    IConfigStrategy<IConfigDetailsForGetKlassTaxonomyTreeRequestModel, IConfigDetailsOrganizeTreeDataResponseModel> {
  
}
