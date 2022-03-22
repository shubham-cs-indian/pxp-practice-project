package com.cs.pim.runtime.textassetinstance;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.abstrct.AbstractCreateInstanceService;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceService extends AbstractCreateInstanceService<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateTextAssetInstanceService {
  
  @Autowired protected Long textassetKlassCounter;

  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
  {
    try
    {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForTextAsset();
    }
    catch (KlassNotFoundException e) { //TODO: handle this exception from orient db plugin
      throw new TextAssetKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return textassetKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;
  }

  @Override
  protected BaseType getBaseType()
  {
    return BaseType.TEXT_ASSET;
  }
}
