package com.cs.core.config.strategy.usecase.variant;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.model.context.ConfigDetailsForLinkedVariantDuplicateCheckResponseModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckRequestModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckResponseModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetConfigtDetailsForDuplicateLinkedVariantCheckStrategy;

@Component
public class GetConfigtDetailsForDuplicateLinkedVariantCheckStrategy extends OrientDBBaseStrategy
    implements IGetConfigtDetailsForDuplicateLinkedVariantCheckStrategy {
  
  @Override
  public IConfigDetailsForLinkedVariantDuplicateCheckResponseModel execute(
      IConfigDetailsForLinkedVariantDuplicateCheckRequestModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_DUPLICATE_LINKED_VARIANT, model,
        ConfigDetailsForLinkedVariantDuplicateCheckResponseModel.class);
  }
}
