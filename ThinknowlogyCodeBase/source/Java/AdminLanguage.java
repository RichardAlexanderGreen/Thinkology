/*
 *	Class:			AdminLanguage
 *	Supports class:	AdminItem
 *	Purpose:		To create and assign the grammar and interface languages
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

class AdminLanguage
	{
	// Private constructible variables

	private WordItem foundLanguageWordItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;
	private String languageKey_;


	// Private methods

	private byte findLanguageByName( String languageNameString )
		{
		WordResultType wordResult = new WordResultType();
		foundLanguageWordItem_ = null;

		if( languageNameString != null )
			{
			// Initially the language name words are not linked
			// to the language defintion word. So, search in all words.
			if( ( wordResult = myWord_.findWordTypeInAllWords( false, false, Constants.WORD_TYPE_PROPER_NAME, languageNameString.length(), languageNameString.toString() ) ).result == Constants.RESULT_OK )
				foundLanguageWordItem_ = wordResult.foundWordItem;
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a language word" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given language name string is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminLanguage( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		foundLanguageWordItem_ = null;

		admin_ = admin;
		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();
		languageKey_ = moduleNameString_;		// The address of the class is the key - not the content of the string

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

	protected byte authorizeLanguageWord( WordItem authorizationWordItem )
		{
		if( authorizationWordItem != null )
			{
			if( authorizationWordItem.assignChangePermissions( languageKey_ ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign my language change permissions to a word" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given authorization word item is undefined" );

		return CommonVariables.result;
		}

	protected byte createGrammarLanguage( String languageNameString )
		{
		WordResultType wordResult = new WordResultType();
		WordItem grammarLanguageWordItem;

		// Create new language or switch to another language if needed
		if( findLanguageByName( languageNameString ) == Constants.RESULT_OK )
			{
			if( foundLanguageWordItem_ == null )
				{
				// From now, all word-types need to be created in the new grammar language
				CommonVariables.currentGrammarLanguageNr++;

				if( ( wordResult = admin_.createWord( false, Constants.WORD_TYPE_PROPER_NAME, Constants.NO_WORD_PARAMETER, languageNameString.length(), languageNameString ) ).result == Constants.RESULT_OK )
					{
					if( ( grammarLanguageWordItem = wordResult.createdWordItem ) != null )
						{
						if( admin_.authorizeLanguageWord( grammarLanguageWordItem ) == Constants.RESULT_OK )
							CommonVariables.currentGrammarLanguageWordItem = grammarLanguageWordItem;
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the created grammar language word" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The last created word item is undefined" );
					}
				else
					{
					CommonVariables.currentGrammarLanguageNr--;		// Restore old grammar language
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a grammar language word" );
					}
				}
			else
				CommonVariables.currentGrammarLanguageWordItem = foundLanguageWordItem_;
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the grammar language" );

		return CommonVariables.result;
		}

	protected byte createInterfaceLanguage( String languageNameString )
		{
		WordResultType wordResult = new WordResultType();
		WordItem interfaceLanguageWordItem;

		// Create new language or switch to another language if needed
		if( findLanguageByName( languageNameString ) == Constants.RESULT_OK )
			{
			if( foundLanguageWordItem_ == null )
				{
				// From now, all word-types need to be created in the new (grammar) language
				CommonVariables.currentGrammarLanguageNr++;

				if( ( wordResult = admin_.createWord( false, Constants.WORD_TYPE_PROPER_NAME, Constants.NO_WORD_PARAMETER, languageNameString.length(), languageNameString ) ).result == Constants.RESULT_OK )
					{
					if( ( interfaceLanguageWordItem = wordResult.createdWordItem ) != null )
						{
						if( admin_.authorizeLanguageWord( interfaceLanguageWordItem ) == Constants.RESULT_OK )
							CommonVariables.currentInterfaceLanguageWordItem = interfaceLanguageWordItem;
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to authorize the created interface language word" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The last created word item is undefined" );
					}
				else
					{
					CommonVariables.currentGrammarLanguageNr--;	// Restore old grammar language number
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create an interface language word" );
					}
				}
			else
				CommonVariables.currentInterfaceLanguageWordItem = foundLanguageWordItem_;
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the grammar language" );

		return CommonVariables.result;
		}

	protected byte createLanguageSpecification( WordItem languageWordItem, WordItem languageNounWordItem )
		{
		if( languageWordItem != null )
			{
			if( languageWordItem.isPropername() )
				{
				if( languageNounWordItem != null )
					{
					if( admin_.addSpecification( true, false, false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.WORD_TYPE_PROPER_NAME, Constants.WORD_TYPE_NOUN_SINGULAR, Constants.WORD_TYPE_UNDEFINED, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_COLLECTION_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, 0, null, languageWordItem, languageNounWordItem, null, null ).result != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a new language specification" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given language noun word item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given language word is not a proper-name" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given language word item is undefined" );

		return CommonVariables.result;
		}

	protected byte assignInterfaceLanguage( String languageNameString )
		{
		// Switch to another language if needed
		if( findLanguageByName( languageNameString ) == Constants.RESULT_OK )
			{
			if( foundLanguageWordItem_ != null )
				{
				if( foundLanguageWordItem_.isInterfaceLanguage() )
					{
					if( CommonVariables.currentInterfaceLanguageWordItem != foundLanguageWordItem_ )
						{
						if( assignSpecificationWithAuthorization( false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, 0, null, foundLanguageWordItem_, admin_.predefinedNounInterfaceLanguageWordItem(), null ).result == Constants.RESULT_OK )
							CommonVariables.currentInterfaceLanguageWordItem = foundLanguageWordItem_;
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign an interface language" );
						}
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given language is not an interface language" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the requested language" );
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the grammar language" );

		return CommonVariables.result;
		}

	protected byte assignGrammarAndInterfaceLanguage( short newLanguageNr )
		{
		boolean foundLanguage = false;
		GeneralizationItem currentGeneralizationItem;
		WordItem generalizationWordItem;

		if( CommonVariables.predefinedNounGrammarLanguageWordItem != null )
			{
			if( ( currentGeneralizationItem = CommonVariables.predefinedNounGrammarLanguageWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
				{
				do	{
					if( ( generalizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
						{
						if( generalizationWordItem.hasCollectionOrderNr( newLanguageNr ) )
							{
							foundLanguage = true;
							CommonVariables.currentGrammarLanguageWordItem = generalizationWordItem;
							}
						}
					}
				while( !foundLanguage &&
				( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );

				if( foundLanguage ||
				newLanguageNr == 1 )	// First language has no collection to another language
					{
					if( CommonVariables.currentGrammarLanguageWordItem != null )
						{
						if( assignSpecificationWithAuthorization( false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, 0, null, CommonVariables.currentGrammarLanguageWordItem, CommonVariables.predefinedNounGrammarLanguageWordItem, null ).result == Constants.RESULT_OK )
							{
							CommonVariables.currentGrammarLanguageNr = newLanguageNr;

							// Also assign the interface language, if possible
							if( CommonVariables.currentGrammarLanguageWordItem.isInterfaceLanguage() )
								{
								CommonVariables.currentInterfaceLanguageWordItem = CommonVariables.currentGrammarLanguageWordItem;

								if( assignSpecificationWithAuthorization( false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, 0, null, CommonVariables.currentInterfaceLanguageWordItem, admin_.predefinedNounInterfaceLanguageWordItem(), null ).result != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the interface language with authorization" );
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the grammar language with authorization" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The current grammar language word item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the requested language" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined grammar language noun word item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType addSpecificationWithAuthorization( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( ( specificationResult = generalizationWordItem.addSpecificationInWord( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, specificationWordItem, relationWordItem, specificationString, languageKey_ ) ).result != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification with authorization" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType assignSpecificationWithAuthorization( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( ( specificationResult = generalizationWordItem.assignSpecificationInWord( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString, languageKey_ ) ).result != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign a specification with authorization" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}
	};

/*************************************************************************
 *
 *	"The Sovereign Lord has given me his words of wisdom,
 *	so that I know how to comfort the weary.
 *	Morning by morning he wakens me
 *	and opens my understanding to his will." (Psalm 50:4)
 *
 *************************************************************************/
