package com.cs.dam.runtime.interactor.usecase.assetinstance.attachment;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkCreateAttachmentInstance
    extends IRuntimeInteractor<IBulkCreateAssetInstanceModel, IBulkAssetInstanceInformationModel> {
  
}
