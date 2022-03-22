package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.BulkSaveTemplatesResponseModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveTemplateStrategy extends OrientDBBaseStrategy
    implements IBulkSaveTemplateStrategy {
  
  @Override
  public IBulkSaveTemplatesResponseModel execute(IListModel<ISaveCustomTemplateModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    return execute(BULK_SAVE_TEMPLATE, requestMap, BulkSaveTemplatesResponseModel.class);
  }
}
