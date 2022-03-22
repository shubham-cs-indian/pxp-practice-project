package com.cs.core.rdbms.process.dao;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * supported for PGSQL DB only
 *
 * @author PankajGajjar
 */
public class LocaleCatalogDAOTests extends AbstractRDBMSDriverTests {
  
  List<String> localeInheritance = new ArrayList<>();
  
  @Before
  @Override
  public void init() throws RDBMSException
  {
    super.init();
    // Must overwrite here the catalog created at super level to test correctly
    // locale inheritance schemas
    localeCatalogDto = userSession.newLocaleCatalogDTO("fr_FR", "pim", IStandardConfig.STANDARD_ORGANIZATION_CODE);
    localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
    // Initialize a locale inheritance schema:
    localeInheritance.add("fr_FR"); // Purposefully enter french here for test
    localeInheritance.add("fr_CA");
    localeInheritance.add("en_CA");
    localeInheritance.add("en_US");
    localeCatalogDao.applyLocaleInheritanceSchema(localeInheritance);
  }
  
  @Test
  public void getEntityByID() throws RDBMSException, CSFormatException
  {
    // TopParent info is returning
    System.out.println("\nLocaleCatalogDAOTests getEntityByID TEST:");
    String objectID = "Plugable-Universal-Docking-Station-Ethernet";
    
    // GetBaseEntityDAO Interface - > LocaleCatalogDAO Factor
    IBaseEntityDTO dto = localeCatalogDao.getEntityByID(objectID);
    
    System.out.println("Return base entity = " + dto != null ? dto.toPXON() : "<null>");
    assert (dto != null);
    assert (dto.getBaseEntityID()
        .equals(objectID)); // fill by first view
    assert (dto.getBaseEntityName() != null); // fill by second view
  }
  
  @Test
  public void getAllEntities() throws RDBMSException, CSFormatException
  {
    printTestTitle("getAllEntities:");
    IRDBMSOrderedCursor<IBaseEntityDTO> curs = localeCatalogDao
        .getAllEntities(IBaseEntityIDDTO.BaseType.ARTICLE);
    System.out.println("Found : " + curs.getCount() + " articles in catalog");
    List<IBaseEntityDTO> entities = curs.getNext(0, 3);
    System.out.println("\n3 First articles in bulk order:");
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);
    
    Map<String, OrderDirection> orderBy = new HashMap<>();
    orderBy.put("nameattribute", OrderDirection.ASC);
    curs.setOrderBy(orderBy);
    
