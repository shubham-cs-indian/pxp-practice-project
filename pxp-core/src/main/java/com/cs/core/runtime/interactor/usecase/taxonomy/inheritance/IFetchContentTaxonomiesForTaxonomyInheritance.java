package com.cs.core.runtime.interactor.usecase.taxonomy.inheritance;

import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyInheritanceRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IFetchContentTaxonomiesForTaxonomyInheritance extends IRuntimeInteractor<ITaxonomyInheritanceRequestModel, IGetInheritanceTaxonomyIdsResponseModel> {
}