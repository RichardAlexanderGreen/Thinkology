/*
 *	Class:			WordItem
 *	Parent class:	Item
 *	Purpose:		To store and process word information
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

class WordItem extends Item
	{
	// Private constructible variables

	private	String changeKey_;

	private WordAssignment wordAssignment_;
	private WordCleanup wordCleanup_;
	private WordCollection wordCollection_;
	private WordQuery wordQuery_;
	private WordQuestion wordQuestion_;
	private WordSpecification wordSpecification_;
	private WordType wordType_;
	private WordWrite wordWrite_;
	private WordWriteSentence wordWriteSentence_;
	private WordWriteWords wordWriteWords_;


	// Private loadable variables

	private short wordParameter_;


	// Private common methods

	private String userNameInWord( short userNr )
		{
		GeneralizationItem currentGeneralizationItem;
		String userName = nameByCollectionOrderNr( userNr );

		if( userNr == 1 &&		// No user collection - only one user available
		userName == null &&		// No name by collection found
		( currentGeneralizationItem = firstActiveGeneralizationItemOfSpecification() ) != null )
			{
			do	{
				if( currentGeneralizationItem.isGeneralizationPropername() )
					return currentGeneralizationItem.generalizationWordItem().anyWordTypeString();
				}
			while( ( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
			}

		return userName;
		}

	private String wordTypeNameInWord( short wordTypeNr )
		{
		if( grammarList != null )
			return grammarList.wordTypeNameInList( wordTypeNr );

		return null;
		}

	private String grammarLanguageNameInWord( short languageNr )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem generalizationWordItem;
		String languageName = nameByCollectionOrderNr( languageNr );

		if( languageNr == 1 &&		// No language collection - only one language available
		languageName == null &&		// No name by collection found
		( currentGeneralizationItem = firstActiveGeneralizationItemOfSpecification() ) != null )
			{
			do	{
				if( ( generalizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
					{
					if( generalizationWordItem.isGrammarLanguage() )
						return currentGeneralizationItem.generalizationWordItem().anyWordTypeString();
					}
				}
			while( ( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
			}

		return languageName;
		}


	// Private assignment methods

	private SpecificationItem firstAssignment( boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( false, isDeactive, isArchive, isQuestion );

		return null;
		}

	private SpecificationItem firstDeactiveAssignment( boolean includeAnsweredQuestions, short questionParameter )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeAnsweredQuestions, true, false, questionParameter );

		return null;
		}

	private SpecificationItem firstArchiveAssignment( boolean includeAnsweredQuestions, short questionParameter )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeAnsweredQuestions, false, true, questionParameter );

		return null;
		}


	// Private collection methods

	private short highestCollectionOrderNrInWord( int collectionNr )
		{
		if( collectionList != null &&
		collectionNr > Constants.NO_COLLECTION_NR )
			return collectionList.highestCollectionOrderNr( collectionNr );

		return Constants.NO_ORDER_NR;
		}

	private int collectionNrByCompoundGeneralizationWordInWord( short collectionWordTypeNr, WordItem compoundGeneralizationWordItem )
		{
		if( collectionList != null )
			return collectionList.collectionNrByCompoundGeneralizationWord( collectionWordTypeNr, compoundGeneralizationWordItem );

		return Constants.NO_COLLECTION_NR;
		}

	private int collectionNrByCommonWordInWord( short collectionWordTypeNr, WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.collectionNrByCommonWord( collectionWordTypeNr, commonWordItem );

		return Constants.NO_COLLECTION_NR;
		}

	private int highestCollectionNrInWord()
		{
		if( collectionList != null )
			return collectionList.highestCollectionNr();

		return Constants.NO_COLLECTION_NR;
		}

	private int nonCompoundCollectionNrInWord( int compoundCollectionNr )
		{
		if( compoundCollectionNr > Constants.NO_CONTEXT_NR &&
		collectionList != null )
			return collectionList.nonCompoundCollectionNr( compoundCollectionNr );

		return Constants.NO_COLLECTION_NR;
		}


	// Private context methods

	private boolean isContextCurrentlyUpdatedInWord( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		if( contextList != null )
			return contextList.isContextCurrentlyUpdated( isPossessive, contextNr, specificationWordItem );

		return false;
		}

	private boolean isContextSimilarInWord( int firstContextNr, int secondContextNr )
		{
		if( contextList != null &&
		firstContextNr > Constants.NO_CONTEXT_NR &&
		secondContextNr > Constants.NO_CONTEXT_NR )
			return ( contextList.hasContext( firstContextNr ) == contextList.hasContext( secondContextNr ) );

		return true;
		}

	private boolean isContextSubsetInWord( int fullSetContextNr, int subsetContextNr )
		{
		if( contextList != null )
			return contextList.isContextSubset( fullSetContextNr, subsetContextNr );

		return false;
		}

	private ContextItem contextItemInWord( int contextNr )
		{
		if( contextList != null )
			return contextList.contextItem( contextNr );

		return null;
		}


	// Private query methods

	private String nameByCollectionOrderNr( short collectionOrderNr )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( ( currentGeneralizationItem = firstActiveGeneralizationItemOfSpecification() ) != null )
			{
			do	{
				if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
					{
					if( currentGeneralizationWordItem.hasCollectionOrderNr( collectionOrderNr ) )
						return currentGeneralizationWordItem.anyWordTypeString();
					}
				}
			while( ( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
			}

		return null;
		}


	// Private question methods

	private SpecificationItem firstAnsweredQuestion( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, short questionParameter )
		{
		if( isAssignment )
			{
			if( assignmentList != null )
				return assignmentList.firstAssignmentItem( true, isDeactiveAssignment, isArchiveAssignment, questionParameter );
			}
		else
			{
			if( specificationList != null )
				return specificationList.firstActiveSpecificationItem( true, questionParameter );
			}

		return null;
		}

	private SpecificationItem firstQuestionSpecification( boolean includeAnsweredQuestions, short questionParameter )
		{
		if( questionParameter > Constants.NO_QUESTION_PARAMETER &&
		specificationList != null )
			return specificationList.firstActiveSpecificationItem( includeAnsweredQuestions, questionParameter );

		return null;
		}


	// Private specification methods

	private byte checkSpecificationForUsageInWord( SpecificationItem unusedSpecificationItem )
		{
		if( assignmentList != null &&
		assignmentList.checkSpecificationItemForUsage( false, false, unusedSpecificationItem ) == Constants.RESULT_OK )
			{
			if( assignmentList.checkSpecificationItemForUsage( true, false, unusedSpecificationItem ) == Constants.RESULT_OK )
				assignmentList.checkSpecificationItemForUsage( false, true, unusedSpecificationItem );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			specificationList.checkSpecificationItemForUsage( false, false, unusedSpecificationItem );

		if( CommonVariables.result == Constants.RESULT_OK &&
		justificationList != null )
			justificationList.checkSpecificationItemForUsage( unusedSpecificationItem );

		return CommonVariables.result;
		}


	// Protected constructible variables

	protected boolean isWordCheckedForSolving;

	protected SpecificationList assignmentList;
	protected CollectionList collectionList;
	protected ContextList contextList;
	protected GeneralizationList generalizationList;
	protected GrammarList grammarList;
	protected InterfaceList interfaceList;
	protected JustificationList justificationList;
	protected WriteList writeList;
	protected SpecificationList specificationList;
	protected WordTypeList wordTypeList;

	protected List[] wordList = new List[Constants.NUMBER_OF_WORD_LISTS];


	// Constructor for AdminItem

	protected WordItem()
		{
		// Private constructible variables

		changeKey_ = null;

		wordAssignment_ = null;
		wordCleanup_ = null;
		wordCollection_ = null;
		wordQuery_ = null;
		wordQuestion_ = null;
		wordSpecification_ = null;
		wordType_ = null;
		wordWrite_ = null;
		wordWriteSentence_ = null;
		wordWriteWords_ = null;

		// Private loadable variables

		wordParameter_ = Constants.NO_WORD_PARAMETER;

		// Protected constructible variables

		isWordCheckedForSolving = false;

		assignmentList = null;
		collectionList = null;
		contextList = null;
		generalizationList = null;
		grammarList = null;
		interfaceList = null;
		justificationList = null;
		writeList = null;
		specificationList = null;
		wordTypeList = null;

		for( short wordListNr : Constants.WordLists )
			wordList[wordListNr] = null;
		}


	// Constructor

	protected WordItem( short wordParameter, List myList )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, this );

		// Private constructible variables

		changeKey_ = null;

		wordAssignment_ = null;
		wordCleanup_ = null;
		wordCollection_ = null;
		wordQuery_ = null;
		wordQuestion_ = null;
		wordSpecification_ = null;
		wordType_ = null;
		wordWrite_ = null;
		wordWriteSentence_ = null;
		wordWriteWords_ = null;

		// Private loadable variables

		wordParameter_ = wordParameter;

		// Protected constructible variables

		isWordCheckedForSolving = false;

		assignmentList = null;
		collectionList = null;
		contextList = null;
		generalizationList = null;
		grammarList = null;
		interfaceList = null;
		justificationList = null;
		writeList = null;
		specificationList = null;
		wordTypeList = null;

		for( short wordListNr : Constants.WordLists )
			wordList[wordListNr] = null;
		}


	// Protected methods

	protected void addErrorInWord( int methodLevel, String moduleNameString, String errorString )
		{
		addErrorInItem( ( methodLevel + 1 ), moduleNameString, anyWordTypeString(), errorString );
		}

	protected void addErrorInWord( char listChar, int methodLevel, String moduleNameString, String errorString )
		{
		addErrorInItem( listChar, ( methodLevel + 1 ), moduleNameString, anyWordTypeString(), errorString );
		}

	protected byte setErrorInWord( int methodLevel, String moduleNameString, String errorString )
		{
		return setErrorInItem( ( methodLevel + 1 ), moduleNameString, anyWordTypeString(), errorString );
		}
	protected byte setSystemErrorInWord( int methodLevel, String moduleNameString, String errorString )
		{
		return setSystemErrorInItem( ( methodLevel + 1 ), moduleNameString, anyWordTypeString(), errorString );
		}


	// Protected virtual item methods

	protected void showWords( boolean returnQueryToPosition )
		{
		if( wordTypeList != null &&
		isSelectedByQuery )
			wordTypeList.showWords( returnQueryToPosition );
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( wordParameter_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				wordParameter_ > Constants.NO_WORD_PARAMETER ) );
		}

	protected byte checkForUsage()
		{
		if( assignmentList != null &&
		assignmentList.checkWordItemForUsage( false, false, this ) == Constants.RESULT_OK )
			{
			if( assignmentList.checkWordItemForUsage( true, false, this ) == Constants.RESULT_OK )
				assignmentList.checkWordItemForUsage( false, true, this );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		collectionList != null )
			collectionList.checkWordItemForUsage( this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		contextList != null )
			contextList.checkWordItemForUsage( this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		generalizationList != null )
			generalizationList.checkWordItemForUsage( this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			specificationList.checkWordItemForUsage( false, false, this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminConditionList != null )
			CommonVariables.adminConditionList.checkWordItemForUsage( this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminActionList != null )
			CommonVariables.adminActionList.checkWordItemForUsage( this );

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminAlternativeList != null )
			CommonVariables.adminAlternativeList.checkWordItemForUsage( this );

		return CommonVariables.result;
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// Ascending creationSentenceNr
				creationSentenceNr() < nextSortItem.creationSentenceNr() );
		}

	protected byte findMatchingWordReferenceString( String searchString )
		{
		if( wordTypeList != null )
			return wordTypeList.findMatchingWordReferenceString( searchString );

		return setErrorInWord( 1, null, "The word type list isn't created yet" );
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		if( wordQuery_ != null )
			return wordQuery_.toStringBuffer( queryWordTypeNr );

		return null;
		}


	// Protected common methods

	protected boolean hasItems()
		{
		for( short wordListNr : Constants.WordLists )
			{
			if( wordList[wordListNr] != null &&
			wordList[wordListNr].hasItems() )
				return true;
			}

		return false;
		}

	protected boolean iAmAdmin()
		{
		return ( myList() == null );
		}

	protected boolean isAdjectiveAssignedOrClear()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_CLEAR ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_ASSIGNED );
		}

	protected boolean isAdjectiveCalledOrNamed()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_CALLED_OR_NAMED );
		}

	protected boolean isAdjectiveClear()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_CLEAR );
		}

	protected boolean isAdjectiveComparison()
		{
		return ( isAdjectiveComparisonLess() ||
				isAdjectiveComparisonEqual() ||
				isAdjectiveComparisonMore() );
		}

	protected boolean isAdjectiveComparisonLess()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_LESS ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_EARLIER ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_SMALLER );
		}

	protected boolean isAdjectiveComparisonEqual()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_EQUAL ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_SAME );
		}

	protected boolean isAdjectiveComparisonMore()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_MORE ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_BIGGER ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_LARGER ||
				wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_LATER );
		}
/*
	protected boolean isAdjectiveNegative()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_NO ||
				wordParameter_ == Constants.WORD_PARAMETER_ADVERB_NOT );
//				wordParameter_ == Constants.WORD_PARAMETER_ADVERB_DO_NOT );
		}
*/
	protected boolean isAdjectiveOdd()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_ODD );
		}

	protected boolean isAdjectiveEven()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_EVEN );
		}

	protected boolean isAdjectiveOddOrEven()
		{
		return ( isAdjectiveOdd() ||
				isAdjectiveEven() );
		}

	protected boolean isNounDeveloper()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_DEVELOPER );
		}

	protected boolean isNounHead()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_HEAD );
		}

	protected boolean isNounTail()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_TAIL );
		}

	protected boolean isNounNumber()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_NUMBER );
		}

	protected boolean isNounPassword()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_PASSWORD );
		}

	protected boolean isNounUser()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_USER );
		}

	protected boolean isNounValue()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_VALUE );
		}

	protected boolean isPredefinedBasicVerb()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_IS ||
				wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_WAS ||
				wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_CAN_BE ||
				wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_HAS ||
				wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_HAD );
		}

	protected boolean isVerbImperativeLogin()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_LOGIN );
		}

	protected boolean isPredefinedWord()
		{
		return ( wordParameter_ > Constants.NO_WORD_PARAMETER );
		}

	protected boolean isUserDefinedWord()
		{
		return ( wordParameter_ == Constants.NO_WORD_PARAMETER );
		}

	protected boolean isNounWordType( short wordTypeNr )
		{
		return ( wordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR ||
				wordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean needsAuthorizationForChanges()
		{
		return ( changeKey_ != null );
		}

	protected short wordParameter()
		{
		return wordParameter_;
		}

	protected byte assignChangePermissions( String changeKey )
		{
		if( changeKey_ == null )
			changeKey_ = changeKey;
		else
			{
			// Not the content of the string, but its address
			// identifies the owner. So, compare the addresses.
			if( changeKey_ != changeKey )
				return setErrorInWord( 1, null, "The change key is already assigned" );
			}

		return CommonVariables.result;
		}

	protected char wordListChar( short wordListNr )
		{
		switch( wordListNr )
			{
			case Constants.WORD_ASSIGNMENT_LIST:
				return Constants.WORD_ASSIGNMENT_LIST_SYMBOL;

			case Constants.WORD_COLLECTION_LIST:
				return Constants.WORD_COLLECTION_LIST_SYMBOL;

			case Constants.WORD_GENERALIZATION_LIST:
				return Constants.WORD_GENERALIZATION_LIST_SYMBOL;

			case Constants.WORD_INTERFACE_LANGUAGE_LIST:
				return Constants.WORD_INTERFACE_LANGUAGE_LIST_SYMBOL;

			case Constants.WORD_JUSTIFICATION_LIST:
				return Constants.WORD_JUSTIFICATION_LIST_SYMBOL;

			case Constants.WORD_GRAMMAR_LANGUAGE_LIST:
				return Constants.WORD_GRAMMAR_LANGUAGE_LIST_SYMBOL;

			case Constants.WORD_WRITE_LIST:
				return Constants.WORD_WRITE_LIST_SYMBOL;

			case Constants.WORD_SPECIFICATION_LIST:
				return Constants.WORD_SPECIFICATION_LIST_SYMBOL;

			case Constants.WORD_TYPE_LIST:
				return Constants.WORD_TYPE_LIST_SYMBOL;

			case Constants.WORD_CONTEXT_LIST:
				return Constants.WORD_CONTEXT_LIST_SYMBOL;
			}

		return Constants.SYMBOL_QUESTION_MARK;
		}

	protected String userName( short userNr )
		{
		if( CommonVariables.predefinedNounUserWordItem != null )
			return CommonVariables.predefinedNounUserWordItem.userNameInWord( userNr );

		return null;
		}

	protected String wordTypeName( short wordTypeNr )
		{
		if( CommonVariables.currentGrammarLanguageWordItem != null )
			return CommonVariables.currentGrammarLanguageWordItem.wordTypeNameInWord( wordTypeNr );

		return null;
		}

	protected String grammarLanguageName( short wordTypeNr )
		{
		if( CommonVariables.predefinedNounGrammarLanguageWordItem != null )
			return CommonVariables.predefinedNounGrammarLanguageWordItem.grammarLanguageNameInWord( wordTypeNr );

		return null;
		}

	protected WordItem predefinedWordItem( short wordParameter )
		{
		WordItem currentWordItem = CommonVariables.firstWordItem;		// Do in all words

		if( wordParameter > Constants.NO_WORD_PARAMETER )
			{
			while( currentWordItem != null )
				{
				if( currentWordItem.wordParameter() == wordParameter )
					return currentWordItem;

				currentWordItem = currentWordItem.nextWordItem();
				}
			}

		return null;
		}

	protected WordItem nextWordItem()
		{
		return (WordItem)nextItem;
		}


	// Protected assignment methods

	protected int numberOfActiveAssignments()
		{
		if( assignmentList != null )
			return assignmentList.numberOfActiveAssignments();

		return 0;
		}

	protected byte createNewAssignmentLevel()
		{
		if( wordAssignment_ != null ||
		( wordAssignment_ = new WordAssignment( this ) ) != null )
			return wordAssignment_.createNewAssignmentLevel();

		return setErrorInWord( 1, null, "I failed to create my word assignment module" );
		}

	protected byte deleteAssignmentLevelInWord()
		{
		if( assignmentList != null )
			return assignmentList.deleteAssignmentLevelInList();

		return CommonVariables.result;
		}

	protected byte deactivateActiveAssignment( SpecificationItem activeItem )
		{
		if( wordAssignment_ != null )
			return wordAssignment_.deactivateActiveAssignment( activeItem );

		return setErrorInWord( 1, null, "The word assignment module isn't created yet" );
		}

	protected byte archiveDeactiveAssignment( SpecificationItem deactiveItem )
		{
		if( wordAssignment_ != null )
			return wordAssignment_.archiveDeactiveAssignment( deactiveItem );

		return setErrorInWord( 1, null, "The word assignment module isn't created yet" );
		}

	protected SpecificationResultType getAssignmentOrderNr( short collectionWordTypeNr )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordAssignment_ != null )
			return wordAssignment_.getAssignmentOrderNr( collectionWordTypeNr );

		return specificationResult;
		}

	protected SpecificationResultType getAssignmentWordParameter()
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordAssignment_ != null )
			return wordAssignment_.getAssignmentWordParameter();

		return specificationResult;
		}

	protected SpecificationResultType assignSpecificationInWord( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString, String changeKey )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( isAuthorizedForChanges( changeKey ) )
			{
			if( specificationWordItem == null ||
			specificationWordItem.isAuthorizedForChanges( changeKey ) )
				{
				if( wordAssignment_ != null ||
				( wordAssignment_ = new WordAssignment( this ) ) != null )
					return wordAssignment_.assignSpecification( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString );

				setErrorInWord( 1, null, "I failed to create my word assignment module" );
				}
			else
				setErrorInWord( 1, null, "You are not authorized to assign the given specification" );
			}
		else
			setErrorInWord( 1, null, "You are not authorized to assign this word" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType findAssignmentByRelationContext( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isPossessive, short questionParameter, WordItem relationContextWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( assignmentList != null )
			return assignmentList.findAssignmentItemByRelationContext( includeAnsweredQuestions, isDeactive, isArchive, isPossessive, questionParameter, relationContextWordItem );

		return specificationResult;
		}

	protected SpecificationResultType createAssignment( boolean isAnsweredQuestion, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short assignmentLevel, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordAssignment_ != null )
			return wordAssignment_.createAssignment( isAnsweredQuestion, isConcludedAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isValueSpecification, assignmentLevel, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString );

		specificationResult.result = setErrorInWord( 1, null, "The word assignment module isn't created yet" );
		return specificationResult;
		}

	protected SpecificationItem firstActiveNumeralAssignment()
		{
		if( assignmentList != null )
			return assignmentList.firstNumeralAssignmentItem( false, false, false, false );

		return null;
		}

	protected SpecificationItem firstActiveStringAssignment()
		{
		if( assignmentList != null )
			return assignmentList.firstStringAssignmentItem( false, false, false, false );

		return null;
		}

	protected SpecificationItem firstActiveAssignmentButNotAQuestion()
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( false, false, false, false );

		return null;
		}

	protected SpecificationItem lastActiveAssignmentButNotAQuestion()
		{
		if( assignmentList != null )
			return assignmentList.lastAssignmentItem( false, false, false, false );

		return null;
		}

	protected SpecificationItem firstActiveAssignment( boolean isQuestion )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( false, false, false, isQuestion );

		return null;
		}

	protected SpecificationItem firstActiveAssignment( boolean includeAnsweredQuestions, short questionParameter )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeAnsweredQuestions, false, false, questionParameter );

		return null;
		}

	protected SpecificationItem firstActiveAssignment( boolean isDifferentRelationContext, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( false, false, isDifferentRelationContext, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return null;
		}

	protected SpecificationItem firstAssignment( boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( false, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return null;
		}

	protected SpecificationItem firstAssignment( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeActiveItems, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString );

		return null;
		}

	protected SpecificationItem firstAssignment( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeActiveItems, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return null;
		}

	protected SpecificationItem firstAssignment( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( assignmentList != null )
			return assignmentList.firstAssignmentItem( includeActiveItems, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return null;
		}


	// Protected cleanup methods

	protected void deleteWriteList()
		{
		if( writeList != null )
			writeList.deleteList();
		}

	protected int highestSentenceNr()
		{
		short wordListNr = 0;
		int tempSentenceNr;
		int highestSentenceNr = Constants.NO_SENTENCE_NR;

		while( wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( wordList[wordListNr] != null &&
			( tempSentenceNr = wordList[wordListNr].highestSentenceNrInList() ) > highestSentenceNr )
				highestSentenceNr = tempSentenceNr;

			wordListNr++;
			}

		return highestSentenceNr;
		}

	protected byte getCurrentItemNrInWord()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.currentItemNr();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte getHighestInUseSentenceNrInWord( boolean includeTemporaryLists, boolean includeDeletedItems, int highestSentenceNr )
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.getHighestInUseSentenceNr( includeTemporaryLists, includeDeletedItems, highestSentenceNr );

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte deleteRollbackInfo()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.deleteRollbackInfo();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte deleteSentencesInWord( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.deleteSentences( isAvailableForRollback, lowestSentenceNr );

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte rollbackDeletedRedoInfo()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.rollbackDeletedRedoInfo();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte undoCurrentSentence()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.undoCurrentSentence();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte redoCurrentSentence()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.redoCurrentSentence();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte removeFirstRangeOfDeletedItems()
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.removeFirstRangeOfDeletedItems();

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte decrementSentenceNrsInWord( int startSentenceNr )
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.decrementSentenceNrs( startSentenceNr );

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}

	protected byte decrementItemNrRangeInWord( int startSentenceNr, int decrementItemNr, int decrementOffset )
		{
		if( wordCleanup_ != null ||
		( wordCleanup_ = new WordCleanup( this ) ) != null )
			return wordCleanup_.decrementItemNrRange( startSentenceNr, decrementItemNr, decrementOffset );

		return setErrorInWord( 1, null, "I failed to create my word cleanup module" );
		}


	// Protected collection methods

	protected boolean hasCollections()
		{
		return ( collectionList != null );
		}

	protected boolean hasCollection( WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.hasCollectionItem( commonWordItem );

		return false;
		}

	protected boolean hasCollectionNr( int collectionNr )
		{
		if( collectionList != null )
			return collectionList.hasCollectionNr( collectionNr );

		return false;
		}

	protected boolean hasCollectionOrderNr( int collectionOrderNr )
		{
		if( collectionList != null )
			return collectionList.hasCollectionOrderNr( collectionOrderNr );

		return false;
		}

	protected boolean isCompoundCollection( int collectionNr )
		{
		if( collectionList != null )
			return collectionList.isCompoundCollection( collectionNr );

		return false;
		}

	protected boolean isCompoundCollection( int collectionNr, WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.isCompoundCollection( collectionNr, commonWordItem );

		return false;
		}

	protected boolean isCollectedCommonWord( WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.isCollectedCommonWord( commonWordItem );

		return false;
		}

	protected boolean isExclusiveCollection( int collectionNr )
		{
		if( collectionList != null )
			return collectionList.isExclusiveCollection( collectionNr );

		return false;
		}

	protected boolean foundCollection( int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem )
		{
		if( collectionList != null )
			return collectionList.foundCollection( collectionNr, collectionWordItem, commonWordItem, compoundGeneralizationWordItem );

		return false;
		}

	protected short collectionOrderNrByWordTypeNr( short collectionWordTypeNr )
		{
		if( collectionList != null )
			return collectionList.collectionOrderNrByWordTypeNr( collectionWordTypeNr );

		return Constants.NO_ORDER_NR;
		}

	protected short collectionOrderNr( int collectionNr, WordItem collectionWordItem, WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.collectionOrderNr( collectionNr, collectionWordItem, commonWordItem );

		return Constants.NO_ORDER_NR;
		}

	protected short highestCollectionOrderNrInAllWords( int collectionNr )
		{
		short tempCollectionOrderNr;
		short highestCollectionOrderNr = Constants.NO_ORDER_NR;
		WordItem currentWordItem;

		if( collectionNr > Constants.NO_COLLECTION_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( tempCollectionOrderNr = currentWordItem.highestCollectionOrderNrInWord( collectionNr ) ) > highestCollectionOrderNr )
					highestCollectionOrderNr = tempCollectionOrderNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return highestCollectionOrderNr;
		}

	protected int collectionNr( short collectionWordTypeNr )
		{
		if( collectionList != null )
			return collectionList.collectionNr( collectionWordTypeNr );

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNr( short collectionWordTypeNr, WordItem commonWordItem )
		{
		if( collectionList != null )
			return collectionList.collectionNr( collectionWordTypeNr, commonWordItem );

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNr( short collectionWordTypeNr, WordItem commonWordItem, WordItem compoundGeneralizationWordItem )
		{
		if( collectionList != null )
			return collectionList.collectionNr( collectionWordTypeNr, commonWordItem, compoundGeneralizationWordItem );

		return Constants.NO_COLLECTION_NR;
		}

	protected int compoundCollectionNr( short collectionWordTypeNr )
		{
		if( collectionList != null )
			return collectionList.compoundCollectionNr( collectionWordTypeNr );

		return Constants.NO_COLLECTION_NR;
		}

	protected int nonCompoundCollectionNr( short collectionWordTypeNr )
		{
		if( collectionList != null )
			return collectionList.nonCompoundCollectionNr( collectionWordTypeNr );

		return Constants.NO_COLLECTION_NR;
		}

	protected int nonCompoundCollectionNrInAllWords( int compoundCollectionNr )
		{
		int nonCompoundCollectionNr;
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
			{
			do	{
				if( ( nonCompoundCollectionNr = currentWordItem.nonCompoundCollectionNrInWord( compoundCollectionNr ) ) > Constants.NO_COLLECTION_NR )
					return nonCompoundCollectionNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNrInAllWords( int contextNr )
		{
		ContextItem foundContextItem;
		WordItem currentWordItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( foundContextItem = currentWordItem.contextItemInWord( contextNr ) ) != null )
					return currentWordItem.collectionNrByCommonWordInWord( foundContextItem.contextWordTypeNr(), foundContextItem.specificationWordItem() );
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNrInAllWords( short collectionWordTypeNr, WordItem compoundGeneralizationWordItem )
		{
		int collectionNr;
		WordItem currentWordItem;

		if( collectionWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		compoundGeneralizationWordItem != null &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( collectionNr = currentWordItem.collectionNrByCompoundGeneralizationWordInWord( collectionWordTypeNr, compoundGeneralizationWordItem ) ) > Constants.NO_COLLECTION_NR )
					return collectionNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int highestCollectionNrInAllWords()
		{
		int tempCollectionNr;
		int highestCollectionNr = Constants.NO_COLLECTION_NR;
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( tempCollectionNr = currentWordItem.highestCollectionNrInWord() ) > highestCollectionNr )
					highestCollectionNr = tempCollectionNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return highestCollectionNr;
		}

	protected CollectionResultType createCollectionByGeneralization( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, boolean tryGeneralizations, short collectionWordTypeNr, WordItem generalizationWordItem, WordItem collectionWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();

		if( wordCollection_ != null ||
		( wordCollection_ = new WordCollection( this ) ) != null )
			return wordCollection_.createCollectionByGeneralization( isExclusive, isExclusiveGeneralization, isQuestion, tryGeneralizations, collectionWordTypeNr, generalizationWordItem, collectionWordItem );

		collectionResult.result = setErrorInWord( 1, null, "I failed to create my word collection module" );
		return collectionResult;
		}

	protected CollectionResultType findCollection( boolean allowDifferentCommonWord, WordItem collectionWordItem, WordItem commonWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();

		if( collectionList != null )
			return collectionList.findCollectionItem( allowDifferentCommonWord, collectionWordItem, commonWordItem );

		return collectionResult;
		}

	protected byte createCollection( boolean isExclusive, short collectionWordTypeNr, short commonWordTypeNr, int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem, String collectionString )
		{
		if( wordCollection_ != null ||
		( wordCollection_ = new WordCollection( this ) ) != null )
			return wordCollection_.createCollection( isExclusive, collectionWordTypeNr, commonWordTypeNr, collectionNr, collectionWordItem, commonWordItem, compoundGeneralizationWordItem, collectionString );

		return setErrorInWord( 1, null, "I failed to create my word collection module" );
		}

	protected WordItem commonWordItem( int collectionNr, WordItem compoundGeneralizationWordItem )
		{
		if( collectionList != null )
			return collectionList.commonWordItem( collectionNr, compoundGeneralizationWordItem );

		return null;
		}

	protected WordItem compoundCollectionWordItem( int collectionNr, WordItem notThisCommonWordItem )
		{
		if( collectionList != null )
			return collectionList.compoundCollectionWordItem( collectionNr, notThisCommonWordItem );

		return null;
		}


	// Protected context methods

	protected void clearContextWriteLevelInWord( short currentWriteLevel, int contextNr )
		{
		if( contextList != null )
			contextList.clearContextWriteLevel( currentWriteLevel, contextNr );
		}

	protected boolean hasContextInWord( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		if( contextList != null )
			return contextList.hasContext( isPossessive, contextNr, specificationWordItem );

		return false;
		}

	protected boolean isContextCurrentlyUpdatedInAllWords( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		WordItem currentWordItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( currentWordItem.isContextCurrentlyUpdatedInWord( isPossessive, contextNr, specificationWordItem ) )
					return true;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return false;
		}

	protected boolean isContextSimilarInAllWords( int firstContextNr, int secondContextNr )
		{
		WordItem currentWordItem;

		if( firstContextNr > Constants.NO_CONTEXT_NR &&
		secondContextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )	// Do in all words
			{
			do	{
				if( !currentWordItem.isContextSimilarInWord( firstContextNr, secondContextNr ) )
					return false;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return true;
		}

	protected boolean isContextSubsetInAllWords( int fullSetContextNr, int subsetContextNr )
		{
		WordItem currentWordItem;

		if( subsetContextNr == Constants.NO_CONTEXT_NR ||
		fullSetContextNr == subsetContextNr )
			return true;

		if( fullSetContextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( currentWordItem.isContextSubsetInWord( fullSetContextNr, subsetContextNr ) )
					return true;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return false;
		}

	protected short contextWordTypeNrInWord( int contextNr )
		{
		ContextItem foundContextItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( foundContextItem = contextItemInWord( contextNr ) ) != null )		// Do in all words
			return foundContextItem.contextWordTypeNr();

		return Constants.WORD_TYPE_UNDEFINED;
		}

	protected short contextWordTypeNrInAllWords( int contextNr )
		{
		short contextWordTypeNr;
		WordItem currentWordItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( contextWordTypeNr = currentWordItem.contextWordTypeNrInWord( contextNr ) ) > Constants.WORD_TYPE_UNDEFINED )
					return contextWordTypeNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return Constants.WORD_TYPE_UNDEFINED;
		}

	protected int contextNrInWord( boolean isPossessive, short contextWordTypeNr, WordItem specificationWordItem )
		{
		if( contextList != null )
			return contextList.contextNr( isPossessive, contextWordTypeNr, specificationWordItem );

		return Constants.NO_CONTEXT_NR;
		}

	protected int highestContextNrInWord()
		{
		if( contextList != null )
			return contextList.highestContextNr();

		return Constants.NO_CONTEXT_NR;
		}

	protected int nContextWords( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		int nContextWords = 0;
		WordItem currentWordItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		specificationWordItem != null &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( currentWordItem.hasContextInWord( isPossessive, contextNr, specificationWordItem ) )
					nContextWords++;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return nContextWords;
		}

	protected byte addContext( boolean isPossessive, short contextWordTypeNr, short specificationWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		if( !iAmAdmin() )
			{
			if( contextList == null )
				{
				if( ( contextList = new ContextList( this ) ) != null )
					wordList[Constants.WORD_CONTEXT_LIST] = contextList;
				else
					return setErrorInWord( 1, null, "I failed to create a context list" );
				}

			return contextList.addContext( isPossessive, contextWordTypeNr, specificationWordTypeNr, contextNr, specificationWordItem );
			}
		else
			return setErrorInWord( 1, null, "The admin does not have context" );
		}

	protected ContextItem firstActiveContext()
		{
		if( contextList != null )
			return contextList.firstActiveContextItem();

		return null;
		}

	protected ContextItem contextItemInWord( boolean isPossessive, short contextWordTypeNr, WordItem specificationWordItem )
		{
		if( contextList != null )
			return contextList.contextItem( isPossessive, contextWordTypeNr, specificationWordItem );

		return null;
		}

	protected ContextItem contextItemInWord( boolean isPossessive, short contextWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		if( contextList != null )
			return contextList.contextItem( isPossessive, contextWordTypeNr, contextNr, specificationWordItem );

		return null;
		}

	protected WordItem contextWordInAllWords( boolean isPossessive, int contextNr, WordItem specificationWordItem, WordItem previousWordItem )
		{
		WordItem currentWordItem;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = ( previousWordItem == null ? CommonVariables.firstWordItem : previousWordItem.nextWordItem() ) ) != null )
			{
			do	{
				if( currentWordItem.hasContextInWord( isPossessive, contextNr, specificationWordItem ) )
					return currentWordItem;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return null;
		}


	// Protected generalization methods

	protected GeneralizationResultType findGeneralization( boolean isRelation, short questionParameter, short generalizationWordTypeNr, WordItem generalizationWordItem )
		{
		GeneralizationResultType generalizationResult = new GeneralizationResultType();

		if( generalizationList != null )
			return generalizationList.findGeneralizationItem( isRelation, questionParameter, generalizationWordTypeNr, generalizationWordItem );

		return generalizationResult;
		}

	protected byte createGeneralization( boolean isRelation, short questionParameter, short specificationWordTypeNr, short generalizationWordTypeNr, WordItem generalizationWordItem )
		{
		if( !iAmAdmin() )
			{
			if( generalizationList == null )
				{
				if( ( generalizationList = new GeneralizationList( this ) ) != null )
					wordList[Constants.WORD_GENERALIZATION_LIST] = generalizationList;
				else
					return setErrorInWord( 1, null, "I failed to create a generalization list" );
				}

			return generalizationList.createGeneralizationItem( isRelation, questionParameter, specificationWordTypeNr, generalizationWordTypeNr, generalizationWordItem );
			}
		else
			return setErrorInWord( 1, null, "The admin doesn't have generalizations" );
		}

	protected GeneralizationItem firstActiveGeneralizationItem()
		{
		if( generalizationList != null )
			return generalizationList.firstActiveGeneralizationItem();

		return null;
		}

	protected GeneralizationItem firstActiveGeneralizationItemOfSpecification()
		{
		if( generalizationList != null )
			return generalizationList.firstActiveGeneralizationItem( false );

		return null;
		}

	protected GeneralizationItem firstActiveGeneralizationItemOfRelation()
		{
		if( generalizationList != null )
			return generalizationList.firstActiveGeneralizationItem( true );

		return null;
		}


	// Protected grammar methods

	protected void setOptionEnd()
		{
		if( grammarList != null )
			grammarList.setOptionEnd();
		}

	protected void setChoiceEnd( int choiceEndItemNr )
		{
		if( grammarList != null )
			grammarList.setChoiceEnd( choiceEndItemNr );
		}

	protected boolean isGrammarLanguage()
		{
		return ( grammarList != null );
		}

	protected boolean needToCheckGrammar()
		{
		if( grammarList != null )
			return grammarList.needToCheckGrammar();

		return false;
		}

	protected int numberOfActiveGrammarLanguages()
		{
		int nLanguages = 0;
		GeneralizationItem currentGeneralizationItem;
		WordItem generalizationWordItem;

		if( CommonVariables.predefinedNounGrammarLanguageWordItem != null &&
		( currentGeneralizationItem = CommonVariables.predefinedNounGrammarLanguageWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
			{
			do	{
				if( ( generalizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
					{
					if( generalizationWordItem.isGrammarLanguage() )
						nLanguages++;
					}
				}
			while( ( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
			}

		return nLanguages;
		}

	protected GrammarResultType createGrammar( boolean isDefinitionStart, boolean isNewStart, boolean isOptionStart, boolean isChoiceStart, boolean skipOptionForWriting, short wordTypeNr, short grammarParameter, int grammarStringLength, String grammarString, GrammarItem definitionGrammarItem )
		{
		GrammarResultType grammarResult = new GrammarResultType();

		if( !iAmAdmin() )
			{
			if( grammarList == null )
				{
				if( ( grammarList = new GrammarList( this ) ) != null )
					wordList[Constants.WORD_GRAMMAR_LANGUAGE_LIST] = grammarList;
				else
					{
					grammarResult.result = setErrorInWord( 1, null, "I failed to create a grammar list" );
					return grammarResult;
					}
				}

			return grammarList.createGrammarItem( isDefinitionStart, isNewStart, isOptionStart, isChoiceStart, skipOptionForWriting, wordTypeNr, grammarParameter, grammarStringLength, grammarString, definitionGrammarItem );
			}
		else
			setErrorInWord( 1, null, "The admin doesn't have grammar" );

		grammarResult.result = CommonVariables.result;
		return grammarResult;
		}

	protected GrammarResultType findGrammar( boolean ignoreValue, short grammarParameter, int grammarStringLength, String grammarString )
		{
		GrammarResultType grammarResult = new GrammarResultType();

		if( grammarList != null )
			return grammarList.findGrammarItem( ignoreValue, grammarParameter, grammarStringLength, grammarString );

		return grammarResult;
		}

	protected GrammarResultType removeDuplicateGrammarDefinition()
		{
		GrammarResultType grammarResult = new GrammarResultType();

		if( grammarList != null )
			return grammarList.removeDuplicateGrammarDefinition();

		grammarResult.result = setErrorInWord( 1, null, "The grammar list isn't created yet" );
		return grammarResult;
		}

	protected byte checkGrammar()
		{
		if( grammarList != null )
			return grammarList.checkGrammar();

		return setErrorInWord( 1, null, "The grammar list isn't created yet" );
		}

	protected byte checkGrammarForUsageInWord( GrammarItem unusedGrammarItem )
		{
		if( grammarList != null )
			{
			if( grammarList.checkGrammarItemForUsage( unusedGrammarItem ) == Constants.RESULT_OK )
				{
				if( writeList != null )
					return writeList.checkGrammarItemForUsage( unusedGrammarItem );
				}
			}
		else
			return setErrorInWord( 1, null, "The grammar list isn't created yet" );

		return CommonVariables.result;
		}

	protected byte linkLaterDefinedGrammarWords()
		{
		if( grammarList != null )
			return grammarList.linkLaterDefinedGrammarWords();

		return setErrorInWord( 1, null, "The grammar list isn't created yet" );
		}

	protected GrammarItem firstPluralNounEndingGrammarItem()
		{
		GrammarItem firstGrammarItem;

		if( grammarList != null &&
		( firstGrammarItem = grammarList.firstActiveGrammarItem() ) != null )
			return firstGrammarItem.firstPluralNounEndingGrammarItem();

		return null;
		}

	protected GrammarItem startOfGrammar()
		{
		if( grammarList != null )
			return grammarList.startOfGrammar();

		return null;
		}


	// Protected interface methods

	protected boolean isInterfaceLanguage()
		{
		return ( interfaceList != null );
		}

	protected byte checkInterface( short interfaceParameter, String interfaceString )
		{
		if( !iAmAdmin() )
			{
			if( interfaceList == null )
				{
				if( ( interfaceList = new InterfaceList( this ) ) != null )
					wordList[Constants.WORD_INTERFACE_LANGUAGE_LIST] = interfaceList;
				else
					return setErrorInWord( 1, null, "I failed to create an interface list" );
				}

			return interfaceList.checkInterface( interfaceParameter, interfaceString );
			}
		else
			return setErrorInWord( 1, null, "The admin doesn't have interfacing" );
		}

	protected byte createInterface( short interfaceParameter, int interfaceStringLength, String interfaceString )
		{
		if( interfaceList != null )
			return interfaceList.createInterfaceItem( interfaceParameter, interfaceStringLength, interfaceString );

		return setErrorInWord( 1, null, "The interface list isn't created yet" );
		}

	protected String interfaceString( short interfaceParameter )
		{
		if( interfaceList != null )
			return interfaceList.interfaceString( interfaceParameter );

		return Constants.INTERFACE_STRING_NOT_AVAILABLE;
		}


	// Protected justification methods

	protected boolean needToRecalculateAssumptionsAfterwards()
		{
		if( justificationList != null )
			return justificationList.needToRecalculateAssumptionsAfterwards();

		return false;
		}

	protected JustificationResultType addJustification( boolean forceToCreateJustification, short justificationTypeNr, short orderNr, int originalSentenceNr, JustificationItem attachedJustificationItem, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();

		if( !iAmAdmin() )
			{
			if( justificationList == null )
				{
				if( ( justificationList = new JustificationList( myWord() ) ) != null )
					wordList[Constants.WORD_JUSTIFICATION_LIST] = justificationList;
				else
					{
					justificationResult.result = setErrorInWord( 1, null, "I failed to create a justification list" );
					return justificationResult;
					}
				}

			return justificationList.addJustificationItem( forceToCreateJustification, true, justificationTypeNr, orderNr, originalSentenceNr, attachedJustificationItem, definitionSpecificationItem, anotherDefinitionSpecificationItem, specificSpecificationItem );
			}
		else
			setErrorInWord( 1, null, "The admin does not have justification" );

		justificationResult.result = CommonVariables.result;
		return justificationResult;
		}

	protected JustificationResultType checkForConfirmedJustifications( boolean isExclusiveGeneralization, short justificationTypeNr, JustificationItem specificationJustificationItem, SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem, WordItem specificationWordItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		if( justificationList != null )
			return justificationList.checkForConfirmedJustificationItems( isExclusiveGeneralization, justificationTypeNr, specificationJustificationItem, definitionSpecificationItem, specificSpecificationItem, specificationWordItem );

		justificationResult.result = setErrorInWord( 1, null, "The justification list isn't created yet" );
		return justificationResult;
		}

	protected byte attachJustification( JustificationItem attachJustificationItem, SpecificationItem currentSpecificationItem )
		{
		if( justificationList != null )
			return justificationList.attachJustificationItem( attachJustificationItem, currentSpecificationItem );

		return setErrorInWord( 1, null, "The justification list isn't created yet" );
		}

	protected byte archiveJustification( boolean isExclusiveGeneralization, JustificationItem oldJustificationItem, JustificationItem replacingJustificationItem )
		{
		if( justificationList != null )
			return justificationList.archiveJustificationItem( false, isExclusiveGeneralization, oldJustificationItem, replacingJustificationItem );

		return setErrorInWord( 1, null, "The justification list isn't created yet" );
		}

	protected byte checkSpecificationForUsageOfInvolvedWords( SpecificationItem unusedSpecificationItem )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( justificationList != null )
			justificationList.checkSpecificationItemForUsage( unusedSpecificationItem );

		if( CommonVariables.result == Constants.RESULT_OK )
			{
			if( checkSpecificationForUsageInWord( unusedSpecificationItem ) == Constants.RESULT_OK )
				{
				if( ( currentGeneralizationItem = firstActiveGeneralizationItem() ) != null )
					{
					do	{
						if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
							{
							if( currentGeneralizationWordItem.checkSpecificationForUsageInWord( unusedSpecificationItem ) != Constants.RESULT_OK )
								addErrorInWord( 1, null, "I failed to check the specifications in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" for its usage" );
							}
						else
							return setErrorInWord( 1, null, "I found an undefined generalization word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItem() ) != null );
					}
				}
			else
				addErrorInWord( 1, null, "I failed to check my specifications" );
			}

		return CommonVariables.result;
		}

	protected byte updateSpecificationsInJustificationInWord( SpecificationItem oldSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		if( justificationList != null )
			{
			if( justificationList.updateSpecificationsInJustificationItems( true, oldSpecificationItem, replacingSpecificationItem ) == Constants.RESULT_OK )
				return justificationList.updateSpecificationsInJustificationItems( false, oldSpecificationItem, replacingSpecificationItem );
			}

		return CommonVariables.result;
		}


	// Protected query methods

	protected void countQueryResult()
		{
		if( wordQuery_ != null )
			wordQuery_.countQueryResult();
		}

	protected void clearQuerySelections()
		{
		if( wordQuery_ != null )
			wordQuery_.clearQuerySelections();
		}

	protected byte itemQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.itemQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte listQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.listQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte wordTypeQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.wordTypeQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte parameterQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.parameterQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte wordQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordNameString )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.wordQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte wordReferenceQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.wordReferenceQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte stringQueryInWord( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString )
		{
		if( wordQuery_ != null ||
		( wordQuery_ = new WordQuery( this ) ) != null )
			return wordQuery_.stringQuery( selectOnFind, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString );

		return setErrorInWord( 1, null, "I failed to create my word query module" );
		}

	protected byte showQueryResultInWord( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		if( wordQuery_ != null )
			return wordQuery_.showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth );

		return CommonVariables.result;
		}


	// Protected question methods

	protected byte findAnswerToNewUserQuestion()
		{
		if( wordQuestion_ != null ||
		( wordQuestion_ = new WordQuestion( this ) ) != null )
			return wordQuestion_.findAnswerToNewUserQuestion();

		return setErrorInWord( 1, null, "I failed to create my word question module" );
		}

	protected byte findPossibleQuestionAndMarkAsAnswered( int compoundSpecificationCollectionNr, SpecificationItem answerSpecificationItem )
		{
		if( wordQuestion_ != null ||
		( wordQuestion_ = new WordQuestion( this ) ) != null )
			return wordQuestion_.findPossibleQuestionAndMarkAsAnswered( compoundSpecificationCollectionNr, answerSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word question module" );
		}

	protected byte writeAnswerToQuestion( boolean isNegativeAnswer, boolean isPositiveAnswer, SpecificationItem questionSpecificationItem, SpecificationItem answerSpecificationItem )
		{
		if( wordQuestion_ != null )
			return wordQuestion_.writeAnswerToQuestion( isNegativeAnswer, isPositiveAnswer, false, questionSpecificationItem, answerSpecificationItem );

		return setErrorInWord( 1, null, "The word question module isn't created yet" );
		}

	protected SpecificationResultType findQuestionToAdjustedByCompoundCollection( boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordQuestion_ != null )
			return wordQuestion_.findQuestionToAdjustedByCompoundCollection( isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		specificationResult.result = setErrorInWord( 1, null, "The word question module isn't created yet" );
		return specificationResult;
		}


	// Protected selection methods

	protected byte checkSelectionForUsageInWord( SelectionItem unusedSelectionItem )
		{
		if( CommonVariables.adminScoreList != null )
			{
			if( CommonVariables.adminScoreList.checkSelectionItemForUsage( unusedSelectionItem ) != Constants.RESULT_OK )
				addErrorInWord( 1, null, "The admin score list isn't created yet" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminConditionList != null )
			{
			if( CommonVariables.adminConditionList.checkSelectionItemForUsage( unusedSelectionItem ) != Constants.RESULT_OK )
				addErrorInWord( 1, null, "The admin condition list isn't created yet" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminActionList != null )
			{
			if( CommonVariables.adminActionList.checkSelectionItemForUsage( unusedSelectionItem ) != Constants.RESULT_OK )
				addErrorInWord( 1, null, "The admin action list isn't created yet" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.adminAlternativeList != null )
			{
			if( CommonVariables.adminAlternativeList.checkSelectionItemForUsage( unusedSelectionItem ) != Constants.RESULT_OK )
				addErrorInWord( 1, null, "The admin alternative list isn't created yet" );
			}

		return CommonVariables.result;
		}


	// Protected specification methods

	protected void clearLastCheckedAssumptionLevelItemNrInWord()
		{
		if( assignmentList != null )
			{
			assignmentList.clearLastCheckedAssumptionLevelItemNr( false, false );
			assignmentList.clearLastCheckedAssumptionLevelItemNr( true, false );
			assignmentList.clearLastCheckedAssumptionLevelItemNr( false, true );
			}

		if( specificationList != null )
			specificationList.clearLastCheckedAssumptionLevelItemNr( false, false );
		}

	protected void clearHasConfirmedAssumption()
		{
		if( wordSpecification_ != null )
			wordSpecification_.clearHasConfirmedAssumption();
		}

	protected void clearLastShownConflictSpecification()
		{
		if( wordSpecification_ != null )
			wordSpecification_.clearLastShownConflictSpecification();
		}

	protected boolean addSuggestiveQuestionAssumption()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.addSuggestiveQuestionAssumption();

		return false;
		}

	protected boolean isConfirmedAssumption()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.isConfirmedAssumption();

		return false;
		}

	protected boolean hasPossessiveSpecificationButNotAQuestion()
		{
		if( specificationList != null )
			return specificationList.hasPossessiveSpecificationItemButNotAQuestion();

		return false;
		}

	protected boolean isAuthorizedForChanges( String changeKey )
		{
		return ( changeKey_ == null ||
				changeKey_ == changeKey );
		}

	protected boolean isCorrectedAssumption()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.isCorrectedAssumption();

		return false;
		}

	protected boolean isCorrectedAssumptionByKnowledge()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.isCorrectedAssumptionByKnowledge();

		return false;
		}

	protected boolean isCorrectedAssumptionByOppositeQuestion()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.isCorrectedAssumptionByOppositeQuestion();

		return false;
		}

	protected byte archiveOrDeleteSpecification( SpecificationItem oldSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		if( oldSpecificationItem != null )
			{
			if( oldSpecificationItem.isAssignment() )
				{
				if( assignmentList != null )
					return assignmentList.archiveOrDeleteSpecificationItem( oldSpecificationItem, replacingSpecificationItem );

				return setErrorInWord( 1, null, "The assignment list isn't created yet" );
				}
			else
				{
				if( specificationList != null )
					return specificationList.archiveOrDeleteSpecificationItem( oldSpecificationItem, replacingSpecificationItem );

				return setErrorInWord( 1, null, "The specification list isn't created yet" );
				}
			}
		else
			return setErrorInWord( 1, null, "The given old specification item is undefined" );
		}

	protected byte checkForSpecificationConflict( boolean skipCompoundRelatedConflict, boolean isExclusive, boolean isNegative, boolean isPossessive, short specificationWordTypeNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		if( wordSpecification_ != null )
			return wordSpecification_.checkForSpecificationConflict( skipCompoundRelatedConflict, isExclusive, isNegative, isPossessive, specificationWordTypeNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString );

		return setErrorInWord( 1, null, "The word specification module isn't created yet" );
		}

	protected byte checkJustificationForUsageInWord( JustificationItem unusedJustificationItem )
		{
		if( assignmentList != null &&
		assignmentList.checkJustificationItemForUsage( false, false, unusedJustificationItem ) == Constants.RESULT_OK )
			{
			if( assignmentList.checkJustificationItemForUsage( true, false, unusedJustificationItem ) == Constants.RESULT_OK )
				assignmentList.checkJustificationItemForUsage( false, true, unusedJustificationItem );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			specificationList.checkJustificationItemForUsage( false, false, unusedJustificationItem );

		if( CommonVariables.result == Constants.RESULT_OK &&
		justificationList != null )
			justificationList.checkJustificationItemForUsage( unusedJustificationItem );

		return CommonVariables.result;
		}

	protected byte collectGeneralizationAndSpecifications( boolean isExclusiveGeneralization, boolean isGeneralizationCollection, boolean isQuestion, int collectionNr )
		{
		if( assignmentList != null &&
		assignmentList.collectGeneralizationAndSpecifications( false, false, isExclusiveGeneralization, isGeneralizationCollection, isQuestion, collectionNr ) == Constants.RESULT_OK )
			{
			if( assignmentList.collectGeneralizationAndSpecifications( true, false, isExclusiveGeneralization, isGeneralizationCollection, isQuestion, collectionNr ) == Constants.RESULT_OK )
				assignmentList.collectGeneralizationAndSpecifications( false, true, isExclusiveGeneralization, isGeneralizationCollection, isQuestion, collectionNr );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			return specificationList.collectGeneralizationAndSpecifications( false, false, isExclusiveGeneralization, isGeneralizationCollection, isQuestion, collectionNr );

		return CommonVariables.result;
		}

	protected byte confirmSpecificationButNotRelation( SpecificationItem confirmedSpecificationItem, SpecificationItem confirmationSpecificationItem )
		{
		if( assignmentList != null )
			{
			if( assignmentList.confirmSpecificationButNotRelation( false, false, confirmedSpecificationItem, confirmationSpecificationItem ) == Constants.RESULT_OK )
				{
				if( assignmentList.confirmSpecificationButNotRelation( true, false, confirmedSpecificationItem, confirmationSpecificationItem ) == Constants.RESULT_OK )
					assignmentList.confirmSpecificationButNotRelation( false, true, confirmedSpecificationItem, confirmationSpecificationItem );
				}
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			specificationList.confirmSpecificationButNotRelation( false, false, confirmedSpecificationItem, confirmationSpecificationItem );

		return CommonVariables.result;
		}

	protected byte recalculateAssumptionsInWord()
		{
		if( assignmentList != null &&
		specificationList.recalculateAssumptions( false, false ) == Constants.RESULT_OK )
			{
			if( specificationList.recalculateAssumptions( true, false ) == Constants.RESULT_OK )
				specificationList.recalculateAssumptions( false, true );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			return specificationList.recalculateAssumptions( false, false );

		return CommonVariables.result;
		}

	protected byte recalculateAssumptionsOfInvolvedWords()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.recalculateAssumptionsOfInvolvedWords();

		return setErrorInWord( 1, null, "The word specification module isn't created yet" );
		}

	protected byte replaceOlderSpecifications( int oldRelationContextNr, SpecificationItem replacingSpecificationItem )
		{
		if( specificationList != null )
			return specificationList.replaceOlderSpecificationItems( oldRelationContextNr, replacingSpecificationItem );

		return setErrorInWord( 1, null, "The specification list isn't created yet" );
		}

	protected byte updateJustificationInSpecifications( boolean isExclusive, boolean isExclusiveGeneralization, JustificationItem oldJustificationItem, JustificationItem replacingJustificationItem )
		{
		if( assignmentList != null )
			{
			if( assignmentList.updateJustificationInSpecificationItems( isExclusive, isExclusiveGeneralization, false, false, oldJustificationItem, replacingJustificationItem ) == Constants.RESULT_OK )
				{
				if( assignmentList.updateJustificationInSpecificationItems( isExclusive, isExclusiveGeneralization, true, false, oldJustificationItem, replacingJustificationItem ) == Constants.RESULT_OK )
					assignmentList.updateJustificationInSpecificationItems( isExclusive, isExclusiveGeneralization, false, true, oldJustificationItem, replacingJustificationItem );
				}
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationList != null )
			return specificationList.updateJustificationInSpecificationItems( isExclusive, isExclusiveGeneralization, false, false, oldJustificationItem, replacingJustificationItem );

		return CommonVariables.result;
		}

	protected byte updateSpecificationsInJustificationOfInvolvedWords( SpecificationItem oldSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		if( wordSpecification_ != null )
			return wordSpecification_.updateSpecificationsInJustificationOfInvolvedWords( oldSpecificationItem, replacingSpecificationItem );

		return setErrorInWord( 1, null, "The word specification module isn't created yet" );
		}

	protected SpecificationResultType addSpecificationInWord( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString, String changeKey )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( isAuthorizedForChanges( changeKey ) )
			{
			if( specificationWordItem == null ||
			specificationWordItem.isAuthorizedForChanges( changeKey ) )
				{
				if( wordSpecification_ != null ||
				( wordSpecification_ = new WordSpecification( this ) ) != null )
					return wordSpecification_.addSpecification( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, specificationWordItem, relationWordItem, specificationString );

				setErrorInWord( 1, null, "I failed to create my word specification module" );
				}
			else
				setErrorInWord( 1, null, "You are not authorized to add the given specification" );
			}
		else
			setErrorInWord( 1, null, "You are not authorized to add this word" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType createSpecification( boolean isAnsweredQuestion, boolean isConditional, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSpecificationGeneralization, boolean isValueSpecification, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordSpecification_ != null )
			return wordSpecification_.createSpecification( isAnsweredQuestion, isConditional, isConcludedAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isSpecificationGeneralization, isValueSpecification, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString );

		specificationResult.result = setErrorInWord( 1, null, "The word specification module isn't created yet" );
		return specificationResult;
		}

	protected SpecificationResultType findRelatedSpecification( boolean checkRelationContext, SpecificationItem searchSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordSpecification_ != null )
			return wordSpecification_.findRelatedSpecification( checkRelationContext, searchSpecificationItem );

		specificationResult.result = setErrorInWord( 1, null, "The word specification module isn't created yet" );
		return specificationResult;
		}

	protected SpecificationResultType findRelatedSpecification( boolean includeAssignments, boolean includingDeactiveAssignments, boolean isExclusive, boolean isPossessive, short questionParameter, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( wordSpecification_ != null )
			return wordSpecification_.findRelatedSpecification( false, false, false, includeAssignments, includingDeactiveAssignments, isExclusive, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString );

		specificationResult.result = setErrorInWord( 1, null, "The word specification module isn't created yet" );
		return specificationResult;
		}

	protected SpecificationResultType findSpecification( boolean includeAssignments, boolean includingDeactiveAssignments, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, WordItem relationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( includeAssignments &&
		assignmentList != null )
			specificationResult = assignmentList.findSpecificationItem( includingDeactiveAssignments, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, relationWordItem );

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificationResult.foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.findSpecificationItem( false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, relationWordItem );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationItem firstActiveQuestionSpecification()
		{
		if( specificationList != null )
			return specificationList.firstActiveSpecificationItem( false, true );

		return null;
		}

	protected SpecificationItem firstAssignmentOrSpecification( boolean isPossessive, short questionParameter, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;

		if( assignmentList != null )
			foundSpecificationItem = assignmentList.firstActiveSpecificationItem( isPossessive, questionParameter, relationContextNr, specificationWordItem );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstActiveSpecificationItem( isPossessive, questionParameter, relationContextNr, specificationWordItem );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssignmentOrSpecification( boolean includeAssignments, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeAssignments &&
		assignmentList != null )
			foundSpecificationItem = assignmentList.firstSpecificationItem( false, false, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, null );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstSpecificationItem( false, false, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, null );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssignmentOrSpecification( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeAssignments, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeAssignments &&
		assignmentList != null )
			foundSpecificationItem = assignmentList.firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssignmentOrSpecification( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeAssignments, boolean includeDeactiveAssignments, boolean includeArchiveAssignments, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeAssignments &&
		assignmentList != null )
			foundSpecificationItem = assignmentList.findSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, includeDeactiveAssignments, includeArchiveAssignments, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( CommonVariables.result == Constants.RESULT_OK &&
		foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.findSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssignmentOrSpecification( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeAssignments, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeAssignments &&
		assignmentList != null )
			foundSpecificationItem = assignmentList.firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, includeDeactiveItems, includeArchiveItems, isNegative, isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssumptionSpecification( boolean includeAssignments, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeAssignments &&
		assignmentList != null )
			foundSpecificationItem = assignmentList.firstAssumptionSpecificationItem( false, false, true, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstAssumptionSpecificationItem( false, false, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstSelectedSpecification( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isQuestion )
		{
		return ( isAssignment ? firstAssignment( isDeactiveAssignment, isArchiveAssignment, isQuestion ) :
				( isQuestion ? firstActiveQuestionSpecification() : firstSpecificationButNotAQuestion() ) );
		}

	protected SpecificationItem firstSelectedSpecification( boolean includeAnsweredQuestions, boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, short questionParameter )
		{
		return ( includeAnsweredQuestions && questionParameter > Constants.NO_QUESTION_PARAMETER ? firstAnsweredQuestion( isAssignment, isDeactiveAssignment, isArchiveAssignment, questionParameter ) :
				( isAssignment ? ( isArchiveAssignment ? firstArchiveAssignment( includeAnsweredQuestions, questionParameter ) : ( isDeactiveAssignment ? firstDeactiveAssignment( includeAnsweredQuestions, questionParameter ) : firstActiveAssignment( includeAnsweredQuestions, questionParameter ) ) ) :
				( questionParameter == Constants.NO_QUESTION_PARAMETER ? firstSpecificationButNotAQuestion() : firstQuestionSpecification( includeAnsweredQuestions, questionParameter ) ) ) );
		}

	protected SpecificationItem firstSpecification( boolean includeAnsweredQuestions, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		if( specificationList != null )
			return specificationList.firstSpecificationItem( true, includeAnsweredQuestions, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return null;
		}

	protected SpecificationItem firstSpecificationButNotAQuestion()
		{
		if( specificationList != null )
			return specificationList.firstActiveSpecificationItem( false, false );

		return null;
		}

	protected SpecificationItem firstAssignmentOrSpecificationButNotAQuestion( boolean allowEmptyContextResult, boolean includeActiveAssignments, boolean includeDeactiveAssignments, boolean includeArchiveAssignments, boolean isNegative, boolean isPossessive, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;

		if( assignmentList != null )
			foundSpecificationItem = assignmentList.firstSpecificationItem( allowEmptyContextResult, false, includeActiveAssignments, includeDeactiveAssignments, includeArchiveAssignments, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstSpecificationItem( allowEmptyContextResult, false, true, false, false, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstUserSpecification( boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem = null;

		if( assignmentList != null )
			foundSpecificationItem = assignmentList.firstUserSpecificationItem( true, true, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( foundSpecificationItem == null &&
		specificationList != null )
			return specificationList.firstUserSpecificationItem( true, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundSpecificationItem;
		}

	protected SpecificationItem correctedSuggestiveQuestionAssumptionSpecificationItem()
		{
		if( wordSpecification_ != null )
			return wordSpecification_.correctedSuggestiveQuestionAssumptionSpecificationItem();

		return null;
		}


	// Protected word type methods

	protected void clearWriteLevel( short currentWriteLevel )
		{
		if( wordTypeList != null )
			wordTypeList.clearWriteLevel( currentWriteLevel );
		}

	protected boolean isNoun()
		{
		return ( isSingularNoun() ||
				isPluralNoun() );
		}

	protected boolean isSingularNoun()
		{
		if( wordTypeList != null )
			return ( wordTypeList.activeWordTypeItem( Constants.WORD_TYPE_NOUN_SINGULAR ) != null );

		return false;
		}

	protected boolean isPluralNoun()
		{
		if( wordTypeList != null )
			return ( wordTypeList.activeWordTypeItem( Constants.WORD_TYPE_NOUN_PLURAL ) != null );

		return false;
		}

	protected boolean isPropername()
		{
		if( wordTypeList != null )
			return ( wordTypeList.activeWordTypeItem( Constants.WORD_TYPE_PROPER_NAME ) != null );

		return false;
		}

	protected boolean isPropernamePrecededByDefiniteArticle()
		{
		WordTypeItem foundWordTypeItem;

		if( wordTypeList != null &&
		( foundWordTypeItem = wordTypeList.activeWordTypeItem( Constants.WORD_TYPE_PROPER_NAME ) ) != null )
			return foundWordTypeItem.isPropernamePrecededByDefiniteArticle();

		return false;
		}

	protected boolean isNumeral()
		{
		if( wordTypeList != null )
			return ( wordTypeList.activeWordTypeItem( Constants.WORD_TYPE_NUMERAL ) != null );

		return false;
		}

	protected boolean checkHiddenWordType( short wordTypeNr, String compareString, String hideKey )
		{
		if( wordTypeList != null )
			return wordTypeList.checkHiddenWordType( wordTypeNr, compareString, hideKey );

		return false;
		}

	protected byte createWordType( boolean isDefiniteArticle, boolean isPropernamePrecededByDefiniteArticle, short wordTypeNr, int wordLength, String wordTypeString )
		{
		if( wordType_ == null &&
		( wordType_ = new WordType( this ) ) == null )
			return setErrorInWord( 1, null, "I failed to create my word type module" );

		return wordType_.createWordType( isDefiniteArticle, isPropernamePrecededByDefiniteArticle, wordTypeNr, wordLength, wordTypeString );
		}

	protected byte hideWordType( short wordTypeNr, String authorizationKey )
		{
		if( wordTypeList != null )
			return wordTypeList.hideWordTypeItem( wordTypeNr, authorizationKey );

		return CommonVariables.result;
		}

	protected byte deleteWordType( short wordTypeNr )
		{
		if( wordTypeList != null )
			return wordTypeList.deleteWordType( wordTypeNr );

		return CommonVariables.result;
		}

	protected byte markWordTypeAsWritten( short wordTypeNr )
		{
		if( wordTypeList != null )
			return wordTypeList.markWordTypeAsWritten( isPredefinedWord(), wordTypeNr );

		return setErrorInWord( 1, null, "The word type list isn't created yet" );
		}

	protected WordResultType checkWordTypeForBeenWritten( short wordTypeNr )
		{
		WordResultType wordResult = new WordResultType();

		if( wordTypeList != null )
			return wordTypeList.checkWordTypeForBeenWritten( isPredefinedWord(), wordTypeNr );

		wordResult.result = setErrorInWord( 1, null, "The word type list isn't created yet" );
		return wordResult;
		}

	protected WordResultType findWordType( boolean checkAllLanguages, boolean firstLetterHasDifferentCase, short searchWordTypeNr, int searchWordStringLength, String searchWordString )
		{
		WordResultType wordResult = new WordResultType();

		if( wordType_ != null )
			return wordType_.findWordType( checkAllLanguages, firstLetterHasDifferentCase, searchWordTypeNr, searchWordStringLength, searchWordString );

		wordResult.result = setErrorInWord( 1, null, "The word type module isn't created yet" );
		return wordResult;
		}

	protected WordResultType findWordTypeInAllWords( boolean checkAllLanguages, boolean firstLetterHasDifferentCase, short searchWordTypeNr, int searchWordStringLength, String searchWordString )
		{
		WordResultType wordResult = new WordResultType();

		if( wordType_ == null &&
		( wordType_ = new WordType( this ) ) == null )
			{
			wordResult.result = setErrorInWord( 1, null, "I failed to create my word type module" );
			return wordResult;
			}

		return wordType_.findWordTypeInAllWords( checkAllLanguages, firstLetterHasDifferentCase, searchWordTypeNr, searchWordStringLength, searchWordString );
		}

	protected String anyWordTypeString()
		{
		if( wordType_ != null )
			return wordType_.wordTypeString( Constants.WORD_TYPE_UNDEFINED );

		return null;
		}

	protected String wordTypeString( short wordTypeNr )
		{
		if( wordType_ != null )
			return wordType_.wordTypeString( wordTypeNr );

		return null;
		}

	protected String activeSingularNounString()
		{
		return activeWordTypeString( Constants.WORD_TYPE_NOUN_SINGULAR );
		}

	protected String activeWordTypeString( short wordTypeNr )
		{
		if( wordTypeList != null )
			return wordTypeList.activeWordTypeString( wordTypeNr );

		return null;
		}

	protected WordTypeItem activeWordTypeItem( short wordTypeNr )
		{
		if( wordTypeList != null )
			return wordTypeList.activeWordTypeItem( wordTypeNr );

		return null;
		}


	// Protected write methods

	protected byte writeJustificationSpecification( SpecificationItem justificationSpecificationItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeJustificationSpecification( justificationSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}

	protected byte writeSelectedSpecification( boolean writeGivenSpecificationWordOnly, SpecificationItem writeSpecificationItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeSelectedSpecification( true, true, false, writeGivenSpecificationWordOnly, Constants.NO_ANSWER_PARAMETER, writeSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}

	protected byte writeSelectedSpecification( boolean forceResponseNotBeingAssignment, boolean forceResponseNotBeingFirstSpecification, boolean writeCurrentSentenceOnly, boolean writeGivenSpecificationWordOnly, short answerParameter, SpecificationItem writeSpecificationItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeSelectedSpecification( forceResponseNotBeingAssignment, forceResponseNotBeingFirstSpecification, writeCurrentSentenceOnly, writeGivenSpecificationWordOnly, answerParameter, writeSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}

	protected byte writeSelectedSpecificationInfo( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isQuestion, WordItem writeWordItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeSelectedSpecificationInfo( isAssignment, isDeactiveAssignment, isArchiveAssignment, isQuestion, writeWordItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}

	protected byte writeSelectedRelationInfo( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isQuestion, WordItem writeWordItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeSelectedRelationInfo( isAssignment, isDeactiveAssignment, isArchiveAssignment, isQuestion, writeWordItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}

	protected byte writeSpecification( boolean isAdjustedSpecification, boolean isCorrectedAssumptionByKnowledge, boolean isCorrectedAssumptionByOppositeQuestion, SpecificationItem writeSpecificationItem )
		{
		if( wordWrite_ != null ||
		( wordWrite_ = new WordWrite( this ) ) != null )
			return wordWrite_.writeSpecification( isAdjustedSpecification, isCorrectedAssumptionByKnowledge, isCorrectedAssumptionByOppositeQuestion, writeSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word write module" );
		}


	// Protected write sentence methods

	protected byte parseGrammarToWriteSentence( boolean writeGivenSpecificationWordOnly, short answerParameter, short grammarLevel, GrammarItem parseGrammarItem, SpecificationItem writeSpecificationItem )
		{
		if( wordWriteSentence_ != null ||
		( wordWriteSentence_ = new WordWriteSentence( this ) ) != null )
			return wordWriteSentence_.parseGrammarToWriteSentence( writeGivenSpecificationWordOnly, answerParameter, grammarLevel, parseGrammarItem, writeSpecificationItem );

		return setErrorInWord( 1, null, "I failed to create my word write sentence module" );
		}


	// Protected write words methods

	protected void initializeWordWriteWordsVariables()
		{
		if( wordWriteWords_ != null )
			wordWriteWords_.initializeWordWriteWordsVariables();
		}

	protected void initializeWordWriteWordsSpecificationVariables( int startWordPosition )
		{
		if( wordWriteWords_ != null )
			wordWriteWords_.initializeWordWriteWordsSpecificationVariables( startWordPosition );
		}

	protected WriteResultType writeWordsToSentence( boolean writeGivenSpecificationWordOnly, short answerParameter, GrammarItem definitionGrammarItem, SpecificationItem writeSpecificationItem )
		{
		WriteResultType writeResult = new WriteResultType();

		if( wordWriteWords_ != null ||
		( wordWriteWords_ = new WordWriteWords( this ) ) != null )
			return wordWriteWords_.writeWordsToSentence( writeGivenSpecificationWordOnly, answerParameter, definitionGrammarItem, writeSpecificationItem );

		writeResult.result = setErrorInWord( 1, null, "I failed to create my word write words module" );
		return writeResult;
		}
	};

/*************************************************************************
 *
 *	"Fear of the Lord is the foundation of true wisdom.
 *	All who obey his commandments will grow in wisdom." (Psalm 111:10)
 *
 *************************************************************************/
