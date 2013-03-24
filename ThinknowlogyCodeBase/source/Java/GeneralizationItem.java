/*
 *	Class:			GeneralizationItem
 *	Parent class:	Item
 *	Purpose:		To store info about generalizations of a word,
 *					which are the "parents" of that word,
 *					and is the opposite direction of its specifications
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

class GeneralizationItem extends Item
	{
	// Private loadable variables

	private boolean isRelation_;

	private short questionParameter_;
	private short specificationWordTypeNr_;
	private short generalizationWordTypeNr_;

	private WordItem generalizationWordItem_;


	// Constructor

	protected GeneralizationItem( boolean isRelation, short questionParameter, short specificationWordTypeNr, short generalizationWordTypeNr, WordItem generalizationWordItem, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		isRelation_ = isRelation;

		questionParameter_ = questionParameter;
		specificationWordTypeNr_ = specificationWordTypeNr;
		generalizationWordTypeNr_ = generalizationWordTypeNr;
		generalizationWordItem_ = generalizationWordItem;

		if( generalizationWordItem_ == null )
			setSystemErrorInItem( 1, null, null, "The given generalization word item is undefined" );
		}


	// Protected virtual methods

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( generalizationWordItem_ != null &&
		( wordString = generalizationWordItem_.wordTypeString( generalizationWordTypeNr_ ) ) != null )
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
		return ( questionParameter_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				questionParameter_ > Constants.NO_QUESTION_PARAMETER ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( generalizationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : generalizationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : generalizationWordItem_.itemNr() == queryItemNr ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( specificationWordTypeNr_ == queryWordTypeNr ||
				generalizationWordTypeNr_ == queryWordTypeNr );
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( generalizationWordItem_ != null )
			{
			if( generalizationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the generalization word" );
			}

		return CommonVariables.result;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String generalizationWordTypeString = myWord().wordTypeName( generalizationWordTypeNr_ );
		String specificationWordTypeString = myWord().wordTypeName( specificationWordTypeNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( isRelation_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isRelation" );

		if( questionParameter_ > Constants.NO_QUESTION_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "questionParameter:" + questionParameter_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordType:" + ( specificationWordTypeString == null ? Constants.EMPTY_STRING : specificationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + specificationWordTypeNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationWordType:" + ( generalizationWordTypeString == null ? Constants.EMPTY_STRING : generalizationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + generalizationWordTypeNr_ );

		if( generalizationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + generalizationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + generalizationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = generalizationWordItem_.wordTypeString( generalizationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isRelation()
		{
		return isRelation_;
		}

	protected boolean isSpecificationNounPlural()
		{
		return ( specificationWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isGeneralizationPropername()
		{
		return ( generalizationWordTypeNr_ == Constants.WORD_TYPE_PROPER_NAME );
		}

	protected boolean isGeneralizationSingularNoun()
		{
		return ( generalizationWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR );
		}

	protected boolean isGeneralizationPluralNoun()
		{
		return ( generalizationWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected short questionParameter()
		{
		return questionParameter_;
		}

	protected short specificationWordTypeNr()
		{
		return specificationWordTypeNr_;
		}

	protected short generalizationWordTypeNr()
		{
		return generalizationWordTypeNr_;
		}

	protected WordItem generalizationWordItem()
		{
		return generalizationWordItem_;
		}

	protected GeneralizationItem getGeneralizationItem( boolean includeThisItem, boolean isRelation )
		{
		GeneralizationItem searchItem = ( includeThisItem ? this : nextGeneralizationItem() );

		while( searchItem != null )
			{
			if( searchItem.isRelation() == isRelation )
				return searchItem;

			searchItem = searchItem.nextGeneralizationItem();
			}

		return null;
		}

	protected GeneralizationItem nextGeneralizationItem()
		{
		return (GeneralizationItem)nextItem;
		}

	protected GeneralizationItem nextGeneralizationItemOfSpecification()
		{
		return getGeneralizationItem( false, false );
		}

	protected GeneralizationItem nextGeneralizationItemOfRelation()
		{
		return getGeneralizationItem( false, true );
		}

	protected GeneralizationItem nextGeneralizationItem( boolean isRelation )
		{
		return getGeneralizationItem( false, isRelation );
		}
	};

/*************************************************************************
 *
 *	"Give thanks to him who made the heavens so skillfully.
 *	His faithful love endures forever." (Psalm 136:5)
 *
 *************************************************************************/
