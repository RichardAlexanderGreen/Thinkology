/*
 *	Class:			AdminAuthorization
 *	Supports class:	AdminItem
 *	Purpose:		To handle authorization
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

class AdminAuthorization
	{
	// Private constructible variables

	private boolean userHasPasswordAssigned_;
	private boolean userEnteredCorrectPassword_;

	private short foundUserNr_;

	private int loginSentenceNr_;

	private SpecificationItem passwordAssignmentItem_;

	private WordItem currentUserWordItem_;
	private WordItem foundUserWordItem_;

	private AdminItem admin_;
	private WordItem myWord_;
	private String moduleNameString_;
	private String authorizationKey_;


	// Private methods

	private byte findUserWord( String userNameString )
		{
		WordResultType wordResult = new WordResultType();
		GeneralizationItem currentGeneralizationItem;
		WordItem currentGeneralizationWordItem;

		foundUserNr_ = Constants.NO_USER_NR;
		foundUserWordItem_ = null;

		if( userNameString != null )
			{
			if( CommonVariables.predefinedNounUserWordItem != null )
				{
				if( ( currentGeneralizationItem = CommonVariables.predefinedNounUserWordItem.firstActiveGeneralizationItemOfSpecification() ) != null )
					{
					do	{
						if( ( currentGeneralizationWordItem = currentGeneralizationItem.generalizationWordItem() ) != null )
							{
							if( currentGeneralizationItem.isGeneralizationPropername() )
								{
								if( ( wordResult = currentGeneralizationWordItem.findWordType( true, false, Constants.WORD_TYPE_PROPER_NAME, userNameString.length(), userNameString ) ).result == Constants.RESULT_OK )
									{
									if( wordResult.foundWordTypeItem != null )
										{
										if( ( foundUserNr_ = currentGeneralizationWordItem.collectionOrderNrByWordTypeNr( Constants.WORD_TYPE_PROPER_NAME ) ) == Constants.NO_ORDER_NR )
											foundUserNr_ = 1;	// Only one user (not collected)

										foundUserWordItem_ = currentGeneralizationWordItem;
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the user name" );
								}
							}
						else
							return myWord_.setErrorInItem( 1, moduleNameString_, "I found an undefined generalization word" );
						}
					while( CommonVariables.result == Constants.RESULT_OK &&
					( currentGeneralizationItem = currentGeneralizationItem.nextGeneralizationItemOfSpecification() ) != null );
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined user noun word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given user name string is undefined" );

		return CommonVariables.result;
		}

	private byte checkUserForPasswordAssignment( WordItem userWordItem )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();
		WordItem predefinedNounPasswordWordItem;

		userHasPasswordAssigned_ = true;		// Fail safe
		passwordAssignmentItem_ = null;

		if( userWordItem != null )
			{
			if( ( predefinedNounPasswordWordItem = admin_.predefinedNounPasswordWordItem() ) != null )
				{
				if( ( specificationResult = predefinedNounPasswordWordItem.findAssignmentByRelationContext( false, false, false, false, Constants.NO_QUESTION_PARAMETER, userWordItem ) ).result == Constants.RESULT_OK )
					{
					if( ( passwordAssignmentItem_ = specificationResult.foundSpecificationItem ) == null )
						{
						// No assignment found. So, check for explict negative password specification
						if( userWordItem.firstAssignmentOrSpecificationButNotAQuestion( false, false, false, false, true, true, Constants.NO_COLLECTION_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, predefinedNounPasswordWordItem ) != null )
							userHasPasswordAssigned_ = false;
						}
					}
				else
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the password assignment" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined password noun word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given user word item is undefined" );

		return CommonVariables.result;
		}

	private byte checkPassword( String enteredPasswordString )
		{
		userEnteredCorrectPassword_ = false;		// Fail safe

		if( enteredPasswordString != null )
			{
			if( passwordAssignmentItem_ != null )
				{
				if( passwordAssignmentItem_.specificationWordItem() != null )
					{

					if( passwordAssignmentItem_.specificationWordItem().checkHiddenWordType( passwordAssignmentItem_.specificationWordTypeNr(), enteredPasswordString, authorizationKey_ ) )
						userEnteredCorrectPassword_ = true;
					}
				else
					return myWord_.setErrorInItem( 1, moduleNameString_, "The password assignment specification item is undefined" );
				}
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given password string is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected AdminAuthorization( AdminItem admin, WordItem myWord )
		{
		String errorString = null;

		userHasPasswordAssigned_ = true;		// Fail safe
		userEnteredCorrectPassword_ = false;	// Fail safe

		foundUserNr_ = Constants.NO_USER_NR;

		loginSentenceNr_ = Constants.NO_SENTENCE_NR;

		passwordAssignmentItem_ = null;
		currentUserWordItem_ = null;
		foundUserWordItem_ = null;

		admin_ = admin;
		myWord_ = myWord;
		moduleNameString_ = this.getClass().getName();
		authorizationKey_ = moduleNameString_;		// The address of the class is the key - not the content of the string

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

	protected int myFirstSentenceNr()
		{
		return ( loginSentenceNr_ + 1 );
		}

	protected byte authorizeWord( WordItem authorizationWordItem )
		{
		if( authorizationWordItem != null )
			{
			if( admin_.isSystemStartingUp() )
				{
				if( authorizationWordItem.assignChangePermissions( authorizationKey_ ) != Constants.RESULT_OK )
					myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign my authorization permissions to a word" );
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "You are not authorized to authorize the given word" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The given authorization word item is undefined" );

		return CommonVariables.result;
		}

	protected byte login( WordItem specificationWordItem )
		{
		WordItem predefinedNounPasswordWordItem;
		StringBuffer readUserNameStringBuffer = new StringBuffer();
		StringBuffer readPasswordStringBuffer = new StringBuffer();

		userHasPasswordAssigned_ = true;		// Fail safe
		userEnteredCorrectPassword_ = false;	// Fail safe
		passwordAssignmentItem_ = null;
		foundUserWordItem_ = null;

		if( CommonVariables.predefinedNounUserWordItem != null )
			{
			if( ( predefinedNounPasswordWordItem = admin_.predefinedNounPasswordWordItem() ) != null )
				{
				if( specificationWordItem == null )		// No user name is given
					{
					if( admin_.getUserInput( false, true, false, CommonVariables.predefinedNounUserWordItem.activeSingularNounString(), readUserNameStringBuffer ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read to user name" );
					}
				else
					readUserNameStringBuffer.append( specificationWordItem.anyWordTypeString() );

				if( CommonVariables.result == Constants.RESULT_OK &&
				readUserNameStringBuffer.length() > 0 )
					{
					if( findUserWord( readUserNameStringBuffer.toString() ) != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to find the user" );
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( foundUserWordItem_ != null &&
					foundUserWordItem_ == currentUserWordItem_ )
						{
						if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_CONSOLE_ALREADY_LOGGED_IN_START, readUserNameStringBuffer.toString(), Constants.INTERFACE_CONSOLE_ALREADY_LOGGED_IN_END ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
						}
					else
						{
						if( foundUserWordItem_ != null )
							{
							if( checkUserForPasswordAssignment( foundUserWordItem_ ) != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the user for password assignment" );
							}

						if( CommonVariables.result == Constants.RESULT_OK )
							{
							if( foundUserWordItem_ != null &&
							!userHasPasswordAssigned_ )
								userEnteredCorrectPassword_ = true;		// User has no password
							else
								{
								if( admin_.getUserInput( true, true, false, predefinedNounPasswordWordItem.activeSingularNounString(), readPasswordStringBuffer ) == Constants.RESULT_OK )
									{
									if( foundUserWordItem_ != null )	// Only check password if user was found
										{
										if( checkPassword( readPasswordStringBuffer.toString() ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to check the user and password" );
										}
									}
								else
									myWord_.addErrorInItem( 1, moduleNameString_, "I failed to read to password" );
								}

							if( CommonVariables.result == Constants.RESULT_OK )
								{
								if( userEnteredCorrectPassword_ &&
								foundUserWordItem_ != null )
									{
									if( assignSpecificationWithAuthorization( false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_CONTEXT_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, Constants.NO_SENTENCE_NR, 0, null, foundUserWordItem_, CommonVariables.predefinedNounUserWordItem, null ).result == Constants.RESULT_OK )
										{
										currentUserWordItem_ = foundUserWordItem_;
										loginSentenceNr_ = CommonVariables.currentSentenceNr;
										CommonVariables.myFirstSentenceNr = loginSentenceNr_;
										CommonVariables.currentUserNr = foundUserNr_;

										if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_CONSOLE_LOGGED_IN_START, readUserNameStringBuffer.toString(), Constants.INTERFACE_CONSOLE_LOGGED_IN_END ) == Constants.RESULT_OK )
											{
											}
										else
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
										}
									else
										myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign the user" );
									}
								else
									{
									if( !admin_.isSystemStartingUp() )
										{
										if( Presentation.writeInterfaceText( false, Constants.PRESENTATION_PROMPT_NOTIFICATION, Constants.INTERFACE_CONSOLE_LOGIN_FAILED ) != Constants.RESULT_OK )
											myWord_.addErrorInItem( 1, moduleNameString_, "I failed to write an interface notification" );
										}
									else
										return myWord_.setSystemErrorInItem( 1, moduleNameString_, "The user name or it's password isn't correct" );
									}
								}
							}
						}
					}
				}
			else
				return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined password noun word item is undefined" );
			}
		else
			return myWord_.setErrorInItem( 1, moduleNameString_, "The predefined user noun word item is undefined" );

		return CommonVariables.result;
		}

	protected SpecificationResultType addSpecificationWithAuthorization( boolean isAssignment, boolean isConditional, boolean isDeactiveAssignment, boolean isArchiveAssignment, boolean isExclusive, boolean isNegative, boolean isPossessive, boolean isSelection, boolean isSpecificationGeneralization, boolean isValueSpecification, short prepositionParameter, short questionParameter, short generalizationWordTypeNr, short specificationWordTypeNr, short relationWordTypeNr, int generalizationCollectionNr, int specificationCollectionNr, int relationCollectionNr, int generalizationContextNr, int specificationContextNr, int relationContextNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, WordItem relationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( specificationWordItem != null )
				{
				if( generalizationWordItem.isNounPassword() ||
				specificationWordItem.isNounDeveloper() ||
				specificationWordItem.isNounUser() ||
				generalizationWordItem.isVerbImperativeLogin() )
					{
					if( generalizationWordItem.isNounPassword() )
						{
						if( specificationWordItem.hideWordType( specificationWordTypeNr, authorizationKey_ ) != Constants.RESULT_OK )
							myWord_.addErrorInItem( 1, moduleNameString_, "I failed to hide a password" );
						}
					else
						{
						if( !isNegative &&
						specificationWordItem.isNounUser() &&
						questionParameter == Constants.NO_QUESTION_PARAMETER &&
						admin_.predefinedVerbLoginWordItem() != null )		// Create a login for this user
							{
							if( ( specificationResult = admin_.predefinedVerbLoginWordItem().addSpecificationInWord( false, false, false, false, false, false, false, false, false, false, Constants.NO_PREPOSITION_PARAMETER, Constants.NO_QUESTION_PARAMETER, Constants.WORD_TYPE_VERB_SINGULAR, generalizationWordTypeNr, Constants.WORD_TYPE_UNDEFINED, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, Constants.NO_CONTEXT_NR, generalizationContextNr, Constants.NO_CONTEXT_NR, nContextRelations, specificationJustificationItem, generalizationWordItem, null, null, authorizationKey_ ) ).result != Constants.RESULT_OK )
								myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification with authorization" );
							}
						}
					}

				if( CommonVariables.result == Constants.RESULT_OK )
					{
					if( ( specificationResult = generalizationWordItem.addSpecificationInWord( isAssignment, isConditional, isDeactiveAssignment, isArchiveAssignment, isExclusive, isNegative, isPossessive, isSelection, isSpecificationGeneralization, isValueSpecification, prepositionParameter, questionParameter, generalizationWordTypeNr, specificationWordTypeNr, relationWordTypeNr, generalizationCollectionNr, specificationCollectionNr, relationCollectionNr, generalizationContextNr, specificationContextNr, relationContextNr, nContextRelations, specificationJustificationItem, specificationWordItem, relationWordItem, specificationString, authorizationKey_ ) ).result != Constants.RESULT_OK )
						myWord_.addErrorInItem( 1, moduleNameString_, "I failed to add a specification with authorization" );
					}
				}
			else
				myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected SpecificationResultType assignSpecificationWithAuthorization( boolean isAmbiguousRelationContext, boolean isAssignedOrClear, boolean isDeactive, boolean isArchive, boolean isNegative, boolean isPossessive, boolean isSelfGenerated, short prepositionParameter, short questionParameter, int generalizationContextNr, int specificationContextNr, int relationContextNr, int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, int nContextRelations, JustificationItem specificationJustificationItem, WordItem generalizationWordItem, WordItem specificationWordItem, String specificationString )
		{
		SpecificationResultType specificationResult = new SpecificationResultType();

		if( generalizationWordItem != null )
			{
			if( ( specificationResult = generalizationWordItem.assignSpecificationInWord( isAmbiguousRelationContext, isAssignedOrClear, isDeactive, isArchive, isNegative, isPossessive, isSelfGenerated, prepositionParameter, questionParameter, generalizationContextNr, specificationContextNr, relationContextNr, originalSentenceNr, activeSentenceNr, deactiveSentenceNr, archiveSentenceNr, nContextRelations, specificationJustificationItem, specificationWordItem, specificationString, authorizationKey_ ) ).result != Constants.RESULT_OK )
				myWord_.addErrorInItem( 1, moduleNameString_, "I failed to assign a specification with authorization" );
			}
		else
			myWord_.setErrorInItem( 1, moduleNameString_, "The given generalization word item is undefined" );

		specificationResult.result = CommonVariables.result;
		return specificationResult;
		}

	protected String currentUserName()
		{
		return ( currentUserWordItem_ == null ? null : currentUserWordItem_.anyWordTypeString() );
		}
	};

/*************************************************************************
 *
 *	"We thank you, O God!
 *	We give thanks because you are near.
 *	People everywhere tell your wonderful deeds." (Psalm 75:1)
 *
 *************************************************************************/
