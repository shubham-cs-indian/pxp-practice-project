package com.cs.dam.runtime.interactor.usecase.assetinstance.attachment;

import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IBulkCreateAttachmentInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkCreateAttachmentInstance extends AbstractRuntimeInteractor<IBulkCreateAssetInstanceModel, IBulkAssetInstanceInformationModel>
    implements IBulkCreateAttachmentInstance {
  
  @Autowired
  protected Long     assetKlassCounter;

  @Autowired
  protected IBulkCreateAttachmentInstanceService bulkCreateAttachmentInstanceService;

  @Override
  protected IBulkAssetInstanceInformationModel executeInternal(IBulkCreateAssetInstanceModel model)
      throws Exception
  {
    try {
      return bulkCreateAttachmentInstanceService.execute(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForAsset();
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException();
    }
  }

}
