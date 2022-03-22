package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.config.interactor.model.articleimportcomponent.ComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiKlassInstanceImportMessageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JmsImportModel implements IJmsImportModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected IComponentModel                    componentModel   = new ComponentModel();
  // protected Map<String, Object> contentMap = new HashMap<>();
  protected List<Map<String, Object>>          contentList      = new ArrayList<>();
  protected ITransactionDataModel              transactionData;
  protected IDiKlassInstanceImportMessageModel data;
  
  @Override
  public IComponentModel getComponentModel()
  {
    return componentModel;
  }
  
  @Override
  public void setComponentModel(IComponentModel componentModel)
  {
    this.componentModel = componentModel;
  }
  
  @Override
  public ITransactionDataModel getTransactionDataModel()
  {
    return transactionData;
  }
  
  @Override
  public void setTransactionDataModel(ITransactionDataModel transactionData)
  {
    this.transactionData = transactionData;
  }
  
  /*@Override
  public Map<String, Object> getContentMap()
  {
    return contentMap;
  }
  
  @Override
  public void setContentMap(Map<String, Object> contentMap)
  {
    this.contentMap = contentMap;
  }*/
  
  /*@Override
  public List<Map<String, Object>> getContentList()
  {
    return contentList;
  }
  
  @Override
  public void setContentList(List<Map<String, Object>> contentList)
  {
    this.contentList = contentList;
  }*/
  
  @Override
  public IDiKlassInstanceImportMessageModel getData()
  {
    return data;
  }
  
  @Override
  public void setData(IDiKlassInstanceImportMessageModel data)
  {
    this.data = data;
  }
}
