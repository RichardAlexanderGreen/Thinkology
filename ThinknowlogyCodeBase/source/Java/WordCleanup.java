/*
 *	Class:			WordCleanup
 *	Supports class:	WordItem
 *	Purpose:		To cleanup unnecessary items
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

class WordCleanup
	{
	// Private constructible variables

	private WordItem myWord_;
	private String moduleNameString_;


	// Constructor

	protected WordCleanup( WordItem myWord )
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

	protected byte currentItemNr()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].getCurrentItemNrInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to get the current item number" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte getHighestInUseSentenceNr( boolean includeTemporaryLists, boolean includeDeletedItems, int highestSentenceNr )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS &&
		CommonVariables.highestInUseSentenceNr < highestSentenceNr )
			{
			if( myWord_.wordList[wordListNr] != null &&

			( includeTemporaryLists ||
			!myWord_.wordList[wordListNr].isTemporaryList() ) )
				{
				if( myWord_.wordList[wordListNr].getHighestInUseSentenceNrInList( includeDeletedItems, highestSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to check if the given sentence is empty" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte rollbackDeletedRedoInfo()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].rollbackDeletedRedoInfoInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to delete the current redo info" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte deleteRollbackInfo()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].deleteRollbackInfoInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to delete the rollback info" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte decrementSentenceNrs( int startSentenceNr )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].decrementSentenceNrsInList( startSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to decrement the sentence numbers from the current sentence number in one of my lists" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte undoCurrentSentence()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].undoCurrentSentenceInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to undo the current sentence" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte redoCurrentSentence()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].redoCurrentSentenceInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to redo the current sentence" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte removeFirstRangeOfDeletedItems()
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS &&
		CommonVariables.nDeletedItems == 0 )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].removeFirstRangeOfDeletedItemsInList() != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to remove the first deleted items" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte decrementItemNrRange( int decrementSentenceNr, int decrementItemNr, int decrementOffset )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].decrementItemNrRangeInList( decrementSentenceNr, decrementItemNr, decrementOffset ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to decrement item number range" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}

	protected byte deleteSentences( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		short wordListNr = 0;

		while( CommonVariables.result == Constants.RESULT_OK &&
		wordListNr < Constants.NUMBER_OF_WORD_LISTS )
			{
			if( myWord_.wordList[wordListNr] != null )
				{
				if( myWord_.wordList[wordListNr].deleteSentencesInList( isAvailableForRollback, lowestSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInWord( myWord_.wordListChar( wordListNr ), 1, moduleNameString_, "I failed to delete sentences in one of my lists" );
				}

			wordListNr++;
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"He has paid a full ransom for his people.
 *	He has guaranteed his convenant with them forever.
 *	What a holy, awe-inspiring name he has!" (Psalm 111:9)
 *
 *************************************************************************/
