package com.cs.dam.runtime.smartdocument;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;

public interface IGetVariantInstancesForSmartDocumentService extends
    IRuntimeService<IGetEntityForSmartDocumentRequestModel, IListModel<ISmartDocumentKlassInstanceDataModel>> {
  
}
