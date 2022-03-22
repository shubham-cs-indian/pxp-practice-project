package com.cs.core.bgprocess.services.dataintegration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.cs.config.dao.RefConfigurationDAO;
import com.cs.config.idto.IConfigAttributeDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.idto.IConfigDataRuleDTO;
import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigJSONDTO;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.config.idto.IConfigPropertyCollectionDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagDTO;
import com.cs.config.idto.IConfigTaskDTO;
import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.data.ISODateTime;
import com.cs.core.dataintegration.dto.PXONExportPlanDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.config.idto.IRootConfigDTO.ItemType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.exception.exporttoexcel.UnauthorizedNatureKlassException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.authorization.utils.PartnerAuthorizationUtils;
import com.cs.di.runtime.business.process.utils.GetBaseEntityIidsByBookmarkIdUtils;
import com.cs.di.runtime.entity.dao.IPartnerAuthorizationDAO;
import com.cs.di.runtime.entity.dao.PartnerAuthorizationDAO;

public class PXONExporter extends AbstractBGProcessJob implements IBGProcessJob {
  
  public static final String    USER_SECTION_HEADER               = "User Names";
  public static final String    TASK_SECTION_HEADER               = "Task codes";
  public static final String    RULE_SECTION_HEADER               = "Rule codes";
  public static final String    GOLDEN_RECORD_RULE_SECTION_HEADER = "Golden record rule codes";
  public static final String    ORGANIZATION_SECTION_HEADER       = "Organization codes";
  public static final String    TRANSLATION_SECTION_HEADER        = "Translations";
  public static final String    LANGUAGE_SECTION_HEADER           = "Language codes";
  public static final String    TAB_SECTION_HEADER                = "Tab codes";
  public static final String    BOOKMARK_EXPORT                   = "BOOKMARK_EXPORT";
  public static final String    BOOKMARK_ID                       = "bookmarkId";
  public static final String    EXPORT_SUB_TYPE                   = "exportSubType";
  public static final String    PERMISSION_SECTION_HEADER         = "Permission codes";
  
  private final Set<STEPS> APPLICABLE_STEPS = new HashSet<>();

  private HashMap<String, Object> partnerAuthorizationMapping = new HashMap<>();

  private enum STEPS
  {
    EXPORT_ATTRIBUTE, EXPORT_TAG, EXPORT_RELATIONSHIP, EXPORT_CLASSIFIER, EXPORT_BASEENTITY, EXPORT_PROPERTY_COLLECTION, 
    EXPORT_CONTEXT, EXPORT_MASTER_TAXONOMY, EXPORT_USER, EXPORT_TASK, EXPORT_RULE, EXPORT_GOLDEN_RECORD_RULE, EXPORT_ORGANIZATION,
    EXPORT_TRANSLATION, EXPORT_HIERARCHY_TAXONOMY, EXPORT_LANGUAGE, EXPORT_TAB, EXPORT_PERMISSION;
    
    private static final STEPS[] values = values();
    
    public static STEPS valueOf(int idx)
    {
      return values[idx];
    }
    
    private static final String[] labels = { "attributes", "tags", "relationships", "classifiers", "entities", "property collections",
        "contexts", "master taxonomies", "top hierarchies", "users", "tasks", "rules" , "golden record rules",
        "organization" , "translations", "language", "tab", "permission"  };
    
    public String getLabel()
    {
      return String.format("Export %s", labels[this.ordinal()]);
    }
    
    public static List<String> getLabels()
    {
      return Arrays.stream(values()).map(STEPS::getLabel).collect(Collectors.toList());
    }
  }
  
  private final PXONExportPlanDTO              pxonExportPlanDTO      = new PXONExportPlanDTO();
  private final List<String>                   attributCodes          = new ArrayList<>();
  private final List<String>                   tagCodes               = new ArrayList<>();
  private final List<String>                   relationshipCodes      = new ArrayList<>();
  private final Map<Long, Map<String, Object>> exportBaseEntities     = new HashMap<>();
  private final PXONExporterDAS                pxonExporterDAS        = new PXONExporterDAS();
  private final List<String>                   classifierCodes        = new ArrayList<>();
  private final List<String>                   taxonomyCodes          = new ArrayList<>();
  private final RefConfigurationDAO            configurationDAO       = new RefConfigurationDAO();
  private FileOutputStream                     outputStream;
  private int                                  entityBatchSize;
  private int                                  entityBatchNo          = 0;
  private final List<Long>                     exportedBaseEntityIIDs = new ArrayList<>();
  
