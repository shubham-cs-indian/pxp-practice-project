package com.cs.dam.runtime.interactor.usecase.smartdocument;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.smartdocument.fetchdata.IGetVariantInstancesForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.smartdocument.IGetVariantInstancesForSmartDocumentService;

/**
 * 
 * Description:- Fetch data of embedded variant for smart document based on
 * parentId and contextID.
 * 
 * @author rahul.sehrawat
 *
 */
@Component
public class GetVariantInstancesForSmartDocument extends
    AbstractRuntimeInteractor<IGetEntityForSmartDocumentRequestModel, IListModel<ISmartDocumentKlassInstanceDataModel>>
    implements IGetVariantInstancesForSmartDocument {
  
  IGetVariantInstancesForSmartDocumentService getVariantInstancesForSmartDocumentService;
  
  @Override
  protected IListModel<ISmartDocumentKlassInstanceDataModel> executeInternal(IGetEntityForSmartDocumentRequestModel model) throws Exception
  {
    return getVariantInstancesForSmartDocumentService.execute(model);
  }
}
