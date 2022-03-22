package com.cs.core.config.interactor.model.tag;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.model.datarule.ConfigDetailsForDataRuleModel;
import com.cs.core.config.interactor.model.datarule.DataRule;
import com.cs.core.config.interactor.model.datarule.DataRuleIntermediateEntitys;
import com.cs.core.config.interactor.model.datarule.DataRuleTags;
import com.cs.core.config.interactor.model.datarule.IConfigDetailsForDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleIntermediateEntitys;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleTags;
import com.cs.core.config.interactor.model.datarule.IRuleViolationEntity;
import com.cs.core.config.interactor.model.datarule.Normalization;
import com.cs.core.config.interactor.model.datarule.RuleViolationEntity;
import com.cs.core.config.interactor.model.rulelist.IRuleList;
import com.cs.core.config.interactor.model.rulelist.RuleList;
import com.cs.core.runtime.interactor.model.configuration.AbstractAdditionalPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataRuleModel extends AbstractAdditionalPropertiesModel implements IDataRuleModel {
  
  private static final long                serialVersionUID = 1L;
  protected IDataRule                      entity;
  protected List<String>                   klassIds;
  protected String                         userId;
  protected IConfigDetailsForDataRuleModel configDetails;
  protected Boolean                        isPhysicalCatalogsChanged;
  protected List<String>                   physicalCatalogList;
  protected List<IAuditLogModel>           auditLogInfo;
  
  public DataRuleModel()
  {
    this.entity = new DataRule();
  }
  
  public DataRuleModel(IDataRule dataRule)
  {
    this.entity = dataRule;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getRoles()
  {
    return this.entity.getRoles();
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setRoles(List<IDataRuleIntermediateEntitys> roles)
  {
    this.entity.setRoles(roles);
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getAttributes()
  {
    return this.entity.getAttributes();
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setAttributes(List<IDataRuleIntermediateEntitys> attributes)
  {
    this.entity.setAttributes(attributes);
  }
  
  @Override
  public String getLabel()
  {
    return this.entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public String getId()
  {
    return this.entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return this.entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return this.entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getRelationships()
  {
    return this.entity.getRelationships();
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setRelationships(List<IDataRuleIntermediateEntitys> relationships)
  {
    this.entity.setRelationships(relationships);
  }
  
  @Override
  public List<String> getTypes()
  {
    return this.entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.entity.setTypes(types);
  }
  
  @Override
  public List<IRuleList> getReferencedRuleList()
  {
    return this.entity.getReferencedRuleList();
  }
  
  @JsonDeserialize(contentAs = RuleList.class)
  @Override
  public void setReferencedRuleList(List<IRuleList> ruleList)
  {
    this.entity.setReferencedRuleList(ruleList);
  }
  
  @Override
  public List<IDataRuleTags> getTags()
  {
    return this.entity.getTags();
  }
  
  @JsonDeserialize(contentAs = DataRuleTags.class)
  @Override
  public void setTags(List<IDataRuleTags> tags)
  {
    this.entity.setTags(tags);
  }
  
  @Override
  public List<IRuleViolationEntity> getRuleViolations()
  {
    return this.entity.getRuleViolations();
  }
  
  @JsonDeserialize(contentAs = RuleViolationEntity.class)
  @Override
  public void setRuleViolations(List<IRuleViolationEntity> ruleViolations)
  {
    this.entity.setRuleViolations(ruleViolations);
  }
  
  @Override
  public List<? extends INormalization> getNormalizations()
  {
    return this.entity.getNormalizations();
  }
  
  @JsonDeserialize(contentAs = Normalization.class)
  @Override
  public void setNormalizations(List<? extends INormalization> normalizations)
  {
    this.entity.setNormalizations(normalizations);
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public IConfigDetailsForDataRuleModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForDataRuleModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForDataRuleModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<String> getTaxonomies()
  {
    return entity.getTaxonomies();
  }
  
  @Override
  public void setTaxonomies(List<String> taxonomies)
  {
    entity.setTaxonomies(taxonomies);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public List<String> getOrganizations()
  {
    return entity.getOrganizations();
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    entity.setOrganizations(organizations);
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return entity.getPhysicalCatalogIds();
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    entity.setPhysicalCatalogIds(physicalCatalogIds);
  }
  
  @Override
  public List<String> getEndpoints()
  {
    return entity.getEndpoints();
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    entity.setEndpoints(endpoints);
  }
  
  @Override
  public Boolean getIsLanguageDependent()
  {
    return entity.getIsLanguageDependent();
  }
  
  @Override
  public void setIsLanguageDependent(Boolean isDependent)
  {
    entity.setIsLanguageDependent(isDependent);
  }
  
  @Override
  public List<String> getLanguages()
  {
    return entity.getLanguages();
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    entity.setLanguages(languages);
  }
  
  @Override
  public Boolean getIsPhysicalCatalogsChanged()
  {
    return isPhysicalCatalogsChanged;
  }
  
  @Override
  public void setIsPhysicalCatalogsChanged(Boolean isPhysicalCatalogsChanged)
  {
    this.isPhysicalCatalogsChanged = isPhysicalCatalogsChanged;
  }
  
  @Override
  public List<String> getPhysicalCatalogList()
  {
    if (physicalCatalogList == null) {
      physicalCatalogList = new ArrayList<>();
    }
    return physicalCatalogList;
  }
  
  @Override
  public void setPhysicalCatalogList(List<String> physicalCatalogList)
  {
    this.physicalCatalogList = physicalCatalogList;
  }
  
  @Override
  @JsonDeserialize(contentAs = AuditLogModel.class)
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    return this.auditLogInfo;
  }
}
