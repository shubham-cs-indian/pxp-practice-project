package com.cs.ui.config.controller.usecase.attribute;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.attribute.IGetGridAttributes;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetGridAttributesController extends BaseController implements IConfigController {
  
  @Autowired
  IGetGridAttributes getAttributeForGrid;
  
  @RequestMapping(value = "/attributes/grid", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel getAllModel) throws Exception
  {
    return createResponse(getAttributeForGrid.execute(getAllModel));
  }
}
