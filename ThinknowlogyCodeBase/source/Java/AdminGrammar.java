/*
 *	Class:			AdminGrammar
 *	Supports class:	AdminItem
 *	Purpose:		To read the grammar and interface files
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

class AdminGrammar
	{
	// Private constructible variables

	protected WordItem predefinedAdjectiveBusyWordItem_;
	protected WordItem predefinedAdjectiveDoneWordItem_;
	protected WordItem predefinedAdjectiveInvertedWordItem_;

	protected WordItem predefinedNounInterfaceLanguageWordItem_;
	protected WordItem predefinedNounPasswordWordItem_;
	protected WordItem predefinedNounSolveLevelWordItem_;
	protected WordItem predefinedNounSolveMethodWordItem_;
	protected WordItem predefinedNounSolveStrategyWordItem_;
	protected WordItem predefinedVerbLoginWordItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Constructor

	protected AdminGrammar( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		predefinedAdjectiveBusyWordItem_ = null;
		predefinedAdjectiveDoneWordItem_ = null;
		predefinedAdjectiveInvertedWordItem_ = null;

		predefinedNounInterfaceLanguageWordItem_ = null;
		predefinedNounPasswordWordItem_ = null;
		predefinedNounSolveLevelWordItem_ = null;
		predefinedNounSolveMethodWordItem_ = null;
		predefinedNounSolveStrategyWordItem_ = null;
		predefinedVerbLoginWordItem_ = null;

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

	protected byte addGrammar( String grammarString )
		{
		GrammarResultType grammarResult = new GrammarResultType();
		ReadResultType readResult = new ReadResultType();
		WordResultType wordResult = new WordResultType();
		boolean hasCreatedInterface = false;
		boolean foundChoiceAlternatives = false;
		boolean foundOnlyOptions = true;
		boolean foundParameter = false;
		boolean foundPipe = false;
		boolean foundWordTypeNr = false;
		boolean hasGrammarWords = false;
		boolean isChoice = false;
		boolean isChoiceCheck = false;
		boolean isChoiceStart = false;
		boolean isOption = false;
		boolean isOptionCheck = false;
		boolean isOptionStart = false;
		boolean isNewStart = true;
		boolean skipOptionForWriting = false;
		short grammarParameter = Constants.NO_GRAMMAR_PARAMETER;
		short wordTypeNr = Constants.WORD_TYPE_UNDEFINED;
		int firstCreationItemNr = Constants.NO_ITEM_NR;
		int grammarStringLength;
		int grammarPosition = 0;
		GrammarItem foundGrammarItem;
		GrammarItem grammarDefinitionItem = null;
		WordItem foundWordItem;
		WordItem createdWordItem;
		WordItem currentGrammarLanguageWordItem;

		if( grammarString != null )
			{
			if( ( grammarStringLength = grammarString.length() ) > 0 )
				{
				do	{
					if( ( readResult = admin_.getWordInfo( false, grammarPosition, grammarString ) ).result == Constants.RESULT_OK )
						{
						switch( grammarString.charAt( grammarPosition ) )
							{
							case Constants.QUERY_WORD_TYPE_CHAR:
								if( !foundWordTypeNr )
									{
									while( grammarPosition + 1 < grammarStringLength &&
									Character.isDigit( grammarString.charAt( grammarPosition + 1 ) ) )
										{
										foundWordTypeNr = true;
										wordTypeNr = (short)( wordTypeNr * 10 + grammarString.charAt( ++grammarPosition ) - '0' );
										}

									if( !foundWordTypeNr )
										return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the word type number from a grammar definition line" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "I found more than one word type paramaters defined in a grammar definition line" );

								break;

							case Constants.QUERY_PARAMETER_CHAR:
								if( !foundParameter )
									{
									while( grammarPosition + 1 < grammarStringLength &&
									Character.isDigit( grammarString.charAt( grammarPosition + 1 ) ) )
										{
										foundParameter = true;
										grammarParameter = (short)( grammarParameter * 10 + grammarString.charAt( ++grammarPosition ) - '0' );
										}

									if( !foundParameter )
										return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the grammar parameter from a grammar definition line" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "I found more than one values paramaters defined in a grammar definition line" );

								break;

							case Constants.GRAMMAR_WORD_DEFINITION_CHAR:
								if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
									{
									if( !hasCreatedInterface )
										{
										if( firstCreationItemNr == Constants.NO_ITEM_NR )		// Add grammar word or grammar definition word
											{
											if( ( readResult = admin_.getWordInfo( false, ++grammarPosition, grammarString ) ).result == Constants.RESULT_OK )
												{
												if( ( grammarResult = currentGrammarLanguageWordItem.findGrammar( ( grammarParameter >= Constants.GRAMMAR_SENTENCE ), grammarParameter, readResult.wordLength, grammarString.substring( grammarPosition ) ) ).result == Constants.RESULT_OK )
													{
													if( !foundWordTypeNr ||
													grammarParameter >= Constants.GRAMMAR_SENTENCE ||
													grammarResult.foundGrammarItem == null )
														{
														if( ( grammarResult = currentGrammarLanguageWordItem.createGrammar( true, ( foundParameter && grammarParameter < Constants.GRAMMAR_SENTENCE ), false, false, false, wordTypeNr, grammarParameter, readResult.wordLength, grammarString.substring( grammarPosition ), null ) ).result == Constants.RESULT_OK )
															{
															firstCreationItemNr = CommonVariables.currentItemNr;
															grammarDefinitionItem = grammarResult.createdGrammarItem;
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a grammar definition word item" );
														}
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a grammar definition item" );
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the current grammar word" );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition word must be the first word in the grammar definition" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "Interface definition and grammar definitions can not be mixed" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );

								break;

							case Constants.GRAMMAR_OPTION_READ_NOT_WRITE_START:
								skipOptionForWriting = true;

								// Don't insert a break statement here

							case Constants.GRAMMAR_OPTION_START:
								if( !foundWordTypeNr ||
								grammarParameter >= Constants.GRAMMAR_SENTENCE )
									{
									if( CommonVariables.currentItemNr > Constants.NO_ITEM_NR )
										{
										if( !isOption )
											{
											if( !foundPipe )
												{
												isOption = true;
												isNewStart = true;
												isOptionStart = true;
												isChoiceCheck = isChoice;
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "Pipes with different levels isn't allowed in the grammar definition" );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar opion definition can not be nested" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition must start with a grammar definition word" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition can not have a value parameter lower than the grammar value" );

								break;

							case Constants.GRAMMAR_OPTION_READ_NOT_WRITE_END:
								skipOptionForWriting = false;

								// Don't insert a break statement here

							case Constants.GRAMMAR_OPTION_END:
								if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
									{
									if( isOption )
										{
										if( isChoiceCheck == isChoice )
											{
											if( !foundPipe )
												{
												isOption = false;
												isNewStart = true;
												currentGrammarLanguageWordItem.setOptionEnd();
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "I found an extra pipe character before a square bracket in the grammar definition" );
											}
										else
											{
											if( isChoice )
												return myWord_.setErrorInItem( 1, moduleNameString_, "Choices are started within an option" );
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "Choices are ended within an option" );
											}
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I found an extra square bracket character in the grammar definition" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );

								break;

							case Constants.GRAMMAR_CHOICE_START:
								if( !foundWordTypeNr ||
								grammarParameter >= Constants.GRAMMAR_SENTENCE )
									{
									if( CommonVariables.currentItemNr > Constants.NO_ITEM_NR )
										{
										if( !isChoice )
											{
											if( !foundPipe )
												{
												isChoice = true;
												isNewStart = true;
												isChoiceStart = true;
												isOptionCheck = isOption;
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "Pipes with different levels isn't allowed in the grammar definition" );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "Nesting curved brackets isn't allowed" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition must start with a grammar definition word" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition can not have a value parameter lower than the grammar value" );

								break;

							case Constants.GRAMMAR_CHOICE_END:
								if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
									{
									if( isChoice )
										{
										if( isOptionCheck == isOption )
											{
											if( !foundPipe )
												{
												if( foundChoiceAlternatives )
													{
													isChoice = false;
													isNewStart = true;
													foundChoiceAlternatives = false;
													currentGrammarLanguageWordItem.setChoiceEnd( CommonVariables.currentItemNr );
													}
												else
													return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition choice must have alternatives" );
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "I found an extra pipe character before a curved bracket in the grammar definition" );
											}
										else
											{
											if( isOption )
												return myWord_.setErrorInItem( 1, moduleNameString_, "An option is started within choices" );
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "An option is ended within choices" );
											}
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I found an extra curved bracket character in the grammar definition" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );

								break;

							case Constants.SYMBOL_PIPE:
								if( !foundWordTypeNr ||
								grammarParameter >= Constants.GRAMMAR_SENTENCE )
									{
									if( !foundPipe )
										{
										if( isOption ||
										isChoice )
											{
											foundPipe = true;

											if( isChoice )
												foundChoiceAlternatives = true;
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "Pipes are only allowed within grammar definition options or choices" );
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "I found an extra pipe character in the grammar definition" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "A grammar definition can not have a value parameter lower than the grammar value" );

								break;

							case Constants.SYMBOL_DOUBLE_QUOTE:	// Interface definition or Guided by Grammar
								if( isNewStart &&
								!hasGrammarWords &&
								!isChoice &&
								!isOption )
									{
									if( grammarDefinitionItem == null )		// Interface definition
										{
										if( foundParameter )
											{
											grammarPosition++;

											if( grammarPosition < grammarStringLength ||
											grammarString.charAt( grammarStringLength - 1 ) == Constants.SYMBOL_DOUBLE_QUOTE )
												{
												if( CommonVariables.currentInterfaceLanguageWordItem != null )
													{
													if( CommonVariables.currentInterfaceLanguageWordItem.checkInterface( grammarParameter, grammarString.substring( grammarPosition ) ) == Constants.RESULT_OK )
														{
														if( CommonVariables.currentInterfaceLanguageWordItem.createInterface( grammarParameter, ( grammarStringLength - grammarPosition - 1 ), grammarString.substring( grammarPosition ) ) == Constants.RESULT_OK )
															{
															hasCreatedInterface = true;
															grammarPosition = ( grammarStringLength - 1 );
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add an interface definition word item" );
														}
													else
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add an interface definition word item" );
													}
												else
													return myWord_.setErrorInItem( 1, moduleNameString_, "The current interface language word item is undefined" );
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "I could a corrupte interface definition" );
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "An interface definition must have a parameter" );
										}
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "Grammar definition and interface definitions can not be mixed" );

								break;

							default:
								if( !hasCreatedInterface )
									{
									if( CommonVariables.currentItemNr > Constants.NO_ITEM_NR )
										{
										if( foundParameter &&
										grammarParameter < Constants.GRAMMAR_SENTENCE )
											{
											if( grammarParameter > Constants.NO_GRAMMAR_PARAMETER )
												{
												if( grammarParameter == Constants.WORD_PLURAL_NOUN_ENDINGS )
													{
													if( grammarDefinitionItem != null )
														{
														if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
															{
															if( ( grammarResult = currentGrammarLanguageWordItem.createGrammar( false, false, false, false, false, Constants.WORD_TYPE_UNDEFINED, grammarParameter, readResult.wordLength, grammarString.substring( grammarPosition ), grammarDefinitionItem ) ).result == Constants.RESULT_OK )
																{
																if( grammarDefinitionItem.nextDefinitionGrammarItem == null )
																	grammarDefinitionItem.nextDefinitionGrammarItem = grammarResult.createdGrammarItem;
																else
																	return myWord_.setErrorInItem( 1, moduleNameString_, "The next definition grammar item is undefined" );
																}
															else
																myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a grammar definition word item" );
															}
														else
															return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
														}
													else
														return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition item is undefined" );
													}
												else
													{
													if( ( foundWordItem = myWord_.predefinedWordItem( grammarParameter ) ) == null )
														{
														if( ( wordResult = admin_.createWord( false, wordTypeNr, grammarParameter, readResult.wordLength, grammarString.substring( grammarPosition ) ) ).result == Constants.RESULT_OK )
															{
															if( ( createdWordItem = wordResult.createdWordItem ) != null )
																{
																switch( grammarParameter )
																	{
																	case Constants.WORD_PARAMETER_ADJECTIVE_BUSY:
																		if( predefinedAdjectiveBusyWordItem_ == null )
																			predefinedAdjectiveBusyWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedAdjectiveBusyWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined busy adjective word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_ADJECTIVE_DONE:
																		if( predefinedAdjectiveDoneWordItem_ == null )
																			predefinedAdjectiveDoneWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedAdjectiveDoneWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined done adjective word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_ADJECTIVE_INVERTED:
																		if( predefinedAdjectiveInvertedWordItem_ == null )
																			predefinedAdjectiveInvertedWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedAdjectiveInvertedWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined inverted adjective word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_GRAMMAR_LANGUAGE:
																		if( CommonVariables.predefinedNounGrammarLanguageWordItem == null )
																			{
																			CommonVariables.predefinedNounGrammarLanguageWordItem = createdWordItem;

																			if( admin_.authorizeLanguageWord( CommonVariables.predefinedNounGrammarLanguageWordItem ) == Constants.RESULT_OK )
																				{
																				if( admin_.createLanguageSpecification( CommonVariables.currentGrammarLanguageWordItem, CommonVariables.predefinedNounGrammarLanguageWordItem ) != Constants.RESULT_OK )
																					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a grammar language specification" );
																				}
																			else
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the predefined grammar language noun word" );
																			}
																		else
																			{
																			if( CommonVariables.predefinedNounGrammarLanguageWordItem != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined grammar language noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_INTERFACE_LANGUAGE:
																		if( predefinedNounInterfaceLanguageWordItem_ == null )
																			{
																			predefinedNounInterfaceLanguageWordItem_ = createdWordItem;

																			if( admin_.authorizeLanguageWord( predefinedNounInterfaceLanguageWordItem_ ) == Constants.RESULT_OK )
																				{
																				if( admin_.createLanguageSpecification( CommonVariables.currentInterfaceLanguageWordItem, predefinedNounInterfaceLanguageWordItem_ ) != Constants.RESULT_OK )
																					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an interface language specification" );
																				}
																			else
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the predefined interface language noun word" );
																			}
																		else
																			{
																			if( predefinedNounInterfaceLanguageWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined interface language noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_PASSWORD:
																		if( predefinedNounPasswordWordItem_ == null )
																			{
																			predefinedNounPasswordWordItem_ = createdWordItem;

																			if( admin_.authorizeWord( predefinedNounPasswordWordItem_ ) != Constants.RESULT_OK )
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the predefined password noun word" );
																			}
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedNounPasswordWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined password noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_SOLVE_LEVEL:
																		if( predefinedNounSolveLevelWordItem_ == null )
																			predefinedNounSolveLevelWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedNounSolveLevelWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined solve level noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_SOLVE_METHOD:
																		if( predefinedNounSolveMethodWordItem_ == null )
																			predefinedNounSolveMethodWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedNounSolveMethodWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined solve-method noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_SOLVE_STRATEGY:
																		if( predefinedNounSolveStrategyWordItem_ == null )
																			predefinedNounSolveStrategyWordItem_ = createdWordItem;
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedNounSolveStrategyWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined solve-strategy noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_NOUN_USER:
																		if( CommonVariables.predefinedNounUserWordItem == null )
																			{
																			CommonVariables.predefinedNounUserWordItem = createdWordItem;

																			if( admin_.authorizeWord( CommonVariables.predefinedNounUserWordItem ) != Constants.RESULT_OK )
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the predefined user noun word" );
																			}
																		else
																			{
																			if( createdWordItem != null &&
																			CommonVariables.predefinedNounUserWordItem != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined user noun word" );
																			}

																		break;

																	case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_LOGIN:
																		if( predefinedVerbLoginWordItem_ == null )
																			{
																			predefinedVerbLoginWordItem_ = createdWordItem;

																			if( admin_.authorizeWord( predefinedVerbLoginWordItem_ ) != Constants.RESULT_OK )
																				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the predefined login verb word" );
																			}
																		else
																			{
																			if( createdWordItem != null &&
																			predefinedVerbLoginWordItem_ != createdWordItem )
																				return myWord_.setErrorInItem( 1, moduleNameString_, "I found a different predefined login verb word" );
																			}

																		break;
																	}
																}
															else
																return myWord_.setErrorInItem( 1, moduleNameString_, "The last created word item is undefined" );
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a grammar word" );
														}
													else
														{
														if( foundWordItem.createWordType( ( grammarParameter == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_NEUTER || grammarParameter == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_MALE_FEMALE ), false, wordTypeNr, readResult.wordLength, grammarString.substring( grammarPosition ) ) == Constants.RESULT_OK )
															{
															switch( grammarParameter )
																{
																case Constants.WORD_PARAMETER_NOUN_GRAMMAR_LANGUAGE:
																	if( CommonVariables.predefinedNounGrammarLanguageWordItem != null )
																		{
																		if( admin_.createLanguageSpecification( CommonVariables.currentGrammarLanguageWordItem, CommonVariables.predefinedNounGrammarLanguageWordItem ) != Constants.RESULT_OK )
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a grammar language specification" );
																		}
																	else
																		return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined grammar language noun word item is undefined" );

																	break;

																case Constants.WORD_PARAMETER_NOUN_INTERFACE_LANGUAGE:
																	if( predefinedNounInterfaceLanguageWordItem_ != null )
																		{
																		if( admin_.createLanguageSpecification( CommonVariables.currentInterfaceLanguageWordItem, predefinedNounInterfaceLanguageWordItem_ ) != Constants.RESULT_OK )
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an interface language specification" );
																		}
																	else
																		return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined interface language noun word item is undefined" );
																}
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create another word type for grammar word \"" + foundWordItem.anyWordTypeString() + "\"" );
														}
													}
												}
											}
										else
											{
											if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
												{
												// Get grammar identification
												if( ( grammarResult = currentGrammarLanguageWordItem.findGrammar( true, Constants.NO_GRAMMAR_PARAMETER, readResult.wordLength, grammarString.substring( grammarPosition ) ) ).result == Constants.RESULT_OK )
													{
													foundGrammarItem = grammarResult.foundGrammarItem;

													if( ( grammarResult = currentGrammarLanguageWordItem.createGrammar( false, ( foundPipe || isNewStart ), isOptionStart, isChoiceStart, skipOptionForWriting, Constants.WORD_TYPE_UNDEFINED, Constants.NO_GRAMMAR_PARAMETER, readResult.wordLength, grammarString.substring( grammarPosition ), foundGrammarItem ) ).result == Constants.RESULT_OK )
														{
														isNewStart = false;
														isOptionStart = false;
														isChoiceStart = false;

														foundPipe = false;
														hasGrammarWords = true;

														if( !isOptionStart )
															foundOnlyOptions = false;

														if( foundGrammarItem != null &&
														!foundGrammarItem.hasCurrentCreationSentenceNr() )
															foundGrammarItem.isGrammarItemInUse = true;
														}
													else
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a grammar item" );
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a grammar definition word item" );
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
											}
										}
									else
										return myWord_.setErrorInItem( 1, moduleNameString_, "The first grammar word in a grammar definition is the grammar definition word and must start with a grammar character" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "Interface definition and grammar definitions can not be mixed" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						readResult.nextWordPosition < grammarStringLength )
							{
							if( ( readResult = admin_.getWordInfo( false, grammarPosition, grammarString ) ).result == Constants.RESULT_OK )
								grammarPosition = readResult.nextWordPosition;
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the next word" );
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the current grammar word" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				readResult.nextWordPosition < grammarStringLength );

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( isOption )
						return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition option isn't closed" );

					if( isChoice )
						return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition choice isn't closed" );

					if( foundPipe )
						return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition ended with an open pipe" );

					if( grammarDefinitionItem != null )
						{
						if( foundOnlyOptions &&

						( !foundWordTypeNr ||
						grammarParameter >= Constants.GRAMMAR_SENTENCE ) )
							{
							if( hasGrammarWords )
								return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition only exists of options" );
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar definition only exists of a grammar definition word" );
							}
						else
							{
							if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
								{
								// Remove possible duplicate grammar definition
								if( ( grammarResult = currentGrammarLanguageWordItem.removeDuplicateGrammarDefinition() ).result == Constants.RESULT_OK )
									{
									if( !grammarResult.foundDuplicateGrammar )
										{
										if( currentGrammarLanguageWordItem.linkLaterDefinedGrammarWords() != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to link later defined grammar words" );
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove a possible duplicate grammar definition" );
								}
							else
								return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
							}
						}
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given grammar string is empty" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given grammar string is undefined" );

		return CommonVariables.result;
		}

	protected WordItem predefinedAdjectiveBusyWordItem()
		{
		return predefinedAdjectiveBusyWordItem_;
		}

	protected WordItem predefinedAdjectiveDoneWordItem()
		{
		return predefinedAdjectiveDoneWordItem_;
		}

	protected WordItem predefinedAdjectiveInvertedWordItem()
		{
		return predefinedAdjectiveInvertedWordItem_;
		}

	protected WordItem predefinedNounInterfaceLanguageWordItem()
		{
		return predefinedNounInterfaceLanguageWordItem_;
		}

	protected WordItem predefinedNounPasswordWordItem()
		{
		return predefinedNounPasswordWordItem_;
		}

	protected WordItem predefinedNounSolveLevelWordItem()
		{
		return predefinedNounSolveLevelWordItem_;
		}

	protected WordItem predefinedNounSolveMethodWordItem()
		{
		return predefinedNounSolveMethodWordItem_;
		}

	protected WordItem predefinedNounSolveStrategyWordItem()
		{
		return predefinedNounSolveStrategyWordItem_;
		}

	protected WordItem predefinedVerbLoginWordItem()
		{
		return predefinedVerbLoginWordItem_;
		}
	};

/*************************************************************************
 *
 *	"The Lord is king!
 *	Let the nations tremble!
 *	He sits on his trone between the cherubim.
 *	Let the whole earth quake!" (Psalm 99:1)
 *
 *************************************************************************/
