/*
 *	Class:			JustificationItem
 *	Parent class:	Item
 *	Purpose:		To store info need to write the justification reports
 *					for the self-generated knowledge
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

class JustificationItem extends Item
	{
	// Private loadable variables

	private short justificationTypeNr_;

	private JustificationItem attachedJustificationItem_;

	private SpecificationItem definitionSpecificationItem_;
	private SpecificationItem anotherDefinitionSpecificationItem_;
	private SpecificationItem specificSpecificationItem_;


	// Protected constructible variables

	protected short orderNr;

	protected JustificationItem replacingJustificationItem;


	// Private methods

	private boolean isSameJustificationType( JustificationItem referenceJustificationItem )
		{
		return ( referenceJustificationItem != null &&
				orderNr == referenceJustificationItem.orderNr &&
				justificationTypeNr_ == referenceJustificationItem.justificationTypeNr() );
		}


	// Constructor

	protected JustificationItem( short justificationTypeNr, short _orderNr, int originalSentenceNr, JustificationItem attachedJustificationItem, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem, List myList, WordItem myWord )
		{
		initializeItemVariables( originalSentenceNr, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		justificationTypeNr_ = justificationTypeNr;

		attachedJustificationItem_ = attachedJustificationItem;

		definitionSpecificationItem_ = definitionSpecificationItem;
		anotherDefinitionSpecificationItem_ = anotherDefinitionSpecificationItem;
		specificSpecificationItem_ = specificSpecificationItem;

		// Protected constructible variables

		orderNr = _orderNr;

		replacingJustificationItem = null;
		}


	// Protected virtual methods

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		return ( ( attachedJustificationItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : attachedJustificationItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : attachedJustificationItem_.itemNr() == queryItemNr ) ) ||

				( definitionSpecificationItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : definitionSpecificationItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : definitionSpecificationItem_.itemNr() == queryItemNr ) ) ||

				( anotherDefinitionSpecificationItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : anotherDefinitionSpecificationItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : anotherDefinitionSpecificationItem_.itemNr() == queryItemNr ) ) ||

				( specificSpecificationItem_ == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : specificSpecificationItem_.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : specificSpecificationItem_.itemNr() == queryItemNr ) ) ||

				( replacingJustificationItem == null ? false :
					( querySentenceNr == Constants.NO_SENTENCE_NR ? true : replacingJustificationItem.creationSentenceNr() == querySentenceNr ) &&
					( queryItemNr == Constants.NO_ITEM_NR ? true : replacingJustificationItem.itemNr() == queryItemNr ) ) );
		}

	protected byte checkForUsage()
		{
		return myWord().checkJustificationForUsageInWord( this );
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		baseToStringBuffer( queryWordTypeNr );

		switch( justificationTypeNr_ )
			{
			case Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_GENERALIZATION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isGeneralizationAssumptionByGeneralization" );
				break;

			case Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_SPECIFICATION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isGeneralizationAssumptionBySpecification" );
				break;

			case Constants.JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isOppositePossessiveConditionalSpecificationAssumption" );
				break;

			case Constants.JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isBackFiredPossessiveConditionalSpecificationAssumption" );
				break;

			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSpecificationSubstitutionAssumption" );
				break;

			case Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isExclusiveSpecificationSubstitutionAssumption" );
				break;

			case Constants.JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSuggestiveQuestionAssumption" );
				break;

			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSpecificationGeneralizationConclusion" );
				break;

			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSpecificationSubstitutionConclusion" );
				break;

			case Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isPossessiveReversibleConclusion" );
				break;

			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSpecificationSubstitutionQuestion" );
				break;

			default:
				CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "justificationType:" + justificationTypeNr_ );
			}

		CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "orderNr:" + orderNr );

		if( definitionSpecificationItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "definitionSpecification" + Constants.QUERY_REF_ITEM_START_CHAR + definitionSpecificationItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + definitionSpecificationItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );
			}

		if( anotherDefinitionSpecificationItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "anotherDefinitionSpecification" + Constants.QUERY_REF_ITEM_START_CHAR + anotherDefinitionSpecificationItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + anotherDefinitionSpecificationItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );
			}

		if( specificSpecificationItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "specificSpecification" + Constants.QUERY_REF_ITEM_START_CHAR + specificSpecificationItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + specificSpecificationItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );
			}

		if( attachedJustificationItem_ != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "attachedJustification" + Constants.QUERY_REF_ITEM_START_CHAR + attachedJustificationItem_.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + attachedJustificationItem_.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );
			}

		if( replacingJustificationItem != null )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "replacingJustification" + Constants.QUERY_REF_ITEM_START_CHAR + replacingJustificationItem.creationSentenceNr() + Constants.QUERY_SEPARATOR_CHAR + replacingJustificationItem.itemNr() + Constants.QUERY_REF_ITEM_END_CHAR );
			}

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean hasDefinitionSpecification()
		{
		return ( definitionSpecificationItem_ != null );
		}

	protected boolean hasSpecificSpecification()
		{
		return ( specificSpecificationItem_ != null );
		}

	protected boolean hasNewInformation()
		{
		JustificationItem searchItem = this;

		while( searchItem != null )
			{
			if( searchItem.hasCurrentActiveSentenceNr() )
				return true;

			searchItem = searchItem.attachedJustificationItem();
			}

		return false;

		// Recursive alternative:
/*		return ( hasCurrentActiveSentenceNr() ||

				( attachedJustificationItem_ != null &&
				attachedJustificationItem_.hasNewInformation() ) );
*/		}

	protected boolean hasOnlyExclusiveSpecificationSubstitutionAssumptionsWithoutDefinition()
		{
		JustificationItem searchItem = this;

		while( searchItem != null )
			{
			if( searchItem.hasDefinitionSpecification() ||
			searchItem.justificationTypeNr() != Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION )
				return false;

			searchItem = searchItem.attachedJustificationItem();
			}

		return true;
		}

	protected boolean isAssumption()
		{
		return ( justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_GENERALIZATION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_SPECIFICATION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION );
		}
