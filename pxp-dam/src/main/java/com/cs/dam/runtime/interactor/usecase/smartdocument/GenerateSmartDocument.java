package com.cs.dam.runtime.interactor.usecase.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.smartdocument.IGenerateSmartDocumentService;

/**
 * This service is used to generate smart documents.
 * 
 * @author vannya.kalani
 *
 */
@Service
public class GenerateSmartDocument extends
    AbstractRuntimeInteractor<IGenerateSmartDocumentRequestModel, IGenerateSmartDocumentResponseModel>
    implements IGenerateSmartDocument {
  
  @Autowired
  IGenerateSmartDocumentService generateSmartDocumentService;
  
  @Override
  protected IGenerateSmartDocumentResponseModel executeInternal(IGenerateSmartDocumentRequestModel model) throws Exception
  {
    return generateSmartDocumentService.execute(model);
  }
  
}
