package com.cs.core.runtime.instancetree;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;

@Service
public class GetFilterChildrenValuesService extends
    AbstractGetFilterChildrenValues<IGetFilterChildrenRequestModel,IGetFilterChildrenResponseModel>
    implements IGetFilterChildrenValuesService {
  
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
  protected List<IGetFilterChildrenModel> executeRuntimeStrategy(
      IGetFilterChildrenRequestModel model, IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    return getFilterChildrenValues(model, configDetails);
  }

  @Override
  protected List<String> getModuleEntities(IGetFilterChildrenRequestModel model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
}
