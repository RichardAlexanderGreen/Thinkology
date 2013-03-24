/*
 *	Class:			AdminQuery
 *	Supports class:	AdminItem
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

class AdminQuery
	{
	// Private constructible variables

	private int queryItemNr_;
	private int querySentenceNr_;
	private int queryStringPosition_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private void clearQuerySelections()
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			admin_.wordList.clearQuerySelectionsInWordList();

		while( adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				admin_.adminList[adminListNr].clearQuerySelectionsInList();

			adminListNr++;
			}
		}

	private boolean foundMoreCategories()
		{
		return ( ( CommonVariables.nActiveQueryItems > 0 &&
				CommonVariables.nDeactiveQueryItems > 0 ) ||

				( CommonVariables.nActiveQueryItems > 0 &&
				CommonVariables.nArchivedQueryItems > 0 ) ||

				( CommonVariables.nActiveQueryItems > 0 &&
				CommonVariables.nDeletedQueryItems > 0 ) ||

				( CommonVariables.nDeactiveQueryItems > 0 &&
				CommonVariables.nArchivedQueryItems > 0 ) ||

				( CommonVariables.nDeactiveQueryItems > 0 &&
				CommonVariables.nDeletedQueryItems > 0 ) ||

				( CommonVariables.nArchivedQueryItems > 0 &&
				CommonVariables.nDeletedQueryItems > 0 ) );
		}

	private boolean isAdminListChar( char queryListChar )
		{
		return ( queryListChar == Constants.ADMIN_FILE_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_READ_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_SCORE_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_WORD_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_CONDITION_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_ACTION_LIST_SYMBOL ||
				queryListChar == Constants.ADMIN_ALTERNATIVE_LIST_SYMBOL );
		}

	private boolean isWordListChar( char queryListChar )
		{
		return ( queryListChar == Constants.WORD_ASSIGNMENT_LIST_SYMBOL ||
				queryListChar == Constants.WORD_COLLECTION_LIST_SYMBOL ||
				queryListChar == Constants.WORD_GENERALIZATION_LIST_SYMBOL ||
				queryListChar == Constants.WORD_INTERFACE_LANGUAGE_LIST_SYMBOL ||
				queryListChar == Constants.WORD_JUSTIFICATION_LIST_SYMBOL ||
				queryListChar == Constants.WORD_GRAMMAR_LANGUAGE_LIST_SYMBOL ||
				queryListChar == Constants.WORD_WRITE_LIST_SYMBOL ||
				queryListChar == Constants.WORD_SPECIFICATION_LIST_SYMBOL ||
				queryListChar == Constants.WORD_TYPE_LIST_SYMBOL ||
				queryListChar == Constants.WORD_CONTEXT_LIST_SYMBOL );
		}

	private int nTotalCount()
		{
		return ( CommonVariables.nActiveQueryItems +
				CommonVariables.nDeactiveQueryItems +
				CommonVariables.nArchivedQueryItems +
				CommonVariables.nDeletedQueryItems );
		}

	private int countQueryResult()
		{
		short adminListNr = 0;

		CommonVariables.nActiveQueryItems = 0;
		CommonVariables.nDeactiveQueryItems = 0;
		CommonVariables.nArchivedQueryItems = 0;
		CommonVariables.nDeletedQueryItems = 0;

		if( admin_.wordList != null )						// Inside words
			admin_.wordList.countQueryResultInWordList();

		while( adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				admin_.adminList[adminListNr].countQueryResultInList();

			adminListNr++;
			}

		return nTotalCount();
		}

	private byte itemQuery( boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean suppressMessage, StringBuffer textStringBuffer )
		{
		if( textStringBuffer != null )
			{
			if( itemQuery( true, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, false, Constants.NO_SENTENCE_NR, Constants.NO_ITEM_NR ) == Constants.RESULT_OK )
				{
				if( CommonVariables.currentInterfaceLanguageWordItem != null )
					{
					if( !suppressMessage &&
					countQueryResult() == 0 )
						textStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_ITEMS_WERE_FOUND ) );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The current interface language word item is undefined" );
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query items" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given text string buffer is undefined" );

		return CommonVariables.result;
		}

	private byte showQueryResult( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.showQueryResultInWordList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, ( queryWordTypeNr == Constants.WORD_TYPE_UNDEFINED ? CommonVariables.matchingWordTypeNr : queryWordTypeNr ), queryWidth ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to show the query result in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].showQueryResultInList( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to show the query result" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte getIdFromQuery( boolean hasEndChar, String sourceString, char startChar, char endChar )
		{
		querySentenceNr_ = Constants.NO_SENTENCE_NR;
		queryItemNr_ = Constants.NO_ITEM_NR;

		if( sourceString != null )
			{
			if( queryStringPosition_ >= 0 )
				{
				if( queryStringPosition_ < sourceString.length() )
					{
					if( sourceString.charAt( queryStringPosition_ ) == startChar )
						{
						queryStringPosition_++;

						if( queryStringPosition_ < sourceString.length() &&
						sourceString.charAt( queryStringPosition_ ) != endChar )
							{
							if( sourceString.charAt( queryStringPosition_ ) == Constants.SYMBOL_ASTERISK )
								{
								queryStringPosition_++;
								querySentenceNr_ = Constants.MAX_SENTENCE_NR;
								}
							else
								{
								if( sourceString.charAt( queryStringPosition_ ) != Constants.QUERY_SEPARATOR_CHAR )
									{
									if( Character.isDigit( sourceString.charAt( queryStringPosition_ ) ) )
										{
										while( queryStringPosition_ < sourceString.length() &&
										Character.isDigit( sourceString.charAt( queryStringPosition_ ) ) &&
										querySentenceNr_ <= Constants.MAX_SENTENCE_NR / 10 )
											{
											querySentenceNr_ = ( querySentenceNr_ * 10 ) + ( sourceString.charAt( queryStringPosition_ ) - '0' );
											queryStringPosition_++;
											}
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find a number in the query string" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								hasEndChar &&
								queryStringPosition_ < sourceString.length() &&
								sourceString.charAt( queryStringPosition_ ) == Constants.QUERY_SEPARATOR_CHAR )
									{
									queryStringPosition_++;

									if( queryStringPosition_ < sourceString.length() &&
									sourceString.charAt( queryStringPosition_ ) != endChar )
										{
										if( Character.isDigit( sourceString.charAt( queryStringPosition_ ) ) )
											{
											while( queryStringPosition_ < sourceString.length() &&
											Character.isDigit( sourceString.charAt( queryStringPosition_ ) ) &&
											queryItemNr_ <= Constants.MAX_SENTENCE_NR / 10 )
												{
												queryItemNr_ = ( queryItemNr_ * 10 ) + ( sourceString.charAt( queryStringPosition_ ) - '0' );
												queryStringPosition_++;
												}
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the item number in the query string" );
										}
									}
								}
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						hasEndChar )
							{
							if( queryStringPosition_ < sourceString.length() &&
							sourceString.charAt( queryStringPosition_ ) == endChar )
								queryStringPosition_++;
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The given query string is corrupt" );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given command does not start with character '" + startChar + "', but with '" + sourceString.charAt( queryStringPosition_ ) + "'" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given source string is empty or the given query source string position is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given query source string position is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given source string is undefined" );

		return CommonVariables.result;
		}

	private byte getNameFromQuery( String sourceString, StringBuffer nameStringBuffer, char startChar, char endChar )
		{
		int startSourceStringPosition;

		if( sourceString != null )
			{
			if( nameStringBuffer != null )
				{
				if( queryStringPosition_ >= 0 )
					{
					if( queryStringPosition_ < sourceString.length() )
						{
						if( sourceString.charAt( queryStringPosition_ ) == startChar )
							{
							if( queryStringPosition_ + 1 < sourceString.length() )
								{
								queryStringPosition_++;

								if( sourceString.charAt( queryStringPosition_ ) != endChar )
									{
									startSourceStringPosition = queryStringPosition_;

									while( queryStringPosition_ + 1 < sourceString.length() &&
									sourceString.charAt( queryStringPosition_ ) != endChar )
										queryStringPosition_++;

									if( sourceString.charAt( queryStringPosition_ ) == endChar )
										nameStringBuffer.append( sourceString.substring( startSourceStringPosition, queryStringPosition_ ) );
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The name in the given query string is corrupt" );
									}

								queryStringPosition_++;
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The name in the given query string is corrupt" );
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The given name does not start with character '" + startChar + "', but with '" + sourceString.charAt( 0 ) + "'" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given source string is empty or the given query source string position is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given query source string position is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given name string is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given source string is undefined" );

		return CommonVariables.result;
		}

	private byte itemQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr_, int queryItemNr_ )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.itemQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr_, queryItemNr_ ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query item numbers in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].itemQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr_, queryItemNr_ ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to query item numbers in" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte listQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.listQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].listQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryListStringBuffer ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to do a list query" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte wordTypeQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.wordTypeQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query word types in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].wordTypeQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to word types" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte parameterQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.parameterQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query parameters in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].parameterQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to parameters" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte wordQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordNameString )
		{
		if( admin_.wordList != null )		// Inside words
			{
			if( admin_.wordList.wordQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordNameString ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query the words in my word list" );
			}

		return CommonVariables.result;
		}

	private byte wordReferenceQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.wordReferenceQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query word references in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].wordReferenceQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to query word references" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte stringQuery( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.stringQueryInWordList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query strings in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].stringQueryInList( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to query strings" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	// Constructor

	protected AdminQuery( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		queryItemNr_ = Constants.NO_ITEM_NR;
		querySentenceNr_ = Constants.NO_SENTENCE_NR;
		queryStringPosition_ = 1;

		admin_ = admin;
		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();

		if( admin_ != null )
			{
			if( myWord_ == null )
				errorString = "The given my word is undefined";
			}
		else
			errorString = "The given admin is undefined";

		if( errorString != null )
			{
			if( myWord_ != null )
				myWord_.setSystemErrorInItem( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected methods

	protected void initializeQueryStringPosition()
		{
		queryStringPosition_ = 1;
		}

	protected byte writeTextWithPossibleQueryCommands( short promptTypeNr, String textString )
		{
		boolean foundNewLine = false;
		int previousPosition;
		int position = 0;
		char textChar = Constants.SYMBOL_QUESTION_MARK;
		StringBuffer writeStringBuffer = new StringBuffer();

		if( textString != null )
			{
			if( textString.charAt( 0 ) == Constants.SYMBOL_DOUBLE_QUOTE )
				position++;

			while( CommonVariables.result == Constants.RESULT_OK &&
			position < textString.length() &&
			textString.charAt( position ) != Constants.SYMBOL_DOUBLE_QUOTE )
				{
				if( textString.charAt( position ) == Constants.QUERY_CHAR )
					{
					if( ++position < textString.length() )
						{
						previousPosition = position;
						queryStringPosition_ = position;

						if( executeQuery( true, false, true, promptTypeNr, textString ) == Constants.RESULT_OK )
							position = queryStringPosition_;
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute query \"" + textString.substring( previousPosition ) + "\"" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The text string ended with a query character" );
					}
				else
					{
					if( textString.charAt( position ) == Constants.TEXT_DIACRITICAL_CHAR )
						{
						if( ++position < textString.length() )
							{
							if( ( textChar = Presentation.convertDiacriticalChar( textString.charAt( position ) ) ) == Constants.NEW_LINE_CHAR )
								foundNewLine = true;
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The text string ended with a diacritical sign" );
						}
					else
						textChar = textString.charAt( position );

					position++;
					writeStringBuffer.append( Constants.EMPTY_STRING + textChar );
					}

				if( foundNewLine ||

				( position < textString.length() &&
				textString.charAt( position ) != Constants.SYMBOL_DOUBLE_QUOTE &&
				textString.charAt( position ) == Constants.QUERY_CHAR &&
				writeStringBuffer.length() > 0 ) )
					{
					if( Presentation.writeText( false, promptTypeNr, Constants.NO_CENTER_WIDTH, writeStringBuffer ) == Constants.RESULT_OK )
						{
						foundNewLine = false;
						writeStringBuffer = new StringBuffer();
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a character" );
					}
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			writeStringBuffer.length() > 0 )
				{
				if( Presentation.writeText( false, promptTypeNr, Constants.NO_CENTER_WIDTH, writeStringBuffer ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the last characters" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given text string is undefined" );

		return CommonVariables.result;
		}

	protected byte executeQuery( boolean suppressMessage, boolean returnToPosition, boolean writeQueryResult, short promptTypeNr, String queryString )
		{
		boolean showCount = false;
		boolean invalidChar = false;
		boolean isEndOfQuery = false;
		boolean showOnlyStrings = false;
		boolean showOnlyWords = false;
		boolean showOnlyWordReferences = false;
		boolean selectActiveItems = true;
		boolean selectDeactiveItems = true;
		boolean selectArchiveItems = true;
		boolean selectDeletedItems = true;
		boolean isFirstInstruction = true;
		boolean returnQueryToPosition = returnToPosition;
		short queryWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		int nTotal;
		int listStringPosition;
		int queryWidth = Constants.NO_CENTER_WIDTH;
		StringBuffer nameStringBuffer = new StringBuffer();

		CommonVariables.queryStringBuffer = new StringBuffer();

		if( queryString != null )
			{
			if( queryStringPosition_ > 0 &&
			queryStringPosition_ < queryString.length() )
				{
				if( CommonVariables.currentInterfaceLanguageWordItem != null )
					{
					clearQuerySelections();

					querySentenceNr_ = Constants.NO_SENTENCE_NR;
					queryItemNr_ = Constants.NO_ITEM_NR;

					CommonVariables.foundQuery = false;
					CommonVariables.matchingWordTypeNr = Constants.WORD_TYPE_UNDEFINED;

					while( CommonVariables.result == Constants.RESULT_OK &&
					!isEndOfQuery &&
					CommonVariables.queryStringBuffer.length() == 0 &&
					queryStringPosition_ < queryString.length() )
						{
						switch( queryString.charAt( queryStringPosition_ ) )
							{
							case Constants.QUERY_ITEM_START_CHAR:

								if( getIdFromQuery( true, queryString, Constants.QUERY_ITEM_START_CHAR, Constants.QUERY_ITEM_END_CHAR ) == Constants.RESULT_OK )
									{
									if( itemQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, false, querySentenceNr_, queryItemNr_ ) == Constants.RESULT_OK )
										{
										isFirstInstruction = false;

										if( !suppressMessage &&
										countQueryResult() == 0 )
											CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_ITEM_WAS_FOUND ) );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query items" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get an identification from the item" );

								break;

							case Constants.QUERY_REF_ITEM_START_CHAR:

								if( getIdFromQuery( true, queryString, Constants.QUERY_REF_ITEM_START_CHAR, Constants.QUERY_REF_ITEM_END_CHAR ) == Constants.RESULT_OK )
									{
									if( itemQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, true, querySentenceNr_, queryItemNr_ ) == Constants.RESULT_OK )
										{
										isFirstInstruction = false;

										if( !suppressMessage &&
										countQueryResult() == 0 )
											CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_REFERENCE_ITEM_WAS_FOUND ) );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query item references" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a reference identification from the item" );

								break;

							case Constants.QUERY_LIST_START_CHAR:
								nameStringBuffer = new StringBuffer();

								if( getNameFromQuery( queryString, nameStringBuffer, Constants.QUERY_LIST_START_CHAR, Constants.QUERY_LIST_END_CHAR ) == Constants.RESULT_OK )
									{
									listStringPosition = 0;

									do	{	// Check list characters on existence
										if( nameStringBuffer.length() > 0 &&
										!isWordListChar( nameStringBuffer.charAt( listStringPosition ) ) &&
										!isAdminListChar( nameStringBuffer.charAt( listStringPosition ) ) )
											{
											invalidChar = true;
											CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( suppressMessage ? Constants.INTERFACE_QUERY_ERROR : Constants.INTERFACE_QUERY_INVALID_CHARACTER_IN_LIST ) );
											}
										}
									while( !invalidChar &&
									++listStringPosition < nameStringBuffer.length() );

									if( !invalidChar )	// All list characters are valid
										{
										if( listQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, nameStringBuffer ) == Constants.RESULT_OK )
											{
											isFirstInstruction = false;

											if( !suppressMessage &&
											countQueryResult() == 0 )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_LIST_WAS_FOUND ) );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query lists" );
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a list string from the text" );

								break;

							case Constants.QUERY_WORD_START_CHAR:
								if( queryStringPosition_ + 1 < queryString.length() &&
								queryString.charAt( queryStringPosition_ + 1 ) != Constants.QUERY_CHAR )
									{
									nameStringBuffer = new StringBuffer();

									if( getNameFromQuery( queryString, nameStringBuffer, Constants.QUERY_WORD_START_CHAR, Constants.QUERY_WORD_END_CHAR ) == Constants.RESULT_OK )
										{
										if( nameStringBuffer.length() == 0 )
											{
											if( queryStringPosition_ < queryString.length() &&
											queryString.charAt( queryStringPosition_ ) != Constants.QUERY_CHAR )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( suppressMessage ? Constants.INTERFACE_QUERY_ERROR : Constants.INTERFACE_QUERY_EMPTY_WORD_SPECIFICATION ) );
											else
												{
												showOnlyWords = true;
												returnQueryToPosition = true;
												}
											}
										else
											{
											if( wordQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, nameStringBuffer.toString() ) == Constants.RESULT_OK )
												{
												isFirstInstruction = false;

												if( !suppressMessage &&
												countQueryResult() == 0 )
													CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_WORD_WAS_FOUND ) );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query words" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a word name from the query specification" );
									}
								else
									{
									showOnlyWords = true;
									returnQueryToPosition = false;
									queryStringPosition_++;
									}

								break;

							case Constants.QUERY_WORD_REFERENCE_START_CHAR:
								if( queryStringPosition_ + 1 < queryString.length() &&
								queryString.charAt( queryStringPosition_ + 1 ) != Constants.QUERY_CHAR )
									{
									nameStringBuffer = new StringBuffer();

									if( getNameFromQuery( queryString, nameStringBuffer, Constants.QUERY_WORD_REFERENCE_START_CHAR, Constants.QUERY_WORD_REFERENCE_END_CHAR ) == Constants.RESULT_OK )
										{
										if( nameStringBuffer.length() == 0 )
											{
											if( queryStringPosition_ < queryString.length() &&
											queryString.charAt( queryStringPosition_ ) != Constants.QUERY_CHAR )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( suppressMessage ? Constants.INTERFACE_QUERY_ERROR : Constants.INTERFACE_QUERY_EMPTY_WORD_REFERENCE ) );
											else
												{
												returnQueryToPosition = true;
												showOnlyWordReferences = true;
												}
											}
										else
											{
											if( wordReferenceQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, nameStringBuffer.toString() ) == Constants.RESULT_OK )
												{
												isFirstInstruction = false;

												if( !suppressMessage &&
												countQueryResult() == 0 )
													CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_WORD_REFERENCE_WAS_FOUND ) );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query word references" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a word reference name from the query specification" );
									}
								else
									{
									returnQueryToPosition = false;
									showOnlyWordReferences = true;
									queryStringPosition_++;
									}

								break;

							case Constants.SYMBOL_BACK_SLASH:		// Escape character for string
								if( queryStringPosition_ + 1 < queryString.length() &&
								queryString.charAt( queryStringPosition_ + 1 ) != Constants.QUERY_CHAR )
									queryStringPosition_++;

								// Don't insert a break statement here

							case Constants.QUERY_STRING_START_CHAR:
								if( queryStringPosition_ + 1 < queryString.length() &&
								queryString.charAt( queryStringPosition_ + 1 ) != Constants.QUERY_CHAR )
									{
									nameStringBuffer = new StringBuffer();

									if( getNameFromQuery( queryString, nameStringBuffer, Constants.QUERY_STRING_START_CHAR, Constants.QUERY_STRING_END_CHAR ) == Constants.RESULT_OK )
										{
										if( nameStringBuffer.length() == 0 )
											{
											if( queryStringPosition_ < queryString.length() &&
											queryString.charAt( queryStringPosition_ ) != Constants.QUERY_CHAR )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( suppressMessage ? Constants.INTERFACE_QUERY_ERROR : Constants.INTERFACE_QUERY_EMPTY_STRING_SPECIFICATION ) );
											else
												{
												showOnlyStrings = true;
												returnQueryToPosition = true;
												}
											}
										else
											{
											if( stringQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, nameStringBuffer.toString() ) == Constants.RESULT_OK )
												{
												isFirstInstruction = false;

												if( !suppressMessage &&
												countQueryResult() == 0 )
													CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_STRING_WAS_FOUND ) );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query strings" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a string from the query specification" );
									}
								else
									{
									showOnlyStrings = true;
									returnQueryToPosition = false;
									queryStringPosition_++;
									}

								break;

							case Constants.QUERY_WORD_TYPE_CHAR:

								querySentenceNr_ = Constants.NO_SENTENCE_NR;

								if( getIdFromQuery( false, queryString, Constants.QUERY_WORD_TYPE_CHAR, Constants.QUERY_WORD_TYPE_CHAR ) == Constants.RESULT_OK )
									{
									if( queryItemNr_ == Constants.NO_ITEM_NR )
										{
										if( wordTypeQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, (short)querySentenceNr_ ) == Constants.RESULT_OK )
											{
											isFirstInstruction = false;
											queryWordTypeNr = (short)querySentenceNr_;		// Remember given word type number

											if( !suppressMessage &&
											countQueryResult() == 0 )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_WORD_TYPE_WAS_FOUND ) );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query word types" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The given parameter is undefined" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a word type" );

								break;

							case Constants.QUERY_PARAMETER_CHAR:

								querySentenceNr_ = Constants.NO_SENTENCE_NR;

								if( getIdFromQuery( false, queryString, Constants.QUERY_PARAMETER_CHAR, Constants.QUERY_PARAMETER_CHAR ) == Constants.RESULT_OK )
									{
									if( queryItemNr_ == Constants.NO_ITEM_NR )
										{
										if( parameterQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, querySentenceNr_ ) == Constants.RESULT_OK )
											{
											isFirstInstruction = false;

											if( !suppressMessage &&
											countQueryResult() == 0 )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_PARAMETER_WAS_FOUND ) );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query parameters" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The given parameter is undefined" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get a parameter" );

								break;

							case Constants.QUERY_CHAR:

								isEndOfQuery = true;
								queryStringPosition_++;

								break;

							case Constants.QUERY_ACTIVE_CHAR:

								if( selectActiveItems &&
								selectDeactiveItems &&
								selectArchiveItems &&
								selectDeletedItems )	// Initially
									{
									selectDeactiveItems = false;
									selectArchiveItems = false;
									selectDeletedItems = false;
									}
								else
									selectActiveItems = true;

								queryStringPosition_++;

								if( queryStringPosition_ >= queryString.length() ||
								queryString.charAt( queryStringPosition_ ) == Constants.QUERY_CHAR )	// End of query
									{
									if( itemQuery( selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, suppressMessage, CommonVariables.queryStringBuffer ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to perform an item query of active items" );
									}

								break;

							case Constants.QUERY_DEACTIVE_CHAR:

								if( selectActiveItems &&
								selectDeactiveItems &&
								selectArchiveItems &&
								selectDeletedItems )	// Initially
									{
									selectActiveItems = false;
									selectArchiveItems = false;
									selectDeletedItems = false;
									}
								else
									selectDeactiveItems = true;

								queryStringPosition_++;

								if( queryStringPosition_ >= queryString.length() ||
								queryString.charAt( queryStringPosition_ ) == Constants.QUERY_CHAR )	// End of query
									{
									if( itemQuery( selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, suppressMessage, CommonVariables.queryStringBuffer ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to perform an item query of deactive items" );
									}

								break;

							case Constants.QUERY_ARCHIVE_CHAR:

								if( selectActiveItems &&
								selectDeactiveItems &&
								selectArchiveItems &&
								selectDeletedItems )	// Initially
									{
									selectActiveItems = false;
									selectDeactiveItems = false;
									selectDeletedItems = false;
									}
								else
									selectArchiveItems = true;

								queryStringPosition_++;

								if( queryStringPosition_ >= queryString.length() ||
								queryString.charAt( queryStringPosition_ ) == Constants.QUERY_CHAR )	// End of query
									{
									if( itemQuery( selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, suppressMessage, CommonVariables.queryStringBuffer ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to perform an item query of archive items" );
									}

								break;

							case Constants.QUERY_DELETED_CHAR:

								if( selectActiveItems &&
								selectDeactiveItems &&
								selectArchiveItems &&
								selectDeletedItems )	// Initially
									{
									selectActiveItems = false;
									selectDeactiveItems = false;
									selectArchiveItems = false;
									}
								else
									selectDeletedItems = true;

								queryStringPosition_++;

								if( queryStringPosition_ >= queryString.length() ||
								queryString.charAt( queryStringPosition_ ) == Constants.QUERY_CHAR )	// End of query
									{
									if( itemQuery( selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, suppressMessage, CommonVariables.queryStringBuffer ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to perform an item query of deleted items" );
									}

								break;

							case Constants.QUERY_COUNT_CHAR:

								showCount = true;
								queryStringPosition_++;

								if( queryStringPosition_ >= queryString.length() ||
								queryString.charAt( queryStringPosition_ ) == Constants.QUERY_CHAR )	// End of query
									{
									if( itemQuery( true, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, false, Constants.NO_SENTENCE_NR, Constants.NO_ITEM_NR ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query items" );
									}

								break;

							default:
								// Set query width parameter
								if( Character.isDigit( queryString.charAt( queryStringPosition_ ) ) )
									{
									while( queryStringPosition_ < queryString.length() &&
									Character.isDigit( queryString.charAt( queryStringPosition_ ) ) &&
									queryWidth <= Constants.MAX_SENTENCE_NR / 10 )
										{
										queryWidth = ( queryWidth * 10 + queryString.charAt( queryStringPosition_ ) - '0' );
										queryStringPosition_++;
										}
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "I found an illegal character '" + queryString.charAt( queryStringPosition_ ) + "' in the query" );
							}
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( CommonVariables.queryStringBuffer.length() == 0 )
							{
							if( isFirstInstruction )	// No query performed yet
								{
								if( itemQuery( true, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, false, Constants.NO_SENTENCE_NR, Constants.NO_ITEM_NR ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to query items" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( showCount )
									{
									nTotal = countQueryResult();

									if( suppressMessage )
										CommonVariables.queryStringBuffer.append( nTotal );
									else
										{
										if( nTotal == 0 )
											CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_ITEMS_WERE_FOUND ) );
										else
											{
											if( foundMoreCategories() )		// Total only needed when more categories are found
												CommonVariables.queryStringBuffer.append( "total:" + nTotal );

											if( CommonVariables.nActiveQueryItems > 0 )
												{
												if( CommonVariables.queryStringBuffer.length() > 0 )
													CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_SPACE_STRING );

												CommonVariables.queryStringBuffer.append( "active:" + CommonVariables.nActiveQueryItems );
												}

											if( CommonVariables.nDeactiveQueryItems > 0 )
												{
												if( CommonVariables.queryStringBuffer.length() > 0 )
													CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_SPACE_STRING );

												CommonVariables.queryStringBuffer.append( "deactive:" + CommonVariables.nDeactiveQueryItems );
												}

											if( CommonVariables.nArchivedQueryItems > 0 )
												{
												if( CommonVariables.queryStringBuffer.length() > 0 )
													CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_SPACE_STRING );

												CommonVariables.queryStringBuffer.append( "archive:" + CommonVariables.nArchivedQueryItems );
												}

											if( CommonVariables.nDeletedQueryItems > 0 )
												{
												if( CommonVariables.queryStringBuffer.length() > 0 )
													CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_SPACE_STRING );

												CommonVariables.queryStringBuffer.append( "rollback:" + CommonVariables.nDeletedQueryItems );
												}
											}
										}
									}
								else
									{
									CommonVariables.foundQuery = false;

									if( showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth ) == Constants.RESULT_OK )
										{
										if( !CommonVariables.foundQuery &&
										!suppressMessage &&
										CommonVariables.queryStringBuffer.length() == 0 )
											{
											if( showOnlyWords )
												CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_WORDS_WERE_FOUND ) );
											else
												{
												if( showOnlyWordReferences )
													CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_WORD_REFERENCES_WERE_FOUND ) );
												else
													{
													if( showOnlyStrings )
														CommonVariables.queryStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_QUERY_NO_STRINGS_WERE_FOUND ) );
													}
												}
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to show the query result" );
									}
								}
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( nTotalCount() == 0 ||	// Show comment on empty query
							CommonVariables.foundQuery ||
							queryWidth > Constants.NO_CENTER_WIDTH )
								{
								if( writeQueryResult &&

								( queryWidth > Constants.NO_CENTER_WIDTH ||
								CommonVariables.queryStringBuffer.length() > 0 ) )
									{
									if( Presentation.writeText( ( !suppressMessage && !CommonVariables.foundQuery && queryWidth == Constants.NO_CENTER_WIDTH ), promptTypeNr, queryWidth, CommonVariables.queryStringBuffer ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the query result" );
									}
								}
							else
								CommonVariables.queryStringBuffer = new StringBuffer();
							}
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The current interface language word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given instruction string buffer is empty or the given instruction string position is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given instruction string buffer is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Honor the Lord, you heavenly beings;
 *	honor the Lord for his glory and strength." (Psalm 29:1)
 *
 *************************************************************************/
