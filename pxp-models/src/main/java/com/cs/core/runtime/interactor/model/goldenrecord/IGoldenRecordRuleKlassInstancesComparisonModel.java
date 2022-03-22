package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesForComparisonModel;

import java.util.List;
import java.util.Map;

public interface IGoldenRecordRuleKlassInstancesComparisonModel extends IModel {
  
  public static String KLASSINSTANCES_DETAILS = "klassInstancesDetails";
  public static String RULE_ID                = "ruleId";
  public static String KLASS_INSTANCE_IDS     = "klassInstanceIds";
  public static String KLASS_IDS              = "klassIds";
  public static String TAXONOMY_IDS           = "taxonomyIds";
  public static String SUPPLIER_CONTENTS_MAP  = "supplierContentsMap";
  public static String GOLDEN_RECORD_ID       = "goldenRecordId";
  
  public Map<String, IKlassInstancesForComparisonModel> getKlassInstancesDetails();
  
  public void setKlassInstancesDetails(
      Map<String, IKlassInstancesForComparisonModel> klassInstancesDetails);
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIdsIds(List<String> taxonomyIds);
  
  public Map<String, List<String>> getSupplierContentsMap();
  
  public void setSupplierContentsMap(Map<String, List<String>> supplierContentsMap);
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
}
