package com.cs.ui.runtime.controller.usecase.initializedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.interactor.usecase.initialize.IStaticLabelTranslationsInitialize;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping
public class InitializeStaticLabelTranslationsController extends BaseController {

  @Autowired
  IStaticLabelTranslationsInitialize staticLabelTranslationsInitialize;

  @RequestMapping(value = "/translationinitialization", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody VoidModel voidModel) throws Exception {
    return createResponse(staticLabelTranslationsInitialize.execute(voidModel));
  }


}