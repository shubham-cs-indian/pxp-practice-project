package com.cs.core.runtime.interactor.model.version;

import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassInstanceVersionsForComparisonModel extends IGetKlassInstanceModel {
  
  public static final String VERSIONS                                        = "versions";
  public static final String REFERENCED_KLASSES_INFO                         = "referencedKlassesInfo";
  public static final String REFERENCED_TAXONOMIES_INFO                      = "referencedTaxonomiesInfo";
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS        = "referenceRelationshipInstanceElements";
  public static final String REFERENCE_NATURE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceNatureRelationshipInstanceElements";
  public static final String CONTENT_RELATIONSHIPS                           = "contentRelationships";
  public static final String NATURE_RELATIONSHIPS                            = "natureRelationships";
  public static final String VERSION_COMPARISION_LANGUAGES                   = "versionComparisionLanguages";
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceNatureRelationshipInstanceElements();
  
  // RelationshipInstance id as a string and the list of element instances
  public void setReferenceNatureRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referencedNatureRelationshipInstances);
  
  public List<IKlassInstance> getVersions();
  
  public void setVersions(List<IKlassInstance> versions);
  
  public Map<String, IIdLabelTypeModel> getReferencedKlassesInfo();
  
  public void setReferencedKlassesInfo(Map<String, IIdLabelTypeModel> referencedKlassesInfo);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomiesInfo();
  
  public void setReferencedTaxonomiesInfo(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomiesInfo);
  
  public Map<Long, IContentRelationshipInformationModel> getContentRelationships();
  
  public void setContentRelationships(
      Map<Long, IContentRelationshipInformationModel> contentRelationships);
  
  public Map<Long, IContentRelationshipInformationModel> getNatureRelationships();
  
  public void setNatureRelationships(
      Map<Long, IContentRelationshipInformationModel> natureRelationships);
  
  public List<String> getVersionComparisionLanguages();
  
  public void setVersionComparisionLanguages(List<String> versionComparisionLanguages);
}
