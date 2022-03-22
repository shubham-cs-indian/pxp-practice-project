package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllowedTypesByBaseType
    extends IRuntimeInteractor<IAllowedTypesRequestModel, ITaxonomyHierarchyModel> {
  
}
