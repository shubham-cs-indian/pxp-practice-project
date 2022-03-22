package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.SaveDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.usecase.datarule.ICreateDataRule;
import com.cs.core.config.interactor.usecase.datarule.IGetDataRule;
import com.cs.core.config.interactor.usecase.datarule.ISaveDataRule;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@Service("datarulesAPIStrategy")
public class DataRulesAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  protected ICreateDataRule         createDataRule;
  
  @Autowired
  protected IGetDataRule            getDataRule;
  
  @Autowired
  protected ISaveDataRule           saveDataRule;
  
  private static final ObjectMapper mapper                   = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  public static final String        ADDED_TYPES              = "addedTypes";
  public static final String        ADDED_TAXONOMIES         = "addedTaxonomies";
  public static final String        MODIFIED_ATTRIBUTE_RULES = "modifiedAttributeRules";
  public static final String        DELETED_ATTRIBUTE_RULES  = "deletedAttributeRules";
  public static final String        MODIFIED_TAG_RULES       = "modifiedTagRules";
  public static final String        DELETED_TAG_RULES        = "deletedTagRules";
  public static final String        ADDED_NORMALIZATIONS     = "addedNormalizations";
  public static final String        MODIFIED_NORMALIZATIONS  = "modifiedNormalizations";
  public static final String        DELETED_NORMALIZATIONS   = "deletedNormalizations";
  public static final String        TYPE                     = "type";
  public static final String        TAXONOMY                 = "taxonomy";
  public static final String        ATTRIBUTE                = "attribute";
  public static final String        TAG                      = "tag";
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel model = new IdParameterModel(code);
    return getDataRule.execute(model);
    
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    IDataRuleModel createusermodel = mapper.convertValue(inputData, DataRuleModel.class);
    return createDataRule.execute(createusermodel);
    
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    removeDuplicateOrganisation(inputData, getData);
    removeDuplicateClasses(inputData, getData);
    removeDuplicateTaxonomies(inputData, getData);
    ISaveDataRuleModel datarules = mapper.convertValue(inputData, SaveDataRuleModel.class);
    datarules.setId((String) getData.get("id"));
    return saveDataRule.execute(datarules);
    
  }
  
  /**
   * To remove duplicate taxonomies if passed as input
   * 
   * @param inputData
   * @param getData
   */
  private void removeDuplicateTaxonomies(Map<String, Object> inputData, Map<String, Object> getData)
  {
    
    List<String> taxonomies = (List<String>) getData.get(IDataRule.TAXONOMIES);
    List<String> inputtaxonomies = (List<String>) inputData.get(ADDED_TAXONOMIES);
    inputtaxonomies.removeAll(new HashSet<>(taxonomies));
    inputData.put(ADDED_TAXONOMIES, inputtaxonomies);
  }
  
  /**
   * To remove duplicate organization if passed as input
   * 
   * @param inputData
   * @param getData
   */
  private void removeDuplicateOrganisation(Map<String, Object> inputData, Map<String, Object> getData)
  {
    List<String> organisation = (List<String>) getData.get(IDataRule.ORGANIZATIONS);
    List<String> organisationofinput = (List<String>) inputData.get(ISaveDataRuleModel.ADDED_ORGANIZATION_IDS);
    List<String> duplwithoutorganisationofinput = new ArrayList<>(new HashSet<>(organisationofinput));
    duplwithoutorganisationofinput.removeAll(new HashSet<>(organisation));
    inputData.put(ISaveDataRuleModel.ADDED_ORGANIZATION_IDS, duplwithoutorganisationofinput);
  }
  
  /**
   * To remove duplicate classes if passed as input
   * 
   * @param inputData
   * @param getData
   */
  private void removeDuplicateClasses(Map<String, Object> inputData, Map<String, Object> getData)
  {
    List<String> classesOfGet = (List<String>) getData.get(IDataRule.TYPES);
    List<String> inputClasses = (List<String>) inputData.get(ADDED_TYPES);
    inputClasses.removeAll(new HashSet<>(classesOfGet));
    inputData.put(ADDED_TYPES, inputClasses);
  }
  
}
