package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.attribute.standard.BulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveAttributeStrategy extends OrientDBBaseStrategy
    implements ISaveAttributeStrategy {
  
  @Override
  public IBulkSaveAttributeResponseModel execute(IListModel<ISaveAttributeModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return super.execute(OrientDBBaseStrategy.SAVE_ATTRIBUTE, requestMap,
        BulkSaveAttributeResponseModel.class);
  }
}
