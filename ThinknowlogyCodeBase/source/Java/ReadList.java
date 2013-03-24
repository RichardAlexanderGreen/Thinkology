/*
 *	Class:			ReadList
 *	Parent class:	List
 *	Purpose:		To temporarily store read items
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

class ReadList extends List
	{
	// Private constructible variables

	private int lastActivatedWordPosition_;

	private ReadItem failedWordReadItem_;


	// Constructor

	protected ReadList( WordItem myWord )
		{
		lastActivatedWordPosition_ = 0;
		failedWordReadItem_ = null;

		initializeListVariables( Constants.ADMIN_READ_LIST_SYMBOL, myWord );
		}


	// Protected virtual methods

	protected boolean isTemporaryList()
		{
		return true;
		}

	protected byte findWordReference( WordItem referenceWordItem )
		{
		ReadItem searchItem = firstCurrentLanguageActiveReadItem();

		CommonVariables.foundWordReference = false;

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!CommonVariables.foundWordReference )
			{
			if( searchItem.findWordReference( referenceWordItem ) == Constants.RESULT_OK )
				searchItem = searchItem.nextCurrentLanguageReadItem();
			else
				addError( 1, null, "I failed to check for a reference word item in an active read item" );
			}

		searchItem = firstCurrentLanguageDeactiveReadItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null &&
		!CommonVariables.foundWordReference )
			{
			if( searchItem.findWordReference( referenceWordItem ) == Constants.RESULT_OK )
				searchItem = searchItem.nextCurrentLanguageReadItem();
			else
				addError( 1, null, "I failed to check for a reference word item in a deactive read item" );
			}

		// Don't search in the deleted items - since this method is used for cleanup purposes

		return CommonVariables.result;
		}


	// Protected methods

	protected void initForParsingReadWords()
		{
		failedWordReadItem_ = null;
		lastActivatedWordPosition_ = 0;
		}

	protected boolean createImperativeSentence()
		{
		int nWords = 0;
		int previousWordPosition = 0;
		String readWordString;
		ReadItem startItem = null;
		ReadItem searchItem = firstActiveReadItem();

		CommonVariables.writeSentenceStringBuffer = null;

		while( searchItem != null )
			{
			if( ( nWords > 0 ||
			searchItem.isSpecificationWord() ) &&					// Trigger

			searchItem.wordPosition() > previousWordPosition )		// First appearance of new word
				{
				nWords++;
				previousWordPosition = searchItem.wordPosition();

				if( startItem == null )
					startItem = searchItem;
				}

			searchItem = searchItem.nextReadItem();
			}

		if( nWords > 2 )	// Start creation of imperative sentence
			{
			previousWordPosition = 0;
			searchItem = startItem;
			CommonVariables.writeSentenceStringBuffer = new StringBuffer();

			while( searchItem != null )
				{
				if( searchItem.wordPosition() > previousWordPosition &&
				searchItem.readWordItem() != null &&	// Skip text
				( readWordString = searchItem.readWordTypeString() ) != null )
					{
					if( previousWordPosition > 0 &&
					searchItem.grammarParameter != Constants.GRAMMAR_SENTENCE )	// End of string (colon, question mark, etc)
						CommonVariables.writeSentenceStringBuffer.append( Constants.SPACE_STRING );

					previousWordPosition = searchItem.wordPosition();
					CommonVariables.writeSentenceStringBuffer.append( readWordString );
					}

				searchItem = searchItem.nextReadItem();
				}
			}

		return ( nWords > 2 );
		}

	protected boolean foundReadItem( short wordTypeLanguageNr, short wordParameter, short wordTypeNr, int wordPosition, String readString, WordItem readWordItem )
		{
		ReadItem searchItem = firstActiveReadItem();

		while( searchItem != null )
			{
			if( searchItem.wordTypeLanguageNr() == wordTypeLanguageNr &&
			searchItem.wordParameter() == wordParameter &&
			searchItem.wordTypeNr() == wordTypeNr &&
			searchItem.wordPosition() == wordPosition &&

			( ( searchItem.readString == null &&
			readString == null ) ||

			( searchItem.readString != null &&
			readString != null &&
			searchItem.readString.equals( readString ) ) ) &&

			searchItem.readWordItem() == readWordItem )
				return true;

			searchItem = searchItem.nextReadItem();
			}

		return false;
		}

	protected boolean hasPassedGrammarIntegrityCheck()
		{
		ReadItem searchItem = firstCurrentLanguageActiveReadItem();

		while( searchItem != null )
			{
			if( !searchItem.isWordPassingGrammarIntegrityCheck )
				return false;

			searchItem = searchItem.nextCurrentLanguageReadItem();
			}

		return true;
		}

	protected byte deleteReadWords( int currentWordPosition )
		{
		ReadItem searchItem = firstActiveReadItem();

		if( currentWordPosition > 0 )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.wordPosition() > currentWordPosition )
					{
					if( deleteActiveItem( false, searchItem ) == Constants.RESULT_OK )
						searchItem = nextReadListItem();
					else
						addError( 1, null, "I failed to delete an active item" );
					}
				else
					searchItem = searchItem.nextReadItem();
				}
			}

		return CommonVariables.result;
		}

	protected byte createReadItem( short wordTypeLanguageNr, short wordParameter, short wordTypeNr, int wordPosition, int readStringLength, String readString, WordItem readWordItem )
		{
		ReadItem newItem;

		if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
		wordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
				{
				if( ( newItem = new ReadItem( wordTypeLanguageNr, wordParameter, wordTypeNr, wordPosition, readStringLength, readString, readWordItem, this, myWord() ) ) != null )
					{
					if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add a read item" );
					}
				else
					return setError( 1, null, "I failed to create a read item" );
				}
			else
				return setError( 1, null, "The current item number is undefined" );
			}
		else
			return setError( 1, null, "The given read word type number is undefined or out of bounds" );

		return CommonVariables.result;
		}

	protected byte setGrammarParameter( boolean isValid, int startWordPosition, int endWordPosition, GrammarItem definitionGrammarItem )
		{
		boolean found = false;
		boolean isMarked = true;
		String definitionGrammarString;
		ReadItem searchItem = firstActiveReadItem();

		if( endWordPosition > 0 )
			{
			if( startWordPosition < endWordPosition )
				{
				if( definitionGrammarItem != null )
					{
					if( isValid )
						{
						while( isMarked &&
						searchItem != null &&
						searchItem.wordPosition() <= endWordPosition )
							{
							if( !searchItem.isMarkedBySetGrammarParameter &&
							searchItem.wordPosition() > startWordPosition &&
							searchItem.wordPosition() <= endWordPosition )
								isMarked = false;

							searchItem = searchItem.nextReadItem();
							}

						searchItem = firstActiveReadItem();
						}

					while( searchItem != null &&
					searchItem.wordPosition() <= endWordPosition )
						{
						if( searchItem.wordPosition() > startWordPosition &&
						searchItem.wordPosition() <= endWordPosition )
							{
							found = true;

							if( isValid )
								{
								if( isMarked ||
								definitionGrammarItem.grammarParameter() > searchItem.grammarParameter )
									{
									searchItem.isMarkedBySetGrammarParameter = false;
									searchItem.grammarParameter = definitionGrammarItem.grammarParameter();

									searchItem.definitionGrammarItem = definitionGrammarItem;

									if( searchItem.readString == null &&
									( definitionGrammarString = definitionGrammarItem.grammarString() ) != null )
										{
										if( searchItem.readString != null )
											searchItem.readString = definitionGrammarString;
										else
											{
											if( ( searchItem.readString = new String( definitionGrammarString ) ) == null )
														return setError( 1, null, "I failed to create a grammar string" );
											}
										}
									}
								}
							else
								searchItem.isMarkedBySetGrammarParameter = true;
							}

						searchItem = searchItem.nextReadItem();
						}

					if( !found )
						return setError( 1, null, "I couldn't find any item between the given word positions" );
					}
				else
					return setError( 1, null, "The given grammar definition word item is undefined" );
				}
			else
				return setError( 1, null, "The given start word position is equal or higher than the given end word position" );
			}
		else
			return setError( 1, null, "The given end word position is undefined" );

		return CommonVariables.result;
		}

	protected ReadResultType findMoreInterpretations()
		{
		ReadResultType readResult = new ReadResultType();
		int maxReachedWordPosition = ( failedWordReadItem_ == null ? 0 : failedWordReadItem_.wordPosition() - 1 );
		ReadItem previousSearchItem = null;
		ReadItem searchItem = firstActiveReadItem();
		ReadItem activeReadItem = firstActiveReadItem();
		ReadItem deactiveReadItem = firstCurrentLanguageDeactiveReadItem();

		// Clear grammar parameters
		while( searchItem != null &&
		searchItem.grammarParameter >= Constants.GRAMMAR_SENTENCE )
			{
			searchItem.grammarParameter = Constants.NO_GRAMMAR_PARAMETER;
			previousSearchItem = searchItem;

			searchItem = searchItem.nextReadItem();
			}

		// Get last deactive interpretion
		while( deactiveReadItem != null &&
		deactiveReadItem.nextReadItem() != null )
			deactiveReadItem = deactiveReadItem.nextReadItem();

		if( deactiveReadItem != null )
			{
			if( previousSearchItem != null )		// Get the next position, which is the failing position
				{
				failedWordReadItem_ = previousSearchItem;
				maxReachedWordPosition = previousSearchItem.wordPosition();

				while( failedWordReadItem_ != null &&
				failedWordReadItem_.wordPosition() == maxReachedWordPosition )
					failedWordReadItem_ = failedWordReadItem_.nextReadItem();
				}

			if( deactiveReadItem.wordPosition() < maxReachedWordPosition &&
			lastActivatedWordPosition_ < maxReachedWordPosition )
				{
				while( activeReadItem != null &&	// Get first active interpretion after the max reached word position
				( activeReadItem.wordPosition() < maxReachedWordPosition ||
				( activeReadItem.nextReadItem() != null &&
				activeReadItem.wordPosition() != activeReadItem.nextReadItem().wordPosition() ) ) )
					activeReadItem = activeReadItem.nextReadItem();

				if( activeReadItem != null )
					{
					readResult.foundMoreInterpretations = true;

					if( deactivateActiveItem( activeReadItem ) == Constants.RESULT_OK )
						activeReadItem = nextReadListItem();
					else
						addError( 1, null, "I failed to deactive an active item" );
					}
				}
			else
				{
				readResult.foundMoreInterpretations = true;
				lastActivatedWordPosition_ = deactiveReadItem.wordPosition();

				if( activateDeactiveItem( deactiveReadItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to active a deactive item" );
				}
			}

		readResult.failedWordReadItem = failedWordReadItem_;
		readResult.result = CommonVariables.result;
		return readResult;
		}

	protected ReadResultType numberOfReadWordReferences( short wordTypeNr, WordItem readWordItem )
		{
		ReadResultType readResult = new ReadResultType();
		ReadItem searchItem = firstCurrentLanguageActiveReadItem();

		if( readWordItem != null )
			{
			while( searchItem != null )
				{
				if( !searchItem.isUnusedReadItem &&
				searchItem.wordTypeNr() == wordTypeNr &&
				searchItem.readWordItem() == readWordItem )
					readResult.nReadWordReferences++;

				searchItem = searchItem.nextCurrentLanguageReadItem();
				}

			searchItem = firstCurrentLanguageDeactiveReadItem();

			while( searchItem != null )
				{
				if( !searchItem.isUnusedReadItem &&
				searchItem.readWordItem() == readWordItem &&
				searchItem.wordTypeNr() == wordTypeNr )
					readResult.nReadWordReferences++;

				searchItem = searchItem.nextCurrentLanguageReadItem();
				}
			}
		else
			setError( 1, null, "The given read word is undefined" );

		readResult.result = CommonVariables.result;
		return readResult;
		}

	protected ReadResultType selectMatchingWordType( short wordTypeNr, short wordParameter, int currentWordPosition )
		{
		ReadResultType readResult = new ReadResultType();
		ReadItem activeReadItem;
		ReadItem endReadItem = firstActiveReadItem();

		if( currentWordPosition > 0 )	// Find current word position
			{
			while( endReadItem != null &&
			endReadItem.wordPosition() <= currentWordPosition )
				endReadItem = endReadItem.nextReadItem();
			}

		activeReadItem = endReadItem;

		while( CommonVariables.result == Constants.RESULT_OK &&
		!readResult.foundMatchingWordType &&
		endReadItem != null )
			{
			if( endReadItem.wordPosition() == currentWordPosition + 1 )
				{
				if( endReadItem.wordTypeNr() == wordTypeNr &&
				endReadItem.wordParameter() == wordParameter )
					{
					while( CommonVariables.result == Constants.RESULT_OK &&
					activeReadItem != endReadItem )
						{
						if( deactivateActiveItem( activeReadItem ) == Constants.RESULT_OK )
							activeReadItem = nextReadListItem();
						else
							addError( 1, null, "I failed to deactive an active item" );
						}

					readResult.foundMatchingWordType = true;
					}
				else
					endReadItem = ( endReadItem.wordPosition() <= lastActivatedWordPosition_ ? null : endReadItem.nextReadItem() );
				}
			else
				endReadItem = null;
			}

		readResult.result = CommonVariables.result;
		return readResult;
		}

	protected ReadItem firstActiveReadItem()
		{
		return (ReadItem)firstActiveItem();
		}

	protected ReadItem firstCurrentLanguageActiveReadItem()
		{
		ReadItem searchItem = (ReadItem)firstActiveItem();

		while( searchItem != null &&
		searchItem.wordTypeLanguageNr() < CommonVariables.currentGrammarLanguageNr )
			searchItem = searchItem.nextReadItem();

		return searchItem;
		}

	protected ReadItem firstDeactiveReadItem()
		{
		return (ReadItem)firstDeactiveItem();
		}

	protected ReadItem firstCurrentLanguageDeactiveReadItem()
		{
		ReadItem searchItem = (ReadItem)firstDeactiveItem();

		while( searchItem != null &&
		searchItem.wordTypeLanguageNr() < CommonVariables.currentGrammarLanguageNr )
			searchItem = searchItem.nextReadItem();

		return searchItem;
		}

	protected ReadItem nextReadListItem()
		{
		return (ReadItem)nextListItem();
		}
	};

/*************************************************************************
 *
 *	"Everything he does reveals his glory and majesty.
 *	His righteousness never fails." (Psalm 111:3)
 *
 *************************************************************************/
