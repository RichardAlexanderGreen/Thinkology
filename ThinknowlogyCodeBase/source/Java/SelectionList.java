/*
 *	Class:			SelectionList
 *	Parent class:	List
 *	Purpose:		To store selection items
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

class SelectionList extends List
	{
	// Private selection methods

	private int getLowerSentenceNr( int duplicateSentenceNr )
		{
		Item searchItem = firstActiveItem();
		int lowerSentenceNr = Constants.NO_SENTENCE_NR;

		while( searchItem != null )
			{
			if( searchItem.activeSentenceNr() < duplicateSentenceNr &&
			searchItem.activeSentenceNr() > lowerSentenceNr )
				lowerSentenceNr = searchItem.activeSentenceNr();

			searchItem = searchItem.nextItem;
			}

		return lowerSentenceNr;
		}

	private SelectionItem firstDeactiveSelectionItem()
		{
		return (SelectionItem)firstDeactiveItem();
		}


	// Constructor

	protected SelectionList( char _listChar, WordItem myWord )
		{
		initializeListVariables( _listChar, myWord );
		}


	// Protected virtual methods

	protected byte findWordReference( WordItem referenceWordItem )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		CommonVariables.foundWordReference = false;

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!CommonVariables.foundWordReference )
			{
			if( searchItem.findWordReference( referenceWordItem ) == Constants.RESULT_OK )
				searchItem = searchItem.nextSelectionItem();
			else
				addError( 1, null, "I failed to check for a reference word item in an active selection item" );
			}

		searchItem = firstDeactiveSelectionItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!CommonVariables.foundWordReference )
			{
			if( searchItem.findWordReference( referenceWordItem ) == Constants.RESULT_OK )
				searchItem = searchItem.nextSelectionItem();
			else
				addError( 1, null, "I failed to check for a reference word item in a deactive selection item" );
			}

		// Don't search in the deleted items - since this method is used for cleanup purposes

		return CommonVariables.result;
		}


	// Protected methods

	protected void clearConditionChecksForSolving( short selectionLevel, int conditionSentenceNr )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		while( searchItem != null )
			{
			if( searchItem.selectionLevel() < selectionLevel &&
			searchItem.activeSentenceNr() == conditionSentenceNr )
				searchItem.isConditionCheckedForSolving = false;

			searchItem = searchItem.nextSelectionItem();
			}
		}

	protected byte checkSelectionItemForUsage( SelectionItem unusedSelectionItem )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		if( unusedSelectionItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.nextExecutionItem() == unusedSelectionItem )
					return setError( 1, null, "The reference selection item is still in use" );

				searchItem = searchItem.nextSelectionItem();
				}
			}
		else
			return setError( 1, null, "The given unused justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte checkWordItemForUsage( WordItem unusedWordItem )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		if( unusedWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.generalizationWordItem() == unusedWordItem )
					return setError( 1, null, "The generalization word item is still in use" );

				if( searchItem.specificationWordItem() == unusedWordItem )
					return setError( 1, null, "The specification word item is still in use" );

				searchItem = searchItem.nextSelectionItem();
				}
			}
		else
			return setError( 1, null, "The given unused word item is undefined" );

		return CommonVariables.result;
		}

	protected SelectionResultType checkDuplicateCondition()
		{
		SelectionResultType selectionResult = new SelectionResultType();

		selectionResult.duplicateConditionSentenceNr = CommonVariables.currentSentenceNr;

		do	{
			if( ( selectionResult.duplicateConditionSentenceNr = getLowerSentenceNr( selectionResult.duplicateConditionSentenceNr ) ) > Constants.NO_SENTENCE_NR )
				{
				if( ( selectionResult = checkDuplicateSelectionPart( selectionResult.duplicateConditionSentenceNr ) ).result != Constants.RESULT_OK )
					addError( 1, null, "I failed to check if the alternative selection part is duplicate" );
				}
			}
		while( CommonVariables.result == Constants.RESULT_OK &&
		!selectionResult.foundDuplicateSelection &&
		selectionResult.duplicateConditionSentenceNr > Constants.NO_SENTENCE_NR );

		selectionResult.result = CommonVariables.result;
		return selectionResult;
		}

	protected SelectionResultType checkDuplicateSelectionPart( int duplicateConditionSentenceNr )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		SelectionItem currentSelectionItem = null;
		SelectionItem searchItem = firstActiveSelectionItem();

		if( duplicateConditionSentenceNr > Constants.NO_SENTENCE_NR )
			{
			if( duplicateConditionSentenceNr < CommonVariables.currentSentenceNr )
				{
				selectionResult.foundDuplicateSelection = true;

				while( searchItem != null &&
				searchItem.activeSentenceNr() >= duplicateConditionSentenceNr )
					{
					if( searchItem.activeSentenceNr() == duplicateConditionSentenceNr )
						{
						currentSelectionItem = firstActiveSelectionItem();

						do	{
							if( currentSelectionItem.isAction() == searchItem.isAction() &&
							currentSelectionItem.isAssignedOrClear() == searchItem.isAssignedOrClear() &&
							currentSelectionItem.isNegative() == searchItem.isNegative() &&
							currentSelectionItem.isNewStart() == searchItem.isNewStart() &&
							currentSelectionItem.selectionLevel() == searchItem.selectionLevel() &&
							currentSelectionItem.generalizationWordItem() == searchItem.generalizationWordItem() &&
							currentSelectionItem.specificationWordItem() == searchItem.specificationWordItem() )
								{
								if( currentSelectionItem.specificationString() != null &&
								searchItem.specificationString() != null )
									selectionResult.foundDuplicateSelection = ( currentSelectionItem.specificationString().equals( searchItem.specificationString() ) );
								else
									{
									if( currentSelectionItem.specificationString() != null ||
									searchItem.specificationString() != null )
										selectionResult.foundDuplicateSelection = false;
									}

								if( selectionResult.foundDuplicateSelection )
									{
									currentSelectionItem = ( currentSelectionItem.nextSelectionItem() != null &&
															currentSelectionItem.nextSelectionItem().hasCurrentCreationSentenceNr() ? currentSelectionItem.nextSelectionItem() : null );

									searchItem = ( searchItem.nextSelectionItem() != null &&
												searchItem.nextSelectionItem().activeSentenceNr() == duplicateConditionSentenceNr ? searchItem.nextSelectionItem() : null );
									}
								else
									searchItem = null;
								}
							else
								{
								selectionResult.foundDuplicateSelection = false;
								searchItem = null;
								}
							}
						while( selectionResult.foundDuplicateSelection &&
						currentSelectionItem != null &&
						searchItem != null );
						}
					else
						searchItem = searchItem.nextSelectionItem();
					}
				}
			else
				setError( 1, null, "The given duplicate sentence number is equal or higher than the current sentence number" );
			}
		else
			setError( 1, null, "The given duplicate sentence number is undefined" );

		selectionResult.result = CommonVariables.result;
		return selectionResult;
		}

	protected SelectionResultType createSelectionItem( boolean isAction, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isFirstComparisonPart, boolean isNewStart, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short selectionLevel, short imperativeParameter, short prepositionParameter, short specificationWordParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SelectionResultType selectionResult = new SelectionResultType();

		if( generalizationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		generalizationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( specificationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
			specificationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
				{
				if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
					{
					if( ( selectionResult.lastCreatedSelectionItem = new SelectionItem( isAction, isAssignedOrClear, isDeactive, isArchive, isFirstComparisonPart, isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString, this, myWord() ) ) != null )
						{
						if( addItemToActiveList( (Item)selectionResult.lastCreatedSelectionItem ) != Constants.RESULT_OK )
							addError( 1, null, "I failed to add a selection item" );
						}
					else
						setError( 1, null, "I failed to create a selection item" );
					}
				else
					setError( 1, null, "The current item number is undefined" );
				}
			else
				setError( 1, null, "The given specification word type number is undefined or out of bounds" );
			}
		else
			setError( 1, null, "The given generalization word type number is undefined or out of bounds" );

		selectionResult.result = CommonVariables.result;
		return selectionResult;
		}

	protected SelectionItem executionStartEntry( short executionLevel, int executionSentenceNr )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		while( searchItem != null &&
		searchItem.activeSentenceNr() >= executionSentenceNr )
			{
			if( searchItem.activeSentenceNr() == executionSentenceNr &&
			searchItem.selectionLevel() == executionLevel )
				return searchItem;

			searchItem = searchItem.nextSelectionItem();
			}

		return null;
		}

	protected SelectionItem firstActiveSelectionItem()
		{
		return (SelectionItem)firstActiveItem();
		}

	protected SelectionItem firstConditionSelectionItem( int conditionSentenceNr )
		{
		SelectionItem searchItem = firstActiveSelectionItem();

		while( searchItem != null &&
		searchItem.activeSentenceNr() >= conditionSentenceNr )
			{
			if( searchItem.activeSentenceNr() == conditionSentenceNr )
				return searchItem;

			searchItem = searchItem.nextSelectionItem();
			}

		return null;
		}

	protected SelectionResultType findFirstExecutionItem( WordItem solveWordItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		SelectionItem firstSelectionItem;

		if( ( firstSelectionItem = firstActiveSelectionItem() ) != null )
			{
			if( firstSelectionItem.findNextExecutionSelectionItem( true, solveWordItem ) == Constants.RESULT_OK )
				selectionResult.firstExecutionItem = firstSelectionItem.nextExecutionItem();
			else
				addError( 1, null, "I failed to find the first execution selection item" );
			}

		selectionResult.result = CommonVariables.result;
		return selectionResult;
		}
	};

/*************************************************************************
 *
 *	"Everywhere - from east to west -
 *	praise the name of the Lord.
 *	For the Lord is high above the nations;
 *	his glory is higher than the heavens." (Psalm 113:3-4)
 *
 *************************************************************************/
