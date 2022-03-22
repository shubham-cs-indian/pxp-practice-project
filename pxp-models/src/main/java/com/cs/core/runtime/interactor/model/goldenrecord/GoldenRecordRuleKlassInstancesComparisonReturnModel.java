package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.ConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldenRecordRuleKlassInstancesComparisonReturnModel
    implements IGoldenRecordRuleKlassInstancesComparisonReturnModel {
  
  private static final long                                serialVersionUID = 1L;
  protected IGoldenRecordRuleKlassInstancesComparisonModel klassInstances;
  protected IConfigDetailsForGetKlassInstancesToMergeModel configDetails;
  protected Map<String, List<String>>                      recommendation;
  
  @Override
  public IGoldenRecordRuleKlassInstancesComparisonModel getKlassInstances()
  {
    return klassInstances;
  }
  
  @Override
  @JsonDeserialize(as = GoldenRecordRuleKlassInstancesComparisonModel.class)
  public void setKlassInstances(IGoldenRecordRuleKlassInstancesComparisonModel klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public IConfigDetailsForGetKlassInstancesToMergeModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ConfigDetailsForGetKlassInstancesToMergeModel.class)
  public void setConfigDetails(IConfigDetailsForGetKlassInstancesToMergeModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Map<String, List<String>> getRecommendation()
  {
    if (recommendation == null) {
      recommendation = new HashMap<>();
    }
    return recommendation;
  }
  
  @Override
  public void setRecommendation(Map<String, List<String>> recommendation)
  {
    this.recommendation = recommendation;
  }
}
