package com.cs.ui.config.controller.usecase.article;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetFilterAndSortDataForKlass;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetFilterAndSortDataForKlassController extends BaseController {
  
  @Autowired
  IGetFilterAndSortDataForKlass getFilterAndSortDataForKlass;
  
  @RequestMapping(value = "/klasstaxonomy/filtersortdata", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(getFilterAndSortDataForKlass.execute(model));
  }
}
