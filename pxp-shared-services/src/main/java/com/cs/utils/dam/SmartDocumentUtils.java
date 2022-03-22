package com.cs.utils.dam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.DAMConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentAttributeInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentContextDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTagInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTagValueDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentAttributeInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentContextDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentTagInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentTagValueDataModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
@Component
public class SmartDocumentUtils {
  
  @Autowired
  protected IAssetServerDetailsModel assetServerDetails;
  
  public static ISmartDocumentKlassInstanceDataModel getSmartDocumentKlassInstance(IBaseEntityDTO articleInstance,
      Map<String, IAssetInformationModel> imageAttributeInstanceMap, IGetConfigDetailsForCustomTabModel articleConfigDetails,
      Map<String, String> userCodeLabelMap) throws Exception{
    
    ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel = new SmartDocumentKlassInstanceDataModel();
    smartTemplateKlassInstanceModel.setId(articleInstance.getBaseEntityID());
    smartTemplateKlassInstanceModel.setLabel(articleInstance.getBaseEntityName());
    
    fillAttribtesAndTags(articleInstance, articleConfigDetails, smartTemplateKlassInstanceModel, userCodeLabelMap);
    fillTypes(articleConfigDetails.getReferencedKlasses(), smartTemplateKlassInstanceModel);
    fillTaxonomies(articleInstance, articleConfigDetails.getReferencedTaxonomies(),
        smartTemplateKlassInstanceModel);
    fillInstanceImageInformation(imageAttributeInstanceMap, smartTemplateKlassInstanceModel);
    //TODO : Fetch Embedded Variant Data API
    //fillVariantContextInformation(articleInstance, articleConfigDetails, smartTemplateKlassInstanceModel);
    return smartTemplateKlassInstanceModel;
  }
  
