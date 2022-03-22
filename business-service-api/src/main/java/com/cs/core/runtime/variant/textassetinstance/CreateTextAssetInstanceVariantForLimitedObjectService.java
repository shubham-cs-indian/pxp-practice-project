package com.cs.core.runtime.variant.textassetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantForLimitedObjectService;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceVariantForLimitedObjectService extends AbstractCreateInstanceVariantForLimitedObjectService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel>
    implements ICreateTextAssetInstanceVariantForLimitedObjectService {

  @Autowired
  protected Long textassetKlassCounter;

  @Autowired
  protected IGetTextAssetVariantInstancesInTableViewService getTextAssetVariantInstancesInTableViewService;

  @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForTextAsset();
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return textassetKlassCounter++;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.TEXT_ASSET;
  }

  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;
  }

  @Override
  protected String getStringBaseType()
  {
    return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel)
      throws Exception
  {
    return getTextAssetVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }

}
