package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForDataTransferModel extends IModel {
  
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS = "typeIdIdentifierAttributeIds";
  public static final String KLASS_DATA_RULES               = "klassDataRules";
  public static final String REFERENCED_DATA_RULES          = "referencedDataRules";
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getKlassDataRules();
  
  public void setKlassDataRules(Map<String, List<String>> klassDataRules);
  
  public Map<String, IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules);
}
