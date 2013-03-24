/*
 *	Class:			GrammarResultType
 *	Purpose:		To return justification variables of a method
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

class GrammarResultType
	{
	// Protected variables

	protected byte result;
	protected boolean foundDuplicateGrammar;
	protected GrammarItem foundGrammarItem;
	protected GrammarItem createdGrammarItem;

	// Constructor

	GrammarResultType()
		{
		result = Constants.RESULT_OK;
		foundDuplicateGrammar = false;
		foundGrammarItem = null;
		createdGrammarItem = null;
		}
	};

/*************************************************************************
 *
 *	"Sing praises to God, sing praises;
 *	sing praises to our King, sing praises.
 *	For God is the King over all the earth.
 *	Praise him with a psalm!" (Psalm 47:6-7)
 *
 *************************************************************************/
