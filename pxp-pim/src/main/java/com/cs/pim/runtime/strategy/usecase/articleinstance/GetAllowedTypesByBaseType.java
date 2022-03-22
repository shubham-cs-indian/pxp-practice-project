package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.klass.IGetAllowedTypesByBaseTypeService;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetAllowedTypesByBaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Write seperate calls for klasses & taxonomies

@Service
public class GetAllowedTypesByBaseType extends AbstractRuntimeInteractor<IAllowedTypesRequestModel, ITaxonomyHierarchyModel>
    implements IGetAllowedTypesByBaseType {

  @Autowired
  protected IGetAllowedTypesByBaseTypeService getAllowedTypesByBaseTypeService;

  @Override public ITaxonomyHierarchyModel executeInternal(IAllowedTypesRequestModel dataModel) throws Exception
  {
    return getAllowedTypesByBaseTypeService.execute(dataModel);
  }
}