 /**
   * Write data to the current export file
   *
   * @param data
   */
  private void writeExportData(String data) throws CSFormatException {
    try {
      outputStream.write(data.getBytes());
      outputStream.flush();
    } catch (IOException ex) {
      RDBMSLogger.instance().exception(ex);
      throw new CSFormatException("Write file error", ex);
    }
  }

  /**
   * Write the header of the current export file
   */
  private void writeExportHeader() throws CSFormatException {
    StringBuilder header = new StringBuilder("[\n");
    header.append(String.format("\"bgp %d started %s\",\n", getIID(), ISODateTime.now()));
    header.append(String.format("\"scope=%s\",", JSONValue.escape(pxonExportPlanDTO.toJSON())));
    writeExportData(header.toString());
    jobData.getLog().info(log() + "export scope:" + pxonExportPlanDTO.toJSON());
 }

  /**
   * Write a trailer to the current export file
   */
  private void writeExportTrailer() throws CSFormatException {
    writeExportData(
            String.format("\"bgp %d ended %s\"\n]", getIID(), ISODateTime.now()));
    try {
      outputStream.close();
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
  }

  /**
   * Write a section header to the current export file
   */
  private void writeSectionHeader(String title, List<String> codes) throws CSFormatException {
    String section = String.format("\"%s=%s\",\n", title, codes.isEmpty() ? "All" : codes);
    writeExportData(section);
    jobData.getLog().info(log() + section);
  }

  private <T extends IConfigJSONDTO> void writeConfigDTOs(Collection<T> dtos) throws CSFormatException {
    StringBuilder content = new StringBuilder();
    for (T dto : dtos) {
      try {
        content.append(dto.toPXON()).append(",\n");
      }catch(Exception e) {
        jobData.getLog().error(e.getMessage());
      }
    }
    writeExportData(content.toString());
  }
  
  private <T extends IConfigJSONDTO> void writeConfigDTOs(Map<String, Collection<T>> dtosMap) throws CSFormatException
  {
    StringBuilder content = new StringBuilder();
    for (String key : dtosMap.keySet()) {
      content.append("{").append("\"").append(key).append("\"");
      content.append("[");
      Collection<T> dtos = dtosMap.get(key);
      for (T dto : dtos) {
        content.append(dto.toPXON()).append(",\n");
      }
      content.append("[");
    }
  }
  
  /**
   * Prepare steps based on {@link PXONExporter#pxonExportPlanDTO}
   */
  private void prepareStepsToExport() throws RDBMSException {
    // Add more steps for other exports base on pxonExportPlanDto
    Collection<ItemType> itemTypes = pxonExportPlanDTO.getConfigItemTypes();
    if (pxonExportPlanDTO.includeAllEntities() || pxonExportPlanDTO.getBaseEntityIIDs().size() > 0) {
      APPLICABLE_STEPS.add(STEPS.EXPORT_BASEENTITY);
      if (pxonExportPlanDTO.includeAllEntities()) {
        exportedBaseEntityIIDs.addAll(pxonExporterDAS.getAllEntitiesIID(pxonExportPlanDTO));
      } else {
        exportedBaseEntityIIDs.addAll(pxonExportPlanDTO.getBaseEntityIIDs());
      }
    }
    itemTypes.forEach(item -> {
      switch (item) {
        case PROPERTY:
          if (attributCodes.isEmpty() && tagCodes.isEmpty() && relationshipCodes.isEmpty()) {
            APPLICABLE_STEPS.add(STEPS.EXPORT_ATTRIBUTE);
            APPLICABLE_STEPS.add(STEPS.EXPORT_TAG);
            APPLICABLE_STEPS.add(STEPS.EXPORT_RELATIONSHIP);
          }

          if (!attributCodes.isEmpty()) {
            APPLICABLE_STEPS.add(STEPS.EXPORT_ATTRIBUTE);
          }
          if (!tagCodes.isEmpty()) {
            APPLICABLE_STEPS.add(STEPS.EXPORT_TAG);
          }
          if (!relationshipCodes.isEmpty()) {
            APPLICABLE_STEPS.add(STEPS.EXPORT_RELATIONSHIP);
          }
          break;

        case PROPERTY_COLLECTION:
          APPLICABLE_STEPS.add(STEPS.EXPORT_PROPERTY_COLLECTION);
          break;

        case CONTEXT:
          APPLICABLE_STEPS.add(STEPS.EXPORT_CONTEXT);
          break;

        case CLASSIFIER:
          APPLICABLE_STEPS.add(STEPS.EXPORT_CLASSIFIER);
          break;

        case MASTER_TAXONOMY:
          APPLICABLE_STEPS.add(STEPS.EXPORT_MASTER_TAXONOMY);
          break;
        
        case USER:
          APPLICABLE_STEPS.add(STEPS.EXPORT_USER);
          break;
          
        case TASK:
          APPLICABLE_STEPS.add(STEPS.EXPORT_TASK);
          break;
        
        case RULE:
          APPLICABLE_STEPS.add(STEPS.EXPORT_RULE);
          break;
          
        case GOLDEN_RECORD_RULE:
          APPLICABLE_STEPS.add(STEPS.EXPORT_GOLDEN_RECORD_RULE);
          break;
          
        case ORGANIZATION:
          APPLICABLE_STEPS.add(STEPS.EXPORT_ORGANIZATION);
          break;
          
        case TRANSLATION:
          APPLICABLE_STEPS.add(STEPS.EXPORT_TRANSLATION);
          break;
        // TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies   
        /*case HIERARCHY_TAXONOMY:
          APPLICABLE_STEPS.add(STEPS.EXPORT_HIERARCHY_TAXONOMY);
          break;*/
        case LANGUAGE:
          APPLICABLE_STEPS.add(STEPS.EXPORT_LANGUAGE);
          break;
          
        case TAB:
          APPLICABLE_STEPS.add(STEPS.EXPORT_TAB);
          break;
        
        case PERMISSION:
          APPLICABLE_STEPS.add(STEPS.EXPORT_PERMISSION);
          break;

        default:
          break;
      }
    });
  }

  private void prepareClassifierTaxonomyCodesToExport() {
    Collection<String> classifierConfigCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.CLASSIFIER);
    if (classifierConfigCodes != null) {
      prepareClassifierTaxonomyCodesToExport(classifierCodes, taxonomyCodes, classifierConfigCodes);
    }
  }

