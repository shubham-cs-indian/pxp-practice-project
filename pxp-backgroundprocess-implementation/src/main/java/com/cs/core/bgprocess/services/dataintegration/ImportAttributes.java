package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.idto.IConfigAttributeDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hpsf.NoFormatIDException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.util.Map.Entry;

/**
 * @author vallee
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImportAttributes implements IImportEntity {

  private static final String UPSERT_ATTRIBUTES = "UpsertAttributes";
  
  private static final String  ATTRIBUTE             = "ATTRIBUTE";
  private static final String  VALUE                 = "VALUE";
  private static final String  ADD                   = "ADD";
  private static final String  SUBTRACT              = "SUBTRACT";
  private static final String  MULTIPLY              = "MULTIPLY";
  private static final String  DIVIDE                = "DIVIDE";
  private static final String  OPENING_BRACKET       = "OPENING_BRACKET";
  private static final String  CLOSING_BRACKET       = "CLOSING_BRACKET";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> attributeBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_ATTRIBUTE);
    try (PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigAttributeDTO> attributeDTOList = new ArrayList<>();
      List<IConfigAttributeDTO> conAndCalAttribute = new ArrayList<>();
      Map<String, Integer> calAndConAttributeFormulaSize = new HashMap<>();
      // Upserting attributes in RDBMS
      for (Entry attributeBlock : attributeBlocks.entrySet()) {
        ConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();
        try {
          String pxonBlock = PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) attributeBlock.getValue());
          attributeDTO.fromPXON(pxonBlock);
          String type = attributeDTO.getType();
          if (type.equals(IConfigClass.PropertyClass.CALCULATED_ATTRIBUTE_TYPE.toString())
              || type.equals(IConfigClass.PropertyClass.CONCATENATED_ATTRIBUTE_TYPE.toString())) {
            conAndCalAttribute.add(attributeDTO);
          }
          else {
            upsertAttribute(importer, attributeDTOList, calAndConAttributeFormulaSize,
                attributeDTO, type);
          }
        }catch(Exception e) {
          importer.incrementNumberOfException();
          importer.getJobData().getLog().error(e.getMessage());
        }
        
      }
      
      for(IConfigAttributeDTO attributeDTO :conAndCalAttribute) {
        upsertAttribute(importer, attributeDTOList, calAndConAttributeFormulaSize,(ConfigAttributeDTO) attributeDTO,attributeDTO.getType());
      }
      // Upserting Attributes in ODB
      Map<String, Object> responseMap = configurationImport(importer.getImportDTO().getLocaleID(), calAndConAttributeFormulaSize,
          attributeDTOList.toArray(new IConfigAttributeDTO[0]));
      importer.logIds(responseMap);
      
    }

    catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  private void upsertAttribute(PXONImporter importer,
      List<IConfigAttributeDTO> attributeDTOList,
      Map<String, Integer> calAndConAttributeFormulaSize, ConfigAttributeDTO attributeDTO,
      String type) throws RDBMSException
  {
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(attributeDTO.getType());
    IPropertyDTO propertyDTO = configurationDAO.createProperty(attributeDTO.getCode(),
        propertyType);
    attributeDTO.setPropertyIID(propertyDTO.getPropertyIID());
    attributeDTOList.add(attributeDTO);
    validateAttributeFormulaByType(attributeDTO, importer, calAndConAttributeFormulaSize);
  }

  public Map<String, Object> configurationImport(String localeID, Map<String, Integer> calAndConAttributeFormulaSize,
      IConfigAttributeDTO... attributes) throws CSInitializationException, CSFormatException
  {

    List<IConfigAttributeDTO> attributeList = Arrays.asList(attributes);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, attributeList);
    requestModel.put(CommonConstants.ATTRIBUTE_FORMULA_CODE_SIZE, calAndConAttributeFormulaSize);

    return CSConfigServer.instance().request(requestModel, UPSERT_ATTRIBUTES, localeID);
  }
  
  /**
   * Validate calculated and concatenated formula and prepare size map
   * @param attributeDTO
   * @param importer
   * @param calAndConAttributeFormulaSize
   */
  private void validateAttributeFormulaByType(ConfigAttributeDTO attributeDTO, PXONImporter importer, Map<String, Integer> calAndConAttributeFormulaSize)
  {
    PropertyType type = attributeDTO.getPropertyDTO().getPropertyType();
    String code = attributeDTO.getPropertyDTO().getPropertyCode();
    switch (type) {
      case CALCULATED:
        attributeDTO.setIsTranslatable(false);
        List attributeOperators =  attributeDTO.getAttributeOperatorList();
        if (!attributeOperators.isEmpty()) {
          List<Map<String, Object>> attributeOperatorList = attributeOperators;
          sortListAccordingToOrder(attributeOperatorList);
          int size = attributeOperators.size();
          calAndConAttributeFormulaSize.put(code, size);
          validateCalculatedExpression(attributeOperatorList, code, importer);
          attributeDTO.setAttributeOperatorList(attributeOperators);
        }
        else {
          attributeDTO.setAttributeOperatorList(new ArrayList<>());
        }
        break;
        
      case CONCATENATED:
        attributeDTO.setIsTranslatable(true);
        List concatenatedAttributes =  attributeDTO.getAttributeConcatenatedList();
        if (!concatenatedAttributes.isEmpty()) {
          List<Map<String, Object>> concatenatedAttributeList = concatenatedAttributes;
          sortListAccordingToOrder(concatenatedAttributeList);
          int size = concatenatedAttributes.size();
          calAndConAttributeFormulaSize.put(code, size);
          validateConcatenatedAttribute(concatenatedAttributeList, code, importer);
          attributeDTO.setAttributeConcatenatedList(concatenatedAttributes);
        }
        else {
          attributeDTO.setAttributeConcatenatedList(new ArrayList<>());
        }
      
       // TODO : Temporary Fixed 
      case DATE :
        String defaultValue = attributeDTO.getDefaultValue();
        if(StringUtils.isNotEmpty(defaultValue) && defaultValue.equals("0")) {
          attributeDTO.setDefaultValue("");
          attributeDTO.setDefaultValueAsHTML("");
        }
        
      default:
        break;
    }
  }
  
  private void sortListAccordingToOrder(List<Map<String, Object>> attributeOperatorList)
  {
    Collections.sort(attributeOperatorList, new Comparator<Map<String, Object>>()
    {
      
      public int compare(final Map<String, Object> o1, final Map<String, Object> o2)
      {
        return ((Long) o1.get(ConfigTag.order.toString())).compareTo((Long) o2.get(ConfigTag.order.toString()));
      }
    });
  }
  
  /**
   * If expression is not valid then clear the list 
   * @param attributeOperators
   * @param code
   * @param attributeDTO 
   */
  private void validateCalculatedExpression(List<Map<String, Object>> attributeOperators, String code, PXONImporter importer)
  {
    try {
      //if expression starts or ends with an operator
      Map<String, Object> calculatedAttributeAtFirstIndex = attributeOperators.get(0);
      String firstIndextype = (String) calculatedAttributeAtFirstIndex.get(CommonConstants.TYPE_PROPERTY);
      int size = attributeOperators.size();
      int lastIndex = size - 1;
      Map<String, Object> calculatedAttributeAtLastIndex = attributeOperators.get(lastIndex);
      String lastIndextype = (String) calculatedAttributeAtLastIndex.get(CommonConstants.TYPE_PROPERTY);
      if (isAnOperator(firstIndextype) || isAnOperator(lastIndextype)) {
        throw new NoFormatIDException("Expression Does not start or end with operator");
      }
      
      if (firstIndextype.equals(CLOSING_BRACKET)) {
        throw new NoFormatIDException("Expression Does not start with closing bracket");
      }
      
      if (lastIndextype.equals(OPENING_BRACKET)) {
        throw new NoFormatIDException("Expression Does not end with opening bracket");
      }
      
      //mismatching number of opening and closing
      int unclosedParenthesis = 0;
      for (int i = 0; i < size; i++) {
        Map<String, Object> calculatedAttribute = attributeOperators.get(i);
        Long order = (Long) calculatedAttribute.get(ConfigTag.order.toString());
        if (i != order) {
          throw new IndexOutOfBoundsException("Index is " + i + " and order is " + order + " mismatch");
        }
        String type = (String) calculatedAttribute.get(CommonConstants.TYPE_PROPERTY);
        switch (type) {
          case OPENING_BRACKET:
            unclosedParenthesis++;
            checkedOperatorAndType(calculatedAttribute, type);
            checkedNextIsNotOppBracket(attributeOperators, lastIndex, i, CLOSING_BRACKET);
            break;
          
          case CLOSING_BRACKET:
            unclosedParenthesis--;
            checkedOperatorAndType(calculatedAttribute, type);
            checkedNextIsNotOppBracket(attributeOperators, lastIndex, i, OPENING_BRACKET);
            break;
          
          case ADD:
          case MULTIPLY:
          case SUBTRACT:
          case DIVIDE:
            checkedOperatorAndType(calculatedAttribute, type);
            validateOperatorBeforAndAfterType(attributeOperators, i);
            break;
          
          case ATTRIBUTE:
            validateAttribute(calculatedAttribute);
            validateAttributeAndValueBeforeAndAfterType(attributeOperators, i, lastIndex);
            break;
          
          case VALUE:
            String value = (String) calculatedAttribute.get(ConfigTag.value.toString());
            Double.valueOf(value);
            validateAttributeAndValueBeforeAndAfterType(attributeOperators, i, lastIndex);
            break;
          
          default:
            throw new NoSuchElementException(type);
        }
      }
      
      if (unclosedParenthesis != 0) {
        throw new NoFormatIDException("Number of parenthsis are unmatched");
      }
      
    }
    catch (Exception e) {
      attributeOperators.clear();
      importer.getJobData().getLog().error(e.getMessage() + " For attribute code = " + code);
    }
  }
  
  /**
   * check operator or not
   * @param operator
   * @return
   */
  private boolean isAnOperator(String operator)
  {
    return (operator.equals(MULTIPLY) || operator.equals(DIVIDE) || operator.equals(SUBTRACT) || operator.equals(ADD));
  }
  
  /**
   *  type and attribute must be same otherwise throw NoFormatIDException
   * @param calculatedAttribute
   * @param type
   */
  private void checkedOperatorAndType(Map<String, Object> calculatedAttribute, String type)
  {
    String operator = (String) calculatedAttribute.get(ConfigTag.operator.toString());
    if (!type.equals(operator)) {
      throw new NoFormatIDException("Operator and type mismatch for type " + type + " and it operator is " + operator);
    }
  }
  
 /**
  * If it is opening bracket then it can't follow close bracket and vice versa
  * @param attributeOperatorList
  * @param lastIndex
  * @param i
  * @param type
  */
  private void checkedNextIsNotOppBracket(List<Map<String, Object>> attributeOperatorList, int lastIndex, int i, String type)
  {
    if (i != lastIndex) {
      Map<String, Object> calculatedAtributeAfter =  attributeOperatorList.get(i + 1);
      String typeAfter = (String) calculatedAtributeAfter.get(CommonConstants.TYPE_PROPERTY);
      if (typeAfter.equals(type)) {
        throw new NoFormatIDException("Opposite bracket are not allowed after bracket is " + typeAfter);
      }
    }
  }
  
  /**
   * Validation for attribute and value before and after checked
   * @param attributeOperatorList
   * @param currentIndex
   * @param size
   */
  private static void validateAttributeAndValueBeforeAndAfterType(List<Map<String, Object>> attributeOperatorList, int currentIndex, int size)
  {
    if (currentIndex != 0) {
      Map<String, Object> calculatedAtributeBefore =   attributeOperatorList.get(currentIndex - 1);
      String typeBefore = (String) calculatedAtributeBefore.get(CommonConstants.TYPE_PROPERTY);
      if (typeBefore.equals(ATTRIBUTE) || typeBefore.equals(VALUE) || typeBefore.equals(CLOSING_BRACKET)) {
        throw new NoFormatIDException("Found wrong type preceding Attribute or Value " + typeBefore);
      }
    }
    
    if (currentIndex != size) {
      Map<String, Object> calculatedAtributeAfter = attributeOperatorList.get(currentIndex + 1);
      String typeAfter = (String) calculatedAtributeAfter.get(CommonConstants.TYPE_PROPERTY);
      if (typeAfter.equals(ATTRIBUTE) || typeAfter.equals(VALUE) || typeAfter.equals(OPENING_BRACKET)) {
        throw new NoFormatIDException("Found wrong type after Attribute or Value " + typeAfter);
      }
    }
    
  }
  
  /**
   * validate operator before and after
   * @param attributeOperatorList
   * @param i
   */
  private void validateOperatorBeforAndAfterType(List<Map<String, Object>> attributeOperatorList, int i)
  {
    Map<String, Object> calculatedAtributeBefore = attributeOperatorList.get(i - 1);
    String typeBefore = (String) calculatedAtributeBefore.get(CommonConstants.TYPE_PROPERTY);
    Map<String, Object> calculatedAtributeAfter = attributeOperatorList.get(i + 1);
    String typeAfter = (String) calculatedAtributeAfter.get(CommonConstants.TYPE_PROPERTY);
    
    if (typeBefore.equals(OPENING_BRACKET) || typeAfter.equals(CLOSING_BRACKET) || isAnOperator(typeAfter)) {
      throw new NoFormatIDException(
          "Found wrongly preceding or following parenthesis to operator or Found an operator following another operator");
    }
  }
  
  /**
   * If attribute code not found then it will throw RDBMS exception
   * @param concatenatedAttribute
   * @throws RDBMSException
   */
  private void validateAttribute(Map<String, Object> concatenatedAttribute) throws RDBMSException
  {
    String attributeID = (String) concatenatedAttribute.get(ConfigTag.attributeId.toString());
    ConfigurationDAO.instance().getPropertyByCode(attributeID);
  }
  
  /**
   * validate concatenated attribute
   * No duplicate order is allowed
   * Order start with 0
   * validate attributes and tags 
   * if above all case satisfy we create attribute with formula for concatenated attribute.
   * @param concatenatedAttributes
   * @param code
   * @param importer 
   */
  private void validateConcatenatedAttribute(List<Map<String, Object>> concatenatedAttributes, String code, PXONImporter importer)
  {
    try {
      for (int i = 0; i < concatenatedAttributes.size(); i++) {
        Map<String, Object> concatenatedAttribute = concatenatedAttributes.get(i);
        Long order = (Long) concatenatedAttribute.get(ConfigTag.order.toString());
        if (i != order) {
          throw new IndexOutOfBoundsException("Index is " + i + " and order is " + order + " mismatch");
        }
        String type = (String) concatenatedAttribute.get(ICalculatedAttribute.TYPE);
        switch (type) {
          case CommonConstants.ATTRIBUTE:
            validateAttribute(concatenatedAttribute);
            break;
          
          case CommonConstants.TAG:
            validateTag(concatenatedAttribute);
            break;
          
          case CommonConstants.HTML:
            break;
          
          default:
            throw new NoSuchFieldException(type);
        }
      }
    }
    catch (Exception e) {
      concatenatedAttributes.clear();
      importer.getJobData().getLog().error(e.getMessage() + " For attribute code = " + code);
    }
  }

  /**
   * If tag Code is not present then it will throw RDBMSException
   * @param concatenatedAttribute
   * @throws RDBMSException
   */
  private void validateTag(Map<String, Object> concatenatedAttribute) throws RDBMSException
  {
    String tagID = (String) concatenatedAttribute.get(ConfigTag.tagId.toString());
    ConfigurationDAO.instance().getPropertyByCode(tagID);
  }
}
