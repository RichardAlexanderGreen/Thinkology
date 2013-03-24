/*
 *	Class:			AdminReadCreateWords
 *	Supports class:	AdminItem
 *	Purpose:		To create words of the read sentence
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

class AdminReadCreateWords
	{
	// Private constructible variables

	private boolean hasCreatedReadWord_;
	private boolean isProbablyPluralNoun_;

	private short wordTypeNr_;

	private int currentWordPosition_;

	private int singularWordLength_;
	private int wordStringLength_;

	private String exactWordString_;
	private String lowerCaseWordString_;
	private String singularNounString_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean isSymbol( char character )
		{
		return ( character == Constants.SYMBOL_COMMA ||
			character == Constants.SYMBOL_COLON ||
			character == Constants.SYMBOL_SEMI_COLON ||
			character == Constants.SYMBOL_DOUBLE_COLON ||
			character == Constants.SYMBOL_QUESTION_MARK ||
			character == Constants.SYMBOL_EXCLAMATION_MARK ||
			character == Constants.SYMBOL_PIPE ||
			character == Constants.SYMBOL_AMPERSAND ||
			character == Constants.SYMBOL_ASTERISK ||
			character == Constants.SYMBOL_PERCENT ||
			character == Constants.SYMBOL_DOLLAR ||
			character == Constants.SYMBOL_SLASH ||
			character == Constants.SYMBOL_BACK_SLASH ||
			character == Constants.SYMBOL_QUOTE ||
			// Don't add Constants.SYMBOL_DOUBLE_QUOTE to avoid analyzing in text strings
			character == Constants.SYMBOL_OPEN_ROUNDED_BRACKET ||
			character == Constants.SYMBOL_CLOSE_ROUNDED_BRACKET ||
			character == Constants.SYMBOL_OPEN_CURVED_BRACKET ||
			character == Constants.SYMBOL_CLOSE_CURVED_BRACKET ||
			character == Constants.SYMBOL_OPEN_HOOKED_BRACKET ||
			character == Constants.SYMBOL_CLOSE_HOOKED_BRACKET ||
			character == Constants.SYMBOL_OPEN_SQUARE_BRACKET ||
			character == Constants.SYMBOL_CLOSE_SQUARE_BRACKET );
		}

	private byte comparePluralEndingOfWord( int singularNounWordStringLength, int nounWordStringLength, String singularNounEndingString, String nounWordString, String pluralNounEndingString )
		{
		int tempWordLength;
		int pluralNounEndingStringLength;

		isProbablyPluralNoun_ = false;
		singularWordLength_ = 0;
		singularNounString_ = null;

		if( nounWordStringLength > 0 )
			{
			if( nounWordString != null )
				{
				if( pluralNounEndingString != null )
					{
					pluralNounEndingStringLength = pluralNounEndingString.length();
					tempWordLength = ( nounWordStringLength - pluralNounEndingStringLength );
					singularWordLength_ = ( nounWordStringLength + singularNounWordStringLength - pluralNounEndingStringLength );

					if( tempWordLength >= 0 &&
					singularWordLength_ > 0 )
						{
						if( nounWordString.substring( tempWordLength ).startsWith( pluralNounEndingString ) )
							{
							isProbablyPluralNoun_ = true;

							singularNounString_ = new String( nounWordString.substring( 0, tempWordLength ) + ( singularNounEndingString == null ? Constants.EMPTY_STRING : singularNounEndingString ) );
							}
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The grammar string is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given noun word string is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The noun word string length is undefined" );

		return CommonVariables.result;
		}

	private byte checkNounWordType( int nounWordStringLength, String nounWordString, GrammarItem pluralNounEndingGrammarItem )
		{
		String singularNounEndingString = null;
		GrammarItem singularNounEndingGrammarItem;

		singularWordLength_ = 0;
		singularNounString_ = null;

		if( nounWordStringLength > 0 )
			{
			if( nounWordString != null )
				{
				if( pluralNounEndingGrammarItem != null )
					{
					do	{
						singularNounEndingGrammarItem = pluralNounEndingGrammarItem.nextDefinitionGrammarItem;
						singularNounEndingString = ( singularNounEndingGrammarItem == null ? null : singularNounEndingGrammarItem.grammarString() );

						if( comparePluralEndingOfWord( ( singularNounEndingString == null ? 0 : singularNounEndingString.length() ), nounWordStringLength, singularNounEndingString, nounWordString, pluralNounEndingGrammarItem.itemString() ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the plural ending of an undefined word type" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					!isProbablyPluralNoun_ &&
					( pluralNounEndingGrammarItem = pluralNounEndingGrammarItem.nextPluralNounEndingGrammarItem() ) != null );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given noun word string is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given noun word string is empty" );

		return CommonVariables.result;
		}

	private byte checkNounWordTypeOfAllLanguages( int nounWordStringLength, String nounWordString )
		{
		GeneralizationItem currentGeneralizationItem;
		GrammarItem pluralNounEndingGrammarItem;
		WordItem currentGeneralizationWordItem;
		WordItem currentGrammarLanguageWordItem;

		singularWordLength_ = 0;
		singularNounString_ = null;

		if( ( currentGrammarLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem ) != null )
			{
			if( ( pluralNounEndingGrammarItem = CommonVariables.currentGrammarLanguageWordItem.firstPluralNounEndingGrammarItem() ) != null )
				{
				if( checkNounWordType( nounWordStringLength, nounWordString, pluralNounEndingGrammarItem ) == Constants.RESULT_OK )
					{
					if( !isProbablyPluralNoun_ )
						{
						if( CommonVariables.predefinedNounGrammarLanguageWordItem != null )
							{
							if( ( currentGeneralizationItem = CommonVariables.predefinedNounGrammarLanguageWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
								{
								do	{
									if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
										{
										if( currentGeneralizationWordItem != currentGrammarLanguageWordItem )
											{
											if( ( pluralNounEndingGrammarItem = currentGeneralizationWordItem.firstPluralNounEndingGrammarItem() ) != null )
												{
												if( checkNounWordType( nounWordStringLength, nounWordString, pluralNounEndingGrammarItem ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for noun type" );
												}
											else
												return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the first plural noun ending grammar item of grammar word \"" + currentGeneralizationWordItem.anyWordTypeString() + "\"" );
											}
										}
									}
								while( CommonVariables.result == Constants.RESULT_OK &&
								!isProbablyPluralNoun_ &&
								( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined grammar language noun word item is undefined" );
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check for noun type" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to get the first plural noun ending grammar item" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );

		return CommonVariables.result;
		}

	private byte createWordStrings( String wordString )
		{
		ReadResultType readResult = new ReadResultType();
		wordStringLength_ = 0;
		exactWordString_ = null;
		lowerCaseWordString_ = null;

		if( wordString != null )
			{
			if( ( readResult = getWordInfo( false, 0, wordString ) ).result == Constants.RESULT_OK )
				{
				if( ( wordStringLength_ = readResult.wordLength ) > 0 )
					{
					exactWordString_ = wordString.substring( 0, wordStringLength_ );
					lowerCaseWordString_ = wordString.substring( 0, wordStringLength_ ).toLowerCase();
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given word string is empty or has no words left anymore" );
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the given word string" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given word string is undefined" );

		return CommonVariables.result;
		}

	private byte getWordTypeNr( boolean checkPropername, int wordTypeStringLength, String wordTypeString )
		{
		int wordPosition = 0;

		wordTypeNr_ = Constants.WORD_TYPE_UNDEFINED;

		if( wordTypeString != null )
			{
			if( wordTypeStringLength > 0 )
				{
				if( Character.isLetter( wordTypeString.charAt( wordPosition ) ) )
					{
					if( wordTypeStringLength == 1 )
						wordTypeNr_ = ( Character.isUpperCase( wordTypeString.charAt( wordPosition ) ) ? Constants.WORD_TYPE_LETTER_CAPITAL : Constants.WORD_TYPE_LETTER_SMALL );
					else
						{
						if( checkPropername &&
						Character.isUpperCase( wordTypeString.charAt( wordPosition ) ) )
							wordTypeNr_ = Constants.WORD_TYPE_PROPER_NAME;
						}
					}
				else
					{
					while( wordPosition < wordTypeStringLength &&
					Character.isDigit( wordTypeString.charAt( wordPosition ) ) )
						wordPosition++;

					if( wordPosition == wordTypeStringLength )
						wordTypeNr_ = Constants.WORD_TYPE_NUMERAL;
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given word type string is empty" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given word type string is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminReadCreateWords( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		hasCreatedReadWord_ = false;
		isProbablyPluralNoun_ = false;

		wordTypeNr_ = Constants.WORD_TYPE_UNDEFINED;

		currentWordPosition_ = 0;
		singularWordLength_ = 0;
		wordStringLength_ = 0;

		exactWordString_ = null;
		lowerCaseWordString_ = null;
		singularNounString_ = null;

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

	protected ReadResultType createReadWords( String readSentenceString )
		{
		ReadResultType readResult = new ReadResultType();
		WordResultType wordResult = new WordResultType();
		boolean foundExactWord;
		boolean foundSingularNounWord;
		boolean foundPluralNounWord;
		boolean wasPreviousWordAdjective;
		boolean wasPreviousWordArticle;
		boolean wasPreviousWordConjunction;
		boolean wasPreviousWordDefiniteArticle;
		boolean wasPreviousWordPossessivePronoun1;
		boolean wasPreviousWordSymbol;
		boolean wasPreviousWordUndefined = false;
		boolean isSymbol = false;
		boolean isLetter = false;
		boolean isNumeral = false;
		boolean isAdjective = false;
		boolean isArticle = false;
		boolean isConjunction = false;
		boolean isDefiniteArticle = false;
		boolean isPossessivePronoun1 = false;
		boolean isUndefinedWord = false;
		boolean isPredefinedBasicVerb = false;
		int readPosition = 0;
		int readSentenceStringLength;
		WordItem currentWordItem;
		WordItem createdWordItem;
		WordItem foundWordItem;
		WordItem pluralNounWordItem;
		WordItem singularNounWordItem;
		WordTypeItem foundWordTypeItem;

		hasCreatedReadWord_ = true;		// Set to pass while loop for the first time
		currentWordPosition_ = 0;

		if( readSentenceString != null )
			{
			if( ( readSentenceStringLength = readSentenceString.length() ) > 0 )
				{
				do	{
					if( ++currentWordPosition_ < Constants.MAX_POSITION )
						{
						hasCreatedReadWord_ = false;

						if( readSentenceString.charAt( readPosition ) == Constants.SYMBOL_DOUBLE_QUOTE )
							{
							if( createReadWord( CommonVariables.currentGrammarLanguageNr, Constants.WORD_TYPE_TEXT, currentWordPosition_, readSentenceString.substring( readPosition ), null ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a read word" );
							}
						else
							{
							foundExactWord = false;
							foundSingularNounWord = false;
							foundPluralNounWord = false;

							wasPreviousWordAdjective = isAdjective;
							wasPreviousWordArticle = isArticle;
							wasPreviousWordConjunction = isConjunction;
							wasPreviousWordDefiniteArticle = isDefiniteArticle;
							wasPreviousWordPossessivePronoun1 = isPossessivePronoun1;
							wasPreviousWordSymbol = isSymbol;
							wasPreviousWordUndefined = isUndefinedWord;

							isSymbol = false;
							isLetter = false;
							isNumeral = false;
							isAdjective = false;
							isArticle = false;
							isConjunction = false;
							isDefiniteArticle = false;
							isPossessivePronoun1 = false;
							isUndefinedWord = false;
							isPredefinedBasicVerb = false;

							createdWordItem = null;
							pluralNounWordItem = null;
							singularNounWordItem = null;

							if( createWordStrings( readSentenceString.substring( readPosition ) ) == Constants.RESULT_OK )
								{
								// Do in all words for an exact match
								if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
									{
									do	{
										if( ( wordResult = currentWordItem.findWordType( true, false, Constants.WORD_TYPE_UNDEFINED, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
											{
											if( ( foundWordTypeItem = wordResult.foundWordTypeItem ) != null )	// Found exact word
												{
												if( createReadWord( foundWordTypeItem.wordTypeLanguageNr(), foundWordTypeItem.wordTypeNr(), currentWordPosition_, null, currentWordItem ) == Constants.RESULT_OK )
													{
													foundExactWord = true;

													if( foundWordTypeItem.isSymbol() )
														isSymbol = true;

													if( foundWordTypeItem.isLetter() )
														isLetter = true;

													if( foundWordTypeItem.isNumeral() )
														isNumeral = true;

													if( foundWordTypeItem.isAdjective() )
														isAdjective = true;

													if( foundWordTypeItem.isArticle() )
														isArticle = true;

													if( foundWordTypeItem.isConjunction() )
														isConjunction = true;

													if( foundWordTypeItem.isDefiniteArticle() )
														isDefiniteArticle = true;

													if( foundWordTypeItem.isSingularNounWordType() )
														foundSingularNounWord = true;

													if( foundWordTypeItem.isPluralNounWordType() )
														foundPluralNounWord = true;

													if( foundWordTypeItem.isPossessivePronoun1() )
														isPossessivePronoun1 = true;

													if( currentWordItem.isPredefinedBasicVerb() )
														isPredefinedBasicVerb = true;
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create another read word" );
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find exact words" );
										}
									while( CommonVariables.result == Constants.RESULT_OK &&
									( currentWordItem = currentWordItem.nextWordItem() ) != null );

									if( CommonVariables.result == Constants.RESULT_OK &&
									currentWordPosition_ == 1 &&	// First word in sentence
									Character.isLetter( readSentenceString.charAt( readPosition ) ) )
										{
										// Do in all words for a match with difference case of the first letter
										if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
											{
											do	{
												if( ( wordResult = currentWordItem.findWordType( true, true, Constants.WORD_TYPE_UNDEFINED, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
													{
													if( ( foundWordTypeItem = wordResult.foundWordTypeItem ) != null )	// Found word with difference case of the first letter in this word
														{
														if( createReadWord( foundWordTypeItem.wordTypeLanguageNr(), foundWordTypeItem.wordTypeNr(), currentWordPosition_, null, currentWordItem ) == Constants.RESULT_OK )
															{
															if( foundWordTypeItem.isArticle() )
																isArticle = true;

															if( foundWordTypeItem.isDefiniteArticle() )
																isDefiniteArticle = true;

															if( foundWordTypeItem.isPossessivePronoun1() )
																isPossessivePronoun1 = true;

															if( currentWordItem.isPredefinedBasicVerb() )
																isPredefinedBasicVerb = true;
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create specific read word" );
														}
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find exact words" );
												}
											while( CommonVariables.result == Constants.RESULT_OK &&
											( currentWordItem = currentWordItem.nextWordItem() ) != null );

											if( CommonVariables.result == Constants.RESULT_OK )
												{
												if( ( wordResult = myWord_.findWordTypeInAllWords( true, true, Constants.WORD_TYPE_UNDEFINED, wordStringLength_, lowerCaseWordString_ ) ).result == Constants.RESULT_OK )
													{
													if( wordResult.foundWordItem == null )
														{
														if( getWordTypeNr( false, wordStringLength_, lowerCaseWordString_ ) == Constants.RESULT_OK )
															{
															if( wordTypeNr_ > Constants.WORD_TYPE_UNDEFINED )
																{
																if( ( wordResult = createWord( wasPreviousWordDefiniteArticle, wordTypeNr_, Constants.NO_WORD_PARAMETER, wordStringLength_, lowerCaseWordString_ ) ).result == Constants.RESULT_OK )
																	{
																	if( ( createdWordItem = wordResult.createdWordItem ) != null )
																		{
																		if( createReadWord( CommonVariables.currentGrammarLanguageNr, wordTypeNr_, currentWordPosition_, null, createdWordItem ) != Constants.RESULT_OK )
																			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a lower-case read word" );
																		}
																	else
																		myWord_.setErrorInItem( 1, moduleNameString_, "The created lower-case word is undefined" );
																	}
																else
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a lower-case word" );
																}
															else
																isUndefinedWord = true;
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the word type number of a lower-case word" );
														}
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a lower-case word" );
												}
											}
										else
											myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
										}

									if( CommonVariables.result == Constants.RESULT_OK &&
									!foundExactWord )
										{
										if( ( wordResult = myWord_.findWordTypeInAllWords( true, false, Constants.WORD_TYPE_UNDEFINED, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
											{
											if( wordResult.foundWordItem == null )
												{
												if( getWordTypeNr( true, wordStringLength_, exactWordString_ ) == Constants.RESULT_OK )
													{
													if( wordTypeNr_ > Constants.WORD_TYPE_UNDEFINED )
														{
														// Small letters, capital letters, numerals and proper-names
														if( ( wordResult = createWord( wasPreviousWordDefiniteArticle, wordTypeNr_, Constants.NO_WORD_PARAMETER, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
															{
															if( ( createdWordItem = wordResult.createdWordItem ) != null )
																{
																if( createReadWord( CommonVariables.currentGrammarLanguageNr, wordTypeNr_, currentWordPosition_, null, createdWordItem ) != Constants.RESULT_OK )
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an exact read word" );
																}
															else
																myWord_.setErrorInItem( 1, moduleNameString_, "The last created exact word is undefined" );
															}
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an exact word" );
														}
													else
														isUndefinedWord = true;
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the word type number of an exact word" );
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find an read word" );
										}

									// Create a noun
									if( CommonVariables.result == Constants.RESULT_OK &&
									!isSymbol &&
									!isLetter &&
									!isNumeral &&
									!isAdjective &&
									!isArticle &&
									!isConjunction &&
									!isPossessivePronoun1 &&
									!isPredefinedBasicVerb &&

									( isUndefinedWord ||
									wasPreviousWordArticle ||
									wasPreviousWordConjunction ||
									wasPreviousWordSymbol ) &&

									( currentWordPosition_ == 1 ||		// First word in sentence
									!Character.isUpperCase( readSentenceString.charAt( readPosition ) ) ) )
										{
										if( checkNounWordTypeOfAllLanguages( wordStringLength_, exactWordString_ ) == Constants.RESULT_OK )
											{
											if( isProbablyPluralNoun_ ||
											wasPreviousWordAdjective ||
											wasPreviousWordArticle ||
											wasPreviousWordConjunction ||
											wasPreviousWordSymbol ||
											wasPreviousWordPossessivePronoun1 )
												{
												if( isProbablyPluralNoun_ )
													{
													if( ( wordResult = myWord_.findWordTypeInAllWords( true, false, Constants.WORD_TYPE_NOUN_SINGULAR, singularWordLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
														{
														if( ( foundWordItem = wordResult.foundWordItem ) == null )
															{
															if( !foundSingularNounWord )
																{
																if( ( wordResult = createWord( wasPreviousWordDefiniteArticle, Constants.WORD_TYPE_NOUN_PLURAL, Constants.NO_WORD_PARAMETER, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
																	{
																	pluralNounWordItem = wordResult.createdWordItem;

																	if( pluralNounWordItem.createWordType( false, false, Constants.WORD_TYPE_NOUN_SINGULAR, singularWordLength_, singularNounString_ ) == Constants.RESULT_OK )
																		singularNounWordItem = pluralNounWordItem;
																	else
																		myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a singular noun word type item for plural noun word \"" + pluralNounWordItem.anyWordTypeString() + "\"" );
																	}
																else
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a singular noun word" );
																}
															}
														else	// Found singular noun
															{
															if( !foundPluralNounWord )
																{
																if( foundWordItem.createWordType( false, false, Constants.WORD_TYPE_NOUN_PLURAL, wordStringLength_, exactWordString_ ) == Constants.RESULT_OK )
																	{
																	pluralNounWordItem = foundWordItem;
																	singularNounWordItem = foundWordItem;
																	}
																else
																	myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a plural noun word type item for word \"" + foundWordItem.anyWordTypeString() + "\"" );
																}
															}
														}
													else
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find if a singular noun word already exists" );
													}
												else
													{
													if( !foundSingularNounWord )
														{
														if( ( wordResult = createWord( wasPreviousWordDefiniteArticle, Constants.WORD_TYPE_NOUN_SINGULAR, Constants.NO_WORD_PARAMETER, wordStringLength_, exactWordString_ ) ).result == Constants.RESULT_OK )
															singularNounWordItem = wordResult.createdWordItem;
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a singular noun word" );
														}
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												singularNounWordItem != null )
													{
													if( createReadWord( CommonVariables.currentGrammarLanguageNr, Constants.WORD_TYPE_NOUN_SINGULAR, currentWordPosition_, null, singularNounWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a singular noun read word" );
													}

												if( CommonVariables.result == Constants.RESULT_OK &&
												pluralNounWordItem != null )
													{
													if( createReadWord( CommonVariables.currentGrammarLanguageNr, Constants.WORD_TYPE_NOUN_PLURAL, currentWordPosition_, null, pluralNounWordItem ) != Constants.RESULT_OK )
														myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a plural noun read word" );
													}
												}

											// Create an adjective
											if( CommonVariables.result == Constants.RESULT_OK &&
											!foundExactWord &&
											!wasPreviousWordUndefined &&

											( wasPreviousWordConjunction ||
											wasPreviousWordSymbol ||

											( pluralNounWordItem == null &&
											singularNounWordItem == null ) ) )
												{
												if( ( wordResult = createWord( wasPreviousWordDefiniteArticle, Constants.WORD_TYPE_ADJECTIVE, Constants.NO_WORD_PARAMETER, wordStringLength_, lowerCaseWordString_ ) ).result == Constants.RESULT_OK )
													{
													if( ( createdWordItem = wordResult.createdWordItem ) != null )
														{
														if( createReadWord( CommonVariables.currentGrammarLanguageNr, Constants.WORD_TYPE_ADJECTIVE, currentWordPosition_, null, createdWordItem ) == Constants.RESULT_OK )
															isAdjective = true;
														else
															myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an adjective read word" );
														}
													else
														myWord_.setErrorInItem( 1, moduleNameString_, "The last created adjective word is undefined" );
													}
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an adjective word" );
												}
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check a noun word type in all languages" );
										}
									}
								else
									myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create the word strings" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						hasCreatedReadWord_ )
							{
							if( ( readResult = getWordInfo( false, readPosition, readSentenceString ) ).result == Constants.RESULT_OK )
								readPosition = readResult.nextWordPosition;
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the next word" );
							}
						}
					else
						myWord_.setSystemErrorInItem( 1, moduleNameString_, "Word position overflow! I cannot accept more words" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				hasCreatedReadWord_ &&
				readPosition < readSentenceStringLength );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given read sentence string is empty" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given read sentence string is undefined" );

		readResult.hasCreatedAllReadWords = hasCreatedReadWord_;
		readResult.result = CommonVariables.result;
		return readResult;
		}

	protected ReadResultType getWordInfo( boolean skipDoubleQuotes, int startWordPosition, String wordString )
		{
		ReadResultType readResult = new ReadResultType();
		boolean text = false;
		boolean wordStartedWithDoubleQuote = false;
		int wordStringLength;
		int wordPosition = startWordPosition;

		if( wordString != null )
			{
			wordStringLength = wordString.length();

			if( wordPosition < wordStringLength )
				{
				while( wordPosition < wordStringLength &&
				Character.isWhitespace( wordString.charAt( wordPosition ) ) )
					wordPosition++;

				if( wordPosition < wordStringLength )
					{
					readResult.startWordPosition = wordPosition;

					if( isSymbol( wordString.charAt( wordPosition ) ) )
						{
						wordPosition++;
						readResult.wordLength++;
						}
					else
						{
						if( skipDoubleQuotes &&
						wordString.charAt( wordPosition ) == Constants.SYMBOL_DOUBLE_QUOTE )
							wordStartedWithDoubleQuote = true;

						while( wordPosition < wordStringLength &&

						( text ||
						( !Character.isWhitespace( wordString.charAt( wordPosition ) ) &&
						!isSymbol( wordString.charAt( wordPosition ) ) ) ) )
							{
							if( wordString.charAt( wordPosition ) == Constants.SYMBOL_DOUBLE_QUOTE &&

							( wordPosition == 0 ||
							wordString.charAt( wordPosition - 1 ) != Constants.SYMBOL_BACK_SLASH ) )	// Skip escaped double quote character
								text = !text;

							wordPosition++;
							readResult.wordLength++;
							}

						if( wordStartedWithDoubleQuote &&
						readResult.wordLength > 1 )
							readResult.wordLength--;

						if( skipDoubleQuotes &&
						wordPosition > 1 &&
						readResult.wordLength > 1 &&
						wordString.charAt( wordPosition - 1 ) == Constants.SYMBOL_DOUBLE_QUOTE )
							{
							readResult.wordLength--;
							readResult.startWordPosition++;
							}
						}

					while( wordPosition < wordStringLength &&
					Character.isWhitespace( wordString.charAt( wordPosition ) ) )
						wordPosition++;
					}
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given start word position is invalid" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given word string is undefined" );

		readResult.result = CommonVariables.result;
		readResult.nextWordPosition = wordPosition;
		return readResult;
		}

	protected byte createReadWord( short wordTypeLanguageNr, short wordTypeNr, int wordPosition, String textString, WordItem readWordItem )
		{
		ReadResultType readResult = new ReadResultType();
		short wordParameter = ( readWordItem == null ? Constants.NO_WORD_PARAMETER : readWordItem.wordParameter() );

		hasCreatedReadWord_ = false;

		if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED )
			{
			if( textString != null ||
			readWordItem != null )
				{
				if( textString != null )
					{
					if( ( readResult = getWordInfo( true, 0, textString ) ).result == Constants.RESULT_OK )
						{
						if( readResult.startWordPosition >= textString.length() )
							return myWord_.setErrorInItem( 1, moduleNameString_, "The found start word position is invalid" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the length of the current text string" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( admin_.readList == null )
						{
						if( ( admin_.readList = new ReadList( myWord_ ) ) != null )
							admin_.adminList[Constants.ADMIN_READ_LIST] = admin_.readList;
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create a read list" );
						}
					else
						{
						if( admin_.readList.foundReadItem( wordTypeLanguageNr, wordParameter, wordTypeNr, wordPosition, textString, readWordItem ) )
							return myWord_.setErrorInItem( 1, moduleNameString_, "The given read item already exists" );
						}

					if( admin_.readList.createReadItem( wordTypeLanguageNr, wordParameter, wordTypeNr, wordPosition, readResult.wordLength, ( textString == null ? null : textString.substring( readResult.startWordPosition ) ), readWordItem ) == Constants.RESULT_OK )
						hasCreatedReadWord_ = true;
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an admin read words item" );
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "Both the given text string and the given read word item are undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given word type number is undefined" );

		return CommonVariables.result;
		}

	protected WordResultType createWord( boolean wasPreviousWordDefiniteArticle, short wordTypeNr, short wordParameter, int wordTypeStringLength, String wordTypeString )
		{
		WordResultType wordResult = new WordResultType();
		WordItem createdWordItem;

		if( wordTypeString != null )
			{
			if( wordTypeStringLength > 0 &&
			wordTypeString.length() > 0 )
				{
				if( wordTypeNr > Constants.WORD_TYPE_UNDEFINED )
					{
					if( admin_.wordList == null )
						{
						if( ( admin_.wordList = new WordList( myWord_ ) ) != null )
							admin_.adminList[Constants.ADMIN_WORD_LIST] = admin_.wordList;
						else
							myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create a word list" );
						}

					if( ( wordResult = myWord_.findWordTypeInAllWords( true, false, wordTypeNr, wordTypeStringLength, wordTypeString ) ).result == Constants.RESULT_OK )
						{
						if( wordParameter == Constants.NO_WORD_PARAMETER ||
						wordResult.foundWordItem == null ||
						wordResult.foundWordItem.wordParameter() != wordParameter )
							{
							if( ( wordResult = admin_.wordList.createWordItem( wordParameter ) ).result == Constants.RESULT_OK )
								{
								if( ( createdWordItem = wordResult.createdWordItem ) != null )
									{
									if( CommonVariables.firstWordItem == null )
										CommonVariables.firstWordItem = createdWordItem;		// Initialize the first word

									if( createdWordItem.createWordType( ( wordParameter == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_NEUTER || wordParameter == Constants.WORD_PARAMETER_ARTICLE_DEFINITE_MALE_FEMALE ), ( wasPreviousWordDefiniteArticle && wordTypeNr == Constants.WORD_TYPE_PROPER_NAME ), wordTypeNr, wordTypeStringLength, wordTypeString ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a word type for the new word" );
									}
								else
									myWord_.setErrorInItem( 1, moduleNameString_, "The last created word item is undefined" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a word item" );
							}
						else
							myWord_.setErrorInItem( 1, moduleNameString_, "The given word type string already exists with the same word parameter" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a word type in all words" );
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given word type number is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given word type string is empty or has no words left anymore" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given word type string is undefined" );

		wordResult.result = CommonVariables.result;
		return wordResult;
		}
	};

/*************************************************************************
 *
 *	"How joyful are those who fear the Lord-
 *	all who follow his ways!" (Psalm 128:1)
 *
 *************************************************************************/
