/*
 *	Class:			WordQuestion
 *	Supports class:	WordItem
 *	Purpose:		To answer questions about this word
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

class WordQuestion
	{
	// Private constructible variables

	private boolean foundDeeperPositiveAnswer_;
	private boolean foundSpecificationGeneralizationAnswer_;
	private boolean isNegativeAnswer_;
	private boolean showAnsweredQuestion_;

	private SpecificationItem uncertainAboutAnswerRelationSpecificationItem_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte findAnswerToQuestion( SpecificationItem questionSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean hasRelationContext;
		boolean isAssignment;
		boolean isDeactiveAssignment;
		boolean isArchiveAssignment;
		boolean isExclusive;
		boolean isNegative;
		boolean isPossessive;
		boolean isNegativeAnswer = false;
		boolean isPositiveAnswer = false;
		boolean isUncertainAboutRelation = false;
		short questionAssumptionLevel;
		short answerAssumptionLevel;
		int specificationCollectionNr;
		int generalizationContextNr;
		int specificationContextNr;
		int relationContextNr;
		SpecificationItem answerSpecificationItem = null;
		WordItem specificationWordItem;

		if( questionSpecificationItem != null )
			{
			hasRelationContext = questionSpecificationItem.hasRelationContext();

			isAssignment = ( hasRelationContext ||
							questionSpecificationItem.isAssignment() );

			isDeactiveAssignment = questionSpecificationItem.isDeactiveAssignment();
			isArchiveAssignment = questionSpecificationItem.isArchiveAssignment();
			isExclusive = questionSpecificationItem.isExclusive();
			isNegative = questionSpecificationItem.isNegative();
			isPossessive = questionSpecificationItem.isPossessive();
			specificationCollectionNr = questionSpecificationItem.specificationCollectionNr();
			generalizationContextNr = questionSpecificationItem.generalizationContextNr();
			specificationContextNr = questionSpecificationItem.specificationContextNr();
			relationContextNr = questionSpecificationItem.relationContextNr();
			specificationWordItem = questionSpecificationItem.specificationWordItem();

			// Find correct answer
			if( ( answerSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, isDeactiveAssignment, isArchiveAssignment, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) == null )
				{
				// Find answer with different relation context
				if( ( answerSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, isDeactiveAssignment, isArchiveAssignment, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem ) ) == null )
					{
					// Find negative answer
					if( ( answerSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, isDeactiveAssignment, isArchiveAssignment, !isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) == null )
						{
						// Find possessive answer
						if( ( answerSpecificationItem = myWord_.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, isDeactiveAssignment, isArchiveAssignment, isNegative, !isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) ) != null )
							isNegativeAnswer = true;
						}
					else
						isNegativeAnswer = true;
					}
				else
					{
					// Find out if the relation context of the question lies within the relation context set of the answer
					if( hasRelationContext &&
					!answerSpecificationItem.hasRelationContext() )
						isUncertainAboutRelation = true;
					else
						isNegativeAnswer = true;
					}
				}
			else
				{
				if( ( !isExclusive &&
				answerSpecificationItem.isExclusive() ) ||

				answerSpecificationItem.specificationWordItem() != specificationWordItem )
					isNegativeAnswer = true;	// Has different specification word
				else
					{
					// Only confirm the answer with 'yes' if the answer is at least as reliable as the question
					if( !isExclusive &&

					// If a structure question is mapped to an assignment question, the answer is negative
					answerSpecificationItem.isRelatedQuestion( isExclusive, questionSpecificationItem.specificationCollectionNr(), relationContextNr ) )
						{
						if( ( specificationResult = questionSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
							{
							questionAssumptionLevel = specificationResult.assumptionLevel;

							if( ( specificationResult = answerSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
								{
								answerAssumptionLevel = specificationResult.assumptionLevel;

								if( questionAssumptionLevel >= answerAssumptionLevel )
									isPositiveAnswer = true;
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to get the answer assumption level" );
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to get the question assumption level" );
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK &&

				( ( isPositiveAnswer ||
				isNegativeAnswer ) &&

				!hasRelationContext &&
				answerSpecificationItem.isAssignment() &&
				answerSpecificationItem.hasRelationContext() ) )
					{
					if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_AMBIGUOUS_QUESTION_MISSING_RELATION ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification about ambiguity" );
					}
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			answerSpecificationItem != null &&
			answerSpecificationItem.isOlderSentence() )		// Ignore suggestive assumptions
				{
				if( writeAnswerToQuestion( isNegativeAnswer, isPositiveAnswer, isUncertainAboutRelation, questionSpecificationItem, answerSpecificationItem ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an answer to a question" );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	private byte findAlternativeAnswerToQuestion( SpecificationItem questionSpecificationItem )
		{
		boolean isAssignment;
		boolean isNegative;
		boolean isPossessive;
		short generalizationWordTypeNr;
		int generalizationCollectionNr;
		int specificationCollectionNr;
		int generalizationContextNr;
		int specificationContextNr;
		int relationContextNr;
		int tempSpecificationGeneralizationCollectionNr;
		int specificationGeneralizationCollectionNr = Constants.NO_COLLECTION_NR;
		SpecificationItem currentSpecificationItem;
		WordItem currentSpecificationWordItem;
		WordItem specificationWordItem;

		foundSpecificationGeneralizationAnswer_ = false;

		if( questionSpecificationItem != null )
			{
			if( ( currentSpecificationItem = myWord_.firstSelectedSpecification( questionSpecificationItem.isAssignment(), questionSpecificationItem.isDeactiveItem(), questionSpecificationItem.isArchiveItem(), false ) ) != null )
				{
				isAssignment = ( questionSpecificationItem.isAssignment() ||
								questionSpecificationItem.hasRelationContext() );

				isNegative = questionSpecificationItem.isNegative();
				isPossessive = questionSpecificationItem.isPossessive();
				generalizationWordTypeNr = questionSpecificationItem.generalizationWordTypeNr();
				generalizationCollectionNr = questionSpecificationItem.generalizationCollectionNr();
				specificationCollectionNr = questionSpecificationItem.specificationCollectionNr();
				generalizationContextNr = questionSpecificationItem.generalizationContextNr();
				specificationContextNr = questionSpecificationItem.specificationContextNr();
				relationContextNr = questionSpecificationItem.relationContextNr();
				specificationWordItem = questionSpecificationItem.specificationWordItem();

				do	{
					if( currentSpecificationItem.isSpecificationGeneralization() )
						{
						tempSpecificationGeneralizationCollectionNr = myWord_.collectionNr( generalizationWordTypeNr, specificationWordItem );

						if( specificationGeneralizationCollectionNr == Constants.NO_COLLECTION_NR )
							{
							foundSpecificationGeneralizationAnswer_ = true;
							specificationGeneralizationCollectionNr = tempSpecificationGeneralizationCollectionNr;
							}
						else
							{
							if( specificationGeneralizationCollectionNr != tempSpecificationGeneralizationCollectionNr )
								return myWord_.setErrorInWord( 1, moduleNameString_, "I found specification-generalization multiple collection numbers" );
							}
						}

					if( relationContextNr == Constants.NO_CONTEXT_NR ||
					uncertainAboutAnswerRelationSpecificationItem_ == null )
						{
						if( currentSpecificationItem.isRelatedSpecification( isNegative, isPossessive, generalizationCollectionNr, specificationCollectionNr, relationContextNr ) )
							{
							if( writeAnswerToQuestion( true, false, false, questionSpecificationItem, currentSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an answer to a question" );
							}
						else
							{
							if( questionSpecificationItem.isSpecificationGeneralization() &&
							( currentSpecificationWordItem = currentSpecificationItem.specificationWordItem() ) != null )
								{
								if( currentSpecificationWordItem.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, true, true, isNegative, isPossessive, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem ) != null )
									{
									foundDeeperPositiveAnswer_ = true;
									foundSpecificationGeneralizationAnswer_ = true;
									}
								}
							}
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentSpecificationItem = currentSpecificationItem.nextSelectedSpecificationItem( false ) ) != null );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	private byte findAlternativeAnswerToQuestionInOtherWords( boolean foundSpecificationGeneralizationAnswer, SpecificationItem questionSpecificationItem )
		{
		boolean isAssignment;
		boolean isDeactiveAssignment;
		boolean isArchiveAssignment;
		boolean isNegative;
		boolean isPossessive;
		short generalizationWordTypeNr;
		int generalizationContextNr;
		int specificationContextNr;
		int relationContextNr;
		SpecificationItem foundSpecificationItem;
		WordItem currentWordItem;
		WordItem specificationWordItem;

		if( questionSpecificationItem != null )
			{
			isAssignment = ( questionSpecificationItem.isAssignment() ||
							questionSpecificationItem.hasRelationContext() );

			isDeactiveAssignment = questionSpecificationItem.isDeactiveAssignment();
			isArchiveAssignment = questionSpecificationItem.isArchiveAssignment();
			isNegative = questionSpecificationItem.isNegative();
			isPossessive = questionSpecificationItem.isPossessive();
			generalizationWordTypeNr = questionSpecificationItem.generalizationWordTypeNr();
			generalizationContextNr = questionSpecificationItem.generalizationContextNr();
			specificationContextNr = questionSpecificationItem.specificationContextNr();
			relationContextNr = questionSpecificationItem.relationContextNr();
			specificationWordItem = questionSpecificationItem.specificationWordItem();

			// Do in all words for an alternative answer
			if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
				{
				do	{
					if( currentWordItem != myWord_ )
						{
						foundSpecificationItem = currentWordItem.firstAssignmentOrSpecificationButNotAQuestion( false, isAssignment, isDeactiveAssignment, isArchiveAssignment, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

						if( foundSpecificationItem != null &&

						( foundSpecificationGeneralizationAnswer ||
						foundSpecificationItem.isRelatedSpecification( isNegative, isPossessive, generalizationWordTypeNr ) ) )
							{
							if( currentWordItem.writeAnswerToQuestion( !foundDeeperPositiveAnswer_, foundDeeperPositiveAnswer_, questionSpecificationItem, foundSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an answer to a question" );
							}
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentWordItem = currentWordItem.nextWordItem() ) != null );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The first word item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	private byte markQuestionAsAnswered( SpecificationItem questionSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isPossessive;
		short questionParameter;
		int generalizationContextNr;
		int specificationContextNr;
		SpecificationItem answeredQuestion;
		SpecificationItem foundSpecificationItem;
		WordItem specificationWordItem;
		String specificationString;

		if( questionSpecificationItem != null )
			{
			if( !questionSpecificationItem.isAnsweredQuestion() )
				{
				if( !questionSpecificationItem.isDeletedItem() )
					{
					isPossessive = questionSpecificationItem.isPossessive();
					questionParameter = questionSpecificationItem.questionParameter();
					generalizationContextNr = questionSpecificationItem.generalizationContextNr();
					specificationContextNr = questionSpecificationItem.specificationContextNr();
					specificationWordItem = questionSpecificationItem.specificationWordItem();
					specificationString = questionSpecificationItem.specificationString();

					if( questionSpecificationItem.isAssignment() )
						{
						if( ( specificationResult = myWord_.createAssignment( true, questionSpecificationItem.isConcludedAssumption(), questionSpecificationItem.isDeactiveItem(), questionSpecificationItem.isArchiveItem(), questionSpecificationItem.isExclusive(), questionSpecificationItem.isNegative(), isPossessive, questionSpecificationItem.isValueSpecification(), questionSpecificationItem.assignmentLevel(), questionSpecificationItem.assumptionLevel(), questionSpecificationItem.prepositionParameter(), questionParameter, questionSpecificationItem.generalizationWordTypeNr(), questionSpecificationItem.specificationWordTypeNr(), questionSpecificationItem.generalizationCollectionNr(), questionSpecificationItem.specificationCollectionNr(), generalizationContextNr, specificationContextNr, questionSpecificationItem.relationContextNr(), questionSpecificationItem.originalSentenceNr(), questionSpecificationItem.activeSentenceNr(), questionSpecificationItem.deactiveSentenceNr(), questionSpecificationItem.archiveSentenceNr(), questionSpecificationItem.nContextRelations(), questionSpecificationItem.specificationJustificationItem(), specificationWordItem, questionSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
							{
							if( ( answeredQuestion = specificationResult.createdSpecificationItem ) != null )
								{
								if( myWord_.archiveOrDeleteSpecification( questionSpecificationItem, answeredQuestion ) == Constants.RESULT_OK )
									{
									if( ( foundSpecificationItem = myWord_.firstSpecification( false, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, specificationString ) ) != null )
										{
										if( markQuestionAsAnswered( foundSpecificationItem ) != Constants.RESULT_OK )
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark the specification of an assignment question as been answered" );
										}
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete an answered question assignment" );
								}
							else
								return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't create an answered question assignment item" );
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create an answered question assignment item" );
						}
					else
						{
						if( ( specificationResult = myWord_.createSpecification( true, questionSpecificationItem.isConditional(), questionSpecificationItem.isConcludedAssumption(), questionSpecificationItem.isDeactiveItem(), questionSpecificationItem.isArchiveItem(), questionSpecificationItem.isExclusive(), questionSpecificationItem.isNegative(), isPossessive, questionSpecificationItem.isSpecificationGeneralization(), questionSpecificationItem.isValueSpecification(), questionSpecificationItem.assumptionLevel(), questionSpecificationItem.prepositionParameter(), questionParameter, questionSpecificationItem.generalizationWordTypeNr(), questionSpecificationItem.specificationWordTypeNr(), questionSpecificationItem.generalizationCollectionNr(), questionSpecificationItem.specificationCollectionNr(), generalizationContextNr, specificationContextNr, questionSpecificationItem.relationContextNr(), questionSpecificationItem.originalSentenceNr(), questionSpecificationItem.nContextRelations(), questionSpecificationItem.specificationJustificationItem(), specificationWordItem, questionSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
							{
							if( ( answeredQuestion = specificationResult.createdSpecificationItem ) != null )
								{
								if( myWord_.archiveOrDeleteSpecification( questionSpecificationItem, answeredQuestion ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete an answered question specification" );
								}
							else
								return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't create an answered question specification item" );
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create an answered question specification item" );
						}

					// Don't archive or delete the context or justification of answered questions
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is deleted" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is already answered" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	private byte findAnswerToNewUserQuestion( SpecificationItem questionSpecificationItem )
		{
		isNegativeAnswer_ = false;
		foundDeeperPositiveAnswer_ = false;
		uncertainAboutAnswerRelationSpecificationItem_ = null;

		if( questionSpecificationItem != null )
			{
			if( questionSpecificationItem.isQuestion() )
				{
				if( !questionSpecificationItem.isDeletedItem() )
					{
					if( findAnswerToQuestion( questionSpecificationItem ) == Constants.RESULT_OK )
						{
						if( !CommonVariables.foundAnswerToQuestion )
							{
							// Check collections for an alternative current-tense assignment answer to the question
							if( findAlternativeAnswerToQuestion( questionSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find an alternative answer to a question" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&

						( isNegativeAnswer_ ||
						foundSpecificationGeneralizationAnswer_ ||
						uncertainAboutAnswerRelationSpecificationItem_ != null ) )
							{
							// Check other words for an alternative answer to the question
							if( findAlternativeAnswerToQuestionInOtherWords( foundSpecificationGeneralizationAnswer_, questionSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find an alternative answer to a question" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( CommonVariables.foundAnswerToQuestion &&
							uncertainAboutAnswerRelationSpecificationItem_ == null )
								{
								if( markQuestionAsAnswered( false, questionSpecificationItem ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark a question specification as been answered" );
								}
							else
								{
								if( myWord_.firstSpecificationButNotAQuestion() == null )
									{
									// I don't know anything at all about this word
									if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_QUESTION_I_DONT_KNOW_ANYTHING_ABOUT_WORD_START, questionSpecificationItem.activeGeneralizationWordTypeString(), Constants.INTERFACE_QUESTION_I_DONT_KNOW_ANYTHING_ABOUT_WORD_END ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an interface notification" );
									}
								else	// A specification exists, but it is not a question
									{
									if( uncertainAboutAnswerRelationSpecificationItem_ != null )
										{
										if( myWord_.writeSelectedSpecification( true, true, false, false, Constants.NO_ANSWER_PARAMETER, uncertainAboutAnswerRelationSpecificationItem_ ) == Constants.RESULT_OK )
											{
											if( CommonVariables.writeSentenceStringBuffer != null &&
											CommonVariables.writeSentenceStringBuffer.length() > 0 )
												{
												if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_LISTING_I_ONLY_KNOW ) == Constants.RESULT_OK )
													{
													if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
														myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an answer to a question" );
													}
												else
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a header" );
												}
											else
												return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the selected answer to a question" );
											}
										else
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected answer to a question" );
										}
									}
								}
							}
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find an answer to a question" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is deleted" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is not a question" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	private byte findPossibleQuestionAndMarkAsAnswered( boolean isAssignment, boolean isSelfGenerated, int compoundSpecificationCollectionNr, SpecificationItem answerSpecificationItem )
		{
		boolean isPossessive;
		int answerGeneralizationCollectionNr;
		int answerSpecificationCollectionNr;
		SpecificationItem currentQuestionSpecificationItem;
		WordItem answerSpecificationWordItem;

		if( answerSpecificationItem != null )
			{
			if( !answerSpecificationItem.isQuestion() )
				{
				if( ( currentQuestionSpecificationItem = myWord_.firstSelectedSpecification( isAssignment, false, false, true ) ) != null )
					{
					isPossessive = answerSpecificationItem.isPossessive();
					answerGeneralizationCollectionNr = answerSpecificationItem.generalizationCollectionNr();
					answerSpecificationCollectionNr = answerSpecificationItem.specificationCollectionNr();
					answerSpecificationWordItem = answerSpecificationItem.specificationWordItem();

					do	{
						if( currentQuestionSpecificationItem.isOlderSentence() &&	// To avoid deactivating questions created during the current sentence
						currentQuestionSpecificationItem.isRelatedSpecification( isPossessive, isSelfGenerated, answerGeneralizationCollectionNr, answerSpecificationCollectionNr, compoundSpecificationCollectionNr, answerSpecificationWordItem ) )
							{
							if( markQuestionAsAnswered( showAnsweredQuestion_, currentQuestionSpecificationItem ) == Constants.RESULT_OK )
								{
								if( currentQuestionSpecificationItem.isAssignment() )
									showAnsweredQuestion_ = false;		// Don't show the specification of this assignment

								currentQuestionSpecificationItem = myWord_.firstSelectedSpecification( isAssignment, false, false, true );
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark a related question as been answered" );
							}
						else
							currentQuestionSpecificationItem = currentQuestionSpecificationItem.nextSelectedSpecificationItem( false );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					currentQuestionSpecificationItem != null );
					}
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given answer specification item is a question" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given answer specification item is undefined" );

		return CommonVariables.result;
		}

	private SpecificationItem firstActiveNewUserQuestion()
		{
		SpecificationItem firstSpecificationItem;
		SpecificationItem firstNewUserQuestion = null;

		if( ( firstSpecificationItem = myWord_.firstActiveAssignment( true ) ) != null )
			firstNewUserQuestion = firstSpecificationItem.firstNewUserQuestion();

		if( firstNewUserQuestion == null &&
		( firstSpecificationItem = myWord_.firstActiveQuestionSpecification() ) != null )
			return firstSpecificationItem.firstNewUserQuestion();

		return firstNewUserQuestion;
		}


	// Constructor

	protected WordQuestion( WordItem myWord )
		{
		String errorString = null;

		foundDeeperPositiveAnswer_ = false;
		foundSpecificationGeneralizationAnswer_ = false;
		isNegativeAnswer_ = false;
		showAnsweredQuestion_ = false;

		uncertainAboutAnswerRelationSpecificationItem_ = null;

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

	protected byte findPossibleQuestionAndMarkAsAnswered( int compoundSpecificationCollectionNr, SpecificationItem answerSpecificationItem )
		{
		showAnsweredQuestion_ = true;

		if( findPossibleQuestionAndMarkAsAnswered( true, false, compoundSpecificationCollectionNr, answerSpecificationItem ) == Constants.RESULT_OK )
			{
			if( findPossibleQuestionAndMarkAsAnswered( false, false, compoundSpecificationCollectionNr, answerSpecificationItem ) == Constants.RESULT_OK )
				{
				if( findPossibleQuestionAndMarkAsAnswered( true, true, compoundSpecificationCollectionNr, answerSpecificationItem ) == Constants.RESULT_OK )
					{
					if( findPossibleQuestionAndMarkAsAnswered( false, true, compoundSpecificationCollectionNr, answerSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a posssible self-generated question in my specifications and mark it as been answered" );
					}
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a posssible self-generated question in my assignments and mark it as been answered" );
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a posssible user question in my specifications and mark it as been answered" );
			}
		else
			myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a posssible user question in my assignments and mark it as been answered" );

		return CommonVariables.result;
		}

	protected byte findAnswerToNewUserQuestion()
		{
		SpecificationItem questionSpecificationItem;

		if( ( questionSpecificationItem = firstActiveNewUserQuestion() ) != null )
			{
				do	{
					if( findAnswerToNewUserQuestion( questionSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find an answer to a question" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( questionSpecificationItem = ( questionSpecificationItem.isDeletedItem() ? firstActiveNewUserQuestion() : questionSpecificationItem.nextNewUserQuestion() ) ) != null );
			}

		return CommonVariables.result;
		}

	protected byte markQuestionAsAnswered( boolean showAnsweredQuestion, SpecificationItem questionSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem foundRelatedSpecificationItem = null;

		if( questionSpecificationItem != null )
			{
			if( questionSpecificationItem.isQuestion() )
				{
				if( showAnsweredQuestion )
					{
					if( myWord_.writeSpecification( false, false, false, questionSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the answered question" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					do	{
						if( ( specificationResult = myWord_.findRelatedSpecification( false, questionSpecificationItem ) ).result == Constants.RESULT_OK )
							{
							if( ( foundRelatedSpecificationItem = specificationResult.relatedSpecificationItem ) != null )
								{
								if( markQuestionAsAnswered( foundRelatedSpecificationItem ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark a related question as been answered" );
								}
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related answered question part" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					foundRelatedSpecificationItem != null );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				!questionSpecificationItem.isDeletedItem() )
					{
					if( markQuestionAsAnswered( questionSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark a question as been answered" );
					}
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is not a question" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeAnswerToQuestion( boolean isNegativeAnswer, boolean isPositiveAnswer, boolean isUncertainAboutRelation, SpecificationItem questionSpecificationItem, SpecificationItem answerSpecificationItem )
		{
		short answerParameter;

		if( questionSpecificationItem != null )
			{
			if( answerSpecificationItem != null )
				{
				if( isPositiveAnswer )
					{
					if( CommonVariables.currentGrammarLanguageWordItem != null )
						{
						if( myWord_.parseGrammarToWriteSentence( false, ( isPositiveAnswer ? Constants.WORD_PARAMETER_ANSWER_YES : Constants.NO_ANSWER_PARAMETER ), Constants.NO_GRAMMAR_LEVEL, CommonVariables.currentGrammarLanguageWordItem.startOfGrammar(), answerSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to parse the grammar for an answer" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The current language word item is undefined" );
					}
				else	// Neutral or negative answer
					{
					if( isUncertainAboutRelation )
						{
						if( uncertainAboutAnswerRelationSpecificationItem_ == null )
							uncertainAboutAnswerRelationSpecificationItem_ = answerSpecificationItem;
						else
							return myWord_.setErrorInWord( 1, moduleNameString_, "The uncertain about relation specification item is already assigned" );
						}
					else
						{
						answerParameter = ( isNegativeAnswer &&
											CommonVariables.isFirstAnswerToQuestion ? Constants.WORD_PARAMETER_ANSWER_NO : Constants.NO_ANSWER_PARAMETER );

						if( myWord_.writeSelectedSpecification( true, true, false, false, answerParameter, answerSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected answer to a question" );
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				!isUncertainAboutRelation &&
				CommonVariables.writeSentenceStringBuffer != null &&
				CommonVariables.writeSentenceStringBuffer.length() > 0 )
					{
					if( ( CommonVariables.hasShownMessage &&
					CommonVariables.isFirstAnswerToQuestion ) ||

					answerSpecificationItem.isSelfGeneratedAssumption() )
						{
						if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( answerSpecificationItem.isSelfGeneratedAssumption() ? Constants.INTERFACE_LISTING_I_AM_NOT_SURE_I_ASSUME : Constants.INTERFACE_LISTING_MY_ANSWER ) ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a listing header" );
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) == Constants.RESULT_OK )
							{
							CommonVariables.foundAnswerToQuestion = true;
							CommonVariables.isFirstAnswerToQuestion = false;

							if( isNegativeAnswer )
								isNegativeAnswer_ = true;
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write an answer to a question" );
						}
					}
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given answer specification item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given question specification item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType findQuestionToAdjustedByCompoundCollection( boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		WordItem currentCollectionWordItem;
		WordItem currentWordItem;

		if( specificationWordItem != null )
			{
			if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
				{
				do	{
					if( ( currentCollectionWordItem = currentWordItem.compoundCollectionWordItem( specificationCollectionNr, specificationWordItem ) ) != null )
						{
						if( ( specificationResult.adjustedQuestionSpecificationItem = myWord_.firstAssignmentOrSpecification( false, false, false, true, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, currentCollectionWordItem, null ) ) != null )
							{
							if( myWord_.archiveOrDeleteSpecification( specificationResult.adjustedQuestionSpecificationItem, null ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete a question part" );
							}
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentWordItem = currentWordItem.nextWordItem() ) != null );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The first word item is undefined" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}
	};

/*************************************************************************
 *
 *	"I will sing a new song to you, O God!
 *	I will sing your praises with a ten-stringed harp." (Psalm 144:9)
 *
 *************************************************************************/
