package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.GetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveTemplateStrategy extends OrientDBBaseStrategy implements ISaveTemplateStrategy {
  
  public static final String useCase = "SaveTemplate";
  
  @Override
  public IGetCustomTemplateModel execute(ISaveCustomTemplateModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("template", model);
    return execute(useCase, requestMap, GetCustomTemplateModel.class);
  }
}
