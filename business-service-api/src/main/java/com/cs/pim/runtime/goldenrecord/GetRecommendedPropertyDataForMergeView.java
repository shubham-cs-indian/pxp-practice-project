package com.cs.pim.runtime.goldenrecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.attribute.ConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.ConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.ConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedHtmlOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.goldenrecord.AttributeRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GRMergeViewHelperModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IAttributeRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGRMergeViewHelperModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ITagRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.TagRecommendationModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
public class GetRecommendedPropertyDataForMergeView {

	@Autowired
	protected RDBMSComponentUtils rdbmsComponentUtils;
	
	@Autowired
	GoldenRecordUtils goldenRecordUtil;
	
	private static final String SEPERATOR = "_";

	public void execute(IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
			IConfigDetailsForGetKlassInstancesToMergeModel configDetails,
      IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, List<IBaseEntityDTO> baseEntityDTOs, ILocaleCatalogDAO localeCatlogDAO) throws Throwable
  {
    String goldenRecordId = dataModel.getGoldenRecordId();
    
    IGetConfigDetailsModel configDetailsForKlassInstanceBuilder = goldenRecordUtil.prepareConfigRequestmodelForKlassInstanceBuilder(configDetails);
    
    if(goldenRecordId != null) {
      IBaseEntityDTO entityByID = localeCatlogDAO.getEntityByIID(Long.parseLong(goldenRecordId));
      IContentInstance goldenRecordInstance = getKlassInstanceFromBaseEntityDAO(configDetailsForKlassInstanceBuilder, entityByID);
      responseModel.setGoldenRecordInstance(goldenRecordInstance);
    }
    
    IMergeEffect mergeEffect = dataModel.getMergeEffect();
    List<IMergeEffectType> mergeEffectAttributes = mergeEffect != null ? mergeEffect.getAttributes() : new ArrayList<IMergeEffectType>();
    List<IMergeEffectType> mergeEffectTags = mergeEffect != null ? mergeEffect.getTags() : new ArrayList<IMergeEffectType>();

    IGRMergeViewHelperModel helperModel = new GRMergeViewHelperModel();
    
    fillAttributesAndTagsMapForRecommendation(baseEntityDTOs, configDetailsForKlassInstanceBuilder,
        mergeEffectAttributes, mergeEffectTags, helperModel, dataModel, responseModel, configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    
    Map<String, IRecommendationModel> recommendation = new HashMap<>();
    
    if(dataModel.getIsAutoCreate()) {
      fillRecommendationForAttributesAccordingToMergeEffect(dataModel.getDependentAttributeIds(),
          mergeEffectAttributes, recommendation, helperModel, responseModel, dataModel.getAllLocales());
    }
    
    else {
      fillRecommendationForAttributesAccordingToMergeEffect(mergeEffectAttributes, helperModel, recommendation);
    }

    fillRecommendationForTagsAccordingToMergeEffect(mergeEffectTags, helperModel, recommendation);
    responseModel.setRecommendation(recommendation);
  }

  @SuppressWarnings("unchecked")
  private void fillRecommendationForAttributesAccordingToMergeEffect(
      List<String> dependentAttributeIds, List<IMergeEffectType> mergeEffectAttributes,
      Map<String, IRecommendationModel> recommendation, IGRMergeViewHelperModel helperModel,
      IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, List<String> allLocales)
  {
    Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes = helperModel.getLastModifiedAttributes();
    Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes = helperModel.getLastModifiedDependentAttributes();
    
    Map<String, List<IAttributeRecommendationModel>> supplierAttributes = helperModel.getSupplierAttributes();
    Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes = helperModel.getSupplierDependentAttributes();
    
    Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation = helperModel.getDependentAttributesRecommendation();
    
    for(IMergeEffectType mergeEffectAttribute : mergeEffectAttributes) {
      String id = mergeEffectAttribute.getEntityId();
      
      if(dependentAttributeIds.contains(id)) {
        if(mergeEffectAttribute.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
          handleForSupplier(allLocales, supplierDependentAttributes, dependentAttributeRecommendation, mergeEffectAttribute, id);
        }
        else {
          handleForLastModified(allLocales, lastModifiedDependentAttributes, dependentAttributeRecommendation, id);
        }
      }
      
      else {
        if(mergeEffectAttribute.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
          fillAllRecommendationOnAttributeBySupplier(supplierAttributes, recommendation, mergeEffectAttribute, id);
        }
        else {
          List<IAttributeRecommendationModel> recommendedAttributes = lastModifiedAttributes.get(id);
          fillAllRecommendationOnAttributeByLastmodified(recommendedAttributes, id, recommendation);
        }
      }
    }
    responseModel.setDependentAttributeRecommendation(dependentAttributeRecommendation);

  }

  private void handleForLastModified(List<String> allLocales,
      Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes,
      Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation, String id)
  {
    for(String locale : allLocales) {
      Map<String, List<IAttributeRecommendationModel>> lastModifiedDependentAttributeAsPerLangauge = lastModifiedDependentAttributes.get(locale);
      List<IAttributeRecommendationModel> recommendedAttribute = lastModifiedDependentAttributeAsPerLangauge.get(id);
      if(recommendedAttribute == null) {
        continue;
      }
      Map<String, IRecommendationModel> dependentAttributeRecommendationAsPerLanguage = getDependentAttributeRecommendationAsPerLanguage(dependentAttributeRecommendation, locale);
      fillAllRecommendationOnAttributeByLastmodified(recommendedAttribute, id, dependentAttributeRecommendationAsPerLanguage);
    }
  }

  private void handleForSupplier(List<String> allLocales,
      Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes,
      Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation,
      IMergeEffectType mergeEffectAttribute, String id)
  {
    for(String locale : allLocales) {
      Map<String, List<IAttributeRecommendationModel>> supplierDependentAttributeAsPerLangauge = supplierDependentAttributes.get(locale);
      Map<String, IRecommendationModel> dependentAttributeRecommendationAsPerLanguage = getDependentAttributeRecommendationAsPerLanguage(dependentAttributeRecommendation, locale);
      fillAllRecommendationOnAttributeBySupplier(supplierDependentAttributeAsPerLangauge, dependentAttributeRecommendationAsPerLanguage, mergeEffectAttribute, id);
    }
  }
  
  private Map<String, IRecommendationModel> getDependentAttributeRecommendationAsPerLanguage(
      Map<String, Map<String, IRecommendationModel>> dependentAttributeRecommendation,
      String languageCode)
  {
    Map<String, IRecommendationModel> dependentAttributeRecommendationAsPerLanguage = dependentAttributeRecommendation.get(languageCode);
    if(dependentAttributeRecommendationAsPerLanguage == null) {
      dependentAttributeRecommendationAsPerLanguage = new HashMap<>();
      dependentAttributeRecommendation.put(languageCode, dependentAttributeRecommendationAsPerLanguage);
    }
    return dependentAttributeRecommendationAsPerLanguage;
  }

  @SuppressWarnings("unchecked")
  private void fillAttributesAndTagsMapForRecommendation(List<IBaseEntityDTO> baseEntitiesDTO,
      IGetConfigDetailsModel configDetailsForKlassInstanceBuilder, List<IMergeEffectType> mergeEffectAttributes,
      List<IMergeEffectType> mergeEffectTags,
      IGRMergeViewHelperModel helperModel,
      IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
      IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags) throws Exception
  
  {
    List<String> dependentAttributesBySupplier = new ArrayList<>();
    List<String> independentAttributesBySupplier = new ArrayList<>();
    List<String> dependentAttributesByLastModified = new ArrayList<>();
    List<String> independentAttributesByLastModified = new ArrayList<>();
    List<String> tagsBySupplier = new ArrayList<>();
    List<String> tagsByLastmodified = new ArrayList<>();
    
    Map<String, List<ITagRecommendationModel>> lastModifiedTags = helperModel.getLastModifiedTags();
    Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes = helperModel.getLastModifiedAttributes();
    Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes = helperModel.getLastModifiedDependentAttributes();
    
    Map<String, List<ITagRecommendationModel>> supplierTags = helperModel.getSupplierTags();
    Map<String, List<IAttributeRecommendationModel>> supplierAttributes = helperModel.getSupplierAttributes();
    Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes = helperModel.getSupplierDependentAttributes();
    
    List<String> allLocales = dataModel.getAllLocales();
    Set<String> propertyCodes = new HashSet<>();

    fillAttributeIds(mergeEffectAttributes, dependentAttributesBySupplier,
        independentAttributesBySupplier, dependentAttributesByLastModified,
        independentAttributesByLastModified, dataModel.getDependentAttributeIds(), propertyCodes);
    
    fillTagIds(mergeEffectTags, tagsBySupplier, tagsByLastmodified, propertyCodes);
    
    Set<IPropertyDTO> properties = getPropertyDTOsFromPropertyCodes(propertyCodes);

    if(dataModel.getIsAutoCreate()) {
      goldenRecordUtil.fillMatchPropertiesModelAndRule(responseModel, dataModel.getBucketInstanceId());
    }
    
    for (IBaseEntityDTO baseEntityDTO : baseEntitiesDTO) {
      List<IValueRecordDTO> attributes = new ArrayList<>();
      List<ITagsRecordDTO> tags = new ArrayList<>();
      
      String klassInstanceId = String.valueOf(baseEntityDTO.getBaseEntityIID());
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openBaseEntity(baseEntityDTO);
      baseEntityDTO = baseEntityDAO.loadPropertyRecords(properties.toArray(new IPropertyDTO[0]));
      
      for(IPropertyRecordDTO propertyRecord : baseEntityDTO.getPropertyRecords()) {
        
        if(propertyRecord instanceof IValueRecordDTO)
            attributes.add((IValueRecordDTO)propertyRecord);
        
        else if( propertyRecord instanceof ITagsRecordDTO)
            tags.add((ITagsRecordDTO)propertyRecord);
      }
      
      Map<String, Long> propertyVsLastModified = fillLastModifiedValuesForAttributesAndTags(attributes, tags, baseEntityDTO.getBaseEntityIID(),
          independentAttributesByLastModified, dependentAttributesByLastModified, tagsByLastmodified);
      
      String supplierId = baseEntityDTO.getSourceOrganizationCode();
      
      if (!attributes.isEmpty()) {
        List<IValueRecordDTO> languageInstanceAttribute = getDependentAttributeFromLanguageInstance(dataModel.getLanguageCode(), attributes);
        attributes.removeAll(languageInstanceAttribute);
       
        fillAllRecommendationForAttributes(independentAttributesBySupplier, independentAttributesByLastModified,
            supplierAttributes, lastModifiedAttributes, attributes, supplierId, klassInstanceId, propertyVsLastModified,
            referencedAttributes, referencedTags, baseEntityDTO);
        
        if(dataModel.getIsAutoCreate()) {
          fillDependentAttributesForAutoCreate(supplierDependentAttributes,
              lastModifiedDependentAttributes, allLocales,
              dependentAttributesBySupplier, dependentAttributesByLastModified, baseEntityDTO, referencedAttributes, referencedTags);
        }
       
        else {
        fillAllRecommendationForAttributes(dependentAttributesBySupplier, dependentAttributesByLastModified,
            supplierAttributes, lastModifiedAttributes, languageInstanceAttribute, supplierId, klassInstanceId,
            propertyVsLastModified, referencedAttributes, referencedTags, baseEntityDTO);
        }
      }
      
      if(!tags.isEmpty()) 
        fillAllRecommendationForTags(tagsBySupplier, tagsByLastmodified, supplierTags, lastModifiedTags, tags,
            supplierId, klassInstanceId, propertyVsLastModified);
    }
  }
  
  private Set<IPropertyDTO> getPropertyDTOsFromPropertyCodes(Set<String> propertyCodes) throws RDBMSException
  {
    Set<IPropertyDTO> properties = new HashSet<>();
    for(String propertyCode : propertyCodes) {
      properties.add(RDBMSUtils.newConfigurationDAO().getPropertyByCode(propertyCode));
    }
    return properties;
  }

  private void fillDependentAttributesForAutoCreate(
      Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes,
      Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes,
      List<String> allLocales, List<String> dependentAttributesBySupplier,
      List<String> dependentAttributesByLastModified, IBaseEntityDTO baseEntityDTO, Map<String, IAttribute> referencedAttributes, Map<String, ITag> referencedTags)
      throws Exception
  {
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    String supplierId = baseEntityDTO.getSourceOrganizationCode();
    String instanceId = String.valueOf(baseEntityDTO.getBaseEntityIID());
    
    Map<String, List<IValueRecordDTO>> localeVsValueRecords = getLocaleVsValueRecords(baseEntityIID);
    
    for(String locale : localeVsValueRecords.keySet()) {
      List<IValueRecordDTO> attributes = localeVsValueRecords.get(locale);
      Map<String, List<IAttributeRecommendationModel>> supplierDependentAttributesOfLocale = getDependentAttributeAsPerLanguage(supplierDependentAttributes, locale);
      Map<String, List<IAttributeRecommendationModel>> lastModifiedDependentAttributesOfLocale = getDependentAttributeAsPerLanguage(lastModifiedDependentAttributes, locale);
     
      Map<String, Long> propertyVsLastModified = fillLastModifiedValuesForAttributesAndTags(
          attributes, new ArrayList<>(), baseEntityDTO.getBaseEntityIID(), new ArrayList<>(),
          dependentAttributesByLastModified, new ArrayList<>());
      
      fillAllRecommendationForAttributes(dependentAttributesBySupplier,
          dependentAttributesByLastModified, supplierDependentAttributesOfLocale,
          lastModifiedDependentAttributesOfLocale, attributes, supplierId, instanceId, propertyVsLastModified, referencedAttributes, referencedTags, baseEntityDTO);
    }
  }

  private Map<String, List<IValueRecordDTO>> getLocaleVsValueRecords(Long baseEntityIID) throws RDBMSException
  {
    Map<Long, List<IValueRecordDTO>> allValueRecords = rdbmsComponentUtils.getLocaleCatlogDAO().getAllValueRecords(baseEntityIID);
    List<IValueRecordDTO> valueRecords = allValueRecords.get(baseEntityIID);
    
    Map<String, List<IValueRecordDTO>> localeVsValueRecords = new HashMap<>();
    
    for(IValueRecordDTO valueRecord : valueRecords) {
      String locale = valueRecord.getLocaleID();
      if(!locale.isEmpty()) {
        List<IValueRecordDTO> records = localeVsValueRecords.get(locale);
        if(records == null) {
          records = new ArrayList<>();
          localeVsValueRecords.put(locale, records);
        }
        records.add(valueRecord);
      }
    }
    return localeVsValueRecords;
  }
  
  private Map<String, List<IAttributeRecommendationModel>> getDependentAttributeAsPerLanguage(
      Map<String, Map<String, List<IAttributeRecommendationModel>>> dependentAttributesForSupplierPriorityMap, String languageCode)
  {
    Map<String, List<IAttributeRecommendationModel>> dependentAttributeMapAsPerLanguage = dependentAttributesForSupplierPriorityMap.get(languageCode);
    if(dependentAttributeMapAsPerLanguage == null) {
      dependentAttributeMapAsPerLanguage = new HashMap<>();
      dependentAttributesForSupplierPriorityMap.put(languageCode, dependentAttributeMapAsPerLanguage);
    }
    return dependentAttributeMapAsPerLanguage;
  }

  private IContentInstance getKlassInstanceFromBaseEntityDAO(IGetConfigDetailsModel config, IBaseEntityDTO baseEntityDTO) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, config,
        rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    return (IContentInstance) klassInstance;
  }

