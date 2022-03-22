package com.cs.core.runtime.variant.assetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.runtime.variant.abstrct.AbstractCreateInstanceVariantForLimitedObjectService;
import com.cs.dam.runtime.assetinstance.IGetAssetVariantInstancesInTableViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAssetInstanceVariantForLimitedObjectService extends AbstractCreateInstanceVariantForLimitedObjectService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel>
    implements ICreateAssetInstanceVariantForLimitedObjectService {

  @Autowired
  protected IGetKlassInstanceTypeStrategy getAssetInstanceTypeStrategy;

  @Autowired
  protected Long                          assetKlassCounter;

  @Autowired
  protected IGetAssetVariantInstancesInTableViewService getAssetVariantInstancesInTableViewService;
  
  @Override
  public IGetVariantInstancesInTableViewModel execute(
      ICreateVariantForLimitedObjectRequestModel dataModel) throws Exception
  {
    try {
      return super.executeInternal(dataModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForAsset();
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException();
    }
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEASSET;
  }
  
  @Override
  protected Long getCounter()
  {
    return assetKlassCounter++;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.ASSET;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.ASSET_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  protected String getStringBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }

  @Override
  protected IGetVariantInstancesInTableViewModel executeGetTableView(IGetVariantInstanceInTableViewRequestModel tableViewRequestModel)
      throws Exception
  {
    return getAssetVariantInstancesInTableViewService.execute(tableViewRequestModel);
  }
}
