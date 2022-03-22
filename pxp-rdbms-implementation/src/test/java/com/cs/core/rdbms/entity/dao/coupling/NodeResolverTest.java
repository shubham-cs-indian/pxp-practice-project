package com.cs.core.rdbms.entity.dao.coupling;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.services.resolvers.NodeResolver;
import com.cs.core.rdbms.services.resolvers.NodeResolver.CouplingMaster;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
public class NodeResolverTest extends AbstractRDBMSDriverTests {
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void getClassifierCouplingMaster() throws CSFormatException, RDBMSException
  {
    printTestTitle("getClassifierCouplingMaster");
    IPropertyDTO nameProp = localeCatalogDao.newPropertyDTO(200, "nameattribute",
        IPropertyDTO.PropertyType.TEXT);
    IPropertyDTO modelProp = localeCatalogDao.newPropertyDTO(2018, "Model-Name",
        IPropertyDTO.PropertyType.TEXT);
    
    ICSECoupling coupling1 = (new CSEParser())
        .parseCoupling("[c>Electronics]|.[Model-Name]");
    ICSECoupling coupling2 = (new CSEParser())
        .parseCoupling("$nature&.[nameattribute]");
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          NodeResolver resolver = new NodeResolver( connection, localeCatalogDao);
          CouplingMaster master1 = resolver.getClassifierCouplingMaster(coupling1.getSource(),
              100005, modelProp);
          println(
              String.format("Source %s, master ID %s", coupling1.getSource(), master1.getNodeID()));
          assert (!master1.getNodeID()
              .isEmpty());
          CouplingMaster master2 = resolver.getClassifierCouplingMaster(coupling2.getSource(),
              100005, nameProp);
          println(
              String.format("Source %s, master ID %s", coupling2.getSource(), master2.getNodeID()));
          assert (!master2.getNodeID()
              .isEmpty());
        });
  }
  
  @Test
  public void getSourceBaseEntityIID() throws CSFormatException, RDBMSException
  {
    printTestTitle("getSourceBaseEntityIID");
    ICSECoupling coupling1 = (new CSEParser())
        .parseCoupling("[e>B07CVL2D2S]|.[nameattribute]");
    ICSECoupling coupling2 = (new CSEParser())
        .parseCoupling("$source|.[nameattribute]");
    ICSECoupling coupling3 = (new CSEParser())
        .parseCoupling("$origin|.[nameattribute]");
    ICSECoupling coupling4 = (new CSEParser())
        .parseCoupling("$parent|.[nameattribute]");
    ICSECoupling coupling5 = (new CSEParser())
        .parseCoupling("$top|.[nameattribute]");
    ICSECoupling coupling6 = (new CSEParser())
        .parseCoupling("[Similar-items $side=1]|.[nameattribute]");
    ICSECoupling coupling7 = (new CSEParser())
        .parseCoupling("[$iid=7003 $side=2]|.[nameattribute]");
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          NodeResolver resolver = new NodeResolver( connection, localeCatalogDao);
          long iid1 = resolver.getSourceBaseEntityIID(coupling1.getSource(), 100007);
          println("Entity by ID source -> " + iid1 + " from target -> " + 100007);
          assert (iid1 == 100006);
          long iid2 = resolver.getSourceBaseEntityIID(coupling2.getSource(), 100037);
          println("Entity by source catalog -> " + iid2 + " from target -> " + 100037);
          assert (iid2 == 100036);
          long iid3 = resolver.getSourceBaseEntityIID(coupling3.getSource(), 100038);
          println("Entity by clone origin -> " + iid3 + " from target -> " + 100038);
          assert (iid3 == 100037);
          long iid4 = resolver.getSourceBaseEntityIID(coupling4.getSource(), 100010);
          println("Entity by parent -> " + iid4 + " from target -> " + 100010);
          assert (iid4 == 100005);
          long iid5 = resolver.getSourceBaseEntityIID(coupling5.getSource(), 100010);
          println("Entity by top parent -> " + iid5 + " from target -> " + 100010);
          assert (iid5 == 100005);
          long iid6 = resolver.getSourceBaseEntityIID(coupling6.getSource(), 100010);
          println("Entity by relationship side 1 -> " + iid6 + " from target -> " + 100010);
          assert (iid6 == 100005);
          long iid7 = resolver.getSourceBaseEntityIID(coupling7.getSource(), 100005);
          println("Entity by relationship side 2 -> " + iid7 + " from target -> " + 100005);
          assert (iid7 == 100010);
        });
  }
  
  @Test
  public void getEntityCouplingMaster() throws CSFormatException, RDBMSException
  {
    printTestTitle("getEntityCouplingMaster");
    IPropertyDTO nameProp = localeCatalogDao.newPropertyDTO(200, "nameattribute",
        IPropertyDTO.PropertyType.TEXT);
    ICSECoupling coupling1 = (new CSEParser())
        .parseCoupling("[Similar-items $side=2]|.[nameattribute]");
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          NodeResolver resolver = new NodeResolver( connection, localeCatalogDao);
          CouplingMaster master1 = resolver.getEntityCouplingMaster(coupling1.getSource(), 100005,
              nameProp);
          println(String.format("Source %s, master signature %s", coupling1.getSource(),
              master1.getNodeID()));
          assert (master1.getEntityIID() > 0L);
        });
  }
  
  @Test
  public void getRelationCouplingMaster() throws RDBMSException, CSFormatException
  {
    printTestTitle("getRelationCouplingMaster");
    IPropertyDTO nameProp = localeCatalogDao.newPropertyDTO(200, "nameattribute",
        IPropertyDTO.PropertyType.TEXT);
    ICSEElement relation1 = (new CSEParser())
        .parseDefinition("[Similar-items $side=1]");
    ICSEElement relation2 = (new CSEParser())
        .parseDefinition("[Similar-items $side=2]");
    ICSEElement relation3 = (new CSEParser())
        .parseDefinition("[7003 $side=2]");
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection connection) -> {
          NodeResolver resolver = new NodeResolver( connection, localeCatalogDao);
          CouplingMaster master1 = resolver.getRelationCouplingMaster((ICSEProperty) relation1,
              100010, nameProp);
          println(String.format("Relation %s, master ID %s", relation1, master1.getNodeID()));
          assert (master1.getEntityIID() > 0L);
          CouplingMaster master2 = resolver.getRelationCouplingMaster((ICSEProperty) relation2,
              100005, nameProp);
          println(String.format("Relation %s, master ID %s", relation2, master2.getNodeID()));
          assert (master2.getEntityIID() > 0L);
          CouplingMaster master3 = resolver.getRelationCouplingMaster((ICSEProperty) relation3,
              100005, nameProp);
          println(String.format("Relation %s, master ID %s", relation3, master3.getNodeID()));
          assert (master3.getEntityIID() > 0L);
        });
  }
}