/*
	protected boolean isConclusion()
		{
		return ( justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION ||
				justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION );
		}
*/
	protected boolean isQuestion()
		{
		return ( justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_QUESTION );
		}

	protected boolean isOppositePossessiveConditionalSpecificationAssumption()
		{
		return ( justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION );
		}

	protected boolean isPossessiveReversibleConclusion()
		{
		return ( justificationTypeNr_ == Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION );
		}

	protected short assumptionGrade()
		{
		switch( justificationTypeNr_ )
			{
			case Constants.JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION:
			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION:
			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_GENERALIZATION_SUBSTITUTION_CONCLUSION:
			case Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_CONCLUSION:
			case Constants.JUSTIFICATION_TYPE_POSSESSIVE_REVERSIBLE_CONCLUSION:
				return 0;

			case Constants.JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION:
				return 2;

			default:
				return 1;
			}
		}

	protected short justificationTypeNr()
		{
		return justificationTypeNr_;
		}

	protected int nJustificationContextRelations( int relationContextNr, int nSpecificationRelationWords )
		{
		if( relationContextNr > Constants.NO_CONTEXT_NR )
			{
			if( specificSpecificationItem_ != null &&
			specificSpecificationItem_.hasRelationContext() &&
			myWord().isContextSimilarInAllWords( specificSpecificationItem_.relationContextNr(), relationContextNr ) )
				return nSpecificationRelationWords;

			return 1;
			}

		return 0;
		}

	protected byte attachJustificationItem( JustificationItem attachedJustificationItem, SpecificationItem mySpecificationItem )
		{
		boolean isMySpecification = false;
		JustificationItem searchItem;

		if( attachedJustificationItem != null )
			{
			if( attachedJustificationItem != this )
				{
				if( mySpecificationItem != null )
					{
					if( hasCurrentCreationSentenceNr() )
						{
						if( attachedJustificationItem.isActiveItem() )
							{
							if( attachedJustificationItem_ == null )
								{
								if( ( searchItem = mySpecificationItem.specificationJustificationItem() ) != null )
									{
									while( searchItem != null )
										{
										if( searchItem == this )
											isMySpecification = true;

										if( searchItem == attachedJustificationItem )
											return setErrorInItem( 1, null, null, "The given attached justification item is already one of the attached justification items of my specification item" );

										searchItem = searchItem.attachedJustificationItem();
										}

									if( isMySpecification )
										attachedJustificationItem_ = attachedJustificationItem;		// Add attached justification item
									else
										return setErrorInItem( 1, null, null, "The given my specification item is not my specification item" );
									}
								else
									return setErrorInItem( 1, null, null, "The given my specification item has no specification justification item" );
								}
							else
								return setErrorInItem( 1, null, null, "I already have an attached justification item" );
							}
						else
							return setErrorInItem( 1, null, null, "The given attached justification item isn't active" );
						}
					else
						return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
					}
				else
					return setErrorInItem( 1, null, null, "The given my specification item is undefined" );
				}
			else
				return setErrorInItem( 1, null, null, "The given attached justification item is the same justification item as me" );
			}
		else
			return setErrorInItem( 1, null, null, "The given attached justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte changeAttachedJustificationItem( JustificationItem replacingAttachedJustificationItem )
		{
		boolean isExistingJustification = false;
		JustificationItem searchItem = this;

		if( replacingAttachedJustificationItem != null )
			{
			if( replacingAttachedJustificationItem.isActiveItem() )
				{
				if( hasCurrentCreationSentenceNr() )
					{
					while( searchItem != null &&
					!isExistingJustification )
						{
						// Check me and my attached justifications against the replacing justification to avoid loops
						if( searchItem == replacingAttachedJustificationItem )
							isExistingJustification = true;
						else
							searchItem = searchItem.attachedJustificationItem();
						}

					searchItem = replacingAttachedJustificationItem;

					while( searchItem != null &&
					!isExistingJustification )
						{
						// Check replacing justification against this justification to avoid loops
						if( searchItem == this )
							isExistingJustification = true;
						else
							searchItem = searchItem.attachedJustificationItem();
						}

					attachedJustificationItem_ = ( isExistingJustification ? null : replacingAttachedJustificationItem );
					}
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given replacing attached justification item isn't active" );
			}
		else
			return setErrorInItem( 1, null, null, "The given replacing attached justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte changeDefinitionSpecificationItem( SpecificationItem replacingSpecificationItem )
		{
		if( replacingSpecificationItem != null )
			{
			if( replacingSpecificationItem.replacingSpecificationItem == null )
				{
				if( hasCurrentCreationSentenceNr() )
					definitionSpecificationItem_ = replacingSpecificationItem;
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given replacing specification item has a replacing specification item" );
			}
		else
			return setErrorInItem( 1, null, null, "The given replacing specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte changeAnotherDefinitionSpecificationItem( SpecificationItem replacingSpecificationItem )
		{
		if( replacingSpecificationItem != null )
			{
			if( replacingSpecificationItem.replacingSpecificationItem == null )
				{
				if( hasCurrentCreationSentenceNr() )
					anotherDefinitionSpecificationItem_ = replacingSpecificationItem;
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given replacing specification item has a replacing specification item" );
			}
		else
			return setErrorInItem( 1, null, null, "The given replacing specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte changeSpecificSpecificationItem( SpecificationItem replacingSpecificationItem )
		{
		if( replacingSpecificationItem != null )
			{
			if( replacingSpecificationItem.replacingSpecificationItem == null )
				{
				if( hasCurrentCreationSentenceNr() )
					specificSpecificationItem_ = replacingSpecificationItem;
				else
					return setErrorInItem( 1, null, null, "It isn't allowed to change an older item afterwards" );
				}
			else
				return setErrorInItem( 1, null, null, "The given replacing specification item has a replacing specification item" );
			}
		else
			return setErrorInItem( 1, null, null, "The given replacing specification item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType getCombinedAssumptionLevel()
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		int combinedAssumptionLevel = Constants.NO_ASSUMPTION_LEVEL;

		if( definitionSpecificationItem_ != null &&
		( specificationResult = definitionSpecificationItem_.getAssumptionLevel() ).result == Constants.RESULT_OK )
			combinedAssumptionLevel = specificationResult.assumptionLevel;

		if( CommonVariables.result == Constants.RESULT_OK &&
		anotherDefinitionSpecificationItem_ != null &&
		( specificationResult = anotherDefinitionSpecificationItem_.getAssumptionLevel() ).result == Constants.RESULT_OK )
			combinedAssumptionLevel += specificationResult.assumptionLevel;

		if( CommonVariables.result == Constants.RESULT_OK &&
		specificSpecificationItem_ != null &&
		( specificationResult = specificSpecificationItem_.getAssumptionLevel() ).result == Constants.RESULT_OK )
			combinedAssumptionLevel += specificationResult.assumptionLevel;

		if( combinedAssumptionLevel < Constants.MAX_LEVEL )
			specificationResult.combinedAssumptionLevel = (short)combinedAssumptionLevel;
		else
			specificationResult.result = setSystemErrorInItem( 1, null, null, "Assumption level overflow" );

		return specificationResult;
		}

	protected JustificationItem attachedJustificationItem()
		{
		return attachedJustificationItem_;
		}

	protected JustificationItem predecessorOfOldAttachedJustificationItem( JustificationItem oldJustificationItem )
		{
		JustificationItem searchItem = this;

		while( searchItem != null )
			{
			if( searchItem.attachedJustificationItem() == oldJustificationItem )
				return searchItem;

			searchItem = searchItem.attachedJustificationItem();
			}

		return null;

		// Recursive alternative:
		// return ( attachedJustificationItem_ == null ? null : attachedJustificationItem_ == oldJustificationItem ? this : attachedJustificationItem_.predecessorOfOldAttachedJustificationItem( oldJustificationItem ) );
		}

	protected JustificationItem nextJustificationItem()
		{
		return (JustificationItem)nextItem;
		}

	protected JustificationItem nextJustificationItemWithSameTypeAndOrderNr()
		{
		JustificationItem searchItem = attachedJustificationItem_;

		while( searchItem != null )
			{
			if( isSameJustificationType( searchItem ) )
				return searchItem;

			searchItem = searchItem.attachedJustificationItem();
			}

		return null;
		}

	protected JustificationItem nextJustificationItemWithDifferentTypeOrOrderNr( JustificationItem firstJustificationItem )
		{
		JustificationItem usedTypeJustificationItem;
		JustificationItem nextTypeJustificationItem = attachedJustificationItem_;

		if( firstJustificationItem != null )
			{
			do	{
				// Find next occurrence with different type
				while( nextTypeJustificationItem != null &&
				isSameJustificationType( nextTypeJustificationItem ) )
					nextTypeJustificationItem = nextTypeJustificationItem.attachedJustificationItem();

				if( nextTypeJustificationItem != null )
					{
					// Check if different type is already used
					usedTypeJustificationItem = firstJustificationItem;

					while( usedTypeJustificationItem != null &&
					!usedTypeJustificationItem.isSameJustificationType( nextTypeJustificationItem ) )
						usedTypeJustificationItem = usedTypeJustificationItem.attachedJustificationItem();

					if( usedTypeJustificationItem == nextTypeJustificationItem )
						return nextTypeJustificationItem;
					}
				}
			while( nextTypeJustificationItem != null &&
			( nextTypeJustificationItem = nextTypeJustificationItem.attachedJustificationItem() ) != null );
			}

		return null;
		}

	protected JustificationItem definitionSpecificationWithoutRelationContextJustificationItem( SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		if( definitionSpecificationItem != null )
			{
			if( definitionSpecificationItem_ != null &&
			!definitionSpecificationItem_.hasRelationContext() &&
			specificSpecificationItem_ == specificSpecificationItem &&
			definitionSpecificationItem_.specificationWordItem() == definitionSpecificationItem.specificationWordItem() )
				return this;

			if( attachedJustificationItem_ != null )
				return attachedJustificationItem_.definitionSpecificationWithoutRelationContextJustificationItem( definitionSpecificationItem, specificSpecificationItem );
			}

		return null;
		}

	protected JustificationItem foundSpecificSpecificationQuestion()
		{
		if( specificSpecificationItem_ != null &&
		specificSpecificationItem_.isQuestion() )
			return this;

		if( attachedJustificationItem_ != null )
			return attachedJustificationItem_.foundSpecificSpecificationQuestion();

		return null;
		}

	protected JustificationItem selfGeneratedSpecificSpecificationJustificationItem( SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		if( specificSpecificationItem != null )
			{
			if( definitionSpecificationItem_ == definitionSpecificationItem &&
			specificSpecificationItem_.isSelfGenerated() &&
			specificSpecificationItem_.specificationWordItem() == specificSpecificationItem.specificationWordItem() )
				return this;

			if( attachedJustificationItem_ != null )
				return attachedJustificationItem_.selfGeneratedSpecificSpecificationJustificationItem( definitionSpecificationItem, specificSpecificationItem );
			}

		return null;
		}
/*
	protected JustificationItem updatedJustificationItem()
		{
		JustificationItem searchItem = this;

		while( searchItem.replacingJustificationItem != null )
			searchItem = searchItem.replacingJustificationItem;

		return searchItem;
		}
*/
	protected SpecificationItem definitionSpecificationItem()
		{
		return definitionSpecificationItem_;
		}

	protected SpecificationItem anotherDefinitionSpecificationItem()
		{
		return anotherDefinitionSpecificationItem_;
		}

	protected SpecificationItem specificSpecificationItem()
		{
		return specificSpecificationItem_;
		}
	};

/*************************************************************************
 *
 *	"The voice of the Lord is powerful;
 *	the voice of the Lord is majestic." (Psalm 29:4)
 *
 *************************************************************************/
