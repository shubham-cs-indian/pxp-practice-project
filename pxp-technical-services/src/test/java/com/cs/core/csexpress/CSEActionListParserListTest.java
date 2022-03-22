package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import org.junit.Test;

/**
 * @author vallee
 */
public class CSEActionListParserListTest extends QuickPrinter {

  @Test
  public void assignmentActionParser() throws CSFormatException {
    printTestTitle("assignmentActionParser");
    println("assignment list 1: " + (new CSEParser())
            .parseActionList("[Length] := 36, $parent.[Length] := 25 || [Length].unit"));
    println("assignment list 2: " + (new CSEParser())
            .parseActionList("[name] := upper([name])"));
  }

  @Test
  public void classificationActionParser() throws CSFormatException {
    printTestTitle("classificationActionParser");
    println("classification list 1: " + (new CSEParser())
            .parseActionList("$parent => $nature"));
    println("classification list 2: " + (new CSEParser())
            .parseActionList(
                    "$entity => [c>Electronics], $parent => $nature, [e>1000005] => [c>4005]"));
  }

  @Test
  public void qualityFlagActionParser() throws CSFormatException {
    printTestTitle("qualityFlagActionParser");
    println("quality flag list 1: " + (new CSEParser())
            .parseActionList("[ShortDesc] >> $red('empty content!')"));
    println("quality flag list 2: " + (new CSEParser())
            .parseActionList(
                    "[ShortDesc] >> $red('empty content!'), $parent.[Availability] >> $green"));
  }

  @Test
  public void actionListToJSON() throws CSFormatException {
    printTestTitle("actionListToJSON");
    println("assignment list 1: " + (new CSEParser())
            .parseActionList("[Length] := 36, $parent.[Length] := 36 || [Length].unit")
            .toJSON());
    println("classification list 2: " + (new CSEParser())
            .parseActionList("$entity => [c>Electronics], $parent => $nature, [e>1000005] => [c>4005]")
            .toJSON());
    println("quality flag list 2: " + (new CSEParser())
            .parseActionList("[ShortDesc] >> $red('empty content!'), $parent.[Availability] >> $green")
            .toJSON());
  }
}
