/*
 *	Class:		CommonVariables
 *	Purpose:	To hold the common variables
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

class CommonVariables
	{
	protected static boolean foundAnswerToQuestion;
	protected static boolean foundLanguageMixUp;
	protected static boolean foundMatchingStrings;
	protected static boolean foundQuery;
	protected static boolean foundWordReference;

	protected static boolean hasShownMessage;
	protected static boolean hasShownWarning;

	protected static boolean isAssignmentChanged;
	protected static boolean isFirstAnswerToQuestion;
	protected static boolean isQuestionAlreadyAnswered;
	protected static boolean isSpecificationConfirmedByUser;
	protected static boolean isUserQuestion;

	protected static byte result;

	protected static short matchingWordTypeNr;
	protected static short queryWordTypeNr;

	protected static short currentAssignmentLevel;
	protected static short currentGrammarLanguageNr;
	protected static short currentUserNr;
	protected static short currentWriteLevel;

	protected static int nDeletedItems;
	protected static int nActiveQueryItems;
	protected static int nDeactiveQueryItems;
	protected static int nArchivedQueryItems;
	protected static int nDeletedQueryItems;

	protected static int currentSentenceNr;
	protected static int highestInUseSentenceNr;
	protected static int myFirstSentenceNr;
	protected static int removeSentenceNr;

	protected static int currentItemNr;
	protected static int removeStartItemNr;

	protected static ScoreList adminScoreList;

	protected static SelectionList adminConditionList;
	protected static SelectionList adminActionList;
	protected static SelectionList adminAlternativeList;

	protected static SpecificationItem lastShownMoreSpecificSpecificationItem;

	protected static WordItem currentGrammarLanguageWordItem;
	protected static WordItem currentInterfaceLanguageWordItem;
	protected static WordItem firstWordItem;
	protected static WordItem predefinedNounGrammarLanguageWordItem;
	protected static WordItem predefinedNounUserWordItem;

	protected static StringBuffer queryStringBuffer;
	protected static StringBuffer writeSentenceStringBuffer;

	protected static StringBuffer currentPathStringBuffer;


	// Protected methods

	protected static void init()
		{
		foundAnswerToQuestion = false;
		foundLanguageMixUp = false;
		foundMatchingStrings = false;
		foundQuery = false;
		foundWordReference = false;

		hasShownMessage = false;
		hasShownWarning = false;

		isAssignmentChanged = false;
		isFirstAnswerToQuestion = false;
		isQuestionAlreadyAnswered = false;
		isSpecificationConfirmedByUser = false;
		isUserQuestion = false;

		result = Constants.RESULT_OK;

		matchingWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		queryWordTypeNr = Constants.WORD_TYPE_UNDEFINED;

		currentAssignmentLevel = Constants.NO_ASSIGNMENT_LEVEL;
		currentGrammarLanguageNr = Constants.NO_LANGUAGE_NR;
		currentUserNr = Constants.NO_USER_NR;
		currentWriteLevel = Constants.NO_WRITE_LEVEL;

		nDeletedItems = 0;
		nActiveQueryItems = 0;
		nDeactiveQueryItems = 0;
		nArchivedQueryItems = 0;
		nDeletedQueryItems = 0;

		currentSentenceNr = 1;		// First sentence
		highestInUseSentenceNr = Constants.NO_SENTENCE_NR;
		myFirstSentenceNr = Constants.NO_SENTENCE_NR;
		removeSentenceNr = Constants.NO_SENTENCE_NR;

		currentItemNr = Constants.NO_ITEM_NR;
		removeStartItemNr = Constants.NO_ITEM_NR;

		adminScoreList = null;

		adminConditionList = null;
		adminActionList = null;
		adminAlternativeList = null;

		lastShownMoreSpecificSpecificationItem = null;

		currentGrammarLanguageWordItem = null;
		currentInterfaceLanguageWordItem = null;
		firstWordItem = null;
		predefinedNounGrammarLanguageWordItem = null;
		predefinedNounUserWordItem = null;

		queryStringBuffer = null;
		writeSentenceStringBuffer = null;

		// Don't initialize 'currentPathStringBuffer' there. 
		// It will be set in the main() method of the Thinknowlogy class.
		// Otherwise it will destroy the initialization by Thinknowlogy
//		currentPathStringBuffer = null;
		}
	};

/*************************************************************************
 *
 *	"Honor the Lord for the glory of his name.
 *	Worship the Lord in the splendor of his holiness." (Psalm 29:2)
 *
 *************************************************************************/
