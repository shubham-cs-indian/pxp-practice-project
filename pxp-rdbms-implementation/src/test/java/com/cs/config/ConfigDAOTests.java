package com.cs.config;

import com.cs.config.dao.RefConfigurationDAO;
import com.cs.config.idto.*;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class ConfigDAOTests extends QuickPrinter {
  
  private RefConfigurationDAO configurationDAO;
  
  @Before
  public void init() throws CSInitializationException
  {
    CSProperties.init("./rdbms-props/rdbms.properties");
    configurationDAO = new RefConfigurationDAO();
  }
  
  @Test
  public void attributeDAOTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("attributeDAOTest");
    Collection<IConfigAttributeDTO> attributeDTOList = configurationDAO.getAttributes(null,
        "nameattribute");
    printTestTitle("Attribute PXON List : ");
    for (IConfigAttributeDTO iConfigAttributeDTO : attributeDTOList) {
      println(iConfigAttributeDTO.toPXON());
    }
  }
  
  @Test
  public void tagDAOTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("tagDAOTest");
    Collection<IConfigTagDTO> tagDTOList = configurationDAO.getTags(null, "statustag");
    printTestTitle("Tag PXON List : ");
    for (IConfigTagDTO iConfigTagDTO : tagDTOList) {
      println(iConfigTagDTO.toPXON());
    }
  }
  
  @Test
  public void relationshipDAOTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("relationshipDAOTest");
    Collection<IConfigRelationshipDTO> relationshipDTOList = configurationDAO.getRelationships(null,
        "standardArticleAssetRelationship");
    printTestTitle("Tag PXON List : ");
    for (IConfigRelationshipDTO iRelationshipTagDTO : relationshipDTOList) {
      println(iRelationshipTagDTO.toPXON());
    }
  }
  
  @Test
  public void tagValueTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("tagValueTest");
    IConfigTagValueDTO tagValueDTO = configurationDAO.getTagValueByCode("de_DE", "enrichmenttag");
    printf("Label of tagvalue is %s\n", tagValueDTO.getLabel());
    println(tagValueDTO.toPXON());
  }
  
  @Test
  public void propertyCollectionTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("propertyCollectionTest PXON List : ");
    Collection<IConfigPropertyCollectionDTO> pcDTOList = configurationDAO
        .getPropertyCollections("de_DE", "articlegeneralInformationPropertyCollection");
    
    for (IConfigPropertyCollectionDTO iConfigPCTO : pcDTOList) {
      println(iConfigPCTO.toPXON());
    }
  }
  
  @Test
  public void contextTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("ContextTest List : ");
    Collection<IConfigContextDTO> contextDTOList = configurationDAO.getContexts("de_DE", "CTX1000",
        "CTX1001");
    for (IConfigContextDTO iConfigContextDTO : contextDTOList) {
      println(iConfigContextDTO.toPXON());
      println(iConfigContextDTO.toJSON());
    }
  }
  
  @Test
  public void classifierTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("classifierTest");
    Collection<IConfigClassifierDTO> classifierDTOList = configurationDAO.getClasses(true, "en_US",
        EntityType.ARTICLE, "single_article");
    for (IConfigClassifierDTO iConfigClassifierDTO : classifierDTOList) {
      println(iConfigClassifierDTO.toPXON());
    }
  }
  
  @Test
  public void taxonomyTest() throws CSFormatException, CSInitializationException
  {
    printTestTitle("taxonomyTest List : ");
    Collection<IConfigClassifierDTO> classifierDTOList = configurationDAO.getTaxonomies(true,
        "en_US", "Electronics12");
    for (IConfigClassifierDTO iConfigClassifierDTO : classifierDTOList) {
      println(iConfigClassifierDTO.toPXON());
    }
  }
}
