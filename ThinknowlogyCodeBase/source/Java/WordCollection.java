/*
 *	Class:			WordCollection
 *	Supports class:	WordItem
 *	Purpose:		To create collection structures
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

class WordCollection
	{
	// Private constructible variables

	private boolean hasCreatedCollection_;

	private WordItem foundCollectionWordItem_;
	private WordItem foundGeneralizationWordItem_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte createCollectionByExclusiveSpecification( boolean isExclusive, short collectionWordTypeNr, WordItem generalizationWordItem, WordItem collectionWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		int collectionNr;
		SpecificationItem currentSpecificationItem;
		WordItem currentSpecificationWordItem;

		hasCreatedCollection_ = false;

		if( generalizationWordItem != null )
			{
			if( collectionWordItem != null )
				{
				if( myWord_.isNoun() )
					{
					if( collectionWordItem.isNoun() )
						{
						if( ( currentSpecificationItem = myWord_.firstSpecificationButNotAQuestion() ) != null )
							{
							do	{
								if( currentSpecificationItem.isExclusive() &&
								( currentSpecificationWordItem = currentSpecificationItem.specificationWordItem() ) == generalizationWordItem )
									{
									if( foundCollectionWordItem_ == null )
										{
										foundCollectionWordItem_ = collectionWordItem;
										foundGeneralizationWordItem_ = currentSpecificationWordItem;
										}
									else
										{
										if( ( collectionResult = foundCollectionWordItem_.findCollection( false, collectionWordItem, myWord_ ) ).result == Constants.RESULT_OK )
											{
											if( !collectionResult.isCollected )
												{
												if( ( collectionNr = myWord_.highestCollectionNrInAllWords() ) < Constants.MAX_COLLECTION_NR )
													{
													if( foundCollectionWordItem_.createCollection( isExclusive, collectionWordTypeNr, Constants.WORD_TYPE_UNDEFINED, ++collectionNr, collectionWordItem, myWord_, null, null ) == Constants.RESULT_OK )
														{
														if( collectionWordItem.createCollection( isExclusive, collectionWordTypeNr, Constants.WORD_TYPE_UNDEFINED, collectionNr, foundCollectionWordItem_, myWord_, null, null ) == Constants.RESULT_OK )
															{
															hasCreatedCollection_ = true;
															foundCollectionWordItem_ = null;
															}
														else
															myWord_.addErrorInWord( 1, moduleNameString_, "I failed to collect word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" with word \"" + collectionWordItem.anyWordTypeString() + "\"" );
														}
													else
														myWord_.addErrorInWord( 1, moduleNameString_, "I failed to collect word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" with word \"" + collectionWordItem.anyWordTypeString() + "\"" );
													}
												else
													return myWord_.setSystemErrorInWord( 1, moduleNameString_, "Collection number overflow" );
												}

											}
										else
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find out if word \"" + collectionWordItem.anyWordTypeString() + "\" is collected with word \"" + foundCollectionWordItem_.anyWordTypeString() + "\"" );
										}
									}
								}
							while( CommonVariables.result == Constants.RESULT_OK &&
							!hasCreatedCollection_ &&
							( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemButNotAQuestion() ) != null );
							}
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The given collected word is not a noun" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "I am not a noun" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given collected word item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected WordCollection( WordItem myWord )
		{
		String errorString = null;

		hasCreatedCollection_ = false;

		foundCollectionWordItem_ = null;
		foundGeneralizationWordItem_ = null;

		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();

		if( myWord_ == null )
			errorString = "The given my word is undefined";

		if( errorString != null )
			{
			if( myWord_ != null )
				myWord_.setSystemErrorInWord( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected methods

	protected CollectionResultType createCollectionByGeneralization( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, boolean tryGeneralizations, short collectionWordTypeNr, WordItem generalizationWordItem, WordItem collectionWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( collectionWordItem != null )
			{
			if( createCollectionByExclusiveSpecification( isExclusive, collectionWordTypeNr, generalizationWordItem, collectionWordItem ) == Constants.RESULT_OK )
				{
				if( tryGeneralizations &&
				!hasCreatedCollection_ &&
				foundGeneralizationWordItem_ == null &&
				( currentGeneralizationItem = myWord_.firstActiveGeneralizationItem() ) != null )
					{
					do	{
						if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
							{
							if( currentGeneralizationWordItem.isNoun() &&
							currentGeneralizationWordItem != collectionWordItem )
								{
								if( ( collectionResult = currentGeneralizationWordItem.createCollectionByGeneralization( isExclusive, isExclusiveGeneralization, isQuestion, false, collectionWordTypeNr, generalizationWordItem, collectionWordItem ) ).result == Constants.RESULT_OK )
									{
									if( collectionResult.foundGeneralizationWordItem != null )
										{
										// Collect by generalization
										if( collectionResult.foundGeneralizationWordItem.collectGeneralizationAndSpecifications( isExclusiveGeneralization, false, isQuestion, collectionWordItem.collectionNr( collectionWordTypeNr, currentGeneralizationWordItem ) ) != Constants.RESULT_OK )
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to collect specifications in word \"" + collectionResult.foundGeneralizationWordItem.anyWordTypeString() + "\"" );
										}
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a collection by generalization in word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
								}
							}
						else
							myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined generalization word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					collectionResult.foundGeneralizationWordItem == null &&
					( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItem() ) != null );
					}
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a specification collection by exclusive specification" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given collection word item is undefined" );

		collectionResult.foundGeneralizationWordItem = foundGeneralizationWordItem_;
		collectionResult.result = CommonVariables.result;
		return collectionResult;
		}

	protected byte createCollection( boolean isExclusive, short collectionWordTypeNr, short commonWordTypeNr, int collectionNr, WordItem collectionWordItem, WordItem commonWordItem, WordItem compoundGeneralizationWordItem, String collectionString )
		{
		short collectionOrderNr;
		int foundCollectionNr;

		foundCollectionWordItem_ = null;

		if( !myWord_.iAmAdmin() )
			{
			if( collectionString == null )
				{
				if( collectionWordItem != null )
					{
					if( commonWordItem != null )
						{
						if( collectionWordItem != myWord_ )
							{
							if( commonWordItem != myWord_ )
								{
								if( myWord_.activeWordTypeItem( collectionWordTypeNr ) != null )
									{
									foundCollectionNr = myWord_.collectionNr( collectionWordTypeNr, commonWordItem, compoundGeneralizationWordItem );

									if( foundCollectionNr > Constants.NO_COLLECTION_NR &&
									foundCollectionNr != collectionNr )
										{
										if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_START, commonWordItem.wordTypeString( commonWordTypeNr ), Constants.INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_WORD, myWord_.wordTypeString( collectionWordTypeNr ), Constants.INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_DUE_TO_SPECIFICATION_END ) != Constants.RESULT_OK )
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification about ambiguity" );
										}
									}
								else
									return myWord_.setErrorInWord( 1, moduleNameString_, "The requested word type with number doesn't exist: " + collectionWordTypeNr );
								}
							else
								return myWord_.setErrorInWord( 1, moduleNameString_, "The given common word is the same as me" );
							}
						else
							return myWord_.setErrorInWord( 1, moduleNameString_, "The given collected word is the same as me" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The given common word is undefined" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The given collected word is undefined" );
				}

			if( CommonVariables.result == Constants.RESULT_OK )
				{
				if( ( collectionOrderNr = myWord_.highestCollectionOrderNrInAllWords( collectionNr ) ) < Constants.MAX_ORDER_NR - 1 )
					{
					if( !myWord_.foundCollection( collectionNr, collectionWordItem, commonWordItem, compoundGeneralizationWordItem ) )
						{
						if( myWord_.collectionList == null )
							{
							if( ( myWord_.collectionList = new CollectionList( myWord_ ) ) != null )
								myWord_.wordList[Constants.WORD_COLLECTION_LIST] = myWord_.collectionList;
							else
								return myWord_.setErrorInWord( 1, moduleNameString_, "I failed to create a collection list" );
							}

						return myWord_.collectionList.createCollectionItem( isExclusive, ++collectionOrderNr, collectionWordTypeNr, commonWordTypeNr, collectionNr, collectionWordItem, commonWordItem, compoundGeneralizationWordItem, collectionString );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "This collection already exists" );
					}
				else
					return myWord_.setSystemErrorInWord( 1, moduleNameString_, "Collection order number overflow" );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The admin does not have collections" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"They do not fear bad news;
 *	they confidently trust the Lord to take care of them.
 *	They are confident and fearless
 *	and can face their foes triumphantly." (Psalm 112:7-8)
 *
 *************************************************************************/
