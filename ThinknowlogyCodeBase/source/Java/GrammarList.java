/*
 *	Class:			GrammarList
 *	Parent class:	List
 *	Purpose:		To store grammar items
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

class GrammarList extends List
	{
	// Private constructible variables

	private boolean needToCheckGrammar_;


	// Constructor

	protected GrammarList( WordItem myWord )
		{
		needToCheckGrammar_ = false;
		initializeListVariables( Constants.WORD_GRAMMAR_LANGUAGE_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected void setOptionEnd()
		{
		boolean found = false;
		GrammarItem searchItem = firstActiveGrammarItem();

		while( searchItem != null &&
		!found )
			{
			if( searchItem.hasCurrentCreationSentenceNr() &&
			searchItem.itemNr() == CommonVariables.currentItemNr )
				{
				found = true;
				searchItem.isOptionEnd = true;
				}

			searchItem = searchItem.nextGrammarItem();
			}
		}

	protected void setChoiceEnd( int choiceEndItemNr )
		{
		boolean found = false;
		GrammarItem searchItem = firstActiveGrammarItem();

		while( searchItem != null &&
		!found )
			{
			if( searchItem.hasCurrentCreationSentenceNr() &&
			searchItem.itemNr() == choiceEndItemNr )
				{
				found = true;
				searchItem.isChoiceEnd = true;
				}

			searchItem = searchItem.nextGrammarItem();
			}
		}

	protected boolean needToCheckGrammar()
		{
		return needToCheckGrammar_;
		}

	protected byte checkGrammar()
		{
		short currentWordTypeNr = ( Constants.NUMBER_OF_WORD_TYPES - 1 );
		GrammarItem currentGrammarItem;

		if( ( currentGrammarItem = firstActiveGrammarItem() ) != null )
			{
			needToCheckGrammar_ = false;

			do	{
				if( currentGrammarItem.isDefinitionStart() )
					{
					if( currentGrammarItem.isNewStart() &&
					!currentGrammarItem.hasGrammarParameter() &&
					currentGrammarItem.grammarWordTypeNr() <= currentWordTypeNr )
						{
						if( currentGrammarItem.grammarWordTypeNr() < currentWordTypeNr )
							{
							if( currentGrammarItem.grammarWordTypeNr() + 1 == currentWordTypeNr )
								{
								if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_START, currentWordTypeNr, Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_MIDDLE, myWord().anyWordTypeString(), Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_END ) == Constants.RESULT_OK )
									currentWordTypeNr = currentGrammarItem.grammarWordTypeNr();
								else
									addError( 1, null, "I failed to write an interface notification" );
								}
							else
								{
								if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_START, ( currentGrammarItem.grammarWordTypeNr() + 1 ), Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_TO, currentWordTypeNr, Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITIONS_MISSING_MIDDLE, myWord().anyWordTypeString(), Constants.INTERFACE_GRAMMAR_WORD_TYPE_DEFINITION_MISSING_END ) == Constants.RESULT_OK )
									currentWordTypeNr = currentGrammarItem.grammarWordTypeNr();
								else
									addError( 1, null, "I failed to write an interface notification" );
								}
							}

						currentWordTypeNr--;
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					currentGrammarItem.hasGrammarParameter() &&
					!currentGrammarItem.isGrammarItemInUse &&
					!currentGrammarItem.isGrammarStart() &&
					!currentGrammarItem.isPluralNounEnding() )
						{
						if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_START, currentGrammarItem.grammarString(), Constants.INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_MIDDLE, myWord().anyWordTypeString(), Constants.INTERFACE_GRAMMAR_DEFINITION_IS_NOT_USED_END ) != Constants.RESULT_OK )
							addError( 1, null, "I failed to write an interface notification" );
						}
					}
				else
					{
					if( currentGrammarItem.definitionGrammarItem == null )
						return setError( 1, null, "Grammar word \"" + currentGrammarItem.grammarString() + "\" in \"" + myWord().anyWordTypeString() + "\" is used, but not defined" );
					}
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentGrammarItem = currentGrammarItem.nextGrammarItem() ) != null );
			}
		else
			return setError( 1, null, "I couldn't find any grammar item" );

		return CommonVariables.result;
		}

	protected byte checkGrammarItemForUsage( GrammarItem unusedGrammarItem )
		{
		GrammarItem searchItem = firstActiveGrammarItem();

		if( unusedGrammarItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.definitionGrammarItem == unusedGrammarItem )
					return setError( 1, null, "The definition grammar item is still in use" );

				if( searchItem.nextDefinitionGrammarItem == unusedGrammarItem )
					return setError( 1, null, "The next definition grammar item is still in use" );

				searchItem = searchItem.nextGrammarItem();
				}
			}
		else
			return setError( 1, null, "The given unused grammar item is undefined" );

		return CommonVariables.result;
		}

	protected byte linkLaterDefinedGrammarWords()
		{
		String definitionGrammarString;
		GrammarItem currentGrammarItem;
		GrammarItem definitionGrammarItem;

		if( ( definitionGrammarItem = firstActiveGrammarItem() ) != null )
			{
			if( ( definitionGrammarString = definitionGrammarItem.grammarString() ) != null )
				{
				currentGrammarItem = definitionGrammarItem;

				while( ( currentGrammarItem = currentGrammarItem.nextGrammarItem() ) != null )
					{
					if( currentGrammarItem.grammarString() != null )
						{
						if( currentGrammarItem.definitionGrammarItem == null )
							{
							if( !currentGrammarItem.isDefinitionStart() &&
							definitionGrammarString.equals( currentGrammarItem.grammarString() ) )
								{
								currentGrammarItem.definitionGrammarItem = definitionGrammarItem;

								if( !definitionGrammarItem.hasCurrentCreationSentenceNr() )
									definitionGrammarItem.isGrammarItemInUse = true;
								}
							}
						else
							{
							if( currentGrammarItem.definitionGrammarItem == definitionGrammarItem.nextDefinitionGrammarItem )
								currentGrammarItem.definitionGrammarItem = definitionGrammarItem;
							}
						}
					else
						return setError( 1, null, "The grammar string of the grammar definition word is undefined" );
					}
				}
			else
				return setError( 1, null, "The grammar string of the grammar definition word is undefined" );
			}
		else
			return setError( 1, null, "I couldn't find any grammar item" );

		return CommonVariables.result;
		}

	protected GrammarResultType createGrammarItem( boolean isDefinitionStart, boolean isNewStart, boolean isOptionStart, boolean isChoiceStart, boolean skipOptionForWriting, short wordTypeNr, short grammarParameter, int grammarStringLength, String grammarString, GrammarItem definitionGrammarItem )
		{
		GrammarResultType grammarResult = new GrammarResultType();

		if( wordTypeNr >= Constants.WORD_TYPE_UNDEFINED &&
		wordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
			{
			if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
				{
				if( ( grammarResult.createdGrammarItem = new GrammarItem( isDefinitionStart, isNewStart, isOptionStart, isChoiceStart, skipOptionForWriting, wordTypeNr, grammarParameter, grammarStringLength, grammarString, definitionGrammarItem, this, myWord() ) ) != null )
					{
					if( addItemToActiveList( (Item)grammarResult.createdGrammarItem ) == Constants.RESULT_OK )
						needToCheckGrammar_ = true;
					else
						addError( 1, null, "I failed to add a grammar item" );
					}
				else
					setError( 1, null, "I failed to create a grammar item" );
				}
			else
				setError( 1, null, "The current item number is undefined" );
			}
		else
			setError( 1, null, "The given collected word type number is undefined or out of bounds" );

		grammarResult.result = CommonVariables.result;
		return grammarResult;
		}

	protected GrammarResultType findGrammarItem( boolean ignoreValue, short grammarParameter, int grammarStringLength, String grammarString )
		{
		GrammarResultType grammarResult = new GrammarResultType();
		GrammarItem searchItem = firstActiveGrammarItem();

		if( grammarString != null )
			{
			while( searchItem != null &&
			grammarResult.foundGrammarItem == null )
				{
				if( searchItem.grammarString() != null )
					{
					if( searchItem.isDefinitionStart() &&

					( ignoreValue ||
					searchItem.grammarParameter() == grammarParameter ) &&

					grammarStringLength == searchItem.grammarString().length() &&
					grammarString.startsWith( searchItem.grammarString() ) )
						grammarResult.foundGrammarItem = searchItem;
					else
						searchItem = searchItem.nextGrammarItem();
					}
				else
					setError( 1, null, "I found a grammar word without grammar string" );
				}
			}
		else
			setError( 1, null, "The given grammar string is undefined" );

		grammarResult.result = CommonVariables.result;
		return grammarResult;
		}

	protected GrammarResultType removeDuplicateGrammarDefinition()
		{
		GrammarResultType grammarResult = new GrammarResultType();
		boolean isIdentical;
		String grammarString;
		String definitionGrammarString;
		GrammarItem currentGrammarItem;
		GrammarItem duplicateGrammarItem;
		GrammarItem definitionGrammarItem = null;
		GrammarItem duplicateDefinitionGrammarItem = null;
		GrammarItem searchItem = firstActiveGrammarItem();

		while( searchItem != null &&
		duplicateDefinitionGrammarItem == null )
			{
			if( searchItem.isDefinitionStart() )
				{
				if( searchItem.hasCurrentCreationSentenceNr() )
					definitionGrammarItem = searchItem;
				else
					{
					if( definitionGrammarItem != null &&
					searchItem.creationSentenceNr() < CommonVariables.currentSentenceNr &&
					( grammarString = searchItem.grammarString() ) != null &&
					( definitionGrammarString = definitionGrammarItem.grammarString() ) != null )
						{
						if( definitionGrammarString.equals( grammarString ) )
							{
							searchItem.isGrammarItemInUse = true;
							duplicateDefinitionGrammarItem = searchItem;
							definitionGrammarItem.nextDefinitionGrammarItem = searchItem;
							}
						}
					}
				}

			searchItem = searchItem.nextGrammarItem();
			}

		if( definitionGrammarItem != null )
			{
			if( duplicateDefinitionGrammarItem != null )
				{
				do	{
					isIdentical = true;
					currentGrammarItem = definitionGrammarItem;
					duplicateGrammarItem = duplicateDefinitionGrammarItem;

					do	{
						if( currentGrammarItem.isIdentical( duplicateGrammarItem ) )
							{
							currentGrammarItem = ( currentGrammarItem.nextGrammarItem() != null &&
													currentGrammarItem.nextGrammarItem().hasCurrentCreationSentenceNr() ? currentGrammarItem.nextGrammarItem() : null );

							duplicateGrammarItem = ( duplicateGrammarItem.nextGrammarItem() != null &&
													duplicateGrammarItem.nextGrammarItem().creationSentenceNr() == duplicateDefinitionGrammarItem.creationSentenceNr() ? duplicateGrammarItem.nextGrammarItem() : null );
							}
						else
							isIdentical = false;
						}
					while( isIdentical &&
					currentGrammarItem != null &&
					duplicateGrammarItem != null );

					if( isIdentical &&
					currentGrammarItem == null &&
					duplicateGrammarItem == null )
						{
						if( deleteActiveItemsWithCurrentSentenceNr() == Constants.RESULT_OK )
							grammarResult.foundDuplicateGrammar = true;
						else
							addError( 1, null, "I failed to delete a duplicate grammar definition" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				!grammarResult.foundDuplicateGrammar &&
				( duplicateDefinitionGrammarItem = duplicateDefinitionGrammarItem.nextDefinitionGrammarItem ) != null );
				}
			}
		else
			setError( 1, null, "I couldn't find the last grammar definition word" );

		grammarResult.result = CommonVariables.result;
		return grammarResult;
		}

	protected GrammarItem firstActiveGrammarItem()
		{
		return (GrammarItem)firstActiveItem();
		}

	protected GrammarItem startOfGrammar()
		{
		GrammarItem searchItem = firstActiveGrammarItem();

		while( searchItem != null &&
		!searchItem.isGrammarStart() )
			searchItem = searchItem.nextGrammarItem();

		return searchItem;
		}

	protected String wordTypeNameInList( short wordTypeNr )
		{
		GrammarItem searchItem = firstActiveGrammarItem();

		while( searchItem != null )
			{
			if( !searchItem.hasGrammarParameter() &&
			searchItem.isDefinitionStart() &&
			searchItem.isNewStart() &&
			searchItem.grammarWordTypeNr() == wordTypeNr )
				return searchItem.grammarString();

			searchItem = searchItem.nextGrammarItem();
			}

		return null;
		}
	};

/*************************************************************************
 *
 *	"The voice of the Lord echoes above the sea.
 *	The God of glory thunders.
 *	The Lord thunders over the mighty sea." (Psalm 29:3)
 *
 *************************************************************************/
