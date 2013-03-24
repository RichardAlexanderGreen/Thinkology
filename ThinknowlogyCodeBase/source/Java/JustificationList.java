/*
 *	Class:			JustificationList
 *	Parent class:	List
 *	Purpose:		To store justification items for the
 *					self-generated knowledge
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

class JustificationList extends List
	{
	// Private constructible variables

	private boolean needToRecalculateAssumptionsAfterwards_;


	// Private methods

	private byte updateArchivedReplacingJustificationItems( JustificationItem updateJustificationItem )
		{
		JustificationItem replacingJustificationItem;
		JustificationItem searchItem = firstArchiveJustificationItem();

		if( updateJustificationItem != null )
			{
			while( searchItem != null )
				{
				// Update the justification items that are filtered by getAssignmentItem
				// variable: replacingSpecificationItem
				if( searchItem.replacingJustificationItem == updateJustificationItem &&
				( replacingJustificationItem = updateJustificationItem.replacingJustificationItem ) != null )
					searchItem.replacingJustificationItem = replacingJustificationItem;

				searchItem = searchItem.nextJustificationItem();
				}
			}
		else
			return setError( 1, null, "The given update justification item is undefined" );

		return CommonVariables.result;
		}

	private byte checkJustificationItemForUsage( boolean isActiveItem, JustificationItem unusedJustificationItem )
		{
		JustificationItem searchItem = firstJustificationItem( isActiveItem );

		if( unusedJustificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.attachedJustificationItem() == unusedJustificationItem )
					return setError( 1, null, "The attached justification item is still in use" );

				if( searchItem.replacingJustificationItem == unusedJustificationItem )
					{
					if( searchItem.creationSentenceNr() < unusedJustificationItem.creationSentenceNr() ||
					searchItem.creationSentenceNr() < CommonVariables.myFirstSentenceNr )
						searchItem.replacingJustificationItem = null;
					else
						return setError( 1, null, "The replacing justification item is still in use" );
					}

				searchItem = searchItem.nextJustificationItem();
				}
			}
		else
			return setError( 1, null, "The given unused justification item is undefined" );

		return CommonVariables.result;
		}

	private byte checkSpecificationItemForUsage( boolean isActiveItem, SpecificationItem unusedSpecificationItem )
		{
		JustificationItem searchItem = firstJustificationItem( isActiveItem );

		if( unusedSpecificationItem != null )
			{
			while( searchItem != null )
				{
				if( searchItem.definitionSpecificationItem() == unusedSpecificationItem )
					return setError( 1, null, "The definition specification item is still in use" );

				if( searchItem.specificSpecificationItem() == unusedSpecificationItem )
					return setError( 1, null, "The specific specification item is still in use" );

				searchItem = searchItem.nextJustificationItem();
				}
			}
		else
			return setError( 1, null, "The given unused specification item is undefined" );

		return CommonVariables.result;
		}

	private JustificationItem firstActiveJustificationItem()
		{
		return (JustificationItem)firstActiveItem();
		}

	private JustificationItem firstArchiveJustificationItem()
		{
		return (JustificationItem)firstArchiveItem();
		}

	private JustificationItem firstJustificationItem( boolean isActiveItem )
		{
		return ( isActiveItem ? firstActiveJustificationItem() : firstArchiveJustificationItem() );
		}

	private JustificationItem foundJustificationItem( boolean isActiveItem, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		JustificationItem searchItem = firstJustificationItem( isActiveItem );

		while( searchItem != null )
			{
			if( searchItem.definitionSpecificationItem() == definitionSpecificationItem &&
			searchItem.anotherDefinitionSpecificationItem() == anotherDefinitionSpecificationItem &&
			searchItem.specificSpecificationItem() == specificSpecificationItem )
				return searchItem;

			searchItem = searchItem.nextJustificationItem();
			}

		return null;
		}

	private JustificationItem foundSpecificSpecificationJustificationItem( boolean hasDefinitionSpecification, boolean isPossessive, WordItem specificSpecificationWordItem, WordItem specificRelationWordItem )
		{
		JustificationItem searchItem = firstActiveJustificationItem();
		SpecificationItem specificSpecificationItem;

		if( specificSpecificationWordItem != null &&
		specificRelationWordItem != null )
			{
			while( searchItem != null )
				{
				if( hasDefinitionSpecification == searchItem.hasDefinitionSpecification() &&
				( specificSpecificationItem = searchItem.specificSpecificationItem() ) != null )
					{
					if( specificSpecificationItem.isSelfGeneratedAssumption() &&
					specificSpecificationItem.isPossessive() == isPossessive &&
					specificSpecificationItem.specificationWordItem() == specificSpecificationWordItem &&
					specificSpecificationItem.relationWordItem() == specificRelationWordItem )
						return searchItem;
					}

				searchItem = searchItem.nextJustificationItem();
				}
			}

		return null;
		}


	// Constructor

	protected JustificationList( WordItem myWord )
		{
		needToRecalculateAssumptionsAfterwards_ = false;
		initializeListVariables( Constants.WORD_JUSTIFICATION_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected boolean needToRecalculateAssumptionsAfterwards()
		{
		return needToRecalculateAssumptionsAfterwards_;
		}

	protected JustificationResultType addJustificationItem( boolean forceToCreateJustification, boolean isActiveItem, short justificationTypeNr, short orderNr, int originalSentenceNr, JustificationItem attachedJustificationItem, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		SpecificationItem updatedDefinitionSpecificationItem = ( definitionSpecificationItem == null ? null : definitionSpecificationItem.updatedSpecificationItem() );
		SpecificationItem updatedAnotherDefinitionSpecificationItem = ( anotherDefinitionSpecificationItem == null ? null : anotherDefinitionSpecificationItem.updatedSpecificationItem() );
		SpecificationItem updatedSpecificSpecificationItem = ( specificSpecificationItem == null ? null : specificSpecificationItem.updatedSpecificationItem() );

		if( definitionSpecificationItem != null ||
		specificSpecificationItem != null )
			{
			justificationResult.foundJustificationItem = foundJustificationItem( isActiveItem, updatedDefinitionSpecificationItem, updatedAnotherDefinitionSpecificationItem, updatedSpecificSpecificationItem );

			if( !isActiveItem ||
			forceToCreateJustification ||
			justificationResult.foundJustificationItem == null ||

			( attachedJustificationItem != null &&
			justificationResult.foundJustificationItem.isActiveItem() == isActiveItem &&					// Check state
			justificationResult.foundJustificationItem.justificationTypeNr() == justificationTypeNr &&		// Check type
			justificationResult.foundJustificationItem.attachedJustificationItem() != attachedJustificationItem ) )
				{
				if( justificationResult.foundJustificationItem == null ||
				justificationResult.foundJustificationItem.justificationTypeNr() == justificationTypeNr )
					{
					if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
						{
						if( ( justificationResult.createdJustificationItem = new JustificationItem( justificationTypeNr, orderNr, originalSentenceNr, attachedJustificationItem, updatedDefinitionSpecificationItem, updatedAnotherDefinitionSpecificationItem, updatedSpecificSpecificationItem, this, myWord() ) ) != null )
							{
							if( isActiveItem )
								{
								if( addItemToActiveList( (Item)justificationResult.createdJustificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to add an active justification item" );
								}
							else	// Archive
								{
								justificationResult.createdJustificationItem.setArchiveStatus();

								if( addItemToArchiveList( (Item)justificationResult.createdJustificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to add an archive justification item" );
								}
							}
						else
							setError( 1, null, "I failed to create an justification item" );
						}
					else
						setError( 1, null, "The current item number is undefined" );
					}
				else
					setError( 1, null, "The found justification type number is different from the given create justification type number" );
				}
			}
		else
			setError( 1, null, "None of the given specification items is defined" );

		justificationResult.result = CommonVariables.result;
		return justificationResult;
		}

	protected JustificationResultType checkForConfirmedJustificationItems( boolean isExclusiveGeneralization, short justificationTypeNr, JustificationItem specificationJustificationItem, SpecificationItem definitionSpecificationItem, SpecificationItem specificSpecificationItem, WordItem specificationWordItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		boolean foundConfirmation = false;
		JustificationItem foundSpecificSpecificationJustification;

		if( specificationJustificationItem != null )
			{
			if( specificationWordItem != null )
				{
				if( definitionSpecificationItem != null &&
				specificSpecificationItem != null )
					{
					if( ( foundSpecificSpecificationJustification = foundSpecificSpecificationJustificationItem( true, !specificSpecificationItem.isPossessive(), specificSpecificationItem.specificationWordItem(), specificSpecificationItem.generalizationWordItem() ) ) != null )
						{
						if( archiveJustificationItem( false, isExclusiveGeneralization, foundSpecificSpecificationJustification, specificationJustificationItem ) == Constants.RESULT_OK )
							foundConfirmation = true;
						else
							addError( 1, null, "I failed to replace an old specific justification item" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					( foundSpecificSpecificationJustification = foundSpecificSpecificationJustificationItem( false, !specificSpecificationItem.isPossessive(), specificSpecificationItem.specificationWordItem(), specificSpecificationItem.generalizationWordItem() ) ) != null )
						{
						if( ( justificationResult = addJustificationItem( false, true, justificationTypeNr, foundSpecificSpecificationJustification.orderNr, CommonVariables.currentSentenceNr, null, null, null, specificSpecificationItem ) ).result == Constants.RESULT_OK )
							{
							if( ( specificationJustificationItem = justificationResult.createdJustificationItem ) != null )
								{
								if( archiveJustificationItem( false, isExclusiveGeneralization, foundSpecificSpecificationJustification, justificationResult.createdJustificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to replace an old specific justification item without definition specification" );
								}
							else
								setError( 1, null, "I couldn't find or create a justification without definition specification" );
							}
						else
							addError( 1, null, "I failed to add a justification without definition specification" );
						}
					}
				}
			else
				setError( 1, null, "The given specification word item is undefined" );
			}
		else
			setError( 1, null, "The given specification justification item is undefined" );

		justificationResult.foundConfirmation = foundConfirmation;
		justificationResult.result = CommonVariables.result;
		return justificationResult;
		}

	protected byte checkJustificationItemForUsage( JustificationItem unusedJustificationItem )
		{
		if( checkJustificationItemForUsage( true, unusedJustificationItem ) == Constants.RESULT_OK )
			return checkJustificationItemForUsage( false, unusedJustificationItem );

		return CommonVariables.result;
		}

	protected byte checkSpecificationItemForUsage( SpecificationItem unusedSpecificationItem )
		{
		if( checkSpecificationItemForUsage( true, unusedSpecificationItem ) == Constants.RESULT_OK )
			return checkSpecificationItemForUsage( false, unusedSpecificationItem );

		return CommonVariables.result;
		}

	protected byte attachJustificationItem( JustificationItem attachJustificationItem, SpecificationItem attachSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		short justificationTypeNr;
		short highestAttachedJustificationOrderNr;
		short previousAssumptionLevel;
		JustificationItem firstJustificationItem;
		JustificationItem lastAttachedJustificationItem;
		JustificationItem oldJustificationItem = null;
		JustificationItem previousAttachedJustificationItem = null;
		SpecificationItem attachedDefinitionSpecificationItem;
		SpecificationItem attachedSpecificSpecificationItem;
		SpecificationItem replacingSpecificationItem = null;

		needToRecalculateAssumptionsAfterwards_ = false;

		if( attachJustificationItem != null )
			{
			if( attachSpecificationItem != null )
				{
				if( attachJustificationItem.isActiveItem() )
					{
					if( !attachSpecificationItem.isArchiveItem() )
						{
						if( ( firstJustificationItem = attachSpecificationItem.specificationJustificationItem() ) != null )
							{
							justificationTypeNr = attachJustificationItem.justificationTypeNr();

							if( !attachSpecificationItem.foundJustificationOfSameType( justificationTypeNr, attachJustificationItem.definitionSpecificationItem(), attachJustificationItem.specificSpecificationItem() ) )
								{
								if( ( highestAttachedJustificationOrderNr = attachSpecificationItem.highestAttachedJustificationOrderNr( justificationTypeNr ) ) < Constants.MAX_ORDER_NR )
									attachJustificationItem.orderNr = (short)( highestAttachedJustificationOrderNr + 1 );
								else
									return setSystemError( 1, null, "Justification order number overflow" );
								}

							if( attachJustificationItem.foundSpecificSpecificationQuestion() == null &&
							( oldJustificationItem = firstJustificationItem.foundSpecificSpecificationQuestion() ) == null )
								{
								attachedDefinitionSpecificationItem = attachJustificationItem.definitionSpecificationItem();

								if( attachedDefinitionSpecificationItem != null &&
								attachedDefinitionSpecificationItem.hasRelationContext() )
									oldJustificationItem = firstJustificationItem.definitionSpecificationWithoutRelationContextJustificationItem( attachedDefinitionSpecificationItem, attachJustificationItem.specificSpecificationItem() );
								else
									{
									attachedSpecificSpecificationItem = attachJustificationItem.specificSpecificationItem();

									if( attachedSpecificSpecificationItem != null &&
									attachedSpecificSpecificationItem.isUserSpecification() )
										oldJustificationItem = firstJustificationItem.selfGeneratedSpecificSpecificationJustificationItem( attachedDefinitionSpecificationItem, attachJustificationItem.specificSpecificationItem() );
									}

								if( oldJustificationItem != null )
									{
									if( archiveJustificationItem( false, false, oldJustificationItem, attachJustificationItem ) == Constants.RESULT_OK )
										attachJustificationItem.orderNr = oldJustificationItem.orderNr;
									else
										addError( 1, null, "I failed to archive a justification item" );
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( oldJustificationItem == null )
									{
									// Find a justification item with current sentence number, but without attached justification item
									lastAttachedJustificationItem = firstJustificationItem;

									while( lastAttachedJustificationItem.hasCurrentCreationSentenceNr() &&
									lastAttachedJustificationItem.attachedJustificationItem() != null )
										lastAttachedJustificationItem = lastAttachedJustificationItem.attachedJustificationItem();

									if( lastAttachedJustificationItem.hasCurrentCreationSentenceNr() )
										{
										if( ( specificationResult = attachSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
											{
											previousAssumptionLevel = specificationResult.assumptionLevel;

											if( lastAttachedJustificationItem.attachJustificationItem( attachJustificationItem, attachSpecificationItem ) == Constants.RESULT_OK )
												{
												if( ( specificationResult = attachSpecificationItem.recalculateAssumptionLevel() ).result == Constants.RESULT_OK )
													{
													if( attachSpecificationItem.isOlderSentence() &&
													specificationResult.assumptionLevel != previousAssumptionLevel )
														{
														// Write adjusted specification
														if( myWord().writeSpecification( true, false, false, attachSpecificationItem ) != Constants.RESULT_OK )
															addError( 1, null, "I failed to write an adjusted specification" );
														}
													}
												else
													addError( 1, null, "I failed to recalculate the assumption level" );
												}
											else
												addError( 1, null, "I failed to attach a justification item" );
											}
										else
											addError( 1, null, "I failed to get the assumption level" );
										}
									else	// Otherwise, attach the justification item to the given specification item
										{
										if( attachJustificationItem.attachedJustificationItem() == null )
											{
											if( attachSpecificationItem.hasCurrentActiveSentenceNr() )
												{
												if( attachSpecificationItem.changeJustificationItem( attachJustificationItem ) == Constants.RESULT_OK )
													{
													if( attachJustificationItem.attachJustificationItem( firstJustificationItem, attachSpecificationItem ) != Constants.RESULT_OK )
														addError( 1, null, "I failed to attach the first justification item to the given attached justification item" );
													}
												else
													addError( 1, null, "I failed to change the attached justification item of the given attach specification item" );
												}
											else
												{
												if( attachSpecificationItem.isAssignment() )
													{
													if( ( specificationResult = myWord().createAssignment( attachSpecificationItem.isAnsweredQuestion(), attachSpecificationItem.isConcludedAssumption(), attachSpecificationItem.isDeactiveItem(), attachSpecificationItem.isArchiveItem(), attachSpecificationItem.isExclusive(), attachSpecificationItem.isNegative(), attachSpecificationItem.isPossessive(), attachSpecificationItem.isValueSpecification(), attachSpecificationItem.assignmentLevel(), attachSpecificationItem.assumptionLevel(), attachSpecificationItem.prepositionParameter(), attachSpecificationItem.questionParameter(), attachSpecificationItem.generalizationWordTypeNr(), attachSpecificationItem.specificationWordTypeNr(), attachSpecificationItem.generalizationCollectionNr(), attachSpecificationItem.specificationCollectionNr(), attachSpecificationItem.generalizationContextNr(), attachSpecificationItem.specificationContextNr(), attachSpecificationItem.relationContextNr(), attachSpecificationItem.originalSentenceNr(), attachSpecificationItem.activeSentenceNr(), attachSpecificationItem.deactiveSentenceNr(), attachSpecificationItem.archiveSentenceNr(), attachSpecificationItem.nContextRelations(), attachJustificationItem, attachSpecificationItem.specificationWordItem(), attachSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
														{
														if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
															return setError( 1, null, "I couldn't create an assignment" );
														}
													else
														addError( 1, null, "I failed to add an assignment" );
													}
												else
													{
													if( ( specificationResult = myWord().createSpecification( attachSpecificationItem.isAnsweredQuestion(), attachSpecificationItem.isConditional(), attachSpecificationItem.isConcludedAssumption(), attachSpecificationItem.isDeactiveItem(), attachSpecificationItem.isArchiveItem(), attachSpecificationItem.isExclusive(), attachSpecificationItem.isNegative(), attachSpecificationItem.isPossessive(), attachSpecificationItem.isSpecificationGeneralization(), attachSpecificationItem.isValueSpecification(), attachSpecificationItem.assumptionLevel(), attachSpecificationItem.prepositionParameter(), attachSpecificationItem.questionParameter(), attachSpecificationItem.generalizationWordTypeNr(), attachSpecificationItem.specificationWordTypeNr(), attachSpecificationItem.generalizationCollectionNr(), attachSpecificationItem.specificationCollectionNr(), attachSpecificationItem.generalizationContextNr(), attachSpecificationItem.specificationContextNr(), attachSpecificationItem.relationContextNr(), attachSpecificationItem.originalSentenceNr(), attachSpecificationItem.nContextRelations(), attachJustificationItem, attachSpecificationItem.specificationWordItem(), attachSpecificationItem.specificationString() ) ).result == Constants.RESULT_OK )
														{
														if( ( replacingSpecificationItem = specificationResult.createdSpecificationItem ) == null )
															return setError( 1, null, "I couldn't create a specification" );
														}
													else
														addError( 1, null, "I failed to add a specification" );
													}

												if( CommonVariables.result == Constants.RESULT_OK )
													{
													if( attachJustificationItem.attachJustificationItem( firstJustificationItem, replacingSpecificationItem ) == Constants.RESULT_OK )
														{
														if( myWord().archiveOrDeleteSpecification( attachSpecificationItem, replacingSpecificationItem ) == Constants.RESULT_OK )
															{
															if( replacingSpecificationItem.isOlderSentence() &&
															replacingSpecificationItem.isSelfGeneratedAssumption() )
																needToRecalculateAssumptionsAfterwards_ = true;
															}
														else
															addError( 1, null, "I failed to archive or delete a specification" );
														}
													else
														addError( 1, null, "I failed to attach the first justification item to the given attached justification item" );
													}
												}
											}
										else
											{
											lastAttachedJustificationItem = firstJustificationItem;

											while( lastAttachedJustificationItem.hasCurrentCreationSentenceNr() &&
											lastAttachedJustificationItem.attachedJustificationItem() != null )
												{
												previousAttachedJustificationItem = lastAttachedJustificationItem;
												lastAttachedJustificationItem = lastAttachedJustificationItem.attachedJustificationItem();
												}

											// The last attached justification item has no attached justification item
											if( lastAttachedJustificationItem.attachedJustificationItem() == null )
												{
												if( previousAttachedJustificationItem != null )
													{
													// Attach attachJustificationItem to a copy of the last attached justification item
													if( ( justificationResult = addJustificationItem( false, true, lastAttachedJustificationItem.justificationTypeNr(), lastAttachedJustificationItem.orderNr, lastAttachedJustificationItem.originalSentenceNr(), attachJustificationItem, lastAttachedJustificationItem.definitionSpecificationItem(), lastAttachedJustificationItem.anotherDefinitionSpecificationItem(), lastAttachedJustificationItem.specificSpecificationItem() ) ).result == Constants.RESULT_OK )
														{
														if( justificationResult.createdJustificationItem != null )
															{
															// Link the created justification item to the previous attached justification item
															if( previousAttachedJustificationItem.changeAttachedJustificationItem( justificationResult.createdJustificationItem ) == Constants.RESULT_OK )
																{
																// Archive the lastAttachedJustificationItem
																if( archiveJustificationItem( false, false, lastAttachedJustificationItem, justificationResult.createdJustificationItem ) != Constants.RESULT_OK )
																	addError( 1, null, "I failed to archive a justification item" );
																}
															else
																addError( 1, null, "I failed to change the attached justification item of the previous justification item" );
															}
														else
															return setError( 1, null, "I couldn't create a justification item" );
														}
													else
														addError( 1, null, "I failed to add a justification item" );
													}
												else
													return setError( 1, null, "The previous attached justification item is undefined. I have no solution implemented to solve this problem" );
												}
											else
												return setError( 1, null, "The last attached justification item is not the only old one. I have no solution implemented to solve this problem" );
											}
										}
									}
								else
									{
									if( myWord().updateJustificationInSpecifications( false, false, oldJustificationItem, attachJustificationItem ) != Constants.RESULT_OK )
										addError( 1, null, "I failed to update a question justification item by a conclusion justification item" );
									}
								}
							}
						else
							return setError( 1, null, "The given attach specification item has no specification justification item" );
						}
					else
						return setError( 1, null, "The given attach specification item is archived" );
					}
				else
					return setError( 1, null, "The given attached justification item is not active" );
				}
			else
				return setError( 1, null, "The given attach specification item is undefined" );
			}
		else
			return setError( 1, null, "The given attached justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte archiveJustificationItem( boolean isExclusive, boolean isExclusiveGeneralization, JustificationItem oldJustificationItem, JustificationItem replacingJustificationItem )
		{
		JustificationItem predecessorOfOldAttachedJustificationItem;

		if( oldJustificationItem != null )
			{
			if( oldJustificationItem.isOlderSentence() )
				{
				if( oldJustificationItem.replacingJustificationItem == null )
					{
					if( replacingJustificationItem == null ||
					replacingJustificationItem.replacingJustificationItem == null )
						{
						if( oldJustificationItem != replacingJustificationItem )
							{
							predecessorOfOldAttachedJustificationItem = oldJustificationItem.attachedJustificationItem();

							if( predecessorOfOldAttachedJustificationItem != null &&							// Old justification has an attached justification,
							replacingJustificationItem != null &&
							replacingJustificationItem.hasCurrentActiveSentenceNr() &&			// but the replacing one is current
							replacingJustificationItem.attachedJustificationItem() == null )	// and has no attached justifications
								{
								if( replacingJustificationItem.changeAttachedJustificationItem( predecessorOfOldAttachedJustificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to change an attached justification item" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( predecessorOfOldAttachedJustificationItem == null ||
								predecessorOfOldAttachedJustificationItem.isArchiveItem() ||

								// Replacing justification item has the same attached justification item
								( replacingJustificationItem != null &&
								replacingJustificationItem.attachedJustificationItem() == predecessorOfOldAttachedJustificationItem ) )
									{
									oldJustificationItem.replacingJustificationItem = replacingJustificationItem;

									// Update the justification items that are filtered by getAssignmentItem
									// variable: replacingSpecificationItem
									if( updateArchivedReplacingJustificationItems( oldJustificationItem ) == Constants.RESULT_OK )
										{
										if( oldJustificationItem.isActiveItem() )
											{
											if( archiveActiveItem( oldJustificationItem ) != Constants.RESULT_OK )
												addError( 1, null, "I failed to archive an active justification item" );
											}

										if( CommonVariables.result == Constants.RESULT_OK )
											{
											if( myWord().updateJustificationInSpecifications( isExclusive, isExclusiveGeneralization, oldJustificationItem, replacingJustificationItem ) != Constants.RESULT_OK )
												addError( 1, null, "I failed to update a justification in specifications of my word" );
											}
										}
									else
										addError( 1, null, "I failed to update the replacing specification item of the archived justification items" );
									}
								else
									return setError( 1, null, "The given old justification item has an active attached justification item" );
								}
							}
						else
							return setError( 1, null, "The given old justification item and the given replacing justification item are the same" );
						}
					else
						return setError( 1, null, "The given replacing justification item has a replacing justification item itself" );
					}
				else
					return setError( 1, null, "The given old justification item has already a replacing justification item" );
				}
			else
				return setError( 1, null, "The given old justification item isn't old" );
			}
		else
			return setError( 1, null, "The given old justification item is undefined" );

		return CommonVariables.result;
		}

	protected byte updateSpecificationsInJustificationItems( boolean isActiveItem, SpecificationItem oldSpecificationItem, SpecificationItem replacingSpecificationItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		boolean skipArchiving;
		boolean isReplaceDefinition;
		boolean isReplaceAnotherDefinition;
		boolean isReplaceSpecific;
		boolean isExclusive = ( replacingSpecificationItem == null ? false : replacingSpecificationItem.isExclusive() );
		boolean isExclusiveGeneralization = ( replacingSpecificationItem == null ? false : replacingSpecificationItem.hasExclusiveGeneralizationCollection() );
		SpecificationItem definitionSpecificationItem;
		SpecificationItem anotherDefinitionSpecificationItem;
		SpecificationItem specificSpecificationItem;
		JustificationItem createdJustificationItem = null;
		JustificationItem searchItem = firstJustificationItem( isActiveItem );

		if( oldSpecificationItem != null )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				definitionSpecificationItem = searchItem.definitionSpecificationItem();
				anotherDefinitionSpecificationItem = searchItem.anotherDefinitionSpecificationItem();
				specificSpecificationItem = searchItem.specificSpecificationItem();

				isReplaceDefinition = ( definitionSpecificationItem == oldSpecificationItem );
				isReplaceAnotherDefinition = ( anotherDefinitionSpecificationItem == oldSpecificationItem );
				isReplaceSpecific = ( specificSpecificationItem == oldSpecificationItem );

				if( isReplaceDefinition ||
				isReplaceAnotherDefinition ||
				isReplaceSpecific )
					{
					if( searchItem.hasCurrentCreationSentenceNr() )
						{
						if( isReplaceDefinition )
							{
							if( searchItem.changeDefinitionSpecificationItem( replacingSpecificationItem ) != Constants.RESULT_OK )
								addError( 1, null, "I failed to change the definition specificationItem item of a justification item" );
							}
						else
							{
							if( isReplaceAnotherDefinition )
								{
								if( searchItem.changeAnotherDefinitionSpecificationItem( replacingSpecificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to change the another definition specificationItem item of a justification item" );
								}
							else
								{
								if( searchItem.changeSpecificSpecificationItem( replacingSpecificationItem ) != Constants.RESULT_OK )
									addError( 1, null, "I failed to change the specific specificationItem item of a justification item" );
								}
							}

						searchItem = searchItem.nextJustificationItem();
						}
					else
						{
						skipArchiving = false;

						if( replacingSpecificationItem == null ||
						myWord().isCorrectedAssumption() )
							createdJustificationItem = null;
						else
							{
							if( ( justificationResult = addJustificationItem( false, isActiveItem, searchItem.justificationTypeNr(), searchItem.orderNr, searchItem.originalSentenceNr(), searchItem.attachedJustificationItem(), ( isReplaceDefinition ? replacingSpecificationItem : definitionSpecificationItem ), ( isReplaceAnotherDefinition ? replacingSpecificationItem : anotherDefinitionSpecificationItem ), ( isReplaceSpecific ? replacingSpecificationItem : specificSpecificationItem ) ) ).result == Constants.RESULT_OK )
								{
								if( ( createdJustificationItem = justificationResult.createdJustificationItem ) != null )
									{
									if( !isActiveItem )
										{
										skipArchiving = true;
										searchItem.replacingJustificationItem = createdJustificationItem;
										searchItem = searchItem.nextJustificationItem();	// Next active, while justification item is archived
										}
									}
								else
									return setError( 1, null, "I couldn't create a justification item" );
								}
							else
								addError( 1, null, "I failed to add an justification item" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						!skipArchiving )
							{
							if( archiveJustificationItem( isExclusive, isExclusiveGeneralization, searchItem, createdJustificationItem ) == Constants.RESULT_OK )
								searchItem = ( createdJustificationItem == null ? searchItem.nextJustificationItem() : firstActiveJustificationItem() );
							else
								addError( 1, null, "I failed to archive a justification item" );
							}
						}
					}
				else
					searchItem = searchItem.nextJustificationItem();
				}
			}
		else
			return setError( 1, null, "The given old specification item is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"I will praise you every day;
 *	yes, I will praise you forever." (Psalm 145:2)
 *
 *************************************************************************/
