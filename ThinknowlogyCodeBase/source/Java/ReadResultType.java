/*
 *	Class:			ReadResultType
 *	Purpose:		To return read word variables of a method
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

class ReadResultType
	{
	// Protected variables

	protected byte result;

	protected boolean foundMatchingWordType;
	protected boolean foundMoreInterpretations;
	protected boolean hasCreatedAllReadWords;
	protected boolean isGuidedByGrammarCanceled;

	protected short nReadWordReferences;

	protected int currentParseWordPosition;
	protected int startWordPosition;
	protected int nextWordPosition;
	protected int wordLength;

	protected ReadItem failedWordReadItem;

	// Constructor

	ReadResultType()
		{
		result = Constants.RESULT_OK;

		foundMatchingWordType = false;
		foundMoreInterpretations = false;
		hasCreatedAllReadWords = false;
		isGuidedByGrammarCanceled = false;

		nReadWordReferences = 0;

		currentParseWordPosition = 0;
		startWordPosition = 0;
		nextWordPosition = 0;
		wordLength = 0;

		failedWordReadItem = null;
		}
	};

/*************************************************************************
 *
 *	"As a deer longs for streams of water,
 *	so I long for you, O God.
 *	I thirst for God, the living God.
 *	When can I go and stand before him?" (Psalm 42:1-2)
 *
 *************************************************************************/
