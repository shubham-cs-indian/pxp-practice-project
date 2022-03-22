package com.cs.ui.config.controller.usecase.textasset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetKlassSaveModel;
import com.cs.core.config.interactor.usecase.textasset.ISaveTextAsset;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveTextAssetController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveTextAsset saveTextAsset;
  
  @RequestMapping(value = "/textassets", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AbstractKlassSaveModel targetModel) throws Exception
  {
    return createResponse(saveTextAsset.execute((ITextAssetKlassSaveModel) targetModel));
  }
}
