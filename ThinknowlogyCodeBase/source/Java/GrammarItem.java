/*
 *	Class:			GrammarItem
 *	Parent class:	Item
 *	Purpose:		To store info about the grammar of a language, which
 *					will be used for reading as well as writing sentences
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

class GrammarItem extends Item
{
	// Private loadable variables

	private boolean isDefinitionStart_;
	private boolean isNewStart_;
	private boolean isOptionStart_;
	private boolean isChoiceStart_;
	private boolean skipOptionForWriting_;

	private short grammarParameter_;
	private short grammarWordTypeNr_;

	private String grammarString_;


	// Protected constructible variables

	protected boolean isOptionEnd;
	protected boolean isChoiceEnd;
	protected boolean isGrammarItemInUse;

	protected short selectedByUserSelectionNr;
	protected short skippedByUserSelectionNr;

	protected GrammarItem nextDefinitionGrammarItem;


	// Protected loadable variables

	protected GrammarItem definitionGrammarItem;


	// Private methods

	private GrammarItem pluralNounEndingGrammarItem( boolean includeThisItem )
	{
		GrammarItem searchItem = ( includeThisItem ? this : nextGrammarItem() );

		while( searchItem != null )
		{
			if( searchItem.isPluralNounEnding() )
				return searchItem;

			searchItem = searchItem.nextGrammarItem();
		}

		return null;
	}


	// Constructor
	
	// TODO: See if we can fix these monsters.
	// My suspect that an option object should be passed instead of these option lists.

	protected GrammarItem( boolean isDefinitionStart
							, boolean isNewStart
							, boolean isOptionStart
							, boolean isChoiceStart
							, boolean skipOptionForWriting
							, short grammarWordTypeNr
							, short grammarParameter
							, int grammarStringLength
							, String grammarString
							, GrammarItem _grammarDefinitionWordItem
							, List myList
							, WordItem myWord )
	{
		initializeItemVariables( Constants.NO_SENTENCE_NR
								, Constants.NO_SENTENCE_NR
								, Constants.NO_SENTENCE_NR
								, Constants.NO_SENTENCE_NR
								, myList
								, myWord );

		// Private loadable variables

		isDefinitionStart_ = isDefinitionStart;
		isNewStart_ = isNewStart;
		isOptionStart_ = isOptionStart;
		isChoiceStart_ = isChoiceStart;
		skipOptionForWriting_ = skipOptionForWriting;

		grammarParameter_ = grammarParameter;
		grammarWordTypeNr_ = grammarWordTypeNr;

		grammarString_ = null;

		if( grammarString != null )
		{
			if( ( grammarString_ = new String( grammarString.substring( 0, grammarStringLength ) ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create the grammar string" );
		}
		else
			setSystemErrorInItem( 1, null, null, "The given grammar string is undefined" );


		// Protected constructible variables

		isOptionEnd = false;
		isChoiceEnd = false;
		isGrammarItemInUse = false;

		selectedByUserSelectionNr = 0;
		skippedByUserSelectionNr = 0;

		nextDefinitionGrammarItem = null;

		// Protected loadable variables

		definitionGrammarItem = _grammarDefinitionWordItem;
	}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
	{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( grammarString_ != null )
		{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING 
					                                                            : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( grammarString_ );
		}
	}

	protected boolean checkParameter( int queryParameter )
	{
		return ( grammarParameter_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				grammarParameter_ > Constants.NO_GRAMMAR_PARAMETER ) );
	}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
	{
		// TODO: See if we can make this more readable.  
		// OMG -- It combines && with || -- what is the prededence rule ???
		
		return ( ( definitionGrammarItem == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : definitionGrammarItem.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr     == Constants.NO_ITEM_NR     ? true : definitionGrammarItem.itemNr() == queryItemNr ) ) ||

				( nextDefinitionGrammarItem == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : nextDefinitionGrammarItem.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr     == Constants.NO_ITEM_NR     ? true : nextDefinitionGrammarItem.itemNr() == queryItemNr ) ) );
	}

	protected boolean checkWordType( short queryWordTypeNr )
	{
		return ( grammarWordTypeNr_ == queryWordTypeNr );
	}

	protected byte checkForUsage()
	{
		return myWord().checkGrammarForUsageInWord( this );
	}

	protected String itemString()
	{
		return grammarString_;
	}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
	{
		String grammarWordTypeString = myWord().wordTypeName( grammarWordTypeNr_ );
		baseToStringBuffer( queryWordTypeNr );
/*
		if( isUsed )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isUsed" );
*/
		if( isDefinitionStart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isDefinitionStart" );

		if( isNewStart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isNewStart" );

		if( isOptionStart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isOptionStart" );

		if( isOptionEnd )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isOptionEnd" );

		if( isChoiceStart_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isChoiceStart" );

		if( isChoiceEnd )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isChoiceEnd" );

		if( skipOptionForWriting_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "skipOptionForWriting" );

		if( isGrammarItemInUse )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isGrammarItemInUse" );

		if( selectedByUserSelectionNr > 0 )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "selectedByUserSelectionNr:" + selectedByUserSelectionNr );

		if( skippedByUserSelectionNr > 0 )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "skippedByUserSelectionNr:" + skippedByUserSelectionNr );

		if( grammarParameter_ > Constants.NO_GRAMMAR_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "grammarParameter:" + grammarParameter_ );

		if( grammarWordTypeNr_ > Constants.WORD_TYPE_UNDEFINED )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING 
				                                    + "grammarWordType:" 
													+ ( grammarWordTypeString == null ? Constants.QUERY_WORD_TYPE_STRING + grammarWordTypeNr_ 
														                              : grammarWordTypeString ) 
													);

		if( definitionGrammarItem != null )
		{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING 
				                                    + "definitionGrammarItem" 
													+ Constants.QUERY_REF_ITEM_START_CHAR 
													+ definitionGrammarItem.creationSentenceNr() 
													+ Constants.QUERY_SEPARATOR_CHAR 
													+ definitionGrammarItem.itemNr() 
													+ Constants.QUERY_REF_ITEM_END_CHAR 
													);
		}

		if( nextDefinitionGrammarItem != null )
		{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING 
													+ "nextDefinitionGrammar" + Constants.QUERY_REF_ITEM_START_CHAR 
													+ nextDefinitionGrammarItem.creationSentenceNr() 
													+ Constants.QUERY_SEPARATOR_CHAR 
													+ nextDefinitionGrammarItem.itemNr() 
													+ Constants.QUERY_REF_ITEM_END_CHAR 
													);
		}

		if( grammarString_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "grammarString:" + grammarString_ );

		return CommonVariables.queryStringBuffer;
	}


	// Protected methods

	protected boolean allowToEnterNewWord()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_TYPE_PROPER_NAME ||
				grammarWordTypeNr_ == Constants.WORD_TYPE_ADJECTIVE ||
				grammarWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR ||
				grammarWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
	}

	protected boolean isDefinitionStart()
	{
		return isDefinitionStart_;
	}

	protected boolean isNewStart()
	{
		return isNewStart_;
	}

	protected boolean isOptionStart()
	{
		return isOptionStart_;
	}

	protected boolean isChoiceStart()
	{
		return isChoiceStart_;
	}

	protected boolean skipOptionForWriting()
	{
		return skipOptionForWriting_;
	}

	protected boolean hasGrammarParameter()
	{
		return ( grammarParameter_ > Constants.NO_GRAMMAR_PARAMETER );
	}

	protected boolean isPredefinedWord()
	{
		return ( grammarParameter_ > Constants.NO_GRAMMAR_PARAMETER );
	}

	protected boolean isUserDefinedWord()
	{
		return ( grammarParameter_ == Constants.NO_GRAMMAR_PARAMETER );
	}

	protected boolean isPluralNounEnding()
	{
		return ( grammarParameter_ == Constants.WORD_PLURAL_NOUN_ENDINGS );
	}

	protected boolean isGrammarWord()
	{
		return ( grammarParameter_ < Constants.GRAMMAR_SENTENCE );
	}

	protected boolean isGrammarStart()
	{
		return ( grammarParameter_ == Constants.GRAMMAR_SENTENCE );
	}

	protected boolean isWordTypeNumeral()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_TYPE_NUMERAL );
	}

	protected boolean isWordTypeSingularNoun()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR );
	}

	protected boolean isWordTypePluralNoun()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
	}

	protected boolean isWordTypeDefinedArticle()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_NEUTER ||
				 grammarWordTypeNr_ == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_MALE_FEMALE );
	}

	protected boolean isWordTypeText()
	{
		return ( grammarWordTypeNr_ == Constants.WORD_TYPE_TEXT );
	}

	protected boolean isIdentical( GrammarItem checkGrammarItem )
	{
		// TODO -- This should probably be an iteration on a collection of parameters.
		return ( checkGrammarItem != null &&
				isNewStart_        == checkGrammarItem.isNewStart() &&
				isOptionStart_     == checkGrammarItem.isOptionStart() &&
				isOptionEnd        == checkGrammarItem.isOptionEnd &&
				isChoiceStart_     == checkGrammarItem.isChoiceStart() &&
				isChoiceEnd        == checkGrammarItem.isChoiceEnd &&
				grammarParameter_  == checkGrammarItem.grammarParameter() &&
				grammarWordTypeNr_ == checkGrammarItem.grammarWordTypeNr() &&
				itemNr()           == checkGrammarItem.itemNr() &&
				grammarString_ != null &&
				checkGrammarItem.grammarString() != null &&
				grammarString_.equals( checkGrammarItem.grammarString() ) );
	}

	protected short grammarParameter()
	{
		return grammarParameter_;
	}

	protected short grammarWordTypeNr()
	{
		return grammarWordTypeNr_;
	}

	protected String grammarString()
	{
		return grammarString_;
	}

	protected GrammarItem nextGrammarItem()
	{
		return (GrammarItem)nextItem;
	}

	protected GrammarItem firstPluralNounEndingGrammarItem()
	{
		return pluralNounEndingGrammarItem( true );
	}

	protected GrammarItem nextPluralNounEndingGrammarItem()
	{
		return pluralNounEndingGrammarItem( false );
	}
};
