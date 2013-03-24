/*
 *	Class:			FileList
 *	Parent class:	List
 *	Purpose:		To store file items
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

class FileList extends List
	{
	private boolean fileNameContainsExtension( String fileNameString )
		{
		int position;

		if( fileNameString != null )
			{
			position = fileNameString.length();

			while( position > 0 &&
			fileNameString.charAt( position - 1 ) != Constants.SYMBOL_SLASH &&
			fileNameString.charAt( position - 1 ) != Constants.SYMBOL_BACK_SLASH )
				{
				if( fileNameString.charAt( --position ) == Constants.SYMBOL_COLON )
					return true;
				}
			}

		return false;
		}

	private FileResultType createFileItem( boolean isInfoFile, String readFileNameString, BufferedReader readFile )
		{
		FileResultType fileResult = new FileResultType();

		if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
			{
			if( ( fileResult.createdFileItem = new FileItem( isInfoFile, readFileNameString, readFile, this, myWord() ) ) != null )
				{
				if( addItemToActiveList( (Item)fileResult.createdFileItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add a file item" );
				}
			else
				setError( 1, null, "I failed to create a file item" );
			}
		else
			setError( 1, null, "The current item number is undefined" );

		fileResult.result = CommonVariables.result;
		return fileResult;
		}

	private FileItem firstActiveFileItem()
		{
		return (FileItem)firstActiveItem();
		}


	// Constructor

	protected FileList( WordItem myWord )
		{
		initializeListVariables( Constants.ADMIN_FILE_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected boolean showLine()
		{
		FileItem searchItem = firstActiveFileItem();

		while( searchItem != null )
			{
			if( searchItem.isInfoFile() )
				return false;

			searchItem = searchItem.nextFileItem();
			}

		return true;
		}

	protected byte closeCurrentFile( FileItem closeFileItem )
		{
		FileItem currentFileItem = firstActiveFileItem();

		if( currentFileItem != null )
			{
			if( currentFileItem == closeFileItem )	// Check to be sure to close the right file
				{
				try	{
					if( currentFileItem.readFile() != null )
						currentFileItem.readFile().close();
					}
				catch( IOException exception )
					{
					return setError( 1, null, "I couldn't close read file: \"" + currentFileItem.readFileNameString() + "\"" );
					}

				if( deleteActiveItem( false, currentFileItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to delete a file item" );
				}
			else
				return setError( 1, null, "The given file item isn't the current file" );
			}
		else
			return setError( 1, null, "There is no file to close" );

		return CommonVariables.result;
		}

	protected FileResultType openFile( boolean addSubPath, boolean isInfoFile, boolean reportErrorIfFileDoesNotExist, String defaultSubPathString, String openFileNameString )
		{
		FileResultType fileResult = new FileResultType();
		BufferedReader readFile = null;
		InputStream inputStream = null;
//		StringBuffer fileNameStringBuffer = new StringBuffer();
		StringBuffer readFileNameWithinJarStringBuffer = new StringBuffer();
		StringBuffer readFileNameFromFileSystemStringBuffer = new StringBuffer();

		if( openFileNameString != null )
			{
			if( defaultSubPathString != null )
				{
				if( openFileNameString.length() > 0 )
					{
					if( openFileNameString.charAt( 0 ) != Constants.SYMBOL_SLASH &&		// Skip absolute path
					openFileNameString.charAt( 0 ) != Constants.SYMBOL_BACK_SLASH &&
					openFileNameString.indexOf( Constants.DOUBLE_COLON_STRING ) < 0 )
						{
						readFileNameWithinJarStringBuffer.append( Constants.SYMBOL_SLASH );
						readFileNameFromFileSystemStringBuffer.append( CommonVariables.currentPathStringBuffer );

						if( addSubPath &&
						openFileNameString.indexOf( defaultSubPathString ) < 0 )		// File name doesn't contains sub-path
							{
							readFileNameWithinJarStringBuffer.append( defaultSubPathString );
							readFileNameFromFileSystemStringBuffer.append( defaultSubPathString );
							}
						}

					readFileNameWithinJarStringBuffer.append( openFileNameString );
					readFileNameFromFileSystemStringBuffer.append( openFileNameString );

					if( !fileNameContainsExtension( openFileNameString.toString() ) )
						{
						readFileNameWithinJarStringBuffer.append( Constants.FILE_EXTENSION_STRING );
						readFileNameFromFileSystemStringBuffer.append( Constants.FILE_EXTENSION_STRING );
						}

					try	{
						// First try to open the file within the Jar
						inputStream = getClass().getResourceAsStream( readFileNameWithinJarStringBuffer.toString() );

						// If not exists, try to open the file from a file system
						readFile = new BufferedReader( inputStream == null ? new FileReader( readFileNameFromFileSystemStringBuffer.toString() ) : new InputStreamReader( inputStream ) );

						if( ( fileResult = createFileItem( isInfoFile, ( inputStream == null ? readFileNameFromFileSystemStringBuffer.toString() : readFileNameWithinJarStringBuffer.toString() ), readFile ) ).result != Constants.RESULT_OK )
							{
							if( fileResult.createdFileItem != null )
								closeCurrentFile( fileResult.createdFileItem );

							addError( 1, null, "I failed to create a file item" );
							}
						}
					catch( IOException exception )
						{
						if( reportErrorIfFileDoesNotExist )
							setError( 1, null, "Either you are trying to start this Jar file still packed in a Zip file, or I couldn't open file for reading: \"" + ( inputStream == null ? readFileNameFromFileSystemStringBuffer : readFileNameWithinJarStringBuffer ) + "\"" );
						}
					}
				else
					setError( 1, null, "The copied file name string buffer is empty" );
				}
			else
				setError( 1, null, "The given default subpath string is undefined" );
			}
		else
			setError( 1, null, "The given open file name string is undefined" );

		fileResult.result = CommonVariables.result;
		return fileResult;
		}

	protected String currentReadFileNameString()
		{
		FileItem currentFileItem = firstActiveFileItem();
		return ( currentFileItem == null ? null : currentFileItem.readFileNameString() );
		}

	protected BufferedReader currentReadFile()
		{
		FileItem currentFileItem = firstActiveFileItem();
		return ( currentFileItem == null ? null : currentFileItem.readFile() );
		}
	};

/*************************************************************************
 *
 *	"Praise the Lord, for the Lord is good;
 *	celebrate his lovely name with music." (Psalm 135:3)
 *
 *************************************************************************/
