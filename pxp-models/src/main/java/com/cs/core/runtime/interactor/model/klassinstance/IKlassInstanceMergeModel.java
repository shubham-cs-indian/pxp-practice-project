package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

import java.util.Map;

public interface IKlassInstanceMergeModel extends IRuntimeModel {
  
  public static final String RESOLVED           = "resolved";
  public static final String STRUCTURE_CHILDREN = "structureChildren";
  public static final String LATEST_VERSION_ID  = "latestVersionId";
  public static final String WORKING_VERSION_ID = "workingVersionId";
  
  Map<String, String> getResolved();
  
  void setResolved(Map<String, String> resolved);
  
  Map<String, ? extends IKlassInstanceStructureMergeModel> getStructureChildren();
  
  void setStructureChildren(
      Map<String, ? extends IKlassInstanceStructureMergeModel> structureChildren);
  
  Long getLatestVersionId();
  
  void setLatestVersionId(Long latestVersionId);
  
  Long getWorkingVersionId();
  
  void setWorkingVersionId(Long workingVersionId);
}
