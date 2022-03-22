package com.cs.core.printer;

import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Convenient printing functions, more test unit oriented
 *
 * @author vallee
 */
public class QuickPrinter {

  private static PrintStream printOut = System.out;

  /**
   * @param dto a DTO to be quick printed
   * @throws CSFormatException
   */
  public static void printJSON(ISimpleDTO dto) throws CSFormatException {
    String[] classPath = dto.getClass()
            .getName()
            .split("[.]");
    printOut.println(classPath[classPath.length - 1] + ": " + dto.toJSON());
  }

  /**
   * @param objectName a title name to that DTO
   * @param dto a DTO to be quick printed
   * @throws CSFormatException
   */
  public static void printJSON(String objectName, ISimpleDTO dto) throws CSFormatException {
    String[] classPath = dto.getClass()
            .getName()
            .split("[.]");
    printOut.println(objectName + " (" + classPath[classPath.length - 1] + "): " + dto.toJSON());
  }

  /**
   * @param listName a title name to that list
   * @param content a content of JSONs
   * @throws CSFormatException
   */
  public static void printJSON(String listName, Collection<IJSONContent> content)
          throws CSFormatException {
    JSONContent output = new JSONContent();
    Iterator<IJSONContent> itr = content.iterator();
    for (int i = 1; itr.hasNext(); i++) {
      output.setField("" + i, itr.next());
    }
    printOut.println(listName + ": " + output.toString());
  }

  /**
   * @param message printed message
   */
  public static void print(String message) {
    printOut.print(message);
  }

  /**
   * @param format printed format
   * @param args printing arguments
   */
  public static void printf(String format, Object... args) {
    printOut.print(String.format(format, args));
  }

  /**
   * @param message printed message with new line
   */
  public static void println(String message) {
    printOut.println(message);
  }

  /**
   * @param title test title to print
   */
  public static void printTestTitle(String title) {
    printOut.println("\nSTART TEST " + title);
  }

  /**
   * The out stream is by default System.out
   *
   * @param out any stream for redirection
   */
  public void redirect(PrintStream out) {
    printOut = out;
  }
}
