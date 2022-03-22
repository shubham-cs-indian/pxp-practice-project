package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceSingleClone;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceSingleCloneService
    extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateTextAssetInstanceSingleCloneService {

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForTextAsset();
  }

}
