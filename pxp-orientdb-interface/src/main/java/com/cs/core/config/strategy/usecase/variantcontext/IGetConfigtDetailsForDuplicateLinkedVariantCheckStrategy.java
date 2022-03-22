package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckRequestModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckResponseModel;

public interface IGetConfigtDetailsForDuplicateLinkedVariantCheckStrategy extends
    IConfigStrategy<IConfigDetailsForLinkedVariantDuplicateCheckRequestModel, IConfigDetailsForLinkedVariantDuplicateCheckResponseModel> {
  
}
