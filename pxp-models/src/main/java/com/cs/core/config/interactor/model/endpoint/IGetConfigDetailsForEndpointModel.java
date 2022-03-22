package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForEndpointModel extends IConfigDetailsForDataTransferModel {
  
  public static final String ID                    = "id";
  public static final String ENDPOINTID            = "endpointId";
  public static final String SYSTEMID              = "systemId";
  public static final String PROCESS_FLOW          = "processFlow";
  public static final String DATA_RULES            = "dataRules";
  public static final String PROCESS_LABEL         = "processLabel";
  public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public Map<String, Object> getProcessFlow();
  
  public void setProcessFlow(Map<String, Object> processFlow);
  
  public List<IDataRuleModel> getDataRules();
  
  public void setDataRules(List<IDataRuleModel> dataRules);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public String getProcessLabel();
  
  public void setProcessLabel(String processLabel);
  
  public String getProcessDefinitionId();
  
  public void setProcessDefinitionId(String processDefinitionId);
}