	@SuppressWarnings("unchecked")
  private void fillRecommendationForAttributesAccordingToMergeEffect(List<IMergeEffectType> mergeEffectAttributes,
			IGRMergeViewHelperModel helperModel,
			Map<String, IRecommendationModel> recommendation) {
	  
	  Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes = helperModel.getLastModifiedAttributes();
    Map<String, List<IAttributeRecommendationModel>> supplierAttributes = helperModel.getSupplierAttributes();
    
		for (IMergeEffectType mergeEffect : mergeEffectAttributes) {
			String entityId = (String) mergeEffect.getEntityId();
			if (mergeEffect.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
			  if (mergeEffect.getEntityType().equals(CommonConstants.ATTRIBUTES)) {
					fillAllRecommendationOnAttributeBySupplier(supplierAttributes, recommendation,mergeEffect, entityId);
				}
			} 
			
			else {
				List<IAttributeRecommendationModel> recommendedEntities = lastModifiedAttributes.get(entityId);
				fillAllRecommendationOnAttributeByLastmodified(recommendedEntities, entityId, recommendation);
			}
		}
	}

	@SuppressWarnings("unchecked")
  private void fillRecommendationForTagsAccordingToMergeEffect(List<IMergeEffectType> mergeEffectAttributes,
			IGRMergeViewHelperModel helperModel,
			Map<String, IRecommendationModel> recommendation) {
	  
	  Map<String, List<ITagRecommendationModel>> lastModifiedTags = helperModel.getLastModifiedTags();
    Map<String, List<ITagRecommendationModel>> supplierTags = helperModel.getSupplierTags();
    
		for (IMergeEffectType mergeEffect : mergeEffectAttributes) {
			String entityId = (String) mergeEffect.getEntityId();
			if (mergeEffect.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
				if (mergeEffect.getEntityType().equals(CommonConstants.TAGS)) {
					fillAllRecommendationOnTagBySupplier(supplierTags, recommendation, mergeEffect, entityId);
				}
			} 
			else {
				List<ITagRecommendationModel> recommandedEntities = lastModifiedTags.get(entityId);
				fillAllRecommendationOnTagByLastmodified(recommandedEntities, entityId, recommendation);
			}
		}
	}

