/*
 *	Class:			AdminReadSentence
 *	Supports class:	AdminItem
 *	Purpose:		To read and analyze sentences
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

class AdminReadSentence
	{
	// Private constructible variables

	private boolean areReadItemsStillValid_;
	private boolean deleteRollbackInfo_;
	private boolean isAssignment_;
	private boolean isConditional_;
	private boolean isExclusive_;
	private boolean isDeactiveAssignment_;
	private boolean isArchiveAssignment_;
	private boolean isPossessive_;
	private boolean isSpecificationGeneralization_;
	private boolean isLinkedGeneralizationConjunction_;
	private boolean showConclusions_;
	private boolean isGuidedByGrammarCanceled_;

	private short prepositionParameter_;
	private short questionParameter_;

	private int specificationCollectionNr_;
	private int generalizationContextNr_;
	private int specificationContextNr_;
	private int relationContextNr_;

	private ReadItem currentReadItem_;
	private ReadItem startGeneralizationWordReadItem_;
	private ReadItem endGeneralizationWordReadItem_;
	private ReadItem startSpecificationWordReadItem_;
	private ReadItem endSpecificationWordReadItem_;
	private ReadItem startRelationWordReadItem_;
	private ReadItem endRelationWordReadItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private void setVerbVariables( short wordParameter )
		{
		switch( wordParameter )
			{
			case Constants.WORD_PARAMETER_SINGULAR_VERB_CAN_BE:
			case Constants.WORD_PARAMETER_PLURAL_VERB_CAN_BE:
				isConditional_ = true;
				break;

			case Constants.WORD_PARAMETER_SINGULAR_VERB_WAS:
			case Constants.WORD_PARAMETER_PLURAL_VERB_WERE:
				isAssignment_ = true;
				isArchiveAssignment_ = true;
				break;

			case Constants.WORD_PARAMETER_SINGULAR_VERB_HAS:
			case Constants.WORD_PARAMETER_PLURAL_VERB_HAVE:
				isPossessive_ = true;
				break;

			case Constants.WORD_PARAMETER_SINGULAR_VERB_HAD:
			case Constants.WORD_PARAMETER_PLURAL_VERB_HAD:
				isAssignment_ = true;
				isArchiveAssignment_ = true;
				isPossessive_ = true;
				break;
			}
		}

	private void clearLastCheckedAssumptionLevelItemNrInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.clearLastCheckedAssumptionLevelItemNrInWord();
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		}

	private byte scanSpecification( String readSentenceString )
		{
		ContextResultType contextResult = new ContextResultType();
		boolean stop = false;
		short wordTypeNr;
		ReadItem previousReadItem = null;
		WordItem readWordItem;

		isAssignment_ = false;
		isConditional_ = false;
		isExclusive_ = false;
		isDeactiveAssignment_ = false;
		isArchiveAssignment_ = false;
		isPossessive_ = false;
		isSpecificationGeneralization_ = false;

		prepositionParameter_ = Constants.NO_PREPOSITION_PARAMETER;
		questionParameter_ = Constants.NO_QUESTION_PARAMETER;

		generalizationContextNr_ = Constants.NO_CONTEXT_NR;
		specificationContextNr_ = Constants.NO_CONTEXT_NR;
		relationContextNr_ = Constants.NO_CONTEXT_NR;

		startSpecificationWordReadItem_ = null;
		endSpecificationWordReadItem_ = null;
		startRelationWordReadItem_ = null;
		endRelationWordReadItem_ = null;

		if( currentReadItem_ != null )
			{
			if( isLinkedGeneralizationConjunction_ )
				isLinkedGeneralizationConjunction_ = false;
			else
				{
				startGeneralizationWordReadItem_ = null;
				endGeneralizationWordReadItem_ = null;
				}

			do	{
				wordTypeNr = currentReadItem_.wordTypeNr();
				readWordItem = currentReadItem_.readWordItem();

				switch( currentReadItem_.grammarParameter )
					{
					case Constants.GRAMMAR_SENTENCE:
						if( !currentReadItem_.isSeparator() )
							{
							if( readSentenceString != null &&
							admin_.isSystemStartingUp() )
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an unknown word in sentence \"" + readSentenceString + "\" at position " + currentReadItem_.wordPosition() + " with grammar parameter " + currentReadItem_.grammarParameter + " and word parameter " + currentReadItem_.wordParameter() );
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an unknown word at position " + currentReadItem_.wordPosition() + " with grammar parameter " + currentReadItem_.grammarParameter + " and word parameter " + currentReadItem_.wordParameter() );
							}

						break;

					case Constants.GRAMMAR_ANSWER:
						break;	// Needs to be implemented

					case Constants.GRAMMAR_GENERALIZATION_ASSIGNMENT:		// Assignment generalization-specification
						isAssignment_ = true;

						// Don't insert a break statement here

					case Constants.GRAMMAR_IMPERATIVE:
					case Constants.GRAMMAR_GENERALIZATION_SPECIFICATION:
					case Constants.GRAMMAR_GENERALIZATION_PART:
					case Constants.GRAMMAR_GENERALIZATION_WORD:
						if( currentReadItem_.isAdjectivePrevious() )
							{
							isAssignment_ = true;
							isDeactiveAssignment_ = true;
							}

						if( currentReadItem_.isReadWordPronoun() )
							{
							if( generalizationContextNr_ == Constants.NO_CONTEXT_NR )
								{
								if( ( contextResult = admin_.addPronounContext( wordTypeNr, readWordItem ) ).result == Constants.RESULT_OK )
									generalizationContextNr_ = contextResult.contextNr;
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a generalization pronoun context" );
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The generalization context number is already assigned" );
							}

						if( startGeneralizationWordReadItem_ == null )
							startGeneralizationWordReadItem_ = currentReadItem_;

						endGeneralizationWordReadItem_ = currentReadItem_;

						break;

					case Constants.GRAMMAR_LINKED_GENERALIZATION_CONJUNCTION:
						stop = true;
						isLinkedGeneralizationConjunction_ = true;

						break;

					case Constants.GRAMMAR_EXCLUSIVE_SPECIFICATION_CONJUNCTION:
						isExclusive_ = true;

						break;

					case Constants.GRAMMAR_RELATION_PART:
					case Constants.GRAMMAR_RELATION_WORD:
						if( currentReadItem_.isPreposition() )
							prepositionParameter_ = currentReadItem_.wordParameter();

						if( startSpecificationWordReadItem_ == null )
							startSpecificationWordReadItem_ = currentReadItem_;

						endSpecificationWordReadItem_ = currentReadItem_;

						if( startRelationWordReadItem_ == null )
							startRelationWordReadItem_ = currentReadItem_;

						endRelationWordReadItem_ = currentReadItem_;

						break;

					case Constants.GRAMMAR_ASSIGNMENT_PART:
					case Constants.GRAMMAR_SPECIFICATION_ASSIGNMENT:
						isAssignment_ = true;

						if( currentReadItem_.isNegative() ||
						currentReadItem_.isReadWordPronoun() )
							{
							if( specificationContextNr_ == Constants.NO_CONTEXT_NR )
								{
								if( ( contextResult = admin_.addPronounContext( wordTypeNr, readWordItem ) ).result == Constants.RESULT_OK )
									specificationContextNr_ = contextResult.contextNr;
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification pronoun context" );
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The specification context number is already assigned" );
							}

						// Don't insert a break statement here

					case Constants.GRAMMAR_SPECIFICATION_PART:
					case Constants.GRAMMAR_SPECIFICATION_WORD:
					case Constants.GRAMMAR_TEXT:
						if( startSpecificationWordReadItem_ == null )
							startSpecificationWordReadItem_ = currentReadItem_;

						endSpecificationWordReadItem_ = currentReadItem_;

						break;

					case Constants.GRAMMAR_RELATION_ASSIGNMENT:
						isAssignment_ = true;

						if( currentReadItem_.isNegative() ||
						currentReadItem_.isReadWordPronoun() )
							{
							if( relationContextNr_ == Constants.NO_CONTEXT_NR )
								{
								if( ( contextResult = admin_.addPronounContext( wordTypeNr, readWordItem ) ).result == Constants.RESULT_OK )
									relationContextNr_ = contextResult.contextNr;
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a relation pronoun context" );
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The relation context number is already assigned" );
							}

						break;

					case Constants.GRAMMAR_QUESTION_VERB:
						questionParameter_ = Constants.WORD_PARAMETER_SINGULAR_VERB_IS;

						// Don't insert a break statement here

					case Constants.GRAMMAR_VERB:
						setVerbVariables( currentReadItem_.wordParameter() );

						if( startSpecificationWordReadItem_ == null )
							startSpecificationWordReadItem_ = currentReadItem_;

						endSpecificationWordReadItem_ = currentReadItem_;

						break;

					case Constants.GRAMMAR_GENERALIZATION_QUESTION_VERB:
						questionParameter_ = Constants.WORD_PARAMETER_SINGULAR_VERB_IS;

						// Don't insert a break statement here

					case Constants.GRAMMAR_GENERALIZATION_VERB:
						isSpecificationGeneralization_ = true;
						setVerbVariables( currentReadItem_.wordParameter() );

						if( startSpecificationWordReadItem_ == null )
							startSpecificationWordReadItem_ = currentReadItem_;

						endSpecificationWordReadItem_ = currentReadItem_;

						break;

					default:
						if( previousReadItem != null )
							{
							stop = true;
							currentReadItem_ = previousReadItem;
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I found a word that does not belong to an assignment or a specification" );
					}

				previousReadItem = currentReadItem_;
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			!stop &&
			( currentReadItem_ = currentReadItem_.nextCurrentLanguageReadItem() ) != null );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The read item is undefined" );

		return CommonVariables.result;
		}

	private byte interpretSentence( String readSentenceString )
		{
		ReadResultType readResult = new ReadResultType();
		boolean hasCreatedAllReadWords = false;
		boolean foundMoreInterpretations;
		short currentLanguageNr = CommonVariables.currentGrammarLanguageNr;
		short originalLanguageNr = CommonVariables.currentGrammarLanguageNr;
		int nLanguages = myWord_.numberOfActiveGrammarLanguages();
		int currentWordPosition = 0;
		GrammarItem startOfGrammar;
		ReadItem failedWordReadItem = null;
		String readWordString;

		isGuidedByGrammarCanceled_ = false;

		if( nLanguages > 0 )
			{
			if( nLanguages >= CommonVariables.currentGrammarLanguageNr )
				{
				do	{
					if( admin_.deleteUnusedInterpretations( true ) == Constants.RESULT_OK )
						{
						if( currentLanguageNr != CommonVariables.currentGrammarLanguageNr )	// Need to switch language
							{
							if( admin_.assignGrammarAndInterfaceLanguage( currentLanguageNr ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the grammar and interface language" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( CommonVariables.currentGrammarLanguageWordItem != null )
								{
								if( CommonVariables.currentGrammarLanguageWordItem.needToCheckGrammar() )
									{
									if( CommonVariables.currentGrammarLanguageWordItem.checkGrammar() != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the grammar" );
									}

								if( CommonVariables.result == Constants.RESULT_OK )
									{
									admin_.deleteReadList();

									if( admin_.cleanupDeletedItems() == Constants.RESULT_OK )
										{
										foundMoreInterpretations = false;
										CommonVariables.foundLanguageMixUp = false;
										startOfGrammar = CommonVariables.currentGrammarLanguageWordItem.startOfGrammar();

											if( ( readResult = admin_.createReadWords( readSentenceString ) ).result == Constants.RESULT_OK )
												hasCreatedAllReadWords = readResult.hasCreatedAllReadWords;
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create the read words" );

										if( CommonVariables.result == Constants.RESULT_OK )
											{
											if( admin_.readList != null )
												{
												foundMoreInterpretations = false;
												currentWordPosition = 0;
												admin_.readList.initForParsingReadWords();

												if( hasCreatedAllReadWords )
													{
													do	{
														if( ( readResult = admin_.parseReadWords( startOfGrammar ) ).result == Constants.RESULT_OK )
															{
															currentWordPosition = ( CommonVariables.foundLanguageMixUp ? 0 : readResult.currentParseWordPosition );

															if( currentWordPosition == 0 )
																{
																if( ( readResult = admin_.readList.findMoreInterpretations() ).result == Constants.RESULT_OK )
																	{
																	foundMoreInterpretations = readResult.foundMoreInterpretations;
																	failedWordReadItem = readResult.failedWordReadItem;
																	}
																else
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find more interpretions" );
																}
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to parse of the read words" );
														}
													while( CommonVariables.result == Constants.RESULT_OK &&
													foundMoreInterpretations &&
													currentWordPosition == 0 );
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												!foundMoreInterpretations &&
												currentWordPosition == 0 )
													{
													if( nLanguages <= 1 )
														currentLanguageNr++;							// The only language
													else
														{
														if( currentLanguageNr == originalLanguageNr )	// Failed for current language
															currentLanguageNr = 1;						// Try all languages
														else
															currentLanguageNr++;

														if( currentLanguageNr == originalLanguageNr )	// Skip current language (already tested)
															currentLanguageNr++;
														}
													}
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "The read list isn't created yet" );
											}
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to cleanup the deleted items" );
									}
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete unused interpretions" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				currentWordPosition == 0 &&
				currentLanguageNr <= nLanguages );

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( admin_.deleteUnusedInterpretations( false ) == Constants.RESULT_OK )
						{
						if( !isGuidedByGrammarCanceled_ &&
						currentWordPosition == 0 )		// Failed to interpret sentence
							{
							if( CommonVariables.currentGrammarLanguageNr != originalLanguageNr )
								{
								if( admin_.assignGrammarAndInterfaceLanguage( originalLanguageNr ) != Constants.RESULT_OK )	// Restore the original language
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the grammar and interface language after an interpretion failure" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( failedWordReadItem == null )
									{
									if( admin_.isSystemStartingUp() )
										return myWord_.setSystemErrorInItem( 1, moduleNameString_, "I don't understand this sentence:\n1) Please make sure you enter a sentence within my limited grammar definition\n2) and don't forget to add a colon (or other punctuation mark) at the end of each sentence" );
									else
										{
										if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_WARNING_NOT_CONFORM_GRAMMAR ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
										}
									}
								else
									{
									if( ( readWordString = failedWordReadItem.readWordTypeString() ) == null )
										{
										if( admin_.isSystemStartingUp() )
											return myWord_.setSystemErrorInItem( 1, moduleNameString_, "I don't understand the sentence from the word at position " + failedWordReadItem.wordPosition() );
										else
											{
											if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_WARNING_DONT_UNDERSTAND_FROM_WORD_POSITION_START, failedWordReadItem.wordPosition(), Constants.INTERFACE_SENTENCE_WARNING_AT_POSITION_END ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
											}
										}
									else
										{
										if( admin_.isSystemStartingUp() )
											return myWord_.setSystemErrorInItem( 1, moduleNameString_, "I don't understand the sentence from word \"" + readWordString + "\" at position " + failedWordReadItem.wordPosition() );
										else
											{
											if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_WARNING_DONT_UNDERSTAND_FROM_WORD_START, readWordString, Constants.INTERFACE_SENTENCE_WARNING_AT_POSITION_START, failedWordReadItem.wordPosition(), Constants.INTERFACE_SENTENCE_WARNING_AT_POSITION_END ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
											}
										}
									}
								}
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the unused interpretions of the read words" );
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language number exceeds the number of languages" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find any grammar language" );

		return CommonVariables.result;
		}

	private byte executeSentence( String readSentenceString )
		{
		boolean isAction = false;
		boolean isNewStart = true;
		short selectionLevel = Constants.NO_SELECTION_LEVEL;
		short selectionListNr = Constants.NO_LIST_NR;

		if( ( currentReadItem_ = admin_.firstCurrentLanguageActiveReadItem() ) != null )
			{
			isLinkedGeneralizationConjunction_ = false;

			admin_.initializeLinkedWord();
			endGeneralizationWordReadItem_ = null;
			startGeneralizationWordReadItem_ = null;

			do	{
				if( !currentReadItem_.isSeparator() )
					{
					switch( currentReadItem_.grammarParameter )
						{
						case Constants.GRAMMAR_GENERALIZATION_SPECIFICATION:
						case Constants.GRAMMAR_GENERALIZATION_PART:
						case Constants.GRAMMAR_GENERALIZATION_WORD:
						case Constants.GRAMMAR_SPECIFICATION_PART:
						case Constants.GRAMMAR_ASSIGNMENT_PART:
						case Constants.GRAMMAR_SPECIFICATION_WORD:
						case Constants.GRAMMAR_RELATION_PART:
						case Constants.GRAMMAR_RELATION_WORD:
						case Constants.GRAMMAR_GENERALIZATION_ASSIGNMENT:
						case Constants.GRAMMAR_SPECIFICATION_ASSIGNMENT:
						case Constants.GRAMMAR_RELATION_ASSIGNMENT:
						case Constants.GRAMMAR_VERB:
						case Constants.GRAMMAR_QUESTION_VERB:
						case Constants.GRAMMAR_GENERALIZATION_VERB:
						case Constants.GRAMMAR_GENERALIZATION_QUESTION_VERB:
							if( readSpecification( isAction, isNewStart, selectionLevel, selectionListNr, readSentenceString ) == Constants.RESULT_OK )
								isNewStart = false;
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a generalization-specification" );

							break;

						case Constants.GRAMMAR_IMPERATIVE:
							if( readImperative( isAction, isNewStart, selectionLevel, selectionListNr, readSentenceString ) == Constants.RESULT_OK )
								isNewStart = false;
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read an imperative" );

							break;

						case Constants.GRAMMAR_ANSWER:
							if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_SENTENCE_WARNING_NOT_ABLE_TO_LINK_YES_NO_TO_QUESTION ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to write an interface warning" );

							break;

						case Constants.GRAMMAR_TEXT:
							if( readText( isAction, isNewStart, selectionLevel, selectionListNr, currentReadItem_.readString ) == Constants.RESULT_OK )
								isNewStart = false;
							else
								myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to read the text at position " + currentReadItem_.wordPosition() );

							break;

						case Constants.GRAMMAR_SENTENCE_CONJUNCTION:
							switch( currentReadItem_.wordParameter() )
								{
								case Constants.WORD_PARAMETER_SYMBOL_COMMA:
								case Constants.WORD_PARAMETER_CONJUNCTION_AND:
									break;

								case Constants.WORD_PARAMETER_CONJUNCTION_OR:
									isNewStart = true;

									break;

								default:
									return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I found an illegal conjunction word parameter: " + currentReadItem_.wordParameter() );
								}

							break;

						case Constants.GRAMMAR_SELECTION:
						case Constants.WORD_PARAMETER_SELECTION_IF:	// In case "then" is missing
						case Constants.WORD_PARAMETER_SELECTION_THEN:
						case Constants.WORD_PARAMETER_SELECTION_ELSE:
							switch( currentReadItem_.wordParameter() )
								{
								case Constants.WORD_PARAMETER_SELECTION_IF:
									if( selectionListNr != Constants.NO_LIST_NR )
										{
										if( ++selectionLevel == Constants.MAX_LEVEL )
											return myWord_.setSystemErrorInItem( 1, moduleNameString_, "Selection overflow in list <" + admin_.adminListChar( selectionListNr ) + "> at word position " + currentReadItem_.wordPosition() );
										}

									isNewStart = true;
									selectionListNr = Constants.ADMIN_CONDITION_LIST;

									break;

								case Constants.WORD_PARAMETER_SELECTION_THEN:
									isNewStart = true;
									isAction = true;
									selectionListNr = Constants.ADMIN_ACTION_LIST;

									break;

								case Constants.WORD_PARAMETER_SELECTION_ELSE:
									isNewStart = true;
									isAction = false;
									selectionListNr = Constants.ADMIN_ALTERNATIVE_LIST;

									break;

								default:
									return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I found an illegal selection word" );
								}

							break;

						default:
							if( readSentenceString != null &&
							admin_.isSystemStartingUp() )
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an unknown word in sentence \"" + readSentenceString + "\" at position " + currentReadItem_.wordPosition() + " with grammar parameter " + currentReadItem_.grammarParameter + " and word parameter " + currentReadItem_.wordParameter() );
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "I found an unknown word at position " + currentReadItem_.wordPosition() + " with grammar parameter " + currentReadItem_.grammarParameter + " and word parameter " + currentReadItem_.wordParameter() );
						}
					}
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			!admin_.isRestart() &&
			!CommonVariables.hasShownWarning &&
			currentReadItem_ != null &&
			( currentReadItem_ = currentReadItem_.nextCurrentLanguageReadItem() ) != null );

			if( CommonVariables.result == Constants.RESULT_OK &&
			selectionListNr != Constants.NO_LIST_NR )
				{
				if( admin_.checkForDuplicateSelection() != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check on a duplicate selection" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the first read item" );

		return CommonVariables.result;
		}

	private byte addReadSpecification( boolean isAction, boolean isNewStart, short selectionLevel, short selectionListNr )
		{
		CollectionResultType collectionResult = new CollectionResultType();
		boolean isConditional = ( isConditional_ || selectionListNr != Constants.NO_LIST_NR );
		boolean initializeVariables = true;
		short imperativeParameter = Constants.NO_IMPERATIVE_PARAMETER;
		short specificationWordParameter = Constants.NO_WORD_PARAMETER;
		ReadItem currentGeneralizationReadItem = startGeneralizationWordReadItem_;

		if( currentGeneralizationReadItem != null )
			{
			if( endGeneralizationWordReadItem_ != null )
				{
				admin_.initializeAdminAssumptionVariables();
				admin_.initializeAdminConclusionVariables();
				admin_.initializeAdminSpecificationVariables();

				do	{
					switch( currentGeneralizationReadItem.grammarParameter )
						{
						case Constants.GRAMMAR_IMPERATIVE:
							imperativeParameter = currentGeneralizationReadItem.wordParameter();
							// Don't insert a break statement

						case Constants.GRAMMAR_GENERALIZATION_WORD:
							if( ( collectionResult = admin_.addUserSpecifications( initializeVariables, isAction, isAssignment_, isConditional, isDeactiveAssignment_, isArchiveAssignment_, isExclusive_, isNewStart, isPossessive_, isSpecificationGeneralization_, prepositionParameter_, questionParameter_, selectionLevel, selectionListNr, imperativeParameter, specificationWordParameter, specificationCollectionNr_, generalizationContextNr_, specificationContextNr_, relationContextNr_, currentGeneralizationReadItem, startSpecificationWordReadItem_, endSpecificationWordReadItem_, startRelationWordReadItem_, endRelationWordReadItem_ ) ).result == Constants.RESULT_OK )
								{
								if( collectionResult.needToRedoSpecificationCollection )
									{
									if( ( collectionResult = admin_.addUserSpecifications( initializeVariables, isAction, isAssignment_, isConditional, isDeactiveAssignment_, isArchiveAssignment_, isExclusive_, isNewStart, isPossessive_, isSpecificationGeneralization_, prepositionParameter_, questionParameter_, selectionLevel, selectionListNr, imperativeParameter, specificationWordParameter, specificationCollectionNr_, generalizationContextNr_, specificationContextNr_, relationContextNr_, currentGeneralizationReadItem, startSpecificationWordReadItem_, endSpecificationWordReadItem_, startRelationWordReadItem_, endRelationWordReadItem_ ) ).result != Constants.RESULT_OK )
										myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add the read user specifications" );
									}

								initializeVariables = false;
								}
							else
								myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add the read user specifications" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				!CommonVariables.hasShownWarning &&
				currentGeneralizationReadItem != endGeneralizationWordReadItem_ &&
				( currentGeneralizationReadItem = currentGeneralizationReadItem.nextCurrentLanguageReadItem() ) != null );
				}
			else
				return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The end generalization read item is undefined" );
			}
		else
			return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The start generalization read item is undefined" );

		return CommonVariables.result;
		}

	private byte readSpecification( boolean isAction, boolean isNewStart, short selectionLevel, short selectionListNr, String readSentenceString )
		{
		if( scanSpecification( readSentenceString ) == Constants.RESULT_OK )
			{
			if( addReadSpecification( isAction, isNewStart, selectionLevel, selectionListNr ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to add the read specification" );
			}
		else
			myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to scan the generalization-specification" );

		return CommonVariables.result;
		}

	private byte readImperative( boolean isAction, boolean isNewStart, short selectionLevel, short selectionListNr, String readSentenceString )
		{
		short imperativeParameter = Constants.NO_IMPERATIVE_PARAMETER;
		short specificationWordParameter = Constants.NO_WORD_PARAMETER;
		ReadItem firstReadItem;
		ReadItem imperativeReadItem;
		ReadItem specificationReadItem;
		WordItem imperativeWordItem = null;

		areReadItemsStillValid_ = true;

		if( currentReadItem_ != null )
			{
			startGeneralizationWordReadItem_ = null;
			endGeneralizationWordReadItem_ = null;

			if( scanSpecification( readSentenceString ) == Constants.RESULT_OK )
				{
				if( ( imperativeReadItem = startGeneralizationWordReadItem_ ) != null &&
				endGeneralizationWordReadItem_ != null )
					{
					do	{
						if( imperativeReadItem.isReadWordVerb() )
							{
							imperativeParameter = imperativeReadItem.wordParameter();
							imperativeWordItem = imperativeReadItem.readWordItem();
							}
						}
					while( ( imperativeReadItem = imperativeReadItem.nextCurrentLanguageReadItem() ) != null );

					if( ( specificationReadItem = startSpecificationWordReadItem_ ) == null )	// Only imperative word
						{
						if( admin_.executeImperative( true, Constants.NO_LIST_NR, startGeneralizationWordReadItem_.wordParameter(), specificationWordParameter, Constants.WORD_TYPE_UNDEFINED, Constants.MAX_PROGRESS, startGeneralizationWordReadItem_.readString, startGeneralizationWordReadItem_.readWordItem(), null, startRelationWordReadItem_, endRelationWordReadItem_, null, null ) != Constants.RESULT_OK )
							{
							if( startGeneralizationWordReadItem_ == null )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute the single imperative" );
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute the single imperative at position " + startGeneralizationWordReadItem_.wordPosition() );
							}
						}
					else		// Imperative word has specifications
						{
						if( endSpecificationWordReadItem_ != null )
							{
							if( addReadSpecification( isAction, isNewStart, selectionLevel, selectionListNr ) == Constants.RESULT_OK )
								{
								if( !CommonVariables.hasShownWarning &&
								selectionListNr == Constants.NO_LIST_NR )
									{
									if( ( firstReadItem = admin_.firstCurrentLanguageActiveReadItem() ) != null )
										{
										do	{
											if( specificationReadItem.isReadWordNoun() ||
											specificationReadItem.isUserDefined() )
												{
												if( specificationReadItem.isNounFile() ||				// Make distinction between reading a normal file or a test file
												specificationReadItem.isNounJustificationReport() )	// Make distinction between showing a word or justification report
													specificationWordParameter = specificationReadItem.wordParameter();
												else
													{
													if( admin_.executeImperative( true, Constants.NO_LIST_NR, imperativeParameter, ( specificationWordParameter == Constants.NO_WORD_PARAMETER ? specificationReadItem.wordParameter() : specificationWordParameter ), specificationReadItem.wordTypeNr(), Constants.MAX_PROGRESS, specificationReadItem.readString, imperativeWordItem, specificationReadItem.readWordItem(), startRelationWordReadItem_, endRelationWordReadItem_, null, null ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute an imperative with specifications" );
													}
												}

											if( CommonVariables.result == Constants.RESULT_OK &&
											firstReadItem != admin_.firstCurrentLanguageActiveReadItem() )
												areReadItemsStillValid_ = false;
											}
										while( CommonVariables.result == Constants.RESULT_OK &&
										areReadItemsStillValid_ &&
										!admin_.isRestart() &&
										!CommonVariables.hasShownWarning &&

										( specificationWordParameter > Constants.NO_WORD_PARAMETER ||		// Loop shouldn't end when virtual list prepositions are used to e.g. show justification reports
										!specificationReadItem.isVirtualListPreposition() ) &&

										specificationReadItem != endSpecificationWordReadItem_ &&
										( specificationReadItem = specificationReadItem.nextCurrentLanguageReadItem() ) != null );
										}
									else
										return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I couldn't find the first read item" );
									}
								}
							else
								{
								if( startGeneralizationWordReadItem_ == null ||
								endGeneralizationWordReadItem_ == null )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add the generalization part of the read specification" );
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add the generalization part of the read specification between the positions " + startGeneralizationWordReadItem_.wordPosition() + " and " + endGeneralizationWordReadItem_.wordPosition() );
								}
							}
						else
							return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The end specification read item is undefined" );
						}
					}
				else
					return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I couldn't find the imperative" );
				}
			else
				myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to scan the generalization-specification" );
			}
		else
			return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The current read item is undefined" );

		return CommonVariables.result;
		}

	private byte readText( boolean isAction, boolean isNewStart, short selectionLevel, short selectionListNr, String textString )
		{
		if( textString != null )
			{
			if( selectionListNr == Constants.NO_LIST_NR )
				{
				if( admin_.writeTextWithPossibleQueryCommands( Constants.PRESENTATION_PROMPT_INFO, textString ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a text with possible query commands" );
				}
			else
				{
				if( selectionListNr == Constants.ADMIN_ACTION_LIST ||
				selectionListNr == Constants.ADMIN_ALTERNATIVE_LIST )
					{
					if( admin_.createSelectionTextPart( isAction, isNewStart, selectionLevel, selectionListNr, Constants.GRAMMAR_TEXT, textString ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "I failed to create a selection text part" );
					}
				else
					return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given selection list number isn't the execution entry list number. A text can not be a part of the condition of a selection" );
				}
			}
		else
			return myWord_.setErrorInItem( admin_.adminListChar( selectionListNr ), 1, moduleNameString_, "The given text string is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminReadSentence( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		areReadItemsStillValid_ = false;
		deleteRollbackInfo_ = true;
		isAssignment_ = false;
		isConditional_ = false;
		isExclusive_ = false;
		isDeactiveAssignment_ = false;
		isArchiveAssignment_ = false;
		isPossessive_ = false;
		isSpecificationGeneralization_ = false;
		isLinkedGeneralizationConjunction_ = false;
		isGuidedByGrammarCanceled_ = false;
		showConclusions_ = false;

		prepositionParameter_ = Constants.NO_PREPOSITION_PARAMETER;
		questionParameter_ = Constants.NO_QUESTION_PARAMETER;

		generalizationContextNr_ = Constants.NO_CONTEXT_NR;
		specificationContextNr_ = Constants.NO_CONTEXT_NR;
		relationContextNr_ = Constants.NO_CONTEXT_NR;

		currentReadItem_ = null;
		startGeneralizationWordReadItem_ = null;
		endGeneralizationWordReadItem_ = null;
		startSpecificationWordReadItem_ = null;
		endSpecificationWordReadItem_ = null;
		startRelationWordReadItem_ = null;
		endRelationWordReadItem_ = null;

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

	protected void dontShowConclusions()
		{
		showConclusions_ = false;
		}

	protected void dontDeleteRollbackInfo()
		{
		deleteRollbackInfo_ = false;
		}

	protected boolean areReadItemsStillValid()
		{
		return areReadItemsStillValid_;
		}

	protected boolean isPossessivePronounStructure()
		{
		ReadItem readAheadWordItem = currentReadItem_;

		while( readAheadWordItem != null &&
		!readAheadWordItem.isReadWordPossessivePronoun1() )
			readAheadWordItem = readAheadWordItem.nextCurrentLanguageReadItem();

		return ( readAheadWordItem != null );
		}

	protected boolean isUserSentencePossessive()
		{
		return isPossessive_;
		}

	protected byte readSentence( String readSentenceString )
		{
		int startSentenceNr = CommonVariables.currentSentenceNr;

		CommonVariables.hasShownWarning = false;

		if( interpretSentence( readSentenceString ) == Constants.RESULT_OK )
			{
			if( !isGuidedByGrammarCanceled_ &&
			!CommonVariables.hasShownWarning )
				{
				showConclusions_ = true;
				CommonVariables.foundAnswerToQuestion = false;
				CommonVariables.isFirstAnswerToQuestion = true;
				CommonVariables.isQuestionAlreadyAnswered = false;
				CommonVariables.isSpecificationConfirmedByUser = false;
				CommonVariables.isUserQuestion = false;

				CommonVariables.lastShownMoreSpecificSpecificationItem = null;

				clearLastCheckedAssumptionLevelItemNrInAllWords();

				if( executeSentence( readSentenceString ) == Constants.RESULT_OK )
					{
					if( deleteRollbackInfo_ )
						{
						if( admin_.deleteRollbackInfo() != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the rollback info" );
						}
					else
						deleteRollbackInfo_ = true;

					if( CommonVariables.result == Constants.RESULT_OK &&
					showConclusions_ &&
					!admin_.isRestart() &&
					!CommonVariables.hasShownWarning &&
					startSentenceNr == CommonVariables.currentSentenceNr )
						{
						if( readSentenceString != null )
							{
							if( admin_.integrityCheck( ( questionParameter_ > Constants.NO_QUESTION_PARAMETER ), readSentenceString ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to perform an integrity check on sentence \"" + readSentenceString + "\"" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&

						( admin_.foundChange() &&
						!CommonVariables.hasShownWarning ) )		// Passed integrity check
							{
							// Show self-generated conclusions of the last sentence
							if( admin_.writeSelfGeneratedInfo( true, false, false ) == Constants.RESULT_OK )
								{
								// Show self-generated assumptions of the last sentence
								if( admin_.writeSelfGeneratedInfo( false, true, false ) == Constants.RESULT_OK )
									{
									// Show self-generated questions of the last sentence
									if( admin_.writeSelfGeneratedInfo( false, false, true ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the self-generated questions" );
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the self-generated assumptions" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the self-generated conclusions" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						!CommonVariables.hasShownWarning &&

						( admin_.foundChange() ||
						CommonVariables.isUserQuestion ) )
							{
							if( admin_.answerQuestions() != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to answer questions" );
							}
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute the sentence" );
				}
			}
		else
			{
			if( readSentenceString == null )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to interpret an undefined sentence" );
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to interpret the sentence: \"" + readSentenceString + "\"" );
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"You have turned my mourning into joyful dancing.
 *	You have taken away my clothes of mourning and
 *	clothed me with joy,
 *	that I might sing praises to you and not be silent.
 *	O Lord my God, I will give you thanks forever!" (Psalm 30:11-12)
 *
 *************************************************************************/
