/*
 *	Class:			WordList
 *	Parent class:	List
 *	Purpose:		To store word items
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

class WordList extends List
	{
	// Private assignment methods

	private byte createNewAssignmentLevelInWordList( WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			searchItem.createNewAssignmentLevel();
			searchItem = searchItem.nextWordItem();
			}

		return CommonVariables.result;
		}


	// Private cleanup methods

	private int highestSentenceNrInWordList( WordItem searchItem )
		{
		int tempSentenceNr;
		int highestSentenceNr = Constants.NO_SENTENCE_NR;

		while( searchItem != null )
			{
			if( ( tempSentenceNr = searchItem.highestSentenceNr() ) > highestSentenceNr )
				highestSentenceNr = tempSentenceNr;

			searchItem = searchItem.nextWordItem();
			}

		return highestSentenceNr;
		}

	private byte getCurrentItemNrInWordList( WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.getCurrentItemNrInWord() == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to get the current item number in a word" );
			}

		return CommonVariables.result;
		}

	private byte rollbackDeletedRedoInfoInWordList( WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.rollbackDeletedRedoInfo() == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to rollback a deleted item in a word" );
			}

		return CommonVariables.result;
		}

	private byte deleteRollbackInfoInWordList( WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.deleteRollbackInfo() == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to delete the rollback info in a word" );
			}

		return CommonVariables.result;
		}

	private byte deleteSentencesInWordList( boolean isAvailableForRollback, int lowestSentenceNr, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.deleteSentencesInWord( isAvailableForRollback, lowestSentenceNr ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to delete sentences in a word" );
			}

		return CommonVariables.result;
		}

	private byte removeFirstRangeOfDeletedItemsInWordList( WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		CommonVariables.nDeletedItems == 0 )
			{
			if( searchItem.removeFirstRangeOfDeletedItems() == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to remove the first deleted items in a word" );
			}

		return CommonVariables.result;
		}

	private byte getHighestInUseSentenceNrInWordList( boolean includeTemporaryLists, boolean includeDeletedItems, int highestSentenceNr, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		CommonVariables.highestInUseSentenceNr < highestSentenceNr )
			{
			if( searchItem.getHighestInUseSentenceNrInWord( includeTemporaryLists, includeDeletedItems, highestSentenceNr ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to check if the sentence is empty in a word" );
			}

		return CommonVariables.result;
		}

	private byte decrementSentenceNrsInWordList( int startSentenceNr, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.decrementSentenceNrsInWord( startSentenceNr ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to decrement the sentence numbers from the current sentence number in a word" );
			}

		return CommonVariables.result;
		}

	private byte decrementItemNrRangeInWordList( int decrementSentenceNr, int decrementItemNr, int decrementOffset, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.decrementItemNrRangeInWord( decrementSentenceNr, decrementItemNr, decrementOffset ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to decrement item numbers in a word" );
			}

		return CommonVariables.result;
		}


	// Private query methods

	private void countQueryResultInWordList( WordItem searchItem )
		{
		while( searchItem != null )
			{
			searchItem.countQueryResult();
			searchItem = searchItem.nextWordItem();
			}
		}

	private void clearQuerySelectionsInWordList( WordItem searchItem )
		{
		while( searchItem != null )
			{
			searchItem.clearQuerySelections();
			searchItem = searchItem.nextWordItem();
			}
		}

	private byte wordQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordNameString, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.wordQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query a word" );
			}

		return CommonVariables.result;
		}

	private byte itemQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.itemQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query item numbers in a word" );
			}

		return CommonVariables.result;
		}

	private byte listQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.listQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query lists in a word" );
			}

		return CommonVariables.result;
		}

	private byte wordTypeQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.wordTypeQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query word types in a word" );
			}

		return CommonVariables.result;
		}

	private byte parameterQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( isFirstInstruction &&
			searchItem.checkParameter( queryParameter ) )
				searchItem.isSelectedByQuery = true;

//			if( searchItem.isSelectedByQuery )
				{
				if( searchItem.parameterQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to query parameters in a word" );
				}

			searchItem = searchItem.nextWordItem();
			}

		return CommonVariables.result;
		}

	private byte wordReferenceQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.wordReferenceQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query word references in a word" );
			}

		return CommonVariables.result;
		}

	private byte stringQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.stringQueryInWord( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to query strings in a word" );
			}

		return CommonVariables.result;
		}

	private byte showQueryResultInWordList( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth, WordItem searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.showQueryResultInWord( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth ) == Constants.RESULT_OK )
				searchItem = searchItem.nextWordItem();
			else
				addError( 1, null, "I failed to show the query result in a word" );
			}

		return CommonVariables.result;
		}


	// Private common methods

	private WordItem firstActiveWordItem()
		{
		return (WordItem)firstActiveItem();
		}

	private WordItem firstDeactiveWordItem()
		{
		return (WordItem)firstDeactiveItem();
		}

	private WordItem firstArchiveWordItem()
		{
		return (WordItem)firstArchiveItem();
		}

	private WordItem firstDeletedWordItem()
		{
		return (WordItem)firstDeleteItem();
		}


	// Constructor

	protected WordList( WordItem myWord )
		{
		initializeListVariables( Constants.ADMIN_WORD_LIST_SYMBOL, myWord );
		}


	// Protected assignment methods

	protected byte createNewAssignmentLevelInWordList()
		{
		if( createNewAssignmentLevelInWordList( firstActiveWordItem() ) == Constants.RESULT_OK )
			return createNewAssignmentLevelInWordList( firstDeactiveWordItem() );

		return CommonVariables.result;
		}


	// Protected cleanup methods

	protected int highestSentenceNrInWordList()
		{
		int tempSentenceNr;
		int highestSentenceNr = highestSentenceNrInWordList( firstActiveWordItem() );

		if( ( tempSentenceNr = highestSentenceNrInWordList( firstDeactiveWordItem() ) ) > highestSentenceNr )
			highestSentenceNr = tempSentenceNr;

		if( ( tempSentenceNr = highestSentenceNrInWordList( firstArchiveWordItem() ) ) > highestSentenceNr )
			highestSentenceNr = tempSentenceNr;

		if( ( tempSentenceNr = highestSentenceNrInWordList( firstDeletedWordItem() ) ) > highestSentenceNr )
			highestSentenceNr = tempSentenceNr;

		return highestSentenceNr;
		}

	protected byte getCurrentItemNrInWordList()
		{
		if( getCurrentItemNrInWordList( firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( getCurrentItemNrInWordList( firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( getCurrentItemNrInWordList( firstArchiveWordItem() ) == Constants.RESULT_OK )
					return getCurrentItemNrInWordList( firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte rollbackDeletedRedoInfoInWordList()
		{
		if( rollbackDeletedRedoInfoInWordList( firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( rollbackDeletedRedoInfoInWordList( firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( rollbackDeletedRedoInfoInWordList( firstArchiveWordItem() ) == Constants.RESULT_OK )
					return rollbackDeletedRedoInfoInWordList( firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte deleteRollbackInfoInWordList()
		{
		if( deleteRollbackInfoInWordList( firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( deleteRollbackInfoInWordList( firstDeactiveWordItem() ) == Constants.RESULT_OK )
				return deleteRollbackInfoInWordList( firstArchiveWordItem() );
			}

		return CommonVariables.result;
		}

	protected byte deleteSentencesInWordList( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		if( deleteSentencesInWordList( isAvailableForRollback, lowestSentenceNr, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( deleteSentencesInWordList( isAvailableForRollback, lowestSentenceNr, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( deleteSentencesInWordList( isAvailableForRollback, lowestSentenceNr, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return deleteSentencesInWordList( isAvailableForRollback, lowestSentenceNr, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte removeFirstRangeOfDeletedItemsInWordList()
		{
		if( removeFirstRangeOfDeletedItemsInWordList( firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( removeFirstRangeOfDeletedItemsInWordList( firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( removeFirstRangeOfDeletedItemsInWordList( firstArchiveWordItem() ) == Constants.RESULT_OK )
					return removeFirstRangeOfDeletedItemsInWordList( firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte getHighestInUseSentenceNrInWordList( boolean includeTemporaryLists, boolean includeDeletedItems, int highestSentenceNr )
		{
		if( getHighestInUseSentenceNrInWordList( includeTemporaryLists, includeDeletedItems, highestSentenceNr, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( CommonVariables.highestInUseSentenceNr < highestSentenceNr )
				{
				if( getHighestInUseSentenceNrInWordList( includeTemporaryLists, includeDeletedItems, highestSentenceNr, firstDeactiveWordItem() ) == Constants.RESULT_OK )
					{
					if( CommonVariables.highestInUseSentenceNr < highestSentenceNr )
						{
						if( getHighestInUseSentenceNrInWordList( includeTemporaryLists, includeDeletedItems, highestSentenceNr, firstArchiveWordItem() ) == Constants.RESULT_OK )
							{
							if( includeDeletedItems &&
							CommonVariables.highestInUseSentenceNr < highestSentenceNr )
								return getHighestInUseSentenceNrInWordList( includeTemporaryLists, includeDeletedItems, highestSentenceNr, firstDeletedWordItem() );
							}
						}
					}
				}
			}

		return CommonVariables.result;
		}

	protected byte decrementSentenceNrsInWordList( int startSentenceNr )
		{
		if( decrementSentenceNrsInWordList( startSentenceNr, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( decrementSentenceNrsInWordList( startSentenceNr, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( decrementSentenceNrsInWordList( startSentenceNr, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return decrementSentenceNrsInWordList( startSentenceNr, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte decrementItemNrRangeInWordList( int decrementSentenceNr, int decrementItemNr, int decrementOffset )
		{
		if( decrementItemNrRangeInWordList( decrementSentenceNr, decrementItemNr, decrementOffset, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( decrementItemNrRangeInWordList( decrementSentenceNr, decrementItemNr, decrementOffset, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( decrementItemNrRangeInWordList( decrementSentenceNr, decrementItemNr, decrementOffset, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return decrementItemNrRangeInWordList( decrementSentenceNr, decrementItemNr, decrementOffset, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}


	// Protected query methods

	protected void countQueryResultInWordList()
		{
		countQueryResultInWordList( firstActiveWordItem() );
		countQueryResultInWordList( firstDeactiveWordItem() );
		countQueryResultInWordList( firstArchiveWordItem() );
		countQueryResultInWordList( firstDeletedWordItem() );
		}

	protected void clearQuerySelectionsInWordList()
		{
		clearQuerySelectionsInWordList( firstActiveWordItem() );
		clearQuerySelectionsInWordList( firstDeactiveWordItem() );
		clearQuerySelectionsInWordList( firstArchiveWordItem() );
		clearQuerySelectionsInWordList( firstDeletedWordItem() );
		}

	protected byte wordQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordNameString )
		{
		if( wordQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( wordQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( wordQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return wordQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte itemQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr )
		{
		if( itemQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( itemQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( itemQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return itemQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte listQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer )
		{
		if( listQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer ) == Constants.RESULT_OK )
			{
			if( listQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer, firstActiveWordItem() ) == Constants.RESULT_OK )
				{
				if( listQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer, firstDeactiveWordItem() ) == Constants.RESULT_OK )
					{
					if( listQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer, firstArchiveWordItem() ) == Constants.RESULT_OK )
						return listQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer, firstDeletedWordItem() );
					}
				}
			}
		else
			addError( 1, null, "I failed to query my own list" );

		return CommonVariables.result;
		}

	protected byte wordTypeQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		if( wordTypeQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( wordTypeQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( wordTypeQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return wordTypeQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte parameterQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		if( parameterQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( parameterQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( parameterQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return parameterQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte wordReferenceQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		if( wordReferenceQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( wordReferenceQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( wordReferenceQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return wordReferenceQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte stringQueryInWordList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString )
		{
		if( stringQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( stringQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( stringQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return stringQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}

	protected byte showQueryResultInWordList( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		if( showQueryResultInWordList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, firstActiveWordItem() ) == Constants.RESULT_OK )
			{
			if( showQueryResultInWordList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, firstDeactiveWordItem() ) == Constants.RESULT_OK )
				{
				if( showQueryResultInWordList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, firstArchiveWordItem() ) == Constants.RESULT_OK )
					return showQueryResultInWordList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, firstDeletedWordItem() );
				}
			}

		return CommonVariables.result;
		}


	// Protected read word methods

	protected WordResultType createWordItem( short wordParameter )
		{
		WordResultType wordResult = new WordResultType();

		if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
			{
			if( ( wordResult.createdWordItem = new WordItem( wordParameter, this ) ) != null )
				{
				if( addItemToActiveList( (Item)wordResult.createdWordItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add a word item" );
				}
			else
				setError( 1, null, "I failed to create a word item" );
			}
		else
			setError( 1, null, "The current item number is undefined" );

		wordResult.result = CommonVariables.result;
		return wordResult;
		}
	};

/*************************************************************************
 *
 *	"They share freely and give generously to those in need.
 *	Their good deeds will be remembered forever.
 *	They will have influence and honor." (Psalm 112:9)
 *
 *************************************************************************/
