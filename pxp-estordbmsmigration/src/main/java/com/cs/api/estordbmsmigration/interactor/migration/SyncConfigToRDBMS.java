package com.cs.api.estordbmsmigration.interactor.migration;

import com.cs.api.estordbmsmigration.model.migration.*;
import com.cs.api.estordbmsmigration.strategy.migration.ISyncCongifToRDBMSStrategy;
import com.cs.api.estordbmsmigration.strategy.migration.ISyncIIDToODBStrategy;
import com.cs.api.estordbmsmigration.strategy.migration.ISyncKPIStrategy;
import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.interactor.entity.governancerule.*;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.usecase.assembler.KpiAssembler;
import com.cs.core.config.interactor.exception.validationontype.InvalidDataRuleTypeException;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.usecase.assembler.RuleAssembler;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SyncConfigToRDBMS extends AbstractService<IVoidModel, IVoidModel> implements ISyncConfigToRDBMS {
  
  private static Map<String, Long> IdIidMap = new HashMap<>();
  
  static {
    
    // ----ATTRIBUTE---------------
    IdIidMap.put("nameattribute", 200l);
    IdIidMap.put("createdbyattribute", 201l);
    IdIidMap.put("createdonattribute", 202l);
    IdIidMap.put("lastmodifiedbyattribute", 203l);
    IdIidMap.put("lastmodifiedattribute", 204l);
    IdIidMap.put("assetcoverflowattribute", 205l);
    IdIidMap.put("addressattribute", 206l);
    IdIidMap.put("descriptionattribute", 207l);
    IdIidMap.put("longdescriptionattribute", 208l);
    IdIidMap.put("shortdescriptionattribute", 209l);
    IdIidMap.put("duedateattribute", 210l);
    IdIidMap.put("discountattribute", 211l);
    IdIidMap.put("gtin_attribute", 212l);
    IdIidMap.put("listpriceattribute", 213l);
    IdIidMap.put("maximumpriceattribute", 214l);
    IdIidMap.put("minimumpriceattribute", 215l);
    IdIidMap.put("sellingpriceattribute", 216l);
    IdIidMap.put("firstnameattribute", 217l);
    IdIidMap.put("lastnameattribute", 218l);
    IdIidMap.put("pid_attribute", 219l);
    IdIidMap.put("pincodeattribute", 220l);
    IdIidMap.put("sku_attribute", 221l);
    IdIidMap.put("telephonenumberattribute", 222l);
    IdIidMap.put("I_META_FILE_NAME", 223l);
    IdIidMap.put("I_META_DOCUMENT_TYPE", 224l);
    IdIidMap.put("I_APPLICATION", 225l);
    IdIidMap.put("I_CREATE_DATE", 226l);
    IdIidMap.put("I_MODIFICATION_DATE", 227l);
    IdIidMap.put("I_META_FILE_SIZE", 228l);
    IdIidMap.put("I_DIMENSIONS", 229l);
    IdIidMap.put("I_DIMENSION_IN_INCH", 230l);
    IdIidMap.put("I_RESOLUTION", 231l);
    IdIidMap.put("I_BIT_DEPTH", 232l);
    IdIidMap.put("I_COLOR_MODE", 233l);
    IdIidMap.put("I_COLOR_PROFILE", 234l);
    IdIidMap.put("I_META_CREATOR_AUTHOR", 235l);
    IdIidMap.put("I_META_CREATOR_STREET", 236l);
    IdIidMap.put("I_META_CREATOR_LOCATION", 237l);
    IdIidMap.put("I_META_CREATOR_STATE", 238l);
    IdIidMap.put("I_META_CREATOR_POSTAL_CODE", 239l);
    IdIidMap.put("I_META_CREATOR_COUNTRY", 240l);
    IdIidMap.put("I_META_CREATOR_TEL", 241l);
    IdIidMap.put("I_META_CREATOR_EMAIL", 242l);
    IdIidMap.put("I_META_CREATOR_WWW", 243l);
    IdIidMap.put("I_META_HEADING", 244l);
    IdIidMap.put("I_META_DESCRIPTION", 245l);
    IdIidMap.put("I_META_KEYWORDS", 246l);
    IdIidMap.put("I_META_LOCATION_DETAIL", 247l);
    IdIidMap.put("I_META_LOCATION", 248l);
    IdIidMap.put("I_META_STATE", 249l);
    IdIidMap.put("I_META_COUNTRY", 250l);
    IdIidMap.put("I_META_TITEL", 251l);
    IdIidMap.put("I_META_COPYRIGHT", 252l);
    IdIidMap.put("I_META_COPYRIGHT_STATUS", 253l);
    IdIidMap.put("I_META_EXPOSURE_INDEX", 254l);
    IdIidMap.put("I_META_FOCAL_RANGE", 255l);
    IdIidMap.put("I_META_MAX_BLINDING_VALUE", 256l);
    IdIidMap.put("I_META_RECORDING_DATE", 257l);
    IdIidMap.put("I_META_EXPOSURE_CONTROLS", 258l);
    IdIidMap.put("I_META_LIGHT_SOURCE", 259l);
    IdIidMap.put("I_META_SENSOR_TYPE", 260l);
    IdIidMap.put("I_META_BRAND", 261l);
    IdIidMap.put("I_META_MODELL", 262l);
    IdIidMap.put("I_META_SERIAL_NUMBER", 263l);
    IdIidMap.put("I_META_PIXEL_X_DIMENSION", 264l);
    IdIidMap.put("I_META_PIXEL_Y_DIMENSION", 265l);
    IdIidMap.put("I_META_HIGH_RESOLUTION", 266l);
    IdIidMap.put("I_META_WIDTH_RESOLUTION", 267l);
    IdIidMap.put("I_META_X_RESOLUTION", 268l);
    IdIidMap.put("I_META_Y_RESOLUTION", 269l);
    IdIidMap.put("schedulestartattribute", 270l);
    IdIidMap.put("scheduleendattribute", 271l);
    IdIidMap.put("assetdownloadcountattribute", 272l);
    
    // ---------TAGS---------------------------------------
    
    IdIidMap.put("statustag", 301l);
    IdIidMap.put("availabilitytag", 302l);
    IdIidMap.put("languagetag", 303l);
    IdIidMap.put("lifestatustag", 304l);
    
    IdIidMap.put("listingstatustag", 305l);
    IdIidMap.put("T_STATUS_SYSTEM", 306l);
    IdIidMap.put("resolutiontag", 307l);
    IdIidMap.put("imageextensiontag", 308l);
    
    IdIidMap.put("T_STATUS_PROJECT", 309l);
    IdIidMap.put("taskstatustag", 310l);
    IdIidMap.put("isordertag", 311l);
    IdIidMap.put("issalestag", 312l);
    IdIidMap.put("isbaseunittag", 313l);
    
    // --------------RELATIONS------------------------------
    
    IdIidMap.put("standardArticleAssetRelationship", 400l);
    IdIidMap.put("standardMarketAssetRelationship", 401l);
    IdIidMap.put("standardSupplierAssetRelationship", 402l);
    
    IdIidMap.put("standardTextAsset-AssetRelationship", 403l);
    IdIidMap.put("standardArticleMarketRelationship", 404l);
    IdIidMap.put("standardVirtualCatalogAssetRelationship", 405l);
    IdIidMap.put("standardArticleGoldenArticleRelationship", 406l);
    
    // ---------------USERS--------------------------------------
    
    IdIidMap.put("admin", 10l);
    IdIidMap.put("backgrounduserstandardorganization", 11l);
    
    // ------------KLASS-------------------------------
    
    IdIidMap.put("article", 20l);
    IdIidMap.put("product_types", 21l);
    IdIidMap.put("attribute_classes", 22l);
    IdIidMap.put("single_article", 23l);
    IdIidMap.put("fileklass", 24l);
    IdIidMap.put("golden_article_klass", 25l);
    IdIidMap.put("attachment_asset", 27l);
    IdIidMap.put("asset_asset", 28l);
    IdIidMap.put("image_asset", 29l);
    IdIidMap.put("video_asset", 30l);
    IdIidMap.put("document_asset", 31l);
    IdIidMap.put("smartdocument_asset", 32l);
    IdIidMap.put("market", 33l);
    IdIidMap.put("supplier", 34l);
    IdIidMap.put("suppliers", 35l);
    IdIidMap.put("marketplaces", 36l);
    IdIidMap.put("distributors", 37l);
    IdIidMap.put("wholesalers", 38l);
    IdIidMap.put("translation_agency", 39l);
    IdIidMap.put("content_enrichment_agency", 40l);
    IdIidMap.put("digital_asset_agency", 41l);
    IdIidMap.put("text_asset", 42l);
    IdIidMap.put("virtual_catalog", 43l);
    
  }
  
  @Autowired
  protected ISyncCongifToRDBMSStrategy syncCongifToRDBMSStrategy;
  
  @Autowired
  protected ISyncIIDToODBStrategy      syncIIDToODBStrategy;

  @Autowired
  protected ISyncKPIStrategy syncKPIStrategy;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    
    int size = 500;
    int from = 0;


    // sync for postres and oDB
    syncKLasses(configurationDAO, size, from);
    syncTaxonomies(configurationDAO, size, from);
    syncAttributes(configurationDAO, size, from);
    syncTags(configurationDAO, size, from);
    syncRelationships(configurationDAO, size, from, PropertyType.RELATIONSHIP, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    syncRelationships(configurationDAO, size, from, PropertyType.NATURE_RELATIONSHIP, VertexLabelConstants.NATURE_RELATIONSHIP);
    syncUsers(configurationDAO, size, from);

    // sync for postres only
    syncLanguages(configurationDAO, size, from);
    syncContexts(configurationDAO, size, from);
    syncTasks(configurationDAO, size, from);
    syncKPI(configurationDAO, size, from);
    syncRule(configurationDAO, size, from);
    return new VoidModel();
  }

  private void syncKPI(IConfigurationDAO configurationDAO, int size, int from) throws Exception
  {

    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);

    ISyncKPIResponseModel response = syncKPIStrategy.execute(requestModel); /*syncCongifToRDBMSStrategy.execute(requestModel);*/

    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (IGetKeyPerformanceIndexModel kpiModel : response.getList()) {
      Map<String, IConfigEntityInformationModel> referencedKlasses = kpiModel.getReferencedKlasses();

      IKeyPerformanceIndicator kpi = kpiModel.getKeyPerformanceIndex();
      Map<String, IGovernanceRuleBlock> referencedRules = kpiModel.getReferencedRules();
      for (IGovernanceRuleBlock referencedRule : referencedRules.values()) {
        List<IGovernanceRule> rules = referencedRule.getRules();
        for (IGovernanceRule governanceRule : rules) {
          StringBuilder evaluation = new StringBuilder();
          if (governanceRule.getType().equals("completeness")) {

            List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
            List<IGovernanceRuleTags> tags = governanceRule.getTags();
            evaluation.append(KpiAssembler.instance().assembleEvaluationForCompletenessAndUniqueness(attributes, tags));

          }
          else if (governanceRule.getType().equals("uniqueness")) {

            List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
            List<IGovernanceRuleTags> tags = governanceRule.getTags();
            evaluation.append(KpiAssembler.instance().assembleEvaluationForUniqueness(attributes, tags));

          }
          else {
            List<IGovernanceRuleIntermediateEntity> attributes = governanceRule.getAttributes();
            List<IGovernanceRuleTags> tags = governanceRule.getTags();
            evaluation.append(KpiAssembler.instance().assembleEvaluation(attributes, tags));
          }

          StringBuilder scope = new StringBuilder();
          if (!kpi.getTargetFilters().getKlassIds().isEmpty() || !kpi.getTargetFilters().getTaxonomyIds().isEmpty()) {
            String natureClass = referencedKlasses.values().stream().map(IConfigEntityInformationModel::getId).findFirst().orElse("");
            scope.append(RuleAssembler.instance().scopeForCatalog(kpi.getPhysicalCatalogIds()));
            scope.append(RuleAssembler.instance().scopeForOrganizations(kpi.getOrganizations()));
            scope.append(
                RuleAssembler.instance().scopeForClassifiers(natureClass, governanceRule.getKlassIds(), governanceRule.getTaxonomyIds()));

          }
          if(StringUtils.isEmpty(scope.toString()) || StringUtils.isEmpty(evaluation.toString())) {
            continue;
          }

          String ruleExpression = RuleAssembler.instance().assembleRule(scope.toString(), evaluation.toString(), "");
          String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.KPI.getPrefix());
          String ruleExpressionIID = id.contains(IStandardConfig.UniquePrefix.KPI.getPrefix()) ? id.replace(IStandardConfig.UniquePrefix.KPI.getPrefix(), "") : id;
          configurationDAO.upsertRule(referencedRule.getCode(), Long.parseLong(ruleExpressionIID), ruleExpression, ICSERule.RuleType.kpi);

          IIIDCodeModel iidCodeModel = new IIDCodeModel();
          iidCodeModel.setIID(Long.parseLong(ruleExpressionIID));
          iidCodeModel.setCode(governanceRule.getCode());
          modelList.add(iidCodeModel);
        }
      }
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.GOVERNANCE_RULES);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncKPI(configurationDAO, 500, from + 500);
    }
  }

  private void syncRule(IConfigurationDAO configurationDAO, int size, int from) throws Exception
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.DATA_RULE);
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    for (IDataRuleModel model : response.getDataRuleResponseList()) {
      String ruleExpression = assembleRule(model);
      Long ruleExpressionIID = Long.parseLong(RDBMSUtils.newUniqueID("EXPR").split("EXPR")[1]);
      configurationDAO.upsertRule(model.getCode(), ruleExpressionIID, ruleExpression, RuleType.dataquality);
    }
    if (response.getCount() > (from + size)) {
      syncRule(configurationDAO, 500, from + 500);
    }
  }

  private String assembleRule(IDataRuleModel responseDataRuleModel) throws InvalidDataRuleTypeException
  {
    String actions = RuleAssembler.instance().assembleActions(responseDataRuleModel);
    if(actions.isEmpty()) {
      return "";
    }
    String scope = assembleScope(responseDataRuleModel);
    String evaluation = RuleAssembler.instance().assembleEvaluation(responseDataRuleModel.getAttributes(), responseDataRuleModel.getTags());
    return RuleAssembler.instance().assembleRule(scope, evaluation, actions);
  }

  private String assembleScope(IDataRuleModel responseDataRuleModel) {
    String natureClass = getNatureClass(responseDataRuleModel.getConfigDetails().getReferencedKlasses());
    List<String> otherClassifiers = new ArrayList<>(responseDataRuleModel.getTypes());
    otherClassifiers.remove(natureClass);
    List<String> organizations = new ArrayList<>(responseDataRuleModel.getOrganizations());

    String scopeForClassifier = RuleAssembler.instance().scopeForClassifiers(natureClass, otherClassifiers, responseDataRuleModel.getTaxonomies());
    String scopeForLocale = RuleAssembler.instance().scopeForLocale(responseDataRuleModel.getLanguages());
    String scopeForCatalog = RuleAssembler.instance().scopeForCatalog(responseDataRuleModel.getPhysicalCatalogIds());
    String scopeForOrganization = RuleAssembler.instance().scopeForOrganizations(organizations);

    String scope = String.format("%s %s %s %s", scopeForCatalog, scopeForOrganization, scopeForLocale, scopeForClassifier);
    return scope;
  }

  private String getNatureClass(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
  return referencedKlasses.values().stream().filter(x -> x.getIsNature()).map(x -> x.getId())
        .findFirst().orElse("");
  }
  
  private void syncKLasses(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      Long iid = IdIidMap.get(model.getCode());
      IClassifierDTO.ClassifierType classifierType = IConfigMap.getClassType(model.getType());
      if (classifierType == null) {
        RDBMSLogger.instance().warn(" Classifier type not found for classifier code " + model.getCode());
        continue;
      }
      if (!(classifierType == IClassifierDTO.ClassifierType.CLASS)) {
        continue;
      }
      IClassifierDTO classifierDTO = null;
      if (iid == null) {
        classifierDTO = configurationDAO.createClassifier(model.getCode(), classifierType);
      }
      else {
        classifierDTO = configurationDAO.createStandardClassifier(iid, model.getCode(), classifierType);
      }
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(classifierDTO.getClassifierIID());
      iidCodeModel.setCode(model.getCode());
      modelList.add(iidCodeModel);
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncKLasses(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncTaxonomies(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      
      List<Long> parentIIDs = new ArrayList<>();
      parentIIDs.add(-1l);
      IClassifierDTO.ClassifierType classifierType = IConfigMap.getClassType(model.getType());
      IClassifierDTO classifierDTO = configurationDAO.createTaxonomyClassifier(model.getCode(), classifierType, parentIIDs);
      
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(classifierDTO.getClassifierIID());
      iidCodeModel.setCode(model.getCode());
      modelList.add(iidCodeModel);
      List<ISyncCongifToRDBMSModel> childrens = model.getChildrens();
      parentIIDs.remove(-1l);
      parentIIDs.add(classifierDTO.getClassifierIID());
      syncTaxonomyChildren(configurationDAO, modelList, childrens, parentIIDs);
      
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncTaxonomies(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncTaxonomyChildren(IConfigurationDAO configurationDAO, List<IIIDCodeModel> modelList,
      List<ISyncCongifToRDBMSModel> childrens, List<Long> parentIIDs) throws RDBMSException
  {
    for (ISyncCongifToRDBMSModel children : childrens) {
      IClassifierDTO.ClassifierType classifierType = IConfigMap.getClassType(children.getType());
      if(classifierType == null) {
        System.out.println("================ classifier type is null for taxonomy "+ children.getCode() +"  type" + children.getType());
      }
      IClassifierDTO classifierDTO = configurationDAO.createTaxonomyClassifier(children.getCode(), classifierType, parentIIDs);
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(classifierDTO.getClassifierIID());
      iidCodeModel.setCode(children.getCode());
      modelList.add(iidCodeModel);
      
      List<Long> parent = new ArrayList<Long>();
      parent.addAll(parentIIDs);
      parent.add(classifierDTO.getClassifierIID());
      
      syncTaxonomyChildren(configurationDAO, modelList, children.getChildrens(), parent);
      
    }
    
  }
  
  private void syncAttributes(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      Long iid = IdIidMap.get(model.getCode());
      String type = model.getType();
     
      IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(type);
      if (propertyType == null) {
        RDBMSLogger.instance().warn(" ---Propertytype is not found for Attribute code " + model.getCode() + " and attributeType is " + model.getType());
        continue;
      }
      IPropertyDTO proeprtyDTO = null;
      if (iid == null) {
        proeprtyDTO = configurationDAO.createProperty(model.getCode(), propertyType);
      }
      else {
        proeprtyDTO = configurationDAO.createStandardProperty(iid, model.getCode(), propertyType);
      }
      
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(proeprtyDTO.getPropertyIID());
      iidCodeModel.setCode(model.getCode());
      modelList.add(iidCodeModel);
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncAttributes(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncTags(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TAG);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      Long iid = IdIidMap.get(model.getCode());
      
      String type = model.getType();
      if (type != null) {
        IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(type);
        if (propertyType == null) {
          RDBMSLogger.instance()
              .warn(" ---Propertytype is not found for tag code " + model.getCode() + " and tagType is " + model.getType());
          continue;
        }
        IPropertyDTO proeprtyDTO = null;
        if (iid == null) {
          proeprtyDTO = configurationDAO.createProperty(model.getCode(), propertyType);
        }
        else {
          proeprtyDTO = configurationDAO.createStandardProperty(iid, model.getCode(), propertyType);
        }
        IIIDCodeModel iidCodeModel = new IIDCodeModel();
        iidCodeModel.setIID(proeprtyDTO.getPropertyIID());
        iidCodeModel.setCode(model.getCode());
        modelList.add(iidCodeModel);
        
        for (ISyncCongifToRDBMSModel children : model.getChildrens()) {
          configurationDAO.createTagValue(children.getCode(), proeprtyDTO.getPropertyIID());
        }
      }
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.ENTITY_TAG);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncTags(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncRelationships(IConfigurationDAO configurationDAO, int size, int from, PropertyType propertyType, String vertex)
      throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(vertex);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      Long iid = IdIidMap.get(model.getCode());
      IPropertyDTO proeprtyDTO = null;
      if (iid == null) {
        proeprtyDTO = configurationDAO.createProperty(model.getCode(), propertyType);
      }
      else {
        proeprtyDTO = configurationDAO.createStandardProperty(iid, model.getCode(), propertyType);
      }
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(proeprtyDTO.getPropertyIID());
      iidCodeModel.setCode(model.getCode());
      modelList.add(iidCodeModel);
      
      for (ISyncCongifToRDBMSModel children : model.getChildrens()) {
        configurationDAO.createTagValue(children.getCode(), proeprtyDTO.getPropertyIID());
      }
      
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(vertex);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncRelationships(configurationDAO, size + 500, size, propertyType, vertex);
    }
  }
  
  private void syncUsers(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_USER);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<IIIDCodeModel> modelList = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      Long iid = IdIidMap.get(model.getCode());
      IUserDTO userDTO = null;
      if (iid == null) {
        userDTO = configurationDAO.createUser(model.getUserName());
      }
      else {
        userDTO = configurationDAO.createStandardUser(iid, model.getUserName());
      }
      IIIDCodeModel iidCodeModel = new IIDCodeModel();
      iidCodeModel.setIID(userDTO.getUserIID());
      iidCodeModel.setCode(model.getCode());
      modelList.add(iidCodeModel);
      
    }
    if (modelList.size() > 0) {
      ISyncIIDToODBModel syncIIDToODBModel = new SyncIIDToODBModel();
      syncIIDToODBModel.setList(modelList);
      syncIIDToODBModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_USER);
      syncIIDToODBStrategy.execute(syncIIDToODBModel);
    }
    if (response.getCount() > (from + size)) {
      syncUsers(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncLanguages(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.LANGUAGE);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      
      configurationDAO.createLanguageConfig(model.getCode(), "-1");
      syncLanChildren(configurationDAO, model.getChildrens(), model.getCode());
    }
    
    if (response.getCount() > (from + size)) {
      syncLanguages(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncLanChildren(IConfigurationDAO configurationDAO, List<ISyncCongifToRDBMSModel> childrens, String parentCode)
      throws RDBMSException
  {
    for (ISyncCongifToRDBMSModel children : childrens) {
      configurationDAO.createLanguageConfig(children.getCode(), parentCode);
      syncLanChildren(configurationDAO, children.getChildrens(), children.getCode());
    }
  }
  
  private void syncContexts(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.VARIANT_CONTEXT);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      
      ContextType contextType = IConfigMap.getContextType(model.getType());
      if (contextType != null)
        configurationDAO.createContext(model.getCode(), contextType);
    }
    
    if (response.getCount() > (from + size)) {
      syncContexts(configurationDAO, 500, from + 500);
    }
  }
  
  private void syncTasks(IConfigurationDAO configurationDAO, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_TASK);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      
      TaskType taskType = IConfigMap.getTaskType(model.getType());
      configurationDAO.createTask(model.getCode(), taskType);
    }
    
    if (response.getCount() > (from + size)) {
      syncTasks(configurationDAO, 500, from + 500);
    }
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
