package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetSourceInfoForBucketResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRuleIdLangaugeCodesModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForGetSourceInfoForBucket extends AbstractConfigDetails {
  
  public GetConfigDetailsForGetSourceInfoForBucket(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetSourceInfoForBucket/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> languageCodes = (List<String>) requestMap
        .get(IRuleIdLangaugeCodesModel.LANGUAGE_CODES);
    List<String> organizationIds = (List<String>) requestMap
        .get(IRuleIdLangaugeCodesModel.ORGANIZATION_IDS);
    String ruleId = (String) requestMap.get(IRuleIdLangaugeCodesModel.RULE_ID);
    
    if (languageCodes.isEmpty()) {
      return new HashMap<>();
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IConfigDetailsForGetSourceInfoForBucketResponseModel.REFERENCED_LANGUAGES,
        new LinkedHashMap<>());
    fillReferencedLanguages(returnMap, languageCodes);
    returnMap.put(IConfigDetailsForGetSourceInfoForBucketResponseModel.REFERENCED_ORGANIZATIONS,
        getReferencedOrganizations(organizationIds));
    
    if (ruleId != null) {
      Vertex vertexByIndexedId = UtilClass.getVertexByIndexedId(ruleId,
          VertexLabelConstants.GOLDEN_RECORD_RULE);
      returnMap.put(IConfigDetailsForGetSourceInfoForBucketResponseModel.GOLDEN_RECORD_RULE,
          UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
              IGoldenRecordRule.LABEL, IGoldenRecordRule.CODE), vertexByIndexedId));
    }
    return returnMap;
  }
  
  private Map<String, Object> getReferencedOrganizations(List<String> organizationIds)
      throws Exception
  {
    Iterable<Vertex> organizations = UtilClass.getVerticesByIndexedIds(organizationIds,
        VertexLabelConstants.ORGANIZATION);
    Map<String, Object> referencedOrganizations = new HashMap<>();
    organizations.forEach(organizationVertex -> {
      Map<String, Object> organizationInfo = new HashMap<>();
      organizationInfo.put(IGetLanguagesInfoModel.ID, UtilClass.getCodeNew(organizationVertex));
      organizationInfo.put(IGetLanguagesInfoModel.LABEL,
          UtilClass.getValueByLanguage(organizationVertex, ILanguage.LABEL));
      organizationInfo.put(IGetLanguagesInfoModel.CODE,
          organizationVertex.getProperty(IOrganization.CODE));
      referencedOrganizations.put(UtilClass.getCodeNew(organizationVertex), organizationInfo);
    });
    
    return referencedOrganizations;
  }
}
