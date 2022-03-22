package com.cs.core.runtime.interactor.usecase.exporttask;

//import com.cs.core.rdbms.driver.RDBMSLogger;
//import com.cs.core.runtime.interactor.model.logger.TransactionData;
//import com.cs.core.runtime.interactor.model.transfer.IDiExportDataModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//@Component
//@Scope("prototype")
//public class DiDataExportTask implements Runnable {
//
//  @Autowired
//  protected InitiateExportDiData initiateExportDiData;
//
//  protected IDiExportDataModel   dataModel;
//
//  protected TransactionData      transactionData;
//
//  public void setInitiateExportDiDataParameters(IDiExportDataModel dataModel,
//      TransactionData transactionData)
//  {
//    this.dataModel = dataModel;
//    this.transactionData = transactionData;
//  }
//
//  @Override
//  public void run()
//  {
//    try {
//      initiateExportDiData.setTransactionData(transactionData);
//      initiateExportDiData.execute(dataModel);
//    }
//    catch (Exception e) {
//      RDBMSLogger.instance().exception(e);
//    }
//  }
//}
