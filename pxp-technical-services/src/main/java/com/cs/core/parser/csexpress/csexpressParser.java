// Generated from csexpress\csexpress.g4 by ANTLR 4.7

    package com.cs.core.parser.csexpress;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class csexpressParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, FUNIQUE=3, FUPPER=4, FLOWER=5, FREPLACE=6, FADD=7, FREMOVE=8, 
		KSOURCE=9, KENTITY=10, KORIGIN=11, KPARENT=12, KTOP=13, KBASELOCALE=14, 
		KNATURE=15, KISEMPTY=16, KNOW=17, KANY=18, KWITHCHILDREN=19, KNULL=20, 
		KSTDO=21, KISVER=22, KTARGET=23, SIID=24, SCATALOG=25, SORGANZIATION=26, 
		SLOCALE=27, SENDPOINT=28, SPROPERTY=29, SSIDE=30, SCONTEXT=31, STAG=32, 
		SDATE=33, SSTART=34, SEND=35, SBASETYPE=36, SENTITYIID=37, STYPE=38, SRANGE=39, 
		SRANGEOF=40, SGREEN=41, SYELLOW=42, SORANGE=43, SRED=44, SARTICLE=45, 
		SEXTENSION=46, SASSET=47, SPID=48, STEXTASSET=49, SVCATALOG=50, SSUPPLIER=51, 
		STXAONOMY=52, STARGET=53, FQUALITY=54, FEXPIRED=55, FDUPLICATE=56, FNUMBER=57, 
		FHTML=58, FUNIT=59, FLENGTH=60, FLIST_COMPLEMENT=61, FRANGE=62, CXTUAL=63, 
		RELR=64, TAGSR=65, VALUER=66, ENTITY=67, COLLECTION=68, TRACKING=69, TAB=70, 
		PROPERTYCOLL=71, TAGVALUE=72, USER=73, CONTEXT=74, CATALOG=75, CLASSIFIER=76, 
		PROPERTY=77, RULE=78, ORGANIZATION=79, ENDPOINT=80, MAPPING=81, BPMNPROC=82, 
		AUTHORIZATION=83, ROLEAUTH=84, TASK=85, EVENT=86, LANGCONF=87, SMARTDOC=88, 
		TEMPLATE=89, GOLDEN_RULE=90, TRANSLATION=91, ROLE=92, PROPERTY_PERMISSION=93, 
		RELATIONSHI_PPERMISSION=94, PERMISSION=95, COUPLING_SOURCE=96, SELECT=97, 
		FOR=98, WHEN=99, WHERE=100, IS=101, IN=102, UNDER=103, HAS=104, BELONGSTO=105, 
		BETWEEN=106, THEN=107, EXISTIN=108, ASSIGNS=109, MOVETO=110, RAISETO=111, 
		EQUALS=112, PLUS=113, MINUS=114, MULTIPLY=115, DIVIDE=116, MODULUS=117, 
		CONCATENATE=118, LTE=119, LT=120, GTE=121, GT=122, DIFF=123, LOR=124, 
		LAND=125, LXOR=126, NOT=127, LIKE=128, REGEX=129, CONTAINS=130, NOTCONTAINS=131, 
		INVOLVES=132, RELATES=133, LOCALEID=134, DATETIME=135, DATE=136, TIME=137, 
		DOUBLE=138, INTEGER=139, METACODE=140, CODE=141, TARGETS=142, USERCODE=143, 
		STRING=144, OR=145, AND=146, SEP=147, DOT=148, COLUMN=149, DYNCOUPLING=150, 
		TIGHCOUPLING=151, GROUP_IN=152, GROUP_OUT=153, LIST_IN=154, LIST_OUT=155, 
		OBJECT_IN=156, OBJECT_OUT=157, WS=158;
	public static final int
		RULE_search_expression = 0, RULE_rule_expression = 1, RULE_calculation = 2, 
		RULE_scope = 3, RULE_basetype_scope = 4, RULE_entity_basetypes = 5, RULE_basetype = 6, 
		RULE_catalog_scope = 7, RULE_catalogs = 8, RULE_organization_scope = 9, 
		RULE_organizations = 10, RULE_organization_code = 11, RULE_catalog_code = 12, 
		RULE_locale_scope = 13, RULE_locales = 14, RULE_locale_code = 15, RULE_iid_scope = 16, 
		RULE_entity_iids = 17, RULE_entity_iid = 18, RULE_endpoint_scope = 19, 
		RULE_endpoints = 20, RULE_endpoint_code = 21, RULE_entity_scope = 22, 
		RULE_entity_filter = 23, RULE_scope_not = 24, RULE_scope_operator = 25, 
		RULE_list_through_property = 26, RULE_evaluated_filter = 27, RULE_context = 28, 
		RULE_collection = 29, RULE_expiry = 30, RULE_duplicate = 31, RULE_translation = 32, 
		RULE_action_list = 33, RULE_action = 34, RULE_evaluated_action = 35, RULE_target_property = 36, 
		RULE_target_object = 37, RULE_classifier = 38, RULE_predefinedclassifier = 39, 
		RULE_quality_level = 40, RULE_evaluation_expression = 41, RULE_operand = 42, 
		RULE_evaluated_operand = 43, RULE_unary_operator = 44, RULE_function_operator = 45, 
		RULE_function_parameter = 46, RULE_operator = 47, RULE_math_operator = 48, 
		RULE_text_operator = 49, RULE_conditional_operator = 50, RULE_property_operand = 51, 
		RULE_property_field = 52, RULE_literal_operand = 53, RULE_tag_literal = 54, 
		RULE_tag_code = 55, RULE_literal = 56, RULE_range_literal_operand = 57, 
		RULE_rangeLiteral = 58, RULE_coupling = 59, RULE_relationcoupling = 60, 
		RULE_objectcoupling = 61, RULE_predefinedobject = 62, RULE_element = 63, 
		RULE_list = 64, RULE_property = 65, RULE_propertytype = 66, RULE_tag_value = 67, 
		RULE_tagvaluetype = 68, RULE_object = 69, RULE_objecttype = 70, RULE_meta = 71, 
		RULE_identifierelt = 72, RULE_iidspec = 73, RULE_catalogspec = 74, RULE_organizationspec = 75, 
		RULE_localespec = 76, RULE_contextspec = 77, RULE_propertyspec = 78, RULE_typespec = 79, 
		RULE_sidespec = 80, RULE_startspec = 81, RULE_endspec = 82, RULE_tagspec = 83, 
		RULE_tagcode = 84, RULE_rangespec = 85, RULE_rangeofspec = 86, RULE_datespec = 87, 
		RULE_isversionablespec = 88, RULE_targets_spec = 89, RULE_endpointspec = 90, 
		RULE_primaryidentifier = 91;
	public static final String[] ruleNames = {
		"search_expression", "rule_expression", "calculation", "scope", "basetype_scope", 
		"entity_basetypes", "basetype", "catalog_scope", "catalogs", "organization_scope", 
		"organizations", "organization_code", "catalog_code", "locale_scope", 
		"locales", "locale_code", "iid_scope", "entity_iids", "entity_iid", "endpoint_scope", 
		"endpoints", "endpoint_code", "entity_scope", "entity_filter", "scope_not", 
		"scope_operator", "list_through_property", "evaluated_filter", "context", 
		"collection", "expiry", "duplicate", "translation", "action_list", "action", 
		"evaluated_action", "target_property", "target_object", "classifier", 
		"predefinedclassifier", "quality_level", "evaluation_expression", "operand", 
		"evaluated_operand", "unary_operator", "function_operator", "function_parameter", 
		"operator", "math_operator", "text_operator", "conditional_operator", 
		"property_operand", "property_field", "literal_operand", "tag_literal", 
		"tag_code", "literal", "range_literal_operand", "rangeLiteral", "coupling", 
		"relationcoupling", "objectcoupling", "predefinedobject", "element", "list", 
		"property", "propertytype", "tag_value", "tagvaluetype", "object", "objecttype", 
		"meta", "identifierelt", "iidspec", "catalogspec", "organizationspec", 
		"localespec", "contextspec", "propertyspec", "typespec", "sidespec", "startspec", 
		"endspec", "tagspec", "tagcode", "rangespec", "rangeofspec", "datespec", 
		"isversionablespec", "targets_spec", "endpointspec", "primaryidentifier"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'$true'", "'$false'", "'unique'", "'upper'", "'lower'", "'replace'", 
		"'add'", "'remove'", "'$source'", "'$entity'", "'$origin'", "'$parent'", 
		"'$top'", "'$baselocale'", "'$nature'", "'$empty'", "'$now'", "'$any'", 
		"'$withchildren'", "'$null'", "'$stdo'", "'$isver'", null, "'$iid'", "'$ctlg'", 
		"'$org'", "'$locale'", "'$endpoint'", "'$prop'", "'$side'", "'$cxt'", 
		"'$tag'", "'$date'", "'$start'", "'$end'", "'$basetype'", "'$entityiid'", 
		"'$type'", "'$range'", "'$rangeof'", "'$green'", "'$yellow'", "'$orange'", 
		"'$red'", "'$article'", "'$extension'", "'$asset'", "'$pid'", "'$textasset'", 
		"'$vcatalog'", "'$supplier'", "'$taxonomy'", null, "'quality'", "'expired'", 
		"'duplicate'", "'number'", "'html'", "'unit'", "'length'", "'complement'", 
		"'range'", "'x>'", "'r>'", "'t>'", "'v>'", "'e>'", "'b>'", "'!>'", "'W>'", 
		"'G>'", "'T>'", "'U>'", "'X>'", "'C>'", "'c>'", "'P>'", "'R>'", "'O>'", 
		"'I>'", "'M>'", "'B>'", "'A>'", "'Z>'", "'K>'", "'E>'", "'L>'", "'D>'", 
		"'Y>'", "'S>'", "'N>'", "'Q>'", "'H>'", "'J>'", "'V>'", "':>'", "'select'", 
		"'for'", "'when'", "'where'", "'is'", "'in'", "'under'", "'has'", "'belongsto'", 
		"'between'", "'then'", "'existin'", "':='", "'=>'", "'>>'", "'='", "'+'", 
		"'-'", "'*'", "'/'", "'%'", "'||'", "'<='", "'<'", "'>='", "'>'", "'<>'", 
		"'or'", "'and'", "'xor'", "'not'", "'like'", "'regex'", "'contains'", 
		"'notcontains'", "'involves'", "'relates'", null, null, null, null, null, 
		null, null, null, null, null, null, "'|'", "'&'", "','", "'.'", "':'", 
		"'&.'", "'|.'", "'('", "')'", "'{'", "'}'", "'['", "']'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "TRUE", "FALSE", "FUNIQUE", "FUPPER", "FLOWER", "FREPLACE", "FADD", 
		"FREMOVE", "KSOURCE", "KENTITY", "KORIGIN", "KPARENT", "KTOP", "KBASELOCALE", 
		"KNATURE", "KISEMPTY", "KNOW", "KANY", "KWITHCHILDREN", "KNULL", "KSTDO", 
		"KISVER", "KTARGET", "SIID", "SCATALOG", "SORGANZIATION", "SLOCALE", "SENDPOINT", 
		"SPROPERTY", "SSIDE", "SCONTEXT", "STAG", "SDATE", "SSTART", "SEND", "SBASETYPE", 
		"SENTITYIID", "STYPE", "SRANGE", "SRANGEOF", "SGREEN", "SYELLOW", "SORANGE", 
		"SRED", "SARTICLE", "SEXTENSION", "SASSET", "SPID", "STEXTASSET", "SVCATALOG", 
		"SSUPPLIER", "STXAONOMY", "STARGET", "FQUALITY", "FEXPIRED", "FDUPLICATE", 
		"FNUMBER", "FHTML", "FUNIT", "FLENGTH", "FLIST_COMPLEMENT", "FRANGE", 
		"CXTUAL", "RELR", "TAGSR", "VALUER", "ENTITY", "COLLECTION", "TRACKING", 
		"TAB", "PROPERTYCOLL", "TAGVALUE", "USER", "CONTEXT", "CATALOG", "CLASSIFIER", 
		"PROPERTY", "RULE", "ORGANIZATION", "ENDPOINT", "MAPPING", "BPMNPROC", 
		"AUTHORIZATION", "ROLEAUTH", "TASK", "EVENT", "LANGCONF", "SMARTDOC", 
		"TEMPLATE", "GOLDEN_RULE", "TRANSLATION", "ROLE", "PROPERTY_PERMISSION", 
		"RELATIONSHI_PPERMISSION", "PERMISSION", "COUPLING_SOURCE", "SELECT", 
		"FOR", "WHEN", "WHERE", "IS", "IN", "UNDER", "HAS", "BELONGSTO", "BETWEEN", 
		"THEN", "EXISTIN", "ASSIGNS", "MOVETO", "RAISETO", "EQUALS", "PLUS", "MINUS", 
		"MULTIPLY", "DIVIDE", "MODULUS", "CONCATENATE", "LTE", "LT", "GTE", "GT", 
		"DIFF", "LOR", "LAND", "LXOR", "NOT", "LIKE", "REGEX", "CONTAINS", "NOTCONTAINS", 
		"INVOLVES", "RELATES", "LOCALEID", "DATETIME", "DATE", "TIME", "DOUBLE", 
		"INTEGER", "METACODE", "CODE", "TARGETS", "USERCODE", "STRING", "OR", 
		"AND", "SEP", "DOT", "COLUMN", "DYNCOUPLING", "TIGHCOUPLING", "GROUP_IN", 
		"GROUP_OUT", "LIST_IN", "LIST_OUT", "OBJECT_IN", "OBJECT_OUT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "csexpress.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public csexpressParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class Search_expressionContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(csexpressParser.SELECT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public ScopeContext scope() {
			return getRuleContext(ScopeContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(csexpressParser.WHERE, 0); }
		public Evaluation_expressionContext evaluation_expression() {
			return getRuleContext(Evaluation_expressionContext.class,0);
		}
		public Search_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_search_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterSearch_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitSearch_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitSearch_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Search_expressionContext search_expression() throws RecognitionException {
		Search_expressionContext _localctx = new Search_expressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_search_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			match(SELECT);
			setState(185);
			match(WS);
			setState(186);
			scope();
			setState(187);
			match(WS);
			setState(188);
			match(WHERE);
			setState(189);
			match(WS);
			setState(190);
			evaluation_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Rule_expressionContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(csexpressParser.FOR, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public ScopeContext scope() {
			return getRuleContext(ScopeContext.class,0);
		}
		public TerminalNode WHEN() { return getToken(csexpressParser.WHEN, 0); }
		public Evaluation_expressionContext evaluation_expression() {
			return getRuleContext(Evaluation_expressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(csexpressParser.THEN, 0); }
		public Action_listContext action_list() {
			return getRuleContext(Action_listContext.class,0);
		}
		public Rule_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rule_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRule_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRule_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRule_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Rule_expressionContext rule_expression() throws RecognitionException {
		Rule_expressionContext _localctx = new Rule_expressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_rule_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(FOR);
			setState(193);
			match(WS);
			setState(194);
			scope();
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(195);
				match(WS);
				setState(196);
				match(WHEN);
				setState(197);
				match(WS);
				setState(198);
				evaluation_expression();
				}
				break;
			}
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(201);
				match(WS);
				setState(202);
				match(THEN);
				setState(203);
				match(WS);
				setState(204);
				action_list();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CalculationContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public Evaluation_expressionContext evaluation_expression() {
			return getRuleContext(Evaluation_expressionContext.class,0);
		}
		public TerminalNode WS() { return getToken(csexpressParser.WS, 0); }
		public CalculationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_calculation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCalculation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCalculation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCalculation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CalculationContext calculation() throws RecognitionException {
		CalculationContext _localctx = new CalculationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_calculation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(EQUALS);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(208);
				match(WS);
				}
			}

			setState(211);
			evaluation_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ScopeContext extends ParserRuleContext {
		public Catalog_scopeContext catalog_scope() {
			return getRuleContext(Catalog_scopeContext.class,0);
		}
		public Basetype_scopeContext basetype_scope() {
			return getRuleContext(Basetype_scopeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Organization_scopeContext organization_scope() {
			return getRuleContext(Organization_scopeContext.class,0);
		}
		public Locale_scopeContext locale_scope() {
			return getRuleContext(Locale_scopeContext.class,0);
		}
		public Endpoint_scopeContext endpoint_scope() {
			return getRuleContext(Endpoint_scopeContext.class,0);
		}
		public Iid_scopeContext iid_scope() {
			return getRuleContext(Iid_scopeContext.class,0);
		}
		public Entity_scopeContext entity_scope() {
			return getRuleContext(Entity_scopeContext.class,0);
		}
		public ScopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterScope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitScope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitScope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScopeContext scope() throws RecognitionException {
		ScopeContext _localctx = new ScopeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SBASETYPE) {
				{
				setState(213);
				basetype_scope();
				setState(214);
				match(WS);
				}
			}

			setState(218);
			catalog_scope();
			setState(221);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(219);
				match(WS);
				setState(220);
				organization_scope();
				}
				break;
			}
			setState(225);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(223);
				match(WS);
				setState(224);
				locale_scope();
				}
				break;
			}
			setState(229);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(227);
				match(WS);
				setState(228);
				endpoint_scope();
				}
				break;
			}
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(231);
				match(WS);
				setState(232);
				iid_scope();
				}
				break;
			}
			setState(237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(235);
				match(WS);
				setState(236);
				entity_scope();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Basetype_scopeContext extends ParserRuleContext {
		public TerminalNode SBASETYPE() { return getToken(csexpressParser.SBASETYPE, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public Entity_basetypesContext entity_basetypes() {
			return getRuleContext(Entity_basetypesContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Basetype_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basetype_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterBasetype_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitBasetype_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitBasetype_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Basetype_scopeContext basetype_scope() throws RecognitionException {
		Basetype_scopeContext _localctx = new Basetype_scopeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_basetype_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(SBASETYPE);
			setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(240);
				match(WS);
				}
			}

			setState(243);
			match(EQUALS);
			setState(245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(244);
				match(WS);
				}
			}

			setState(247);
			entity_basetypes();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Entity_basetypesContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<BasetypeContext> basetype() {
			return getRuleContexts(BasetypeContext.class);
		}
		public BasetypeContext basetype(int i) {
			return getRuleContext(BasetypeContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public Entity_basetypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity_basetypes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEntity_basetypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEntity_basetypes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEntity_basetypes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Entity_basetypesContext entity_basetypes() throws RecognitionException {
		Entity_basetypesContext _localctx = new Entity_basetypesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_entity_basetypes);
		int _la;
		try {
			int _alt;
			setState(286);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(249);
				match(GROUP_IN);
				setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(250);
					match(WS);
					}
				}

				setState(253);
				basetype();
				setState(264);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(255);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(254);
							match(WS);
							}
						}

						setState(257);
						match(OR);
						setState(259);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(258);
							match(WS);
							}
						}

						setState(261);
						basetype();
						}
						} 
					}
					setState(266);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				}
				setState(268);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(267);
					match(WS);
					}
				}

				setState(270);
				match(GROUP_OUT);
				}
				break;
			case SARTICLE:
			case SEXTENSION:
			case SASSET:
			case SPID:
			case STEXTASSET:
			case SVCATALOG:
			case SSUPPLIER:
			case STXAONOMY:
			case STARGET:
				enterOuterAlt(_localctx, 2);
				{
				setState(272);
				basetype();
				setState(283);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(274);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(273);
							match(WS);
							}
						}

						setState(276);
						match(OR);
						setState(278);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(277);
							match(WS);
							}
						}

						setState(280);
						basetype();
						}
						} 
					}
					setState(285);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BasetypeContext extends ParserRuleContext {
		public TerminalNode SARTICLE() { return getToken(csexpressParser.SARTICLE, 0); }
		public TerminalNode SEXTENSION() { return getToken(csexpressParser.SEXTENSION, 0); }
		public TerminalNode SASSET() { return getToken(csexpressParser.SASSET, 0); }
		public TerminalNode SPID() { return getToken(csexpressParser.SPID, 0); }
		public TerminalNode STEXTASSET() { return getToken(csexpressParser.STEXTASSET, 0); }
		public TerminalNode SVCATALOG() { return getToken(csexpressParser.SVCATALOG, 0); }
		public TerminalNode SSUPPLIER() { return getToken(csexpressParser.SSUPPLIER, 0); }
		public TerminalNode STXAONOMY() { return getToken(csexpressParser.STXAONOMY, 0); }
		public TerminalNode STARGET() { return getToken(csexpressParser.STARGET, 0); }
		public BasetypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basetype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterBasetype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitBasetype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitBasetype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BasetypeContext basetype() throws RecognitionException {
		BasetypeContext _localctx = new BasetypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_basetype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SARTICLE) | (1L << SEXTENSION) | (1L << SASSET) | (1L << SPID) | (1L << STEXTASSET) | (1L << SVCATALOG) | (1L << SSUPPLIER) | (1L << STXAONOMY) | (1L << STARGET))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Catalog_scopeContext extends ParserRuleContext {
		public TerminalNode SCATALOG() { return getToken(csexpressParser.SCATALOG, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public CatalogsContext catalogs() {
			return getRuleContext(CatalogsContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Catalog_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catalog_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCatalog_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCatalog_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCatalog_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Catalog_scopeContext catalog_scope() throws RecognitionException {
		Catalog_scopeContext _localctx = new Catalog_scopeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_catalog_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(290);
			match(SCATALOG);
			setState(292);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(291);
				match(WS);
				}
			}

			setState(294);
			match(EQUALS);
			setState(296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(295);
				match(WS);
				}
			}

			setState(298);
			catalogs();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatalogsContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<Catalog_codeContext> catalog_code() {
			return getRuleContexts(Catalog_codeContext.class);
		}
		public Catalog_codeContext catalog_code(int i) {
			return getRuleContext(Catalog_codeContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public CatalogsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catalogs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCatalogs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCatalogs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCatalogs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatalogsContext catalogs() throws RecognitionException {
		CatalogsContext _localctx = new CatalogsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_catalogs);
		int _la;
		try {
			int _alt;
			setState(337);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(300);
				match(GROUP_IN);
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(301);
					match(WS);
					}
				}

				setState(304);
				catalog_code();
				setState(315);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(306);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(305);
							match(WS);
							}
						}

						setState(308);
						match(OR);
						setState(310);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(309);
							match(WS);
							}
						}

						setState(312);
						catalog_code();
						}
						} 
					}
					setState(317);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				setState(319);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(318);
					match(WS);
					}
				}

				setState(321);
				match(GROUP_OUT);
				}
				break;
			case CODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(323);
				catalog_code();
				setState(334);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(325);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(324);
							match(WS);
							}
						}

						setState(327);
						match(OR);
						setState(329);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(328);
							match(WS);
							}
						}

						setState(331);
						catalog_code();
						}
						} 
					}
					setState(336);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Organization_scopeContext extends ParserRuleContext {
		public TerminalNode SORGANZIATION() { return getToken(csexpressParser.SORGANZIATION, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public OrganizationsContext organizations() {
			return getRuleContext(OrganizationsContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Organization_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_organization_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOrganization_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOrganization_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOrganization_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Organization_scopeContext organization_scope() throws RecognitionException {
		Organization_scopeContext _localctx = new Organization_scopeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_organization_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(339);
			match(SORGANZIATION);
			setState(341);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(340);
				match(WS);
				}
			}

			setState(343);
			match(EQUALS);
			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(344);
				match(WS);
				}
			}

			setState(347);
			organizations();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrganizationsContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<Organization_codeContext> organization_code() {
			return getRuleContexts(Organization_codeContext.class);
		}
		public Organization_codeContext organization_code(int i) {
			return getRuleContext(Organization_codeContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public OrganizationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_organizations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOrganizations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOrganizations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOrganizations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrganizationsContext organizations() throws RecognitionException {
		OrganizationsContext _localctx = new OrganizationsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_organizations);
		int _la;
		try {
			int _alt;
			setState(386);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(349);
				match(GROUP_IN);
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(350);
					match(WS);
					}
				}

				setState(353);
				organization_code();
				setState(364);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(355);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(354);
							match(WS);
							}
						}

						setState(357);
						match(OR);
						setState(359);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(358);
							match(WS);
							}
						}

						setState(361);
						organization_code();
						}
						} 
					}
					setState(366);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
				}
				setState(368);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(367);
					match(WS);
					}
				}

				setState(370);
				match(GROUP_OUT);
				}
				break;
			case KNULL:
			case KSTDO:
			case LOCALEID:
			case INTEGER:
			case CODE:
			case USERCODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(372);
				organization_code();
				setState(383);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(374);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(373);
							match(WS);
							}
						}

						setState(376);
						match(OR);
						setState(378);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(377);
							match(WS);
							}
						}

						setState(380);
						organization_code();
						}
						} 
					}
					setState(385);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,40,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Organization_codeContext extends ParserRuleContext {
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public Organization_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_organization_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOrganization_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOrganization_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOrganization_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Organization_codeContext organization_code() throws RecognitionException {
		Organization_codeContext _localctx = new Organization_codeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_organization_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(388);
			primaryidentifier();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Catalog_codeContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(csexpressParser.CODE, 0); }
		public Catalog_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catalog_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCatalog_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCatalog_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCatalog_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Catalog_codeContext catalog_code() throws RecognitionException {
		Catalog_codeContext _localctx = new Catalog_codeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_catalog_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(CODE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Locale_scopeContext extends ParserRuleContext {
		public TerminalNode SLOCALE() { return getToken(csexpressParser.SLOCALE, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public LocalesContext locales() {
			return getRuleContext(LocalesContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Locale_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locale_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLocale_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLocale_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLocale_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Locale_scopeContext locale_scope() throws RecognitionException {
		Locale_scopeContext _localctx = new Locale_scopeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_locale_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(SLOCALE);
			setState(394);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(393);
				match(WS);
				}
			}

			setState(396);
			match(EQUALS);
			setState(398);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(397);
				match(WS);
				}
			}

			setState(400);
			locales();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalesContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<Locale_codeContext> locale_code() {
			return getRuleContexts(Locale_codeContext.class);
		}
		public Locale_codeContext locale_code(int i) {
			return getRuleContext(Locale_codeContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public LocalesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locales; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLocales(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLocales(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLocales(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalesContext locales() throws RecognitionException {
		LocalesContext _localctx = new LocalesContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_locales);
		int _la;
		try {
			int _alt;
			setState(436);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(402);
				match(GROUP_IN);
				setState(404);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(403);
					match(WS);
					}
				}

				setState(406);
				locale_code();
				setState(417);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==OR || _la==WS) {
					{
					{
					setState(408);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(407);
						match(WS);
						}
					}

					setState(410);
					match(OR);
					setState(412);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(411);
						match(WS);
						}
					}

					setState(414);
					locale_code();
					}
					}
					setState(419);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(420);
				match(GROUP_OUT);
				}
				break;
			case KBASELOCALE:
			case LOCALEID:
				enterOuterAlt(_localctx, 2);
				{
				setState(422);
				locale_code();
				setState(433);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(424);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(423);
							match(WS);
							}
						}

						setState(426);
						match(OR);
						setState(428);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(427);
							match(WS);
							}
						}

						setState(430);
						locale_code();
						}
						} 
					}
					setState(435);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,50,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Locale_codeContext extends ParserRuleContext {
		public TerminalNode LOCALEID() { return getToken(csexpressParser.LOCALEID, 0); }
		public TerminalNode KBASELOCALE() { return getToken(csexpressParser.KBASELOCALE, 0); }
		public Locale_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locale_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLocale_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLocale_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLocale_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Locale_codeContext locale_code() throws RecognitionException {
		Locale_codeContext _localctx = new Locale_codeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_locale_code);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(438);
			_la = _input.LA(1);
			if ( !(_la==KBASELOCALE || _la==LOCALEID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Iid_scopeContext extends ParserRuleContext {
		public TerminalNode SENTITYIID() { return getToken(csexpressParser.SENTITYIID, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public Entity_iidsContext entity_iids() {
			return getRuleContext(Entity_iidsContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Iid_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iid_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterIid_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitIid_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitIid_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Iid_scopeContext iid_scope() throws RecognitionException {
		Iid_scopeContext _localctx = new Iid_scopeContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_iid_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			match(SENTITYIID);
			setState(442);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(441);
				match(WS);
				}
			}

			setState(444);
			match(EQUALS);
			setState(446);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(445);
				match(WS);
				}
			}

			setState(448);
			entity_iids();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Entity_iidsContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<Entity_iidContext> entity_iid() {
			return getRuleContexts(Entity_iidContext.class);
		}
		public Entity_iidContext entity_iid(int i) {
			return getRuleContext(Entity_iidContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public Entity_iidsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity_iids; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEntity_iids(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEntity_iids(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEntity_iids(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Entity_iidsContext entity_iids() throws RecognitionException {
		Entity_iidsContext _localctx = new Entity_iidsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_entity_iids);
		int _la;
		try {
			int _alt;
			setState(487);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(450);
				match(GROUP_IN);
				setState(452);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(451);
					match(WS);
					}
				}

				setState(454);
				entity_iid();
				setState(465);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(456);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(455);
							match(WS);
							}
						}

						setState(458);
						match(OR);
						setState(460);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(459);
							match(WS);
							}
						}

						setState(462);
						entity_iid();
						}
						} 
					}
					setState(467);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
				}
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(468);
					match(WS);
					}
				}

				setState(471);
				match(GROUP_OUT);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 2);
				{
				setState(473);
				entity_iid();
				setState(484);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(475);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(474);
							match(WS);
							}
						}

						setState(477);
						match(OR);
						setState(479);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(478);
							match(WS);
							}
						}

						setState(481);
						entity_iid();
						}
						} 
					}
					setState(486);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Entity_iidContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public Entity_iidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity_iid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEntity_iid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEntity_iid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEntity_iid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Entity_iidContext entity_iid() throws RecognitionException {
		Entity_iidContext _localctx = new Entity_iidContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_entity_iid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(489);
			match(INTEGER);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Endpoint_scopeContext extends ParserRuleContext {
		public TerminalNode SENDPOINT() { return getToken(csexpressParser.SENDPOINT, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public EndpointsContext endpoints() {
			return getRuleContext(EndpointsContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Endpoint_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoint_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEndpoint_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEndpoint_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEndpoint_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Endpoint_scopeContext endpoint_scope() throws RecognitionException {
		Endpoint_scopeContext _localctx = new Endpoint_scopeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_endpoint_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			match(SENDPOINT);
			setState(493);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(492);
				match(WS);
				}
			}

			setState(495);
			match(EQUALS);
			setState(497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(496);
				match(WS);
				}
			}

			setState(499);
			endpoints();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndpointsContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public List<Endpoint_codeContext> endpoint_code() {
			return getRuleContexts(Endpoint_codeContext.class);
		}
		public Endpoint_codeContext endpoint_code(int i) {
			return getRuleContext(Endpoint_codeContext.class,i);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public EndpointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEndpoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEndpoints(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEndpoints(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndpointsContext endpoints() throws RecognitionException {
		EndpointsContext _localctx = new EndpointsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_endpoints);
		int _la;
		try {
			int _alt;
			setState(535);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case GROUP_IN:
				enterOuterAlt(_localctx, 1);
				{
				setState(501);
				match(GROUP_IN);
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(502);
					match(WS);
					}
				}

				setState(505);
				endpoint_code();
				setState(516);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==OR || _la==WS) {
					{
					{
					setState(507);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(506);
						match(WS);
						}
					}

					setState(509);
					match(OR);
					setState(511);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(510);
						match(WS);
						}
					}

					setState(513);
					endpoint_code();
					}
					}
					setState(518);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(519);
				match(GROUP_OUT);
				}
				break;
			case KNULL:
			case KSTDO:
			case LOCALEID:
			case INTEGER:
			case CODE:
			case USERCODE:
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
				endpoint_code();
				setState(532);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(523);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(522);
							match(WS);
							}
						}

						setState(525);
						match(OR);
						setState(527);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(526);
							match(WS);
							}
						}

						setState(529);
						endpoint_code();
						}
						} 
					}
					setState(534);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,71,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Endpoint_codeContext extends ParserRuleContext {
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public Endpoint_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoint_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEndpoint_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEndpoint_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEndpoint_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Endpoint_codeContext endpoint_code() throws RecognitionException {
		Endpoint_codeContext _localctx = new Endpoint_codeContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_endpoint_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(537);
			primaryidentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Entity_scopeContext extends ParserRuleContext {
		public List<Entity_filterContext> entity_filter() {
			return getRuleContexts(Entity_filterContext.class);
		}
		public Entity_filterContext entity_filter(int i) {
			return getRuleContext(Entity_filterContext.class,i);
		}
		public List<Scope_notContext> scope_not() {
			return getRuleContexts(Scope_notContext.class);
		}
		public Scope_notContext scope_not(int i) {
			return getRuleContext(Scope_notContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<Scope_operatorContext> scope_operator() {
			return getRuleContexts(Scope_operatorContext.class);
		}
		public Scope_operatorContext scope_operator(int i) {
			return getRuleContext(Scope_operatorContext.class,i);
		}
		public Entity_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEntity_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEntity_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEntity_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Entity_scopeContext entity_scope() throws RecognitionException {
		Entity_scopeContext _localctx = new Entity_scopeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_entity_scope);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(539);
				scope_not();
				setState(540);
				match(WS);
				}
			}

			setState(544);
			entity_filter();
			setState(561);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,77,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(546);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(545);
						match(WS);
						}
					}

					setState(548);
					scope_operator();
					setState(550);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(549);
						match(WS);
						}
					}

					setState(555);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(552);
						scope_not();
						setState(553);
						match(WS);
						}
					}

					setState(557);
					entity_filter();
					}
					} 
				}
				setState(563);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,77,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Entity_filterContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public Evaluated_filterContext evaluated_filter() {
			return getRuleContext(Evaluated_filterContext.class,0);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Target_objectContext target_object() {
			return getRuleContext(Target_objectContext.class,0);
		}
		public List<ClassifierContext> classifier() {
			return getRuleContexts(ClassifierContext.class);
		}
		public ClassifierContext classifier(int i) {
			return getRuleContext(ClassifierContext.class,i);
		}
		public TerminalNode IN() { return getToken(csexpressParser.IN, 0); }
		public TerminalNode UNDER() { return getToken(csexpressParser.UNDER, 0); }
		public TerminalNode IS() { return getToken(csexpressParser.IS, 0); }
		public List<TerminalNode> OR() { return getTokens(csexpressParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(csexpressParser.OR, i);
		}
		public List_through_propertyContext list_through_property() {
			return getRuleContext(List_through_propertyContext.class,0);
		}
		public TerminalNode BELONGSTO() { return getToken(csexpressParser.BELONGSTO, 0); }
		public CollectionContext collection() {
			return getRuleContext(CollectionContext.class,0);
		}
		public List<TranslationContext> translation() {
			return getRuleContexts(TranslationContext.class);
		}
		public TranslationContext translation(int i) {
			return getRuleContext(TranslationContext.class,i);
		}
		public TerminalNode EXISTIN() { return getToken(csexpressParser.EXISTIN, 0); }
		public List<ContextContext> context() {
			return getRuleContexts(ContextContext.class);
		}
		public ContextContext context(int i) {
			return getRuleContext(ContextContext.class,i);
		}
		public TerminalNode HAS() { return getToken(csexpressParser.HAS, 0); }
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public TerminalNode CONTAINS() { return getToken(csexpressParser.CONTAINS, 0); }
		public TerminalNode INVOLVES() { return getToken(csexpressParser.INVOLVES, 0); }
		public TerminalNode DOT() { return getToken(csexpressParser.DOT, 0); }
		public TerminalNode FQUALITY() { return getToken(csexpressParser.FQUALITY, 0); }
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public List<Quality_levelContext> quality_level() {
			return getRuleContexts(Quality_levelContext.class);
		}
		public Quality_levelContext quality_level(int i) {
			return getRuleContext(Quality_levelContext.class,i);
		}
		public TerminalNode FEXPIRED() { return getToken(csexpressParser.FEXPIRED, 0); }
		public ExpiryContext expiry() {
			return getRuleContext(ExpiryContext.class,0);
		}
		public TerminalNode FDUPLICATE() { return getToken(csexpressParser.FDUPLICATE, 0); }
		public DuplicateContext duplicate() {
			return getRuleContext(DuplicateContext.class,0);
		}
		public Entity_filterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEntity_filter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEntity_filter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEntity_filter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Entity_filterContext entity_filter() throws RecognitionException {
		Entity_filterContext _localctx = new Entity_filterContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_entity_filter);
		int _la;
		try {
			int _alt;
			setState(847);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(564);
				match(GROUP_IN);
				setState(566);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(565);
					match(WS);
					}
				}

				setState(568);
				evaluated_filter();
				setState(570);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(569);
					match(WS);
					}
				}

				setState(572);
				match(GROUP_OUT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(574);
				target_object();
				setState(575);
				match(WS);
				setState(576);
				_la = _input.LA(1);
				if ( !(((((_la - 101)) & ~0x3f) == 0 && ((1L << (_la - 101)) & ((1L << (IS - 101)) | (1L << (IN - 101)) | (1L << (UNDER - 101)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(577);
				match(WS);
				setState(578);
				classifier();
				setState(589);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(580);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(579);
							match(WS);
							}
						}

						setState(582);
						match(OR);
						setState(584);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(583);
							match(WS);
							}
						}

						setState(586);
						classifier();
						}
						} 
					}
					setState(591);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,82,_ctx);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(592);
				target_object();
				setState(593);
				match(WS);
				setState(594);
				_la = _input.LA(1);
				if ( !(((((_la - 101)) & ~0x3f) == 0 && ((1L << (_la - 101)) & ((1L << (IS - 101)) | (1L << (IN - 101)) | (1L << (UNDER - 101)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(595);
				match(WS);
				setState(596);
				match(GROUP_IN);
				setState(598);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(597);
					match(WS);
					}
				}

				setState(600);
				classifier();
				setState(611);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(602);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(601);
							match(WS);
							}
						}

						setState(604);
						match(OR);
						setState(606);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(605);
							match(WS);
							}
						}

						setState(608);
						classifier();
						}
						} 
					}
					setState(613);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,86,_ctx);
				}
				setState(615);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(614);
					match(WS);
					}
				}

				setState(617);
				match(GROUP_OUT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(619);
				target_object();
				setState(620);
				match(WS);
				{
				setState(621);
				match(BELONGSTO);
				}
				setState(622);
				match(WS);
				setState(623);
				list_through_property();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(625);
				target_object();
				setState(626);
				match(WS);
				{
				setState(627);
				match(BELONGSTO);
				}
				setState(628);
				match(WS);
				setState(629);
				collection();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(631);
				target_object();
				setState(632);
				match(WS);
				{
				setState(633);
				match(EXISTIN);
				}
				setState(634);
				match(WS);
				setState(635);
				translation();
				setState(646);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(637);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(636);
							match(WS);
							}
						}

						setState(639);
						match(OR);
						setState(641);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(640);
							match(WS);
							}
						}

						setState(643);
						translation();
						}
						} 
					}
					setState(648);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,90,_ctx);
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(649);
				target_object();
				setState(650);
				match(WS);
				{
				setState(651);
				match(EXISTIN);
				}
				setState(652);
				match(WS);
				setState(653);
				match(GROUP_IN);
				setState(655);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(654);
					match(WS);
					}
				}

				setState(657);
				translation();
				setState(668);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,94,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(659);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(658);
							match(WS);
							}
						}

						setState(661);
						match(OR);
						setState(663);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(662);
							match(WS);
							}
						}

						setState(665);
						translation();
						}
						} 
					}
					setState(670);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,94,_ctx);
				}
				setState(672);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(671);
					match(WS);
					}
				}

				setState(674);
				match(GROUP_OUT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(676);
				target_object();
				setState(677);
				match(WS);
				{
				setState(678);
				match(HAS);
				}
				setState(679);
				match(WS);
				setState(680);
				context();
				setState(691);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(682);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(681);
							match(WS);
							}
						}

						setState(684);
						match(OR);
						setState(686);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(685);
							match(WS);
							}
						}

						setState(688);
						context();
						}
						} 
					}
					setState(693);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,98,_ctx);
				}
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(694);
				target_object();
				setState(695);
				match(WS);
				{
				setState(696);
				match(HAS);
				}
				setState(697);
				match(WS);
				setState(698);
				match(GROUP_IN);
				setState(700);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(699);
					match(WS);
					}
				}

				setState(702);
				context();
				setState(713);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(704);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(703);
							match(WS);
							}
						}

						setState(706);
						match(OR);
						setState(708);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(707);
							match(WS);
							}
						}

						setState(710);
						context();
						}
						} 
					}
					setState(715);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,102,_ctx);
				}
				setState(717);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(716);
					match(WS);
					}
				}

				setState(719);
				match(GROUP_OUT);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(721);
				target_object();
				setState(722);
				match(WS);
				setState(723);
				_la = _input.LA(1);
				if ( !(_la==CONTAINS || _la==INVOLVES) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(724);
				match(WS);
				setState(725);
				property();
				setState(736);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,106,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(727);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(726);
							match(WS);
							}
						}

						setState(729);
						match(OR);
						setState(731);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(730);
							match(WS);
							}
						}

						setState(733);
						property();
						}
						} 
					}
					setState(738);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,106,_ctx);
				}
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(739);
				target_object();
				setState(740);
				match(WS);
				setState(741);
				_la = _input.LA(1);
				if ( !(_la==CONTAINS || _la==INVOLVES) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(742);
				match(WS);
				setState(743);
				match(GROUP_IN);
				setState(745);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(744);
					match(WS);
					}
				}

				setState(747);
				property();
				setState(758);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(749);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(748);
							match(WS);
							}
						}

						setState(751);
						match(OR);
						setState(753);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(752);
							match(WS);
							}
						}

						setState(755);
						property();
						}
						} 
					}
					setState(760);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
				}
				setState(762);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(761);
					match(WS);
					}
				}

				setState(764);
				match(GROUP_OUT);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(766);
				target_object();
				setState(767);
				match(DOT);
				setState(768);
				match(FQUALITY);
				setState(770);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(769);
					match(WS);
					}
				}

				setState(772);
				match(EQUALS);
				setState(774);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(773);
					match(WS);
					}
				}

				setState(776);
				quality_level();
				setState(787);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,116,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(778);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(777);
							match(WS);
							}
						}

						setState(780);
						match(OR);
						setState(782);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(781);
							match(WS);
							}
						}

						setState(784);
						quality_level();
						}
						} 
					}
					setState(789);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,116,_ctx);
				}
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(790);
				target_object();
				setState(791);
				match(DOT);
				setState(792);
				match(FQUALITY);
				setState(794);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(793);
					match(WS);
					}
				}

				setState(796);
				match(EQUALS);
				setState(798);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(797);
					match(WS);
					}
				}

				setState(800);
				match(GROUP_IN);
				setState(802);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(801);
					match(WS);
					}
				}

				setState(804);
				quality_level();
				setState(815);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(806);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(805);
							match(WS);
							}
						}

						setState(808);
						match(OR);
						setState(810);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(809);
							match(WS);
							}
						}

						setState(812);
						quality_level();
						}
						} 
					}
					setState(817);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,122,_ctx);
				}
				setState(819);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(818);
					match(WS);
					}
				}

				setState(821);
				match(GROUP_OUT);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(823);
				target_object();
				setState(824);
				match(DOT);
				setState(825);
				match(FEXPIRED);
				setState(827);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(826);
					match(WS);
					}
				}

				setState(829);
				match(EQUALS);
				setState(831);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(830);
					match(WS);
					}
				}

				setState(833);
				expiry();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(835);
				target_object();
				setState(836);
				match(DOT);
				setState(837);
				match(FDUPLICATE);
				setState(839);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(838);
					match(WS);
					}
				}

				setState(841);
				match(EQUALS);
				setState(843);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(842);
					match(WS);
					}
				}

				setState(845);
				duplicate();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Scope_notContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(csexpressParser.NOT, 0); }
		public Scope_notContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scope_not; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterScope_not(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitScope_not(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitScope_not(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Scope_notContext scope_not() throws RecognitionException {
		Scope_notContext _localctx = new Scope_notContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_scope_not);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(849);
			match(NOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Scope_operatorContext extends ParserRuleContext {
		public TerminalNode LAND() { return getToken(csexpressParser.LAND, 0); }
		public TerminalNode LOR() { return getToken(csexpressParser.LOR, 0); }
		public TerminalNode LXOR() { return getToken(csexpressParser.LXOR, 0); }
		public Scope_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scope_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterScope_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitScope_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitScope_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Scope_operatorContext scope_operator() throws RecognitionException {
		Scope_operatorContext _localctx = new Scope_operatorContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_scope_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			_la = _input.LA(1);
			if ( !(((((_la - 124)) & ~0x3f) == 0 && ((1L << (_la - 124)) & ((1L << (LOR - 124)) | (1L << (LAND - 124)) | (1L << (LXOR - 124)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class List_through_propertyContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public List<TerminalNode> DOT() { return getTokens(csexpressParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(csexpressParser.DOT, i);
		}
		public Property_fieldContext property_field() {
			return getRuleContext(Property_fieldContext.class,0);
		}
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedobjectContext predefinedobject() {
			return getRuleContext(PredefinedobjectContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List_through_propertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list_through_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterList_through_property(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitList_through_property(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitList_through_property(this);
			else return visitor.visitChildren(this);
		}
	}

	public final List_through_propertyContext list_through_property() throws RecognitionException {
		List_through_propertyContext _localctx = new List_through_propertyContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_list_through_property);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,132,_ctx) ) {
			case 1:
				{
				setState(855);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case OBJECT_IN:
					{
					setState(853);
					object();
					}
					break;
				case KSOURCE:
				case KENTITY:
				case KORIGIN:
				case KPARENT:
				case KTOP:
				case KNATURE:
					{
					setState(854);
					predefinedobject();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(858);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(857);
					match(WS);
					}
				}

				setState(860);
				match(DOT);
				setState(862);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(861);
					match(WS);
					}
				}

				}
				break;
			}
			setState(866);
			property();
			setState(869);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(867);
				match(DOT);
				setState(868);
				property_field();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Evaluated_filterContext extends ParserRuleContext {
		public Entity_scopeContext entity_scope() {
			return getRuleContext(Entity_scopeContext.class,0);
		}
		public Evaluated_filterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluated_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEvaluated_filter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEvaluated_filter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEvaluated_filter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Evaluated_filterContext evaluated_filter() throws RecognitionException {
		Evaluated_filterContext _localctx = new Evaluated_filterContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_evaluated_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(871);
			entity_scope();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContextContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public ContextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_context; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterContext(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitContext(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitContext(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextContext context() throws RecognitionException {
		ContextContext _localctx = new ContextContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_context);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(873);
			object();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CollectionContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public CollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectionContext collection() throws RecognitionException {
		CollectionContext _localctx = new CollectionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_collection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			object();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpiryContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(csexpressParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(csexpressParser.FALSE, 0); }
		public ExpiryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expiry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterExpiry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitExpiry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitExpiry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpiryContext expiry() throws RecognitionException {
		ExpiryContext _localctx = new ExpiryContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_expiry);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(877);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DuplicateContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(csexpressParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(csexpressParser.FALSE, 0); }
		public DuplicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_duplicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterDuplicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitDuplicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitDuplicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DuplicateContext duplicate() throws RecognitionException {
		DuplicateContext _localctx = new DuplicateContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_duplicate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(879);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TranslationContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public TranslationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTranslation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTranslation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTranslation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TranslationContext translation() throws RecognitionException {
		TranslationContext _localctx = new TranslationContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_translation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(881);
			object();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Action_listContext extends ParserRuleContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<TerminalNode> SEP() { return getTokens(csexpressParser.SEP); }
		public TerminalNode SEP(int i) {
			return getToken(csexpressParser.SEP, i);
		}
		public Action_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterAction_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitAction_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitAction_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Action_listContext action_list() throws RecognitionException {
		Action_listContext _localctx = new Action_listContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_action_list);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(884);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(883);
				match(WS);
				}
			}

			setState(886);
			action();
			setState(897);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,137,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(888);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(887);
						match(WS);
						}
					}

					setState(890);
					match(SEP);
					setState(892);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(891);
						match(WS);
						}
					}

					setState(894);
					action();
					}
					} 
				}
				setState(899);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,137,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public Evaluated_actionContext evaluated_action() {
			return getRuleContext(Evaluated_actionContext.class,0);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Target_propertyContext target_property() {
			return getRuleContext(Target_propertyContext.class,0);
		}
		public TerminalNode ASSIGNS() { return getToken(csexpressParser.ASSIGNS, 0); }
		public Evaluation_expressionContext evaluation_expression() {
			return getRuleContext(Evaluation_expressionContext.class,0);
		}
		public TerminalNode RAISETO() { return getToken(csexpressParser.RAISETO, 0); }
		public Quality_levelContext quality_level() {
			return getRuleContext(Quality_levelContext.class,0);
		}
		public TerminalNode STRING() { return getToken(csexpressParser.STRING, 0); }
		public Target_objectContext target_object() {
			return getRuleContext(Target_objectContext.class,0);
		}
		public TerminalNode MOVETO() { return getToken(csexpressParser.MOVETO, 0); }
		public ClassifierContext classifier() {
			return getRuleContext(ClassifierContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitAction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_action);
		int _la;
		try {
			setState(947);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(900);
				match(GROUP_IN);
				setState(902);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,138,_ctx) ) {
				case 1:
					{
					setState(901);
					match(WS);
					}
					break;
				}
				setState(904);
				evaluated_action();
				setState(906);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(905);
					match(WS);
					}
				}

				setState(908);
				match(GROUP_OUT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(910);
				target_property();
				setState(912);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(911);
					match(WS);
					}
				}

				setState(914);
				match(ASSIGNS);
				setState(916);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(915);
					match(WS);
					}
				}

				setState(918);
				evaluation_expression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(920);
				target_property();
				setState(922);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(921);
					match(WS);
					}
				}

				setState(924);
				match(RAISETO);
				setState(926);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(925);
					match(WS);
					}
				}

				setState(928);
				quality_level();
				setState(935);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
				case 1:
					{
					setState(930);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(929);
						match(WS);
						}
					}

					setState(932);
					match(GROUP_IN);
					setState(933);
					match(STRING);
					setState(934);
					match(GROUP_OUT);
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(937);
				target_object();
				setState(939);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(938);
					match(WS);
					}
				}

				setState(941);
				match(MOVETO);
				setState(943);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(942);
					match(WS);
					}
				}

				setState(945);
				classifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Evaluated_actionContext extends ParserRuleContext {
		public Action_listContext action_list() {
			return getRuleContext(Action_listContext.class,0);
		}
		public Evaluated_actionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluated_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEvaluated_action(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEvaluated_action(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEvaluated_action(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Evaluated_actionContext evaluated_action() throws RecognitionException {
		Evaluated_actionContext _localctx = new Evaluated_actionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_evaluated_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(949);
			action_list();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Target_propertyContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public TerminalNode DOT() { return getToken(csexpressParser.DOT, 0); }
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedobjectContext predefinedobject() {
			return getRuleContext(PredefinedobjectContext.class,0);
		}
		public Target_propertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_target_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTarget_property(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTarget_property(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTarget_property(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Target_propertyContext target_property() throws RecognitionException {
		Target_propertyContext _localctx = new Target_propertyContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_target_property);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(957);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(953);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case OBJECT_IN:
					{
					setState(951);
					object();
					}
					break;
				case KSOURCE:
				case KENTITY:
				case KORIGIN:
				case KPARENT:
				case KTOP:
				case KNATURE:
					{
					setState(952);
					predefinedobject();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(955);
				match(DOT);
				}
				break;
			}
			setState(959);
			property();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Target_objectContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedobjectContext predefinedobject() {
			return getRuleContext(PredefinedobjectContext.class,0);
		}
		public Target_objectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_target_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTarget_object(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTarget_object(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTarget_object(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Target_objectContext target_object() throws RecognitionException {
		Target_objectContext _localctx = new Target_objectContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_target_object);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(963);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OBJECT_IN:
				{
				setState(961);
				object();
				}
				break;
			case KSOURCE:
			case KENTITY:
			case KORIGIN:
			case KPARENT:
			case KTOP:
			case KNATURE:
				{
				setState(962);
				predefinedobject();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassifierContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedclassifierContext predefinedclassifier() {
			return getRuleContext(PredefinedclassifierContext.class,0);
		}
		public ClassifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterClassifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitClassifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitClassifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContext classifier() throws RecognitionException {
		ClassifierContext _localctx = new ClassifierContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_classifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(967);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OBJECT_IN:
				{
				setState(965);
				object();
				}
				break;
			case KNATURE:
			case KISEMPTY:
				{
				setState(966);
				predefinedclassifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredefinedclassifierContext extends ParserRuleContext {
		public TerminalNode KNATURE() { return getToken(csexpressParser.KNATURE, 0); }
		public TerminalNode KISEMPTY() { return getToken(csexpressParser.KISEMPTY, 0); }
		public PredefinedclassifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predefinedclassifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterPredefinedclassifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitPredefinedclassifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitPredefinedclassifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredefinedclassifierContext predefinedclassifier() throws RecognitionException {
		PredefinedclassifierContext _localctx = new PredefinedclassifierContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_predefinedclassifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(969);
			_la = _input.LA(1);
			if ( !(_la==KNATURE || _la==KISEMPTY) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Quality_levelContext extends ParserRuleContext {
		public TerminalNode SRED() { return getToken(csexpressParser.SRED, 0); }
		public TerminalNode SORANGE() { return getToken(csexpressParser.SORANGE, 0); }
		public TerminalNode SYELLOW() { return getToken(csexpressParser.SYELLOW, 0); }
		public TerminalNode SGREEN() { return getToken(csexpressParser.SGREEN, 0); }
		public Quality_levelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quality_level; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterQuality_level(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitQuality_level(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitQuality_level(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Quality_levelContext quality_level() throws RecognitionException {
		Quality_levelContext _localctx = new Quality_levelContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_quality_level);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(971);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SGREEN) | (1L << SYELLOW) | (1L << SORANGE) | (1L << SRED))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Evaluation_expressionContext extends ParserRuleContext {
		public List<OperandContext> operand() {
			return getRuleContexts(OperandContext.class);
		}
		public OperandContext operand(int i) {
			return getRuleContext(OperandContext.class,i);
		}
		public List<Unary_operatorContext> unary_operator() {
			return getRuleContexts(Unary_operatorContext.class);
		}
		public Unary_operatorContext unary_operator(int i) {
			return getRuleContext(Unary_operatorContext.class,i);
		}
		public List<OperatorContext> operator() {
			return getRuleContexts(OperatorContext.class);
		}
		public OperatorContext operator(int i) {
			return getRuleContext(OperatorContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Evaluation_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluation_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEvaluation_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEvaluation_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEvaluation_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Evaluation_expressionContext evaluation_expression() throws RecognitionException {
		Evaluation_expressionContext _localctx = new Evaluation_expressionContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_evaluation_expression);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(977);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(973);
				unary_operator();
				setState(975);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(974);
					match(WS);
					}
				}

				}
			}

			setState(979);
			operand();
			setState(997);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,159,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(981);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(980);
						match(WS);
						}
					}

					setState(983);
					operator();
					setState(985);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(984);
						match(WS);
						}
					}

					setState(991);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NOT) {
						{
						setState(987);
						unary_operator();
						setState(989);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(988);
							match(WS);
							}
						}

						}
					}

					setState(993);
					operand();
					}
					} 
				}
				setState(999);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,159,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandContext extends ParserRuleContext {
		public TerminalNode GROUP_IN() { return getToken(csexpressParser.GROUP_IN, 0); }
		public Evaluated_operandContext evaluated_operand() {
			return getRuleContext(Evaluated_operandContext.class,0);
		}
		public TerminalNode GROUP_OUT() { return getToken(csexpressParser.GROUP_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Function_operatorContext function_operator() {
			return getRuleContext(Function_operatorContext.class,0);
		}
		public List<Function_parameterContext> function_parameter() {
			return getRuleContexts(Function_parameterContext.class);
		}
		public Function_parameterContext function_parameter(int i) {
			return getRuleContext(Function_parameterContext.class,i);
		}
		public List<TerminalNode> SEP() { return getTokens(csexpressParser.SEP); }
		public TerminalNode SEP(int i) {
			return getToken(csexpressParser.SEP, i);
		}
		public Property_operandContext property_operand() {
			return getRuleContext(Property_operandContext.class,0);
		}
		public TerminalNode DOT() { return getToken(csexpressParser.DOT, 0); }
		public Property_fieldContext property_field() {
			return getRuleContext(Property_fieldContext.class,0);
		}
		public Literal_operandContext literal_operand() {
			return getRuleContext(Literal_operandContext.class,0);
		}
		public OperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOperand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandContext operand() throws RecognitionException {
		OperandContext _localctx = new OperandContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_operand);
		int _la;
		try {
			int _alt;
			setState(1045);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,170,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1000);
				match(GROUP_IN);
				setState(1002);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1001);
					match(WS);
					}
				}

				setState(1004);
				evaluated_operand();
				setState(1006);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1005);
					match(WS);
					}
				}

				setState(1008);
				match(GROUP_OUT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(1010);
				function_operator();
				setState(1012);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1011);
					match(WS);
					}
				}

				}
				setState(1014);
				match(GROUP_IN);
				setState(1016);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,163,_ctx) ) {
				case 1:
					{
					setState(1015);
					match(WS);
					}
					break;
				}
				setState(1032);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << FUNIQUE) | (1L << FUPPER) | (1L << FLOWER) | (1L << FREPLACE) | (1L << FADD) | (1L << FREMOVE) | (1L << KSOURCE) | (1L << KENTITY) | (1L << KORIGIN) | (1L << KPARENT) | (1L << KTOP) | (1L << KNATURE) | (1L << KNULL))) != 0) || ((((_la - 127)) & ~0x3f) == 0 && ((1L << (_la - 127)) & ((1L << (NOT - 127)) | (1L << (DOUBLE - 127)) | (1L << (INTEGER - 127)) | (1L << (CODE - 127)) | (1L << (STRING - 127)) | (1L << (GROUP_IN - 127)) | (1L << (LIST_IN - 127)) | (1L << (OBJECT_IN - 127)))) != 0)) {
					{
					setState(1018);
					function_parameter();
					setState(1029);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,166,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(1020);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==WS) {
								{
								setState(1019);
								match(WS);
								}
							}

							setState(1022);
							match(SEP);
							setState(1024);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==WS) {
								{
								setState(1023);
								match(WS);
								}
							}

							setState(1026);
							function_parameter();
							}
							} 
						}
						setState(1031);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,166,_ctx);
					}
					}
				}

				setState(1035);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1034);
					match(WS);
					}
				}

				setState(1037);
				match(GROUP_OUT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1039);
				property_operand();
				setState(1042);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(1040);
					match(DOT);
					setState(1041);
					property_field();
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1044);
				literal_operand();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Evaluated_operandContext extends ParserRuleContext {
		public Evaluation_expressionContext evaluation_expression() {
			return getRuleContext(Evaluation_expressionContext.class,0);
		}
		public Evaluated_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluated_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEvaluated_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEvaluated_operand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEvaluated_operand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Evaluated_operandContext evaluated_operand() throws RecognitionException {
		Evaluated_operandContext _localctx = new Evaluated_operandContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_evaluated_operand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1047);
			evaluation_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_operatorContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(csexpressParser.NOT, 0); }
		public Unary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterUnary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitUnary_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitUnary_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unary_operatorContext unary_operator() throws RecognitionException {
		Unary_operatorContext _localctx = new Unary_operatorContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_unary_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1049);
			match(NOT);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_operatorContext extends ParserRuleContext {
		public TerminalNode FUNIQUE() { return getToken(csexpressParser.FUNIQUE, 0); }
		public TerminalNode FUPPER() { return getToken(csexpressParser.FUPPER, 0); }
		public TerminalNode FLOWER() { return getToken(csexpressParser.FLOWER, 0); }
		public TerminalNode FREPLACE() { return getToken(csexpressParser.FREPLACE, 0); }
		public TerminalNode FADD() { return getToken(csexpressParser.FADD, 0); }
		public TerminalNode FREMOVE() { return getToken(csexpressParser.FREMOVE, 0); }
		public Function_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterFunction_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitFunction_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitFunction_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_operatorContext function_operator() throws RecognitionException {
		Function_operatorContext _localctx = new Function_operatorContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_function_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1051);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FUNIQUE) | (1L << FUPPER) | (1L << FLOWER) | (1L << FREPLACE) | (1L << FADD) | (1L << FREMOVE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_parameterContext extends ParserRuleContext {
		public Property_operandContext property_operand() {
			return getRuleContext(Property_operandContext.class,0);
		}
		public Literal_operandContext literal_operand() {
			return getRuleContext(Literal_operandContext.class,0);
		}
		public Evaluated_operandContext evaluated_operand() {
			return getRuleContext(Evaluated_operandContext.class,0);
		}
		public TerminalNode DOT() { return getToken(csexpressParser.DOT, 0); }
		public Property_fieldContext property_field() {
			return getRuleContext(Property_fieldContext.class,0);
		}
		public Function_parameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterFunction_parameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitFunction_parameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitFunction_parameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_parameterContext function_parameter() throws RecognitionException {
		Function_parameterContext _localctx = new Function_parameterContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_function_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1060);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,172,_ctx) ) {
			case 1:
				{
				setState(1053);
				property_operand();
				setState(1056);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(1054);
					match(DOT);
					setState(1055);
					property_field();
					}
				}

				}
				break;
			case 2:
				{
				setState(1058);
				literal_operand();
				}
				break;
			case 3:
				{
				setState(1059);
				evaluated_operand();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public Math_operatorContext math_operator() {
			return getRuleContext(Math_operatorContext.class,0);
		}
		public Text_operatorContext text_operator() {
			return getRuleContext(Text_operatorContext.class,0);
		}
		public Conditional_operatorContext conditional_operator() {
			return getRuleContext(Conditional_operatorContext.class,0);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_operator);
		try {
			setState(1065);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PLUS:
			case MINUS:
			case MULTIPLY:
			case DIVIDE:
			case MODULUS:
				enterOuterAlt(_localctx, 1);
				{
				setState(1062);
				math_operator();
				}
				break;
			case CONCATENATE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1063);
				text_operator();
				}
				break;
			case BETWEEN:
			case EQUALS:
			case LTE:
			case LT:
			case GTE:
			case GT:
			case DIFF:
			case LOR:
			case LAND:
			case LXOR:
			case LIKE:
			case REGEX:
			case CONTAINS:
			case NOTCONTAINS:
			case RELATES:
				enterOuterAlt(_localctx, 3);
				{
				setState(1064);
				conditional_operator();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Math_operatorContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(csexpressParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(csexpressParser.MINUS, 0); }
		public TerminalNode MULTIPLY() { return getToken(csexpressParser.MULTIPLY, 0); }
		public TerminalNode DIVIDE() { return getToken(csexpressParser.DIVIDE, 0); }
		public TerminalNode MODULUS() { return getToken(csexpressParser.MODULUS, 0); }
		public Math_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_math_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterMath_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitMath_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitMath_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Math_operatorContext math_operator() throws RecognitionException {
		Math_operatorContext _localctx = new Math_operatorContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_math_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1067);
			_la = _input.LA(1);
			if ( !(((((_la - 113)) & ~0x3f) == 0 && ((1L << (_la - 113)) & ((1L << (PLUS - 113)) | (1L << (MINUS - 113)) | (1L << (MULTIPLY - 113)) | (1L << (DIVIDE - 113)) | (1L << (MODULUS - 113)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Text_operatorContext extends ParserRuleContext {
		public TerminalNode CONCATENATE() { return getToken(csexpressParser.CONCATENATE, 0); }
		public Text_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_text_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterText_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitText_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitText_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Text_operatorContext text_operator() throws RecognitionException {
		Text_operatorContext _localctx = new Text_operatorContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_text_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1069);
			match(CONCATENATE);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Conditional_operatorContext extends ParserRuleContext {
		public TerminalNode EQUALS() { return getToken(csexpressParser.EQUALS, 0); }
		public TerminalNode DIFF() { return getToken(csexpressParser.DIFF, 0); }
		public TerminalNode LT() { return getToken(csexpressParser.LT, 0); }
		public TerminalNode LTE() { return getToken(csexpressParser.LTE, 0); }
		public TerminalNode GT() { return getToken(csexpressParser.GT, 0); }
		public TerminalNode GTE() { return getToken(csexpressParser.GTE, 0); }
		public TerminalNode LOR() { return getToken(csexpressParser.LOR, 0); }
		public TerminalNode LAND() { return getToken(csexpressParser.LAND, 0); }
		public TerminalNode LXOR() { return getToken(csexpressParser.LXOR, 0); }
		public TerminalNode CONTAINS() { return getToken(csexpressParser.CONTAINS, 0); }
		public TerminalNode LIKE() { return getToken(csexpressParser.LIKE, 0); }
		public TerminalNode REGEX() { return getToken(csexpressParser.REGEX, 0); }
		public TerminalNode RELATES() { return getToken(csexpressParser.RELATES, 0); }
		public TerminalNode BETWEEN() { return getToken(csexpressParser.BETWEEN, 0); }
		public TerminalNode NOTCONTAINS() { return getToken(csexpressParser.NOTCONTAINS, 0); }
		public Conditional_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditional_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterConditional_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitConditional_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitConditional_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Conditional_operatorContext conditional_operator() throws RecognitionException {
		Conditional_operatorContext _localctx = new Conditional_operatorContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_conditional_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1071);
			_la = _input.LA(1);
			if ( !(((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & ((1L << (BETWEEN - 106)) | (1L << (EQUALS - 106)) | (1L << (LTE - 106)) | (1L << (LT - 106)) | (1L << (GTE - 106)) | (1L << (GT - 106)) | (1L << (DIFF - 106)) | (1L << (LOR - 106)) | (1L << (LAND - 106)) | (1L << (LXOR - 106)) | (1L << (LIKE - 106)) | (1L << (REGEX - 106)) | (1L << (CONTAINS - 106)) | (1L << (NOTCONTAINS - 106)) | (1L << (RELATES - 106)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Property_operandContext extends ParserRuleContext {
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public TerminalNode DOT() { return getToken(csexpressParser.DOT, 0); }
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedobjectContext predefinedobject() {
			return getRuleContext(PredefinedobjectContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Property_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterProperty_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitProperty_operand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitProperty_operand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Property_operandContext property_operand() throws RecognitionException {
		Property_operandContext _localctx = new Property_operandContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_property_operand);
		int _la;
		try {
			setState(1088);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1073);
				property();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1077);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
				case 1:
					{
					setState(1074);
					object();
					}
					break;
				case 2:
					{
					setState(1075);
					predefinedobject();
					}
					break;
				case 3:
					{
					setState(1076);
					property();
					}
					break;
				}
				setState(1080);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1079);
					match(WS);
					}
				}

				setState(1082);
				match(DOT);
				setState(1084);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1083);
					match(WS);
					}
				}

				setState(1086);
				property();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Property_fieldContext extends ParserRuleContext {
		public TerminalNode FNUMBER() { return getToken(csexpressParser.FNUMBER, 0); }
		public TerminalNode FHTML() { return getToken(csexpressParser.FHTML, 0); }
		public TerminalNode FUNIT() { return getToken(csexpressParser.FUNIT, 0); }
		public TerminalNode FLENGTH() { return getToken(csexpressParser.FLENGTH, 0); }
		public TerminalNode FLIST_COMPLEMENT() { return getToken(csexpressParser.FLIST_COMPLEMENT, 0); }
		public TerminalNode FRANGE() { return getToken(csexpressParser.FRANGE, 0); }
		public Property_fieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterProperty_field(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitProperty_field(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitProperty_field(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Property_fieldContext property_field() throws RecognitionException {
		Property_fieldContext _localctx = new Property_fieldContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_property_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1090);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FNUMBER) | (1L << FHTML) | (1L << FUNIT) | (1L << FLENGTH) | (1L << FLIST_COMPLEMENT) | (1L << FRANGE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Literal_operandContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Tag_literalContext tag_literal() {
			return getRuleContext(Tag_literalContext.class,0);
		}
		public Range_literal_operandContext range_literal_operand() {
			return getRuleContext(Range_literal_operandContext.class,0);
		}
		public Literal_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLiteral_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLiteral_operand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLiteral_operand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Literal_operandContext literal_operand() throws RecognitionException {
		Literal_operandContext _localctx = new Literal_operandContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_literal_operand);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1095);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				{
				setState(1092);
				literal();
				}
				break;
			case 2:
				{
				setState(1093);
				tag_literal();
				}
				break;
			case 3:
				{
				setState(1094);
				range_literal_operand();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Tag_literalContext extends ParserRuleContext {
		public List<Tag_codeContext> tag_code() {
			return getRuleContexts(Tag_codeContext.class);
		}
		public Tag_codeContext tag_code(int i) {
			return getRuleContext(Tag_codeContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(csexpressParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(csexpressParser.AND, i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Tag_valueContext tag_value() {
			return getRuleContext(Tag_valueContext.class,0);
		}
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public Tag_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTag_literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTag_literal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTag_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Tag_literalContext tag_literal() throws RecognitionException {
		Tag_literalContext _localctx = new Tag_literalContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_tag_literal);
		int _la;
		try {
			int _alt;
			setState(1113);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CODE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(1097);
				tag_code();
				setState(1108);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,181,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1099);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(1098);
							match(WS);
							}
						}

						setState(1101);
						match(AND);
						setState(1103);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(1102);
							match(WS);
							}
						}

						setState(1105);
						tag_code();
						}
						} 
					}
					setState(1110);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,181,_ctx);
				}
				}
				}
				break;
			case OBJECT_IN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1111);
				tag_value();
				}
				break;
			case LIST_IN:
				enterOuterAlt(_localctx, 3);
				{
				setState(1112);
				list();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Tag_codeContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(csexpressParser.CODE, 0); }
		public Tag_codeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTag_code(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTag_code(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTag_code(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Tag_codeContext tag_code() throws RecognitionException {
		Tag_codeContext _localctx = new Tag_codeContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_tag_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1115);
			match(CODE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(csexpressParser.STRING, 0); }
		public TerminalNode DOUBLE() { return getToken(csexpressParser.DOUBLE, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode TRUE() { return getToken(csexpressParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(csexpressParser.FALSE, 0); }
		public TerminalNode KNULL() { return getToken(csexpressParser.KNULL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1117);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TRUE) | (1L << FALSE) | (1L << KNULL))) != 0) || ((((_la - 138)) & ~0x3f) == 0 && ((1L << (_la - 138)) & ((1L << (DOUBLE - 138)) | (1L << (INTEGER - 138)) | (1L << (STRING - 138)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Range_literal_operandContext extends ParserRuleContext {
		public TerminalNode OBJECT_IN() { return getToken(csexpressParser.OBJECT_IN, 0); }
		public List<RangeLiteralContext> rangeLiteral() {
			return getRuleContexts(RangeLiteralContext.class);
		}
		public RangeLiteralContext rangeLiteral(int i) {
			return getRuleContext(RangeLiteralContext.class,i);
		}
		public TerminalNode SEP() { return getToken(csexpressParser.SEP, 0); }
		public TerminalNode OBJECT_OUT() { return getToken(csexpressParser.OBJECT_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public Range_literal_operandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range_literal_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRange_literal_operand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRange_literal_operand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRange_literal_operand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Range_literal_operandContext range_literal_operand() throws RecognitionException {
		Range_literal_operandContext _localctx = new Range_literal_operandContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_range_literal_operand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1119);
			match(OBJECT_IN);
			setState(1121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1120);
				match(WS);
				}
			}

			setState(1123);
			rangeLiteral();
			setState(1125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1124);
				match(WS);
				}
			}

			setState(1127);
			match(SEP);
			setState(1129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1128);
				match(WS);
				}
			}

			setState(1131);
			rangeLiteral();
			setState(1133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1132);
				match(WS);
				}
			}

			setState(1135);
			match(OBJECT_OUT);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeLiteralContext extends ParserRuleContext {
		public TerminalNode DOUBLE() { return getToken(csexpressParser.DOUBLE, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode TIME() { return getToken(csexpressParser.TIME, 0); }
		public TerminalNode DATE() { return getToken(csexpressParser.DATE, 0); }
		public RangeLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRangeLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRangeLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRangeLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeLiteralContext rangeLiteral() throws RecognitionException {
		RangeLiteralContext _localctx = new RangeLiteralContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_rangeLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1137);
			_la = _input.LA(1);
			if ( !(((((_la - 136)) & ~0x3f) == 0 && ((1L << (_la - 136)) & ((1L << (DATE - 136)) | (1L << (TIME - 136)) | (1L << (DOUBLE - 136)) | (1L << (INTEGER - 136)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CouplingContext extends ParserRuleContext {
		public RelationcouplingContext relationcoupling() {
			return getRuleContext(RelationcouplingContext.class,0);
		}
		public ObjectcouplingContext objectcoupling() {
			return getRuleContext(ObjectcouplingContext.class,0);
		}
		public CouplingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coupling; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCoupling(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCoupling(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCoupling(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CouplingContext coupling() throws RecognitionException {
		CouplingContext _localctx = new CouplingContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_coupling);
		try {
			setState(1141);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,187,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1139);
				relationcoupling();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1140);
				objectcoupling();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationcouplingContext extends ParserRuleContext {
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public TerminalNode DYNCOUPLING() { return getToken(csexpressParser.DYNCOUPLING, 0); }
		public TerminalNode TIGHCOUPLING() { return getToken(csexpressParser.TIGHCOUPLING, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public RelationcouplingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationcoupling; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRelationcoupling(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRelationcoupling(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRelationcoupling(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationcouplingContext relationcoupling() throws RecognitionException {
		RelationcouplingContext _localctx = new RelationcouplingContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_relationcoupling);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1143);
			property();
			setState(1145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1144);
				match(WS);
				}
			}

			setState(1147);
			_la = _input.LA(1);
			if ( !(_la==DYNCOUPLING || _la==TIGHCOUPLING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1148);
				match(WS);
				}
			}

			setState(1151);
			property();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectcouplingContext extends ParserRuleContext {
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public TerminalNode DYNCOUPLING() { return getToken(csexpressParser.DYNCOUPLING, 0); }
		public TerminalNode TIGHCOUPLING() { return getToken(csexpressParser.TIGHCOUPLING, 0); }
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PredefinedobjectContext predefinedobject() {
			return getRuleContext(PredefinedobjectContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public ObjectcouplingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectcoupling; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterObjectcoupling(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitObjectcoupling(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitObjectcoupling(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectcouplingContext objectcoupling() throws RecognitionException {
		ObjectcouplingContext _localctx = new ObjectcouplingContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_objectcoupling);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1155);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OBJECT_IN:
				{
				setState(1153);
				object();
				}
				break;
			case KSOURCE:
			case KENTITY:
			case KORIGIN:
			case KPARENT:
			case KTOP:
			case KNATURE:
				{
				setState(1154);
				predefinedobject();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1157);
				match(WS);
				}
			}

			setState(1160);
			_la = _input.LA(1);
			if ( !(_la==DYNCOUPLING || _la==TIGHCOUPLING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(1162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1161);
				match(WS);
				}
			}

			setState(1164);
			property();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredefinedobjectContext extends ParserRuleContext {
		public TerminalNode KORIGIN() { return getToken(csexpressParser.KORIGIN, 0); }
		public TerminalNode KSOURCE() { return getToken(csexpressParser.KSOURCE, 0); }
		public TerminalNode KNATURE() { return getToken(csexpressParser.KNATURE, 0); }
		public TerminalNode KENTITY() { return getToken(csexpressParser.KENTITY, 0); }
		public TerminalNode KPARENT() { return getToken(csexpressParser.KPARENT, 0); }
		public TerminalNode KTOP() { return getToken(csexpressParser.KTOP, 0); }
		public PredefinedobjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predefinedobject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterPredefinedobject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitPredefinedobject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitPredefinedobject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredefinedobjectContext predefinedobject() throws RecognitionException {
		PredefinedobjectContext _localctx = new PredefinedobjectContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_predefinedobject);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1166);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << KSOURCE) | (1L << KENTITY) | (1L << KORIGIN) | (1L << KPARENT) | (1L << KTOP) | (1L << KNATURE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementContext extends ParserRuleContext {
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public Tag_valueContext tag_value() {
			return getRuleContext(Tag_valueContext.class,0);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_element);
		try {
			setState(1172);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,193,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1168);
				list();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1169);
				object();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1170);
				property();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1171);
				tag_value();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListContext extends ParserRuleContext {
		public TerminalNode LIST_IN() { return getToken(csexpressParser.LIST_IN, 0); }
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public TerminalNode LIST_OUT() { return getToken(csexpressParser.LIST_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<MetaContext> meta() {
			return getRuleContexts(MetaContext.class);
		}
		public MetaContext meta(int i) {
			return getRuleContext(MetaContext.class,i);
		}
		public List<TerminalNode> SEP() { return getTokens(csexpressParser.SEP); }
		public TerminalNode SEP(int i) {
			return getToken(csexpressParser.SEP, i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_list);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1174);
			match(LIST_IN);
			setState(1176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1175);
				match(WS);
				}
			}

			setState(1183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==METACODE) {
				{
				{
				setState(1178);
				meta();
				setState(1179);
				match(WS);
				}
				}
				setState(1185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1186);
			element();
			setState(1197);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,198,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1188);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(1187);
						match(WS);
						}
					}

					setState(1190);
					match(SEP);
					setState(1192);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(1191);
						match(WS);
						}
					}

					setState(1194);
					element();
					}
					} 
				}
				setState(1199);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,198,_ctx);
			}
			setState(1201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1200);
				match(WS);
				}
			}

			setState(1203);
			match(LIST_OUT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public TerminalNode OBJECT_IN() { return getToken(csexpressParser.OBJECT_IN, 0); }
		public TerminalNode OBJECT_OUT() { return getToken(csexpressParser.OBJECT_OUT, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public PropertytypeContext propertytype() {
			return getRuleContext(PropertytypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<MetaContext> meta() {
			return getRuleContexts(MetaContext.class);
		}
		public MetaContext meta(int i) {
			return getRuleContext(MetaContext.class,i);
		}
		public List<IdentifiereltContext> identifierelt() {
			return getRuleContexts(IdentifiereltContext.class);
		}
		public IdentifiereltContext identifierelt(int i) {
			return getRuleContext(IdentifiereltContext.class,i);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_property);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1205);
			match(OBJECT_IN);
			setState(1207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PROPERTY) {
				{
				setState(1206);
				propertytype();
				}
			}

			setState(1210);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1209);
				match(WS);
				}
			}

			setState(1217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==METACODE) {
				{
				{
				setState(1212);
				meta();
				setState(1213);
				match(WS);
				}
				}
				setState(1219);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			{
			setState(1220);
			primaryidentifier();
			}
			setState(1225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1221);
					match(WS);
					setState(1222);
					identifierelt();
					}
					} 
				}
				setState(1227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,203,_ctx);
			}
			setState(1229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1228);
				match(WS);
				}
			}

			setState(1231);
			match(OBJECT_OUT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertytypeContext extends ParserRuleContext {
		public TerminalNode PROPERTY() { return getToken(csexpressParser.PROPERTY, 0); }
		public PropertytypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertytype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterPropertytype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitPropertytype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitPropertytype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertytypeContext propertytype() throws RecognitionException {
		PropertytypeContext _localctx = new PropertytypeContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_propertytype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1233);
			match(PROPERTY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Tag_valueContext extends ParserRuleContext {
		public TerminalNode OBJECT_IN() { return getToken(csexpressParser.OBJECT_IN, 0); }
		public TerminalNode OBJECT_OUT() { return getToken(csexpressParser.OBJECT_OUT, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public TagvaluetypeContext tagvaluetype() {
			return getRuleContext(TagvaluetypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<MetaContext> meta() {
			return getRuleContexts(MetaContext.class);
		}
		public MetaContext meta(int i) {
			return getRuleContext(MetaContext.class,i);
		}
		public List<IdentifiereltContext> identifierelt() {
			return getRuleContexts(IdentifiereltContext.class);
		}
		public IdentifiereltContext identifierelt(int i) {
			return getRuleContext(IdentifiereltContext.class,i);
		}
		public Tag_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTag_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTag_value(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTag_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Tag_valueContext tag_value() throws RecognitionException {
		Tag_valueContext _localctx = new Tag_valueContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_tag_value);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1235);
			match(OBJECT_IN);
			setState(1237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TAGVALUE) {
				{
				setState(1236);
				tagvaluetype();
				}
			}

			setState(1240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1239);
				match(WS);
				}
			}

			setState(1247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==METACODE) {
				{
				{
				setState(1242);
				meta();
				setState(1243);
				match(WS);
				}
				}
				setState(1249);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			{
			setState(1250);
			primaryidentifier();
			}
			setState(1255);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,208,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1251);
					match(WS);
					setState(1252);
					identifierelt();
					}
					} 
				}
				setState(1257);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,208,_ctx);
			}
			setState(1259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1258);
				match(WS);
				}
			}

			setState(1261);
			match(OBJECT_OUT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagvaluetypeContext extends ParserRuleContext {
		public TerminalNode TAGVALUE() { return getToken(csexpressParser.TAGVALUE, 0); }
		public TagvaluetypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagvaluetype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTagvaluetype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTagvaluetype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTagvaluetype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagvaluetypeContext tagvaluetype() throws RecognitionException {
		TagvaluetypeContext _localctx = new TagvaluetypeContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_tagvaluetype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1263);
			match(TAGVALUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectContext extends ParserRuleContext {
		public TerminalNode OBJECT_IN() { return getToken(csexpressParser.OBJECT_IN, 0); }
		public ObjecttypeContext objecttype() {
			return getRuleContext(ObjecttypeContext.class,0);
		}
		public TerminalNode OBJECT_OUT() { return getToken(csexpressParser.OBJECT_OUT, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public List<MetaContext> meta() {
			return getRuleContexts(MetaContext.class);
		}
		public MetaContext meta(int i) {
			return getRuleContext(MetaContext.class,i);
		}
		public List<IdentifiereltContext> identifierelt() {
			return getRuleContexts(IdentifiereltContext.class);
		}
		public IdentifiereltContext identifierelt(int i) {
			return getRuleContext(IdentifiereltContext.class,i);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_object);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1265);
			match(OBJECT_IN);
			setState(1266);
			objecttype();
			setState(1268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1267);
				match(WS);
				}
			}

			setState(1275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==METACODE) {
				{
				{
				setState(1270);
				meta();
				setState(1271);
				match(WS);
				}
				}
				setState(1277);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			{
			setState(1278);
			primaryidentifier();
			}
			setState(1283);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,212,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1279);
					match(WS);
					setState(1280);
					identifierelt();
					}
					} 
				}
				setState(1285);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,212,_ctx);
			}
			setState(1287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1286);
				match(WS);
				}
			}

			setState(1289);
			match(OBJECT_OUT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjecttypeContext extends ParserRuleContext {
		public TerminalNode CXTUAL() { return getToken(csexpressParser.CXTUAL, 0); }
		public TerminalNode RELR() { return getToken(csexpressParser.RELR, 0); }
		public TerminalNode TAGSR() { return getToken(csexpressParser.TAGSR, 0); }
		public TerminalNode VALUER() { return getToken(csexpressParser.VALUER, 0); }
		public TerminalNode ENTITY() { return getToken(csexpressParser.ENTITY, 0); }
		public TerminalNode TAB() { return getToken(csexpressParser.TAB, 0); }
		public TerminalNode PROPERTYCOLL() { return getToken(csexpressParser.PROPERTYCOLL, 0); }
		public TerminalNode CONTEXT() { return getToken(csexpressParser.CONTEXT, 0); }
		public TerminalNode CATALOG() { return getToken(csexpressParser.CATALOG, 0); }
		public TerminalNode CLASSIFIER() { return getToken(csexpressParser.CLASSIFIER, 0); }
		public TerminalNode RULE() { return getToken(csexpressParser.RULE, 0); }
		public TerminalNode USER() { return getToken(csexpressParser.USER, 0); }
		public TerminalNode COLLECTION() { return getToken(csexpressParser.COLLECTION, 0); }
		public TerminalNode TRACKING() { return getToken(csexpressParser.TRACKING, 0); }
		public TerminalNode ORGANIZATION() { return getToken(csexpressParser.ORGANIZATION, 0); }
		public TerminalNode ENDPOINT() { return getToken(csexpressParser.ENDPOINT, 0); }
		public TerminalNode MAPPING() { return getToken(csexpressParser.MAPPING, 0); }
		public TerminalNode BPMNPROC() { return getToken(csexpressParser.BPMNPROC, 0); }
		public TerminalNode AUTHORIZATION() { return getToken(csexpressParser.AUTHORIZATION, 0); }
		public TerminalNode ROLEAUTH() { return getToken(csexpressParser.ROLEAUTH, 0); }
		public TerminalNode TASK() { return getToken(csexpressParser.TASK, 0); }
		public TerminalNode EVENT() { return getToken(csexpressParser.EVENT, 0); }
		public TerminalNode LANGCONF() { return getToken(csexpressParser.LANGCONF, 0); }
		public TerminalNode SMARTDOC() { return getToken(csexpressParser.SMARTDOC, 0); }
		public TerminalNode TEMPLATE() { return getToken(csexpressParser.TEMPLATE, 0); }
		public TerminalNode GOLDEN_RULE() { return getToken(csexpressParser.GOLDEN_RULE, 0); }
		public TerminalNode ROLE() { return getToken(csexpressParser.ROLE, 0); }
		public TerminalNode TRANSLATION() { return getToken(csexpressParser.TRANSLATION, 0); }
		public TerminalNode COUPLING_SOURCE() { return getToken(csexpressParser.COUPLING_SOURCE, 0); }
		public TerminalNode PROPERTY_PERMISSION() { return getToken(csexpressParser.PROPERTY_PERMISSION, 0); }
		public TerminalNode RELATIONSHI_PPERMISSION() { return getToken(csexpressParser.RELATIONSHI_PPERMISSION, 0); }
		public TerminalNode PERMISSION() { return getToken(csexpressParser.PERMISSION, 0); }
		public ObjecttypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objecttype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterObjecttype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitObjecttype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitObjecttype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjecttypeContext objecttype() throws RecognitionException {
		ObjecttypeContext _localctx = new ObjecttypeContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_objecttype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1291);
			_la = _input.LA(1);
			if ( !(((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (CXTUAL - 63)) | (1L << (RELR - 63)) | (1L << (TAGSR - 63)) | (1L << (VALUER - 63)) | (1L << (ENTITY - 63)) | (1L << (COLLECTION - 63)) | (1L << (TRACKING - 63)) | (1L << (TAB - 63)) | (1L << (PROPERTYCOLL - 63)) | (1L << (USER - 63)) | (1L << (CONTEXT - 63)) | (1L << (CATALOG - 63)) | (1L << (CLASSIFIER - 63)) | (1L << (RULE - 63)) | (1L << (ORGANIZATION - 63)) | (1L << (ENDPOINT - 63)) | (1L << (MAPPING - 63)) | (1L << (BPMNPROC - 63)) | (1L << (AUTHORIZATION - 63)) | (1L << (ROLEAUTH - 63)) | (1L << (TASK - 63)) | (1L << (EVENT - 63)) | (1L << (LANGCONF - 63)) | (1L << (SMARTDOC - 63)) | (1L << (TEMPLATE - 63)) | (1L << (GOLDEN_RULE - 63)) | (1L << (TRANSLATION - 63)) | (1L << (ROLE - 63)) | (1L << (PROPERTY_PERMISSION - 63)) | (1L << (RELATIONSHI_PPERMISSION - 63)) | (1L << (PERMISSION - 63)) | (1L << (COUPLING_SOURCE - 63)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MetaContext extends ParserRuleContext {
		public TerminalNode METACODE() { return getToken(csexpressParser.METACODE, 0); }
		public TerminalNode STRING() { return getToken(csexpressParser.STRING, 0); }
		public MetaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_meta; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterMeta(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitMeta(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitMeta(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaContext meta() throws RecognitionException {
		MetaContext _localctx = new MetaContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_meta);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1293);
			match(METACODE);
			setState(1296);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(1294);
				match(EQUALS);
				setState(1295);
				match(STRING);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifiereltContext extends ParserRuleContext {
		public IidspecContext iidspec() {
			return getRuleContext(IidspecContext.class,0);
		}
		public CatalogspecContext catalogspec() {
			return getRuleContext(CatalogspecContext.class,0);
		}
		public OrganizationspecContext organizationspec() {
			return getRuleContext(OrganizationspecContext.class,0);
		}
		public LocalespecContext localespec() {
			return getRuleContext(LocalespecContext.class,0);
		}
		public TypespecContext typespec() {
			return getRuleContext(TypespecContext.class,0);
		}
		public SidespecContext sidespec() {
			return getRuleContext(SidespecContext.class,0);
		}
		public ContextspecContext contextspec() {
			return getRuleContext(ContextspecContext.class,0);
		}
		public PropertyspecContext propertyspec() {
			return getRuleContext(PropertyspecContext.class,0);
		}
		public StartspecContext startspec() {
			return getRuleContext(StartspecContext.class,0);
		}
		public EndspecContext endspec() {
			return getRuleContext(EndspecContext.class,0);
		}
		public DatespecContext datespec() {
			return getRuleContext(DatespecContext.class,0);
		}
		public TagspecContext tagspec() {
			return getRuleContext(TagspecContext.class,0);
		}
		public RangespecContext rangespec() {
			return getRuleContext(RangespecContext.class,0);
		}
		public RangeofspecContext rangeofspec() {
			return getRuleContext(RangeofspecContext.class,0);
		}
		public IsversionablespecContext isversionablespec() {
			return getRuleContext(IsversionablespecContext.class,0);
		}
		public Targets_specContext targets_spec() {
			return getRuleContext(Targets_specContext.class,0);
		}
		public EndpointspecContext endpointspec() {
			return getRuleContext(EndpointspecContext.class,0);
		}
		public IdentifiereltContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierelt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterIdentifierelt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitIdentifierelt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitIdentifierelt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifiereltContext identifierelt() throws RecognitionException {
		IdentifiereltContext _localctx = new IdentifiereltContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_identifierelt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1315);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SIID:
				{
				setState(1298);
				iidspec();
				}
				break;
			case SCATALOG:
				{
				setState(1299);
				catalogspec();
				}
				break;
			case SORGANZIATION:
				{
				setState(1300);
				organizationspec();
				}
				break;
			case SLOCALE:
				{
				setState(1301);
				localespec();
				}
				break;
			case STYPE:
				{
				setState(1302);
				typespec();
				}
				break;
			case SSIDE:
				{
				setState(1303);
				sidespec();
				}
				break;
			case SCONTEXT:
				{
				setState(1304);
				contextspec();
				}
				break;
			case SPROPERTY:
				{
				setState(1305);
				propertyspec();
				}
				break;
			case SSTART:
				{
				setState(1306);
				startspec();
				}
				break;
			case SEND:
				{
				setState(1307);
				endspec();
				}
				break;
			case SDATE:
				{
				setState(1308);
				datespec();
				}
				break;
			case STAG:
				{
				setState(1309);
				tagspec();
				}
				break;
			case SRANGE:
				{
				setState(1310);
				rangespec();
				}
				break;
			case SRANGEOF:
				{
				setState(1311);
				rangeofspec();
				}
				break;
			case KISVER:
				{
				setState(1312);
				isversionablespec();
				}
				break;
			case KTARGET:
				{
				setState(1313);
				targets_spec();
				}
				break;
			case SENDPOINT:
				{
				setState(1314);
				endpointspec();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IidspecContext extends ParserRuleContext {
		public TerminalNode SIID() { return getToken(csexpressParser.SIID, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public IidspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iidspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterIidspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitIidspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitIidspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IidspecContext iidspec() throws RecognitionException {
		IidspecContext _localctx = new IidspecContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_iidspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1317);
			match(SIID);
			setState(1318);
			match(EQUALS);
			setState(1319);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatalogspecContext extends ParserRuleContext {
		public TerminalNode SCATALOG() { return getToken(csexpressParser.SCATALOG, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public CatalogspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catalogspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterCatalogspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitCatalogspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitCatalogspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatalogspecContext catalogspec() throws RecognitionException {
		CatalogspecContext _localctx = new CatalogspecContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_catalogspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1321);
			match(SCATALOG);
			setState(1322);
			match(EQUALS);
			setState(1323);
			primaryidentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrganizationspecContext extends ParserRuleContext {
		public TerminalNode SORGANZIATION() { return getToken(csexpressParser.SORGANZIATION, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public OrganizationspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_organizationspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterOrganizationspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitOrganizationspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitOrganizationspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrganizationspecContext organizationspec() throws RecognitionException {
		OrganizationspecContext _localctx = new OrganizationspecContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_organizationspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1325);
			match(SORGANZIATION);
			setState(1326);
			match(EQUALS);
			{
			setState(1327);
			primaryidentifier();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalespecContext extends ParserRuleContext {
		public TerminalNode SLOCALE() { return getToken(csexpressParser.SLOCALE, 0); }
		public TerminalNode LOCALEID() { return getToken(csexpressParser.LOCALEID, 0); }
		public TerminalNode KBASELOCALE() { return getToken(csexpressParser.KBASELOCALE, 0); }
		public LocalespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterLocalespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitLocalespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitLocalespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalespecContext localespec() throws RecognitionException {
		LocalespecContext _localctx = new LocalespecContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_localespec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1329);
			match(SLOCALE);
			setState(1330);
			match(EQUALS);
			setState(1331);
			_la = _input.LA(1);
			if ( !(_la==KBASELOCALE || _la==LOCALEID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContextspecContext extends ParserRuleContext {
		public TerminalNode SCONTEXT() { return getToken(csexpressParser.SCONTEXT, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public ContextspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contextspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterContextspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitContextspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitContextspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContextspecContext contextspec() throws RecognitionException {
		ContextspecContext _localctx = new ContextspecContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_contextspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1333);
			match(SCONTEXT);
			setState(1334);
			match(EQUALS);
			setState(1335);
			primaryidentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyspecContext extends ParserRuleContext {
		public TerminalNode SPROPERTY() { return getToken(csexpressParser.SPROPERTY, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public PropertyspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterPropertyspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitPropertyspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitPropertyspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyspecContext propertyspec() throws RecognitionException {
		PropertyspecContext _localctx = new PropertyspecContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_propertyspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1337);
			match(SPROPERTY);
			setState(1338);
			match(EQUALS);
			setState(1339);
			primaryidentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypespecContext extends ParserRuleContext {
		public TerminalNode STYPE() { return getToken(csexpressParser.STYPE, 0); }
		public TerminalNode CODE() { return getToken(csexpressParser.CODE, 0); }
		public TypespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTypespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTypespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTypespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypespecContext typespec() throws RecognitionException {
		TypespecContext _localctx = new TypespecContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_typespec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1341);
			match(STYPE);
			setState(1342);
			match(EQUALS);
			setState(1343);
			match(CODE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SidespecContext extends ParserRuleContext {
		public TerminalNode SSIDE() { return getToken(csexpressParser.SSIDE, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public SidespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sidespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterSidespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitSidespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitSidespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SidespecContext sidespec() throws RecognitionException {
		SidespecContext _localctx = new SidespecContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_sidespec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1345);
			match(SSIDE);
			setState(1346);
			match(EQUALS);
			setState(1347);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StartspecContext extends ParserRuleContext {
		public TerminalNode SSTART() { return getToken(csexpressParser.SSTART, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode DATETIME() { return getToken(csexpressParser.DATETIME, 0); }
		public StartspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_startspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterStartspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitStartspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitStartspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartspecContext startspec() throws RecognitionException {
		StartspecContext _localctx = new StartspecContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_startspec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1349);
			match(SSTART);
			setState(1350);
			match(EQUALS);
			setState(1351);
			_la = _input.LA(1);
			if ( !(_la==DATETIME || _la==INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndspecContext extends ParserRuleContext {
		public TerminalNode SEND() { return getToken(csexpressParser.SEND, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode DATETIME() { return getToken(csexpressParser.DATETIME, 0); }
		public EndspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEndspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEndspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEndspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndspecContext endspec() throws RecognitionException {
		EndspecContext _localctx = new EndspecContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_endspec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1353);
			match(SEND);
			setState(1354);
			match(EQUALS);
			setState(1355);
			_la = _input.LA(1);
			if ( !(_la==DATETIME || _la==INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagspecContext extends ParserRuleContext {
		public TerminalNode STAG() { return getToken(csexpressParser.STAG, 0); }
		public List<TagcodeContext> tagcode() {
			return getRuleContexts(TagcodeContext.class);
		}
		public TagcodeContext tagcode(int i) {
			return getRuleContext(TagcodeContext.class,i);
		}
		public List<TerminalNode> SEP() { return getTokens(csexpressParser.SEP); }
		public TerminalNode SEP(int i) {
			return getToken(csexpressParser.SEP, i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public TagspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTagspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTagspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTagspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagspecContext tagspec() throws RecognitionException {
		TagspecContext _localctx = new TagspecContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_tagspec);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1357);
			match(STAG);
			setState(1358);
			match(EQUALS);
			{
			setState(1359);
			tagcode();
			}
			setState(1370);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,218,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1361);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(1360);
						match(WS);
						}
					}

					setState(1363);
					match(SEP);
					setState(1365);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==WS) {
						{
						setState(1364);
						match(WS);
						}
					}

					{
					setState(1367);
					tagcode();
					}
					}
					} 
				}
				setState(1372);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,218,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagcodeContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(csexpressParser.CODE, 0); }
		public TerminalNode OBJECT_IN() { return getToken(csexpressParser.OBJECT_IN, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode OBJECT_OUT() { return getToken(csexpressParser.OBJECT_OUT, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public TagcodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagcode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTagcode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTagcode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTagcode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagcodeContext tagcode() throws RecognitionException {
		TagcodeContext _localctx = new TagcodeContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_tagcode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1373);
			match(CODE);
			setState(1389);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,223,_ctx) ) {
			case 1:
				{
				setState(1375);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1374);
					match(WS);
					}
				}

				setState(1377);
				match(OBJECT_IN);
				setState(1379);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1378);
					match(WS);
					}
				}

				setState(1381);
				match(INTEGER);
				setState(1383);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(1382);
					match(WS);
					}
				}

				setState(1385);
				match(OBJECT_OUT);
				setState(1387);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,222,_ctx) ) {
				case 1:
					{
					setState(1386);
					match(WS);
					}
					break;
				}
				}
				break;
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangespecContext extends ParserRuleContext {
		public TerminalNode SRANGE() { return getToken(csexpressParser.SRANGE, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public TerminalNode MINUS() { return getToken(csexpressParser.MINUS, 0); }
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public RangespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRangespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRangespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRangespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangespecContext rangespec() throws RecognitionException {
		RangespecContext _localctx = new RangespecContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_rangespec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1391);
			match(SRANGE);
			setState(1392);
			match(EQUALS);
			setState(1400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(1393);
				match(MINUS);
				setState(1397);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(1394);
					match(WS);
					}
					}
					setState(1399);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1402);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeofspecContext extends ParserRuleContext {
		public TerminalNode SRANGEOF() { return getToken(csexpressParser.SRANGEOF, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(csexpressParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(csexpressParser.INTEGER, i);
		}
		public TerminalNode OR() { return getToken(csexpressParser.OR, 0); }
		public List<TerminalNode> MINUS() { return getTokens(csexpressParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(csexpressParser.MINUS, i);
		}
		public List<TerminalNode> WS() { return getTokens(csexpressParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(csexpressParser.WS, i);
		}
		public RangeofspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeofspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterRangeofspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitRangeofspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitRangeofspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeofspecContext rangeofspec() throws RecognitionException {
		RangeofspecContext _localctx = new RangeofspecContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_rangeofspec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1404);
			match(SRANGEOF);
			setState(1405);
			match(EQUALS);
			setState(1413);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(1406);
				match(MINUS);
				setState(1410);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(1407);
					match(WS);
					}
					}
					setState(1412);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1415);
			match(INTEGER);
			setState(1417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1416);
				match(WS);
				}
			}

			setState(1419);
			match(OR);
			setState(1421);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(1420);
				match(WS);
				}
			}

			setState(1430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(1423);
				match(MINUS);
				setState(1427);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(1424);
					match(WS);
					}
					}
					setState(1429);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1432);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatespecContext extends ParserRuleContext {
		public TerminalNode SDATE() { return getToken(csexpressParser.SDATE, 0); }
		public TerminalNode KNOW() { return getToken(csexpressParser.KNOW, 0); }
		public TerminalNode DATETIME() { return getToken(csexpressParser.DATETIME, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public DatespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterDatespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitDatespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitDatespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatespecContext datespec() throws RecognitionException {
		DatespecContext _localctx = new DatespecContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_datespec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1434);
			match(SDATE);
			setState(1435);
			match(EQUALS);
			setState(1436);
			_la = _input.LA(1);
			if ( !(_la==KNOW || _la==DATETIME || _la==INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsversionablespecContext extends ParserRuleContext {
		public TerminalNode KISVER() { return getToken(csexpressParser.KISVER, 0); }
		public TerminalNode TRUE() { return getToken(csexpressParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(csexpressParser.FALSE, 0); }
		public IsversionablespecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isversionablespec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterIsversionablespec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitIsversionablespec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitIsversionablespec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IsversionablespecContext isversionablespec() throws RecognitionException {
		IsversionablespecContext _localctx = new IsversionablespecContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_isversionablespec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1438);
			match(KISVER);
			setState(1439);
			match(EQUALS);
			setState(1440);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Targets_specContext extends ParserRuleContext {
		public TerminalNode KTARGET() { return getToken(csexpressParser.KTARGET, 0); }
		public TerminalNode TARGETS() { return getToken(csexpressParser.TARGETS, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public Targets_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_targets_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterTargets_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitTargets_spec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitTargets_spec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Targets_specContext targets_spec() throws RecognitionException {
		Targets_specContext _localctx = new Targets_specContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_targets_spec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1442);
			match(KTARGET);
			setState(1443);
			match(EQUALS);
			setState(1444);
			_la = _input.LA(1);
			if ( !(_la==INTEGER || _la==TARGETS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndpointspecContext extends ParserRuleContext {
		public TerminalNode SENDPOINT() { return getToken(csexpressParser.SENDPOINT, 0); }
		public PrimaryidentifierContext primaryidentifier() {
			return getRuleContext(PrimaryidentifierContext.class,0);
		}
		public EndpointspecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpointspec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterEndpointspec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitEndpointspec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitEndpointspec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndpointspecContext endpointspec() throws RecognitionException {
		EndpointspecContext _localctx = new EndpointspecContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_endpointspec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1446);
			match(SENDPOINT);
			setState(1447);
			match(EQUALS);
			setState(1448);
			primaryidentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimaryidentifierContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(csexpressParser.CODE, 0); }
		public TerminalNode USERCODE() { return getToken(csexpressParser.USERCODE, 0); }
		public TerminalNode KNULL() { return getToken(csexpressParser.KNULL, 0); }
		public TerminalNode LOCALEID() { return getToken(csexpressParser.LOCALEID, 0); }
		public TerminalNode KSTDO() { return getToken(csexpressParser.KSTDO, 0); }
		public TerminalNode INTEGER() { return getToken(csexpressParser.INTEGER, 0); }
		public PrimaryidentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryidentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).enterPrimaryidentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof csexpressListener ) ((csexpressListener)listener).exitPrimaryidentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof csexpressVisitor ) return ((csexpressVisitor<? extends T>)visitor).visitPrimaryidentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryidentifierContext primaryidentifier() throws RecognitionException {
		PrimaryidentifierContext _localctx = new PrimaryidentifierContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_primaryidentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1450);
			_la = _input.LA(1);
			if ( !(_la==KNULL || _la==KSTDO || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & ((1L << (LOCALEID - 134)) | (1L << (INTEGER - 134)) | (1L << (CODE - 134)) | (1L << (USERCODE - 134)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a0\u05af\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00ca\n\3\3\3\3\3\3"+
		"\3\3\3\5\3\u00d0\n\3\3\4\3\4\5\4\u00d4\n\4\3\4\3\4\3\5\3\5\3\5\5\5\u00db"+
		"\n\5\3\5\3\5\3\5\5\5\u00e0\n\5\3\5\3\5\5\5\u00e4\n\5\3\5\3\5\5\5\u00e8"+
		"\n\5\3\5\3\5\5\5\u00ec\n\5\3\5\3\5\5\5\u00f0\n\5\3\6\3\6\5\6\u00f4\n\6"+
		"\3\6\3\6\5\6\u00f8\n\6\3\6\3\6\3\7\3\7\5\7\u00fe\n\7\3\7\3\7\5\7\u0102"+
		"\n\7\3\7\3\7\5\7\u0106\n\7\3\7\7\7\u0109\n\7\f\7\16\7\u010c\13\7\3\7\5"+
		"\7\u010f\n\7\3\7\3\7\3\7\3\7\5\7\u0115\n\7\3\7\3\7\5\7\u0119\n\7\3\7\7"+
		"\7\u011c\n\7\f\7\16\7\u011f\13\7\5\7\u0121\n\7\3\b\3\b\3\t\3\t\5\t\u0127"+
		"\n\t\3\t\3\t\5\t\u012b\n\t\3\t\3\t\3\n\3\n\5\n\u0131\n\n\3\n\3\n\5\n\u0135"+
		"\n\n\3\n\3\n\5\n\u0139\n\n\3\n\7\n\u013c\n\n\f\n\16\n\u013f\13\n\3\n\5"+
		"\n\u0142\n\n\3\n\3\n\3\n\3\n\5\n\u0148\n\n\3\n\3\n\5\n\u014c\n\n\3\n\7"+
		"\n\u014f\n\n\f\n\16\n\u0152\13\n\5\n\u0154\n\n\3\13\3\13\5\13\u0158\n"+
		"\13\3\13\3\13\5\13\u015c\n\13\3\13\3\13\3\f\3\f\5\f\u0162\n\f\3\f\3\f"+
		"\5\f\u0166\n\f\3\f\3\f\5\f\u016a\n\f\3\f\7\f\u016d\n\f\f\f\16\f\u0170"+
		"\13\f\3\f\5\f\u0173\n\f\3\f\3\f\3\f\3\f\5\f\u0179\n\f\3\f\3\f\5\f\u017d"+
		"\n\f\3\f\7\f\u0180\n\f\f\f\16\f\u0183\13\f\5\f\u0185\n\f\3\r\3\r\3\16"+
		"\3\16\3\17\3\17\5\17\u018d\n\17\3\17\3\17\5\17\u0191\n\17\3\17\3\17\3"+
		"\20\3\20\5\20\u0197\n\20\3\20\3\20\5\20\u019b\n\20\3\20\3\20\5\20\u019f"+
		"\n\20\3\20\7\20\u01a2\n\20\f\20\16\20\u01a5\13\20\3\20\3\20\3\20\3\20"+
		"\5\20\u01ab\n\20\3\20\3\20\5\20\u01af\n\20\3\20\7\20\u01b2\n\20\f\20\16"+
		"\20\u01b5\13\20\5\20\u01b7\n\20\3\21\3\21\3\22\3\22\5\22\u01bd\n\22\3"+
		"\22\3\22\5\22\u01c1\n\22\3\22\3\22\3\23\3\23\5\23\u01c7\n\23\3\23\3\23"+
		"\5\23\u01cb\n\23\3\23\3\23\5\23\u01cf\n\23\3\23\7\23\u01d2\n\23\f\23\16"+
		"\23\u01d5\13\23\3\23\5\23\u01d8\n\23\3\23\3\23\3\23\3\23\5\23\u01de\n"+
		"\23\3\23\3\23\5\23\u01e2\n\23\3\23\7\23\u01e5\n\23\f\23\16\23\u01e8\13"+
		"\23\5\23\u01ea\n\23\3\24\3\24\3\25\3\25\5\25\u01f0\n\25\3\25\3\25\5\25"+
		"\u01f4\n\25\3\25\3\25\3\26\3\26\5\26\u01fa\n\26\3\26\3\26\5\26\u01fe\n"+
		"\26\3\26\3\26\5\26\u0202\n\26\3\26\7\26\u0205\n\26\f\26\16\26\u0208\13"+
		"\26\3\26\3\26\3\26\3\26\5\26\u020e\n\26\3\26\3\26\5\26\u0212\n\26\3\26"+
		"\7\26\u0215\n\26\f\26\16\26\u0218\13\26\5\26\u021a\n\26\3\27\3\27\3\30"+
		"\3\30\3\30\5\30\u0221\n\30\3\30\3\30\5\30\u0225\n\30\3\30\3\30\5\30\u0229"+
		"\n\30\3\30\3\30\3\30\5\30\u022e\n\30\3\30\3\30\7\30\u0232\n\30\f\30\16"+
		"\30\u0235\13\30\3\31\3\31\5\31\u0239\n\31\3\31\3\31\5\31\u023d\n\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0247\n\31\3\31\3\31\5\31"+
		"\u024b\n\31\3\31\7\31\u024e\n\31\f\31\16\31\u0251\13\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\5\31\u0259\n\31\3\31\3\31\5\31\u025d\n\31\3\31\3\31\5"+
		"\31\u0261\n\31\3\31\7\31\u0264\n\31\f\31\16\31\u0267\13\31\3\31\5\31\u026a"+
		"\n\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0280\n\31\3\31\3\31\5\31\u0284"+
		"\n\31\3\31\7\31\u0287\n\31\f\31\16\31\u028a\13\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\5\31\u0292\n\31\3\31\3\31\5\31\u0296\n\31\3\31\3\31\5\31\u029a"+
		"\n\31\3\31\7\31\u029d\n\31\f\31\16\31\u02a0\13\31\3\31\5\31\u02a3\n\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u02ad\n\31\3\31\3\31\5\31"+
		"\u02b1\n\31\3\31\7\31\u02b4\n\31\f\31\16\31\u02b7\13\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\5\31\u02bf\n\31\3\31\3\31\5\31\u02c3\n\31\3\31\3\31\5"+
		"\31\u02c7\n\31\3\31\7\31\u02ca\n\31\f\31\16\31\u02cd\13\31\3\31\5\31\u02d0"+
		"\n\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u02da\n\31\3\31\3\31"+
		"\5\31\u02de\n\31\3\31\7\31\u02e1\n\31\f\31\16\31\u02e4\13\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\5\31\u02ec\n\31\3\31\3\31\5\31\u02f0\n\31\3\31\3"+
		"\31\5\31\u02f4\n\31\3\31\7\31\u02f7\n\31\f\31\16\31\u02fa\13\31\3\31\5"+
		"\31\u02fd\n\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0305\n\31\3\31\3\31"+
		"\5\31\u0309\n\31\3\31\3\31\5\31\u030d\n\31\3\31\3\31\5\31\u0311\n\31\3"+
		"\31\7\31\u0314\n\31\f\31\16\31\u0317\13\31\3\31\3\31\3\31\3\31\5\31\u031d"+
		"\n\31\3\31\3\31\5\31\u0321\n\31\3\31\3\31\5\31\u0325\n\31\3\31\3\31\5"+
		"\31\u0329\n\31\3\31\3\31\5\31\u032d\n\31\3\31\7\31\u0330\n\31\f\31\16"+
		"\31\u0333\13\31\3\31\5\31\u0336\n\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31"+
		"\u033e\n\31\3\31\3\31\5\31\u0342\n\31\3\31\3\31\3\31\3\31\3\31\3\31\5"+
		"\31\u034a\n\31\3\31\3\31\5\31\u034e\n\31\3\31\3\31\5\31\u0352\n\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\5\34\u035a\n\34\3\34\5\34\u035d\n\34\3\34\3"+
		"\34\5\34\u0361\n\34\5\34\u0363\n\34\3\34\3\34\3\34\5\34\u0368\n\34\3\35"+
		"\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\5#\u0377\n#\3#\3#\5#"+
		"\u037b\n#\3#\3#\5#\u037f\n#\3#\7#\u0382\n#\f#\16#\u0385\13#\3$\3$\5$\u0389"+
		"\n$\3$\3$\5$\u038d\n$\3$\3$\3$\3$\5$\u0393\n$\3$\3$\5$\u0397\n$\3$\3$"+
		"\3$\3$\5$\u039d\n$\3$\3$\5$\u03a1\n$\3$\3$\5$\u03a5\n$\3$\3$\3$\5$\u03aa"+
		"\n$\3$\3$\5$\u03ae\n$\3$\3$\5$\u03b2\n$\3$\3$\5$\u03b6\n$\3%\3%\3&\3&"+
		"\5&\u03bc\n&\3&\3&\5&\u03c0\n&\3&\3&\3\'\3\'\5\'\u03c6\n\'\3(\3(\5(\u03ca"+
		"\n(\3)\3)\3*\3*\3+\3+\5+\u03d2\n+\5+\u03d4\n+\3+\3+\5+\u03d8\n+\3+\3+"+
		"\5+\u03dc\n+\3+\3+\5+\u03e0\n+\5+\u03e2\n+\3+\3+\7+\u03e6\n+\f+\16+\u03e9"+
		"\13+\3,\3,\5,\u03ed\n,\3,\3,\5,\u03f1\n,\3,\3,\3,\3,\5,\u03f7\n,\3,\3"+
		",\5,\u03fb\n,\3,\3,\5,\u03ff\n,\3,\3,\5,\u0403\n,\3,\7,\u0406\n,\f,\16"+
		",\u0409\13,\5,\u040b\n,\3,\5,\u040e\n,\3,\3,\3,\3,\3,\5,\u0415\n,\3,\5"+
		",\u0418\n,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\5\60\u0423\n\60\3\60\3\60"+
		"\5\60\u0427\n\60\3\61\3\61\3\61\5\61\u042c\n\61\3\62\3\62\3\63\3\63\3"+
		"\64\3\64\3\65\3\65\3\65\3\65\5\65\u0438\n\65\3\65\5\65\u043b\n\65\3\65"+
		"\3\65\5\65\u043f\n\65\3\65\3\65\5\65\u0443\n\65\3\66\3\66\3\67\3\67\3"+
		"\67\5\67\u044a\n\67\38\38\58\u044e\n8\38\38\58\u0452\n8\38\78\u0455\n"+
		"8\f8\168\u0458\138\38\38\58\u045c\n8\39\39\3:\3:\3;\3;\5;\u0464\n;\3;"+
		"\3;\5;\u0468\n;\3;\3;\5;\u046c\n;\3;\3;\5;\u0470\n;\3;\3;\3<\3<\3=\3="+
		"\5=\u0478\n=\3>\3>\5>\u047c\n>\3>\3>\5>\u0480\n>\3>\3>\3?\3?\5?\u0486"+
		"\n?\3?\5?\u0489\n?\3?\3?\5?\u048d\n?\3?\3?\3@\3@\3A\3A\3A\3A\5A\u0497"+
		"\nA\3B\3B\5B\u049b\nB\3B\3B\3B\7B\u04a0\nB\fB\16B\u04a3\13B\3B\3B\5B\u04a7"+
		"\nB\3B\3B\5B\u04ab\nB\3B\7B\u04ae\nB\fB\16B\u04b1\13B\3B\5B\u04b4\nB\3"+
		"B\3B\3C\3C\5C\u04ba\nC\3C\5C\u04bd\nC\3C\3C\3C\7C\u04c2\nC\fC\16C\u04c5"+
		"\13C\3C\3C\3C\7C\u04ca\nC\fC\16C\u04cd\13C\3C\5C\u04d0\nC\3C\3C\3D\3D"+
		"\3E\3E\5E\u04d8\nE\3E\5E\u04db\nE\3E\3E\3E\7E\u04e0\nE\fE\16E\u04e3\13"+
		"E\3E\3E\3E\7E\u04e8\nE\fE\16E\u04eb\13E\3E\5E\u04ee\nE\3E\3E\3F\3F\3G"+
		"\3G\3G\5G\u04f7\nG\3G\3G\3G\7G\u04fc\nG\fG\16G\u04ff\13G\3G\3G\3G\7G\u0504"+
		"\nG\fG\16G\u0507\13G\3G\5G\u050a\nG\3G\3G\3H\3H\3I\3I\3I\5I\u0513\nI\3"+
		"J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\5J\u0526\nJ\3K\3K\3"+
		"K\3K\3L\3L\3L\3L\3M\3M\3M\3M\3N\3N\3N\3N\3O\3O\3O\3O\3P\3P\3P\3P\3Q\3"+
		"Q\3Q\3Q\3R\3R\3R\3R\3S\3S\3S\3S\3T\3T\3T\3T\3U\3U\3U\3U\5U\u0554\nU\3"+
		"U\3U\5U\u0558\nU\3U\7U\u055b\nU\fU\16U\u055e\13U\3V\3V\5V\u0562\nV\3V"+
		"\3V\5V\u0566\nV\3V\3V\5V\u056a\nV\3V\3V\5V\u056e\nV\5V\u0570\nV\3W\3W"+
		"\3W\3W\7W\u0576\nW\fW\16W\u0579\13W\5W\u057b\nW\3W\3W\3X\3X\3X\3X\7X\u0583"+
		"\nX\fX\16X\u0586\13X\5X\u0588\nX\3X\3X\5X\u058c\nX\3X\3X\5X\u0590\nX\3"+
		"X\3X\7X\u0594\nX\fX\16X\u0597\13X\5X\u0599\nX\3X\3X\3Y\3Y\3Y\3Y\3Z\3Z"+
		"\3Z\3Z\3[\3[\3[\3[\3\\\3\\\3\\\3\\\3]\3]\3]\2\2^\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnp"+
		"rtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094"+
		"\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac"+
		"\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\2\27\3\2/\67\4\2\20\20\u0088\u0088"+
		"\3\2gi\4\2\u0084\u0084\u0086\u0086\3\2~\u0080\3\2\3\4\3\2\21\22\3\2+."+
		"\3\2\5\n\3\2sw\7\2llrry\u0080\u0082\u0085\u0087\u0087\3\2;@\6\2\3\4\26"+
		"\26\u008c\u008d\u0092\u0092\3\2\u008a\u008d\3\2\u0098\u0099\4\2\13\17"+
		"\21\21\5\2AIKNPb\4\2\u0089\u0089\u008d\u008d\5\2\23\23\u0089\u0089\u008d"+
		"\u008d\4\2\u008d\u008d\u0090\u0090\7\2\26\27\u0088\u0088\u008d\u008d\u008f"+
		"\u008f\u0091\u0091\2\u0661\2\u00ba\3\2\2\2\4\u00c2\3\2\2\2\6\u00d1\3\2"+
		"\2\2\b\u00da\3\2\2\2\n\u00f1\3\2\2\2\f\u0120\3\2\2\2\16\u0122\3\2\2\2"+
		"\20\u0124\3\2\2\2\22\u0153\3\2\2\2\24\u0155\3\2\2\2\26\u0184\3\2\2\2\30"+
		"\u0186\3\2\2\2\32\u0188\3\2\2\2\34\u018a\3\2\2\2\36\u01b6\3\2\2\2 \u01b8"+
		"\3\2\2\2\"\u01ba\3\2\2\2$\u01e9\3\2\2\2&\u01eb\3\2\2\2(\u01ed\3\2\2\2"+
		"*\u0219\3\2\2\2,\u021b\3\2\2\2.\u0220\3\2\2\2\60\u0351\3\2\2\2\62\u0353"+
		"\3\2\2\2\64\u0355\3\2\2\2\66\u0362\3\2\2\28\u0369\3\2\2\2:\u036b\3\2\2"+
		"\2<\u036d\3\2\2\2>\u036f\3\2\2\2@\u0371\3\2\2\2B\u0373\3\2\2\2D\u0376"+
		"\3\2\2\2F\u03b5\3\2\2\2H\u03b7\3\2\2\2J\u03bf\3\2\2\2L\u03c5\3\2\2\2N"+
		"\u03c9\3\2\2\2P\u03cb\3\2\2\2R\u03cd\3\2\2\2T\u03d3\3\2\2\2V\u0417\3\2"+
		"\2\2X\u0419\3\2\2\2Z\u041b\3\2\2\2\\\u041d\3\2\2\2^\u0426\3\2\2\2`\u042b"+
		"\3\2\2\2b\u042d\3\2\2\2d\u042f\3\2\2\2f\u0431\3\2\2\2h\u0442\3\2\2\2j"+
		"\u0444\3\2\2\2l\u0449\3\2\2\2n\u045b\3\2\2\2p\u045d\3\2\2\2r\u045f\3\2"+
		"\2\2t\u0461\3\2\2\2v\u0473\3\2\2\2x\u0477\3\2\2\2z\u0479\3\2\2\2|\u0485"+
		"\3\2\2\2~\u0490\3\2\2\2\u0080\u0496\3\2\2\2\u0082\u0498\3\2\2\2\u0084"+
		"\u04b7\3\2\2\2\u0086\u04d3\3\2\2\2\u0088\u04d5\3\2\2\2\u008a\u04f1\3\2"+
		"\2\2\u008c\u04f3\3\2\2\2\u008e\u050d\3\2\2\2\u0090\u050f\3\2\2\2\u0092"+
		"\u0525\3\2\2\2\u0094\u0527\3\2\2\2\u0096\u052b\3\2\2\2\u0098\u052f\3\2"+
		"\2\2\u009a\u0533\3\2\2\2\u009c\u0537\3\2\2\2\u009e\u053b\3\2\2\2\u00a0"+
		"\u053f\3\2\2\2\u00a2\u0543\3\2\2\2\u00a4\u0547\3\2\2\2\u00a6\u054b\3\2"+
		"\2\2\u00a8\u054f\3\2\2\2\u00aa\u055f\3\2\2\2\u00ac\u0571\3\2\2\2\u00ae"+
		"\u057e\3\2\2\2\u00b0\u059c\3\2\2\2\u00b2\u05a0\3\2\2\2\u00b4\u05a4\3\2"+
		"\2\2\u00b6\u05a8\3\2\2\2\u00b8\u05ac\3\2\2\2\u00ba\u00bb\7c\2\2\u00bb"+
		"\u00bc\7\u00a0\2\2\u00bc\u00bd\5\b\5\2\u00bd\u00be\7\u00a0\2\2\u00be\u00bf"+
		"\7f\2\2\u00bf\u00c0\7\u00a0\2\2\u00c0\u00c1\5T+\2\u00c1\3\3\2\2\2\u00c2"+
		"\u00c3\7d\2\2\u00c3\u00c4\7\u00a0\2\2\u00c4\u00c9\5\b\5\2\u00c5\u00c6"+
		"\7\u00a0\2\2\u00c6\u00c7\7e\2\2\u00c7\u00c8\7\u00a0\2\2\u00c8\u00ca\5"+
		"T+\2\u00c9\u00c5\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cf\3\2\2\2\u00cb"+
		"\u00cc\7\u00a0\2\2\u00cc\u00cd\7m\2\2\u00cd\u00ce\7\u00a0\2\2\u00ce\u00d0"+
		"\5D#\2\u00cf\u00cb\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\5\3\2\2\2\u00d1\u00d3"+
		"\7r\2\2\u00d2\u00d4\7\u00a0\2\2\u00d3\u00d2\3\2\2\2\u00d3\u00d4\3\2\2"+
		"\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\5T+\2\u00d6\7\3\2\2\2\u00d7\u00d8\5"+
		"\n\6\2\u00d8\u00d9\7\u00a0\2\2\u00d9\u00db\3\2\2\2\u00da\u00d7\3\2\2\2"+
		"\u00da\u00db\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00df\5\20\t\2\u00dd\u00de"+
		"\7\u00a0\2\2\u00de\u00e0\5\24\13\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0\u00e3\3\2\2\2\u00e1\u00e2\7\u00a0\2\2\u00e2\u00e4\5\34\17\2"+
		"\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e6"+
		"\7\u00a0\2\2\u00e6\u00e8\5(\25\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2"+
		"\2\u00e8\u00eb\3\2\2\2\u00e9\u00ea\7\u00a0\2\2\u00ea\u00ec\5\"\22\2\u00eb"+
		"\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00ee\7\u00a0"+
		"\2\2\u00ee\u00f0\5.\30\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0"+
		"\t\3\2\2\2\u00f1\u00f3\7&\2\2\u00f2\u00f4\7\u00a0\2\2\u00f3\u00f2\3\2"+
		"\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\7r\2\2\u00f6"+
		"\u00f8\7\u00a0\2\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9"+
		"\3\2\2\2\u00f9\u00fa\5\f\7\2\u00fa\13\3\2\2\2\u00fb\u00fd\7\u009a\2\2"+
		"\u00fc\u00fe\7\u00a0\2\2\u00fd\u00fc\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00ff\u010a\5\16\b\2\u0100\u0102\7\u00a0\2\2\u0101\u0100"+
		"\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0105\7\u0093\2"+
		"\2\u0104\u0106\7\u00a0\2\2\u0105\u0104\3\2\2\2\u0105\u0106\3\2\2\2\u0106"+
		"\u0107\3\2\2\2\u0107\u0109\5\16\b\2\u0108\u0101\3\2\2\2\u0109\u010c\3"+
		"\2\2\2\u010a\u0108\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010e\3\2\2\2\u010c"+
		"\u010a\3\2\2\2\u010d\u010f\7\u00a0\2\2\u010e\u010d\3\2\2\2\u010e\u010f"+
		"\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u0111\7\u009b\2\2\u0111\u0121\3\2\2"+
		"\2\u0112\u011d\5\16\b\2\u0113\u0115\7\u00a0\2\2\u0114\u0113\3\2\2\2\u0114"+
		"\u0115\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0118\7\u0093\2\2\u0117\u0119"+
		"\7\u00a0\2\2\u0118\u0117\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011a\3\2\2"+
		"\2\u011a\u011c\5\16\b\2\u011b\u0114\3\2\2\2\u011c\u011f\3\2\2\2\u011d"+
		"\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2"+
		"\2\2\u0120\u00fb\3\2\2\2\u0120\u0112\3\2\2\2\u0121\r\3\2\2\2\u0122\u0123"+
		"\t\2\2\2\u0123\17\3\2\2\2\u0124\u0126\7\33\2\2\u0125\u0127\7\u00a0\2\2"+
		"\u0126\u0125\3\2\2\2\u0126\u0127\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u012a"+
		"\7r\2\2\u0129\u012b\7\u00a0\2\2\u012a\u0129\3\2\2\2\u012a\u012b\3\2\2"+
		"\2\u012b\u012c\3\2\2\2\u012c\u012d\5\22\n\2\u012d\21\3\2\2\2\u012e\u0130"+
		"\7\u009a\2\2\u012f\u0131\7\u00a0\2\2\u0130\u012f\3\2\2\2\u0130\u0131\3"+
		"\2\2\2\u0131\u0132\3\2\2\2\u0132\u013d\5\32\16\2\u0133\u0135\7\u00a0\2"+
		"\2\u0134\u0133\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0138"+
		"\7\u0093\2\2\u0137\u0139\7\u00a0\2\2\u0138\u0137\3\2\2\2\u0138\u0139\3"+
		"\2\2\2\u0139\u013a\3\2\2\2\u013a\u013c\5\32\16\2\u013b\u0134\3\2\2\2\u013c"+
		"\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u0141\3\2"+
		"\2\2\u013f\u013d\3\2\2\2\u0140\u0142\7\u00a0\2\2\u0141\u0140\3\2\2\2\u0141"+
		"\u0142\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\7\u009b\2\2\u0144\u0154"+
		"\3\2\2\2\u0145\u0150\5\32\16\2\u0146\u0148\7\u00a0\2\2\u0147\u0146\3\2"+
		"\2\2\u0147\u0148\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014b\7\u0093\2\2\u014a"+
		"\u014c\7\u00a0\2\2\u014b\u014a\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014d"+
		"\3\2\2\2\u014d\u014f\5\32\16\2\u014e\u0147\3\2\2\2\u014f\u0152\3\2\2\2"+
		"\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0150"+
		"\3\2\2\2\u0153\u012e\3\2\2\2\u0153\u0145\3\2\2\2\u0154\23\3\2\2\2\u0155"+
		"\u0157\7\34\2\2\u0156\u0158\7\u00a0\2\2\u0157\u0156\3\2\2\2\u0157\u0158"+
		"\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015b\7r\2\2\u015a\u015c\7\u00a0\2"+
		"\2\u015b\u015a\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015e"+
		"\5\26\f\2\u015e\25\3\2\2\2\u015f\u0161\7\u009a\2\2\u0160\u0162\7\u00a0"+
		"\2\2\u0161\u0160\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0163\3\2\2\2\u0163"+
		"\u016e\5\30\r\2\u0164\u0166\7\u00a0\2\2\u0165\u0164\3\2\2\2\u0165\u0166"+
		"\3\2\2\2\u0166\u0167\3\2\2\2\u0167\u0169\7\u0093\2\2\u0168\u016a\7\u00a0"+
		"\2\2\u0169\u0168\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\3\2\2\2\u016b"+
		"\u016d\5\30\r\2\u016c\u0165\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c\3"+
		"\2\2\2\u016e\u016f\3\2\2\2\u016f\u0172\3\2\2\2\u0170\u016e\3\2\2\2\u0171"+
		"\u0173\7\u00a0\2\2\u0172\u0171\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0174"+
		"\3\2\2\2\u0174\u0175\7\u009b\2\2\u0175\u0185\3\2\2\2\u0176\u0181\5\30"+
		"\r\2\u0177\u0179\7\u00a0\2\2\u0178\u0177\3\2\2\2\u0178\u0179\3\2\2\2\u0179"+
		"\u017a\3\2\2\2\u017a\u017c\7\u0093\2\2\u017b\u017d\7\u00a0\2\2\u017c\u017b"+
		"\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180\5\30\r\2"+
		"\u017f\u0178\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2\2\2\u0181\u0182"+
		"\3\2\2\2\u0182\u0185\3\2\2\2\u0183\u0181\3\2\2\2\u0184\u015f\3\2\2\2\u0184"+
		"\u0176\3\2\2\2\u0185\27\3\2\2\2\u0186\u0187\5\u00b8]\2\u0187\31\3\2\2"+
		"\2\u0188\u0189\7\u008f\2\2\u0189\33\3\2\2\2\u018a\u018c\7\35\2\2\u018b"+
		"\u018d\7\u00a0\2\2\u018c\u018b\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e"+
		"\3\2\2\2\u018e\u0190\7r\2\2\u018f\u0191\7\u00a0\2\2\u0190\u018f\3\2\2"+
		"\2\u0190\u0191\3\2\2\2\u0191\u0192\3\2\2\2\u0192\u0193\5\36\20\2\u0193"+
		"\35\3\2\2\2\u0194\u0196\7\u009a\2\2\u0195\u0197\7\u00a0\2\2\u0196\u0195"+
		"\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u01a3\5 \21\2\u0199"+
		"\u019b\7\u00a0\2\2\u019a\u0199\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019c"+
		"\3\2\2\2\u019c\u019e\7\u0093\2\2\u019d\u019f\7\u00a0\2\2\u019e\u019d\3"+
		"\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01a2\5 \21\2\u01a1"+
		"\u019a\3\2\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a3\u01a4\3\2"+
		"\2\2\u01a4\u01a6\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6\u01a7\7\u009b\2\2\u01a7"+
		"\u01b7\3\2\2\2\u01a8\u01b3\5 \21\2\u01a9\u01ab\7\u00a0\2\2\u01aa\u01a9"+
		"\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ae\7\u0093\2"+
		"\2\u01ad\u01af\7\u00a0\2\2\u01ae\u01ad\3\2\2\2\u01ae\u01af\3\2\2\2\u01af"+
		"\u01b0\3\2\2\2\u01b0\u01b2\5 \21\2\u01b1\u01aa\3\2\2\2\u01b2\u01b5\3\2"+
		"\2\2\u01b3\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b7\3\2\2\2\u01b5"+
		"\u01b3\3\2\2\2\u01b6\u0194\3\2\2\2\u01b6\u01a8\3\2\2\2\u01b7\37\3\2\2"+
		"\2\u01b8\u01b9\t\3\2\2\u01b9!\3\2\2\2\u01ba\u01bc\7\'\2\2\u01bb\u01bd"+
		"\7\u00a0\2\2\u01bc\u01bb\3\2\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2"+
		"\2\u01be\u01c0\7r\2\2\u01bf\u01c1\7\u00a0\2\2\u01c0\u01bf\3\2\2\2\u01c0"+
		"\u01c1\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c3\5$\23\2\u01c3#\3\2\2\2"+
		"\u01c4\u01c6\7\u009a\2\2\u01c5\u01c7\7\u00a0\2\2\u01c6\u01c5\3\2\2\2\u01c6"+
		"\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01d3\5&\24\2\u01c9\u01cb\7\u00a0"+
		"\2\2\u01ca\u01c9\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc"+
		"\u01ce\7\u0093\2\2\u01cd\u01cf\7\u00a0\2\2\u01ce\u01cd\3\2\2\2\u01ce\u01cf"+
		"\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d2\5&\24\2\u01d1\u01ca\3\2\2\2\u01d2"+
		"\u01d5\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d7\3\2"+
		"\2\2\u01d5\u01d3\3\2\2\2\u01d6\u01d8\7\u00a0\2\2\u01d7\u01d6\3\2\2\2\u01d7"+
		"\u01d8\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\7\u009b\2\2\u01da\u01ea"+
		"\3\2\2\2\u01db\u01e6\5&\24\2\u01dc\u01de\7\u00a0\2\2\u01dd\u01dc\3\2\2"+
		"\2\u01dd\u01de\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01e1\7\u0093\2\2\u01e0"+
		"\u01e2\7\u00a0\2\2\u01e1\u01e0\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e3"+
		"\3\2\2\2\u01e3\u01e5\5&\24\2\u01e4\u01dd\3\2\2\2\u01e5\u01e8\3\2\2\2\u01e6"+
		"\u01e4\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01ea\3\2\2\2\u01e8\u01e6\3\2"+
		"\2\2\u01e9\u01c4\3\2\2\2\u01e9\u01db\3\2\2\2\u01ea%\3\2\2\2\u01eb\u01ec"+
		"\7\u008d\2\2\u01ec\'\3\2\2\2\u01ed\u01ef\7\36\2\2\u01ee\u01f0\7\u00a0"+
		"\2\2\u01ef\u01ee\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1"+
		"\u01f3\7r\2\2\u01f2\u01f4\7\u00a0\2\2\u01f3\u01f2\3\2\2\2\u01f3\u01f4"+
		"\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f6\5*\26\2\u01f6)\3\2\2\2\u01f7"+
		"\u01f9\7\u009a\2\2\u01f8\u01fa\7\u00a0\2\2\u01f9\u01f8\3\2\2\2\u01f9\u01fa"+
		"\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb\u0206\5,\27\2\u01fc\u01fe\7\u00a0\2"+
		"\2\u01fd\u01fc\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u01ff\3\2\2\2\u01ff\u0201"+
		"\7\u0093\2\2\u0200\u0202\7\u00a0\2\2\u0201\u0200\3\2\2\2\u0201\u0202\3"+
		"\2\2\2\u0202\u0203\3\2\2\2\u0203\u0205\5,\27\2\u0204\u01fd\3\2\2\2\u0205"+
		"\u0208\3\2\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0209\3\2"+
		"\2\2\u0208\u0206\3\2\2\2\u0209\u020a\7\u009b\2\2\u020a\u021a\3\2\2\2\u020b"+
		"\u0216\5,\27\2\u020c\u020e\7\u00a0\2\2\u020d\u020c\3\2\2\2\u020d\u020e"+
		"\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0211\7\u0093\2\2\u0210\u0212\7\u00a0"+
		"\2\2\u0211\u0210\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\3\2\2\2\u0213"+
		"\u0215\5,\27\2\u0214\u020d\3\2\2\2\u0215\u0218\3\2\2\2\u0216\u0214\3\2"+
		"\2\2\u0216\u0217\3\2\2\2\u0217\u021a\3\2\2\2\u0218\u0216\3\2\2\2\u0219"+
		"\u01f7\3\2\2\2\u0219\u020b\3\2\2\2\u021a+\3\2\2\2\u021b\u021c\5\u00b8"+
		"]\2\u021c-\3\2\2\2\u021d\u021e\5\62\32\2\u021e\u021f\7\u00a0\2\2\u021f"+
		"\u0221\3\2\2\2\u0220\u021d\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0222\3\2"+
		"\2\2\u0222\u0233\5\60\31\2\u0223\u0225\7\u00a0\2\2\u0224\u0223\3\2\2\2"+
		"\u0224\u0225\3\2\2\2\u0225\u0226\3\2\2\2\u0226\u0228\5\64\33\2\u0227\u0229"+
		"\7\u00a0\2\2\u0228\u0227\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022d\3\2\2"+
		"\2\u022a\u022b\5\62\32\2\u022b\u022c\7\u00a0\2\2\u022c\u022e\3\2\2\2\u022d"+
		"\u022a\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0230\5\60"+
		"\31\2\u0230\u0232\3\2\2\2\u0231\u0224\3\2\2\2\u0232\u0235\3\2\2\2\u0233"+
		"\u0231\3\2\2\2\u0233\u0234\3\2\2\2\u0234/\3\2\2\2\u0235\u0233\3\2\2\2"+
		"\u0236\u0238\7\u009a\2\2\u0237\u0239\7\u00a0\2\2\u0238\u0237\3\2\2\2\u0238"+
		"\u0239\3\2\2\2\u0239\u023a\3\2\2\2\u023a\u023c\58\35\2\u023b\u023d\7\u00a0"+
		"\2\2\u023c\u023b\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u023e\3\2\2\2\u023e"+
		"\u023f\7\u009b\2\2\u023f\u0352\3\2\2\2\u0240\u0241\5L\'\2\u0241\u0242"+
		"\7\u00a0\2\2\u0242\u0243\t\4\2\2\u0243\u0244\7\u00a0\2\2\u0244\u024f\5"+
		"N(\2\u0245\u0247\7\u00a0\2\2\u0246\u0245\3\2\2\2\u0246\u0247\3\2\2\2\u0247"+
		"\u0248\3\2\2\2\u0248\u024a\7\u0093\2\2\u0249\u024b\7\u00a0\2\2\u024a\u0249"+
		"\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024e\5N(\2\u024d"+
		"\u0246\3\2\2\2\u024e\u0251\3\2\2\2\u024f\u024d\3\2\2\2\u024f\u0250\3\2"+
		"\2\2\u0250\u0352\3\2\2\2\u0251\u024f\3\2\2\2\u0252\u0253\5L\'\2\u0253"+
		"\u0254\7\u00a0\2\2\u0254\u0255\t\4\2\2\u0255\u0256\7\u00a0\2\2\u0256\u0258"+
		"\7\u009a\2\2\u0257\u0259\7\u00a0\2\2\u0258\u0257\3\2\2\2\u0258\u0259\3"+
		"\2\2\2\u0259\u025a\3\2\2\2\u025a\u0265\5N(\2\u025b\u025d\7\u00a0\2\2\u025c"+
		"\u025b\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u025e\3\2\2\2\u025e\u0260\7\u0093"+
		"\2\2\u025f\u0261\7\u00a0\2\2\u0260\u025f\3\2\2\2\u0260\u0261\3\2\2\2\u0261"+
		"\u0262\3\2\2\2\u0262\u0264\5N(\2\u0263\u025c\3\2\2\2\u0264\u0267\3\2\2"+
		"\2\u0265\u0263\3\2\2\2\u0265\u0266\3\2\2\2\u0266\u0269\3\2\2\2\u0267\u0265"+
		"\3\2\2\2\u0268\u026a\7\u00a0\2\2\u0269\u0268\3\2\2\2\u0269\u026a\3\2\2"+
		"\2\u026a\u026b\3\2\2\2\u026b\u026c\7\u009b\2\2\u026c\u0352\3\2\2\2\u026d"+
		"\u026e\5L\'\2\u026e\u026f\7\u00a0\2\2\u026f\u0270\7k\2\2\u0270\u0271\7"+
		"\u00a0\2\2\u0271\u0272\5\66\34\2\u0272\u0352\3\2\2\2\u0273\u0274\5L\'"+
		"\2\u0274\u0275\7\u00a0\2\2\u0275\u0276\7k\2\2\u0276\u0277\7\u00a0\2\2"+
		"\u0277\u0278\5<\37\2\u0278\u0352\3\2\2\2\u0279\u027a\5L\'\2\u027a\u027b"+
		"\7\u00a0\2\2\u027b\u027c\7n\2\2\u027c\u027d\7\u00a0\2\2\u027d\u0288\5"+
		"B\"\2\u027e\u0280\7\u00a0\2\2\u027f\u027e\3\2\2\2\u027f\u0280\3\2\2\2"+
		"\u0280\u0281\3\2\2\2\u0281\u0283\7\u0093\2\2\u0282\u0284\7\u00a0\2\2\u0283"+
		"\u0282\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\3\2\2\2\u0285\u0287\5B"+
		"\"\2\u0286\u027f\3\2\2\2\u0287\u028a\3\2\2\2\u0288\u0286\3\2\2\2\u0288"+
		"\u0289\3\2\2\2\u0289\u0352\3\2\2\2\u028a\u0288\3\2\2\2\u028b\u028c\5L"+
		"\'\2\u028c\u028d\7\u00a0\2\2\u028d\u028e\7n\2\2\u028e\u028f\7\u00a0\2"+
		"\2\u028f\u0291\7\u009a\2\2\u0290\u0292\7\u00a0\2\2\u0291\u0290\3\2\2\2"+
		"\u0291\u0292\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u029e\5B\"\2\u0294\u0296"+
		"\7\u00a0\2\2\u0295\u0294\3\2\2\2\u0295\u0296\3\2\2\2\u0296\u0297\3\2\2"+
		"\2\u0297\u0299\7\u0093\2\2\u0298\u029a\7\u00a0\2\2\u0299\u0298\3\2\2\2"+
		"\u0299\u029a\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u029d\5B\"\2\u029c\u0295"+
		"\3\2\2\2\u029d\u02a0\3\2\2\2\u029e\u029c\3\2\2\2\u029e\u029f\3\2\2\2\u029f"+
		"\u02a2\3\2\2\2\u02a0\u029e\3\2\2\2\u02a1\u02a3\7\u00a0\2\2\u02a2\u02a1"+
		"\3\2\2\2\u02a2\u02a3\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a5\7\u009b\2"+
		"\2\u02a5\u0352\3\2\2\2\u02a6\u02a7\5L\'\2\u02a7\u02a8\7\u00a0\2\2\u02a8"+
		"\u02a9\7j\2\2\u02a9\u02aa\7\u00a0\2\2\u02aa\u02b5\5:\36\2\u02ab\u02ad"+
		"\7\u00a0\2\2\u02ac\u02ab\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02ae\3\2\2"+
		"\2\u02ae\u02b0\7\u0093\2\2\u02af\u02b1\7\u00a0\2\2\u02b0\u02af\3\2\2\2"+
		"\u02b0\u02b1\3\2\2\2\u02b1\u02b2\3\2\2\2\u02b2\u02b4\5:\36\2\u02b3\u02ac"+
		"\3\2\2\2\u02b4\u02b7\3\2\2\2\u02b5\u02b3\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6"+
		"\u0352\3\2\2\2\u02b7\u02b5\3\2\2\2\u02b8\u02b9\5L\'\2\u02b9\u02ba\7\u00a0"+
		"\2\2\u02ba\u02bb\7j\2\2\u02bb\u02bc\7\u00a0\2\2\u02bc\u02be\7\u009a\2"+
		"\2\u02bd\u02bf\7\u00a0\2\2\u02be\u02bd\3\2\2\2\u02be\u02bf\3\2\2\2\u02bf"+
		"\u02c0\3\2\2\2\u02c0\u02cb\5:\36\2\u02c1\u02c3\7\u00a0\2\2\u02c2\u02c1"+
		"\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4\u02c6\7\u0093\2"+
		"\2\u02c5\u02c7\7\u00a0\2\2\u02c6\u02c5\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7"+
		"\u02c8\3\2\2\2\u02c8\u02ca\5:\36\2\u02c9\u02c2\3\2\2\2\u02ca\u02cd\3\2"+
		"\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc\u02cf\3\2\2\2\u02cd"+
		"\u02cb\3\2\2\2\u02ce\u02d0\7\u00a0\2\2\u02cf\u02ce\3\2\2\2\u02cf\u02d0"+
		"\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1\u02d2\7\u009b\2\2\u02d2\u0352\3\2\2"+
		"\2\u02d3\u02d4\5L\'\2\u02d4\u02d5\7\u00a0\2\2\u02d5\u02d6\t\5\2\2\u02d6"+
		"\u02d7\7\u00a0\2\2\u02d7\u02e2\5\u0084C\2\u02d8\u02da\7\u00a0\2\2\u02d9"+
		"\u02d8\3\2\2\2\u02d9\u02da\3\2\2\2\u02da\u02db\3\2\2\2\u02db\u02dd\7\u0093"+
		"\2\2\u02dc\u02de\7\u00a0\2\2\u02dd\u02dc\3\2\2\2\u02dd\u02de\3\2\2\2\u02de"+
		"\u02df\3\2\2\2\u02df\u02e1\5\u0084C\2\u02e0\u02d9\3\2\2\2\u02e1\u02e4"+
		"\3\2\2\2\u02e2\u02e0\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3\u0352\3\2\2\2\u02e4"+
		"\u02e2\3\2\2\2\u02e5\u02e6\5L\'\2\u02e6\u02e7\7\u00a0\2\2\u02e7\u02e8"+
		"\t\5\2\2\u02e8\u02e9\7\u00a0\2\2\u02e9\u02eb\7\u009a\2\2\u02ea\u02ec\7"+
		"\u00a0\2\2\u02eb\u02ea\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ed\3\2\2\2"+
		"\u02ed\u02f8\5\u0084C\2\u02ee\u02f0\7\u00a0\2\2\u02ef\u02ee\3\2\2\2\u02ef"+
		"\u02f0\3\2\2\2\u02f0\u02f1\3\2\2\2\u02f1\u02f3\7\u0093\2\2\u02f2\u02f4"+
		"\7\u00a0\2\2\u02f3\u02f2\3\2\2\2\u02f3\u02f4\3\2\2\2\u02f4\u02f5\3\2\2"+
		"\2\u02f5\u02f7\5\u0084C\2\u02f6\u02ef\3\2\2\2\u02f7\u02fa\3\2\2\2\u02f8"+
		"\u02f6\3\2\2\2\u02f8\u02f9\3\2\2\2\u02f9\u02fc\3\2\2\2\u02fa\u02f8\3\2"+
		"\2\2\u02fb\u02fd\7\u00a0\2\2\u02fc\u02fb\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd"+
		"\u02fe\3\2\2\2\u02fe\u02ff\7\u009b\2\2\u02ff\u0352\3\2\2\2\u0300\u0301"+
		"\5L\'\2\u0301\u0302\7\u0096\2\2\u0302\u0304\78\2\2\u0303\u0305\7\u00a0"+
		"\2\2\u0304\u0303\3\2\2\2\u0304\u0305\3\2\2\2\u0305\u0306\3\2\2\2\u0306"+
		"\u0308\7r\2\2\u0307\u0309\7\u00a0\2\2\u0308\u0307\3\2\2\2\u0308\u0309"+
		"\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u0315\5R*\2\u030b\u030d\7\u00a0\2\2"+
		"\u030c\u030b\3\2\2\2\u030c\u030d\3\2\2\2\u030d\u030e\3\2\2\2\u030e\u0310"+
		"\7\u0093\2\2\u030f\u0311\7\u00a0\2\2\u0310\u030f\3\2\2\2\u0310\u0311\3"+
		"\2\2\2\u0311\u0312\3\2\2\2\u0312\u0314\5R*\2\u0313\u030c\3\2\2\2\u0314"+
		"\u0317\3\2\2\2\u0315\u0313\3\2\2\2\u0315\u0316\3\2\2\2\u0316\u0352\3\2"+
		"\2\2\u0317\u0315\3\2\2\2\u0318\u0319\5L\'\2\u0319\u031a\7\u0096\2\2\u031a"+
		"\u031c\78\2\2\u031b\u031d\7\u00a0\2\2\u031c\u031b\3\2\2\2\u031c\u031d"+
		"\3\2\2\2\u031d\u031e\3\2\2\2\u031e\u0320\7r\2\2\u031f\u0321\7\u00a0\2"+
		"\2\u0320\u031f\3\2\2\2\u0320\u0321\3\2\2\2\u0321\u0322\3\2\2\2\u0322\u0324"+
		"\7\u009a\2\2\u0323\u0325\7\u00a0\2\2\u0324\u0323\3\2\2\2\u0324\u0325\3"+
		"\2\2\2\u0325\u0326\3\2\2\2\u0326\u0331\5R*\2\u0327\u0329\7\u00a0\2\2\u0328"+
		"\u0327\3\2\2\2\u0328\u0329\3\2\2\2\u0329\u032a\3\2\2\2\u032a\u032c\7\u0093"+
		"\2\2\u032b\u032d\7\u00a0\2\2\u032c\u032b\3\2\2\2\u032c\u032d\3\2\2\2\u032d"+
		"\u032e\3\2\2\2\u032e\u0330\5R*\2\u032f\u0328\3\2\2\2\u0330\u0333\3\2\2"+
		"\2\u0331\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0335\3\2\2\2\u0333\u0331"+
		"\3\2\2\2\u0334\u0336\7\u00a0\2\2\u0335\u0334\3\2\2\2\u0335\u0336\3\2\2"+
		"\2\u0336\u0337\3\2\2\2\u0337\u0338\7\u009b\2\2\u0338\u0352\3\2\2\2\u0339"+
		"\u033a\5L\'\2\u033a\u033b\7\u0096\2\2\u033b\u033d\79\2\2\u033c\u033e\7"+
		"\u00a0\2\2\u033d\u033c\3\2\2\2\u033d\u033e\3\2\2\2\u033e\u033f\3\2\2\2"+
		"\u033f\u0341\7r\2\2\u0340\u0342\7\u00a0\2\2\u0341\u0340\3\2\2\2\u0341"+
		"\u0342\3\2\2\2\u0342\u0343\3\2\2\2\u0343\u0344\5> \2\u0344\u0352\3\2\2"+
		"\2\u0345\u0346\5L\'\2\u0346\u0347\7\u0096\2\2\u0347\u0349\7:\2\2\u0348"+
		"\u034a\7\u00a0\2\2\u0349\u0348\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u034b"+
		"\3\2\2\2\u034b\u034d\7r\2\2\u034c\u034e\7\u00a0\2\2\u034d\u034c\3\2\2"+
		"\2\u034d\u034e\3\2\2\2\u034e\u034f\3\2\2\2\u034f\u0350\5@!\2\u0350\u0352"+
		"\3\2\2\2\u0351\u0236\3\2\2\2\u0351\u0240\3\2\2\2\u0351\u0252\3\2\2\2\u0351"+
		"\u026d\3\2\2\2\u0351\u0273\3\2\2\2\u0351\u0279\3\2\2\2\u0351\u028b\3\2"+
		"\2\2\u0351\u02a6\3\2\2\2\u0351\u02b8\3\2\2\2\u0351\u02d3\3\2\2\2\u0351"+
		"\u02e5\3\2\2\2\u0351\u0300\3\2\2\2\u0351\u0318\3\2\2\2\u0351\u0339\3\2"+
		"\2\2\u0351\u0345\3\2\2\2\u0352\61\3\2\2\2\u0353\u0354\7\u0081\2\2\u0354"+
		"\63\3\2\2\2\u0355\u0356\t\6\2\2\u0356\65\3\2\2\2\u0357\u035a\5\u008cG"+
		"\2\u0358\u035a\5~@\2\u0359\u0357\3\2\2\2\u0359\u0358\3\2\2\2\u035a\u035c"+
		"\3\2\2\2\u035b\u035d\7\u00a0\2\2\u035c\u035b\3\2\2\2\u035c\u035d\3\2\2"+
		"\2\u035d\u035e\3\2\2\2\u035e\u0360\7\u0096\2\2\u035f\u0361\7\u00a0\2\2"+
		"\u0360\u035f\3\2\2\2\u0360\u0361\3\2\2\2\u0361\u0363\3\2\2\2\u0362\u0359"+
		"\3\2\2\2\u0362\u0363\3\2\2\2\u0363\u0364\3\2\2\2\u0364\u0367\5\u0084C"+
		"\2\u0365\u0366\7\u0096\2\2\u0366\u0368\5j\66\2\u0367\u0365\3\2\2\2\u0367"+
		"\u0368\3\2\2\2\u0368\67\3\2\2\2\u0369\u036a\5.\30\2\u036a9\3\2\2\2\u036b"+
		"\u036c\5\u008cG\2\u036c;\3\2\2\2\u036d\u036e\5\u008cG\2\u036e=\3\2\2\2"+
		"\u036f\u0370\t\7\2\2\u0370?\3\2\2\2\u0371\u0372\t\7\2\2\u0372A\3\2\2\2"+
		"\u0373\u0374\5\u008cG\2\u0374C\3\2\2\2\u0375\u0377\7\u00a0\2\2\u0376\u0375"+
		"\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u0378\3\2\2\2\u0378\u0383\5F$\2\u0379"+
		"\u037b\7\u00a0\2\2\u037a\u0379\3\2\2\2\u037a\u037b\3\2\2\2\u037b\u037c"+
		"\3\2\2\2\u037c\u037e\7\u0095\2\2\u037d\u037f\7\u00a0\2\2\u037e\u037d\3"+
		"\2\2\2\u037e\u037f\3\2\2\2\u037f\u0380\3\2\2\2\u0380\u0382\5F$\2\u0381"+
		"\u037a\3\2\2\2\u0382\u0385\3\2\2\2\u0383\u0381\3\2\2\2\u0383\u0384\3\2"+
		"\2\2\u0384E\3\2\2\2\u0385\u0383\3\2\2\2\u0386\u0388\7\u009a\2\2\u0387"+
		"\u0389\7\u00a0\2\2\u0388\u0387\3\2\2\2\u0388\u0389\3\2\2\2\u0389\u038a"+
		"\3\2\2\2\u038a\u038c\5H%\2\u038b\u038d\7\u00a0\2\2\u038c\u038b\3\2\2\2"+
		"\u038c\u038d\3\2\2\2\u038d\u038e\3\2\2\2\u038e\u038f\7\u009b\2\2\u038f"+
		"\u03b6\3\2\2\2\u0390\u0392\5J&\2\u0391\u0393\7\u00a0\2\2\u0392\u0391\3"+
		"\2\2\2\u0392\u0393\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0396\7o\2\2\u0395"+
		"\u0397\7\u00a0\2\2\u0396\u0395\3\2\2\2\u0396\u0397\3\2\2\2\u0397\u0398"+
		"\3\2\2\2\u0398\u0399\5T+\2\u0399\u03b6\3\2\2\2\u039a\u039c\5J&\2\u039b"+
		"\u039d\7\u00a0\2\2\u039c\u039b\3\2\2\2\u039c\u039d\3\2\2\2\u039d\u039e"+
		"\3\2\2\2\u039e\u03a0\7q\2\2\u039f\u03a1\7\u00a0\2\2\u03a0\u039f\3\2\2"+
		"\2\u03a0\u03a1\3\2\2\2\u03a1\u03a2\3\2\2\2\u03a2\u03a9\5R*\2\u03a3\u03a5"+
		"\7\u00a0\2\2\u03a4\u03a3\3\2\2\2\u03a4\u03a5\3\2\2\2\u03a5\u03a6\3\2\2"+
		"\2\u03a6\u03a7\7\u009a\2\2\u03a7\u03a8\7\u0092\2\2\u03a8\u03aa\7\u009b"+
		"\2\2\u03a9\u03a4\3\2\2\2\u03a9\u03aa\3\2\2\2\u03aa\u03b6\3\2\2\2\u03ab"+
		"\u03ad\5L\'\2\u03ac\u03ae\7\u00a0\2\2\u03ad\u03ac\3\2\2\2\u03ad\u03ae"+
		"\3\2\2\2\u03ae\u03af\3\2\2\2\u03af\u03b1\7p\2\2\u03b0\u03b2\7\u00a0\2"+
		"\2\u03b1\u03b0\3\2\2\2\u03b1\u03b2\3\2\2\2\u03b2\u03b3\3\2\2\2\u03b3\u03b4"+
		"\5N(\2\u03b4\u03b6\3\2\2\2\u03b5\u0386\3\2\2\2\u03b5\u0390\3\2\2\2\u03b5"+
		"\u039a\3\2\2\2\u03b5\u03ab\3\2\2\2\u03b6G\3\2\2\2\u03b7\u03b8\5D#\2\u03b8"+
		"I\3\2\2\2\u03b9\u03bc\5\u008cG\2\u03ba\u03bc\5~@\2\u03bb\u03b9\3\2\2\2"+
		"\u03bb\u03ba\3\2\2\2\u03bc\u03bd\3\2\2\2\u03bd\u03be\7\u0096\2\2\u03be"+
		"\u03c0\3\2\2\2\u03bf\u03bb\3\2\2\2\u03bf\u03c0\3\2\2\2\u03c0\u03c1\3\2"+
		"\2\2\u03c1\u03c2\5\u0084C\2\u03c2K\3\2\2\2\u03c3\u03c6\5\u008cG\2\u03c4"+
		"\u03c6\5~@\2\u03c5\u03c3\3\2\2\2\u03c5\u03c4\3\2\2\2\u03c6M\3\2\2\2\u03c7"+
		"\u03ca\5\u008cG\2\u03c8\u03ca\5P)\2\u03c9\u03c7\3\2\2\2\u03c9\u03c8\3"+
		"\2\2\2\u03caO\3\2\2\2\u03cb\u03cc\t\b\2\2\u03ccQ\3\2\2\2\u03cd\u03ce\t"+
		"\t\2\2\u03ceS\3\2\2\2\u03cf\u03d1\5Z.\2\u03d0\u03d2\7\u00a0\2\2\u03d1"+
		"\u03d0\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2\u03d4\3\2\2\2\u03d3\u03cf\3\2"+
		"\2\2\u03d3\u03d4\3\2\2\2\u03d4\u03d5\3\2\2\2\u03d5\u03e7\5V,\2\u03d6\u03d8"+
		"\7\u00a0\2\2\u03d7\u03d6\3\2\2\2\u03d7\u03d8\3\2\2\2\u03d8\u03d9\3\2\2"+
		"\2\u03d9\u03db\5`\61\2\u03da\u03dc\7\u00a0\2\2\u03db\u03da\3\2\2\2\u03db"+
		"\u03dc\3\2\2\2\u03dc\u03e1\3\2\2\2\u03dd\u03df\5Z.\2\u03de\u03e0\7\u00a0"+
		"\2\2\u03df\u03de\3\2\2\2\u03df\u03e0\3\2\2\2\u03e0\u03e2\3\2\2\2\u03e1"+
		"\u03dd\3\2\2\2\u03e1\u03e2\3\2\2\2\u03e2\u03e3\3\2\2\2\u03e3\u03e4\5V"+
		",\2\u03e4\u03e6\3\2\2\2\u03e5\u03d7\3\2\2\2\u03e6\u03e9\3\2\2\2\u03e7"+
		"\u03e5\3\2\2\2\u03e7\u03e8\3\2\2\2\u03e8U\3\2\2\2\u03e9\u03e7\3\2\2\2"+
		"\u03ea\u03ec\7\u009a\2\2\u03eb\u03ed\7\u00a0\2\2\u03ec\u03eb\3\2\2\2\u03ec"+
		"\u03ed\3\2\2\2\u03ed\u03ee\3\2\2\2\u03ee\u03f0\5X-\2\u03ef\u03f1\7\u00a0"+
		"\2\2\u03f0\u03ef\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1\u03f2\3\2\2\2\u03f2"+
		"\u03f3\7\u009b\2\2\u03f3\u0418\3\2\2\2\u03f4\u03f6\5\\/\2\u03f5\u03f7"+
		"\7\u00a0\2\2\u03f6\u03f5\3\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u03f8\3\2\2"+
		"\2\u03f8\u03fa\7\u009a\2\2\u03f9\u03fb\7\u00a0\2\2\u03fa\u03f9\3\2\2\2"+
		"\u03fa\u03fb\3\2\2\2\u03fb\u040a\3\2\2\2\u03fc\u0407\5^\60\2\u03fd\u03ff"+
		"\7\u00a0\2\2\u03fe\u03fd\3\2\2\2\u03fe\u03ff\3\2\2\2\u03ff\u0400\3\2\2"+
		"\2\u0400\u0402\7\u0095\2\2\u0401\u0403\7\u00a0\2\2\u0402\u0401\3\2\2\2"+
		"\u0402\u0403\3\2\2\2\u0403\u0404\3\2\2\2\u0404\u0406\5^\60\2\u0405\u03fe"+
		"\3\2\2\2\u0406\u0409\3\2\2\2\u0407\u0405\3\2\2\2\u0407\u0408\3\2\2\2\u0408"+
		"\u040b\3\2\2\2\u0409\u0407\3\2\2\2\u040a\u03fc\3\2\2\2\u040a\u040b\3\2"+
		"\2\2\u040b\u040d\3\2\2\2\u040c\u040e\7\u00a0\2\2\u040d\u040c\3\2\2\2\u040d"+
		"\u040e\3\2\2\2\u040e\u040f\3\2\2\2\u040f\u0410\7\u009b\2\2\u0410\u0418"+
		"\3\2\2\2\u0411\u0414\5h\65\2\u0412\u0413\7\u0096\2\2\u0413\u0415\5j\66"+
		"\2\u0414\u0412\3\2\2\2\u0414\u0415\3\2\2\2\u0415\u0418\3\2\2\2\u0416\u0418"+
		"\5l\67\2\u0417\u03ea\3\2\2\2\u0417\u03f4\3\2\2\2\u0417\u0411\3\2\2\2\u0417"+
		"\u0416\3\2\2\2\u0418W\3\2\2\2\u0419\u041a\5T+\2\u041aY\3\2\2\2\u041b\u041c"+
		"\7\u0081\2\2\u041c[\3\2\2\2\u041d\u041e\t\n\2\2\u041e]\3\2\2\2\u041f\u0422"+
		"\5h\65\2\u0420\u0421\7\u0096\2\2\u0421\u0423\5j\66\2\u0422\u0420\3\2\2"+
		"\2\u0422\u0423\3\2\2\2\u0423\u0427\3\2\2\2\u0424\u0427\5l\67\2\u0425\u0427"+
		"\5X-\2\u0426\u041f\3\2\2\2\u0426\u0424\3\2\2\2\u0426\u0425\3\2\2\2\u0427"+
		"_\3\2\2\2\u0428\u042c\5b\62\2\u0429\u042c\5d\63\2\u042a\u042c\5f\64\2"+
		"\u042b\u0428\3\2\2\2\u042b\u0429\3\2\2\2\u042b\u042a\3\2\2\2\u042ca\3"+
		"\2\2\2\u042d\u042e\t\13\2\2\u042ec\3\2\2\2\u042f\u0430\7x\2\2\u0430e\3"+
		"\2\2\2\u0431\u0432\t\f\2\2\u0432g\3\2\2\2\u0433\u0443\5\u0084C\2\u0434"+
		"\u0438\5\u008cG\2\u0435\u0438\5~@\2\u0436\u0438\5\u0084C\2\u0437\u0434"+
		"\3\2\2\2\u0437\u0435\3\2\2\2\u0437\u0436\3\2\2\2\u0438\u043a\3\2\2\2\u0439"+
		"\u043b\7\u00a0\2\2\u043a\u0439\3\2\2\2\u043a\u043b\3\2\2\2\u043b\u043c"+
		"\3\2\2\2\u043c\u043e\7\u0096\2\2\u043d\u043f\7\u00a0\2\2\u043e\u043d\3"+
		"\2\2\2\u043e\u043f\3\2\2\2\u043f\u0440\3\2\2\2\u0440\u0441\5\u0084C\2"+
		"\u0441\u0443\3\2\2\2\u0442\u0433\3\2\2\2\u0442\u0437\3\2\2\2\u0443i\3"+
		"\2\2\2\u0444\u0445\t\r\2\2\u0445k\3\2\2\2\u0446\u044a\5r:\2\u0447\u044a"+
		"\5n8\2\u0448\u044a\5t;\2\u0449\u0446\3\2\2\2\u0449\u0447\3\2\2\2\u0449"+
		"\u0448\3\2\2\2\u044am\3\2\2\2\u044b\u0456\5p9\2\u044c\u044e\7\u00a0\2"+
		"\2\u044d\u044c\3\2\2\2\u044d\u044e\3\2\2\2\u044e\u044f\3\2\2\2\u044f\u0451"+
		"\7\u0094\2\2\u0450\u0452\7\u00a0\2\2\u0451\u0450\3\2\2\2\u0451\u0452\3"+
		"\2\2\2\u0452\u0453\3\2\2\2\u0453\u0455\5p9\2\u0454\u044d\3\2\2\2\u0455"+
		"\u0458\3\2\2\2\u0456\u0454\3\2\2\2\u0456\u0457\3\2\2\2\u0457\u045c\3\2"+
		"\2\2\u0458\u0456\3\2\2\2\u0459\u045c\5\u0088E\2\u045a\u045c\5\u0082B\2"+
		"\u045b\u044b\3\2\2\2\u045b\u0459\3\2\2\2\u045b\u045a\3\2\2\2\u045co\3"+
		"\2\2\2\u045d\u045e\7\u008f\2\2\u045eq\3\2\2\2\u045f\u0460\t\16\2\2\u0460"+
		"s\3\2\2\2\u0461\u0463\7\u009e\2\2\u0462\u0464\7\u00a0\2\2\u0463\u0462"+
		"\3\2\2\2\u0463\u0464\3\2\2\2\u0464\u0465\3\2\2\2\u0465\u0467\5v<\2\u0466"+
		"\u0468\7\u00a0\2\2\u0467\u0466\3\2\2\2\u0467\u0468\3\2\2\2\u0468\u0469"+
		"\3\2\2\2\u0469\u046b\7\u0095\2\2\u046a\u046c\7\u00a0\2\2\u046b\u046a\3"+
		"\2\2\2\u046b\u046c\3\2\2\2\u046c\u046d\3\2\2\2\u046d\u046f\5v<\2\u046e"+
		"\u0470\7\u00a0\2\2\u046f\u046e\3\2\2\2\u046f\u0470\3\2\2\2\u0470\u0471"+
		"\3\2\2\2\u0471\u0472\7\u009f\2\2\u0472u\3\2\2\2\u0473\u0474\t\17\2\2\u0474"+
		"w\3\2\2\2\u0475\u0478\5z>\2\u0476\u0478\5|?\2\u0477\u0475\3\2\2\2\u0477"+
		"\u0476\3\2\2\2\u0478y\3\2\2\2\u0479\u047b\5\u0084C\2\u047a\u047c\7\u00a0"+
		"\2\2\u047b\u047a\3\2\2\2\u047b\u047c\3\2\2\2\u047c\u047d\3\2\2\2\u047d"+
		"\u047f\t\20\2\2\u047e\u0480\7\u00a0\2\2\u047f\u047e\3\2\2\2\u047f\u0480"+
		"\3\2\2\2\u0480\u0481\3\2\2\2\u0481\u0482\5\u0084C\2\u0482{\3\2\2\2\u0483"+
		"\u0486\5\u008cG\2\u0484\u0486\5~@\2\u0485\u0483\3\2\2\2\u0485\u0484\3"+
		"\2\2\2\u0486\u0488\3\2\2\2\u0487\u0489\7\u00a0\2\2\u0488\u0487\3\2\2\2"+
		"\u0488\u0489\3\2\2\2\u0489\u048a\3\2\2\2\u048a\u048c\t\20\2\2\u048b\u048d"+
		"\7\u00a0\2\2\u048c\u048b\3\2\2\2\u048c\u048d\3\2\2\2\u048d\u048e\3\2\2"+
		"\2\u048e\u048f\5\u0084C\2\u048f}\3\2\2\2\u0490\u0491\t\21\2\2\u0491\177"+
		"\3\2\2\2\u0492\u0497\5\u0082B\2\u0493\u0497\5\u008cG\2\u0494\u0497\5\u0084"+
		"C\2\u0495\u0497\5\u0088E\2\u0496\u0492\3\2\2\2\u0496\u0493\3\2\2\2\u0496"+
		"\u0494\3\2\2\2\u0496\u0495\3\2\2\2\u0497\u0081\3\2\2\2\u0498\u049a\7\u009c"+
		"\2\2\u0499\u049b\7\u00a0\2\2\u049a\u0499\3\2\2\2\u049a\u049b\3\2\2\2\u049b"+
		"\u04a1\3\2\2\2\u049c\u049d\5\u0090I\2\u049d\u049e\7\u00a0\2\2\u049e\u04a0"+
		"\3\2\2\2\u049f\u049c\3\2\2\2\u04a0\u04a3\3\2\2\2\u04a1\u049f\3\2\2\2\u04a1"+
		"\u04a2\3\2\2\2\u04a2\u04a4\3\2\2\2\u04a3\u04a1\3\2\2\2\u04a4\u04af\5\u0080"+
		"A\2\u04a5\u04a7\7\u00a0\2\2\u04a6\u04a5\3\2\2\2\u04a6\u04a7\3\2\2\2\u04a7"+
		"\u04a8\3\2\2\2\u04a8\u04aa\7\u0095\2\2\u04a9\u04ab\7\u00a0\2\2\u04aa\u04a9"+
		"\3\2\2\2\u04aa\u04ab\3\2\2\2\u04ab\u04ac\3\2\2\2\u04ac\u04ae\5\u0080A"+
		"\2\u04ad\u04a6\3\2\2\2\u04ae\u04b1\3\2\2\2\u04af\u04ad\3\2\2\2\u04af\u04b0"+
		"\3\2\2\2\u04b0\u04b3\3\2\2\2\u04b1\u04af\3\2\2\2\u04b2\u04b4\7\u00a0\2"+
		"\2\u04b3\u04b2\3\2\2\2\u04b3\u04b4\3\2\2\2\u04b4\u04b5\3\2\2\2\u04b5\u04b6"+
		"\7\u009d\2\2\u04b6\u0083\3\2\2\2\u04b7\u04b9\7\u009e\2\2\u04b8\u04ba\5"+
		"\u0086D\2\u04b9\u04b8\3\2\2\2\u04b9\u04ba\3\2\2\2\u04ba\u04bc\3\2\2\2"+
		"\u04bb\u04bd\7\u00a0\2\2\u04bc\u04bb\3\2\2\2\u04bc\u04bd\3\2\2\2\u04bd"+
		"\u04c3\3\2\2\2\u04be\u04bf\5\u0090I\2\u04bf\u04c0\7\u00a0\2\2\u04c0\u04c2"+
		"\3\2\2\2\u04c1\u04be\3\2\2\2\u04c2\u04c5\3\2\2\2\u04c3\u04c1\3\2\2\2\u04c3"+
		"\u04c4\3\2\2\2\u04c4\u04c6\3\2\2\2\u04c5\u04c3\3\2\2\2\u04c6\u04cb\5\u00b8"+
		"]\2\u04c7\u04c8\7\u00a0\2\2\u04c8\u04ca\5\u0092J\2\u04c9\u04c7\3\2\2\2"+
		"\u04ca\u04cd\3\2\2\2\u04cb\u04c9\3\2\2\2\u04cb\u04cc\3\2\2\2\u04cc\u04cf"+
		"\3\2\2\2\u04cd\u04cb\3\2\2\2\u04ce\u04d0\7\u00a0\2\2\u04cf\u04ce\3\2\2"+
		"\2\u04cf\u04d0\3\2\2\2\u04d0\u04d1\3\2\2\2\u04d1\u04d2\7\u009f\2\2\u04d2"+
		"\u0085\3\2\2\2\u04d3\u04d4\7O\2\2\u04d4\u0087\3\2\2\2\u04d5\u04d7\7\u009e"+
		"\2\2\u04d6\u04d8\5\u008aF\2\u04d7\u04d6\3\2\2\2\u04d7\u04d8\3\2\2\2\u04d8"+
		"\u04da\3\2\2\2\u04d9\u04db\7\u00a0\2\2\u04da\u04d9\3\2\2\2\u04da\u04db"+
		"\3\2\2\2\u04db\u04e1\3\2\2\2\u04dc\u04dd\5\u0090I\2\u04dd\u04de\7\u00a0"+
		"\2\2\u04de\u04e0\3\2\2\2\u04df\u04dc\3\2\2\2\u04e0\u04e3\3\2\2\2\u04e1"+
		"\u04df\3\2\2\2\u04e1\u04e2\3\2\2\2\u04e2\u04e4\3\2\2\2\u04e3\u04e1\3\2"+
		"\2\2\u04e4\u04e9\5\u00b8]\2\u04e5\u04e6\7\u00a0\2\2\u04e6\u04e8\5\u0092"+
		"J\2\u04e7\u04e5\3\2\2\2\u04e8\u04eb\3\2\2\2\u04e9\u04e7\3\2\2\2\u04e9"+
		"\u04ea\3\2\2\2\u04ea\u04ed\3\2\2\2\u04eb\u04e9\3\2\2\2\u04ec\u04ee\7\u00a0"+
		"\2\2\u04ed\u04ec\3\2\2\2\u04ed\u04ee\3\2\2\2\u04ee\u04ef\3\2\2\2\u04ef"+
		"\u04f0\7\u009f\2\2\u04f0\u0089\3\2\2\2\u04f1\u04f2\7J\2\2\u04f2\u008b"+
		"\3\2\2\2\u04f3\u04f4\7\u009e\2\2\u04f4\u04f6\5\u008eH\2\u04f5\u04f7\7"+
		"\u00a0\2\2\u04f6\u04f5\3\2\2\2\u04f6\u04f7\3\2\2\2\u04f7\u04fd\3\2\2\2"+
		"\u04f8\u04f9\5\u0090I\2\u04f9\u04fa\7\u00a0\2\2\u04fa\u04fc\3\2\2\2\u04fb"+
		"\u04f8\3\2\2\2\u04fc\u04ff\3\2\2\2\u04fd\u04fb\3\2\2\2\u04fd\u04fe\3\2"+
		"\2\2\u04fe\u0500\3\2\2\2\u04ff\u04fd\3\2\2\2\u0500\u0505\5\u00b8]\2\u0501"+
		"\u0502\7\u00a0\2\2\u0502\u0504\5\u0092J\2\u0503\u0501\3\2\2\2\u0504\u0507"+
		"\3\2\2\2\u0505\u0503\3\2\2\2\u0505\u0506\3\2\2\2\u0506\u0509\3\2\2\2\u0507"+
		"\u0505\3\2\2\2\u0508\u050a\7\u00a0\2\2\u0509\u0508\3\2\2\2\u0509\u050a"+
		"\3\2\2\2\u050a\u050b\3\2\2\2\u050b\u050c\7\u009f\2\2\u050c\u008d\3\2\2"+
		"\2\u050d\u050e\t\22\2\2\u050e\u008f\3\2\2\2\u050f\u0512\7\u008e\2\2\u0510"+
		"\u0511\7r\2\2\u0511\u0513\7\u0092\2\2\u0512\u0510\3\2\2\2\u0512\u0513"+
		"\3\2\2\2\u0513\u0091\3\2\2\2\u0514\u0526\5\u0094K\2\u0515\u0526\5\u0096"+
		"L\2\u0516\u0526\5\u0098M\2\u0517\u0526\5\u009aN\2\u0518\u0526\5\u00a0"+
		"Q\2\u0519\u0526\5\u00a2R\2\u051a\u0526\5\u009cO\2\u051b\u0526\5\u009e"+
		"P\2\u051c\u0526\5\u00a4S\2\u051d\u0526\5\u00a6T\2\u051e\u0526\5\u00b0"+
		"Y\2\u051f\u0526\5\u00a8U\2\u0520\u0526\5\u00acW\2\u0521\u0526\5\u00ae"+
		"X\2\u0522\u0526\5\u00b2Z\2\u0523\u0526\5\u00b4[\2\u0524\u0526\5\u00b6"+
		"\\\2\u0525\u0514\3\2\2\2\u0525\u0515\3\2\2\2\u0525\u0516\3\2\2\2\u0525"+
		"\u0517\3\2\2\2\u0525\u0518\3\2\2\2\u0525\u0519\3\2\2\2\u0525\u051a\3\2"+
		"\2\2\u0525\u051b\3\2\2\2\u0525\u051c\3\2\2\2\u0525\u051d\3\2\2\2\u0525"+
		"\u051e\3\2\2\2\u0525\u051f\3\2\2\2\u0525\u0520\3\2\2\2\u0525\u0521\3\2"+
		"\2\2\u0525\u0522\3\2\2\2\u0525\u0523\3\2\2\2\u0525\u0524\3\2\2\2\u0526"+
		"\u0093\3\2\2\2\u0527\u0528\7\32\2\2\u0528\u0529\7r\2\2\u0529\u052a\7\u008d"+
		"\2\2\u052a\u0095\3\2\2\2\u052b\u052c\7\33\2\2\u052c\u052d\7r\2\2\u052d"+
		"\u052e\5\u00b8]\2\u052e\u0097\3\2\2\2\u052f\u0530\7\34\2\2\u0530\u0531"+
		"\7r\2\2\u0531\u0532\5\u00b8]\2\u0532\u0099\3\2\2\2\u0533\u0534\7\35\2"+
		"\2\u0534\u0535\7r\2\2\u0535\u0536\t\3\2\2\u0536\u009b\3\2\2\2\u0537\u0538"+
		"\7!\2\2\u0538\u0539\7r\2\2\u0539\u053a\5\u00b8]\2\u053a\u009d\3\2\2\2"+
		"\u053b\u053c\7\37\2\2\u053c\u053d\7r\2\2\u053d\u053e\5\u00b8]\2\u053e"+
		"\u009f\3\2\2\2\u053f\u0540\7(\2\2\u0540\u0541\7r\2\2\u0541\u0542\7\u008f"+
		"\2\2\u0542\u00a1\3\2\2\2\u0543\u0544\7 \2\2\u0544\u0545\7r\2\2\u0545\u0546"+
		"\7\u008d\2\2\u0546\u00a3\3\2\2\2\u0547\u0548\7$\2\2\u0548\u0549\7r\2\2"+
		"\u0549\u054a\t\23\2\2\u054a\u00a5\3\2\2\2\u054b\u054c\7%\2\2\u054c\u054d"+
		"\7r\2\2\u054d\u054e\t\23\2\2\u054e\u00a7\3\2\2\2\u054f\u0550\7\"\2\2\u0550"+
		"\u0551\7r\2\2\u0551\u055c\5\u00aaV\2\u0552\u0554\7\u00a0\2\2\u0553\u0552"+
		"\3\2\2\2\u0553\u0554\3\2\2\2\u0554\u0555\3\2\2\2\u0555\u0557\7\u0095\2"+
		"\2\u0556\u0558\7\u00a0\2\2\u0557\u0556\3\2\2\2\u0557\u0558\3\2\2\2\u0558"+
		"\u0559\3\2\2\2\u0559\u055b\5\u00aaV\2\u055a\u0553\3\2\2\2\u055b\u055e"+
		"\3\2\2\2\u055c\u055a\3\2\2\2\u055c\u055d\3\2\2\2\u055d\u00a9\3\2\2\2\u055e"+
		"\u055c\3\2\2\2\u055f\u056f\7\u008f\2\2\u0560\u0562\7\u00a0\2\2\u0561\u0560"+
		"\3\2\2\2\u0561\u0562\3\2\2\2\u0562\u0563\3\2\2\2\u0563\u0565\7\u009e\2"+
		"\2\u0564\u0566\7\u00a0\2\2\u0565\u0564\3\2\2\2\u0565\u0566\3\2\2\2\u0566"+
		"\u0567\3\2\2\2\u0567\u0569\7\u008d\2\2\u0568\u056a\7\u00a0\2\2\u0569\u0568"+
		"\3\2\2\2\u0569\u056a\3\2\2\2\u056a\u056b\3\2\2\2\u056b\u056d\7\u009f\2"+
		"\2\u056c\u056e\7\u00a0\2\2\u056d\u056c\3\2\2\2\u056d\u056e\3\2\2\2\u056e"+
		"\u0570\3\2\2\2\u056f\u0561\3\2\2\2\u056f\u0570\3\2\2\2\u0570\u00ab\3\2"+
		"\2\2\u0571\u0572\7)\2\2\u0572\u057a\7r\2\2\u0573\u0577\7t\2\2\u0574\u0576"+
		"\7\u00a0\2\2\u0575\u0574\3\2\2\2\u0576\u0579\3\2\2\2\u0577\u0575\3\2\2"+
		"\2\u0577\u0578\3\2\2\2\u0578\u057b\3\2\2\2\u0579\u0577\3\2\2\2\u057a\u0573"+
		"\3\2\2\2\u057a\u057b\3\2\2\2\u057b\u057c\3\2\2\2\u057c\u057d\7\u008d\2"+
		"\2\u057d\u00ad\3\2\2\2\u057e\u057f\7*\2\2\u057f\u0587\7r\2\2\u0580\u0584"+
		"\7t\2\2\u0581\u0583\7\u00a0\2\2\u0582\u0581\3\2\2\2\u0583\u0586\3\2\2"+
		"\2\u0584\u0582\3\2\2\2\u0584\u0585\3\2\2\2\u0585\u0588\3\2\2\2\u0586\u0584"+
		"\3\2\2\2\u0587\u0580\3\2\2\2\u0587\u0588\3\2\2\2\u0588\u0589\3\2\2\2\u0589"+
		"\u058b\7\u008d\2\2\u058a\u058c\7\u00a0\2\2\u058b\u058a\3\2\2\2\u058b\u058c"+
		"\3\2\2\2\u058c\u058d\3\2\2\2\u058d\u058f\7\u0093\2\2\u058e\u0590\7\u00a0"+
		"\2\2\u058f\u058e\3\2\2\2\u058f\u0590\3\2\2\2\u0590\u0598\3\2\2\2\u0591"+
		"\u0595\7t\2\2\u0592\u0594\7\u00a0\2\2\u0593\u0592\3\2\2\2\u0594\u0597"+
		"\3\2\2\2\u0595\u0593\3\2\2\2\u0595\u0596\3\2\2\2\u0596\u0599\3\2\2\2\u0597"+
		"\u0595\3\2\2\2\u0598\u0591\3\2\2\2\u0598\u0599\3\2\2\2\u0599\u059a\3\2"+
		"\2\2\u059a\u059b\7\u008d\2\2\u059b\u00af\3\2\2\2\u059c\u059d\7#\2\2\u059d"+
		"\u059e\7r\2\2\u059e\u059f\t\24\2\2\u059f\u00b1\3\2\2\2\u05a0\u05a1\7\30"+
		"\2\2\u05a1\u05a2\7r\2\2\u05a2\u05a3\t\7\2\2\u05a3\u00b3\3\2\2\2\u05a4"+
		"\u05a5\7\31\2\2\u05a5\u05a6\7r\2\2\u05a6\u05a7\t\25\2\2\u05a7\u00b5\3"+
		"\2\2\2\u05a8\u05a9\7\36\2\2\u05a9\u05aa\7r\2\2\u05aa\u05ab\5\u00b8]\2"+
		"\u05ab\u00b7\3\2\2\2\u05ac\u05ad\t\26\2\2\u05ad\u00b9\3\2\2\2\u00ea\u00c9"+
		"\u00cf\u00d3\u00da\u00df\u00e3\u00e7\u00eb\u00ef\u00f3\u00f7\u00fd\u0101"+
		"\u0105\u010a\u010e\u0114\u0118\u011d\u0120\u0126\u012a\u0130\u0134\u0138"+
		"\u013d\u0141\u0147\u014b\u0150\u0153\u0157\u015b\u0161\u0165\u0169\u016e"+
		"\u0172\u0178\u017c\u0181\u0184\u018c\u0190\u0196\u019a\u019e\u01a3\u01aa"+
		"\u01ae\u01b3\u01b6\u01bc\u01c0\u01c6\u01ca\u01ce\u01d3\u01d7\u01dd\u01e1"+
		"\u01e6\u01e9\u01ef\u01f3\u01f9\u01fd\u0201\u0206\u020d\u0211\u0216\u0219"+
		"\u0220\u0224\u0228\u022d\u0233\u0238\u023c\u0246\u024a\u024f\u0258\u025c"+
		"\u0260\u0265\u0269\u027f\u0283\u0288\u0291\u0295\u0299\u029e\u02a2\u02ac"+
		"\u02b0\u02b5\u02be\u02c2\u02c6\u02cb\u02cf\u02d9\u02dd\u02e2\u02eb\u02ef"+
		"\u02f3\u02f8\u02fc\u0304\u0308\u030c\u0310\u0315\u031c\u0320\u0324\u0328"+
		"\u032c\u0331\u0335\u033d\u0341\u0349\u034d\u0351\u0359\u035c\u0360\u0362"+
		"\u0367\u0376\u037a\u037e\u0383\u0388\u038c\u0392\u0396\u039c\u03a0\u03a4"+
		"\u03a9\u03ad\u03b1\u03b5\u03bb\u03bf\u03c5\u03c9\u03d1\u03d3\u03d7\u03db"+
		"\u03df\u03e1\u03e7\u03ec\u03f0\u03f6\u03fa\u03fe\u0402\u0407\u040a\u040d"+
		"\u0414\u0417\u0422\u0426\u042b\u0437\u043a\u043e\u0442\u0449\u044d\u0451"+
		"\u0456\u045b\u0463\u0467\u046b\u046f\u0477\u047b\u047f\u0485\u0488\u048c"+
		"\u0496\u049a\u04a1\u04a6\u04aa\u04af\u04b3\u04b9\u04bc\u04c3\u04cb\u04cf"+
		"\u04d7\u04da\u04e1\u04e9\u04ed\u04f6\u04fd\u0505\u0509\u0512\u0525\u0553"+
		"\u0557\u055c\u0561\u0565\u0569\u056d\u056f\u0577\u057a\u0584\u0587\u058b"+
		"\u058f\u0595\u0598";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}