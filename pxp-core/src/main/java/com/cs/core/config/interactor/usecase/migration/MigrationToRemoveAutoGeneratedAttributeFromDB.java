package com.cs.core.config.interactor.usecase.migration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.config.strategy.usecase.migration.IMigrationToRemoveAutoGeneratedAttributeFromDBStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;

@Service
public class MigrationToRemoveAutoGeneratedAttributeFromDB
    implements IMigrationToRemoveAutoGeneratedAttributeFromDB {
  
  @Autowired
  protected WorkflowUtils                                          workflowUtils;
  
  @Autowired
  protected TransactionThreadData                                  transactionThread;
  
  @Autowired
  protected IMigrationToRemoveAutoGeneratedAttributeFromDBStrategy migrationToRemoveAutoGeneratedAttributeFromDBStrategy;
  
  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception
  {
    IIdsListParameterModel configResponse = migrationToRemoveAutoGeneratedAttributeFromDBStrategy
        .execute(null);
    propertyDeleteBGP(configResponse);
    return null;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  protected void propertyDeleteBGP(IIdsListParameterModel responseModel) throws Exception
  {
    List<String> attribuesList = (List<String>) responseModel.getIds();
    
    Set<IPropertyDTO> propertyDelete = attribuesList.stream()
        .map(attributeCode -> {
          IPropertyDTO propertyDTO = null;
          try {
            propertyDTO = RDBMSUtils.getPropertyByCode(attributeCode);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
          return propertyDTO;
        })
        .collect(Collectors.toSet());
    
    if (propertyDelete != null && !propertyDelete.isEmpty()) {
      IPropertyDeleteDTO propertyDeleteDTO = new PropertyDeleteDTO();
      propertyDeleteDTO.setProperties(propertyDelete);
      this.workflowUtils.executeApplicationEvent(ApplicationActionType.PROPERTY_DELETE,
          propertyDeleteDTO.toJSON(), BGPPriority.MEDIUM);
    }
  }
}
