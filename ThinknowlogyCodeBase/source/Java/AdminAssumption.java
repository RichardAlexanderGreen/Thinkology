/*
 *	Class:			AdminAssumption
 *	Supports class:	AdminItem
 *	Purpose:		To create assumptions autonomously
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

class AdminAssumption
	{
	// Private constructible variables

	private SpecificationItem foundAssumptionSpecificationItem_;
	private SpecificationItem foundOppositePossessiveSpecificationItem_;
	private SpecificationItem foundPossessiveSpecificationItem_;
	private SpecificationItem possessiveSpecificationItem_;
	private SpecificationItem anotherDefinitionSpecificationItem_;
	private SpecificationItem specificSpecificationItem_;

	private WordItem oppositePossessiveSpecificationWordItem_;
	private WordItem possessiveSpecificationWordItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean isAssumption( short justificationTypeNr )
		{
		return ( justificationTypeNr == Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_GENERALIZATION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_SPECIFICATION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_SPECIFICATION_SUBSTITUTION_ASSUMPTION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION ||
				justificationTypeNr == Constants.JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION );
		}

	private byte findPossessiveSpecifications( int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		SpecificationItem possessiveSpecificationItem;
		SpecificationItem previousPossessiveSpecificationItem = null;
		WordItem possessiveSpecificationWordItem;
		WordItem higherLevelPossessiveSpecificationWordItem = null;

		foundPossessiveSpecificationItem_ = null;
		foundOppositePossessiveSpecificationItem_ = null;

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				// First try to find a possessive specification on a higher level
				if( ( possessiveSpecificationItem = generalizationWordItem.firstSpecificationButNotAQuestion() ) != null )
					{
					do	{
						if( possessiveSpecificationItem.isPossessive() &&
						!possessiveSpecificationItem.isExclusive() &&
						possessiveSpecificationItem.isSpecificationNoun() &&
						( possessiveSpecificationWordItem = possessiveSpecificationItem.specificationWordItem() ) != null )
							{
							if( possessiveSpecificationWordItem != higherLevelPossessiveSpecificationWordItem )
								{
								if( possessiveSpecificationWordItem.firstAssignmentOrSpecification( false, false, false, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem ) != null )
									{
									if( higherLevelPossessiveSpecificationWordItem == null )
										higherLevelPossessiveSpecificationWordItem = possessiveSpecificationWordItem;
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I found at least two fitting higher level specification words for generalization word \"" + specificationWordItem.anyWordTypeString() + "\": \"" + higherLevelPossessiveSpecificationWordItem.anyWordTypeString() + "\" and \"" + possessiveSpecificationWordItem.anyWordTypeString() + "\"" );
									}
								}
							}
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					( possessiveSpecificationItem = possessiveSpecificationItem.nextSpecificationItemButNotAQuestion() ) != null );

					if( CommonVariables.result == Constants.RESULT_OK &&
					( possessiveSpecificationItem = generalizationWordItem.firstSpecificationButNotAQuestion() ) != null )
						{
						if( possessiveSpecificationItem.hasSpecificationCollection() )
							{
							previousPossessiveSpecificationItem = null;

							if( higherLevelPossessiveSpecificationWordItem != null )
								specificationWordItem = higherLevelPossessiveSpecificationWordItem;

							// Try to find the opposite of the possessive specification
							do	{
								if( possessiveSpecificationItem.isPossessive() &&
								!possessiveSpecificationItem.isExclusive() &&
								possessiveSpecificationItem.specificationWordItem() != null &&
								possessiveSpecificationItem.specificationWordItem().isSingularNoun() &&

								( foundOppositePossessiveSpecificationItem_ == null ||
								possessiveSpecificationItem.specificationWordItem() != foundOppositePossessiveSpecificationItem_.specificationWordItem() ) )
									{
									if( previousPossessiveSpecificationItem != null &&
									previousPossessiveSpecificationItem.specificationWordItem() != possessiveSpecificationItem.specificationWordItem() &&

									( possessiveSpecificationItem.specificationWordItem() == specificationWordItem ||
									previousPossessiveSpecificationItem.specificationWordItem() == specificationWordItem ) &&
									previousPossessiveSpecificationItem.specificationCollectionNr() == possessiveSpecificationItem.specificationCollectionNr() )
										{
										if( foundOppositePossessiveSpecificationItem_ == null )
											{
											foundPossessiveSpecificationItem_ = ( specificationWordItem == possessiveSpecificationItem.specificationWordItem() ? possessiveSpecificationItem : previousPossessiveSpecificationItem );
											foundOppositePossessiveSpecificationItem_ = ( specificationWordItem == possessiveSpecificationItem.specificationWordItem() ? previousPossessiveSpecificationItem : possessiveSpecificationItem );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "I found at least two fitting opposite possessive specification words for generalization word \"" + specificationWordItem.anyWordTypeString() + "\": \"" + higherLevelPossessiveSpecificationWordItem.anyWordTypeString() + "\" and \"" + previousPossessiveSpecificationItem.specificationWordItem().anyWordTypeString() + "\"" );
										}

									previousPossessiveSpecificationItem = possessiveSpecificationItem;
									}
								}
							while( ( possessiveSpecificationItem = possessiveSpecificationItem.nextSpecificationItemButNotAQuestion() ) != null );
							}
						}
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}

	private byte findPossessiveConditionalSpecificationAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		if( myWord_.isNounWordType( specificationWordTypeNr ) )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( relationWordItem != null )
						{
						if( specificSpecificationItem_ != null )
							{
							if( admin_.isGeneralizationReasoningWordType( false, generalizationWordTypeNr ) )
								{
								if( possessiveSpecificationWordItem_ != null &&
								possessiveSpecificationWordItem_ != specificationWordItem )
									{
									if( possessiveSpecificationWordItem_.isSingularNoun() )
										{
										if( addAssumption( isDeactive, isArchive, isExclusive, isNegative, !isPossessive, Constants.JUSTIFICATION_TYPE_BACK_FIRED_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, relationWordTypeNr, Constants.WORD_TYPE_NOUN_SINGULAR, generalizationWordTypeNr, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, null, null, specificSpecificationItem_, relationWordItem, possessiveSpecificationWordItem_, generalizationWordItem ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a possessive conditional specification assumption in relation word \"" + relationWordItem.anyWordTypeString() + "\" to specification word \"" + possessiveSpecificationWordItem_.anyWordTypeString() + "\"" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The back-fire opposite possessive specification word should be a singular noun" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								!CommonVariables.hasShownWarning &&
								oppositePossessiveSpecificationWordItem_ != null )
									{
									if( oppositePossessiveSpecificationWordItem_.isSingularNoun() )
										{
										if( possessiveSpecificationItem_ != null )
											{
											if( addAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_OPPOSITE_POSSESSIVE_CONDITIONAL_SPECIFICATION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, relationWordTypeNr, Constants.WORD_TYPE_NOUN_SINGULAR, ( isPossessive ? Constants.WORD_TYPE_UNDEFINED : generalizationWordTypeNr ), Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, possessiveSpecificationItem_, null, specificSpecificationItem_, relationWordItem, oppositePossessiveSpecificationWordItem_, ( isPossessive ? null : generalizationWordItem ) ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a possessive conditional specification assumption from relation word \"" + relationWordItem.anyWordTypeString() + "\" to specification word \"" + oppositePossessiveSpecificationWordItem_.anyWordTypeString() + "\"" );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "The possessive specification item is undefined" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The opposite possessive specification word should be a singular noun" );
									}
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The specific specification item is undefined" );
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
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word type is not a noun" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminAssumption( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		foundAssumptionSpecificationItem_ = null;
		foundOppositePossessiveSpecificationItem_ = null;
		foundPossessiveSpecificationItem_ = null;
		possessiveSpecificationItem_ = null;
		anotherDefinitionSpecificationItem_ = null;
		specificSpecificationItem_ = null;

		oppositePossessiveSpecificationWordItem_ = null;
		possessiveSpecificationWordItem_ = null;

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

	protected void initializeAdminAssumptionVariables()
		{
		anotherDefinitionSpecificationItem_ = null;
		specificSpecificationItem_ = null;
		}

	protected byte addSuggestiveQuestionAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, SpecificationItem specificSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( specificSpecificationItem != null )
			{
			if( generalizationWordItem != null )
				{
				if( relationWordItem != null )
					{
					if( ( specificationResult = generalizationWordItem.findRelatedSpecification( false, false, isExclusive, isPossessive, Constants.NO_QUESTION_PARAMETER, specificSpecificationItem.specificationCollectionNr(), specificSpecificationItem.relationCollectionNr(), generalizationContextNr, specificationContextNr, relationContextNr, specificationWordItem, relationWordItem, null ) ).result == Constants.RESULT_OK )
						{
						if( specificationResult.relatedSpecificationItem == null )
							{
							if( addAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_SUGGESTIVE_QUESTION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, null, null, specificSpecificationItem, generalizationWordItem, specificationWordItem, null ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a suggestive question assumption in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" is related to the found specification" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given relation word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specific specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte findGeneralizationAssumptionBySpecification( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem definitionSpecificationItem;
		SpecificationItem specificSpecificationItem;
		WordItem currentGeneralizationWordItem;

		if( myWord_.isNounWordType( specificationWordTypeNr ) )
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
								if( currentGeneralizationWordItem.isNoun() &&
								!currentGeneralizationWordItem.hasCollection( null ) &&
								!currentGeneralizationWordItem.hasPossessiveSpecificationButNotAQuestion() &&
								currentGeneralizationWordItem != generalizationWordItem )
									{
									// Get definition specification for justification
									if( ( definitionSpecificationItem = currentGeneralizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, specificationWordItem ) ) != null )
										{
										if( !definitionSpecificationItem.isSpecificationGeneralization() )
											{
											// Get specific specification for justification
											if( ( specificSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, definitionSpecificationItem.specificationCollectionNr(), definitionSpecificationItem.generalizationContextNr(), definitionSpecificationItem.specificationContextNr(), definitionSpecificationItem.relationContextNr(), definitionSpecificationItem.specificationWordItem(), definitionSpecificationItem.specificationString() ) ) != null )
												{
												if( addAssumption( isDeactive, isArchive, false, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_SPECIFICATION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, Constants.NO_CONTEXT_NR, definitionSpecificationItem, null, specificSpecificationItem, generalizationWordItem, currentGeneralizationWordItem, null ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a generalization assumption in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to specification word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
												}
											}
										}
									}
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						!CommonVariables.hasShownWarning &&
						( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word type is not a noun" );

		return CommonVariables.result;
		}

	protected byte findGeneralizationAssumptionByGeneralization( boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, short specificQuestionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, int generalizationContextNr, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem definitionSpecificationItem;
		SpecificationItem specificSpecificationItem;
		WordItem currentGeneralizationWordItem;

		if( admin_.isGeneralizationReasoningWordType( true, generalizationWordTypeNr ) )
			{
			if( myWord_.isNounWordType( specificationWordTypeNr ) )
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
									currentGeneralizationWordItem.isNoun() &&
									!currentGeneralizationWordItem.hasCollection( null ) &&
									!currentGeneralizationWordItem.hasPossessiveSpecificationButNotAQuestion() )
										{
										// Check for related specification
										// Get definition specification for justification
										if( ( definitionSpecificationItem = currentGeneralizationWordItem.firstAssignmentOrSpecificationButNotAQuestion( true, true, true, true, isNegative, isPossessive, Constants.NO_COLLECTION_NR, generalizationContextNr, Constants.NO_CONTEXT_NR, relationContextNr, specificationWordItem ) ) != null )
											{
											// Get specific specification for justification
											if( ( specificSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, true, true, true, true, isNegative, isPossessive, specificQuestionParameter, definitionSpecificationItem.generalizationContextNr(), definitionSpecificationItem.specificationContextNr(), definitionSpecificationItem.relationContextNr(), definitionSpecificationItem.specificationWordItem(), definitionSpecificationItem.specificationString() ) ) != null )
												{
												if( addAssumption( isDeactive, isArchive, false, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_GENERALIZATION_ASSUMPTION_BY_GENERALIZATION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, Constants.NO_CONTEXT_NR, definitionSpecificationItem, null, specificSpecificationItem, generalizationWordItem, currentGeneralizationWordItem, null ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a generalization assumption in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to specification word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
												}
											}
										}
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
								}
							while( CommonVariables.result == Constants.RESULT_OK &&
							!CommonVariables.hasShownWarning &&
							( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word type is not a noun" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word type is not a reasoning word type" );

		return CommonVariables.result;
		}

	protected byte findExclusiveSpecificationSubstitutionAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem definitionSpecificationItem;
		SpecificationItem foundSpecificationItem;
		WordItem currentWordItem;

		possessiveSpecificationItem_ = null;

		possessiveSpecificationWordItem_ = null;
		oppositePossessiveSpecificationWordItem_ = null;

		if( myWord_.isNounWordType( specificationWordTypeNr ) )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( relationWordItem != null )
						{
						if( ( specificationResult = generalizationWordItem.findSpecification( true, true, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, specificationWordItem, relationWordItem ) ).result == Constants.RESULT_OK )
							{
							if( ( foundSpecificationItem = specificationResult.foundSpecificationItem ) != null )
								{
								// Initially some wanted words are not linked. So, search in all words.
								if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
									{
									do	{
										if( currentWordItem.isNoun() )
											{
											if( findPossessiveSpecifications( generalizationContextNr, specificationContextNr, currentWordItem, specificationWordItem ) == Constants.RESULT_OK )
												{
												if( foundPossessiveSpecificationItem_ != null )
													{
													possessiveSpecificationItem_ = foundPossessiveSpecificationItem_;

													if( ( possessiveSpecificationWordItem_ = possessiveSpecificationItem_.specificationWordItem() ) != null )
														{
														if( ( definitionSpecificationItem = possessiveSpecificationWordItem_.firstAssignmentOrSpecification( false, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem ) ) != null )
															{
															if( addAssumption( isDeactive, isArchive, false, isNegative, isPossessive, Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, Constants.WORD_TYPE_NOUN_SINGULAR, relationWordTypeNr, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, definitionSpecificationItem, null, foundSpecificationItem, generalizationWordItem, possessiveSpecificationWordItem_, relationWordItem ) == Constants.RESULT_OK )
																{
																if( specificSpecificationItem_ == null )
																	specificSpecificationItem_ = foundAssumptionSpecificationItem_;
																}
															else
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add an exclusive specification substitution assumption about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and possessive specification word \"" + possessiveSpecificationWordItem_.anyWordTypeString() + "\"" );
															}
														}
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												!CommonVariables.hasShownWarning &&
												foundOppositePossessiveSpecificationItem_ != null &&
												( oppositePossessiveSpecificationWordItem_ = foundOppositePossessiveSpecificationItem_.specificationWordItem() ) != null )
													{
													if( specificSpecificationItem_ == null )
														{
														specificSpecificationItem_ = foundSpecificationItem;

														if( isPossessive )		// Find another definition
															anotherDefinitionSpecificationItem_ = possessiveSpecificationWordItem_.firstAssignmentOrSpecification( false, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, specificationWordItem );
														}

													if( addAssumption( isDeactive, isArchive, false, isNegative, !isPossessive, Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION, Constants.NO_PREPOSITION_PARAMETER, generalizationWordTypeNr, Constants.WORD_TYPE_NOUN_SINGULAR, relationWordTypeNr, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, generalizationContextNr, specificationContextNr, foundOppositePossessiveSpecificationItem_, anotherDefinitionSpecificationItem_, specificSpecificationItem_, generalizationWordItem, oppositePossessiveSpecificationWordItem_, relationWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add an exclusive specification substitution assumption about generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and opposite possessive specification word \"" + oppositePossessiveSpecificationWordItem_.anyWordTypeString() + "\"" );
													}
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find possessive specifications in word \"" + currentWordItem.anyWordTypeString() + "\"" );
											}
										}
									while( CommonVariables.result == Constants.RESULT_OK &&
									!CommonVariables.hasShownWarning &&
									( currentWordItem = currentWordItem.nextWordItem() ) != null );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							!CommonVariables.hasShownWarning &&
							specificSpecificationItem_ != null )
								{
								if( findPossessiveConditionalSpecificationAssumption( isDeactive, isArchive, isExclusive, isNegative, isPossessive, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, generalizationWordItem, specificationWordItem, relationWordItem ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find an exclusive specification substitution assumption with generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find an assignment from generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to specification word \"" + specificationWordItem.anyWordTypeString() + "\"" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given relation word item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word type is not a noun" );

		return CommonVariables.result;
		}

	protected byte addAssumption( boolean isDeactive, boolean isArchive, boolean isExclusive, boolean isNegative, boolean isPossessive, short justificationTypeNr, short prepositionParamater, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, SpecificationItem definitionSpecificationItem, SpecificationItem anotherDefinitionSpecificationItem, SpecificationItem specificSpecificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem )
		{
		JustificationResultType justificationResult = new JustificationResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean hasCreatedJustification = false;
		boolean isAssignment = ( isDeactive || isArchive );
		boolean isExclusiveSpecificationSubstitutionAssumption = false;
		int relationContextNr;
		JustificationItem archiveJustificationItem;
		JustificationItem specificationJustificationItem = null;
		SpecificationItem createdSpecificationItem;

		foundAssumptionSpecificationItem_ = null;

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				if( isAssumption( justificationTypeNr ) )
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
							if( ( specificationResult = admin_.addSpecification( isAssignment, isDeactive, isArchive, isExclusive, isNegative, isPossessive, true, false, false, prepositionParamater, Constants.NO_QUESTION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, 0, specificationJustificationItem, generalizationWordItem, specificationWordItem, relationWordItem, null ) ).result == Constants.RESULT_OK )
								{
								if( !CommonVariables.hasShownWarning )
									{
									createdSpecificationItem = specificationResult.createdSpecificationItem;
									foundAssumptionSpecificationItem_ = specificationResult.foundSpecificationItem;

									if( !isPossessive &&
									justificationTypeNr == Constants.JUSTIFICATION_TYPE_EXCLUSIVE_SPECIFICATION_SUBSTITUTION_ASSUMPTION )
										{
										isExclusiveSpecificationSubstitutionAssumption = true;
										specificSpecificationItem_ = createdSpecificationItem;
										}

									if( createdSpecificationItem == null )
										{
										if( isExclusiveSpecificationSubstitutionAssumption &&
										generalizationWordItem.isConfirmedAssumption() &&
										foundAssumptionSpecificationItem_ != null &&
										( archiveJustificationItem = foundAssumptionSpecificationItem_.foundSpecificSpecificationJustificationItem( relationWordItem ) ) != null )
											{
											if( generalizationWordItem.archiveJustification( foundAssumptionSpecificationItem_.hasExclusiveGeneralizationCollection(), archiveJustificationItem, specificationJustificationItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to archive a justification item" );
											}
										else
											{
											// A justification has been created, but the assumption specification already exists.
											// So, the justification needs to be added separately
											if( hasCreatedJustification )
												{
												if( foundAssumptionSpecificationItem_ != null )
													{
													if( ( justificationResult = generalizationWordItem.checkForConfirmedJustifications( foundAssumptionSpecificationItem_.hasExclusiveGeneralizationCollection(), justificationTypeNr, specificationJustificationItem, definitionSpecificationItem, specificSpecificationItem, specificationWordItem ) ).result == Constants.RESULT_OK )
														{
														if( !justificationResult.foundConfirmation )
															{
															// Add (attach) new created justification to the found specification
															if( generalizationWordItem.attachJustification( specificationJustificationItem, foundAssumptionSpecificationItem_ ) != Constants.RESULT_OK )
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to attach a justification to an assumption in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
															}
														}
													else
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for confirmations in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
													}
												else
													// Check assumption for existence before calling this method - rather than deleting the just created justification
													return myWord_.setErrorInItem( 1, moduleNameString_, "I have created a justification, but it isn't used. Please check if the assumption already exists, before calling this method" );
												}
											}
										}
									else
										{
										// Check assumption for integrity
										if( generalizationWordItem.writeSelectedSpecification( true, true, false, false, Constants.NO_ANSWER_PARAMETER, createdSpecificationItem ) == Constants.RESULT_OK )
											{
											if( CommonVariables.writeSentenceStringBuffer != null &&
											CommonVariables.writeSentenceStringBuffer.length() > 0 )
												{
												if( ( relationContextNr = createdSpecificationItem.relationContextNr() ) > Constants.NO_CONTEXT_NR )
													{
													if( admin_.collectGeneralizationWordWithPreviousOne( isExclusive, createdSpecificationItem.hasExclusiveGeneralizationCollection(), isPossessive, generalizationWordTypeNr, specificationWordTypeNr, Constants.NO_QUESTION_PARAMETER, relationContextNr, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to collect a generalization word with a previous one" );
													}
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "Integrity error! I couldn't write the created assumption with generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" and specification word \"" + specificationWordItem.anyWordTypeString() + "\". I guess, the implementation of my writing modules is insufficient to write this particular sentence structure" );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the created assumption in generalization word \"" + generalizationWordItem.anyWordTypeString() + "\" to check the writing integrity" );
										}

									if( CommonVariables.result == Constants.RESULT_OK &&
									myWord_.isNounWordType( specificationWordTypeNr ) &&

									( hasCreatedJustification ||
									createdSpecificationItem != null ) )
										{
										if( admin_.findSpecificationSubstitutionConclusionOrQuestion( true, isDeactive, isArchive, isExclusive, isNegative, isPossessive, false, Constants.NO_QUESTION_PARAMETER, generalizationWordTypeNr, specificationWordTypeNr, generalizationContextNr, specificationContextNr, generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a specification substitution conclusion or question for generalization word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
										}
									}
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add an assumpted specification" );
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find or create a specification justification" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a justification" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given justification type number is not an assumption" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"He is my loving ally and my fortress,
 *	my tower of safety, my rescuer.
 *	He is my shield, and I take refuge in him.
 *	He makes the nations submit to me." (Psalm 144:2)
 *
 *************************************************************************/
