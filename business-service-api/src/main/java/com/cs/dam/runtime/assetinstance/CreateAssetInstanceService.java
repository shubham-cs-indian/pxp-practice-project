package com.cs.dam.runtime.assetinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.abstrct.AbstractCreateInstanceService;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.exception.assetinstance.UserNotHaveCreatePermissionForAsset;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAssetInstanceService
    extends AbstractCreateInstanceService<ICreateAssetInstanceModel, IKlassInstanceInformationModel>
    implements ICreateAssetInstanceService {
  
  /* @Autowired
  protected ICreateAssetInstanceStrategy           createAssetInstanceStrategy;*/
  
  @Autowired
  protected KlassInstanceUtils klassInstanceUtils;
  
  @Autowired
  protected Long               assetKlassCounter;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateAssetInstanceModel model)
      throws Exception
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
  
  public IAttributeInstance getAttributeInstance(IBaseEntityDTO baseEntityDto,
      IPropertyRecordDTO propertyRecord)
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    
    attributeInstance.setKlassInstanceId(baseEntityDto.getBaseEntityID());
    attributeInstance.setCode(propertyRecord.getProperty()
        .getCode());
    attributeInstance.setLanguage(baseEntityDto.getBaseLocaleID());
    attributeInstance.setBaseType(AttributeInstance.class.getName());
    attributeInstance.setValue(baseEntityDto.getBaseEntityName());
    attributeInstance.setAttributeId(propertyRecord.getProperty()
        .getCode());
    attributeInstance.setId(String.valueOf(((IValueRecordDTO) propertyRecord).getValueIID()));
    attributeInstance.setIid(((IValueRecordDTO) propertyRecord).getValueIID());
    return attributeInstance;
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.CREATEASSET;
  }
}
