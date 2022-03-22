package com.cs.core.config.relationship;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.relationship.IDeleteRelationshipStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSCache;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;

@Service
public class DeleteRelationshipsService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteRelationshipService {
  
  @Autowired
  protected IDeleteRelationshipStrategy     deleteRelationshipStrategy;
  
  @Autowired
  protected TransactionThreadData           transactionThread;
  
  @Autowired
  protected IGetEntityConfigurationStrategy getRelationshipEntityConfigurationStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    
    IGetEntityConfigurationResponseModel getEntityResponse = getRelationshipEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    
    if (!referenceData.keySet().isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }
    
    // delete relationship from orient
    IBulkDeleteReturnModel returnModel = deleteRelationshipStrategy.execute(dataModel);
    this.propertyDeleteBGP(returnModel);
    return returnModel;
  }
  
  @SuppressWarnings("unchecked")
  protected void propertyDeleteBGP(IBulkDeleteReturnModel responseModel) throws Exception
  {
    List<String> success = (List<String>) responseModel.getSuccess();
    
    Set<IPropertyDTO> propertyDelete = success.stream().map(relationshipCode -> {
      IPropertyDTO propertyDTO = null;
      try {
        propertyDTO = RDBMSUtils.getPropertyByCode(relationshipCode);
        CSCache.deletePropertyCache(relationshipCode);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      return propertyDTO;
    }).collect(Collectors.toSet());
    
    IPropertyDeleteDTO propertyDeleteDTO = new PropertyDeleteDTO();
    if (propertyDelete != null && !propertyDelete.isEmpty()) {
      propertyDeleteDTO.setProperties(propertyDelete);
      this.workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.PROPERTY_DELETE, propertyDeleteDTO.toJSON(),
          BGPPriority.MEDIUM);
    }
  }
}
