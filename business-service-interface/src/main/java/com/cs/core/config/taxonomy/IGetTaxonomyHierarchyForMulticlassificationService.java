package com.cs.core.config.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;

public interface IGetTaxonomyHierarchyForMulticlassificationService
    extends IRuntimeService<IIdPaginationModel, IConfigEntityTreeInformationModel> {
  
}