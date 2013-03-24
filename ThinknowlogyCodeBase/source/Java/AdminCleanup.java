/*
 *	Class:			AdminCleanup
 *	Supports class:	AdminItem
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

class AdminCleanup
	{
	// Private constructible variables

	private boolean dontIncrementCurrentSentenceNr_;
	private boolean foundChange_;
	private boolean wasUndoOrRedo_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private void deleteResponseListsInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.deleteWriteList();
			while( ( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		}

	private byte getHighestInUseSentenceNr( boolean includeTemporaryLists, boolean includeDeletedItems, int highestSentenceNr )
		{
		short adminListNr = 0;

		CommonVariables.highestInUseSentenceNr = Constants.NO_SENTENCE_NR;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.getHighestInUseSentenceNrInWordList( includeTemporaryLists, includeDeletedItems, highestSentenceNr ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check if the given sentence is empty in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.highestInUseSentenceNr < highestSentenceNr &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null &&	// Inside admin lists

			( includeTemporaryLists ||
			!admin_.adminList[adminListNr].isTemporaryList() ) )
				{
				if( admin_.adminList[adminListNr].getHighestInUseSentenceNrInList( includeDeletedItems, highestSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to check if the given sentence is empty" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte undoCurrentSentenceInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.undoCurrentSentence();
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );

		return CommonVariables.result;
		}

	private byte redoCurrentSentenceInAllWords()
		{
		WordItem currentWordItem;

		if( ( currentWordItem = CommonVariables.firstWordItem ) != null )		// Do in all words
			{
			do	currentWordItem.redoCurrentSentence();
			while( CommonVariables.result == Constants.RESULT_OK &&
			( currentWordItem = currentWordItem.nextWordItem() ) != null );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The first word item is undefined" );

		return CommonVariables.result;
		}

	private byte rollbackDeletedRedoInfo()
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.rollbackDeletedRedoInfoInWordList() != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the current redo info of my words in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].rollbackDeletedRedoInfoInList() != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to delete the current redo info" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte undoCurrentSentence()
		{
		short adminListNr = 0;

		if( undoCurrentSentenceInAllWords() == Constants.RESULT_OK )		// Inside words
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
				{
				if( admin_.adminList[adminListNr] != null )	// Inside admin lists
					{
					if( admin_.adminList[adminListNr].undoCurrentSentenceInList() != Constants.RESULT_OK )
						myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to undo the current sentence" );
					}

				adminListNr++;
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to undo the current sentence in my word list" );

		return CommonVariables.result;
		}

	private byte redoCurrentSentence()
		{
		short adminListNr = 0;

		if( redoCurrentSentenceInAllWords() == Constants.RESULT_OK )		// Inside words
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
				{
				if( admin_.adminList[adminListNr] != null )	// Inside admin lists
					{
					if( admin_.adminList[adminListNr].redoCurrentSentenceInList() != Constants.RESULT_OK )
						myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to redo the current sentence" );
					}

				adminListNr++;
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to redo the current sentence in my word list" );

		return CommonVariables.result;
		}

	private byte deleteUnusedWordTypes( boolean deleteAllActiveWordTypes, boolean deactiveItems )
		{
		ReadResultType readResult = new ReadResultType();
		short nReadWordReferences;
		String pluralNounString;
		WordTypeItem unusedWordTypeItem;
		WordTypeItem singularNounWordTypeItem;
		ReadItem previousReadItem = null;
		ReadItem unusedReadItem = ( deactiveItems ? admin_.firstDeactiveReadItem() : admin_.firstActiveReadItem() );
		WordItem unusedReadWordItem;

		while( CommonVariables.result == Constants.RESULT_OK &&
		unusedReadItem != null )
			{
			if( !unusedReadItem.isReadWordSingularNoun() &&		// Don't delete singular noun read words

			( deactiveItems ||
			deleteAllActiveWordTypes ||

			( previousReadItem != null &&
			previousReadItem.wordPosition() == unusedReadItem.wordPosition() ) ) )
				{
				if( ( unusedReadWordItem = unusedReadItem.readWordItem() ) != null )	// Skip text
					{
					if( ( unusedWordTypeItem = unusedReadItem.readWordTypeItem() ) != null )
						{
						if( unusedWordTypeItem.hasCurrentCreationSentenceNr() )
							{
							if( unusedReadItem.isReadWordPluralNoun() &&		// Wrong assumption: This noun is not plural but singular
							unusedReadWordItem.isUserDefinedWord() &&
							( pluralNounString = unusedWordTypeItem.itemString() ) != null &&
							( singularNounWordTypeItem = unusedReadWordItem.activeWordTypeItem( Constants.WORD_TYPE_NOUN_SINGULAR ) ) != null )
								{
								if( singularNounWordTypeItem.createNewWordTypeString( pluralNounString ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create a new word string for a singular noun word type of an active read word" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( admin_.readList != null )
									{
									if( ( readResult = admin_.readList.numberOfReadWordReferences( unusedReadItem.wordTypeNr(), unusedReadWordItem ) ).result == Constants.RESULT_OK )
										{
										if( ( nReadWordReferences = readResult.nReadWordReferences ) >= 1 )
											{
											unusedReadItem.isUnusedReadItem = true;

											if( nReadWordReferences == 1 )
												{
												if( unusedReadWordItem.deleteWordType( unusedReadItem.wordTypeNr() ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete an unused word type item" );
												}
											}
										else
											return myWord_.setErrorInItem( 1, moduleNameString_, "I found an invalid number of read word references" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the number of read word references" );
									}
								else
									return myWord_.setErrorInItem( 1, moduleNameString_, "The read list isn't created yet" );
								}
							}
						}
					else
						return myWord_.setErrorInItem( 1, moduleNameString_, "I couldn't find the word type of an active read word" );
					}
				}

			previousReadItem = unusedReadItem;
			unusedReadItem = unusedReadItem.nextReadItem();
			}

		return CommonVariables.result;
		}

	private byte deleteUnusedWordTypes( boolean deleteAllActiveWordTypes )
		{
		if( deleteUnusedWordTypes( deleteAllActiveWordTypes, false ) == Constants.RESULT_OK )		// From active read items
			{
			if( deleteUnusedWordTypes( deleteAllActiveWordTypes, true ) != Constants.RESULT_OK )	// From deactive read items
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the active unused word types" );
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the deactive unused word types" );

		return CommonVariables.result;
		}

	private byte removeFirstRangeOfDeletedItems()
		{
		short adminListNr = 0;

		CommonVariables.nDeletedItems = 0;
		CommonVariables.removeSentenceNr = Constants.NO_SENTENCE_NR;
		CommonVariables.removeStartItemNr = Constants.NO_ITEM_NR;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.removeFirstRangeOfDeletedItemsInWordList() != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove the first deleted items in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS &&
		CommonVariables.nDeletedItems == 0 )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].removeFirstRangeOfDeletedItemsInList() != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to remove the first deleted items" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte decrementSentenceNrs( int startSentenceNr )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.decrementSentenceNrsInWordList( startSentenceNr ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to decrement the sentence numbers from the current sentence number in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].decrementSentenceNrsInList( startSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to decrement the sentence numbers from the current sentence number in one of my lists" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	private byte decrementCurrentSentenceNr()
		{
		int myFirstSentenceNr = admin_.myFirstSentenceNr();

		if( CommonVariables.currentSentenceNr > Constants.NO_SENTENCE_NR )
			{
			if( CommonVariables.currentSentenceNr - 1 >= myFirstSentenceNr )		// Is my sentence
				{
				if( getHighestInUseSentenceNr( false, false, --CommonVariables.currentSentenceNr ) == Constants.RESULT_OK )
					{
					if( CommonVariables.highestInUseSentenceNr < CommonVariables.currentSentenceNr )
						CommonVariables.currentSentenceNr = ( CommonVariables.highestInUseSentenceNr > myFirstSentenceNr ? CommonVariables.highestInUseSentenceNr : myFirstSentenceNr );

					// Necessary after decrement of current sentence number
					if( currentItemNr() != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the current item number after decrementation" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if this sentence is empty" );
				}
			else
				dontIncrementCurrentSentenceNr_ = true;
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The current sentence number is undefined" );

		return CommonVariables.result;
		}

	private byte decrementItemNrRange( int decrementSentenceNr, int startDecrementItemNr, int decrementOffset )
		{
		short adminListNr = 0;

		if( CommonVariables.currentSentenceNr == decrementSentenceNr &&
		CommonVariables.currentItemNr > startDecrementItemNr )
			CommonVariables.currentItemNr -= decrementOffset;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.decrementItemNrRangeInWordList( decrementSentenceNr, startDecrementItemNr, decrementOffset ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to decrement item number range in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].decrementItemNrRangeInList( decrementSentenceNr, startDecrementItemNr, decrementOffset ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to decrement item number range" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}


	// Constructor

	protected AdminCleanup( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		dontIncrementCurrentSentenceNr_ = false;
		foundChange_ = false;
		wasUndoOrRedo_ = false;

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

	protected void clearDontIncrementCurrentSentenceNr()
		{
		dontIncrementCurrentSentenceNr_ = false;
		}

	protected boolean dontIncrementCurrentSentenceNr()
		{
		return dontIncrementCurrentSentenceNr_;
		}

	protected boolean foundChange()
		{
		return foundChange_;
		}

	protected boolean wasUndoOrRedo()
		{
		return wasUndoOrRedo_;
		}

	protected int highestSentenceNr()
		{
		short adminListNr = 0;
		int tempSentenceNr;
		int highestSentenceNr = Constants.NO_SENTENCE_NR;

		if( admin_.wordList != null )						// Inside words
			highestSentenceNr = admin_.wordList.highestSentenceNrInWordList();

		while( adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null &&	// Inside admin lists
			( tempSentenceNr = admin_.adminList[adminListNr].highestSentenceNrInList() ) > highestSentenceNr )
				highestSentenceNr = tempSentenceNr;

			adminListNr++;
			}

		return highestSentenceNr;
		}

	protected byte checkForChanges()
		{
		foundChange_ = true;

		if( getHighestInUseSentenceNr( false, false, CommonVariables.currentSentenceNr ) == Constants.RESULT_OK )
			{
			if( CommonVariables.highestInUseSentenceNr < CommonVariables.currentSentenceNr )
				foundChange_ = false;
			else
				clearDontIncrementCurrentSentenceNr();		// Found new knowledge
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find out if the sentence is empty" );

		return CommonVariables.result;
		}

	protected byte cleanupDeletedItems()
		{
		int previousRemoveSentenceNr = Constants.NO_SENTENCE_NR;

//		if( !admin_.isSystemStartingUp() )
//			Presentation.showStatus( Constants.INTERFACE_CONSOLE_I_AM_CLEANING_UP_DELETED_ITEMS );

		do	{
			if( removeFirstRangeOfDeletedItems() == Constants.RESULT_OK )
				{
				if( previousRemoveSentenceNr > Constants.NO_SENTENCE_NR &&
				previousRemoveSentenceNr != CommonVariables.removeSentenceNr )		// Previous deleted sentence is may be empty
					{
					if( getHighestInUseSentenceNr( true, true, previousRemoveSentenceNr ) == Constants.RESULT_OK )
						{
						if( CommonVariables.highestInUseSentenceNr < previousRemoveSentenceNr )	// All items of this sentence are deleted
							{																// So, decrement all higher sentence numbers
							if( decrementSentenceNrs( previousRemoveSentenceNr ) == Constants.RESULT_OK )
								{
								if( CommonVariables.removeSentenceNr > previousRemoveSentenceNr )
									CommonVariables.removeSentenceNr--;

								if( CommonVariables.currentSentenceNr >= previousRemoveSentenceNr )
									{
									if( decrementCurrentSentenceNr() != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to decrement the current sentence number" );
									}
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to decrement the sentence numbers from the current sentence number" );
							}
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check if the deleted sentence is empty" );
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				CommonVariables.nDeletedItems > 0 )
					{
					if( decrementItemNrRange( CommonVariables.removeSentenceNr, CommonVariables.removeStartItemNr, CommonVariables.nDeletedItems ) == Constants.RESULT_OK )
						previousRemoveSentenceNr = CommonVariables.removeSentenceNr;
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to decrement item number range" );
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to remove the first deleted items" );
			}
		while( CommonVariables.result == Constants.RESULT_OK &&
		CommonVariables.nDeletedItems > 0 );

//		Presentation.clearStatus();

		return CommonVariables.result;
		}

	protected byte currentItemNr()
		{
		short adminListNr = 0;

		CommonVariables.currentItemNr = Constants.NO_ITEM_NR;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.getCurrentItemNrInWordList() != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the current item number of my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].getCurrentItemNrInList() != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to get the current item number" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	protected byte deleteRollbackInfo()
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.deleteRollbackInfoInWordList() != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the rollback info of my words in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].deleteRollbackInfoInList() != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to delete the rollback info in my lists" );
				}

			adminListNr++;
			}

		return CommonVariables.result;
		}

	protected byte deleteAllTemporaryLists()
		{
		wasUndoOrRedo_ = false;

		admin_.deleteReadList();			// Read list is a temporary list
		admin_.deleteScoreList();			// Score list is a temporary list
		deleteResponseListsInAllWords();	// Response lists are temporary lists

		if( cleanupDeletedItems() != Constants.RESULT_OK )
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to cleanup the deleted items" );

		return CommonVariables.result;
		}

	protected byte deleteUnusedInterpretations( boolean deleteAllActiveWordTypes )
		{
		short adminListNr;
		int previousWordPosition = 0;
		WordItem unusedWordItem;
		ReadItem unusedReadItem = admin_.firstActiveReadItem();

		if( admin_.readList != null )
			{
			if( admin_.wordList != null )
				{
				if( deleteUnusedWordTypes( deleteAllActiveWordTypes ) == Constants.RESULT_OK )	// From active read items
					{
					while( CommonVariables.result == Constants.RESULT_OK &&
					unusedReadItem != null )
						{
						if( unusedReadItem.isUnusedReadItem ||
						unusedReadItem.wordPosition() == previousWordPosition )
							{
							previousWordPosition = unusedReadItem.wordPosition();

							if( admin_.readList.deleteActiveItem( false, unusedReadItem ) == Constants.RESULT_OK )
								{
								if( ( unusedWordItem = unusedReadItem.readWordItem() ) != null )	// Skip text
									{
									if( !unusedWordItem.hasItems() &&
									unusedWordItem.hasCurrentCreationSentenceNr() )
										{
										adminListNr = 0;
										CommonVariables.foundWordReference = false;

										while( CommonVariables.result == Constants.RESULT_OK &&
										!CommonVariables.foundWordReference &&
										adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
											{
											if( admin_.adminList[adminListNr] != null )
												{
												// Check the selection lists for a reference to this word
												if( admin_.adminList[adminListNr].findWordReference( unusedWordItem ) != Constants.RESULT_OK )
													myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to find a reference to an active word \"" + unusedWordItem.anyWordTypeString() + "\" in one of my lists" );
												}

											adminListNr++;
											}

										if( CommonVariables.result == Constants.RESULT_OK &&
										!CommonVariables.foundWordReference )
											{
											if( admin_.wordList.deleteActiveItem( false, unusedWordItem ) != Constants.RESULT_OK )
												myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete a word item" );
											}
										}
									}

								unusedReadItem = admin_.readList.nextReadListItem();
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete an active item" );
							}
						else
							{
							previousWordPosition = unusedReadItem.wordPosition();
							unusedReadItem = unusedReadItem.nextReadItem();
							}
						}

					while( CommonVariables.result == Constants.RESULT_OK &&
					( unusedReadItem = admin_.firstDeactiveReadItem() ) != null )
						{
						if( admin_.readList.deleteDeactiveItem( false, unusedReadItem ) == Constants.RESULT_OK )
							{
							unusedWordItem = unusedReadItem.readWordItem();

							if( unusedWordItem != null &&
							!unusedWordItem.hasItems() &&
							unusedWordItem.hasCurrentCreationSentenceNr() )
								{
								adminListNr = 0;
								CommonVariables.foundWordReference = false;

								while( CommonVariables.result == Constants.RESULT_OK &&
								!CommonVariables.foundWordReference &&
								adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
									{
									if( admin_.adminList[adminListNr] != null )
										{
										// Check my lists for a reference to this word
										if( admin_.adminList[adminListNr].findWordReference( unusedWordItem ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to find a reference to a deactive word \"" + unusedWordItem.anyWordTypeString() + "\" in one of my lists" );
										}

									adminListNr++;
									}

								if( CommonVariables.result == Constants.RESULT_OK &&
								!CommonVariables.foundWordReference )
									{
									if( admin_.wordList.deleteActiveItem( false, unusedWordItem ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete a word item" );
									}
								}
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete an unused (deactive) read word" );
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the unused word types" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The word list isn't created yet" );
			}

		return CommonVariables.result;
		}

	protected byte deleteSentences( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		short adminListNr = 0;

		if( admin_.wordList != null )						// Inside words
			{
			if( admin_.wordList.deleteSentencesInWordList( isAvailableForRollback, lowestSentenceNr ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete sentences in my word list" );
			}

		while( CommonVariables.result == Constants.RESULT_OK &&
		adminListNr < Constants.NUMBER_OF_ADMIN_LISTS )
			{
			if( admin_.adminList[adminListNr] != null )	// Inside admin lists
				{
				if( admin_.adminList[adminListNr].deleteSentencesInList( isAvailableForRollback, lowestSentenceNr ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( admin_.adminListChar( adminListNr ), 1, moduleNameString_, "I failed to delete sentences in a list" );
				}

			adminListNr++;
			}

		if( CommonVariables.result == Constants.RESULT_OK )
			{
			if( cleanupDeletedItems() != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to cleanup the deleted items" );
			}

		return CommonVariables.result;
		}

	protected byte undoLastSentence()
		{
		boolean skipUndo = false;

		if( CommonVariables.currentSentenceNr > Constants.NO_SENTENCE_NR )
			{
			if( deleteAllTemporaryLists() == Constants.RESULT_OK )			// Delete read words of the undo sentence
				{
				if( CommonVariables.currentSentenceNr > highestSentenceNr() )
					{
					if( CommonVariables.currentSentenceNr - 1 >= admin_.myFirstSentenceNr() )		// Is my sentence
						{
						CommonVariables.currentSentenceNr--;

						if( currentItemNr() != Constants.RESULT_OK )			// Necessary after decrement of current sentence number
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the current item number after decrementation" );
						}
					else
						{
						if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_UNDO_SENTENCE_OF_ANOTHER_USER ) == Constants.RESULT_OK )
							{
							skipUndo = true;
							dontIncrementCurrentSentenceNr_ = true;
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification about another user" );
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				!skipUndo )
					{
					if( rollbackDeletedRedoInfo() == Constants.RESULT_OK )	// Roll-back Undo sentence. Handle it as a command, not as a sentence
						{
						if( undoCurrentSentence() == Constants.RESULT_OK )
							{
							if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_I_HAVE_UNDONE_SENTENCE_NR, CommonVariables.currentSentenceNr, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_SENTENCE_NR_END ) == Constants.RESULT_OK )
								{
								wasUndoOrRedo_ = true;
								dontIncrementCurrentSentenceNr_ = true;
								admin_.dontShowConclusions();
								}
							else
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification about having undone a sentence" );
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to undo the current sentence" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to rollback the deleted redo info" );
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete all temporary lists" );
			}
		else
			{
			if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_NO_SENTENCES_LEFT_TO_UNDO ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification about no sentences left to undo" );
			}

		return CommonVariables.result;
		}

	protected byte redoLastUndoneSentence()
		{
		if( deleteAllTemporaryLists() == Constants.RESULT_OK )	// Delete read words of the undo sentence
			{
			if( !dontIncrementCurrentSentenceNr_ &&
			CommonVariables.currentSentenceNr > highestSentenceNr() )
				{
				if( rollbackDeletedRedoInfo() == Constants.RESULT_OK )
					{
					if( redoCurrentSentence() == Constants.RESULT_OK )
						{
						if( Presentation.writeInterfaceText( Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_I_HAVE_REDONE_SENTENCE_NR, CommonVariables.currentSentenceNr, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_SENTENCE_NR_END ) == Constants.RESULT_OK )
							{
							wasUndoOrRedo_ = true;
							admin_.dontShowConclusions();
							}
						else
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification about having redone a sentence" );
						}
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to redo the current sentence" );
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to rollback the deleted redo info" );
				}
			else
				{
				if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_IMPERATIVE_NOTIFICATION_NO_SENTENCES_TO_REDO ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification about no sentences to redo" );
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete all temporary lists" );

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"How great is the Lord,
 *	how deserving of praise,
 *	in the city of our God,
 *	which sits on his holy mountain!" (Psalm 48:1)
 *
 *************************************************************************/
