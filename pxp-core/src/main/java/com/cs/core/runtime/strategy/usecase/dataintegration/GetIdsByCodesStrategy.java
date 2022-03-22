package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.EntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetIdsByCodesStrategy extends OrientDBBaseStrategy implements IGetIdsByCodesStrategy {
  
  private static final String usecase = "GetIdsByCodes";
  
  @Override
  public IEntityLabelCodeMapModel execute(IEntityLabelCodeMapModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IEntityLabelCodeMapModel.ENTITYlABELCODEMAP, model.getEntityLabelCodeMap());
    
    return execute(usecase, requestMap, EntityLabelCodeMapModel.class);
  }
}
