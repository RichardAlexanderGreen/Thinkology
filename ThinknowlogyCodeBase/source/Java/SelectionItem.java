/*
 *	Class:			SelectionItem
 *	Parent class:	Item
 *	Purpose:		To store the selection structure
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

class SelectionItem extends Item
	{
	// Private constructible variables

	private SelectionItem nextExecutionItem_;


	// Private loadable variables

	private boolean isAction_;
	private boolean isAssignedOrClear_;
	private boolean isDeactive_;
	private boolean isArchive_;
	private boolean isFirstComparisonPart_;
	private boolean isNewStart_;
	private boolean isNegative_;
	private boolean isPossessive_;
	private boolean isValueSpecification_;

	private short selectionLevel_;
	private short imperativeParameter_;
	private short prepositionParameter_;
	private short specificationWordParameter_;

	private short generalizationWordTypeNr_;
	private short specificationWordTypeNr_;
	private short relationWordTypeNr_;

	private int generalizationContextNr_;
	private int specificationContextNr_;
	private int relationContextNr_;

	private int nContextRelations_;

	private WordItem generalizationWordItem_;
	private WordItem specificationWordItem_;
	private WordItem relationWordItem_;

	private String specificationString_;


	// Protected constructible variables

	protected boolean isConditionCheckedForSolving;


	// Constructor

	protected SelectionItem( boolean isAction, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isFirstComparisonPart, boolean isNewStart, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short selectionLevel, short imperativeParameter, short prepositionParameter, short specificationWordParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private constructible variables

		nextExecutionItem_ = null;

		// Private loadable variables

		isAction_ = isAction;
		isAssignedOrClear_ = isAssignedOrClear;
		isNewStart_ = isNewStart;

		isDeactive_ = isDeactive;
		isArchive_ = isArchive;

		isFirstComparisonPart_ = isFirstComparisonPart;

		isNegative_ = isNegative;
		isPossessive_ = isPossessive;
		isValueSpecification_ = isValueSpecification;

		selectionLevel_ = selectionLevel;

		imperativeParameter_ = imperativeParameter;
		prepositionParameter_ = prepositionParameter;
		specificationWordParameter_ = specificationWordParameter;

		generalizationWordTypeNr_ = generalizationWordTypeNr;
		specificationWordTypeNr_ = specificationWordTypeNr;
		relationWordTypeNr_ = relationWordTypeNr;

		generalizationContextNr_ = generalizationContextNr;
		specificationContextNr_ = specificationContextNr;
		relationContextNr_ = relationContextNr;

		nContextRelations_ = nContextRelations;

		specificationString_ = null;

		generalizationWordItem_ = generalizationWordItem;
		specificationWordItem_ = specificationWordItem;
		relationWordItem_ = relationWordItem;


		// Protected constructible variables

		isConditionCheckedForSolving = false;

		if( specificationString != null )
			{
			if( ( specificationString_ = new String( specificationString ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create a specification string" );
			}
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( specificationString_ != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( specificationString_ );
			}
		}

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( generalizationWordItem_ != null &&
		( wordString = generalizationWordItem_.wordTypeString( generalizationWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}

		if( specificationWordItem_ != null &&
		( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery ||
			CommonVariables.queryStringBuffer.length() > 0 )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}

		if( relationWordItem_ != null &&
		( wordString = relationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery ||
			CommonVariables.queryStringBuffer.length() > 0 )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( selectionLevel_ == queryParameter ||
				imperativeParameter_ == queryParameter ||
				prepositionParameter_ == queryParameter ||
				specificationWordParameter_ == queryParameter ||
				generalizationContextNr_ == queryParameter ||
				specificationContextNr_ == queryParameter ||
				relationContextNr_ == queryParameter ||
				nContextRelations_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				( selectionLevel_ > Constants.NO_SELECTION_LEVEL ||
				imperativeParameter_ > Constants.NO_IMPERATIVE_PARAMETER ||
				prepositionParameter_ > Constants.NO_PREPOSITION_PARAMETER ||
				specificationWordParameter_ > Constants.NO_WORD_PARAMETER ||
				generalizationContextNr_ > Constants.NO_CONTEXT_NR ||
				specificationContextNr_ > Constants.NO_CONTEXT_NR ||
				relationContextNr_ > Constants.NO_CONTEXT_NR ||
				nContextRelations_ > 0 ) ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( ( generalizationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : generalizationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : generalizationWordItem_.itemNr() == queryItemNr ) ) ||

				( specificationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : specificationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : specificationWordItem_.itemNr() == queryItemNr ) ) ||

				( relationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : relationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : relationWordItem_.itemNr() == queryItemNr ) ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( generalizationWordTypeNr_ == queryWordTypeNr ||
				specificationWordTypeNr_ == queryWordTypeNr ||
				relationWordTypeNr_ == queryWordTypeNr );
		}

	protected byte checkForUsage()
		{
		return myWord().checkSelectionForUsageInWord( this );
		}

	protected byte findWordReference( WordItem referenceWordItem )
		{
		CommonVariables.foundWordReference = false;

		if( referenceWordItem != null )
			{
			if( generalizationWordItem_ == referenceWordItem ||
			specificationWordItem_ == referenceWordItem ||
			relationWordItem_ == referenceWordItem )
				CommonVariables.foundWordReference = true;
			}
		else
			return setErrorInItem( 1, null, specificationString_, "The given reference word is undefined" );

		return CommonVariables.result;
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( generalizationWordItem_ != null )
			{
			if( generalizationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, specificationString_, "I failed to find a matching word reference string for the generalization word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		!CommonVariables.foundMatchingStrings &&
		specificationWordItem_ != null )
			{
			if( specificationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, specificationString_, "I failed to find a matching word reference string for the specification word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		!CommonVariables.foundMatchingStrings &&
		relationWordItem_ != null )
			{
			if( relationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, specificationString_, "I failed to find a matching word reference string for the relation word" );
			}

		return CommonVariables.result;
		}

	protected String itemString()
		{
		return specificationString_;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String generalizationWordTypeString = myWord().wordTypeName( generalizationWordTypeNr_ );
		String specificationWordTypeString = myWord().wordTypeName( specificationWordTypeNr_ );
		String relationWordTypeString = myWord().wordTypeName( relationWordTypeNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( isAction_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isAction" );

		if( isAssignedOrClear_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isAssignedOrClear" );

		if( isNewStart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isNewStart" );

		if( isDeactive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isDeactive" );

		if( isArchive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isArchive" );

		if( isFirstComparisonPart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isFirstComparisonPart" );

		if( isNegative_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isNegative" );

		if( isPossessive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isPossessive" );

		if( isValueSpecification_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isValueSpecification" );

		if( isConditionCheckedForSolving )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isConditionCheckedForSolving" );

		if( selectionLevel_ > Constants.NO_SELECTION_LEVEL )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "selectionLevel:" + selectionLevel_ );

		if( imperativeParameter_ > Constants.NO_IMPERATIVE_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "imperativeParameter:" + imperativeParameter_ );

		if( prepositionParameter_ > Constants.NO_PREPOSITION_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "prepositionParameter:" + prepositionParameter_ );

		if( specificationWordParameter_ > Constants.NO_WORD_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordParameter:" + specificationWordParameter_ );

		if( generalizationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationContextNr:" + generalizationContextNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationWordType:" + ( generalizationWordTypeString == null ? Constants.EMPTY_STRING : generalizationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + generalizationWordTypeNr_ );

		if( generalizationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationWord" + Constants.QUERY_REF_ITEM_START_CHAR + generalizationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + generalizationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = generalizationWordItem_.wordTypeString( generalizationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( specificationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationContextNr:" + specificationContextNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordType:" + ( specificationWordTypeString == null ? Constants.EMPTY_STRING : specificationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + specificationWordTypeNr_ );

		if( specificationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWord" + Constants.QUERY_REF_ITEM_START_CHAR + specificationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + specificationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( relationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "relationContextNr:" + relationContextNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "relationWordType:" + ( relationWordTypeString == null ? Constants.EMPTY_STRING : relationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + relationWordTypeNr_ );

		if( relationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "relationWord" + Constants.QUERY_REF_ITEM_START_CHAR + relationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + relationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = relationWordItem_.wordTypeString( relationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( nContextRelations_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "nContextRelations:" + nContextRelations_ );

		if( specificationString_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationString:" + specificationString_ );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isNumeralRelation()
		{
		return ( relationWordTypeNr_ == Constants.WORD_TYPE_NUMERAL );
		}

	protected boolean isAction()
		{
		return isAction_;
		}

	protected boolean isAssignedOrClear()
		{
		return isAssignedOrClear_;
		}

	protected boolean isNewStart()
		{
		return isNewStart_;
		}

	protected boolean isDeactive()
		{
		return isDeactive_;
		}

	protected boolean isArchive()
		{
		return isArchive_;
		}

	protected boolean isFirstComparisonPart()
		{
		return isFirstComparisonPart_;
		}

	protected boolean isNegative()
		{
		return isNegative_;
		}

	protected boolean isPossessive()
		{
		return isPossessive_;
		}

	protected boolean isValueSpecification()
		{
		return isValueSpecification_;
		}

	protected short selectionLevel()
		{
		return selectionLevel_;
		}

	protected short imperativeParameter()
		{
		return imperativeParameter_;
		}

	protected short prepositionParameter()
		{
		return prepositionParameter_;
		}

	protected short specificationWordParameter()
		{
		return specificationWordParameter_;
		}

	protected short specificationWordTypeNr()
		{
		return specificationWordTypeNr_;
		}

	protected short relationWordTypeNr()
		{
		return relationWordTypeNr_;
		}

	protected int generalizationContextNr()
		{
		return generalizationContextNr_;
		}

	protected int specificationContextNr()
		{
		return specificationContextNr_;
		}

	protected int relationContextNr()
		{
		return relationContextNr_;
		}

	protected int nContextRelations()
		{
		return nContextRelations_;
		}

	protected byte findNextExecutionSelectionItem( WordItem solveGeneralizationWordItem )
		{
		return findNextExecutionSelectionItem( false, solveGeneralizationWordItem );
		}

	protected byte findNextExecutionSelectionItem( boolean includeThisItem, WordItem solveWordItem )
		{
		SelectionItem searchItem = ( includeThisItem ? this : nextSelectionItem() );

		nextExecutionItem_ = null;

		if( solveWordItem != null )
			{
			while( searchItem != null &&
			nextExecutionItem_ == null )
				{
				if( searchItem.generalizationWordItem_ == solveWordItem &&
				searchItem.specificationWordItem_ != null )
					nextExecutionItem_ = searchItem;
				else
					searchItem = searchItem.nextSelectionItem();
				}
			}
		else
			return setErrorInItem( 1, null, specificationString_, "The given solve word item is undefined" );

		return CommonVariables.result;
		}

	protected String specificationString()
		{
		return specificationString_;
		}

	protected SelectionItem nextExecutionItem()
		{
		return nextExecutionItem_;
		}

	protected SelectionItem nextSelectionItem()
		{
		return (SelectionItem)nextItem;
		}

	protected SelectionItem nextConditionItem( short executionLevel, int conditionSentenceNr )
		{
		if( nextSelectionItem() != null &&
		nextSelectionItem().selectionLevel_ <= executionLevel &&
		nextSelectionItem().creationSentenceNr() == conditionSentenceNr )
			return nextSelectionItem();

		return null;
		}

	protected SelectionItem nextExecutionItem( short executionLevel, int executionSentenceNr )
		{
		if( nextSelectionItem() != null &&
		nextSelectionItem().selectionLevel_ == executionLevel &&
		nextSelectionItem().creationSentenceNr() == executionSentenceNr )
			return nextSelectionItem();

		return null;
		}

	protected WordItem generalizationWordItem()
		{
		return generalizationWordItem_;
		}

	protected WordItem specificationWordItem()
		{
		return specificationWordItem_;
		}

	protected WordItem relationWordItem()
		{
		return relationWordItem_;
		}
	};

/*************************************************************************
 *
 *	"Light shines in the darkness for the godly.
 *	They are generous, compassionate, and righteous." (Psalm 112:4)
 *
 *************************************************************************/
