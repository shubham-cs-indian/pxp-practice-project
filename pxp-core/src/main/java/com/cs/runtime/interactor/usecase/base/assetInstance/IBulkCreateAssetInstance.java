package com.cs.runtime.interactor.usecase.base.assetInstance;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkCreateAssetInstance
    extends IRuntimeInteractor<IBulkCreateAssetInstanceModel, IBulkAssetInstanceInformationModel> {
}
