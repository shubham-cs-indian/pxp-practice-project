package com.cs.core.runtime.interactor.model.logger;

import org.springframework.stereotype.Service;

@Service
public class TransactionThreadData {
  
  ThreadLocal<TransactionData> transactionData = new ThreadLocal<TransactionData>();
  
  public TransactionData getTransactionData()
  {
    TransactionData data = transactionData.get();
    if (data == null) {
      data = new TransactionData();
      transactionData.set(data);
    }
    return data;
  }
  
  public void setTransactionData(TransactionData logData)
  {
    this.transactionData.set(logData);
  }
  
  public void removeTransactionData()
  {
    this.transactionData.remove();
  }
}
