/*
 *	Class:			AdminReadParse
 *	Supports class:	AdminItem
 *	Purpose:		To parse the read sentence
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

class AdminReadParse
	{
	// Private constructible variables


	private int currentParseWordPosition_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Constructor

	protected AdminReadParse( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		currentParseWordPosition_ = 0;
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

	protected ReadResultType parseReadWords( short grammarLevel, GrammarItem parseGrammarItem )
		{
		ReadResultType readResult = new ReadResultType();
		boolean isOption;
		boolean isChoice;
		boolean isStartGrammarDefinition;
		boolean waitForNewStart;
		boolean waitForChoiceEnd;
		int startWordPosition;
		int previousWordPosition;
		int choiceWordPosition = 0;
		int optionWordPosition = 0;
		GrammarItem localDefinitionGrammarItem;

		if( grammarLevel < Constants.MAX_GRAMMAR_LEVEL )
			{
			if( parseGrammarItem != null )
				{
				if( admin_.readList != null )
					{
					if( grammarLevel == Constants.NO_GRAMMAR_LEVEL )
						currentParseWordPosition_ = 0;

					startWordPosition = currentParseWordPosition_;

					do	{
						isOption = false;
						isChoice = false;
						isStartGrammarDefinition = true;
						waitForNewStart = true;
						waitForChoiceEnd = false;
						currentParseWordPosition_ = startWordPosition;
						previousWordPosition = startWordPosition;
						localDefinitionGrammarItem = parseGrammarItem;

						do	{
							if( waitForNewStart &&
							parseGrammarItem.isNewStart() )	// Skip first grammar definition item, if not is a data item
								waitForNewStart = false;

							if( !waitForNewStart &&
							!waitForChoiceEnd )
								{
								previousWordPosition = currentParseWordPosition_;

								if( parseGrammarItem.isOptionStart() )
									{
									isOption = true;
									optionWordPosition = currentParseWordPosition_;
									}

								if( parseGrammarItem.isChoiceStart() )
									{
									isChoice = true;
									choiceWordPosition = currentParseWordPosition_;
									}

								if( isStartGrammarDefinition )
									{
									if( ( readResult = admin_.readList.selectMatchingWordType( localDefinitionGrammarItem.grammarWordTypeNr(), localDefinitionGrammarItem.grammarParameter(), currentParseWordPosition_ ) ).result == Constants.RESULT_OK )
										{
										if( readResult.foundMatchingWordType )
											currentParseWordPosition_++;
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to select a matching word type" );
									}
								else
									{
									if( grammarLevel + 1 < Constants.MAX_GRAMMAR_LEVEL )
										{
										if( parseGrammarItem.definitionGrammarItem != null )
											{
											if( parseReadWords( (short)( grammarLevel + 1 ), parseGrammarItem.definitionGrammarItem ).result == Constants.RESULT_OK )
												{
												if( currentParseWordPosition_ == previousWordPosition )		// Unsuccessful
													{
													if( isOption )
														waitForNewStart = true;

													if( isChoice &&
													choiceWordPosition >= optionWordPosition )
														{
														if( !parseGrammarItem.isChoiceEnd )
															waitForNewStart = true;

														previousWordPosition = choiceWordPosition;
														currentParseWordPosition_ = choiceWordPosition;
														}
													}
												}
											else
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to parse the read words at grammar word \"" + parseGrammarItem.grammarString() + "\"" );
											}
										else
											myWord_.setErrorInItem( 1, moduleNameString_, "Grammar word \"" + parseGrammarItem.grammarString() + "\" isn't defined in the grammar file" );
										}
									else
										myWord_.setErrorInItem( 1, moduleNameString_, "There is probably an endless loop in the grammar definitions, because the grammar level reached: #" + grammarLevel );
									}
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								isStartGrammarDefinition = false;

								if( parseGrammarItem.isOptionEnd )
									isOption = false;

								if( parseGrammarItem.isChoiceEnd )
									{
									isChoice = false;
									waitForChoiceEnd = false;
									}

								parseGrammarItem = parseGrammarItem.nextGrammarItem();

								if( isChoice &&
								!waitForChoiceEnd &&
								parseGrammarItem != null &&
								parseGrammarItem.isNewStart() &&
								currentParseWordPosition_ > previousWordPosition )
									waitForChoiceEnd = true;
								}
							}
						while( CommonVariables.result == Constants.RESULT_OK &&

						( waitForNewStart ||
						waitForChoiceEnd ||
						currentParseWordPosition_ > previousWordPosition ) &&

						parseGrammarItem != null &&
						localDefinitionGrammarItem.activeSentenceNr() == parseGrammarItem.activeSentenceNr() );

						if( !waitForNewStart &&
						currentParseWordPosition_ > startWordPosition &&
						currentParseWordPosition_ == previousWordPosition )
							currentParseWordPosition_ = startWordPosition;
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					currentParseWordPosition_ == startWordPosition &&
					localDefinitionGrammarItem != null &&
					( parseGrammarItem = localDefinitionGrammarItem.nextDefinitionGrammarItem ) != null );

					if( CommonVariables.result == Constants.RESULT_OK &&
					localDefinitionGrammarItem != null &&
					localDefinitionGrammarItem.hasGrammarParameter() &&

					( previousWordPosition > startWordPosition ||
					currentParseWordPosition_ > startWordPosition ) )
						{
						if( admin_.readList.setGrammarParameter( ( currentParseWordPosition_ > startWordPosition ), startWordPosition, ( currentParseWordPosition_ > startWordPosition ? currentParseWordPosition_ : previousWordPosition ), localDefinitionGrammarItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to set the grammar parameter of the read words between the positions " + startWordPosition + " and " + currentParseWordPosition_ );
						}
					}
				else
					myWord_.setErrorInItem( 1, moduleNameString_, "The read list isn't created yet" );
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given parse grammar item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given grammar level is too high: #" + grammarLevel );

		readResult.currentParseWordPosition = currentParseWordPosition_;
		readResult.result = CommonVariables.result;
		return readResult;
		
		}
	};

/*************************************************************************
 *
 *	"Sing to the Lord a new song.
 *	Sing his praises in the assembly of the faithful." (Psalm 149:1)
 *
 *************************************************************************/
