package com.cs.dam.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractRestoreInstance;
import com.cs.dam.runtime.assetinstance.IRestoreAssetInstanceService;

@Service
public class RestoreAssetInstance
    extends AbstractRestoreInstance<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreAssetInstance {
  
  @Autowired
  IRestoreAssetInstanceService restoreAssetInstanceService;
  
  @Override
  protected IBulkResponseModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return restoreAssetInstanceService.execute(model);
  }
  
}
