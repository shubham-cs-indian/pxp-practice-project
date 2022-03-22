package com.cs.ui.config.controller.usecase.mapping;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.mapping.SaveMappingModel;
import com.cs.core.config.interactor.usecase.mapping.ISaveMapping;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveMappingController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveMapping saveMapping;
  
  @RequestMapping(value = "/mappings", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveMappingModel saveMappingModel) throws Exception
  {
    return createResponse(saveMapping.execute(saveMappingModel));
  }
}
