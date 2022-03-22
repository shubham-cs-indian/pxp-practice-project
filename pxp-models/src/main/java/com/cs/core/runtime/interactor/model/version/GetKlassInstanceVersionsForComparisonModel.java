package com.cs.core.runtime.interactor.model.version;

import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.AbstractGetKlassInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKlassInstanceVersionsForComparisonModel extends AbstractGetKlassInstanceModel
    implements IGetKlassInstanceVersionsForComparisonModel {
  
  private static final long                                   serialVersionUID                             = 1L;
  protected List<IKlassInstance>                              versions;
  protected Map<String, IIdLabelTypeModel>                    referencedKlassesInfo;
  protected Map<String, IReferencedArticleTaxonomyModel>      referencedTaxonomiesInfo;
  protected Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements        = new HashMap<>();
  protected Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstanceElements = new HashMap<>();
  
  // Key is versionId
  protected Map<Long, IContentRelationshipInformationModel>   contentRelationships                         = new HashMap<>();
  protected Map<Long, IContentRelationshipInformationModel>   natureRelationships                          = new HashMap<>();
  
  protected List<String>                                      versionComparisionLanguages                  = new ArrayList<>();
  
  @Override
  public List<IKlassInstance> getVersions()
  {
    if (versions == null)
      versions = new ArrayList<IKlassInstance>();
    return versions;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractKlassInstance.class)
  public void setVersions(List<IKlassInstance> versions)
  {
    this.versions = versions;
  }
  
  @Override
  public Map<String, IIdLabelTypeModel> getReferencedKlassesInfo()
  {
    return referencedKlassesInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setReferencedKlassesInfo(Map<String, IIdLabelTypeModel> referencedKlassesInfo)
  {
    this.referencedKlassesInfo = referencedKlassesInfo;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomiesInfo()
  {
    return referencedTaxonomiesInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setReferencedTaxonomiesInfo(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomiesInfo)
  {
    this.referencedTaxonomiesInfo = referencedTaxonomiesInfo;
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
  
  @Override
  public Map<Long, IContentRelationshipInformationModel> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentRelationshipsInformationModel.class)
  public void setContentRelationships(
      Map<Long, IContentRelationshipInformationModel> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public Map<Long, IContentRelationshipInformationModel> getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentRelationshipsInformationModel.class)
  public void setNatureRelationships(
      Map<Long, IContentRelationshipInformationModel> natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public List<String> getVersionComparisionLanguages()
  {
    return versionComparisionLanguages;
  }
  
  @Override
  public void setVersionComparisionLanguages(List<String> versionComparisionLanguages)
  {
    this.versionComparisionLanguages = versionComparisionLanguages;
  }
}
