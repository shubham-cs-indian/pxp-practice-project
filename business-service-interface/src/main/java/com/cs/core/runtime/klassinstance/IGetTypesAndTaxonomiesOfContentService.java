package com.cs.core.runtime.klassinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

public interface IGetTypesAndTaxonomiesOfContentService
    extends IRuntimeService<IAllowedTypesRequestModel, IReferencedTypesAndTaxonomiesModel> {
  
}