	private List<IValueRecordDTO> getDependentAttributeFromLanguageInstance(String language, List<IValueRecordDTO> attributes)
  {
    return attributes.stream().filter(attr -> attr.getLocaleID().equals(language)).collect(Collectors.toList());
  }
	
	private void fillAllRecommendationOnAttributeByLastmodified(
			List<IAttributeRecommendationModel> recommandedEntities, String propertyId,
			Map<String, IRecommendationModel> recommendation) {
		if (recommandedEntities == null) {
			return;
		}

		Optional<IAttributeRecommendationModel> filterRecommandedProperty = recommandedEntities.stream()
				.max(Comparator.comparingLong(property -> (Long) property.getLastModified()));

		if (filterRecommandedProperty.isPresent()) {
			IAttributeRecommendationModel recommandedProperty = filterRecommandedProperty.get();
			recommendation.put(propertyId, recommandedProperty);
		}

	}

	private void fillAllRecommendationOnTagByLastmodified(List<ITagRecommendationModel> recommandedEntities,
			String propertyId, Map<String, IRecommendationModel> recommendation) {
		if (recommandedEntities == null) {
			return;
		}

		Optional<ITagRecommendationModel> filterRecommandedProperty = recommandedEntities.stream()
				.max(Comparator.comparingLong(property -> (Long) property.getLastModified()));

		if (filterRecommandedProperty.isPresent()) {
			ITagRecommendationModel recommandedProperty = filterRecommandedProperty.get();
			recommendation.put(propertyId, recommandedProperty);
		}

	}

