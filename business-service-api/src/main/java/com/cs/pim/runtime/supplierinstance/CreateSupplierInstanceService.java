package com.cs.pim.runtime.supplierinstance;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.exception.klasssupplier.SupplierKlassNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.abstrct.AbstractCreateInstanceService;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.supplierinstance.UserNotHaveCreatePermissionForSupplier;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSupplierInstanceService
    extends AbstractCreateInstanceService<ICreateInstanceModel, IKlassInstanceInformationModel>
    implements ICreateSupplierInstanceService {
  
  @Autowired
  protected Long supplierKlassCounter;
  
  @Override
  protected IKlassInstanceInformationModel executeInternal(ICreateInstanceModel model)
      throws Exception
  {
    try {
      return super.executeInternal(model);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForSupplier();
    }
    catch (KlassNotFoundException e) {
      throw new SupplierKlassNotFoundException();
    }
  }
  
  @Override
  protected Long getCounter()
  {
    return supplierKlassCounter++;
  }
  
  @Override
  protected String getModuleEntityType()
  {
    return CommonConstants.SUPPLIER_INSTANCE_MODULE_ENTITY;
  }
  
  @Override
  protected BaseType getBaseType()
  {
    return BaseType.SUPPLIER;
  }
}
