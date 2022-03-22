package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.references.IKlassInstanceReferenceInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IGetKlassInstanceCustomTabModel extends IGetKlassInstanceModel {
  
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS        = "referenceRelationshipInstanceElements";
  public static final String REFERENCE_NATURE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceNatureRelationshipInstanceElements";
  public static final String REFERENCED_CONTENTS                             = "referencedContents";
  public static final String PROPERTY_IDS_HAVING_TASK                        = "propertyIdsHavingTask";
  public static final String CONTENT_RELATIONSHIPS                           = "contentRelationships";
  public static final String NATURE_RELATIONSHIPS                            = "natureRelationships";
  public static final String ATTRIBUTE_VARIANTS_STATS                        = "attributeVariantsStats";
  public static final String REFERENCES                                      = "references";
  public static final String NATURE_REFERENCES                               = "natureReferences";
  public static final String REFERENCED_ELEMENT_INSTNACES                    = "referencedElementInstances";
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances);
  
  // key : contentId
  // value : content label
  public Map<String, String> getReferencedContents();
  
  public void setReferencedContents(Map<String, String> referencedContents);
  
  public Set<String> getPropertyIdsHavingTask();
  
  public void setPropertyIdsHavingTask(Set<String> propertyIdsHavingTask);
  
  public List<IKlassInstanceRelationshipInstance> getContentRelationships();
  
  public void setContentRelationships(
      List<IKlassInstanceRelationshipInstance> contentRelationships);
  
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships();
  
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships);
  
  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantsStats();
  
  public void setAttributeVariantsStats(
      Map<String, IAttributeVariantsStatsModel> attributeVariantsStats);
  
  public List<IKlassInstanceReferenceInstanceModel> getReferences();
  
  public void setReferences(List<IKlassInstanceReferenceInstanceModel> references);
  
  public List<IKlassInstanceReferenceInstanceModel> getNatureReferences();
  
  public void setNatureReferences(List<IKlassInstanceReferenceInstanceModel> natureReferences);
  
  public Map<String, IKlassInstanceInformationModel> getReferencedElementInstances();
  
  public void setReferencedElementInstances(
      Map<String, IKlassInstanceInformationModel> referencedElementInstances);
}
