/*
 *	Class:			AdminWrite
 *	Supports class:	AdminItem
 *	Purpose:		To write selected specifications as sentences
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

class AdminWrite
	{
	// Private constructible variables

	private boolean foundAnyWordPassingGrammarIntegrityCheck_;

	private boolean isFirstSelfGeneratedAssumption_;
	private boolean isFirstSelfGeneratedConclusion_;
	private boolean isFirstSelfGeneratedQuestion_;
	private boolean isFirstUserQuestion_;
	private boolean isFirstUserSpecifications_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean hasAllWordsPassed()
		{
		ReadItem currentReadItem;

		if( ( currentReadItem = admin_.firstCurrentLanguageActiveReadItem() ) == null )
			return false;
		else
			{
			do	{
				if( currentReadItem.readWordTypeString() != null &&	// Skip hidden word types
				!currentReadItem.isWordPassingGrammarIntegrityCheck )
					return false;
				}
			while( ( currentReadItem = currentReadItem.nextCurrentLanguageReadItem() ) != null );
			}

		return true;
		}

	private byte writeSpecification( boolean isAssignment, boolean isDeactive, boolean isArchive, boolean writeCurrentSentenceOnly, boolean writeJustification, boolean writeUserSpecifications, boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeUserQuestions, boolean writeSelfGeneratedQuestions, String integrityCheckSentenceString, WordItem writeWordItem )
		{
		SpecificationItem currentSpecificationItem;

		if( writeWordItem != null )
			{
			if( ( currentSpecificationItem = writeWordItem.firstSelectedSpecification( isAssignment, isDeactive, isArchive, ( writeUserQuestions || writeSelfGeneratedQuestions ) ) ) != null )
				{
				do	{
					// Filter self-generated specifications
					if( ( ( writeSelfGeneratedConclusions &&
					currentSpecificationItem.isSelfGeneratedConclusion() ) ||

					( writeSelfGeneratedAssumptions &&
					currentSpecificationItem.isSelfGeneratedAssumption() ) ||

					( writeSelfGeneratedQuestions &&
					currentSpecificationItem.isSelfGeneratedQuestion() ) ||

					// Filter user-entered specifications
					( writeUserSpecifications &&
					currentSpecificationItem.isUserSpecification() ) ||

					( writeUserQuestions &&
					currentSpecificationItem.isUserQuestion() ) ) &&

					// Filter on current or updated sentences
					( !admin_.foundChange() ||		// Nothing have changed. Will result in notification: "I know".
					!writeCurrentSentenceOnly ||
					currentSpecificationItem.hasNewInformation() ) )
						{
						if( writeWordItem.writeSelectedSpecification( false, false, writeCurrentSentenceOnly, false, Constants.NO_ANSWER_PARAMETER, currentSpecificationItem ) == Constants.RESULT_OK )
							{
							if( CommonVariables.writeSentenceStringBuffer != null &&
							CommonVariables.writeSentenceStringBuffer.length() > 0 )
								{
								if( integrityCheckSentenceString == null )
									{
									if( ( writeSelfGeneratedConclusions &&
									isFirstSelfGeneratedConclusion_ ) ||

									( writeSelfGeneratedAssumptions &&
									isFirstSelfGeneratedAssumption_ ) ||

									( writeSelfGeneratedQuestions &&
									isFirstSelfGeneratedQuestion_ ) ||

									( writeUserSpecifications &&
									isFirstUserSpecifications_ ) ||

									( writeUserQuestions &&
									isFirstUserQuestion_ ) )
										{
										if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( currentSpecificationItem.isSelfGenerated() ? ( writeSelfGeneratedConclusions ? Constants.INTERFACE_LISTING_MY_CONCLUSIONS : ( writeSelfGeneratedAssumptions ? Constants.INTERFACE_LISTING_MY_ASSUMPTIONS : Constants.INTERFACE_LISTING_MY_QUESTIONS ) ) : ( writeUserSpecifications ? Constants.INTERFACE_LISTING_YOUR_INFORMATION : Constants.INTERFACE_LISTING_YOUR_QUESTIONS ) ) ) == Constants.RESULT_OK )
											{
											if( writeSelfGeneratedConclusions )
												isFirstSelfGeneratedConclusion_ = false;
											else
												{
												if( writeSelfGeneratedAssumptions )
													isFirstSelfGeneratedAssumption_ = false;
												else
													{
													if( writeSelfGeneratedQuestions )
														isFirstSelfGeneratedQuestion_ = false;
													else
														{
														if( writeUserSpecifications )
															isFirstUserSpecifications_ = false;
														else
															{
															if( writeUserQuestions )
																isFirstUserQuestion_ = false;
															}
														}
													}
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write my conclusions header" );
										}

									if( CommonVariables.result == Constants.RESULT_OK )
										{
										if( writeJustification &&
										currentSpecificationItem.isSelfGenerated() )
											{
											if( admin_.writeJustificationSpecification( CommonVariables.writeSentenceStringBuffer.toString(), currentSpecificationItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a justification line" );
											}
										else
											{
											if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a sentence" );
											}
										}
									}
								else
									{
									if( markWordsPassingGrammarIntegrityCheck() != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to mark the words that are passing the integrity check" );
									}
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a selected specification" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&

				( integrityCheckSentenceString == null ||
				!admin_.hasPassedGrammarIntegrityCheck() ) &&

				( currentSpecificationItem = currentSpecificationItem.nextSelectedSpecificationItem( false ) ) != null );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given write word item is undefined" );

		return CommonVariables.result;
		}

	private byte writeSelfGeneratedInfo( boolean writeCurrentSentenceOnly, boolean writeJustification, boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeSelfGeneratedQuestions, WordItem writeWordItem )
		{
		if( writeSpecification( false, false, false, writeCurrentSentenceOnly, writeJustification, false, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, false, writeSelfGeneratedQuestions, null, writeWordItem ) == Constants.RESULT_OK )
			{
			if( writeSpecification( true, false, false, writeCurrentSentenceOnly, writeJustification, false, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, false, writeSelfGeneratedQuestions, null, writeWordItem ) == Constants.RESULT_OK )
				{
				if( writeSpecification( true, true, false, writeCurrentSentenceOnly, writeJustification, false, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, false, writeSelfGeneratedQuestions, null, writeWordItem ) == Constants.RESULT_OK )
					{
					if( writeSpecification( true, false, true, writeCurrentSentenceOnly, writeJustification, false, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, false, writeSelfGeneratedQuestions, null, writeWordItem ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated archive assignments" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated past-tense assignments" );
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated current-tense assignments" );
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated specifications" );

		return CommonVariables.result;
		}

	private byte writeUserInfo( boolean writeCurrentSentenceOnly, boolean writeUserSpecifications, boolean writeUserQuestions, String integrityCheckSentenceString, WordItem writeWordItem )
		{
		// Respond with a user-entered specification or assignment
		if( writeUserSpecifications )
			{
			if( writeSpecification( false, false, false, writeCurrentSentenceOnly, false, writeUserSpecifications, false, false, false, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
				{
				if( writeSpecification( true, false, false, writeCurrentSentenceOnly, false, writeUserSpecifications, false, false, false, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
					{
					if( writeSpecification( true, true, false, writeCurrentSentenceOnly, false, writeUserSpecifications, false, false, false, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
						{
						if( writeSpecification( true, false, true, writeCurrentSentenceOnly, false, writeUserSpecifications, false, false, false, false, integrityCheckSentenceString, writeWordItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user-entered archive assignments" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user-entered past-tense assignments" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user-entered current-tense assignments" );
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user-entered specifications" );
			}

		// Respond with a user question
		if( CommonVariables.result == Constants.RESULT_OK &&
		writeUserQuestions )
			{
			if( writeSpecification( false, false, false, writeCurrentSentenceOnly, false, false, false, false, writeUserQuestions, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
				{
				if( writeSpecification( true, false, false, writeCurrentSentenceOnly, false, false, false, false, writeUserQuestions, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
					{
					if( writeSpecification( true, true, false, writeCurrentSentenceOnly, false, false, false, false, writeUserQuestions, false, integrityCheckSentenceString, writeWordItem ) == Constants.RESULT_OK )
						{
						if( writeSpecification( true, false, true, writeCurrentSentenceOnly, false, false, false, false, writeUserQuestions, false, integrityCheckSentenceString, writeWordItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user questions with archive assignments" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user questions with past-tense assignments" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user questions with current-tense assignments" );
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write user questions without assignments" );
			}

		return CommonVariables.result;
		}

	private byte writeSelectedSpecificationInfo( WordItem writeWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( ( currentGeneralizationItem = writeWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
			{
			do	{
				if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
					{
					// Respond with active specifications
					if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( false, false, false, false, writeWordItem ) == Constants.RESULT_OK )
						{
						// Respond with active specification questions
						if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( false, false, false, true, writeWordItem ) == Constants.RESULT_OK )
							{
							// Respond with active assignments
							if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, false, false, false, writeWordItem ) == Constants.RESULT_OK )
								{
								// Respond with active assignment questions
								if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, false, false, true, writeWordItem ) == Constants.RESULT_OK )
									{
									// Respond with deactive assignments
									if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, true, false, false, writeWordItem ) == Constants.RESULT_OK )
										{
										// Respond with deactive assignment questions
										if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, true, false, true, writeWordItem ) == Constants.RESULT_OK )
											{
											// Respond with archive assignments
											if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, false, true, false, writeWordItem ) == Constants.RESULT_OK )
												{
												// Respond with archive assignment questions
												if( currentGeneralizationWordItem.writeSelectedSpecificationInfo( true, false, true, true, writeWordItem ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write archive assignment questions" );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write archive assignments" );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write deactive assignment questions" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write deactive assignments" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active assignment questions" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active assignments" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active specification questions" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active specifications" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
			}

		return CommonVariables.result;
		}

	private byte writeSelectedRelationInfo( WordItem writeWordItem )
		{
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		if( ( currentGeneralizationItem = writeWordItem.firstActiveGeneralizationItemOfRelation() ) != null )
			{
			do	{
				if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
					{
					if( currentGeneralizationItem.isRelation() )
						{
						// Respond with active related specifications
						if( currentGeneralizationWordItem.writeSelectedRelationInfo( false, false, false, false, writeWordItem ) == Constants.RESULT_OK )
							{
							// Respond with active related specification questions
							if( currentGeneralizationWordItem.writeSelectedRelationInfo( false, false, false, true, writeWordItem ) == Constants.RESULT_OK )
								{
								// Respond with active related assignments
								if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, false, false, false, writeWordItem ) == Constants.RESULT_OK )
									{
									// Respond with active related assignment questions
									if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, false, false, true, writeWordItem ) == Constants.RESULT_OK )
										{
										// Respond with deactive related assignments
										if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, true, false, false, writeWordItem ) == Constants.RESULT_OK )
											{
											// Respond with deactive related assignment questions
											if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, true, false, true, writeWordItem ) == Constants.RESULT_OK )
												{
												// Respond with archive related assignments
												if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, false, true, false, writeWordItem ) == Constants.RESULT_OK )
													{
													// Respond with archive related assignment questions
													if( currentGeneralizationWordItem.writeSelectedRelationInfo( true, false, true, true, writeWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write archive related assignment questions" );
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write archive related assignment" );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write deactive related assignment questions" );
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active related assignments" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active related assignment assignments" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active related assignments" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active related specification questions" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write active related specifications" );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfRelation() ) != null );
			}

		return CommonVariables.result;
		}

	private byte markWordsPassingGrammarIntegrityCheck()
		{
		ReadResultType readResult = new ReadResultType();
		int writeSentenceStringBufferLength;
		int wordPosition = 0;
		String readWordTypeString;
		ReadItem currentReadItem;
		ReadItem skipReadItem = null;

		if( ( currentReadItem = admin_.firstCurrentLanguageActiveReadItem() ) != null )
			{
			if( CommonVariables.writeSentenceStringBuffer != null &&
			( writeSentenceStringBufferLength = CommonVariables.writeSentenceStringBuffer.length() ) > 0 )
				{
				do	{
					do	{
						if( ( readResult = admin_.getWordInfo( false, wordPosition, CommonVariables.writeSentenceStringBuffer.toString() ) ).result == Constants.RESULT_OK )
							{
							if( readResult.wordLength > 0 &&
							( readWordTypeString = currentReadItem.readWordTypeString() ) != null )	// Skip hidden word types
								{
								if( readWordTypeString.length() == readResult.wordLength &&
								CommonVariables.writeSentenceStringBuffer.substring( wordPosition ).startsWith( readWordTypeString ) )
									{
									foundAnyWordPassingGrammarIntegrityCheck_ = true;
									currentReadItem.isWordPassingGrammarIntegrityCheck = true;
									}
								else
									{
									// Skip generalization conjunction
									if( currentReadItem.isGeneralizationSpecification() ||

									// Skip linked conjunction
									currentReadItem.isLinkedGeneralizationConjunction() ||

									// Skip grammar conjunction
									currentReadItem.isSentenceConjunction() ||

									// Skip text until it is implemented
									currentReadItem.isReadWordText() )
										currentReadItem.isWordPassingGrammarIntegrityCheck = true;	// Skip until implemented
									else
										{
										if( skipReadItem == null )
											skipReadItem = currentReadItem;
										}
									}
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the current grammar word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					readResult.wordLength > 0 &&
					( currentReadItem = currentReadItem.nextCurrentLanguageReadItem() ) != null );

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( skipReadItem != null &&
						currentReadItem == null )
							currentReadItem = skipReadItem;

						wordPosition = readResult.nextWordPosition;
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				currentReadItem != null &&
				readResult.nextWordPosition < writeSentenceStringBufferLength );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The write sentence string buffer is empty" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find any read words" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminWrite( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		foundAnyWordPassingGrammarIntegrityCheck_ = false;

		isFirstSelfGeneratedAssumption_ = true;
		isFirstSelfGeneratedConclusion_ = true;
		isFirstSelfGeneratedQuestion_ = true;
		isFirstUserQuestion_ = true;
		isFirstUserSpecifications_ = true;

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

	protected void initializeAdminWriteVariables()
		{
		isFirstSelfGeneratedAssumption_ = true;
		isFirstSelfGeneratedConclusion_ = true;
		isFirstSelfGeneratedQuestion_ = true;
		isFirstUserQuestion_ = true;
		isFirstUserSpecifications_ = true;
		}

	protected byte answerQuestions()
		{
		WordItem currentWordItem;

		if( CommonVariables.isQuestionAlreadyAnswered )
			{
			if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_QUESTION_IS_ALREADY_ANSWERED ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
			}

		// Do in all words for an answer
		if( CommonVariables.result == Constants.RESULT_OK )
			{
			if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
				{
				do	{
					if( currentWordItem.findAnswerToNewUserQuestion() != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find an answer to new questions of the user about word \"" + currentWordItem.anyWordTypeString() + "\"" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentWordItem = currentWordItem.nextWordItem() ) != null );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.isUserQuestion &&
		!CommonVariables.foundAnswerToQuestion &&
		!CommonVariables.isQuestionAlreadyAnswered )
			{
			if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_QUESTION_I_LL_ANSWER_AS_SOON_AS_I_HAVE_ENOUGH_INFORMATION ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
			}

		return CommonVariables.result;
		}

	protected byte integrityCheck( boolean isQuestion, String integrityCheckSentenceString )
		{
		ReadItem currentReadItem;

		foundAnyWordPassingGrammarIntegrityCheck_ = false;

		if( admin_.checkForChanges() == Constants.RESULT_OK )
			{
			if( admin_.foundChange() &&		// Skip direct text and known knowledge
			( currentReadItem = admin_.firstCurrentLanguageActiveReadItem() ) != null )
				{
				do	{	// Select all generalizations in a sentence
					if( currentReadItem.isGeneralizationWord() )
						{
						if( writeInfoAboutWord( true, false, !isQuestion, false, false, isQuestion, false, false, false, integrityCheckSentenceString, currentReadItem.readWordItem() ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write info about word \"" + currentReadItem.readWordItem().anyWordTypeString() + "\"" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&

				// Skip selections until writing of selection is implemented
				!currentReadItem.isSelection() &&

				// Skip imperatives until writing of imperatives is implemented
				!currentReadItem.isImperative() &&
				( currentReadItem = currentReadItem.nextCurrentLanguageReadItem() ) != null );

				if( CommonVariables.result == Constants.RESULT_OK &&
				currentReadItem == null &&	// Sentence isn't skipped (selections and imperatives)
				!hasAllWordsPassed() )
					{
					if( admin_.isSystemStartingUp() &&
					integrityCheckSentenceString != null )
						{
						if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_STORE_OR_RETRIEVE, Constants.EMPTY_STRING, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_SENTENCE, integrityCheckSentenceString, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_SENTENCE_END ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
						}
					else
						{
						if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_STORE_OR_RETRIEVE, Constants.EMPTY_STRING, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_THIS_SENTENCE ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					foundAnyWordPassingGrammarIntegrityCheck_ &&
					CommonVariables.writeSentenceStringBuffer != null &&
					CommonVariables.writeSentenceStringBuffer.length() > 0 )
						{
						if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_I_RETRIEVED_FROM_MY_SYSTEM, CommonVariables.writeSentenceStringBuffer.toString(), Constants.INTERFACE_SENTENCE_ERROR_GRAMMAR_INTEGRITY_SENTENCE_END ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					admin_.isSystemStartingUp() &&
					CommonVariables.hasShownWarning )
						return myWord_.setErrorInItem( 1, moduleNameString_, "An integrity error occurred during startup" );
					}
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for changes" );

		return CommonVariables.result;
		}

	protected byte writeSelfGeneratedInfo( boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeSelfGeneratedQuestions )
		{
		WordItem currentWordItem;
		isFirstSelfGeneratedAssumption_ = true;
		isFirstSelfGeneratedConclusion_ = true;
		isFirstSelfGeneratedQuestion_ = true;

		// Do in all words for self-generated info
		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
			{
			do	{
				if( writeInfoAboutWord( true, false, false, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, false, writeSelfGeneratedQuestions, false, false, null, currentWordItem ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write info about word \"" + currentWordItem.anyWordTypeString() + "\"" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeSelfGeneratedInfo( boolean writeCurrentSentenceOnly, boolean writeJustification, boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeSelfGeneratedQuestions, String integrityCheckSentenceString, WordItem writeWordItem )
		{
		// Respond with a self-generated conclusions
		if( integrityCheckSentenceString == null )	// Don't perform integrity check for self-generated info
			{
			if( writeSelfGeneratedConclusions )		// Self-generated conclusions
				{
				if( writeSelfGeneratedInfo( writeCurrentSentenceOnly, writeJustification, true, false, false, writeWordItem ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated conclusions" );
				}
			}

		// Respond with a self-generated assumptions
		if( CommonVariables.result == Constants.RESULT_OK &&
		writeSelfGeneratedAssumptions )
			{
			if( writeSelfGeneratedInfo( writeCurrentSentenceOnly, writeJustification, false, true, false, writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated assumptions" );
			}

		// Respond with a self-generated questions
		if( CommonVariables.result == Constants.RESULT_OK &&
		writeSelfGeneratedQuestions )
			{
			if( writeSelfGeneratedInfo( writeCurrentSentenceOnly, writeJustification, false, false, true, writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write self-generated questions" );
			}

		return CommonVariables.result;
		}

	protected byte writeInfoAboutWord( boolean writeCurrentSentenceOnly, boolean writeJustification, boolean writeUserSpecifications, boolean writeSelfGeneratedConclusions, boolean writeSelfGeneratedAssumptions, boolean writeUserQuestions, boolean writeSelfGeneratedQuestions, boolean writeSpecificationInfo, boolean writeRelatedInfo, String integrityCheckSentenceString, WordItem writeWordItem )
		{
		if( writeUserQuestions ||
		writeUserSpecifications )
			{
			if( writeUserInfo( writeCurrentSentenceOnly, writeUserSpecifications, writeUserQuestions, integrityCheckSentenceString, writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the user generated info" );
			}

		if( CommonVariables.result == Constants.RESULT_OK &&
		integrityCheckSentenceString == null &&		// Don't perform integrity check for self-generated conclusions

		( writeSelfGeneratedConclusions ||
		writeSelfGeneratedAssumptions ||
		writeSelfGeneratedQuestions ) )
			{
			if( writeSelfGeneratedInfo( writeCurrentSentenceOnly, writeJustification, writeSelfGeneratedConclusions, writeSelfGeneratedAssumptions, writeSelfGeneratedQuestions, null, writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the self-generated info" );
			}

		// Respond with other specification info
		if( CommonVariables.result == Constants.RESULT_OK &&
		integrityCheckSentenceString == null &&		// Don't perform integrity check for other specification info
		writeSpecificationInfo )
			{
			if( writeSelectedSpecificationInfo( writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write selected specification info" );
			}

		// Respond with related info
		if( CommonVariables.result == Constants.RESULT_OK &&
		integrityCheckSentenceString == null &&		// Don't perform integrity check for related info
		writeRelatedInfo )
			{
			if( writeSelectedRelationInfo( writeWordItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write selected related info" );
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Give thanks to the Lord, for he is good!
 *	His faithful love endures forever." (Psalm 107:1)
 *
 *************************************************************************/
