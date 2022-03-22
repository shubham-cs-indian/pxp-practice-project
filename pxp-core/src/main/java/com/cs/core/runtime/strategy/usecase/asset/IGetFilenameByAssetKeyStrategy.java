package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetFileNameModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetObjectKeyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetFilenameByAssetKeyStrategy
    extends IRuntimeStrategy<IAssetObjectKeyModel, IAssetFileNameModel> {
  
}
