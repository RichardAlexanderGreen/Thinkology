/*
 *	Class:			ReadItem
 *	Parent class:	Item
 *	Purpose:		To temporarily store info about the read words of a sentence
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

class ReadItem extends Item
	{
	// Private loadable variables

	private short wordTypeLanguageNr_;
	private short wordParameter_;
	private short wordTypeNr_;

	private int wordPosition_;

	private WordItem readWordItem_;


	// Protected constructible variables

	protected boolean isMarkedBySetGrammarParameter;
	protected boolean isUnusedReadItem;
	protected boolean isWordPassingGrammarIntegrityCheck;

	protected short grammarParameter;

	protected GrammarItem definitionGrammarItem;


	// Protected loadable variables

	protected String readString;


	// Constructor

	protected ReadItem( short wordTypeLanguageNr, short wordParameter, short wordTypeNr, int wordPosition, int readStringLength, String _readString, WordItem readWordItem, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );


		// Private loadable variables

		wordTypeLanguageNr_ = wordTypeLanguageNr;
		wordParameter_ = wordParameter;
		wordTypeNr_ = wordTypeNr;

		wordPosition_ = wordPosition;

		readWordItem_ = readWordItem;


		// Protected constructible variables

		isMarkedBySetGrammarParameter = false;
		isUnusedReadItem = false;
		isWordPassingGrammarIntegrityCheck = false;

		grammarParameter = Constants.NO_GRAMMAR_PARAMETER;

		definitionGrammarItem = null;


		// Protected loadable variables

		readString = null;

		if( _readString != null )
			{
			if( ( readString = new String( _readString.substring( 0, readStringLength ) ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create the read string" );
			}
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( readString != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( readString );
			}
		}

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( readWordItem_ != null &&
		( wordString = readWordItem_.wordTypeString( wordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( wordPosition_ == queryParameter ||
				grammarParameter == queryParameter ||
				wordParameter_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&

				( wordPosition_ > 0 ||
				grammarParameter > Constants.NO_GRAMMAR_PARAMETER ||
				wordParameter_ > Constants.NO_WORD_PARAMETER ) ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( ( readWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : readWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : readWordItem_.itemNr() == queryItemNr ) ) ||

				( definitionGrammarItem == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : definitionGrammarItem.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : definitionGrammarItem.itemNr() == queryItemNr ) ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( wordTypeNr_ == queryWordTypeNr );
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// 1) Descending creationSentenceNr
				creationSentenceNr() > nextSortItem.creationSentenceNr() ||

				// 2) Ascending wordPosition
				( creationSentenceNr() == nextSortItem.creationSentenceNr() &&
				( wordPosition_ < ( (ReadItem)nextSortItem ).wordPosition_ ||

				// 3) Descending wordTypeNr
				( wordPosition_ == ( (ReadItem)nextSortItem ).wordPosition_ &&
				wordTypeNr_ > ( (ReadItem)nextSortItem ).wordTypeNr_ ) ) ) );
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( readWordItem_ != null )
			{
			if( readWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, readString, "I failed to find the word reference for the word reference query" );
			}

		return CommonVariables.result;
		}

	protected byte findWordReference( WordItem referenceWordItem )
		{
		CommonVariables.foundWordReference = false;

		if( referenceWordItem != null )
			{
			if( readWordItem_ == referenceWordItem )
				CommonVariables.foundWordReference = true;
			}
		else
			return setErrorInItem( 1, null, readString, "The given reference word is undefined" );

		return CommonVariables.result;
		}

	protected String itemString()
		{
		return readString;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String languageNameString = myWord().grammarLanguageName( wordTypeLanguageNr_ );
		String readWordTypeString = myWord().wordTypeName( wordTypeNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( isUnusedReadItem )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isUnusedReadItem" );

		if( isWordPassingGrammarIntegrityCheck )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isWordPassingGrammarIntegrityCheck" );

		if( isMarkedBySetGrammarParameter )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isMarkedBySetGrammarParameter" );

		if( wordTypeLanguageNr_ > Constants.NO_LANGUAGE_NR )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "language:" + ( languageNameString == null ? wordTypeLanguageNr_ : languageNameString ) );
			}

		if( wordParameter_ > Constants.NO_WORD_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wordParameter:" + wordParameter_ );

		if( grammarParameter > Constants.NO_GRAMMAR_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "grammarParameter:" + grammarParameter );

		if( wordPosition_ > 0 )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wordPosition:" + wordPosition_ );

		if( readString != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "readString:" + readString );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "readWordType:" + ( readWordTypeString == null ? Constants.EMPTY_STRING : readWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + wordTypeNr_ );

		if( readWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "readWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + readWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + readWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = readWordItem_.wordTypeString( wordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( definitionGrammarItem != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "definitionGrammar" + Constants.QUERY_REF_ITEM_START_CHAR + definitionGrammarItem.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + definitionGrammarItem.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isReadWordSymbol()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_SYMBOL );
		}

	protected boolean isReadWordNumeral()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NUMERAL );
		}

	protected boolean isReadWordNoun()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR ||
				wordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isReadWordArticle()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_ARTICLE );
		}

	protected boolean isReadWordSingularNoun()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR );
		}

	protected boolean isReadWordPluralNoun()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isReadWordPronoun()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_PERSONAL_PRONOUN_SINGULAR_2 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_2 ||
				wordTypeNr_ == Constants.WORD_TYPE_PERSONAL_PRONOUN_PLURAL_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_PERSONAL_PRONOUN_PLURAL_2 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_2 );
		}

	protected boolean isReadWordPossessivePronoun1()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_1 );
		}

	protected boolean isReadWordVerb()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_VERB_SINGULAR ||
				wordTypeNr_ == Constants.WORD_TYPE_VERB_PLURAL );
		}

	protected boolean isReadWordText()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_TEXT );
		}

	protected boolean isAdjectiveAssigned()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_ASSIGNED );
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

	protected boolean isAdjectivePrevious()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_PREVIOUS );
		}

	protected boolean isPreposition()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_ABOUT ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_FOR ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_FROM ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_IN ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_OF ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_TO );
		}

	protected boolean isVirtualListPreposition()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_FROM ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_OF ||
				wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_TO );
		}

	protected boolean isPrepositionIn()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_PREPOSITION_IN );
		}

	protected boolean isNegative()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_ADJECTIVE_NO ||
				wordParameter_ == Constants.WORD_PARAMETER_ADVERB_NOT );
//				wordParameter_ == Constants.WORD_PARAMETER_ADVERB_DO_NOT );
		}

	protected boolean isNounJustificationReport()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_JUSTIFICATION_REPORT );
		}

	protected boolean isNounValue()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_VALUE );
		}

	protected boolean isNounFile()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_NOUN_FILE );
		}

	protected boolean isSeparator()
		{
		return ( wordParameter_ == Constants.WORD_PARAMETER_SYMBOL_COMMA ||
				wordParameter_ == Constants.WORD_PARAMETER_SYMBOL_COLON ||
				wordParameter_ == Constants.WORD_PARAMETER_SYMBOL_EXCLAMATION_MARK ||
				wordParameter_ == Constants.WORD_PARAMETER_SYMBOL_QUESTION_MARK );
		}

	protected boolean isUserDefined()
		{
		return ( wordParameter_ == Constants.NO_WORD_PARAMETER );
		}

	protected boolean isSelection()
		{
		return ( grammarParameter == Constants.GRAMMAR_SELECTION );
		}

	protected boolean isImperative()
		{
		return ( grammarParameter == Constants.GRAMMAR_IMPERATIVE );
		}

	protected boolean isGeneralizationWord()
		{
		return ( grammarParameter == Constants.GRAMMAR_GENERALIZATION_WORD );
		}

	protected boolean isSpecificationWord()
		{
		return ( grammarParameter == Constants.GRAMMAR_SPECIFICATION_WORD );
		}

	protected boolean isRelationWord()
		{
		return ( wordTypeNr_ != Constants.WORD_TYPE_ARTICLE &&	// To avoid triggering on the article before a propername-preceded-by-defined-article
				grammarParameter == Constants.GRAMMAR_RELATION_WORD );
		}

	protected boolean isGeneralizationPart()
		{
		return ( grammarParameter == Constants.GRAMMAR_GENERALIZATION_PART ||
				grammarParameter == Constants.GRAMMAR_GENERALIZATION_ASSIGNMENT );
		}

	protected boolean isGeneralizationSpecification()
		{
		return ( grammarParameter == Constants.GRAMMAR_GENERALIZATION_SPECIFICATION );
		}

	protected boolean isLinkedGeneralizationConjunction()
		{
		return ( grammarParameter == Constants.GRAMMAR_LINKED_GENERALIZATION_CONJUNCTION );
		}

	protected boolean isSentenceConjunction()
		{
		return ( grammarParameter == Constants.GRAMMAR_SENTENCE_CONJUNCTION );
		}

	protected boolean isVerb()
		{
		return ( grammarParameter == Constants.GRAMMAR_VERB );
		}

	protected boolean isQuestionVerb()
		{
		return ( grammarParameter == Constants.GRAMMAR_QUESTION_VERB );
		}

	protected boolean foundRelationWordInThisList( WordItem relationWordItem )
		{
		ReadItem searchItem = this;

		if( relationWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.isRelationWord() &&
				searchItem.readWordItem() == relationWordItem )
					return true;

				searchItem = searchItem.nextCurrentLanguageReadItem();
				}
			}

		return false;
		}

	protected short wordTypeLanguageNr()
		{
		return wordTypeLanguageNr_;
		}

	protected short wordParameter()
		{
		return wordParameter_;
		}

	protected short wordTypeNr()
		{
		return wordTypeNr_;
		}

	protected int wordPosition()
		{
		return wordPosition_;
		}

	protected String readWordTypeString()
		{
		if( readWordItem_ != null )
			return readWordItem_.activeWordTypeString( wordTypeNr_ );

		return null;
		}

	protected ReadItem nextReadItem()
		{
		return (ReadItem)nextItem;
		}

	protected ReadItem nextCurrentLanguageReadItem()
		{
		ReadItem nextCurrentLanguageReadItem = nextReadItem();

		return ( nextCurrentLanguageReadItem != null &&
				nextCurrentLanguageReadItem.wordTypeLanguageNr() == CommonVariables.currentGrammarLanguageNr ? nextCurrentLanguageReadItem : null );
		}

	protected ReadItem getFirstRelationWordReadItem()
		{
		ReadItem searchItem = this;

		while( searchItem != null )
			{
			if( searchItem.isRelationWord() )
				return searchItem;

			searchItem = searchItem.nextCurrentLanguageReadItem();
			}

		return null;
		}

	protected WordItem readWordItem()
		{
		return readWordItem_;
		}

	protected WordTypeItem readWordTypeItem()
		{
		return ( readWordItem_ == null ? null : readWordItem_.activeWordTypeItem( wordTypeNr_ ) );
		}
	};

/*************************************************************************
 *
 *	"The godly will see these things and be glad,
 *	while the wicked are struck in silent." (Psalm 107:42)
 *
 *************************************************************************/
