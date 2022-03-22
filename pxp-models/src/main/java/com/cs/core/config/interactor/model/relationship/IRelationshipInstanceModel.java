package com.cs.core.config.interactor.model.relationship;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

public interface IRelationshipInstanceModel extends IModel {
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances);
  
  public List<IKlassInstanceRelationshipInstance> getContentRelationships();
  
  public void setContentRelationships(
      List<IKlassInstanceRelationshipInstance> contentRelationships);
  
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships();
  
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships);
  
}
