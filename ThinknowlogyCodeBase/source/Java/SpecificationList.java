/*
 *	Class:			SpecificationList
 *	Parent class:	List
 *	Purpose:		To store specification items
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

class SpecificationList extends List
	{
	// Private assignment methods

	private byte createNewAssignmentLevel( boolean isDeactive, boolean isArchive )
		{
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		if( searchItem != null &&
		searchItem.assignmentLevel() > CommonVariables.currentAssignmentLevel )
			searchItem = searchItem.nextAssignmentItemWithCurrentLevel();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( createAssignmentItem( searchItem.isAnsweredQuestion(), searchItem.isConcludedAssumption(), isDeactive, isArchive, searchItem.isExclusive(), searchItem.isNegative(), searchItem.isPossessive(), searchItem.isValueSpecification(), (short)(searchItem.assignmentLevel() + 1 ), searchItem.assumptionLevel(), searchItem.prepositionParameter(), searchItem.questionParameter(), searchItem.generalizationWordTypeNr(), searchItem.specificationWordTypeNr(), searchItem.generalizationCollectionNr(), searchItem.specificationCollectionNr(), searchItem.generalizationContextNr(), searchItem.specificationContextNr(), searchItem.relationContextNr(), searchItem.originalSentenceNr(), searchItem.activeSentenceNr(), searchItem.deactiveSentenceNr(), searchItem.archiveSentenceNr(), searchItem.nContextRelations(), searchItem.specificationJustificationItem(), searchItem.specificationWordItem(), searchItem.specificationString() ).result == Constants.RESULT_OK )
				searchItem = searchItem.nextAssignmentItemWithCurrentLevel();
			else
				addError( 1, null, "I failed to create an assignment item" );
			}

		return CommonVariables.result;
		}

	private byte updateArchivedReplacingSpecificationItems( SpecificationItem updateSpecificationItem )
		{
		SpecificationItem searchItem = firstSpecificationItem( false, true );

		if( updateSpecificationItem != null )
			{
			while( searchItem != null )
				{
				// Update the specification items that are filtered by getAssignmentItem
				// variable: replacingSpecificationItem
				if( searchItem.replacingSpecificationItem == updateSpecificationItem &&
				updateSpecificationItem.replacingSpecificationItem != null )
					searchItem.replacingSpecificationItem = updateSpecificationItem.replacingSpecificationItem;

				searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given update specification item is undefined" );

		return CommonVariables.result;
		}

	private SpecificationItem _firstAssignmentItem( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentItem( false, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.isSelfGenerated() == isSelfGenerated &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&
			searchItem.hasRelationContext() &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
			}

		return null;
		}

	private SpecificationItem _firstAssignmentItem( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentItem( false, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( true, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
			}

		return null;
		}

	private SpecificationItem _firstAssignmentItem( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentItem( false, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.isSelfGenerated() == isSelfGenerated &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( true, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
			}

		return null;
		}


	// Private specification methods

	private byte changeOlderSpecificationItem( boolean isExclusiveGeneralization, int generalizationCollectionNr, int specificationCollectionNr, SpecificationItem olderSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem replacingSpecificationItem = null;

		if( olderSpecificationItem != null )
			{
			if( olderSpecificationItem.isOlderSentence() )
				{
				if( ( isExclusiveGeneralization ||
				!olderSpecificationItem.isExclusive() ) ||

				( generalizationCollectionNr > Constants.NO_COLLECTION_NR ||
				!olderSpecificationItem.hasGeneralizationCollection() ) ||

				( specificationCollectionNr > Constants.NO_COLLECTION_NR &&
				!olderSpecificationItem.hasSpecificationCollection() ) )
					{
					if( isAssignmentList() )
						{
						if( ( specificationResult = createAssignmentItem( olderSpecificationItem.isAnsweredQuestion(), olderSpecificationItem.isConcludedAssumption(), olderSpecificationItem.isDeactiveItem(), olderSpecificationItem.isArchiveItem(), ( isExclusiveGeneralization || olderSpecificationItem.isExclusive() ), olderSpecificationItem.isNegative(), olderSpecificationItem.isPossessive(), olderSpecificationItem.isValueSpecification(), olderSpecificationItem.assignmentLevel(), olderSpecificationItem.assumptionLevel(), olderSpecificationItem.prepositionParameter(), olderSpecificationItem.questionParameter(), olderSpecificationItem.generalizationWordTypeNr(), olderSpecificationItem.specificationWordTypeNr(), ( generalizationCollectionNr > Constants.NO_COLLECTION_NR ? generalizationCollectionNr : olderSpecificationItem.generalizationCollectionNr() ), ( specificationCollectionNr > Constants.NO_COLLECTION_NR ? specificationCollectionNr : olderSpecificationItem.specificationCollectionNr() ), olderSpecificationItem.generalizationContextNr(), olderSpecificationItem.specificationContextNr(), olderSpecificationItem.relationContextNr(), olderSpecificationItem.originalSentenceNr(), olderSpecificationItem.activeSentenceNr(), olderSpecificationItem.deactiveSentenceNr(), olderSpecificationItem.archiveSentenceNr(), olderSpecificationItem.nContextRelations(), olderSpecificationItem.specificationJustificationItem(), olderSpecificationItem.specificationWordItem(), olderSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
							{
							if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
								setError( 1, null, "I couldn't create an assignment" );
							}
						else
							addError( 1, null, "I failed to create an assignment" );
						}
					else
						{
						if( ( specificationResult = createSpecificationItem( olderSpecificationItem.isAnsweredQuestion(), olderSpecificationItem.isConditional(), olderSpecificationItem.isConcludedAssumption(), olderSpecificationItem.isDeactiveItem(), olderSpecificationItem.isArchiveItem(), ( isExclusiveGeneralization || olderSpecificationItem.isExclusive() ), olderSpecificationItem.isNegative(), olderSpecificationItem.isPossessive(), olderSpecificationItem.isSpecificationGeneralization(), olderSpecificationItem.isValueSpecification(), olderSpecificationItem.assumptionLevel(), olderSpecificationItem.prepositionParameter(), olderSpecificationItem.questionParameter(), olderSpecificationItem.generalizationWordTypeNr(), olderSpecificationItem.specificationWordTypeNr(), ( generalizationCollectionNr > Constants.NO_COLLECTION_NR ? generalizationCollectionNr : olderSpecificationItem.generalizationCollectionNr() ), ( specificationCollectionNr > Constants.NO_COLLECTION_NR ? specificationCollectionNr : olderSpecificationItem.specificationCollectionNr() ), olderSpecificationItem.generalizationContextNr(), olderSpecificationItem.specificationContextNr(), ( isExclusiveGeneralization ? Constants.NO_CONTEXT_NR : olderSpecificationItem.relationContextNr() ), olderSpecificationItem.originalSentenceNr(), olderSpecificationItem.nContextRelations(), olderSpecificationItem.specificationJustificationItem(), olderSpecificationItem.specificationWordItem(), olderSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
							{
							if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
								setError( 1, null, "I couldn't create a specification" );
							}
						else
							addError( 1, null, "I failed to create a specification" );
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( archiveOrDeleteSpecificationItem( olderSpecificationItem, replacingSpecificationItem ) != Constants.RESULT_OK )
							addError( 1, null, "I failed to archive or delete the given specification" );
						}
					}
				else
					return setError( 1, null, "I couldn't find any changing parameter" );
				}
			else
				return setError( 1, null, "The given older specification item isn't old" );
			}
		else
			return setError( 1, null, "The given older specification item is undefined" );

		return CommonVariables.result;
		}

	private SpecificationResultType _findSpecificationItem( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, WordItem relationContextWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( false, isDeactive, isArchive, questionParameter );

		if( relationContextWordItem != null )
			{
			while( searchItem != null &&
			specificationResult.foundSpecificationItem == null )
				{
				if( searchItem.isNegative() == isNegative &&
				searchItem.isPossessive() == isPossessive &&
				searchItem.hasRelationContext() &&
				searchItem.specificationWordItem() == specificationWordItem &&
				searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
				searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&
				relationContextWordItem.hasContextInWord( isPossessive, searchItem.relationContextNr(), specificationWordItem ) )
					specificationResult.foundSpecificationItem = searchItem;
				else
					searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
				}
			}
		else
			setError( 1, null, "The given relation context word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	private SpecificationItem firstActiveSpecificationItem()
		{
		return (SpecificationItem)firstActiveItem();
		}

	private SpecificationItem firstDeactiveSpecificationItem()
		{
		return (SpecificationItem)firstDeactiveItem();
		}

	private SpecificationItem firstArchiveSpecificationItem()
		{
		return (SpecificationItem)firstArchiveItem();
		}

	private SpecificationItem firstAssignmentItem( boolean isDeactive, boolean isArchive )
		{
		SpecificationItem firstAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( firstAssignmentItem != null )
			return firstAssignmentItem.getAssignmentItem( false, true );

		return null;
		}

	private SpecificationItem firstSpecificationItem( boolean isDeactive, boolean isArchive )
		{
		return ( isArchive ? firstArchiveSpecificationItem() : ( isDeactive ? firstDeactiveSpecificationItem() : firstActiveSpecificationItem() ) );
		}

	private SpecificationItem firstAssignmentOrSpecificationItem( boolean isDeactive, boolean isArchive )
		{
		return ( isAssignmentList() ? firstAssignmentItem( isDeactive, isArchive ) : firstActiveSpecificationItem() );
		}

	private SpecificationItem firstAssignmentOrSpecificationItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		return ( isAssignmentList() ? firstAssignmentItem( includeAnsweredQuestions, isDeactive, isArchive, isQuestion ) : firstActiveSpecificationItem( includeAnsweredQuestions, isQuestion ) );
		}

	private SpecificationItem firstAssignmentOrSpecificationItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, short questionParameter )
		{
		return ( isAssignmentList() ? firstAssignmentItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter ) : firstActiveSpecificationItem( includeAnsweredQuestions, questionParameter ) );
		}

	private SpecificationItem _firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&

			( searchItem.specificationWordItem() == specificationWordItem ||

			( !searchItem.isSpecificationGeneralization() &&
			specificationCollectionNr > Constants.NO_COLLECTION_NR &&
			searchItem.specificationCollectionNr() == specificationCollectionNr ) ) &&

			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( allowEmptyContextResult, relationContextNr ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return foundSpecificationItem;
		}

	private SpecificationItem _firstUserSpecificationItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isUserSpecification() &&
			searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( true, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	private SpecificationItem _firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( allowEmptyContextResult, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	private SpecificationItem _firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.specificationCollectionNr() == specificationCollectionNr &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( allowEmptyContextResult, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	private SpecificationItem _firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.isSelfGenerated() == isSelfGenerated &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( allowEmptyContextResult, relationContextNr ) &&

			( searchItem.specificationCollectionNr() == specificationCollectionNr ||

			// Also check for compound collection to avoid duplicates
			( specificationWordItem != null &&
			specificationWordItem.isCompoundCollection( specificationCollectionNr ) ) ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	private SpecificationItem _firstAssumptionSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isSelfGeneratedAssumption() &&
			searchItem.hasRelationContext() &&
			searchItem.isNegative() == isNegative &&
			searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	private SpecificationItem nextSpecificationListItem()
		{
		return (SpecificationItem)nextListItem();
		}


	// Constructor

	protected SpecificationList( char _listChar, WordItem myWord )
		{
		initializeListVariables( _listChar, myWord );
		}


	// Protected assignment methods

	protected int numberOfActiveAssignments()
		{
		int nItems = 0;
		SpecificationItem searchItem = firstAssignmentItem( false, false, false, false );

		while( searchItem != null )
			{
			nItems++;
			searchItem = searchItem.nextAssignmentItemButNotAQuestion();
			}

		return nItems;
		}

	protected byte createNewAssignmentLevel()
		{
		if( CommonVariables.currentAssignmentLevel < Constants.MAX_LEVEL )
			{
			if( createNewAssignmentLevel( false, false ) == Constants.RESULT_OK )
				{
				if( createNewAssignmentLevel( true, false ) == Constants.RESULT_OK )
					{
					if( createNewAssignmentLevel( false, true ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to create an archive assignment level" );
					}
				else
					addError( 1, null, "I failed to create a deactive assignment level" );
				}
			else
				addError( 1, null, "I failed to create an active assignment level" );
			}
		else
			return setSystemError( 1, null, "Assignment level overflow" );

		return CommonVariables.result;
		}

	protected byte deleteAssignmentLevelInList()
		{
		SpecificationItem searchItem = firstActiveSpecificationItem();

		if( CommonVariables.currentAssignmentLevel > Constants.NO_ASSIGNMENT_LEVEL )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null &&
			searchItem.assignmentLevel() >= CommonVariables.currentAssignmentLevel )
				{
				if( searchItem.assignmentLevel() == CommonVariables.currentAssignmentLevel )
					{
					if( deleteActiveItem( false, searchItem ) == Constants.RESULT_OK )
//						{
//						CommonVariables.isAssignmentChanged = true;		// Don't indicate change on higher assignment level
						searchItem = nextSpecificationListItem();
//						}
					else
						addError( 1, null, "I failed to delete an active item" );
					}
				else
					searchItem = searchItem.nextSpecificationItem();
				}

			searchItem = firstDeactiveSpecificationItem();

			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null &&
			searchItem.assignmentLevel() >= CommonVariables.currentAssignmentLevel )
				{
				if( searchItem.assignmentLevel() == CommonVariables.currentAssignmentLevel )
					{
					if( deleteDeactiveItem( false, searchItem ) == Constants.RESULT_OK )
//						{
//						CommonVariables.isAssignmentChanged = true;		// Don't indicate change on higher assignment level
						searchItem = nextSpecificationListItem();
//						}
					else
						addError( 1, null, "I failed to delete a deactive item" );
					}
				else
					searchItem = searchItem.nextSpecificationItem();
				}

			searchItem = firstArchiveSpecificationItem();

			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null &&
			searchItem.assignmentLevel() >= CommonVariables.currentAssignmentLevel )
				{
				if( searchItem.assignmentLevel() == CommonVariables.currentAssignmentLevel )
					{
					if( deleteArchiveItem( false, searchItem ) == Constants.RESULT_OK )
//						{
//						CommonVariables.isAssignmentChanged = true;		// Don't indicate change on higher assignment level
						searchItem = nextSpecificationListItem();
//						}
					else
						addError( 1, null, "I failed to delete an archive item" );
					}
				else
					searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The current assignment level is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType findAssignmentItemByRelationContext( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isPossessive, short questionParameter, WordItem relationContextWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem searchItem = firstAssignmentItem( includeAnsweredQuestions, isDeactive, isArchive, questionParameter );

		if( relationContextWordItem != null )
			{
			while( searchItem != null &&
			specificationResult.foundSpecificationItem == null )
				{
				if( searchItem.isPossessive() == isPossessive &&
				relationContextWordItem.hasContextInWord( isPossessive, searchItem.relationContextNr(), searchItem.specificationWordItem() ) )
					specificationResult.foundSpecificationItem = searchItem;
				else
					searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
				}
			}
		else
			setError( 1, null, "The given relation context word is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType createAssignmentItem( boolean isAnsweredQuestion, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short assignmentLevel, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( isAssignmentList() )
			{
			if( generalizationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
			generalizationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
				{
				if( specificationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
				specificationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
					{
					if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
						{
						if( specificationJustificationItem == null ||
						specificationJustificationItem.isActiveItem() ||

						( isArchive &&
						specificationJustificationItem.isArchiveItem() ) )
							{
							if( ( specificationResult.createdSpecificationItem = new SpecificationItem( isAnsweredQuestion, false, isConcludedAssumption, isExclusive, isNegative, isPossessive, false, isValueSpecification, assignmentLevel, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString, this, myWord() ) ) != null )
								{
								if( isArchive )
									{
									specificationResult.createdSpecificationItem.setArchiveStatus();

									if( addItemToArchiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
										addError( 1, null, "I failed to add an archive assignment item" );
									}
								else
									{
									if( isDeactive )
										{
										specificationResult.createdSpecificationItem.setDeactiveStatus();

										if( addItemToDeactiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
											addError( 1, null, "I failed to add a deactive assignment item" );
										}
									else
										{
										if( addItemToActiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
											addError( 1, null, "I failed to add an active assignment item" );
										}
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								assignmentLevel == Constants.NO_ASSIGNMENT_LEVEL &&
								originalSentenceNr == CommonVariables.currentSentenceNr )
									CommonVariables.isAssignmentChanged = true;
								}
							else
								setError( 1, null, "I failed to create an assignment item" );
							}
						else
							setError( 1, null, "The given specification justification item isn't active" );
						}
					else
						setError( 1, null, "The current item number is undefined" );
					}
				else
					setError( 1, null, "The given specification word type number is undefined or out of bounds" );
				}
			else
				setError( 1, null, "The given generalization word type number is undefined or out of bounds" );
			}
		else
			setError( 1, null, "I am not an assignment list" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationItem firstNumeralAssignmentItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		SpecificationItem currentAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( currentAssignmentItem != null &&
		// This is the first assignment. Now get the first valid assignment.
		( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, true, isQuestion ) ) != null )
			{
			do	{
				if( currentAssignmentItem.isSpecificationNumeral() )
					return currentAssignmentItem;
				}
			while( ( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, false, isQuestion ) ) != null );
			}

		return null;
		}

	protected SpecificationItem firstStringAssignmentItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		SpecificationItem currentAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( currentAssignmentItem != null &&
		// This is the first assignment. Now get the first valid assignment.
		( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, true, isQuestion ) ) != null )
			{
			do	{
				if( currentAssignmentItem.specificationString() != null )
					return currentAssignmentItem;
				}
			while( ( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, false, isQuestion ) ) != null );
			}

		return null;
		}

	protected SpecificationItem firstAssignmentItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		SpecificationItem firstAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( firstAssignmentItem != null )
		// This is the first assignment. Now get the first valid assignment.
			return firstAssignmentItem.getAssignmentItem( includeAnsweredQuestions, true, isQuestion );

		return null;
		}

	protected SpecificationItem lastAssignmentItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, boolean isQuestion )
		{
		SpecificationItem lastAssignmentItem = null;
		SpecificationItem currentAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( currentAssignmentItem != null &&
		// This is the first assignment. Now get the first valid assignment.
		( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, true, isQuestion ) ) != null )
			{
			do	lastAssignmentItem = currentAssignmentItem;
			while( ( currentAssignmentItem = currentAssignmentItem.getAssignmentItem( includeAnsweredQuestions, false, isQuestion ) ) != null );
			}

		return lastAssignmentItem;
		}

	protected SpecificationItem firstAssignmentItem( boolean includeAnsweredQuestions, boolean isDeactive, boolean isArchive, short questionParameter )
		{
		SpecificationItem firstAssignmentItem = firstSpecificationItem( isDeactive, isArchive );

		if( firstAssignmentItem != null )
			return firstAssignmentItem.getAssignmentItem( includeAnsweredQuestions, true, questionParameter );

		return null;
		}

	protected SpecificationItem firstAssignmentItem( boolean isDifferentRelationContext, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundAssignmentItem;

		if( ( foundAssignmentItem = firstAssignmentItem( false, false, isDifferentRelationContext, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( ( foundAssignmentItem = firstAssignmentItem( false, true, isDifferentRelationContext, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
				foundAssignmentItem = firstAssignmentItem( true, false, isDifferentRelationContext, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
			}

		return foundAssignmentItem;
		}

	protected SpecificationItem firstAssignmentItem( boolean isDeactive, boolean isArchive, boolean isDifferentRelationContext, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstAssignmentItem( false, isDeactive, isArchive, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( true, specificationContextNr ) &&

			( ( !isDifferentRelationContext &&
			searchItem.isMatchingRelationContextNr( true, relationContextNr ) ) ||

			( isDifferentRelationContext &&
			searchItem.hasRelationContext() &&
			searchItem.relationContextNr() != relationContextNr ) ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
			}

		return null;
		}

	protected SpecificationItem firstAssignmentItem( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundAssignmentItem = null;

		if( includeActiveItems )
			foundAssignmentItem = _firstAssignmentItem( false, false, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString );

		if( includeDeactiveItems &&
		foundAssignmentItem == null )
			foundAssignmentItem = _firstAssignmentItem( true, false, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString );

		if( includeArchiveItems &&
		foundAssignmentItem == null )
			return _firstAssignmentItem( false, true, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, specificationString );

		return foundAssignmentItem;
		}

	protected SpecificationItem firstAssignmentItem( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundAssignmentItem = null;

		if( includeActiveItems )
			foundAssignmentItem = _firstAssignmentItem( false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( includeDeactiveItems &&
		foundAssignmentItem == null )
			foundAssignmentItem = _firstAssignmentItem( true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( includeArchiveItems &&
		foundAssignmentItem == null )
			return _firstAssignmentItem( false, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundAssignmentItem;
		}

	protected SpecificationItem firstAssignmentItem( boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundAssignmentItem = null;

		if( includeActiveItems )
			foundAssignmentItem = _firstAssignmentItem( false, false, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( includeDeactiveItems &&
		foundAssignmentItem == null )
			foundAssignmentItem = _firstAssignmentItem( true, false, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		if( includeArchiveItems &&
		foundAssignmentItem == null )
			return _firstAssignmentItem( false, true, isNegative, isPossessive, isSelfGenerated, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

		return foundAssignmentItem;
		}


	// Protected specification methods

	protected void clearLastCheckedAssumptionLevelItemNr( boolean isDeactive, boolean isArchive )
		{
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		while( searchItem != null )
			{
			searchItem.clearLastCheckedAssumptionLevelItemNr();
			searchItem = searchItem.nextSpecificationItem();
			}
		}

	protected boolean hasPossessiveSpecificationItemButNotAQuestion()
		{
		SpecificationItem searchItem = firstActiveSpecificationItem( false, false );

		while( searchItem != null )
			{
			if( searchItem.isPossessive() )
				return true;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( false );
			}

		return false;
		}

	protected byte archiveOrDeleteSpecificationItem( SpecificationItem oldSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		if( oldSpecificationItem != null )
			{
			if( oldSpecificationItem.hasCurrentArchiveSentenceNr() )
				oldSpecificationItem.replacingSpecificationItem = replacingSpecificationItem;
			else
				{
				if( !oldSpecificationItem.isArchiveItem() ||
				oldSpecificationItem.replacingSpecificationItem == null )
					{
					if( replacingSpecificationItem == null ||
					replacingSpecificationItem.replacingSpecificationItem == null )
						{
						if( oldSpecificationItem != replacingSpecificationItem )
							{
							if( oldSpecificationItem.isAssignment() == isAssignmentList() )
								{
								if( replacingSpecificationItem == null ||
								replacingSpecificationItem.isAssignment() == isAssignmentList() )
									{
									oldSpecificationItem.replacingSpecificationItem = replacingSpecificationItem;

									// Update the specification items that are filtered by getAssignmentItem
									// variables: replacingSpecificationItem
									if( updateArchivedReplacingSpecificationItems( oldSpecificationItem ) == Constants.RESULT_OK )
										{
										if( oldSpecificationItem.hasCurrentCreationSentenceNr() )
											{
											if( oldSpecificationItem.isActiveItem() )
												{
												if( deleteActiveItem( false, oldSpecificationItem ) != Constants.RESULT_OK )
													addError( 1, null, "I failed to delete an active assignment item" );
												}
											else
												{
												if( oldSpecificationItem.isDeactiveItem() )
													{
													if( deleteDeactiveItem( false, oldSpecificationItem ) != Constants.RESULT_OK )
														addError( 1, null, "I failed to delete a deactive assignment item" );
													}
												}
											}
										else
											{
											if( oldSpecificationItem.isActiveItem() )
												{
												if( archiveActiveItem( oldSpecificationItem ) != Constants.RESULT_OK )
													addError( 1, null, "I failed to archive an active specification item" );
												}
											else
												{
												if( oldSpecificationItem.isDeactiveItem() )
													{
													if( archiveDeactiveItem( oldSpecificationItem ) != Constants.RESULT_OK )
														addError( 1, null, "I failed to archive a deactive specification item" );
													}
												}
											}

										if( CommonVariables.result == Constants.RESULT_OK )
											{
											if( myWord().updateSpecificationsInJustificationOfInvolvedWords( oldSpecificationItem, replacingSpecificationItem ) != Constants.RESULT_OK )
												addError( 1, null, "I failed to update the specifications in the justification of involved words" );
											}
										}
									else
										addError( 1, null, "I failed to update the replacing specification item of the archive specification items" );
									}
								else
									return setError( 1, null, "The given replacing specification item is an assignment item and I am a specification list, or the given replacing specification item is a specification item and I am an assignment list" );
								}
							else
								return setError( 1, null, "The given old specification item is an assignment item and I am a specification list, or the given old specification item is a specification item and I am an assignment list" );
							}
						else
							return setError( 1, null, "The given old specification item and the given replacing specification item are the same" );
						}
					else
						return setError( 1, null, "The given replacing specification item has a replacing specification item itself" );
					}
				else
					return setError( 1, null, "The given old specification item is already archived and has already a replacing specification item" );
				}
			}
		else
			return setError( 1, null, "The given old specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte collectGeneralizationAndSpecifications( boolean isDeactive, boolean isArchive, boolean isExclusiveGeneralization, boolean isGeneralizationCollection, boolean isQuestion, int collectionNr )
		{
		boolean isCollectGeneralization;
		boolean isCollectSpecification;
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( false, isDeactive, isArchive, isQuestion );
		WordItem specificationWordItem;

		if( collectionNr > Constants.NO_COLLECTION_NR )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( ( specificationWordItem = searchItem.specificationWordItem() ) == null )
					specificationWordItem = myWord();	// Specification string

				isCollectGeneralization = ( isGeneralizationCollection &&
										!searchItem.hasGeneralizationCollection() &&
										myWord().hasCollectionNr( collectionNr ) );

				isCollectSpecification = ( !isGeneralizationCollection &&
										!searchItem.hasSpecificationCollection() &&
										specificationWordItem.hasCollectionNr( collectionNr ) );

				if( isCollectGeneralization ||
				isCollectSpecification )
					{
					if( searchItem.hasCurrentCreationSentenceNr() )
						{
						if( searchItem.collectSpecificationItem( isCollectGeneralization, isCollectSpecification, isExclusiveGeneralization, collectionNr ) == Constants.RESULT_OK )
							searchItem = searchItem.nextSelectedSpecificationItem( false );
						else
							addError( 1, null, "I failed to collect a specification" );
						}
					else
						{
						if( changeOlderSpecificationItem( isExclusiveGeneralization, ( isCollectGeneralization ? collectionNr : Constants.NO_COLLECTION_NR ), ( isCollectSpecification ? collectionNr : Constants.NO_COLLECTION_NR ), searchItem ) == Constants.RESULT_OK )
							searchItem = firstAssignmentOrSpecificationItem( false, isDeactive, isArchive, isQuestion );
						else
							addError( 1, null, "I failed to collect an older specification" );
						}
					}
				else
					searchItem = searchItem.nextSelectedSpecificationItem( false );
				}
			}
		else
			return setError( 1, null, "The given collection number is undefined" );

		return CommonVariables.result;
		}

	protected byte checkJustificationItemForUsage( boolean isDeactive, boolean isArchive, JustificationItem unusedJustificationItem )
		{
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		if( unusedJustificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.specificationJustificationItem() == unusedJustificationItem )
					return setError( 1, null, "The specification justification item is still in use" );

				searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given unused justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte checkSpecificationItemForUsage( boolean isDeactive, boolean isArchive, SpecificationItem unusedSpecificationItem )
		{
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		if( unusedSpecificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.replacingSpecificationItem == unusedSpecificationItem )
					{
					if( searchItem.creationSentenceNr() < unusedSpecificationItem.creationSentenceNr() ||
					searchItem.creationSentenceNr() < CommonVariables.myFirstSentenceNr )
						searchItem.replacingSpecificationItem = null;
					else
						return setError( 1, null, "The replacing specification item is still in use" );
					}

				searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given unused specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte checkWordItemForUsage( boolean isDeactive, boolean isArchive, WordItem unusedWordItem )
		{
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		if( unusedWordItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.specificationWordItem() == unusedWordItem )
					return setError( 1, null, "The specification word item is still in use" );

				searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given unused word item is undefined" );

		return CommonVariables.result;
		}

	protected byte confirmSpecificationButNotRelation( boolean isDeactive, boolean isArchive, SpecificationItem confirmedSpecificationItem, SpecificationItem confirmationSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		JustificationItem replacingJustificationItem;
		JustificationItem foundDefinitionSpecificationJustificationItem;
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( isDeactive, isArchive );

		if( confirmedSpecificationItem != null )
			{
			if( confirmedSpecificationItem.isSelfGenerated() )
				{
				if( confirmationSpecificationItem != null )
					{
					if( confirmationSpecificationItem.isUserSpecification() )
						{
						while( CommonVariables.result == Constants.RESULT_OK &&
						searchItem != null )
							{
							if( !searchItem.hasRelationContext() &&
							( foundDefinitionSpecificationJustificationItem = searchItem.foundDefinitionSpecificationJustificationItem( confirmedSpecificationItem ) ) != null )
								{
								if( ( justificationResult = myWord().addJustification( false, foundDefinitionSpecificationJustificationItem.justificationTypeNr(), foundDefinitionSpecificationJustificationItem.orderNr, foundDefinitionSpecificationJustificationItem.originalSentenceNr(), foundDefinitionSpecificationJustificationItem.attachedJustificationItem(), confirmationSpecificationItem, foundDefinitionSpecificationJustificationItem.anotherDefinitionSpecificationItem(), foundDefinitionSpecificationJustificationItem.specificSpecificationItem() ) ).result == Constants.RESULT_OK )
									{
									if( ( replacingJustificationItem = justificationResult.createdJustificationItem ) != null )
										{
										if( myWord().archiveJustification( searchItem.hasExclusiveGeneralizationCollection(), foundDefinitionSpecificationJustificationItem, replacingJustificationItem ) == Constants.RESULT_OK )
											searchItem = firstAssignmentOrSpecificationItem( isDeactive, isArchive );
										else
											addError( 1, null, "I failed to archive the attached justification item before the archived justification item in my word" );
										}
									else
										return setError( 1, null, "I couldn't create a replacing specification justification" );
									}
								else
									addError( 1, null, "I failed to add a justification" );
								}
							else
								searchItem = searchItem.nextAssignmentOrSpecificationItem();
							}
						}
					else
						return setError( 1, null, "The given confirmation specification item is not a user specification" );
					}
				else
					return setError( 1, null, "The given confirmation specification item is undefined" );
				}
			else
				return setError( 1, null, "The given confirmed specification but not its relation(s) specification item is not self-generated" );
			}
		else
			return setError( 1, null, "The given confirmed specification but not its relation(s) specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte recalculateAssumptions( boolean isDeactive, boolean isArchive )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		short previousAssumptionLevel;
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( isDeactive, isArchive );

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.isOlderSentence() &&
			searchItem.isSelfGeneratedAssumption() )
				{
				if( ( specificationResult = searchItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
					{
					previousAssumptionLevel = specificationResult.assumptionLevel;

					if( searchItem.hasOnlyExclusiveSpecificationSubstitutionAssumptionsWithoutDefinition() )
						searchItem.markAsConcludedAssumption();

					if( ( specificationResult = searchItem.recalculateAssumptionLevel() ).result == Constants.RESULT_OK )
						{
						if( specificationResult.assumptionLevel == Constants.NO_ASSUMPTION_LEVEL )
							searchItem.markAsConcludedAssumption();
						else
							{
							if( specificationResult.assumptionLevel != previousAssumptionLevel )
								{
								// Write adjusted specification
								if( myWord().writeSpecification( true, false, false, searchItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to write an adjusted specification" );
								}
							}
						}
					else
						addError( 1, null, "I failed to recalculate the assumption level" );
					}
				else
					addError( 1, null, "I failed to get the assumption level" );
				}

			searchItem = searchItem.nextAssignmentOrSpecificationItem();
			}

		return CommonVariables.result;
		}

	protected byte replaceOlderSpecificationItems( int oldRelationContextNr, SpecificationItem replacingSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		JustificationItem specificationJustificationItem;
		SpecificationItem searchItem = firstActiveSpecificationItem();

		if( replacingSpecificationItem != null )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.isOlderSentence() &&
				searchItem.relationContextNr() == oldRelationContextNr &&
				( specificationJustificationItem = searchItem.specificationJustificationItem() ) != null )
					{
					replacingSpecificationItem.markAsConcludedAssumption();

					if( archiveOrDeleteSpecificationItem( searchItem, replacingSpecificationItem ) == Constants.RESULT_OK )
						{
						if( !specificationJustificationItem.hasCurrentActiveSentenceNr() )
							{
							if( ( justificationResult = myWord().addJustification( true, specificationJustificationItem.justificationTypeNr(), specificationJustificationItem.orderNr, specificationJustificationItem.originalSentenceNr(), specificationJustificationItem.attachedJustificationItem(), specificationJustificationItem.definitionSpecificationItem(), specificationJustificationItem.anotherDefinitionSpecificationItem(), specificationJustificationItem.specificSpecificationItem() ) ).result == Constants.RESULT_OK )
								{
								if( justificationResult.createdJustificationItem != null )
									{
									if( myWord().archiveJustification( false, specificationJustificationItem, justificationResult.createdJustificationItem ) == Constants.RESULT_OK )
										specificationJustificationItem = justificationResult.createdJustificationItem;
									else
										addError( 1, null, "I failed to archive a justification item" );
									}
								else
									return setError( 1, null, "I couldn't create a justification item" );
								}
							else
								addError( 1, null, "I failed to add a justification item" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( myWord().attachJustification( specificationJustificationItem, replacingSpecificationItem ) == Constants.RESULT_OK )
								searchItem = nextSpecificationListItem();
							else
								addError( 1, null, "I failed to attach a justification item" );
							}
						}
					else
						addError( 1, null, "I failed to archive or delete a specification" );
					}
				else
					searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given replacing specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte updateJustificationInSpecificationItems( boolean isExclusive, boolean isExclusiveGeneralization, boolean isDeactive, boolean isArchive, JustificationItem oldJustificationItem, JustificationItem replacingJustificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		int archiveSentenceNr;
		JustificationItem predecessorOfOldAttachedJustificationItem;
		JustificationItem replacingAttachedJustificationItem;
		JustificationItem specificationJustificationItem;
		SpecificationItem replacingSpecificationItem;
		SpecificationItem searchItem = firstSpecificationItem( isDeactive, isArchive );

		if( oldJustificationItem != null )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( ( searchItem.hasCurrentArchiveSentenceNr() ||
				searchItem.replacingSpecificationItem == null ) &&

				( specificationJustificationItem = searchItem.specificationJustificationItem() ) != null )
					{
					if( specificationJustificationItem == oldJustificationItem )
						{
						if( searchItem.hasCurrentCreationSentenceNr() )	// Allowed to change without archiving this one and creating a new one
							{
							if( searchItem.changeJustificationItem( replacingJustificationItem ) == Constants.RESULT_OK )
								searchItem = searchItem.nextSpecificationItem();
							else
								addError( 1, null, "I failed to change to specification justification item of a specification item" );
							}
						else
							{
							replacingSpecificationItem = null;

							if( replacingJustificationItem == null )
								{
								if( myWord().isCorrectedAssumption() )
									{
									// Write corrected specification
									if( myWord().writeSpecification( false, myWord().isCorrectedAssumptionByKnowledge(), myWord().isCorrectedAssumptionByOppositeQuestion(), searchItem ) != Constants.RESULT_OK )
										addError( 1, null, "I failed to write an adjusted specification" );
									}
								}
							else
								{
								if( isAssignmentList() )
									{
									if( ( specificationResult = createAssignmentItem( searchItem.isAnsweredQuestion(), searchItem.isConcludedAssumption(), searchItem.isDeactiveItem(), isArchive, ( isExclusive || isExclusiveGeneralization || searchItem.isExclusive() ), searchItem.isNegative(), searchItem.isPossessive(), searchItem.isValueSpecification(), searchItem.assignmentLevel(), searchItem.assumptionLevel(), searchItem.prepositionParameter(), searchItem.questionParameter(), searchItem.generalizationWordTypeNr(), searchItem.specificationWordTypeNr(), searchItem.generalizationCollectionNr(), searchItem.specificationCollectionNr(), searchItem.generalizationContextNr(), searchItem.specificationContextNr(), searchItem.relationContextNr(), searchItem.originalSentenceNr(), searchItem.activeSentenceNr(), searchItem.deactiveSentenceNr(), searchItem.archiveSentenceNr(), searchItem.nContextRelations(), replacingJustificationItem, searchItem.specificationWordItem(), searchItem.specificationString() ) ).result == Constants.RESULT_OK )
										{
										if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
											return setError( 1, null, "I couldn't create an assignment" );
										}
									else
										addError( 1, null, "I failed to create an assignment" );
									}
								else
									{
									if( ( specificationResult = createSpecificationItem( searchItem.isAnsweredQuestion(), searchItem.isConditional(), searchItem.isConcludedAssumption(), searchItem.isDeactiveItem(), isArchive, ( isExclusive || isExclusiveGeneralization || searchItem.isExclusive() ), searchItem.isNegative(), searchItem.isPossessive(), searchItem.isSpecificationGeneralization(), searchItem.isValueSpecification(), searchItem.assumptionLevel(), searchItem.prepositionParameter(), searchItem.questionParameter(), searchItem.generalizationWordTypeNr(), searchItem.specificationWordTypeNr(), searchItem.generalizationCollectionNr(), searchItem.specificationCollectionNr(), searchItem.generalizationContextNr(), searchItem.specificationContextNr(), ( isExclusiveGeneralization ? Constants.NO_CONTEXT_NR : searchItem.relationContextNr() ), searchItem.originalSentenceNr(), searchItem.nContextRelations(), replacingJustificationItem, searchItem.specificationWordItem(), searchItem.specificationString() ) ).result == Constants.RESULT_OK )
										{
										if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
											return setError( 1, null, "I couldn't create a specification" );
										}
									else
										addError( 1, null, "I failed to create a specification" );
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								archiveSentenceNr = searchItem.archiveSentenceNr();	// Remember the archive sentence number

								if( archiveOrDeleteSpecificationItem( searchItem, replacingSpecificationItem ) == Constants.RESULT_OK )
									{
									if( !isAssignmentList() &&
									isExclusiveGeneralization )
										{
										if( myWord().assignSpecificationInWord( false, false, true, false, searchItem.isNegative(), searchItem.isPossessive(), searchItem.isSelfGenerated(), searchItem.prepositionParameter(), searchItem.questionParameter(), searchItem.generalizationContextNr(), searchItem.specificationContextNr(), searchItem.relationContextNr(), searchItem.originalSentenceNr(), searchItem.activeSentenceNr(), searchItem.deactiveSentenceNr(), archiveSentenceNr, searchItem.nContextRelations(), replacingJustificationItem, searchItem.specificationWordItem(), null, searchItem.specificationString() ).result != Constants.RESULT_OK )
											addError( 1, null, "I failed to create an assignment" );
										}

									if( CommonVariables.result == Constants.RESULT_OK &&
									CommonVariables.isSpecificationConfirmedByUser &&
									replacingSpecificationItem != null &&
									replacingSpecificationItem.isSelfGeneratedAssumption() )
										{
										if( myWord().recalculateAssumptionsInWord() != Constants.RESULT_OK )
											addError( 1, null, "I failed to recalculate the assumptions in my word" );
										}

									searchItem = ( isArchive ? searchItem.nextSpecificationItem() : firstSpecificationItem( isDeactive, isArchive ) );
									}
								else
									addError( 1, null, "I failed to archive or delete a specification" );
								}
							}
						}
					else	// Check attached justification items
						{
						if( ( predecessorOfOldAttachedJustificationItem = specificationJustificationItem.predecessorOfOldAttachedJustificationItem( oldJustificationItem ) ) != null )
							{
							if( predecessorOfOldAttachedJustificationItem.hasCurrentCreationSentenceNr() )
								{
								if( predecessorOfOldAttachedJustificationItem.changeAttachedJustificationItem( replacingJustificationItem ) == Constants.RESULT_OK )
									searchItem = searchItem.nextSpecificationItem();
								else
									addError( 1, null, "I failed to change the attached justification item of a justification item" );
								}
							else
								{
								if( ( justificationResult = myWord().addJustification( false, predecessorOfOldAttachedJustificationItem.justificationTypeNr(), predecessorOfOldAttachedJustificationItem.orderNr, predecessorOfOldAttachedJustificationItem.originalSentenceNr(), replacingJustificationItem, predecessorOfOldAttachedJustificationItem.definitionSpecificationItem(), predecessorOfOldAttachedJustificationItem.anotherDefinitionSpecificationItem(), predecessorOfOldAttachedJustificationItem.specificSpecificationItem() ) ).result == Constants.RESULT_OK )
									{
									if( ( replacingAttachedJustificationItem = justificationResult.createdJustificationItem ) != null )
										{
										if( myWord().archiveJustification( isExclusiveGeneralization, predecessorOfOldAttachedJustificationItem, replacingAttachedJustificationItem ) == Constants.RESULT_OK )
											searchItem = firstSpecificationItem( isDeactive, isArchive );
										else
											addError( 1, null, "I failed to archive the attached justification item before the archived justification item in my word" );
										}
									else
										searchItem = searchItem.nextSpecificationItem();
									}
								else
									addError( 1, null, "I failed to add a replacing attached justification item" );
								}
							}
						else
							searchItem = searchItem.nextSpecificationItem();
						}
					}
				else
					searchItem = searchItem.nextSpecificationItem();
				}
			}
		else
			return setError( 1, null, "The given old justification item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType findSpecificationItem( boolean includeDeactiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem, WordItem relationContextWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( ( specificationResult = _findSpecificationItem( false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, relationContextWordItem ) ).result == Constants.RESULT_OK )
			{
			if( includeDeactiveItems &&
			specificationResult.foundSpecificationItem == null )
				{
				if( ( specificationResult = _findSpecificationItem( true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem, relationContextWordItem ) ).result != Constants.RESULT_OK )
					addError( 1, null, "I failed to find a deactive specification item with relation" );
				}
			}
		else
			addError( 1, null, "I failed to find an active specification item with relation" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType createSpecificationItem( boolean isAnsweredQuestion, boolean isConditional, boolean isConcludedAssumption, boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSpecificationGeneralization, boolean isValueSpecification, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( !isAssignmentList() )
			{
			if( generalizationWordTypeNr >= Constants.WORD_TYPE_UNDEFINED &&
			generalizationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
				{
				if( specificationWordTypeNr > Constants.WORD_TYPE_UNDEFINED &&
				specificationWordTypeNr < Constants.NUMBER_OF_WORD_TYPES )
					{
					if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
						{
						if( specificationJustificationItem == null ||
						specificationJustificationItem.isActiveItem() ||

						( isArchive &&
						specificationJustificationItem.isArchiveItem() ) )
							{
							if( ( specificationResult.createdSpecificationItem = new SpecificationItem( isAnsweredQuestion, isConditional, isConcludedAssumption, isExclusive, isNegative, isPossessive, isSpecificationGeneralization, isValueSpecification, Constants.NO_ASSIGNMENT_LEVEL, assumptionLevel, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString, this, myWord() ) ) != null )
								{
								if( isArchive )
									{
									specificationResult.createdSpecificationItem.setArchiveStatus();

									if( addItemToArchiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
										addError( 1, null, "I failed to add an archive specification item" );
									}
								else
									{
									if( isDeactive )
										{
										specificationResult.createdSpecificationItem.setDeactiveStatus();

										if( addItemToDeactiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
											addError( 1, null, "I failed to add a deactive specification item" );
										}
									else
										{
										if( addItemToActiveList( (Item)specificationResult.createdSpecificationItem ) != Constants.RESULT_OK )
											addError( 1, null, "I failed to add an active specification item" );
										}
									}
								}
							else
								setError( 1, null, "I failed to create a specification item" );
							}
						else
							setError( 1, null, "The given specification justification item isn't active" );
						}
					else
						setError( 1, null, "The current item number is undefined" );
					}
				else
					setError( 1, null, "The given specification word type number is undefined or out of bounds" );
				}
			else
				setError( 1, null, "The given generalization word type number is undefined or out of bounds" );
			}
		else
			setError( 1, null, "I am an assignment list" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationItem firstActiveSpecificationItem( boolean includeAnsweredQuestions, boolean isQuestion )
		{
		SpecificationItem firstSpecificationItem;

		if( ( firstSpecificationItem = firstActiveSpecificationItem() ) != null )
			return firstSpecificationItem.getSpecificationItem( includeAnsweredQuestions, true, isQuestion );

		return null;
		}

	protected SpecificationItem firstActiveSpecificationItem( boolean includeAnsweredQuestions, short questionParameter )
		{
		SpecificationItem firstSpecificationItem;

		if( ( firstSpecificationItem = firstActiveSpecificationItem() ) != null )
			return firstSpecificationItem.getSpecificationItem( includeAnsweredQuestions, true, questionParameter );

		return null;
		}

	protected SpecificationItem firstActiveSpecificationItem( boolean isPossessive, short questionParameter, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem searchItem = firstAssignmentOrSpecificationItem( false, false, false, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.relationContextNr() == relationContextNr &&
			searchItem.specificationWordItem() == specificationWordItem )
				return searchItem;

			searchItem = searchItem.nextSelectedSpecificationItem( false );
			}

		return null;
		}

	protected SpecificationItem firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeActiveItems, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem = null;

		if( includeActiveItems )
			foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		if( includeDeactiveItems &&
		foundSpecificationItem == null )
			foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, true, false, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		if( includeArchiveItems &&
		foundSpecificationItem == null )
			return _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem );

		return foundSpecificationItem;
		}

	protected SpecificationItem firstUserSpecificationItem( boolean includeAnsweredQuestions, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem;

		if( ( foundSpecificationItem = _firstUserSpecificationItem( includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( includeDeactiveItems )
				foundSpecificationItem = _firstUserSpecificationItem( includeAnsweredQuestions, true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

			if( includeArchiveItems &&
			foundSpecificationItem == null )
				return _firstUserSpecificationItem( includeAnsweredQuestions, false, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
			}

		return foundSpecificationItem;
		}

	protected SpecificationItem firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem searchItem = firstActiveSpecificationItem( includeAnsweredQuestions, questionParameter );

		while( searchItem != null )
			{
			if( searchItem.isPossessive() == isPossessive &&
			searchItem.specificationWordItem() == specificationWordItem &&
			searchItem.isMatchingGeneralizationContextNr( allowEmptyContextResult, generalizationContextNr ) &&
			searchItem.isMatchingSpecificationContextNr( allowEmptyContextResult, specificationContextNr ) &&
			searchItem.isMatchingRelationContextNr( allowEmptyContextResult, relationContextNr ) &&

			( specificationString == null ||
			searchItem.specificationString() == null ||
			searchItem.specificationString().equals( specificationString ) ) )
				return searchItem;

			searchItem = searchItem.nextSpecificationItemWithSameQuestionParameter( includeAnsweredQuestions );
			}

		return null;
		}

	protected SpecificationItem firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem;

		if( ( foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( includeDeactiveItems )
				foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

			if( includeArchiveItems &&
			foundSpecificationItem == null )
				return _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
			}

		return foundSpecificationItem;
		}

	protected SpecificationItem findSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem;

		if( ( foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( includeDeactiveItems )
				foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, true, false, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

			if( includeArchiveItems &&
			foundSpecificationItem == null )
				return _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, true, isNegative, isPossessive, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
			}

		return foundSpecificationItem;
		}

	protected SpecificationItem firstSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short questionParameter, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem, String specificationString )
		{
		SpecificationItem foundSpecificationItem;

		if( ( foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString ) ) == null )
			{
			if( includeDeactiveItems )
				foundSpecificationItem = _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, true, false, isNegative, isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );

			if( includeArchiveItems &&
			foundSpecificationItem == null )
				return _firstSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, true, isNegative, isPossessive, isSelfGenerated, questionParameter, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, specificationString );
			}

		return foundSpecificationItem;
		}

	protected SpecificationItem firstAssumptionSpecificationItem( boolean allowEmptyContextResult, boolean includeAnsweredQuestions, boolean includeDeactiveItems, boolean includeArchiveItems, boolean isNegative, boolean isPossessive, short questionParameter, int generalizationContextNr, int specificationContextNr, WordItem specificationWordItem )
		{
		SpecificationItem foundSpecificationItem;

		if( ( foundSpecificationItem = _firstAssumptionSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem ) ) == null )
			{
			if( includeDeactiveItems )
				foundSpecificationItem = _firstAssumptionSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, true, false, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem );

			if( includeArchiveItems &&
			foundSpecificationItem == null )
				return _firstAssumptionSpecificationItem( allowEmptyContextResult, includeAnsweredQuestions, false, true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, specificationWordItem );
			}

		return foundSpecificationItem;
		}
	};

/*************************************************************************
 *
 *	"Good comes to those who lend money generously
 *	and conduct their business fairly.
 *	Such people will not be overcome by evil.
 *	Those who are righteous will be long remembered" (Psalm 112:5-6)
 *
 *************************************************************************/
