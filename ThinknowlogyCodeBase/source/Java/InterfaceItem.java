/*
 *	Class:			InterfaceItem
 *	Parent class:	Item
 *	Purpose:		To store info about the interface messages
 *					in a certain language that can be shown to the user
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

class InterfaceItem extends Item
	{
	// Private loadable variables

	private short interfaceParameter_;

	private String interfaceString_;


	// Protected constructible variables


	// Constructor

	protected InterfaceItem( short interfaceParameter, int interfaceStringLength, String interfaceString, List myList, WordItem myWord )
		{
		initializeItemVariables( Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, myList, myWord );

		// Private loadable variables

		interfaceParameter_ = interfaceParameter;
		interfaceString_ = null;

		// Protected constructible variables

		if( interfaceString != null )
			{
			if( ( interfaceString_ = new String( interfaceString.substring( 0, interfaceStringLength ) ) ) == null )
				setSystemErrorInItem( 1, null, null, "I failed to create the interface string" );
			}
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();

		if( interfaceString_ != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar() );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( interfaceString_ );
			}
		}

	protected boolean checkParameter( int queryParameter )
		{
		return ( interfaceParameter_ == queryParameter ||

				( queryParameter == Constants.MAX_QUERY_PARAMETER &&
				interfaceParameter_ > Constants.NO_INTERFACE_PARAMETER ) );
		}

	protected String itemString()
		{
		return interfaceString_;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		baseToStringBuffer( queryWordTypeNr );

		if( interfaceParameter_ > Constants.NO_INTERFACE_PARAMETER )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "interfaceParameter:" + interfaceParameter_ );

			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "interfaceString:" + interfaceString_ );

		return CommonVariables.queryStringBuffer;
		}


	// Protected methods

	protected short interfaceParameter()
		{
		return interfaceParameter_;
		}

	protected String interfaceString()
		{
		return interfaceString_;
		}

	protected InterfaceItem nextInterfaceItem()
		{
		return (InterfaceItem)nextItem;
		}
	};

/*************************************************************************
 *
 *	"O Lord, what are human beings that you should notice them,
 *	mere mortals that you should think about them?" (Psalm 144:3)
 *
 *************************************************************************/
