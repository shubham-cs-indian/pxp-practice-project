package com.cs.ui.runtime.controller.usecase.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.smartdocument.GenerateSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentResponseModel;
import com.cs.dam.runtime.interactor.usecase.smartdocument.IGenerateSmartDocument;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is used to generate smart documents.
 * @author vannya.kalani
 *
 */
@RestController
@RequestMapping(value="/runtime")
public class GenerateSmartDocumentController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGenerateSmartDocument generateSmartDocument;

  @PostMapping(value = "/generatesmartdocument")
  public IGenerateSmartDocumentResponseModel execute(@RequestBody GenerateSmartDocumentRequestModel requestModel)
      throws Exception
  {
    return generateSmartDocument.execute(requestModel);
  }
}
