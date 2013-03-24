/*
 *	Class:			ContextList
 *	Parent class:	List
 *	Purpose:		To store context items
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

class ContextList extends List
	{

	// Private methods

	private boolean hasContext( boolean isPossessive, int contextNr )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.contextNr() == contextNr )
				return true;

			searchItem = searchItem.nextContextItem();
			}

		return false;
		}

	private byte createContextItem( boolean isPossessive, short contextWordTypeNr, short specificationWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		ContextItem newItem;

		if( contextNr > Constants.NO_CONTEXT_NR )
			{
			if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
				{
				if( ( newItem = new ContextItem( isPossessive, contextWordTypeNr, specificationWordTypeNr, contextNr, specificationWordItem, this, myWord() ) ) != null )
					{
					if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add a context item" );
					}
				else
					return setError( 1, null, "I failed to create a context item" );
				}
			else
				return setError( 1, null, "The current item number is undefined" );
			}
		else
			return setError( 1, null, "The given context number is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected ContextList( WordItem myWord )
		{
		initializeListVariables( Constants.WORD_CONTEXT_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected void clearContextWriteLevel( short currentWriteLevel, int contextNr )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() == contextNr )
				myWord().clearWriteLevel( currentWriteLevel );

			searchItem = searchItem.nextContextItem();
			}
		}

	protected boolean hasContext( int contextNr )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() == contextNr )
				return true;

			searchItem = searchItem.nextContextItem();
			}

		return false;
		}

	protected boolean hasContext( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.contextNr() == contextNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return true;

			searchItem = searchItem.nextContextItem();
			}

		return false;
		}

	protected boolean isContextCurrentlyUpdated( boolean isPossessive, int contextNr, WordItem specificationWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.hasCurrentCreationSentenceNr() &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.contextNr() == contextNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return true;

			searchItem = searchItem.nextContextItem();
			}

		return false;
		}

	protected boolean isContextSubset( int subsetContextNr, int fullSetContextNr )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() == subsetContextNr &&
			hasContext( searchItem.isPossessive(), fullSetContextNr, searchItem.specificationWordItem() ) )
				return true;

			searchItem = searchItem.nextContextItem();
			}

		return false;
		}

	protected int contextNr( boolean isPossessive, short contextWordTypeNr, WordItem specificationWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.contextWordTypeNr() == contextWordTypeNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return searchItem.contextNr();

			searchItem = searchItem.nextContextItem();
			}

		return Constants.NO_CONTEXT_NR;
		}

	protected int highestContextNr()
		{
		int highestContextNr = Constants.NO_CONTEXT_NR;
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() > highestContextNr )
				highestContextNr = searchItem.contextNr();

			searchItem = searchItem.nextContextItem();
			}

		searchItem = firstArchiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() > highestContextNr )
				highestContextNr = searchItem.contextNr();

			searchItem = searchItem.nextContextItem();
			}

		return highestContextNr;
		}

	protected byte checkWordItemForUsage( WordItem unusedWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		if( unusedWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.specificationWordItem() == unusedWordItem )
					return setError( 1, null, "The specification word item is still in use" );

				searchItem = searchItem.nextContextItem();
				}
			}
		else
			return setError( 1, null, "The given unused word item is undefined" );

		return CommonVariables.result;
		}

	protected byte addContext( boolean isPossessive, short contextWordTypeNr, short specificationWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		if( contextNr > Constants.NO_CONTEXT_NR )
			{
			if( ( specificationWordItem == null &&				// Pronoun context
			!hasContext( contextNr ) ) ||

			( specificationWordItem != null &&					// Relation context
			!hasContext( isPossessive, contextNr, specificationWordItem ) ) )
				{
				if( !hasContext( !isPossessive, contextNr ) )	// Opposite possessive context should have a different context number
					{
					if( createContextItem( isPossessive, contextWordTypeNr, specificationWordTypeNr, contextNr, specificationWordItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to create a context item" );
					}
				else
					return setError( 1, null, "The given context number already exists as an opposite possessive" );
				}
			}
		else
			return setError( 1, null, "The given context number is undefined" );

		return CommonVariables.result;
		}

	protected ContextItem firstActiveContextItem()
		{
		return (ContextItem)firstActiveItem();
		}

	protected ContextItem firstArchiveContextItem()
		{
		return (ContextItem)firstArchiveItem();
		}

	protected ContextItem nextContextListItem()
		{
		return (ContextItem)nextListItem();
		}

	protected ContextItem contextItem( int contextNr )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.contextNr() == contextNr )
				return searchItem;

			searchItem = searchItem.nextContextItem();
			}

		return null;
		}

	protected ContextItem contextItem( boolean isPossessive, short contextWordTypeNr, WordItem specificationWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.contextWordTypeNr() == contextWordTypeNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return searchItem;

			searchItem = searchItem.nextContextItem();
			}

		return null;
		}

	protected ContextItem contextItem( boolean isPossessive, short contextWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		ContextItem searchItem = firstActiveContextItem();

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.contextWordTypeNr() == contextWordTypeNr &&
			searchItem.contextNr() == contextNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return searchItem;

			searchItem = searchItem.nextContextItem();
			}

		return null;
		}
	};

/*************************************************************************
 *
 *	"O Lord my God, you have performed many wonders for us.
 *	Your plans for us are too numerous to list.
 *	You have no equal.
 *	I have tried to recite all your wonderful deeds,
 *	I would never come to the end of them." (Psalm 40:5)
 *
 *************************************************************************/
