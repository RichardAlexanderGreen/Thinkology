/*
 *	Class:			WordSpecification
 *	Supports class:	WordItem
 *	Purpose:		To create specification structures
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

class WordSpecification
	{
	// Private constructible variables

	private boolean addSuggestiveQuestionAssumption_;
	private boolean isConfirmedAssumption_;
	private boolean isConfirmedAssignment_;
	private boolean isConfirmedExclusive_;
	private boolean isCorrectedAssumptionByKnowledge_;
	private boolean isCorrectedAssumptionByOppositeQuestion_;
	private boolean isNonExclusiveSpecification_;

	private int generalizationCollectionNr_;

	private SpecificationItem archiveAssignmentItem_;
	private SpecificationItem confirmedSpecificationItem_;
	private SpecificationItem confirmedArchiveSpecificationItem_;
	private SpecificationItem correctedArchiveSpecificationItem_;
	private SpecificationItem correctedSuggestiveQuestionAssumptionSpecificationItem_;
	private SpecificationItem lastShownConflictSpecificationItem_;
	private SpecificationItem sameSimilarRelatedArchiveSpecificationItem_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte addGeneralization( short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, short questionParameter, WordItem specificationWordItem, WordItem relationWordItem )
		{
		GeneralizationResultType generalizationResult = new GeneralizationResultType();

		if( specificationWordItem != null )
			{
			if( ( generalizationResult = specificationWordItem.findGeneralization( false, questionParameter, generalizationWordTypeNr, myWord_ ) ).result == Constants.RESULT_OK )
				{
				if( !generalizationResult.foundGeneralization )
					{
					if( specificationWordItem.createGeneralization( false, questionParameter, specificationWordTypeNr, generalizationWordTypeNr, myWord_ ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a generalization item" );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				relationWordItem != null )
					{
					if( ( generalizationResult = relationWordItem.findGeneralization( true, questionParameter, generalizationWordTypeNr, myWord_ ) ).result == Constants.RESULT_OK )
						{
						if( !generalizationResult.foundGeneralization )
							{
							if( relationWordItem.createGeneralization( true, questionParameter, relationWordTypeNr, generalizationWordTypeNr, myWord_ ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a relation generalization item" );
							}
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a relation generalization item" );
					}
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a generalization item" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte archiveOrDeleteSpecification( SpecificationItem archiveSpecificationItem, SpecificationItem createdSpecificationItem )
		{
		if( archiveSpecificationItem != null )
			{
			if( createdSpecificationItem != null )
				{
				if( archiveSpecificationItem.isAssignment() )
					{
					if( archiveAssignmentItem_ == null )
						archiveAssignmentItem_ = archiveSpecificationItem;
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The archive assignment item is already defined" );
					}
				else
					{
					if( myWord_.archiveOrDeleteSpecification( archiveSpecificationItem, createdSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a specification" );
					}
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given created specification item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given archive specification item is undefined" );

		return CommonVariables.result;
		}

	private byte checkUserQuestion( boolean isAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, SpecificationItem foundSpecificationItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isRelatedQuestion = false;
		boolean isSameQuestion = false;
		boolean isSimilarQuestion = false;
		boolean hasRelationContext = ( relationContextNr > Constants.NO_CONTEXT_NR );
		SpecificationItem tempSpecificationItem;

		isCorrectedAssumptionByOppositeQuestion_ = false;
		correctedSuggestiveQuestionAssumptionSpecificationItem_ = null;

		if( specificationWordItem != null )
			{
			CommonVariables.isUserQuestion = true;

			if( foundSpecificationItem != null &&
			relationContextNr == foundSpecificationItem.relationContextNr() )
				{
				if( foundSpecificationItem.isAnsweredQuestion() )
					CommonVariables.isQuestionAlreadyAnswered = true;
				else
					{
					if( hasRelationContext &&
					myWord_.isContextCurrentlyUpdatedInAllWords( isPossessive, relationContextNr, specificationWordItem ) )
						CommonVariables.isQuestionAlreadyAnswered = false;
					}
				}

			if( foundSpecificationItem == null ||

			( foundSpecificationItem.isOlderSentence() &&

			( foundSpecificationItem.isQuestion() ||
			specificationCollectionNr > Constants.NO_COLLECTION_NR ) ) )
				{
				if( foundSpecificationItem != null &&
				foundSpecificationItem.isQuestion() )
					addSuggestiveQuestionAssumption_ = true;
				else
					{
					if( ( specificationResult = findRelatedSpecification( false, false, true, isAssignment, isAssignment, isExclusive, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
						{
						if( ( foundSpecificationItem = specificationResult.relatedSpecificationItem ) != null )
							{
							if( !isExclusive &&
							specificationWordItem.isExclusiveCollection( specificationCollectionNr ) )
								{
								if( foundSpecificationItem.isOlderSentence() )
									{
									if( hasRelationContext )
										{
										if( correctedSuggestiveQuestionAssumptionSpecificationItem_ == null )
											{
											if( ( correctedSuggestiveQuestionAssumptionSpecificationItem_ = myWord_.firstAssignmentOrSpecification( true, false, true, true, true, foundSpecificationItem.isNegative(), foundSpecificationItem.isPossessive(), true, Constants.NO_QUESTION_PARAMETER, foundSpecificationItem.specificationCollectionNr(), foundSpecificationItem.generalizationContextNr(), foundSpecificationItem.specificationContextNr(), foundSpecificationItem.relationContextNr(), foundSpecificationItem.specificationWordItem(), foundSpecificationItem.specificationString() ) ) != null )
												{
												if( myWord_.writeSpecification( false, false, true, correctedSuggestiveQuestionAssumptionSpecificationItem_ ) == Constants.RESULT_OK )
													{
													isCorrectedAssumptionByOppositeQuestion_ = true;
													correctedArchiveSpecificationItem_ = correctedSuggestiveQuestionAssumptionSpecificationItem_;
													}
												else
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a conflicting specification" );
												}
											}
										else
											return myWord_.setErrorInWord( 1, moduleNameString_, "The corrected suggestive question assumption specification item already assigned" );
										}
									}
								else
									{
									if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_QUESTION_IN_CONFLICT_WITH_ITSELF ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface warning" );
									}
								}
							}
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related question" );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				foundSpecificationItem != null &&
				foundSpecificationItem.isOlderSentence() )
					{
					if( hasRelationContext )
						{
						tempSpecificationItem = myWord_.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

						if( tempSpecificationItem != null &&
						isExclusive == tempSpecificationItem.isExclusive() &&
						relationContextNr == tempSpecificationItem.relationContextNr() )
							foundSpecificationItem = tempSpecificationItem;
						}

					if( ( isAssignment == foundSpecificationItem.isAssignment() ||
					hasRelationContext != foundSpecificationItem.hasRelationContext() ) &&

					specificationWordItem == foundSpecificationItem.specificationWordItem() )
						{
						if( isExclusive == foundSpecificationItem.isExclusive() &&
						relationContextNr == foundSpecificationItem.relationContextNr() )
							isSameQuestion = true;
						else
							{
							if( foundSpecificationItem.hasRelationContext() &&
							relationContextNr != foundSpecificationItem.relationContextNr() &&

							( !hasRelationContext ||
							!foundSpecificationItem.isMatchingRelationContextNr( false, relationContextNr ) ) )
								isRelatedQuestion = true;
							}
						}
					else
						isSimilarQuestion = true;

					if( isExclusive ||
					isRelatedQuestion ||
					isSameQuestion ||
					isSimilarQuestion )
						{
						if( isExclusive &&
						!foundSpecificationItem.isExclusive() )
							sameSimilarRelatedArchiveSpecificationItem_ = foundSpecificationItem;

						if( isSameQuestion )
							{
							if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( foundSpecificationItem.isSelfGenerated() ? Constants.INTERFACE_QUESTION_I_HAD_THE_SAME_QUESTION_BEFORE : Constants.INTERFACE_QUESTION_YOU_HAD_THE_SAME_QUESTION_BEFORE ) ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification" );
							}
						else
							{
							if( myWord_.writeSelectedSpecification( true, foundSpecificationItem ) == Constants.RESULT_OK )
								{
								if( CommonVariables.writeSentenceStringBuffer != null &&
								CommonVariables.writeSentenceStringBuffer.length() > 0 )
									{
									if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( foundSpecificationItem.isSelfGenerated() ? ( isRelatedQuestion ? Constants.INTERFACE_QUESTION_I_HAD_A_RELATED_QUESTION_BEFORE : Constants.INTERFACE_QUESTION_I_HAD_A_SIMILAR_QUESTION_BEFORE ) : ( isRelatedQuestion ? Constants.INTERFACE_QUESTION_YOU_HAD_A_RELATED_QUESTION_BEFORE : Constants.INTERFACE_QUESTION_YOU_HAD_A_SIMILAR_QUESTION_BEFORE ) ) ) == Constants.RESULT_OK )
										{
										if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence about the same, a similar or a relation question" );
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification" );
									}
								else
									return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the found specification sentence" );
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the found specification sentence" );
							}
						}
					else
						{
						if( CommonVariables.lastShownMoreSpecificSpecificationItem != null &&
						CommonVariables.lastShownMoreSpecificSpecificationItem.originalSentenceNr() == foundSpecificationItem.originalSentenceNr() )
							{
							if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_QUESTION_IN_CONFLICT_WITH_ITSELF ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface warning" );
							}
						else
							{
							if( writeMoreSpecificSpecification( foundSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a more specific related question" );
							}
						}
					}
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte checkUserSpecification( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isValueSpecification, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, SpecificationItem foundSpecificationItem, WordItem specificationWordItem, String specificationString )
		{
		boolean hasRelationContext = ( relationContextNr > Constants.NO_CONTEXT_NR );

		if( foundSpecificationItem != null )
			{
			if( specificationWordItem != null )
				{
				if( foundSpecificationItem.isValueSpecification() == isValueSpecification )
					{
					if( !isAssignment &&
					!isExclusive &&
					foundSpecificationItem.isExclusive() &&
					foundSpecificationItem.isUserSpecification() )
						{
						if( writeMoreSpecificSpecification( foundSpecificationItem ) == Constants.RESULT_OK )
							isNonExclusiveSpecification_ = true;
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an notification about a more specific non-exclusive specification" );
						}
					else
						{
						if( !isSelection &&
						hasRelationContext &&
						foundSpecificationItem.isOlderSentence() &&
						!foundSpecificationItem.hasRelationContext() )
							{
							if( writeMoreSpecificSpecification( foundSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an notification about a more specific related specification" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( foundSpecificationItem.hasRelationContext() &&
							foundSpecificationItem.relationContextNr() != relationContextNr &&
							isDeactiveAssignment == foundSpecificationItem.isDeactiveAssignment() &&
							isArchiveAssignment == foundSpecificationItem.isArchiveAssignment() )
								{
								if( myWord_.firstUserSpecification( isNegative, isPossessive, questionParameter, ( isAssignment ? Constants.NO_CONTEXT_NR : generalizationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : specificationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : relationContextNr ), specificationWordItem, specificationString ) == null )
									{
									if( myWord_.writeSelectedSpecification( true, foundSpecificationItem ) == Constants.RESULT_OK )
										{
										if( CommonVariables.writeSentenceStringBuffer != null &&
										CommonVariables.writeSentenceStringBuffer.length() > 0 )
											{
											if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( foundSpecificationItem.isSelfGeneratedAssumption() ? ( hasRelationContext ? Constants.INTERFACE_LISTING_CONFIRMED_SOME_RELATION_WORDS_OF_MY_ASSUMPTION : Constants.INTERFACE_LISTING_CONFIRMED_SPECIFICATION_OF_MY_ASSUMPTION ) : ( hasRelationContext ? Constants.INTERFACE_LISTING_CONFIRMED_SOME_RELATION_WORDS_OF_MY_CONCLUSION : Constants.INTERFACE_LISTING_CONFIRMED_SPECIFICATION_OF_MY_CONCLUSION ) ) ) == Constants.RESULT_OK )
												{
												if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
													{
													if( !hasRelationContext )
														confirmedSpecificationItem_ = foundSpecificationItem;

													CommonVariables.isSpecificationConfirmedByUser = true;
													generalizationCollectionNr_ = foundSpecificationItem.generalizationCollectionNr();
													}
												else
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a confirmed sentence" );
												}
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a confirmation interface text" );
											}
										else
											return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the confirmed self-generated specification sentence" );
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the confirmed self-generated specification sentence" );
									}
								}
							else
								{
								if( hasRelationContext &&
								!foundSpecificationItem.isOlderSentence() &&
								foundSpecificationItem.isUserSpecification() &&
								foundSpecificationItem.relationContextNr() == relationContextNr )
									foundSpecificationItem = myWord_.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, true, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
								else
									foundSpecificationItem = myWord_.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

								if( foundSpecificationItem != null &&

								( foundSpecificationItem.isSelfGenerated() ||	// Replace a self-generated with a user-entered specification

								( !hasRelationContext &&
								foundSpecificationItem.hasRelationContext() &&
								myWord_.firstAssignmentOrSpecification( false, false, true, true, true, isNegative, isPossessive, true, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) != null ) ) )
									{
									if( myWord_.writeSelectedSpecification( true, foundSpecificationItem ) == Constants.RESULT_OK )
										{
										if( CommonVariables.writeSentenceStringBuffer != null &&
										CommonVariables.writeSentenceStringBuffer.length() > 0 )
											{
											if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( foundSpecificationItem.isSelfGeneratedAssumption() ? Constants.INTERFACE_LISTING_MY_ASSUMPTIONS_THAT_ARE_CONFIRMED : Constants.INTERFACE_LISTING_MY_CONCLUSIONS_THAT_ARE_CONFIRMED ) ) == Constants.RESULT_OK )
												{
												if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
													{
													if( foundSpecificationItem.isAssignment() )
														isConfirmedAssignment_ = true;

													if( foundSpecificationItem.isExclusive() )
														isConfirmedExclusive_ = true;

													if( foundSpecificationItem.isSelfGeneratedAssumption() )
														isConfirmedAssumption_ = true;

													CommonVariables.isSpecificationConfirmedByUser = true;
													confirmedArchiveSpecificationItem_ = foundSpecificationItem;
													}
												else
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence about the same, a similar or a relation question" );
												}
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification" );
											}
										else
											return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the found specification sentence" );
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the found specification sentence" );
									}
								}
							}
						}
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "Value specification conflict! Specification word \"" + specificationWordItem.anyWordTypeString() + "\" is already a value specification or it is already a normal specification and you want to make it a value specification" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given found specification item is undefined" );

		return CommonVariables.result;
		}

	private byte checkUserSpecificationOrQuestion( boolean isAssignment, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isValueSpecification, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		GeneralizationResultType generalizationResult = new GeneralizationResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean checkQuestion = false;
		boolean isQuestion = ( questionParameter > Constants.NO_QUESTION_PARAMETER );
		boolean hasRelationContext = ( relationContextNr > Constants.NO_CONTEXT_NR );
		SpecificationItem foundSpecificationItem;

		isCorrectedAssumptionByKnowledge_ = false;
		correctedSuggestiveQuestionAssumptionSpecificationItem_ = null;

		if( specificationWordItem != null )
			{
			// Check specification in opposite direction
			if( ( generalizationResult = myWord_.findGeneralization( false, questionParameter, generalizationWordTypeNr, specificationWordItem ) ).result == Constants.RESULT_OK )
				{
				if( generalizationResult.foundGeneralization )
					{
					if( checkForSpecificationConflict( false, isExclusive, isNegative, isPossessive, specificationWordTypeNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check for a specification looping conflict" );
					}
				else
					{
					// Check current assignment, specification or question (with relation)
					foundSpecificationItem = myWord_.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, ( isAssignment ? Constants.NO_CONTEXT_NR : generalizationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : specificationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : relationContextNr ), specificationWordItem, specificationString );

					if( isQuestion &&
					foundSpecificationItem == null )
						foundSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, false, false, false, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

					if( foundSpecificationItem == null )
						{
						// Check current assignment, specification or question (without this relation)
						if( isQuestion &&
						( foundSpecificationItem = myWord_.firstUserSpecification( isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, specificationString ) ) == null )
							foundSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, false, false, false, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem );

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( isQuestion )
								{
								checkQuestion = true;

								if( isAssignment &&
								hasRelationContext &&

								( foundSpecificationItem == null ||
								foundSpecificationItem.isSelfGenerated() ) )
									addSuggestiveQuestionAssumption_ = true;
								}
							else
								{
								if( !isExclusive &&										// Exclusive specifications are not conflicting
								!isPossessive &&
								specificationCollectionNr > Constants.NO_COLLECTION_NR &&			// Possessive specifications are not conflicting
								generalizationWordTypeNr == Constants.WORD_TYPE_PROPER_NAME &&	// Only report conflicts on proper-names
								foundSpecificationItem == null )
									{
									if( ( specificationResult = findRelatedSpecification( false, true, false, false, false, false, isPossessive, Constants.NO_QUESTION_PARAMETER, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
										{
										if( specificationResult.relatedSpecificationItem != null &&
										specificationResult.relatedSpecificationItem.isSelfGeneratedAssumption() )
											{
											// Assumption needs to be corrected
											if( correctedSuggestiveQuestionAssumptionSpecificationItem_ == null )
												{
												if( myWord_.writeSpecification( false, true, false, specificationResult.relatedSpecificationItem ) == Constants.RESULT_OK )
													{
													isCorrectedAssumptionByKnowledge_ = true;
													correctedArchiveSpecificationItem_ = specificationResult.relatedSpecificationItem;
													correctedSuggestiveQuestionAssumptionSpecificationItem_ = specificationResult.relatedSpecificationItem;
													}
												else
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a conflicting specification" );
												}
											else
												return myWord_.setErrorInWord( 1, moduleNameString_, "The corrected suggestive question assumption specification item already assigned" );
											}
										else
											{
											if( checkForSpecificationConflict( true, isExclusive, isNegative, isPossessive, specificationWordTypeNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) == Constants.RESULT_OK )
												{
												if( !CommonVariables.hasShownWarning &&
												isAssignment &&
												isExclusive &&
												!isNegative &&
												!hasRelationContext &&
												generalizationContextNr == Constants.NO_CONTEXT_NR &&
												specificationContextNr == Constants.NO_CONTEXT_NR )
													{
													if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_SENTENCE_MISSING_RELATION ) != Constants.RESULT_OK )
														myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification about ambiguity" );
													}
												}
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check for a specification conflict" );
											}
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification" );
									}
								}
							}
						}
					else
						{
						checkQuestion = true;

						if( !hasRelationContext &&
						isAssignment &&
						!isNegative &&
						generalizationContextNr == Constants.NO_CONTEXT_NR &&
						specificationContextNr == Constants.NO_CONTEXT_NR &&
						foundSpecificationItem.hasRelationContext() )
							{
							if( myWord_.writeSelectedSpecification( true, foundSpecificationItem ) == Constants.RESULT_OK )
								{
								if( CommonVariables.writeSentenceStringBuffer != null &&
								CommonVariables.writeSentenceStringBuffer.length() > 0 )
									{
									if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_MISSING_RELATION_I_ASSUME_YOU_MEAN ) == Constants.RESULT_OK )
										{
										if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
											CommonVariables.hasShownMessage = false;
										else
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence with an assumption about the relation" );
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification with an assumption about the relation" );
									}
								else
									return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write a sentence with an assumption about the relation" );
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence with an assumption about the relation" );
							}
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					!CommonVariables.hasShownWarning &&
					checkQuestion )
						{
						if( isQuestion )
							{
							if( checkUserQuestion( isAssignment, isExclusive, isNegative, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, foundSpecificationItem, specificationWordItem, relationWordItem, specificationString ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check the user question" );
							}
						else
							{
							if( checkUserSpecification( isAssignment, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isValueSpecification, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, foundSpecificationItem, specificationWordItem, specificationString ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check the user specification" );
							}
						}
					}
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find out if word \"" + specificationWordItem.anyWordTypeString() + "\" is one of my generalization words" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte writeMoreSpecificSpecification( SpecificationItem earlierSpecificationItem )
		{
		if( earlierSpecificationItem != null )
			{
			if( myWord_.writeSelectedSpecification( !earlierSpecificationItem.isExclusive(), earlierSpecificationItem ) == Constants.RESULT_OK )
				{
				if( CommonVariables.writeSentenceStringBuffer != null &&
				CommonVariables.writeSentenceStringBuffer.length() > 0 )
					{
					if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( earlierSpecificationItem.isQuestion() ? ( earlierSpecificationItem.isSelfGenerated() ? Constants.INTERFACE_LISTING_YOUR_QUESTION_IS_MORE_SPECIFIC_THAN_MY_QUESTION : Constants.INTERFACE_LISTING_THIS_QUESTION_IS_MORE_SPECIFIC_THAN_YOUR_QUESTION ) : ( earlierSpecificationItem.isSelfGenerated() ? ( earlierSpecificationItem.isSelfGeneratedAssumption() ? Constants.INTERFACE_LISTING_YOUR_INFO_IS_MORE_SPECIFIC_THAN_MY_ASSUMPTION : Constants.INTERFACE_LISTING_YOUR_INFO_IS_MORE_SPECIFIC_THAN_MY_CONCLUSION ) : Constants.INTERFACE_LISTING_THIS_INFO_IS_MORE_SPECIFIC_THAN_YOUR_EARLIER_INFO ) ) ) == Constants.RESULT_OK )
						{
						if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
							CommonVariables.lastShownMoreSpecificSpecificationItem = earlierSpecificationItem;
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the earlier sentence" );
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface listing text" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write a selected specification" );
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected specification" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given more specific specification item is undefined" );

		return CommonVariables.result;
		}

	private SpecificationResultType findRelatedSpecificationItem( boolean checkAllQuestions, boolean checkRelationContext, boolean ignoreExclusive, boolean includeAnsweredQuestions, boolean isAssignment, boolean isDeactive, boolean isExclusive, boolean isPossessive, short questionParameter, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isFirstRelatedSpecification = true;
		SpecificationItem currentSpecificationItem = ( checkAllQuestions ? myWord_.firstSelectedSpecification( isAssignment, isDeactive, false, true ) : myWord_.firstSelectedSpecification( includeAnsweredQuestions, isAssignment, isDeactive, false, questionParameter ) );

		if( specificationWordItem != null ||
		specificationString != null )
			{
			if( currentSpecificationItem != null &&

			( specificationCollectionNr > Constants.NO_COLLECTION_NR ||
			relationCollectionNr > Constants.NO_COLLECTION_NR ) )
				{
				do	{
					if( currentSpecificationItem.isRelatedSpecification( checkRelationContext, ignoreExclusive, isExclusive, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr ) )
						{
						// Found the given specification item
						if( currentSpecificationItem.relationContextNr() == relationContextNr &&
						currentSpecificationItem.specificationWordItem() == specificationWordItem &&

						( specificationString == null ||
						currentSpecificationItem.specificationString() == null ||
						currentSpecificationItem.specificationString().equals( specificationString ) ) )
							isFirstRelatedSpecification = false;
						else
							{
							if( relationWordItem == null ||		// If the specification has no relation, select the oldest one (the first of a series)
							specificationResult.relatedSpecificationItem == null )
								{
								specificationResult.isFirstRelatedSpecification = isFirstRelatedSpecification;
								specificationResult.relatedSpecificationItem = currentSpecificationItem;
								}
							}
						}
					}
				while( ( currentSpecificationItem = ( checkAllQuestions ? currentSpecificationItem.nextSelectedSpecificationItem( includeAnsweredQuestions ) : currentSpecificationItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions ) ) ) != null );
				}
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}


	// Constructor

	protected WordSpecification( WordItem myWord )
		{
		String errorString = null;

		addSuggestiveQuestionAssumption_ = false;
		isConfirmedAssumption_ = false;
		isConfirmedAssignment_ = false;
		isConfirmedExclusive_ = false;
		isCorrectedAssumptionByKnowledge_ = false;
		isCorrectedAssumptionByOppositeQuestion_ = false;
		isNonExclusiveSpecification_ = false;

		generalizationCollectionNr_ = Constants.NO_COLLECTION_NR;

		archiveAssignmentItem_ = null;
		confirmedSpecificationItem_ = null;
		confirmedArchiveSpecificationItem_ = null;
		correctedArchiveSpecificationItem_ = null;
		correctedSuggestiveQuestionAssumptionSpecificationItem_ = null;
		lastShownConflictSpecificationItem_ = null;
		sameSimilarRelatedArchiveSpecificationItem_ = null;

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

	protected void clearHasConfirmedAssumption()
		{
		isConfirmedAssumption_ = false;
		}

	protected void clearLastShownConflictSpecification()
		{
		lastShownConflictSpecificationItem_ = null;
		}

	protected boolean addSuggestiveQuestionAssumption()
		{
		return addSuggestiveQuestionAssumption_;
		}

	protected boolean isConfirmedAssumption()
		{
		return isConfirmedAssumption_;
		}

	protected boolean isCorrectedAssumption()
		{
		return ( correctedSuggestiveQuestionAssumptionSpecificationItem_ != null );
		}

	protected boolean isCorrectedAssumptionByKnowledge()
		{
		return isCorrectedAssumptionByKnowledge_;
		}

	protected boolean isCorrectedAssumptionByOppositeQuestion()
		{
		return isCorrectedAssumptionByOppositeQuestion_;
		}

	protected byte checkForSpecificationConflict( boolean skipCompoundRelatedConflict, boolean isExclusive, boolean isNegative, boolean isPossessive, short specificationWordTypeNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isInConflictWithItself = false;
		int compoundSpecificationCollectionNr;
		int nonCompoundSpecificationCollectionNr;
		SpecificationItem relatedSpecificationItem;
		SpecificationItem conflictingSpecificationItem = null;
		WordItem generalizationWordItem;

		if( specificationWordItem != null )
			{
			// Derive conflict
			if( ( specificationResult = findRelatedSpecification( false, true, false, false, false, false, isPossessive, Constants.NO_QUESTION_PARAMETER, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
				{
				relatedSpecificationItem = specificationResult.relatedSpecificationItem;

				if( relatedSpecificationItem == null ||
				relatedSpecificationItem.isNegative() != isNegative )
					{
					// Check for negative assignment or specification (with relation)
					if( ( conflictingSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, !isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) == null )
						{
						// Check for negative assignment or specification (without this relation)
						if( ( conflictingSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, !isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem ) ) == null )
							{
							if( ( conflictingSpecificationItem = specificationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, myWord_ ) ) == null )
								{
								if( ( compoundSpecificationCollectionNr = specificationWordItem.compoundCollectionNr( specificationWordTypeNr ) ) > Constants.NO_COLLECTION_NR )
									{
									// Check for already existing compound specification
									if( ( conflictingSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, compoundSpecificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, null ) ) == null )
										{
										if( !skipCompoundRelatedConflict )
											{
											// Check for related compound collection
											if( ( specificationResult = findRelatedSpecification( true, true, false, false, false, false, isPossessive, Constants.NO_QUESTION_PARAMETER, compoundSpecificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
												conflictingSpecificationItem = specificationResult.relatedSpecificationItem;
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification" );
											}

										if( CommonVariables.result == Constants.RESULT_OK &&
										conflictingSpecificationItem == null &&
										( nonCompoundSpecificationCollectionNr = myWord_.nonCompoundCollectionNrInAllWords( compoundSpecificationCollectionNr ) ) > Constants.NO_COLLECTION_NR )
											conflictingSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, nonCompoundSpecificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, null );
										}
									}
								}
							}
						}
					}
				else
					{
					if( !isNegative )
						conflictingSpecificationItem = specificationResult.relatedSpecificationItem;
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				conflictingSpecificationItem != null )
					{
					// Process conflict
					if( ( generalizationWordItem = conflictingSpecificationItem.generalizationWordItem() ) != null )
						{
						if( ( specificationCollectionNr == Constants.NO_COLLECTION_NR ||
						specificationWordItem.isExclusiveCollection( specificationCollectionNr ) ) &&

						conflictingSpecificationItem != lastShownConflictSpecificationItem_ )
							{
							if( generalizationWordItem.writeSelectedSpecification( false, conflictingSpecificationItem ) == Constants.RESULT_OK )
								{
								if( CommonVariables.writeSentenceStringBuffer != null &&
								CommonVariables.writeSentenceStringBuffer.length() > 0 )
									{
									if( !isExclusive &&
									specificationCollectionNr > Constants.NO_COLLECTION_NR &&
									!conflictingSpecificationItem.isOlderSentence() )
										isInConflictWithItself = true;

									if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_WARNING, ( isInConflictWithItself ? Constants.INTERFACE_SENTENCE_IN_CONFLICT_WITH_ITSELF : Constants.INTERFACE_LISTING_CONFLICT ) ) == Constants.RESULT_OK )
										{
										if( !isInConflictWithItself )
											{
											if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
												lastShownConflictSpecificationItem_ = conflictingSpecificationItem;
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the conflict sentence" );
											}
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface warning" );
									}
								else
									return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the conflicting specification sentence" );
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the conflicting specification sentence" );
							}
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The generalization word item of the conflicting specification item is undefined" );
					}
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	protected byte recalculateAssumptionsOfInvolvedWords()
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( myWord_.recalculateAssumptionsInWord() == Constants.RESULT_OK )
			{
			if( ( currentGeneralizationItem = myWord_.firstActiveGeneralizationItem() ) != null )
				{
				do	{
					if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
						{
						if( currentGeneralizationWordItem.recalculateAssumptionsInWord() != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to recalculate the assumptions in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined generalization word" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItem() ) != null );
				}
			}
		else
			myWord_.addErrorInWord( 1, moduleNameString_, "I failed to recalculate my assumptions" );

		return CommonVariables.result;
		}

	protected byte updateSpecificationsInJustificationOfInvolvedWords( SpecificationItem archiveSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( myWord_.updateSpecificationsInJustificationInWord( archiveSpecificationItem, replacingSpecificationItem ) == Constants.RESULT_OK )
			{
			if( ( currentGeneralizationItem = myWord_.firstActiveGeneralizationItem() ) != null )
				{
				do	{
					if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
						{
						if( currentGeneralizationWordItem.updateSpecificationsInJustificationInWord( archiveSpecificationItem, replacingSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to update the justification items in generalization word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined generalization word" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItem() ) != null );
				}
			}
		else
			myWord_.addErrorInWord( 1, moduleNameString_, "I failed to update my justification items" );

		return CommonVariables.result;
		}

	protected SpecificationResultType addSpecification( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isExclusiveGeneralizationCollection = false;
		boolean isQuestion = ( questionParameter > Constants.NO_QUESTION_PARAMETER );
		boolean isSelfGenerated = ( specificationJustificationItem != null );
		int compoundCollectionNr = Constants.NO_COLLECTION_NR;
		int nonCompoundCollectionNr = Constants.NO_COLLECTION_NR;
		SpecificationItem createdSpecificationItem;
		SpecificationItem foundSpecificationItem = null;
		SpecificationItem foundArchiveSpecificationItem = null;

		addSuggestiveQuestionAssumption_ = false;
		isConfirmedAssignment_ = false;
		isConfirmedExclusive_ = false;
		isNonExclusiveSpecification_ = false;

		generalizationCollectionNr_ = generalizationCollectionNr;

		archiveAssignmentItem_ = null;
		confirmedSpecificationItem_ = null;
		confirmedArchiveSpecificationItem_ = null;
		correctedArchiveSpecificationItem_ = null;
		sameSimilarRelatedArchiveSpecificationItem_ = null;

		if( !isExclusive &&
		!isQuestion &&
		myWord_.isNoun() &&
		specificationWordItem != null &&
		specificationWordItem.isNoun() &&
		!specificationWordItem.hasCollection( null ) )
			{
			if( myWord_.createCollectionByGeneralization( isExclusive, isAssignment, isQuestion, true, specificationWordTypeNr, myWord_, specificationWordItem ).result != Constants.RESULT_OK )
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a collection by generalization of specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
			}

		if( CommonVariables.result == Constants.RESULT_OK )
			{
			if( specificationWordItem == null )		// Specification string
				specificationCollectionNr = myWord_.collectionNr( specificationWordTypeNr, null );
			else
				{
				if( specificationCollectionNr == Constants.NO_COLLECTION_NR )
					{
					compoundCollectionNr = specificationWordItem.compoundCollectionNr( specificationWordTypeNr );
					nonCompoundCollectionNr = specificationWordItem.nonCompoundCollectionNr( specificationWordTypeNr );
					specificationCollectionNr = ( ( !isExclusive && nonCompoundCollectionNr > Constants.NO_COLLECTION_NR ) || compoundCollectionNr == Constants.NO_COLLECTION_NR ? nonCompoundCollectionNr : compoundCollectionNr );
					}
				}

			if( relationWordItem != null &&
			relationCollectionNr == Constants.NO_COLLECTION_NR )
				relationCollectionNr = relationWordItem.collectionNr( relationWordTypeNr, specificationWordItem );

			if( !isSelfGenerated &&
			specificationWordItem != null )
				{
				if( checkUserSpecificationOrQuestion( isAssignment, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isValueSpecification, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) == Constants.RESULT_OK )
					{
					if( !isAssignment &&
					isConfirmedAssignment_ )
						isAssignment = true;

					if( generalizationCollectionNr_ == Constants.NO_COLLECTION_NR )
						generalizationCollectionNr_ = myWord_.collectionNr( generalizationWordTypeNr, specificationWordItem, null );

					if( !isExclusive &&
					generalizationCollectionNr_ > Constants.NO_COLLECTION_NR &&
					myWord_.isExclusiveCollection( generalizationCollectionNr_ ) )
						isExclusiveGeneralizationCollection = true;
					}
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check a user specification or question with specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			!CommonVariables.hasShownWarning )
				{
				foundSpecificationItem = myWord_.firstAssignmentOrSpecification( false, false, false, false, false, ( !isAssignment && isNegative ), isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, ( isAssignment ? Constants.NO_CONTEXT_NR : generalizationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : specificationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : relationContextNr ), specificationWordItem, specificationString );

				if( foundSpecificationItem == null ||

				// Exceptions
				isNonExclusiveSpecification_ ||
				confirmedArchiveSpecificationItem_ != null ||
				correctedArchiveSpecificationItem_ != null ||
				sameSimilarRelatedArchiveSpecificationItem_ != null ||

				// Overwrite non-conditional specification by conditional one
				( isConditional &&
				!foundSpecificationItem.isConditional() ) ||

				// Overwrite non-exclusive specification by exclusive one
				( isExclusive &&
				!foundSpecificationItem.isExclusive() ) ||

				// Overwrite self-generated assignment by user assignment
				( isAssignment &&
				!isSelfGenerated &&
				foundSpecificationItem.isSelfGenerated() ) ||

				// Accept different relation context (e.g. ambiguous specification)
				( ( relationContextNr > Constants.NO_CONTEXT_NR &&
				foundSpecificationItem.hasRelationContext() &&
				foundSpecificationItem.relationContextNr() != relationContextNr &&

				( isAssignment ||
				foundSpecificationItem.relationWordItem() == relationWordItem ) ) &&

				( !isSelfGenerated ||
				!foundSpecificationItem.foundSpecificationJustification( specificationJustificationItem ) ) ) ||

				( isQuestion &&
				!isAssignment &&
				!isSelfGenerated &&
				foundSpecificationItem.isExclusive() ) )
					{
					if( foundSpecificationItem != null )
						{
						if( !isConditional &&
						foundSpecificationItem.isConditional() )
							isConditional = true;

						if( !isExclusive &&
						!isQuestion &&
						foundSpecificationItem.isExclusive() )
							isExclusive = true;

						if( !isQuestion &&
						!isConfirmedAssignment_ &&
						foundSpecificationItem.isUserSpecification() &&
						foundSpecificationItem != confirmedArchiveSpecificationItem_ &&
						foundSpecificationItem != correctedArchiveSpecificationItem_ &&
						sameSimilarRelatedArchiveSpecificationItem_ != foundSpecificationItem )
							foundArchiveSpecificationItem = foundSpecificationItem;
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( isExclusive )
							{
							if( isNonExclusiveSpecification_ )
								isExclusive = false;
							}
						else
							{
							if( isConfirmedExclusive_ ||
							isExclusiveGeneralizationCollection ||

							( isConditional &&

							( generalizationCollectionNr_ > Constants.NO_COLLECTION_NR ||
							specificationCollectionNr > Constants.NO_COLLECTION_NR ) ) )
								isExclusive = true;
							}

						if( ( specificationResult = createSpecification( false, isConditional, false, false, false, isExclusive, ( isAssignment ? false : isNegative ), isPossessive, isSpecificationGeneralization, isValueSpecification, Constants.NO_ASSUMPTION_LEVEL, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr_, specificationCollectionNr, ( isAssignment ? Constants.NO_CONTEXT_NR : generalizationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : specificationContextNr ), ( isAssignment ? Constants.NO_CONTEXT_NR : relationContextNr ), CommonVariables.currentSentenceNr, ( isAssignment ? 0 : nContextRelations ), specificationJustificationItem, specificationWordItem, specificationString ) ).result == Constants.RESULT_OK )
							{
							if( ( createdSpecificationItem = specificationResult.createdSpecificationItem ) != null )
								{
								if( confirmedArchiveSpecificationItem_ != null )
									{
									if( archiveOrDeleteSpecification( confirmedArchiveSpecificationItem_, createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a confirmed specification" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								correctedArchiveSpecificationItem_ != null )
									{
									if( archiveOrDeleteSpecification( correctedArchiveSpecificationItem_, createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a corrected specification" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								sameSimilarRelatedArchiveSpecificationItem_ != null )
									{
									if( archiveOrDeleteSpecification( sameSimilarRelatedArchiveSpecificationItem_, createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a same-similar-related specification" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								foundArchiveSpecificationItem != null )
									{
									if( archiveOrDeleteSpecification( foundArchiveSpecificationItem, createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a found specification" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								confirmedSpecificationItem_ != null )
									{
									if( myWord_.confirmSpecificationButNotRelation( confirmedSpecificationItem_, createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to confirm a specification, but not its relation(s)" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								!isNegative &&
								!isQuestion &&
								specificationWordItem != null &&
								correctedSuggestiveQuestionAssumptionSpecificationItem_ == null )
									{
									if( myWord_.findPossibleQuestionAndMarkAsAnswered( specificationWordItem.compoundCollectionNr( specificationWordTypeNr ), createdSpecificationItem ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a possible question and mark it as been answered" );
									}
								}
							else
								myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't create a specification item" );
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a specification item" );
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				specificationWordItem != null )
					{
					if( addGeneralization( generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, questionParameter, specificationWordItem, relationWordItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to add a generalization" );
					}
				}
			}

		specificationResult.archiveAssignmentItem = archiveAssignmentItem_;
		specificationResult.foundSpecificationItem = foundSpecificationItem;
		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType createSpecification( boolean isAnsweredQuestion, boolean isConditional, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSpecificationGeneralization, boolean isValueSpecification, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( !myWord_.iAmAdmin() )
			{
			if( specificationWordItem == null ||
			!specificationWordItem.isNounValue() )
				{
				if( myWord_.specificationList == null )
					{
					if( ( myWord_.specificationList = new SpecificationList( Constants.WORD_SPECIFICATION_LIST_SYMBOL, myWord_ ) ) != null )
						myWord_.wordList[Constants.WORD_SPECIFICATION_LIST] = myWord_.specificationList;
					else
						{
						specificationResult.result = myWord_.setErrorInWord( 1, moduleNameString_, "I failed to create a specification list" );
						return specificationResult;
						}
					}

				return myWord_.specificationList.createSpecificationItem( isAnsweredQuestion, isConditional, isConcludedAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isSpecificationGeneralization, isValueSpecification, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is a value word" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The admin does not have specifications" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType findRelatedSpecification( boolean checkRelationContext, SpecificationItem searchSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( searchSpecificationItem != null )
			return findRelatedSpecificationItem( false, checkRelationContext, false, searchSpecificationItem.isAnsweredQuestion(), searchSpecificationItem.isAssignment(), searchSpecificationItem.isDeactiveItem(), searchSpecificationItem.isExclusive(), searchSpecificationItem.isPossessive(), searchSpecificationItem.questionParameter(), searchSpecificationItem.specificationCollectionNr(), searchSpecificationItem.relationCollectionNr(), searchSpecificationItem.generalizationContextNr(), searchSpecificationItem.specificationContextNr(), searchSpecificationItem.relationContextNr(), searchSpecificationItem.specificationWordItem(), searchSpecificationItem.relationWordItem(), searchSpecificationItem.specificationString() );

		specificationResult.result = myWord_.setErrorInWord( 1, moduleNameString_, "The given search specification item is undefined" );
		return specificationResult;
		}

	protected SpecificationResultType findRelatedSpecification( boolean checkAllQuestions, boolean ignoreExclusive, boolean includeAnsweredQuestions, boolean includeAssignments, boolean includingDeactiveAssignments, boolean isExclusive, boolean isPossessive, short questionParameter, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		// Either active assignments or specifications
		if( ( specificationResult = findRelatedSpecificationItem( checkAllQuestions, false, ignoreExclusive, includeAnsweredQuestions, includeAssignments, false, isExclusive, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
			{
			if( includeAssignments )
				{
				if( includingDeactiveAssignments &&
				specificationResult.relatedSpecificationItem == null )
					{
					// Past-tense assignments
					if( ( specificationResult = findRelatedSpecificationItem( checkAllQuestions, false, ignoreExclusive, includeAnsweredQuestions, true, true, isExclusive, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related past-tense assigment" );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				specificationResult.relatedSpecificationItem == null )
					{
					// Specifications
					if( ( specificationResult = findRelatedSpecificationItem( checkAllQuestions, false, ignoreExclusive, includeAnsweredQuestions, false, false, isExclusive, isPossessive, questionParameter, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, specificationString ) ).result != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification as well" );
					}
				}
			}
		else
			myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationItem correctedSuggestiveQuestionAssumptionSpecificationItem()
		{
		return correctedSuggestiveQuestionAssumptionSpecificationItem_;
		}
	};

/*************************************************************************
 *
 *	"Yes, joyful are those who live like this!
 *	Joyful indeed are those whose God is the Lord." (Psalm 144:15)
 *
 *************************************************************************/
