package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IIdPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetTaxonomyHierarchyForMulticlassification
    extends IRuntimeInteractor<IIdPaginationModel, IConfigEntityTreeInformationModel> {
  
}