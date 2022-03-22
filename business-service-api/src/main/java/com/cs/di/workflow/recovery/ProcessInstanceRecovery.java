package com.cs.di.workflow.recovery;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.utils.DiUtils;

@Component
public class ProcessInstanceRecovery
    implements IProcessInstanceRecovery {
  
  @Transactional(transactionManager = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public void updateStuckProcessInstanceStatusDueToRestart(List<String> stuckProcessIds)
  {
    try {
      ILocaleCatalogDAO localeCatalogDAO = DiUtils
          .createLocaleCatalogDAO(DiUtils.createUserSessionDto(), DiUtils.createTransactionData());
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      workflowStatusDAO.updateWFStatusForStuckInstances(stuckProcessIds);
    }
    catch (Exception e) {
      System.out.println("### Stuck workflow process instances status update failed ###");
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
  
  @Transactional(transactionManager = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public void updateFailedProcessInstanceStatus(List<String> failedProcessIds)
  {
    try {
      ILocaleCatalogDAO localeCatalogDAO = DiUtils
          .createLocaleCatalogDAO(DiUtils.createUserSessionDto(), DiUtils.createTransactionData());
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      workflowStatusDAO.updateWFStatusForFailedInstances(failedProcessIds);
    }
    catch (Exception e) {
      System.out.println("### Failed workflow process instances status update failed ###");
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }  
  
}
