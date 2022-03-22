package com.cs.core.rdbms.process.dao;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.scope.CSEScope;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.services.resolvers.SearchResolver;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.SetUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author niraj
 */
public class SearchResolverTest extends AbstractRDBMSDriverTests {
  
  private final CSEParser parser;
  public SearchResolverTest()
  {
    parser = (new CSEParser());
  }
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }

  @Test
  public void entityFilterTest() throws CSFormatException, RDBMSException {
      printTestTitle("entityFilterTest");
      RDBMSConnectionManager.instance()
              .runTransaction((connection) -> {
                  SearchResolver searchResolver = new SearchResolver(connection);
/*          ICSESearch search3 = parser.parseSearch("select $basetype = $article $ctlg=pim $locale=en_US"
              + " $entity is [c>Article] and $entity in [c>Cross-Selling] and  $entity in [c>Peripheral] and $entity in [c>PC-Environmental-Characteristics]");


          Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) search3.getScope());
          Set<Long> expectedResult = new HashSet<>(Arrays.asList(100001l));
          assert (baseEntityIds.containsAll(expectedResult));*/

                  ICSESearch search3 = parser.parseSearch("select $basetype = $article $ctlg=pim $locale=en_US"
                          + " $entity in [c>PC-Environmental-Characteristics]|$empty");

                  Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) search3.getScope());
                  Set<Long> expectedResult = new HashSet<>(Arrays.asList(100001l));
                  assert (baseEntityIds.containsAll(expectedResult));
              });
  }

  // All search
  @Test
  public void contextFilterTest() throws CSFormatException, RDBMSException {
    printTestTitle("CONTEXT FITLER");
    RDBMSConnectionManager.instance()
    .runTransaction(( connection) -> {
      
    ICSESearch context = parser.parseSearch(
            "select $basetype=$article $ctlg = pim|onboarding $localeID=en_US|en_UK $entity has [X> Packaging $iid=100005]");
    
        SearchResolver searchResolver =  new SearchResolver( connection);
        Set<Long> scopeResult = searchResolver.getScopeResult((CSEScope) context.getScope());
        assert(scopeResult.containsAll(Arrays.asList(100010l,100011l, 100039l, 100044l)));
        
        context = parser.parseSearch("select $basetype=$article $ctlg = pim|onboarding $localeID=en_US|en_UK $entity is [c>Article] and $entity has [X>Packaging $iid=100005]");
        scopeResult = searchResolver.getScopeResult((CSEScope) context.getScope());
        assert(scopeResult.containsAll(Arrays.asList(100010l,100011l, 100039l, 100044l)));
    });
  }
 
  // context search with start time and end time only
  @Test
  public void contextFilterTestWithStartAndEndTime() throws CSFormatException, RDBMSException
  {
    printTestTitle("Context filter with start and endTime");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      
      ICSESearch context = parser.parseSearch(
          "select $basetype=$article $ctlg = pim|onboarding $localeID=en_US|en_UK $entity has [X> Packaging $iid=100005 $start=1549219138 $end=1549219138]");
      
      SearchResolver searchResolver = new SearchResolver(connection);
      Set<Long> scopeResult = searchResolver.getScopeResult((CSEScope) context.getScope());
      assert (scopeResult.containsAll(Arrays.asList(100039l, 100044l)));
    });
  }
  
  // context search with context tags only
  @Test
  public void contextFilterTestWithTags() throws CSFormatException, RDBMSException
  {
    printTestTitle("Context filetr with tags");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      
      ICSESearch context = parser
          .parseSearch("select $basetype=$article $ctlg = pim|onboarding $org=$stdo $localeID=en_US|en_UK $entity has [X> Packaging $iid=100005]"
              + " where [Colors] = {[T>red $range=10]}  ");
      
      SearchResolver searchResolver = new SearchResolver(connection);
      Set<Long> scopeResult = searchResolver.getTotalResult(context.getScope(), context.getEvaluation());
      assert (scopeResult.containsAll(Arrays.asList(100011l, 100039l)));
    });
  }
  
  // context search with start time , end time and context tags
  @Test
  public void contextFilterTestWithTagsStartTimeEndTime() throws CSFormatException, RDBMSException
  {
    printTestTitle("Context filetr with start and endTime and tags");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      
      ICSESearch context = parser.parseSearch(
          "select $basetype=$article $ctlg = pim|onboarding $localeID=en_US|en_UK $entity has [X> Packaging $iid=100005 $start=1549219138 $end=1549220138]"
              + " where [Colors] = {[T>red $range=10]}  ");
      
      SearchResolver searchResolver = new SearchResolver(connection); 
      Set<Long> scopeResult = searchResolver.getTotalResult(context.getScope(), context.getEvaluation());
      assert (SetUtils.isEqualSet(Arrays.asList(100039l, 100044l), scopeResult));
    });
  }
 
  // context search with start time , end time and base entity tags 
  // if context tag value is not exist or not found then it will search on normal base entity tags records
  @Test
  public void contextFilterTestWithTagsRecordStartTimeEndTime() throws CSFormatException, RDBMSException
  {
    printTestTitle("Context filter with start and endTime and tags");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      
      ICSESearch context = parser.parseSearch(
          "select $basetype=$article $ctlg = pim|onboarding $localeID=en_US|en_UK $entity has [X> Packaging $iid=100005 $start=1549219138 $end=1549220138]"
              + " where [Colors] = {[T>blue $range=50]}  ");
      SearchResolver searchResolver = new SearchResolver(connection);
      Set<Long> scopeResult = searchResolver.getTotalResult(context.getScope(), context.getEvaluation()); 
      assert (SetUtils.isEqualSet(Arrays.asList(100044l), scopeResult));
    });
  }
  
  @Test
  public void qualityFilterTest() throws CSFormatException, RDBMSException
  {
    printTestTitle("QUALITY FITLER");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          ICSESearch quality = parser.parseSearch("select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " + " $entity.quality = ($yellow | $red)");
          SearchResolver searchResolver = new SearchResolver( connection);
          Set<Long> scopeResult = searchResolver.getScopeResult((CSEScope) quality.getScope());
           assert(scopeResult.containsAll(List.of(100003l , 100005l)));
          
          quality = parser.parseSearch("select $basetype=$article $ctlg= pim|onboarding " + " $entity.quality = $red");
          scopeResult = searchResolver.getScopeResult((CSEScope) quality.getScope());
          assert(scopeResult.containsAll(Arrays.asList( 100003l,100005l, 100006l)));
        });
  }
  
  @Test
  public void propertyFilterTest() throws RDBMSException
  {
    printTestTitle("propertyFilterTest");
    RDBMSConnectionManager.instance()
        .runTransaction((connection) -> {
        	SearchResolver searchResolver = new SearchResolver( connection);

        //COMPARE TWO ATTRIBUTES
          ICSESearch search3 = parser.parseSearch(
              "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
              "$entity is [c>Article] where [nameattribute] = [Model-Name]");
          
          Set<Long> totalResult = searchResolver.getTotalResult(search3.getScope() ,search3.getEvaluation());
          assert(totalResult.containsAll(Arrays.asList(100002l)));
          
          //COMPARE ONE ATTRIBUTE WITH LITERAL
          ICSESearch search4 = parser.parseSearch(
              "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
              "$entity is [c>Article] where [nameattribute] like '%Mouse%'");
          
          Set<Long> searchR = searchResolver.getTotalResult(search4.getScope(), search4.getEvaluation());
          assert(searchR.containsAll(Arrays.asList(100002l)));

          //ATTRIBUTE IS EMPTY AND NOT EMPTY
          ICSESearch search5 = parser.parseSearch(
                  "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
                  "$entity is [c>Article] where ($entity.[nameattribute] <> $null) and not $entity.[ShortDescription] <> $null");
          
          Set<Long> isEmptyAndNot = searchResolver.getTotalResult(search5.getScope(), search5.getEvaluation());
          List<Long> expectedResult = Arrays.asList( 100003l, 100005l, 100007l, 100006l, 100009l, 100008l, 100011l, 100010l, 100013l, 100012l, 100014l);
          assert(isEmptyAndNot.containsAll(expectedResult));

          //ATTRIBUTE RANGE
          ICSESearch search9 = parser.parseSearch(
                  "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
                  "$entity is [c>Article] where [Parcel-Length] > 10 and [Parcel-Length] < 40");

          Set<Long> attributeRange = searchResolver.getTotalResult(search9.getScope(), search9.getEvaluation());
          assert(attributeRange.containsAll(Arrays.asList( 100003l)));
          
          //FLAT FIELD
          ICSESearch search10 = parser.parseSearch(
                  "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
                  "$entity is [c>Article] where [createdonattribute] > 10");

          Set<Long> flatAttributes = searchResolver.getTotalResult(search10.getScope(), search10.getEvaluation());
          System.out.println(flatAttributes);
        });
  }      
  
  @Test
  public void relationshipFilterTest() throws  RDBMSException
  {
    printTestTitle("relationshipFilterTest");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
          
          ICSESearch search2 = parser.parseSearch(
          "select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US " +
          "$entity belongsto [e>Helelo $iid=100005].[Similar-items $side=1] and " + "$entity is [c>Article]");

          
          SearchResolver searchResolver = new SearchResolver( connection);
          CSEScope scope = (CSEScope) search2.getScope();
          Set<Long> baseEntityIds = searchResolver.getScopeResult(scope);
          assert(baseEntityIds.containsAll(Arrays.asList(100014l, 100013l,100010l)));

          ICSESearch search3 = parser.parseSearch("select $basetype=$article "
          		+ "$ctlg= pim|onboarding $org=$stdo $localeID=en_US "
        		+ "$entity belongsto [e>Helelo $iid=100005].[Similar-items $side=1].complement and " 
          		+ "$entity is [c>Article]");
                  
          scope = (CSEScope) search3.getScope();
          baseEntityIds = searchResolver.getScopeResult(scope);
          assert(baseEntityIds.containsAll(Arrays.asList(100014l, 100013l,100010l)));
        });
  } 
  
  @Test
  public void tagFilterTest() throws RDBMSException
  {
    printTestTitle("propertyFilterTest");
    RDBMSConnectionManager.instance()
        .runTransaction((connection) -> {
          SearchResolver searchResolver = new SearchResolver(connection);
          
          // TAG MATCH
          ICSESearch search8 = parser.parseSearch("select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US "
              + "$entity is [c>Article] where [Colors] = {[T>blue $range=50],[T>red $range=50]}");
          
          Set<Long> tagMatch = searchResolver.getTotalResult(search8.getScope(), search8.getEvaluation());
          assert (tagMatch.containsAll(Arrays.asList(100002l, 100001l, 100003l, 100005l)));
        });
    
    RDBMSConnectionManager.instance()
        .runTransaction((connection) -> {
          SearchResolver searchResolver = new SearchResolver(connection);
          
          // RANGE MATCH
          ICSESearch search11 = parser.parseSearch("select $basetype=$article $ctlg= pim|onboarding $org=$stdo $localeID=en_US "
              + "$entity is [c>Article] where [VideoPorts] = [VGA].range between [5,15] ");
          Set<Long> tagMatch = searchResolver.getTotalResult(search11.getScope(), search11.getEvaluation());
          assert (tagMatch.contains(100005l));
        });
  }

    @Test
    public void classifierFilterTest() throws RDBMSException
    {
        printTestTitle("classifierFilterTest");
        RDBMSConnectionManager.instance()
                .runTransaction((connection) -> {
                    printTestTitle("TAXONOMY");
                    ICSESearch classifier1 = parser.parseSearch("select $basetype = $article $ctlg=pim $locale=en_US"
                            + " $entity in [c>taxonomy1]");

                    SearchResolver searchResolver = new SearchResolver(connection);
                    Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) classifier1.getScope());
                    Set<Long> expectedResult = Set.of(100046l,100047l,100048l,100049l, 100050l);
                    assert (baseEntityIds.containsAll(expectedResult));
                });
        RDBMSConnectionManager.instance()
                .runTransaction((connection) -> {
                    printTestTitle("NONNATURE");
                    ICSESearch classifier1 = parser.parseSearch("select $basetype = $article $ctlg=pim $locale=en_US"
                            + " $entity is [c>Article] and $entity in [c>Cross-Selling] ");

                    SearchResolver searchResolver = new SearchResolver(connection);
                    Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) classifier1.getScope());
                    assert (baseEntityIds.containsAll(Set.of(100004l, 100001l)));
                });
        RDBMSConnectionManager.instance()
                .runTransaction((connection) -> {
                    printTestTitle("TAXONOMY OR NON_NATURE");
                    ICSESearch classifier1 = parser.parseSearch("select $basetype = $article $ctlg=pim $locale=en_US"
                            + " $entity is [c>Article] and $entity in [c>Cross-Selling]|[c>taxonomy1-1-1] ");

                    SearchResolver searchResolver = new SearchResolver(connection);
                    Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) classifier1.getScope());
                    assert (baseEntityIds.containsAll(Set.of(100004l, 100001l, 100048l)));
                });

    }

  @Test
  public void expiryFilterTest() throws CSFormatException, RDBMSException
  {
    printTestTitle("EXPIRY FILTER TEST");
    RDBMSConnectionManager.instance()
        .runTransaction((connection) -> {

          ICSESearch search3 = parser.parseSearch("select $basetype = $asset $ctlg=pim $org=$stdo $locale=en_US $entity.expired = $true");

          SearchResolver searchResolver = new SearchResolver(connection);
          Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) search3.getScope());
          Set<Long> expectedResult = new HashSet<>(Arrays.asList(100051L));
          assert (baseEntityIds.containsAll(expectedResult));
        });
  }
  
  @Test
  public void translationFilterTest() throws CSFormatException, RDBMSException
  {
    printTestTitle("translation FITLER");
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      
      ICSESearch classifier1 = parser
          .parseSearch("select $basetype = $article $ctlg=pim $locale=en_US" + " $entityiid = (100002 | 100003 | 100004) $entity existin  [N>en_US] ");
      
      SearchResolver searchResolver = new SearchResolver(connection);
      Set<Long> baseEntityIds = searchResolver.getScopeResult((CSEScope) classifier1.getScope());
      
      assert (baseEntityIds.containsAll(Arrays.asList(100002l, 100003l, 100004l)));
      
    });
  }
 
  
}

