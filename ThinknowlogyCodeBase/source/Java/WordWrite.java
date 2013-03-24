/*
 *	Class:			WordWrite
 *	Supports class:	WordItem
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

class WordWrite
	{
	// Private constructible variables

	private WordItem myWord_;
	private String moduleNameString_;

	// Constructor

	protected WordWrite( WordItem myWord )
		{
		String errorString = null;

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

	protected byte writeJustificationSpecification( SpecificationItem justificationSpecificationItem )
		{
		if( justificationSpecificationItem != null )
			{
			if( writeSelectedSpecification( true, true, false, false, Constants.NO_ANSWER_PARAMETER, justificationSpecificationItem ) == Constants.RESULT_OK )
				{
				if( CommonVariables.writeSentenceStringBuffer != null &&
				CommonVariables.writeSentenceStringBuffer.length() > 0 )
					{
					if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WRITE, Constants.INTERFACE_LISTING_JUSTIFICATION_SENTENCE_START ) == Constants.RESULT_OK )
						{
						if( Presentation.writeDiacriticalText( true, false, Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a justification sentence" );
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write the justification sentence start string" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "I couldn't write the selected specification with justification" );
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected specification with justification" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given justification specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeSelectedSpecificationInfo( boolean isAssignment, boolean isDeactive, boolean isArchive, boolean isQuestion, WordItem writeWordItem )
		{
		SpecificationItem currentSpecificationItem;

		if( writeWordItem != null )
			{
			if( ( currentSpecificationItem = myWord_.firstSelectedSpecification( isAssignment, isDeactive, isArchive, isQuestion ) ) != null )
				{
				do	{
					if( currentSpecificationItem.specificationWordItem() == writeWordItem )
						{
						if( writeSelectedSpecification( false, true, false, false, Constants.NO_ANSWER_PARAMETER, currentSpecificationItem ) == Constants.RESULT_OK )
							{
							if( CommonVariables.writeSentenceStringBuffer != null &&
							CommonVariables.writeSentenceStringBuffer.length() > 0 )
								{
								if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_WRITE, ( isQuestion ? Constants.INTERFACE_LISTING_SPECIFICATION_QUESTIONS : Constants.INTERFACE_LISTING_SPECIFICATIONS ) ) == Constants.RESULT_OK )
									{
									if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
										myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence" );
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a related header" );
								}
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected specification" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentSpecificationItem = currentSpecificationItem.nextSelectedSpecificationItem( false ) ) != null );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write word item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeSelectedRelationInfo( boolean isAssignment, boolean isDeactive, boolean isArchive, boolean isQuestion, WordItem writeWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		SpecificationItem foundAssignmentItem;
		SpecificationItem currentSpecificationItem = null;

		if( writeWordItem != null )
			{
			if( ( currentSpecificationItem = myWord_.firstSelectedSpecification( isAssignment, isDeactive, isArchive, isQuestion ) ) != null )
				{
				do	{
					if( writeWordItem.hasContextInWord( currentSpecificationItem.isPossessive(), currentSpecificationItem.relationContextNr(), currentSpecificationItem.specificationWordItem() ) )
						{
						if( ( specificationResult = myWord_.findAssignmentByRelationContext( true, isDeactive, isArchive, currentSpecificationItem.isPossessive(), currentSpecificationItem.questionParameter(), writeWordItem ) ).result == Constants.RESULT_OK )
							{
							foundAssignmentItem = specificationResult.foundSpecificationItem;

							if( isQuestion ||

							( isAssignment &&
							foundAssignmentItem != null ) ||

							( !isAssignment &&
							foundAssignmentItem == null ) )
								{
								if( writeSelectedSpecification( false, false, false, false, Constants.NO_ANSWER_PARAMETER, currentSpecificationItem ) == Constants.RESULT_OK )
									{
									if( CommonVariables.writeSentenceStringBuffer != null &&
									CommonVariables.writeSentenceStringBuffer.length() > 0 )
										{
										if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( isQuestion ? Constants.INTERFACE_LISTING_RELATED_QUESTIONS : Constants.INTERFACE_LISTING_RELATED_INFORMATION ) ) == Constants.RESULT_OK )
											{
											if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
												myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence" );
											}
										else
											myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a related header" );
										}
									}
								else
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected specification" );
								}
							}
						else
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find an assignment by relation context" );
						}
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentSpecificationItem = currentSpecificationItem.nextSelectedSpecificationItem( false ) ) != null );
				}
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write word item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeSelectedSpecification( boolean forceResponseNotBeingAssignment, boolean forceResponseNotBeingFirstSpecification, boolean writeOnlyCurrentSentence, boolean writeOnlyGivenSpecificationWord, short answerParameter, SpecificationItem writeSpecificationItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		boolean isFirstRelatedSpecification;
		boolean isSpecificationCompoundCollection;
		boolean hasAssignment = false;
		boolean isCombinedSpecification = false;
		boolean isLastCompoundSpecification = false;
		boolean isPossessiveRelation = false;
		boolean isSelfGeneratedDefinitionConclusion = false;
		short currentCollectionOrderNr;
		short highestCollectionOrderNr;
		SpecificationItem foundRelatedSpecificationItem;
		WordItem specificationWordItem;
		String specificationString;

		if( writeSpecificationItem != null )
			{
			if( ( specificationResult = myWord_.findRelatedSpecification( !writeSpecificationItem.isSelfGeneratedAssumption(), writeSpecificationItem ) ).result == Constants.RESULT_OK )
				{
				isFirstRelatedSpecification = specificationResult.isFirstRelatedSpecification;
				foundRelatedSpecificationItem = specificationResult.relatedSpecificationItem;
				specificationWordItem = writeSpecificationItem.specificationWordItem();
				specificationString = writeSpecificationItem.specificationString();

				if( !writeSpecificationItem.isAssignment() )
					{
					if( myWord_.firstAssignment( writeSpecificationItem.isPossessive(), writeSpecificationItem.questionParameter(), writeSpecificationItem.generalizationContextNr(), writeSpecificationItem.specificationContextNr(), writeSpecificationItem.relationContextNr(), specificationWordItem, specificationString ) != null )
						hasAssignment = true;
					}

				isSpecificationCompoundCollection = writeSpecificationItem.isSpecificationCompoundCollection();
				highestCollectionOrderNr = myWord_.highestCollectionOrderNrInAllWords( writeSpecificationItem.specificationCollectionNr() );
				currentCollectionOrderNr = ( specificationWordItem == null ? myWord_.collectionOrderNrByWordTypeNr( writeSpecificationItem.specificationWordTypeNr() ) : specificationWordItem.collectionOrderNrByWordTypeNr( writeSpecificationItem.specificationWordTypeNr() ) );

				if( !isFirstRelatedSpecification &&
				isSpecificationCompoundCollection &&
				currentCollectionOrderNr == highestCollectionOrderNr )
					isLastCompoundSpecification = true;

				if( !hasAssignment &&
				writeOnlyCurrentSentence &&
				!isSpecificationCompoundCollection &&
				foundRelatedSpecificationItem != null &&
				foundRelatedSpecificationItem.isOlderSentence() &&

				( currentCollectionOrderNr == 0 ||							// No specification collection
				currentCollectionOrderNr == 1 ||							// First or
				currentCollectionOrderNr == highestCollectionOrderNr ) )	// last specification of a specification collection
					isCombinedSpecification = true;

				if( writeSpecificationItem.isPossessive() &&
				writeSpecificationItem.hasRelationContext() )
					isPossessiveRelation = true;

				if( writeSpecificationItem.isGeneralizationNoun() &&
				writeSpecificationItem.isSelfGeneratedConclusion() )
					isSelfGeneratedDefinitionConclusion = true;

				if( isCombinedSpecification ||

				// Self-generated
				( isSelfGeneratedDefinitionConclusion &&

				( isLastCompoundSpecification ||
				forceResponseNotBeingFirstSpecification ||

				( isFirstRelatedSpecification &&
				!isSpecificationCompoundCollection ) ) ) ||

				// User generated
				( !isSelfGeneratedDefinitionConclusion &&

				( isPossessiveRelation ||
				isFirstRelatedSpecification ||
				forceResponseNotBeingFirstSpecification ||
				foundRelatedSpecificationItem == null ) &&

				( !hasAssignment ||
				forceResponseNotBeingAssignment ||
				writeSpecificationItem.isAssignment() ||
				writeSpecificationItem.isConditional() ) ) )
					{
					if( CommonVariables.currentGrammarLanguageWordItem != null )
						{
						if( myWord_.parseGrammarToWriteSentence( ( isPossessiveRelation || writeOnlyGivenSpecificationWord ), answerParameter, Constants.NO_GRAMMAR_LEVEL, CommonVariables.currentGrammarLanguageWordItem.startOfGrammar(), writeSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInWord( 1, moduleNameString_, "I failed to parse the grammar to write a sentence" );
						}
					else
						return myWord_.setErrorInWord( 1, moduleNameString_, "The current language word item is undefined" );
					}
				else
					CommonVariables.writeSentenceStringBuffer = null;
				}
			else
				myWord_.addErrorInWord( 1, moduleNameString_, "I failed to find a related specification" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );

		return CommonVariables.result;
		}

	protected byte writeSpecification( boolean isAdjustedSpecification, boolean isCorrectedAssumptionByKnowledge, boolean isCorrectedAssumptionByOppositeQuestion, SpecificationItem writeSpecificationItem )
		{
		boolean isQuestion;

		if( writeSpecificationItem != null )
			{
			isQuestion = writeSpecificationItem.isQuestion();

			if( isQuestion ||
			writeSpecificationItem.isSelfGeneratedAssumption() )
				{
				if( writeSpecificationItem.isOlderSentence() )
					{
					if( writeSelectedSpecification( true, true, false, false, Constants.NO_ANSWER_PARAMETER, writeSpecificationItem ) == Constants.RESULT_OK )
						{
						if( CommonVariables.writeSentenceStringBuffer != null &&
						CommonVariables.writeSentenceStringBuffer.length() > 0 )
							{
							if( Presentation.writeInterfaceText( true, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( isCorrectedAssumptionByKnowledge ? Constants.INTERFACE_LISTING_MY_CORRECTED_ASSUMPTIONS_BY_KNOWLEDGE : ( isCorrectedAssumptionByOppositeQuestion ? Constants.INTERFACE_LISTING_MY_CORRECTED_ASSUMPTIONS_BY_OPPOSITE_QUESTION : ( isAdjustedSpecification ? ( isQuestion ? Constants.INTERFACE_LISTING_MY_ADJUSTED_QUESTIONS : Constants.INTERFACE_LISTING_MY_ASSUMPTIONS_THAT_ARE_ADJUSTED ) : ( writeSpecificationItem.isSelfGenerated() ? Constants.INTERFACE_LISTING_MY_QUESTIONS_THAT_ARE_ANSWERED : Constants.INTERFACE_LISTING_YOUR_QUESTIONS_THAT_ARE_ANSWERED ) ) ) ) ) == Constants.RESULT_OK )
								{
								if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, CommonVariables.writeSentenceStringBuffer.toString() ) != Constants.RESULT_OK )
									myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a sentence" );
								}
							else
								myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a header" );
							}
						}
					else
						myWord_.addErrorInWord( 1, moduleNameString_, "I failed to write a selected specification" );
					}
				else
					return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item isn't old" );
				}
			else
				return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is not a question, nor an assumption" );
			}
		else
			return myWord_.setErrorInWord( 1, moduleNameString_, "The given write specification item is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Tremble, O earth, at the presence of the Lord,
 *	at the presence of the God of Jacob.
 *	He turned the rock into a pool of water;
 *	yes, a spring of water flowed from solid rock." (Psalm 114:7-8)
 *
 *************************************************************************/
