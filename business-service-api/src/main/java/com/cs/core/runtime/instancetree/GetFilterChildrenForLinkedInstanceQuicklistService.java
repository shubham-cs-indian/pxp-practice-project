package com.cs.core.runtime.instancetree;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;

@Service
public class GetFilterChildrenForLinkedInstanceQuicklistService
    extends AbstractGetFilterChildrenValues<IGetFilterChildrenForLIQRequestModel, IGetFilterChildrenResponseModel>
    implements IGetFilterChildrenForLinkedInstanceQuicklistService {
  
  @Override
  protected IConfigDetailsForGetFilterChildrenResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetFilterChildrenRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetFilterChildrenStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IConfigDetailsForGetFilterChildrenRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetFilterChildrenRequestModel();
  }
  
  @Override
  protected List<IGetFilterChildrenModel> executeRuntimeStrategy(IGetFilterChildrenForLIQRequestModel model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(model.getModuleEntities().get(0)));
    String searchExpression = generateSearchExpression(model);
    if (model.getFilterType().equals(CommonConstants.COLOR_VOILATION_FILTER)) {
      return getAllUtils.fillCountsChildDataQualityRules(model, searchExpression, false, configDetails.getRuleViolationsLabels());
    }
    else {
      return getAllUtils.getFilterChildren(model, searchExpression, false);
    }
  }
  
  protected List<String> getModuleEntities(IGetFilterChildrenRequestModel model) throws Exception
  {
    return model.getModuleEntities();
  }
  
}