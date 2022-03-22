package com.cs.ui.runtime.controller.usecase.collections.statics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetStaticCollectionDetails;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetStaticCollectionDetailsController  extends BaseController implements IRuntimeController  {
  
  @Autowired
  protected IGetStaticCollectionDetails getStaticCollectionDetails;
  
  @RequestMapping(value = "/staticcollectionsdetails/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    IIdParameterModel model = new IdParameterModel(id);
    return createResponse(getStaticCollectionDetails.execute(model));
  }
}