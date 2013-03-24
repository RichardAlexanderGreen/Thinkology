/*
 *	Class:			WordQuery
 *	Supports class:	WordItem
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

class WordQuery
	{
	// Private constructible variables

	private WordItem myWord_;
	private String moduleNameString_;


	// Constructor

	protected WordQuery( WordItem myWord )
		{
		String errorString = null;

		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();

		if( myWord_ == null )
			errorString = "The given my word is undefined";

		if( errorString != null )
			{
			if( myWord_ != null )
				myWord_.setSystemErrorInWord( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected virtual methods

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		myWord_.baseToStringBuffer( queryWordTypeNr );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + Constants.QUERY_WORD_START_CHAR + myWord_.anyWordTypeString() + Constants.QUERY_WORD_END_CHAR );

		if( myWord_.needsAuthorizationForChanges() )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "needsAuthorizationForChanges" );

		if( myWord_.wordParameter() > Constants.NO_WORD_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wordParameter:" + myWord_.wordParameter() );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected void countQueryResult()
		{
		short wordListNr = 0;

		while( wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				myWord_.wordList[wordListNr].countQueryResultInList();

			wordListNr++;
			}
		}

	protected void clearQuerySelections()
		{
		for( short wordListNr : Constants.WordLists )
			{
			if( myWord_.wordList[wordListNr] != null )
				myWord_.wordList[wordListNr].clearQuerySelectionsInList();
			}
		}

	protected byte itemQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].itemQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query item numbers" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte listQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].listQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to do a list query list" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte wordTypeQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].wordTypeQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query word types" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte parameterQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].parameterQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query parameters" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte wordQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordNameString )
		{
		short wordListNr = 0;

		if( myWord_.findMatchingWordReferenceString( wordNameString ) == Constants.RESULT_OK )
			{
			if( CommonVariables.foundMatchingStrings )
				{
				if( isFirstInstruction &&
				!myWord_.isSelectedByQuery )
					{
					CommonVariables.foundQuery = true;
					myWord_.isSelectedByQuery = true;
					}
				}
			else
				{
				if( !isFirstInstruction &&
				myWord_.isSelectedByQuery )
					myWord_.isSelectedByQuery = false;
				}

			while( CommonVariables.result == Constants.RESULT_OK &&
			wordListNr < Constants.NUMBER_OF_WORD_LISTS )
				{
				if( myWord_.wordList[wordListNr] != null )
					{
					if( myWord_.wordList[wordListNr].wordQueryInList( ( isFirstInstruction && CommonVariables.foundMatchingStrings ), selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query word items" );
					}

				wordListNr++;
				}
			}
		else
			myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find words" );

		return CommonVariables.result;
		}

	protected byte wordReferenceQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].wordReferenceQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query word references" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte stringQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].stringQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to query strings" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte showQueryResult( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].showQueryResultInList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to show the items" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Who can be compared with the Lord or God,
 *	who is enthroned on high?" (Psalm 113:5)
 *
 *************************************************************************/
