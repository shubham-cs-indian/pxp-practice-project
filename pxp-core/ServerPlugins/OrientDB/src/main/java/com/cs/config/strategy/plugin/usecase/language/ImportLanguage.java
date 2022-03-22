package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.*;

public class ImportLanguage extends AbstractOrientPlugin {
  
  private static final String SUCCESS_DATA_RULE_LIST = "successDataRuleList";
  private static final String PARENT_CODE            = "parentCode";
  
  public ImportLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportLanguage/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> languageList = (List<Map<String, Object>>) requestMap.get("list");
    String newDefaultLanguageCode = (String) requestMap.get("defaultLanguageCode");
    List<String> languageCodesList = (List<String>) requestMap.get("languageCodesList");
    List<Map<String, Object>> successLanguageList = new ArrayList<>();
    List<Map<String, Object>> failedTLanguageList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> responseMap = new ArrayList<Map<String,Object>>();
    
    Vertex defaultLanguageVertex = LanguageUtil.getDefaultLanguageVertex(false);
    String existingDefaultLanguageCode = null;
    if (defaultLanguageVertex != null) {
      existingDefaultLanguageCode = defaultLanguageVertex.getProperty(CommonConstants.CODE_PROPERTY);
    }
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.LANGUAGE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> language : languageList) {
      try {
        Map<String, Object> languageMap = upsertLanguage(language, vertexType);
        Map<String, Object> createdDataRuleMap = new HashMap<>();
        createdDataRuleMap.put(CommonConstants.CODE_PROPERTY, language.get(CommonConstants.CODE_PROPERTY));
        createdDataRuleMap.put(CommonConstants.LABEL_PROPERTY, language.get(CommonConstants.LABEL_PROPERTY));
        successLanguageList.add(createdDataRuleMap);
        responseMap.add(languageMap);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, (String) language.get(CommonConstants.LABEL_PROPERTY));
        addToFailureIds(failedTLanguageList, language);
      }
    }

    if(newDefaultLanguageCode != null){
      Vertex language = UtilClass.getVertexByCode(newDefaultLanguageCode, VertexLabelConstants.LANGUAGE);
      language.setProperty(ILanguage.IS_DEFAULT_LANGUAGE, true);
    }
    if(defaultLanguageVertex != null && !Objects.equals(newDefaultLanguageCode, existingDefaultLanguageCode)){
      if(newDefaultLanguageCode != null){
        defaultLanguageVertex.setProperty(ILanguage.IS_DEFAULT_LANGUAGE, false);
      }
      else{
        defaultLanguageVertex.setProperty(ILanguage.IS_DEFAULT_LANGUAGE, true);
      }
    }

    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, successLanguageList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTLanguageList);
    result.put(SUCCESS_DATA_RULE_LIST, responseMap);
    
    return result;
  }
  
  private Map<String, Object> upsertLanguage(Map<String, Object> language, OrientVertexType vertexType) throws Exception
  {
    String code = (String) language.get(CommonConstants.CODE_PROPERTY);
    Vertex languageNode;
    try {
      languageNode = UtilClass.getVertexByCode(code, VertexLabelConstants.LANGUAGE);
      String label = (String) language.get(CommonConstants.LABEL_PROPERTY);
      languageNode.setProperty(CommonConstants.LABEL_PROPERTY, label);
      language.remove(PARENT_CODE);
      language.put(ILanguage.IS_DEFAULT_LANGUAGE, false);
      UtilClass.saveNode(language, languageNode, new ArrayList<>());
    } catch (NotFoundException e) {
      String parentCode = (String) language.get(PARENT_CODE);
      language.remove(PARENT_CODE);
      languageNode = UtilClass.createNode(language, vertexType, new ArrayList<>());
      
	  Vertex parent = null;
      if (!parentCode.isEmpty() && !parentCode.equals(Constants.STANDARD_ORGANIZATION)) {
        parent = UtilClass.getVertexByCode(parentCode, VertexLabelConstants.LANGUAGE);
        languageNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parent);
      }
    }
    Map<String, Object> returnMap = UtilClass.getMapFromNode(languageNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, languageNode, Entities.LANGUAGE_TREE, Elements.UNDEFINED);
    
    return returnMap;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedLanguageList, Map<String, Object> language)
  {
    Map<String, Object> failedTaxonomyMap = new HashMap<>();
    failedTaxonomyMap.put(ISummaryInformationModel.LABEL, language.get(IKlassModel.LABEL));
    failedLanguageList.add(failedTaxonomyMap);
  }
}
