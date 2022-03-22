package com.cs.pim.runtime.strategy.usecase.articleinstance.variants;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDataForCreateVariantInTableView<P extends IIdParameterModel, R extends IGetConfigDetailsForCustomTabModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy getConfigDetailsForCustomTabStrategy;
  
  @Autowired
  protected PermissionUtils                       permissionUtils;
  
  @Override
  protected R executeInternal(P idModel) throws Exception
  {
    List<String> klassIds = new ArrayList<>();
    klassIds.add(idModel.getId());
    IMulticlassificationRequestModel requestModel = new MulticlassificationRequestModel();
    requestModel.setKlassIds(klassIds);
    requestModel.setUserId(context.getUserId());
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForCustomTabStrategy
        .execute(requestModel);
    
    permissionUtils.manageKlassInstancePermissions(configDetails);
    
    return (R) configDetails;
  }
  
}
