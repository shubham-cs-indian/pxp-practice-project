package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;
import java.util.Map;

public interface IGetAllRelationshipInstancesModel extends IModel {
  
  public static final String ID                                              = "id";
  public static final String REFERENCED_ELEMENTS                             = "referencedElements";
  public static final String REFERENCED_RELATIONSHIPS                        = "referencedRelationships";
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS        = "referenceRelationshipInstanceElements";
  public static final String CONTENT_RELATIONSHIPS                           = "contentRelationships";
  public static final String REFERENCED_RELATIONSHIPS_MAPPING                = "referencedRelationshipsMapping";
  public static final String REFERENCE_NATURE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceNatureRelationshipInstanceElements";
  public static final String NATURE_RELATIONSHIPS                            = "natureRelationships";
  
  public String getId();
  
  public void setId(String id);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public List<IKlassInstanceRelationshipInstance> getContentRelationships();
  
  public void setContentRelationships(
      List<IKlassInstanceRelationshipInstance> contentRelationships);
  
  public Map<String, IRelationship> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IRelationship> referencedRelationships);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships();
  
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceNatureRelationshipInstances);
  
  public Map<String, String> getReferencedRelationshipsMapping();
  
  public void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping);
}