	private void fillAllRecommendationOnAttributeBySupplier(
			Map<String, List<IAttributeRecommendationModel>> supplierAttributes,
			Map<String, IRecommendationModel> recommendation, IMergeEffectType mergeEffect, String attributeId) {
		List<String> supplierIds = (List<String>) mergeEffect.getSupplierIds();
		
		if(supplierAttributes == null) {
		  return;
		}
		
    for (String supplierId : supplierIds) {
      
      if(supplierId.equals(Constants.STANDARD_ORGANIZATION)) {
        supplierId = IStandardConfig.STANDARD_ORGANIZATION_RCODE;
      }
      
			String id = supplierId + SEPERATOR + attributeId;
			
			if (supplierAttributes.containsKey(id)) {
				List<IAttributeRecommendationModel> recommandedAttributes = (List<IAttributeRecommendationModel>) supplierAttributes
						.get(id);

				Optional<IAttributeRecommendationModel> findFirst = recommandedAttributes.stream()
						.filter(attribute -> !attribute.getValue().isEmpty()).findFirst();

				IRecommendationModel recommendedAttribute = new AttributeRecommendationModel();
				if (findFirst.isPresent()) {
					recommendedAttribute = findFirst.get();
				} else {
					recommendedAttribute = recommandedAttributes.get(0);
				}
				recommendation.put(attributeId, recommendedAttribute);
				break;
			}
		}
	}

