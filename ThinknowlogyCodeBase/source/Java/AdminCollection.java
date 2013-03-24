/*
 *	Class:			AdminCollection
 *	Supports class:	AdminItem
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

class AdminCollection
	{
	// Private constructible variables

	private short existingPairCollectionOrderNr_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte checkCollectionInAllWords( int collectionNr, WordItem collectionWordItem, WordItem commonWordItem )
		{
		short foundCollectionOrderNr = 0;
		int nWords = 0;
		WordItem currentWordItem;

		existingPairCollectionOrderNr_ = Constants.NO_ORDER_NR;

		if( collectionNr > Constants.NO_COLLECTION_NR )
			{
			if( collectionWordItem != null )
				{
				if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
					{
					do	{
						if( ( foundCollectionOrderNr = currentWordItem.collectionOrderNr( collectionNr, collectionWordItem, commonWordItem ) ) > Constants.NO_ORDER_NR )
							{
							if( ++nWords == 2 )		// Found existing collection pair
								existingPairCollectionOrderNr_ = foundCollectionOrderNr;
							}
						}
					while( existingPairCollectionOrderNr_ == Constants.NO_ORDER_NR &&
					( currentWordItem = currentWordItem.nextWordItem() ) != null );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given collection word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given collection number is undefined" );

		return CommonVariables.result;
		}

	private byte collectGeneralizationWords( boolean isExclusiveGeneralization, boolean isQuestion, short generalizationWordTypeNr, short commonWordTypeNr, WordItem previousGeneralizationWordItem, WordItem newGeneralizationWordItem, WordItem commonWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		boolean foundCollection = false;
		int collectionNr = Constants.NO_COLLECTION_NR;

		if( previousGeneralizationWordItem != null )
			{
			if( newGeneralizationWordItem != null )
				{
				if( previousGeneralizationWordItem != newGeneralizationWordItem )
					{
					if( commonWordItem != null )
						{
						if( ( collectionNr = previousGeneralizationWordItem.collectionNr( generalizationWordTypeNr, commonWordItem, null ) ) == Constants.NO_COLLECTION_NR )
							collectionNr = newGeneralizationWordItem.collectionNr( generalizationWordTypeNr, commonWordItem, null );

						if( collectionNr == Constants.NO_COLLECTION_NR )
							{
							if( ( collectionNr = myWord_.highestCollectionNrInAllWords() ) < Constants.MAX_COLLECTION_NR )
								collectionNr++;
							else
								return myWord_.setSystemErrorInItem( 1, moduleNameString_, "Collection number overflow" );
							}
						else
							{
							if( ( collectionResult = newGeneralizationWordItem.findCollection( false, previousGeneralizationWordItem, commonWordItem ) ).result == Constants.RESULT_OK )
								{
								if( collectionResult.isCollected )
									foundCollection = true;
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if word \"" + previousGeneralizationWordItem.anyWordTypeString() + "\" is collected with word \"" + newGeneralizationWordItem.anyWordTypeString() + "\"" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						!foundCollection )
							{
							if( previousGeneralizationWordItem.createCollection( isExclusiveGeneralization, generalizationWordTypeNr, commonWordTypeNr, collectionNr, newGeneralizationWordItem, commonWordItem, null, null ) == Constants.RESULT_OK )
								{
								if( newGeneralizationWordItem.createCollection( isExclusiveGeneralization, generalizationWordTypeNr, commonWordTypeNr, collectionNr, previousGeneralizationWordItem, commonWordItem, null, null ) == Constants.RESULT_OK )
									{
									if( previousGeneralizationWordItem.collectGeneralizationAndSpecifications( isExclusiveGeneralization, true, isQuestion, collectionNr ) == Constants.RESULT_OK )
										{
										if( newGeneralizationWordItem.collectGeneralizationAndSpecifications( isExclusiveGeneralization, true, isQuestion, collectionNr ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the generalizations and specifications in the new generalization word" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the generalizations and specifications in the previous generalization word" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + newGeneralizationWordItem.anyWordTypeString() + "\" with word \"" + previousGeneralizationWordItem.anyWordTypeString() + "\"" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + previousGeneralizationWordItem.anyWordTypeString() + "\" with word \"" + newGeneralizationWordItem.anyWordTypeString() + "\"" );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given common word item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given previous and new generalization word items are the same" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given new generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given previous generalization word item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminCollection( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		existingPairCollectionOrderNr_ = Constants.NO_ORDER_NR;

		admin_ = admin;
		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();

		if( admin_ != null )
			{
			if( myWord_ == null )
				errorString = "The given my word is undefined";
			}
		else
			errorString = "The given admin is undefined";

		if( errorString != null )
			{
			if( myWord_ != null )
				myWord_.setSystemErrorInItem( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected methods

	protected CollectionResultType collectSpecificationWords( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, short generalizationWordTypeNr, short specificationWordTypeNr, WordItem compoundGeneralizationWordItem, WordItem generalizationWordItem, WordItem previousSpecificationWordItem, WordItem currentSpecificationWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		boolean foundCollection = false;
		short collectionOrderNr = Constants.NO_ORDER_NR;
		int collectionNr = Constants.NO_COLLECTION_NR;

		if( generalizationWordItem != null )
			{
			if( previousSpecificationWordItem != null )
				{
				if( currentSpecificationWordItem != null )
					{
					if( previousSpecificationWordItem != currentSpecificationWordItem )
						{
						if( compoundGeneralizationWordItem == null )
							{
							if( ( collectionNr = previousSpecificationWordItem.collectionNr( specificationWordTypeNr, generalizationWordItem ) ) == Constants.NO_COLLECTION_NR )
								{
								if( ( collectionNr = currentSpecificationWordItem.collectionNr( specificationWordTypeNr, generalizationWordItem ) ) == Constants.NO_COLLECTION_NR )
									{
									if( ( collectionNr = previousSpecificationWordItem.collectionNr( specificationWordTypeNr ) ) == Constants.NO_COLLECTION_NR )
										collectionNr = currentSpecificationWordItem.collectionNr( specificationWordTypeNr );
									}
								}
							}
						else
							collectionNr = myWord_.collectionNrInAllWords( specificationWordTypeNr, compoundGeneralizationWordItem );

						if( collectionNr > Constants.NO_COLLECTION_NR &&
						previousSpecificationWordItem.hasCollections() )
							{
							if( checkCollectionInAllWords( collectionNr, currentSpecificationWordItem, generalizationWordItem ) == Constants.RESULT_OK )
								{
								if( existingPairCollectionOrderNr_ > Constants.NO_ORDER_NR )
									{
									if( ( collectionOrderNr = myWord_.highestCollectionOrderNrInAllWords( collectionNr ) ) > 1 )
										{
										if( existingPairCollectionOrderNr_ < collectionOrderNr - 1 )	// "- 1" because collections come in pairs
											{
											collectionNr = Constants.NO_COLLECTION_NR;
											collectionResult.needToRedoSpecificationCollection = true;
											}
										}
									}
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the collection in all words" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( collectionNr > Constants.NO_COLLECTION_NR )
								{
								if( ( collectionResult = previousSpecificationWordItem.findCollection( true, currentSpecificationWordItem, generalizationWordItem ) ).result == Constants.RESULT_OK )
									{
									if( collectionResult.isCollected )
										foundCollection = true;
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" is collected with word \"" + previousSpecificationWordItem.anyWordTypeString() + "\"" );
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							!foundCollection )
								{
								if( collectionNr == Constants.NO_COLLECTION_NR )
									{
									if( ( collectionNr = myWord_.highestCollectionNrInAllWords() ) < Constants.MAX_COLLECTION_NR )
										collectionNr++;
									else
										myWord_.setSystemErrorInItem( 1, moduleNameString_, "Collection number overflow" );
									}

								if( collectionOrderNr < Constants.MAX_ORDER_NR - 2 )
									{
									if( previousSpecificationWordItem.createCollection( isExclusive, specificationWordTypeNr, generalizationWordTypeNr, collectionNr, currentSpecificationWordItem, generalizationWordItem, compoundGeneralizationWordItem, null ) == Constants.RESULT_OK )
										{
										if( currentSpecificationWordItem.createCollection( isExclusive, specificationWordTypeNr, generalizationWordTypeNr, collectionNr, previousSpecificationWordItem, generalizationWordItem, compoundGeneralizationWordItem, null ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" with word \"" + previousSpecificationWordItem.anyWordTypeString() + "\"" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + previousSpecificationWordItem.anyWordTypeString() + "\" with word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
									}
								else
									myWord_.setSystemErrorInItem( 1, moduleNameString_, "Collection order number overflow" );
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							!collectionResult.needToRedoSpecificationCollection )
								{
								if( generalizationWordItem.collectGeneralizationAndSpecifications( isExclusiveGeneralization, false, isQuestion, collectionNr ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the specification in the given generalization word" );
								}
							}
						}
					else
						myWord_.setErrorInItem( 1, moduleNameString_, "The given previous and current specification words are the same" );
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given current specification word item is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given previous specification word item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		collectionResult.result = CommonVariables.result;
		return collectionResult;
		}

	protected CollectionResultType collectSpecificationStrings( boolean isExclusive, boolean isExclusiveGeneralization, boolean isQuestion, short generalizationWordTypeNr, short specificationWordTypeNr, WordItem generalizationWordItem, String previousSpecificationString, String currentSpecificationString )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		int collectionNr;

		if( generalizationWordItem != null )
			{
			if( previousSpecificationString != null )
				{
				if( currentSpecificationString != null )
					{
					if( ( collectionNr = generalizationWordItem.collectionNr( specificationWordTypeNr, null ) ) == Constants.NO_COLLECTION_NR )
						{
						if( ( collectionNr = myWord_.highestCollectionNrInAllWords() ) < Constants.MAX_COLLECTION_NR )
							{
							if( generalizationWordItem.createCollection( isExclusive, specificationWordTypeNr, generalizationWordTypeNr, ++collectionNr, null, null, null, previousSpecificationString ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the previous specification string \"" + previousSpecificationString + "\" in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
							}
						else
							myWord_.setSystemErrorInItem( 1, moduleNameString_, "Collection number overflow" );
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( generalizationWordItem.createCollection( isExclusive, specificationWordTypeNr, generalizationWordTypeNr, collectionNr, null, null, null, currentSpecificationString ) == Constants.RESULT_OK )
							{
							if( generalizationWordItem.collectGeneralizationAndSpecifications( isExclusiveGeneralization, false, isQuestion, collectionNr ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the specification in the given generalization word" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect the current specification string \"" + currentSpecificationString + "\" in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
						}
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given current specification string item is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given previous specification string item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		collectionResult.result = CommonVariables.result;
		return collectionResult;
		}

	protected byte collectGeneralizationWordWithPreviousOne( boolean isExclusive, boolean isExclusiveGeneralization, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short questionParameter, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem foundSpecificationItem;
		WordItem currentGeneralizationWordItem;
		WordItem mostRecentGeneralizationWordItem = null;

		if( admin_.isGeneralizationReasoningWordType( true, generalizationWordTypeNr ) )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( ( currentGeneralizationItem = specificationWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
						{
						do	{
							if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
								{
								if( currentGeneralizationWordItem != generalizationWordItem &&
								currentGeneralizationItem.generalizationWordTypeNr() == generalizationWordTypeNr )
									{
									if( ( foundSpecificationItem = currentGeneralizationWordItem.firstAssignmentOrSpecification( isPossessive, questionParameter, relationContextNr, specificationWordItem ) ) != null )
										{
										if( foundSpecificationItem.isExclusive() == isExclusive ||
										foundSpecificationItem.hasGeneralizationCollection() )
											mostRecentGeneralizationWordItem = currentGeneralizationWordItem;
										}
									}
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );

						if( CommonVariables.result == Constants.RESULT_OK &&
						mostRecentGeneralizationWordItem != null )
							{
							if( collectGeneralizationWords( isExclusiveGeneralization, ( questionParameter > Constants.NO_QUESTION_PARAMETER ), generalizationWordTypeNr, specificationWordTypeNr, mostRecentGeneralizationWordItem, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect generalization words \"" + generalizationWordItem.anyWordTypeString() + "\" and \"" + mostRecentGeneralizationWordItem.anyWordTypeString() + "\"" );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find any generalization word" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word type is not a reasoning type" );

		return CommonVariables.result;
		}

	protected byte collectRelationWords( boolean isExclusive, short relationWordTypeNr, short commonWordTypeNr, WordItem previousRelationWordItem, WordItem currentRelationWordItem, WordItem commonWordItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		boolean foundCollection = false;
		int collectionNr = Constants.NO_COLLECTION_NR;

		if( previousRelationWordItem != null )
			{
			if( currentRelationWordItem != null )
				{
				if( previousRelationWordItem != currentRelationWordItem )
					{
					if( ( collectionNr = previousRelationWordItem.collectionNr( relationWordTypeNr, commonWordItem, null ) ) == Constants.NO_COLLECTION_NR )
						collectionNr = currentRelationWordItem.collectionNr( relationWordTypeNr, commonWordItem, null );

					if( collectionNr == Constants.NO_COLLECTION_NR )
						{
						if( ( collectionNr = myWord_.highestCollectionNrInAllWords() ) < Constants.MAX_COLLECTION_NR )
							collectionNr++;
						else
							return myWord_.setSystemErrorInItem( 1, moduleNameString_, "Collection number overflow" );
						}
					else
						{
						if( ( collectionResult = previousRelationWordItem.findCollection( false, currentRelationWordItem, commonWordItem ) ).result == Constants.RESULT_OK )
							{
							if( collectionResult.isCollected )
								foundCollection = true;
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if word \"" + previousRelationWordItem.anyWordTypeString() + "\" is collected with word \"" + currentRelationWordItem.anyWordTypeString() + "\"" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					!foundCollection )
						{
						if( previousRelationWordItem.createCollection( isExclusive, relationWordTypeNr, commonWordTypeNr, collectionNr, currentRelationWordItem, commonWordItem, null, null ) == Constants.RESULT_OK )
							{
							if( currentRelationWordItem.createCollection( isExclusive, relationWordTypeNr, commonWordTypeNr, collectionNr, previousRelationWordItem, commonWordItem, null, null ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + currentRelationWordItem.anyWordTypeString() + "\" with word \"" + previousRelationWordItem.anyWordTypeString() + "\"" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect word \"" + previousRelationWordItem.anyWordTypeString() + "\" with word \"" + currentRelationWordItem.anyWordTypeString() + "\"" );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given previous and current relation words are the same" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given current relation word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given previous relation word item is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"I will sing of your love and justice, O Lord.
 *	I will praise you with songs." (Psalm 101:1)
 *
 *************************************************************************/
