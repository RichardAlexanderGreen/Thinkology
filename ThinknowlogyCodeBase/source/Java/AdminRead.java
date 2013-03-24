/*
 *	Class:			AdminRead
 *	Supports class:	AdminItem
 *	Purpose:		To handle text files
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


class AdminRead
	{
	// Private constructible variables

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;


	// Private methods

	private boolean isGrammarChar( char grammarChar )
		{
		return ( grammarChar == Constants.QUERY_WORD_TYPE_CHAR ||
				grammarChar == Constants.QUERY_PARAMETER_CHAR ||
				grammarChar == Constants.GRAMMAR_WORD_DEFINITION_CHAR );
		}

	private boolean showLine()
		{
		if( !admin_.isSystemStartingUp() &&
		admin_.fileList != null )
			return admin_.fileList.showLine();

		return false;
		}

	private FileResultType openFile( boolean addSubPath, boolean isInfoFile, boolean reportErrorIfFileDoesNotExist, String defaultSubpathString, String fileNameString )
		{
		FileResultType fileResult = new FileResultType();

		if( admin_.fileList != null )
			return admin_.fileList.openFile( addSubPath, isInfoFile, reportErrorIfFileDoesNotExist, defaultSubpathString, fileNameString );

		fileResult.result = myWord_.setErrorInItem( 1, moduleNameString_, "The file list isn't created yet" );
		return fileResult;
		}

	private byte readLanguageFile( boolean isGrammarLanguage, String languageNameString )
		{
		FileResultType fileResult = new FileResultType();
		byte originalResult;
		int position = 0;
		FileItem openedLanguageFileItem;

		if( languageNameString != null )
			{
			while( position < languageNameString.length() &&
			( languageNameString.charAt( position ) == Constants.SYMBOL_SLASH ||
			languageNameString.charAt( position ) == Constants.SYMBOL_BACK_SLASH ) )	// Skip directory
				position++;

			if( ( fileResult = openFile( true, false, false, ( isGrammarLanguage ? Constants.GRAMMAR_DIRECTORY_NAME_STRING : Constants.INTERFACE_DIRECTORY_NAME_STRING ), languageNameString ) ).result == Constants.RESULT_OK )
				{
				if( ( openedLanguageFileItem = fileResult.createdFileItem ) != null )
					{
					if( isGrammarLanguage )
						{
						if( admin_.createGrammarLanguage( languageNameString ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create grammar language: \"" + languageNameString + "\"" );
						}
					else
						{
						if( admin_.createInterfaceLanguage( languageNameString ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to create interface language: \"" + languageNameString + "\"" );
						}

					if( CommonVariables.result == Constants.RESULT_OK )
						{
						if( readAndExecute() != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read and execute the opened language file" );

						originalResult = CommonVariables.result;

						if( closeCurrentFileItem( openedLanguageFileItem ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to close the language file item" );

						if( originalResult != Constants.RESULT_OK )
							CommonVariables.result = originalResult;
						}
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to open the grammar file" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given language name is undefined" );

		return CommonVariables.result;
		}

	private byte executeLine( StringBuffer readStringBuffer )
		{
		byte originalResult;
		int previousSentenceNr = Constants.MAX_SENTENCE_NR;

		if( readStringBuffer != null )
			{
			if( readStringBuffer.length() > 0 &&	// Skip empty line
			readStringBuffer.charAt( 0 ) != Constants.COMMENT_CHAR )	// and comment line
				{
				CommonVariables.hasShownMessage = false;
				CommonVariables.isAssignmentChanged = false;


				if( admin_.cleanupDeletedItems() == Constants.RESULT_OK )
					{
					previousSentenceNr = CommonVariables.currentSentenceNr;

					if( readStringBuffer.charAt( 0 ) == Constants.QUERY_CHAR )					// Guided-by-grammar, grammar/interface language or query
						{
						if( readStringBuffer.length() == 1 )				// Guided-by-grammar
							{
							if( admin_.readSentence( null ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a sentence" );
							}
						else
							{
							if( Character.isLetter( readStringBuffer.charAt( 1 ) ) )			// Grammar/interface language
								{
								if( admin_.isSystemStartingUp() )			// Read grammar and/or interface language file
									{
									// First try to read the user-interface language file
									if( readLanguageFile( false, readStringBuffer.substring( 1 ) ) == Constants.RESULT_OK )
										{
										// Now, try to read the grammar language file
										if( readLanguageFile( true, readStringBuffer.substring( 1 ) ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a grammar file" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read an interface file" );
									}
								else										// Change interface language
									{
									if( admin_.assignInterfaceLanguage( readStringBuffer.substring( 1 ) ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the interface language" );
									}
								}
							else
								{
								if( readStringBuffer.charAt( 0 ) == Constants.QUERY_CHAR )		// Query
									{
									admin_.initializeQueryStringPosition();

									if( admin_.executeQuery( false, true, true, Constants.PRESENTATION_PROMPT_QUERY, readStringBuffer.toString() ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute query: \"" + readStringBuffer + "\"" );
									}
								}
							}
						}
					else													// Sentence or grammar definition
						{
						if( CommonVariables.currentSentenceNr < Constants.MAX_SENTENCE_NR )
							{
							if( admin_.dontIncrementCurrentSentenceNr() )
								{
								admin_.clearDontIncrementCurrentSentenceNr();
								previousSentenceNr--;	// No increment is done. So, the previous sentence number should be one less
								}
							else
								{
								if( CommonVariables.currentSentenceNr + 1 >= admin_.myFirstSentenceNr() )		// Is my sentence
									{
									CommonVariables.currentSentenceNr++;

									if( admin_.currentItemNr() != Constants.RESULT_OK )	// Necessary after increment of current sentence number
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to get the current item number" );
									}
								else
									return myWord_.setSystemErrorInItem( 1, moduleNameString_, "Integrity violation! The next sentence is already used by another user" );
								}

							if( CommonVariables.result == Constants.RESULT_OK &&
							CommonVariables.currentSentenceNr > Constants.NO_SENTENCE_NR &&
							CommonVariables.currentSentenceNr <= admin_.highestSentenceNr() )
								{
								if( admin_.deleteSentences( true, CommonVariables.currentSentenceNr ) != Constants.RESULT_OK )
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the current redo info" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( isGrammarChar( readStringBuffer.charAt( 0 ) ) )	// Grammar definition
									{
									if( admin_.addGrammar( readStringBuffer.toString() ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add grammar: \"" + readStringBuffer + "\"" );
									}
								else										// Sentence
									{
									if( admin_.readSentence( readStringBuffer.toString() ) != Constants.RESULT_OK )
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a sentence" );
									}
								}
							}
						else
							return myWord_.setSystemErrorInItem( 1, moduleNameString_, "Sentence number overflow! I cannot except anymore input" );
						}

					if( CommonVariables.result != Constants.RESULT_SYSTEM_ERROR &&
					!admin_.isRestart() )
						{
						if( CommonVariables.result == Constants.RESULT_OK &&
						admin_.foundChange() &&

						( admin_.wasUndoOrRedo() ||		// Execute selections after Undo or Redo
						!CommonVariables.hasShownMessage ) &&
						!CommonVariables.hasShownWarning )
							{
							if( admin_.executeSelections() != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute selections after reading the sentence" );
							}

						if( CommonVariables.result == Constants.RESULT_OK &&
						!admin_.foundChange() &&
						!admin_.isSystemStartingUp() &&
						!CommonVariables.hasShownMessage &&
						!CommonVariables.hasShownWarning )
							{
							if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_SENTENCE_NOTIFICATION_I_KNOW ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( admin_.deleteAllTemporaryLists() != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete all temporary lists" );
							}

						if( ( CommonVariables.hasShownWarning ||
						CommonVariables.result != Constants.RESULT_OK ) &&

						CommonVariables.currentSentenceNr == previousSentenceNr + 1 )	// This sentence has items
							{
							originalResult = CommonVariables.result;					// Remember original result
							CommonVariables.result = Constants.RESULT_OK;						// Clear current result

							// Delete current redo info. After this, 'Redo' isn't possible
							if( admin_.deleteSentences( false, CommonVariables.currentSentenceNr ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to delete the current redo information" );

							if( originalResult != Constants.RESULT_OK )
								CommonVariables.result = originalResult;				// Restore original result
							}
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to cleanup the deleted items" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given read string buffer is undefined" );

		return CommonVariables.result;
		}

	private byte closeCurrentFileItem( FileItem closeFileItem )
		{
		if( admin_.fileList != null )
			{
			if( admin_.fileList.closeCurrentFile( closeFileItem ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to close a file" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The file list isn't created yet" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminRead( AdminItem admin, WordItem myWord )
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


	// Protected methods

	protected byte readStartupFile()
		{
		FileResultType fileResult = new FileResultType();
		byte originalResult;
		FileItem openedStartupFileItem;
		if( admin_.fileList == null )
			{
			if( ( admin_.fileList = new FileList( myWord_ ) ) != null )
				admin_.adminList[Constants.ADMIN_FILE_LIST] = admin_.fileList;
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "I failed to create an admin file list" );
			}

		if( ( fileResult = openFile( true, false, true, Constants.STARTUP_DIRECTORY_NAME_STRING, Constants.STARTUP_FILE_NAME_STRING ) ).result == Constants.RESULT_OK )
			{
			if( ( openedStartupFileItem = fileResult.createdFileItem ) != null )
				{
				if( readAndExecute() != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read and execute the opened startup file" );

				originalResult = CommonVariables.result;

				if( closeCurrentFileItem( openedStartupFileItem ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to close the startup file item" );

				if( originalResult != Constants.RESULT_OK )
					CommonVariables.result = originalResult;
				}
			}
		else
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to open an example file" );

		return CommonVariables.result;
		}

	protected byte readExamplesFile( String examplesFileNameString )
		{
		FileResultType fileResult = new FileResultType();
		byte originalResult;
		FileItem openedExampleFileItem;
		if( examplesFileNameString != null )
			{
			if( ( fileResult = openFile( !admin_.isSystemStartingUp(), false, true, Constants.EXAMPLES_DIRECTORY_NAME_STRING, examplesFileNameString ) ).result == Constants.RESULT_OK )
				{
				if( ( openedExampleFileItem = fileResult.createdFileItem ) != null )
					{
					if( readAndExecute() != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read and execute the opened example file" );

					originalResult = CommonVariables.result;

					if( closeCurrentFileItem( openedExampleFileItem ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to close the example file item" );

					if( originalResult != Constants.RESULT_OK )
						CommonVariables.result = originalResult;
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to open an example file" );
			}
		else
			{
			if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_WARNING, Constants.INTERFACE_IMPERATIVE_WARNING_I_DONT_KNOW_WHICH_FILE_TO_READ ) != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface warning" );
			}

		return CommonVariables.result;
		}

	protected byte readAndExecute()
		{
		boolean isLineExecuted;
		StringBuffer readStringBuffer = new StringBuffer();

		do	{
			isLineExecuted = false;
			readStringBuffer = new StringBuffer();

			if( readLine( false, false, false, false, ( admin_.dontIncrementCurrentSentenceNr() ? CommonVariables.currentSentenceNr : CommonVariables.currentSentenceNr + 1 ), admin_.currentUserName(), readStringBuffer ) == Constants.RESULT_OK )
				{
				if( Presentation.hasReadNewLine() )
					{
					if( executeLine( readStringBuffer ) == Constants.RESULT_OK )
						isLineExecuted = true;
					else
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to execute the read line" );
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a line" );
			}
		while( CommonVariables.result == Constants.RESULT_OK &&
		isLineExecuted &&
		!admin_.isRestart() &&
		!CommonVariables.hasShownWarning );

		if( admin_.isSystemStartingUp() &&
		CommonVariables.hasShownWarning )
			CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;

		return CommonVariables.result;
		}

	protected byte readLine( boolean clearInputField, boolean isPassword, boolean isQuestion, boolean isText, int promptSentenceNr, String promptUserNameString, StringBuffer readStringBuffer )
		{
		StringBuffer promptStringBuffer = new StringBuffer();

		if( promptSentenceNr > Constants.NO_SENTENCE_NR )
			promptStringBuffer.append( promptSentenceNr );

		if( promptUserNameString != null )
			{
			if( promptStringBuffer.length() > 0 )
				promptStringBuffer.append( Constants.QUERY_SEPARATOR_STRING );

			promptStringBuffer.append( promptUserNameString );
			}

		if( Presentation.readLine( clearInputField, isPassword, isQuestion, isText, showLine(), promptStringBuffer.toString(), readStringBuffer, ( admin_.fileList == null ? null : admin_.fileList.currentReadFile() ) ) != Constants.RESULT_OK )
			myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read a line from a file or from input" );

		return CommonVariables.result;
		}

	protected FileResultType readInfoFile( boolean reportErrorIfFileDoesNotExist, String infoFileNameString )
		{
		FileResultType fileResult = new FileResultType();
		byte originalResult;
		FileItem openedInfoFileItem;
		StringBuffer infoPathStringBuffer = new StringBuffer( Constants.INFO_DIRECTORY_NAME_STRING );

		if( infoFileNameString != null )
			{
			if( CommonVariables.currentGrammarLanguageWordItem != null )
				infoPathStringBuffer.append( CommonVariables.currentGrammarLanguageWordItem.anyWordTypeString() + Constants.SYMBOL_SLASH );

			if( ( fileResult = openFile( true, true, reportErrorIfFileDoesNotExist, infoPathStringBuffer.toString(), infoFileNameString ) ).result == Constants.RESULT_OK )
				{
				if( ( openedInfoFileItem = fileResult.createdFileItem ) != null )
					{
					if( readAndExecute() != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read and execute the opened info file" );

					originalResult = CommonVariables.result;

					if( closeCurrentFileItem( openedInfoFileItem ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to close the info file item" );

					if( originalResult != Constants.RESULT_OK )
						CommonVariables.result = originalResult;
					}
				}
			else
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to open the info file" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given info file name string is undefined" );

		fileResult.result = CommonVariables.result;
		return fileResult;
		}
	};

/*************************************************************************
 *
 *	"Shout to the Lord, all the earth;
 *	break out in praise and sing for joy!" (Psalm 98:4)
 *
 *************************************************************************/