	private void fillAllRecommendationOnTagBySupplier(
			Map<String, List<ITagRecommendationModel>> attributesForSupplierPriorityMap,
			Map<String, IRecommendationModel> recommendation, IMergeEffectType mergeEffect, String tagId) {
		List<String> supplierIds = (List<String>) mergeEffect.getSupplierIds();
		for (String supplierId : supplierIds) {
			String id = supplierId + SEPERATOR + tagId;
			if (attributesForSupplierPriorityMap.containsKey(id)) {
				List<ITagRecommendationModel> recommandedTags = attributesForSupplierPriorityMap.get(id);

				@SuppressWarnings("unchecked")
				Optional<ITagRecommendationModel> findFirst = recommandedTags.stream()
						.filter(tag -> !tag.getTagValues().isEmpty())
						.filter(tag -> isContainNonZeroRelevanceTagValue(
								(List<Map<String, Object>>) ((Map<String, Object>) tag).get(ITagInstance.TAG_VALUES)))
						.findFirst();

				ITagRecommendationModel recommandedTag = new TagRecommendationModel();
				if (findFirst.isPresent()) {
					recommandedTag = findFirst.get();
				} else {
					recommandedTag = recommandedTags.get(0);
				}
				recommendation.put(tagId, recommandedTag);
				break;
			}
		}
	}

	private Boolean isContainNonZeroRelevanceTagValue(List<Map<String, Object>> list) {
		return list.stream().filter(tagValue -> !tagValue.get(ITagValue.RELEVANCE).equals(0)).findFirst().isPresent();
	}

  private void fillAllRecommendationForTags(List<String> tagsBySupplier,
      List<String> tagsByLastModified, Map<String, List<ITagRecommendationModel>> supplierTags,
      Map<String, List<ITagRecommendationModel>> lastModifiedTags, List<ITagsRecordDTO> tags,
      String supplierId, String klassInstanceId, Map<String, Long> propertyVsLastModified)
  {
	  tags.forEach(tag -> {
			String tagId = (String) tag.getProperty().getCode();
			String id = supplierId + SEPERATOR + tagId;
			ITagRecommendationModel recommendedPropertyMapForTag = getRecommendedPropertyMapForTag(tag, supplierId,
					klassInstanceId, propertyVsLastModified, tagId);
			
			if (tagsByLastModified.contains(tagId)) {
				fillTagForRecommendation(lastModifiedTags, recommendedPropertyMapForTag, tagId);
			} 
			else if (tagsBySupplier.contains(tagId)) {
				fillTagForRecommendation(supplierTags, recommendedPropertyMapForTag, id);
			}
			
		});
	}

