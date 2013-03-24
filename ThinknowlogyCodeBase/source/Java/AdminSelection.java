/*
 *	Class:			AdminSelection
 *	Supports class:	AdminItem
 *	Purpose:		To process selections
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

class AdminSelection
	{
	// Private constructible variables

	private SelectionItem firstSelectionItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte removeDuplicateSelection()
		{
		if( admin_.conditionList != null )
			{
			if( admin_.actionList != null )
				{
				if( admin_.conditionList.deleteActiveItemsWithCurrentSentenceNr() == Constants.RESULT_OK )
					{
					if( admin_.actionList.deleteActiveItemsWithCurrentSentenceNr() == Constants.RESULT_OK )
						{
						if( admin_.alternativeList != null )
							{
							if( admin_.alternativeList.deleteActiveItemsWithCurrentSentenceNr() != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove the alternative of a selection" );
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove the action of a selection" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove the condition of a selection" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The action list isn't created yet" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The condition list isn't created yet" );

		return CommonVariables.result;
		}

	private SelectionItem firstCondition()
		{
		if( admin_.conditionList != null )
			return admin_.conditionList.firstActiveSelectionItem();

		return null;
		}


	// Constructor

	protected AdminSelection( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		firstSelectionItem_ = null;

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

	protected byte checkForDuplicateSelection()
		{
		SelectionResultType selectionResult = new SelectionResultType();
		boolean foundDuplicateSelection = false;
		int duplicateConditionSentenceNr;

		if( admin_.conditionList != null )
			{
			if( admin_.actionList != null )
				{
				if( ( selectionResult = admin_.conditionList.checkDuplicateCondition() ).result == Constants.RESULT_OK )
					{
					if( ( duplicateConditionSentenceNr = selectionResult.duplicateConditionSentenceNr ) > Constants.NO_SENTENCE_NR )
						{
						if( ( selectionResult = admin_.actionList.checkDuplicateSelectionPart( duplicateConditionSentenceNr ) ).result == Constants.RESULT_OK )
							{
							if( selectionResult.foundDuplicateSelection )
								{
								if( admin_.alternativeList == null )
									foundDuplicateSelection = true;
								else
									{
									if( ( selectionResult = admin_.alternativeList.checkDuplicateSelectionPart( duplicateConditionSentenceNr ) ).result == Constants.RESULT_OK )
										{
										if( selectionResult.foundDuplicateSelection )
											foundDuplicateSelection = true;
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check if the alternative selection part is duplicate" );
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							foundDuplicateSelection )
								{
								if( removeDuplicateSelection() != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove a duplicate selection" );
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check if the action selection part is duplicate" );
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check if the condition selection part is duplicate" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The action list isn't created yet" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The condition list isn't created yet" );

		return CommonVariables.result;
		}

	protected byte createSelectionPart( boolean isAction, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isFirstComparisonPart, boolean isNewStart, boolean isNegative, boolean isPossessive, boolean isValueSpecification, short selectionLevel, short selectionListNr, short imperativeParameter, short prepositionParameter, short specificationWordParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SelectionResultType selectionResult = new SelectionResultType();

		if( generalizationWordItem != null ||
		specificationString != null )
			{
			switch( selectionListNr )
				{
				case Constants.ADMIN_CONDITION_LIST:
					if( admin_.conditionList == null )
						{
						if( ( admin_.conditionList = new SelectionList( Constants.ADMIN_CONDITION_LIST_SYMBOL, myWord_ ) ) != null )
							{
							CommonVariables.adminConditionList = admin_.conditionList;
							admin_.adminList[Constants.ADMIN_CONDITION_LIST] = admin_.conditionList;
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create an admin condition list" );
						}

					if( ( selectionResult = admin_.conditionList.createSelectionItem( isAction, isAssignedOrClear, isDeactive, isArchive, isFirstComparisonPart, isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
						{
						if( firstSelectionItem_ == null )
							firstSelectionItem_ = selectionResult.lastCreatedSelectionItem;
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a copy of a temporary generalization noun selection item in the admin condition list" );

					break;

				case Constants.ADMIN_ACTION_LIST:
					if( admin_.actionList == null )
						{
						if( ( admin_.actionList = new SelectionList( Constants.ADMIN_ACTION_LIST_SYMBOL, myWord_ ) ) != null )
							{
							CommonVariables.adminActionList = admin_.actionList;
							admin_.adminList[Constants.ADMIN_ACTION_LIST] = admin_.actionList;
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create an admin action list" );
						}

					if( ( selectionResult = admin_.actionList.createSelectionItem( false, isAssignedOrClear, isDeactive, isArchive, isFirstComparisonPart, isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
						{
						if( firstSelectionItem_ == null )
							firstSelectionItem_ = selectionResult.lastCreatedSelectionItem;
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a copy of a temporary generalization noun selection item in the admin action list" );

					break;

				case Constants.ADMIN_ALTERNATIVE_LIST:
					if( admin_.alternativeList == null )
						{
						if( ( admin_.alternativeList = new SelectionList( Constants.ADMIN_ALTERNATIVE_LIST_SYMBOL, myWord_ ) ) != null )
							{
							CommonVariables.adminAlternativeList = admin_.alternativeList;
							admin_.adminList[Constants.ADMIN_ALTERNATIVE_LIST] = admin_.alternativeList;
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create an admin alternative list" );
						}

					if( ( selectionResult = admin_.alternativeList.createSelectionItem( false, isAssignedOrClear, isDeactive, isArchive, isFirstComparisonPart, isNewStart, isNegative, isPossessive, isValueSpecification, selectionLevel, imperativeParameter, prepositionParameter, specificationWordParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, generalizationWordItem, specificationWordItem, relationWordItem, specificationString ) ).result == Constants.RESULT_OK )
						{
						if( firstSelectionItem_ == null )
							firstSelectionItem_ = selectionResult.lastCreatedSelectionItem;
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a copy of a temporary generalization noun selection item in the admin alternative list" );

					break;

				default:
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given list number is invalid: " + selectionListNr );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word or specification string is undefined" );

		return CommonVariables.result;
		}

	protected byte executeSelection( int endSolveProgress, SelectionItem actionSelectionItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		boolean isNewStart;
		boolean isSatisfied;
		boolean waitForNewStart;
		boolean waitForNewLevel;
		boolean waitForExecution;
		boolean doneLastExecution;
		boolean initializeVariables;
		short executionLevel;
		short executionListNr;
		short selectionLevel;
		short nSelectionExecutions = 0;
		int executionSentenceNr;
		WordItem conditionWordItem;
		SelectionItem conditionSelectionItem;
		SelectionItem executionSelectionItem;

		do	{
			doneLastExecution = false;
			CommonVariables.isAssignmentChanged = false;

			if( ( conditionSelectionItem = firstCondition() ) != null )
				{
				isSatisfied = false;
				waitForNewStart = false;
				waitForNewLevel = false;
				waitForExecution = false;
				executionLevel = conditionSelectionItem.selectionLevel();
				executionSentenceNr = conditionSelectionItem.activeSentenceNr();

				do	{
					if( conditionSelectionItem == null ||
					executionSentenceNr != conditionSelectionItem.activeSentenceNr() )
						{
						executionListNr = ( isSatisfied ? Constants.ADMIN_ACTION_LIST : Constants.ADMIN_ALTERNATIVE_LIST );
						executionSelectionItem = null;

						if( isSatisfied )
							{
							if( admin_.actionList != null )
								executionSelectionItem = admin_.actionList.executionStartEntry( executionLevel, executionSentenceNr );
							}
						else
							{
							if( admin_.alternativeList != null )
								executionSelectionItem = admin_.alternativeList.executionStartEntry( executionLevel, executionSentenceNr );
							}

						if( executionSelectionItem != null )
							{
							initializeVariables = true;

							do	{
								if( admin_.executeImperative( initializeVariables, executionListNr, executionSelectionItem.imperativeParameter(), executionSelectionItem.specificationWordParameter(), executionSelectionItem.specificationWordTypeNr(), endSolveProgress, executionSelectionItem.specificationString(), executionSelectionItem.generalizationWordItem(), executionSelectionItem.specificationWordItem(), null, null, executionSelectionItem, actionSelectionItem ) == Constants.RESULT_OK )
									initializeVariables = false;
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute an imperative" );
								}
							while( CommonVariables.result == Constants.RESULT_OK &&
							!admin_.isRestart() &&
							!CommonVariables.hasShownWarning &&
							( executionSelectionItem = executionSelectionItem.nextExecutionItem( executionLevel, executionSentenceNr ) ) != null );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						conditionSelectionItem != null )		// Found new condition
							{
							isSatisfied = false;
							waitForNewStart = false;
							waitForNewLevel = false;
							waitForExecution = false;
							executionLevel = conditionSelectionItem.selectionLevel();
							}
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( conditionSelectionItem == null )
							doneLastExecution = true;
						else
							{
							isNewStart = conditionSelectionItem.isNewStart();
							selectionLevel = conditionSelectionItem.selectionLevel();

							if( isNewStart &&
							waitForNewStart &&
							executionLevel == selectionLevel )	// Found new start
								waitForNewStart = false;

							if( waitForNewLevel &&
							executionLevel != selectionLevel )	// Found new level
								waitForNewLevel = false;

							if( !waitForNewStart &&
							!waitForNewLevel &&
							!waitForExecution )
								{
								if( !isNewStart &&
								!isSatisfied &&
								executionLevel == selectionLevel )
									waitForNewStart = true;		// Skip checking of this condition part and wait for a new start to come on this level
								else
									{
									if( isNewStart &&
									isSatisfied &&
									executionLevel == selectionLevel )
										waitForNewLevel = true;		// Skip checking of this condition part and wait for a new level to come
									else
										{
										if( executionLevel != selectionLevel &&
										isSatisfied != conditionSelectionItem.isAction() )
											waitForExecution = true;	// Skip checking of this condition and wait for the next condition sentence number to come
										else
											{
											conditionWordItem = conditionSelectionItem.generalizationWordItem();

											if( conditionWordItem != null )
												{
												if( ( selectionResult = admin_.checkCondition( conditionSelectionItem ) ).result == Constants.RESULT_OK )
													{
													isSatisfied = selectionResult.isConditionSatisfied;
													executionLevel = selectionLevel;
													executionSentenceNr = conditionSelectionItem.activeSentenceNr();
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the condition of word \"" + conditionWordItem.anyWordTypeString() + "\"" );
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined condition word" );
											}
										}
									}
								}

							conditionSelectionItem = conditionSelectionItem.nextSelectionItem();
							}
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				!doneLastExecution &&
				!admin_.isRestart() &&
				!CommonVariables.hasShownWarning );
				}
			}
		while( CommonVariables.result == Constants.RESULT_OK &&
		!admin_.isRestart() &&
		!CommonVariables.hasShownWarning &&
		CommonVariables.isAssignmentChanged &&
		++nSelectionExecutions < Constants.MAX_SELECTION_EXECUTIONS );

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.isAssignmentChanged &&
		nSelectionExecutions == Constants.MAX_SELECTION_EXECUTIONS )
			return myWord_.setErrorInItem( 1, moduleNameString_, "I think there is an endless loop in the selections" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Praise his name with dancing,
 *	accompanied by tambourine and harp.
 *	For the Lord delights in his people;
 *	he crowns the humble with victory." (Psalm 149:3-4)
 *
 *************************************************************************/