    entities = curs.getNext(0, 5);
    System.out.println("\n5 First articles by name order:");
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);
    
    entities = curs.getNext(6, 10);
    System.out.println("\n10 next articles by name order:");
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);
  }
  
  @Test
  public void getAllEntitiesByNameLike() throws RDBMSException, CSFormatException
  {
    printTestTitle("getAllEntitiesByNameLike:");
    localeCatalogDao.getLocaleCatalogDTO().setLocaleID("en_US");
    String nameLike = "Xiao";
    IRDBMSOrderedCursor<IBaseEntityDTO> curs = localeCatalogDao.getAllEntitiesByNameLike(IBaseEntityIDDTO.BaseType.ARTICLE, nameLike);
    System.out.println("Found : " + curs.getCount() + "  articles in catalog ");
    assert ( curs.getCount() >= 5 );
    
    List<IBaseEntityDTO> entities = curs.getNext(0, 3);
    for (IBaseEntityIDDTO dto : entities)
      printJSON(dto);
    assert (3 == entities.size());
    
    Map<String, OrderDirection> orderBy = new HashMap<>();
    orderBy.put("nameattribute", OrderDirection.ASC);
    curs.setOrderBy(orderBy);
    entities = curs.getNext(0, 5);
    
    for (IBaseEntityIDDTO dto : entities)
      printJSON(dto);
    assert (5 == entities.size());
  }
  
  /*@Test
  public void getAllEntitiesBySearchText() throws RDBMSException, CSFormatException
  {
    printTestTitle("getAllEntitiesBySearchText:");
    String searchText = "Redmi";
    IRDBMSOrderedCursor<IBaseEntityDTO> curs = localeCatalogDao
        .getAllEntitiesByText(IBaseEntityIDDTO.BaseType.ARTICLE, searchText);
    System.out.println("Found : " + curs.getCount() + "  articles in catalog ");
    assert (curs.getCount() > 1);
    
    System.out.println("3 first articles found ");
    List<IBaseEntityDTO> entities = curs.getNext(0, 3);
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);
    assert (entities.size() >= 2);
    
    System.out.println("5 found order by article name");
    
    Map<String, OrderDirection> orderBy = new HashMap<>();
    orderBy.put("articlename", OrderDirection.ASC);
    curs.setOrderBy(orderBy);
    entities = curs.getNext(0, 5);
    
    entities = curs.getNext(0, 5);
    for (IBaseEntityIDDTO dto : entities)
      printJSON(String.format("Contains '%s'", searchText), dto);
    assert (entities.size() >= 2);
  }*/
  
  @Test
  public void getEntityByIID() throws RDBMSException, CSFormatException
  {
    printTestTitle("getEntityByIID:");
    IBaseEntityDTO entity = localeCatalogDao.getEntityByIID(100002);
    assert (!entity.isNull());
    printJSON("Entity from another catalog", entity);
    entity = localeCatalogDao.getEntityByIID(100010);
    assert (!entity.isNull());
    printJSON("Child entity", entity);
    entity = localeCatalogDao.getEntityByIID(110001);
    assert (!entity.isNull());
    printJSON("Relationship extension", entity);
  }
  
  @Test
  public void getAllEntitiesFilterByClass() throws RDBMSException, CSFormatException
  {
    printTestTitle("getAllEntitiesFilterByClass: getAll");
    String searchExpression = "select $basetype = $article $ctlg=pim $entity in [c>PC-Environmental-Characteristics] and $entity in [c>Screen]";
    IRDBMSOrderedCursor<IBaseEntityDTO> curs = localeCatalogDao.getAllEntitiesBySearchExpression(searchExpression);

    List<IBaseEntityDTO> entities = curs.getNext(0, 5);
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);
    System.out.println(curs.getCount());
    assert (curs.getCount() == entities.size());
    
    printTestTitle("getAllEntitiesFilterByClass : getAllEntitiesByNameLike");
    
    searchExpression = String.format("select $basetype = $article $ctlg=pim $locale=en_US $entity in [c>PC-Environmental-Characteristics] and $entity in [c>Screen] "
        + "where [nameattribute] like '%s'", "%Screen%");

		IRDBMSOrderedCursor<IBaseEntityDTO> cursor = localeCatalogDao
				.getAllEntitiesBySearchExpression(searchExpression);

    List<IBaseEntityDTO> entitiesByNameLike = cursor.getNext(0, 3);
    for (IBaseEntityDTO dto : entitiesByNameLike)
      printJSON(dto);
    
    assert (cursor.getCount() == entitiesByNameLike.size());
    assert(entitiesByNameLike.size() == 2);
    System.out.println(cursor.getCount());
  }
  
  @Test
  public void getAllEntitiesFilterByClassForRelation() throws RDBMSException, CSFormatException
  {
    printTestTitle("getAllEntitiesFilterByClass For relation: getAllEntities");
    String searchExpression = "select $basetype=$article $ctlg= pim|onboarding $localeID=en_US " +
        "$entity belongsto [e>Helelo $iid=100005].[Similar-items $side=1] ";

    IRDBMSOrderedCursor<IBaseEntityDTO> curs = localeCatalogDao.getAllEntitiesBySearchExpression(searchExpression);

    List<IBaseEntityDTO> entities = curs.getNext(0, 10);
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);

    println("After Relation filter by attach entity count = " + curs.getCount());
    assert (3 == entities.size());

    searchExpression = "select $basetype=$article $ctlg= pim|onboarding $localeID=en_US " +
        "$entity belongsto [e>Helelo $iid=100005].[Similar-items $side=1].complement ";
    curs = localeCatalogDao.getAllEntitiesBySearchExpression(searchExpression);
    entities = curs.getNext(0, 10);
    for (IBaseEntityDTO dto : entities)
      printJSON(dto);

    println("After Relation filter by attach entity count = " + curs.getCount());
    assert (10 == entities.size());

  }

  @Test
  public void getLinkedVariantIds() throws RDBMSException
  {
    printTestTitle("GET LINKED VARIANTS");
    IBaseEntityDTO entityByIID = localeCatalogDao.getEntityByIID(100007);
    Map<Long, Long> linkedVariantIds = localeCatalogDao.getLinkedVariantIds(Arrays.asList(entityByIID), Arrays.asList("Family-product"));
    assert(linkedVariantIds.get(100007l).equals(100006l));

    //negative test
    IBaseEntityDTO entityByIID2 = localeCatalogDao.getEntityByIID(100013);
    linkedVariantIds = localeCatalogDao.getLinkedVariantIds(Arrays.asList(entityByIID2), Arrays.asList("Similiar-Items"));
    assert(linkedVariantIds.isEmpty());

    IBaseEntityDTO entityByIID3 = localeCatalogDao.getEntityByIID(100014);
    linkedVariantIds = localeCatalogDao.getLinkedVariantIds(Arrays.asList(entityByIID,entityByIID2, entityByIID3), Arrays.asList("Family-product", "Relation#5008"));
    assert(linkedVariantIds.get(100007l).equals(100006l));
    assert(linkedVariantIds.get(100014l).equals(1000000l));
    assert(linkedVariantIds.get(100013l).equals(1000000l));
  }

  @Test
  public void getExistingBaseEntityIDs() throws RDBMSException
  {
    printTestTitle("GET EXISTING ENTITIES");
    List<String> existingBaseEntityIDs = LocaleCatalogDAO.getExistingBaseEntityIDs(List.of("B07CVL2D2S", "ART1048"));
    assert(existingBaseEntityIDs.contains("B07CVL2D2S"));
  }

  @Test
  public void getBaseEntitiesByIIDs() throws RDBMSException, CSFormatException
  {
    printTestTitle("GET Base EntitiesBy IID");
    println("FOR FRENCH LANGUAGE");
    List<IBaseEntityDTO> entities = localeCatalogDao.getBaseEntitiesByIIDs(Arrays.asList("100004" , "100005" , "100006" , "100007" , "100008" , "100009" , "100001" , "100010" , "100011"));
    for(IBaseEntityDTO baseEntity : entities){
      printJSON(baseEntity);
    }
    println("FOR CANADIAN FRENCH LANGUAGE");
    localeCatalogDao.getLocaleCatalogDTO().setLocaleID("fr_CA");
    entities = localeCatalogDao.getBaseEntitiesByIIDs(Arrays.asList("100004" , "100005" , "100006" , "100007" ,"100002","100003"));
    for(IBaseEntityDTO baseEntity : entities){
      printJSON(baseEntity);
    }

  }

}
