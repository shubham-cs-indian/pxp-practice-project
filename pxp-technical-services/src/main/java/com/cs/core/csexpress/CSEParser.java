package com.cs.core.csexpress;

import com.cs.core.csexpress.actions.CSEActionListParser;
import com.cs.core.csexpress.calculation.CSECalculationParser;
import com.cs.core.csexpress.coupling.CSECouplingParser;
import com.cs.core.csexpress.definition.CSEDefinitionParser;
import com.cs.core.csexpress.rules.CSERuleParser;
import com.cs.core.csexpress.rules.CSESearchParser;
import com.cs.core.csexpress.scope.CSEEntityFilterParser;
import com.cs.core.csexpress.scope.CSEScopeParser;
import com.cs.core.parser.csexpress.csexpressLexer;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEParser;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.*;

/**
 * CSExpression parser
 *
 * @author vallee
 */
public class CSEParser implements ICSEParser {

  private final List<String> exceptionList;
  private csexpressLexer lexer;
  private csexpressParser parser;

  public CSEParser() {
    exceptionList = Collections.synchronizedList(new ArrayList<>());
  }

  private CSEParser(List<String> inheritedList) {
    exceptionList = inheritedList;
  }

  public CSEParser newSubParser() {
    return new CSEParser(exceptionList);
  }

  public List<String> getExceptionList() {
    return exceptionList;
  }

  private void checkParseErrors(String title, String expression) throws CSFormatException {
    if (exceptionList.size() > 0) {
      String msg = String.format("CSExpress %s error on '%s', parser returned: %s", title,
              expression, String.join("; ", exceptionList));
      throw new CSFormatException(msg);
    }
  }

  /**
   * Prepare the ANTLR context of parsing
   *
   * @param expression
   */
  private void initParser(String expression) {
    exceptionList.clear();
    CodePointCharStream input = CharStreams.fromString(expression.trim());
    lexer = new csexpressLexer(input);
    parser = new csexpressParser(new CommonTokenStream(lexer));
    parser.removeErrorListeners();
    parser.addErrorListener(new ErrorListener());
  }

  /**
   * @return the inner initialized parser
   */
  public csexpressParser getInnerParser() {
    return parser;
  }

  @Override
  public ICSEElement parseDefinition(String expression) throws CSFormatException {
    initParser(expression);
    CSEDefinitionParser defParser = new CSEDefinitionParser(this);
    ICSEElement result = defParser.parse();
    checkParseErrors("Defintion", expression);
    return result;
  }

  @Override
  public ICSECoupling parseCoupling(String expression) throws CSFormatException {
    initParser(expression);
    CSECouplingParser cplParser = new CSECouplingParser(this);
    ICSECoupling result = cplParser.parse();
    checkParseErrors("Coupling", expression);
    return result;
  }

  @Override
  public ICSECalculationNode parseCalculation(String expression) throws CSFormatException {
    initParser(expression);
    CSECalculationParser calcParser = new CSECalculationParser(this);
    ICSECalculationNode result = calcParser.parse();
    checkParseErrors("Calculation", expression);
    return result;
  }

  @Override
  public ICSEActionList parseActionList(String expression) throws CSFormatException {
    initParser(expression);
    CSEActionListParser actionsParser = new CSEActionListParser(this);
    ICSEActionList actions = actionsParser.parse();
    checkParseErrors("Actions List", expression);
    return actions;
  }

  @Override
  public ICSEEntityFilterNode parseEntityFilter(String expression) throws CSFormatException {
    initParser(expression);
    CSEEntityFilterParser filterParser = new CSEEntityFilterParser(this);
    ICSEEntityFilterNode filter = filterParser.parse();
    checkParseErrors("Entity Filter", expression);
    return filter;
  }

  @Override
  public ICSEScope parseScope(String expression) throws CSFormatException {
    initParser(expression);
    CSEScopeParser scopeParser = new CSEScopeParser(this);
    ICSEScope scope = scopeParser.parse();
    checkParseErrors("Scope", expression);
    return scope;
  }

  @Override
  public ICSERule parseRule(String expression) throws CSFormatException {
    initParser(expression);
    CSERuleParser ruleParser = new CSERuleParser(this);
    ICSERule rule = ruleParser.parse();
    checkParseErrors("Rule", expression);
    return rule;
  }

  @Override
  public ICSESearch parseSearch(String expression) throws CSFormatException {
    initParser(expression);
    CSESearchParser searchParser = new CSESearchParser(this);
    ICSESearch search = searchParser.parse();
    checkParseErrors("Search", expression);
    return search;
  }

  public class ErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int posInLine, String msg, RecognitionException e) {
      StringBuffer errMessage = new StringBuffer().append("Line/pos: ")
              .append(line).append("/").append(posInLine).append("' :").append(msg);
      exceptionList.add(errMessage.toString());
    }
  }
}
