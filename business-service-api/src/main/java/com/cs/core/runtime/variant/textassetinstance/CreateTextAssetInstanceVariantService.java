package com.cs.core.runtime.variant.textassetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceVariantService extends AbstractCreateInstanceVariantService<ICreateVariantModel, IGetKlassInstanceModel>
    implements ICreateTextAssetInstanceVariantService {
  
  @Autowired protected Long textassetKlassCounter;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ICreateVariantModel model)
      throws Exception
  {
    try
    {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForTextAsset(e);
    } 
    catch (KlassNotFoundException e) { //TODO: handle this exception from orient db plugin
      throw new TextAssetKlassNotFoundException(e);
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
