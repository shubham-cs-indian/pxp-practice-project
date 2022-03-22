package com.cs.core.bgprocess.services.datarules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;

@SuppressWarnings("unchecked")
public class DataRuleUtil {
  
  public static final String GET_CONFIG_DETAILS_FOR_BULK_PROPAGATION = "GetConfigDetailsForBulkPropagation";
  
  public static void propagateRule(IBaseEntityDAO baseEntityDAO, IConfigDetailsForBulkPropagationResponseModel configDetails)
  {
    try {
      RuleHandler ruleHandler = new RuleHandler();
      ruleHandler.initiateRuleHandling(baseEntityDAO, baseEntityDAO.getLocaleCatalog(), false, configDetails.getReferencedElements(),
          configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  public static IConfigDetailsForBulkPropagationResponseModel getConfigDetailsForBulkPropagtion(Map<String, Object> requestModel, String baseLocaleID)
  {
    try {
      Map<String, Object> response = CSConfigServer.instance().request(requestModel, GET_CONFIG_DETAILS_FOR_BULK_PROPAGATION,
          baseLocaleID);
      IConfigDetailsForBulkPropagationResponseModel configDetails = ObjectMapperUtil
          .readValue(ObjectMapperUtil.writeValueAsString(response), ConfigDetailsForBulkPropagationResponseModel.class);
      return configDetails;
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return null;
  }
  
  public static Map<String, Object> prepareConfigBulkPropagtionRequestModel(IBaseEntityDTO currentEntity, String userID)
  {
    List<String> nonNatureClassifierCodes = new ArrayList<>();
    nonNatureClassifierCodes.add( currentEntity.getNatureClassifier().getCode());
    List<String> selectedTaxonomyCodes = new ArrayList<>();
    currentEntity.getOtherClassifiers().forEach(classifier -> {
        ClassifierType classifierType = classifier.getClassifierType();
        if (classifierType.equals(ClassifierType.CLASS)) {
          nonNatureClassifierCodes.add(classifier.getCode());
        } else {
          selectedTaxonomyCodes.add(classifier.getCode());
        }
      });
  
    Map<String, Object> requestModel = new HashMap<>();
    requestModel.put("klassIds", nonNatureClassifierCodes);
    requestModel.put("selectedTaxonomyIds", selectedTaxonomyCodes);
    requestModel.put("addedKlassIds", new ArrayList<>());
    requestModel.put("addedTaxonomyIds", new ArrayList<>());
    requestModel.put("collectionIds", new ArrayList<>());
    requestModel.put("organizationId", "-1");
    requestModel.put("physicalCatalogId", "pim");
    requestModel.put("parentKlassIds", new ArrayList<>());
    requestModel.put("parentTaxonomyIds", new ArrayList<>());
    requestModel.put("languageCodes", new ArrayList<>());
    requestModel.put("userId", userID);
    return requestModel;
  }
}
