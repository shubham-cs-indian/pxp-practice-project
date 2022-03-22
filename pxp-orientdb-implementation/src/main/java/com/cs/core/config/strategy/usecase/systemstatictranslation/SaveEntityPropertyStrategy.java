package com.cs.core.config.strategy.usecase.systemstatictranslation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.entity.hidden.ISaveEntityPropertyResponseModel;
import com.cs.core.config.interactor.model.entity.hidden.SaveEntityPropertyResponseModel;
import com.cs.core.config.interactor.model.hidden.IPropertyModificationInputModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveEntityPropertyStrategy extends OrientDBBaseStrategy
    implements ISaveEntityPropertyStrategy {
  
  @Override
  public ISaveEntityPropertyResponseModel execute(IPropertyModificationInputModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(CommonConstants.DATA_PROPERTY, model);
    return execute(OrientDBBaseStrategy.SAVE_ENTITY_PROPERTY, requestMap, SaveEntityPropertyResponseModel.class);
  }
}