  private void fillAllRecommendationForAttributes(List<String> independentAttributesBySupplier,
      List<String> independentAttributesByLastModified, Map<String, List<IAttributeRecommendationModel>> supplierAttributes,
      Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes, List<IValueRecordDTO> attributes, String supplierId,
      String klassInstanceId, Map<String, Long> propertyVsLastModified, Map<String, IAttribute> referencedAttributes,
      Map<String, ITag> referencedTags, IBaseEntityDTO baseEntityDTO)
  {
    attributes.forEach(attribute -> {
      String attributeId = attribute.getProperty().getCode();
      String id = supplierId + "_" + attributeId;
    	
			IAttributeRecommendationModel recommendedPropertyMapForAtttribute = getRecommendedPropertyMapForAtttribute(
					attribute, supplierId, klassInstanceId, propertyVsLastModified, referencedAttributes, referencedTags, baseEntityDTO);
			if (independentAttributesByLastModified.contains(attributeId)) {
				fillAttributeForRecommendation(lastModifiedAttributes, recommendedPropertyMapForAtttribute,
						attributeId);
			} else if (independentAttributesBySupplier.contains(attributeId)) {
				fillAttributeForRecommendation(supplierAttributes, recommendedPropertyMapForAtttribute,
						id);
			}
		});
	}

  private ITagRecommendationModel getRecommendedPropertyMapForTag(ITagsRecordDTO tag, String supplierId, String klassInstanceId,
      Map<String, Long> propertyVsLastModified, String tagId)
  {
    List<IIdRelevance> tagValues = getTagValueAsIdRelevence(tag);
    Long lastModified = propertyVsLastModified.containsKey(tagId) ? propertyVsLastModified.get(tagId) : 0L;
    ITagRecommendationModel tagRecommendation = new TagRecommendationModel();
    tagRecommendation.setcontentId(klassInstanceId);
    tagRecommendation.setTagValues(tagValues);
    tagRecommendation.setPropertyType(CommonConstants.TAG);
    tagRecommendation.setcontentId(klassInstanceId);
    tagRecommendation.setSupplierId(supplierId);
    tagRecommendation.setLastModified(lastModified);
    
    return tagRecommendation;
  }
  
  private List<IIdRelevance> getTagValueAsIdRelevence(ITagsRecordDTO tag)
  {
    Set<ITagDTO> values = tag.getTags();
    List<IIdRelevance> newTagValues = new ArrayList<>();
    
    values.stream().filter(value -> !(value.getRange() == 0)).forEach(value -> {
      IIdRelevance idRelevence = new IdRelevance();
      idRelevence.setTagId(value.getTagValueCode());
      idRelevence.setRelevance(value.getRange());
      newTagValues.add(idRelevence);
    });
    
    return newTagValues;
  }

  private IAttributeRecommendationModel getRecommendedPropertyMapForAtttribute(IValueRecordDTO attribute, String supplierId,
      String klassInstanceId, Map<String, Long> propertyVsLastModified, Map<String, IAttribute> referencedAttributes,
      Map<String, ITag> referencedTags, IBaseEntityDTO baseEntityDTO)
  {
    String attributeId = attribute.getProperty().getCode();
    String value = attribute.getValue();
    String valueAsHtml = attribute.getAsHTML();
    List<IConcatenatedOperator> valueAsExpression = attribute.getCalculation().isEmpty() ? new ArrayList<>()
        : getValueAsExpression(attribute.getCalculation(), referencedAttributes, referencedTags, baseEntityDTO, attributeId);
    Long lastModified;
    String localeId = attribute.getLocaleID();
    if (localeId.isEmpty()) {
      lastModified = propertyVsLastModified.containsKey(attributeId) ? propertyVsLastModified.get(attributeId) : 0L;
    }
    else {
      String code = attributeId + "_" + localeId;
      lastModified = propertyVsLastModified.containsKey(code) ? propertyVsLastModified.get(code) : 0L;
    }
    
    IAttributeRecommendationModel attributeRecommendations = new AttributeRecommendationModel();
    attributeRecommendations.setValue(value);
    attributeRecommendations.setValueAsHtml(valueAsHtml);
    attributeRecommendations.setValueAsExpression(valueAsExpression);
    attributeRecommendations.setPropertyId(attributeId);
    attributeRecommendations.setPropertyType(CommonConstants.ATTRIBUTE);
    attributeRecommendations.setcontentId(klassInstanceId);
    attributeRecommendations.setSupplierId(supplierId);
    attributeRecommendations.setLastModified(lastModified);
    
    return attributeRecommendations;
  }

