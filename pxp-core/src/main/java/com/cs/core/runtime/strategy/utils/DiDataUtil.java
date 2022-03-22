package com.cs.core.runtime.strategy.utils;

//import com.cs.core.runtime.interactor.constants.application.Constants;
//import com.cs.core.runtime.interactor.model.logger.TransactionData;
//import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
//import com.cs.core.runtime.interactor.model.transfer.IDiExportDataModel;
//import com.cs.core.runtime.interactor.usecase.exporttask.DiDataExportTask;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DiDataUtil {
//
//  @Autowired
//  protected ThreadPoolTaskExecutor taskExecutor;
//
//  @Autowired
//  protected ApplicationContext     applicationContext;
//
//  @Autowired
//  protected TransactionThreadData  controllerThread;
//
//  public void initiateExportDiData(IDiExportDataModel dataModel)
//  {
//    TransactionData transactionData = controllerThread.getTransactionData();
//    String physicalCatalogId = transactionData.getPhysicalCatalogId();
//    if (physicalCatalogId != null
//        && !physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
//      DiDataExportTask dataExportTask = (DiDataExportTask) applicationContext
//          .getBean("diDataExportTask");
//      dataExportTask.setInitiateExportDiDataParameters(dataModel, transactionData);
//      taskExecutor.execute(dataExportTask);
//    }
//  }
//}