  private void prepareClassifierTaxonomyCodesToExport(List<String> classifiers,
          List<String> taxonomys, Collection<String> configCodes) {
    configCodes.forEach(codeWithType -> {
      String code = codeWithType.split(":")[0];
      String classifierType = codeWithType.split(":")[1];
      IClassifierDTO.ClassifierType classifier = IClassifierDTO.ClassifierType.valueOf(classifierType);
      switch (classifier) {
        case CLASS:
          classifiers.add(code);
          break;
        case TAXONOMY:
        case MINOR_TAXONOMY:  
       // case HIERARCHY: TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
          taxonomys.add(code);
          break;
        default:
          break;
      }
    });
  }

  private void preparePropertyCodesToExport() {
    Collection<String> propertyCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.PROPERTY);
    if (propertyCodes != null) {
      propertyCodes.forEach(codeWithType -> {
        String code = codeWithType.split(":")[0];
        String propertyType = codeWithType.split(":")[1];

        SuperType propertySuperType = PropertyType.valueOf(propertyType).getSuperType();

        switch (propertySuperType) {
          case ATTRIBUTE:
            attributCodes.add(code);
            break;

          case TAGS:
            tagCodes.add(code);
            break;

          case RELATION_SIDE:
            if (List.of(PropertyType.RELATIONSHIP.toString(), PropertyType.NATURE_RELATIONSHIP.toString()).contains(propertyType))
            relationshipCodes.add(code);
            break;
          
          default:
            break;
        }
      });
    }
  }

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
          throws CSInitializationException, CSFormatException, RDBMSException {
    super.initBeforeStart(initialProcessData, userSession);
    // Entry data to work on
    entityBatchSize = CSProperties.instance().getInt(propName("batchSize"));
    entityBatchSize = Math.max(entityBatchSize, 1); // minimum 1
    pxonExportPlanDTO.fromJSON(jobData.getEntryData().toString());
   Map<String, Object> searchCriteria = pxonExportPlanDTO.getSearchCriteria();
   if (!searchCriteria.isEmpty()) {
     String exportSubType = (String) searchCriteria.get(EXPORT_SUB_TYPE);   
     if (exportSubType.equals(BOOKMARK_EXPORT)) {
       IIdsListParameterModel iids = new GetBaseEntityIidsByBookmarkIdUtils()
           .triggerGetBaseEntityIidsByBookmarkId(userSession,
               pxonExportPlanDTO.getOrganizationCode(), pxonExportPlanDTO.getCatalogCode(),
               pxonExportPlanDTO.getLocaleID(), (String) searchCriteria.get(BOOKMARK_ID));
       
       iids.getIds().stream().forEach((baseEntityIid)-> pxonExportPlanDTO.getBaseEntityIIDs().add(Long.valueOf(baseEntityIid)));
       
     }else {
       pxonExportPlanDTO.getBaseEntityIIDs().addAll(bgpDAS.getBaseEntityIIdsByQuery(pxonExportPlanDTO));
      }
    }
    String directory = CSProperties.instance().getString(propName("filePath"));
    Path path = FileSystems.getDefault()
            .getPath(directory, "export#" + initialProcessData.getJobIID() + ".pxon");
    File dir = new File(directory);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    try {
      outputStream = new FileOutputStream(path.toFile());
      writeExportHeader();
    } catch (IOException exc) {
      throw new CSInitializationException("Can't open export file", exc);
    }

    // Prepare property codes
    preparePropertyCodesToExport();

    // prepare classifier codes
    prepareClassifierTaxonomyCodesToExport();

    // Prepare steps based on DTO
    prepareStepsToExport();

    jobData.getProgress().initSteps(STEPS.getLabels().toArray(new String[0]));
    updateProcessData();
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d / export file=%s ",
            getService(), getIID(), path.toString());
  }

  private void exportTaxonomy() throws CSFormatException, CSInitializationException {
    Collection<IConfigClassifierDTO> taxonomies = configurationDAO.getTaxonomies(true,
            pxonExportPlanDTO.getLocaleID(), taxonomyCodes.toArray(new String[0]));
    writeSectionHeader("Taxonomy codes", taxonomyCodes);
    writeConfigDTOs(taxonomies);
  }

  private void exportClassifier() throws CSFormatException, CSInitializationException {
    EntityType entityType = pxonExportPlanDTO.getEntityType();
    if(entityType.ordinal() == EntityType.UNDEFINED.ordinal())
      entityType = EntityType.KLASS;
    Collection<IConfigClassifierDTO> classes = configurationDAO.getClasses(true,
            pxonExportPlanDTO.getLocaleID(), entityType, classifierCodes.toArray(new String[0]));
    writeSectionHeader("Classifier codes", classifierCodes);
    writeConfigDTOs(classes);
    writeExportData("\"Classifier codes=" + (classifierCodes.isEmpty() ? "All" : classifierCodes) + "\",");
  }

  private void exportContext() throws CSFormatException, CSInitializationException {
    Collection<String> configItemCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.CONTEXT);
    List<String> contextCodes = prepareContextCodeToExport(configItemCodes);
    Collection<IConfigContextDTO> configContexts = configurationDAO
            .getContexts(pxonExportPlanDTO.getLocaleID(), contextCodes.toArray(new String[0]));
    writeSectionHeader("Context codes", contextCodes);
    writeConfigDTOs(configContexts);
  }

  private List<String> prepareContextCodeToExport(Collection<String> configItemCodes)
  {
    List<String> contextCodes = new ArrayList<>();
    if (configItemCodes != null) {
      configItemCodes.forEach(codeWithType -> {
        String code = codeWithType.split(":")[0];
        contextCodes.add(code);
      });
    }
    return contextCodes;
  }

  private void exportPropertyCollection() throws CSFormatException, CSInitializationException {
    Collection<String> configItemCodes = pxonExportPlanDTO
            .getConfigItemCodes(ItemType.PROPERTY_COLLECTION);
    List<String> propertyCollectionCodes = new ArrayList<>(configItemCodes);
    Collection<IConfigPropertyCollectionDTO> propertyCollections = configurationDAO
            .getPropertyCollections(pxonExportPlanDTO.getLocaleID(), propertyCollectionCodes.toArray(new String[0]));
    writeSectionHeader("PropertyCollection codes", propertyCollectionCodes);
    writeConfigDTOs(propertyCollections);
  }

  private void exportAttributes() throws CSFormatException, CSInitializationException {
    Collection<IConfigAttributeDTO> attributes
            = configurationDAO.getAttributes(pxonExportPlanDTO.getLocaleID(), attributCodes.toArray(new String[0]));
    writeSectionHeader("Attribute codes", attributCodes);
    writeConfigDTOs(attributes);
  }

  private void exportTags() throws CSFormatException, CSInitializationException {
    Collection<IConfigTagDTO> tags = configurationDAO.getTags(pxonExportPlanDTO.getLocaleID(),
            tagCodes.toArray(new String[0]));
    writeSectionHeader("Tag codes", tagCodes);
    writeConfigDTOs(tags);
  }

  private void exportRelationships() throws CSFormatException, CSInitializationException {
    Collection<IConfigRelationshipDTO> relationships = configurationDAO.getRelationships(
            pxonExportPlanDTO.getLocaleID(), relationshipCodes.toArray(new String[0]));
    writeSectionHeader("Relationships codes", relationshipCodes);
    writeConfigDTOs(relationships);
  }

  private void exportUser() throws CSFormatException, CSInitializationException
  {
    Collection<String> configItemCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.USER);
    List<String> userNames = new ArrayList<>(configItemCodes);
    Collection<IConfigUserDTO> configUsers = configurationDAO.getUsers(pxonExportPlanDTO.getLocaleID(), userNames.toArray(new String[0]));
    writeSectionHeader(USER_SECTION_HEADER, userNames);
    writeConfigDTOs(configUsers);
  }
  
  private void exportTask() throws CSFormatException, CSInitializationException
  {
    Collection<String> configTaskCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.TASK);
    List<String> taskCodes = new ArrayList<>(configTaskCodes);
    Collection<IConfigTaskDTO> configTasks = configurationDAO.getTasks(pxonExportPlanDTO.getLocaleID(), taskCodes.toArray(new String[0]));
    writeSectionHeader(TASK_SECTION_HEADER, taskCodes);
    writeConfigDTOs(configTasks);
  }
  
  private void exportRule() throws CSFormatException, CSInitializationException
  {
    Collection<String> configRuleCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.RULE);
    List<String> ruleCodes = new ArrayList<>(configRuleCodes);
    Collection<IConfigDataRuleDTO> configRules = configurationDAO.getRules(pxonExportPlanDTO.getLocaleID(),
        ruleCodes.toArray(new String[0]));
    writeSectionHeader(RULE_SECTION_HEADER, ruleCodes);
    writeConfigDTOs(configRules);
  }
  
  
  private void exportGoldenRecordRule() throws CSFormatException, CSInitializationException
  {
    Collection<String> configRuleCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.GOLDEN_RECORD_RULE);
    List<String> ruleCodes = new ArrayList<>(configRuleCodes);
    Collection<IConfigGoldenRecordRuleDTO> configGoldenRules = configurationDAO.getGoldenRecordRules(pxonExportPlanDTO.getLocaleID(),
        ruleCodes.toArray(new String[0]));
    writeSectionHeader(GOLDEN_RECORD_RULE_SECTION_HEADER, ruleCodes);
    writeConfigDTOs(configGoldenRules);
  }
  
  private void exportOrganizations() throws CSFormatException, CSInitializationException
  {
    Collection<String> configOrganizationCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.ORGANIZATION);
    List<String> organizationCodes = new ArrayList<>(configOrganizationCodes);
    Collection<IConfigOrganizationDTO> configOrganizations = configurationDAO.getOrganizations(pxonExportPlanDTO.getLocaleID(), organizationCodes.toArray(new String[0]));
    writeSectionHeader(ORGANIZATION_SECTION_HEADER, organizationCodes);
    writeConfigDTOs(configOrganizations);
  }
  
  private void exportTranslations() throws CSFormatException, CSInitializationException
  {
    Set<String> localeIDs = pxonExportPlanDTO.getLanguages();
    IConfigTranslationDTO.EntityType entityType = pxonExportPlanDTO.getEntityType();
    Collection<IConfigTranslationDTO> translations = configurationDAO.getTranslations(entityType.getEntityType(),
        pxonExportPlanDTO.getSortLanguage(), pxonExportPlanDTO.getSortBy(), localeIDs.toArray(new String[0]));
    writeSectionHeader(TRANSLATION_SECTION_HEADER, new ArrayList<>());
    writeConfigDTOs(translations);
  }
  
  @Deprecated // PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
  private void exportHierarchyTaxonomy() throws CSFormatException, CSInitializationException {
    Collection<IConfigClassifierDTO> taxonomies = configurationDAO.getHierarchyTaxonomies(
            true, pxonExportPlanDTO.getLocaleID(), taxonomyCodes.toArray(new String[0]));
    writeSectionHeader("Hiearchy Taxonomy codes", taxonomyCodes);
    writeConfigDTOs(taxonomies);
  }

  private void exportLanguages() throws CSFormatException, CSInitializationException
  {
    Collection<String> configLanguageCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.LANGUAGE);
    List<String> languageCodes = new ArrayList<>(configLanguageCodes);
    Collection<IConfigLanguageDTO> configLanguages = configurationDAO.getLanguages(pxonExportPlanDTO.getLocaleID(), languageCodes.toArray(new String[0]));
    writeSectionHeader(LANGUAGE_SECTION_HEADER, languageCodes);
    writeConfigDTOs(configLanguages);
  }
  
  private void exportTabs() throws CSFormatException, CSInitializationException
  {
    Collection<String> configTabCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.TAB);
    List<String> tabCodes = new ArrayList<>(configTabCodes);
    Collection<IConfigTabDTO> configTabs = configurationDAO.getTabs(pxonExportPlanDTO.getLocaleID(), tabCodes.toArray(new String[0]));
    writeSectionHeader(TAB_SECTION_HEADER, tabCodes);
    writeConfigDTOs(configTabs);
  }
  
  private void exportPermissions() throws CSFormatException, CSInitializationException
  {
    Collection<String> configPermissionCodes = pxonExportPlanDTO.getConfigItemCodes(ItemType.PERMISSION);
    List<String> permissionCodes = new ArrayList<>(configPermissionCodes);
    Collection<IConfigPermissionDTO> configPermissions = configurationDAO.getPermissions(pxonExportPlanDTO.getLocaleID(), permissionCodes.toArray(new String[0]));
    writeSectionHeader(PERMISSION_SECTION_HEADER, permissionCodes);
    writeConfigDTOs(configPermissions);
  }

  /**
   * @return the base entity iids to be exported by current batch
   */
  private List<Long> getBatchBaseEntityIIDs() {
     int startIndex = entityBatchNo * entityBatchSize;
    int endIndex = startIndex + entityBatchSize;
    endIndex = endIndex <= exportedBaseEntityIIDs.size() ? endIndex : exportedBaseEntityIIDs.size();
    // select the IIDs to be exported
    List<Long> iids = new ArrayList<>();
    for (int i = startIndex; i < endIndex; i++) {
      iids.add(exportedBaseEntityIIDs.get(i));
    }
    return iids;
  }
  
  /**
   * Export a batch of base entities
   * @param iids the list of base entity iids to be considered in the batch
   * @return true when all entities IIDs have been exported
   * @throws RDBMSException
   * @throws CSInitializationException
   * @throws CSFormatException
   */
  private void exportBaseEntities( List<Long> iids)
          throws RDBMSException, CSInitializationException, CSFormatException{
    int successCount = 0;
    jobData.getSummary().setTotalCount(jobData.getSummary().getTotalCount() + iids.size());
    String exportSection = String.format("Base Entity Export batch %d with batch size of %d",
            entityBatchNo + 1, iids.size());
    
    writeExportData("\"" + exportSection + "\",");
    jobData.getLog().info(log() + exportSection);

    // complete exported IIDs
    pxonExporterDAS.getAllBaseEntittiesByIID(iids, exportBaseEntities, pxonExportPlanDTO.getLocaleID());
    pxonExporterDAS.exportAllValueRecord(iids, pxonExportPlanDTO.getLocaleID(), exportBaseEntities);
    pxonExporterDAS.exportAllTagRecord(iids, exportBaseEntities);
    pxonExporterDAS.getOtherClassifier(iids, exportBaseEntities);
    pxonExporterDAS.getEmptyRecords(iids, exportBaseEntities, pxonExportPlanDTO);
    pxonExporterDAS.exportAllRelationRecord(iids, exportBaseEntities);
    if (pxonExportPlanDTO.includeEmbeddedEntities()) {
      pxonExporterDAS.getAllEmbdEntities(iids, exportBaseEntities, pxonExportPlanDTO.getLocaleID());
    }
    
    StringBuilder buffer = new StringBuilder();
    String partnerAuthorizationId = pxonExportPlanDTO.getPartnerAuthorizationId();
    if (partnerAuthorizationId != null) {
      partnerAuthorizationMapping = getPartnerAuthMapping(pxonExportPlanDTO);
    }
    
    for (Entry<Long, Map<String, Object>> baseEntity : exportBaseEntities.entrySet()) {
      if (!(partnerAuthorizationMapping == null || partnerAuthorizationMapping.isEmpty())) {
        boolean isExportSuccess = applyAuthorizationOnPXONGeneration(buffer, baseEntity);
        if (isExportSuccess)
          successCount ++;
      }
      else {
        buffer.append(JSONValue.toJSONString(baseEntity.getValue())).append(",");
        jobData.getSuccessIds().add(String.valueOf(baseEntity.getKey()));
        successCount++;
      }
    }
    writeExportData(buffer.toString());
    exportBaseEntities.clear();
    entityBatchNo++;
    jobData.getSummary().setSuccessCount(jobData.getSummary().getSuccessCount() + successCount);
  }

  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException {
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      return jobData.getStatus();
    }
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    STEPS currentStep = STEPS.valueOf(currentStepNo - 1);
    if (APPLICABLE_STEPS.contains(currentStep)) {
      jobData.getLog().progress(log() + String.format("Execution of step: %d. %s", currentStepNo, currentStep.getLabel()));
      switch (currentStep) {
        case EXPORT_ATTRIBUTE:
          exportAttributes();
          break;
        case EXPORT_TAG:
          exportTags();
          break;
        case EXPORT_RELATIONSHIP:
          exportRelationships();
          break;
        case EXPORT_PROPERTY_COLLECTION:
          exportPropertyCollection();
          break;
        case EXPORT_CONTEXT:
          exportContext();
          break;
        case EXPORT_CLASSIFIER:
          exportClassifier();
          break;
        case EXPORT_MASTER_TAXONOMY:
          exportTaxonomy();
          break;
          
        case EXPORT_USER:
          exportUser(); 
          break;
          
        case EXPORT_TASK:
          exportTask();
          break;
          
        case EXPORT_RULE:
          exportRule();
          break;
          
        case EXPORT_GOLDEN_RECORD_RULE:
          exportGoldenRecordRule();
          break;
          
        case EXPORT_ORGANIZATION:
          exportOrganizations();
          break;
          
        case EXPORT_LANGUAGE:
          exportLanguages();
          break;

        case EXPORT_TAB:
          exportTabs();
          break;
          
        case EXPORT_PERMISSION:
          exportPermissions();
          break;

        case EXPORT_BASEENTITY:
          // run a batch of export base entities and return only if all batches have been executed
          List<Long> iids = getBatchBaseEntityIIDs();
          if ( !iids.isEmpty() ) {
            exportBaseEntities(iids);
            jobData.setStatus(BGPStatus.RUNNING);
            return jobData.getStatus();
          } else {
            jobData.getLog().info(log() + "All base entities exported.");
          }
          break;
          
        case EXPORT_TRANSLATION:
          exportTranslations();
          break;
        
        //PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
        /*case EXPORT_HIERARCHY_TAXONOMY:
           exportHierarchyTaxonomy();*/
          
        default:
          RDBMSLogger.instance().warn("Found unexpected step: " + currentStep);
          break;
      }
    }
    jobData.getProgress().incrStepNo();

    if (jobData.getProgress().getPercentageCompletion() == 100) {
      writeExportTrailer();
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
    } else {
      jobData.setStatus(BGPStatus.RUNNING);
    }
    return jobData.getStatus();
  }
  
  
  /**
   * this method will write the header for log file.
   */  
  @Override  
  protected void writeLogHeader()
  {
    jobData.getLog().info("Time|BGP Service|Job iid|Session Id|User Name|Comment" );
    jobData.getLog().progress(log() + "Starting from status %s\n", jobData.getStatus());
  }
  
  /**
   * gives authorization mapping
   * @param exportPlanDTO
   * @return
   */
  private HashMap<String, Object> getPartnerAuthMapping(PXONExportPlanDTO exportPlanDTO)
  {
    Map<String, Object> authorizationMapping = null;
    if (!exportPlanDTO.getPartnerAuthorizationId().equals("null")) {
      IPartnerAuthorizationDAO partnerAuthorizationDAO = new PartnerAuthorizationDAO();
      authorizationMapping = partnerAuthorizationDAO.getPartnerAuthMapping(exportPlanDTO.getPartnerAuthorizationId(),
          exportPlanDTO.getLocaleID(),true);
    }
    return (authorizationMapping != null) ? (HashMap<String, Object>) authorizationMapping.get(PartnerAuthorizationUtils.ENTITY) : null;
  }
  
  /**
   * This applies authorization 
   * and generate PXON block only for authorized entities
   * if authorization is linked to the Export Task 
   * @param buffer
   * @param baseEntity
   * @param succesCount 
   */
  private boolean applyAuthorizationOnPXONGeneration(StringBuilder buffer, Entry<Long, Map<String, Object>> baseEntity)
      throws UnauthorizedNatureKlassException
  {
    try {
      IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
      baseEntityDTO.fromPXON(JSONValue.toJSONString(baseEntity.getValue()));
      PartnerAuthorizationUtils.exportAuthorizationfilter(baseEntityDTO, partnerAuthorizationMapping);
      buffer.append(baseEntityDTO.toJSON()).append(",");
      jobData.getSuccessIds().add(String.valueOf(baseEntity.getKey()));
      return true;
    }
    catch (UnauthorizedNatureKlassException e) {
      jobData.getLog().warn(e.getMessage()); // report warning in the log
      jobData.getFailedIds().add(String.valueOf(baseEntity.getKey()));
    }
    catch (Exception exception) {
      RDBMSLogger.instance().debug("PXONExporter :: applyAuthorizationToGenenratePXON failed due to", new Object[] { exception });
      jobData.getFailedIds().add(String.valueOf(baseEntity.getKey()));
    }
    return false;
  }
  
  @Override
  protected String getCallbackData()
  {
    Map<String, Object> callBackData = new HashMap<>();
    callBackData.put(SUCCESS_IIDS, jobData.getSuccessIds());
    callBackData.put(FAILED_IIDS, jobData.getFailedIds());
    return new JSONObject(callBackData).toString();
  }
}