/*
 *	Class:			CollectionList
 *	Parent class:	List
 *	Purpose:		To store collection items
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

class CollectionList extends List
	{
	// Private methods

	private CollectionItem firstActiveCollectionItem()
		{
		return (CollectionItem)firstActiveItem();
		}


	// Constructor

	protected CollectionList( WordItem myWord )
		{
		initializeListVariables( Constants.WORD_COLLECTION_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected boolean hasCollectionNr( int collectionNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr )
					return true;

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected boolean hasCollectionOrderNr( int collectionOrderNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionOrderNr() == collectionOrderNr )
				return true;

			searchItem = searchItem.nextCollectionItem();
			}

		return false;
		}

	protected boolean hasCollectionItem( WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( commonWordItem == null ||
			searchItem.commonWordItem() == commonWordItem )
				return true;

			searchItem = searchItem.nextCollectionItem();
			}

		return false;
		}

	protected boolean isCollectedCommonWord( WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( commonWordItem != null )
			{
			while( searchItem != null )
				{
				if( commonWordItem.hasCollectionNr( searchItem.collectionNr() ) )
					return true;

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected boolean isCompoundCollection( int collectionNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr )
					return ( searchItem.compoundGeneralizationWordItem() != null );

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected boolean isCompoundCollection( int collectionNr, WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR &&
		commonWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr &&
				searchItem.commonWordItem() == commonWordItem )
					return ( searchItem.compoundGeneralizationWordItem() != null );

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected boolean isExclusiveCollection( int collectionNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr )
					return searchItem.isExclusive();

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected boolean foundCollection( int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR &&
		collectionWordItem != null &&
		commonWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr &&
				searchItem.collectionWordItem() == collectionWordItem &&
				searchItem.commonWordItem() == commonWordItem &&
				searchItem.compoundGeneralizationWordItem() == compoundGeneralizationWordItem )
					return searchItem.isExclusive();

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return false;
		}

	protected short highestCollectionOrderNr( int collectionNr )
		{
		short highestCollectionOrderNr = Constants.NO_ORDER_NR;
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionNr() == collectionNr &&
			searchItem.collectionOrderNr() > highestCollectionOrderNr )
				highestCollectionOrderNr = searchItem.collectionOrderNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return highestCollectionOrderNr;
		}

	protected short collectionOrderNrByWordTypeNr( short collectionWordTypeNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionWordTypeNr() == collectionWordTypeNr )
				return searchItem.collectionOrderNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_ORDER_NR;
		}

	protected short collectionOrderNr( int collectionNr, WordItem collectionWordItem, WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionNr() == collectionNr &&
			searchItem.collectionWordItem() == collectionWordItem &&
			searchItem.commonWordItem() == commonWordItem )
				return searchItem.collectionOrderNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_ORDER_NR;
		}

	protected int collectionNr( short collectionWordTypeNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNr( short collectionWordTypeNr, WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) &&

			( searchItem.commonWordItem() == commonWordItem ||
			searchItem.commonWordItem().isCollectedCommonWord( commonWordItem ) ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNr( short collectionWordTypeNr, WordItem commonWordItem, WordItem compoundGeneralizationWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) &&
			searchItem.compoundGeneralizationWordItem() == compoundGeneralizationWordItem &&

			( searchItem.commonWordItem() == commonWordItem ||
			searchItem.commonWordItem().isCollectedCommonWord( commonWordItem ) ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int compoundCollectionNr( short collectionWordTypeNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.compoundGeneralizationWordItem() != null &&
			searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int nonCompoundCollectionNr( short collectionWordTypeNr )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.compoundGeneralizationWordItem() == null &&
			searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNrByCommonWord( short collectionWordTypeNr, WordItem commonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.commonWordItem() == commonWordItem &&
			searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int collectionNrByCompoundGeneralizationWord( short collectionWordTypeNr, WordItem compoundGeneralizationWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.isMatchingCollectionWordTypeNr( collectionWordTypeNr ) &&
			searchItem.compoundGeneralizationWordItem() == compoundGeneralizationWordItem )
				return searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected int highestCollectionNr()
		{
		int highestCollectionNr = Constants.NO_COLLECTION_NR;
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionNr() > highestCollectionNr )
				highestCollectionNr = searchItem.collectionNr();

			searchItem = searchItem.nextCollectionItem();
			}

		return highestCollectionNr;
		}

	protected int nonCompoundCollectionNr( int compoundCollectionNr )
		{
		int nonCompoundCollectionNr;
		CollectionItem searchItem = firstActiveCollectionItem();
		WordItem collectionWordItem;

		while( searchItem != null )
			{
			if( searchItem.collectionNr() == compoundCollectionNr &&
			( collectionWordItem = searchItem.collectionWordItem() ) != null )
				{
				if( ( nonCompoundCollectionNr = collectionWordItem.nonCompoundCollectionNr( searchItem.collectionWordTypeNr() ) ) > Constants.NO_COLLECTION_NR )
					return nonCompoundCollectionNr;
				}

			searchItem = searchItem.nextCollectionItem();
			}

		return Constants.NO_COLLECTION_NR;
		}

	protected byte checkWordItemForUsage( WordItem unusedWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( unusedWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionWordItem() == unusedWordItem )
					return setError( 1, null, "The collected word item is still in use" );

				if( searchItem.commonWordItem() == unusedWordItem )
					return setError( 1, null, "The common word item is still in use" );

				if( searchItem.compoundGeneralizationWordItem() == unusedWordItem )
					return setError( 1, null, "The compound word item is still in use" );

				searchItem = searchItem.nextCollectionItem();
				}
			}
		else
			return setError( 1, null, "The given unused word item is undefined" );

		return CommonVariables.result;
		}

	protected byte createCollectionItem( boolean isExclusive, short collectionOrderNr, short collectionWordTypeNr, short commonWordTypeNr, int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem, String collectionString )
		{
		CollectionItem newItem;

		if( collectionWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		collectionWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
				{
				if( ( newItem = new CollectionItem( isExclusive, collectionOrderNr, collectionWordTypeNr, commonWordTypeNr, collectionNr, collectionWordItem, commonWordItem, compoundGeneralizationWordItem, collectionString, this, myWord() ) ) != null )
					{
					if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add a collection item" );
					}
				else
					return setError( 1, null, "I failed to create a collection item" );
				}
			else
				return setError( 1, null, "The current item number is undefined" );
			}
		else
			return setError( 1, null, "The given collected word type number is undefined or out of bounds" );

		return CommonVariables.result;
		}

	protected WordItem commonWordItem( int collectionNr, WordItem compoundGeneralizationWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		if( collectionNr > Constants.NO_COLLECTION_NR &&
		compoundGeneralizationWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.collectionNr() == collectionNr &&
				searchItem.compoundGeneralizationWordItem() == compoundGeneralizationWordItem )
					return searchItem.commonWordItem();

				searchItem = searchItem.nextCollectionItem();
				}
			}

		return null;
		}

	protected CollectionResultType findCollectionItem( boolean allowDifferentCommonWord, WordItem collectionWordItem, WordItem commonWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		CollectionItem currentCollectionItem;

		if( commonWordItem != null )
			{
			if( collectionWordItem != null )
				{
				if( ( currentCollectionItem = firstActiveCollectionItem() ) != null )
					{
					do	{
						if( currentCollectionItem.collectionWordItem() == collectionWordItem &&

						( currentCollectionItem.commonWordItem() == commonWordItem ||

						( allowDifferentCommonWord &&
						collectionWordItem.hasCollectionNr( currentCollectionItem.collectionNr() ) ) ) )
							collectionResult.isCollected = true;
						}
					while( !collectionResult.isCollected &&
					( currentCollectionItem = currentCollectionItem.nextCollectionItem() ) != null );
					}
				}
			else
				setError( 1, null, "The given common word is undefined" );
			}
		else
			setError( 1, null, "The given collected word is undefined" );

		collectionResult.result = CommonVariables.result;
		return collectionResult;
		}

	protected WordItem compoundCollectionWordItem( int collectionNr, WordItem notThisCommonWordItem )
		{
		CollectionItem searchItem = firstActiveCollectionItem();

		while( searchItem != null )
			{
			if( searchItem.collectionNr() == collectionNr &&
			searchItem.commonWordItem() != notThisCommonWordItem &&
			searchItem.compoundGeneralizationWordItem() != null )
				return searchItem.collectionWordItem();

			searchItem = searchItem.nextCollectionItem();
			}

		return null;
		}
	};

/*************************************************************************
 *
 *	"Give thanks to the Lords of lords.
 *	His faithful love endures forever." (Psalm 136:3)
 *
 *************************************************************************/
