/*
 *	Class:			AdminSolve
 *	Supports class:	AdminItem
 *	Purpose:		Trying to solve (= assign) words according the selections
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

class AdminSolve
	{
	// Private constructible variables

	private boolean canWordBeSolved_;
	private boolean foundPossibility_;
	private boolean foundScoringAssignment_;
	private boolean isConditionSatisfied_;

	private short solveLevel_;

	private long oldSatisfiedScore_;
	private long newSatisfiedScore_;
	private long oldDissatisfiedScore_;
	private long newDissatisfiedScore_;
	private long oldNotBlockingScore_;
	private long newNotBlockingScore_;
	private long oldBlockingScore_;
	private long newBlockingScore_;

	private int currentSolveProgress_;

	private SelectionItem currentExecutionSelectionItem_;

	private SpecificationItem comparisonAssignmentItem_;
	private SpecificationItem firstComparisonAssignmentItem_;
	private SpecificationItem secondComparisonAssignmentItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private void clearAllWordSolveChecksInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.isWordCheckedForSolving = false;
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		}

	private boolean isNumeralString( String checkString )
		{
		int stringLength;
		int position = 0;

		if( checkString != null &&
		( stringLength = checkString.length() ) > 0 )
			{
			while( position < stringLength &&
			Character.isDigit( checkString.charAt( position ) ) )
				position++;

			return ( position == stringLength );
			}

		return false;
		}

	private byte getComparisonAssignment( boolean isNumeralRelation, WordItem specificationWordItem, WordItem relationWordItem )
		{
		comparisonAssignmentItem_ = null;

		if( specificationWordItem != null )
			{
			if( isNumeralRelation )
				comparisonAssignmentItem_ = specificationWordItem.firstActiveNumeralAssignment();
			else
				{
				if( relationWordItem == null )
					comparisonAssignmentItem_ = specificationWordItem.firstActiveStringAssignment();
				else
					{
					if( specificationWordItem.isNounHead() )
						comparisonAssignmentItem_ = relationWordItem.lastActiveAssignmentButNotAQuestion();
					else
						{
						if( specificationWordItem.isNounTail() )
							comparisonAssignmentItem_ = relationWordItem.firstActiveAssignmentButNotAQuestion();
						else
							{
							if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_START, specificationWordItem.anyWordTypeString(), Constants.INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_END ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
							}
						}
					}
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte deleteAssignmentLevelInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.deleteAssignmentLevelInWord();
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return CommonVariables.result;
		}

	private byte backTrackConditionScorePaths( boolean addScores, boolean isInverted, boolean duplicatesAllowed, boolean prepareSort, short executionLevel, short solveStrategyParameter, int conditionSentenceNr )
		{
		boolean isNewStart;
		boolean waitForNewLevel;
		boolean waitForNewStart;
		boolean foundScore = false;
		short selectionLevel;
		short handledSelectionLevel;
		short previousSelectionLevel;
		short unhandledSelectionLevel;
		SelectionItem conditionSelectionItem;
		SelectionItem previousConditionSelectionItem;

		if( admin_.conditionList != null )
			{
			admin_.conditionList.clearConditionChecksForSolving( Constants.MAX_LEVEL, conditionSentenceNr );

			do	{
				waitForNewLevel = false;
				waitForNewStart = false;
				handledSelectionLevel = Constants.MAX_LEVEL;
				previousSelectionLevel = Constants.NO_SELECTION_LEVEL;
				unhandledSelectionLevel = Constants.MAX_LEVEL;
				previousConditionSelectionItem = null;

				if( ( conditionSelectionItem = admin_.conditionList.firstConditionSelectionItem( conditionSentenceNr ) ) != null )
					{
					if( addScores )
						{
						oldSatisfiedScore_ = Constants.NO_SCORE;
						newSatisfiedScore_ = Constants.NO_SCORE;
						oldDissatisfiedScore_ = Constants.NO_SCORE;
						newDissatisfiedScore_ = Constants.NO_SCORE;
						oldNotBlockingScore_ = Constants.NO_SCORE;
						newNotBlockingScore_ = Constants.NO_SCORE;
						oldBlockingScore_ = Constants.NO_SCORE;
						newBlockingScore_ = Constants.NO_SCORE;
						}

					do	{
						isNewStart = conditionSelectionItem.isNewStart();
						selectionLevel = conditionSelectionItem.selectionLevel();

						if( conditionSelectionItem.isConditionCheckedForSolving )
							waitForNewStart = true;
						else
							{
							if( isNewStart &&
							previousConditionSelectionItem != null )											// Not first start item
								{
								waitForNewStart = false;

								if( selectionLevel == previousSelectionLevel )			// Second brance on the same level
									{
									if( !waitForNewLevel )
										{
										if( handledSelectionLevel == Constants.MAX_LEVEL )
											{
											handledSelectionLevel = selectionLevel;

											if( previousConditionSelectionItem.isConditionCheckedForSolving )	// This is a new brance
												conditionSelectionItem.isConditionCheckedForSolving = true;
											else						// The previous brance was a new brance and this one is unhandled
												{
												waitForNewLevel = true;
												unhandledSelectionLevel = selectionLevel;
												previousConditionSelectionItem.isConditionCheckedForSolving = true;
												}
											}
										else
											{
											waitForNewLevel = true;

											if( unhandledSelectionLevel == Constants.MAX_LEVEL )	// This brance isn't handled yet
												unhandledSelectionLevel = selectionLevel;
											}
										}
									}
								else									// New start on a new level
									waitForNewLevel = false;
								}

							if( !waitForNewLevel &&
							!waitForNewStart )
								{
								if( calculateScorePaths( isInverted, duplicatesAllowed, prepareSort, solveStrategyParameter, conditionSelectionItem ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to calculate the score paths" );
								}
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							previousSelectionLevel = selectionLevel;

							if( isNewStart )
								previousConditionSelectionItem = conditionSelectionItem;
							}
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					( conditionSelectionItem = conditionSelectionItem.nextConditionItem( executionLevel, conditionSentenceNr ) ) != null );

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( conditionSelectionItem != null ||
						previousSelectionLevel == executionLevel )
							{
							// All brances on same level are done and there are brances on a higher level unhandled,
							// so start again with the handled brances (by clearing their checks) until all paths are handled
							if( unhandledSelectionLevel < Constants.MAX_LEVEL &&
							handledSelectionLevel < unhandledSelectionLevel )
								admin_.conditionList.clearConditionChecksForSolving( unhandledSelectionLevel, conditionSentenceNr );

							if( addScores )
								{
								if( admin_.scoreList == null ||
								( oldSatisfiedScore_ == Constants.NO_SCORE &&
								newSatisfiedScore_ == Constants.NO_SCORE &&
								oldDissatisfiedScore_ == Constants.NO_SCORE &&
								newDissatisfiedScore_ == Constants.NO_SCORE &&
								oldNotBlockingScore_ == Constants.NO_SCORE &&
								newNotBlockingScore_ == Constants.NO_SCORE &&
								oldBlockingScore_ == Constants.NO_SCORE &&
								newBlockingScore_ == Constants.NO_SCORE ) )
									foundScore = false;
								else
									{
									if( admin_.scoreList.checkScores( isInverted, solveStrategyParameter, oldSatisfiedScore_, newSatisfiedScore_, oldDissatisfiedScore_, newDissatisfiedScore_, oldNotBlockingScore_, newNotBlockingScore_, oldBlockingScore_, newBlockingScore_ ) == Constants.RESULT_OK )
										foundScore = admin_.scoreList.foundScore();
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the scores" );
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								!foundScore &&
								duplicatesAllowed &&
								CommonVariables.currentAssignmentLevel > Constants.NO_ASSIGNMENT_LEVEL )
									{
									if( createScore( ( CommonVariables.currentAssignmentLevel > Constants.NO_ASSIGNMENT_LEVEL ), oldSatisfiedScore_, newSatisfiedScore_, oldDissatisfiedScore_, newDissatisfiedScore_, oldNotBlockingScore_, newNotBlockingScore_, oldBlockingScore_, newBlockingScore_, null ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an empty solve score" );
									}
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't reach the given execution level " + executionLevel + ". The highest reached level was " + handledSelectionLevel );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't get the first item of the condition with sentence number " + conditionSentenceNr );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			unhandledSelectionLevel < Constants.MAX_LEVEL );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The condition list isn't created yet" );

		return CommonVariables.result;
		}

	private byte canWordBeSolved( boolean isAction, WordItem solveWordItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		SelectionItem originalExecutionSelectionItem = currentExecutionSelectionItem_;
		WordItem specificationWordItem;

		canWordBeSolved_ = false;

		if( isAction )
			{
			if( admin_.actionList != null )
				{
				if( ( selectionResult = admin_.actionList.findFirstExecutionItem( solveWordItem ) ).result == Constants.RESULT_OK )
					currentExecutionSelectionItem_ = selectionResult.firstExecutionItem;
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the first action execution selection item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
				}
			}
		else
			{
			if( admin_.alternativeList != null )
				{
				if( ( selectionResult = admin_.alternativeList.findFirstExecutionItem( solveWordItem ) ).result == Constants.RESULT_OK )
					currentExecutionSelectionItem_ = selectionResult.firstExecutionItem;
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the first alternative execution selection item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
				}
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		currentExecutionSelectionItem_ != null )
			{
			do	{
				if( ( specificationWordItem = currentExecutionSelectionItem_.specificationWordItem() ) != null )
					{
					if( specificationWordItem.firstActiveAssignmentButNotAQuestion() == null )
						{
						if( currentExecutionSelectionItem_.isValueSpecification() )
							{
							if( canWordBeSolved( specificationWordItem ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if value specification word \"" + specificationWordItem.anyWordTypeString() + "\" can be solved" );
							}
						else
							{
							if( !currentExecutionSelectionItem_.isNegative() )
								canWordBeSolved_ = true;
							}
						}
					else	// Word has active assignments
						canWordBeSolved_ = true;

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( currentExecutionSelectionItem_.findNextExecutionSelectionItem( solveWordItem ) == Constants.RESULT_OK )
							currentExecutionSelectionItem_ = currentExecutionSelectionItem_.nextExecutionItem();
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the next execution selection item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The specification word of the current execution selection item is undefined" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			!canWordBeSolved_ &&
			currentExecutionSelectionItem_ != null );
			}

		if( CommonVariables.result == Constants.RESULT_OK )
			currentExecutionSelectionItem_ = originalExecutionSelectionItem;

		return CommonVariables.result;
		}

	protected byte checkComparison( SelectionItem conditionSelectionItem )
		{
		boolean isNegative;
		boolean isNumeralRelation;
		int comparisonResult = 0;
		int firstNumeral = 0;
		int secondNumeral = 0;
		String firstString = null;
		String secondString = null;
		WordItem generalizationWordItem;
		WordItem specificationWordItem;
		WordItem firstSpecificationWordItem;
		WordItem secondSpecificationWordItem;

		isConditionSatisfied_ = false;

		if( conditionSelectionItem != null )
			{
			if( ( generalizationWordItem = conditionSelectionItem.generalizationWordItem() ) != null )
				{
				if( ( specificationWordItem = conditionSelectionItem.specificationWordItem() ) != null )
					{
					isNegative = conditionSelectionItem.isNegative();

					if( conditionSelectionItem.isFirstComparisonPart() )			// First part
						{
						if( getComparisonAssignment( false, specificationWordItem, conditionSelectionItem.relationWordItem() ) == Constants.RESULT_OK )
							{
							isConditionSatisfied_ = true;	// Allow the second part of the comparison to be checked

							firstComparisonAssignmentItem_ = comparisonAssignmentItem_;
							secondComparisonAssignmentItem_ = null;
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed get the first comparison assignment" );
						}
					else
						{
						isNumeralRelation = conditionSelectionItem.isNumeralRelation();

						if( !isNumeralRelation &&
						conditionSelectionItem.specificationString() == null )		// Second part
							{
							if( getComparisonAssignment( false, specificationWordItem, conditionSelectionItem.relationWordItem() ) == Constants.RESULT_OK )
								{
								secondComparisonAssignmentItem_ = comparisonAssignmentItem_;

								firstSpecificationWordItem = ( firstComparisonAssignmentItem_ == null ? null : firstComparisonAssignmentItem_.specificationWordItem() );
								secondSpecificationWordItem = ( secondComparisonAssignmentItem_ == null ? null : secondComparisonAssignmentItem_.specificationWordItem() );

								firstString = ( firstSpecificationWordItem == null ? null : firstSpecificationWordItem.anyWordTypeString() );
								secondString = ( secondSpecificationWordItem == null ? null : secondSpecificationWordItem.anyWordTypeString() );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed get a comparison assignment" );
							}
						else	// Numeral or specification string
							{
							if( getComparisonAssignment( isNumeralRelation, specificationWordItem, conditionSelectionItem.relationWordItem() ) == Constants.RESULT_OK )
								{
								firstString = ( comparisonAssignmentItem_ == null ? null : ( isNumeralRelation ? ( comparisonAssignmentItem_.specificationWordItem() == null ? null : comparisonAssignmentItem_.specificationWordItem().anyWordTypeString() ) : comparisonAssignmentItem_.specificationString() ) );
								secondString = ( isNumeralRelation ? ( conditionSelectionItem.relationWordItem() == null ? null : conditionSelectionItem.relationWordItem().anyWordTypeString() ) : conditionSelectionItem.specificationString() );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed get the first comparison assignment" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( firstString == null ||
							secondString == null )
								comparisonResult = ( firstString == null && secondString == null ? 0 : ( firstString == null ? -1 : 1 ) );
							else
								{
								if( isNumeralString( firstString ) &&
								isNumeralString( secondString ) )
									{
									firstNumeral = Integer.parseInt( firstString );
									secondNumeral = Integer.parseInt( secondString );

									comparisonResult = ( firstNumeral == secondNumeral ? 0 : ( firstNumeral < secondNumeral ? -1 : 1 ) );
									}
								else
									comparisonResult = firstString.compareTo( secondString );
								}

							if( generalizationWordItem.isAdjectiveComparisonLess() )
								isConditionSatisfied_ = ( comparisonResult < 0 ? !isNegative : isNegative );
							else
								{
								if( generalizationWordItem.isAdjectiveComparisonEqual() )
									isConditionSatisfied_ = ( comparisonResult == 0 ? !isNegative : isNegative );
								else
									{
									if( generalizationWordItem.isAdjectiveComparisonMore() )
										isConditionSatisfied_ = ( comparisonResult > 0 ? !isNegative : isNegative );
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "Word \"" + generalizationWordItem.anyWordTypeString() + "\" is not comparison word" );
									}
								}
							}
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The specification word of the given condition selection item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The generalization word of the given condition selection item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given condition selection item is undefined" );

		return CommonVariables.result;
		}

	protected byte checkForOddOrEven( SelectionItem conditionSelectionItem )
		{
		boolean isPossessive;
		boolean isNegative;
		int nAssignments;
		int relationContextNr;
		WordItem generalizationWordItem;
		WordItem specificationWordItem;
		WordItem relationWordItem;

		isConditionSatisfied_ = false;

		if( conditionSelectionItem != null )
			{
			if( ( generalizationWordItem = conditionSelectionItem.generalizationWordItem() ) != null )
				{
				if( ( specificationWordItem = conditionSelectionItem.specificationWordItem() ) != null )
					{
					isPossessive = conditionSelectionItem.isPossessive();
					isNegative = conditionSelectionItem.isNegative();
					relationContextNr = conditionSelectionItem.relationContextNr();

					if( ( relationWordItem = myWord_.contextWordInAllWords( isPossessive, relationContextNr, specificationWordItem, null ) ) != null )
						{
						if( specificationWordItem.isNounNumber() )
							{
							nAssignments = relationWordItem.numberOfActiveAssignments();

							if( generalizationWordItem.isAdjectiveOdd() )
								isConditionSatisfied_ = ( nAssignments % 2 == 1 ? !isNegative : isNegative );
							else
								{
								if( generalizationWordItem.isAdjectiveEven() )
									isConditionSatisfied_ = ( nAssignments % 2 == 0 ? !isNegative : isNegative );
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "Word \"" + generalizationWordItem.anyWordTypeString() + "\" is not about odd or even" );
								}
							}
						else
							{
							if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_START, relationWordItem.anyWordTypeString(), Constants.INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHAT_TO_IMPERATIVE_END ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the relation word" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The specification word of the given condition selection item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The generalization word of the given condition selection item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given condition selection item is undefined" );

		return CommonVariables.result;
		}

	private byte createScore( boolean isChecked, long oldSatisfiedScore, long newSatisfiedScore, long oldDissatisfiedScore, long newDissatisfiedScore, long oldNotBlockingScore, long newNotBlockingScore, long oldBlockingScore, long newBlockingScore, SelectionItem referenceSelectionItem )
		{
		if( admin_.scoreList == null )
			{
			if( ( admin_.scoreList = new ScoreList( myWord_ ) ) != null )
				{
				CommonVariables.adminScoreList = admin_.scoreList;
				admin_.adminList[Constants.ADMIN_SCORE_LIST] = admin_.scoreList;
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create the admin solve score list" );
			}

		if( admin_.scoreList.createScoreItem( isChecked, oldSatisfiedScore, newSatisfiedScore, oldDissatisfiedScore, newDissatisfiedScore, oldNotBlockingScore, newNotBlockingScore, oldBlockingScore, newBlockingScore, referenceSelectionItem ) != Constants.RESULT_OK )
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a score item" );

		return CommonVariables.result;
		}

	private byte findScoringAssignment( boolean isBlocking, WordItem generalizationWordItem )
		{
		SpecificationItem currentAssignmentItem;

		foundScoringAssignment_ = false;

		if( generalizationWordItem != null )
			{
			if( ( currentAssignmentItem = generalizationWordItem.firstActiveAssignmentButNotAQuestion() ) != null )
				{
				do	{
					if( !currentAssignmentItem.isNegative() )
						{
						foundScoringAssignment_ = true;

						if( isBlocking )
							{
							if( currentAssignmentItem.isOlderSentence() )
								oldBlockingScore_++;
							else
								newBlockingScore_++;
							}
						else
							{
							if( currentAssignmentItem.isOlderSentence() )
								oldNotBlockingScore_++;
							else
								newNotBlockingScore_++;
							}
						}
					}
				while( !foundScoringAssignment_ &&
				( currentAssignmentItem = currentAssignmentItem.nextAssignmentItemButNotAQuestion() ) != null );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}

	private byte findScoringAssignment( boolean isPossessive, boolean isSatisfiedScore, int relationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		SpecificationItem currentAssignmentItem;

		foundScoringAssignment_ = false;

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				if( ( currentAssignmentItem = generalizationWordItem.firstActiveAssignmentButNotAQuestion() ) != null )
					{
					do	{
						if( currentAssignmentItem.isRelatedSpecification( false, isPossessive, relationContextNr, specificationWordItem ) )
							{
							foundScoringAssignment_ = true;

							if( isSatisfiedScore )
								{
								if( currentAssignmentItem.isOlderSentence() )
									oldSatisfiedScore_++;
								else
									newSatisfiedScore_++;
								}
							else
								{
								if( currentAssignmentItem.isOlderSentence() )
									oldDissatisfiedScore_++;
								else
									newDissatisfiedScore_++;
								}
							}
						}
					while( !foundScoringAssignment_ &&
					( currentAssignmentItem = currentAssignmentItem.nextAssignmentItemButNotAQuestion() ) != null );
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		return CommonVariables.result;
		}

	private byte checkConditionByValue( boolean isNegative, boolean isPossessive, int generalizationContextNr, int specificationContextNr, WordItem generalizationWordItem, WordItem specificationWordItem )
		{
		boolean isSatisfiedScore;
		SpecificationItem foundAssignmentItem;
		SpecificationItem currentSpecificationItem;

		isConditionSatisfied_ = true;

		if( specificationWordItem != null )
			{
			if( ( currentSpecificationItem = specificationWordItem.firstSpecificationButNotAQuestion() ) != null )
				{
				do	{
					foundAssignmentItem = specificationWordItem.firstAssignment( true, false, false, false, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, currentSpecificationItem.relationContextNr(), currentSpecificationItem.specificationWordItem(), currentSpecificationItem.specificationString() );
					isSatisfiedScore = ( isNegative == ( foundAssignmentItem == null || foundAssignmentItem.isNegative() ) );

					if( findScoringAssignment( isPossessive, isSatisfiedScore, currentSpecificationItem.relationContextNr(), generalizationWordItem, currentSpecificationItem.specificationWordItem() ) == Constants.RESULT_OK )
						{
						if( foundScoringAssignment_ != isSatisfiedScore )
							isConditionSatisfied_ = false;
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a scoring assignment" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemButNotAQuestion() ) != null );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );

		return CommonVariables.result;
		}

	private byte calculateScorePaths( boolean isInverted, boolean duplicatesAllowed, boolean prepareSort, short solveStrategyParameter, SelectionItem referenceSelectionItem )
		{
		boolean addLocalScores;
		boolean originalfoundpossibility;
		boolean foundScore = false;
		long localOldSatisfiedScore;
		long localNewSatisfiedScore;
		long localOldDissatisfiedScore;
		long localNewDissatisfiedScore;
		long localOldNotBlockingScore;
		long localNewNotBlockingScore;
		long localOldBlockingScore;
		long localNewBlockingScore;
		long oldSatisfiedScore = oldSatisfiedScore_;
		long newSatisfiedScore = newSatisfiedScore_;
		long oldDissatisfiedScore = oldDissatisfiedScore_;
		long newDissatisfiedScore = newDissatisfiedScore_;
		long oldNotBlockingScore = oldNotBlockingScore_;
		long newNotBlockingScore = newNotBlockingScore_;
		long oldBlockingScore = oldBlockingScore_;
		long newBlockingScore = newBlockingScore_;
		WordItem generalizationWordItem;
		WordItem specificationWordItem;

		if( referenceSelectionItem != null )
			{
			if( ( generalizationWordItem = referenceSelectionItem.generalizationWordItem() ) != null )
				{
				oldSatisfiedScore_ = Constants.NO_SCORE;
				newSatisfiedScore_ = Constants.NO_SCORE;
				oldDissatisfiedScore_ = Constants.NO_SCORE;
				newDissatisfiedScore_ = Constants.NO_SCORE;
				oldNotBlockingScore_ = Constants.NO_SCORE;
				newNotBlockingScore_ = Constants.NO_SCORE;
				oldBlockingScore_ = Constants.NO_SCORE;
				newBlockingScore_ = Constants.NO_SCORE;

				if( checkCondition( referenceSelectionItem ).result == Constants.RESULT_OK )
					{
					addLocalScores = true;

					localOldSatisfiedScore = oldSatisfiedScore_;
					localNewSatisfiedScore = newSatisfiedScore_;
					localOldDissatisfiedScore = oldDissatisfiedScore_;
					localNewDissatisfiedScore = newDissatisfiedScore_;
					localOldNotBlockingScore = oldNotBlockingScore_;
					localNewNotBlockingScore = newNotBlockingScore_;
					localOldBlockingScore = oldBlockingScore_;
					localNewBlockingScore = newBlockingScore_;

					oldSatisfiedScore_ = oldSatisfiedScore;
					newSatisfiedScore_ = newSatisfiedScore;
					oldDissatisfiedScore_ = oldDissatisfiedScore;
					newDissatisfiedScore_ = newDissatisfiedScore;
					oldNotBlockingScore_ = oldNotBlockingScore;
					newNotBlockingScore_ = newNotBlockingScore;
					oldBlockingScore_ = oldBlockingScore;
					newBlockingScore_ = newBlockingScore;

					if( !isConditionSatisfied_ &&
					!generalizationWordItem.isWordCheckedForSolving )
						{
						if( referenceSelectionItem.isAssignedOrClear() )
							{
							if( !referenceSelectionItem.isNegative() )
								{
								if( generalizationWordItem.firstActiveAssignmentButNotAQuestion() == null )		// Word has no active assignments
									{
									originalfoundpossibility = foundPossibility_;

									if( admin_.findPossibilityToSolveWord( false, isInverted, duplicatesAllowed, prepareSort, solveStrategyParameter, generalizationWordItem ) == Constants.RESULT_OK )
										{
										if( foundPossibility_ )
											addLocalScores = false;
										else
											foundPossibility_ = originalfoundpossibility;
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a possibility to solve condition word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
									}
								}
							}
						else
							{
							if( ( specificationWordItem = referenceSelectionItem.specificationWordItem() ) != null )
								{
								if( !referenceSelectionItem.isNegative() &&
								!specificationWordItem.isWordCheckedForSolving &&
								generalizationWordItem.firstActiveAssignmentButNotAQuestion() == null )		// Word has no active assignments
									{
									if( canWordBeSolved( specificationWordItem ) == Constants.RESULT_OK )
										{
										if( canWordBeSolved_ )
											{
											originalfoundpossibility = foundPossibility_;

											if( admin_.findPossibilityToSolveWord( false, isInverted, duplicatesAllowed, prepareSort, solveStrategyParameter, generalizationWordItem ) == Constants.RESULT_OK )
												{
												if( foundPossibility_ )
													addLocalScores = false;
												else
													foundPossibility_ = originalfoundpossibility;
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a possibility to solve condition word \"" + generalizationWordItem.anyWordTypeString() + "\"" );
											}
										else
											{
											if( duplicatesAllowed ||
											admin_.scoreList == null )
												foundScore = false;
											else
												{
												if( admin_.scoreList.findScore( prepareSort, referenceSelectionItem ) == Constants.RESULT_OK )
													foundScore = admin_.scoreList.foundScore();
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a score item" );
												}

											if( CommonVariables.result == Constants.RESULT_OK &&
											!foundScore )
												{
												if( createScore( ( CommonVariables.currentAssignmentLevel > Constants.NO_ASSIGNMENT_LEVEL ), Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, referenceSelectionItem ) == Constants.RESULT_OK )
													foundPossibility_ = true;
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an empty solve score" );
												}
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if specification word \"" + specificationWordItem.anyWordTypeString() + "\" can be solved" );
									}
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The specification word of the given score item is undefined" );
							}
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					!CommonVariables.hasShownWarning &&
					addLocalScores )
						{
						oldSatisfiedScore_ += localOldSatisfiedScore;
						newSatisfiedScore_ += localNewSatisfiedScore;
						oldDissatisfiedScore_ += localOldDissatisfiedScore;
						newDissatisfiedScore_ += localNewDissatisfiedScore;
						oldNotBlockingScore_ += localOldNotBlockingScore;
						newNotBlockingScore_ += localNewNotBlockingScore;
						oldBlockingScore_ += localOldBlockingScore;
						newBlockingScore_ += localNewBlockingScore;
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the condition of the item with sentence number " + referenceSelectionItem.activeSentenceNr() + " and item number " + referenceSelectionItem.itemNr() );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The generalization word of the given score item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given reference selection item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminSolve( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		canWordBeSolved_ = false;
		foundPossibility_ = false;
		foundScoringAssignment_ = false;
		isConditionSatisfied_ = false;

		solveLevel_ = Constants.NO_SOLVE_LEVEL;

		oldSatisfiedScore_ = Constants.NO_SCORE;
		newSatisfiedScore_ = Constants.NO_SCORE;
		oldDissatisfiedScore_ = Constants.NO_SCORE;
		newDissatisfiedScore_ = Constants.NO_SCORE;
		oldNotBlockingScore_ = Constants.NO_SCORE;
		newNotBlockingScore_ = Constants.NO_SCORE;
		oldBlockingScore_ = Constants.NO_SCORE;
		newBlockingScore_ = Constants.NO_SCORE;

		currentSolveProgress_ = 0;

		currentExecutionSelectionItem_ = null;
		comparisonAssignmentItem_ = null;
		firstComparisonAssignmentItem_ = null;
		secondComparisonAssignmentItem_ = null;

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

	protected void clearCurrentSolveProgress()
		{
		currentSolveProgress_ = 0;
		}

	protected byte canWordBeSolved( WordItem solveWordItem )
		{
		if( canWordBeSolved( true, solveWordItem ) == Constants.RESULT_OK )
			{
			if( !canWordBeSolved_ &&
			!CommonVariables.hasShownWarning )
				{
				if( canWordBeSolved( false, solveWordItem ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if a word can be solved by an alternative action" );
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if a word can be solved by an action" );

		return CommonVariables.result;
		}

	protected byte solveWord( int endSolveProgress, WordItem solveWordItem, SelectionItem actionSelectionItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isInverted = false;
		int nPossibilities;
		int possibilityNumber = 0;
		int solveProgressStep;
		int tempEndSolveProgress;
		ScoreItem possibilityItem;
		SpecificationItem foundSpecificationItem;
		WordItem predefinedNounSolveLevelWordItem;
		WordItem predefinedNounSolveMethodWordItem;
		WordItem predefinedNounSolveStrategyWordItem;

		if( solveWordItem != null )
			{
			if( ( predefinedNounSolveMethodWordItem = admin_.predefinedNounSolveMethodWordItem() ) != null )
				{
				if( ( predefinedNounSolveStrategyWordItem = admin_.predefinedNounSolveStrategyWordItem() ) != null )
					{
					if( currentSolveProgress_ == 0 )
						{
						if( ( predefinedNounSolveLevelWordItem = admin_.predefinedNounSolveLevelWordItem() ) == null )
							solveLevel_ = Constants.NO_SOLVE_LEVEL;
						else
							{
							if( ( specificationResult = predefinedNounSolveLevelWordItem.getAssignmentOrderNr( Constants.WORD_TYPE_ADJECTIVE ) ).result == Constants.RESULT_OK )
								solveLevel_ = specificationResult.assignmentOrderNr;
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the solve level" );
							}
						}

					if( solveWordItem.firstActiveAssignmentButNotAQuestion() == null )		// Word has no active assignments
						{
						if( CommonVariables.currentAssignmentLevel <= solveLevel_ )
							{
							if( admin_.assignSpecification( predefinedNounSolveMethodWordItem, admin_.predefinedAdjectiveBusyWordItem() ) == Constants.RESULT_OK )
								{
								if( CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )
									{
									clearAllWordSolveChecksInAllWords();

									if( admin_.scoreList != null )
										{
										if( admin_.scoreList.deleteScores() != Constants.RESULT_OK )	// Make sure no scores are left at the start
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the admin score list" );
										}
									}

								if( CommonVariables.result == Constants.RESULT_OK )
									{
									foundSpecificationItem = predefinedNounSolveMethodWordItem.firstAssignment( true, false, false, false, false, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, admin_.predefinedAdjectiveInvertedWordItem(), null );
									isInverted = ( foundSpecificationItem != null );

									if( ( specificationResult = predefinedNounSolveStrategyWordItem.getAssignmentWordParameter() ).result == Constants.RESULT_OK )
										{
										if( findPossibilityToSolveWord( true, isInverted, ( CommonVariables.currentAssignmentLevel == solveLevel_ ), ( CommonVariables.currentAssignmentLevel + 1 < solveLevel_ ), specificationResult.assignmentParameter, solveWordItem ) == Constants.RESULT_OK )
											{
											if( foundPossibility_ )
												{
												if( CommonVariables.currentAssignmentLevel < solveLevel_ )
													{
													if( admin_.scoreList != null )
														{
														nPossibilities = admin_.scoreList.numberOfPossibilities();
														solveProgressStep = ( ( endSolveProgress - currentSolveProgress_ ) / nPossibilities );

														if( solveLevel_ > 1 )
															Presentation.startProgress( currentSolveProgress_, Constants.MAX_PROGRESS, Constants.INTERFACE_CONSOLE_I_AM_EXECUTING_SELECTIONS_START, solveLevel_, Constants.INTERFACE_CONSOLE_I_AM_EXECUTING_SELECTIONS_END );

														if( admin_.wordList != null )
															{
															if( ( possibilityItem = admin_.scoreList.firstPossibility() ) != null )
																{
																do	{
																	if( CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )	// Copy solve action of Constants.NO_ASSIGNMENT_LEVEL to higher levels
																		actionSelectionItem = possibilityItem.scoreReference();

																	if( admin_.wordList.createNewAssignmentLevelInWordList() == Constants.RESULT_OK )
																		{
																		CommonVariables.currentAssignmentLevel++;

																		if( admin_.assignSelectionSpecification( possibilityItem.scoreReference() ) == Constants.RESULT_OK )
																			{
																			tempEndSolveProgress = currentSolveProgress_ + solveProgressStep;

																			if( admin_.executeSelection( (int)( currentSolveProgress_ + solveProgressStep / 2L ), actionSelectionItem ) == Constants.RESULT_OK )
																				{
																				if( solveWordItem.firstActiveAssignmentButNotAQuestion() != null )		// Word has active assignments
																					{
																					foundSpecificationItem = predefinedNounSolveMethodWordItem.firstAssignment( true, false, false, false, false, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, admin_.predefinedAdjectiveInvertedWordItem(), null );
																					isInverted = ( foundSpecificationItem != null );

																					if( !isInverted &&
																					CommonVariables.currentAssignmentLevel < solveLevel_ )
																						{
																						if( admin_.scoreList.deleteScores() == Constants.RESULT_OK )
																							solveLevel_ = CommonVariables.currentAssignmentLevel;		// Don't solve any deeper when there is a winning score
																						else
																							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the scores with an assignment level higher than " + CommonVariables.currentAssignmentLevel );
																						}

																					if( CommonVariables.result == Constants.RESULT_OK )
																						{
																						// Create winning or losing score
																						if( createScore( false, Constants.NO_SCORE, ( isInverted ? Constants.NO_SCORE : Constants.WINNING_SCORE ), Constants.NO_SCORE, ( isInverted ? Constants.WINNING_SCORE : Constants.NO_SCORE ), Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, Constants.NO_SCORE, actionSelectionItem ) == Constants.RESULT_OK )
																							{
																							currentSolveProgress_ = tempEndSolveProgress;

																							if( solveLevel_ > 1 )
																								Presentation.showProgress( currentSolveProgress_ );
																							}
																						else
																							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a winning or losing score of solve word \"" + solveWordItem.anyWordTypeString() + "\" at assignment level " + CommonVariables.currentAssignmentLevel );
																						}
																					}
																				else
																					{
																					if( solveWord( tempEndSolveProgress, solveWordItem, actionSelectionItem ) == Constants.RESULT_OK )
																						{
																						if( CommonVariables.currentAssignmentLevel == 1 )
																							admin_.scoreList.changeAction( actionSelectionItem );
																						}
																					else
																						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to solve word \"" + solveWordItem.anyWordTypeString() + "\" at assignment level " + CommonVariables.currentAssignmentLevel );
																					}

																				if( CommonVariables.result == Constants.RESULT_OK )
																					{
																					if( deleteAssignmentLevelInAllWords() == Constants.RESULT_OK )
																						{
																						CommonVariables.currentAssignmentLevel--;
																						possibilityItem = possibilityItem.nextPossibilityItem();

																						if( ++possibilityNumber <= nPossibilities )
																							{
																							if( possibilityItem != null &&
																							possibilityNumber == nPossibilities )
																								return myWord_.setErrorInItem( 1, moduleNameString_, "I found more possibility items than number of possibilities" );
																							}
																						else
																							{
																							if( possibilityItem == null )
																								return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't get the next possibility item before the number of possibilities is reached" );
																							}
																						}
																					else
																						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the assignments of level " + CommonVariables.currentAssignmentLevel );
																					}
																				}
																			else
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute a selection during the solving of word \"" + solveWordItem.anyWordTypeString() + "\" at assignment level " + CommonVariables.currentAssignmentLevel );
																			}
																		else
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign a selection specifcation at assignment level: " + CommonVariables.currentAssignmentLevel );
																		}
																	else
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a new assignment level: " + CommonVariables.currentAssignmentLevel );
																	}
																while( CommonVariables.result == Constants.RESULT_OK &&
																possibilityItem != null );

																if( CommonVariables.result == Constants.RESULT_OK &&
																admin_.scoreList != null &&

																( nPossibilities > 1 ||
																CommonVariables.currentAssignmentLevel > Constants.NO_ASSIGNMENT_LEVEL ) )	// Higher level has possibilities
																	{
																	if( admin_.scoreList.deleteScores() != Constants.RESULT_OK )
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the scores with assignment level " + CommonVariables.currentAssignmentLevel );
																	}
																}
															else
																return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the first possibility item at assignment level " + CommonVariables.currentAssignmentLevel );
															}
														else
															return myWord_.setErrorInItem( 1, moduleNameString_, "The word list isn't created yet" );
														}
													else
														return myWord_.setErrorInItem( 1, moduleNameString_, "The solve scores list isn't created yet at assignment level " + CommonVariables.currentAssignmentLevel );
													}
												else
													{
													currentSolveProgress_ = endSolveProgress;

													if( solveLevel_ > 1 )
														Presentation.showProgress( currentSolveProgress_ );
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )
													{
													Presentation.clearProgress();

													if( admin_.scoreList != null )
														{
														if( ( specificationResult = predefinedNounSolveStrategyWordItem.getAssignmentWordParameter() ).result == Constants.RESULT_OK )
															{
															if( ( selectionResult = admin_.scoreList.getBestAction( specificationResult.assignmentParameter ) ).result == Constants.RESULT_OK )
																{
																if( ( actionSelectionItem = selectionResult.bestActionItem ) != null )
																	{
																	if( admin_.assignSelectionSpecification( actionSelectionItem ) == Constants.RESULT_OK )
																		{
																		if( admin_.assignSpecification( predefinedNounSolveMethodWordItem, admin_.predefinedAdjectiveDoneWordItem() ) != Constants.RESULT_OK )
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the done flag to the solve method at assignment level " + CommonVariables.currentAssignmentLevel );
																		}
																	else
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign a selection specification at assignment level " + CommonVariables.currentAssignmentLevel );
																	}
																else
																	return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't get the best action selection item" );
																}
															else
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the best action of solve word \"" + solveWordItem.anyWordTypeString() + "\" at assignment level " + CommonVariables.currentAssignmentLevel );
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the solve strategy at assignment level " + CommonVariables.currentAssignmentLevel );
														}
													else
														return myWord_.setErrorInItem( 1, moduleNameString_, "The solve scores list isn't created yet" );
													}
												}
											else
												{
												if( CommonVariables.currentAssignmentLevel == Constants.NO_ASSIGNMENT_LEVEL )
													{
													if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_IMPERATIVE_WARNING_I_COULD_FIND_SOLVE_INFO_START, solveWordItem.anyWordTypeString(), Constants.INTERFACE_IMPERATIVE_WARNING_I_COULD_FIND_SOLVE_INFO_END ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
													}
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a possibility to solve word \"" + solveWordItem.anyWordTypeString() + "\" at assignment level " + CommonVariables.currentAssignmentLevel );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the solve strategy at assignment level " + CommonVariables.currentAssignmentLevel );
									}
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the busy flag to the solve method at assignment level " + CommonVariables.currentAssignmentLevel );
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The given assignment level of " + CommonVariables.currentAssignmentLevel + " is higher than the given solve level " + solveLevel_ );
						}
					else
						{
						if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_IMPERATIVE_WARNING_WORD_ALREADY_SOLVED_START, solveWordItem.anyWordTypeString(), Constants.INTERFACE_IMPERATIVE_WARNING_WORD_ALREADY_SOLVED_END ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined solve strategy noun word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined solve-method noun word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given solve word is undefined" );

		return CommonVariables.result;
		}

	protected byte findPossibilityToSolveWord( boolean addScores, boolean isInverted, boolean duplicatesAllowed, boolean prepareSort, short solveStrategyParameter, WordItem solveWordItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		SelectionItem originalExecutionSelectionItem;

		foundPossibility_ = false;

		if( solveWordItem != null )
			{
			if( !solveWordItem.isWordCheckedForSolving )
				{
				if( canWordBeSolved( true, solveWordItem ) == Constants.RESULT_OK )
					{
					solveWordItem.isWordCheckedForSolving = true;

					if( canWordBeSolved_ &&
					admin_.actionList != null )
						{
						originalExecutionSelectionItem = currentExecutionSelectionItem_;

						if( ( selectionResult = admin_.actionList.findFirstExecutionItem( solveWordItem ) ).result == Constants.RESULT_OK )
							{
							if( ( currentExecutionSelectionItem_ = selectionResult.firstExecutionItem ) != null )
								{
								do	{
									if( backTrackConditionScorePaths( addScores, isInverted, duplicatesAllowed, prepareSort, currentExecutionSelectionItem_.selectionLevel(), solveStrategyParameter, currentExecutionSelectionItem_.activeSentenceNr() ) == Constants.RESULT_OK )
										{
										if( currentExecutionSelectionItem_.findNextExecutionSelectionItem( solveWordItem ) == Constants.RESULT_OK )
											currentExecutionSelectionItem_ = currentExecutionSelectionItem_.nextExecutionItem();
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the next action selection item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to back-track the condition score paths for the action with sentence number " + currentExecutionSelectionItem_.activeSentenceNr() );
									}
								while( CommonVariables.result == Constants.RESULT_OK &&
								currentExecutionSelectionItem_ != null );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								currentExecutionSelectionItem_ = originalExecutionSelectionItem;
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the first action selection item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					!CommonVariables.hasShownWarning )
						{
						if( canWordBeSolved( false, solveWordItem ) == Constants.RESULT_OK )
							{
							if( canWordBeSolved_ &&
							admin_.alternativeList != null )
								{
								originalExecutionSelectionItem = currentExecutionSelectionItem_;

								if( ( selectionResult = admin_.alternativeList.findFirstExecutionItem( solveWordItem ) ).result == Constants.RESULT_OK )
									{
									if( ( currentExecutionSelectionItem_ = selectionResult.firstExecutionItem ) != null )
										{
										do	{
											if( backTrackConditionScorePaths( addScores, isInverted, duplicatesAllowed, prepareSort, currentExecutionSelectionItem_.selectionLevel(), solveStrategyParameter, currentExecutionSelectionItem_.activeSentenceNr() ) == Constants.RESULT_OK )
												{
												if( currentExecutionSelectionItem_.findNextExecutionSelectionItem( solveWordItem ) == Constants.RESULT_OK )
													currentExecutionSelectionItem_ = currentExecutionSelectionItem_.nextExecutionItem();
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the next alternative item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to back-track the condition score paths for the alternative with sentence number " + currentExecutionSelectionItem_.activeSentenceNr() );
											}
										while( CommonVariables.result == Constants.RESULT_OK &&
										currentExecutionSelectionItem_ != null );
										}

									if( CommonVariables.result == Constants.RESULT_OK )
										currentExecutionSelectionItem_ = originalExecutionSelectionItem;
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the first alternative item with solve word \"" + solveWordItem.anyWordTypeString() + "\"" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								solveWordItem.isWordCheckedForSolving = false;
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if the given word \"" + solveWordItem.anyWordTypeString() + "\" can be solved by alternative" );
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if the given word \"" + solveWordItem.anyWordTypeString() + "\" can be solved by action" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given solve word \"" + solveWordItem.anyWordTypeString() + "\" is already checked" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given solve word is undefined" );

		return CommonVariables.result;
		}

	protected SelectionResultType checkCondition( SelectionItem conditionSelectionItem )
		{
		SelectionResultType selectionResult = new SelectionResultType();
		boolean isPossessive;
		boolean isNegative;
		WordItem generalizationWordItem;
		WordItem specificationWordItem;

		isConditionSatisfied_ = false;

		if( conditionSelectionItem != null )
			{
			if( ( generalizationWordItem = conditionSelectionItem.generalizationWordItem() ) != null )
				{
				if( ( specificationWordItem = conditionSelectionItem.specificationWordItem() ) != null )
					{
					isPossessive = conditionSelectionItem.isPossessive();
					isNegative = conditionSelectionItem.isNegative();

					if( conditionSelectionItem.isAssignedOrClear() )
						{
						if( specificationWordItem.isAdjectiveClear() )
							{
							if( findScoringAssignment( !isNegative, generalizationWordItem ) == Constants.RESULT_OK )
								isConditionSatisfied_ = ( foundScoringAssignment_ ? isNegative : !isNegative );
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a scoring assignment" );
							}
						else	// Is Assigned
							{
							if( findScoringAssignment( isNegative, generalizationWordItem ) == Constants.RESULT_OK )
								isConditionSatisfied_ = ( foundScoringAssignment_ ? !isNegative : isNegative );
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a scoring assignment" );
							}
						}
					else
						{
						if( generalizationWordItem.isAdjectiveComparison() )
							{
							if( checkComparison( conditionSelectionItem ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check a comparison" );
							}
						else
							{
							if( generalizationWordItem.isAdjectiveOddOrEven() )
								{
								if( checkForOddOrEven( conditionSelectionItem ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for odd or even" );
								}
							else
								{
								if( conditionSelectionItem.isValueSpecification() )
									{
									if( checkConditionByValue( isNegative, isPossessive, conditionSelectionItem.generalizationContextNr(), conditionSelectionItem.specificationContextNr(), generalizationWordItem, specificationWordItem ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the condition of a specification by value" );
									}
								else
									{
									if( findScoringAssignment( isPossessive, !isNegative, conditionSelectionItem.relationContextNr(), generalizationWordItem, specificationWordItem ) == Constants.RESULT_OK )
										{
										if( foundScoringAssignment_ != isNegative )
											isConditionSatisfied_ = true;
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a scoring assignment" );
									}
								}
							}
						}
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The specification word of the given condition selection item is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The generalization word of the given condition selection item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given condition selection item is undefined" );

		selectionResult.isConditionSatisfied = isConditionSatisfied_;
		selectionResult.result = CommonVariables.result;
		return selectionResult;
		}
	};

/*************************************************************************
 *
 *	"Give thanks to the God of gods.
 *	His faithful love endures forever." (Psalm 136:2)
 *
 *************************************************************************/
