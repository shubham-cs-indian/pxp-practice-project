package com.cs.core.config.propertycollection;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.propertycollection.IBulkDeletePropertyCollectionReturnModel;
import com.cs.core.config.interactor.model.propertycollection.IBulkDeleteSuccessPropertyCollectionModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.propertycollection.IDeletePropertyCollectionStrategy;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeletePropertyCollectionService
    extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeletePropertyCollectionService {
  
  @Autowired
  protected IDeletePropertyCollectionStrategy deletePropertyCollectionStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getPropertyCollectionEntityConfigurationStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel propertyCollectionModel)
      throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getPropertyCollectionEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(propertyCollectionModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet()
        .isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    IBulkDeletePropertyCollectionReturnModel strategyResponseModel = deletePropertyCollectionStrategy
        .execute(propertyCollectionModel);
    IBulkDeleteSuccessPropertyCollectionModel successResponse = (IBulkDeleteSuccessPropertyCollectionModel) strategyResponseModel
        .getSuccess();
    Map<String, List<String>> typeIdsAssociatedMap = successResponse.getAssociatedTypeIds();
    if (!typeIdsAssociatedMap.isEmpty()) {
      IContentDiffModelToPrepareDataForBulkPropagation contentDiffModelToPrepareDataForBulkPropagation = new ContentDiffModelToPrepareDataForBulkPropagation();
      contentDiffModelToPrepareDataForBulkPropagation
          .setDeletedPropertiesFromSource(typeIdsAssociatedMap);
      
      //TODO: BGP
    }
    List<String> successDeletedPropertyCollectionIds = successResponse.getSuccess();
    IBulkDeleteReturnModel responseModel = new BulkDeleteReturnModel();
    responseModel.setSuccess(successDeletedPropertyCollectionIds);
    responseModel.setFailure(strategyResponseModel.getFailure());
    responseModel.setAuditLogInfo(strategyResponseModel.getAuditLogInfo());
    
    return responseModel;
  }
}
