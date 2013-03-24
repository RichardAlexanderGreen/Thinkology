/*
 *	Class:			WordAssignment
 *	Supports class:	WordItem
 *	Purpose:		To assign specifications
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

class WordAssignment
	{
	// Private constructible variables

	private WordItem myWord_;

	private String moduleNameString_;


	// Private methods

	private byte deactivateCurrentAssignments()
		{
		SpecificationItem activeAssignmentItem;

		while( CommonVariables.result == Constants.RESULT_OK &&
		( activeAssignmentItem = myWord_.firstActiveAssignmentButNotAQuestion() ) != null )
			{
			if( deactivateActiveAssignment( activeAssignmentItem ) != Constants.RESULT_OK )
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactive an active assignment item" );
			}

		return CommonVariables.result;
		}

	private byte deactivateOrArchiveCurrentGeneralizationAssignments( boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem foundActiveAssignmentItem = null;
		SpecificationItem foundDeactiveAssignmentItem = null;
		WordItem currentGeneralizationWordItem;

		if( specificationWordItem != null )
			{
			if( !specificationWordItem.isNounValue() )
				{
				if( ( currentGeneralizationItem = specificationWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
					{
					do	{
						if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
							{
							if( foundActiveAssignmentItem == null &&
							currentGeneralizationWordItem != myWord_ )		// Skip my word for activate assignments, because this is the one to be assigned
								foundActiveAssignmentItem = currentGeneralizationWordItem.firstAssignment( true, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, null );

							if( foundDeactiveAssignmentItem == null )		// Allow to find a deactive assignment in my word
								foundDeactiveAssignmentItem = currentGeneralizationWordItem.firstAssignment( false, true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, null );
							}
						else
							return myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined generalization word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&

					( foundActiveAssignmentItem == null ||
					foundDeactiveAssignmentItem == null ) &&

					( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				foundActiveAssignmentItem != null )		// Only archive a deactive assignment when an active assignment is being deactivated
					{
					if( foundActiveAssignmentItem.hasGeneralizationCollection() )
						{
						if( foundActiveAssignmentItem.generalizationWordItem() != null )
							{
							if( foundActiveAssignmentItem.generalizationWordItem().deactivateActiveAssignment( foundActiveAssignmentItem ) == Constants.RESULT_OK )
								{
								if( foundDeactiveAssignmentItem != null )
									{
									if( foundDeactiveAssignmentItem.hasGeneralizationCollection() )
										{
										if( foundDeactiveAssignmentItem.generalizationWordItem() != null )
											{
											if( foundDeactiveAssignmentItem.generalizationWordItem().archiveDeactiveAssignment( foundDeactiveAssignmentItem ) != Constants.RESULT_OK )
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive a deactive assignment" );
											}
										else
											return myWord_.setErrorInWord( 1, moduleNameString_, "I found a deactive assignment without generalization word item" );
										}
									else
										return myWord_.setErrorInWord( 1, moduleNameString_, "I found a deactive assignment without generalization collection number" );
									}
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate an active assignment" );
							}
						else
							return myWord_.setErrorInWord( 1, moduleNameString_, "I found an active assignment without generalization word item" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "I found an active assignment without generalization collection number" );
					}
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "Processing a noun value specification isn't implemented yet" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte deactivateAssignment( boolean isAmbiguousRelationContext, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short specificationWordTypeNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem foundAssignmentItem;
		SpecificationItem foundActiveAssignmentItem;

		if( ( foundAssignmentItem = myWord_.firstActiveAssignment( isAmbiguousRelationContext, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( relationContextNr == Constants.NO_CONTEXT_NR )
				{
				if( isExclusive ||
				specificationWordTypeNr == Constants.WORD_TYPE_NUMERAL )
					{
					if( ( specificationResult = myWord_.findRelatedSpecification( true, false, isExclusive, isPossessive, Constants.NO_QUESTION_PARAMETER, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, null, specificationString ) ).result == Constants.RESULT_OK )
						{
						if( specificationResult.relatedSpecificationItem != null &&
						specificationResult.relatedSpecificationItem.isAssignment() )	// Skip specification
							{
							if( deactivateActiveAssignment( specificationResult.relatedSpecificationItem ) != Constants.RESULT_OK )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate a related active assignment" );
							}
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related assignment" );
					}
				}
			else	// With relation context
				{
				if( ( foundActiveAssignmentItem = myWord_.firstAssignment( true, false, false, isNegative, isPossessive, isSelfGenerated, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString ) ) != null )
					{
					if( ( foundAssignmentItem = myWord_.firstAssignment( false, true, false, isNegative, isPossessive, isSelfGenerated, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString ) ) != null )
						{
						// First archive a deactive assignment
						if( archiveDeactiveAssignment( foundAssignmentItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive a deactive assignment" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					foundActiveAssignmentItem != null )
						{
						// Now deactivate the found active assignment
						if( deactivateActiveAssignment( foundActiveAssignmentItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate an active assignment" );
						}
					}
				}
			}
		else
			{
			if( isAmbiguousRelationContext ||
			foundAssignmentItem.isNegative() != isNegative )
				{
				if( deactivateActiveAssignment( foundAssignmentItem ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate a negative active assignment" );
				}
			}

		return CommonVariables.result;
		}

	private SpecificationResultType assignSpecification( boolean isAmbiguousRelationContext, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem foundAssignmentItem;

		if( questionParameter == Constants.NO_QUESTION_PARAMETER )
			{
			if( deactivateAssignment( isAmbiguousRelationContext, isExclusive, isNegative, isPossessive, isSelfGenerated, specificationWordTypeNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) != Constants.RESULT_OK )
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate an assignment" );
			}

		if( CommonVariables.result == Constants.RESULT_OK )
			{
			if( ( foundAssignmentItem = myWord_.firstAssignment( true, true, true, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
				{
				if( isSelfGenerated )	// Not found a self-generated assignment. Now try to find a confirmed assignment.
					foundAssignmentItem = myWord_.firstAssignment( true, true, true, isNegative, isPossessive, false, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
				}

			if( foundAssignmentItem == null ||

			( isDeactive != foundAssignmentItem.isDeactiveItem() ||	// Ambiguous when has different tense (time)
			isArchive != foundAssignmentItem.isArchiveItem() ) )		// (active, deactive or archive)
				{
				if( !isExclusive &&
				questionParameter == Constants.NO_QUESTION_PARAMETER &&
				generalizationCollectionNr > Constants.NO_COLLECTION_NR )
					isExclusive = true;

				if( ( specificationResult = createAssignment( false, false, isDeactive, isArchive, isExclusive, isNegative, isPossessive, false, CommonVariables.currentAssignmentLevel, Constants.NO_ASSUMPTION_LEVEL, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString ) ).result == Constants.RESULT_OK )
					{
					if( specificationResult.createdSpecificationItem != null )
						{
						if( foundAssignmentItem != null )
							{
							if( foundAssignmentItem.isActiveItem() )
								{
								if( deactivateActiveAssignment( foundAssignmentItem ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate an active ambiguous assignment" );
								}
							else
								{
								if( !foundAssignmentItem.hasCurrentDeactiveSentenceNr() )
									{
									if( myWord_.archiveOrDeleteSpecification( foundAssignmentItem, null ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete an ambiguous assignment" );
									}
								}
							}
						}
					else
						{
						if( specificationWordItem == null )
							myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't create an assignment item with specification string \"" + specificationString + "\"" );
						else
							myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't create an assignment item with specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
						}
					}
				else
					{
					if( specificationWordItem == null )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create an assignment with specification string \"" + specificationString + "\"" );
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create an assignment with specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
					}
				}
			}

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	private SpecificationResultType assignSpecificationByValue( short questionParameter, JustificationItem specificationJustificationItem, WordItem specificationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem currentAssignmentItem;

		if( specificationWordItem != null )
			{
			if( ( currentAssignmentItem = specificationWordItem.firstActiveAssignment( false, questionParameter ) ) != null )
				{
				do	{
					if( ( specificationResult = assignSpecification( false, currentAssignmentItem.isDeactiveItem(), currentAssignmentItem.isArchiveItem(), currentAssignmentItem.isExclusive(), currentAssignmentItem.isNegative(), currentAssignmentItem.isPossessive(), currentAssignmentItem.isSelfGenerated(), currentAssignmentItem.prepositionParameter(), currentAssignmentItem.questionParameter(), currentAssignmentItem.generalizationWordTypeNr(), currentAssignmentItem.specificationWordTypeNr(), currentAssignmentItem.generalizationCollectionNr(), currentAssignmentItem.specificationCollectionNr(), currentAssignmentItem.relationCollectionNr(), currentAssignmentItem.generalizationContextNr(), currentAssignmentItem.specificationContextNr(), currentAssignmentItem.relationContextNr(), Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, currentAssignmentItem.nContextRelations(), specificationJustificationItem, currentAssignmentItem.specificationWordItem(), currentAssignmentItem.specificationString() ) ).result != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to assign specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentAssignmentItem = currentAssignmentItem.nextSpecificationItemWithSameQuestionParameter( false ) ) != null );
				}
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}


		// Constructor

	protected WordAssignment( WordItem myWord )
		{
		String errorString = null;

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

	protected byte createNewAssignmentLevel()
		{
		if( !myWord_.iAmAdmin() )
			{
			if( myWord_.assignmentList != null )
				{
				if( myWord_.assignmentList.createNewAssignmentLevel() != Constants.RESULT_OK )
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create assignment level " + ( CommonVariables.currentAssignmentLevel + 1 ) );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The admin does not have assignments" );

		return CommonVariables.result;
		}

	protected byte deactivateActiveAssignment( SpecificationItem activeItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem replacingAssignmentItem;

		if( activeItem != null )
			{
			if( activeItem.isActiveAssignment() )
				{
				if( myWord_.assignmentList != null )
					{
					if( activeItem.hasDeactiveSentenceNr() )	// Has been deactive before
						{
						if( ( specificationResult = createAssignment( activeItem.isAnsweredQuestion(), activeItem.isConcludedAssumption(), true, false, activeItem.isExclusive(), activeItem.isNegative(), activeItem.isPossessive(), false, activeItem.assignmentLevel(), activeItem.assumptionLevel(), activeItem.prepositionParameter(), activeItem.questionParameter(), activeItem.generalizationWordTypeNr(), activeItem.specificationWordTypeNr(), activeItem.generalizationCollectionNr(), activeItem.specificationCollectionNr(), activeItem.generalizationContextNr(), activeItem.specificationContextNr(), activeItem.relationContextNr(), activeItem.originalSentenceNr(), activeItem.activeSentenceNr(), activeItem.deactiveSentenceNr(), activeItem.archiveSentenceNr(), activeItem.nContextRelations(), activeItem.specificationJustificationItem(), activeItem.specificationWordItem(), activeItem.specificationString() ) ).result == Constants.RESULT_OK )
							{
							if( ( replacingAssignmentItem = specificationResult.createdSpecificationItem ) != null )
								{
								if( myWord_.archiveOrDeleteSpecification( activeItem, replacingAssignmentItem ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive or delete the given active assignment" );
								}
							else
								return myWord_.setErrorInWord( 1, moduleNameString_, "The last created assignment item is undefined" );
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create an active assignment item" );
						}
					else
						{
						if( myWord_.assignmentList.deactivateActiveItem( activeItem ) == Constants.RESULT_OK )
							{
							if( CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )
								CommonVariables.isAssignmentChanged = true;
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate an active assignment" );
						}
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The assignment list isn't created yet" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given assignment is not active" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given active assignment is undefined" );

		return CommonVariables.result;
		}

	protected byte archiveDeactiveAssignment( SpecificationItem deactiveItem )
		{

		if( deactiveItem != null )
			{
			if( deactiveItem.isDeactiveAssignment() )
				{
				if( myWord_.assignmentList != null )
					{
					if( myWord_.assignmentList.archiveDeactiveItem( deactiveItem ) == Constants.RESULT_OK )
						{
						if( CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )
							CommonVariables.isAssignmentChanged = true;
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to archive a deactive assignment" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The assignment list isn't created yet" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given assignment is not deactive" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given deactive assignment is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType getAssignmentOrderNr( short collectionWordTypeNr )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem assignmentOrderItem;
		WordItem specificationWordItem;

		if( myWord_.numberOfActiveAssignments() <= 1 )
			{
			if( ( assignmentOrderItem = myWord_.firstActiveAssignmentButNotAQuestion() ) != null )
				{
				if( ( specificationWordItem = assignmentOrderItem.specificationWordItem() ) != null )
					specificationResult.assignmentOrderNr = specificationWordItem.collectionOrderNrByWordTypeNr( collectionWordTypeNr );
				else
					myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined assignment word at assignment level " + CommonVariables.currentAssignmentLevel );
				}
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "I have more than one assignments at assignment level " + CommonVariables.currentAssignmentLevel );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType getAssignmentWordParameter()
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem assignmentItem;
		WordItem specificationWordItem;

		if( myWord_.numberOfActiveAssignments() <= 1 )
			{
			if( ( assignmentItem = myWord_.firstActiveAssignmentButNotAQuestion() ) != null )
				{
				if( ( specificationWordItem = assignmentItem.specificationWordItem() ) != null )
					specificationResult.assignmentParameter = specificationWordItem.wordParameter();
				else
					myWord_.setErrorInWord( 1, moduleNameString_, "I found an undefined assignment word at assignment level " + CommonVariables.currentAssignmentLevel );
				}
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "I have more than one assignments at assignment level " + CommonVariables.currentAssignmentLevel );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType assignSpecification( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem foundSpecificationItem;

		if( isAssignedOrClear )
			{
			if( questionParameter == Constants.NO_QUESTION_PARAMETER )
				{
				if( deactivateCurrentAssignments() != Constants.RESULT_OK )
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate my current assignments" );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "A question can only be answered, not be cleared" );
			}
		else
			{
			// Find the specification of the assignment
			if( ( foundSpecificationItem = myWord_.firstAssignmentOrSpecification( true, false, false, false, false, false, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) != null )
				{
				if( foundSpecificationItem.hasExclusiveGeneralizationCollection() )
					{
					if( deactivateOrArchiveCurrentGeneralizationAssignments( isNegative, isPossessive, questionParameter, foundSpecificationItem.generalizationContextNr(), foundSpecificationItem.specificationContextNr(), relationContextNr, specificationWordItem ) != Constants.RESULT_OK )
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to deactivate or archive current generalization assignments" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( foundSpecificationItem.isValueSpecification() )
						{
						if( ( specificationResult = assignSpecificationByValue( questionParameter, specificationJustificationItem, specificationWordItem ) ).result != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to assign the value of a specification word" );
						}
					else
						{
						if( ( specificationResult = assignSpecification( isAmbiguousRelationContext, isDeactive, isArchive, foundSpecificationItem.isExclusive(), isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, foundSpecificationItem.generalizationWordTypeNr(), foundSpecificationItem.specificationWordTypeNr(), foundSpecificationItem.generalizationCollectionNr(), foundSpecificationItem.specificationCollectionNr(), foundSpecificationItem.relationCollectionNr(), generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString ) ).result != Constants.RESULT_OK )
							{
							if( specificationWordItem == null )
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to assign specification string \"" + specificationString + "\"" );
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to assign specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
							}
						}
					}
				}
			else
				{
				if( specificationWordItem == null )
					myWord_.setErrorInWord( 1, moduleNameString_, "String \"" + specificationString + "\" isn't one of my specifications" );
				else
					myWord_.setErrorInWord( 1, moduleNameString_, "Word \"" + specificationWordItem.anyWordTypeString() + "\" isn't one of my specifications" );
				}
			}

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType createAssignment( boolean isAnsweredQuestion, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short assignmentLevel, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( !myWord_.iAmAdmin() )
			{
			if( specificationWordItem == null ||
			!specificationWordItem.isNounValue() )
				{
				if( myWord_.assignmentList == null )
					{
					if( ( myWord_.assignmentList = new SpecificationList( Constants.WORD_ASSIGNMENT_LIST_SYMBOL, myWord_ ) ) != null )
						myWord_.wordList[Constants.WORD_ASSIGNMENT_LIST] = myWord_.assignmentList;
					else
						myWord_.setErrorInWord( 1, moduleNameString_, "I failed to create an assignment list" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					return myWord_.assignmentList.createAssignmentItem( isAnsweredQuestion, isConcludedAssumption, isDeactive, isArchive, isExclusive, isNegative, isPossessive, isValueSpecification, assignmentLevel, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The given specification word item is a value word" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The admin does not have assignments" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}
	};

/*************************************************************************
 *
 *	"The Lord has made the heavens his throne;
 *	from there he rules over everything." (Psalm 103:19)
 *
 *************************************************************************/
