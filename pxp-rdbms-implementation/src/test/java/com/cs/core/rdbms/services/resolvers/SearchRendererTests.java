package com.cs.core.rdbms.services.resolvers;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class SearchRendererTests extends AbstractRDBMSDriverTests {
  
  @Test
  public void renderSearchEntities() throws RDBMSException, CSFormatException
  {
    List<Long> baseEntityIID = new ArrayList<>();
    baseEntityIID.add(100005L);
    baseEntityIID.add(100006L);
    ICSESearch search = (new CSEParser()).parseSearch(
        "select $ctlg= pim|onboarding $localeID=en_US|en_UK $entity is [c>single_article] "
            + "where  [ShortDescription] = 'short description of screen DELL W530XP' and [nameattribute] contains 'mouse'");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          SearchRenderer renderer = new SearchRenderer( currentConn, search);
          List<BaseEntityDTO> renderSearchEntities = renderer.renderSearchEntities(baseEntityIID);
          for (BaseEntityDTO baseEntity : renderSearchEntities) {
            printJSON(baseEntity);
          }
        });
  }
  
  @Test
  public void getOrderByExpression() throws RDBMSException, CSFormatException
  {
    ICSESearch search = (new CSEParser()).parseSearch(
        "select $ctlg= pim|onboarding $localeID=en_US|en_UK $entity is [c>single_article] "
            + "where  [ShortDescription] = 'short description of screen DELL W530XP' and [nameattribute] contains 'mouse'");
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          SearchRenderer renderer = new SearchRenderer( currentConn, search);
          String baseQuery = "select * from tmpScope001";
          Map<String, OrderDirection> orderBy = new HashMap<>();
          String orderByExpression = renderer.getOrderByExpression(baseQuery, orderBy, false, "");
          assert (orderByExpression.equals(baseQuery));
          
          
          orderBy.put("nameattribute", OrderDirection.ASC);
          orderBy.put("shortdescriptionattribute", OrderDirection.DESC);
          orderByExpression = renderer.getOrderByExpression(baseQuery, orderBy, false, "");
          assert (orderByExpression.equals("select * from tmpScope001 left join pxp.valen_USen_UK a_0 on (a_0.entityiid = a.baseentityiid and a_0.propertyiid = 200)"
              + "  left join pxp.valen_USen_UK a_1 on (a_1.entityiid = a.baseentityiid and a_1.propertyiid = 208)  order by a_0.value ASC,a_1.value DESC"));
          
          orderBy.clear();
          orderBy.put("createdonattribute", OrderDirection.ASC);
          orderBy.put("shortdescriptionattribute", OrderDirection.ASC);
          orderByExpression = renderer.getOrderByExpression(baseQuery, orderBy, false, "");
          assert (orderByExpression.equals("select * from tmpScope001 left join baseentity be where a.baseentityiid = be.baseentityiid  "
              + "left join pxp.valen_USen_UK a_0 on (a_0.entityiid = a.baseentityiid and a_0.propertyiid = 208)  order by createtime ASC,a_0.value ASC"));
          
          orderBy.clear();
          String queryWithSelectField = renderer.getQueryWithSelectField(true, baseQuery);
          orderBy.put("createdonattribute", OrderDirection.DESC);
          orderBy.put("lastmodifiedattribute", OrderDirection.DESC);
          orderByExpression = renderer.getOrderByExpression(queryWithSelectField, orderBy, false, "");
          assert (orderByExpression.equals("select * from tmpScope001 left join baseentity be where a.baseentityiid = be.baseentityiid "
              + " order by createtime DESC,lastmodifiedtime DESC"));
          
        });
  }
  
  @Test
  public void getPropertyCount() throws RDBMSException, CSFormatException 
  {
    ICSESearch search = (new CSEParser()).parseSearch(
        "select $ctlg= pim|onboarding $locale=en_US");
    List<String> inheritanceSchema = new ArrayList<>();
    inheritanceSchema.add("en_US");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      SearchRenderer sr = new SearchRenderer( currentConn, search);
      //USECASE 1: NO ATTRIBUTE  OR TAG PRESENT IN DTO
      List<IPropertyDTO> propertyDTOs = new ArrayList<>();
      List<IValueCountDTO> propertyCount = sr.getPropertyCount(false, propertyDTOs, inheritanceSchema); 
      assert(propertyCount.isEmpty());
      //USECASE 1: GET COUNT FOR ATTRIBUTE:
      IPropertyDTO name = ConfigurationDAO.instance().getPropertyByCode("nameattribute");
      propertyDTOs.add(name);
      propertyCount = sr.getPropertyCount(false, propertyDTOs, inheritanceSchema); 
      assert(propertyCount.get(0).getValueVScount().size() >= 22);
      
      //USECASE 2: GET COUNT FOR TAGS
      propertyDTOs.clear();
      IPropertyDTO color = ConfigurationDAO.instance().getPropertyByCode("Colors");
      propertyDTOs.add(color);
      propertyCount = sr.getPropertyCount(false, propertyDTOs, inheritanceSchema); 
      assert(propertyCount.get(0).getValueVScount().size() >= 1);
      
      //USECASE 3: GET COUNT FOR ATTRIBUTES AND TAGS
      IPropertyDTO shortDes = ConfigurationDAO.instance().getPropertyByCode("ShortDescription");
      propertyDTOs.add(shortDes);
      propertyDTOs.add(name);
      propertyCount = sr.getPropertyCount(false, propertyDTOs, inheritanceSchema); 
      assert(propertyCount.get(0).getValueVScount().size() >= 1);
      assert(propertyCount.get(1).getValueVScount().size() >= 9);
      assert(propertyCount.get(2).getValueVScount().size() >= 22);

      //USECASE 1: GET COUNT FOR ATTRIBUTE:
      propertyCount = sr.getPropertyCount(true, propertyDTOs, inheritanceSchema); 
      assert(propertyCount.get(0).getValueVScount().size() >= 2);
      assert(propertyCount.get(1).getValueVScount().size() >= 11);
      assert(propertyCount.get(2).getValueVScount().size() >= 25);
    });
  }

  @Test
  public void getTaxonomyCount() throws RDBMSException, CSFormatException
  {
    ICSESearch search = (new CSEParser()).parseSearch(
        "select $ctlg= pim|onboarding $locale=en_US");
    List<String> inheritanceSchema = new ArrayList<>();
    inheritanceSchema.add("en_US");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      SearchRenderer sr = new SearchRenderer( currentConn, search);
      Map<String, Long> count = sr.getTaxonomyCount(false, List.of("tax1", "tax1-2"));

      assert(count.get("tax1").equals(5l));
      assert(count.get("tax1-2").equals(2l));
    });
  }

  @Test
  public void getClassCount() throws RDBMSException, CSFormatException
  {
    ICSESearch search = (new CSEParser()).parseSearch(
        "select $ctlg= pim|onboarding $locale=en_US");
    List<String> inheritanceSchema = new ArrayList<>();
    inheritanceSchema.add("en_US");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      SearchRenderer sr = new SearchRenderer( currentConn, search);
      Map<String, Long> article = sr.getClassCount(false, List.of("Cross-Selling", "Mobile-Article"));
      assert(article.get("Cross-Selling").equals(4l));
      assert(article.get("Mobile-Article").equals(5l));

    });
  }

}
