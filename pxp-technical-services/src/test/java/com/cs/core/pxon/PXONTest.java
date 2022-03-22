package com.cs.core.pxon;

import com.cs.core.json.JSONContent;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Arrays;
import org.junit.Test;

/**
 * Test export / import PXON objects of this module
 *
 * @author vallee
 */
public class PXONTest extends QuickPrinter {

  @Test
  public void writePXON() throws CSFormatException {
    printTestTitle("writePXON");
    UserDTO arthur = new UserDTO(10, "Arthur Bernstein");
    printJSON(arthur);
    SessionDTO session = new SessionDTO("s345674", System.currentTimeMillis());
    printJSON(session);
  }

  @Test
  public void readPXON() throws CSFormatException {
    printTestTitle("readPXON");
    String csonUser = (new UserDTO(10, "Arthur Bernstein")).toPXON();
    UserDTO arthur = new UserDTO();
    arthur.fromJSON(csonUser);
    printJSON("read user PXON: ", arthur);
    String csonSession = (new SessionDTO("s345674", System.currentTimeMillis())).toJSON();
    SessionDTO session = new SessionDTO();
    printJSON("empty session JSON: ", session);
    session.fromJSON(csonSession);
    printJSON("read session JSON: ", session);
  }

  @Test
  public void JSONContentTest() throws CSFormatException {
    printTestTitle("JSONContentTest");
    IJSONContent content1 = new JSONContent();
    content1.setField("A", "a");
    content1.setField("1000", 1000);
    content1.setField("true", true);
    content1.setLongArrayField("array", Arrays.asList(1L, 2L, 3L, 5L, 10L));
    content1.setField("STATUS", IPropertyRecordDTO.RecordStatus.NOTIFIED);
    IJSONContent selfContent = new JSONContent(content1.toString());
    content1.setField("self", selfContent);
    println("content1: " + content1.toString());
    println("A = " + content1.getInitField("A", "<missed>"));
    println("1000 = " + content1.getInitField("1000", -1));
    println("true = " + content1.getInitField("true", false));
    println("array = " + content1.getArrayField("array", Long.class));
    println("STATUS = " + content1.getInitField("STATUS", IPropertyRecordDTO.RecordStatus.class,
            IPropertyRecordDTO.RecordStatus.UNDEFINED));
    println("self = " + content1.getInitField("self", new JSONContent()));
    println("default = " + content1.getInitField("default", "default"));
  }
}
