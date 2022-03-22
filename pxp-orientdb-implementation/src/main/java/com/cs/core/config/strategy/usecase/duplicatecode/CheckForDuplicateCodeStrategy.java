package com.cs.core.config.strategy.usecase.duplicatecode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;
import com.cs.core.config.interactor.model.duplicatecode.CheckForDuplicateCodeReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CheckForDuplicateCodeStrategy extends OrientDBBaseStrategy
    implements ICheckForDuplicateCodeStrategy {
  
  @Override
  public ICheckForDuplicateCodeReturnModel execute(ICheckForDuplicateCodeModel dataModel)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(ICheckForDuplicateCodeModel.ID, dataModel.getId());
    requestMap.put(ICheckForDuplicateCodeModel.ENTITYTYPE, dataModel.getEntityType());
    
    return execute(OrientDBBaseStrategy.CHECK_FOR_DUPLICATE_CODE, requestMap,
        CheckForDuplicateCodeReturnModel.class);
  }
}
