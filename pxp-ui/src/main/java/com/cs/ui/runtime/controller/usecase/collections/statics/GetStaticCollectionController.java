package com.cs.ui.runtime.controller.usecase.collections.statics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.usecase.staticcollection.IGetStaticCollection;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetStaticCollectionController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetStaticCollection getStaticCollection;
  
  @RequestMapping(value = "/staticcollections/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id,
      @RequestParam(required = false, defaultValue = "false") Boolean isLoadMore,
      @RequestParam(required = false, defaultValue = "false") Boolean getAllChildren,
      @RequestBody GetKlassInstanceTreeStrategyModel model)
      throws Exception
  {
    model.setId(id);
    model.setGetFolders(false);
    model.setIsLoadMore(isLoadMore);
    model.setGetAllChildren(getAllChildren);
    return createResponse(getStaticCollection.execute(model));
  }
}