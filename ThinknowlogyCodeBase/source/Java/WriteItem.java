/*
 *	Class:			WriteItem
 *	Parent class:	Item
 *	Purpose:		To temporarily store info about a word
 *					during the process of writing a sentence
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

class WriteItem extends Item
	{
	// Private constructible variables

	private boolean isDeadEndRoad_;


	// Private loadable variables

	private boolean isChoiceEnd_;

	private short grammarLevel_;

	private GrammarItem startOfChoiceOrOptionGrammarItem_;


	// Protected constructible variables

	protected boolean wasSuccessful;

	protected short writeLevel;


	// Protected loadable variables

	protected boolean isSkipped;


	// Constructor

	protected WriteItem( boolean _isSkipped, boolean isChoiceEnd, short grammarLevel, GrammarItem startOfChoiceOrOptionGrammarItem, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private constructible variables

		isDeadEndRoad_ = false;

		// Private loadable variables

		isChoiceEnd_ = isChoiceEnd;
		grammarLevel_ = grammarLevel;

		// Protected constructible variables

		wasSuccessful = false;
		writeLevel = Constants.NO_WRITE_LEVEL;

		// Protected loadable variables

		isSkipped = _isSkipped;

		if( ( startOfChoiceOrOptionGrammarItem_ = startOfChoiceOrOptionGrammarItem ) == null )
			setSystemErrorInItem( 1, null, null, "The given start of grammar choice or option grammar item is undefined" );
		}


	// Protected virtual methods

	protected boolean checkParameter( int queryParameter )
		{
		return ( writeLevel == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				writeLevel > Constants.NO_WRITE_LEVEL ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( startOfChoiceOrOptionGrammarItem_ == null ? false :
				( querySentenceNr == Constants.NO_SENTENCE_NR ? true : startOfChoiceOrOptionGrammarItem_.creationSentenceNr() == querySentenceNr ) &&
				( queryItemNr == Constants.NO_ITEM_NR ? true : startOfChoiceOrOptionGrammarItem_.itemNr() == queryItemNr ) );
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		baseToStringBuffer( queryWordTypeNr );

		if( isSkipped )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSkipped" );

		if( isChoiceEnd_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isChoiceEnd" );

		if( isDeadEndRoad_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isDeadEndRoad" );

		if( wasSuccessful )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wasSuccessful" );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "grammarLevel:" + grammarLevel_ );

		if( writeLevel > Constants.NO_WRITE_LEVEL )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "writeLevel:" + writeLevel );

		if( startOfChoiceOrOptionGrammarItem_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "startOfChoiceOrOptionGrammar" + Constants.QUERY_REF_ITEM_START_CHAR + startOfChoiceOrOptionGrammarItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + startOfChoiceOrOptionGrammarItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isDeadEndRoad()
		{
		return isDeadEndRoad_;
		}

	protected boolean foundNewPathInGrammarRecursion()
		{
		WriteItem searchItem = this;
		WriteItem newPossibilityWriteItem = null;

		do	{
			if( searchItem.wasSuccessful &&
			!searchItem.isChoiceEnd_ &&
			!searchItem.isDeadEndRoad() &&

			( newPossibilityWriteItem == null ||
			searchItem.writeLevel > newPossibilityWriteItem.writeLevel ) )
				newPossibilityWriteItem = searchItem;

			searchItem = searchItem.nextWriteItem();
			}
		while( searchItem != null &&
		searchItem.grammarLevel_ >= grammarLevel_ );

		if( newPossibilityWriteItem != null )
			{
			searchItem = newPossibilityWriteItem;
			newPossibilityWriteItem.isDeadEndRoad_ = true;

			while( ( searchItem = searchItem.nextWriteItem() ) != null )
				searchItem.isDeadEndRoad_ = false;

			return true;
			}

		return false;
		}

	protected short grammarLevel()
		{
		return grammarLevel_;
		}

	protected GrammarItem startOfChoiceOrOptionGrammarItem()
		{
		return startOfChoiceOrOptionGrammarItem_;
		}

	protected WriteItem nextWriteItem()
		{
		return (WriteItem)nextItem;
		}
	};

/*************************************************************************
 *
 *	"Those who are wise will take all this to heart;
 *	they will see in our history the faithful love
 *	of the Lord." (Psalm 107:43)
 *
 *************************************************************************/
