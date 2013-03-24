/*
 *	Class:			ContextItem
 *	Parent class:	Item
 *	Purpose:		To store the context info of a word that a
 *					specification can have
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

class ContextItem extends Item
	{
	// Private loadable variables

	private boolean isPossessive_;

	private short contextWordTypeNr_;
	private short specificationWordTypeNr_;

	private int contextNr_;

	private WordItem specificationWordItem_;


	// Constructor

	protected ContextItem( boolean isPossessive, short contextWordTypeNr, short specificationWordTypeNr, int contextNr, WordItem specificationWordItem, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		isPossessive_ = isPossessive;

		contextWordTypeNr_ = contextWordTypeNr;
		specificationWordTypeNr_ = specificationWordTypeNr;

		contextNr_ = contextNr;

		specificationWordItem_ = specificationWordItem;
		}


	// Protected virtual methods

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();


		if( specificationWordItem_ != null &&
		( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( contextNr_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				contextNr_ > Constants.NO_CONTEXT_NR ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( ( specificationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : specificationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : specificationWordItem_.itemNr() == queryItemNr ) ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( contextWordTypeNr_ == queryWordTypeNr ||
				specificationWordTypeNr_ == queryWordTypeNr );
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( specificationWordItem_ != null )
			{
			if( specificationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the specification word" );
			}

		return CommonVariables.result;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String contextWordTypeString = myWord().wordTypeName( contextWordTypeNr_ );
		String specificationWordTypeString = myWord().wordTypeName( specificationWordTypeNr_ );
		baseToStringBuffer( queryWordTypeNr );

		if( isPossessive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isPossessive" );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "contextWordType:" + ( contextWordTypeString == null ? Constants.EMPTY_STRING : contextWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + contextWordTypeNr_ );

		if( contextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "contextNr:" + contextNr_ );

		if( specificationWordTypeNr_ > Constants.WORD_TYPE_UNDEFINED )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordType:" + ( specificationWordTypeString == null ? Constants.EMPTY_STRING : specificationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + specificationWordTypeNr_ );

		if( specificationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + specificationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + specificationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isPossessive()
		{
		return isPossessive_;
		}

	protected short contextWordTypeNr()
		{
		return contextWordTypeNr_;
		}

	protected short specificationWordTypeNr()
		{
		return specificationWordTypeNr_;
		}

	protected int contextNr()
		{
		return contextNr_;
		}

	protected ContextItem nextContextItem()
		{
		return (ContextItem)nextItem;
		}

	protected WordItem specificationWordItem()
		{
		return specificationWordItem_;
		}
	};

/*************************************************************************
 *
 *	"Enter his gates with thanksgiving;
 *	go into his courts with praise.
 *	Give thanks to him and praise his name." (Psalm 100:4)
 *
 *************************************************************************/
