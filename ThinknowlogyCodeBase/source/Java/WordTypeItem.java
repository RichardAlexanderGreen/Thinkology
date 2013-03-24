/*
 *	Class:			WordTypeItem
 *	Parent class:	Item
 *	Purpose:		To store the word-types of a word
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

class WordTypeItem extends Item
	{
	// Private loadable variables

	private boolean isDefiniteArticle_;
	private boolean isPropernamePrecededByDefiniteArticle_;

	private short wordTypeNr_;
	private short wordTypeLanguageNr_;


	// Private constructible variables

	private short writeLevel_;

	private String wordTypeString_;

	private	String hideKey_;


	// Constructor

	protected WordTypeItem( boolean isDefiniteArticle, boolean isPropernamePrecededByDefiniteArticle, short wordTypeLanguageNr, short wordTypeNr, int wordTypeStringLength, String _wordTypeString, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		isDefiniteArticle_ = isDefiniteArticle;
		isPropernamePrecededByDefiniteArticle_ = isPropernamePrecededByDefiniteArticle;

		wordTypeNr_ = wordTypeNr;
		wordTypeLanguageNr_ = wordTypeLanguageNr;

		// Private constructible variables

		writeLevel_ = Constants.NO_WRITE_LEVEL;

		hideKey_ = null;
		wordTypeString_ = null;

		if( _wordTypeString != null )
			{
			if( _wordTypeString.length() > 0 )
				{
				if( wordTypeStringLength > 0 )
					{
					if( ( wordTypeString_ = new String( Character.isUpperCase( _wordTypeString.charAt( 0 ) ) && wordTypeNr != Constants.WORD_TYPE_LETTER_CAPITAL && wordTypeNr != Constants.WORD_TYPE_PROPER_NAME ? Character.toLowerCase( _wordTypeString.charAt( 0 ) ) + _wordTypeString.substring( 1, wordTypeStringLength ) : _wordTypeString.substring( 0, wordTypeStringLength ) ) ) == null )
						setSystemErrorInItem( 1, null, null, "I failed to create the word type string" );
					}
				else
					setSystemErrorInItem( 1, null, null, "The given word type string length is undefined" );
				}
			else
				setSystemErrorInItem( 1, null, null, "The given word type string is empty" );
			}
		else
			setSystemErrorInItem( 1, null, null, "The given word type string is undefined" );
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( itemString() != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( itemString() );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( writeLevel_ == queryParameter ||
				wordTypeLanguageNr_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&

				( writeLevel_ > Constants.NO_WRITE_LEVEL ||
				wordTypeLanguageNr_ > 0 ) ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( wordTypeNr_ == queryWordTypeNr );
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// Ascending wordTypeLanguageNr
				wordTypeLanguageNr_ < ( (WordTypeItem)nextSortItem ).wordTypeLanguageNr_ );
		}

	protected String itemString()
		{
		return ( hideKey_ == null ? wordTypeString_ : null );
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String wordTypeString = myWord().wordTypeName( wordTypeNr_ );
		String grammarLanguageNameString = myWord().grammarLanguageName( wordTypeLanguageNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( hideKey_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isHidden" );

		if( isDefiniteArticle_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isDefiniteArticle" );

		if( isPropernamePrecededByDefiniteArticle_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isPropernamePrecededByDefiniteArticle" );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wordType:" + ( wordTypeString == null ? Constants.EMPTY_STRING : wordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + wordTypeNr_ );

		if( wordTypeLanguageNr_ > 0 )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "grammarLanguage:" + ( grammarLanguageNameString == null ? wordTypeLanguageNr_ : grammarLanguageNameString ) );
			}

		if( ( wordString = itemString() ) != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wordString:" + wordString );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected void clearWriteLevel( short currentWriteLevel )
		{
		if( writeLevel_ > currentWriteLevel )
			writeLevel_ = Constants.NO_WRITE_LEVEL;
		}

	protected boolean isAdjective()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_ADJECTIVE );
		}

	protected boolean isArticle()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_ARTICLE );
		}

	protected boolean isConjunction()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_CONJUNCTION );
		}

	protected boolean isDefiniteArticle()
		{
		return isDefiniteArticle_;
		}

	protected boolean isSingularNounWordType()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR );
		}

	protected boolean isPluralNounWordType()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isPossessivePronoun1()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_SINGULAR_1 ||
				wordTypeNr_ == Constants.WORD_TYPE_POSSESSIVE_PRONOUN_PLURAL_1 );
		}

	protected boolean isPropernamePrecededByDefiniteArticle()
		{
		return isPropernamePrecededByDefiniteArticle_;
		}

	protected boolean isSymbol()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_SYMBOL );
		}

	protected boolean isLetter()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_LETTER_SMALL ||
				wordTypeNr_ == Constants.WORD_TYPE_LETTER_CAPITAL );
		}

	protected boolean isNumeral()
		{
		return ( wordTypeNr_ == Constants.WORD_TYPE_NUMERAL );
		}

	protected boolean checkHiddenWordType( String compareString, String hideKey )
		{
		if( hideKey_ == hideKey &&
		compareString != null &&
		wordTypeString_.equals( compareString ) )
			return true;

		return false;
		}

	protected short wordTypeNr()
		{
		return wordTypeNr_;
		}

	protected short wordTypeLanguageNr()
		{
		return wordTypeLanguageNr_;
		}

	protected boolean isWordAlreadyWritten()
		{
		return ( writeLevel_ > Constants.NO_WRITE_LEVEL );
		}

	protected byte markWordTypeAsWritten()
		{
		if( CommonVariables.currentWriteLevel < Constants.MAX_LEVEL )
			{
			if( writeLevel_ == Constants.NO_WRITE_LEVEL )
				writeLevel_ = ++CommonVariables.currentWriteLevel;
			else
				return setErrorInItem( 1, null, itemString(), "My write level is already assigned" );
			}
		else
			return setSystemErrorInItem( 1, null, itemString(), "Current write word level overflow" );

		return CommonVariables.result;
		}

	protected byte hideWordType( String hideKey )
		{
		if( hideKey_ == null )
			hideKey_ = hideKey;
		else
			return setErrorInItem( 1, null, itemString(), "This word type is already hidden" );

		return CommonVariables.result;
		}

	protected byte createNewWordTypeString( String newWordTypeString )
		{
		if( newWordTypeString != null )
			{
			if( newWordTypeString.length() > 0 )
				wordTypeString_ = new String( newWordTypeString );
			else
				return setErrorInItem( 1, null, itemString(), "The given new word type string is empty" );
			}
		else
			return setErrorInItem( 1, null, itemString(), "The given new word type string is undefined" );

		return CommonVariables.result;
		}

	protected WordTypeItem nextWordTypeItem()
		{
		return (WordTypeItem)nextItem;
		}

	protected WordTypeItem nextCurrentLanguageWordTypeItem()
		{
		WordTypeItem nextCurrentLanguageWordTypeItem = nextWordTypeItem();

		return ( nextCurrentLanguageWordTypeItem != null &&
				nextCurrentLanguageWordTypeItem.wordTypeLanguageNr() == CommonVariables.currentGrammarLanguageNr ? nextCurrentLanguageWordTypeItem : null );
		}

	protected WordTypeItem nextActiveWordTypeItem( boolean checkAllLanguages, short wordTypeNr )
		{
		WordTypeItem searchItem = ( checkAllLanguages ? nextWordTypeItem() : nextCurrentLanguageWordTypeItem() );

		while( searchItem != null )
			{
			if( wordTypeNr == Constants.WORD_TYPE_UNDEFINED ||
			searchItem.wordTypeNr_ == wordTypeNr )
				return searchItem;

			searchItem = ( checkAllLanguages ? searchItem.nextWordTypeItem() : searchItem.nextCurrentLanguageWordTypeItem() );
			}

		return null;
		}
	};

/*************************************************************************
 *
 *	"The Lord gives his people strength.
 *	The Lord blesses them with peace." (Psalm 29:11)
 *
 *************************************************************************/
