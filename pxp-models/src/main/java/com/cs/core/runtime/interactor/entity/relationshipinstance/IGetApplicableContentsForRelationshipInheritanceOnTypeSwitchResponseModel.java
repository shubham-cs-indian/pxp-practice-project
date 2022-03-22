package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;
import java.util.Map;

public interface IGetApplicableContentsForRelationshipInheritanceOnTypeSwitchResponseModel
    extends IModel {
  
  public static final String APPLICABLE_CONTENTS_PER_RELATIONSHIP            = "applicableContentsPerRelationship";
  public static final String APPLICABLE_CONTENTS_PER_RELATIONSHIP_TO_INHERIT = "applicableContentsPerRelationshipToInherit";
  public static final String REFERENCED_INSTANCES                            = "referencedInstances";
  public static final String APPLICABLE_CONTENTS_TO_REMOVE_CONFLICTS         = "applicableContentsToRemoveConflicts";
  
  // key:natureRelId__REL_SIDE__sideId
  // key for internal map: relId__REL_SIDE__sideId
  public Map<String, Map<String, List<String>>> getApplicableContentsPerRelationship();
  
  public void setApplicableContentsPerRelationship(
      Map<String, Map<String, List<String>>> applicableContentsPerRelationship);
  
  // key:natureRelId__REL_SIDE__sideId
  // key for internal map: relId__REL_SIDE__sideId
  public Map<String, Map<String, List<String>>> getApplicableContentsPerRelationshipToInherit();
  
  public void setApplicableContentsPerRelationshipToInherit(
      Map<String, Map<String, List<String>>> applicableContentsPerRelationshipToInherit);
  
  // key:contentId
  public Map<String, IContentTypeIdsInfoModel> getReferencedInstances();
  
  public void setReferencedInstances(Map<String, IContentTypeIdsInfoModel> referencedInstances);
  
  // key:contentId
  public Map<String, List<String>> getApplicableContentsToRemoveConflicts();
  
  public void setApplicableContentsToRemoveConflicts(
      Map<String, List<String>> applicableContentsToRemoveConflicts);
}
