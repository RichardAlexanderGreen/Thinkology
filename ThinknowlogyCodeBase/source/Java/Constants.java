/*
 *	Class:		Constants
 *	Purpose:	To hold the constants
 *	Version:	Thinknowlogy 2012 (release 2)
 *
 *************************************************************************/
/*
 *	Thinknowlogy is grammar-based software,
 *	designed to utilize the intelligence contained within grammar,
 *	in order to create intelligence through natural language in software,
 *	which is demonstrated by:
 *	- Programming in natural language;
 *	- Reasoning in natural language:
 *		- drawing conclusions,
 *		- making assumptions (with self-adjusting level of uncertainty),
 *		- asking questions about gaps in the knowledge,
 *		- detecting conflicts in the knowledge;
 *	- Detection of semantic ambiguity (static as well as dynamic);
 *	- Detection of semantic ambiguity (static as well as dynamic);
 *	- Intelligent answering of "is" questions (by providing alternatives).
 *
 *************************************************************************/
/*
 *	Copyright (C) 2009-2012, Menno Mafait
 *
 *	Your additions, modifications, suggestions and bug reports
 *	are welcome at http://mafait.org
 *
 *************************************************************************/
/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *************************************************************************/

class Constants
	{
	// Version constants
	protected static final String	PRODUCT_NAME =											"Thinknowlogy";
	protected static final String	VERSION_NAME =											"2012 (release 2)";

	protected static final short	NEW_RELEASE_AVAILABLE_YEAR =							2013;
	protected static final short	NEW_RELEASE_AVAILABLE_MONTH =							5;		// From June, 1st

	// Character constants
	protected static final char		BACK_SPACE_CHAR =										'\b';
	protected static final char		BELL_CHAR =												'\u0007';
	protected static final char		NEW_LINE_CHAR =											'\n';
	protected static final char		CARRIAGE_RETURN_CHAR =									'\r';
	protected static final char		TAB_CHAR =												'\t';

	// Console constants
	protected static final short	CONSOLE_SLEEP_TIME =									20;		// in milliseconds

	protected static final short	CONSOLE_BORDER_SIZE =									4;		// in pixels
	protected static final short	CONSOLE_BUTTON_PANE_HEIGHT =							35;		// in pixels
	protected static final short	CONSOLE_ERROR_PANE_HEIGHT =								400;	// in pixels
	protected static final short	CONSOLE_ERROR_PANE_WIDTH =								600;	// in pixels
	protected static final short	CONSOLE_MAXIMUM_FRAME_WIDTH_AT_STARTUP =				1020;	// in pixels
	protected static final short	CONSOLE_TOP_BOTTOM_SPACE =								75;		// in pixels (space between top of screen and top of GUI and between bottom of GUI and bottom of screen)
	protected static final short	CONSOLE_MAX_SUBMENU_BUTTONS =							30;

	protected static final short	CONSOLE_SUBMENU_INIT =									0;
	protected static final short	CONSOLE_SUBMENU_AMBIGUITY =								1;
	protected static final short	CONSOLE_SUBMENU_PROGRAMMING =							2;
	protected static final short	CONSOLE_SUBMENU_PROGRAMMING_CONNECT4 =					3;
	protected static final short	CONSOLE_SUBMENU_PROGRAMMING_GREETING =					4;
	protected static final short	CONSOLE_SUBMENU_PROGRAMMING_TOWER_OF_HANOI =			5;
	protected static final short	CONSOLE_SUBMENU_REASONING =								6;
	protected static final short	CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS =			7;
	protected static final short	CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS =			8;
	protected static final short	CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT =	9;
	protected static final short	CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS =			10;
	protected static final short	CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO =			11;
	protected static final short	CONSOLE_SUBMENU_HELP =									12;

	protected static final String	CONSOLE_MONO_SPACED_FONT =								"Courier New";
//	protected static final String	CONSOLE_MONO_SPACED_FONT =								"Lucida Console";

	// File constants
	protected static final String	EXAMPLES_DIRECTORY_NAME_STRING =						"data/examples/";
	protected static final String	GRAMMAR_DIRECTORY_NAME_STRING =							"data/grammar/";
	protected static final String	INFO_DIRECTORY_NAME_STRING =							"data/info/";
	protected static final String	INTERFACE_DIRECTORY_NAME_STRING =						"data/interface/";
	protected static final String	STARTUP_DIRECTORY_NAME_STRING =							"data/startup/";

	protected static final String	SOURCE_DIRECTORY_NAME_STRING =							"source/Java/";
	protected static final String	DEVELOPMENT_SOURCE_DIRECTORY_NAME_STRING =				"source/Java_release/";
	protected static final String	DEVELOPMENT_DIRECTORY_NAME_STRING =						"Release/";

	protected static final String	STARTUP_FILE_NAME_STRING =								"startup";

	protected static final String	FILE_EXTENSION_STRING =									".txt";

	// Initialization constants
	protected static final short	NO_ASSIGNMENT_LEVEL =									0;
	protected static final short	NO_ASSUMPTION_LEVEL =									0;
	protected static final short	NO_GRAMMAR_LEVEL =										0;
	protected static final short	NO_SELECTION_LEVEL =									0;
	protected static final short	NO_SOLVE_LEVEL =										0;
	protected static final short	NO_WRITE_LEVEL =										0;
	protected static final short	NO_LANGUAGE_NR =										0;
	protected static final short	NO_ORDER_NR =											0;
	protected static final short	NO_ANSWER_PARAMETER =									0;
	protected static final short	NO_ASSIGNMENT_PARAMETER =								0;
	protected static final short	NO_GRAMMAR_PARAMETER =									0;
	protected static final short	NO_IMPERATIVE_PARAMETER =								0;
	protected static final short	NO_INTERFACE_PARAMETER =								0;
	protected static final short	NO_PREPOSITION_PARAMETER =								0;
	protected static final short	NO_QUESTION_PARAMETER =									0;
	protected static final short	NO_SOLVE_STRATEGY_PARAMETER =							0;
	protected static final short	NO_USER_NR =											0;
	protected static final short	NO_WORD_PARAMETER =										0;
	protected static final short	NO_LIST_NR =											Short.MAX_VALUE;

	protected static final short	MAX_LEVEL =												Short.MAX_VALUE;
	protected static final short	MAX_ORDER_NR =											Short.MAX_VALUE;
	protected static final short	MAX_POSITION =											Short.MAX_VALUE;

	protected static final int		NO_CENTER_WIDTH =										0;
	protected static final int		NO_COLLECTION_NR =										0;
	protected static final int		NO_CONTEXT_NR =											0;
	protected static final int		NO_ITEM_NR =											0;
	protected static final int		NO_SENTENCE_NR =										0;

	protected static final int		MAX_NUMBER =											Integer.MAX_VALUE;
	protected static final int		MAX_ITEM_NR =											Integer.MAX_VALUE;
	protected static final int		MAX_QUERY_PARAMETER =									Integer.MAX_VALUE;
	protected static final int		MAX_SENTENCE_NR =										Integer.MAX_VALUE;
	protected static final int		MAX_COLLECTION_NR =										Integer.MAX_VALUE;
	protected static final int		MAX_CONTEXT_NR =										Integer.MAX_VALUE;
	protected static final int		MAX_PROGRESS =											1024*1024*1024;

	protected static final long		NO_SCORE =												0L;
	protected static final long		MAX_SCORE =												Long.MAX_VALUE;
	protected static final long		WINNING_SCORE =											Long.MAX_VALUE;

	// Presentation error constants
	protected static final String	PRESENTATION_ERROR_INTERNAL_TITLE_STRING =				"Internal error";
	protected static final String	PRESENTATION_ERROR_CURRENT_ID_STRING =					" with current item id: ";

	protected static final String	PRESENTATION_ERROR_CLASS_STRING =						"Class: ";
	protected static final String	PRESENTATION_ERROR_SUPERCLASS_STRING =					" / superclass ";
	protected static final String	PRESENTATION_ERROR_METHOD_STRING =						".\nMethod: ";
	protected static final String	PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME =			"<init>";
	protected static final String	PRESENTATION_ERROR_METHOD_WORD_START_STRING =			" of word \"";
	protected static final String	PRESENTATION_ERROR_METHOD_WORD_END_STRING =				"\"";
	protected static final String	PRESENTATION_ERROR_METHOD_LIST_START_STRING =			" in list <";
	protected static final String	PRESENTATION_ERROR_METHOD_LIST_END_STRING =				">";
	protected static final String	PRESENTATION_ERROR_MESSAGE_START_STRING =				".\nError: ";
	protected static final String	PRESENTATION_ERROR_MESSAGE_END_STRING =					".\n\n";

	// Presentation prompt constants
	protected static final short	PRESENTATION_PROMPT_READ =								0;
	protected static final short	PRESENTATION_PROMPT_WRITE =								1;
	protected static final short	PRESENTATION_PROMPT_INFO =								2;
	protected static final short	PRESENTATION_PROMPT_NOTIFICATION =						3;
	protected static final short	PRESENTATION_PROMPT_WARNING =							4;
	protected static final short	PRESENTATION_PROMPT_QUERY =								5;
//	protected static final short	PRESENTATION_PROMPT_ENTER =								6;

	protected static final String	PRESENTATION_PROMPT_READ_STRING =						"> ";
	protected static final String	PRESENTATION_PROMPT_WRITE_STRING =						"< ";
	protected static final String	PRESENTATION_PROMPT_INFO_STRING =						"- ";
	protected static final String	PRESENTATION_PROMPT_NOTIFICATION_STRING =				"* ";
	protected static final String	PRESENTATION_PROMPT_WARNING_STRING =					"! ";
	protected static final String	PRESENTATION_PROMPT_QUERY_STRING =						"? ";
	protected static final String	PRESENTATION_PROMPT_ENTER_STRING =						": ";

	protected static final String	PRESENTATION_SKIP_COMMENT_STRING =						"#C++";
	protected static final String	PRESENTATION_STRIP_COMMENT_STRING =						"#Java";

	// String constants
	protected static final String	BACK_SLASH_STRING =										"\\";
	protected static final String	DOUBLE_COLON_STRING =									":";
	protected static final String	EMPTY_STRING =											"";
	protected static final String	SLASH_STRING =											"/";
	protected static final String	SPACE_STRING =											" ";
	protected static final String	CARRIAGE_RETURN_NEW_LINE_STRING =						"\r\n";
	protected static final String	NEW_LINE_STRING =										"\n";
	protected static final String	COLON_NEW_LINE_STRING =									".\n";

	// Symbol constants
	protected static final char		SYMBOL_COMMA =											',';
	protected static final char		SYMBOL_COLON =											'.';
	protected static final char		SYMBOL_SEMI_COLON =										';';
	protected static final char		SYMBOL_DOUBLE_COLON =									':';
	protected static final char		SYMBOL_QUESTION_MARK =									'?';
	protected static final char		SYMBOL_EXCLAMATION_MARK =								'!';
	protected static final char		SYMBOL_PIPE =											'|';
	protected static final char		SYMBOL_AMPERSAND =										'&';
	protected static final char		SYMBOL_HASH =											'#';
	protected static final char		SYMBOL_SWUNG_DASH =										'~';
	protected static final char		SYMBOL_AT_SIGN =										'@';
	protected static final char		SYMBOL_ASTERISK =										'*';
	protected static final char		SYMBOL_PERCENT =										'%';
	protected static final char		SYMBOL_DOLLAR =											'$';
	protected static final char		SYMBOL_SLASH =											'/';
	protected static final char		SYMBOL_BACK_SLASH =										'\\';
	protected static final char		SYMBOL_QUOTE =											'\'';
	protected static final char		SYMBOL_BACK_QUOTE =										'`';
	protected static final char		SYMBOL_DOUBLE_QUOTE =									'"';
	protected static final char		SYMBOL_OPEN_ROUNDED_BRACKET =							'(';
	protected static final char		SYMBOL_CLOSE_ROUNDED_BRACKET =							')';
	protected static final char		SYMBOL_OPEN_CURVED_BRACKET =							'{';
	protected static final char		SYMBOL_CLOSE_CURVED_BRACKET =							'}';
	protected static final char		SYMBOL_OPEN_HOOKED_BRACKET =							'<';
	protected static final char		SYMBOL_CLOSE_HOOKED_BRACKET =							'>';
	protected static final char		SYMBOL_OPEN_SQUARE_BRACKET =							'[';
	protected static final char		SYMBOL_CLOSE_SQUARE_BRACKET =							']';

	// Grammar constants
	protected static final char		GRAMMAR_CHOICE_START =									SYMBOL_OPEN_CURVED_BRACKET;
	protected static final char		GRAMMAR_CHOICE_END =									SYMBOL_CLOSE_CURVED_BRACKET;
	protected static final char		GRAMMAR_OPTION_START =									SYMBOL_OPEN_SQUARE_BRACKET;
	protected static final char		GRAMMAR_OPTION_END =									SYMBOL_CLOSE_SQUARE_BRACKET;
	protected static final char		GRAMMAR_OPTION_READ_NOT_WRITE_START =					SYMBOL_OPEN_ROUNDED_BRACKET;
	protected static final char		GRAMMAR_OPTION_READ_NOT_WRITE_END =						SYMBOL_CLOSE_ROUNDED_BRACKET;

	// Query constants
	protected static final char		COMMENT_CHAR =											SYMBOL_HASH;
	protected static final char		GRAMMAR_WORD_DEFINITION_CHAR =							SYMBOL_DOLLAR;

	protected static final char		QUERY_CHAR =											SYMBOL_SWUNG_DASH;
	protected static final char		QUERY_COUNT_CHAR =										SYMBOL_HASH;
	protected static final char		QUERY_NO_LIST_CHAR =									SYMBOL_QUESTION_MARK;
	protected static final char		QUERY_WORD_TYPE_CHAR =									SYMBOL_AT_SIGN;
	protected static final char		QUERY_PARAMETER_CHAR =									SYMBOL_DOUBLE_COLON;
	protected static final char		QUERY_ACTIVE_CHAR =										SYMBOL_EXCLAMATION_MARK;
	protected static final char		QUERY_DEACTIVE_CHAR =									SYMBOL_ASTERISK;
	protected static final char		QUERY_ARCHIVE_CHAR =									SYMBOL_DOLLAR;
	protected static final char		QUERY_DELETED_CHAR =									SYMBOL_AMPERSAND;
	protected static final char		QUERY_SEPARATOR_CHAR =									SYMBOL_COMMA;
	protected static final char		QUERY_ITEM_START_CHAR =									SYMBOL_OPEN_ROUNDED_BRACKET;
	protected static final char		QUERY_ITEM_END_CHAR =									SYMBOL_CLOSE_ROUNDED_BRACKET;
	protected static final char		QUERY_REF_ITEM_START_CHAR =								SYMBOL_OPEN_SQUARE_BRACKET;
	protected static final char		QUERY_REF_ITEM_END_CHAR =								SYMBOL_CLOSE_SQUARE_BRACKET;
	protected static final char		QUERY_DEACTIVE_ITEM_START_CHAR =						SYMBOL_OPEN_CURVED_BRACKET;
	protected static final char		QUERY_DEACTIVE_ITEM_END_CHAR =							SYMBOL_CLOSE_CURVED_BRACKET;
	protected static final char		QUERY_LIST_START_CHAR =									SYMBOL_OPEN_HOOKED_BRACKET;
	protected static final char		QUERY_LIST_END_CHAR =									SYMBOL_CLOSE_HOOKED_BRACKET;
	protected static final char		QUERY_WORD_START_CHAR =									SYMBOL_QUOTE;
	protected static final char		QUERY_WORD_END_CHAR =									SYMBOL_QUOTE;
	protected static final char		QUERY_WORD_REFERENCE_START_CHAR =						SYMBOL_BACK_QUOTE;
	protected static final char		QUERY_WORD_REFERENCE_END_CHAR =							SYMBOL_BACK_QUOTE;
	protected static final char		QUERY_STRING_START_CHAR =								SYMBOL_DOUBLE_QUOTE;
	protected static final char		QUERY_STRING_END_CHAR =									SYMBOL_DOUBLE_QUOTE;

	protected static final char		TEXT_DIACRITICAL_CHAR =									SYMBOL_BACK_SLASH;
	protected static final char		TEXT_BELL_CHAR =										'a';
	protected static final char		TEXT_BACK_SPACE_CHAR =									'b';
	protected static final char		TEXT_NEW_LINE_CHAR =									'n';
	protected static final char		TEXT_TAB_CHAR =											't';

	protected static final String	QUERY_ITEM_START_STRING =								"(";
	protected static final String	QUERY_LIST_START_STRING =								"<";
	protected static final String	QUERY_SEPARATOR_SPACE_STRING =							", ";
	protected static final String	QUERY_WORD_TYPE_STRING =								EMPTY_STRING +	QUERY_WORD_TYPE_CHAR;
	protected static final String	QUERY_SEPARATOR_STRING =								",";

	// Result constants
	protected static final byte		RESULT_OK =												1;
	protected static final byte		RESULT_ERROR =											0;
	protected static final byte		RESULT_SYSTEM_ERROR =									-1;

	// Screen constants
	protected static final short	TAB_LENGTH =											8;


	// Administrator initialization constants
	protected static final short	ADMIN_FILE_LIST =										0;
	protected static final short	ADMIN_READ_LIST =										1;
	protected static final short	ADMIN_SCORE_LIST =										2;
	protected static final short	ADMIN_WORD_LIST =										3;
	protected static final short	ADMIN_CONDITION_LIST =									4;		// SelectionItem
	protected static final short	ADMIN_ACTION_LIST =										5;		// SelectionItem
	protected static final short	ADMIN_ALTERNATIVE_LIST =								6;		// SelectionItem
	protected static final short	NUMBER_OF_ADMIN_LISTS =									7;

	protected static final short	AdminLists[] = {	ADMIN_FILE_LIST,
														ADMIN_READ_LIST,
														ADMIN_SCORE_LIST,
														ADMIN_WORD_LIST,
														ADMIN_CONDITION_LIST,						// SelectionItem
														ADMIN_ACTION_LIST,							// SelectionItem
														ADMIN_ALTERNATIVE_LIST };					// SelectionItem

	protected static final char		ADMIN_FILE_LIST_SYMBOL =								'F';
	protected static final char		ADMIN_READ_LIST_SYMBOL =								'R';
	protected static final char		ADMIN_SCORE_LIST_SYMBOL =								'S';
	protected static final char		ADMIN_WORD_LIST_SYMBOL =								'W';
	protected static final char		ADMIN_CONDITION_LIST_SYMBOL =							'X';	// SelectionItem
	protected static final char		ADMIN_ACTION_LIST_SYMBOL =								'Y';	// SelectionItem
	protected static final char		ADMIN_ALTERNATIVE_LIST_SYMBOL =							'Z';	// SelectionItem

	// Word initialization constants
	protected static final short	WORD_ASSIGNMENT_LIST =									0;		// SpecificationItem
	protected static final short	WORD_COLLECTION_LIST =									1;
	protected static final short	WORD_GENERALIZATION_LIST =								2;
	protected static final short	WORD_INTERFACE_LANGUAGE_LIST =							3;
	protected static final short	WORD_JUSTIFICATION_LIST =								4;
	protected static final short	WORD_GRAMMAR_LANGUAGE_LIST =							5;
	protected static final short	WORD_WRITE_LIST =										6;
	protected static final short	WORD_SPECIFICATION_LIST =								7;
	protected static final short	WORD_SOLVE_LIST =										8;
	protected static final short	WORD_TYPE_LIST =										9;
	protected static final short	WORD_CONTEXT_LIST = 									10;
	protected static final short	NUMBER_OF_WORD_LISTS =									11;

	protected static final short	WordLists[] = {	WORD_ASSIGNMENT_LIST,							// SpecificationItem
													WORD_COLLECTION_LIST,
													WORD_GENERALIZATION_LIST,
													WORD_INTERFACE_LANGUAGE_LIST,
													WORD_JUSTIFICATION_LIST,
													WORD_GRAMMAR_LANGUAGE_LIST,
													WORD_WRITE_LIST,
													WORD_SPECIFICATION_LIST,
													WORD_SOLVE_LIST,
													WORD_TYPE_LIST,
													WORD_CONTEXT_LIST };

	protected static final char		WORD_ASSIGNMENT_LIST_SYMBOL =							'a';	// SpecificationItem
	protected static final char		WORD_COLLECTION_LIST_SYMBOL =							'c';
	protected static final char		WORD_GENERALIZATION_LIST_SYMBOL =						'g';
	protected static final char		WORD_INTERFACE_LANGUAGE_LIST_SYMBOL =					'i';
	protected static final char		WORD_JUSTIFICATION_LIST_SYMBOL =						'j';
	protected static final char		WORD_GRAMMAR_LANGUAGE_LIST_SYMBOL =						'l';
	protected static final char		WORD_WRITE_LIST_SYMBOL =								'r';
	protected static final char		WORD_SPECIFICATION_LIST_SYMBOL =						's';
	protected static final char		WORD_SOLVE_LIST_SYMBOL =								'v';
	protected static final char		WORD_TYPE_LIST_SYMBOL =									'w';
	protected static final char		WORD_CONTEXT_LIST_SYMBOL =								'x';

	// Other initialization constants
	protected static final short	MAX_GRAMMAR_LEVEL =										50;		// Depth of parsing grammar calls
	protected static final short	MAX_ASSUMPTION_LEVEL_RECALCULATIONS =					10;		// Number of repeated calculations
	protected static final short	MAX_SELECTION_EXECUTIONS =								1000;	// Number of repeated selections

	protected static final short	NUMBER_OF_VOWELS = 										5;

	protected static final char		VOWEL[] = {	'a',
												'e',
												'i',
												'o',
												'u' };


	// Justification types
	protected static final short	JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_GENERALIZATION =					0;
	protected static final short	JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_SPECIFICATION =						1;
	protected static final short	JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION =		2;
	protected static final short	JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION =		3;
	protected static final short	JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION =							4;
	protected static final short	JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION =				5;
	protected static final short	JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION =									7;

	protected static final short	JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION =			9;
	protected static final short	JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION =							10;
	protected static final short	JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION =								11;

	protected static final short	JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION =							12;


	//	Word types
	protected static final short	WORD_TYPE_UNDEFINED =									0;
	protected static final short	WORD_TYPE_SYMBOL =										1;
	protected static final short	WORD_TYPE_NUMERAL =										2;
	protected static final short	WORD_TYPE_LETTER_SMALL =								3;
	protected static final short	WORD_TYPE_LETTER_CAPITAL =								4;
	protected static final short	WORD_TYPE_PROPER_NAME =									5;
	protected static final short	WORD_TYPE_ADJECTIVE =									6;
	protected static final short	WORD_TYPE_ADVERB =										7;
	protected static final short	WORD_TYPE_ANSWER =										8;
	protected static final short	WORD_TYPE_ARTICLE =										9;
	protected static final short	WORD_TYPE_CONJUNCTION =									10;
	protected static final short	WORD_TYPE_NOUN_SINGULAR =								11;
	protected static final short	WORD_TYPE_NOUN_PLURAL =									12;
	protected static final short	WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_1 =					13;
	protected static final short	WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_2 =					14;
	protected static final short	WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_1 =				15;
	protected static final short	WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_2 =				16;
	protected static final short	WORD_TYPE_PERSONAL_PRONOUN_PLURAL_1 =					17;
	protected static final short	WORD_TYPE_PERSONAL_PRONOUN_PLURAL_2 =					18;
	protected static final short	WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_1 =					19;
	protected static final short	WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_2 =					20;
	protected static final short	WORD_TYPE_PREPOSITION =									21;
	protected static final short	WORD_TYPE_VERB_SINGULAR =								22;
	protected static final short	WORD_TYPE_VERB_PLURAL =									23;
	protected static final short	WORD_TYPE_SELECTION_WORD =								24;
	protected static final short	WORD_TYPE_TEXT =										25;
	protected static final short	NUMBER_OF_WORD_TYPES =									26;

	protected static final short	WordTypes[] = {	WORD_TYPE_UNDEFINED,
													WORD_TYPE_SYMBOL,
													WORD_TYPE_NUMERAL,
													WORD_TYPE_LETTER_SMALL,
													WORD_TYPE_LETTER_CAPITAL,
													WORD_TYPE_PROPER_NAME,
													WORD_TYPE_ADJECTIVE,
													WORD_TYPE_ADVERB,
													WORD_TYPE_ANSWER,
													WORD_TYPE_ARTICLE,
													WORD_TYPE_CONJUNCTION,
													WORD_TYPE_NOUN_SINGULAR,
													WORD_TYPE_NOUN_PLURAL,
													WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_1,
													WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_2,
													WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_1,
													WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_2,
													WORD_TYPE_PERSONAL_PRONOUN_PLURAL_1,
													WORD_TYPE_PERSONAL_PRONOUN_PLURAL_2,
													WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_1,
													WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_2,
													WORD_TYPE_PREPOSITION,
													WORD_TYPE_VERB_SINGULAR,
													WORD_TYPE_VERB_PLURAL,
													WORD_TYPE_SELECTION_WORD,
													WORD_TYPE_TEXT };

	//	Word Parameters

	//	Symbols
	protected static final short	WORD_PARAMETER_SYMBOL_COMMA =							100;
	protected static final short	WORD_PARAMETER_SYMBOL_COLON =							101;
	protected static final short	WORD_PARAMETER_SYMBOL_EXCLAMATION_MARK =				102;
	protected static final short	WORD_PARAMETER_SYMBOL_QUESTION_MARK =					103;

	//	Numerals
	protected static final short	WORD_PARAMETER_NUMERAL_BOTH =							200;

	//	Small letters

	//	Capital letters

	//	Proper-names

	//	Adjectives
	protected static final short	WORD_PARAMETER_ADJECTIVE_ASSIGNED =						600;
	protected static final short	WORD_PARAMETER_ADJECTIVE_BUSY =							601;
	protected static final short	WORD_PARAMETER_ADJECTIVE_CLEAR =						602;
	protected static final short	WORD_PARAMETER_ADJECTIVE_DONE =							603;
	protected static final short	WORD_PARAMETER_ADJECTIVE_DEFENSIVE =					604;
	protected static final short	WORD_PARAMETER_ADJECTIVE_EXCLUSIVE =					605;
	protected static final short	WORD_PARAMETER_ADJECTIVE_INVERTED =						606;
	protected static final short	WORD_PARAMETER_ADJECTIVE_CALLED_OR_NAMED =				607;
	protected static final short	WORD_PARAMETER_ADJECTIVE_NEW =							608;
	protected static final short	WORD_PARAMETER_ADJECTIVE_NO =							609;
	protected static final short	WORD_PARAMETER_ADJECTIVE_PREVIOUS =						610;
	protected static final short	WORD_PARAMETER_ADJECTIVE_CURRENT =						611;
//	protected static final short	WORD_PARAMETER_ADJECTIVE_NEXT =							612;
	protected static final short	WORD_PARAMETER_ADJECTIVE_ODD =							613;
	protected static final short	WORD_PARAMETER_ADJECTIVE_EVEN =							614;
	protected static final short	WORD_PARAMETER_ADJECTIVE_LESS =							615;
	protected static final short	WORD_PARAMETER_ADJECTIVE_EARLIER =						616;
	protected static final short	WORD_PARAMETER_ADJECTIVE_SMALLER =						617;
	protected static final short	WORD_PARAMETER_ADJECTIVE_EQUAL =						618;
	protected static final short	WORD_PARAMETER_ADJECTIVE_SAME =							619;
	protected static final short	WORD_PARAMETER_ADJECTIVE_MORE =							620;
	protected static final short	WORD_PARAMETER_ADJECTIVE_BIGGER =						621;
	protected static final short	WORD_PARAMETER_ADJECTIVE_LARGER =						622;
	protected static final short	WORD_PARAMETER_ADJECTIVE_LATER =						623;

	//	Adverbs
	protected static final short	WORD_PARAMETER_ADVERB_AS =								700;
	protected static final short	WORD_PARAMETER_ADVERB_ASSUMPTION_MAY_BE =				701;
	protected static final short	WORD_PARAMETER_ADVERB_ASSUMPTION_POSSIBLY =				702;
	protected static final short	WORD_PARAMETER_ADVERB_ASSUMPTION_PROBABLY =				703;
//	protected static final short	WORD_PARAMETER_ADVERB_DO_NOT =							704;
	protected static final short	WORD_PARAMETER_ADVERB_INFO =							705;
	protected static final short	WORD_PARAMETER_ADVERB_NOT =								706;
//	protected static final short	WORD_PARAMETER_ADVERB_PREVIOUSLY =						707;
//	protected static final short	WORD_PARAMETER_ADVERB_CURRENTLY =						708;

	// Answers
	protected static final short	WORD_PARAMETER_ANSWER_YES =								800;
	protected static final short	WORD_PARAMETER_ANSWER_NO =								801;

	//	Articles
	protected static final short	WORD_PARAMETER_ARTICLE_INDEFINITE =						900;
	protected static final short	WORD_PARAMETER_ARTICLE_DEFINITE_NEUTER =				901;
	protected static final short	WORD_PARAMETER_ARTICLE_DEFINITE_MALE_FEMALE =			902;

	//	Conjunctions
	protected static final short	WORD_PARAMETER_CONJUNCTION_AND =						1000;
	protected static final short	WORD_PARAMETER_CONJUNCTION_OR =							1001;
	protected static final short	WORD_PARAMETER_CONJUNCTION_THAN =						1002;

	//	Singular / plural nouns
	protected static final short	WORD_PARAMETER_NOUN_DEVELOPER =							1100;
	protected static final short	WORD_PARAMETER_NOUN_FILE =								1101;
	protected static final short	WORD_PARAMETER_NOUN_GRAMMAR_LANGUAGE =					1102;
	protected static final short	WORD_PARAMETER_NOUN_INTERFACE_LANGUAGE =				1103;
	protected static final short	WORD_PARAMETER_NOUN_JUSTIFICATION_REPORT =				1104;
	protected static final short	WORD_PARAMETER_NOUN_HEAD =								1105;
	protected static final short	WORD_PARAMETER_NOUN_TAIL =								1106;
	protected static final short	WORD_PARAMETER_NOUN_MIND =								1107;
	protected static final short	WORD_PARAMETER_NOUN_NUMBER =							1108;
	protected static final short	WORD_PARAMETER_NOUN_PASSWORD =							1109;
	protected static final short	WORD_PARAMETER_NOUN_SOLVE_LEVEL =						1110;
	protected static final short	WORD_PARAMETER_NOUN_SOLVE_METHOD =						1111;
	protected static final short	WORD_PARAMETER_NOUN_SOLVE_STRATEGY =					1112;
	protected static final short	WORD_PARAMETER_NOUN_TEST_FILE =							1113;
	protected static final short	WORD_PARAMETER_NOUN_USER =								1114;
	protected static final short	WORD_PARAMETER_NOUN_VALUE =								1115;

	// Singular pronouns
	protected static final short	WORD_PARAMETER_SINGULAR_PRONOUN_I_ME_MY_MINE =			1300;
	protected static final short	WORD_PARAMETER_SINGULAR_PRONOUN_YOU_YOU_YOUR_YOURS =	1301;
	protected static final short	WORD_PARAMETER_SINGULAR_PRONOUN_HE_HIM_HIS_HIS =		1302;
	protected static final short	WORD_PARAMETER_SINGULAR_PRONOUN_SHE_HER_HER_HERS =		1303;
	protected static final short	WORD_PARAMETER_SINGULAR_PRONOUN_IT_ITS_ITS_ITS =		1304;

	// Plural pronouns
	protected static final short	WORD_PARAMETER_PLURAL_PRONOUN_WE_US_OUR_OURS =			1700;
	protected static final short	WORD_PARAMETER_PLURAL_PRONOUN_YOU_YOU_YOUR_YOURS =		1701;
	protected static final short	WORD_PARAMETER_PLURAL_PRONOUN_THEY_THEM_THEIR_THEIRS =	1702;

	//	Prepositions
	protected static final short	WORD_PARAMETER_PREPOSITION_ABOUT =						2100;
	protected static final short	WORD_PARAMETER_PREPOSITION_FOR =						2101;
	protected static final short	WORD_PARAMETER_PREPOSITION_FROM =						2102;
	protected static final short	WORD_PARAMETER_PREPOSITION_IN =							2103;
	protected static final short	WORD_PARAMETER_PREPOSITION_OF =							2104;
	protected static final short	WORD_PARAMETER_PREPOSITION_TO =							2105;

	//	Singular verbs
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IS =						2200;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_WAS =						2201;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_CAN_BE =					2202;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_HAS =						2203;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_HAD =						2204;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_CAN_HAVE =					2205;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_ADD =			2206;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_MOVE =			2207;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_REMOVE =		2208;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_CLEAR =			2209;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_HELP =			2210;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_LOGIN =			2211;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_READ =			2212;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_REDO =			2213;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_RESTART =		2214;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_SHOW =			2215;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_SOLVE =			2216;
	protected static final short	WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_UNDO =			2217;

	//	Plural verbs
	protected static final short	WORD_PARAMETER_PLURAL_VERB_ARE =						2300;
	protected static final short	WORD_PARAMETER_PLURAL_VERB_WERE =						2301;
	protected static final short	WORD_PARAMETER_PLURAL_VERB_CAN_BE =						2302;
	protected static final short	WORD_PARAMETER_PLURAL_VERB_HAVE =						2303;
	protected static final short	WORD_PARAMETER_PLURAL_VERB_HAD =						2304;
	protected static final short	WORD_PARAMETER_PLURAL_VERB_CAN_HAVE =					2305;

	//	Selection words
	protected static final short	WORD_PARAMETER_SELECTION_IF =							2400;
	protected static final short	WORD_PARAMETER_SELECTION_THEN =							2401;
	protected static final short	WORD_PARAMETER_SELECTION_ELSE =							2402;

	//	Text

	//	Plural noun endings
	protected static final short	WORD_PLURAL_NOUN_ENDINGS =								2600;


	//	Grammar parameters
	protected static final short	GRAMMAR_SENTENCE =										10000;
	protected static final short	GRAMMAR_SELECTION =										11000;
	protected static final short	GRAMMAR_IMPERATIVE =									11100;
	protected static final short	GRAMMAR_ANSWER =										11200;
	protected static final short	GRAMMAR_GENERALIZATION_SPECIFICATION =					12000;
	protected static final short	GRAMMAR_GENERALIZATION_PART =							12010;
	protected static final short	GRAMMAR_GENERALIZATION_WORD =							12011;
	protected static final short	GRAMMAR_LINKED_GENERALIZATION_CONJUNCTION =				12012;
	protected static final short	GRAMMAR_SPECIFICATION_PART =							12020;
	protected static final short	GRAMMAR_ASSIGNMENT_PART =								12021;
	protected static final short	GRAMMAR_SPECIFICATION_WORD =							12022;
	protected static final short	GRAMMAR_EXCLUSIVE_SPECIFICATION_CONJUNCTION =			12023;
	protected static final short	GRAMMAR_RELATION_PART =									12030;
	protected static final short	GRAMMAR_RELATION_WORD =									12031;
	protected static final short	GRAMMAR_TEXT =											12040;
	protected static final short	GRAMMAR_GENERALIZATION_ASSIGNMENT =						12100;
	protected static final short	GRAMMAR_SPECIFICATION_ASSIGNMENT =						12101;
	protected static final short	GRAMMAR_RELATION_ASSIGNMENT =							12102;
	protected static final short	GRAMMAR_SENTENCE_CONJUNCTION =							12110;
	protected static final short	GRAMMAR_VERB =											12120;
	protected static final short	GRAMMAR_QUESTION_VERB =									12121;
	protected static final short	GRAMMAR_GENERALIZATION_VERB =							12122;
	protected static final short	GRAMMAR_GENERALIZATION_QUESTION_VERB =					12123;


	// INTERFACE REFERENCE CONSTANTS

	protected static final String	INTERFACE_STRING_NOT_AVAILABLE =											"<interface string not available>";
	protected static final String	NO_INTERFACE_LANGUAGE_AVAILABLE =											"<no interface language available>";

	// Console (common)
	protected static final short	INTERFACE_CONSOLE_NEW_RELEASE_AVAILABLE =									100;
	protected static final short	INTERFACE_CONSOLE_START_MESSAGE =											102;
	protected static final short	INTERFACE_CONSOLE_WAITING_FOR_INPUT =										103;
	protected static final short	INTERFACE_CONSOLE_WAITING_FOR_SECURE_INPUT =								104;
	protected static final short	INTERFACE_CONSOLE_LOGGED_IN_START =											106;
	protected static final short	INTERFACE_CONSOLE_LOGGED_IN_END =											107;
	protected static final short	INTERFACE_CONSOLE_ALREADY_LOGGED_IN_START =									108;
	protected static final short	INTERFACE_CONSOLE_ALREADY_LOGGED_IN_END =									109;
	protected static final short	INTERFACE_CONSOLE_LOGIN_FAILED =											110;

	// Console (status and progress)
	protected static final short	INTERFACE_CONSOLE_I_AM_EXECUTING_SELECTIONS_START =							200;
	protected static final short	INTERFACE_CONSOLE_I_AM_EXECUTING_SELECTIONS_END =							201;
//	protected static final short	INTERFACE_CONSOLE_I_AM_CLEANING_UP_DELETED_ITEMS =							202;

	// Console (upper menu)
	protected static final short	INTERFACE_CONSOLE_CLEAR_YOUR_MIND =											300;
	protected static final short	INTERFACE_CONSOLE_RESTART =													301;
	protected static final short	INTERFACE_CONSOLE_UNDO =													302;
	protected static final short	INTERFACE_CONSOLE_REDO =													303;
	protected static final short	INTERFACE_CONSOLE_MORE_EXAMPLES =											307;
	protected static final short	INTERFACE_CONSOLE_READ_FILE_ACTION_START =									308;
	protected static final short	INTERFACE_CONSOLE_READ_FILE_ACTION_END =									309;

	// Console (main menu)
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_AMBIGUITY_BOSTON =							310;
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_AMBIGUITY_PRESIDENTS =						311;
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_CONNECT4 =						312;
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_GREETING =						313;
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_TOWER_OF_HANOI =				314;
	protected static final short	INTERFACE_CONSOLE_READ_THE_FILE_REASONING_FAMILY =							315;
	protected static final short	INTERFACE_CONSOLE_AMBIGUITY_SUBMENU =										317;
	protected static final short	INTERFACE_CONSOLE_PROGRAMMING_SUBMENU =										318;
	protected static final short	INTERFACE_CONSOLE_REASONING_SUBMENU =										319;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SUBMENU =											320;
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_SUBMENU =									321;
	protected static final short	INTERFACE_CONSOLE_FAMILY_JUSTIFICATION_REPORT_SUBMENU =						322;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_SUBMENU =									323;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_SUBMENU =								324;

	protected static final short	INTERFACE_CONSOLE_BACK =													325;
	protected static final short	INTERFACE_CONSOLE_HELP =													326;

	// Console (initial sub-menu)
	protected static final short	INTERFACE_CONSOLE_INITIAL_REMARK_LABEL1 =									330;
	protected static final short	INTERFACE_CONSOLE_INITIAL_REMARK_LABEL2 =									331;
	protected static final short	INTERFACE_CONSOLE_INITIAL_REMARK_LABEL3 =									332;
	protected static final short	INTERFACE_CONSOLE_INITIAL_REMARK_LABEL4 =									333;
	protected static final short	INTERFACE_CONSOLE_INITIAL_REMARK_LABEL5 =									334;

	// Console (programming/connect4 sub-menu)
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_A =									400;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_B =									401;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_C =									402;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_D =									403;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_E =									404;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_F =									405;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_G =									406;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_SHOW_INFO_ABOUT_THE_SET =						407;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_THE_SOLVE_LEVEL_IS_LOW =							408;
	protected static final short	INTERFACE_CONSOLE_CONNECT4_THE_SOLVE_LEVEL_IS_HIGH =						409;

	// Console (programming/tower of Hanoi sub-menu)
	protected static final short	INTERFACE_CONSOLE_TOWER_OF_HANOI_EVEN_NUMBER_OF_DISCS =						410;
	protected static final short	INTERFACE_CONSOLE_TOWER_OF_HANOI_ODD_NUMBER_OF_DISCS =						411;

	// Console (reasoning/family sub-menu)
	protected static final short	INTERFACE_CONSOLE_FAMILY_JOHN_ANN =											500;
	protected static final short	INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_SON =									501;
	protected static final short	INTERFACE_CONSOLE_FAMILY_JOE_IS_A_SON =										502;
	protected static final short	INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_DAUGHTER =								503;

	protected static final short	INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_SON_OF_JOHN_AND_ANN =					504;
	protected static final short	INTERFACE_CONSOLE_FAMILY_JOE_IS_A_SON_OF_JOHN_AND_ANN =						505;
	protected static final short	INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_DAUGHTER_OF_JOHN_AND_ANN =				506;

	protected static final short	INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_FATHER =									507;
	protected static final short	INTERFACE_CONSOLE_FAMILY_ANN_IS_A_MOTHER =									508;

	protected static final short	INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_PARENT =									509;
	protected static final short	INTERFACE_CONSOLE_FAMILY_ANN_IS_A_PARENT =									510;

	protected static final short	INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_MAN =									511;
	protected static final short	INTERFACE_CONSOLE_FAMILY_ANN_IS_A_WOMAN =									512;
	protected static final short	INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_MAN =									513;
	protected static final short	INTERFACE_CONSOLE_FAMILY_JOE_IS_A_MAN =										514;
	protected static final short	INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_WOMAN =									515;

	// Console (reasoning/family info sub-menu)
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_JOHN =								520;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_ANN =								521;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_PAUL =								522;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_JOE =								523;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_LAURA =							524;

	// Console (reasoning/family conflict sub-menu)
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_JOHN_IS_A_WOMAN =							530;
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_JOHN_IS_THE_MOTHER_PETE =					531;
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_ANN_IS_A_SON =							532;
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_PAUL_IS_A_DAUGHTER =						533;
	protected static final short	INTERFACE_CONSOLE_FAMILY_CONFLICT_JOE_IS_A_MOTHER =							534;

	// Console (reasoning/family justification sub-menu)
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_PARENTS =		540;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_CHILDREN =		541;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_JOHN =			542;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_ANN =			543;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_PAUL =			544;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_JOE =			545;
	protected static final short	INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_LAURA =			546;

	// Console (reasoning/family question sub-menu)
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_A_FATHER =						550;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_A_MOTHER =						551;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_THE_FATHER_OF_PAUL =				552;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_THE_MOTHER_OF_PAUL =				553;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_MAN =							554;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_WOMAN =							555;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_MAN_OR_A_WOMAN =				556;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_SON =							557;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_DAUGHTER =						558;
	protected static final short	INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_SON_OR_A_DAUGHTER =				559;

	// Console (help sub-menu)
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_INFO_ABOUT_THE_LISTS =							602;
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_INFO_ABOUT_THE_WORD_TYPES =						604;
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_THE_QUERY_COMMANDS =							605;
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_THE_COPYRIGHT =									606;
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_THE_GPLv2_LICENSE =								607;
	protected static final short	INTERFACE_CONSOLE_HELP_SHOW_THE_WARRANTY =									608;

	// Grammar
	protected static final short	INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_START =							1000;
	protected static final short	INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_MIDDLE =							1001;
	protected static final short	INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_END =								1002;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_START =						1003;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_MIDDLE =						1004;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_START =						1005;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_TO =						1006;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_MIDDLE =					1007;
	protected static final short	INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_END =						1008;
	protected static final short	INTERFACE_GRAMMAR_UNKNOWN_PLURAL_START =									1009;
	protected static final short	INTERFACE_GRAMMAR_UNKNOWN_PLURAL_ENDING =									1010;

	// Query
	protected static final short	INTERFACE_QUERY_NO_ITEM_WAS_FOUND =											1100;
	protected static final short	INTERFACE_QUERY_NO_REFERENCE_ITEM_WAS_FOUND =								1101;
	protected static final short	INTERFACE_QUERY_INVALID_CHARACTER_IN_LIST =									1102;
	protected static final short	INTERFACE_QUERY_NO_LIST_WAS_FOUND =											1103;
	protected static final short	INTERFACE_QUERY_NO_WORD_WAS_FOUND =											1104;
	protected static final short	INTERFACE_QUERY_NO_WORD_REFERENCE_WAS_FOUND =								1105;
	protected static final short	INTERFACE_QUERY_NO_STRING_WAS_FOUND =										1106;
	protected static final short	INTERFACE_QUERY_NO_WORD_TYPE_WAS_FOUND =									1107;
	protected static final short	INTERFACE_QUERY_NO_PARAMETER_WAS_FOUND =									1108;

	protected static final short	INTERFACE_QUERY_NO_ITEMS_WERE_FOUND =										1109;
	protected static final short	INTERFACE_QUERY_NO_WORDS_WERE_FOUND =										1110;
	protected static final short	INTERFACE_QUERY_NO_WORD_REFERENCES_WERE_FOUND =								1111;
	protected static final short	INTERFACE_QUERY_NO_STRINGS_WERE_FOUND =										1112;

	protected static final short	INTERFACE_QUERY_EMPTY_WORD_SPECIFICATION =									1113;
	protected static final short	INTERFACE_QUERY_EMPTY_WORD_REFERENCE =										1114;
	protected static final short	INTERFACE_QUERY_EMPTY_STRING_SPECIFICATION =								1115;

	protected static final short	INTERFACE_QUERY_ERROR =														1199;

	// Imperative (notifications)
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_MY_MIND_IS_CLEAR =						1200;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_UNDO_SENTENCE_OF_ANOTHER_USER =			1201;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_NO_SENTENCES_LEFT_TO_UNDO =				1202;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_NO_SENTENCES_TO_REDO =					1203;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_I_HAVE_UNDONE_SENTENCE_NR =				1204;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_I_HAVE_REDONE_SENTENCE_NR =				1205;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_SENTENCE_NR_END =							1206;
	protected static final short	INTERFACE_IMPERATIVE_NOTIFICATION_I_DONT_HAVE_A =							1207;

	// Imperative (warnings)
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_HAVE_ANY_INFO_ABOUT_START =				1210;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_HAVE_ANY_INFO_ABOUT_END =				1211;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_START =			1212;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_END =			1213;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_TO_DO_WITH_RELATION =				1214;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHICH_FILE_TO_READ =				1215;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_COULD_FIND_SOLVE_INFO_START =				1216;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_I_COULD_FIND_SOLVE_INFO_END =					1217;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_NEEDS_A_LIST_TO_BE_SPECIFIED =					1218;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_PREPOSITION_NOT_USED_FOR_THIS_ACTION =			1219;

	protected static final short	INTERFACE_IMPERATIVE_WARNING_WORD_ALREADY_SOLVED_START =					1220;
	protected static final short	INTERFACE_IMPERATIVE_WARNING_WORD_ALREADY_SOLVED_END =						1221;

	// Listing (conflicts)
	protected static final short	INTERFACE_SENTENCE_IN_CONFLICT_WITH_ITSELF =								1300;
	protected static final short	INTERFACE_QUESTION_IN_CONFLICT_WITH_ITSELF =								1301;
	protected static final short	INTERFACE_LISTING_CONFLICT =												1302;

	// Listing (justification)
	protected static final short	INTERFACE_LISTING_JUSTIFICATION_BECAUSE =									1310;
	protected static final short	INTERFACE_LISTING_JUSTIFICATION_AND =										1311;
	protected static final short	INTERFACE_LISTING_JUSTIFICATION_SENTENCE_START =							1312;

	// Listing (current information)
	protected static final short	INTERFACE_LISTING_YOUR_INFORMATION =										1320;
	protected static final short	INTERFACE_LISTING_YOUR_QUESTIONS =											1321;
	protected static final short	INTERFACE_LISTING_MY_CONCLUSIONS =											1322;
	protected static final short	INTERFACE_LISTING_MY_ASSUMPTIONS =											1323;
	protected static final short	INTERFACE_LISTING_MY_ASSUMPTIONS_THAT_ARE_ADJUSTED =						1324;
	protected static final short	INTERFACE_LISTING_MY_QUESTIONS =											1325;
	protected static final short	INTERFACE_LISTING_MY_ADJUSTED_QUESTIONS =									1326;
	protected static final short	INTERFACE_LISTING_MY_ANSWER =												1327;
	protected static final short	INTERFACE_LISTING_I_ONLY_KNOW =												1328;
	protected static final short	INTERFACE_LISTING_I_AM_NOT_SURE_I_ASSUME =									1329;
	protected static final short	INTERFACE_LISTING_SPECIFICATIONS =											1330;
	protected static final short	INTERFACE_LISTING_SPECIFICATION_QUESTIONS =									1331;
	protected static final short	INTERFACE_LISTING_RELATED_INFORMATION =										1332;
	protected static final short	INTERFACE_LISTING_RELATED_QUESTIONS =										1333;

	// Listing (old information)
	protected static final short	INTERFACE_LISTING_CONFIRMED_SPECIFICATION_OF_MY_ASSUMPTION =				1340;
	protected static final short	INTERFACE_LISTING_CONFIRMED_SPECIFICATION_OF_MY_CONCLUSION =				1341;
	protected static final short	INTERFACE_LISTING_CONFIRMED_SOME_RELATION_WORDS_OF_MY_ASSUMPTION =			1342;
	protected static final short	INTERFACE_LISTING_CONFIRMED_SOME_RELATION_WORDS_OF_MY_CONCLUSION =			1343;
	protected static final short	INTERFACE_LISTING_MY_ASSUMPTIONS_THAT_ARE_CONFIRMED =						1344;
	protected static final short	INTERFACE_LISTING_MY_CONCLUSIONS_THAT_ARE_CONFIRMED =						1345;
	protected static final short	INTERFACE_LISTING_YOUR_INFO_IS_MORE_SPECIFIC_THAN_MY_ASSUMPTION =			1346;
	protected static final short	INTERFACE_LISTING_YOUR_INFO_IS_MORE_SPECIFIC_THAN_MY_CONCLUSION =			1347;
	protected static final short	INTERFACE_LISTING_YOUR_QUESTION_IS_MORE_SPECIFIC_THAN_MY_QUESTION =			1348;
	protected static final short	INTERFACE_LISTING_THIS_INFO_IS_MORE_SPECIFIC_THAN_YOUR_EARLIER_INFO =		1349;
	protected static final short	INTERFACE_LISTING_THIS_QUESTION_IS_MORE_SPECIFIC_THAN_YOUR_QUESTION =		1350;
	protected static final short	INTERFACE_LISTING_YOUR_QUESTIONS_THAT_ARE_ANSWERED =						1351;
	protected static final short	INTERFACE_LISTING_MY_QUESTIONS_THAT_ARE_ANSWERED =							1352;
	protected static final short	INTERFACE_LISTING_MY_CORRECTED_ASSUMPTIONS_BY_KNOWLEDGE =					1353;
	protected static final short	INTERFACE_LISTING_MY_CORRECTED_ASSUMPTIONS_BY_OPPOSITE_QUESTION =			1354;

	// Question
	protected static final short	INTERFACE_QUESTION_YOU_HAD_THE_SAME_QUESTION_BEFORE =						1360;
	protected static final short	INTERFACE_QUESTION_I_HAD_THE_SAME_QUESTION_BEFORE =							1361;
	protected static final short	INTERFACE_QUESTION_YOU_HAD_A_SIMILAR_QUESTION_BEFORE =						1362;
	protected static final short	INTERFACE_QUESTION_I_HAD_A_SIMILAR_QUESTION_BEFORE =						1363;
	protected static final short	INTERFACE_QUESTION_YOU_HAD_A_RELATED_QUESTION_BEFORE =						1364;
	protected static final short	INTERFACE_QUESTION_I_HAD_A_RELATED_QUESTION_BEFORE =						1365;
	protected static final short	INTERFACE_QUESTION_I_DONT_KNOW_ANYTHING_ABOUT_WORD_START =					1366;
	protected static final short	INTERFACE_QUESTION_I_DONT_KNOW_ANYTHING_ABOUT_WORD_END =					1367;
	protected static final short	INTERFACE_QUESTION_IS_ALREADY_ANSWERED =									1368;
	protected static final short	INTERFACE_QUESTION_I_LL_ANSWER_AS_SOON_AS_I_HAVE_ENOUGH_INFORMATION =		1369;

	// Sentence (notifications)
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_I_KNOW =									1400;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_I_NOTICED_SEMANTIC_AMBIGUITY_START =		1401;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_STATIC_SEMANTIC_AMBIGUITY_END =				1402;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_DYNAMIC_SEMANTIC_AMBIGUITY_END =			1403;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_START =		1404;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_WORD =		1405;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_END =		1406;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_QUESTION_MISSING_RELATION =		1407;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_SENTENCE_MISSING_RELATION =		1408;
	protected static final short	INTERFACE_SENTENCE_NOTIFICATION_MISSING_RELATION_I_ASSUME_YOU_MEAN =		1409;

	// Sentence (warnings)
	protected static final short	INTERFACE_SENTENCE_WARNING_NOT_CONFORM_GRAMMAR =							1410;
	protected static final short	INTERFACE_SENTENCE_WARNING_DONT_UNDERSTAND_FROM_WORD_POSITION_START =		1411;
	protected static final short	INTERFACE_SENTENCE_WARNING_DONT_UNDERSTAND_FROM_WORD_START =				1412;
	protected static final short	INTERFACE_SENTENCE_WARNING_ASSIGNED_IS_SELECTION_CONDITION_START =			1413;
	protected static final short	INTERFACE_SENTENCE_WARNING_AT_POSITION_START =								1414;
	protected static final short	INTERFACE_SENTENCE_WARNING_AT_POSITION_END =								1415;
	protected static final short	INTERFACE_SENTENCE_WARNING_IS_ONLY_USED_IN_CONDITION_OF_SELECTION =			1416;
	protected static final short	INTERFACE_SENTENCE_WARNING_NOT_ABLE_TO_LINK_YES_NO_TO_QUESTION =			1417;

	// Sentence (errors)
	protected static final short	INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_STORE_OR_RETRIEVE =				1420;
	protected static final short	INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_THIS_SENTENCE =					1421;
	protected static final short	INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_SENTENCE =						1422;
	protected static final short	INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_I_RETRIEVED_FROM_MY_SYSTEM =		1423;
	protected static final short	INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_SENTENCE_END =					1424;
	};

/*************************************************************************
 *
 *	"He will bless those who fear the Lord,
 *	both great and lowly." (Psalm 115:13)
 *
 *************************************************************************/
