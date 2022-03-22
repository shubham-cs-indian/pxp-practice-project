package com.cs.ui.runtime.controller.usecase.version.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.version.KlassInstanceVersionsComparisonModel;
import com.cs.dam.runtime.interactor.version.IGetAssetInstanceVersionsForComparison;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAssetInstanceVersionsToCompareController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IGetAssetInstanceVersionsForComparison getAssetInstanceVersionsForComparison;
  
  @RequestMapping(value = "/assetinstances/versions/comparison/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody KlassInstanceVersionsComparisonModel model) throws Exception
  {
    model.setId(id);
    
    return createResponse(getAssetInstanceVersionsForComparison.execute(model));
  }
}
