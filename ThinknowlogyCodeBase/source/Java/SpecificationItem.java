/*
 *	Class:			SpecificationItem
 *	Purpose:		To store info about the specification structure
 *					of a word
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

class SpecificationItem extends Item
	{
	// Private constructible variables

	private short nAssignmentLevelRecalculations_;

	// Private loadable variables

	private boolean isAnsweredQuestion_;
	private boolean isConcludedAssumption_;
	private boolean isConditional_;
	private boolean isExclusive_;
	private boolean isNegative_;
	private boolean isPossessive_;
	private boolean isSpecificationGeneralization_;
	private boolean isValueSpecification_;

	private short assignmentLevel_;
	private short assumptionLevel_;
	private short prepositionParameter_;
	private short questionParameter_;

	private short generalizationWordTypeNr_;
	private short specificationWordTypeNr_;

	private int generalizationCollectionNr_;
	private int specificationCollectionNr_;

	private int generalizationContextNr_;
	private int specificationContextNr_;
	private int relationContextNr_;

	private int nContextRelations_;

	private JustificationItem specificationJustificationItem_;

	private WordItem specificationWordItem_;

	private String specificationString_;


	// Private constructible variables

	private short specificationStringWriteLevel_;

	private int lastCheckedAssumptionLevelItemNr_;


	// Private question methods

	private SpecificationItem newUserQuestion( boolean includeThisItem )
		{
		SpecificationItem searchItem = ( includeThisItem ? this : nextSelectedSpecificationItem( false ) );

		while( searchItem != null )
			{
			if( !searchItem.isAnsweredQuestion() &&
			!searchItem.isOlderSentence() &&
			searchItem.isUserQuestion() )
				return searchItem;

			searchItem = searchItem.nextSelectedSpecificationItem( false );
			}

		return searchItem;
		}


	// Private specification methods

	private SpecificationResultType calculateAssumptionLevel( boolean needsRecalculation )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		short highestAssumptionLevel;
		short lowestAssumptionLevel = Constants.MAX_LEVEL;
		int tempAssumptionLevel;
		int nJustificationRelationWords;
		int nSpecificationRelationWords;
		JustificationItem currentJustificationItem;
		JustificationItem nextJustificationItem = specificationJustificationItem_;

		if( ( needsRecalculation ||
		assumptionLevel_ == Constants.NO_ASSUMPTION_LEVEL ) &&

		isSelfGeneratedAssumption() )
			{
			if( ++nAssignmentLevelRecalculations_ < Constants.MAX_ASSUMPTION_LEVEL_RECALCULATIONS )
				{
				nSpecificationRelationWords = myWord().nContextWords( isPossessive_, relationContextNr_, specificationWordItem_ );

				do	{
					nJustificationRelationWords = 0;
					highestAssumptionLevel = Constants.NO_ASSUMPTION_LEVEL;
					currentJustificationItem = nextJustificationItem;

					if( currentJustificationItem != null )
						{
						do	{
							nJustificationRelationWords += currentJustificationItem.nJustificationContextRelations( relationContextNr_, nSpecificationRelationWords );

							if( currentJustificationItem.isAssumption() )
								{
								if( ( specificationResult = currentJustificationItem.getCombinedAssumptionLevel() ).result == Constants.RESULT_OK )
									{
									if( ( tempAssumptionLevel = ( specificationResult.combinedAssumptionLevel + currentJustificationItem.assumptionGrade() ) ) > highestAssumptionLevel )
										{
										if( tempAssumptionLevel < Constants.MAX_LEVEL )
											highestAssumptionLevel = (short)tempAssumptionLevel;
										else
											setSystemErrorInItem( 1, null, null, "Assumption level overflow" );
										}
									}
								else
									addErrorInItem( 1, null, null, "I failed to get the combined assumption level" );
								}
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						( currentJustificationItem = currentJustificationItem.nextJustificationItemWithSameTypeAndOrderNr() ) != null );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					highestAssumptionLevel < lowestAssumptionLevel &&

					( nJustificationRelationWords == nSpecificationRelationWords ||

					( assumptionLevel_ == Constants.NO_ASSUMPTION_LEVEL &&
					lastCheckedAssumptionLevelItemNr_ == CommonVariables.currentItemNr ) ) )	// To avoid looping: calculation A, calculation B, calculation A, etc.
						lowestAssumptionLevel = highestAssumptionLevel;
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( nextJustificationItem = nextJustificationItem.nextJustificationItemWithDifferentTypeOrOrderNr( specificationJustificationItem_ ) ) != null );

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( lowestAssumptionLevel < Constants.MAX_LEVEL )
						{
						nAssignmentLevelRecalculations_ = 0;
						assumptionLevel_ = lowestAssumptionLevel;
						}

					lastCheckedAssumptionLevelItemNr_ = CommonVariables.currentItemNr;
					}
				}
			else
				setErrorInItem( 1, null, "I think there is an endless loop in the assumption level calculation of my specification, because the number of iterations is: " + nAssignmentLevelRecalculations_ );
			}

		specificationResult.assumptionLevel = assumptionLevel_;
		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}


	// Protected constructible variables

	protected SpecificationItem replacingSpecificationItem;


	// Constructor

	protected SpecificationItem( boolean isAnsweredQuestion, boolean isConditional, boolean _isConcludedAssumption, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSpecificationGeneralization, boolean isValueSpecification, short assignmentLevel, short assumptionLevel, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem specificationWordItem, String specificationString, List myList, WordItem myWord )
		{
		initializeItemVariables( originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, myList, myWord );

		// Private constructible variables

		nAssignmentLevelRecalculations_ = 0;

		// Private loadable variables

		isAnsweredQuestion_ = isAnsweredQuestion;
		isConcludedAssumption_ = _isConcludedAssumption;
		isConditional_ = isConditional;
		isExclusive_ = isExclusive;
		isNegative_ = isNegative;
		isPossessive_ = isPossessive;
		isSpecificationGeneralization_ = isSpecificationGeneralization;
		isValueSpecification_ = isValueSpecification;

		assignmentLevel_ = assignmentLevel;
		assumptionLevel_ = assumptionLevel;
		prepositionParameter_ = prepositionParameter;
		questionParameter_ = questionParameter;

		generalizationWordTypeNr_ = generalizationWordTypeNr;
		specificationWordTypeNr_ = specificationWordTypeNr;

		generalizationCollectionNr_ = generalizationCollectionNr;
		specificationCollectionNr_ = specificationCollectionNr;

		generalizationContextNr_ = generalizationContextNr;
		specificationContextNr_ = specificationContextNr;
		relationContextNr_ = relationContextNr;

		nContextRelations_ = nContextRelations;

		specificationJustificationItem_ = specificationJustificationItem;

		specificationWordItem_ = specificationWordItem;

		specificationString_ = null;

		if( specificationString != null )
			{
			if( ( specificationString_ = new String( specificationString ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create a specification string" );
			}

		// Private constructible variables

		specificationStringWriteLevel_ = Constants.NO_WRITE_LEVEL;

		lastCheckedAssumptionLevelItemNr_ = Constants.NO_ITEM_NR;

		// Protected constructible variables

		replacingSpecificationItem = null;
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( specificationString_ != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( specificationString_ );
			}
		}

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		String wordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();
		if( specificationWordItem_ != null &&
		( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( wordString );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( prepositionParameter_ == queryParameter ||
				questionParameter_ == queryParameter ||
				generalizationCollectionNr_ == queryParameter ||
				specificationCollectionNr_ == queryParameter ||
				generalizationContextNr_ == queryParameter ||
				specificationContextNr_ == queryParameter ||
				relationContextNr_ == queryParameter ||
				nContextRelations_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&

				( prepositionParameter_ > Constants.NO_PREPOSITION_PARAMETER ||
				questionParameter_ > Constants.NO_QUESTION_PARAMETER ||
				generalizationCollectionNr_ > Constants.NO_COLLECTION_NR ||
				specificationCollectionNr_ > Constants.NO_COLLECTION_NR ||
				generalizationContextNr_ > Constants.NO_CONTEXT_NR ||
				specificationContextNr_ > Constants.NO_CONTEXT_NR ||
				relationContextNr_ > Constants.NO_CONTEXT_NR ||
				nContextRelations_ > 0 ) ) );
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( ( specificationJustificationItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : specificationJustificationItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : specificationJustificationItem_.itemNr() == queryItemNr ) ) ||

				( specificationWordItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : specificationWordItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : specificationWordItem_.itemNr() == queryItemNr ) ) ||

				( replacingSpecificationItem == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : replacingSpecificationItem.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : replacingSpecificationItem.itemNr() == queryItemNr ) ) );
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		return ( generalizationWordTypeNr_ == queryWordTypeNr ||
				specificationWordTypeNr_ == queryWordTypeNr	);
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// 1) Assignment needs descending assignmentLevel
				assignmentLevel_ > ( (SpecificationItem)nextSortItem ).assignmentLevel_ ||

				// 2) Question and specification needs descending creationSentenceNr
				( assignmentLevel_ == ( (SpecificationItem)nextSortItem ).assignmentLevel_ &&
				creationSentenceNr() > nextSortItem.creationSentenceNr() ) );
		}

	protected byte checkForUsage()
		{
		return myWord().checkSpecificationForUsageOfInvolvedWords( this );
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		CommonVariables.foundMatchingStrings = false;

		if( specificationWordItem_ != null )
			{
			if( specificationWordItem_.findMatchingWordReferenceString( queryString ) != Constants.RESULT_OK )
				addErrorInItem( 1, null, null, "I failed to find a matching word reference string for the specification word" );
			}

		return CommonVariables.result;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		String wordString;
		String generalizationWordTypeString = myWord().wordTypeName( generalizationWordTypeNr_ );
		String specificationWordTypeString = myWord().wordTypeName( specificationWordTypeNr_ );

		baseToStringBuffer( queryWordTypeNr );

		if( isAnsweredQuestion_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isAnsweredQuestion" );

		if( isConditional_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isConditional" );

		if( isConcludedAssumption_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isConcludedAssumption" );

		if( isExclusive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isExclusive" );

		if( isNegative_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isNegative" );

		if( isPossessive_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isPossessive" );

		if( isSpecificationGeneralization_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSpecificationGeneralization" );

		if( isValueSpecification_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isValueSpecification" );

		if( assignmentLevel_ > Constants.NO_ASSIGNMENT_LEVEL )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "assignmentLevel:" + assignmentLevel_ );

		if( assumptionLevel_ > Constants.NO_ASSUMPTION_LEVEL )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "assumptionLevel:" + assumptionLevel_ );

		if( specificationStringWriteLevel_ > Constants.NO_WRITE_LEVEL )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationStringWriteLevel:" + specificationStringWriteLevel_ );

		if( prepositionParameter_ > Constants.NO_PREPOSITION_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "prepositionParameter:" + prepositionParameter_ );

		if( questionParameter_ > Constants.NO_QUESTION_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "questionParameter:" + questionParameter_ );
/*
		if( lastCheckedAssumptionLevelItemNr_ > Constants.NO_ITEM_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "lastCheckedAssumptionLevelItemNr:" + lastCheckedAssumptionLevelItemNr_ );
*/
		if( generalizationCollectionNr_ > Constants.NO_COLLECTION_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationCollectionNr:" + generalizationCollectionNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationWordType:" + ( generalizationWordTypeString == null ? Constants.EMPTY_STRING : generalizationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + generalizationWordTypeNr_ );

		if( generalizationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "generalizationContextNr:" + generalizationContextNr_ );

		if( specificationCollectionNr_ > Constants.NO_COLLECTION_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationCollectionNr:" + specificationCollectionNr_ );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordType:" + ( specificationWordTypeString == null ? Constants.EMPTY_STRING : specificationWordTypeString ) + Constants.QUERY_WORD_TYPE_STRING + specificationWordTypeNr_ );

		if( specificationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationContextNr:" + specificationContextNr_ );

		if( specificationWordItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationWordItem" + Constants.QUERY_REF_ITEM_START_CHAR + specificationWordItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + specificationWordItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

			if( ( wordString = specificationWordItem_.wordTypeString( specificationWordTypeNr_ ) ) != null )
				CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_REFERENCE_START_CHAR + wordString + Constants.QUERY_WORD_REFERENCE_END_CHAR );
			}

		if( relationContextNr_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "relationContextNr:" + relationContextNr_ );

		if( nContextRelations_ > Constants.NO_CONTEXT_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "nContextRelations:" + nContextRelations_ );

		if( specificationJustificationItem_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationJustification" + Constants.QUERY_REF_ITEM_START_CHAR + specificationJustificationItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + specificationJustificationItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

		if( replacingSpecificationItem != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "replacingSpecification" + Constants.QUERY_REF_ITEM_START_CHAR + replacingSpecificationItem.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + replacingSpecificationItem.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );

		if( specificationString_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificationString:" + specificationString_ );

		return CommonVariables.queryStringBuffer;
		}


	// Protected assignment methods

	protected boolean isAssignment()
		{
		return ( myList().isAssignmentList() );
		}

	protected boolean isActiveAssignment()
		{
		return ( isAssignment() &&
				isActiveItem() );
		}

	protected boolean isDeactiveAssignment()
		{
		return ( isAssignment() &&
				isDeactiveItem() );
		}

	protected boolean isArchiveAssignment()
		{
		return ( isAssignment() &&
				isArchiveItem() );
		}

	protected short assignmentLevel()
		{
		return assignmentLevel_;
		}

	protected short assumptionLevel()
		{
		return assumptionLevel_;
		}

	protected SpecificationItem getAssignmentItem( boolean includeAnsweredQuestions, boolean includeThisItem )
		{
		SpecificationItem searchItem = ( includeThisItem ? ( assignmentLevel_ == CommonVariables.currentAssignmentLevel ? this : null ) : nextAssignmentItemWithCurrentLevel() );

		while( searchItem != null )
			{
			// Skip replaced assignments
			if( ( searchItem.replacingSpecificationItem == null ||
			// Skip replaced specifications that are undone
			searchItem.replacingSpecificationItem.creationSentenceNr() > CommonVariables.currentSentenceNr ) &&

			// Skip answered questions
			( includeAnsweredQuestions ||
			!searchItem.isAnsweredQuestion() ) )
				return searchItem;

			searchItem = searchItem.nextAssignmentItemWithCurrentLevel();
			}

		return null;
		}

	protected SpecificationItem getAssignmentItem( boolean includeAnsweredQuestions, boolean includeThisItem, boolean isQuestion )
		{
		SpecificationItem searchItem = ( includeThisItem ? ( assignmentLevel_ == CommonVariables.currentAssignmentLevel ? this : null ) : nextAssignmentItemWithCurrentLevel() );

		while( searchItem != null )
			{
			// Skip replaced assignments
			if( ( searchItem.replacingSpecificationItem == null ||
			// Skip replaced specifications that are undone
			searchItem.replacingSpecificationItem.creationSentenceNr() > CommonVariables.currentSentenceNr ) &&

			// Skip answered questions
			( includeAnsweredQuestions ||
			!searchItem.isAnsweredQuestion() ) &&

			searchItem.isQuestion() == isQuestion )
				return searchItem;

			searchItem = searchItem.nextAssignmentItemWithCurrentLevel();
			}

		return null;
		}

	protected SpecificationItem getAssignmentItem( boolean includeAnsweredQuestions, boolean includeThisItem, short questionParameter )
		{
		SpecificationItem searchItem = ( includeThisItem ? ( assignmentLevel_ == CommonVariables.currentAssignmentLevel ? this : null ) : nextAssignmentItemWithCurrentLevel() );

		while( searchItem != null )
			{
			// Skip replaced assignments
			if( ( searchItem.replacingSpecificationItem == null ||
			// Skip replaced specifications that are undone
			searchItem.replacingSpecificationItem.creationSentenceNr() > CommonVariables.currentSentenceNr ) &&

			// Skip answered questions
			( includeAnsweredQuestions ||
			!searchItem.isAnsweredQuestion() ) &&

			searchItem.questionParameter() == questionParameter )
				return searchItem;

			searchItem = searchItem.nextAssignmentItemWithCurrentLevel();
			}

		return null;
		}

	protected SpecificationItem nextAssignmentItemWithCurrentLevel()
		{
		SpecificationItem nextAssignmentItem = (SpecificationItem)nextItem;

		if( nextAssignmentItem != null &&
		nextAssignmentItem.assignmentLevel() == CommonVariables.currentAssignmentLevel )
			return nextAssignmentItem;

		return null;
		}

	protected SpecificationItem nextAssignmentItemButNotAQuestion()
		{
		return getAssignmentItem( false, false, false );
		}


	// Protected question methods

	protected boolean isAnsweredQuestion()
		{
		return isAnsweredQuestion_;
		}

	protected boolean isConcludedAssumption()
		{
		return isConcludedAssumption_;
		}

	protected boolean isQuestion()
		{
		return ( questionParameter_ > Constants.NO_QUESTION_PARAMETER );
		}

	protected boolean isSelfGeneratedQuestion()
		{
		JustificationItem searchItem = specificationJustificationItem_;

		if( questionParameter_ > Constants.NO_QUESTION_PARAMETER )
			{
			while( searchItem != null )
				{
				if( searchItem.isQuestion() )
					return true;

				searchItem = searchItem.attachedJustificationItem();
				}
			}

		return false;
		}

	protected boolean isUserQuestion()
		{
		return ( questionParameter_ > Constants.NO_QUESTION_PARAMETER &&
				specificationJustificationItem_ == null );
		}

	protected short questionParameter()
		{
		return questionParameter_;
		}

	protected SpecificationItem firstNewUserQuestion()
		{
		return newUserQuestion( true );
		}

	protected SpecificationItem nextNewUserQuestion()
		{
		return newUserQuestion( false );
		}


	// Protected specification methods

	protected void clearLastCheckedAssumptionLevelItemNr()
		{
		lastCheckedAssumptionLevelItemNr_ = Constants.NO_ITEM_NR;
		}

	protected void clearSpecificationStringWriteLevel( short currentWriteLevel )
		{
		if( specificationStringWriteLevel_ > currentWriteLevel )
			specificationStringWriteLevel_ = Constants.NO_WRITE_LEVEL;
		}

	protected void markAsConcludedAssumption()
		{
		isConcludedAssumption_ = true;
		assumptionLevel_ = Constants.NO_ASSUMPTION_LEVEL;
		}

	protected boolean hasNewInformation()
		{
		return ( !isOlderSentence() ||

				( ( isConcludedAssumption_ ||
				isSelfGeneratedAssumption() ) &&

				assumptionLevel_ == Constants.NO_ASSUMPTION_LEVEL &&
				specificationJustificationItem_.hasNewInformation() ) ||	// Has new justification item

				( ( isAssignment() ||
				!hasExclusiveRelationCollection() ) &&

				( hasCurrentDeactiveSentenceNr() ||
				hasCurrentArchiveSentenceNr() ||
				myWord().isContextCurrentlyUpdatedInAllWords( isPossessive_, relationContextNr_, specificationWordItem_ ) ) ) );
		}

	protected boolean hasOnlyExclusiveSpecificationSubstitutionAssumptionsWithoutDefinition()
		{
		return ( specificationJustificationItem_ != null &&
				specificationJustificationItem_.hasOnlyExclusiveSpecificationSubstitutionAssumptionsWithoutDefinition() );
		}

	protected boolean hasGeneralizationCollection()
		{
		return ( generalizationCollectionNr_ > Constants.NO_COLLECTION_NR );
		}

	protected boolean hasExclusiveGeneralizationCollection()
		{
		return ( isExclusive_ &&
				generalizationCollectionNr_ > Constants.NO_COLLECTION_NR );
		}

	protected boolean hasSpecificationCollection()
		{
		return ( specificationCollectionNr_ > Constants.NO_COLLECTION_NR );
		}

	protected boolean hasSpecificationCompoundCollection()
		{
		return ( specificationCollectionNr_ > Constants.NO_COLLECTION_NR &&
				specificationWordItem_ != null &&
				specificationWordItem_.isCompoundCollection( specificationCollectionNr_ ) );
		}

	protected boolean hasRelationCollection()
		{
		return ( relationCollectionNr() > Constants.NO_COLLECTION_NR );
		}

	protected boolean hasExclusiveRelationCollection()
		{
		return ( isExclusive_ &&
				hasRelationCollection() );
		}

	protected boolean hasGeneralizationContext()
		{
		return ( generalizationContextNr_ > Constants.NO_CONTEXT_NR );
		}

	protected boolean hasSpecificationContext()
		{
		return ( specificationContextNr_ > Constants.NO_CONTEXT_NR );
		}

	protected boolean hasRelationContext()
		{
		return ( relationContextNr_ > Constants.NO_CONTEXT_NR );
		}

	protected boolean isConditional()
		{
		return isConditional_;
		}

	protected boolean isExclusive()
		{
		return isExclusive_;
		}

	protected boolean isNegative()
		{
		return isNegative_;
		}

	protected boolean isPossessive()
		{
		return isPossessive_;
		}

	protected boolean isMatchingGeneralizationContextNr( boolean allowEmptyContextResult, int generalizationContextNr )
		{
		if( ( !isExclusive_ && generalizationContextNr == Constants.NO_CONTEXT_NR ) ||			// Empty subset
		generalizationContextNr_ == generalizationContextNr ||	// Same set

		( allowEmptyContextResult &&
		generalizationContextNr_ == Constants.NO_CONTEXT_NR ) )
			return true;

		return myWord().isContextSubsetInAllWords( generalizationContextNr_, generalizationContextNr );
		}

	protected boolean isMatchingSpecificationContextNr( boolean allowEmptyContextResult, int specificationContextNr )
		{
		if( specificationContextNr == Constants.NO_CONTEXT_NR ||			// Empty subset
		specificationContextNr_ == specificationContextNr ||	// Same set

		( allowEmptyContextResult &&
		specificationContextNr_ == Constants.NO_CONTEXT_NR ) )
			return true;

		return myWord().isContextSubsetInAllWords( specificationContextNr_, specificationContextNr );
		}

	protected boolean isMatchingRelationContextNr( boolean allowEmptyContextResult, int relationContextNr )
		{
		if( relationContextNr == Constants.NO_CONTEXT_NR ||		// Empty subset
		relationContextNr_ == relationContextNr ||		// Same set

		( allowEmptyContextResult &&
		relationContextNr_ == Constants.NO_CONTEXT_NR ) )
			return true;

		return myWord().isContextSubsetInAllWords( relationContextNr_, relationContextNr );
		}

	protected boolean isMyJustificationAnAssumption()
		{
		return ( specificationJustificationItem_ != null &&
				specificationJustificationItem_.isAssumption() );
		}

	protected boolean isSelfGeneratedAssumption()
		{
		JustificationItem searchItem = specificationJustificationItem_;

		if( !isConcludedAssumption_ )
			{
			while( searchItem != null )
				{
				if( searchItem.isAssumption() )
					return true;

				searchItem = searchItem.attachedJustificationItem();
				}
			}

		return false;
		}

	protected boolean isSelfGeneratedConclusion()
		{
		return ( isSelfGenerated() &&
				!isSelfGeneratedAssumption() );
		}

	protected boolean isUserSpecification()
		{
		return ( questionParameter_ == Constants.NO_QUESTION_PARAMETER &&
				specificationJustificationItem_ == null );
		}

	protected boolean isSelfGenerated()
		{
		return ( specificationJustificationItem_ != null );
		}

	protected boolean isSelfGeneratedPossessiveReversibleConclusion()
		{
		return ( specificationJustificationItem_ != null &&
				specificationJustificationItem_.isPossessiveReversibleConclusion() );
		}

	protected boolean foundSpecificationJustification( JustificationItem specificationJustificationItem )
		{
		JustificationItem searchItem = specificationJustificationItem_;

		if( specificationJustificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem == specificationJustificationItem )
					return true;

				searchItem = searchItem.attachedJustificationItem();
				}
			}

		return false;
		}

	protected boolean isSpecificationGeneralization()
		{
		return isSpecificationGeneralization_;
		}

	protected boolean isSpecificationStringAlreadyWritten()
		{
		return ( specificationStringWriteLevel_ > Constants.NO_WRITE_LEVEL );
		}

	protected boolean isValueSpecification()
		{
		return isValueSpecification_;
		}

	protected boolean isPrepositionIn()
		{
		return ( prepositionParameter_ == Constants.WORD_PARAMETER_PREPOSITION_IN );
		}

	protected boolean isPrepositionOf()
		{
		return ( prepositionParameter_ == Constants.WORD_PARAMETER_PREPOSITION_OF );
		}

	protected boolean isGeneralizationPropername()
		{
		return ( generalizationWordTypeNr_ == Constants.WORD_TYPE_PROPER_NAME );
		}

	protected boolean isGeneralizationNoun()
		{
		return ( generalizationWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR ||
				generalizationWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isSpecificationNumeral()
		{
		return ( specificationWordTypeNr_ == Constants.WORD_TYPE_NUMERAL );
		}

	protected boolean isSpecificationPropername()
		{
		return ( specificationWordTypeNr_ == Constants.WORD_TYPE_PROPER_NAME );
		}

	protected boolean isSpecificationNoun()
		{
		return ( specificationWordTypeNr_ == Constants.WORD_TYPE_NOUN_SINGULAR ||
				specificationWordTypeNr_ == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	protected boolean isRelationPropername()
		{
		WordItem relationContextWordItem = relationWordItem();

		return ( relationContextWordItem != null &&
				relationContextWordItem.isPropername() );
		}

	protected boolean isSpecificationCompoundCollection()
		{
		return ( specificationWordItem_ != null &&
				specificationWordItem_.isCompoundCollection( specificationCollectionNr_ ) );
		}

	protected boolean isGeneralizationPropernamePrecededByDefiniteArticle()
		{
		return myWord().isPropernamePrecededByDefiniteArticle();
		}

	protected boolean isSpecificationPropernamePrecededByDefiniteArticle()
		{
		return ( specificationWordItem_ != null &&
				specificationWordItem_.isPropernamePrecededByDefiniteArticle() );
		}

	protected boolean isRelationPropernamePrecededByDefiniteArticle()
		{
		WordItem relationContextWordItem = relationWordItem();

		return ( relationContextWordItem != null &&
				relationContextWordItem.isPropernamePrecededByDefiniteArticle() );
		}

	protected boolean isRelatedQuestion( boolean isExclusive, int specificationCollectionNr, int subsetRelationContextNr )
		{
		return ( isExclusive_ == isExclusive &&
				specificationCollectionNr_ == specificationCollectionNr &&
				isMatchingRelationContextNr( true, subsetRelationContextNr ) );
		}

	protected boolean isRelatedSpecification( int generalizationContextNr, int specificationContextNr )
		{
		return ( isMatchingGeneralizationContextNr( true, generalizationContextNr ) &&
				isMatchingSpecificationContextNr( true, specificationContextNr ) );
		}

	protected boolean isRelatedSpecification( boolean isNegative, boolean isPossessive, short generalizationWordTypeNr )
		{
		return ( isNegative_ == isNegative &&
				isPossessive_ == isPossessive &&
				generalizationWordTypeNr_ == generalizationWordTypeNr );
		}

	protected boolean isRelatedSpecification( boolean isExclusive, boolean isNegative, boolean isPossessive, WordItem specificationWordItem )
		{
		return ( isExclusive_ == isExclusive &&
				isNegative_ == isNegative &&
				isPossessive_ == isPossessive &&
				relationContextNr_ > Constants.NO_CONTEXT_NR &&
				specificationWordItem != null &&
				specificationWordItem_ == specificationWordItem );
		}

	protected boolean isRelatedSpecification( boolean isNegative, boolean isPossessive, int relationContextNr, WordItem specificationWordItem )
		{
		return ( isNegative_ == isNegative &&
				isPossessive_ == isPossessive &&
				specificationWordItem != null &&
				specificationWordItem_ == specificationWordItem &&
				isMatchingRelationContextNr( true, relationContextNr ) );
		}

	protected boolean isRelatedSpecification( boolean isPossessive, boolean _isSelfGenerated, int generalizationCollectionNr, int specificationCollectionNr, int compoundSpecificationCollectionNr, WordItem specificationWordItem )
		{
		return ( isPossessive_ == isPossessive &&
				isSelfGenerated() == _isSelfGenerated &&
				generalizationCollectionNr_ == generalizationCollectionNr &&

				( specificationCollectionNr_ == specificationCollectionNr ||

				( compoundSpecificationCollectionNr > Constants.NO_COLLECTION_NR &&
				specificationCollectionNr_ == compoundSpecificationCollectionNr ) ) &&

				( hasSpecificationCollection() ||
				specificationWordItem_ == specificationWordItem ) );
		}

	protected boolean isRelatedSpecification( boolean checkRelationContext, boolean ignoreExclusive, boolean isExclusive, boolean isPossessive, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr )
		{
		return ( ( ignoreExclusive ||
				isExclusive_ == isExclusive ) &&

				isPossessive_ == isPossessive &&
				specificationCollectionNr_ == specificationCollectionNr &&
				generalizationContextNr_ == generalizationContextNr &&		// Don't find a matching contexts when exclusive is tested
				specificationContextNr_ == specificationContextNr &&

				( !checkRelationContext ||
				relationContextNr_ == relationContextNr ) );
		}

	protected boolean isRelatedSpecification( boolean isNegative, boolean isPossessive, int generalizationCollectionNr, int specificationCollectionNr, int relationContextNr )
		{
		return ( isNegative_ == isNegative &&
				isPossessive_ == isPossessive &&
				hasSpecificationCollection() &&
				generalizationCollectionNr_ == generalizationCollectionNr &&
				specificationCollectionNr_ == specificationCollectionNr &&
				isMatchingRelationContextNr( true, relationContextNr ) );
		}

	protected boolean isRelatedSpecification( boolean isExclusive, boolean isPossessive, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr )
		{
		return ( isExclusive_ == isExclusive &&
				isPossessive_ == isPossessive &&
				hasSpecificationCollection() &&
				generalizationCollectionNr_ == generalizationCollectionNr &&
				specificationCollectionNr_ == specificationCollectionNr &&
				generalizationContextNr_ == generalizationContextNr &&		// Don't find a matching contexts when exclusive is tested
				specificationContextNr_ == specificationContextNr &&
				relationContextNr_ == relationContextNr );
		}

	protected boolean isRelatedSpecification( boolean isExclusive, boolean isPossessive, int generalizationCollectionNr, int specificationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, WordItem specificationWordItem )
		{
		return ( isExclusive_ == isExclusive &&
				isPossessive_ == isPossessive &&
				generalizationCollectionNr_ == generalizationCollectionNr &&
				specificationCollectionNr_ == specificationCollectionNr &&
				specificationWordItem != null &&
				specificationWordItem_ == specificationWordItem &&
				generalizationContextNr_ == generalizationContextNr &&		// Don't find a matching contexts when exclusive is tested
				specificationContextNr_ == specificationContextNr &&
				relationContextNr_ == relationContextNr );
		}

	protected boolean foundJustificationOfSameType( short justificationTypeNr, SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		int definitionSpecificationCollectionNr = ( definitionSpecificationItem == null ? Constants.NO_COLLECTION_NR : definitionSpecificationItem.specificationCollectionNr() );
		int specificSpecificationCollectionNr = ( specificSpecificationItem == null ? Constants.NO_COLLECTION_NR : specificSpecificationItem.specificationCollectionNr() );
		JustificationItem searchItem = specificationJustificationItem_;

		while( searchItem != null )
			{
			if( searchItem.justificationTypeNr() == justificationTypeNr &&

			( !searchItem.hasDefinitionSpecification() ||
			!searchItem.hasSpecificSpecification() ||

			( searchItem.definitionSpecificationItem().specificationCollectionNr() == definitionSpecificationCollectionNr &&
			searchItem.specificSpecificationItem().specificationCollectionNr() == specificSpecificationCollectionNr ) ) )
				return true;

			searchItem = searchItem.attachedJustificationItem();
			}

		return false;
		}

	protected short prepositionParameter()
		{
		return prepositionParameter_;
		}

	protected short generalizationWordTypeNr()
		{
		return generalizationWordTypeNr_;
		}

	protected short specificationWordTypeNr()
		{
		return specificationWordTypeNr_;
		}

	protected short relationWordTypeNr()
		{
		return myWord().contextWordTypeNrInAllWords( relationContextNr_ );
		}

	protected short highestAttachedJustificationOrderNr( short justificationTypeNr )
		{
		short highestOrderNr = Constants.NO_ORDER_NR;
		JustificationItem searchItem = specificationJustificationItem_;

		while( searchItem != null )
			{
			if( searchItem.orderNr > highestOrderNr &&
			searchItem.justificationTypeNr() == justificationTypeNr )
				highestOrderNr = searchItem.orderNr;

			searchItem = searchItem.attachedJustificationItem();
			}

		return highestOrderNr;
		}

	protected int generalizationCollectionNr()
		{
		return generalizationCollectionNr_;
		}

	protected int specificationCollectionNr()
		{
		return specificationCollectionNr_;
		}

	protected int relationCollectionNr()
		{
		return myWord().collectionNrInAllWords( relationContextNr_ );
		}

	protected int generalizationContextNr()
		{
		return generalizationContextNr_;
		}

	protected int specificationContextNr()
		{
		return specificationContextNr_;
		}

	protected int relationContextNr()
		{
		return relationContextNr_;
		}

	protected int nContextRelations()
		{
		return nContextRelations_;
		}

	protected byte changeJustificationItem( JustificationItem replacingJustificationItem )
		{
		JustificationItem searchItem = specificationJustificationItem_;

		if( replacingJustificationItem != null )
			{
			if( replacingJustificationItem.isActiveItem() )
				{
				if( hasCurrentCreationSentenceNr() )
					{
					while( searchItem != null )
						{
						if( searchItem == replacingJustificationItem )
							return setErrorInItem( 1, null, null, "The given replacing justification item is already one of my attached justification items" );

						searchItem = searchItem.attachedJustificationItem();
						}

					specificationJustificationItem_ = replacingJustificationItem;
					}
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given replacing justification item isn't active" );
			}
		else
			return setErrorInItem( 1, null, null, "The given replacing justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte collectSpecificationItem( boolean isCollectGeneralization, boolean isCollectSpecification, boolean isExclusiveGeneralization, int collectionNr )
		{
		if( isCollectGeneralization ||
		isCollectSpecification )
			{
			if( collectionNr > Constants.NO_COLLECTION_NR )
				{
				if( hasCurrentCreationSentenceNr() )
					{
					if( isCollectGeneralization )
						{
						if( !hasGeneralizationCollection() )
							{
							if( isExclusiveGeneralization )
								isExclusive_ = true;

							generalizationCollectionNr_ = collectionNr;
							}
						else
							return setErrorInItem( 1, null, null, "The generalization is already collected" );
						}

					if( isCollectSpecification )
						{
						if( !hasSpecificationCollection() )
							specificationCollectionNr_ = collectionNr;
						else
							return setErrorInItem( 1, null, null, "The specification is already collected" );
						}
					}
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given collection number is undefined" );
			}
		else
			return setErrorInItem( 1, null, null, "Nothing to do. All given collection indicators are undefined" );

		return CommonVariables.result;
		}

	protected byte markSpecificationStringAsWritten()
		{
		if( CommonVariables.currentWriteLevel < Constants.MAX_LEVEL )
			{
			if( specificationStringWriteLevel_ == Constants.NO_WRITE_LEVEL )
				specificationStringWriteLevel_ = ++CommonVariables.currentWriteLevel;
			else
				return setErrorInItem( 1, null, itemString(), "My write level is already assigned" );
			}
		else
			return setSystemErrorInItem( 1, null, itemString(), "Current write word level overflow" );

		return CommonVariables.result;
		}

	protected SpecificationResultType getAssumptionLevel()
		{
		return calculateAssumptionLevel( false );
		}

	protected SpecificationResultType recalculateAssumptionLevel()
		{
		nAssignmentLevelRecalculations_ = 0;
		return calculateAssumptionLevel( true );
		}

	protected JustificationItem specificationJustificationItem()
		{
		return specificationJustificationItem_;
		}
/*
	protected JustificationItem updatedSpecificationJustificationItem()
		{
		return ( specificationJustificationItem_ == null ? null : specificationJustificationItem_.updatedJustificationItem() );
		}
*/
	protected JustificationItem foundDefinitionSpecificationJustificationItem( SpecificationItem definitionSpecificationItem )
		{
		JustificationItem searchItem = specificationJustificationItem_;

		if( definitionSpecificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.definitionSpecificationItem() == definitionSpecificationItem )
					return searchItem;

				searchItem = searchItem.attachedJustificationItem();
				}
			}

		return null;
		}

	protected JustificationItem foundSpecificSpecificationJustificationItem( WordItem generalizationWordItem )
		{
		SpecificationItem specificSpecificationItem;
		JustificationItem searchItem = specificationJustificationItem_;

		if( generalizationWordItem != null &&
		specificationCollectionNr_ > Constants.NO_COLLECTION_NR )
			{
			while( searchItem != null )
				{
				if( searchItem.isOppositePossessiveConditionalSpecificationAssumption() &&
				( specificSpecificationItem = searchItem.specificSpecificationItem() ) != null )
					{
					if( specificSpecificationItem.isPossessive() == isPossessive_ &&
					specificSpecificationItem.specificationCollectionNr() == specificationCollectionNr_ &&
					specificSpecificationItem.specificationWordItem() != specificationWordItem_ &&
					specificSpecificationItem.generalizationWordItem() == generalizationWordItem )
						return searchItem;
					}

				searchItem = searchItem.attachedJustificationItem();
				}
			}

		return null;
		}

	protected SpecificationItem nextSpecificationItem()
		{
		return (SpecificationItem)nextItem ;
		}

	protected SpecificationItem nextAssignmentOrSpecificationItem()
		{
		return ( isAssignment() ? nextAssignmentItemWithCurrentLevel() : nextSpecificationItem() );
		}

	protected SpecificationItem updatedSpecificationItem()
		{
		SpecificationItem updatedSpecificationItem;
		SpecificationItem searchItem = this;

		while( ( updatedSpecificationItem = searchItem.replacingSpecificationItem ) != null )
			searchItem = updatedSpecificationItem;

		return searchItem;
		}

	protected SpecificationItem getSpecificationItem( boolean includeAnsweredQuestions, boolean includeThisItem, boolean isQuestion )
		{
		SpecificationItem searchItem = ( includeThisItem ? this : nextSpecificationItem() );

		while( searchItem != null )
			{
			// Skip replaced specifications
			if( ( searchItem.replacingSpecificationItem == null ||
			// Skip replaced specifications that are undone
			searchItem.replacingSpecificationItem.creationSentenceNr() > CommonVariables.currentSentenceNr ) &&

			// Skip answered questions
			( includeAnsweredQuestions ||
			!searchItem.isAnsweredQuestion() ) &&

			searchItem.isQuestion() == isQuestion )
				return searchItem;

			searchItem = searchItem.nextSpecificationItem();
			}

		return null;
		}

	protected SpecificationItem getSpecificationItem( boolean includeAnsweredQuestions, boolean includeThisItem, short questionParameter )
		{
		SpecificationItem searchItem = ( includeThisItem ? this : nextSpecificationItem() );

		while( searchItem != null )
			{
			// Skip replaced specifications
			if( ( searchItem.replacingSpecificationItem == null ||
			// Skip replaced specifications that are undone
			searchItem.replacingSpecificationItem.creationSentenceNr() > CommonVariables.currentSentenceNr ) &&

			// Skip answered questions
			( includeAnsweredQuestions ||
			!searchItem.isAnsweredQuestion() ) &&

			searchItem.questionParameter() == questionParameter )
				return searchItem;

			searchItem = searchItem.nextSpecificationItem();
			}

		return null;
		}

	protected SpecificationItem nextSelectedSpecificationItem( boolean includeAnsweredQuestions )
		{
		return ( isAssignment() ? getAssignmentItem( includeAnsweredQuestions, false, isQuestion() ) : getSpecificationItem( includeAnsweredQuestions, false, isQuestion() ) );
		}

	protected SpecificationItem nextSpecificationItemButNotAQuestion()
		{
		return ( isAssignment() ? getAssignmentItem( false, false, false ) : getSpecificationItem( false, false, false ) );
		}

	protected SpecificationItem nextSpecificationItemWithSameQuestionParameter( boolean includeAnsweredQuestions )
		{
		return ( isAssignment() ? getAssignmentItem( includeAnsweredQuestions, false, questionParameter_ ) : getSpecificationItem( includeAnsweredQuestions, false, questionParameter_ ) );
		}

	protected WordItem generalizationWordItem()
		{
		return myWord();
		}

	protected WordItem specificationWordItem()
		{
		return specificationWordItem_;
		}

	protected WordItem relationWordItem()
		{
		return relationWordItem( null );
		}

	protected WordItem relationWordItem( WordItem previousWordItem )
		{
		return myWord().contextWordInAllWords( isPossessive_, relationContextNr_, specificationWordItem_, previousWordItem );
		}

	protected String specificationString()
		{
		return specificationString_;
		}

	protected String activeGeneralizationWordTypeString()
		{
		return myWord().activeWordTypeString( generalizationWordTypeNr_ );
		}
	};

/*************************************************************************
 *
 *	"All he does is just and good,
 *	and all his commandments are trustworthy." (Psalm 111:7)
 *
 *************************************************************************/
