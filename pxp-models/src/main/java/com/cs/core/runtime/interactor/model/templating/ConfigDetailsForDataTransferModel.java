package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDetailsForDataTransferModel implements IConfigDetailsForDataTransferModel {
  
  private static final long             serialVersionUID = 1L;
  protected Map<String, List<String>>   typeIdIdentifierAttributeIds;
  protected Map<String, IDataRuleModel> referencedDataRules;
  protected Map<String, List<String>>   klassDataRules;
  
  @Override
  public Map<String, IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new HashMap<>();
    }
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
  
  @Override
  public Map<String, List<String>> getKlassDataRules()
  {
    return klassDataRules;
  }
  
  @Override
  public void setKlassDataRules(Map<String, List<String>> klassDataRules)
  {
    this.klassDataRules = klassDataRules;
  }
}
