package com.cs.core.bgprocess.services.di;

import com.cs.core.dataintegration.dto.PXONExportScopeDTO;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Tests of export tests DTO
 *
 * @author vallee
 */
public class PXONExportScopeDTOTest extends QuickPrinter {
  
  private static final String CATALOG_SPEC = "{ \"catalog\": {\"csid\": \"[C>sap $id='65b9d354-7a20-4952-9dee-2f134e0ea247' $locale=en_CA $type=DI]\"},"
      + "\"localeInheritance\": [\"en_WW\", \"en_US\", \"en_CA\"]}";
  
  @Test
  public void runtimeEntityList() throws CSFormatException
  {
    printTestTitle("runtimeEntityList");
    PXONExportScopeDTO scope = new PXONExportScopeDTO();
    scope.fromJSON("{}");
    printJSON("Empty", scope);
    scope.fromJSON(CATALOG_SPEC);
    printJSON("From SAP Catalog", scope);
    scope.setBaseEntityIDs(Arrays.asList("PCScreenDELL500", "PCScreenDELL501"));
    printJSON("Entity ID list", scope);
    scope.setBaseEntityIIDs(Arrays.asList(10001345L, 10001346L));
    printJSON("Entity IID list", scope);
    scope.setBaseEntityProperties(Arrays.asList(200L, 201L, 202L, 203L, 204L, 285L));
    printJSON("Entity with property IID list", scope);
    scope.setBaseEntityPropertiesByCodes(Arrays.asList("nameattribute", "createdbyattribute",
        "createdonattribute", "lastmodifiedbyattribute", "lastmodifiedattribute",
        "standardArticleAssetRelationship"));
    printJSON("Entity with property Codes list", scope);
    String finalJSON = scope.toJSON();
    scope.fromJSON(finalJSON.replace("entityPropertyCodes", "entityPropertyIDs"));
    printJSON("Entity with property IDs list", scope);
  }
  
  @Test
  public void configItemList() throws CSFormatException
  {
    printTestTitle("configItemList");
    PXONExportScopeDTO scope = new PXONExportScopeDTO();
    scope.setConfigItemTypes(Arrays.asList(IRootConfigDTO.ItemType.EVENT,
        IRootConfigDTO.ItemType.TASK, IRootConfigDTO.ItemType.PROPERTY_COLLECTION));
    printJSON("Item configs", scope);
    scope.setPropertyItemTypes(Arrays.asList(
        IPropertyDTO.PropertyType.TEXT, IPropertyDTO.PropertyType.TAG));
    printJSON("Item configs with properties", scope);
  }
  
  @Test
  public void configItemDetail() throws CSFormatException
  {
    printTestTitle("configItemDetail");
    PXONExportScopeDTO scope = new PXONExportScopeDTO();
    Map<IRootConfigDTO.ItemType, Collection<Long>> iidMap = new HashMap<>();
    iidMap.put(IRootConfigDTO.ItemType.CLASSIFIER, Arrays.asList(4000L, 1001465L, 1001466L));
    iidMap.put(IRootConfigDTO.ItemType.PROPERTY, Arrays.asList(200L, 201L, 202L, 203L, 204L, 285L));
    scope.setConfigItemIIDs(iidMap);
    printJSON("Item configs with IIDs", scope);
    Map<IRootConfigDTO.ItemType, Collection<String>> idMap = new HashMap<>();
    idMap.put(IRootConfigDTO.ItemType.CLASSIFIER, Arrays.asList("single_article",
        "8f9ff675-3968-4ef5-bf10-423753d449d5", "0c69f987-60bb-4613-92a5-d1db0c81ae47"));
    idMap.put(IRootConfigDTO.ItemType.PROPERTY,
        Arrays.asList("nameattribute", "createdbyattribute", "createdonattribute",
            "lastmodifiedbyattribute", "lastmodifiedattribute",
            "standardArticleAssetRelationship"));
    idMap.put(IRootConfigDTO.ItemType.CLASSIFIER,
        Arrays.asList("single_article", "Electronics", "Electricity"));
    scope.setConfigItemByCodes(idMap);
    printJSON("Item configs with Codes", scope);
  }
  
  @Test
  public void wrapupTest() throws CSFormatException
  {
    printTestTitle("wrapupTest");
    PXONExportScopeDTO scope = new PXONExportScopeDTO();
    scope.fromJSON(CATALOG_SPEC);
    scope.setBaseEntityIDs(Arrays.asList("PCScreenDELL500", "PCScreenDELL501"));
    scope.setBaseEntityProperties(Arrays.asList(200L, 201L, 202L, 203L, 204L, 285L));
    scope.setConfigItemTypes(Arrays.asList(IRootConfigDTO.ItemType.EVENT,
        IRootConfigDTO.ItemType.TASK, IRootConfigDTO.ItemType.PROPERTY_COLLECTION));
    scope.setPropertyItemTypes(Arrays.asList(
        IPropertyDTO.PropertyType.TEXT, IPropertyDTO.PropertyType.TAG));
    Map<IRootConfigDTO.ItemType, Collection<String>> codeMap = new HashMap<>();
    codeMap.put(IRootConfigDTO.ItemType.CLASSIFIER,
        Arrays.asList("single_article", "Electronics", "Electricity"));
    codeMap.put(IRootConfigDTO.ItemType.PROPERTY,
        Arrays.asList("nameattribute", "createdbyattribute", "createdonattribute",
            "lastmodifiedbyattribute", "lastmodifiedattribute",
            "standardArticleAssetRelationship"));
    scope.setConfigItemByCodes(codeMap);
    String finalJSON = scope.toJSON();
    scope.fromJSON(finalJSON);
    printJSON("Wrap up", scope);
  }
}
