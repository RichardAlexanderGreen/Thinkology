/*
 *	Class:			WriteList
 *	Parent class:	List
 *	Purpose:		To temporarily store write items
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

class WriteList extends List
	{
	// Constructor

	protected WriteList( WordItem myWord )
		{
		initializeListVariables( Constants.WORD_WRITE_LIST_SYMBOL, myWord );
		}


	// Protected virtual methods

	protected boolean isTemporaryList()
		{
		return true;
		}


	// Protected methods

	protected void clearFromWriteLevel( short writeLevel )
		{
		WriteItem searchItem = firstActiveWriteItem();

		while( searchItem != null )
			{
			if( searchItem.writeLevel >= writeLevel )
				searchItem.writeLevel = Constants.NO_WRITE_LEVEL;

			searchItem = searchItem.nextWriteItem();
			}
		}

	protected byte checkGrammarItemForUsage( GrammarItem unusedGrammarItem )
		{
		WriteItem searchItem = firstActiveWriteItem();

		if( unusedGrammarItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.startOfChoiceOrOptionGrammarItem() == unusedGrammarItem )
					return setError( 1, null, "The start of choice or option grammar item is still in use" );

				searchItem = searchItem.nextWriteItem();
				}
			}
		else
			return setError( 1, null, "The given unused grammar item is undefined" );

		return CommonVariables.result;
		}

	protected WriteResultType createWriteItem( boolean isSkipped, boolean isChoiceEnd, short grammarLevel, GrammarItem startOfChoiceOrOptionGrammarItem )
		{
		WriteResultType writeResult = new WriteResultType();

		if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
			{
			if( ( writeResult.createdWriteItem = new WriteItem( isSkipped, isChoiceEnd, grammarLevel, startOfChoiceOrOptionGrammarItem, this, myWord() ) ) != null )
				{
				if( addItemToActiveList( (Item)writeResult.createdWriteItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add a write item" );
				}
			else
				setError( 1, null, "I failed to create a grammar item" );
			}
		else
			setError( 1, null, "The current item number is undefined" );

		writeResult.result = CommonVariables.result;
		return writeResult;
		}

	protected WriteItem firstActiveWriteItem()
		{
		return (WriteItem)firstActiveItem();
		}
	};

/*************************************************************************
 *
 *	"God says, "At the time I have planned,
 *	I will bring justice against the wicked.
 *	When the earth quackes and its people live in turmoil,
 *	I am the one that keeps its foundations firm." (Psalm 75:2-3)
 *
 *************************************************************************/
