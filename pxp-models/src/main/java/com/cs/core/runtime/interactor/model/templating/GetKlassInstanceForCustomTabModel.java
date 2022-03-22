package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interacter.model.references.KlassInstanceReferenceInstanceModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.attribute.AttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.references.IKlassInstanceReferenceInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class GetKlassInstanceForCustomTabModel extends AbstractGetKlassInstanceModel
    implements IGetKlassInstanceCustomTabModel {
  
  private static final long                                   serialVersionUID = 1L;
  
  protected Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements;
  protected Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements;
  protected Map<String, String>                               referencedContents;
  protected Set<String>                                       propertyIdsHavingTask;
  protected List<IKlassInstanceRelationshipInstance>          contentRelationships;
  protected List<IKlassInstanceRelationshipInstance>          natureRelationships;
  protected Map<String, IAttributeVariantsStatsModel>         attributeVariantsStats;
  protected List<IKlassInstanceReferenceInstanceModel>        references;
  protected List<IKlassInstanceReferenceInstanceModel>        natureReferences;
  protected Map<String, IKlassInstanceInformationModel>       referencedElementInstances;
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getContentRelationships()
  {
    if (contentRelationships == null) {
      contentRelationships = new ArrayList<>();
    }
    return contentRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  public void setContentRelationships(List<IKlassInstanceRelationshipInstance> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getNatureRelationships()
  {
    if (natureRelationships == null) {
      natureRelationships = new ArrayList<>();
    }
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceRelationshipInstance.class)
  public void setNatureRelationships(List<IKlassInstanceRelationshipInstance> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements()
  {
    if (referenceRelationshipInstanceElements == null) {
      referenceRelationshipInstanceElements = new HashMap<>();
    }
    return referenceRelationshipInstanceElements;
  }
  
  @JsonDeserialize(contentUsing = customDeserializer.class)
  @Override
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements)
  {
    this.referenceRelationshipInstanceElements = referenceRelationshipInstanceElements;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements()
  {
    if (referencedNatureRelationshipInstanceElements == null) {
      referencedNatureRelationshipInstanceElements = new HashMap<>();
    }
    return referencedNatureRelationshipInstanceElements;
  }
  
  @Override
  @JsonDeserialize(contentUsing = customDeserializer.class)
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements)
  {
    this.referencedNatureRelationshipInstanceElements = referencedNatureRelationshipInstanceElements;
  }
  
  @Override
  public Map<String, String> getReferencedContents()
  {
    if (referencedContents == null) {
      referencedContents = new HashMap<>();
    }
    return referencedContents;
  }
  
  @Override
  public void setReferencedContents(Map<String, String> referencedContents)
  {
    this.referencedContents = referencedContents;
  }
  
  @Override
  public Set<String> getPropertyIdsHavingTask()
  {
    if (propertyIdsHavingTask == null) {
      propertyIdsHavingTask = new HashSet<>();
    }
    return propertyIdsHavingTask;
  }
  
  @Override
  public void setPropertyIdsHavingTask(Set<String> propertyIdsHavingTask)
  {
    this.propertyIdsHavingTask = propertyIdsHavingTask;
  }
  
  @Override
  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantsStats()
  {
    if (attributeVariantsStats == null) {
      attributeVariantsStats = new HashMap<>();
    }
    return attributeVariantsStats;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeVariantsStatsModel.class)
  public void setAttributeVariantsStats(
      Map<String, IAttributeVariantsStatsModel> attributeVariantsStats)
  {
    this.attributeVariantsStats = attributeVariantsStats;
  }
  
  @Override
  public List<IKlassInstanceReferenceInstanceModel> getReferences()
  {
    return references;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceReferenceInstanceModel.class)
  public void setReferences(List<IKlassInstanceReferenceInstanceModel> references)
  {
    this.references = references;
  }
  
  @Override
  public List<IKlassInstanceReferenceInstanceModel> getNatureReferences()
  {
    return natureReferences;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceReferenceInstanceModel.class)
  public void setNatureReferences(List<IKlassInstanceReferenceInstanceModel> natureReferences)
  {
    this.natureReferences = natureReferences;
  }
  
  @Override
  public Map<String, IKlassInstanceInformationModel> getReferencedElementInstances()
  {
    return referencedElementInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  public void setReferencedElementInstances(
      Map<String, IKlassInstanceInformationModel> referencedElementInstances)
  {
    this.referencedElementInstances = referencedElementInstances;
  }
}
