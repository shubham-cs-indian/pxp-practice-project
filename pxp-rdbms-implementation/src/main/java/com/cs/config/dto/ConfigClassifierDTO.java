package com.cs.config.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigEmbeddedRelationDTO;
import com.cs.config.idto.IConfigNatureRelationshipDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigClassifierDTO extends AbstractConfigJSONDTO implements IConfigClassifierDTO {

  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();

  /** Definition of the Classifier and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.classifierIID, IPXON.PXONTag.classifieriid.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isNature, IPXON.PXONTag.isnature.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.natureType, IPXON.PXONTag.naturetype.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.parentCode, IPXON.PXONTag.parentcode.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.subType, IPXON.PXONTag.subtype.toPrivateTag());

    // updatable properties
    KEY_MAP.put(ConfigTag.contextCode, IPXON.PXONTag.contextcode.toTag());
    KEY_MAP.put(ConfigTag.embeddedClasses, IPXON.PXONTag.embeddedclasses.toJSONContentTag());
    KEY_MAP.put(ConfigTag.events, IPXON.PXONTag.events.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.isAbstract, IPXON.PXONTag.isabstract.toTag());
    KEY_MAP.put(ConfigTag.isDefaultChild, IPXON.PXONTag.isdefault.toTag());
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.numberOfVersionsToMaintain, IPXON.PXONTag.numberofversionstomaintain.toTag());
    KEY_MAP.put(ConfigTag.productVariantContextCode,IPXON.PXONTag.productvariantcontextcode.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.promotionalVersionContextCode,IPXON.PXONTag.promotionalversioncontextcode.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.relationships, IPXON.PXONTag.relationships.toCSEListTag());
    KEY_MAP.put(ConfigTag.sections, IPXON.PXONTag.sections.toJSONContentTag());
    KEY_MAP.put(ConfigTag.statusTags, IPXON.PXONTag.statustags.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tasks, IPXON.PXONTag.tasks.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.levelCode, IPXON.PXONTag.levelCode.toCSEListTag());
    KEY_MAP.put(ConfigTag.parentCode, IPXON.PXONTag.parentCode.toTag());
    KEY_MAP.put(ConfigTag.masterParentTag, IPXON.PXONTag.masterParentTag.toTag());
    KEY_MAP.put(ConfigTag.levelLabels, IPXON.PXONTag.levelLabels.toCSEListTag());
    KEY_MAP.put(ConfigTag.taxonomyType, IPXON.PXONTag.taxonomytype.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.previewImage, IPXON.PXONTag.previewimage.toTag());
    KEY_MAP.put(ConfigTag.extensionConfiguration, IPXON.PXONTag.extensionconfiguration.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.isNewlyCreatedLevel, IPXON.PXONTag.isnewlycreatedlevel.toTag());
    KEY_MAP.put(ConfigTag.levelIndex, IPXON.PXONTag.levelindex.toTag());
    KEY_MAP.put(ConfigTag.detectDuplicate , IPXON.PXONTag.detectduplicate.toTag());
    KEY_MAP.put(ConfigTag.uploadZip , IPXON.PXONTag.extractzip.toTag());
    KEY_MAP.put(ConfigTag.trackDownloads , IPXON.PXONTag.trackdownloads.toTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.classifierIID);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
  }

  private final ClassifierDTO                                     classifierDTO       = new ClassifierDTO();
  private final Set<IConfigNatureRelationshipDTO>                 relationshipDTOS    = new HashSet<>();
  private final Map<String, Collection<IConfigSectionElementDTO>> sections            = new HashMap<>();
  private final Map<String, IConfigEmbeddedRelationDTO>           embeddedRelationDTO = new HashMap<>();
  private final List<String>                                      levelCodes          = new ArrayList<>();
  private final List<String>                                      levelLabels         = new ArrayList<>();

  /**
   * Initializing the static key map
   */
  public ConfigClassifierDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    initClassifierDTO();
  }

  private void initClassifierDTO() throws CSFormatException
  {
    classifierDTO.setCode(getString(ConfigTag.code));
    classifierDTO.setIID(getLong(ConfigTag.classifierIID));
    String sPropertyType = getString(ConfigTag.type);
    IClassifierDTO.ClassifierType classifierType = IConfigMap.getClassType(sPropertyType);
    // convert ODB class path into Property type
    String subType = IConfigClass.ClassifierClass.valueOfClassPath(sPropertyType).name();
    setString(ConfigTag.subType, subType);
    classifierDTO.setClassifierType(classifierType);

    //Relationship DTOs preparation
    relationshipDTOS.clear();
    JSONArray relationshipList = getJSONArray(ConfigTag.relationships);
    for (Object relationship : relationshipList) {
      ConfigNatureRelationshipDTO relationshipDTO = new ConfigNatureRelationshipDTO();
      relationshipDTO.fromJSON(relationship.toString());
      relationshipDTOS.add(relationshipDTO);
    }

    //Section DTOs preparation
    sections.clear();
    Map<String, Object> sectionList = (Map<String, Object>) configData.toJSONObject()
        .get(ConfigTag.sections.toString());
    if (sectionList != null) {
      for (Map.Entry section : sectionList.entrySet()) {
        JSONArray sectionElements = (JSONArray) section.getValue();
        Set<IConfigSectionElementDTO> elementDTOS = new HashSet<>();
        for (Object element : sectionElements) {
          ConfigSectionElementDTO elementDTO = new ConfigSectionElementDTO();
          elementDTO.fromJSON(element.toString());
          elementDTOS.add(elementDTO);
        }
        sections.put(section.getKey().toString(), elementDTOS);
      }
    }

    //Embedded class with coupling info
    embeddedRelationDTO.clear();
    Map<String, Object> embeddedClassCodes = (Map<String, Object>) configData.toJSONObject()
        .get(ConfigTag.embeddedClasses.toString());
    if (embeddedClassCodes != null) {
      for (Map.Entry embeddedClass : embeddedClassCodes.entrySet()) {
          ConfigEmbeddedRelationDTO erDTO = new ConfigEmbeddedRelationDTO();
          erDTO.fromJSON(embeddedClass.getValue().toString());
          embeddedRelationDTO.put(embeddedClass.getKey().toString(), erDTO);
      }
    }
    
    //Level Codes
    levelCodes.clear();
    JSONArray levelCodeList = getJSONArray(ConfigTag.levelCode);
    for (Object levelCode : levelCodeList) {
    	levelCodes.add((String) levelCode);
    }
    
    //
    levelLabels.clear();
    JSONArray levelLabelsList = getJSONArray(ConfigTag.levelLabels);
    for (Object levelLabel : levelLabelsList) {
      levelLabels.add((String) levelLabel);
    }
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    super.fromPXON(json);
    setString(ConfigTag.code, classifierDTO.getCode());

    String subType = getString(ConfigTag.subType);
    IConfigClass.ClassifierClass classifierType = IConfigClass.ClassifierClass.valueOf(subType);
    setString(ConfigTag.type, classifierType.toString());

    //Classifier loaded with PXON relationship, sections and embedded classes
    //-> Need to load config json format data in configData

    //Relationship DTOs preparation
    relationshipDTOS.clear();
    JSONArray pxonRelationshipList = getJSONArray(ConfigTag.relationships);
    JSONArray configRelationshipList = new JSONArray();
    for (Object relationship : pxonRelationshipList) {
      ConfigNatureRelationshipDTO relationshipDTO = new ConfigNatureRelationshipDTO();
      relationshipDTO.fromPXON(relationship.toString());
      relationshipDTOS.add(relationshipDTO);
      configRelationshipList.add(new JSONContent(relationshipDTO.toJSON()).toJSONObject());
    }
    configData.setField(ConfigTag.relationships.toString(), configRelationshipList);

    //Section DTOs preparation
    sections.clear();
    Map<String, Object> pxonSectionList = (Map<String, Object>) configData.toJSONObject()
        .get(ConfigTag.sections.toString());
    Map<String, Object> configSectionList = new HashMap<>();
    if (pxonSectionList != null) {
      for (Map.Entry section : pxonSectionList.entrySet()) {
        JSONArray sectionElements = (JSONArray) section.getValue();
        Set<IConfigSectionElementDTO> elementDTOS = new HashSet<>();
        JSONArray configElements = new JSONArray();
        for (Object element : sectionElements) {
          ConfigSectionElementDTO elementDTO = new ConfigSectionElementDTO();
          elementDTO.fromPXON(element.toString());
          elementDTOS.add(elementDTO);
          configElements.add(new JSONContent(elementDTO.toJSON()).toJSONObject());
        }
        sections.put(section.getKey().toString(), elementDTOS);
        configSectionList.put(section.getKey().toString(), configElements);
      }
    }
    configData.setField(ConfigTag.sections.toString(), configSectionList);

    //Embedded class with coupling info
    embeddedRelationDTO.clear();
    Map<String, Object> pxonEmbeddedClass = (Map<String, Object>) configData.toJSONObject()
        .get(ConfigTag.embeddedClasses.toString());
    Map<String, Object> configEmbeddedClasses = new HashMap<>();
    if (pxonEmbeddedClass != null) {
      for (Map.Entry embeddedClass : pxonEmbeddedClass.entrySet()) {
        ConfigEmbeddedRelationDTO erDTO = new ConfigEmbeddedRelationDTO();
        erDTO.fromPXON(embeddedClass.getValue().toString());
        embeddedRelationDTO.put(embeddedClass.getKey().toString(), erDTO);
        configEmbeddedClasses.put(embeddedClass.getKey().toString(),
            new JSONContent(erDTO.toJSON()).toJSONObject());
      }
    }
    configData.setField(ConfigTag.embeddedClasses.toString(), configEmbeddedClasses);
    
    //level preparation
    levelCodes.clear();
    JSONArray pxonLevelCodeList = getJSONArray(ConfigTag.levelCode);
    for (Object level : pxonLevelCodeList) {
      levelCodes.add((String) level);
    }
    configData.setField(ConfigTag.levelCode.toString(), levelCodes);
    
    levelLabels.clear();
    JSONArray pxonLevelLabelsList = getJSONArray(ConfigTag.levelLabels);
    for (Object levelLabel : pxonLevelLabelsList) {
      levelLabels.add((String) levelLabel);
    }
    configData.setField(ConfigTag.levelLabels.toString(), levelLabels);
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return classifierDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    ICSEElement cse = parser.getCSEElement(IPXON.PXONTag.csid.toTag());
    CSEObject ccse = (CSEObject) cse;
    classifierDTO.setCode(ccse.getCode());
    classifierDTO.setClassifierType(ccse.getSpecification(ClassifierType.class, ICSEElement.Keyword.$type));
    classifierDTO.setIID(0);
    configData.setField( ConfigTag.code.toString(), getCode());
    setCode( getString( ConfigTag.code));
    if(KEY_MAP.containsKey(ConfigTag.type)) {
      String objectType = cse.getSpecification( ICSEElement.Keyword.$type);
      configData.setField( ConfigTag.type.toString(), objectType);
    }
  }

  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = super.toConfigPXONContent();
    //Relationship preparation
    JSONArray pxonRelationships = new JSONArray();
    for (IConfigRelationshipDTO configRelationshipDTO: relationshipDTOS) {
      pxonRelationships.add(((ConfigRelationshipDTO)configRelationshipDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.relationships), pxonRelationships);

    //Sections
    Map<String, Object> pxonSections = new HashMap<>();
    for (String sectionCode: sections.keySet()) {
      Collection<IConfigSectionElementDTO> elementDTOs = sections.get(sectionCode);
      JSONArray elements = new JSONArray();
      for (IConfigSectionElementDTO elemDTO: elementDTOs) {
        elements.add(((ConfigSectionElementDTO)elemDTO).toConfigPXONContent().toJSONObject());
      }
      pxonSections.put(sectionCode, elements);
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.sections), pxonSections);

    //Embedded
    Map<String, Object> pxonEmbeddedClasses = new HashMap<>();
    for (String embeddedClassCode: embeddedRelationDTO.keySet()) {
      pxonEmbeddedClasses.put(embeddedClassCode,
          ((ConfigEmbeddedRelationDTO)embeddedRelationDTO.get(embeddedClassCode)).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.embeddedClasses), pxonEmbeddedClasses);
    pxonOutput.setField(KEY_MAP.get(ConfigTag.levelCode), levelCodes);
    pxonOutput.setField(KEY_MAP.get(ConfigTag.levelLabels), levelLabels);

    return pxonOutput;
  }
  
  @Override public String getParentCode()
  {
    return getString(ConfigTag.parentCode);
  }

  @Override public String getType()
  {
    return getString(ConfigTag.type);
  }

  @Override public String getLabel()
  {
    return getString(ConfigTag.label);
  }

  @Override public boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }

  @Override public int getNumberOfVersionsToMaintain()
  {
    return getInt(ConfigTag.numberOfVersionsToMaintain);
  }

  @Override public boolean isDefault()
  {
    return getBoolean(ConfigTag.isDefaultChild);
  }

  @Override public boolean isAbstract()
  {
    return getBoolean(ConfigTag.isAbstract);
  }

  @Override public String getNatureType()
  {
    return getString(ConfigTag.natureType);
  }

  @Override public boolean isNature()
  {
    return getBoolean(ConfigTag.isNature);
  }

  @Override public Collection<String> getEvents()
  {
    return getJSONArray(ConfigTag.events);
  }

  @Override public Collection<String> getTasks()
  {
    return getJSONArray(ConfigTag.tasks);
  }

  @Override public String getContextCode()
  {
    return getString(ConfigTag.contextCode);
  }

  @Override public Collection<String> getStatusTags()
  {
    return getJSONArray(ConfigTag.statusTags);
  }

  @Override public Collection<String> getProductVariantContextCode()
  {
    return getJSONArray(ConfigTag.productVariantContextCode);
  }

  @Override public Collection<String> getPromotionalVersionContextCode()
  {
    return getJSONArray(ConfigTag.promotionalVersionContextCode);
  }

  @Override public long getClassifierIID()
  {
    return getLong(ConfigTag.classifierIID);
  }

  @Override
  public Collection<IConfigNatureRelationshipDTO> getRelationships() {
    return relationshipDTOS;
  }

  @Override
  public Map<String, IConfigEmbeddedRelationDTO> getEmbeddedClasses()
  {
    return embeddedRelationDTO;
  }

  @Override
  public Map<String, Collection<IConfigSectionElementDTO>> getSections() {
    return sections;
  }

  @Override
  public void setClassifierIID(long iid) {
    classifierDTO.setIID(iid);
    setLong(ConfigTag.classifierIID, iid);
  }
  
  @Override
  public List<String> getLevelCodes() {
	return levelCodes;
  }

  @Override
  public String getMasterParentTag() {
	return getString(ConfigTag.masterParentTag);
  }

  @Override
  public void setParentCode(String parentCode)
  {
    setString(ConfigTag.parentCode, parentCode);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setNumberOfVersionsToMaintain(int numberOfVersion)
  {
    setInt(ConfigTag.numberOfVersionsToMaintain, numberOfVersion);
  }

  @Override
  public void setIsDefault(boolean isDefault)
  {
    setBoolean(ConfigTag.isDefaultChild, isDefault);
  }

  @Override
  public void setIsAbstract(boolean isAbstract)
  {
    setBoolean(ConfigTag.isAbstract, isAbstract);
  }

  @Override
  public void setNatureType(String natureType)
  {
    setString(ConfigTag.natureType, natureType);    
  }

  @Override
  public void setIsNature(boolean isNature)
  {
    setBoolean(ConfigTag.isNature, isNature);    
  }

  @Override
  public void setContextCode(String ContextCode)
  {
    setString(ConfigTag.contextCode, ContextCode);
  }

  @Override
  public void setProductVariantContextCode(Collection<String> productVariantContextCode)
  {
    configData.setStringArrayField(ConfigTag.productVariantContextCode.toString(), productVariantContextCode);
  }

  @Override
  public void setPromotionalVersionContextCode(Collection<String> promotionalVersionContextCode)
  {
    configData.setStringArrayField(ConfigTag.promotionalVersionContextCode.toString(), promotionalVersionContextCode);
  }

  @Override
  public void setMasterParentTag(String masterParentTag)
  {
    setString(ConfigTag.masterParentTag, masterParentTag);
  }

  @Override
  public IClassifierDTO getClassifierDTO()
  {
    return classifierDTO;
  }

  @Override
  public void setSubType(String subType)
  {
    setString(ConfigTag.subType, subType);
  }
  
  @Override
  public String getSubType()
  {
    return getString(ConfigTag.subType);
  }
  
  @Override
  public void setEvents(Collection<String> events)
  {
    configData.setStringArrayField(ConfigTag.events.toString(), events);
  }

  @Override
  public void setTasks(Collection<String> tasks)
  {
    configData.setStringArrayField(ConfigTag.tasks.toString(), tasks);
  }

  @Override
  public void setStatusTag(Collection<String> statusTags)
  {
    configData.setStringArrayField(ConfigTag.statusTags.toString(), statusTags);
  }

  @Override
  public void setClassifierDTO(String code, ClassifierType type)
  {
    classifierDTO.setCode(code);
    classifierDTO.setClassifierType(type);
  }

  @Override
  public List<String> getLevelLables()
  {
    return levelLabels;
  }

  @Override
  public void setConfigTaxonomyType(String taxonomyType)
  {
     setString(ConfigTag.taxonomyType, taxonomyType);
  }

  @Override
  public String getConfigTaxonomyType()
  {
    return getString(ConfigTag.taxonomyType);
  }

  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }
  
  @Override
  public String getPreviewImage()
  {
    return getString(ConfigTag.previewImage);
  }

  @Override
  public void setPreviewImage(String previewImage)
  {
    setString(ConfigTag.previewImage, previewImage);
  }

  @Override
  public void setExtensionConfiguration(List<IJSONContent> extensionConfiguration)
  {
    configData.setField(ConfigTag.extensionConfiguration.name(), extensionConfiguration);
  }

  @Override
  public List<IJSONContent> getExtensionConfiguration()
  {
    return getJSONArray(ConfigTag.extensionConfiguration);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    List<Object> relationshipDTOs = new ArrayList<>();
    for(IConfigRelationshipDTO relationship : relationshipDTOS) {
      relationshipDTOs.add(relationship);
    }
    configData.setField(ConfigTag.relationships.toString(), relationshipDTOs);
    configData.setField(ConfigTag.levelCode.toString(), levelCodes);
    configData.setField(ConfigTag.levelLabels.toString(), levelLabels);
    configData.setField(ConfigTag.embeddedClasses.toString(), embeddedRelationDTO);
    configData.setField(ConfigTag.sections.toString(), sections);
    return super.toJSONBuffer();
  }

  @Override
  public int getLevelIndex()
  {
    return getInt(ConfigTag.levelIndex);
  }

  @Override
  public void setLevelIndex(int levelIndex)
  {
    setInt(ConfigTag.levelIndex, levelIndex);
  }

  @Override
  public List<Boolean> getIsNewlyCreated()
  {
    return getJSONArray(ConfigTag.isNewlyCreatedLevel);
  }

  @Override
  public void setIsNewlyCreated(List<Boolean> newlyCreated)
  {
    configData.setField(ConfigTag.isNewlyCreatedLevel.name(), newlyCreated);
  }
  
  public boolean isDetectDuplicate()
  {
    return getBoolean(ConfigTag.detectDuplicate);
  }
  
  public void isDetectDuplicate(boolean isDetectDuplicate)
  {
    setBoolean(ConfigTag.detectDuplicate, isDetectDuplicate);
  }
  
  public boolean isUploadZip()
  {
    return getBoolean(ConfigTag.uploadZip);
  }
  
  public void isUploadZip(boolean isUploadZip)
  {
    setBoolean(ConfigTag.uploadZip, isUploadZip);
  }
  
  public boolean isTrackDownloads()
  {
    return getBoolean(ConfigTag.trackDownloads);
  }
  
  public void isTrackDownloads(boolean isTrackDownloads)
  {
    setBoolean(ConfigTag.trackDownloads, isTrackDownloads);
  }
}
