package com.cs.pim.runtime.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.exception.textassetinstance.TextAssetInstanceSaveFailedException;
import com.cs.core.runtime.interactor.exception.textassetinstance.UserNotHaveCreatePermissionForTextAsset;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConifgDetailsForTypeSwitchOfInstanceStrategy;
import com.cs.core.runtime.interactor.usecase.taxonomy.NewSwitchInstanceTypeService;

@Service
public class SwitchTextAssetInstanceTypeService extends NewSwitchInstanceTypeService<IKlassInstanceTypeSwitchModel, IGetKlassInstanceModel>
    implements ISwitchTextAssetInstanceTypeService {
  
  @Autowired
  protected IGetConifgDetailsForTypeSwitchOfInstanceStrategy getConifgDetailsForTypeSwitchOfInstanceStrategy;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    try {
      return super.executeInternal(typeSwitchModel);
    }
    catch (UserNotHaveCreatePermission e) {
      throw new UserNotHaveCreatePermissionForTextAsset(e);
    }
    catch (KlassNotFoundException e) { // TODO: handle this exception from
                                       // orient db plugin
      throw new TextAssetKlassNotFoundException(e);
    }
    catch (TextAssetInstanceSaveFailedException e) {
      throw ExceptionUtil.getExceptionFromBuildFailureResponse(e);
    }
    
  }
  
  @Override
  protected IGetConfigDetailsForCustomTabModel getMultiClassificationDetails(IConfigDetailsForSwitchTypeRequestModel requestModel)
      throws Exception
  {
    return getConifgDetailsForTypeSwitchOfInstanceStrategy.execute(requestModel);
  }
  
}
