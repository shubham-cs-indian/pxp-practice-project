package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceBulkClone;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceBulkCloneService
    extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateTextAssetInstanceBulkCloneService {

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForTextAsset();
  }
}