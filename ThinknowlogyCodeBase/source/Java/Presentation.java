/*
 *	Class:		Presentation
 *	Purpose:	To format the information presented to the user
 *	Version:	Thinknowlogy 2012 (release 2)
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

import java.io.BufferedReader;
import java.io.IOException;

class Presentation
	{
	// Private static variables

	private static boolean hasReadNewLine_;
	private static boolean isFirstTimeAfterReadingFile_;
	private static boolean showExtraPromptLine_;

	private static short lastUsedPromptTypeNr_;
	private static short lastShownInterfaceParameter_;

	private static int currentPosition_;
	private static int previousProgress_;

	private static StringBuffer outputStringBuffer_;
	private static StringBuffer currentStatusStringBuffer_;

	private static String classNameString_ = "Presentation";


	// Private static methods

	private static void returnOutputToPosition( boolean write )
		{
		addStringToOutput( write, Constants.CARRIAGE_RETURN_NEW_LINE_STRING );
		currentPosition_ = 0;
		}

	private static void addStringToOutput( boolean write, String writeString )
		{
		outputStringBuffer_.append( writeString );

		if( write &&
		outputStringBuffer_.length() > 0 )
			{
				Console.writeText( outputStringBuffer_.toString() );
			outputStringBuffer_ = new StringBuffer();
			}
		}

	private static void showStatus( boolean showCurrentItemId, String newStatusString )
		{
		if( newStatusString == null )
			clearStatus();
		else
			{
			if( currentStatusStringBuffer_ == null ||
			currentStatusStringBuffer_.indexOf( newStatusString ) == -1 )	// Different string
				{
				currentStatusStringBuffer_ = new StringBuffer( newStatusString + ( showCurrentItemId ? "  " + Constants.QUERY_ITEM_START_STRING + CommonVariables.currentSentenceNr + ( CommonVariables.currentItemNr == Constants.NO_SENTENCE_NR ? Constants.EMPTY_STRING : Constants.QUERY_SEPARATOR_STRING + CommonVariables.currentItemNr ) + Constants.QUERY_ITEM_END_CHAR : ( newStatusString.endsWith( "." ) ? Constants.EMPTY_STRING : "..." ) ) );
				Console.showStatus( currentStatusStringBuffer_.toString() );
				}
			}
		}

	private static void showStatus( boolean showCurrentItemId, short interfaceParameter )
		{
		showStatus( showCurrentItemId, ( CommonVariables.currentInterfaceLanguageWordItem == null ? Constants.NO_INTERFACE_LANGUAGE_AVAILABLE : CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter ) ) );
		}

	private static byte writeText( boolean isFirstCharacterToUpperCase, boolean returnToPosition, short promptTypeNr, int leftWidth, int rightWidth, int printStringLength, String promptString, String writeString )
		{
		boolean startNewLine = false;
		int wordPosition;
		int i = 0;
		int length = 0;
		String textString;

		if( writeString != null )
			{
			if( printStringLength > 0 ||
			leftWidth > Constants.NO_CENTER_WIDTH ||
			rightWidth > Constants.NO_CENTER_WIDTH )
				{
				if( currentPosition_ > 0 &&

				( returnToPosition ||
				isFirstTimeAfterReadingFile_ ) )
					returnOutputToPosition( false );

				isFirstTimeAfterReadingFile_ = false;

				if( isFirstCharacterToUpperCase &&
				printStringLength > 0 &&
				Character.isLetter( writeString.charAt( 0 ) ) &&

				( !returnToPosition ||
				currentPosition_ == 0 ) &&

				( promptTypeNr == Constants.PRESENTATION_PROMPT_WRITE ||
				promptTypeNr == Constants.PRESENTATION_PROMPT_WARNING ) )
					textString = new String( Character.toUpperCase( writeString.charAt( 0 ) ) + writeString.substring( 1, printStringLength ) );
				else
					textString = new String( writeString.substring( 0, printStringLength ) );

				if( promptString == null )
					promptString = getPromptString( promptTypeNr );

				while( i < printStringLength ||
				leftWidth > Constants.NO_CENTER_WIDTH )
					{
					if( currentPosition_ == 0 )
						{
						addStringToOutput( false, promptString );
						currentPosition_ = promptString.length() - 1;
						}

					if( i < printStringLength )
						{
						if( textString.charAt( i ) == Constants.TAB_CHAR )
							length = ( Constants.TAB_LENGTH - ( currentPosition_ + 1 ) % Constants.TAB_LENGTH );
						else
							{
							length = 0;
							wordPosition = i;

							do	{
								i++;
								length++;
								}
							while( i < printStringLength &&
							!Character.isWhitespace( textString.charAt( i ) ) );

							if( i < printStringLength &&
							( textString.charAt( i ) == Constants.NEW_LINE_CHAR ||
							textString.charAt( i ) == Constants.CARRIAGE_RETURN_CHAR ) )
								length--;

							i = wordPosition;
							}

						if( i < printStringLength &&
						( textString.charAt( i ) == Constants.NEW_LINE_CHAR ||
						textString.charAt( i ) == Constants.CARRIAGE_RETURN_CHAR ) )
							{
							startNewLine = true;
							addStringToOutput( ( i + 1 == printStringLength ), Constants.CARRIAGE_RETURN_NEW_LINE_STRING );

							i++;
							currentPosition_ = 0;
							}
						}

					if( startNewLine )
						startNewLine = false;
					else
						{
						while( leftWidth > Constants.NO_CENTER_WIDTH )
							{
							leftWidth--;
							currentPosition_++;
							addStringToOutput( false, Constants.SPACE_STRING );
							}

						if( i < printStringLength )
							{
							if( textString.charAt( i ) == Constants.TAB_CHAR )
								{
								addStringToOutput( ( i + 1 == printStringLength ), Constants.EMPTY_STRING + textString.charAt( i ) );

								i++;
								currentPosition_ += length;
								}
							else
								{
								do	{
									addStringToOutput( ( i + 1 == printStringLength ), Constants.EMPTY_STRING + textString.charAt( i ) );

									if( textString.charAt( i ) == Constants.NEW_LINE_CHAR ||
									textString.charAt( i ) == Constants.CARRIAGE_RETURN_CHAR )
										currentPosition_ = 0;
									else
										currentPosition_++;

									i++;
									}
								while( length-- > 1 );
								}
							}
						}
					}

				while( rightWidth-- > Constants.NO_CENTER_WIDTH )
					{
					currentPosition_++;
					addStringToOutput( ( ( i + 1 == printStringLength ) && rightWidth == Constants.NO_CENTER_WIDTH ), Constants.SPACE_STRING );
					}
				}
			else
				{
				showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given write string is empty" );
				CommonVariables.result = Constants.RESULT_ERROR;
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given write string is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	private static byte writeText( boolean isFirstCharacterToUpperCase, boolean returnToPosition, short promptTypeNr, int queryWidth, String promptString, String textString )
		{
		int widthDifference;
		int leftWidth = Constants.NO_CENTER_WIDTH;
		int rightWidth = Constants.NO_CENTER_WIDTH;
		int textStringLength;

		if( textString != null )
			{
			if( textString.length() > 0 ||
			queryWidth > Constants.NO_CENTER_WIDTH )
				{
				clearStatus();
				clearProgress();
				textStringLength = textString.length();

				if( promptTypeNr == Constants.PRESENTATION_PROMPT_WARNING )
					CommonVariables.hasShownWarning = true;
				else
					CommonVariables.hasShownMessage = true;

				if( queryWidth > 0 &&
				queryWidth < textStringLength )
					textStringLength = queryWidth;

				if( queryWidth >= textStringLength )
					{
					widthDifference = ( queryWidth - textStringLength );
					leftWidth = ( widthDifference / 2 );
					rightWidth = ( widthDifference - leftWidth );
					}

					if( writeText( isFirstCharacterToUpperCase, returnToPosition, promptTypeNr, leftWidth, rightWidth, textStringLength, promptString, textString ) == Constants.RESULT_OK )
					lastUsedPromptTypeNr_ = promptTypeNr;
				else
					showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "I failed to write the string" );
				}
			else
				{
				showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given text string is empty" );
				CommonVariables.result = Constants.RESULT_ERROR;
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given text string is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	private static String getPromptString( short promptTypeNr )
		{
		switch( promptTypeNr )
			{
			case Constants.PRESENTATION_PROMPT_READ:
				return Constants.PRESENTATION_PROMPT_READ_STRING;

			case Constants.PRESENTATION_PROMPT_WRITE:
				return Constants.PRESENTATION_PROMPT_WRITE_STRING;

			case Constants.PRESENTATION_PROMPT_INFO:
				return Constants.PRESENTATION_PROMPT_INFO_STRING;

			case Constants.PRESENTATION_PROMPT_NOTIFICATION:
				return Constants.PRESENTATION_PROMPT_NOTIFICATION_STRING;

			case Constants.PRESENTATION_PROMPT_WARNING:
				return Constants.PRESENTATION_PROMPT_WARNING_STRING;
			}

		return Constants.PRESENTATION_PROMPT_QUERY_STRING;
		}


	// Constructor init

	protected static void init()
		{
		hasReadNewLine_ = false;
		isFirstTimeAfterReadingFile_ = false;
		showExtraPromptLine_ = false;

		currentPosition_ = 0;
		previousProgress_ = 0;
		lastUsedPromptTypeNr_ = Constants.PRESENTATION_PROMPT_QUERY;

		outputStringBuffer_ = new StringBuffer();
		currentStatusStringBuffer_ = null;
		}

	// Protected methods

	protected static void clearStatus()
		{
		currentStatusStringBuffer_ = null;
		}

	protected static void clearProgress()
		{
		previousProgress_ = 0;
		Console.clearProgress();

		if( currentStatusStringBuffer_ != null )
			Console.showStatus( currentStatusStringBuffer_.toString() );
		}

	protected static void showStatus( short interfaceParameter )
		{
		showStatus( true, interfaceParameter );
		}

	protected static void showProgress( int currentProgress )
		{
		if( currentProgress != previousProgress_ )
			{
			previousProgress_ = currentProgress;
			Console.showProgress( currentProgress );
			}
		}

	protected static void startProgress( int startProgress, int maxProgress, short interfaceParameter1, short shortNumber, short interfaceParameter2 )
		{
		Console.startProgress( startProgress, maxProgress, ( CommonVariables.currentInterfaceLanguageWordItem == null ? Constants.NO_INTERFACE_LANGUAGE_AVAILABLE : CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + shortNumber + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + "  " + Constants.QUERY_ITEM_START_STRING + CommonVariables.currentSentenceNr + ( CommonVariables.currentItemNr == Constants.NO_SENTENCE_NR ? Constants.EMPTY_STRING : Constants.QUERY_SEPARATOR_STRING + CommonVariables.currentItemNr ) + Constants.QUERY_ITEM_END_CHAR ) );
		}

	protected static void showError( char methodListChar, String classNameString, String subclassNameString, String wordNameString, int methodLevel, String errorString )
		{
		StringBuffer errorStringBuffer = new StringBuffer();
		StackTraceElement[] elements;
		Throwable t = new Throwable();

		addStringToOutput( true, Constants.EMPTY_STRING );	// Show remaining output

		if( classNameString != null )
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_CLASS_STRING + classNameString );

		if( subclassNameString != null )
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_SUPERCLASS_STRING + subclassNameString );

		if( t != null &&
		( elements = t.getStackTrace() ) != null )
			{
			if( elements.length > 0 )
				{
				if( methodLevel < elements.length )		// elements.[0] is this method
					errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_STRING + elements[methodLevel].getMethodName() );
				else
					errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_STRING + "{Invalid method level}" );
				}
			else
				errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_STRING + "{No stack information available}" );
			}
		else
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_STRING + "{Failed to create Throwable or StackTrace}" );

		if( wordNameString != null )
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_WORD_START_STRING + wordNameString + Constants.PRESENTATION_ERROR_METHOD_WORD_END_STRING );

		if( methodListChar != Constants.SYMBOL_QUESTION_MARK )
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_METHOD_LIST_START_STRING + methodListChar + Constants.PRESENTATION_ERROR_METHOD_LIST_END_STRING );

		if( errorString != null )
			errorStringBuffer.append( Constants.PRESENTATION_ERROR_MESSAGE_START_STRING + errorString );

		errorStringBuffer.append( Constants.PRESENTATION_ERROR_MESSAGE_END_STRING );

		Console.addError( Constants.PRESENTATION_ERROR_CURRENT_ID_STRING + Constants.QUERY_ITEM_START_CHAR + CommonVariables.currentSentenceNr + Constants.QUERY_SEPARATOR_CHAR + CommonVariables.currentItemNr + Constants.QUERY_ITEM_END_CHAR, errorStringBuffer.toString() );
		}

	protected static boolean hasReadNewLine()
		{
		return hasReadNewLine_;
		}

	protected static char convertDiacriticalChar( char diacriticalChar )
		{
		switch( diacriticalChar )
			{
			case Constants.TEXT_TAB_CHAR:
				return Constants.TAB_CHAR;

			case Constants.TEXT_BELL_CHAR:
				return Constants.BELL_CHAR;

			case Constants.SYMBOL_DOUBLE_QUOTE:
				return Constants.SYMBOL_DOUBLE_QUOTE;

			case Constants.TEXT_BACK_SPACE_CHAR:
				return Constants.BACK_SPACE_CHAR;

			case Constants.TEXT_NEW_LINE_CHAR:
				return Constants.NEW_LINE_CHAR;

			case Constants.QUERY_CHAR:	// Avoid triggering of query
				return Constants.QUERY_CHAR;
			}

		return diacriticalChar;
		}

	protected static String convertDiacriticalText( String textString )
		{
		int position = 0;
		char textChar = Constants.SYMBOL_QUESTION_MARK;
		StringBuffer textStringBuffer = new StringBuffer();

		if( textString != null )
			{
			if( textString.charAt( 0 ) == Constants.SYMBOL_DOUBLE_QUOTE )
				position++;

			while( position < textString.length() &&
			textString.charAt( position ) != Constants.SYMBOL_DOUBLE_QUOTE )
				{
				if( textString.charAt( position ) == Constants.TEXT_DIACRITICAL_CHAR )
					{
					if( ++position < textString.length() )
						{
						textChar = convertDiacriticalChar( textString.charAt( position ) );
						textStringBuffer.append( Constants.EMPTY_STRING + textChar );
						}
					}
				else
					{
					textChar = textString.charAt( position );
					textStringBuffer.append( Constants.EMPTY_STRING + textChar );
					}

				position++;
				}
			}

		return textStringBuffer.toString();
		}

	protected static byte writeText( boolean returnToPosition, short promptTypeNr, int queryWidth, StringBuffer textStringBuffer )
		{
		if( textStringBuffer != null )
			return writeText( true, returnToPosition, promptTypeNr, queryWidth, null, textStringBuffer.toString() );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given text string buffer is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeDiacriticalText( short promptTypeNr, String diacriticalTextString )
		{
		return writeDiacriticalText( true, true, promptTypeNr, diacriticalTextString );
		}

	protected static byte writeDiacriticalText( boolean isFirstCharacterToUpperCase, boolean returnToPosition, short promptTypeNr, String textString )
		{
		boolean hasFoundNewLine = false;
		int position = 0;
		char textChar = Constants.SYMBOL_QUESTION_MARK;
		StringBuffer textStringBuffer = new StringBuffer();

		if( textString != null )
			{
			showExtraPromptLine_ = true;

			if( textString.charAt( 0 ) == Constants.SYMBOL_DOUBLE_QUOTE )
				position++;

			while( CommonVariables.result == Constants.RESULT_OK &&
			position < textString.length() &&
			textString.charAt( position ) != Constants.SYMBOL_DOUBLE_QUOTE )
				{
				if( textString.charAt( position ) == Constants.TEXT_DIACRITICAL_CHAR )
					{
					if( ++position < textString.length() )
						{
						if( ( textChar = convertDiacriticalChar( textString.charAt( position ) ) ) == Constants.NEW_LINE_CHAR )
							hasFoundNewLine = true;
						}
					else
						{
						showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The text string ended with a diacritical sign" );
						CommonVariables.result = Constants.RESULT_ERROR;
						}
					}
				else
					textChar = textString.charAt( position );

				position++;
				textStringBuffer.append( Constants.EMPTY_STRING + textChar );

				if( hasFoundNewLine ||

				( textStringBuffer.length() > 0 &&
				position < textString.length() &&
				textString.charAt( position ) != Constants.SYMBOL_DOUBLE_QUOTE &&
				textString.charAt( position ) == Constants.QUERY_CHAR ) )
					{
					if( writeText( isFirstCharacterToUpperCase, returnToPosition, promptTypeNr, Constants.NO_CENTER_WIDTH, null, textStringBuffer.toString() ) == Constants.RESULT_OK )
						{
						hasFoundNewLine = false;
						textStringBuffer = new StringBuffer();
						}
					else
						showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "I failed to write a character" );
					}
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			textStringBuffer.length() > 0 )
				{
				if( writeText( isFirstCharacterToUpperCase, returnToPosition, promptTypeNr, Constants.NO_CENTER_WIDTH, null, textStringBuffer.toString() ) != Constants.RESULT_OK )
					showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "I failed to write the last characters" );
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given text string is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( boolean checkForDuplicateInterfaceParameter, short promptTypeNr, short interfaceParameter )
		{
		return writeInterfaceText( checkForDuplicateInterfaceParameter, true, promptTypeNr, interfaceParameter );
		}

	protected static byte writeInterfaceText( boolean checkForDuplicateInterfaceParameter, boolean returnToPosition, short promptTypeNr, short interfaceParameter )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			{
			if( !checkForDuplicateInterfaceParameter ||
			interfaceParameter != lastShownInterfaceParameter_ )
				{
				lastShownInterfaceParameter_ = interfaceParameter;
				return writeDiacriticalText( true, returnToPosition, promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter ) );
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, int intNumber, short interfaceParameter2 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + intNumber + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, int intNumber1, short interfaceParameter2, int intNumber2, short interfaceParameter3 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + intNumber1 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + intNumber2 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, int intNumber, short interfaceParameter2, String textString, short interfaceParameter3 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + intNumber + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + textString + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, int intNumber1, short interfaceParameter2, int intNumber2, short interfaceParameter3, String textString, short interfaceParameter4 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + intNumber1 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + intNumber2 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) + textString + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter4 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( boolean checkForDuplicateInterfaceParameter, short promptTypeNr, short interfaceParameter1, String textString, short interfaceParameter2 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			{
			if( !checkForDuplicateInterfaceParameter ||
			interfaceParameter1 != lastShownInterfaceParameter_ )
				{
				lastShownInterfaceParameter_ = interfaceParameter1;
				return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + textString + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) );
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, String textString1, short interfaceParameter2, String textString2, short interfaceParameter3 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + textString1 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + textString2 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, String textString1, short interfaceParameter2, String textString2, short interfaceParameter3, String textString3, short interfaceParameter4 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + textString1 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + textString2 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) + textString3 + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter4 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte writeInterfaceText( short promptTypeNr, short interfaceParameter1, String textString, short interfaceParameter2, int intNumber, short interfaceParameter3 )
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null )
			return writeDiacriticalText( promptTypeNr, CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter1 ) + textString + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter2 ) + intNumber + CommonVariables.currentInterfaceLanguageWordItem.interfaceString( interfaceParameter3 ) );
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The current interface language word is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}

	protected static byte readLine( boolean clearInputField, boolean isPassword, boolean isQuestion, boolean isText, boolean showline, String promptString, StringBuffer readStringBuffer, BufferedReader readFile )
		{
		int endPosition;
		int startPosition = 0;
		String readString = new String();
		StringBuffer promptStringBuffer = new StringBuffer();

		hasReadNewLine_ = false;
		lastShownInterfaceParameter_ = Constants.NO_INTERFACE_PARAMETER;

		if( readStringBuffer != null )
			{
			if( isText ||
			isQuestion )
				promptStringBuffer.append( Constants.PRESENTATION_PROMPT_WRITE_STRING );

			if( promptString != null )
				{
				if( isQuestion &&
				promptString.length() > 0 &&
				Character.isLetter( promptString.charAt( 0 ) ) )
					promptStringBuffer.append( Character.toUpperCase( promptString.charAt( 0 ) ) + promptString.substring( 1 ) );
				else
					promptStringBuffer.append( promptString );
				}

			promptStringBuffer.append( isQuestion ? Constants.PRESENTATION_PROMPT_QUERY_STRING : ( isText ? Constants.PRESENTATION_PROMPT_ENTER_STRING : Constants.PRESENTATION_PROMPT_READ_STRING ) );

			if( currentPosition_ > 0 )
			returnOutputToPosition( true );

			if( showExtraPromptLine_ )
				{
					if( writeText( true, true, lastUsedPromptTypeNr_, Constants.NO_CENTER_WIDTH, null, Constants.NEW_LINE_STRING ) == Constants.RESULT_OK )
					showExtraPromptLine_ = false;
				else
					showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "I failed to write the line" );
				}

			if( CommonVariables.result == Constants.RESULT_OK )
				{
				if( isPassword ||
				readFile == null )
					{
					currentPosition_ = 0;

					if( isPassword )
						{
						showStatus( false, Constants.INTERFACE_CONSOLE_WAITING_FOR_SECURE_INPUT );
						addStringToOutput( true, promptStringBuffer.toString() + Constants.NEW_LINE_STRING );

						if( ( readString = Console.getPassword() ) != null )
							hasReadNewLine_ = true;
						else
							{
							showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The returned password string is undefined" );
							CommonVariables.result = Constants.RESULT_ERROR;
							}
						}
					else
						{
						showStatus( false, ( clearInputField ? Constants.INTERFACE_CONSOLE_WAITING_FOR_INPUT : Constants.INTERFACE_CONSOLE_START_MESSAGE ) );
						addStringToOutput( true, promptStringBuffer.toString() );

						if( ( readString = Console.readLine( clearInputField ) ) != null )
							hasReadNewLine_ = true;
						else
							{
							showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The returned read line string is undefined" );
							CommonVariables.result = Constants.RESULT_ERROR;
							}
						}
					}
				else
					{
					try	{
						if( ( readString = readFile.readLine() ) != null )
							{
							hasReadNewLine_ = true;

							// Strip Java comment from line
							if( readString.startsWith( Constants.PRESENTATION_STRIP_COMMENT_STRING ) )
								readString = new String( readString.substring( Constants.PRESENTATION_STRIP_COMMENT_STRING.length() ) );

							if( showline &&
							// Skip C++ comment line
							!readString.startsWith( Constants.PRESENTATION_SKIP_COMMENT_STRING ) )
								{
									if( writeText( true, true, Constants.PRESENTATION_PROMPT_READ, Constants.NO_CENTER_WIDTH, promptStringBuffer.toString(), ( readString.length() == 0 ? Constants.NEW_LINE_STRING : readString ) ) != Constants.RESULT_OK )
									showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "I failed to write the text" );
								}
							}
						}
					catch( IOException exception )
						{
						showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, exception.toString() );
						CommonVariables.result = Constants.RESULT_ERROR;
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK &&
				hasReadNewLine_ &&
				readString != null &&
				readString.length() > 0 )
					{
					isFirstTimeAfterReadingFile_ = true;

					while( readString.substring( startPosition ).length() > 0 &&
					Character.isWhitespace( readString.charAt( startPosition ) ) )
						startPosition++;

					endPosition = readString.length();

					while( endPosition > startPosition &&
					Character.isWhitespace( readString.charAt( endPosition - 1 ) ) )
						endPosition--;

					readStringBuffer.append( readString.substring( startPosition, endPosition ) );
					}
				}
			}
		else
			{
			showError( Constants.SYMBOL_QUESTION_MARK, classNameString_, null, null, 1, "The given read string buffer is undefined" );
			CommonVariables.result = Constants.RESULT_ERROR;
			}

		return CommonVariables.result;
		}
	};

	/*************************************************************************
	 *
	 *	"The voice of the Lord twists mighty oaks
	 *	and strips the forest bare.
	 *	In his temple everyone shouts, "Glory!"." (Psalm 29:9)
	 *
	 *************************************************************************/
