package com.cs.core.config.attributiontaxonomy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.attributiontaxonomy.IDeleteMasterTaxonomyService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IDeleteMasterTaxonomyStrategy;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteMasterTaxonomyService
    extends AbstractDeleteTagTaxonomy<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteMasterTaxonomyService {
  
  @Autowired
  protected IDeleteMasterTaxonomyStrategy deleteMasterTaxonomyStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getTaxonomyEntityConfigurationStrategy;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    IGetEntityConfigurationResponseModel getEntityResponse = getTaxonomyEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(model.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    boolean hasChildDependency = getEntityResponse.isHasChildDependency();
    
    if (!referenceData.keySet()
        .isEmpty() || hasChildDependency) {
      throw new EntityConfigurationDependencyException();
    }
    
    return super.executeInternal(model);
  }
  
  @Override
  public IBulkDeleteReturnModel executeDeleteAttributionTaxonomy(IIdsListParameterModel dataModel)
      throws Exception
  {
    return deleteMasterTaxonomyStrategy.execute(dataModel);
  }
}
