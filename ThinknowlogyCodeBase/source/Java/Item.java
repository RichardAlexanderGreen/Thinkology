/*
 *	Class:		Item
 *	Purpose:	Base class for the knowledge structure
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

class Item
	{
	// Private constructible variables
	private short userNr_;
	private int activeSentenceNr_;
	private int deactiveSentenceNr_;
	private int originalSentenceNr_;
	private int creationSentenceNr_;
	private int archiveSentenceNr_;
	private int deleteSentenceNr_;
	private int itemNr_;
	private char statusChar_;
	// Private loadable variables
	private List myList_;
	private WordItem myWord_;
	private String moduleNameString_;
	// Protected constructible variables
	protected boolean isAvailableForRollback;
	protected boolean isSelectedByQuery;
	protected boolean wasActiveBeforeArchiving;
	protected Item previousItem;
	protected Item nextItem;

	// Private methods

	private String myWordTypeString( short queryWordTypeNr )
		{
		String myWordString = null;

		if( myList_ != null &&
		!myList_.isAdminList() &&	// Don't show my word string when the item is in an admin list
		myWord_ != null &&
		( myWordString = myWord_.wordTypeString( queryWordTypeNr ) ) == null )
			myWordString = myWord_.anyWordTypeString();

		return myWordString;
		}


	// Constructor

	protected Item()
		{
		// Private constructible variables

		userNr_ = Constants.NO_USER_NR;

		originalSentenceNr_ = Constants.NO_SENTENCE_NR;
		creationSentenceNr_ = CommonVariables.currentSentenceNr;

		activeSentenceNr_ = Constants.NO_SENTENCE_NR;
		deactiveSentenceNr_ = Constants.NO_SENTENCE_NR;
		archiveSentenceNr_ = Constants.NO_SENTENCE_NR;
		deleteSentenceNr_ = Constants.NO_SENTENCE_NR;

		itemNr_ = Constants.NO_ITEM_NR;

		statusChar_ = Constants.QUERY_ACTIVE_CHAR;

		// Private loadable variables

		myList_ = null;
		myWord_ = null;
		moduleNameString_ = this.getClass().getName();

		// Protected constructible variables

		isAvailableForRollback = false;
		isSelectedByQuery = false;
		wasActiveBeforeArchiving = false;

		nextItem = null;
		previousItem = null;
		}


	// Protected error methods

	protected void addErrorInItem( int methodLevel, String moduleNameString, String errorString )
		{
		Presentation.showError( Constants.SYMBOL_QUESTION_MARK, ( moduleNameString == null ? this.getClass().getName() : moduleNameString ), ( moduleNameString == null ? this.getClass().getSuperclass().getName() : null ), null, ( methodLevel + 1 ), errorString );
		}

	protected void addErrorInItem( int methodLevel, String moduleNameString, String instanceNameString, String errorString )
		{
		Presentation.showError( Constants.SYMBOL_QUESTION_MARK, ( moduleNameString == null ? this.getClass().getName() : moduleNameString ), ( moduleNameString == null ? this.getClass().getSuperclass().getName() : null ), instanceNameString, ( methodLevel + 1 ), errorString );
		}

	protected void addErrorInItem( char listChar, int methodLevel, String moduleNameString, String errorString )
		{
		Presentation.showError( listChar, ( moduleNameString == null ? this.getClass().getName() : moduleNameString ), ( moduleNameString == null ? this.getClass().getSuperclass().getName() : null ), null, ( methodLevel + 1 ), errorString );
		}

	protected void addErrorInItem( char listChar, int methodLevel, String moduleNameString, String instanceNameString, String errorString )
		{
		Presentation.showError( listChar, ( moduleNameString == null ? this.getClass().getName() : moduleNameString ), ( moduleNameString == null ? this.getClass().getSuperclass().getName() : null ), instanceNameString, ( methodLevel + 1 ), errorString );
		}
	protected byte setErrorInItem( int methodLevel, String moduleNameString, String errorString )
		{
		addErrorInItem( ( methodLevel + 1 ), moduleNameString, null, errorString );

		CommonVariables.result = Constants.RESULT_ERROR;

		return Constants.RESULT_ERROR;
		}

	protected byte setErrorInItem( int methodLevel, String moduleNameString, String instanceNameString, String errorString )
		{
		addErrorInItem( ( methodLevel + 1 ), moduleNameString, instanceNameString, errorString );

		CommonVariables.result = Constants.RESULT_ERROR;

		return Constants.RESULT_ERROR;
		}



	protected byte setErrorInItem( char listChar, int methodLevel, String moduleNameString, String errorString )
		{
		addErrorInItem( listChar, ( methodLevel + 1 ), moduleNameString, errorString );

		CommonVariables.result = Constants.RESULT_ERROR;

		return Constants.RESULT_ERROR;
		}

	protected byte setErrorInItem( char listChar, int methodLevel, String moduleNameString, String instanceNameString, String errorString )
		{
		addErrorInItem( listChar, ( methodLevel + 1 ), moduleNameString, instanceNameString, errorString );

		CommonVariables.result = Constants.RESULT_ERROR;

		return Constants.RESULT_ERROR;
		}

	protected byte setSystemErrorInItem( int methodLevel, String moduleNameString, String errorString )
		{
		return setSystemErrorInItem( 1, moduleNameString, null, errorString );
		}

	protected byte setSystemErrorInItem( int methodLevel, String moduleNameString, String instanceNameString, String errorString )
		{
		char textChar;
		int errorStringPosition = 0;
		StringBuffer tempStringBuffer = new StringBuffer();

		while( errorStringPosition < errorString.length() )
			{
			if( errorString.charAt( errorStringPosition ) == Constants.TEXT_DIACRITICAL_CHAR )
				{
				errorStringPosition++;

				if( errorStringPosition < errorString.length() &&
				( textChar = Presentation.convertDiacriticalChar( errorString.charAt( errorStringPosition ) ) ) != Constants.NEW_LINE_CHAR )
					tempStringBuffer.append( textChar );
				}
			else
				tempStringBuffer.append( errorString.charAt( errorStringPosition ) );

			errorStringPosition++;
			}

		addErrorInItem( ( methodLevel + 1 ), moduleNameString, instanceNameString, tempStringBuffer.toString() );

		CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;

		return Constants.RESULT_SYSTEM_ERROR;
		}


	// Protected virtual methods

	protected void showString( boolean returnQueryToPosition )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		}

	protected void showWordReferences( boolean returnQueryToPosition )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		}

	protected boolean isSorted( Item nextSortItem )
		{
		return ( nextSortItem == null ||
				// Descending creationSentenceNr
				creationSentenceNr() > nextSortItem.creationSentenceNr() );
		}

	protected boolean checkParameter( int queryParameter )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		return false;
		}

	protected boolean checkWordType( short queryWordTypeNr )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		return false;
		}

	protected boolean checkReferenceItemById( int querySentenceNr, int queryItemNr )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		return false;
		}

	protected byte checkForUsage()
		{
		return CommonVariables.result;
		}

	protected byte findMatchingWordReferenceString( String queryString )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		CommonVariables.foundMatchingStrings = false;
		return CommonVariables.result;
		}

	protected void showWords( boolean returnQueryToPosition, short queryWordTypeNr )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		String myWordString;

		if( CommonVariables.queryStringBuffer == null )
			CommonVariables.queryStringBuffer = new StringBuffer();
		if( ( myWordString = myWordTypeString( queryWordTypeNr ) ) != null )
			{
			if( CommonVariables.foundQuery )
				CommonVariables.queryStringBuffer.append( returnQueryToPosition ? Constants.NEW_LINE_STRING : Constants.QUERY_SEPARATOR_SPACE_STRING );

			if( !isActiveItem() )	// Show status when not active
				CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar_ );

			CommonVariables.foundQuery = true;
			CommonVariables.queryStringBuffer.append( myWordString );
			}
		}

	protected String itemString()
		{
		return null;
		}

	protected StringBuffer toStringBuffer( short queryWordTypeNr )
		{
		return null;
		}

	protected StringBuffer baseToStringBuffer( short queryWordTypeNr )
		{
		String myWordString = myWordTypeString( queryWordTypeNr );
		String userNameString = ( myWord_ == null ? null : myWord_.userName( userNr_ ) );
		CommonVariables.queryStringBuffer = new StringBuffer();

		if( !isActiveItem() )	// Show status when not active
			CommonVariables.queryStringBuffer.append( Constants.EMPTY_STRING + statusChar_ );

		if( myWordString != null )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_WORD_START_CHAR + myWordString + Constants.QUERY_WORD_END_CHAR );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_LIST_START_STRING + ( myList_ == null ? Constants.QUERY_NO_LIST_CHAR : myList_.listChar() ) + Constants.QUERY_LIST_END_CHAR );

		CommonVariables.queryStringBuffer.append( Constants.QUERY_ITEM_START_STRING + creationSentenceNr_ + Constants.QUERY_SEPARATOR_CHAR + itemNr_ + Constants.QUERY_ITEM_END_CHAR );

		if( isAvailableForRollback )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isAvailableForRollback" );

/*		if( isSelectedByQuery )		// Always true during query
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "isSelectedByQuery" );
*/

		if( wasActiveBeforeArchiving )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "wasActiveBeforeArchiving" );

		if( userNr_ > Constants.NO_USER_NR )
			{
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "user:" + ( userNameString == null ? userNr_ : userNameString ) );
			}

		if( originalSentenceNr_ > Constants.NO_SENTENCE_NR &&
		originalSentenceNr_ != creationSentenceNr_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "original:" + Constants.QUERY_DEACTIVE_ITEM_START_CHAR + originalSentenceNr_ + Constants.QUERY_DEACTIVE_ITEM_END_CHAR );

		if( activeSentenceNr_ > Constants.NO_SENTENCE_NR &&
		activeSentenceNr_ != creationSentenceNr_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "active:" + Constants.QUERY_DEACTIVE_ITEM_START_CHAR + activeSentenceNr_ + Constants.QUERY_DEACTIVE_ITEM_END_CHAR );

		if( deactiveSentenceNr_ > Constants.NO_SENTENCE_NR &&
		deactiveSentenceNr_ != creationSentenceNr_ )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "deactive:" + Constants.QUERY_DEACTIVE_ITEM_START_CHAR + deactiveSentenceNr_ + Constants.QUERY_DEACTIVE_ITEM_END_CHAR );

		if( archiveSentenceNr_ > Constants.NO_SENTENCE_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "archive:" + Constants.QUERY_DEACTIVE_ITEM_START_CHAR + archiveSentenceNr_ + Constants.QUERY_DEACTIVE_ITEM_END_CHAR );

		if( deleteSentenceNr_ > Constants.NO_SENTENCE_NR )
			CommonVariables.queryStringBuffer.append( Constants.QUERY_SEPARATOR_STRING + "delete:" + Constants.QUERY_DEACTIVE_ITEM_START_CHAR + deleteSentenceNr_ + Constants.QUERY_DEACTIVE_ITEM_END_CHAR );

		return CommonVariables.queryStringBuffer;
		}


	// Protected common methods

	protected void setActiveStatus()
		{
		statusChar_ = Constants.QUERY_ACTIVE_CHAR;
		}

	protected void setDeactiveStatus()
		{
		statusChar_ = Constants.QUERY_DEACTIVE_CHAR;
		}

	protected void setArchiveStatus()
		{
		statusChar_ = Constants.QUERY_ARCHIVE_CHAR;
		}

	protected void setDeletedStatus()
		{
		statusChar_ = Constants.QUERY_DELETED_CHAR;
		}

	protected void setActiveSentenceNr()
		{
		if( activeSentenceNr_ == Constants.NO_SENTENCE_NR )
			activeSentenceNr_ = CommonVariables.currentSentenceNr;
		}

	protected void setDeactiveSentenceNr()
		{
		if( deactiveSentenceNr_ == Constants.NO_SENTENCE_NR )
			deactiveSentenceNr_ = CommonVariables.currentSentenceNr;
		}

	protected void setArchiveSentenceNr()
		{
		if( archiveSentenceNr_ == Constants.NO_SENTENCE_NR )
			archiveSentenceNr_ = CommonVariables.currentSentenceNr;
		}

	protected void setDeleteSentenceNr()
		{
		deleteSentenceNr_ = CommonVariables.currentSentenceNr;
		}

	protected void clearArchiveSentenceNr()
		{
		archiveSentenceNr_ = Constants.NO_SENTENCE_NR;
		}

	// Strictly to init AdminItem
	protected void initializeItemVariables( WordItem myWord )
		{
		// Private constructible variables

//		AdminItem has no constructible variables to be initialized

		// Private loadable variables

//		AdminItem has no myList_;
		myWord_ = myWord;

		if( myWord_ == null )
			setSystemErrorInItem( 1, null, null, "The given my word is undefined" );
		}

	protected void initializeItemVariables( int originalSentenceNr, int activeSentenceNr, int deactiveSentenceNr, int archiveSentenceNr, List myList, WordItem myWord )
		{
		// Private loadable variables

		myList_ = myList;
		myWord_ = myWord;

		// Private constructible variables

		if( CommonVariables.currentUserNr > Constants.NO_USER_NR )
			userNr_ = CommonVariables.currentUserNr;

		originalSentenceNr_ = ( originalSentenceNr == Constants.NO_SENTENCE_NR ? CommonVariables.currentSentenceNr : originalSentenceNr );
		activeSentenceNr_ = ( originalSentenceNr == Constants.NO_SENTENCE_NR ? CommonVariables.currentSentenceNr : activeSentenceNr );
		deactiveSentenceNr_ = deactiveSentenceNr;
		archiveSentenceNr_ = archiveSentenceNr;

		itemNr_ = ++CommonVariables.currentItemNr;

		if( myList_ != null )
			{
			if( myWord_ == null )
				setSystemErrorInItem( 1, null, null, "The given my word is undefined" );
			}
		else
			setSystemErrorInItem( 1, null, null, "The given my list is undefined" );
		}

	protected boolean hasActiveSentenceNr()
		{
		return ( activeSentenceNr_ > Constants.NO_SENTENCE_NR );
		}

	protected boolean hasDeactiveSentenceNr()
		{
		return ( deactiveSentenceNr_ > Constants.NO_SENTENCE_NR );
		}

	protected boolean hasArchiveSentenceNr()
		{
		return ( archiveSentenceNr_ > Constants.NO_SENTENCE_NR );
		}

	protected boolean hasCurrentCreationSentenceNr()
		{
		return ( creationSentenceNr_ == CommonVariables.currentSentenceNr );
		}

	protected boolean hasCurrentActiveSentenceNr()
		{
		return ( activeSentenceNr_ == CommonVariables.currentSentenceNr );
		}

	protected boolean hasCurrentDeactiveSentenceNr()
		{
		return ( deactiveSentenceNr_ == CommonVariables.currentSentenceNr );
		}

	protected boolean hasCurrentArchiveSentenceNr()
		{
		return ( archiveSentenceNr_ == CommonVariables.currentSentenceNr );
		}

	protected boolean hasCurrentDeleteSentenceNr()
		{
		return ( deleteSentenceNr_ == CommonVariables.currentSentenceNr );
		}

	protected boolean isOlderSentence()
		{
		return ( originalSentenceNr_ < CommonVariables.currentSentenceNr );
		}

	protected boolean isActiveItem()
		{
		return ( statusChar_ == Constants.QUERY_ACTIVE_CHAR );
		}

	protected boolean isDeactiveItem()
		{
		return ( statusChar_ == Constants.QUERY_DEACTIVE_CHAR );
		}

	protected boolean isArchiveItem()
		{
		return ( statusChar_ == Constants.QUERY_ARCHIVE_CHAR );
		}

	protected boolean isDeletedItem()
		{
		return ( statusChar_ == Constants.QUERY_DELETED_CHAR );
		}

	protected boolean isMoreRecent( Item checkItem )
		{
		return ( checkItem != null &&

				( creationSentenceNr_ > checkItem.creationSentenceNr() ||

				( creationSentenceNr_ == checkItem.creationSentenceNr() &&
				itemNr_ > checkItem.itemNr() ) ) );
		}

	protected int activeSentenceNr()
		{
		return activeSentenceNr_;
		}

	protected int deactiveSentenceNr()
		{
		return deactiveSentenceNr_;
		}

	protected int originalSentenceNr()
		{
		return originalSentenceNr_;
		}

	protected int creationSentenceNr()
		{
		return creationSentenceNr_;
		}

	protected int archiveSentenceNr()
		{
		return archiveSentenceNr_;
		}

	protected int deleteSentenceNr()
		{
		return deleteSentenceNr_;
		}

	protected int itemNr()
		{
		return itemNr_;
		}

	protected byte decrementActiveSentenceNr()
		{
		if( activeSentenceNr_ > Constants.NO_SENTENCE_NR )
			activeSentenceNr_--;
		else
			return setErrorInItem( 1, moduleNameString_, "The active sentence number is too low for a decrement" );

		return CommonVariables.result;
		}

	protected byte decrementDeactiveSentenceNr()
		{
		if( deactiveSentenceNr_ > Constants.NO_SENTENCE_NR )
			deactiveSentenceNr_--;
		else
			return setErrorInItem( 1, moduleNameString_, "The deactive sentence number is too low for a decrement" );

		return CommonVariables.result;
		}

	protected byte decrementOriginalSentenceNr()
		{
		if( originalSentenceNr_ > Constants.NO_SENTENCE_NR )
			originalSentenceNr_--;
		else
			return setErrorInItem( 1, moduleNameString_, "The original sentence number is too low for a decrement" );

		return CommonVariables.result;
		}

	protected byte decrementCreationSentenceNr()
		{
		if( creationSentenceNr_ > Constants.NO_SENTENCE_NR )
			creationSentenceNr_--;
		else
			return setErrorInItem( 1, moduleNameString_, "The creation sentence number is too low for a decrement" );

		return CommonVariables.result;
		}

	protected byte decrementArchiveSentenceNr()
		{
		if( archiveSentenceNr_ > Constants.NO_SENTENCE_NR )
			archiveSentenceNr_--;
		else
			return setErrorInItem( 1, moduleNameString_, "The archive sentence number is too low for a decrement" );

		return CommonVariables.result;
		}

	protected byte decrementCreationItemNr( int decrementOffset )
		{
		if( itemNr_ > decrementOffset )
			itemNr_ -= decrementOffset;
		else
			return setErrorInItem( 1, moduleNameString_, "The given decrement offset is higher than the creation item number itself" );

		return CommonVariables.result;
		}

	protected char statusChar()
		{
		return statusChar_;
		}

	protected List myList()
		{
		return myList_;
		}

	protected WordItem myWord()
		{
		return myWord_;
		}
	};

/*************************************************************************
 *
 *	"Give thanks to him who made the heavenly lights-
 *		His faithful love endures forever.
 *	the sun to rule the day
 *		His faithful love endures forever.
 *	and the moon and stars to rule the night.
 *		His faithful love endures forever." (Psalm 136:7-9)
 *
 *************************************************************************/
