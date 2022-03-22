package com.cs.dam.runtime.interactor.usecase.smartdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.smartdocument.fetchdata.IGetRelationshipInstancesForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.smartdocument.IGetRelationshipInstancesForSmartDocumentService;

@Service
public class GetRelationshipInstancesForSmartDocument extends
    AbstractRuntimeInteractor<IGetEntityForSmartDocumentRequestModel, IListModel<ISmartDocumentKlassInstanceDataModel>>
    implements IGetRelationshipInstancesForSmartDocument {

  @Autowired
  IGetRelationshipInstancesForSmartDocumentService getRelationshipInstancesForSmartDocumentService;
  
  @Override
  protected IListModel<ISmartDocumentKlassInstanceDataModel> executeInternal(IGetEntityForSmartDocumentRequestModel model) throws Exception
  {
    return getRelationshipInstancesForSmartDocumentService.execute(model);
  }
  
}
