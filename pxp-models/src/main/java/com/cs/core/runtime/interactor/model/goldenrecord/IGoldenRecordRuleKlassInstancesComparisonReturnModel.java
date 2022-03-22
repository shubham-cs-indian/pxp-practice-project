package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGoldenRecordRuleKlassInstancesComparisonReturnModel extends IModel {
  
  public static String KLASSINSTANCES = "klassInstances";
  public static String CONFIG_DETAILS = "configDetails";
  public static String RECOMMENDATION = "recommendation";
  
  public IGoldenRecordRuleKlassInstancesComparisonModel getKlassInstances();
  
  public void setKlassInstances(IGoldenRecordRuleKlassInstancesComparisonModel klassInstances);
  
  public IConfigDetailsForGetKlassInstancesToMergeModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetKlassInstancesToMergeModel configDetails);
  
  // Key :- Property Id (TagId, AttributeId, RelationshipId)
  // Value :- KlassInstanceIds
  public Map<String, List<String>> getRecommendation();
  
  public void setRecommendation(Map<String, List<String>> recommendation);
}
