package com.cs.core.runtime.interactor.utils.klassinstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;

public class CreateInstanceUtils {
  
  private static final String VALUE_TO_BE_DISCARDED = "Value to be discarded";
  
  public static IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails, ICreateInstanceModel createInstanceModel, ILocaleCatalogDAO localeCatalogDAO)
      throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, localeCatalogDAO);
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = createAttributePropertyRecordInstance(
        propertyRecordBuilder, configDetails, createInstanceModel, baseEntityDAO);
    // Create tag
    propertyRecords.addAll(
        createTagPropertyRecordInstance(propertyRecordBuilder, configDetails, createInstanceModel, baseEntityDAO));
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  protected static List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    for (IAttribute referencedAttribute : configDetails.getReferencedAttributes()
        .values()) {
      
      IReferencedSectionElementModel referencedSectionElementModel = configDetails.getReferencedElements().get(referencedAttribute.getId());
      IPropertyRecordDTO dto = null;
      if (referencedAttribute.getId()
          .equals(IStandardConfig.StandardProperty.assetcoverflowattribute.toString())) {
        continue;
      }else if(referencedSectionElementModel != null && referencedSectionElementModel.getCouplingType() != null && ! referencedSectionElementModel.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
        dto = handleInheritanceCouplingForAttributes((IReferencedSectionAttributeModel)referencedSectionElementModel, referencedAttribute, baseEntityDAO);
      }else {
        dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
      }
      if (dto != null) {
        attributePropertyRecords.add(dto);
      }
    }
    addMandatoryAttribute(attributePropertyRecords, propertyRecordBuilder, configDetails,
        createInstanceModel);
    return attributePropertyRecords;
  }
  
  protected static IPropertyRecordDTO handleInheritanceCouplingForAttributes(
      IReferencedSectionAttributeModel attributeElement, IAttribute attributeConfig,
      IBaseEntityDAO childBaseEntityDAO) throws Exception
  {
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(attributeConfig);
    String localeID = null;
    if(attributeConfig.getIsTranslatable()) {
      localeID = childBaseEntityDAO.getBaseEntityDTO()
          .getBaseLocaleID();
    }
    boolean isDynamic = false;
    if (attributeElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)) {
      isDynamic = true;
    }
    IValueRecordDTO valueRecordDTO = childBaseEntityDAO
        .newValueRecordDTOBuilder(propertyDTO, VALUE_TO_BE_DISCARDED)
        .localeID(localeID)
        .build();
    valueRecordDTO.addInheritanceCoupling(propertyDTO, isDynamic);
    return valueRecordDTO;
  }
  
  protected static List<IPropertyRecordDTO> createTagPropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    Set<String> referencedElementIds = configDetails.getReferencedElements()
        .keySet();
    Map<String, IReferencedVariantContextModel> embeddedVariantContexts = configDetails
        .getReferencedVariantContexts()
        .getEmbeddedVariantContexts();
    Iterator<IReferencedVariantContextModel> iterator = embeddedVariantContexts.values()
        .iterator();
    List<String> contextTagIds = new ArrayList<>();
    if (iterator.hasNext()) {
      contextTagIds.addAll(iterator.next()
          .getTags()
          .stream()
          .map(x -> x.getTagId())
          .collect(Collectors.toList()));
    }
    
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    configDetails.getReferencedTags()
        .values()
        .stream()
        .filter(
            referencedTag -> !isContextualTag(referencedTag, referencedElementIds, contextTagIds))
        .forEach(referencedTags -> {
          try {
            IReferencedSectionElementModel referencedSectionElementModel = configDetails.getReferencedElements().get(referencedTags.getId());
            IPropertyRecordDTO dto = null;
            if(referencedSectionElementModel != null && referencedSectionElementModel.getCouplingType() != null && ! referencedSectionElementModel.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
              dto = handleInheritanceCouplingForTags((IReferencedSectionTagModel) referencedSectionElementModel, referencedTags, baseEntityDAO);
            }else {
              dto = propertyRecordBuilder.createTagsRecord(referencedTags);
            }
            if (dto != null) {
              tagPropertyRecords.add(dto);
            }
          }
          catch (Exception e) {
            new RuntimeException(e);
          }
        });
    return tagPropertyRecords;
  }
  
  protected static boolean isContextualTag(ITag referencedTag, Set<String> referencedElementIds,
      List<String> contextTagIds)
  {
    String tagId = referencedTag.getId();
    return contextTagIds.contains(tagId) && !referencedElementIds.contains(tagId);
  }
  
  protected static IPropertyRecordDTO handleInheritanceCouplingForTags(
      IReferencedSectionTagModel tagElement, ITag tagConfig,
      IBaseEntityDAO childBaseEntityDAO) throws Exception
  {
    IPropertyDTO propertyDTO = RDBMSUtils.newPropertyDTO(tagConfig);
    boolean isDynamic = false;
    if (tagElement.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)) {
      isDynamic = true;
    }
    ITagsRecordDTO targetTagsRecord = childBaseEntityDAO.newTagsRecordDTOBuilder( propertyDTO).build();
    targetTagsRecord.addInheritanceCoupling(propertyDTO, isDynamic);
    return targetTagsRecord;
  }
  
  protected static void addMandatoryAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel) throws Exception
  {
    addNameAttribute(listOfPropertyRecordsDTO, propertyRecordBuilder,
        createInstanceModel.getName());
  }
  
  protected static void addNameAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, String name) throws Exception
  {
    IPropertyRecordDTO nameAttribute = propertyRecordBuilder.createStandardNameAttribute(name);
    listOfPropertyRecordsDTO.add(nameAttribute);
  }
}