  /**
   * This method fill attributes and tags in @param3 by looping over all the property
   * records of passed IBaseEntityDTO object.
   * 
   * @param baseEntityDTO
   * @param articleConfigDetails
   * @param smartTemplateKlassInstanceModel
   * @param userCodeLabelMap
   */
  private static void fillAttribtesAndTags(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForCustomTabModel articleConfigDetails,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel, Map<String, String> userCodeLabelMap)
  {
    baseEntityDTO.getPropertyRecords().stream()
      .forEach(propertyRecord -> {
          try {
            if (propertyRecord instanceof ITagsRecordDTO || PropertyType.BOOLEAN.equals(propertyRecord.getProperty()
                    .getPropertyType())) {
              fillTag((ITagsRecordDTO) propertyRecord, articleConfigDetails.getReferencedTags(), smartTemplateKlassInstanceModel);
            }
            else if (propertyRecord instanceof IValueRecordDTO) {
              IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
              fillAttribute(valueRecordDTO, articleConfigDetails.getReferencedAttributes(),
                  smartTemplateKlassInstanceModel, userCodeLabelMap);
            }
          }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
  }
  
  /**
   * This method prepares the ISmartDocumentAttributeInstanceDataModel for passed valueRecord.
   * @param valueRecord
   * @param referencedAttributes
   * @param smartTemplateKlassInstanceModel
   * @param userCodeLabelMap
   * @throws AttributeNotFoundException
   */
  
  private static void fillAttribute(IValueRecordDTO valueRecord,
      Map<String, IAttribute> referencedAttributes,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel,
      Map<String, String> userCodeLabelMap)
  {
    String attributeId = valueRecord.getProperty().getCode();
    IAttribute configAttribute = referencedAttributes.get(attributeId);
    if (configAttribute != null) {
      String value = valueRecord.getValue();
      if (attributeId.equals(SystemLevelIds.CREATED_BY_ATTRIBUTE) || attributeId.equals(SystemLevelIds.LAST_MODIFIED_BY_ATTRIBUTE)) {
        value = userCodeLabelMap.get(value);
      }
      String label = StringUtils.isEmpty(configAttribute.getLabel()) ? configAttribute.getCode()
          : configAttribute.getLabel();
      ISmartDocumentAttributeInstanceDataModel smartDocumentAttributeInstanceDataModel = new SmartDocumentAttributeInstanceDataModel();
      smartDocumentAttributeInstanceDataModel.setAttributeId(attributeId);
      smartDocumentAttributeInstanceDataModel.setValue(value);
      smartDocumentAttributeInstanceDataModel.setValueAsHtml(valueRecord.getAsHTML());
      smartDocumentAttributeInstanceDataModel
      .setValueAsNumber(valueRecord.getAsNumber());
      smartDocumentAttributeInstanceDataModel.setAttributeLabel(label);
      smartDocumentAttributeInstanceDataModel.setAttributeType(configAttribute.getType());
      if(CommonConstants.CALCULATED_ATTRIBUTE_TYPE.equals(configAttribute.getType())){
        String defaultUnit = ((ICalculatedAttribute)referencedAttributes.get(attributeId)).getCalculatedAttributeUnit();
        String calculatedAttributeType = ((ICalculatedAttribute)referencedAttributes.get(attributeId)).getCalculatedAttributeType();
        smartDocumentAttributeInstanceDataModel.setDefaultUnit(defaultUnit);
        smartDocumentAttributeInstanceDataModel.setAttributeType(calculatedAttributeType);
      }
      else if((CommonConstants.UNIT_ATTRIBUTE_TYPES).contains(configAttribute.getType())){
        String defaultUnit = ((IUnitAttribute)referencedAttributes.get(attributeId)).getDefaultUnit();
        smartDocumentAttributeInstanceDataModel.setDefaultUnit(defaultUnit);
      }
      smartTemplateKlassInstanceModel.getAttributes().put(configAttribute.getCode(), smartDocumentAttributeInstanceDataModel);
    }
  }
  
  /**
   * This method put the klassType, Nature klass label and non-nature klass labels in model.
   * @param referencedKlassMap
   * @param smartTemplateKlassInstanceModel
   */
  
  private static void fillTypes(Map<String, IReferencedKlassDetailStrategyModel> referencedKlassMap,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel)
  {
    String natureTypeLabel = null;
    List<String> nonNatureTypesLabel = new ArrayList<>();
    String klassType = null;
    
    for (String klass : referencedKlassMap.keySet()) {
      IReferencedKlassDetailStrategyModel referencedKlassDetail = referencedKlassMap.get(klass);
      if (referencedKlassDetail.getIsNature()) {
        klassType = referencedKlassDetail.getNatureType();
        natureTypeLabel = StringUtils.isEmpty(referencedKlassDetail.getLabel())
            ? referencedKlassDetail.getCode() : referencedKlassDetail.getLabel();
      }
      else {
        String label = StringUtils.isEmpty(referencedKlassDetail.getLabel()) ? referencedKlassDetail.getCode()
            : referencedKlassDetail.getLabel();
        nonNatureTypesLabel.add(label);
      }
    }
    smartTemplateKlassInstanceModel.setKlassType(klassType);
    smartTemplateKlassInstanceModel.setNatureKlassLabel(natureTypeLabel);
    smartTemplateKlassInstanceModel.setNonNatureKlassLabels(nonNatureTypesLabel);
  }
  
  /**
   * This method set the taxonomy and selected taxonomy labels in hierarchy.
   * @param baseEntityDTO
   * @param referencedTaxonomies
   * @param smartTemplateKlassInstanceModel
   */
  
  private static void fillTaxonomies(IBaseEntityDTO baseEntityDTO,
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel)
  {
    List<String> selectedTaxonomies = new ArrayList<>();
    List<List<String>> taxonomiesLabel = new ArrayList<>();
    List<String> selectedTaxonomiesLabel = new ArrayList<>();
    
    //fetch the taxonomy list from other classifier list.
    for (IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.TAXONOMY)
          || classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
        selectedTaxonomies.add(classifier.getClassifierCode());
      }
    }
    
    for (String selectedTaxonomy : selectedTaxonomies) {
      IReferencedArticleTaxonomyModel referencedSelectedTaxonomy = referencedTaxonomies
          .get(selectedTaxonomy);
      if (referencedSelectedTaxonomy != null) {
        List<String> taxonomyHierarchy = new ArrayList<>();
        String label = StringUtils.isEmpty(referencedSelectedTaxonomy.getLabel())
            ? referencedSelectedTaxonomy.getCode() : referencedSelectedTaxonomy.getLabel();
        taxonomyHierarchy.add(label);
        addParentList(referencedTaxonomies, referencedSelectedTaxonomy.getParent(),
            taxonomyHierarchy);
        Collections.reverse(taxonomyHierarchy);
        taxonomiesLabel.add(taxonomyHierarchy);
        selectedTaxonomiesLabel.add(label);
      }
    }
    
    smartTemplateKlassInstanceModel.setTaxonomyLabels(taxonomiesLabel);
    smartTemplateKlassInstanceModel.setSelectedTaxonomyLabels(selectedTaxonomiesLabel);
  }
  
  
  private static void addParentList(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies,
      IReferencedTaxonomyParentModel parentReferencedTaxonomy, List<String> taxonomyHierarchy)
  {
    if (!"-1".equals(parentReferencedTaxonomy.getId())) {
      String parentLabel = StringUtils.isEmpty(parentReferencedTaxonomy.getLabel())
          ? parentReferencedTaxonomy.getCode() : parentReferencedTaxonomy.getLabel();
      taxonomyHierarchy.add(parentLabel);
      addParentList(referencedTaxonomies, parentReferencedTaxonomy.getParent(), taxonomyHierarchy);
    }
  }
  

