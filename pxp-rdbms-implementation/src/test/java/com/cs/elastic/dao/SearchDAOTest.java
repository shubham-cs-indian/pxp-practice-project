package com.cs.elastic.dao;

import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elatic.dao.SearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.entity.dto.ClassifierAggregationDTO;
import com.cs.core.rdbms.entity.dto.PropertyAggregationDTO;
import com.cs.core.rdbms.entity.dto.SearchDTO;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO.AggregationType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISortDTO.SortOrder;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAOTests;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchDAOTest extends AbstractRDBMSDriverTests {

  @Test
  public void testClassifierAggregations() throws IOException
  {
    ISearchDTOBuilder searchDTOBuilder = localeCatalogDao.getSearchDTOBuilder(List.of(BaseType.ARTICLE),
        List.of(new SortDTO("lastmodifiedattribute", SortOrder.desc, true)));

    ISearchDTO searchDTO = searchDTOBuilder.build();

    ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);

    IClassifierAggregationDTO  requestDTO =  new ClassifierAggregationDTO();
    requestDTO.setClassifierIds(List.of("single_article"));
    requestDTO.setAggregationType(AggregationType.byClass);
    requestDTO.setSize(20);

    IAggregationResultDTO aggregation = searchDAO.aggregation(requestDTO);
    Long single_article = aggregation.getCount().get("single_article");
    System.out.println(single_article);
  }

  @Test
  public void testPropertyAggregations() throws IOException, RDBMSException
  {
    ISearchDTOBuilder searchDTOBuilder = localeCatalogDao.getSearchDTOBuilder(List.of(BaseType.ARTICLE),
        List.of(new SortDTO("lastmodifiedattribute", SortOrder.desc, true)));

    ISearchDTO searchDTO = searchDTOBuilder.build();

    localeCatalogDao.getLocaleCatalogDTO().setLocaleID("de_DE");
    ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);

    IPropertyAggregationDTO requestDTO = new PropertyAggregationDTO(ConfigurationDAO.instance().getPropertyByCode("nameattribute"), true);
    requestDTO.setAggregationType(AggregationType.byProperty);
    requestDTO.setSize(20);
    requestDTO.setBucketSearch("3");
    IAggregationResultDTO aggregation = searchDAO.aggregation(requestDTO);
    System.out.println(aggregation.getCount());

    IPropertyAggregationDTO requestDTO2 = new PropertyAggregationDTO(ConfigurationDAO.instance().getPropertyByCode("lastmodifiedattribute"), false);
    requestDTO2.setAggregationType(AggregationType.byProperty);
    requestDTO2.setSize(20);
     aggregation = searchDAO.aggregation(requestDTO2);
    System.out.println(aggregation.getCount());

  }
}
