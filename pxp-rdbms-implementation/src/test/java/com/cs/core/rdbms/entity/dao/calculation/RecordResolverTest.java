package com.cs.core.rdbms.entity.dao.calculation;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.services.resolvers.RecordResolver;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
public class RecordResolverTest extends AbstractRDBMSDriverTests {
  
  private CSEParser parser;
  private final String    PARENT_RECORDS_COLLECTION   = " $parent.[nameattribute] || "
      + " $top.[Parcel-Length].number || " + " $top.[Parcel-Length].unit || "
      + " $parent.[lastmodifiedattribute].number || " + " $parent.[LongDescription $cxt=Color] || "
      + " [nameattribute]";
  private final String    RELATION_RECORDS_COLLECTION = " [Similar-items $side=1].[ShortDescription] || "
      + " [Similar-items $side=1].[nameattribute]";
  private final String    TAGS_RECORDS_COLLECTION     = "[taskstatustag]";
    
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    parser = (new CSEParser());
  }
  
  @Test
  public void resolveParentRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveParentRecords");
    ICSECalculationNode expression = parser.parseCalculation("= " + PARENT_RECORDS_COLLECTION);
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          RecordResolver resolver = new RecordResolver( connection, localeCatalogDao);
          for (ICSERecordOperand record : expression.getRecords()) {
            ICSELiteralOperand result = resolver.getResult( 100010, record);
            println(String.format("%s -> %s", record.toString(), result));
          }
        });
  }
  
  @Test
  public void resolveRelationshipRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveRelationshipRecords");
    ICSECalculationNode expression = parser.parseCalculation("= " + RELATION_RECORDS_COLLECTION);
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          RecordResolver resolver = new RecordResolver( connection, localeCatalogDao);
          for (ICSERecordOperand record : expression.getRecords()) {
            ICSELiteralOperand result = resolver.getResult( 100011, record);
            println(String.format("%s -> %s", record.toString(), result));
          }
        });
  }
  
  @Test
  public void resolveTagRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveTagRecords");
    try { // initialization section - avoid duplicate exception
      IPropertyDTO propertyTag = ConfigurationDAO.instance()
          .getPropertyByIID(278);
      IBaseEntityDAO baseEntityDao = DataTestUtils.openBaseEntityDAO(100011);
      ITagsRecordDTO taskStatus = baseEntityDao.newTagsRecordDTOBuilder(propertyTag).build();
      ITagDTO tagRecordX = baseEntityDao.newTagDTO(100,
          IStandardConfig.StandardTagValue_taskstatustag.taskdone.toString());
      ITagDTO tagRecordY = baseEntityDao.newTagDTO(25,
          IStandardConfig.StandardTagValue_taskstatustag.taskplanned.toString());
      taskStatus.setTags(tagRecordX, tagRecordY);
      baseEntityDao.createPropertyRecords(taskStatus);
    }
    catch (RDBMSException ex) {
      println("(notice: tag record already initialized)");
    }
    ICSECalculationNode expression = parser.parseCalculation("= " + TAGS_RECORDS_COLLECTION);
    RDBMSConnectionManager.instance().runTransaction(( connection) -> {
          RecordResolver resolver = new RecordResolver( connection, localeCatalogDao);
          for (ICSERecordOperand record : expression.getRecords()) {
            ICSELiteralOperand result = resolver.getResult( 100011, record);
            println(String.format("%s -> %s", record.toString(), result));
          }
        });
  }
}