  private List<IConcatenatedOperator> getValueAsExpression(String expression, Map<String, IAttribute> referencedAttributes,
      Map<String, ITag> referencedTags, IBaseEntityDTO baseEntityDTO, String attributeId)
  {
    List<IConcatenatedOperator> attributeConcatenatedList = ((IConcatenatedAttribute) referencedAttributes.get(attributeId))
        .getAttributeConcatenatedList();
    
    Map<Integer, String> userTextMap = new HashMap<>();
    for (IConcatenatedOperator attributeConcatenated : attributeConcatenatedList) {
      if (attributeConcatenated.getType().equals("html")) {
        userTextMap.put(attributeConcatenated.getOrder(), attributeConcatenated.getCode());
      }
    }
    
    String expressionIfExist = expression.replace("{", "").replace("}", "");
    List<IConcatenatedOperator> valueAsExpression = new ArrayList<>();
    
    if (!expressionIfExist.isEmpty()) {
      String[] expressionSplit = expressionIfExist.split("=", 2);
      String expressionAfterEqual = expressionSplit[1];
      String expressionRemovalOfSpecialChar = null;
      int order = 0;
      
      if (expressionAfterEqual.contains("[") || expressionAfterEqual.contains("'") || expressionAfterEqual.contains("}")) {
        expressionRemovalOfSpecialChar = expressionAfterEqual.replace("[", "").replace("]", "").replace("\"", "").replace("}", "");
      }
      expressionRemovalOfSpecialChar = StringEscapeUtils.unescapeHtml4(expressionRemovalOfSpecialChar);
      String[] expressionSubStrings = expressionRemovalOfSpecialChar.split("\\|\\|");
      for (String expressionSubString : expressionSubStrings) {
        long propertyIID = 0;
        IAttribute attributeValue = null;
        ITag iTag = null;
        expressionSubString = expressionSubString.replace("'", "");
        
        if (referencedAttributes.containsKey(expressionSubString)) {
          attributeValue = referencedAttributes.get(expressionSubString);
          propertyIID = attributeValue.getPropertyIID();
        }
        else if (referencedTags.containsKey(expressionSubString)) {
          iTag = referencedTags.get(expressionSubString);
          propertyIID = iTag.getPropertyIID();
        }
        
        if (propertyIID != 0) {
          ConcatenatedAttributeOperator concatenatedOperator = new ConcatenatedAttributeOperator();
          IPropertyRecordDTO propertyRecord = baseEntityDTO.getPropertyRecord(propertyIID);
          
          if (propertyRecord != null && propertyRecord instanceof IValueRecordDTO) {
            IValueRecordDTO ValueRecordDto = (IValueRecordDTO) propertyRecord;
            concatenatedOperator.setCode(expressionSubString);
            concatenatedOperator.setId(Long.toString(ValueRecordDto.getProperty().getPropertyIID()));
            concatenatedOperator.setAttributeId(attributeValue.getId());
            concatenatedOperator.setType("attribute");
            concatenatedOperator.setOrder(order);
            valueAsExpression.add(concatenatedOperator);
          }
          else if (propertyRecord != null && propertyRecord instanceof ITagsRecordDTO) {
            ConcatenatedTagOperator concatenatedTagOperator = new ConcatenatedTagOperator();
            ITagsRecordDTO tagRecordDto = (ITagsRecordDTO) propertyRecord;
            concatenatedTagOperator.setCode(expressionSubString);
            concatenatedTagOperator.setId(tagRecordDto.getNodeID());
            concatenatedTagOperator.setTagId(iTag.getId());
            concatenatedTagOperator.setType("tag");
            concatenatedTagOperator.setOrder(order);
            valueAsExpression.add(concatenatedTagOperator);
          }
        }
        else {
          IConcatenatedHtmlOperator concatenatedOperator = new ConcatenatedHtmlOperator();
          concatenatedOperator.setValue(Jsoup.parse(expressionSubString).text());
          concatenatedOperator.setType("html");
          concatenatedOperator.setValueAsHtml(expressionSubString);
          concatenatedOperator.setOrder(order);
          String id = userTextMap.get(order);
          concatenatedOperator.setId(id);
          concatenatedOperator.setCode(id);
          
          valueAsExpression.add(concatenatedOperator);
        }
        order++;
      }
    }
    
    return valueAsExpression;
  }

	private void fillAttributeForRecommendation(
			Map<String, List<IAttributeRecommendationModel>> attributes,
			IAttributeRecommendationModel recommendedAttribute, String id) {
	  
		List<IAttributeRecommendationModel> propertiesForRecommendation = attributes.get(id);
		if (propertiesForRecommendation == null) {
			propertiesForRecommendation = new ArrayList<>();
			attributes.put(id, propertiesForRecommendation);
		}
		propertiesForRecommendation.add(recommendedAttribute);
	}

	private void fillTagForRecommendation(Map<String, List<ITagRecommendationModel>> attributesForLastmodifiedMap,
			ITagRecommendationModel recommendedPropertyMapForAtttribute, String id) {
		List<ITagRecommendationModel> propertiesForRecommendation = attributesForLastmodifiedMap.get(id);
		if (propertiesForRecommendation == null) {
			propertiesForRecommendation = new ArrayList<>();
			attributesForLastmodifiedMap.put(id, propertiesForRecommendation);
		}
		propertiesForRecommendation.add(recommendedPropertyMapForAtttribute);
	}

