/*
 *	Class:			CollectionItem
 *	Parent class:	List
 *	Purpose:		To store collections of a word
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

class CollectionItem extends Item
	{
	// Private loadable variables

	private boolean isExclusive_;

	private short collectionOrderNr_;
	private short collectionWordTypeNr_;
	private short commonWordTypeNr_;

	private int collectionNr_;

	private WordItem collectionWordItem_;
	private WordItem commonWordItem_;
	private WordItem compoundGeneralizationWordItem_;

	private String collectionString_;


	// Constructor

	protected CollectionItem( boolean isExclusive, short collectionOrderNr, short collectionWordTypeNr, short commonWordTypeNr, int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem, String collectionString, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		isExclusive_ = isExclusive;

		collectionOrderNr_ = collectionOrderNr;
		collectionWordTypeNr_ = collectionWordTypeNr;
		commonWordTypeNr_ = commonWordTypeNr;

		collectionNr_ = collectionNr;

		collectionWordItem_ = collectionWordItem;
		commonWordItem_ = commonWordItem;
		compoundGeneralizationWordItem_ = compoundGeneralizationWordItem;

		collectionString_ = null;

		if( collectionString != null )
			{
			if( ( collectionString_ = new String( collectionString ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create a collection string" );
			}
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( collectionString_ != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( collectionString_ );
			}
		}

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( collectionWordItem_ != null &&
		( wordString = collectionWordItem_.wordTypeString( collectionWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}

		if( commonWordItem_ != null &&
		( wordString = commonWordItem_.wordTypeString( commonWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery ||
			CommonVariables.queryStringBuffer.length() > 0 )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}

		if( compoundGeneralizationWordItem_ != null &&
		( wordString = compoundGeneralizationWordItem_.wordTypeString( commonWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery ||
			CommonVariables.queryStringBuffer.length() > 0 )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( collectionOrderNr_ == queryParameter ||
				collectionNr_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				( collectionOrderNr_ > Constants.NO_ORDER_NR ||
				collectionNr_ > Constants.NO_COLLECTION_NR ) ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( collectionWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : collectionWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : collectionWordItem_.itemNr() == queryItemNr ) ) ||

				( commonWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : commonWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : commonWordItem_.itemNr() == queryItemNr ) ) ||

				( compoundGeneralizationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : compoundGeneralizationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : compoundGeneralizationWordItem_.itemNr() == queryItemNr ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( collectionWordTypeNr_ == queryWordTypeNr ||
				commonWordTypeNr_ == queryWordTypeNr );
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( collectionWordItem_ != null )
			{
			if( collectionWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the collected word item" );
			}

		if( commonWordItem_ != null )
			{
			if( commonWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the common word item" );
			}

		if( compoundGeneralizationWordItem_ != null )
			{
			if( compoundGeneralizationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the compound word item" );
			}

		return CommonVariables.result;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String collectionWordTypeString = myWord().wordTypeName( collectionWordTypeNr_ );
		String commonWordTypeString = myWord().wordTypeName( commonWordTypeNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( isExclusive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isExclusive" );

		if( collectionNr_ > Constants.NO_COLLECTION_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "collectionNr:" + collectionNr_ );

		if( collectionOrderNr_ > Constants.NO_ORDER_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "collectionOrderNr:" + collectionOrderNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "collectionWordType:" + ( collectionWordTypeString == null ? Constants.EMPTY_STRING : collectionWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + collectionWordTypeNr_ );

		if( collectionWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "collectionWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + collectionWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + collectionWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = collectionWordItem_.wordTypeString( collectionWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "commonWordType:" + ( commonWordTypeString == null ? Constants.EMPTY_STRING : commonWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + commonWordTypeNr_ );

		if( commonWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "commonWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + commonWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + commonWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = commonWordItem_.wordTypeString( commonWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( compoundGeneralizationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "compoundGeneralizationWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + compoundGeneralizationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + compoundGeneralizationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = compoundGeneralizationWordItem_.wordTypeString( commonWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( collectionString_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "collectionString:" + collectionString_ );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isExclusive()
		{
		return isExclusive_;
		}

	protected boolean isMatchingCollectionWordTypeNr( short collectionWordTypeNr )
		{
		return	( collectionWordTypeNr_ == collectionWordTypeNr ||

				( collectionWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR &&
				collectionWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL ) ||

				( collectionWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL &&
				collectionWordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR ) );
		}

	protected short collectionOrderNr()
		{
		return collectionOrderNr_;
		}

	protected short collectionWordTypeNr()
		{
		return collectionWordTypeNr_;
		}

	protected int collectionNr()
		{
		return collectionNr_;
		}

	protected WordItem collectionWordItem()
		{
		return collectionWordItem_;
		}

	protected WordItem commonWordItem()
		{
		return commonWordItem_;
		}

	protected WordItem compoundGeneralizationWordItem()
		{
		return compoundGeneralizationWordItem_;
		}

	protected CollectionItem nextCollectionItem()
		{
		return (CollectionItem)nextItem;
		}

	protected String collectionString()
		{
		return collectionString_;
		}
	};

/*************************************************************************
 *
 *	"Let them praise to Lord for his great love
 *	and for the wonderful things he has done for them." (Psalm 107:8)
 *
 *************************************************************************/