  /**
   * This method prepares the ISmartDocumentTagInstanceDataModel for passed tagRecord.
   * @param tagRecord
   * @param referencedTags
   * @return
   */

  private static void fillTag(ITagsRecordDTO tagRecord,
      Map<String, ITag> referencedTags, ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel)
  {
    String tagId = tagRecord.getProperty().getCode();
    ITag referencedTag = referencedTags.get(tagId);
    
    if (referencedTag != null) {
      Map<String, IIdLabelCodeModel> refrencedTagValuesIdLabelMap = new HashMap<>();
      for (ITag child : (List<ITag>) referencedTag.getChildren()) {
        IIdLabelCodeModel idLabelCodeModel = new IdLabelCodeModel();
        idLabelCodeModel.setLabel(child.getLabel());
        idLabelCodeModel.setCode(child.getCode());
        refrencedTagValuesIdLabelMap.put(child.getId(), idLabelCodeModel);
      }
      
      Map<String, ISmartDocumentTagValueDataModel> tagValues = new HashMap<>();
      ISmartDocumentTagInstanceDataModel smartDocumentTagInstanceDataModel = new SmartDocumentTagInstanceDataModel();
      String label = StringUtils.isEmpty(referencedTag.getLabel()) ? referencedTag.getCode() : referencedTag.getLabel();
      
      for (ITagDTO tagValue : tagRecord.getTags()) {
        if (tagValue.getRange() != 0) {
          ISmartDocumentTagValueDataModel tagValueModel = new SmartDocumentTagValueDataModel();
          IIdLabelCodeModel idLabelCodeModel = refrencedTagValuesIdLabelMap
              .get(tagValue.getTagValueCode());
          if (idLabelCodeModel != null) {
            String tagValueLabel = StringUtils.isEmpty(idLabelCodeModel.getLabel())
                ? idLabelCodeModel.getCode() : idLabelCodeModel.getLabel();
            tagValueModel.setLabel(tagValueLabel);
            tagValueModel.setRelevance(tagValue.getRange());
            tagValues.put(idLabelCodeModel.getCode(), tagValueModel);
          }
        }
      }
      
      smartDocumentTagInstanceDataModel.setTagGroupId(referencedTag.getId());
      smartDocumentTagInstanceDataModel.setTagGroupLabel(label);
      smartDocumentTagInstanceDataModel.setTagValues(tagValues);
      smartDocumentTagInstanceDataModel.setIsMultiselect(referencedTag.getIsMultiselect());
      smartTemplateKlassInstanceModel.getTags().put(referencedTag.getCode(), smartDocumentTagInstanceDataModel);
    }
  }
  
  
  /**
   * This method puts the previewImage and Image URL (with swift details in URL)
   * for all instances in model.
   * 
   * @param imageAttributeInstanceMap
   * @param smartTemplateKlassInstanceModel
   * @throws CSInitializationException
   * @throws PluginException
   */
  private static void fillInstanceImageInformation(Map<String, IAssetInformationModel> imageAttributeInstanceMap,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel) throws CSInitializationException, PluginException
  {
    IJSONContent serverDetails = CSDAMServer.instance().getServerInformation();
    String storageUrl = serverDetails.getInitField(DAMConstants.RESPONSE_HEADER_STORAGE_URL, "");
    String instanceId = smartTemplateKlassInstanceModel.getId();
    IAssetInformationModel imageAttributeInstance = imageAttributeInstanceMap.get(instanceId);
    if (imageAttributeInstance != null) {
      String imageUrl = "";
      String previewImageUrl = "";
      
      String previewImageKey = imageAttributeInstance.getPreviewImageKey();
      if (previewImageKey != null && !previewImageKey.isEmpty()) {
        previewImageUrl = storageUrl + DAMConstants.PATH_DELIMINATOR
            + CommonConstants.SWIFT_CONTAINER_IMAGE + DAMConstants.PATH_DELIMINATOR + previewImageKey;
      }
      
      String assetObjectKey = imageAttributeInstance.getAssetObjectKey();
      if (assetObjectKey != null && !assetObjectKey.isEmpty()) {
        if (CommonConstants.SWIFT_CONTAINER_IMAGE.equals(imageAttributeInstance.getType())) {
          imageUrl = storageUrl + DAMConstants.PATH_DELIMINATOR + imageAttributeInstance.getType()
              + DAMConstants.PATH_DELIMINATOR + assetObjectKey;
        }
      }
      
      smartTemplateKlassInstanceModel.setPreviewImageUrl(previewImageUrl);
      smartTemplateKlassInstanceModel.setImageUrl(imageUrl);
    }
  }
  
