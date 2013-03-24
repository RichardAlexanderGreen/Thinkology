/*
 *	Class:			FileItem
 *	Parent class:	Item
 *	Purpose:		To store info about the opened files
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

class FileItem extends Item
	{
	// Private loadable variables

	private boolean isInfoFile_;

	private String readFileNameString_;

	private BufferedReader readFile_;

	// Constructor

	protected FileItem( boolean isInfoFile, String readFileNameString, BufferedReader readFile, List myList, WordItem myWord )
		{

		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		isInfoFile_ = isInfoFile;

		readFileNameString_ = null;
		readFile_ = readFile;

		if( readFile_ != null )
			{
			if( readFileNameString != null )
				{
				if( ( readFileNameString_ = new String( readFileNameString ) ) == null )
					setSystemErrorInItem( 1, null, null, "I failed to create the read file name string" );
				}

			}
		else
			setSystemErrorInItem( 1, null, null, "The given read file is undefined" );
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( readFileNameString_ != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( readFileNameString_ );
			}
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// Descending itemNr
				itemNr() > nextSortItem.itemNr() );
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		baseToStringBuffer( queryWordTypeNr );

		if( isInfoFile_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isInfoFile" );

		if( readFileNameString_ != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "readFileNameString:" + readFileNameString_ );


		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected boolean isInfoFile()
		{
		return isInfoFile_;
		}

	protected String readFileNameString()
		{
		return readFileNameString_;
		}

	protected BufferedReader readFile()
		{
		return readFile_;
		}

	protected FileItem nextFileItem()
		{
		return (FileItem)nextItem;
		}
	};

/*************************************************************************
 *
 *	"He spoke, and the winds rose,
 *	stirring up the waves." (Psalm 107:25)
 *
 *************************************************************************/
