/*
 *	Class:			AdminJustification
 *	Supports class:	AdminItem
 *	Purpose:		To write justification reports for the
 *					self-generated knowledge
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

class AdminJustification
	{
	// Private constructible variables

	private WordItem previousSpecificGeneralizationWordItem_;

	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private byte writeJustificationSpecification( boolean showDefinitionSpecification, boolean showSpecificSpecification, JustificationItem specificationJustificationItem )
		{
		SpecificationItem definitionSpecificationItem;
		SpecificationItem anotherDefinitionSpecificationItem;
		SpecificationItem specificSpecificationItem;
		WordItem generalizationWordItem;

		if( specificationJustificationItem != null )
			{
			if( showDefinitionSpecification &&
			( definitionSpecificationItem = specificationJustificationItem.definitionSpecificationItem() ) != null )
				{
				if( ( generalizationWordItem = definitionSpecificationItem.generalizationWordItem() ) != null )
					{
					if( generalizationWordItem.writeJustificationSpecification( definitionSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a definition justification specification" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The definition specification item of the given specification justification item has no generalization word" );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			( anotherDefinitionSpecificationItem = specificationJustificationItem.anotherDefinitionSpecificationItem() ) != null )
				{
				if( ( generalizationWordItem = anotherDefinitionSpecificationItem.generalizationWordItem() ) != null )
					{
					if( generalizationWordItem.writeJustificationSpecification( anotherDefinitionSpecificationItem ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write another definition justification specification" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The another definition specification item of the given specification justification item has no generalization word" );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			( specificSpecificationItem = specificationJustificationItem.specificSpecificationItem() ) != null )
				{
				if( showDefinitionSpecification ||
				showSpecificSpecification )
					{
					if( ( generalizationWordItem = specificSpecificationItem.generalizationWordItem() ) != null )
						{
						if( generalizationWordItem.writeJustificationSpecification( specificSpecificationItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a specific justification specification" );
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The specific specification item of the given specification justification item has no generalization word" );
					}
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification justification item is undefined" );

		return CommonVariables.result;
		}

	private byte writeJustificationSpecifications( boolean showDefinitionSpecification, JustificationItem firstJustificationItem )
		{
		JustificationItem currentJustificationItem;
		SpecificationItem specificSpecificationItem;
		WordItem specificGeneralizationWordItem;

		if( firstJustificationItem != null )
			{
			currentJustificationItem = firstJustificationItem;

			do	{
				specificSpecificationItem = currentJustificationItem.specificSpecificationItem();
				specificGeneralizationWordItem = ( specificSpecificationItem == null ? null : specificSpecificationItem.generalizationWordItem() );

				if( writeJustificationSpecification( showDefinitionSpecification, ( specificSpecificationItem.hasRelationContext() || specificGeneralizationWordItem != previousSpecificGeneralizationWordItem_ ), currentJustificationItem ) == Constants.RESULT_OK )
					{
					if( specificSpecificationItem != null )
						{
						showDefinitionSpecification = false;
						previousSpecificGeneralizationWordItem_ = specificGeneralizationWordItem;
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the current justification specification" );
				}
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentJustificationItem = currentJustificationItem.nextJustificationItemWithSameTypeAndOrderNr() ) != null );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given first justification item is undefined" );

		return CommonVariables.result;
		}

	private byte writeJustificationType( boolean isFirstJustificationType, String justificationSentenceString, JustificationItem specificationJustificationItem, SpecificationItem selfGeneratedSpecificationItem )
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
		WordItem generalizationWordItem;

		previousSpecificGeneralizationWordItem_ = null;

		if( justificationSentenceString != null )
			{
			if( specificationJustificationItem != null )
				{
				if( selfGeneratedSpecificationItem != null )
					{
					if( ( generalizationWordItem = selfGeneratedSpecificationItem.generalizationWordItem() ) != null )
						{
						if( isFirstJustificationType )
							{
							if( Presentation.writeDiacriticalText( Constants.PRESENTATION_PROMPT_WRITE, justificationSentenceString ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the justification sentence" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( Presentation.writeInterfaceText( false, !isFirstJustificationType, Constants.PRESENTATION_PROMPT_NOTIFICATION, ( isFirstJustificationType ? Constants.INTERFACE_LISTING_JUSTIFICATION_BECAUSE : Constants.INTERFACE_LISTING_JUSTIFICATION_AND ) ) == Constants.RESULT_OK )
								{
								if( writeJustificationSpecifications( true, specificationJustificationItem ) == Constants.RESULT_OK )
									{
									isAnsweredQuestion = selfGeneratedSpecificationItem.isAnsweredQuestion();

									if( ( currentSpecificationItem = generalizationWordItem.firstSelectedSpecification( isAnsweredQuestion, selfGeneratedSpecificationItem.isAssignment(), selfGeneratedSpecificationItem.isDeactiveItem(), selfGeneratedSpecificationItem.isArchiveItem(), selfGeneratedSpecificationItem.questionParameter() ) ) != null )
										{
										isExclusive = selfGeneratedSpecificationItem.isExclusive();
										isPossessive = selfGeneratedSpecificationItem.isPossessive();
										generalizationCollectionNr = selfGeneratedSpecificationItem.generalizationCollectionNr();
										specificationCollectionNr = selfGeneratedSpecificationItem.specificationCollectionNr();
										generalizationContextNr = selfGeneratedSpecificationItem.generalizationContextNr();
										specificationContextNr = selfGeneratedSpecificationItem.specificationContextNr();
										relationContextNr = selfGeneratedSpecificationItem.relationContextNr();

										do	{
											if( currentSpecificationItem.isSelfGenerated() &&
											currentSpecificationItem != selfGeneratedSpecificationItem &&
											currentSpecificationItem.isRelatedSpecification( isExclusive, isPossessive, generalizationCollectionNr, specificationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr ) )
												{
												if( writeJustificationSpecifications( false, currentSpecificationItem.specificationJustificationItem() ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the current justification specifications" );
												}
											}
										while( CommonVariables.result == Constants.RESULT_OK &&
										( currentSpecificationItem = currentSpecificationItem.nextSpecificationItemWithSameQuestionParameter( isAnsweredQuestion ) ) != null );
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the justification specifications" );
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write the justification start string" );
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word of the self-generated specification item is undefined" );
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The given self-generated specification item is undefined" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The given specification justification is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given justification sentence string is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminJustification( WordItem myWord )
		{
		String errorString = null;

		previousSpecificGeneralizationWordItem_ = null;

		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();

		if( myWord_ == null )
			errorString = "The given my word is undefined";

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

	protected byte writeJustificationSpecification( String justificationSentenceString, SpecificationItem selfGeneratedSpecificationItem )
		{
		boolean isFirstJustificationType = true;
		JustificationItem firstJustificationItem;
		JustificationItem currentJustificationItem;

		if( selfGeneratedSpecificationItem != null )
			{
			if( ( firstJustificationItem = selfGeneratedSpecificationItem.specificationJustificationItem() ) != null )
				{
				currentJustificationItem = firstJustificationItem;

				do	{
					if( writeJustificationType( isFirstJustificationType, justificationSentenceString, currentJustificationItem, selfGeneratedSpecificationItem ) == Constants.RESULT_OK )
						isFirstJustificationType = false;
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write a justification type of a specification" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				( currentJustificationItem = currentJustificationItem.nextJustificationItemWithDifferentTypeOrOrderNr( firstJustificationItem ) ) != null );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The specification justification of the given self-generated specification item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given self-generated specification item is undefined" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"Oh, the joys of those who trust the Lord,
 *	who have no confidence of the proud
 *	or in those who worship idols." (Psalm 40:4)
 *
 *************************************************************************/
