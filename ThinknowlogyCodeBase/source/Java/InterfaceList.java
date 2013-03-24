/*
 *	Class:			InterfaceList
 *	Parent class:	List
 *	Purpose:		To store interface items
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

class InterfaceList extends List
	{
	// Constructor

	protected InterfaceList( WordItem myWord )
		{
		initializeListVariables( Constants.WORD_INTERFACE_LANGUAGE_LIST_SYMBOL, myWord );
		}


	// Protected methods

	protected byte checkInterface( short interfaceParameter, String interfaceString )
		{
		InterfaceItem searchItem = firstActiveInterfaceItem();

		if( interfaceString != null )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.interfaceParameter() != interfaceParameter )
					{
					if( searchItem.interfaceString() != null )
						{
						if( compareStrings( interfaceString, searchItem.interfaceString() ) == Constants.RESULT_OK )
							{
							if( !CommonVariables.foundMatchingStrings )
								searchItem = searchItem.nextInterfaceItem();
							else
								return setError( 1, null, "The given interface string already exists" );
							}
						else
							addError( 1, null, "I failed to compare two interface strings" );
						}
					else
						return setError( 1, null, "I found an undefined interface string" );
					}
				else
					return setError( 1, null, "The given interface parameter already exists" );
				}
			}
		else
			return setError( 1, null, "The given interface string is undefined" );

		return CommonVariables.result;
		}

	protected byte createInterfaceItem( short interfaceParameter, int interfaceStringLength, String interfaceString )
		{
		InterfaceItem newItem;

		if( CommonVariables.currentItemNr < Constants.MAX_ITEM_NR )
			{
			if( ( newItem = new InterfaceItem( interfaceParameter, interfaceStringLength, interfaceString, this, myWord() ) ) != null )
				{
				if( addItemToActiveList( (Item)newItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add an interface item" );
				}
			else
				return setError( 1, null, "I failed to create an interface item" );
			}
		else
			return setError( 1, null, "The current item number is undefined" );

		return CommonVariables.result;
		}

	protected InterfaceItem firstActiveInterfaceItem()
		{
		return (InterfaceItem)firstActiveItem();
		}

	protected String interfaceString( short interfaceParameter )
		{
		InterfaceItem searchItem = firstActiveInterfaceItem();

		while( searchItem != null )
			{
			if( searchItem.interfaceParameter() == interfaceParameter )
				return searchItem.interfaceString();

			searchItem = searchItem.nextInterfaceItem();
			}

		return Constants.INTERFACE_STRING_NOT_AVAILABLE;
		}
	};

/*************************************************************************
 *
 *	"I will exalt you, my God and King,
 *	and praise your name forever and ever." (Psalm 145:1)
 *
 *************************************************************************/
