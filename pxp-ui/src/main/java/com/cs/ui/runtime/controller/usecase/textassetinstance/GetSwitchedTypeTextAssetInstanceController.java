package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.ISwitchTextAssetInstanceType;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value ="/runtime")
public class GetSwitchedTypeTextAssetInstanceController extends BaseController
    implements IRuntimeController {
  @Autowired
  ISwitchTextAssetInstanceType switchTextAssetInstanceType;
  
  @RequestMapping(value = "/textassetinstances/typeswitch", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody KlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    return createResponse(switchTextAssetInstanceType.execute(typeSwitchModel));
  } 
}
