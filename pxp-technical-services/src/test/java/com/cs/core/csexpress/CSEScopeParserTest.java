package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import org.junit.Test;

/**
 * @author vallee
 */
public class CSEScopeParserTest extends QuickPrinter {

  @Test
  public void simpleFilters() throws CSFormatException {
    printTestTitle("simpleFilters");
    println("Test simple filter 1: " + (new CSEParser())
            .parseEntityFilter("$parent is $nature"));
    ICSEEntityFilterNode filter2 = (new CSEParser())
            .parseEntityFilter("$entity contains [Description] | [ShortDesc]");
    println("Test simple filter 2: " + filter2);
    println("Contained properties: " + filter2.getContainedProperties());
    ICSEEntityFilterNode filter3 = (new CSEParser())
            .parseEntityFilter("$top under [c>SuperMarket] | [c>HyperMarket]");
    println("Test simple filter 3: " + filter3);
    println("Including classifiers: " + filter3.getIncludingClassifiers());
    ICSEEntityFilterNode filter4 = (new CSEParser())
            .parseEntityFilter("not ( $entity under [c>Foods] | [c>HyperMarket] )");
    println("Test simple filter 4: " + filter4);
    println("Including classifiers: " + filter4.getIncludingClassifiers());
    println("Excluding classifiers: " + filter4.getExcludedClassifiers());
    ICSEEntityFilterNode filter5 = (new CSEParser())
            .parseEntityFilter("not ( $entity has [X>Persona] | [X>Country] )");
    println("Test simple filter 5: " + filter5);
    ICSEEntityFilterNode filter6 = (new CSEParser())
            .parseEntityFilter("$parent is $nature xor $parent.quality = ($yellow | $red)");
    println("Test simple filter 6: " + filter6);
    println("Test simple filter 7: " + (new CSEParser())
        .parseEntityFilter("$entity belongsto [e>Article-Sample].[Similar-Item $side=1].complement"));
    println("Test simple filter 8: " + (new CSEParser())
        .parseEntityFilter("$entity belongsto [b>collectionIId]"));
    println("Test simple filter 8: " + (new CSEParser())
            .parseEntityFilter("$entity in $empty"));

  }

  @Test
  public void compoundFilters() throws CSFormatException {
    printTestTitle("compoundFilters");
    ICSEEntityFilterNode filter1 = (new CSEParser())
            .parseEntityFilter("$entity is [c>simplearticle] and $entity in [c>Foods]");
    println("Test coumpond filter 1: " + filter1);
    println("Including classifiers: " + filter1.getIncludingClassifiers());
    println("Excluding classifiers: " + filter1.getExcludedClassifiers());
    println("Test coumpond filter 2: " + (new CSEParser())
            .parseEntityFilter("not($entity is [c>simplearticle] and $entity in [c>Foods])"));
    println("Test coumpond filter 3: " + (new CSEParser())
            .parseEntityFilter(
                    "$entity is [c>simplearticle] xor $entity in [c>Foods] and $entity is [c>simplearticle] or $parent under $nature"));
    ICSEEntityFilterNode filter4 = (new CSEParser())
            .parseEntityFilter(" $entity is [c>simplearticle] and not ($parent in [c>icecreams]) ");
    println("Test coumpond filter 4: " + filter4);
    println("Including classifiers: " + filter4.getIncludingClassifiers());
    println("Excluding classifiers: " + filter4.getExcludedClassifiers());
  }

  @Test
  public void scopes() throws CSFormatException {
    printTestTitle("scopes");
    println("Test scope 1: " + (new CSEParser())
            .parseScope(
                    "$basetype=$article|$asset $ctlg= pim|onboarding $org=contentserv $locale=$baselocale $entity is [c>simplearticle] and not $parent in [c>Foods]"));
    println("Test scope 2: " + (new CSEParser())
            .parseScope(
                    "$basetype=$article $ctlg= pim ($entity is [c>simplearticle] or $parent is [c>simplearticle]) and ( $entity contains [Alergens] or $parent under [c>Alergens] )"));
    println("Test scope 3: " + (new CSEParser())
            .parseScope(
                    "$basetype = $asset $ctlg=pim"));

    ICSEScope scope5 = (new CSEParser()).parseScope(" $basetype = $article $ctlg = onboarding $org = contentserv ");
    println("Test scope 5: " + scope5);

    ICSEScope scope4 = (new CSEParser()).parseScope(" $ctlg = onboarding $org = contentserv | stibo ");
    println("Test scope 4: " + scope4);
    ICSEScope scope6 = (new CSEParser()).parseScope(" $ctlg = onboarding $org = contentserv | stibo $endpoint = yellowShiz | redShiz ");
    println("Test scope 6: " + scope6);
  }

  @Test
  public void jsonFilters() throws CSFormatException {
    printTestTitle("jsonFilters");
    println("Test simple filter 1: " + (new CSEParser())
            .parseEntityFilter("$parent is $nature")
            .toJSON());
    println("Test simple filter 2: " + (new CSEParser())
            .parseEntityFilter("$entity contains [Description] | [ShortDesc]")
            .toJSON());
    println("Test coumpound filter 1: " + (new CSEParser())
            .parseEntityFilter("$entity is [c>simplearticle] and $entity in [c>Foods]")
            .toJSON());
    println("Test coumpound filter 2: " + (new CSEParser())
            .parseEntityFilter("not($entity is [c>simplearticle] and $entity in [c>Foods])")
            .toJSON());
    println("Test coumpound filter 3: " + (new CSEParser())
            .parseEntityFilter(
                    "$entity is [c>simplearticle] xor $entity in [c>Foods] $entity is [c>simplearticle] or $parent under $nature")
            .toJSON());
    println("Test coumpound filter 4: " + (new CSEParser())
            .parseEntityFilter("$entity is [c>simplearticle] and not($parent in [c>icecreams])")
            .toJSON());
  }
  
  @Test
  public void contextScopesFilters() throws CSFormatException {
    printTestTitle("contextScopesFilters");
    
    ICSEEntityFilterNode filter1 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona] | [X>Country] )");
    println("Test context Filter 1: " + filter1);
    
    ICSEEntityFilterNode filter2 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona] | [X>Country $end=2020-02-24] )");
    println("Test context Filter 2: " + filter2);
    
    ICSEEntityFilterNode filter3 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona $end=2020-02-23] | [X>Country $start=2020-02-25] )");
    println("Test context Filter 3: " + filter3);
    
    ICSEEntityFilterNode filter4 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona $start=2020-02-23] | [X>Country $start=2020-02-25] )");
    println("Test context Filter 4: " + filter4);
    
    ICSEEntityFilterNode filter5 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona $end=2020-02-23] | [X>Country $end=2020-02-25] )");
    println("Test context Filter 5: " + filter5);
    
    ICSEEntityFilterNode filter6 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona $start=2020-02-23  $end=2020-02-25] | [X>Country $start=2020-02-24] )");
    println("Test context Filter 6: " + filter6);
    
    ICSEEntityFilterNode filter7 = (new CSEParser())
        .parseEntityFilter("not ( $entity has [X>Persona $start=2020-02-23  $end=2020-02-25] | [X>Country $start=2020-02-24 $end=2020-02-29] )");
    println("Test context Filter 7: " + filter7);
  }
  
  @Test
  public void translationScopesFilters() throws CSFormatException {
    printTestTitle("translationScopesFilters");
    
    ICSEEntityFilterNode filter1 = (new CSEParser())
        .parseEntityFilter("$entity existin  [N>en_US] | [N>en_US]");
    println("Test context Filter 1: " + filter1);
    
  }
}
