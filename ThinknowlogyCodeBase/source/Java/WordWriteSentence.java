/*
 *	Class:			WordWriteSentence
 *	Supports class:	WordItem
 *	Purpose:		To write specifications as sentences
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

class WordWriteSentence
	{
	// Private constructible variables

	private boolean foundWordToWrite_;
	private boolean skipClearWriteLevel_;

	private short currentWriteLevel_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private void clearFromWriteLevelInWord( short writeLevel )
		{
		if( myWord_.writeList != null )
			myWord_.writeList.clearFromWriteLevel( writeLevel );
		}

	private void clearContextWriteLevelInAllWords( short currentWriteLevel, int contextNr )
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.clearContextWriteLevelInWord( currentWriteLevel, contextNr );
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		}

	private byte clearContextWriteLevel( short currentWriteLevel, SpecificationItem clearSpecificationItem )
		{
		if( clearSpecificationItem != null )
			{
			if( clearSpecificationItem.hasGeneralizationContext() )
				clearContextWriteLevelInAllWords( currentWriteLevel, clearSpecificationItem.generalizationContextNr() );

			if( clearSpecificationItem.hasSpecificationContext() )
				clearContextWriteLevelInAllWords( currentWriteLevel, clearSpecificationItem.specificationContextNr() );

			if( clearSpecificationItem.hasRelationContext() )
				clearContextWriteLevelInAllWords( currentWriteLevel, clearSpecificationItem.relationContextNr() );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given clear specification item is undefined" );

		return CommonVariables.result;
		}

	private byte clearWriteLevel( boolean writeGivenSpecificationWordOnly, short currentWriteLevel, SpecificationItem clearSpecificationItem )
		{
		boolean isAnsweredQuestion;
		boolean isExclusive;
		boolean isPossessive;
		int generalizationCollectionNr;
		int specificationCollectionNr;
		int generalizationContextNr;
		int specificationContextNr;
		int relationContextNr;
		SpecificationItem currentSpecificationItem;
		WordItem currentSpecificationWordItem;

		if( clearSpecificationItem != null )
			{
			if( CommonVariables.currentWriteLevel >= currentWriteLevel )
				{
				// Clear generalization
				myWord_.clearWriteLevel( currentWriteLevel );

				isAnsweredQuestion = clearSpecificationItem.isAnsweredQuestion();

				// Clear contexts
				if( clearContextWriteLevel( currentWriteLevel, clearSpecificationItem ) == Constants.RESULT_OK )
					{
					// Clear specification
					if( ( currentSpecificationItem = myWord_.firstSelectedSpecification( true, clearSpecificationItem.isAssignment(), clearSpecificationItem.isDeactiveItem(), clearSpecificationItem.isArchiveItem(), clearSpecificationItem.questionParameter() ) ) != null )
						{
						isExclusive = clearSpecificationItem.isExclusive();
						isPossessive = clearSpecificationItem.isPossessive();
						generalizationCollectionNr = clearSpecificationItem.generalizationCollectionNr();
						specificationCollectionNr = clearSpecificationItem.specificationCollectionNr();
						generalizationContextNr = clearSpecificationItem.generalizationContextNr();
						specificationContextNr = clearSpecificationItem.specificationContextNr();
						relationContextNr = clearSpecificationItem.relationContextNr();

						do	{
							if( currentSpecificationItem == clearSpecificationItem ||

							( !writeGivenSpecificationWordOnly &&
							currentSpecificationItem.isRelatedSpecification( isExclusive, isPossessive, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr ) ) )
								{
								if( ( currentSpecificationWordItem = currentSpecificationItem.specificationWordItem() ) == null )		// Specification string
									currentSpecificationItem.clearSpecificationStringWriteLevel( currentWriteLevel );
								else
									currentSpecificationWordItem.clearWriteLevel( currentWriteLevel );
								}
							}
						while( ( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemWithSameQuestionParameter( isAnsweredQuestion ) ) != null );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't find the specification" );
					}
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to clear the write parse level of the current specification item" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given grammar word parse level is greater than the current grammar word parse level" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given clear specification item is undefined" );

		return CommonVariables.result;
		}

	private byte cleanupWriteInfo( boolean writeGivenSpecificationWordOnly, short startWriteLevel, int startWordPosition, SpecificationItem clearSpecificationItem )
		{
		if( CommonVariables.writeSentenceStringBuffer != null &&
		CommonVariables.writeSentenceStringBuffer.length() > startWordPosition )
			{
			CommonVariables.writeSentenceStringBuffer = ( startWordPosition > 0 ? new StringBuffer( CommonVariables.writeSentenceStringBuffer.substring( 0, startWordPosition ) ) : null );

			if( CommonVariables.currentWriteLevel > startWriteLevel )
				{
				if( clearWriteLevel( writeGivenSpecificationWordOnly, startWriteLevel, clearSpecificationItem ) == Constants.RESULT_OK )
					myWord_.initializeWordWriteWordsSpecificationVariables( startWordPosition );
				else
					myWord_.addErrorInWord( 1, moduleNameString_, "I failed to cleanup the write levels of the write words" );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given start position is equal or smaller than the size of the write sentence string buffer" );

		return CommonVariables.result;
		}

	private WriteResultType createWriteWord( boolean isSkipped, boolean isChoiceEnd, short grammarLevel, GrammarItem parseGrammarItem )
		{
		WriteResultType writeResult = new WriteResultType();

		if( !myWord_.iAmAdmin() )
			{
			if( parseGrammarItem != null )
				{
				if( myWord_.writeList == null )
					{
					if( ( myWord_.writeList = new WriteList( myWord_ ) ) != null )
						myWord_.wordList[Constants.WORD_WRITE_LIST] = myWord_.writeList;
					else
						{
						writeResult.result = myWord_.setErrorInWord( 1, moduleNameString_, "I failed to create a write list" );
						return writeResult;
						}
					}

				return myWord_.writeList.createWriteItem( isSkipped, isChoiceEnd, grammarLevel, parseGrammarItem );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The given grammar definition word item is undefined" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The admin can not have a write list" );

		writeResult.result = CommonVariables.result;
		return writeResult;
		}

	private WriteItem firstWriteItem()
		{
		if( myWord_.writeList != null )
			return myWord_.writeList.firstActiveWriteItem();

		return null;
		}


	// Constructor

	protected WordWriteSentence( WordItem myWord )
		{
		String errorString = null;

		foundWordToWrite_ = false;
		skipClearWriteLevel_ = false;

		currentWriteLevel_ = Constants.NO_WRITE_LEVEL;

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

	protected byte parseGrammarToWriteSentence( boolean writeGivenSpecificationWordOnly, short answerParameter, short grammarLevel, GrammarItem parseGrammarItem, SpecificationItem writeSpecificationItem )
		{
		WriteResultType writeResult = new WriteResultType();
		boolean isChoice;
		boolean isOption;
		boolean stillSuccessful;
		boolean skipThisChoiceOrOptionPart;
		boolean skipNextChoiceOrOptionParts;
		boolean foundNewPathInGrammarRecursion = false;
		short startWriteLevel;
		short startParseLevel = CommonVariables.currentWriteLevel;
		int startWordPosition = ( CommonVariables.writeSentenceStringBuffer == null ? 0 : CommonVariables.writeSentenceStringBuffer.length() );
		GrammarItem definitionGrammarItem = parseGrammarItem;
		WriteItem lastCreatedChoiceOrOptionWriteItem;
		WriteItem newPathInGrammarRecursionWriteItem;
		WriteItem currentWriteItem = null;

		foundWordToWrite_ = false;

		if( grammarLevel < Constants.MAX_GRAMMAR_LEVEL )
			{
			if( parseGrammarItem != null )
				{
				if( writeSpecificationItem != null )
					{
					if( grammarLevel == Constants.NO_GRAMMAR_LEVEL )	// Init
						{
						skipClearWriteLevel_ = false;
						currentWriteLevel_ = Constants.NO_WRITE_LEVEL;
						CommonVariables.currentWriteLevel = Constants.NO_WRITE_LEVEL;

						CommonVariables.writeSentenceStringBuffer = null;

						myWord_.deleteWriteList();
						myWord_.initializeWordWriteWordsVariables();
						}

					startWriteLevel = currentWriteLevel_;

					do	{
						if( definitionGrammarItem.isDefinitionStart() )
							{
							if( definitionGrammarItem.isNewStart() )	// Grammar word
								{
								if( ( writeResult = myWord_.writeWordsToSentence( writeGivenSpecificationWordOnly, answerParameter, definitionGrammarItem, writeSpecificationItem ) ).result == Constants.RESULT_OK )
									{
									if( writeResult.foundWordToWrite )
										foundWordToWrite_ = true;

									skipClearWriteLevel_ = writeResult.skipClearWriteLevel;
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a word to a specification sentence" );
								}
							else										// Grammar definition
								{
								newPathInGrammarRecursionWriteItem = null;

								do	{
									if( ( parseGrammarItem = definitionGrammarItem.nextGrammarItem() ) != null )
										{
										isChoice = false;
										isOption = false;
										stillSuccessful = true;
										skipThisChoiceOrOptionPart = false;
										skipNextChoiceOrOptionParts = false;

										lastCreatedChoiceOrOptionWriteItem = null;

										do	{
											if( parseGrammarItem.isNewStart() )
												{
												if( isChoice ||
												isOption ||
												parseGrammarItem.isChoiceStart() ||
												parseGrammarItem.isOptionStart() )
													{
													currentWriteItem = firstWriteItem();

													while( currentWriteItem != null &&
													( currentWriteItem.grammarLevel() != grammarLevel ||
													currentWriteItem.startOfChoiceOrOptionGrammarItem() != parseGrammarItem ) )
														currentWriteItem = currentWriteItem.nextWriteItem();

													if( isChoice ||
													isOption )	// End of old choice or option - new one starts
														{
														skipThisChoiceOrOptionPart = false;

														if( foundWordToWrite_ )
															skipNextChoiceOrOptionParts = true;
														else
															{
															if( stillSuccessful &&
															currentWriteItem != null &&
															currentWriteItem.isSkipped )
																currentWriteItem.isSkipped = false;
															}

														if( currentWriteItem == null )
															{
															if( ( writeResult = createWriteWord( ( !stillSuccessful || skipNextChoiceOrOptionParts ), parseGrammarItem.isChoiceEnd, grammarLevel, parseGrammarItem ) ).result == Constants.RESULT_OK )
																lastCreatedChoiceOrOptionWriteItem = ( skipNextChoiceOrOptionParts ? null : writeResult.createdWriteItem );
															else
																myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a write word" );
															}
														else
															{
															if( currentWriteItem.startOfChoiceOrOptionGrammarItem().activeSentenceNr() == parseGrammarItem.activeSentenceNr() &&
															currentWriteItem.startOfChoiceOrOptionGrammarItem().itemNr() == parseGrammarItem.itemNr() )
																{
																if( currentWriteItem.grammarLevel() == grammarLevel )
																	{
																	if( currentWriteItem.isDeadEndRoad() )
																		{
																		skipThisChoiceOrOptionPart = true;
																		skipNextChoiceOrOptionParts = false;
																		}

																	if( lastCreatedChoiceOrOptionWriteItem != null )
																		{
																		lastCreatedChoiceOrOptionWriteItem.wasSuccessful = foundWordToWrite_;

																		if( lastCreatedChoiceOrOptionWriteItem.wasSuccessful )
																			lastCreatedChoiceOrOptionWriteItem.writeLevel = ++currentWriteLevel_;
																		}

																	if( newPathInGrammarRecursionWriteItem == null )
																		newPathInGrammarRecursionWriteItem = currentWriteItem;

																	lastCreatedChoiceOrOptionWriteItem = currentWriteItem;
																	currentWriteItem = currentWriteItem.nextWriteItem();
																	}
																else
																	return myWord_.setErrorInWord( 1, moduleNameString_, "I lost track of the grammar parse level" );
																}
															else
																return myWord_.setErrorInWord( 1, moduleNameString_, "I lost track of the grammar parse path" );
															}
														}
													else
														{
														if( parseGrammarItem.isChoiceStart() )
															isChoice = true;
														else
															isOption = true;

														skipThisChoiceOrOptionPart = false;

														if( currentWriteItem == null )
															{
															if( lastCreatedChoiceOrOptionWriteItem == null )
																{
																if( ( writeResult = createWriteWord( !stillSuccessful, parseGrammarItem.isChoiceEnd, grammarLevel, parseGrammarItem ) ).result == Constants.RESULT_OK )
																	{
																	lastCreatedChoiceOrOptionWriteItem = writeResult.createdWriteItem;

																	if( newPathInGrammarRecursionWriteItem == null )
																		newPathInGrammarRecursionWriteItem = writeResult.createdWriteItem;
																	}
																else
																	myWord_.addErrorInWord( 1, moduleNameString_, "I failed to create a start write word" );
																}
															else
																return myWord_.setErrorInWord( 1, moduleNameString_, "The last created choice or option is already assigned" );
															}
														else
															{
															if( currentWriteItem.startOfChoiceOrOptionGrammarItem().activeSentenceNr() == parseGrammarItem.activeSentenceNr() &&
															currentWriteItem.startOfChoiceOrOptionGrammarItem().itemNr() == parseGrammarItem.itemNr() )
																{
																if( currentWriteItem.grammarLevel() == grammarLevel )
																	{
																	if( currentWriteItem.isDeadEndRoad() )
																		skipThisChoiceOrOptionPart = true;
																	}
																else
																	return myWord_.setErrorInWord( 1, moduleNameString_, "I lost track of the grammar parse grammar level at the start of a choice or option" );
																}
															else
																return myWord_.setErrorInWord( 1, moduleNameString_, "I lost track of the grammar parse path at the start of a choice or option" );
															}
														}
													}
												}

											if( CommonVariables.result == Constants.RESULT_OK &&
											stillSuccessful &&
											!skipThisChoiceOrOptionPart &&
											!skipNextChoiceOrOptionParts &&

											( writeSpecificationItem.isQuestion() ||
											!parseGrammarItem.skipOptionForWriting() ) )
												{
												if( grammarLevel + 1 < Constants.MAX_GRAMMAR_LEVEL )
													{
													if( parseGrammarToWriteSentence( writeGivenSpecificationWordOnly, answerParameter, (short)( grammarLevel + 1 ), parseGrammarItem.definitionGrammarItem, writeSpecificationItem ) == Constants.RESULT_OK )
														{
														if( !foundWordToWrite_ )
															skipThisChoiceOrOptionPart = true;	// Failed, try next part
														}
													else
														myWord_.addErrorInWord( 1, moduleNameString_, "I failed to parse the grammar for writing a sentence at grammar level reached: #" + ( grammarLevel + 1 ) );
													}
												else
													return myWord_.setErrorInWord( 1, moduleNameString_, "There is probably an endless loop in the grammar definitions, because the grammar level reached: #" + ( grammarLevel + 1 ) );
												}

											if( CommonVariables.result == Constants.RESULT_OK )
												{
												if( parseGrammarItem.isChoiceEnd ||
												parseGrammarItem.isOptionEnd )
													{
													if( lastCreatedChoiceOrOptionWriteItem != null )
														{
														if( foundWordToWrite_ &&
														stillSuccessful &&
														!skipNextChoiceOrOptionParts )
															{
															lastCreatedChoiceOrOptionWriteItem.wasSuccessful = foundWordToWrite_;

															if( lastCreatedChoiceOrOptionWriteItem.wasSuccessful )
																lastCreatedChoiceOrOptionWriteItem.writeLevel = ++currentWriteLevel_;
															}

														lastCreatedChoiceOrOptionWriteItem = null;
														}

													skipThisChoiceOrOptionPart = false;
													skipNextChoiceOrOptionParts = false;

													if( parseGrammarItem.isChoiceEnd )
														isChoice = false;
													else
														{
														isOption = false;

														if( stillSuccessful )
															foundWordToWrite_ = true;
														}
													}

												parseGrammarItem = parseGrammarItem.nextGrammarItem();

												if( !isChoice &&
												!isOption &&
												!foundWordToWrite_ )
													stillSuccessful = false;
												}
											}
										while( CommonVariables.result == Constants.RESULT_OK &&
										parseGrammarItem != null &&
										!parseGrammarItem.isDefinitionStart() );

										foundNewPathInGrammarRecursion = false;

										if( CommonVariables.result == Constants.RESULT_OK &&
										!foundWordToWrite_ )
											{
											if( !definitionGrammarItem.hasGrammarParameter() &&
											newPathInGrammarRecursionWriteItem != null )
												foundNewPathInGrammarRecursion = newPathInGrammarRecursionWriteItem.foundNewPathInGrammarRecursion();

											currentWriteLevel_ = startWriteLevel;
											clearFromWriteLevelInWord( startWriteLevel );

											if( !skipClearWriteLevel_ &&
											CommonVariables.writeSentenceStringBuffer != null &&
											CommonVariables.writeSentenceStringBuffer.length() > startWordPosition )
												{
												if( cleanupWriteInfo( writeGivenSpecificationWordOnly, startParseLevel, startWordPosition, writeSpecificationItem ) != Constants.RESULT_OK )
													myWord_.addErrorInWord( 1, moduleNameString_, "I failed to cleanup the write info" );
												}
											}
										}
									else
										return myWord_.setErrorInWord( 1, moduleNameString_, "The grammar word item is undefined" );
									}
								while( CommonVariables.result == Constants.RESULT_OK &&
								parseGrammarItem != null &&
								foundNewPathInGrammarRecursion );
								}

							definitionGrammarItem = definitionGrammarItem.nextDefinitionGrammarItem;
							}
						else
							return myWord_.setErrorInWord( 1, moduleNameString_, "The grammar definition word item is not a definition start" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					!foundWordToWrite_ &&
					definitionGrammarItem != null );

					if( CommonVariables.result == Constants.RESULT_OK &&
					grammarLevel == Constants.NO_GRAMMAR_LEVEL )
						{
						if( clearWriteLevel( writeGivenSpecificationWordOnly, Constants.NO_WRITE_LEVEL, writeSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to clear the write word levels in all words" );
						}
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given parse grammar word item is undefined" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given grammar level is too high: #" + grammarLevel );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Let the faithful rejoice that he honors them.
 *	Let them sing for joy as they lie on their beds." (Psalm 149:5)
 *
 *************************************************************************/
