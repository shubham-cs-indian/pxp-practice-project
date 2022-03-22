package com.cs.dam.runtime.assetinstance;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBulkAssetInstancesService extends AbstractCreateBulkAssetInstances<IBulkCreateAssetInstanceModel, IBulkAssetInstanceInformationModel>
    implements ICreateBulkAssetInstancesService {
  
  @Autowired
  protected Long assetKlassCounter;
  
  @Override
  protected IBulkAssetInstanceInformationModel executeInternal(IBulkCreateAssetInstanceModel model)
      throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForAsset();
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException();
    }
  }
  
  @Override
  protected IKlassInstance getKlassInstanceObjectToCreate() throws Exception
  {
    return new AssetInstance();
  }
  
  @Override
  protected Long getCounter()
  {
    return assetKlassCounter++;
  }
}
