package com.cs.core.config.klass;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

public interface IGetAllowedTypesByBaseTypeService
    extends IRuntimeService<IAllowedTypesRequestModel, ITaxonomyHierarchyModel> {
  
}
