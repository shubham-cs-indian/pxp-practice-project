package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.attribute.IDeleteAttributeStrategy;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSCache;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeleteAttributesService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteAttributeService {
  
  @Autowired
  protected TransactionThreadData           transactionThread;
  
  @Autowired
  IDeleteAttributeStrategy                  neo4jDeleteAttributesStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy getAttributeEntityConfigurationStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IGetEntityConfigurationResponseModel getEntityResponse = getAttributeEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    
    if (!referenceData.keySet().isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }
    
    IBulkDeleteReturnModel responseModel = neo4jDeleteAttributesStrategy.execute(dataModel);
    this.propertyDeleteBGP(responseModel);
    return responseModel;
  }
  
  @SuppressWarnings("unchecked")
  protected void propertyDeleteBGP(IBulkDeleteReturnModel responseModel) throws Exception
  {
    List<String> success = (List<String>) responseModel.getSuccess();
    
    Set<IPropertyDTO> propertyDelete = success.stream().map(attributeCode -> {
      IPropertyDTO propertyDTO = null;
      try {
        propertyDTO = RDBMSUtils.getPropertyByCode(attributeCode);
        CSCache.deletePropertyCache(attributeCode);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      return propertyDTO;
    }).collect(Collectors.toSet());
    
    if (propertyDelete != null && !propertyDelete.isEmpty()) {
      IPropertyDeleteDTO propertyDeleteDTO = new PropertyDeleteDTO();
      propertyDeleteDTO.setProperties(propertyDelete);
      this.workflowUtils.executeApplicationEvent(ApplicationActionType.PROPERTY_DELETE, propertyDeleteDTO.toJSON(), BGPPriority.MEDIUM);
    }
  }
  
}
