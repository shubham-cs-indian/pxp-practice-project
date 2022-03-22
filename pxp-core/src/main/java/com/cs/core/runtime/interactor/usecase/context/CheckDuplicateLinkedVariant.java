package com.cs.core.runtime.interactor.usecase.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.variant.context.ICheckDuplicateLinkedVariantService;

@Service
public class CheckDuplicateLinkedVariant
    extends AbstractRuntimeInteractor<ICheckDuplicateLinkedVariantRequestModel, IVoidModel>
    implements ICheckDuplicateLinkedVariant {
  
  @Autowired
  ICheckDuplicateLinkedVariantService checkDuplicateLinkedVariantService;
  
  @Override
  protected IVoidModel executeInternal(ICheckDuplicateLinkedVariantRequestModel model)
      throws Exception
  {
    return checkDuplicateLinkedVariantService.execute(model);
  }
  
}
