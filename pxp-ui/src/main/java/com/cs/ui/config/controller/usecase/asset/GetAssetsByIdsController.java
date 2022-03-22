package com.cs.ui.config.controller.usecase.asset;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.asset.IGetAssetsByIds;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/config")
public class GetAssetsByIdsController extends BaseController implements IConfigController {
  
  @Autowired
  IGetAssetsByIds getAssetsByIds;
  
  @RequestMapping(value = "/assetsbyids", method = RequestMethod.GET)
  public IRESTModel execute(
      @RequestParam(value = "listOfIds", required = true) List<String> listOfIds) throws Exception
  {
    IdsListParameterModel listParameterModel = new IdsListParameterModel();
    listParameterModel.setIds(listOfIds);
    
    return createResponse(getAssetsByIds.execute(listParameterModel));
  }
}
