/*
 *	Class:			AdminConclusion
 *	Supports class:	AdminItem
 *	Purpose:		To create conclusions and questions autonomously
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

class AdminConclusion
	{
	// Private constructible variables

	private JustificationItem possessiveReversibleConclusionJustificationItem_;

	private SpecificationItem createdConclusionSpecificationItem_;
	private SpecificationItem foundConclusionSpecificationItem_;
	private SpecificationItem foundSpecificationGeneralizationDefinitionSpecificationItem_;
	private SpecificationItem lastCreatedSpecificationSubstitutionConclusionSpecificationItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean isConclusion( short justificationTypeNr )
		{
		return ( justificationTypeNr == Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION );
		}

	private byte addSpecificationGeneralizationConclusion( SpecificationItem definitionSpecificationItem )
		{
		if( definitionSpecificationItem != null )
			{
			if( definitionSpecificationItem.isGeneralizationNoun() &&
			definitionSpecificationItem.isSpecificationNoun() )
				{
				if( addConclusion( false, false, false, false, false, false, Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.WORD_TYPE_NOUN_PLURAL, definitionSpecificationItem.generalizationWordTypeNr(), Constants.WORD_TYPE_UNDEFINED, Constants.NO_COLLECTION_NR, definitionSpecificationItem.specificationCollectionNr(), definitionSpecificationItem.relationCollectionNr(), Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, definitionSpecificationItem, null, null, definitionSpecificationItem.specificationWordItem(), definitionSpecificationItem.generalizationWordItem(), null ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification generalization substitution conclusion" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The generalization word and/or specification word of the given definition specification item is not a noun" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given definition specification item is undefined" );

		return CommonVariables.result;
		}

	private byte findSpecificationGeneralizationDefinition( short generalizationWordTypeNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem foundSpecificationItem;
		WordItem currentGeneralizationWordItem;

		foundSpecificationGeneralizationDefinitionSpecificationItem_ = null;

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
							currentGeneralizationItem.generalizationWordTypeNr() == generalizationWordTypeNr &&
							( foundSpecificationItem = currentGeneralizationWordItem.firstAssignmentOrSpecification( false, false, false, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, specificationWordItem ) ) != null )
								{
								if( !foundSpecificationItem.isExclusive() &&
								!foundSpecificationItem.hasSpecificationCollection() )
									foundSpecificationGeneralizationDefinitionSpecificationItem_ = foundSpecificationItem;
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					foundSpecificationGeneralizationDefinitionSpecificationItem_ == null &&
					( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}

	private byte findSpecificationSubstitutionQuestion( boolean isCorrectedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, SpecificationItem currentSpecificationItem, SpecificationItem definitionSpecificationItem, WordItem generalizationWordItem, WordItem currentGeneralizationWordItem, WordItem specificationWordItem, WordItem currentSpecificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean blockCommonWordOfCompoundCollection = false;
		boolean isAssignment = ( isDeactive || isArchive || isNegative );
		int noCompoundCommonWordCollectionNr;
		int nonCompoundSpecificationCollectionNr;
		SpecificationItem adjustedQuestionSpecificationItem = null;
		WordItem commonWordItem;

		if( currentSpecificationItem != null )
			{
			if( generalizationWordItem != null )
				{
				if( currentGeneralizationWordItem != null )
					{
					if( specificationWordItem != null )
						{
						if( currentSpecificationWordItem != null )
							{
							nonCompoundSpecificationCollectionNr = currentSpecificationWordItem.nonCompoundCollectionNr( specificationWordTypeNr );
							commonWordItem = currentSpecificationWordItem.commonWordItem( specificationCollectionNr, currentGeneralizationWordItem );

							if( nonCompoundSpecificationCollectionNr > Constants.NO_COLLECTION_NR &&
							currentSpecificationItem.hasSpecificationCompoundCollection() &&
							currentSpecificationWordItem.isCompoundCollection( specificationCollectionNr, specificationWordItem ) )
								{
								// Detect a conflict
								if( generalizationWordItem.checkForSpecificationConflict( true, isExclusive, isNegative, isPossessive, specificationWordTypeNr, nonCompoundSpecificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), currentSpecificationItem.relationContextNr(), currentSpecificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) == Constants.RESULT_OK )
									{
									if( ( specificationResult = generalizationWordItem.findQuestionToAdjustedByCompoundCollection( isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, currentSpecificationItem.relationContextNr(), specificationWordItem ) ).result == Constants.RESULT_OK )
										{
										if( ( adjustedQuestionSpecificationItem = specificationResult.adjustedQuestionSpecificationItem ) == null )
											{
											if( currentSpecificationWordItem.commonWordItem( specificationCollectionNr, currentGeneralizationWordItem ) != null )
												blockCommonWordOfCompoundCollection = true;
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to inf a question to be adjusted by a compound collection in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" by compound collection" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for a specification conflict in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
								}
							else
								{
								if( !isCorrectedAssumption )
									{
									if( nonCompoundSpecificationCollectionNr == Constants.NO_COLLECTION_NR )
										nonCompoundSpecificationCollectionNr = myWord_.nonCompoundCollectionNrInAllWords( specificationCollectionNr );

									if( ( specificationResult = generalizationWordItem.findRelatedSpecification( false, false, false, isPossessive, Constants.NO_QUESTION_PARAMETER, nonCompoundSpecificationCollectionNr, currentSpecificationItem.relationCollectionNr(), generalizationContextNr, specificationContextNr, currentSpecificationItem.relationContextNr(), specificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
										{
										if( specificationResult.relatedSpecificationItem != null )
											blockCommonWordOfCompoundCollection = true;
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a related specification" );
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							!CommonVariables.hasShownWarning &&
							!blockCommonWordOfCompoundCollection )
								{
								if( commonWordItem != null &&
								( noCompoundCommonWordCollectionNr = commonWordItem.nonCompoundCollectionNr( specificationWordTypeNr ) ) > Constants.NO_COLLECTION_NR )
									{
									if( generalizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, noCompoundCommonWordCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, commonWordItem ) != null )
										{
										if( generalizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, commonWordItem ) == null )
											blockCommonWordOfCompoundCollection = true;
										}
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								!blockCommonWordOfCompoundCollection &&
								!generalizationWordItem.isCorrectedAssumptionByKnowledge() )
									{
									// Create a question
									if( addConclusion( isAssignment, isDeactive, isArchive, true, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION, Constants.NO_PREPOSITION_PARAMETER, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, currentSpecificationItem.generalizationCollectionNr(), specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), Constants.NO_CONTEXT_NR, ( isCorrectedAssumption ? null : definitionSpecificationItem ), ( generalizationWordItem.isCorrectedAssumption() ? definitionSpecificationItem : null ), currentSpecificationItem, generalizationWordItem, currentSpecificationWordItem, null ) == Constants.RESULT_OK )
										{
										if( adjustedQuestionSpecificationItem != null )
											{
											// Write adjusted question
											if( generalizationWordItem.writeSpecification( true, false, false, adjustedQuestionSpecificationItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an adjusted question" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification substitution question about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" with specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
									}
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The given current specfication word item is undefined" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given specfication word item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given current generalization word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given current specfication item is undefined" );

		return CommonVariables.result;
		}

	private byte addConclusion( boolean isAssignment, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short justificationTypeNr, short prepositionParamater, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean hasCreatedJustification = false;
		boolean isQuestion = ( questionParameter > Constants.NO_QUESTION_PARAMETER );
		int createdRelationContextNr;
		JustificationItem specificationJustificationItem = null;

		possessiveReversibleConclusionJustificationItem_ = null;
		createdConclusionSpecificationItem_ = null;
		foundConclusionSpecificationItem_ = null;

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				if( isConclusion( justificationTypeNr ) )
					{
					if( isQuestion ||

					( ( definitionSpecificationItem == null ||

					( !definitionSpecificationItem.isQuestion() &&
					!definitionSpecificationItem.isSelfGeneratedAssumption() ) ) &&

					( specificSpecificationItem == null ||

					( !specificSpecificationItem.isQuestion() &&
					!specificSpecificationItem.isSelfGeneratedAssumption() ) ) ) )
						{
						if( ( justificationResult = generalizationWordItem.addJustification( false, justificationTypeNr, (short)1, CommonVariables.currentSentenceNr, null, definitionSpecificationItem, anotherDefinitionSpecificationItem, specificSpecificationItem ) ).result == Constants.RESULT_OK )
							{
							if( ( specificationJustificationItem = justificationResult.foundJustificationItem ) == null )
								{
								hasCreatedJustification = true;
								specificationJustificationItem = justificationResult.createdJustificationItem;
								}

							if( specificationJustificationItem != null )
								{
								if( ( specificationResult = admin_.addSpecification( isAssignment, isDeactive, isArchive, isExclusive, isNegative, isPossessive, true, ( justificationTypeNr == Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION ), false, prepositionParamater, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, 0, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, null ) ).result == Constants.RESULT_OK )
									{
									if( !CommonVariables.hasShownWarning )
										{
										createdConclusionSpecificationItem_ = specificationResult.createdSpecificationItem;
										foundConclusionSpecificationItem_ = specificationResult.foundSpecificationItem;

										if( createdConclusionSpecificationItem_ == null )
											{
											// A justification has been created, but the conclusion specification already exists.
											// So, the justification needs to be added separately
											if( hasCreatedJustification )
												{
												if( foundConclusionSpecificationItem_ != null )
													{
													if( !isPossessive &&
													relationContextNr > Constants.NO_CONTEXT_NR &&
													relationContextNr != foundConclusionSpecificationItem_.relationContextNr() )
														possessiveReversibleConclusionJustificationItem_ = specificationJustificationItem;
													else
														{
														// Add (attach) new created justification to the found specification
														if( generalizationWordItem.attachJustification( specificationJustificationItem, foundConclusionSpecificationItem_ ) != Constants.RESULT_OK )
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to attach a justification to a conclusion in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
														}
													}
												else
													// Check conclusion for existence before calling this method - rather than deleting the just created justification
													return myWord_.setErrorInItem( 1, moduleNameString_, "I have created a justification, but it isn't used. Please check if the conclusion already exists, before calling this method" );
												}
											}
										else
											{
											// Check conclusion for integrity
											if( generalizationWordItem.writeSelectedSpecification( true, true, false, false, Constants.NO_ANSWER_PARAMETER, createdConclusionSpecificationItem_ ) == Constants.RESULT_OK )
												{
												if( CommonVariables.writeSentenceStringBuffer != null &&
												CommonVariables.writeSentenceStringBuffer.length() > 0 )
													{
													if( ( createdRelationContextNr = createdConclusionSpecificationItem_.relationContextNr() ) > Constants.NO_CONTEXT_NR )
														{
														if( admin_.collectGeneralizationWordWithPreviousOne( isExclusive, createdConclusionSpecificationItem_.hasExclusiveGeneralizationCollection(), isPossessive, generalizationWordTypeNr, specificationWordTypeNr, questionParameter, createdRelationContextNr, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect a generalization word with a previous one" );
														}
													}
												else
													return myWord_.setErrorInItem( 1, moduleNameString_, "Integrity error! I couldn't write the created conclusion with generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and specification word \"" + specificationWordItem.anyWordTypeString() + "\". I guess, the implementation of my writing modules is insufficient to write this particular sentence structure" );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the created conclusion in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to check the writing integrity" );
											}

										if( CommonVariables.result == Constants.RESULT_OK &&
										myWord_.isNounWordType( specificationWordTypeNr ) &&

										( hasCreatedJustification ||
										createdConclusionSpecificationItem_ != null ) )
											{
											if( findSpecificationSubstitutionConclusionOrQuestion( false, isDeactive, isArchive, isExclusive, isNegative, isPossessive, false, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, specificationContextNr, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
											}
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a concluded specification" );
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find or create a specification justification" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a justification" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The definition specification item or specific specification item is a question or an assumption, which can't be a justification for a conclusion" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given justification type number is not a conclusion" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminConclusion( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		possessiveReversibleConclusionJustificationItem_ = null;
		createdConclusionSpecificationItem_ = null;
		foundConclusionSpecificationItem_ = null;
		foundSpecificationGeneralizationDefinitionSpecificationItem_ = null;
		lastCreatedSpecificationSubstitutionConclusionSpecificationItem_ = null;

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

	protected void initializeAdminConclusionVariables()
		{
		lastCreatedSpecificationSubstitutionConclusionSpecificationItem_ = null;
		}

	protected byte addSpecificationGeneralizationConclusion( short generalizationWordTypeNr, short specificationWordTypeNr, SpecificationItem definitionSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		if( generalizationWordItem != null )
			{
			if( !generalizationWordItem.hasCollection( specificationWordItem ) )
				{
				if( findSpecificationGeneralizationDefinition( generalizationWordTypeNr, generalizationWordItem, specificationWordItem ) == Constants.RESULT_OK )
					{
					if( foundSpecificationGeneralizationDefinitionSpecificationItem_ != null )
						{
						if( admin_.collectGeneralizationWordWithPreviousOne( false, false, false, generalizationWordTypeNr, specificationWordTypeNr, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, generalizationWordItem, specificationWordItem ) == Constants.RESULT_OK )
							{
							if( generalizationWordItem.hasCollection( specificationWordItem ) )
								{
								if( addSpecificationGeneralizationConclusion( foundSpecificationGeneralizationDefinitionSpecificationItem_ ) == Constants.RESULT_OK )
									{
									if( addSpecificationGeneralizationConclusion( definitionSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification generalization conclusion about the given sentence" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification generalization conclusion about an earlier sentence" );
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect a generalization word with a previous one" );
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification generalization" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}

	protected byte findPossessiveReversibleConclusion( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int specificationContextNr, int relationPronounContextNr, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		ContextResultType contextResult = new ContextResultType();
		boolean isAssignment;
		boolean foundSpecificDeactiveAssignment = false;
		boolean foundSpecificArchiveAssignment = false;
		int nContextRelations;
		int relationCollectionNr;
		int tempGeneralizationContextNr;
		int generalizationContextNr = Constants.NO_CONTEXT_NR;
		SpecificationItem assumptionSpecificationItem;
		SpecificationItem specificSpecificationItem;
		SpecificationItem tempSpecificationItem;
		SpecificationItem userSpecificationItem;

		if( relationContextNr > Constants.NO_CONTEXT_NR )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( relationWordItem != null )
						{
						if( myWord_.isNounWordType( specificationWordTypeNr ) &&
						admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) )
							{
							tempGeneralizationContextNr = generalizationWordItem.contextNrInWord( !isPossessive, generalizationWordTypeNr, specificationWordItem );

							if( tempGeneralizationContextNr > Constants.NO_COLLECTION_NR &&
							( nContextRelations = myWord_.nContextWords( !isPossessive, tempGeneralizationContextNr, specificationWordItem ) ) > 1 )
								{
								// Find correct generalization context, which will become the relation context
								if( ( contextResult = admin_.getRelationContextNr( isExclusive, isNegative, !isPossessive, false, Constants.NO_QUESTION_PARAMETER, relationPronounContextNr, specificationContextNr, nContextRelations, relationWordItem, specificationWordItem, generalizationWordItem, null ) ).result == Constants.RESULT_OK )
									generalizationContextNr = contextResult.contextNr;
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the relation context number" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								// Check if already exists
								if( generalizationContextNr == Constants.NO_CONTEXT_NR ||
								relationWordItem.firstActiveAssignment( false, !isPossessive, Constants.NO_QUESTION_PARAMETER, relationPronounContextNr, specificationContextNr, generalizationContextNr, specificationWordItem, null ) == null )
									{
									// Find the non-reversible specification
									specificSpecificationItem = generalizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, false, false, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

									if( ( tempSpecificationItem = generalizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, false, true, false, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) != null )
										{
										foundSpecificDeactiveAssignment = true;

										if( specificSpecificationItem == null )
											specificSpecificationItem = tempSpecificationItem;
										}

									if( ( tempSpecificationItem = generalizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, false, false, true, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) != null )
										{
										foundSpecificArchiveAssignment = true;

										if( specificSpecificationItem == null )
											specificSpecificationItem = tempSpecificationItem;
										}

									if( specificSpecificationItem != null )
										{
										// Check if user entered specification already exists
										userSpecificationItem = relationWordItem.firstUserSpecification( isNegative, !isPossessive, Constants.NO_QUESTION_PARAMETER, relationPronounContextNr, specificationContextNr, generalizationContextNr, specificationWordItem, null );

										if( userSpecificationItem == null ||
										userSpecificationItem.isDeactiveAssignment() != foundSpecificDeactiveAssignment ||		// Ambiguous when has different tense (time)
										userSpecificationItem.isArchiveAssignment() != foundSpecificArchiveAssignment )		// (active, deactive or archive)
											{
											isAssignment = ( ( ( isDeactive ||
															isArchive ) &&

															userSpecificationItem == null ) ||

															specificSpecificationItem.hasExclusiveGeneralizationCollection() );

											if( ( relationCollectionNr = relationWordItem.collectionNr( relationWordTypeNr, specificationWordItem ) ) > Constants.NO_COLLECTION_NR )
												{
												if( relationWordItem.isExclusiveCollection( relationCollectionNr ) )
													isExclusive = true;
												}

											if( addConclusion( isAssignment, isDeactive, isArchive, isExclusive, isNegative, !isPossessive, Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, relationWordTypeNr, Constants.WORD_TYPE_NOUN_SINGULAR, generalizationWordTypeNr, relationCollectionNr, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, relationPronounContextNr, specificationContextNr, generalizationContextNr, null, null, specificSpecificationItem, relationWordItem, specificationWordItem, generalizationWordItem ) == Constants.RESULT_OK )
												{
												if( contextResult.replaceContextNr > Constants.NO_CONTEXT_NR )
													{
													if( foundConclusionSpecificationItem_ != null )
														{
														if( possessiveReversibleConclusionJustificationItem_ == null )
															{
															if( relationWordItem.replaceOlderSpecifications( contextResult.replaceContextNr, foundConclusionSpecificationItem_.updatedSpecificationItem() ) != Constants.RESULT_OK )
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to replace older conclusion specifications in relation word \"" + relationWordItem.anyWordTypeString() + "\" to specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
															}
														else
															{
															if( ( assumptionSpecificationItem = relationWordItem.firstAssumptionSpecification( true, isNegative, !isPossessive, Constants.NO_QUESTION_PARAMETER, relationPronounContextNr, specificationContextNr, specificationWordItem ) ) != null )
																{
																if( relationWordItem.attachJustification( possessiveReversibleConclusionJustificationItem_, assumptionSpecificationItem ) == Constants.RESULT_OK )
																	{
																	if( relationWordItem.attachJustification( foundConclusionSpecificationItem_.specificationJustificationItem(), assumptionSpecificationItem.updatedSpecificationItem() ) == Constants.RESULT_OK )
																		{
																		if( relationWordItem.archiveOrDeleteSpecification( foundConclusionSpecificationItem_, assumptionSpecificationItem.updatedSpecificationItem() ) == Constants.RESULT_OK )
																			{
																			if( relationWordItem.recalculateAssumptionsInWord() != Constants.RESULT_OK )
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to recalculate the assumptions in relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
																			}
																		else
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to archive the found conclusion specification in relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
																		}
																	else
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to attach the justification specification of the found conclusion specification to the assumption conclusion specification in relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
																	}
																else
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to attach the possessive reversible conclusion justification to the assumption conclusion specification in relation word \"" + relationWordItem.anyWordTypeString() + "\"" );
																}
															else
																return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find an assumption specification" );
															}
														}
													else
														return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find an older conclusion" );
													}
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a possessive reversible conclusion from relation word \"" + relationWordItem.anyWordTypeString() + "\" to specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
											}
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the non-reversible specification" );
									}
								}
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given relation word item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given specfication word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given relation context number is undefined" );

		return CommonVariables.result;
		}

	protected byte findSpecificationSubstitutionConclusionOrQuestion( boolean isAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isUserSentence, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean includeAssignments;
		boolean isCorrectedAssumption;
		boolean skipCreatingQuestions = false;
		boolean isAssignment = ( isDeactive || isArchive );
		short newQuestionParameter;
		int specificationCollectionNr;
		SpecificationItem currentSpecificationItem;
		SpecificationItem definitionSpecificationItem;
		SpecificationItem foundRelatedSpecificationItem;
		SpecificationItem foundSpecificationItem;
		WordItem currentGeneralizationWordItem;
		WordItem currentSpecificationWordItem;

		if( myWord_.isNounWordType( specificationWordTypeNr ) )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) )
						{
						if( admin_.findGeneralizationAssumptionBySpecification( isDeactive, isArchive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a generalization assumption by specification word \"" + specificationWordItem.anyWordTypeString() + "\" in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
//					!CommonVariables.hasShownWarning &&		// Don't check to find deeper conflicts as well
					( currentSpecificationItem = specificationWordItem.firstSpecificationButNotAQuestion() ) != null )
						{
						do	{
							if( currentSpecificationItem.isSpecificationNoun() &&
							currentSpecificationItem.isNegative() == isNegative &&
							currentSpecificationItem.isPossessive() == isPossessive &&
							!currentSpecificationItem.isSpecificationGeneralization() )
								{
								if( ( currentSpecificationWordItem = currentSpecificationItem.specificationWordItem() ) != null )
									{
									specificationCollectionNr = currentSpecificationItem.specificationCollectionNr();

									foundSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, currentSpecificationWordItem, null );

									if( foundSpecificationItem == null ||

									( !currentSpecificationItem.isExclusive() &&

									( !foundSpecificationItem.isOlderSentence() ||

									( !isAssumption &&
									!isUserSentence &&
									foundSpecificationItem.isSelfGeneratedAssumption() ) ) ) )
										{
										newQuestionParameter = Constants.NO_QUESTION_PARAMETER;

										if( ( specificationResult = generalizationWordItem.findRelatedSpecification( false, false, false, isPossessive, questionParameter, specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), currentSpecificationItem.relationContextNr(), currentSpecificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
											{
											foundRelatedSpecificationItem = specificationResult.relatedSpecificationItem;

											if( foundRelatedSpecificationItem == null &&

											( currentSpecificationItem.hasSpecificationCollection() ||
											admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) ) )
												{
												if( ( currentGeneralizationWordItem = currentSpecificationItem.generalizationWordItem() ) != null )
													{
													isCorrectedAssumption = generalizationWordItem.isCorrectedAssumption();

													if( ( foundSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, currentGeneralizationWordItem, null ) ) != null &&

													( ( !isExclusive &&
													currentSpecificationItem.isExclusive() ) ||

													( isExclusive &&
													generalizationWordTypeNr == Constants.WORD_TYPE_PROPER_NAME ) ) )
														{
														// Question
														if( !skipCreatingQuestions &&
														foundSpecificationItem.hasSpecificationCollection() &&

														( ( isExclusive &&
														!currentSpecificationItem.isExclusive() ) ||

														( !isExclusive &&
														currentSpecificationItem.isExclusive() ) ) &&

														// Skip premature questions on possessive user sentences
														( isPossessive == admin_.isUserSentencePossessive() ||
														generalizationWordItem == admin_.userSentenceGeneralizationWordItem() ||
														currentSpecificationWordItem.isCompoundCollection( specificationCollectionNr, specificationWordItem ) ) )
															{
															if( ( specificationResult = generalizationWordItem.findRelatedSpecification( false, false, true, isPossessive, questionParameter, specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), generalizationContextNr, specificationContextNr, currentSpecificationItem.relationContextNr(), currentSpecificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
																{
																foundRelatedSpecificationItem = specificationResult.relatedSpecificationItem;

																if( isCorrectedAssumption ||
																foundRelatedSpecificationItem == null )
																	{
																	newQuestionParameter = Constants.WORD_PARAMETER_SINGULAR_VERB_IS;

																	if( findSpecificationSubstitutionQuestion( isCorrectedAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, newQuestionParameter, generalizationWordTypeNr, specificationWordTypeNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, currentSpecificationItem, foundSpecificationItem, generalizationWordItem, currentGeneralizationWordItem, specificationWordItem, currentSpecificationWordItem ) == Constants.RESULT_OK )
																		{
																		if( !isCorrectedAssumption &&
																		foundRelatedSpecificationItem != null )
																			{
																			if( generalizationWordItem.archiveOrDeleteSpecification( foundRelatedSpecificationItem, createdConclusionSpecificationItem_ ) != Constants.RESULT_OK )
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to archive a related specification in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
																			}
																		}
																	else
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution question in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" with specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
																	}
																}
															else
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" is related to the found specification" );
															}
														}
													else	// Assumption / conclusion
														{
														if( !isExclusive &&
														currentSpecificationItem.hasSpecificationCollection() &&
														admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) )
															{
															// Detect a conflict
															if( generalizationWordItem.checkForSpecificationConflict( false, isExclusive, isNegative, isPossessive, specificationWordTypeNr, specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), currentSpecificationItem.relationContextNr(), currentSpecificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) != Constants.RESULT_OK )
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for a specification conflict in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
															}

														if( CommonVariables.result == Constants.RESULT_OK &&
														!CommonVariables.hasShownWarning &&
														!isCorrectedAssumption )
															{
															if( isAssumption ||
															currentSpecificationItem.isSelfGeneratedAssumption() )
																{
																// Create an assumption
																if( admin_.addAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, currentSpecificationItem.generalizationCollectionNr(), specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), generalizationContextNr, specificationContextNr, foundSpecificationItem, currentSpecificationItem, generalizationWordItem, currentSpecificationWordItem, null ) != Constants.RESULT_OK )
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a suggestive question assumption about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" with specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
																}
															else
																{
																if( currentSpecificationItem.isSpecificationNoun() )
																	{
																	includeAssignments = ( generalizationContextNr > Constants.NO_CONTEXT_NR ||
																							specificationContextNr > Constants.NO_CONTEXT_NR );

																	if( ( definitionSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( includeAssignments, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, currentGeneralizationWordItem ) ) != null )
																		{
																		if( lastCreatedSpecificationSubstitutionConclusionSpecificationItem_ != null &&
																		lastCreatedSpecificationSubstitutionConclusionSpecificationItem_.generalizationWordItem() == generalizationWordItem )
																			specificationCollectionNr = lastCreatedSpecificationSubstitutionConclusionSpecificationItem_.specificationCollectionNr();

																		// Create a conclusion
																		if( addConclusion( isAssignment, isDeactive, isArchive, isExclusive, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION, Constants.NO_PREPOSITION_PARAMETER, questionParameter, generalizationWordTypeNr, currentSpecificationItem.specificationWordTypeNr(), Constants.WORD_TYPE_UNDEFINED, currentSpecificationItem.generalizationCollectionNr(), specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), Constants.NO_CONTEXT_NR, definitionSpecificationItem, null, currentSpecificationItem, generalizationWordItem, currentSpecificationWordItem, null ) == Constants.RESULT_OK )
																			lastCreatedSpecificationSubstitutionConclusionSpecificationItem_ = createdConclusionSpecificationItem_;
																		else
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification substitution conclusion about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" with specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
																		}
																	else
																		return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find a definition specification item of a conclusion or question" );
																	}
																}
															}
														}

													if( CommonVariables.result == Constants.RESULT_OK &&
													!CommonVariables.hasShownWarning &&
													currentSpecificationItem.hasSpecificationCollection() &&

													( !currentSpecificationItem.isExclusive() ||

													( isExclusive &&
													myWord_.isNounWordType( generalizationWordTypeNr ) ) ) )
														{
														if( admin_.findGeneralizationAssumptionByGeneralization( isDeactive, isArchive, isNegative, isPossessive, newQuestionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, currentSpecificationItem.relationContextNr(), generalizationWordItem, currentSpecificationItem.specificationWordItem() ) != Constants.RESULT_OK )
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a generalization assumption by generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and with specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\"" );
														}
													}
												else
													return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
												}
											else
												{
												if( !currentSpecificationItem.isExclusive() &&
												currentSpecificationItem.isUserSpecification() )
													{
													if( generalizationWordItem.checkForSpecificationConflict( false, isExclusive, isNegative, isPossessive, specificationWordTypeNr, specificationCollectionNr, currentSpecificationItem.relationCollectionNr(), currentSpecificationItem.generalizationContextNr(), currentSpecificationItem.specificationContextNr(), currentSpecificationItem.relationContextNr(), currentSpecificationWordItem, currentSpecificationItem.relationWordItem(), currentSpecificationItem.specificationString() ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for a specification conflict in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
													}
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" is related to the found specification" );
										}
									else
										skipCreatingQuestions = true;
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined specification word" );
								}
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						!CommonVariables.hasShownWarning &&
						( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemButNotAQuestion() ) != null );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given specfication word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word type is not a noun" );

		return CommonVariables.result;
		}

	protected SpecificationResultType findCompoundSpecificationSubstitutionConclusion( boolean isNegative, boolean isPossessive, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem foundSpecificationItem;
		SpecificationItem previousSpecificationItem = null;
		SpecificationItem lastCreatedSpecificationItem = null;
		WordItem currentGeneralizationWordItem;

		if( myWord_.isNounWordType( specificationWordTypeNr ) )
			{
			if( specificationWordItem != null )
				{
				do	{
					if( ( currentGeneralizationItem = specificationWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
						{
						previousSpecificationItem = lastCreatedSpecificationItem;

						do	{
							if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
								{
								if( ( foundSpecificationItem = currentGeneralizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) != null )
									{
									if( findSpecificationSubstitutionConclusionOrQuestion( foundSpecificationItem.isSelfGeneratedAssumption(), foundSpecificationItem.isDeactiveItem(), foundSpecificationItem.isArchiveItem(), foundSpecificationItem.isExclusive(), isNegative, isPossessive, false, Constants.NO_QUESTION_PARAMETER, foundSpecificationItem.generalizationWordTypeNr(), specificationWordTypeNr, generalizationContextNr, specificationContextNr, currentGeneralizationWordItem, foundSpecificationItem.specificationWordItem() ) == Constants.RESULT_OK )
										{
										if( !CommonVariables.hasShownWarning )
											{
											if( ( specificationResult = currentGeneralizationWordItem.findRelatedSpecification( false, foundSpecificationItem ) ).result == Constants.RESULT_OK )
												{
												if( ( foundSpecificationItem = specificationResult.relatedSpecificationItem ) != null )
													{
													if( findSpecificationSubstitutionConclusionOrQuestion( foundSpecificationItem.isSelfGeneratedAssumption(), foundSpecificationItem.isDeactiveItem(), foundSpecificationItem.isArchiveItem(), foundSpecificationItem.isExclusive(), isNegative, isPossessive, false, foundSpecificationItem.questionParameter(), foundSpecificationItem.generalizationWordTypeNr(), specificationWordTypeNr, generalizationContextNr, specificationContextNr, currentGeneralizationWordItem, foundSpecificationItem.specificationWordItem() ) == Constants.RESULT_OK )
														specificationResult.compoundGeneralizationWordItem = currentGeneralizationWordItem;
													else
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" and specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
													}
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a related specification in word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" is related to the found specification" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\" and specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
									}
								}
							else
								myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						!CommonVariables.hasShownWarning &&
						( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				!CommonVariables.hasShownWarning &&
				// Do until no more conclusions or questions are created
				previousSpecificationItem != null &&	// Skip when no generalizations are available
				previousSpecificationItem != lastCreatedSpecificationItem );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word type is not a noun" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}
	};

/*************************************************************************
 *
 *	"He forgives all my sins and heals all my diseases.
 *	He redeems me from death and crowns me with love and
 *	tender mercies. He fills my life with good things.
 *	My youth is renewed like the eagle's!" (Psalm 103:3-5)
 *
 *************************************************************************/