	private void fillTagIds(List<IMergeEffectType> mergeEffectTags, List<String> tagsBySupplier,
			List<String> tagsByLastModified, Set<String> allProperties) {
		mergeEffectTags.forEach(tag -> {
			String tagId = (String) tag.getEntityId();
			if (tag.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
				tagsBySupplier.add(tagId);
			} else {
				tagsByLastModified.add(tagId);
			}
			allProperties.add(tagId);
		});
	}

  private void fillAttributeIds(List<IMergeEffectType> mergeEffectAttributes,
      List<String> dependentAttributeBySupplier, List<String> independentAttributeBySupplier,
      List<String> dependentAttributesByLastModified,
      List<String> independentAttributesByLastModified, List<String> dependentAttribteIds,
      Set<String> propertyCodes)
  {

		mergeEffectAttributes.forEach(attribute -> {

			String attributeId = (String) attribute.getEntityId();
			Boolean isDependent = false;
			if (dependentAttribteIds.contains(attributeId)) {
				isDependent = true;
			}

			if (attribute.getType().equals(CommonConstants.SUPPLIER_PRIORITY)) {
				if (isDependent) {
					dependentAttributeBySupplier.add(attributeId);
				} else {
					independentAttributeBySupplier.add(attributeId);
				}
			} else {
				if (isDependent) {
					dependentAttributesByLastModified.add(attributeId);
				} else {
					independentAttributesByLastModified.add(attributeId);
				}
			}
			propertyCodes.add(attributeId);
		});
	}
	
  private Map<String, Long> fillLastModifiedValuesForAttributesAndTags(List<IValueRecordDTO> attributes, List<ITagsRecordDTO> tags,
      Long baseEntityIid, List<String> independentAttributesByLastModified, List<String> dependentAttributesByLastModified,
      List<String> tagsByLastmodified) throws Exception
  {
    List<String> attributesAndTagsForLastModified = new ArrayList<String>();
    Map<String, Long> propertyVsLastModified = new HashMap<>();
    for (IValueRecordDTO attribute : attributes) {
      String attributeId = attribute.getProperty().getCode();
      if (!attribute.getValue().isEmpty() && !independentAttributesByLastModified.isEmpty() && independentAttributesByLastModified.contains(attributeId)) {
        attributesAndTagsForLastModified.add(attributeId);
      }
      else if (dependentAttributesByLastModified.contains(attributeId) && !attribute.getValue().isEmpty()) {
        String code = attributeId + "_" + attribute.getLocaleID();
        attributesAndTagsForLastModified.add(code);
      }
    }
    
    for (ITagsRecordDTO tag : tags) {
      String tagId = tag.getProperty().getCode();
      if (tagsByLastmodified.contains(tagId) && !tag.getTags().isEmpty()) {
        attributesAndTagsForLastModified.add(tagId);
      }
    }
    
    if (!attributesAndTagsForLastModified.isEmpty()) {
      propertyVsLastModified = getLastModifiedValueForAttributeAndTagsFromObjectTrackings(baseEntityIid, attributesAndTagsForLastModified);
    }
    return propertyVsLastModified;
  }
  
  private Map<String, Long> getLastModifiedValueForAttributeAndTagsFromObjectTrackings(Long baseEntityIid,
      List<String> attributesAndTagsForLastModified) throws Exception
  {
    Map<String, Long> propertyIdVsLastModified = new HashMap<>();
    List<String> attributesAndTags = new ArrayList<>(attributesAndTagsForLastModified);
    
    IRevisionDAO revisionDAO = rdbmsComponentUtils.getRevisionDAO();
    List<IObjectTrackingDTO> objectTrackings = revisionDAO.getAllObjectTtrackingForBaseEntity(baseEntityIid);
    
    for (IObjectTrackingDTO objectTrackingDTO : objectTrackings) {
      Map<ChangeCategory, ICSEList> timelines = objectTrackingDTO.getTimelineData().getTimelines();
      
      for (ICSEList timelineValue : timelines.values()) {
        List<ICSEElement> subElements = timelineValue.getSubElements();
        
        for (ICSEElement subElement : subElements) {
          if (subElement instanceof ICSEObject) {
            ICSEObject subObject = (ICSEObject) subElement;
            
            String code = subObject.getCode();
            String localeId = subObject.getSpecification(Keyword.$locale);
            
            if (attributesAndTags.contains(code) && localeId.isEmpty()) {
              propertyIdVsLastModified.put(code, objectTrackingDTO.getWhen());
              attributesAndTags.remove(code);
            }
            else if (!localeId.isEmpty()) {
              String id = code + "_" + localeId;
              if (attributesAndTags.contains(id)) {
                propertyIdVsLastModified.put(id, objectTrackingDTO.getWhen());
                attributesAndTags.remove(id);
              }
            }
          }
        }
      }
      if (attributesAndTags.isEmpty()) {
        break;
      }
    }
    return propertyIdVsLastModified;
  }
  
}