  /**
   * This method prepares the ISmartDocumentContextDataModel for context
   * information and put it into passed ISmartDocumentKlassInstanceDataModel
   * 
   * @param contextInstance
   * @param tags
   * @param contextRefrencedDetails
   * @param smartTemplateKlassInstanceModel
   */
  public static void fillContextInformation(IContextInstance contextInstance,
      Map<String, ISmartDocumentTagInstanceDataModel> tags,
      ISmartDocumentKlassInstanceDataModel smartTemplateKlassInstanceModel)
  {
    if (contextInstance != null) {

      ISmartDocumentContextDataModel context = new SmartDocumentContextDataModel();
      context.setContextId(contextInstance.getContextId());
      context.setLabel(contextInstance.getContextId());
      context.setTimeRange(contextInstance.getTimeRange());
      context.setLinkedInstances(contextInstance.getLinkedInstances()); 
      context.setTagInstanceIds(context.getTagInstanceIds());
      context.setId(context.getId());
      context.setTags(tags);
      
      smartTemplateKlassInstanceModel.setContext(context);
    }
  }
  
  /**
   * Put referenced tags against tagId
   * @param tagIds
   * @param referencedTags
   * @return
   */
  public static Map<String, ITag> getSelectedTags(List<String> tagIds, Map<String, ITag> referencedTags)
  {
    if (tagIds.isEmpty()) {
      return referencedTags;
    }
    
    Map<String, ITag> selectedTags = new HashMap<>();
    tagIds.forEach(tagId -> {
      ITag referencedTag = referencedTags.get(tagId);
      if (referencedTag != null) {
        selectedTags.put(tagId, referencedTag);
      }
    });
    
    return selectedTags;
  }

