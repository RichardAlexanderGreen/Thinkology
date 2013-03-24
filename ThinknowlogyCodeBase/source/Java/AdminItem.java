/*
 *	Class:			AdminItem
 *	Parent class:	WordItem
 *	Grand parent:	Item
 *	Purpose:		To process tasks at administration level
 *	Version:		Thinknowlogy 2012 (release 2)
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

import java.util.Calendar;
import java.util.GregorianCalendar;

class AdminItem extends WordItem
	{
	// Private constructible variables

	private boolean isSystemStartingUp_;

	private AdminAssumption adminAssumption_;
	private AdminAuthorization adminAuthorization_;
	private AdminCleanup adminCleanup_;
	private AdminCollection adminCollection_;
	private AdminConclusion adminConclusion_;
	private AdminContext adminContext_;
	private AdminGrammar adminGrammar_;
	private AdminImperative adminImperative_;
	private AdminJustification adminJustification_;
	private AdminLanguage adminLanguage_;
	private AdminQuery adminQuery_;
	private AdminRead adminRead_;
	private AdminReadCreateWords adminReadCreateWords_;
	private AdminReadParse adminReadParse_;
	private AdminReadSentence adminReadSentence_;
	private AdminSelection adminSelection_;
	private AdminSolve adminSolve_;
	private AdminSpecification adminSpecification_;
	private AdminWrite adminWrite_;


	// Private methods

	private byte startup()
		{
		Calendar calendar;
		if( readStartupFile() == Constants.RESULT_OK )
			{
			calendar = new GregorianCalendar();

			if( calendar.get( Calendar.YEAR ) > Constants.NEW_RELEASE_AVAILABLE_YEAR ||
			( calendar.get( Calendar.YEAR ) == Constants.NEW_RELEASE_AVAILABLE_YEAR &&
			calendar.get( Calendar.MONTH ) >= Constants.NEW_RELEASE_AVAILABLE_MONTH ) )
				{
				if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_CONSOLE_NEW_RELEASE_AVAILABLE ) != Constants.RESULT_OK )
					addErrorInItem( 1, null, null, "I failed to write the update available text" );
				}
			isSystemStartingUp_ = false;
			}
		else
			return setSystemErrorInItem( 1, null, null, "I failed to read the startup file" );

		return CommonVariables.result;
		}


	// Private read methods

	private byte readStartupFile()
		{
		if( adminRead_ != null ||
		( adminRead_ = new AdminRead( this, this ) ) != null )
			return adminRead_.readStartupFile();

		return setErrorInItem( 1, null, null, "I failed to create the admin read module" );
		}


	// Constructor

	protected AdminItem()
		{
		// Private constructible variables

		isSystemStartingUp_ = true;

		adminAssumption_ = null;
		adminAuthorization_ = null;
		adminCleanup_ = null;
		adminCollection_ = null;
		adminConclusion_ = null;
		adminContext_ = null;
		adminGrammar_ = null;
		adminImperative_ = null;
		adminJustification_ = null;
		adminLanguage_ = null;
		adminQuery_ = null;
		adminRead_ = null;
		adminReadCreateWords_ = null;
		adminReadParse_ = null;
		adminReadSentence_ = null;
		adminSelection_ = null;
		adminSolve_ = null;
		adminSpecification_ = null;
		adminWrite_ = null;

		// Protected constructible variables

		fileList = null;
		readList = null;
		scoreList = null;
		wordList = null;
		conditionList = null;
		actionList = null;
		alternativeList = null;

		for( short adminListNr : Constants.AdminLists )
			adminList[adminListNr] = null;

		// Initialisation

		Presentation.init();		// Init presentation after restart
		CommonVariables.init();		// Init common variables after restart
		initializeItemVariables( this );

		if( startup() != Constants.RESULT_OK )
			addErrorInItem( 1, null, null, "I failed to start the administrator" );
		}


	// Protected constructible variables

	protected FileList fileList;
	protected ReadList readList;
	protected ScoreList scoreList;
	protected WordList wordList;
	protected SelectionList conditionList;
	protected SelectionList actionList;
	protected SelectionList alternativeList;

	protected List[] adminList = new List[Constants.NUMBER_OF_ADMIN_LISTS];


	// Protected common methods

	protected boolean isSystemStartingUp()
		{
		return isSystemStartingUp_;
		}

	protected boolean isGeneralizationReasoningWordType( boolean includeNoun, short generalizationWordTypeNr )
		{
		return ( generalizationWordTypeNr == Constants.WORD_TYPE_SYMBOL ||
				generalizationWordTypeNr == Constants.WORD_TYPE_NUMERAL ||
				generalizationWordTypeNr == Constants.WORD_TYPE_LETTER_SMALL ||
				generalizationWordTypeNr == Constants.WORD_TYPE_LETTER_CAPITAL ||
				generalizationWordTypeNr == Constants.WORD_TYPE_PROPER_NAME ||

				( includeNoun &&

				( generalizationWordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR ||
				generalizationWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL ) ) );
		}

	protected char adminListChar( short adminListNr )
		{
		switch( adminListNr )
			{
			case Constants.ADMIN_FILE_LIST:
				return Constants.ADMIN_FILE_LIST_SYMBOL;

			case Constants.ADMIN_READ_LIST:
				return Constants.ADMIN_READ_LIST_SYMBOL;

			case Constants.ADMIN_SCORE_LIST:
				return Constants.ADMIN_SCORE_LIST_SYMBOL;

			case Constants.ADMIN_WORD_LIST:
				return Constants.ADMIN_WORD_LIST_SYMBOL;

			case Constants.ADMIN_CONDITION_LIST:
				return Constants.ADMIN_CONDITION_LIST_SYMBOL;

			case Constants.ADMIN_ACTION_LIST:
				return Constants.ADMIN_ACTION_LIST_SYMBOL;

			case Constants.ADMIN_ALTERNATIVE_LIST:
				return Constants.ADMIN_ALTERNATIVE_LIST_SYMBOL;
			}

		return Constants.SYMBOL_QUESTION_MARK;
		}


	// Protected assignment methods

	protected byte assignSelectionSpecification( SelectionItem assignmentSelectionItem )
		{
		if( adminSpecification_ != null )
			return adminSpecification_.assignSelectionSpecification( assignmentSelectionItem );

		return setErrorInItem( 1, null, null, "The admin specification module isn't created yet" );
		}

	protected byte assignSpecification( WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminSpecification_ != null )
			return adminSpecification_.assignSpecification( false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, 0, null, generalizationWordItem, specificationWordItem, null ).result;

		return setErrorInItem( 1, null, null, "The admin specification module isn't created yet" );
		}

	protected SpecificationResultType assignSpecificationWithAuthorization( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( specificationWordItem == CommonVariables.predefinedNounGrammarLanguageWordItem ||
		specificationWordItem == predefinedNounInterfaceLanguageWordItem() )
			{
			if( adminLanguage_ != null )
				return adminLanguage_.assignSpecificationWithAuthorization( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, specificationString );
			else
				setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
			}
		else
			{
			if( adminAuthorization_ != null )
				return adminAuthorization_.assignSpecificationWithAuthorization( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, specificationString );
			else
				setErrorInItem( 1, null, null, "The admin authorization module isn't created yet" );
			}

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}


	// Protected assumption methods

	protected void initializeAdminAssumptionVariables()
		{
		if( adminAssumption_ != null )
			adminAssumption_.initializeAdminAssumptionVariables();
		}

	protected byte addSuggestiveQuestionAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, SpecificationItem specificSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		if( adminAssumption_ != null )
			return adminAssumption_.addSuggestiveQuestionAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, specificSpecificationItem, generalizationWordItem, specificationWordItem, relationWordItem );

		return setErrorInItem( 1, null, null, "The admin assumption module isn't created yet" );
		}

	protected byte findGeneralizationAssumptionBySpecification( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminAssumption_ != null ||
		( adminAssumption_ = new AdminAssumption( this, this ) ) != null )
			return adminAssumption_.findGeneralizationAssumptionBySpecification( isDeactive, isArchive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, generalizationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin assumption module" );
		}

	protected byte findGeneralizationAssumptionByGeneralization( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short specificQuestionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminAssumption_ != null )
			return adminAssumption_.findGeneralizationAssumptionByGeneralization( isDeactive, isArchive, isNegative, isPossessive, specificQuestionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, relationContextNr, generalizationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "The admin assumption module isn't created yet" );
		}

	protected byte findExclusiveSpecificationSubstitutionAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		if( adminAssumption_ != null )
			return adminAssumption_.findExclusiveSpecificationSubstitutionAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, generalizationWordItem, specificationWordItem, relationWordItem );

		return setErrorInItem( 1, null, null, "The admin assumption module isn't created yet" );
		}

	protected byte addAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short justificationTypeNr, short prepositionParamater, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		if( adminAssumption_ != null )
			return adminAssumption_.addAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, justificationTypeNr, prepositionParamater, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, definitionSpecificationItem, null, specificSpecificationItem, generalizationWordItem, specificationWordItem, relationWordItem );

		return setErrorInItem( 1, null, null, "The admin assumption module isn't created yet" );
		}


	// Protected authorization methods

	protected int myFirstSentenceNr()
		{
		if( adminAuthorization_ != null )
			return adminAuthorization_.myFirstSentenceNr();

		return Constants.NO_SENTENCE_NR;
		}

	protected byte login( WordItem specificationWordItem )
		{
		if( adminAuthorization_ != null )
			return adminAuthorization_.login( specificationWordItem );

		return setErrorInItem( 1, null, null, "The admin authorization module isn't created yet" );
		}

	protected byte authorizeWord( WordItem authorizationWordItem )
		{
		if( adminAuthorization_ != null ||
		( adminAuthorization_ = new AdminAuthorization( this, this ) ) != null )
			return adminAuthorization_.authorizeWord( authorizationWordItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin authorization module" );
		}

	protected String currentUserName()
		{
		if( adminAuthorization_ != null )
			return adminAuthorization_.currentUserName();

		return null;	// During startup - before login
		}


	// Protected cleanup methods

	protected void clearDontIncrementCurrentSentenceNr()
		{
		if( adminCleanup_ != null )
			adminCleanup_.clearDontIncrementCurrentSentenceNr();
		}

	protected boolean dontIncrementCurrentSentenceNr()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.dontIncrementCurrentSentenceNr();

		return false;
		}

	protected boolean foundChange()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.foundChange();

		return true;	// Default when admin cleanup module isn't created yet
		}

	protected boolean wasUndoOrRedo()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.wasUndoOrRedo();

		return true;	// Default when admin cleanup module isn't created yet
		}

	protected int highestSentenceNr()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.highestSentenceNr();

		return Constants.NO_SENTENCE_NR;
		}

	protected byte checkForChanges()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.checkForChanges();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte cleanupDeletedItems()
		{
		if( adminCleanup_ != null ||
		( adminCleanup_ = new AdminCleanup( this, this ) ) != null )
			return adminCleanup_.cleanupDeletedItems();

		return setErrorInItem( 1, null, null, "I failed to create the admin cleanup module" );
		}

	protected byte currentItemNr()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.currentItemNr();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte deleteRollbackInfo()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.deleteRollbackInfo();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte deleteAllTemporaryLists()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.deleteAllTemporaryLists();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte deleteUnusedInterpretations( boolean deleteAllActiveWordTypes )
		{
		if( adminCleanup_ != null )
			return adminCleanup_.deleteUnusedInterpretations( deleteAllActiveWordTypes );

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte deleteSentences( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		if( adminCleanup_ != null )
			return adminCleanup_.deleteSentences( isAvailableForRollback, lowestSentenceNr );

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte undoLastSentence()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.undoLastSentence();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}

	protected byte redoLastUndoneSentence()
		{
		if( adminCleanup_ != null )
			return adminCleanup_.redoLastUndoneSentence();

		return setErrorInItem( 1, null, null, "The admin cleanup module isn't created yet" );
		}


	// Protected collection methods

	protected CollectionResultType collectSpecificationStrings( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, short generalizationWordTypeNr, short specificationWordTypeNr, WordItem generalizationWordItem, String previousSpecificationString, String currentSpecificationString )
		{
		CollectionResultType collectionResult = new CollectionResultType();

		if( adminCollection_ != null )
			return adminCollection_.collectSpecificationStrings( isExclusive, isExclusiveGeneralization, isQuestion, generalizationWordTypeNr, specificationWordTypeNr, generalizationWordItem, previousSpecificationString, currentSpecificationString );

		collectionResult.result = setErrorInItem( 1, null, null, "The admin collection module isn't created yet" );
		return collectionResult;
		}

	protected CollectionResultType collectSpecificationWords( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, short generalizationWordTypeNr, short specificationWordTypeNr, WordItem compoundGeneralizationWordItem, WordItem generalizationWordItem, WordItem previousSpecificationWordItem, WordItem currentSpecificationWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();

		if( adminCollection_ != null )
			return adminCollection_.collectSpecificationWords( isExclusive, isExclusiveGeneralization, isQuestion, generalizationWordTypeNr, specificationWordTypeNr, compoundGeneralizationWordItem, generalizationWordItem, previousSpecificationWordItem, currentSpecificationWordItem );

		collectionResult.result = setErrorInItem( 1, null, null, "The admin collection module isn't created yet" );
		return collectionResult;
		}

	protected byte collectGeneralizationWordWithPreviousOne( boolean isExclusive, boolean isExclusiveGeneralization, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short questionParameter, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminCollection_ != null ||
		( adminCollection_ = new AdminCollection( this, this ) ) != null )
			return adminCollection_.collectGeneralizationWordWithPreviousOne( isExclusive, isExclusiveGeneralization, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, questionParameter, relationContextNr, generalizationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin collection module" );
		}

	protected byte collectRelationWords( boolean isExclusive, short relationWordTypeNr, short specificationWordTypeNr, WordItem previousRelationWordItem, WordItem currentRelationWordItem, WordItem specificationWordItem )
		{
		if( adminCollection_ != null )
			return adminCollection_.collectRelationWords( isExclusive, relationWordTypeNr, specificationWordTypeNr, previousRelationWordItem, currentRelationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "The admin collection module isn't created yet" );
		}


	// Protected conclusion methods

	protected void initializeAdminConclusionVariables()
		{
		if( adminConclusion_ != null )
			adminConclusion_.initializeAdminConclusionVariables();
		}

	protected byte addSpecificationGeneralizationConclusion( short generalizationWordTypeNr, short specificationWordTypeNr, SpecificationItem definitionSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminConclusion_ != null )
			return adminConclusion_.addSpecificationGeneralizationConclusion( generalizationWordTypeNr, specificationWordTypeNr, definitionSpecificationItem, generalizationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "The admin conclusion module isn't created yet" );
		}

	protected byte findPossessiveReversibleConclusion( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int specificationContextNr, int relationPronounContextNr, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		if( adminConclusion_ != null )
			return adminConclusion_.findPossessiveReversibleConclusion( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, specificationContextNr, relationPronounContextNr, relationContextNr, generalizationWordItem, specificationWordItem, relationWordItem );

		return setErrorInItem( 1, null, null, "The admin conclusion module isn't created yet" );
		}

	protected byte findSpecificationSubstitutionConclusionOrQuestion( boolean isAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isUserSentence, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( adminConclusion_ != null ||
		( adminConclusion_ = new AdminConclusion( this, this ) ) != null )
			return adminConclusion_.findSpecificationSubstitutionConclusionOrQuestion( isAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isUserSentence, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, specificationContextNr, generalizationWordItem, specificationWordItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin conclusion module" );
		}

	protected SpecificationResultType findCompoundSpecificationSubstitutionConclusion( boolean isNegative, boolean isPossessive, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( adminConclusion_ != null )
			return adminConclusion_.findCompoundSpecificationSubstitutionConclusion( isNegative, isPossessive, specificationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		specificationResult.result = setErrorInItem( 1, null, null, "The admin conclusion module isn't created yet" );
		return specificationResult;
		}


	// Protected context methods

	protected int highestContextNr()
		{
		if( adminContext_ != null )
			return adminContext_.highestContextNr();

		return Constants.NO_CONTEXT_NR;
		}

	protected ContextResultType addPronounContext( short contextWordTypeNr, WordItem contextWordItem )
		{
		ContextResultType contextResult = new ContextResultType();
		if( adminContext_ != null ||
		( adminContext_ = new AdminContext( this, this ) ) != null )
			return adminContext_.addPronounContext( contextWordTypeNr, contextWordItem );

		contextResult.result = setErrorInItem( 1, null, null, "I failed to create the admin grammar module" );
		return contextResult;
		}

	protected ContextResultType getRelationContextNr( boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isUserSentence, short questionParameter, int generalizationContextNr, int specificationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, ReadItem startRelationReadItem )
		{
		ContextResultType contextResult = new ContextResultType();
		if( adminContext_ != null ||
		( adminContext_ = new AdminContext( this, this ) ) != null )
			return adminContext_.getRelationContextNr( isExclusive, isNegative, isPossessive, isUserSentence, questionParameter, generalizationContextNr, specificationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, startRelationReadItem );

		contextResult.result = setErrorInItem( 1, null, null, "I failed to create the admin grammar module" );
		return contextResult;
		}


	// Protected grammar methods

	protected byte addGrammar( String grammarString )
		{
		if( adminGrammar_ != null ||
		( adminGrammar_ = new AdminGrammar( this, this ) ) != null )
			return adminGrammar_.addGrammar( grammarString );

		return setErrorInItem( 1, null, null, "I failed to create the admin grammar module" );
		}

	protected WordItem predefinedAdjectiveBusyWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedAdjectiveBusyWordItem();

		return null;
		}

	protected WordItem predefinedAdjectiveDoneWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedAdjectiveDoneWordItem();

		return null;
		}

	protected WordItem predefinedAdjectiveInvertedWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedAdjectiveInvertedWordItem();

		return null;
		}

	protected WordItem predefinedNounInterfaceLanguageWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedNounInterfaceLanguageWordItem();

		return null;
		}

	protected WordItem predefinedNounPasswordWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedNounPasswordWordItem();

		return null;
		}

	protected WordItem predefinedNounSolveLevelWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedNounSolveLevelWordItem();

		return null;
		}

	protected WordItem predefinedNounSolveMethodWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedNounSolveMethodWordItem();

		return null;
		}

	protected WordItem predefinedNounSolveStrategyWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedNounSolveStrategyWordItem();

		return null;
		}

	protected WordItem predefinedVerbLoginWordItem()
		{
		if( adminGrammar_ != null )
			return adminGrammar_.predefinedVerbLoginWordItem();

		return null;
		}


	// Protected imperative methods

	protected boolean isRestart()
		{
		if( adminImperative_ != null )
			return adminImperative_.isRestart();

		return false;
		}

	protected byte executeImperative( boolean initializeVariables, short executionListNr, short imperativeParameter, short specificationWordParameter, short specificationWordTypeNr, int endSolveProgress, String executionString, WordItem generalizationWordItem, WordItem specificationWordItem, ReadItem startRelationWordReadItem, ReadItem endRelationWordReadItem, SelectionItem executionSelectionItem, SelectionItem actionSelectionItem )
		{
		if( adminImperative_ != null ||
		( adminImperative_ = new AdminImperative( this, this ) ) != null )
			return adminImperative_.executeImperative( initializeVariables, executionListNr, imperativeParameter, specificationWordParameter, specificationWordTypeNr, endSolveProgress, executionString, generalizationWordItem, specificationWordItem, startRelationWordReadItem, endRelationWordReadItem, executionSelectionItem, actionSelectionItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin imperative module" );
		}


	// Protected justification methods

	protected byte writeJustificationSpecification( String justificationSentenceString, SpecificationItem justificationSpecificationItem )
		{
		if( adminJustification_ != null ||
		( adminJustification_ = new AdminJustification( this ) ) != null )
			return adminJustification_.writeJustificationSpecification( justificationSentenceString, justificationSpecificationItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin justification module" );
		}


	// Protected language methods

	protected byte authorizeLanguageWord( WordItem authorizationWordItem )
		{
		if( adminLanguage_ != null )
			return adminLanguage_.authorizeLanguageWord( authorizationWordItem );

		return setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
		}

	protected byte createGrammarLanguage( String languageNameString )
		{
		if( adminLanguage_ != null ||
		( adminLanguage_ = new AdminLanguage( this, this ) ) != null )
			return adminLanguage_.createGrammarLanguage( languageNameString );

		return setErrorInItem( 1, null, null, "I failed to create the admin language module" );
		}

	protected byte createInterfaceLanguage( String languageNameString )
		{
		if( adminLanguage_ != null ||
		( adminLanguage_ = new AdminLanguage( this, this ) ) != null )
			return adminLanguage_.createInterfaceLanguage( languageNameString );

		return setErrorInItem( 1, null, null, "I failed to create the admin language module" );
		}

	protected byte createLanguageSpecification( WordItem languageWordItem, WordItem languageNounWordItem )
		{
		if( adminLanguage_ != null )
			return adminLanguage_.createLanguageSpecification( languageWordItem, languageNounWordItem );

		return setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
		}

	protected byte assignInterfaceLanguage( String languageNameString )
		{
		if( adminLanguage_ != null )
			return adminLanguage_.assignInterfaceLanguage( languageNameString );

		return setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
		}

	protected byte assignGrammarAndInterfaceLanguage( short newLanguageNr )
		{
		if( adminLanguage_ != null )
			return adminLanguage_.assignGrammarAndInterfaceLanguage( newLanguageNr );

		return setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
		}


	// Protected query methods

	protected void initializeQueryStringPosition()
		{
		if( adminQuery_ != null )
			adminQuery_.initializeQueryStringPosition();
		}

	protected byte writeTextWithPossibleQueryCommands( short promptTypeNr, String textString )
		{
		if( adminQuery_ != null ||
		( adminQuery_ = new AdminQuery( this, this ) ) != null )
			return adminQuery_.writeTextWithPossibleQueryCommands( promptTypeNr, textString );

		return setErrorInItem( 1, null, null, "I failed to create the admin query module" );
		}

	protected byte executeQuery( boolean suppressMessage, boolean returnToPosition, boolean writeQueryResult, short promptTypeNr, String queryString )
		{
		if( adminQuery_ != null ||
		( adminQuery_ = new AdminQuery( this, this ) ) != null )
			return adminQuery_.executeQuery( suppressMessage, returnToPosition, writeQueryResult, promptTypeNr, queryString );

		return setErrorInItem( 1, null, null, "I failed to create the admin query module" );
		}


	// Protected read methods

	protected byte readExamplesFile( String examplesFileNameString )
		{
		if( adminRead_ != null )
			return adminRead_.readExamplesFile( examplesFileNameString );

		return setErrorInItem( 1, null, null, "The admin read module isn't created yet" );
		}

	protected byte readAndExecute()
		{
		if( adminRead_ != null )
			return adminRead_.readAndExecute();

		return setErrorInItem( 1, null, null, "The admin read module isn't created yet" );
		}

	protected byte getUserInput( boolean isPassword, boolean isQuestion, boolean isText, String promptInputString, StringBuffer readStringBuffer )
		{
		if( adminRead_ != null )
			return adminRead_.readLine( true, isPassword, isQuestion, isText, Constants.NO_SENTENCE_NR, promptInputString, readStringBuffer );

		return setErrorInItem( 1, null, null, "The admin read module isn't created yet" );
		}

	protected FileResultType readInfoFile( boolean reportErrorIfFileDoesNotExist, String infoFileNameString )
		{
		FileResultType fileResult = new FileResultType();

		if( adminRead_ != null )
			return adminRead_.readInfoFile( reportErrorIfFileDoesNotExist, infoFileNameString );

		fileResult.result = setErrorInItem( 1, null, null, "The admin read module isn't created yet" );
		return fileResult;
		}


	// Protected read create words methods

	protected void deleteReadList()
		{
		if( readList != null )
			readList.deleteList();
		}

	protected boolean createImperativeSentence()
		{
		if( readList != null )
			return readList.createImperativeSentence();

		return false;
		}

	protected boolean hasPassedGrammarIntegrityCheck()
		{
		if( readList != null )
			return readList.hasPassedGrammarIntegrityCheck();

		return false;
		}

	protected ReadResultType createReadWords( String grammarString )
		{
		ReadResultType readResult = new ReadResultType();

		if( adminReadCreateWords_ != null )
			return adminReadCreateWords_.createReadWords( grammarString );

		readResult.result = setErrorInItem( 1, null, null, "The admin read words module isn't created yet" );
		return readResult;
		}

	protected ReadResultType getWordInfo( boolean skipDoubleQuotes, int startWordPosition, String wordString )
		{
		ReadResultType readResult = new ReadResultType();

		if( adminReadCreateWords_ != null )
			return adminReadCreateWords_.getWordInfo( skipDoubleQuotes, startWordPosition, wordString );

		readResult.result = setErrorInItem( 1, null, null, "The admin read words module isn't created yet" );
		return readResult;
		}

	protected byte createReadWord( short wordTypeLanguageNr, short wordTypeNr, int wordPosition, String textString, WordItem readWordItem )
		{
		if( adminReadCreateWords_ != null )
			return adminReadCreateWords_.createReadWord( wordTypeLanguageNr, wordTypeNr, wordPosition, textString, readWordItem );

		return setErrorInItem( 1, null, null, "The admin read words module isn't created yet" );
		}

	protected WordResultType createWord( boolean wasPreviousWordDefiniteArticle, short wordTypeNr, short wordParameter, int wordLength, String wordString )
		{
		WordResultType wordResult = new WordResultType();

		if( adminReadCreateWords_ != null ||
		( adminReadCreateWords_ = new AdminReadCreateWords( this, this ) ) != null )
			return adminReadCreateWords_.createWord( wasPreviousWordDefiniteArticle, wordTypeNr, wordParameter, wordLength, wordString );

		wordResult.result = setErrorInItem( 1, null, null, "I failed to create the admin read words module" );
		return wordResult;
		}

	protected ReadItem firstActiveReadItem()
		{
		if( readList != null )
			return readList.firstActiveReadItem();

		return null;
		}

	protected ReadItem firstDeactiveReadItem()
		{
		if( readList != null )
			return readList.firstDeactiveReadItem();

		return null;
		}

	protected ReadItem firstCurrentLanguageActiveReadItem()
		{
		if( readList != null )
			return readList.firstCurrentLanguageActiveReadItem();

		return null;
		}


	// Protected read parse methods

	protected ReadResultType parseReadWords( GrammarItem startOfGrammarItem )
		{
		ReadResultType readResult = new ReadResultType();
		if( adminReadParse_ != null ||
		( adminReadParse_ = new AdminReadParse( this, this ) ) != null )
			return adminReadParse_.parseReadWords( Constants.NO_GRAMMAR_LEVEL, startOfGrammarItem );

		readResult.result = setErrorInItem( 1, null, null, "I failed to create the admin read parse module" );
		return readResult;
		}


	// Protected read sentence methods

	protected void dontShowConclusions()
		{
		if( adminReadSentence_ != null )
			adminReadSentence_.dontShowConclusions();
		}

	protected void dontDeleteRollbackInfo()
		{
		if( adminReadSentence_ != null )
			adminReadSentence_.dontDeleteRollbackInfo();
		}

	protected boolean areReadItemsStillValid()
		{
		if( adminReadSentence_ != null )
			return adminReadSentence_.areReadItemsStillValid();

		return false;
		}

	protected boolean isPossessivePronounStructure()
		{
		if( adminReadSentence_ != null )
			return adminReadSentence_.isPossessivePronounStructure();

		return false;
		}

	protected byte readSentence( String readString )
		{
		if( adminReadSentence_ != null ||
		( adminReadSentence_ = new AdminReadSentence( this, this ) ) != null )
			return adminReadSentence_.readSentence( readString );

		return setErrorInItem( 1, null, null, "I failed to create the admin read sentence module" );
		}


	// Protected selection methods

	protected byte checkForDuplicateSelection()
		{
		if( adminSelection_ != null )
			return adminSelection_.checkForDuplicateSelection();

		return setErrorInItem( 1, null, null, "The admin selection module isn't created yet" );
		}

	protected byte createSelectionTextPart( boolean isAction, boolean isNewStart, short selectionLevel, short selectionListNr, short imperativeParameter, String specificationString )
		{
		if( adminSelection_ != null ||
		( adminSelection_ = new AdminSelection( this, this ) ) != null )
			return adminSelection_.createSelectionPart( isAction, false, false, false, false, isNewStart, false, false, false, selectionLevel, selectionListNr, imperativeParameter, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_WORD_PARAMETER, Constants.WORD_TYPE_TEXT, Constants.WORD_TYPE_TEXT, Constants.WORD_TYPE_UNDEFINED, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, 0, null, null, null, specificationString );

		return setErrorInItem( 1, null, null, "I failed to create the admin selection module" );
		}

	protected byte createSelectionPart( boolean isAction, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isFirstComparisonPart, boolean isNewStart, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short selectionLevel, short selectionListNr, short imperativeParameter, short prepositionParameter, short specificationWordParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		if( adminSelection_ != null ||
		( adminSelection_ = new AdminSelection( this, this ) ) != null )
			return adminSelection_.createSelectionPart( isAction, isAssignedOrClear, isDeactive, isArchive, isFirstComparisonPart, isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, selectionListNr, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString );

		return setErrorInItem( 1, null, null, "I failed to create the admin selection module" );
		}

	protected byte executeSelections()
		{
		if( adminSelection_ != null )
			return adminSelection_.executeSelection( Constants.MAX_PROGRESS, null );

		return CommonVariables.result;
		}

	protected byte executeSelection( int endSolveProgress, SelectionItem actionSelectionItem )
		{
		if( adminSelection_ != null )
			return adminSelection_.executeSelection( endSolveProgress, actionSelectionItem );

		return setErrorInItem( 1, null, null, "The admin selection module isn't created yet" );
		}


	// Protected solve methods

	protected void clearCurrentSolveProgress()
		{
		if( adminSolve_ != null )
			adminSolve_.clearCurrentSolveProgress();
		}

	protected void deleteScoreList()
		{
		if( scoreList != null )
			scoreList.deleteList();
		}

	protected byte canWordBeSolved( WordItem solveWordItem )
		{
		if( adminSolve_ != null )
			return adminSolve_.canWordBeSolved( solveWordItem );

		return setErrorInItem( 1, null, null, "The admin solve module isn't created yet" );
		}

	protected byte solveWord( int endSolveProgress, WordItem solveWordItem, SelectionItem actionSelectionItem )
		{
		if( adminSolve_ != null ||
		( adminSolve_ = new AdminSolve( this, this ) ) != null )
			return adminSolve_.solveWord( endSolveProgress, solveWordItem, actionSelectionItem );

		return setErrorInItem( 1, null, null, "I failed to create the admin solve module" );
		}

	protected byte findPossibilityToSolveWord( boolean addScores, boolean isInverted, boolean duplicatesAllowed, boolean prepareSort, short solveStrategyParameter, WordItem solveWordItem )
		{
		if( adminSolve_ != null )
			return adminSolve_.findPossibilityToSolveWord( addScores, isInverted, duplicatesAllowed, prepareSort, solveStrategyParameter, solveWordItem );

		return setErrorInItem( 1, null, null, "The admin solve module isn't created yet" );
		}

	protected SelectionResultType checkCondition( SelectionItem conditionSelectionItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();

		if( adminSolve_ != null ||
		( adminSolve_ = new AdminSolve( this, this ) ) != null )
			return adminSolve_.checkCondition( conditionSelectionItem );

		selectionResult.result = setErrorInItem( 1, null, null, "I failed to create the admin solve module" );
		return selectionResult;
		}


	// Protected specification methods

	protected void initializeLinkedWord()
		{
		if( adminSpecification_ != null )
			adminSpecification_.initializeLinkedWord();
		}

	protected void initializeAdminSpecificationVariables()
		{
		if( adminSpecification_ != null )
			adminSpecification_.initializeAdminSpecificationVariables();
		}

	protected boolean isUserSentencePossessive()
		{
		if( adminReadSentence_ != null )
			return adminReadSentence_.isUserSentencePossessive();

		return false;
		}

	protected CollectionResultType addUserSpecifications( boolean initializeVariables, boolean isAction, boolean isAssignment, boolean isConditional, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNewStart, boolean isPossessive, boolean isSpecificationGeneralization, short prepositionParameter, short questionParameter, short selectionLevel, short selectionListNr, short imperativeParameter, short specificationWordParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationPronounContextNr, ReadItem generalizationWordItem, ReadItem startSpecificationReadItem, ReadItem endSpecificationReadItem, ReadItem startRelationReadItem, ReadItem endRelationWordReadItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();

		if( adminSpecification_ != null ||
		( adminSpecification_ = new AdminSpecification( this, this ) ) != null )
			return adminSpecification_.addUserSpecifications( initializeVariables, isAction, isAssignment, isConditional, isDeactive, isArchive, isExclusive, isNewStart, isPossessive, isSpecificationGeneralization, prepositionParameter, questionParameter, selectionLevel, selectionListNr, imperativeParameter, specificationWordParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationPronounContextNr, generalizationWordItem, startSpecificationReadItem, endSpecificationReadItem, startRelationReadItem, endRelationWordReadItem );

		collectionResult.result = setErrorInItem( 1, null, null, "I failed to create the admin specification module" );
		return collectionResult;
		}

	protected SpecificationResultType addSpecification( boolean isAssignment, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( adminSpecification_ != null ||
		( adminSpecification_ = new AdminSpecification( this, this ) ) != null )
			return adminSpecification_.addSpecification( isAssignment, false, isDeactive, isArchive, isExclusive, isNegative, isPossessive, false, isSelfGenerated, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, specificationString );

		specificationResult.result = setErrorInItem( 1, null, null, "I failed to create the admin specification module" );
		return specificationResult;
		}

	protected SpecificationResultType addSpecificationWithAuthorization( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( specificationWordItem == CommonVariables.predefinedNounGrammarLanguageWordItem ||
		specificationWordItem == predefinedNounInterfaceLanguageWordItem() )
			{
			if( adminLanguage_ != null )
				return adminLanguage_.addSpecificationWithAuthorization( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, specificationString );
			else
				setErrorInItem( 1, null, null, "The admin language module isn't created yet" );
			}
		else
			{
			if( adminAuthorization_ != null )
				return adminAuthorization_.addSpecificationWithAuthorization( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, specificationString );
			else
				setErrorInItem( 1, null, null, "The admin authorization module isn't created yet" );
			}

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected WordItem userSentenceGeneralizationWordItem()
		{
		if( adminSpecification_ != null )
			return adminSpecification_.userSentenceGeneralizationWordItem();

		return null;
		}


	// Protected write methods

	protected void initializeAdminWriteVariables()
		{
		if( adminWrite_ != null )
			adminWrite_.initializeAdminWriteVariables();
		}

	protected byte answerQuestions()
		{
		if( adminWrite_ != null )
			return adminWrite_.answerQuestions();

		return setErrorInItem( 1, null, null, "The admin write module isn't created yet" );
		}

	protected byte integrityCheck( boolean isQuestion, String integrityCheckSentenceString )
		{
		if( adminWrite_ != null ||
		( adminWrite_ = new AdminWrite( this, this ) ) != null )
			return adminWrite_.integrityCheck( isQuestion, integrityCheckSentenceString );

		return setErrorInItem( 1, null, null, "I failed to create the admin write module" );
		}

	protected byte writeJustificationReport( WordItem justificationWordItem )
		{
		if( adminWrite_ != null )
			return adminWrite_.writeSelfGeneratedInfo( false, true, true, true, true, null, justificationWordItem );

		return setErrorInItem( 1, null, null, "The admin write module isn't created yet" );
		}

	protected byte writeSelfGeneratedInfo( boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeSelfGeneratedQuestions )
		{
		if( adminWrite_ != null )
			return adminWrite_.writeSelfGeneratedInfo( writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, writeSelfGeneratedQuestions );

		return setErrorInItem( 1, null, null, "The admin write module isn't created yet" );
		}

	protected byte writeInfoAboutWord( boolean writeCurrentSentenceOnly, boolean writeUserSpecifications, boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeUserQuestions, boolean writeSelfGeneratedQuestions, boolean writeSpecificationInfo, boolean writeRelatedInfo, String integrityCheckSentenceString, WordItem writeWordItem )
		{
		if( adminWrite_ != null )
			return adminWrite_.writeInfoAboutWord( writeCurrentSentenceOnly, false, writeUserSpecifications, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, writeUserQuestions, writeSelfGeneratedQuestions, writeSpecificationInfo, writeRelatedInfo, integrityCheckSentenceString, writeWordItem );

		return setErrorInItem( 1, null, null, "The admin write module isn't created yet" );
		}


	// Public methods

	protected byte interact()
		{
		if( CommonVariables.result != Constants.RESULT_SYSTEM_ERROR )
			{
			readAndExecute();
			CommonVariables.result = Constants.RESULT_OK;	// Ignore Constants.RESULT_ERROR. Application will only exit on system error
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Listen to me, all you in the distant lands!
 *	Pay attentation, you who are far away!
 *	The Lord called me before my birth;
 *	from within the womb he called me by name." (Psalm 49:1)
 *
 *************************************************************************/
