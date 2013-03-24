/*
 *	Class:			ListQuery
 *	Supports class:	List
 *	Purpose:		To process queries
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

class ListQuery
	{
	// Private constructible variables

	private List myList_;
	private String moduleNameString_;


	// Private methods

	private void itemQuery( boolean selectOnFind, boolean isReferenceQuery, int querySentenceNr, int queryItemNr, Item queryItem )
		{
		while( queryItem != null )
			{
			if( ( isReferenceQuery &&
			queryItem.checkReferenceItemById( querySentenceNr, queryItemNr ) ) ||

			( !isReferenceQuery &&
			( querySentenceNr == Constants.NO_SENTENCE_NR ||
			querySentenceNr == queryItem.creationSentenceNr() ) &&

			( queryItemNr == Constants.NO_SENTENCE_NR ||
			queryItemNr == queryItem.itemNr() ) ) )
				{
				if( selectOnFind &&
				!queryItem.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					queryItem.isSelectedByQuery = true;
					}
				}
			else
				{
				if( !selectOnFind &&
				queryItem.isSelectedByQuery )
					queryItem.isSelectedByQuery = false;
				}

			queryItem = queryItem.nextItem;
			}
		}

	private void listQuery( boolean selectOnFind, Item queryItem )
		{
		while( queryItem != null )
			{
			if( selectOnFind )
				{
				if( !queryItem.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					queryItem.isSelectedByQuery = true;
					}
				}
			else
				{
				if( queryItem.isSelectedByQuery )
					queryItem.isSelectedByQuery = false;
				}

			queryItem = queryItem.nextItem;
			}
		}

	private void wordTypeQuery( boolean selectOnFind, short queryWordTypeNr, Item queryItem )
		{
		while( queryItem != null )
			{
			if( queryItem.checkWordType( queryWordTypeNr ) )
				{
				if( selectOnFind &&
				!queryItem.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					queryItem.isSelectedByQuery = true;
					}
				}
			else
				{
				if( !selectOnFind &&
				queryItem.isSelectedByQuery )
					queryItem.isSelectedByQuery = false;
				}

			queryItem = queryItem.nextItem;
			}
		}

	private void parameterQuery( boolean selectOnFind, int queryParameter, Item queryItem )
		{
		while( queryItem != null )
			{
			if( queryItem.checkParameter( queryParameter ) )
				{
				if( selectOnFind &&
				!queryItem.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					queryItem.isSelectedByQuery = true;
					}
				}
			else
				{
				if( !selectOnFind &&
				queryItem.isSelectedByQuery )
					queryItem.isSelectedByQuery = false;
				}

			queryItem = queryItem.nextItem;
			}
		}

	private void wordQuery( boolean selectOnFind, Item queryItem )
		{
		while( queryItem != null )
			{
			if( selectOnFind )
				{
				if( !queryItem.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					queryItem.isSelectedByQuery = true;
					}
				}
			else
				{
				if( queryItem.isSelectedByQuery )
					queryItem.isSelectedByQuery = false;
				}

			queryItem = queryItem.nextItem;
			}
		}

	private void clearQuerySelections( Item searchItem )
		{
		while( searchItem != null )
			{
			searchItem.isSelectedByQuery = false;
			searchItem = searchItem.nextItem;
			}
		}

	private byte wordReferenceQuery( boolean selectOnFind, String wordReferenceNameString, Item queryItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		queryItem != null )
			{
			if( queryItem.findMatchingWordReferenceString( wordReferenceNameString ) == Constants.RESULT_OK )
				{
				if( CommonVariables.foundMatchingStrings )
					{
					if( selectOnFind &&
					!queryItem.isSelectedByQuery )
						{
						CommonVariables.foundQuery = true;
						queryItem.isSelectedByQuery = true;
						}
					}
				else
					{
					if( !selectOnFind &&
					queryItem.isSelectedByQuery )
						queryItem.isSelectedByQuery = false;
					}

				queryItem = queryItem.nextItem;
				}
			else
				myList_.addError( 1, moduleNameString_, "I failed to check the word references" );
			}

		return CommonVariables.result;
		}

	private byte stringQuery( boolean selectOnFind, String wordString, Item queryItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		queryItem != null )
			{
			if( queryItem.itemString() != null )
				{
				if( compareStrings( wordString, queryItem.itemString() ) == Constants.RESULT_OK )
					{
					if( CommonVariables.foundMatchingStrings )
						{
						if( selectOnFind &&
						!queryItem.isSelectedByQuery )
							{
							CommonVariables.foundQuery = true;
							queryItem.isSelectedByQuery = true;
							}
						}
					else
						{
						if( !selectOnFind &&
						queryItem.isSelectedByQuery )
							queryItem.isSelectedByQuery = false;
						}
					}
				else
					myList_.addError( 1, moduleNameString_, "I failed to compare two strings" );
				}

			queryItem = queryItem.nextItem;
			}

		return CommonVariables.result;
		}

	private byte showQueryResult( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth, Item queryItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		queryItem != null )
			{
			if( queryItem.isSelectedByQuery )
				{
				if( showOnlyWords )
					queryItem.showWords( returnQueryToPosition, queryWordTypeNr );
				else
					{
					if( showOnlyWordReferences )
						queryItem.showWordReferences( returnQueryToPosition );
					else
						{
						if( showOnlyStrings )
							queryItem.showString( returnQueryToPosition );
						else
							{
							if( Presentation.writeText( true, promptTypeNr, queryWidth, queryItem.toStringBuffer( queryWordTypeNr ) ) != Constants.RESULT_OK )
								myList_.addError( 1, moduleNameString_, "I failed to show the info of an active item" );
							}
						}
					}
				}

			queryItem = queryItem.nextItem;
			}

		return CommonVariables.result;
		}


	// Constructor

	protected ListQuery( List myList )
		{
		String errorString = null;

		myList_ = myList;
		moduleNameString_ = this.getClass().getName();

		if( myList_ == null )
			errorString = "The given my list is undefined";

		if( errorString != null )
			{
			if( myList_ != null )
				myList_.setSystemError( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected methods

	protected void countQueryResult()
		{
		Item searchItem = myList_.firstActiveItem();

		while( searchItem != null )
			{
			if( searchItem.isSelectedByQuery )
				CommonVariables.nActiveQueryItems++;

			searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstDeactiveItem();

		while( searchItem != null )
			{
			if( searchItem.isSelectedByQuery )
				CommonVariables.nDeactiveQueryItems++;

			searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstArchiveItem();

		while( searchItem != null )
			{
			if( searchItem.isSelectedByQuery )
				CommonVariables.nArchivedQueryItems++;

			searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstDeleteItem();

		while( searchItem != null )
			{
			if( searchItem.isSelectedByQuery )
				CommonVariables.nDeletedQueryItems++;

			searchItem = searchItem.nextItem;
			}
		}

	protected void clearQuerySelections()
		{
		clearQuerySelections( myList_.firstActiveItem() );
		clearQuerySelections( myList_.firstDeactiveItem() );
		clearQuerySelections( myList_.firstArchiveItem() );
		clearQuerySelections( myList_.firstDeleteItem() );
		}

	protected void itemQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr )
		{
		if( selectActiveItems )
			itemQuery( selectOnFind, isReferenceQuery, querySentenceNr, queryItemNr, myList_.firstActiveItem() );

		if( selectDeactiveItems )
			itemQuery( selectOnFind, isReferenceQuery, querySentenceNr, queryItemNr, myList_.firstDeactiveItem() );

		if( selectArchiveItems )
			itemQuery( selectOnFind, isReferenceQuery, querySentenceNr, queryItemNr, myList_.firstArchiveItem() );

		if( selectDeletedItems )
			itemQuery( selectOnFind, isReferenceQuery, querySentenceNr, queryItemNr, myList_.firstDeleteItem() );
		}

	protected void listQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems )
		{
		if( selectActiveItems )
			listQuery( selectOnFind, myList_.firstActiveItem() );

		if( selectDeactiveItems )
			listQuery( selectOnFind, myList_.firstDeactiveItem() );

		if( selectArchiveItems )
			listQuery( selectOnFind, myList_.firstArchiveItem() );

		if( selectDeletedItems )
			listQuery( selectOnFind, myList_.firstDeleteItem() );
		}

	protected void wordTypeQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		if( selectActiveItems )
			wordTypeQuery( selectOnFind, queryWordTypeNr, myList_.firstActiveItem() );

		if( selectDeactiveItems )
			wordTypeQuery( selectOnFind, queryWordTypeNr, myList_.firstDeactiveItem() );

		if( selectArchiveItems )
			wordTypeQuery( selectOnFind, queryWordTypeNr, myList_.firstArchiveItem() );

		if( selectDeletedItems )
			wordTypeQuery( selectOnFind, queryWordTypeNr, myList_.firstDeleteItem() );
		}

	protected void parameterQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		if( selectActiveItems )
			parameterQuery( selectOnFind, queryParameter, myList_.firstActiveItem() );

		if( selectDeactiveItems )
			parameterQuery( selectOnFind, queryParameter, myList_.firstDeactiveItem() );

		if( selectArchiveItems )
			parameterQuery( selectOnFind, queryParameter, myList_.firstArchiveItem() );

		if( selectDeletedItems )
			parameterQuery( selectOnFind, queryParameter, myList_.firstDeleteItem() );
		}

	protected void wordQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems )
		{
		if( selectActiveItems )
			wordQuery( selectOnFind, myList_.firstActiveItem() );

		if( selectDeactiveItems )
			wordQuery( selectOnFind, myList_.firstDeactiveItem() );

		if( selectArchiveItems )
			wordQuery( selectOnFind, myList_.firstArchiveItem() );

		if( selectDeletedItems )
			wordQuery( selectOnFind, myList_.firstDeleteItem() );
		}

	protected byte compareStrings( String searchString, String sourceString )
		{
		boolean stop;
		int searchStringPosition = 0;
		int sourceStringPosition = 0;

		CommonVariables.foundMatchingStrings = false;

		if( searchString != null )
			{
			if( sourceString != null )
				{
				if( searchString != sourceString )
					{
					CommonVariables.foundMatchingStrings = true;

					while( CommonVariables.foundMatchingStrings &&
					searchStringPosition < searchString.length() &&
					sourceStringPosition < sourceString.length() )
						{
						if( searchString.charAt( searchStringPosition ) == sourceString.charAt( sourceStringPosition ) ||
						searchString.charAt( searchStringPosition ) == Constants.SYMBOL_QUESTION_MARK )
							{
							searchStringPosition++;
							sourceStringPosition++;
							}
						else
							{
							if( searchString.charAt( searchStringPosition ) == Constants.SYMBOL_ASTERISK )
								{
								if( ++searchStringPosition < searchString.length() )
									{
									stop = false;

									while( CommonVariables.result == Constants.RESULT_OK &&
									!stop &&
									sourceStringPosition < sourceString.length() )
										{
										if( searchString.charAt( searchStringPosition ) == sourceString.charAt( sourceStringPosition ) )
											{
											// Check remaining strings
											if( compareStrings( searchString.substring( searchStringPosition ), sourceString.substring( sourceStringPosition ) ) == Constants.RESULT_OK )
												{
												if( CommonVariables.foundMatchingStrings )
													{
													stop = true;
													searchStringPosition++;
													}
												else
													CommonVariables.foundMatchingStrings = true;	// Reset indicator

												sourceStringPosition++;
												}
											else
												myList_.addError( 1, moduleNameString_, "I failed to compare the remaining strings" );
											}
										else
											sourceStringPosition++;					// Skip source characters when not equal
										}
									}
								else
									sourceStringPosition = sourceString.length();	// Empty source string after asterisk
								}
							else
								CommonVariables.foundMatchingStrings = false;
							}
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( CommonVariables.foundMatchingStrings &&
						sourceStringPosition == sourceString.length() )
							{
							// Check search string for extra asterisks
							while( searchStringPosition < searchString.length() &&
							searchString.charAt( searchStringPosition ) == Constants.SYMBOL_ASTERISK )
								searchStringPosition++;		// Skip extra asterisks
							}

						if( searchStringPosition < searchString.length() ||
						sourceStringPosition < sourceString.length() )
							CommonVariables.foundMatchingStrings = false;
						}
					}
				else
					return myList_.setError( 1, moduleNameString_, "The given strings are the same" );
				}
			else
				return myList_.setError( 1, moduleNameString_, "The given source string is undefined" );
			}
		else
			return myList_.setError( 1, moduleNameString_, "The given search string is undefined" );

		return CommonVariables.result;
		}

	protected byte wordReferenceQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		if( selectActiveItems )
			{
			if( wordReferenceQuery( selectOnFind, wordReferenceNameString, myList_.firstActiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to check the word references of an active word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectDeactiveItems )
			{
			if( wordReferenceQuery( selectOnFind, wordReferenceNameString, myList_.firstDeactiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to check the word references of a deactive word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectArchiveItems )
			{
			if( wordReferenceQuery( selectOnFind, wordReferenceNameString, myList_.firstArchiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to check the word references of an archive word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectDeletedItems )
			{
			if( wordReferenceQuery( selectOnFind, wordReferenceNameString, myList_.firstDeleteItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to check the word references of a deleted word" );
			}

		return CommonVariables.result;
		}

	protected byte stringQuery( boolean selectOnFind, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordString )
		{
		if( selectActiveItems )
			{
			if( stringQuery( selectOnFind, wordString, myList_.firstActiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to string query an active word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectDeactiveItems )
			{
			if( stringQuery( selectOnFind, wordString, myList_.firstDeactiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to string query a deactive word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectArchiveItems )
			{
			if( stringQuery( selectOnFind, wordString, myList_.firstArchiveItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to string query an archive word" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		selectDeletedItems )
			{
			if( stringQuery( selectOnFind, wordString, myList_.firstDeleteItem() ) != Constants.RESULT_OK )
				myList_.addError( 1, moduleNameString_, "I failed to string query a deleted word" );
			}

		return CommonVariables.result;
		}

	protected byte showQueryResult( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		if( showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, myList_.firstActiveItem() ) == Constants.RESULT_OK )
			{
			if( showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, myList_.firstDeactiveItem() ) == Constants.RESULT_OK )
				{
				if( showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, myList_.firstArchiveItem() ) == Constants.RESULT_OK )
					{
					if( showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth, myList_.firstDeleteItem() ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to show the query result of a deleted word" );
					}
				else
					myList_.addError( 1, moduleNameString_, "I failed to show the query result of an archive word" );
				}
			else
				myList_.addError( 1, moduleNameString_, "I failed to show the query result of a deactive word" );
			}
		else
			myList_.addError( 1, moduleNameString_, "I failed to show the query result of an active word" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Your name, O Lord, endures forever;
 *	your name, O Lord, is known to every generation." (Psalm 135:13)
 *
 *************************************************************************/
