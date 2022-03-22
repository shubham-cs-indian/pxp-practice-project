package com.cs.core.runtime.variant.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.strategy.model.context.ConfigDetailsForLinkedVariantDuplicateCheckRequestModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckRequestModel;
import com.cs.core.config.strategy.model.context.IConfigDetailsForLinkedVariantDuplicateCheckResponseModel;
import com.cs.core.config.strategy.usecase.variantcontext.IGetConfigtDetailsForDuplicateLinkedVariantCheckStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;

@Service
public class CheckDuplicateLinkedVariantService
    extends AbstractRuntimeService<ICheckDuplicateLinkedVariantRequestModel, IVoidModel>
    implements ICheckDuplicateLinkedVariantService {
  
  @Autowired
  IGetConfigtDetailsForDuplicateLinkedVariantCheckStrategy getConfigtDetailsForDuplicateLinkedVariantCheckStrategy;
  
  @Autowired
  protected RelationshipInstanceUtil                       relationshipInstanceUtil;
  
  @Override
  protected IVoidModel executeInternal(ICheckDuplicateLinkedVariantRequestModel model)
      throws Exception
  {
    IConfigDetailsForLinkedVariantDuplicateCheckRequestModel reqModel = new ConfigDetailsForLinkedVariantDuplicateCheckRequestModel();
    reqModel.setContextId(model.getContextId());
    reqModel.setRelationshipId(model.getRelationshipId());
    
    IConfigDetailsForLinkedVariantDuplicateCheckResponseModel configDetails = getConfigtDetailsForDuplicateLinkedVariantCheckStrategy
        .execute(reqModel);
    
    if (configDetails.getIsDuplicationAllowed()) {
      return null;
    }
    
    relationshipInstanceUtil.validateLinkedVariantContext(model, configDetails.getRelationshipIId());
    
    return null;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
