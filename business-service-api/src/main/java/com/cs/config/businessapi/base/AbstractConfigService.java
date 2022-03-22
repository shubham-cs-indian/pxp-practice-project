package com.cs.config.businessapi.base;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class AbstractConfigService<P extends IModel, R extends IModel>
    extends AbstractService<P, R> implements IConfigService<P, R> {
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.UNDEFINED;
  }
  
  @Override
  public String getEntity(P model)
  {
    return "ConfigData";
  }
  
  protected void auditLog(List<IAuditLogModel> auditLogInfoList) throws Exception
  {
    ServiceType serviceType = getServiceType();
    try {
      IAuditLogDAO auditlogDAO = RDBMSUtils.newUserSessionDAO().newAuditLogDAO();
      List<IAuditLogDTO> auditLogDTOs = new ArrayList<>();
      Long currentTimeMillis = System.currentTimeMillis();
      auditLogInfoList.forEach(auditLogInfo -> { 
        if (auditLogInfo.getActivity() == null) {
          auditLogInfo.setActivity(serviceType.toString());
        }
        auditLogInfo.setDate(currentTimeMillis);
        auditLogInfo.setUserName(context.getUserName());
        auditLogInfo.setIpAddress(context.getClientIPAddress());
        
         IAuditLogDTO auditLogDTO = auditlogDAO.newAuditLogDTO(ServiceType.valueOf(auditLogInfo.getActivity()),
            currentTimeMillis, Elements.valueOf(auditLogInfo.getElement()),
            auditLogInfo.getElementCode(), auditLogInfo.getElementName(),
            auditLogInfo.getElementType(), Entities.valueOf(auditLogInfo.getEntityType()),
            context.getClientIPAddress(), context.getUserName());
        
        auditLogDTOs.add(auditLogDTO);
      });
      
      auditlogDAO.createAuditLogs(auditLogDTOs.toArray(new IAuditLogDTO[] {}));
    }
    catch (RDBMSException | SecurityException e) {
      System.out.println("=================================================================");
      System.out.println(" === Error in Audit log === ");
      System.out.println("=================================================================");
    }
  }
}
