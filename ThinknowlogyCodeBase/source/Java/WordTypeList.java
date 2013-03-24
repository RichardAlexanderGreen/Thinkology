/*
 *	Class:			WordTypeList
 *	Parent class:	List
 *	Purpose:		To store word-type items
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

class WordTypeList extends List
	{
	// Private constructible variables

	private WordTypeItem foundWordTypeItem_;


	// Private methods

	private void showWords( boolean returnQueryToPosition, WordTypeItem searchItem )
		{
		String wordTypeString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		while( searchItem != null )
			{
			if( ( wordTypeString = searchItem.itemString() ) != null )
				{
				if( CommonVariables.foundQuery ||
				CommonVariables.queryStringBuffer.length() > 0 )
					CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

				CommonVariables.foundQuery = true;

				if( !searchItem.isActiveItem() )	// Don't show status with active items
					CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + searchItem.statusChar() );

				CommonVariables.queryStringBuffer.append( wordTypeString );
				}

			searchItem = searchItem.nextWordTypeItem();
			}
		}

	private String wordTypeString( boolean isCurrentLanguage, short wordTypeNr, WordTypeItem searchItem )
		{
		while( searchItem != null )
			{
			if( searchItem.wordTypeNr() == wordTypeNr )
				return searchItem.itemString();
			else
				{
				if( foundWordTypeItem_ == null )
					foundWordTypeItem_ = searchItem;
				}

			searchItem = ( isCurrentLanguage ? searchItem.nextCurrentLanguageWordTypeItem() : searchItem.nextWordTypeItem() );
			}

		return null;
		}

	// Constructor

	protected WordTypeList( WordItem myWord )
		{
		foundWordTypeItem_ = null;
		initializeListVariables( Constants.WORD_TYPE_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected void showWords( boolean returnQueryToPosition )
		{
		showWords( returnQueryToPosition, firstActiveWordTypeItem() );
		showWords( returnQueryToPosition, firstArchiveWordTypeItem() );
		showWords( returnQueryToPosition, firstDeletedWordTypeItem() );
		}

	protected void clearWriteLevel( short currentWriteLevel )
		{
		WordTypeItem searchItem = firstActiveWordTypeItem();

		while( searchItem != null )
			{
			searchItem.clearWriteLevel( currentWriteLevel );
			searchItem = searchItem.nextWordTypeItem();
			}
		}

	protected boolean checkHiddenWordType( short wordTypeNr, String compareString, String authorizationKey )
		{
		WordTypeItem searchItem = firstActiveWordTypeItem();

		while( searchItem != null )
			{
			if( searchItem.wordTypeNr() == wordTypeNr &&
			searchItem.checkHiddenWordType( compareString, authorizationKey ) )
				return true;

			searchItem = searchItem.nextWordTypeItem();
			}

		return false;
		}

	protected byte hideWordTypeItem( short wordTypeNr, String authorizationKey )
		{
		boolean foundWordType = false;
		WordTypeItem searchItem = firstActiveWordTypeItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!foundWordType )
			{
			if( searchItem.wordTypeNr() == wordTypeNr )
				{
				if( searchItem.hideWordType( authorizationKey ) == Constants.RESULT_OK )
					foundWordType = true;
				else
					addError( 1, null, "I failed to hide a word type" );
				}
			else
				searchItem = searchItem.nextWordTypeItem();
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		!foundWordType )
			return setError( 1, null, "I coundn't find the given word type" );

		return CommonVariables.result;
		}

	protected byte deleteWordType( short wordTypeNr )
		{
		boolean foundWordType = false;
		WordTypeItem searchItem = firstActiveCurrentLanguageWordTypeItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!foundWordType )
			{
			if( searchItem.wordTypeNr() == wordTypeNr )
				{
				if( deleteActiveItem( false, searchItem ) == Constants.RESULT_OK )
					foundWordType = true;
				else
					addError( 1, null, "I failed to delete an active item" );
				}
			else
				searchItem = searchItem.nextCurrentLanguageWordTypeItem();
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		!foundWordType )
			return setError( 1, null, "I coundn't find the given word type" );

		return CommonVariables.result;
		}

	protected byte findMatchingWordReferenceString( String searchString )
		{
		WordTypeItem searchItem = firstActiveWordTypeItem();

		CommonVariables.foundMatchingStrings = false;

		while( CommonVariables.result == Constants.RESULT_OK &&
		!CommonVariables.foundMatchingStrings &&
		searchItem != null )
			{
			if( searchItem.itemString() != null )
				{
				if( compareStrings( searchString, searchItem.itemString() ) == Constants.RESULT_OK )
					{
					if( CommonVariables.foundMatchingStrings )
						CommonVariables.matchingWordTypeNr = searchItem.wordTypeNr();
					}
				else
					addError( 1, null, "I failed to compare an active word type string with the query string" );
				}

			searchItem = searchItem.nextWordTypeItem();
			}

		searchItem = firstArchiveWordTypeItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		!CommonVariables.foundMatchingStrings &&
		searchItem != null )
			{
			if( searchItem.itemString() != null )
				{
				if( compareStrings( searchString, searchItem.itemString() ) == Constants.RESULT_OK )
					{
					if( CommonVariables.foundMatchingStrings )
						CommonVariables.matchingWordTypeNr = searchItem.wordTypeNr();
					}
				else
					addError( 1, null, "I failed to compare a deleted word type string with the query string" );
				}

			searchItem = searchItem.nextWordTypeItem();
			}

		searchItem = firstDeletedWordTypeItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		!CommonVariables.foundMatchingStrings &&
		searchItem != null )
			{
			if( searchItem.itemString() != null )
				{
				if( compareStrings( searchString, searchItem.itemString() ) == Constants.RESULT_OK )
					{
					if( CommonVariables.foundMatchingStrings )
						CommonVariables.matchingWordTypeNr = searchItem.wordTypeNr();
					}
				else
					addError( 1, null, "I failed to compare a deleted word type string with the query string" );
				}

			searchItem = searchItem.nextWordTypeItem();
			}

		return CommonVariables.result;
		}

	protected byte markWordTypeAsWritten( boolean isPredefinedWord, short wordTypeNr )
		{
		boolean foundWordTypeNr = false;
		WordTypeItem pluralNounWordTypeItem = null;
		WordTypeItem singularNounWordTypeItem = null;
		WordTypeItem searchItem = firstActiveCurrentLanguageWordTypeItem();

		if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		wordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			// Check current language first
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( !searchItem.isWordAlreadyWritten() )
					{
					if( searchItem.isSingularNounWordType() )
						singularNounWordTypeItem = searchItem;
					else
						{
						if( searchItem.isPluralNounWordType() )
							pluralNounWordTypeItem = searchItem;
						}

					if( searchItem.wordTypeNr() == wordTypeNr )
						{
						if( searchItem.markWordTypeAsWritten() == Constants.RESULT_OK )
							foundWordTypeNr = true;
						else
							addError( 1, null, "I failed to mark a word as written" );
						}
					}

				searchItem = searchItem.nextCurrentLanguageWordTypeItem();
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			!foundWordTypeNr &&
			!isPredefinedWord )
				{
				// Not found in current language. Now, try all languages
				searchItem = firstActiveWordTypeItem();

				while( CommonVariables.result == Constants.RESULT_OK &&
				searchItem != null )
					{
					if( !searchItem.isWordAlreadyWritten() )
						{
						if( searchItem.isSingularNounWordType() )		// Found singular noun
							singularNounWordTypeItem = searchItem;
						else
							{
							if( searchItem.isPluralNounWordType() )	// Found plural noun
								pluralNounWordTypeItem = searchItem;
							}

						if( searchItem.wordTypeNr() == wordTypeNr )
							{
							if( searchItem.markWordTypeAsWritten() == Constants.RESULT_OK )
								foundWordTypeNr = true;
							else
								addError( 1, null, "I failed to mark a word as written" );
							}

						searchItem = searchItem.nextWordTypeItem();
						}
					}
				}

			if( CommonVariables.result == Constants.RESULT_OK )
				{
				if( foundWordTypeNr )
					{
					if( wordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR &&	// If singular noun - also set plural noun
					pluralNounWordTypeItem != null )
						{
						if( pluralNounWordTypeItem.markWordTypeAsWritten() != Constants.RESULT_OK )
							addError( 1, null, "I failed to mark a plural noun word as written" );
						}
					else
						{
						if( wordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL &&	// If plural noun - also set singular noun
						singularNounWordTypeItem != null )
							{
							if( singularNounWordTypeItem.markWordTypeAsWritten() != Constants.RESULT_OK )
								addError( 1, null, "I failed to mark a singular noun word as written" );
							}
						}
					}
				else
					return setError( 1, null, "I couldn't find the given word type number" );
				}
			}
		else
			return setError( 1, null, "The given word type number is undefined or out of bounds" );

		return CommonVariables.result;
		}

	protected byte createWordTypeItem( boolean isDefiniteArticle, boolean isPropernamePrecededByDefiniteArticle, short wordTypeNr, int wordLength, String wordTypeString )
		{
		WordTypeItem newItem;

		if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		wordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( wordTypeString != null )
				{
				if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
					{
					if( ( newItem = new WordTypeItem( isDefiniteArticle, isPropernamePrecededByDefiniteArticle, CommonVariables.currentGrammarLanguageNr, wordTypeNr, wordLength, wordTypeString, this, myWord() ) ) != null )
						{
						if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
							addError( 1, null, "I failed to add a word type item" );
						}
					else
						return setError( 1, null, "I failed to create a word type item" );
					}
				else
					return setError( 1, null, "The current item number is undefined" );
				}
			else
				return setError( 1, null, "The given wordTypeString is undefined" );
			}
		else
			return setError( 1, null, "The given word type number is undefined or out of bounds" );

		return CommonVariables.result;
		}

	protected WordResultType checkWordTypeForBeenWritten( boolean isPredefinedWord, short wordTypeNr )
		{
		WordResultType wordResult = new WordResultType();
		WordTypeItem searchItem = firstActiveCurrentLanguageWordTypeItem();

		if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		wordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			// Check current language first
			while( searchItem != null &&
			!wordResult.isWordAlreadyWritten )
				{
				if( searchItem.isWordAlreadyWritten() &&
				searchItem.wordTypeNr() == wordTypeNr )
					wordResult.isWordAlreadyWritten = true;
				else
					searchItem = searchItem.nextCurrentLanguageWordTypeItem();
				}

			if( !isPredefinedWord &&
			!wordResult.isWordAlreadyWritten )
				{
				// Not found in current language. Now, try all languages
				searchItem = firstActiveWordTypeItem();

				while( searchItem != null &&
				!wordResult.isWordAlreadyWritten )
					{
					if( searchItem.isWordAlreadyWritten() &&
					searchItem.wordTypeNr() == wordTypeNr )
						wordResult.isWordAlreadyWritten = true;
					else
						searchItem = searchItem.nextWordTypeItem();
					}
				}
			}
		else
			setError( 1, null, "The given word type number is undefined or out of bounds" );

		wordResult.result = CommonVariables.result;
		return wordResult;
		}

	protected String wordTypeString( boolean checkAllLanguages, short wordTypeNr )
		{
		String foundWordTypeString;

		foundWordTypeItem_ = null;

		// Try to find word type from the current language
		if( ( foundWordTypeString = wordTypeString( true, wordTypeNr, firstActiveCurrentLanguageWordTypeItem() ) ) == null )
			{
			if( ( foundWordTypeString = wordTypeString( true, wordTypeNr, firstArchiveCurrentLanguageWordTypeItem() ) ) == null )
				{
				if( ( foundWordTypeString = wordTypeString( true, wordTypeNr, firstDeletedCurrentLanguageWordTypeItem() ) ) == null )
					{
					// Not found in current language. Now, try all languages
					if( checkAllLanguages ||
					myWord().isGrammarLanguage() )
						{
						if( ( foundWordTypeString = wordTypeString( false, wordTypeNr, firstActiveWordTypeItem() ) ) == null )
							{
							if( ( foundWordTypeString = wordTypeString( false, wordTypeNr, firstArchiveWordTypeItem() ) ) == null )
								foundWordTypeString = wordTypeString( false, wordTypeNr, firstDeletedWordTypeItem() );
							}
						}
					}
				}
			}

		return ( foundWordTypeString == null ? ( foundWordTypeItem_ == null ? null : foundWordTypeItem_.itemString() ) : foundWordTypeString );
		}

	protected String activeWordTypeString( short wordTypeNr )
		{
		WordTypeItem foundWordTypeItem;

		if( ( foundWordTypeItem = activeWordTypeItem( wordTypeNr ) ) != null )
			return foundWordTypeItem.itemString();

		return null;
		}

	protected WordTypeItem activeWordTypeItem( short wordTypeNr )
		{
		WordTypeItem searchItem = firstActiveCurrentLanguageWordTypeItem();

		// Check current language first
		while( searchItem != null )
			{
			if( wordTypeNr == Constants.WORD_TYPE_UNDEFINED ||
			searchItem.wordTypeNr() == wordTypeNr )
				return searchItem;

			searchItem = searchItem.nextCurrentLanguageWordTypeItem();
			}

		// Not found in current language. Now, try all languages
		if( myWord().isGrammarLanguage() )
			{
			searchItem = firstActiveWordTypeItem();

			while( searchItem != null )
				{
				if( wordTypeNr == Constants.WORD_TYPE_UNDEFINED ||
				searchItem.wordTypeNr() == wordTypeNr )
					return searchItem;

				searchItem = searchItem.nextWordTypeItem();
				}
			}

		return null;
		}

	protected WordTypeItem firstActiveWordTypeItem()
		{
		return (WordTypeItem)firstActiveItem();
		}

	protected WordTypeItem firstActiveCurrentLanguageWordTypeItem()
		{
		WordTypeItem searchItem = firstActiveWordTypeItem();

		while( searchItem != null &&
		searchItem.wordTypeLanguageNr() < CommonVariables.currentGrammarLanguageNr )
			searchItem = searchItem.nextWordTypeItem();

		return searchItem;
		}

	protected WordTypeItem firstArchiveWordTypeItem()
		{
		return (WordTypeItem)firstArchiveItem();
		}

	protected WordTypeItem firstArchiveCurrentLanguageWordTypeItem()
		{
		WordTypeItem searchItem = firstArchiveWordTypeItem();

		while( searchItem != null &&
		searchItem.wordTypeLanguageNr() < CommonVariables.currentGrammarLanguageNr )
			searchItem = searchItem.nextWordTypeItem();

		return searchItem;
		}

	protected WordTypeItem firstDeletedWordTypeItem()
		{
		return (WordTypeItem)firstDeleteItem();
		}

	protected WordTypeItem firstDeletedCurrentLanguageWordTypeItem()
		{
		WordTypeItem searchItem = firstDeletedWordTypeItem();

		while( searchItem != null &&
		searchItem.wordTypeLanguageNr() < CommonVariables.currentGrammarLanguageNr )
			searchItem = searchItem.nextWordTypeItem();

		return searchItem;
		}
	};

/*************************************************************************
 *
 *	"He lifts the poor from the dust
 *	and needy from the garbage dump.
 *	He sets them among princes,
 *	even princes of his own people!" (Psalm 113:7-8)
 *
 *************************************************************************/