  /**
   * Put referenced attributes against attributeId
   * @param attributeIds
   * @param referencedAttributes
   * @return
   */
  public static Map<String, IAttribute> getSelectedAttributes(List<String> attributeIds,
      Map<String, IAttribute> referencedAttributes)
  {
    if (attributeIds.isEmpty()) {
      return referencedAttributes;
    }
    
    Map<String, IAttribute> selectedAttributes = new HashMap<>();
    attributeIds.forEach(attributeId -> {
      IAttribute referencedAttribute = referencedAttributes.get(attributeId);
      if (referencedAttribute != null) {
        selectedAttributes.put(attributeId, referencedAttribute);
      }
    });
    
    return selectedAttributes;
  }
  
  /**
   * Load attributes and tags property for smart document
   * @param baseEntityDAO
   * @param configDetailsForInstance
   * @param smartDocumentPreset
   * @return
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public static IBaseEntityDTO loadAttributesAndTagsPropertyForSmartDocument(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetailsForInstance,
      IGetSmartDocumentPresetModel smartDocumentPreset) throws RDBMSException, CSFormatException
  {
    Map<String, IAttribute> selectedAttributes = getSelectedAttributes(
        smartDocumentPreset.getAttributeIds(), configDetailsForInstance.getReferencedAttributes());
    Map<String, ITag> selectedTags = getSelectedTags(smartDocumentPreset.getTagIds(),
        configDetailsForInstance.getReferencedTags());
    List<IPropertyDTO> referenceAttributesTags = BaseEntityUtils.getReferenceAttributesTagsProperties(
        selectedAttributes, selectedTags, baseEntityDAO);

    IBaseEntityDTO baseEntityDTO = baseEntityDAO
        .loadPropertyRecords(referenceAttributesTags.toArray(new IPropertyDTO[referenceAttributesTags.size()]));
    return baseEntityDTO;
  }
  
  /**
   * Fill tags information in smartDocumentTagInstanceDataModel
   * @param tags
   * @param instanceTags
   */
  public static void fillTagsInformation(Map<String, ISmartDocumentTagInstanceDataModel> tags,
      List<? extends IContentTagInstance> instanceTags)
  {
    for (IContentTagInstance tag : instanceTags) {
      ISmartDocumentTagInstanceDataModel smartDocumentTagInstanceDataModel = new SmartDocumentTagInstanceDataModel();
      smartDocumentTagInstanceDataModel.setTagGroupId(tag.getTagId());
      Map<String, ISmartDocumentTagValueDataModel> tagValues = new HashMap<String, ISmartDocumentTagValueDataModel>();
      for (ITagInstanceValue tagValue : tag.getTagValues()) {
        ISmartDocumentTagValueDataModel tagValueDataModel = new SmartDocumentTagValueDataModel();
        tagValueDataModel.setLabel(tagValue.getId());
        tagValueDataModel.setRelevance(tagValue.getRelevance());
        tagValues.put(tagValue.getId(), tagValueDataModel);
      }
      smartDocumentTagInstanceDataModel.setTagValues(tagValues);
      tags.put(tag.getTagId(), smartDocumentTagInstanceDataModel);
    }
  }
  
  //TODO : Fetch Embedded Variant Data API
  /*public static void fillVariantContextInformation(IBaseEntityDTO articleInstance,
      IGetConfigDetailsForCustomTabModel configDetails, ISmartDocumentKlassInstanceDataModel smartDocumentKlassInstance)
  {
    fillContextInformation(articleInstance.getContextualObject(), smartDocumentKlassInstance.getTags(),
        configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts(), smartDocumentKlassInstance);
  }*/
  
  //TODO : Fetch Relationship Data API
  /*public static void fillRelationshipContextInformation(IRelationshipInstance relationshipInstance,
      IGetConfigDetailsForCustomTabModel configDetails, ISmartDocumentKlassInstanceDataModel smartDocumentKlassInstance)
  {
    fillContextInformation(relationshipInstance.getContext(), relationshipInstance.getTags(),
        configDetails.getReferencedVariantContexts().getRelationshipVariantContexts(),
        configDetails.getReferencedTags(), smartDocumentKlassInstance);
  }*/
}
