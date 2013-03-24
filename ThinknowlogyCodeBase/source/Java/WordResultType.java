/*
 *	Class:			WordResultType
 *	Purpose:		To return word type variables of a method
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

class WordResultType
	{
	// Protected variables

	protected byte result;
	protected boolean isWordAlreadyWritten;
	protected WordItem createdWordItem;
	protected WordItem foundWordItem;
	protected WordTypeItem foundWordTypeItem;

	// Constructor

	WordResultType()
		{
		result = Constants.RESULT_OK;
		isWordAlreadyWritten = false;
		createdWordItem = null;
		foundWordItem = null;
		foundWordTypeItem = null;
		}
	};

/*************************************************************************
 *
 *	"I take joy in doing your will, my God,
 *	for your instruction are written on my heart. (Psalm 40:8)
 *
 *************************************************************************/
