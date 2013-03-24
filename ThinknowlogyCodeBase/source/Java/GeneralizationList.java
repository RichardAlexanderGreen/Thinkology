/*
 *	Class:			GeneralizationList
 *	Parent class:	List
 *	Purpose:		To store generalization items
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

class GeneralizationList extends List
	{
	// Constructor

	protected GeneralizationList( WordItem myWord )
		{
		initializeListVariables( Constants.WORD_GENERALIZATION_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected GeneralizationResultType findGeneralizationItem( boolean isRelation, short questionParameter, short generalizationWordTypeNr, WordItem generalizationWordItem )
		{
		GeneralizationResultType generalizationResult = new GeneralizationResultType();
		GeneralizationItem searchItem = firstActiveGeneralizationItem();

		if( generalizationWordItem != null )
			{
			while( searchItem != null &&
			!generalizationResult.foundGeneralization )
				{
				if( searchItem.isRelation() == isRelation &&
				searchItem.questionParameter() == questionParameter &&

				( searchItem.generalizationWordTypeNr() == generalizationWordTypeNr ||

				( searchItem.isGeneralizationSingularNoun() &&
				generalizationWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL ) ||

				( searchItem.isGeneralizationPluralNoun() &&
				generalizationWordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR ) ) &&

				searchItem.generalizationWordItem() == generalizationWordItem )
					generalizationResult.foundGeneralization = true;
				else
					searchItem = searchItem.nextGeneralizationItem();
				}
			}
		else
			setError( 1, null, "The given generalization word item is undefined" );

		generalizationResult.result = CommonVariables.result;
		return generalizationResult;
		}

	protected byte checkWordItemForUsage( WordItem unusedWordItem )
		{
		GeneralizationItem searchItem = firstActiveGeneralizationItem();

		if( unusedWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.generalizationWordItem() == unusedWordItem )
					return setError( 1, null, "The collected word item is still in use" );

				searchItem = searchItem.nextGeneralizationItem();
				}
			}
		else
			return setError( 1, null, "The given unused word item is undefined" );

		return CommonVariables.result;
		}

	protected byte createGeneralizationItem( boolean isRelation, short questionParameter, short specificationWordTypeNr, short generalizationWordTypeNr, WordItem generalizationWordItem )
		{
		GeneralizationItem newItem;

		if( generalizationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		generalizationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
				{
				if( ( newItem = new GeneralizationItem( isRelation, questionParameter, specificationWordTypeNr, generalizationWordTypeNr, generalizationWordItem, this, myWord() ) ) != null )
					{
					if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add a generalization item" );
					}
				else
					return setError( 1, null, "I failed to create a generalization item" );
				}
			else
				return setError( 1, null, "The current item number is undefined" );
			}
		else
			return setError( 1, null, "The given generalization word type number is undefined or out of bounds" );

		return CommonVariables.result;
		}

	protected GeneralizationItem firstActiveGeneralizationItem()
		{
		return (GeneralizationItem)firstActiveItem();
		}

	protected GeneralizationItem firstActiveGeneralizationItem( boolean isRelation )
		{
		GeneralizationItem firstGeneralizationItem = firstActiveGeneralizationItem();

		if( firstGeneralizationItem != null )
			return firstGeneralizationItem.getGeneralizationItem( true, isRelation );

		return null;
		}
	};

/*************************************************************************
 *
 *	"I know the greatness of the Lord-
 *	that our Lord is greater than any other god." (Psalm 135:5)
 *
 *************************************************************************/
