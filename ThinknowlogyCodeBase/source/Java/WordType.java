/*
 *	Class:			WordType
 *	Supports class:	WordItem
 *	Purpose:		To create word-type structures
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

class WordType
	{
	// Private constructible variables

	private WordItem myWord_;
	private String moduleNameString_;


	// Constructor

	protected WordType( WordItem myWord )
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


	// Protected methods

	protected byte createWordType( boolean isDefiniteArticle, boolean isPropernamePrecededByDefiniteArticle, short wordTypeNr, int wordLength, String wordTypeString )
		{
		WordResultType wordResult = new WordResultType();

		if( !myWord_.iAmAdmin() )
			{
			if( myWord_.wordTypeList == null )
				{
				if( ( myWord_.wordTypeList = new WordTypeList( myWord_ ) ) != null )
					myWord_.wordList[Constants.WORD_TYPE_LIST] = myWord_.wordTypeList;
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "I failed to create a word type list" );
				}
			else
				{
				if( ( wordResult = findWordType( false, false, wordTypeNr, wordLength, wordTypeString ) ).result == Constants.RESULT_OK )
					{
					if( wordResult.foundWordTypeItem != null )
						return myWord_.setErrorInWord( 1, moduleNameString_, "The given word type already exists" );
					}
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find the given word type" );
				}

			return myWord_.wordTypeList.createWordTypeItem( isDefiniteArticle, isPropernamePrecededByDefiniteArticle, wordTypeNr, wordLength, wordTypeString );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The admin does not have word types" );
		}

	protected WordResultType findWordType( boolean checkAllLanguages, boolean firstLetterHasDifferentCase, short searchWordTypeNr, int searchWordStringLength, String searchWordString )
		{
		WordResultType wordResult = new WordResultType();
		int activeWordStringLength;
		String activeWordString;
		WordTypeItem currentWordTypeItem;

		if( searchWordString != null )
			{
			if( searchWordStringLength > 0 )
				{
				if( ( currentWordTypeItem = myWord_.activeWordTypeItem( searchWordTypeNr ) ) != null )
					{
					do	{
						if( ( activeWordString = currentWordTypeItem.itemString() ) != null )	// Could be hidden word type
							{
							if( ( activeWordStringLength = activeWordString.length() ) > 0 )
								{
								if( searchWordStringLength == activeWordStringLength )
									{
									if( firstLetterHasDifferentCase )
										{
										if( searchWordString.charAt( 0 ) != activeWordString.charAt( 0 ) &&
										Character.toLowerCase( searchWordString.charAt( 0 ) ) == Character.toLowerCase( activeWordString.charAt( 0 ) ) )
											{
												if( searchWordString.substring( 1 ).startsWith( activeWordString.substring( 1 ) ) )
												wordResult.foundWordTypeItem = currentWordTypeItem;
											}
										}
									else
										{
											if( searchWordString.startsWith( activeWordString ) )
											wordResult.foundWordTypeItem = currentWordTypeItem;
										}

									if( checkAllLanguages &&
									searchWordTypeNr == Constants.WORD_TYPE_UNDEFINED &&
									wordResult.foundWordTypeItem != null &&
									currentWordTypeItem.wordTypeLanguageNr() != CommonVariables.currentGrammarLanguageNr )
										{
										// Check for language mix-up
										if( ( wordResult = findWordTypeInAllWords( false, firstLetterHasDifferentCase, Constants.WORD_TYPE_UNDEFINED, searchWordStringLength, searchWordString ) ).result == Constants.RESULT_OK )
											{
											if( wordResult.foundWordItem == null )
												{
												CommonVariables.foundLanguageMixUp = true;
												wordResult.foundWordTypeItem = currentWordTypeItem;		// Need to re-assign, because of method findWordTypeInAllWords
												}
											else
												wordResult.foundWordTypeItem = null;
											}
										else
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a word type in the current language" );
										}
									}
								}
							else
								myWord_.setErrorInWord( 1, moduleNameString_, "The active word type string is empty" );
							}
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					wordResult.foundWordTypeItem == null &&
					( currentWordTypeItem = currentWordTypeItem.nextActiveWordTypeItem( checkAllLanguages, searchWordTypeNr ) ) != null );
					}
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The given word type string is empty" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given word type string is undefined" );

		wordResult.result = CommonVariables.result;
		return wordResult;
		}

	protected WordResultType findWordTypeInAllWords( boolean checkAllLanguages, boolean firstLetterHasDifferentCase, short searchWordTypeNr, int searchWordStringLength, String searchWordString )
		{
		WordResultType wordResult = new WordResultType();
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( wordResult = currentWordItem.findWordType( checkAllLanguages, firstLetterHasDifferentCase, searchWordTypeNr, searchWordStringLength, searchWordString ) ).result == Constants.RESULT_OK )
					{
					if( wordResult.foundWordTypeItem != null )
						wordResult.foundWordItem = currentWordItem;
					}
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a word type in word \"" + currentWordItem.anyWordTypeString() + "\"" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			wordResult.foundWordItem == null &&
			( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		wordResult.result = CommonVariables.result;
		return wordResult;
		}

	protected String wordTypeString( short wordTypeNr )
		{
		String wordTypeString = null;

		if( myWord_.wordTypeList == null ||
		( wordTypeString = myWord_.wordTypeList.wordTypeString( true, wordTypeNr ) ) == null )
			return ( Constants.QUERY_ITEM_START_STRING + myWord_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + myWord_.itemNr() + Constants.QUERY_ITEM_END_CHAR );

		return wordTypeString;
		}
	};

/*************************************************************************
 *
 *	"Let them praise your great and awesome name.
 *	Your name is holy!" (Psalm 99:3)
 *
 *************************************************************************/
