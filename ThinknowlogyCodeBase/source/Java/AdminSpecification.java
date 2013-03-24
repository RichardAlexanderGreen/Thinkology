/*
 *	Class:			AdminSpecification
 *	Supports class:	AdminItem
 *	Purpose:		To create and assign specification structures
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

class AdminSpecification
	{
	// Private constructible variables

	private short linkedGeneralizationWordTypeNr_;

	private int specificationRelationContextNr_;
	private int userRelationContextNr_;

	private int doneSpecificationPosition_;

	private WordItem linkedGeneralizationWordItem_;
	private WordItem userSentenceGeneralizationWordItem_;

	private String previousSpecificationString_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private SpecificationResultType addUserSpecificationWithRelation( boolean isAction, boolean isAssignedOrClear, boolean isAssignment, boolean isConditional, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isNewStart, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short selectionLevel, short selectionListNr, short imperativeParameter, short specificationWordParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, ReadItem startRelationReadItem, ReadItem endRelationWordReadItem, WordItem generalizationWordItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isFirstComparisonPart = ( selectionListNr == Constants.ADMIN_CONDITION_LIST );
		short relationWordTypeNr;
		short prepositionParameter = Constants.NO_PREPOSITION_PARAMETER;
		int nRelationWords = 0;
		ReadItem currentRelationReadItem = startRelationReadItem;
		SpecificationItem foundSpecificationItem;
		WordItem mostRecentContextWord;
		WordItem relationWordItem;
		WordItem previousRelationWordItem = null;

		if( startRelationReadItem != null )
			{
			if( endRelationWordReadItem != null )
				{
				if( generalizationWordItem != null )
					{
					if( specificationWordItem != null )
						{
						if( relationContextNr > Constants.NO_CONTEXT_NR )
							{
							do	{
								if( currentRelationReadItem.isRelationWord() &&
								currentRelationReadItem.readWordItem() != null )
									nRelationWords++;	// Count number of relation words in this sentence
								}
							while( currentRelationReadItem != endRelationWordReadItem &&
							( currentRelationReadItem = currentRelationReadItem.nextCurrentLanguageReadItem() ) != null );
							}

						currentRelationReadItem = startRelationReadItem;

						do	{
							if( currentRelationReadItem.isPreposition() )
								prepositionParameter = currentRelationReadItem.wordParameter();
							else
								{
								if( currentRelationReadItem.isRelationWord() )
									{
									if( ( relationWordItem = currentRelationReadItem.readWordItem() ) != null )
										{
										if( relationWordItem != generalizationWordItem )
											{
											if( relationWordItem != specificationWordItem )
												{
												relationWordTypeNr = currentRelationReadItem.wordTypeNr();

												if( nRelationWords == 1 &&
//												previousRelationWordItem == null &&
												( foundSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( false, false, isAssignment, isDeactive, isArchive, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, null ) ) != null )
													{
													if( foundSpecificationItem.hasRelationContext() &&
													( mostRecentContextWord = mostRecentContextWordInAllWords( isPossessive, relationWordTypeNr, foundSpecificationItem.relationContextNr(), specificationWordItem ) ) != null )
														{
														if( mostRecentContextWord != relationWordItem )
															previousRelationWordItem = mostRecentContextWord;
														}
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												previousRelationWordItem != null )
													{
													if( admin_.collectRelationWords( isExclusive, relationWordTypeNr, specificationWordTypeNr, previousRelationWordItem, relationWordItem, specificationWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect previous relation word \"" + previousRelationWordItem.anyWordTypeString() + "\" to relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
													}

												if( CommonVariables.result == Constants.RESULT_OK )
													{
													if( selectionListNr == Constants.NO_LIST_NR )
														{
														if( ( specificationResult = addSpecification( isAssignment, isConditional, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isSelection, false, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, Constants.NO_COLLECTION_NR, specificationCollectionNr, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, null, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result != Constants.RESULT_OK )
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification having relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
														}
													else
														{
														if( ( relationContextNr = relationWordItem.contextNrInWord( isPossessive, relationWordTypeNr, specificationWordItem ) ) == Constants.NO_CONTEXT_NR )
															{
															if( ( relationContextNr = admin_.highestContextNr() ) < Constants.MAX_CONTEXT_NR )
																{
																if( relationWordItem.addContext( isPossessive, relationWordTypeNr, specificationWordTypeNr, ++relationContextNr, specificationWordItem ) != Constants.RESULT_OK )
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a relation context to word \"" + relationWordItem.anyWordTypeString() + "\"" );
																}
															else
																myWord_.setSystemErrorInItem( 1, moduleNameString_, "Context number overflow" );
															}

														if( CommonVariables.result == Constants.RESULT_OK )
															{
															if( admin_.createSelectionPart( isAction, isAssignedOrClear, isDeactive, isArchive, ( isFirstComparisonPart && !relationWordItem.isNumeral() ), isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, selectionListNr, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) == Constants.RESULT_OK )
																isFirstComparisonPart = false;
															else
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a selection part having relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
															}
														}

													previousRelationWordItem = relationWordItem;
													}
												}
											else
												myWord_.setErrorInItem( 1, moduleNameString_, "The relation word is the same as the specification word" );
											}
										else
											myWord_.setErrorInItem( 1, moduleNameString_, "The relation word is the same as the generalization word" );
										}
									else
										myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined read word" );
									}
								}
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						currentRelationReadItem != endRelationWordReadItem &&
						( currentRelationReadItem = currentRelationReadItem.nextCurrentLanguageReadItem() ) != null );
						}
					else
						myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given end relation read item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given start relation read item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	private SpecificationResultType addSpecification( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( admin_.isSystemStartingUp() &&

			( generalizationWordItem.needsAuthorizationForChanges() ||

			( specificationWordItem != null &&
			specificationWordItem.needsAuthorizationForChanges() ) ) )
				{
				if( ( specificationResult = admin_.addSpecificationWithAuthorization( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, specificationRelationContextNr_, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add specification in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" with authorization" );
				}
			else
				{
				if( !generalizationWordItem.isVerbImperativeLogin() )	// Already created by during startup
					{
					if( ( specificationResult = generalizationWordItem.addSpecificationInWord( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, specificationRelationContextNr_, nContextRelations, specificationJustificationItem, specificationWordItem, relationWordItem, specificationString, null ) ).result != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add specification in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
					}
				}
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	private ContextItem contextItemInAllWords( boolean isPossessive, short contextWordTypeNr, WordItem specificationWordItem )
		{
		ContextItem foundContextItem;
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( foundContextItem = currentWordItem.contextItemInWord( isPossessive, contextWordTypeNr, specificationWordItem ) ) != null )
					return foundContextItem;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return null;
		}

	private WordItem mostRecentContextWordInAllWords( boolean isPossessive, short contextWordTypeNr, int contextNr, WordItem specificationWordItem )
		{
		ContextItem currentContextItem;
		ContextItem mostRecentContextItem = null;
		WordItem currentWordItem;
		WordItem mostRecentWordItem = null;

		if( contextNr > Constants.NO_CONTEXT_NR &&
		( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( currentContextItem = currentWordItem.contextItemInWord( isPossessive, contextWordTypeNr, contextNr, specificationWordItem ) ) != null )
					{
					if( mostRecentContextItem == null ||
					currentContextItem.isMoreRecent( mostRecentContextItem ) )
						{
						mostRecentWordItem = currentWordItem;
						mostRecentContextItem = currentContextItem;
						}
					}
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return mostRecentWordItem;
		}


	// Constructor

	protected AdminSpecification( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		linkedGeneralizationWordTypeNr_ = Constants.WORD_TYPE_UNDEFINED;

		doneSpecificationPosition_ = 0;

		specificationRelationContextNr_ = Constants.NO_CONTEXT_NR;
		userRelationContextNr_ = Constants.NO_CONTEXT_NR;

		linkedGeneralizationWordItem_ = null;
		userSentenceGeneralizationWordItem_ = null;
		previousSpecificationString_ = null;

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


	// Protected assignment methods

	protected byte assignSelectionSpecification( SelectionItem assignmentSelectionItem )
		{
		if( assignmentSelectionItem != null )
			{
			if( assignSpecification( false, assignmentSelectionItem.isAssignedOrClear(), assignmentSelectionItem.isDeactive(), assignmentSelectionItem.isArchive(), assignmentSelectionItem.isNegative(), assignmentSelectionItem.isPossessive(), false, assignmentSelectionItem.prepositionParameter(), Constants.NO_QUESTION_PARAMETER, assignmentSelectionItem.generalizationContextNr(), assignmentSelectionItem.specificationContextNr(), assignmentSelectionItem.relationContextNr(), Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, assignmentSelectionItem.nContextRelations(), null, assignmentSelectionItem.generalizationWordItem(), assignmentSelectionItem.specificationWordItem(), assignmentSelectionItem.specificationString() ).result != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign a specification" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given assignment selection item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType assignSpecification( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( admin_.isSystemStartingUp() &&

			( generalizationWordItem.needsAuthorizationForChanges() ||

			( specificationWordItem != null &&
			specificationWordItem.needsAuthorizationForChanges() ) ) )
				{
				if( ( specificationResult = admin_.assignSpecificationWithAuthorization( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, specificationString ) ).result != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to specification word \"" + specificationWordItem.anyWordTypeString() + "\" with authorization" );
				}
			else
				{
				if( ( specificationResult = generalizationWordItem.assignSpecificationInWord( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString, null ) ).result != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
				}
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}


	// Protected specification methods

	protected void initializeLinkedWord()
		{
		linkedGeneralizationWordTypeNr_ = Constants.WORD_TYPE_UNDEFINED;
		linkedGeneralizationWordItem_ = null;
		}

	protected void initializeAdminSpecificationVariables()
		{
//		linkedGeneralizationWordTypeNr_ = Constants.WORD_TYPE_UNDEFINED;	// Don't initialize

		doneSpecificationPosition_ = 0;

		specificationRelationContextNr_ = Constants.NO_CONTEXT_NR;
		userRelationContextNr_ = Constants.NO_CONTEXT_NR;

//		linkedGeneralizationWordItem_ = null;					// Don't initialize
		userSentenceGeneralizationWordItem_ = null;
		}

	protected CollectionResultType addUserSpecifications( boolean initializeVariables, boolean isAction, boolean isAssignment, boolean isConditional, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNewStart, boolean isPossessive, boolean isSpecificationGeneralization, short prepositionParameter, short questionParameter, short selectionLevel, short selectionListNr, short imperativeParameter, short specificationWordParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationPronounContextNr, ReadItem generalizationReadItem, ReadItem startSpecificationReadItem, ReadItem endSpecificationReadItem, ReadItem startRelationReadItem, ReadItem endRelationWordReadItem )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		ContextResultType contextResult = new ContextResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean foundAction = false;
		boolean isExclusiveContext = false;
		boolean isNegative = false;
		boolean isQuestion = ( questionParameter > Constants.NO_QUESTION_PARAMETER );
		boolean isSelection = ( selectionListNr != Constants.NO_LIST_NR );
		boolean isSpecificationWordAlreadyAssignedByComparison = false;
		boolean isValueSpecificationWord = false;
		boolean skipQuestionVerb = false;
		boolean skipThisGeneralizationPart = false;
		boolean waitForRelation = false;
		boolean waitForText = false;
		short generalizationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		short valueGeneralizationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		short linkedSpecificationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		short currentSpecificationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		short previousSpecificationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		int nContextRelations = 0;
		int lastSpecificationWordPosition;
		ReadItem currentReadItem;
		ReadItem readAheadGeneralizationReadItem;
		WordItem currentGeneralizationWordItem;
		WordItem tempSpecificationWordItem;
		WordItem currentSpecificationWordItem = null;
		WordItem compoundGeneralizationWordItem = null;
		WordItem valueGeneralizationWordItem = null;
		WordItem linkedSpecificationWordItem = null;
		WordItem previousSpecificationWordItem = null;
		String currentSpecificationString = null;

		previousSpecificationString_ = null;

		if( initializeVariables )
			{
			doneSpecificationPosition_ = 0;
			userRelationContextNr_ = Constants.NO_CONTEXT_NR;
			}

		if( generalizationReadItem != null )
			{
			if( ( currentGeneralizationWordItem = generalizationReadItem.readWordItem() ) != null )
				{
				generalizationWordTypeNr = generalizationReadItem.wordTypeNr();
				userSentenceGeneralizationWordItem_ = currentGeneralizationWordItem;
				currentGeneralizationWordItem.clearHasConfirmedAssumption();
				currentGeneralizationWordItem.clearLastShownConflictSpecification();

				if( ( currentReadItem = startSpecificationReadItem ) != null )
					{
					if( endSpecificationReadItem != null )
						{
						readAheadGeneralizationReadItem = generalizationReadItem.nextCurrentLanguageReadItem();

						while( readAheadGeneralizationReadItem != null &&
						readAheadGeneralizationReadItem.grammarParameter != Constants.GRAMMAR_GENERALIZATION_WORD )
							readAheadGeneralizationReadItem = readAheadGeneralizationReadItem.nextCurrentLanguageReadItem();

						lastSpecificationWordPosition = ( readAheadGeneralizationReadItem != null ? readAheadGeneralizationReadItem.wordPosition() : endSpecificationReadItem.wordPosition() );

						do	{
							if( currentReadItem.isQuestionVerb() )
								skipQuestionVerb = true;
							else
								{
								if( skipThisGeneralizationPart )
									{
									if( waitForRelation )
										{
										if( currentReadItem.isRelationWord() ||
										currentReadItem.isReadWordText() )
											{
											skipThisGeneralizationPart = false;
											waitForRelation = false;
											waitForText = false;
											}
										}
									else
										{
										if( generalizationReadItem.wordPosition() < currentReadItem.wordPosition() )
											skipThisGeneralizationPart = false;
										}
									}
								else
									{
									if( doneSpecificationPosition_ > currentReadItem.wordPosition() )
										skipThisGeneralizationPart = true;
									else
										{
										if( skipQuestionVerb &&
										!currentReadItem.isGeneralizationPart() )
											skipQuestionVerb = false;
										else
											{
											if( currentReadItem.isGeneralizationWord() )
												{
												skipThisGeneralizationPart = true;
												waitForRelation = true;
												}
											else
												{
												if( currentReadItem.isSpecificationWord() &&
												currentGeneralizationWordItem.isAdjectiveComparison() &&
												( tempSpecificationWordItem = currentReadItem.readWordItem() ) != null )
													{
													if( !tempSpecificationWordItem.isNounHead() &&
													!tempSpecificationWordItem.isNounTail() )		// Skip head and tail in the comparison
														{
														waitForText = true;
														isSpecificationWordAlreadyAssignedByComparison = true;
														currentSpecificationWordItem = tempSpecificationWordItem;
														}
													}
												}
											}
										}
									}
								}

							if( !waitForText &&
							!skipQuestionVerb &&
							!skipThisGeneralizationPart )
								{
								currentSpecificationWordTypeNr = currentReadItem.wordTypeNr();
								currentSpecificationString = null;

								if( isSpecificationWordAlreadyAssignedByComparison )
									isSpecificationWordAlreadyAssignedByComparison = false;
								else
									currentSpecificationWordItem = currentReadItem.readWordItem();

								if( currentReadItem.isReadWordText() ||

								( currentSpecificationWordItem != null &&
								currentSpecificationWordItem.needsAuthorizationForChanges() ) )	// Password
									currentSpecificationString = currentReadItem.readString;

								if( currentReadItem.isNegative() )
									isNegative = true;
								else
									{
									if( isPossessive &&
									currentReadItem.isReadWordNumeral() )
										nContextRelations = Integer.parseInt( currentReadItem.readWordTypeString() );
									else
										{
										if( initializeVariables &&
										startRelationReadItem != null &&
										currentReadItem.isSpecificationWord() )
											{
											if( ( contextResult = admin_.getRelationContextNr( isExclusive, isNegative, isPossessive, true, questionParameter, nContextRelations, generalizationContextNr, specificationContextNr, currentGeneralizationWordItem, currentSpecificationWordItem, null, startRelationReadItem ) ).result == Constants.RESULT_OK )
												{
												foundAction = true;
												isExclusiveContext = contextResult.isExclusiveContext;
												userRelationContextNr_ = contextResult.contextNr;
												}
											else
												myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to get the relation context number" );
											}

										if( CommonVariables.result == Constants.RESULT_OK &&

										( currentReadItem.isSpecificationWord() ||
										currentReadItem.isReadWordText() ||
										currentReadItem.isReadWordNumeral() ||

										( currentReadItem.isRelationWord() &&
										admin_.isPossessivePronounStructure() ) ) )
											{
											if( isValueSpecificationWord )
												{
												if( isSelection )
													{
													if( admin_.createSelectionPart( isAction, false, isDeactive, isArchive, false, isNewStart, isNegative, isPossessive, true, selectionLevel, selectionListNr, imperativeParameter, prepositionParameter, specificationWordParameter, valueGeneralizationWordTypeNr, currentSpecificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, generalizationContextNr, specificationContextNr, relationPronounContextNr, nContextRelations, valueGeneralizationWordItem, currentSpecificationWordItem, null, currentSpecificationString ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to create a value selection item" );
													}

												if( CommonVariables.result == Constants.RESULT_OK )
													{
													if( startRelationReadItem == null )
														{
														if( addSpecification( isAssignment, isConditional, isDeactive, isArchive, isExclusive, ( isConditional ? false : isNegative ), isPossessive, isSelection, false, isSpecificationGeneralization, true, prepositionParameter, questionParameter, valueGeneralizationWordTypeNr, currentSpecificationWordTypeNr, linkedGeneralizationWordTypeNr_, Constants.NO_COLLECTION_NR, specificationCollectionNr, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, nContextRelations, null, valueGeneralizationWordItem, currentSpecificationWordItem, linkedGeneralizationWordItem_, currentSpecificationString ).result != Constants.RESULT_OK )
															myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add a specification in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
														}
													else
														{
														if( addUserSpecificationWithRelation( false, false, isAssignment, isConditional, isDeactive, isArchive, ( isExclusive || isExclusiveContext ), ( isConditional ? false : isNegative ), false, isPossessive, isSelection, isSpecificationGeneralization, true, Constants.NO_SELECTION_LEVEL, Constants.NO_LIST_NR, imperativeParameter, specificationWordParameter, questionParameter, valueGeneralizationWordTypeNr, currentSpecificationWordTypeNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, userRelationContextNr_, nContextRelations, startRelationReadItem, endRelationWordReadItem, valueGeneralizationWordItem, currentSpecificationWordItem, currentSpecificationString ).result == Constants.RESULT_OK )
															foundAction = true;
														else
															myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add a specification in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" with a relation" );
														}

													if( CommonVariables.result == Constants.RESULT_OK )
														{
														isValueSpecificationWord = false;
														valueGeneralizationWordTypeNr = Constants.WORD_TYPE_UNDEFINED;
														valueGeneralizationWordItem = null;
														}
													}
												}
											else
												{
												if( currentReadItem.isNounValue() )
													{
													isValueSpecificationWord = true;
													valueGeneralizationWordTypeNr = generalizationWordTypeNr;
													valueGeneralizationWordItem = currentGeneralizationWordItem;
													}
												else
													{
													if( selectionListNr == Constants.NO_LIST_NR )
														{
														if( previousSpecificationString_ == null )
															{
															if( previousSpecificationWordItem != null &&

															( previousSpecificationWordTypeNr == currentSpecificationWordTypeNr ||

															( previousSpecificationWordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR &&
															currentReadItem.isReadWordPluralNoun() ) ||

															( previousSpecificationWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL &&
															currentReadItem.isReadWordSingularNoun() ) ) )
																{
																if( ( collectionResult = admin_.collectSpecificationWords( ( isExclusive || isSpecificationGeneralization ), isAssignment, isQuestion, generalizationWordTypeNr, currentSpecificationWordTypeNr, compoundGeneralizationWordItem, currentGeneralizationWordItem, previousSpecificationWordItem, currentSpecificationWordItem ) ).result != Constants.RESULT_OK )
																	myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to collect specification words" );
																}
															}
														else
															{
															if( ( collectionResult = admin_.collectSpecificationStrings( ( isExclusive || isSpecificationGeneralization ), isAssignment, isQuestion, generalizationWordTypeNr, currentSpecificationWordTypeNr, currentGeneralizationWordItem, previousSpecificationString_, currentSpecificationString ) ).result != Constants.RESULT_OK )
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to collect specification strings" );
															}

														previousSpecificationWordItem = currentSpecificationWordItem;
														previousSpecificationWordTypeNr = currentSpecificationWordTypeNr;
														previousSpecificationString_ = currentSpecificationString;
														}
													else	// Create selection
														{
														if( startRelationReadItem == null )
															{
															if( admin_.createSelectionPart( isAction, currentReadItem.isAdjectiveAssignedOrClear(), isDeactive, isArchive, false, isNewStart, isNegative, isPossessive, false, selectionLevel, selectionListNr, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, currentSpecificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, generalizationContextNr, specificationContextNr, userRelationContextNr_, nContextRelations, currentGeneralizationWordItem, currentSpecificationWordItem, null, currentSpecificationString ) != Constants.RESULT_OK )
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to create a selection part" );
															}
														else
															{
															if( addUserSpecificationWithRelation( isAction, currentReadItem.isAdjectiveAssignedOrClear(), isAssignment, isConditional, isDeactive, isArchive, ( isExclusive || isExclusiveContext ), isNegative, isNewStart, isPossessive, isSelection, isSpecificationGeneralization, false, selectionLevel, selectionListNr, imperativeParameter, specificationWordParameter, questionParameter, generalizationWordTypeNr, currentSpecificationWordTypeNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, userRelationContextNr_, nContextRelations, startRelationReadItem, endRelationWordReadItem, currentGeneralizationWordItem, currentSpecificationWordItem, currentSpecificationString ).result != Constants.RESULT_OK )
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to create a selection part with relation" );
															}
														}
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												!collectionResult.needToRedoSpecificationCollection &&
												!currentReadItem.isAdjectiveAssignedOrClear() )
													{
													linkedSpecificationWordTypeNr = currentSpecificationWordTypeNr;
													doneSpecificationPosition_ = currentReadItem.wordPosition();
													linkedSpecificationWordItem = currentSpecificationWordItem;

													if( currentSpecificationWordTypeNr != Constants.WORD_TYPE_ADVERB &&
													!currentReadItem.isNounValue() &&
													currentReadItem.wordPosition() <= lastSpecificationWordPosition )
														{
														if( startRelationReadItem == null )
															{
															if( ( specificationResult = addSpecification( isAssignment, isConditional, isDeactive, isArchive, isExclusive, ( isConditional ? false : isNegative ), isPossessive, isSelection, false, isSpecificationGeneralization, false, prepositionParameter, questionParameter, generalizationWordTypeNr, currentSpecificationWordTypeNr, linkedGeneralizationWordTypeNr_, Constants.NO_COLLECTION_NR, specificationCollectionNr, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, userRelationContextNr_, nContextRelations, null, currentGeneralizationWordItem, currentSpecificationWordItem, linkedGeneralizationWordItem_, currentSpecificationString ) ).result == Constants.RESULT_OK )
																foundAction = true;
															else
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add a specification in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
															}
														else
															{
															if( ( specificationResult = addUserSpecificationWithRelation( false, false, isAssignment, isConditional, isDeactive, isArchive, ( isExclusive || isExclusiveContext ), ( isConditional ? false : isNegative ), false, isPossessive, isSelection, isSpecificationGeneralization, false, Constants.NO_SELECTION_LEVEL, Constants.NO_LIST_NR, imperativeParameter, specificationWordParameter, questionParameter, generalizationWordTypeNr, currentSpecificationWordTypeNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, userRelationContextNr_, nContextRelations, startRelationReadItem, endRelationWordReadItem, currentGeneralizationWordItem, currentSpecificationWordItem, currentSpecificationString ) ).result == Constants.RESULT_OK )
																foundAction = true;
															else
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add a user specification with a relation in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
															}

														if( CommonVariables.result == Constants.RESULT_OK &&
														!isQuestion &&
														!isSpecificationGeneralization &&
														myWord_.isNounWordType( currentSpecificationWordTypeNr ) )
															{
															// Generalizations with noun specifications - without relations
															if( admin_.findSpecificationSubstitutionConclusionOrQuestion( false, isDeactive, isArchive, isExclusive, isNegative, isPossessive, true, questionParameter, generalizationWordTypeNr, currentSpecificationWordTypeNr, generalizationContextNr, specificationContextNr, currentGeneralizationWordItem, currentSpecificationWordItem ) == Constants.RESULT_OK )
																{
																if( !CommonVariables.hasShownWarning &&
																myWord_.isNounWordType( generalizationWordTypeNr ) )	// Definition specification
																	{
																	// Definition specifications
																	if( ( specificationResult = admin_.findCompoundSpecificationSubstitutionConclusion( isNegative, isPossessive, generalizationWordTypeNr, generalizationContextNr, specificationContextNr, userRelationContextNr_, currentGeneralizationWordItem ) ).result == Constants.RESULT_OK )
																		compoundGeneralizationWordItem = specificationResult.compoundGeneralizationWordItem;
																	else
																		myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to find a definition specification substitution conclusion about generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
																	}
																}
															else
																myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
															}
														}
													}
												}
											}
										else
											{
											if( isPossessive &&
											currentReadItem.isReadWordArticle() )
												nContextRelations = 0;
											}

										if( CommonVariables.result == Constants.RESULT_OK &&
										!CommonVariables.hasShownWarning &&
										!collectionResult.needToRedoSpecificationCollection &&
										!isSpecificationGeneralization )
											{
											if( currentReadItem.isReadWordVerb() )
												foundAction = true;

											if( currentReadItem.isRelationWord() ||

											( admin_.isPossessivePronounStructure() &&
											currentReadItem.isSpecificationWord() ) )
												{
												if( linkedSpecificationWordItem != null )
													{
													if( !isQuestion &&
													myWord_.isNounWordType( linkedSpecificationWordTypeNr ) )
														{
														if( admin_.findExclusiveSpecificationSubstitutionAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, linkedSpecificationWordTypeNr, currentSpecificationWordTypeNr, generalizationContextNr, specificationContextNr, currentGeneralizationWordItem, linkedSpecificationWordItem, currentSpecificationWordItem ) != Constants.RESULT_OK )
															myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to find an exclusive specification substitution assumption with specification word \"" + linkedSpecificationWordItem.anyWordTypeString() + "\"" );
														}

													if( CommonVariables.result == Constants.RESULT_OK &&
													!CommonVariables.hasShownWarning )
														{
														if( currentSpecificationWordItem == linkedSpecificationWordItem )	// Linked specification
															{
															if( linkedGeneralizationWordItem_ == null )
																{
																linkedGeneralizationWordTypeNr_ = generalizationWordTypeNr;
																linkedGeneralizationWordItem_ = currentGeneralizationWordItem;
																}
															else
																myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "Linked word \"" + linkedGeneralizationWordItem_.anyWordTypeString() + "\" is already assigned" );
															}
														else
															{
															if( !isQuestion )
																{
																if( admin_.findPossessiveReversibleConclusion( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, linkedSpecificationWordTypeNr, currentSpecificationWordTypeNr, specificationContextNr, relationPronounContextNr, userRelationContextNr_, currentGeneralizationWordItem, linkedSpecificationWordItem, currentSpecificationWordItem ) != Constants.RESULT_OK )
																	myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to find a possessive reversible conclusion with generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" and specification word \"" + linkedSpecificationWordItem.anyWordTypeString() + "\"" );
																}
															}
														}
													}
												else
													myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The specification word item is undefined while linking" );
												}
											else
												{
												if( selectionListNr == Constants.NO_LIST_NR &&
												currentReadItem.isAdjectiveAssigned() &&
												currentReadItem.readWordItem() != null )	// Skip text
													{
													if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_WARNING_ASSIGNED_IS_SELECTION_CONDITION_START, currentReadItem.readWordTypeString(), Constants.INTERFACE_SENTENCE_WARNING_AT_POSITION_START, currentReadItem.wordPosition(), Constants.INTERFACE_SENTENCE_WARNING_IS_ONLY_USED_IN_CONDITION_OF_SELECTION ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
													}
												}
											}
										}
									}
								}
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						!CommonVariables.hasShownWarning &&
						!collectionResult.needToRedoSpecificationCollection &&
						currentReadItem != endSpecificationReadItem &&
						( currentReadItem = currentReadItem.nextCurrentLanguageReadItem() ) != null );

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( foundAction )
								{
								if( currentGeneralizationWordItem.needToRecalculateAssumptionsAfterwards() )
									{
									if( currentGeneralizationWordItem.recalculateAssumptionsOfInvolvedWords() != Constants.RESULT_OK )
										myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to recalculate the assumptions of generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" and the involved words" );
									}
								}
							else
								myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I couldn't find anything to do" );
							}
						}
					else
						myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given end specification read item is undefined" );
					}
				else
					myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given start specification read item is undefined" );
				}
			else
				myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given generalization read item has no read word" );
			}
		else
			myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given generalization read item is undefined" );

		collectionResult.result = CommonVariables.result;
		return collectionResult;
		}

	protected SpecificationResultType addSpecification( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSelfGenerated, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean hasRelationContextCollection;
		boolean isAmbiguousRelationContext = false;
		ContextItem foundContextItem;
		SpecificationItem archiveAssignmentItem;
		SpecificationItem foundSpecificationItem;
		SpecificationItem correctedSuggestiveQuestionAssumptionSpecificationItem;
		SpecificationItem createdAssignmentItem = null;
		SpecificationItem createdSpecificationItem = null;
		WordItem foundContextWordItem;

		specificationRelationContextNr_ = relationContextNr;

		if( generalizationWordItem != null )
			{
			if( generalizationWordItem != myWord_ )
				{
				if( specificationWordItem != myWord_ )
					{
					if( generalizationWordItem != specificationWordItem )
						{
						if( relationWordItem != null &&
						!relationWordItem.isNumeral() )
							{
							if( specificationRelationContextNr_ == Constants.NO_CONTEXT_NR )
								{
								hasRelationContextCollection = ( myWord_.collectionNrInAllWords( userRelationContextNr_ ) > Constants.NO_COLLECTION_NR );

								if( ( specificationRelationContextNr_ = relationWordItem.contextNrInWord( isPossessive, relationWordTypeNr, specificationWordItem ) ) == Constants.NO_CONTEXT_NR )
									{
									if( ( foundContextItem = contextItemInAllWords( isPossessive, relationWordTypeNr, specificationWordItem ) ) != null )
										{
										if( ( foundContextWordItem = foundContextItem.myWord() ) != null )
											{
											if( ( specificationResult = generalizationWordItem.findSpecification( true, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, foundContextWordItem ) ).result == Constants.RESULT_OK )
												{
												if( specificationResult.foundSpecificationItem == null )
													{
													if( ( foundSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, specificationString ) ) != null )
														specificationRelationContextNr_ = foundSpecificationItem.relationContextNr();
													}
												else
													specificationRelationContextNr_ = foundContextItem.contextNr();
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specific specification in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
											}
										else
											myWord_.setErrorInItem( 1, moduleNameString_, "The word of the found context item is undefined" );
										}

									if( CommonVariables.result == Constants.RESULT_OK &&

									( specificationRelationContextNr_ == Constants.NO_CONTEXT_NR ||

									( !hasRelationContextCollection &&
									!relationWordItem.hasContextInWord( isPossessive, specificationRelationContextNr_, specificationWordItem ) ) ) )
										{
										if( ( specificationRelationContextNr_ = admin_.highestContextNr() ) < Constants.MAX_CONTEXT_NR )
											specificationRelationContextNr_++;
										else
											myWord_.setSystemErrorInItem( 1, moduleNameString_, "Context number overflow" );
										}
									}
								else
									{
									if( isAssignment &&
									!hasRelationContextCollection &&
									// Check if already exists
									generalizationWordItem.firstActiveAssignment( false, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationRelationContextNr_, specificationWordItem, null ) == null )
										{
										if( ( specificationRelationContextNr_ = admin_.highestContextNr() ) < Constants.MAX_CONTEXT_NR )
											{
											// Dynamic semantic ambiguity
											if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_I_NOTICED_SEMANTIC_AMBIGUITY_START, relationWordItem.anyWordTypeString(), Constants.INTERFACE_SENTENCE_NOTIFICATION_DYNAMIC_SEMANTIC_AMBIGUITY_END ) == Constants.RESULT_OK )
												{
												isAmbiguousRelationContext = true;
												specificationRelationContextNr_++;
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
											}
										else
											myWord_.setSystemErrorInItem( 1, moduleNameString_, "Context number overflow" );
										}
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							!isSelection )
								{
								if( relationWordItem.addContext( isPossessive, relationWordTypeNr, specificationWordTypeNr, specificationRelationContextNr_, specificationWordItem ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a relation context to word \"" + relationWordItem.anyWordTypeString() + "\"" );
								}
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( ( specificationResult = addSpecification( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, nContextRelations, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
								{
								if( !CommonVariables.hasShownWarning )
									{
									archiveAssignmentItem = specificationResult.archiveAssignmentItem;

									if( specificationJustificationItem == null &&	// User specification
									( createdSpecificationItem = specificationResult.createdSpecificationItem ) != null )
										{
										if( admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) )
											{
											if( admin_.collectGeneralizationWordWithPreviousOne( isExclusive, ( isAssignment && questionParameter == Constants.NO_QUESTION_PARAMETER ), isPossessive, generalizationWordTypeNr, specificationWordTypeNr, questionParameter, specificationRelationContextNr_, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect a generalization word with a previous one" );
											}
										else
											{
											if( !isAssignment &&
											!isExclusive &&
											!isNegative &&
											!isPossessive &&
											!isSpecificationGeneralization &&
											!generalizationWordItem.hasCollection( specificationWordItem ) &&
											generalizationContextNr == Constants.NO_CONTEXT_NR &&
											specificationContextNr == Constants.NO_CONTEXT_NR &&
											nContextRelations == 0 &&
											relationWordItem == null &&
											myWord_.isNounWordType( specificationWordTypeNr ) &&
											admin_.isGeneralizationReasoningWordType( true, generalizationWordTypeNr ) )
												{
												if( admin_.addSpecificationGeneralizationConclusion( generalizationWordTypeNr, specificationWordTypeNr, createdSpecificationItem, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a possible specification generalization definition" );
												}
											}
										}

									if( CommonVariables.result == Constants.RESULT_OK &&
									!CommonVariables.hasShownWarning &&
									!isSpecificationGeneralization &&

									( ( isAssignment &&
									!isSelection ) ||	// Selections must be stored - rather than executed. So, don't assign them.

									archiveAssignmentItem != null ) )
										{
										if( ( specificationResult = assignSpecification( isAmbiguousRelationContext, ( specificationWordItem != null && specificationWordItem.isAdjectiveAssignedOrClear() ), isDeactiveAssignment, isArchiveAssignment, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, specificationRelationContextNr_, nContextRelations, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, specificationJustificationItem, generalizationWordItem, specificationWordItem, specificationString ) ).result == Constants.RESULT_OK )
											{
											if( ( createdAssignmentItem = specificationResult.createdSpecificationItem ) != null )
												{
												if( archiveAssignmentItem != null )
													{
													if( generalizationWordItem.archiveOrDeleteSpecification( archiveAssignmentItem, createdAssignmentItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to archive or delete an assignment in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												relationWordItem != null )
													{
													if( ( correctedSuggestiveQuestionAssumptionSpecificationItem = generalizationWordItem.correctedSuggestiveQuestionAssumptionSpecificationItem() ) != null )
														{
														createdAssignmentItem = createdAssignmentItem.updatedSpecificationItem();

														if( admin_.findSpecificationSubstitutionConclusionOrQuestion( createdAssignmentItem.isSelfGeneratedAssumption(), createdAssignmentItem.isDeactiveAssignment(), createdAssignmentItem.isArchiveAssignment(), true, createdAssignmentItem.isNegative(), createdAssignmentItem.isPossessive(), true, createdAssignmentItem.questionParameter(), createdAssignmentItem.generalizationWordTypeNr(), createdAssignmentItem.specificationWordTypeNr(), createdAssignmentItem.generalizationContextNr(), createdAssignmentItem.specificationContextNr(), generalizationWordItem, correctedSuggestiveQuestionAssumptionSpecificationItem.specificationWordItem() ) == Constants.RESULT_OK )
															{
															if( admin_.findSpecificationSubstitutionConclusionOrQuestion( createdAssignmentItem.isSelfGeneratedAssumption(), createdAssignmentItem.isDeactiveAssignment(), createdAssignmentItem.isArchiveAssignment(), true, createdAssignmentItem.isNegative(), createdAssignmentItem.isPossessive(), true, createdAssignmentItem.questionParameter(), createdAssignmentItem.generalizationWordTypeNr(), createdAssignmentItem.specificationWordTypeNr(), createdAssignmentItem.generalizationContextNr(), createdAssignmentItem.specificationContextNr(), generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
														}

													if( CommonVariables.result == Constants.RESULT_OK &&
													generalizationWordItem.addSuggestiveQuestionAssumption() )
														{
														if( admin_.addSuggestiveQuestionAssumption( isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, specificationContextNr, specificationRelationContextNr_, createdAssignmentItem, generalizationWordItem, specificationWordItem, relationWordItem ) != Constants.RESULT_OK )
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a suggestive question assumption about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
														}
													}
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the specification" );
										}
									}
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add specification in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
							}
						}
					else
						myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word and the given specification word are the same" );
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word is the administrator" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word is the administrator" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected WordItem userSentenceGeneralizationWordItem()
		{
		return userSentenceGeneralizationWordItem_;
		}
	};

/*************************************************************************
 *
 *	"How amazing are the deeds of the Lord!
 *	All who delight in him should ponder them." (Psalm 111:2)
 *
 *************************************************************************/
