/*
 *	Class:			SpecificationResultType
 *	Purpose:		To return specification item variables of a method
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

class SpecificationResultType
	{
	// Protected variables

	protected byte result;

	protected boolean isFirstRelatedSpecification;
	protected boolean isRelatedQuestion;
	protected boolean isSameQuestion;
	protected boolean isSimilarQuestion;

	protected short assignmentOrderNr;
	protected short assignmentParameter;
	protected short assumptionLevel;
	protected short combinedAssumptionLevel;

	protected SpecificationItem adjustedQuestionSpecificationItem;
	protected SpecificationItem archiveAssignmentItem;
	protected SpecificationItem createdSpecificationItem;
	protected SpecificationItem foundSpecificationItem;
	protected SpecificationItem relatedSpecificationItem;

	protected WordItem compoundGeneralizationWordItem;

	// Constructor

	SpecificationResultType()
		{
		result = Constants.RESULT_OK;

		isFirstRelatedSpecification = false;
		isRelatedQuestion = false;
		isSameQuestion = false;
		isSimilarQuestion = false;

		assignmentOrderNr = Constants.NO_ORDER_NR;
		assignmentParameter = Constants.NO_ASSIGNMENT_PARAMETER;
		assumptionLevel = Constants.NO_ASSIGNMENT_LEVEL;
		combinedAssumptionLevel = Constants.NO_ASSIGNMENT_LEVEL;

		adjustedQuestionSpecificationItem = null;
		archiveAssignmentItem = null;
		createdSpecificationItem = null;
		foundSpecificationItem = null;
		relatedSpecificationItem = null;

		compoundGeneralizationWordItem = null;
		}
	};

/*************************************************************************
 *
 *	"Even when I walk
 *	through the darkest valley,
 *	I will not be afraid,
 *	for you are close beside me.
 *	Your rod and your staff
 *	protect and conform me." (Psalm 23:4)
 *
 *************************************************************************/
