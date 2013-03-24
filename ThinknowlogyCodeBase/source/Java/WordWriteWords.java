/*
 *	Class:			WordWriteWords
 *	Supports class:	WordItem
 *	Purpose:		To write the words of the sentences
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

class WordWriteWords
	{
	// Private constructible variables

	private boolean foundAllSpecificationWords_;
	private boolean foundAllSpecificationWordsBeforeConjunction_;
	private boolean foundGeneralizationWord_;
	private boolean foundSpecificationWord_;
	private boolean foundQuestionVerb_;
	private boolean foundSingleSpecificationWord_;
	private boolean foundSpecificationGeneralizationVerb_;

	private boolean isUnknownPluralOfNoun_;
	private boolean skipClearWriteLevel_;
	private boolean relationNeedsToWaitForConjunction_;
	private boolean specificationNeedsToWaitForConjunction_;

	private int generalizationStartWordPosition_;
	private int specificationStartWordPosition_;

	private SpecificationItem lastFoundSpecificationItem_;

	private WordItem lastFoundRelationWordItem_;

	private String writeWordString_;

	private StringBuffer lastSpecificationStringBuffer_;
	private StringBuffer previousSpecificationStringBuffer_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean isSpecificationReasoningWordType( short specificationWordTypeNr )
		{
		return ( specificationWordTypeNr == Constants.WORD_TYPE_SYMBOL ||
				specificationWordTypeNr == Constants.WORD_TYPE_NUMERAL ||
				specificationWordTypeNr == Constants.WORD_TYPE_LETTER_SMALL ||
				specificationWordTypeNr == Constants.WORD_TYPE_LETTER_CAPITAL ||
				specificationWordTypeNr == Constants.WORD_TYPE_NOUN_SINGULAR ||
				specificationWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL );
		}

	private byte writeGeneralizationWordToSentence( boolean isPluralSpecificationGeneralization, short grammarWordTypeNr )
		{
		WordResultType wordResult = new WordResultType();
		short generalizationWordTypeNr = grammarWordTypeNr;

		foundGeneralizationWord_ = false;
		isUnknownPluralOfNoun_ = false;

		if( ( writeWordString_ = myWord_.activeWordTypeString( isPluralSpecificationGeneralization ? Constants.WORD_TYPE_NOUN_PLURAL : grammarWordTypeNr ) ) == null )
			{
			if( isPluralSpecificationGeneralization &&
			grammarWordTypeNr == Constants.WORD_TYPE_NOUN_PLURAL &&
			( writeWordString_ = myWord_.activeSingularNounString() ) != null )
				{
				isUnknownPluralOfNoun_ = true;
				generalizationWordTypeNr = Constants.WORD_TYPE_NOUN_SINGULAR;
				}
			}

		if( writeWordString_ != null )
			{
			if( ( wordResult = myWord_.checkWordTypeForBeenWritten( generalizationWordTypeNr ) ).result == Constants.RESULT_OK )
				{
				if( wordResult.isWordAlreadyWritten )
					writeWordString_ = null;
				else
					{
					if( myWord_.markWordTypeAsWritten( generalizationWordTypeNr ) == Constants.RESULT_OK )
						foundGeneralizationWord_ = true;
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark myself as written as generalization word" );
					}
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check if I am already used as generalization word" );
			}

		return CommonVariables.result;
		}

	private byte writeSpecificationWordToSentence( boolean isSpecificationGeneralization, boolean isWordTypeNumeral, boolean isWordTypePluralNoun, boolean isWordTypeSingularNoun, boolean writeGivenSpecificationWordOnly, short grammarWordTypeNr, SpecificationItem writeSpecificationItem )
		{
		WordResultType wordResult = new WordResultType();
		boolean isAnsweredQuestion;
		boolean isExclusive;
		boolean isPossessive;
		boolean isNumberOfRelations = false;
		boolean foundUnwrittenWordType = false;
		short specificationWordTypeNr;
		short writeWordTypeNr = grammarWordTypeNr;
		int nContextRelations;
		int generalizationCollectionNr;
		int specificationCollectionNr;
		int generalizationContextNr;
		int specificationContextNr;
		int relationContextNr;
		SpecificationItem currentSpecificationItem;
		WordItem currentSpecificationWordItem;
		WordItem lastFoundSpecificationWordItem;

		foundSpecificationWord_ = false;
		foundSingleSpecificationWord_ = false;
		writeWordString_ = null;

		if( writeSpecificationItem != null )
			{
			isAnsweredQuestion = writeSpecificationItem.isAnsweredQuestion();

			if( ( currentSpecificationItem = myWord_.firstSelectedSpecification( isAnsweredQuestion, writeSpecificationItem.isAssignment(), writeSpecificationItem.isDeactiveItem(), writeSpecificationItem.isArchiveItem(), writeSpecificationItem.questionParameter() ) ) != null )
				{
				isExclusive = writeSpecificationItem.isExclusive();
				isPossessive = writeSpecificationItem.isPossessive();
				specificationWordTypeNr = writeSpecificationItem.specificationWordTypeNr();
				generalizationCollectionNr = writeSpecificationItem.generalizationCollectionNr();
				specificationCollectionNr = writeSpecificationItem.specificationCollectionNr();
				generalizationContextNr = writeSpecificationItem.generalizationContextNr();
				specificationContextNr = writeSpecificationItem.specificationContextNr();
				relationContextNr = writeSpecificationItem.relationContextNr();

				do	{
					if( currentSpecificationItem == writeSpecificationItem ||

					( !writeGivenSpecificationWordOnly &&
					currentSpecificationItem.isRelatedSpecification( isExclusive, isPossessive, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr ) ) )
						{
						if( currentSpecificationItem != writeSpecificationItem )
							foundUnwrittenWordType = true;

						if( ( currentSpecificationWordItem = currentSpecificationItem.specificationWordItem() ) == null )		// Specification string
							{
							if( !currentSpecificationItem.isSpecificationStringAlreadyWritten() )
								{
								if( currentSpecificationItem.markSpecificationStringAsWritten() == Constants.RESULT_OK )
									{
									foundSpecificationWord_ = true;
									lastFoundSpecificationItem_ = currentSpecificationItem;
									writeWordString_ = currentSpecificationItem.specificationString();

									if( foundAllSpecificationWordsBeforeConjunction_ )
										{
										skipClearWriteLevel_ = false;
										foundAllSpecificationWords_ = true;
										foundAllSpecificationWordsBeforeConjunction_ = false;
										}
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark specification string \"" + currentSpecificationItem.specificationString() + "\" as written" );
								}
							}
						else
							{
							if( ( nContextRelations = currentSpecificationItem.nContextRelations() ) == 0 )
								{
								if( isPossessive &&
								currentSpecificationItem.hasRelationContext() &&
								isSpecificationReasoningWordType( grammarWordTypeNr ) &&
								specificationStartWordPosition_ == 0 )		// To avoid looping in numbers
									nContextRelations = myWord_.nContextWords( isPossessive, currentSpecificationItem.relationContextNr(), currentSpecificationWordItem );
								}

							if( ( nContextRelations == 0 &&
							specificationWordTypeNr == grammarWordTypeNr ) ||

							( nContextRelations == 1 &&
							isWordTypeSingularNoun ) ||

							( nContextRelations > 1 &&

							( isWordTypeNumeral ||
							isWordTypePluralNoun ) ) )
								{
								if( ( writeWordString_ = currentSpecificationWordItem.activeWordTypeString( grammarWordTypeNr ) ) == null )
									{
									if( isWordTypeNumeral )
										{
										if( specificationStartWordPosition_ == 0 )		// To avoid looping in numbers
											{
											// The word 'number' needs to be converted to the number of relations
											isNumberOfRelations = true;
											writeWordString_ = new String( Constants.EMPTY_STRING + nContextRelations );
											}
										}
									else
										{
										if( isWordTypePluralNoun &&
										( writeWordString_ = currentSpecificationWordItem.activeSingularNounString() ) != null )
											// The plural noun is unknown, but the singular is known. So, force a singular noun, but with a remark
											isUnknownPluralOfNoun_ = true;		// Force as singular noun
										else	// Must be hidden word type
											writeWordString_ = currentSpecificationWordItem.anyWordTypeString();
										}
									}

								if( !isNumberOfRelations &&
								writeWordString_ != null )
									{
									if( isUnknownPluralOfNoun_ )
										writeWordTypeNr = Constants.WORD_TYPE_NOUN_SINGULAR;

									if( ( wordResult = currentSpecificationWordItem.checkWordTypeForBeenWritten( writeWordTypeNr ) ).result == Constants.RESULT_OK )
										{
										if( wordResult.isWordAlreadyWritten )
											writeWordString_ = null;
										else
											{
											if( currentSpecificationWordItem.markWordTypeAsWritten( writeWordTypeNr ) == Constants.RESULT_OK )
												{
												foundSpecificationWord_ = true;
												lastFoundSpecificationItem_ = currentSpecificationItem;

												if( foundAllSpecificationWordsBeforeConjunction_ )
													{
													skipClearWriteLevel_ = false;
													foundAllSpecificationWords_ = true;
													}
												}
											else
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" as written" );
											}
										}
									else
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check specification word \"" + currentSpecificationWordItem.anyWordTypeString() + "\" for being written" );
									}
								}
							}
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				writeWordString_ == null &&
				( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemWithSameQuestionParameter( isAnsweredQuestion ) ) != null );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			!foundSpecificationWord_ &&
			!foundAllSpecificationWords_ &&
			!foundAllSpecificationWordsBeforeConjunction_ &&
			lastFoundSpecificationItem_ != null &&
			lastFoundSpecificationItem_.specificationWordTypeNr() == grammarWordTypeNr )
				{
				if( foundUnwrittenWordType )
					{
					if( previousSpecificationStringBuffer_ != null &&
					previousSpecificationStringBuffer_.length() > 0 )
						{
						foundAllSpecificationWordsBeforeConjunction_ = true;
						specificationNeedsToWaitForConjunction_ = true;

						if( !isSpecificationGeneralization )
							skipClearWriteLevel_ = true;

						CommonVariables.writeSentenceStringBuffer = new StringBuffer( previousSpecificationStringBuffer_ );

						if( ( lastFoundSpecificationWordItem = lastFoundSpecificationItem_.specificationWordItem() ) == null )		// Specification string
							lastFoundSpecificationItem_.clearSpecificationStringWriteLevel( Constants.NO_WRITE_LEVEL );
						else
							lastFoundSpecificationWordItem.clearWriteLevel( Constants.NO_WRITE_LEVEL );
						}
					}
				else
					{
					if( isSpecificationGeneralization )
						{
						foundAllSpecificationWords_ = true;
						foundSingleSpecificationWord_ = true;
						}
					}
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );

		return CommonVariables.result;
		}

	private byte writeRelationContextWordToSentence( short relationWordTypeNr, SpecificationItem writeSpecificationItem )
		{
		WordResultType wordResult = new WordResultType();
		WordItem currentRelationWordItem = null;		// Start to search first word in method relationContextWordInAllWords

		writeWordString_ = null;

		if( writeSpecificationItem != null )
			{
			do	{
				if( ( currentRelationWordItem = writeSpecificationItem.relationWordItem( currentRelationWordItem ) ) != null )
					{
					if( ( writeWordString_ = currentRelationWordItem.activeWordTypeString( relationWordTypeNr ) ) != null )
						{
						if( ( wordResult = currentRelationWordItem.checkWordTypeForBeenWritten( relationWordTypeNr ) ).result == Constants.RESULT_OK )
							{
							if( wordResult.isWordAlreadyWritten )
								writeWordString_ = null;
							else
								{
								if( currentRelationWordItem.markWordTypeAsWritten( relationWordTypeNr ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to mark relation context word \"" + currentRelationWordItem.anyWordTypeString() + "\" as written" );
								}
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to check relation context word \"" + currentRelationWordItem.anyWordTypeString() + "\" for being written" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "Relation context word \"" + currentRelationWordItem.anyWordTypeString() + "\" doesn't have the requested word type" );
					}
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			writeWordString_ == null &&
			currentRelationWordItem != null );

			if( CommonVariables.result == Constants.RESULT_OK )
				{
				if( currentRelationWordItem != null &&
				lastFoundRelationWordItem_ != null &&
				lastFoundRelationWordItem_ != currentRelationWordItem &&
				writeSpecificationItem.relationWordItem( currentRelationWordItem ) == null )	// Look ahead: This is the last relation word of the relation context
					{
					relationNeedsToWaitForConjunction_ = true;
					writeWordString_ = null;
					currentRelationWordItem.clearWriteLevel( Constants.NO_WRITE_LEVEL );
					}

				lastFoundRelationWordItem_ = currentRelationWordItem;
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected WordWriteWords( WordItem myWord )
		{
		String errorString = null;

		foundAllSpecificationWords_ = false;
		foundAllSpecificationWordsBeforeConjunction_ = false;
		foundGeneralizationWord_ = false;
		foundSpecificationWord_ = false;
		foundQuestionVerb_ = false;
		foundSingleSpecificationWord_ = false;
		foundSpecificationGeneralizationVerb_ = false;

		isUnknownPluralOfNoun_ = false;
		skipClearWriteLevel_ = false;
		relationNeedsToWaitForConjunction_ = false;
		specificationNeedsToWaitForConjunction_ = false;

		generalizationStartWordPosition_ = 0;
		specificationStartWordPosition_ = 0;

		lastFoundSpecificationItem_ = null;
		lastFoundRelationWordItem_ = null;
		writeWordString_ = null;

		lastSpecificationStringBuffer_ = null;
		previousSpecificationStringBuffer_ = null;

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
				Console.addError( "\nClass:" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nMethod:\t" + errorString + "\nError:\t\t%s.\n" );
				}
			}
		}


	// Protected methods

	protected void initializeWordWriteWordsVariables()
		{
		foundAllSpecificationWords_ = false;
		foundAllSpecificationWordsBeforeConjunction_ = false;
		foundGeneralizationWord_ = false;
		foundSpecificationWord_ = false;
		foundQuestionVerb_ = false;
		foundSingleSpecificationWord_ = false;
		foundSpecificationGeneralizationVerb_ = false;

		skipClearWriteLevel_ = false;
		relationNeedsToWaitForConjunction_ = false;
		specificationNeedsToWaitForConjunction_ = false;

		generalizationStartWordPosition_ = 0;
		specificationStartWordPosition_ = 0;

		lastFoundSpecificationItem_ = null;
		lastFoundRelationWordItem_ = null;
		writeWordString_ = null;

		lastSpecificationStringBuffer_ = null;
		previousSpecificationStringBuffer_ = null;
		}

	protected void initializeWordWriteWordsSpecificationVariables( int startWordPosition )
		{
		if( generalizationStartWordPosition_ > startWordPosition )
			generalizationStartWordPosition_ = 0;

		if( specificationStartWordPosition_ > startWordPosition )
			{
			foundAllSpecificationWordsBeforeConjunction_ = false;
			foundSpecificationGeneralizationVerb_ = false;
			specificationNeedsToWaitForConjunction_ = false;

			specificationStartWordPosition_ = 0;
			}
		}

	protected WriteResultType writeWordsToSentence( boolean writeGivenSpecificationWordOnly, short answerParameter, GrammarItem definitionGrammarItem, SpecificationItem writeSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		WriteResultType writeResult = new WriteResultType();
		boolean isAssignment;
		boolean isActiveAssignment;
		boolean isDeactiveAssignment;
		boolean isArchiveAssignment;
		boolean isConditional;
		boolean isExclusive;
		boolean isNegative;
		boolean isPossessive;
		boolean isQuestion;
		boolean isGeneralization;
		boolean isSpecification;
		boolean isRelation;
		boolean isDefiniteAssignment;
		boolean isDefiniteArticleFollowedByPropername;
		boolean isSpecificationGeneralization;
		boolean isWordTypeNumeral;
		boolean isWordTypeSingularNoun;
		boolean isWordTypePluralNoun;
		boolean insertSeparator = true;
		short definitionGrammarParameter;
		short definitionGrammarWordTypeNr;
		int pronounContextNr = Constants.NO_CONTEXT_NR;
		WordItem predefinedWordItem;
		WordItem pronounWordItem;
		WordItem specificationWordItem;
		String specificationString;
		String predefinedWordString = null;

		foundGeneralizationWord_ = false;
		foundSpecificationWord_ = false;
//		foundRelationWord_ = false;
		isUnknownPluralOfNoun_ = false;

		writeWordString_ = null;

		if( definitionGrammarItem != null )
			{
			if( definitionGrammarItem.isGrammarWord() )
				{
				if( writeSpecificationItem != null )
					{
					isAssignment = writeSpecificationItem.isAssignment();
					isActiveAssignment = writeSpecificationItem.isActiveAssignment();
					isDeactiveAssignment = writeSpecificationItem.isDeactiveAssignment();
					isArchiveAssignment = writeSpecificationItem.isArchiveAssignment();

					isConditional = writeSpecificationItem.isConditional();
					isExclusive = writeSpecificationItem.isExclusive();
					isNegative = writeSpecificationItem.isNegative();
					isPossessive = writeSpecificationItem.isPossessive();
					isQuestion = writeSpecificationItem.isQuestion();
					isSpecificationGeneralization = writeSpecificationItem.isSpecificationGeneralization();

					isGeneralization = ( generalizationStartWordPosition_ == 0 &&

										( !isSpecificationGeneralization ||

										( foundAllSpecificationWords_ &&
										foundSpecificationGeneralizationVerb_ ) ) );

					isRelation = ( !isGeneralization &&
									specificationStartWordPosition_ > 0 &&
									writeSpecificationItem.hasRelationContext() );

					isSpecification = ( !isGeneralization &&
										!isRelation &&

										( !foundAllSpecificationWords_ ||

										( isSpecificationGeneralization &&
										!foundSpecificationGeneralizationVerb_ ) ) );

					definitionGrammarParameter = definitionGrammarItem.grammarParameter();
					definitionGrammarWordTypeNr = definitionGrammarItem.grammarWordTypeNr();

					if( definitionGrammarParameter > Constants.NO_GRAMMAR_PARAMETER &&
					( predefinedWordItem = myWord_.predefinedWordItem( definitionGrammarParameter ) ) != null )
						predefinedWordString = predefinedWordItem.wordTypeString( definitionGrammarWordTypeNr );

					specificationWordItem = writeSpecificationItem.specificationWordItem();
					specificationString = writeSpecificationItem.specificationString();

					switch( definitionGrammarParameter )
						{
						case Constants.WORD_PARAMETER_SYMBOL_COMMA:
						case Constants.WORD_PARAMETER_SYMBOL_COLON:
						case Constants.WORD_PARAMETER_SYMBOL_EXCLAMATION_MARK:
						case Constants.WORD_PARAMETER_SYMBOL_QUESTION_MARK:
							insertSeparator = false;
							writeWordString_ = predefinedWordString;
							break;

						case Constants.WORD_PARAMETER_NUMERAL_BOTH:
							if( writeSpecificationItem.hasExclusiveRelationCollection() &&
							myWord_.nContextWords( isPossessive, writeSpecificationItem.relationContextNr(), specificationWordItem ) == 2 )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_ADJECTIVE_CALLED_OR_NAMED:
							writeWordString_ = predefinedWordString;
							break;

						case Constants.WORD_PARAMETER_ADJECTIVE_NEW:
							if( isActiveAssignment &&
							writeSpecificationItem.hasCurrentActiveSentenceNr() )		// Only show the word "new" during the current sentence
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_ADJECTIVE_PREVIOUS:
							if( isDeactiveAssignment )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_ADJECTIVE_CURRENT:
							if( isActiveAssignment &&
							writeSpecificationItem.hasExclusiveGeneralizationCollection() )
								writeWordString_ = predefinedWordString;

							break;

//						case Constants.WORD_PARAMETER_ADJECTIVE_NEXT:

						case Constants.WORD_PARAMETER_ADVERB_AS:
						case Constants.WORD_PARAMETER_ADVERB_INFO:

//						case Constants.WORD_PARAMETER_ADVERB_PREVIOUSLY:
//						case Constants.WORD_PARAMETER_ADVERB_CURRENTLY:
							break;	// Skip these words

						case Constants.WORD_PARAMETER_ADJECTIVE_NO:
//						case Constants.WORD_PARAMETER_ADVERB_DO_NOT:
						case Constants.WORD_PARAMETER_ADVERB_NOT:
							if( isNegative )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_ADVERB_ASSUMPTION_MAY_BE:
							if( ( specificationResult = writeSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
								{
								if( specificationResult.assumptionLevel > 2 )
									writeWordString_ = predefinedWordString;
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to recalculate the assumption level" );

							break;

						case Constants.WORD_PARAMETER_ADVERB_ASSUMPTION_POSSIBLY:
							if( ( specificationResult = writeSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
								{
								if( specificationResult.assumptionLevel == 2 )
									writeWordString_ = predefinedWordString;
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to recalculate the assumption level" );

							break;

						case Constants.WORD_PARAMETER_ADVERB_ASSUMPTION_PROBABLY:
							if( ( specificationResult = writeSpecificationItem.getAssumptionLevel() ).result == Constants.RESULT_OK )
								{
								if( specificationResult.assumptionLevel == 1 )
									writeWordString_ = predefinedWordString;
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to recalculate the assumption level" );

							break;

						case Constants.WORD_PARAMETER_ARTICLE_INDEFINITE:
							if( !isAssignment ||

							( isArchiveAssignment &&
							writeSpecificationItem.hasRelationContext() ) )
								writeWordString_ = predefinedWordString;

							// Don't insert a break statement here

						case Constants.WORD_PARAMETER_ARTICLE_DEFINITE_NEUTER:
						case Constants.WORD_PARAMETER_ARTICLE_DEFINITE_MALE_FEMALE:
							isDefiniteAssignment = ( isAssignment &&
													!isArchiveAssignment &&

													( ( isGeneralization &&
													!writeSpecificationItem.isGeneralizationPropername() ) ||

													( isSpecification &&
													!writeSpecificationItem.isSpecificationPropername() ) ||

													( isRelation &&
													!writeSpecificationItem.isRelationPropername() ) ) );

							isDefiniteArticleFollowedByPropername = ( ( isGeneralization &&
																	writeSpecificationItem.isGeneralizationPropernamePrecededByDefiniteArticle() ) ||

																	( isSpecification &&
																	writeSpecificationItem.isSpecificationPropernamePrecededByDefiniteArticle() ) ||

																	( isRelation &&
																	writeSpecificationItem.isRelationPropernamePrecededByDefiniteArticle() ) );

							if( isDefiniteAssignment ||
							isDefiniteArticleFollowedByPropername )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_CONJUNCTION_AND:
							if( !isExclusive ||
							isDeactiveAssignment ||
							writeSpecificationItem.hasExclusiveRelationCollection() )
								{
								relationNeedsToWaitForConjunction_ = false;
								specificationNeedsToWaitForConjunction_ = false;
								writeWordString_ = predefinedWordString;
								}

							break;

						case Constants.WORD_PARAMETER_CONJUNCTION_OR:
							if( isExclusive &&
							!isDeactiveAssignment )
								{
								relationNeedsToWaitForConjunction_ = false;
								specificationNeedsToWaitForConjunction_ = false;
								writeWordString_ = predefinedWordString;
								}

							break;

						case Constants.WORD_PARAMETER_ANSWER_YES:
						case Constants.WORD_PARAMETER_ANSWER_NO:
							if( definitionGrammarParameter == answerParameter )
								writeWordString_ = predefinedWordString;

							break;

						// Singular pronouns
						case Constants.WORD_PARAMETER_SINGULAR_PRONOUN_I_ME_MY_MINE:
						case Constants.WORD_PARAMETER_SINGULAR_PRONOUN_YOU_YOU_YOUR_YOURS:
						case Constants.WORD_PARAMETER_SINGULAR_PRONOUN_HE_HIM_HIS_HIS:
						case Constants.WORD_PARAMETER_SINGULAR_PRONOUN_SHE_HER_HER_HERS:
						case Constants.WORD_PARAMETER_SINGULAR_PRONOUN_IT_ITS_ITS_ITS:

						// Plural pronouns
						case Constants.WORD_PARAMETER_PLURAL_PRONOUN_WE_US_OUR_OURS:
						case Constants.WORD_PARAMETER_PLURAL_PRONOUN_YOU_YOU_YOUR_YOURS:
						case Constants.WORD_PARAMETER_PLURAL_PRONOUN_THEY_THEM_THEIR_THEIRS:
							if( !isArchiveAssignment )
								{
								if( isGeneralization )
									pronounContextNr = writeSpecificationItem.generalizationContextNr();
								else
									{
									if( isSpecification )
										pronounContextNr = writeSpecificationItem.specificationContextNr();
									else
										{
										if( isRelation )
											pronounContextNr = writeSpecificationItem.relationContextNr();
										}
									}

								if( pronounContextNr > Constants.NO_CONTEXT_NR )
									{
									if( ( pronounWordItem = myWord_.predefinedWordItem( definitionGrammarParameter ) ) != null )
										{
										if( pronounWordItem.contextWordTypeNrInWord( pronounContextNr ) == definitionGrammarWordTypeNr )
											writeWordString_ = predefinedWordString;
										}
									else
										myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't find the pronoun word with the definition grammar parameter" );
									}
								}

							break;

						case Constants.WORD_PARAMETER_PREPOSITION_ABOUT:
						case Constants.WORD_PARAMETER_PREPOSITION_FOR:
						case Constants.WORD_PARAMETER_PREPOSITION_FROM:
						case Constants.WORD_PARAMETER_PREPOSITION_TO:
							break;	// Skip these words

						case Constants.WORD_PARAMETER_PREPOSITION_IN:
							if( writeSpecificationItem.isPrepositionIn() )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_PREPOSITION_OF:
							if( writeSpecificationItem.isPrepositionOf() ||
							writeSpecificationItem.prepositionParameter() == Constants.NO_PREPOSITION_PARAMETER )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_IS:
						case Constants.WORD_PARAMETER_PLURAL_VERB_ARE:
							if( !isConditional &&
							!isPossessive &&

							( !isAssignment ||		// Definition specification
							isActiveAssignment ) &&

							( isQuestion ||
							foundAllSpecificationWords_ ||
							!isSpecificationGeneralization ) )
								{
								writeWordString_ = predefinedWordString;

								if( isQuestion )
									foundQuestionVerb_ = true;

								if( isSpecificationGeneralization )
									foundSpecificationGeneralizationVerb_ = true;
								}

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_CAN_BE:
						case Constants.WORD_PARAMETER_PLURAL_VERB_CAN_BE:
							if( isConditional &&
							!isPossessive &&

							( !isAssignment ||
							isActiveAssignment ) )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_WAS:
						case Constants.WORD_PARAMETER_PLURAL_VERB_WERE:
							if( !isConditional &&
							!isPossessive &&

							( isDeactiveAssignment ||
							isArchiveAssignment ) &&

							( foundAllSpecificationWords_ ||
							!isSpecificationGeneralization ) )
								{
								if( isQuestion )
									foundQuestionVerb_ = true;

								if( isSpecificationGeneralization )
									foundSpecificationGeneralizationVerb_ = true;

								if( isArchiveAssignment ||
								!writeSpecificationItem.hasGeneralizationCollection() )
									writeWordString_ = predefinedWordString;
								else
									{
									if( myWord_.firstAssignment( false, true, true, isNegative, isPossessive, writeSpecificationItem.questionParameter(), writeSpecificationItem.generalizationContextNr(), writeSpecificationItem.specificationContextNr(), writeSpecificationItem.relationContextNr(), specificationWordItem, specificationString ) == null )
										writeWordString_ = predefinedWordString;
									else
										{
										// Force current tense, because the word 'previous' is used
										if( ( predefinedWordItem = myWord_.predefinedWordItem( definitionGrammarParameter == Constants.WORD_PARAMETER_SINGULAR_VERB_WAS ? Constants.WORD_PARAMETER_SINGULAR_VERB_IS : Constants.WORD_PARAMETER_PLURAL_VERB_ARE ) ) != null )
											{
											if( ( writeWordString_ = predefinedWordItem.wordTypeString( definitionGrammarWordTypeNr ) ) == null )
												myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't get the word type string from the predefined word with word parameter " + ( definitionGrammarParameter == Constants.WORD_PARAMETER_SINGULAR_VERB_WAS ? Constants.WORD_PARAMETER_SINGULAR_VERB_IS : Constants.WORD_PARAMETER_PLURAL_VERB_ARE ) );
											}
										else
											myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't find the predefined word with word parameter " + ( definitionGrammarParameter == Constants.WORD_PARAMETER_SINGULAR_VERB_WAS ? Constants.WORD_PARAMETER_SINGULAR_VERB_IS : Constants.WORD_PARAMETER_PLURAL_VERB_ARE ) );
										}
									}
								}

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_HAS:
						case Constants.WORD_PARAMETER_PLURAL_VERB_HAVE:
							if( !isConditional &&
							isPossessive &&

							( !isAssignment ||
							isActiveAssignment ||
							isDeactiveAssignment ) &&

							( foundAllSpecificationWords_ ||
							!isSpecificationGeneralization ) )
								{
								writeWordString_ = predefinedWordString;

								if( isQuestion )
									foundQuestionVerb_ = true;

								if( isSpecificationGeneralization )
									foundSpecificationGeneralizationVerb_ = true;
								}

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_CAN_HAVE:
						case Constants.WORD_PARAMETER_PLURAL_VERB_CAN_HAVE:
							if( isConditional &&
							isPossessive &&

							( !isAssignment ||
							isActiveAssignment ) )
								writeWordString_ = predefinedWordString;

							break;

						case Constants.WORD_PARAMETER_SINGULAR_VERB_HAD:
						case Constants.WORD_PARAMETER_PLURAL_VERB_HAD:
							if( !isConditional &&
							isPossessive &&

							( isDeactiveAssignment ||
							isArchiveAssignment ) &&

							( foundAllSpecificationWords_ ||
							!isSpecificationGeneralization ) )
								{
								writeWordString_ = predefinedWordString;

								if( isQuestion )
									foundQuestionVerb_ = true;

								if( isSpecificationGeneralization )
									foundSpecificationGeneralizationVerb_ = true;
								}

							break;

						// Verbs
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_ADD:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_MOVE:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_REMOVE:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_CLEAR:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_HELP:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_LOGIN:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_READ:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_REDO:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_RESTART:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_SHOW:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_SOLVE:
						case Constants.WORD_PARAMETER_SINGULAR_VERB_IMPERATIVE_UNDO:

						// Selection words
						case Constants.WORD_PARAMETER_SELECTION_IF:
						case Constants.WORD_PARAMETER_SELECTION_THEN:
						case Constants.WORD_PARAMETER_SELECTION_ELSE:
							break;	// Skip these words

						// Noun words
						case Constants.WORD_PARAMETER_NOUN_VALUE:
							break;	// Skip this words

						case Constants.NO_GRAMMAR_PARAMETER:

						// Adjectives
						case Constants.WORD_PARAMETER_ADJECTIVE_ASSIGNED:
						case Constants.WORD_PARAMETER_ADJECTIVE_BUSY:
						case Constants.WORD_PARAMETER_ADJECTIVE_CLEAR:
						case Constants.WORD_PARAMETER_ADJECTIVE_DONE:
						case Constants.WORD_PARAMETER_ADJECTIVE_DEFENSIVE:
						case Constants.WORD_PARAMETER_ADJECTIVE_EXCLUSIVE:
						case Constants.WORD_PARAMETER_ADJECTIVE_INVERTED:

						// Nouns
						case Constants.WORD_PARAMETER_NOUN_DEVELOPER:
						case Constants.WORD_PARAMETER_NOUN_FILE:
						case Constants.WORD_PARAMETER_NOUN_GRAMMAR_LANGUAGE:
						case Constants.WORD_PARAMETER_NOUN_INTERFACE_LANGUAGE:
						case Constants.WORD_PARAMETER_NOUN_JUSTIFICATION_REPORT:
						case Constants.WORD_PARAMETER_NOUN_HEAD:
						case Constants.WORD_PARAMETER_NOUN_TAIL:
						case Constants.WORD_PARAMETER_NOUN_MIND:
						case Constants.WORD_PARAMETER_NOUN_NUMBER:
						case Constants.WORD_PARAMETER_NOUN_PASSWORD:
						case Constants.WORD_PARAMETER_NOUN_SOLVE_LEVEL:
						case Constants.WORD_PARAMETER_NOUN_SOLVE_METHOD:
						case Constants.WORD_PARAMETER_NOUN_SOLVE_STRATEGY:
						case Constants.WORD_PARAMETER_NOUN_TEST_FILE:
						case Constants.WORD_PARAMETER_NOUN_USER:
							if( ( !isQuestion ||
							foundQuestionVerb_ ) &&

							( !relationNeedsToWaitForConjunction_ &&
							!specificationNeedsToWaitForConjunction_ ) )
								{
								if( isGeneralization )
									{
									if( writeSpecificationItem.generalizationWordTypeNr() == definitionGrammarWordTypeNr )		// Matching word type
										{
										// Generalization
										if( writeGeneralizationWordToSentence( ( isSpecificationGeneralization && !foundSingleSpecificationWord_ ), definitionGrammarWordTypeNr ) != Constants.RESULT_OK )
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the generalization word to the sentence" );
										}
									}
								else
									{
									if( isSpecification )
										{
										isWordTypeNumeral = definitionGrammarItem.isWordTypeNumeral();
										isWordTypePluralNoun = definitionGrammarItem.isWordTypePluralNoun();
										isWordTypeSingularNoun = definitionGrammarItem.isWordTypeSingularNoun();

										if( isWordTypeNumeral ||
										isWordTypePluralNoun ||
										writeSpecificationItem.specificationWordTypeNr() == definitionGrammarWordTypeNr )		// Matching word type
											{
											// Specification
											if( writeSpecificationWordToSentence( isSpecificationGeneralization, isWordTypeNumeral, isWordTypePluralNoun, isWordTypeSingularNoun, writeGivenSpecificationWordOnly, definitionGrammarWordTypeNr, writeSpecificationItem ) != Constants.RESULT_OK )
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a specification word to the sentence" );
											}
										}
									else
										{
										if( isRelation &&
										writeSpecificationItem.relationWordTypeNr() == definitionGrammarWordTypeNr )			// Matching word type
											{
											// Relation context
											if( writeRelationContextWordToSentence( definitionGrammarWordTypeNr, writeSpecificationItem ) != Constants.RESULT_OK )
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a relation context word to the sentence" );
											}
										}
									}
								}

							break;

						default:
							myWord_.setErrorInWord( 1, moduleNameString_, "I don't know how to handle the grammar definition with grammar parameter " + definitionGrammarParameter );
						}

					if( CommonVariables.result == Constants.RESULT_OK &&
					writeWordString_ != null )
						{
						if( CommonVariables.writeSentenceStringBuffer == null )
							CommonVariables.writeSentenceStringBuffer = new StringBuffer( writeWordString_ );
						else
							{
							skipClearWriteLevel_ = false;

							if( insertSeparator )
								CommonVariables.writeSentenceStringBuffer.append( Constants.SPACE_STRING );

							CommonVariables.writeSentenceStringBuffer.append( writeWordString_ );
							}

						if( isUnknownPluralOfNoun_ )
							{
							if( CommonVariables.currentInterfaceLanguageWordItem != null )
								CommonVariables.writeSentenceStringBuffer.append( CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_GRAMMAR_UNKNOWN_PLURAL_START ) + writeWordString_ + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( Constants.INTERFACE_GRAMMAR_UNKNOWN_PLURAL_ENDING ) );
							else
								myWord_.setErrorInWord( 1, moduleNameString_, "The current interface language word item is undefined" );
							}

						if( foundGeneralizationWord_ )
							generalizationStartWordPosition_ = CommonVariables.writeSentenceStringBuffer.length();
						else
							{
							if( foundSpecificationWord_ )
								{
								specificationStartWordPosition_ = CommonVariables.writeSentenceStringBuffer.length();

								// To recovery an unsuccessful specification match
								previousSpecificationStringBuffer_ = lastSpecificationStringBuffer_;
								lastSpecificationStringBuffer_ = new StringBuffer( CommonVariables.writeSentenceStringBuffer );
								}
							}
						}
					}
				else
					myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );
				}
			else
				myWord_.setErrorInWord( 1, moduleNameString_, "The given grammar definition is not a grammar word" );
			}
		else
			myWord_.setErrorInWord( 1, moduleNameString_, "The given definition grammar item is undefined" );

		writeResult.foundWordToWrite = ( writeWordString_ != null );
		writeResult.skipClearWriteLevel = skipClearWriteLevel_;
		writeResult.result = CommonVariables.result;
		return writeResult;
		}
	};

/*************************************************************************
 *
 *	"Your word is a lamp to guide my feet
 *	and a light for my path." (Psalm 119:105)
 *
 *************************************************************************/
