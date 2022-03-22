package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;

public interface IBulkCreateAttachmentInstanceService
    extends IRuntimeService<IBulkCreateAssetInstanceModel, IBulkAssetInstanceInformationModel> {
  
}
