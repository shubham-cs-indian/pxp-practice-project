package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKlassInstanceRelationshipTabModel extends AbstractGetKlassInstanceModel
    implements IGetKlassInstanceRelationshipTabModel {
  
  private static final long                                   serialVersionUID                             = 1L;
  
  protected Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements        = new HashMap<>();
  protected Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements = new HashMap<>();
  protected List<IKlassInstanceRelationshipInstance>          contentRelationships                         = new ArrayList<>();
  protected List<IKlassInstanceRelationshipInstance>          natureRelationships                          = new ArrayList<>();
  
  @Override
  public List<IKlassInstanceRelationshipInstance> getContentRelationships()
  {
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
    
    return referencedNatureRelationshipInstanceElements;
  }
  
  @Override
  @JsonDeserialize(contentUsing = customDeserializer.class)
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements)
  {
    this.referencedNatureRelationshipInstanceElements = referencedNatureRelationshipInstanceElements;
  }
}
