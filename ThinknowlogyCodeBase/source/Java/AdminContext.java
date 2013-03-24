/*
 *	Class:			AdminContext
 *	Supports class:	AdminItem
 *	Purpose:		To create context structures
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

class AdminContext
	{
	// Private constructible variables

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private ContextResultType findPossessiveReversibleConclusionContextNrOfInvolvedWords( boolean isExclusive
																						, boolean isNegative
																						, boolean isPossessive
																						, int nContextRelations
																						, int relationContextNr
																						, WordItem generalizationWordItem
																						, WordItem specificationWordItem )
		{
		ContextResultType contextResult = new ContextResultType();
		boolean foundAllRelationWords;
		int currentRelationContextNr;
		int nContextWords;
		GeneralizationItem currentGeneralizationItem;
		SpecificationItem currentSpecificationItem;
		WordItem currentGeneralizationWordItem;

		if( relationContextNr > Constants.NO_CONTEXT_NR )
			{
			if( generalizationWordItem != null )
				{
				if( specificationWordItem != null )
					{
					if( ( currentGeneralizationItem = generalizationWordItem.firstActiveGeneralizationItem() ) != null )
						{
						do	{
							if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
								{
								if( ( currentSpecificationItem = currentGeneralizationWordItem.firstSelectedSpecification( false, false, false, false ) ) != null )
									{
									do	{
										if( currentSpecificationItem.hasRelationContext() &&
										currentSpecificationItem.isPossessive() == isPossessive &&
										currentSpecificationItem.isRelatedSpecification( isExclusive, isNegative, isPossessive, specificationWordItem ) )
											{
											currentRelationContextNr = currentSpecificationItem.relationContextNr();
											nContextWords = myWord_.nContextWords( isPossessive, currentRelationContextNr, specificationWordItem );
											foundAllRelationWords = ( nContextWords + 1 == nContextRelations );		// This relation word will be the last one

											if( currentSpecificationItem.isSelfGeneratedPossessiveReversibleConclusion() )
												{
												if( ( isPossessive &&
												currentRelationContextNr != relationContextNr ) ||

												( !isPossessive &&
												!foundAllRelationWords &&
												currentRelationContextNr == relationContextNr ) )
													{
													if( foundAllRelationWords )
														{
														contextResult.contextNr = relationContextNr;
														contextResult.replaceContextNr = currentRelationContextNr;
														}
													else
														contextResult.contextNr = currentRelationContextNr;
													}
												}
											else
												{
												if( !isPossessive &&
												nContextWords == nContextRelations &&
												currentRelationContextNr != relationContextNr )
													{
													contextResult.contextNr = currentRelationContextNr;
													contextResult.replaceContextNr = relationContextNr;
													}
												}
											}
										}
									while( contextResult.contextNr == Constants.NO_CONTEXT_NR &&
									( currentSpecificationItem = currentSpecificationItem.nextSelectedSpecificationItem( false ) ) != null );
									}
								}
							else
								myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
							}
						while( CommonVariables.result == Constants.RESULT_OK &&
						( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItem() ) != null );
						}
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given relation context number is undefined" );

		contextResult.result = CommonVariables.result;
		return contextResult;
		}


	// Constructor

	protected AdminContext( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

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


	// Protected context methods

	protected int highestContextNr()
		{
		int tempContextNr;
		int highestContextNr = Constants.NO_CONTEXT_NR;
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	{
				if( ( tempContextNr = currentWordItem.highestContextNrInWord() ) > highestContextNr )
					highestContextNr = tempContextNr;
				}
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}

		return highestContextNr;
		}

	protected ContextResultType addPronounContext( short contextWordTypeNr, WordItem contextWordItem )
		{
		ContextResultType contextResult = new ContextResultType();

		if( contextWordItem != null )
			{
			if( ( contextResult.contextNr = contextWordItem.contextNrInWord( false, contextWordTypeNr, null ) ) == Constants.NO_CONTEXT_NR )
				{
				if( ( contextResult.contextNr = highestContextNr() ) < Constants.MAX_CONTEXT_NR )
					contextResult.contextNr++;
				else
					myWord_.setSystemErrorInItem( 1, moduleNameString_, "Context number overflow" );
				}

			if( contextWordItem.addContext( false, contextWordTypeNr, Constants.WORD_TYPE_UNDEFINED, contextResult.contextNr, null ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a context to word \"" + contextWordItem.anyWordTypeString() + "\"" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The read word of the read ahead item is undefined" );

		contextResult.result = CommonVariables.result;
		return contextResult;
		}

	protected ContextResultType getRelationContextNr( boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isUserSentence, short questionParameter, int generalizationContextNr, int specificationContextNr, int nContextRelations, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, ReadItem startRelationReadItem )
		{
		ContextResultType contextResult = new ContextResultType();
		boolean foundRelationContext;
		boolean foundRelationWordInThisList;
		boolean skipThisContext = false;
		int currentRelationContextNr;
		ContextItem currentRelationContextItem;
		ReadItem relationWordReadItem = null;
		SpecificationItem foundSpecificationItem;
		WordItem currentWordItem;
		WordItem relationContextWordItem = relationWordItem;

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				if( startRelationReadItem != null )
					{
					if( ( relationWordReadItem = startRelationReadItem.getFirstRelationWordReadItem() ) != null )
						relationContextWordItem = relationWordReadItem.readWordItem();
					else
						myWord_.setErrorInItem( 1, moduleNameString_, "The read word of the first relation word is undefined" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( relationContextWordItem != null )
						{
						if( ( currentRelationContextItem = relationContextWordItem.firstActiveContext() ) != null )
							{
							do	{	// Do for all relation context items in the first relation context word
								currentRelationContextNr = currentRelationContextItem.contextNr();

								if( relationContextWordItem.hasContextInWord( isPossessive, currentRelationContextNr, specificationWordItem ) )
									{
									if( ( currentWordItem = CommonVariables.firstWordItem ) != null )
										{
										foundRelationWordInThisList = false;
										skipThisContext = false;

										do	{	// Do for all words - either in the current relation list or outside this list
											foundSpecificationItem = ( isUserSentence ? null : generalizationWordItem.firstAssignmentOrSpecification( true, isNegative, isPossessive, questionParameter, generalizationContextNr, specificationContextNr, currentRelationContextNr, specificationWordItem ) );

											if( foundSpecificationItem == null ||
											!foundSpecificationItem.isSelfGeneratedConclusion() ||
											foundSpecificationItem.relationContextNr() != currentRelationContextNr )
												{
												if( relationWordReadItem != null )
													foundRelationWordInThisList = relationWordReadItem.foundRelationWordInThisList( currentWordItem );

												foundRelationContext = currentWordItem.hasContextInWord( isPossessive, currentRelationContextNr, specificationWordItem );

												// Word is one of the relation words in this list, but doesn't have current context
												if( ( !foundRelationContext &&
												foundRelationWordInThisList ) ||

												// Word is in not current list of relation words, but has current context
												( foundRelationContext &&
												!foundRelationWordInThisList ) )
													skipThisContext = true;
												}
											}
										while( !skipThisContext &&
										( currentWordItem = currentWordItem.nextWordItem() ) != null );

										if( !skipThisContext )	// The relation words in the list contain this context exclusively. (So, no other words)
											contextResult.contextNr = currentRelationContextNr;
										}
									else
										myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );
									}
								}
							while( CommonVariables.result == Constants.RESULT_OK &&
							contextResult.contextNr == Constants.NO_CONTEXT_NR &&
							( currentRelationContextItem = currentRelationContextItem.nextContextItem() ) != null );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						questionParameter == Constants.NO_QUESTION_PARAMETER &&
						contextResult.contextNr == Constants.NO_CONTEXT_NR &&
						( foundSpecificationItem = generalizationWordItem.firstAssignmentOrSpecification( true, false, true, true, true, isNegative, isPossessive, Constants.NO_QUESTION_PARAMETER, generalizationContextNr, specificationContextNr, Constants.NO_CONTEXT_NR, specificationWordItem, null ) ) != null )
							{
							if( ( contextResult.contextNr = foundSpecificationItem.relationContextNr() ) > Constants.NO_CONTEXT_NR )
								{
								if( foundSpecificationItem.isExclusive() )
									// Static (exclusive) semantic ambiguity
									contextResult.isExclusiveContext = true;
								else
									{
									if( !foundSpecificationItem.isAssignment() )
										{
										if( relationWordItem == null )
											{
											if( skipThisContext )
												contextResult.contextNr = Constants.NO_CONTEXT_NR;	// Context not found: Different number of relation words
											else
												{
												// Static (exclusive) semantic ambiguity
												if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_I_NOTICED_SEMANTIC_AMBIGUITY_START, generalizationWordItem.anyWordTypeString(), Constants.INTERFACE_SENTENCE_NOTIFICATION_STATIC_SEMANTIC_AMBIGUITY_END ) == Constants.RESULT_OK )
													contextResult.isExclusiveContext = true;
												else
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
												}
											}
										else	// Try to find the relation context of a possessive reversible conclusion
											{
											if( ( contextResult = findPossessiveReversibleConclusionContextNrOfInvolvedWords( isExclusive, isNegative, isPossessive, nContextRelations, contextResult.contextNr, relationWordItem, specificationWordItem ) ).result != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find a possessive reversible conclusion context number of involved words" );
											}
										}
									}
								}
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						contextResult.contextNr == Constants.NO_CONTEXT_NR )		// No context found
							{
							if( ( contextResult.contextNr = highestContextNr() ) < Constants.MAX_CONTEXT_NR )
								contextResult.contextNr++;
							else
								myWord_.setSystemErrorInItem( 1, moduleNameString_, "Context number overflow" );
							}
						}
					else
						myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find any read context word" );
					}
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given specification word item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		contextResult.result = CommonVariables.result;
		return contextResult;
		}
	};

/*************************************************************************
 *
 *	"Praise the Lord!
 *	How joyful are those who fear the Lord
 *	and delight in obeying his commands." (Psalm 112:1)
 *
 *************************************************************************/
