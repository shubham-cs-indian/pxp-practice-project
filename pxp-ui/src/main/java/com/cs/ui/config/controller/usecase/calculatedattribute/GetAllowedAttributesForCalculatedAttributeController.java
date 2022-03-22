package com.cs.ui.config.controller.usecase.calculatedattribute;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.calculatedattribute.GetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.usecase.calculatedattribute.IGetAllowedAttributesForCalculatedAttribute;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllowedAttributesForCalculatedAttributeController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IGetAllowedAttributesForCalculatedAttribute getAllowedAttributesForCalculatedAttribute;
  
  @RequestMapping(value = "/allowedattributesforcalculatedattribute", method = RequestMethod.POST)
  public IRESTModel execute(
      @RequestBody GetAllowedAttributesForCalculatedAttributeRequestModel requestModel)
      throws Exception
  {
    return createResponse(getAllowedAttributesForCalculatedAttribute.execute(requestModel));
  }
}
