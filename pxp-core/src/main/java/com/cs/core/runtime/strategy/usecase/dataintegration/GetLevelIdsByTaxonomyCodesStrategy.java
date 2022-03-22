package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.ITaxonomyCodeLevelIdMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.TaxonomyCodeLevelIdMapModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetLevelIdsByTaxonomyCodesStrategy extends OrientDBBaseStrategy
    implements IGetLevelIdsByTaxonomyCodesStrategy {
  
  private static final String usecase = "GetLevelIdsByTaxonomyCodes";
  
  @Override
  public ITaxonomyCodeLevelIdMapModel execute(IEntityLabelCodeMapModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IEntityLabelCodeMapModel.ENTITYlABELCODEMAP, model.getEntityLabelCodeMap());
    
    return execute(usecase, requestMap, TaxonomyCodeLevelIdMapModel.class);
  }
}
