package com.cs.core.runtime.variant.assetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAssetInstanceVariantService extends
    AbstractCreateInstanceVariantService<ICreateImageVariantsModel, IGetKlassInstanceModel> implements ICreateAssetInstanceVariantService {
  
  @Autowired
  Long assetKlassCounter;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ICreateImageVariantsModel model) throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForAsset();
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new AssetKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return assetKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.ASSET_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.ASSET;
  }
}
