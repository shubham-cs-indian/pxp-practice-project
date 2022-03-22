package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassInstanceRelationshipTabModel extends IGetKlassInstanceModel {
  
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS        = "referenceRelationshipInstanceElements";
  public static final String REFERENCE_NATURE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceNatureRelationshipInstanceElements";
  public static final String CONTENT_RELATIONSHIPS                           = "contentRelationships";
  public static final String NATURE_RELATIONSHIPS                            = "natureRelationships";
  
  public List<IKlassInstanceRelationshipInstance> getContentRelationships();
  
  public void setContentRelationships(
      List<IKlassInstanceRelationshipInstance> contentRelationships);
  
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships();
  
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances);
}
