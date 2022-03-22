package com.cs.ui.runtime.controller.usecase.goldenrecord;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.goldenrecord.GetTypeInfoForSourcesRequestModel;
import com.cs.pim.runtime.goldenrecord.IGetTypeInfoForSources;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetTypeInfoForSourcesController extends BaseController implements IRuntimeController {

	@Autowired
	IGetTypeInfoForSources getTypeInfoFromSources;
	
	@RequestMapping(value = "/gettypeinfoforsources", method = RequestMethod.POST)
	public IRESTModel execute(@RequestBody GetTypeInfoForSourcesRequestModel model) throws Exception
	  {
	    return createResponse(getTypeInfoFromSources.execute(model));
	  }
}
