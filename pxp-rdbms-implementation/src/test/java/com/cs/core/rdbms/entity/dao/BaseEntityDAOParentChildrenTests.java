package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
public class BaseEntityDAOParentChildrenTests extends AbstractRDBMSDriverTests {
  
  IBaseEntityDAO baseEntityDao = null;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void getParent() throws RDBMSException, CSFormatException
  {
    printTestTitle("getParent");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100010);
    BaseEntityDTO parent = (BaseEntityDTO) baseEntityDao.getParent();
    printJSON("Parent => ", parent);
    assert (parent.getIID() == 100005);
  }
  
  @Test
  public void getChildren() throws RDBMSException, CSFormatException
  {
    printTestTitle("getChildren");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    List<IBaseEntityIDDTO> children = baseEntityDao.getChildren(EmbeddedType.CONTEXTUAL_CLASS);
    for (IBaseEntityIDDTO child : children) {
      println("Child: " + child.getBaseEntityID() + " / " + child.toPXON());
    }
    assert (children != null);
  }
  
  @Test
  public void addChildren() throws RDBMSException, CSFormatException
  {
    printTestTitle("addChildren");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    BaseEntityDTO newChild = DataTestUtils.createRandomBaseEntityWithName("Child");
    println("\tAdded iid:" + newChild.getBaseEntityIID());
    baseEntityDao.addChildren(EmbeddedType.CONTEXTUAL_CLASS, newChild);
    List<IBaseEntityIDDTO> children = baseEntityDao.getChildren(EmbeddedType.CONTEXTUAL_CLASS);
    for (IBaseEntityIDDTO child : children) {
      println("Child: " + child.getBaseEntityID() + " / " + child.toPXON());
    }
    assert (children.size() > 0);
  }
  
  @Test
  public void removeChildren() throws RDBMSException
  {
    printTestTitle("removeChildren");
    baseEntityDao = DataTestUtils.openBaseEntityDAO(100025);
    BaseEntityIDDTO oldChild = DataTestUtils.getBaseEntityDTO(100033);
    println("\tRemoved iid: 100033");
    baseEntityDao.removeChildren(EmbeddedType.CONTEXTUAL_CLASS, oldChild);
  }
}
