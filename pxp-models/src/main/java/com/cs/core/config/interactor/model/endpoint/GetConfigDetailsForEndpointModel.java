package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.model.templating.ConfigDetailsForDataTransferModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForEndpointModel extends ConfigDetailsForDataTransferModel
    implements IGetConfigDetailsForEndpointModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected String               id;
  protected String               endpointId;
  protected String               systemId;
  protected Map<String, Object>  processFlow      = new HashMap<>();
  protected List<IDataRuleModel> dataRules        = new ArrayList<>();
  protected String               processLabel;
  protected String               processDefinitionId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public Map<String, Object> getProcessFlow()
  {
    return processFlow;
  }
  
  @Override
  public void setProcessFlow(Map<String, Object> processFlow)
  {
    this.processFlow = processFlow;
  }
  
  @Override
  public List<IDataRuleModel> getDataRules()
  {
    return dataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setDataRules(List<IDataRuleModel> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public String getProcessLabel()
  {
    return processLabel;
  }
  
  @Override
  public void setProcessLabel(String processLabel)
  {
    this.processLabel = processLabel;
  }
  
  @Override
  public String getProcessDefinitionId()
  {
    return processDefinitionId;
  }
  
  @Override
  public void setProcessDefinitionId(String processDefinitionId)
  {
    this.processDefinitionId = processDefinitionId;
  }
}
