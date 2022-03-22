package com.cs.di.runtime.authorization.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.exception.exporttoexcel.UnauthorizedNatureKlassException;
import com.cs.di.workflow.constants.MessageCode;

public class PartnerAuthorizationUtils
{
  
  public  static String ENTITY                                = "entity";
  //ALL Selection
  private static String IS_ALL_ATTRIBUTES_SELECTED            = "isAllAttributesSelected";
  private static String IS_ALL_CLASS_SELECTED                 = "isAllClassesSelected";
  private static String IS_ALL_TAXONOMIES_SELECTED            = "isAllTaxonomiesSelected";
  private static String IS_ALL_CONTEXTS_SELECTED              = "isAllContextsSelected";
  private static String IS_ALL_TAGS_SELECTED                  = "isAllTagsSelected";
  //Mapping entity
  private static String ATTRIBUTE_MAPPINGS                    = "attributeMappings";
  private static String TAG_MAPPINGS                          = "tagMappings";
  private static String CLASS_MAPPINGS                        = "classMappings";
  private static String CONTEXT_MAPPINGS                      = "contextMappings";
  private static String TAXONOMY_MAPPINGS                     = "taxonomyMappings";
  private static String RELATIONSHIP_MAPPINGS                 = "relationshipMappings";
  //Blank Value allowed
  private static String IS_BLANK_VALUEACCEPTED_FOR_TAXONOMIES = "isBlankValueAcceptedForTaxonomies";
  private static String IS_BLANK_VALUEACCEPTED_FOR_ATTRIBUTES = "isBlankValueAcceptedForAttributes";
  private static String IS_BLANK_VALUEACCEPTED_FOR_TAGS       = "isBlankValueAcceptedForTags";
  private static String IS_ALL_RELATIONSHIPS_SELECTED         = "isAllRelationshipsSelected";
  private static String NAME_ATTRIBUTE                        = "nameattribute";
  
  
  public static void importAuthorizationfilter(IBaseEntityDTO baseEntity,
      IBaseEntityDTO sourceEntityDTOWithProperties, Map<String, Object> partnerAuthorization)
      throws Exception
  {
      // Attribute/Tags/Relation side /Attribute Variant
      // 0 and 1 useCase stands for create and update action respectively
      int useCase = baseEntity.getBaseEntityName().isEmpty() ? 0 : 1;
      filterAuthorizedProperties(baseEntity.getPropertyRecords(), partnerAuthorization, useCase);
      // non Nature class and Taxonomy
      filterAuthorizedClassifier(baseEntity.getOtherClassifiers(), partnerAuthorization);
      
      // handle existing classifier, this method decide the classifier remove from existing product or not.
      handleExistingClassifierWithAuthorization(baseEntity.getOtherClassifiers(), sourceEntityDTOWithProperties.getOtherClassifiers(), partnerAuthorization);
      
      // embedded Variant
      if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntity.getEmbeddedType())) {
          if(!isVariantAuthorized(baseEntity, partnerAuthorization)) 
        //baseEntity.setContextualObject(null);
        throw new Exception("Found Unauthrized Embedded Variant");
      }
      else {
        // Nature class
        if (!isAuthorizedNatureClass(baseEntity, partnerAuthorization)) {
          throw new Exception("Found Unauthrized Nature Class");
        }
      }
  } 
  
  @SuppressWarnings("unchecked")
  public static void filterAuthorizedProperties(Collection<IPropertyRecordDTO> propertyRecords, Map<String, Object> partnerAuthorization, int useCase)
  {
    List<IPropertyRecordDTO> unAuthorizedAttributes = new ArrayList<>();
    List<IPropertyRecordDTO> unAuthorizedTags = new ArrayList<>();
    List<IPropertyRecordDTO> unAuthorizedRelationships = new ArrayList<>();
    
    for (IPropertyRecordDTO propertyRecordDTO : propertyRecords) {
      SuperType superType = propertyRecordDTO.getProperty().getSuperType();
      String propertyCode = propertyRecordDTO.getProperty().getPropertyCode();    
      
      if((superType.equals(SuperType.ATTRIBUTE)))
      {
        if (((!(Boolean) partnerAuthorization.get(IS_ALL_ATTRIBUTES_SELECTED)
            && !((List<String>) partnerAuthorization.get(ATTRIBUTE_MAPPINGS)).contains(propertyCode))
            || (!(Boolean) partnerAuthorization.get(IS_BLANK_VALUEACCEPTED_FOR_ATTRIBUTES)
                && ((IValueRecordDTO) propertyRecordDTO).getValue().isEmpty()))) {
          //PXPFDEV-19359 nameattribute is mandate regardless of authorization hence check added
          if (!NAME_ATTRIBUTE.equals(propertyCode) || useCase == 1 )
          unAuthorizedAttributes.add(propertyRecordDTO);
        }       
        // attribute variant
        else if (!(Boolean) partnerAuthorization.get(IS_ALL_CONTEXTS_SELECTED)
            && ((IValueRecordDTO) propertyRecordDTO).getContextualObject() != null
            && ((IValueRecordDTO) propertyRecordDTO).getContextualObject().getContextCode() != null
            && !((IValueRecordDTO) propertyRecordDTO).getContextualObject().getContextCode().isBlank()
            && !((List<String>) partnerAuthorization.get(CONTEXT_MAPPINGS))
                .contains(((IValueRecordDTO) propertyRecordDTO).getContextualObject().getContextCode())) {
          unAuthorizedAttributes.add(propertyRecordDTO);
        }
      }      
      else if (superType.equals(SuperType.TAGS)) {
        ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) propertyRecordDTO;
        ITagDTO[] tagValues = tagsRecordDTO.getTags().toArray(new ITagDTO[tagsRecordDTO.getTags().size()]);
        
        if ((!(Boolean) partnerAuthorization.get(IS_ALL_TAGS_SELECTED) && !((List<String>)partnerAuthorization.get(TAG_MAPPINGS)).contains(propertyCode))
            || (!(Boolean) partnerAuthorization.get(IS_BLANK_VALUEACCEPTED_FOR_TAGS) && tagValues.length == 0)) {
          
          unAuthorizedTags.add(propertyRecordDTO);
        }
      }
      else if (superType.equals(SuperType.RELATION_SIDE) && 
          (!(Boolean) partnerAuthorization.get(IS_ALL_RELATIONSHIPS_SELECTED) && !((List<String>)partnerAuthorization.get(RELATIONSHIP_MAPPINGS)).contains(propertyCode))) {
        
        unAuthorizedRelationships.add(propertyRecordDTO);
      } 
    }
    propertyRecords.removeAll(unAuthorizedAttributes);
    propertyRecords.removeAll(unAuthorizedTags);
    propertyRecords.removeAll(unAuthorizedRelationships);
  }

  
  /**
   * It filters unAuthorized non Nature & taxonomy
   * from the given list of classRecords
   * @param set 
   * @param classRecords
   * @param partnerAuthorization
   */
  public static void filterAuthorizedClassifier(Collection<IClassifierDTO> sourceClassRecords, Map<String, Object> partnerAuthorization)
  {
    List<IClassifierDTO> unAuthorizedClassess = new ArrayList<>();    
    // non Nature and Taxonomy
    for (IClassifierDTO classRecord : sourceClassRecords) {
      ClassifierType classifierType = classRecord.getClassifierType();
      String classifierCode = classRecord.getClassifierCode();
      if (isAuthorizedClassCondition(partnerAuthorization, classifierType, classifierCode)) {
        unAuthorizedClassess.add(classRecord);
      }
      else if (classifierType.equals(ClassifierType.TAXONOMY)|| classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
        
        if (isAuthorizedTaxonomyCondition(partnerAuthorization, classifierCode)) {
          unAuthorizedClassess.add(classRecord);
        }
      }
    }
    
    sourceClassRecords.removeAll(unAuthorizedClassess);
  }

  /**
   * Decide existing classifier remove or not.
   * 
   * @param sourceClassRecords
   * @param existingClassRecords
   * @param partnerAuthorization
   */
  private static void handleExistingClassifierWithAuthorization(Collection<IClassifierDTO> sourceClassRecords, Set<IClassifierDTO> existingClassRecords,
      Map<String, Object> partnerAuthorization)
  {
    boolean isTaxonomyBlank = getIsTaxonomyBlank(sourceClassRecords);
    boolean booleanHandlerForBlankValueAllowed = isTaxonomyBlank
        ? !(Boolean) partnerAuthorization.get(IS_BLANK_VALUEACCEPTED_FOR_TAXONOMIES): false;
    
    for (IClassifierDTO classRecord : existingClassRecords) {
      
      ClassifierType classifierType = classRecord.getClassifierType();
      String classifierCode = classRecord.getClassifierCode();
      if (isAuthorizedClassCondition(partnerAuthorization, classifierType, classifierCode)) {
        
        sourceClassRecords.add(classRecord);
      }
      else if ((classifierType.equals(ClassifierType.TAXONOMY) || classifierType.equals(ClassifierType.MINOR_TAXONOMY))) {
        if (isAuthorizedTaxonomyCondition(partnerAuthorization, classifierCode) || booleanHandlerForBlankValueAllowed) {
          sourceClassRecords.add(classRecord);
        }
      }
    }
  }


  public static boolean getIsTaxonomyBlank(Collection<IClassifierDTO> sourceClassRecords)
  {
    boolean isTaxonomyBlank = true;
    List<IClassifierDTO> taxonomyFilter = sourceClassRecords
        .stream().filter(p -> p.getClassifierType().equals(ClassifierType.TAXONOMY)
            || p.getClassifierType().equals(ClassifierType.MINOR_TAXONOMY))
        .collect(Collectors.toList());
    if (!taxonomyFilter.isEmpty()) {
      isTaxonomyBlank = false;
    }
    return isTaxonomyBlank;
  }
  
  /**
   * It filters unAuthorized Embedded Variant
   * from the given list of classRecords
   * @param classRecords
   * @param partnerAuthorization
   * @return 
   * @throws Exception 
   */
  public static boolean isVariantAuthorized(IBaseEntityDTO entity, Map<String, Object> partnerAuthorization)
  {
    // embedded variant check for auth Mapping
    if(partnerAuthorization == null)
      return true;
    if (!(Boolean) partnerAuthorization.get(IS_ALL_CONTEXTS_SELECTED) && entity.getContextualObject() != null
        && entity.getContextualObject().getContextCode() != null && !entity.getContextualObject().getContextCode().isBlank()
        && !((List<String>) partnerAuthorization.get(CONTEXT_MAPPINGS)).contains((entity.getContextualObject().getContextCode()))) {
      return false;
    }
    return true;
  }
  
  /**
   * This check is used only for transfer
   * 
   * @param classifier
   * @param authorizationMapping
   * @param booleanHandleForBlankValueAllowed
   * @return
   */
  public static boolean isClassifierAuthorized(IClassifierDTO classifier, Map<String, Object> authorizationMapping, boolean booleanHandleForBlankValueAllowed)
  {
    if (authorizationMapping == null)
      return true;
    ClassifierType classifierType = classifier.getClassifierType();
    String classifierCode = classifier.getClassifierCode();
    if (isAuthorizedClassCondition(authorizationMapping, classifierType, classifierCode)) {
      return false;
    }
    else if (classifierType.equals(ClassifierType.TAXONOMY) || classifierType.equals(ClassifierType.MINOR_TAXONOMY)) {
      if (isAuthorizedTaxonomyCondition(authorizationMapping, classifierCode) || booleanHandleForBlankValueAllowed) {
        return false;
      }
    }
    
    return true;
  }

  /**
   * @param authorizationMapping
   * @param classifierCode
   * @return
   */
  private static boolean isAuthorizedTaxonomyCondition(Map<String, Object> authorizationMapping, String classifierCode)
  {
    return !(Boolean) authorizationMapping.get(IS_ALL_TAXONOMIES_SELECTED)
        && (!((List<String>) authorizationMapping.get(TAXONOMY_MAPPINGS)).contains(classifierCode));
  }

  /**
   * @param authorizationMapping
   * @param classifierType
   * @param classifierCode
   * @return
   */
  private static boolean isAuthorizedClassCondition(Map<String, Object> authorizationMapping, ClassifierType classifierType,
      String classifierCode)
  {
    return (classifierType.equals(ClassifierType.CLASS)) && (!(Boolean) authorizationMapping.get(IS_ALL_CLASS_SELECTED)
        && !((List<String>) authorizationMapping.get(CLASS_MAPPINGS)).contains(classifierCode));
  }
  
  /**
   * @param classifier
   * @param partnerAuthorization 
   * @param taxonomyIds 
   * @return
   */
  public static void getTaxonomyIdsFromBaseEntity(Collection<IClassifierDTO> classifier, Set<String> taxonomyIds)
  {
    for (IClassifierDTO classRecord : classifier) {
      ClassifierType classifierType = classRecord.getClassifierType();
      if ((classifierType.equals(ClassifierType.TAXONOMY) || classifierType.equals(ClassifierType.MINOR_TAXONOMY))) {
        taxonomyIds.add(classRecord.getClassifierCode());
      }
    }
  }
  
  /**
   * This method would help to filter data to export
   * depending on Authorization provided.
   * if user is not authorized to export that field should not be part
   * of PXON generated hence marking as null in few scenarios
   * @param baseEntity
   * @param partnerAuthorization
   * @throws Exception
   */
  public static void exportAuthorizationfilter(IBaseEntityDTO baseEntity, Map<String, Object> partnerAuthorization) throws Exception
  {
    // Attribute/Tags/Relation side /Attribute Variant
    //useCase 0 stands for create and also in case of export user is exporting so of no significance 
    filterAuthorizedProperties(baseEntity.getPropertyRecords(), partnerAuthorization, 0);
    // non Nature class and Taxonomy
    filterAuthorizedClassifier(baseEntity.getOtherClassifiers(), partnerAuthorization);
    // embedded Variant
    if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntity.getEmbeddedType())) {
      if (!isVariantAuthorized(baseEntity, partnerAuthorization))
        // don't export embedded variant Class if user is unauthorized to export hence marking as null
        baseEntity.setContextualObject(null);
    }
    else {
      // Nature class
      if (!isAuthorizedNatureClass(baseEntity, partnerAuthorization)) {
        // don't export Nature Class if user is unauthorized to export hence marking as null
        throw new UnauthorizedNatureKlassException(
            MessageFormat.format(MessageCode.GEN046.getMessage(), baseEntity.getNatureClassifier().getClassifierCode()));
      }
    }
  }
  
    /**
   * It filters unAuthorized Nature Class
   * if true - Nature Class is authorized
   * if false - Nature Class is unautrorized
   * @param entity
   * @param partnerAuthorization
   * @return
   * @throws Exception
   */
  public static boolean isAuthorizedNatureClass(IBaseEntityDTO entity, Map<String, Object> partnerAuthorization) throws Exception
  {
    IClassifierDTO natureClass = entity.getNatureClassifier();
    List<IClassifierDTO> unAuthorizedClassess = new ArrayList<>();
    if (natureClass != null) {
      if ((natureClass.getClassifierType().equals(ClassifierType.CLASS)) && (!(Boolean) partnerAuthorization.get(IS_ALL_CLASS_SELECTED)
          && !((List<String>) partnerAuthorization.get(CLASS_MAPPINGS)).contains(natureClass.getClassifierCode()))) {
        return false;
      }
    }
    return true;
  }
}
