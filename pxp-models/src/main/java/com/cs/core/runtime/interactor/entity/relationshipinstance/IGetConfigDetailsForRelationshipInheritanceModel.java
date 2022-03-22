package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.Map;

public interface IGetConfigDetailsForRelationshipInheritanceModel extends IModel {
  
  public static final String MERGED_COUPLING_TYPES = "mergedCouplingTypes";
  
  public Map<String, IIdAndCouplingTypeModel> getMergedCouplingTypes();
  
  public void setMergedCouplingTypes(Map<String, IIdAndCouplingTypeModel> mergedCouplingTypes);
}
