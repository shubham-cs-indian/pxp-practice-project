package com.cs.core.config.interactor.model.goldenrecord;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;

public interface IGetGoldenRecordRuleModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String GOLDEN_RECORD_RULE                         = "goldenRecordRule";
  public static final String REFERENCED_ATTRIBUTES                      = "referencedAttributes";
  public static final String REFERENCED_TAGS                            = "referencedTags";
  public static final String REFERENCED_KLASSES                         = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES                      = "referencedTaxonomies";
  public static final String REFERENCED_ORGANIZATIONS                   = "referencedOrganizations";
  public static final String REFERENCED_ENDPOINTS                       = "referencedEndpoints";
  public static final String REFERENCED_RELATIONSHIPS                   = "referencedRelationships";
  public static final String REFERENCED_NATURE_RELATIONSHIPS            = "referencedNatureRelationships";
  public static final String PHYSICAL_CATALOG_IDS                       = "physicalCatalogIds";
  public static final String NEED_TO_EVALUATE_GOLDEN_RECORD_RULE_BUCKET = "needToEvaluateGoldenRecordRuleBucket";
  
  public IGoldenRecordRule getGoldenRecordRule();
  
  public void setGoldenRecordRule(IGoldenRecordRule goldenRecordRule);
  
  public Map<String, IIdLabelCodeModel> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IIdLabelCodeModel> referencedAttributes);
  
  public Map<String, IIdLabelCodeModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IIdLabelCodeModel> referencedTags);
  
  public Map<String, IIdLabelNatureTypeModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations();
  
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations);
  
  public Map<String, IIdLabelCodeModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(Map<String, IIdLabelCodeModel> referencedEndpoints);
  
  public Map<String, IIdLabelCodeModel> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IIdLabelCodeModel> referencedRelationships);
  
  public Map<String, IIdLabelCodeModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelCodeModel> referencedNatureRelationships);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public Boolean getNeedToEvaluateGoldenRecordRuleBucket();
  
  public void setNeedToEvaulateGoldenRecordRuleBucket(Boolean needToEvaluateGoldenRecordRuleBucket);
}
