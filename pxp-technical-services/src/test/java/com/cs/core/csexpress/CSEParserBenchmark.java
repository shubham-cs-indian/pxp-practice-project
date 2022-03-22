package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author vallee
 */
@Ignore
public class CSEParserBenchmark extends QuickPrinter {

  private static final String CSE = "{ @CSEBenchmark='June-29-2019' "
          + "[Price#45234 $iid=1000028 $type=PRICE ]," + "[Long-Description $type=TEXT],"
          + "[Pitch $type=HTML]," + "[1000050 $type=TAG]," + "[Mat#45234 $iid=1000029 $type=TAG]    }";
  private static int SIZE = 10000;

  @Test
  public void run() throws CSFormatException {
    printTestTitle("CSEParserBenchmark run");
    ICSEElement[] results = new ICSEElement[SIZE];
    CSEParser parser = (new CSEParser());
    println(String.format("Starting to parse expression %d times", SIZE));
    long chrono = System.currentTimeMillis();
    for (int i = 0; i < SIZE; i++) {
      results[i] = parser.parseDefinition(CSE);
    }
    long stopChrono = System.currentTimeMillis() - chrono;
    println(String.format("Time to parse %d ms", stopChrono));
    double oneRecordChrono = ((double) stopChrono) / SIZE;
    println(String.format("Time to parse 1 record %.3f ms", oneRecordChrono));
    println("End of parsing: " + CSE);
  }
}
