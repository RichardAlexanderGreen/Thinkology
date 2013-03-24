/*
 *	Class:		List
 *	Purpose:	Base class to store the items of the knowledge structure
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

class List
	{
	// Private constructible variables

	private char listChar_;

	private Item activeList_;
	private Item deactiveList_;
	private Item archiveList_;
	private Item deleteList_;

	private Item nextListItem_;

	private ListCleanup listCleanup_;
	private ListQuery listQuery_;

	private WordItem myWord_;


	// Private methods

	private boolean includesThisList( StringBuffer queryListStringBuffer )
		{
		short index = 0;

		if( queryListStringBuffer != null &&
		queryListStringBuffer.length() > 0 )
			{
			while( index < queryListStringBuffer.length() &&
			queryListStringBuffer.charAt( index ) != listChar_ )
				index++;

			if( index == queryListStringBuffer.length() )
				return false;
			}

		return true;
		}

	private byte checkForUsage( Item unusedItem )
		{
		if( unusedItem != null )
			return unusedItem.checkForUsage();

		return setError( 1, null, "The given unused item is undefined" );
		}

	private byte removeItemFromActiveList( Item removeItem )
		{
		if( removeItem != null )
			{
			if( removeItem.myList() == this )
				{
				if( removeItem.isActiveItem() )
					{
					if( removeItem.previousItem == null )	// First item in list
						{
						activeList_ = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = null;
						}
					else
						{
						removeItem.previousItem.nextItem = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = removeItem.previousItem;
						}

					nextListItem_ = removeItem.nextItem;	// Remember next item

					// Disconnect item from active list
					removeItem.previousItem = null;
					removeItem.nextItem = null;
					}
				else
					return setError( 1, null, "The given remove item is not an active item" );
				}
			else
				return setError( 1, null, "The given remove item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given remove item is undefined" );

		return CommonVariables.result;
		}

	private byte removeItemFromDeactiveList( Item removeItem )
		{
		if( removeItem != null )
			{
			if( removeItem.myList() == this )
				{
				if( removeItem.isDeactiveItem() )
					{
					if( removeItem.previousItem == null )	// First item in list
						{
						deactiveList_ = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = null;
						}
					else
						{
						removeItem.previousItem.nextItem = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = removeItem.previousItem;
						}

					nextListItem_ = removeItem.nextItem;	// Remember next item

					// Disconnect item from deactive list
					removeItem.previousItem = null;
					removeItem.nextItem = null;
					}
				else
					return setError( 1, null, "The given remove item is not a deactive item" );
				}
			else
				return setError( 1, null, "The given remove item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given remove item is undefined" );

		return CommonVariables.result;
		}

	private byte removeItemFromArchiveList( Item removeItem )
		{
		if( removeItem != null )
			{
			if( removeItem.myList() == this )
				{
				if( removeItem.isArchiveItem() )
					{
					if( removeItem.previousItem == null )	// First item in list
						{
						archiveList_ = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = null;
						}
					else
						{
						removeItem.previousItem.nextItem = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = removeItem.previousItem;
						}

					nextListItem_ = removeItem.nextItem;	// Remember next item

					// Disconnect item from deactive list
					removeItem.previousItem = null;
					removeItem.nextItem = null;
					}
				else
					return setError( 1, null, "The given remove item is not an archive item" );
				}
			else
				return setError( 1, null, "The given remove item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given remove item is undefined" );

		return CommonVariables.result;
		}

	private byte addItemToDeletedList( boolean isAvailableForRollback, Item newItem )
		{
		Item searchItem = deleteList_;
		Item previousSearchItem = null;

		if( newItem != null )
			{
			if( newItem.myList() == this )
				{
				if( newItem.isDeletedItem() )
					{
					if( newItem.nextItem == null )
						{
						// Sort item in list
						while( searchItem != null &&
						( searchItem.creationSentenceNr() > newItem.creationSentenceNr() ||	// Sort on descending

						( searchItem.creationSentenceNr() == newItem.creationSentenceNr() &&	// creationSentenceNr and
						searchItem.itemNr() < newItem.itemNr() ) ) )							// ascending itemNr
							{
							previousSearchItem = searchItem;
							searchItem = searchItem.nextItem;
							}

						if( searchItem == null ||
						searchItem.creationSentenceNr() != newItem.creationSentenceNr() ||	// Check on duplicates
						searchItem.itemNr() != newItem.itemNr() )								// for integrity
							{
							newItem.previousItem = previousSearchItem;

							if( previousSearchItem == null )	// First item in list
								{
								if( deleteList_ != null )
									deleteList_.previousItem = newItem;

								newItem.nextItem = deleteList_;
								deleteList_ = newItem;
								}
							else
								{
								if( searchItem != null )
									searchItem.previousItem = newItem;

								newItem.nextItem = previousSearchItem.nextItem;
								previousSearchItem.nextItem = newItem;
								}

							newItem.isAvailableForRollback = isAvailableForRollback;
							}
						else
							return setError( 1, null, "I found a deleted item with the same identification" );
						}
					else
						return setError( 1, null, "The given new item seems to be a part of a list" );
					}
				else
					return setError( 1, null, "The given new item is not a deleted item" );
				}
			else
				return setError( 1, null, "The given new item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given new item is undefined" );

		return CommonVariables.result;
		}


	// Constructor

	protected List()
		{
		// Private constructible variables

		listChar_ = Constants.QUERY_NO_LIST_CHAR;

		activeList_ = null;
		deactiveList_ = null;
		archiveList_ = null;
		deleteList_ = null;

		nextListItem_ = null;

		listCleanup_ = null;
		listQuery_ = null;

		myWord_ = null;
		}


	// Protected error methods

	protected void addError( int methodLevel, String moduleNameString, String errorString )
		{
		Presentation.showError( listChar_, ( moduleNameString == null ? this.getClass().getName() : moduleNameString ), ( moduleNameString == null ? this.getClass().getSuperclass().getName() : null ), null, ( methodLevel + 1 ), errorString );
		}

	protected byte setError( int methodLevel, String moduleNameString, String errorString )
		{
		addError( ( methodLevel + 1 ), moduleNameString, errorString );

		CommonVariables.result = Constants.RESULT_ERROR;

		return Constants.RESULT_ERROR;
		}

	protected byte setSystemError( int methodLevel, String moduleNameString, String errorString )
		{
		addError( ( methodLevel + 1 ), moduleNameString, errorString );

		CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;

		return Constants.RESULT_SYSTEM_ERROR;
		}


	// Protected virtual methods

	protected boolean isTemporaryList()
		{
		return false;
		}

	protected byte findWordReference( WordItem referenceWordItem )
		{
		// This is a virtual method. Therefore the given variables are unreferenced
		CommonVariables.foundWordReference = false;
		return CommonVariables.result;
		}


	// Protected common methods

	protected void initializeListVariables( char listChar, WordItem myWord )
		{
		listChar_ = listChar;
		myWord_ = myWord;

		if( myWord_ == null )
			setSystemError( 1, null, "The given my word is undefined" );
		}

	protected void deleteList()
		{
		Item deleteListTailItem = null;
		Item searchItem = deleteList_;

		// Get tail item of delete list
		while( searchItem != null )
			{
			deleteListTailItem = searchItem;
			searchItem = searchItem.nextItem;
			}

		if( deleteListTailItem == null )
			{
			deleteList_ = activeList_;
			searchItem = deleteList_;
			}
		else
			{
			deleteListTailItem.nextItem = activeList_;
			searchItem = deleteListTailItem;
			}

		activeList_ = null;

		// Get tail item of delete list
		while( searchItem != null )
			{
			deleteListTailItem = searchItem;
			searchItem = searchItem.nextItem;
			}

		if( deleteListTailItem == null )
			{
			deleteList_ = deactiveList_;
			searchItem = deleteList_;
			}
		else
			{
			deleteListTailItem.nextItem = deactiveList_;
			searchItem = deleteListTailItem;
			}

		deactiveList_ = null;

		// Get tail item of delete list
		while( searchItem != null )
			{
			deleteListTailItem = searchItem;
			searchItem = searchItem.nextItem;
			}

		if( deleteListTailItem == null )
			deleteList_ = archiveList_;
		else
			deleteListTailItem.nextItem = archiveList_;

		archiveList_ = null;
		}

	protected boolean hasItems()
		{
		return ( activeList_ != null ||
				deactiveList_ != null ||
				archiveList_ != null );
		}

	protected boolean hasActiveItems()
		{
		return ( activeList_ != null );
		}

	protected boolean isAdminList()
		{
		return Character.isUpperCase( listChar_ );
		}

	protected boolean isAssignmentList()
		{
		return ( listChar_ == Constants.WORD_ASSIGNMENT_LIST_SYMBOL );
		}

	protected int highestSentenceNrInList()
		{
		int highestSentenceNr = Constants.NO_SENTENCE_NR;
		Item searchItem = activeList_;

		while( searchItem != null )
			{
			if( searchItem.creationSentenceNr() > highestSentenceNr )
				highestSentenceNr = searchItem.creationSentenceNr();

			searchItem = searchItem.nextItem;
			}

		searchItem = deactiveList_;

		while( searchItem != null )
			{
			if( searchItem.creationSentenceNr() > highestSentenceNr )
				highestSentenceNr = searchItem.creationSentenceNr();

			searchItem = searchItem.nextItem;
			}

		searchItem = archiveList_;

		while( searchItem != null )
			{
			if( searchItem.creationSentenceNr() > highestSentenceNr )
				highestSentenceNr = searchItem.creationSentenceNr();

			searchItem = searchItem.nextItem;
			}

		return highestSentenceNr;
		}

	protected char listChar()
		{
		return listChar_;
		}

	protected byte addItemToActiveList( Item newItem )
		{
		Item searchItem = activeList_;
		Item previousSearchItem = null;

		if( newItem != null )
			{
			if( newItem.myList() == this )
				{
				if( newItem.isActiveItem() )
					{
					if( newItem.nextItem == null )
						{
						newItem.setActiveSentenceNr();

						// Sort item in list
						while( searchItem != null &&
						!newItem.isSorted( searchItem ) )
							{
							previousSearchItem = searchItem;
							searchItem = searchItem.nextItem;
							}

						if( searchItem == null ||
						searchItem.creationSentenceNr() != newItem.creationSentenceNr() ||	// Check on duplicates
						searchItem.itemNr() != newItem.itemNr() )								// for integrity
							{
							newItem.previousItem = previousSearchItem;

							if( previousSearchItem == null )	// First item in list
								{
								if( activeList_ != null )
									activeList_.previousItem = newItem;

								newItem.nextItem = activeList_;
								activeList_ = newItem;
								}
							else
								{
								if( searchItem != null )
									searchItem.previousItem = newItem;

								newItem.nextItem = previousSearchItem.nextItem;
								previousSearchItem.nextItem = newItem;
								}
							}
						else
							return setError( 1, null, "I found an active item with the same identification" );
						}
					else
						return setError( 1, null, "The given new item seems to be a part of a list" );
					}
				else
					return setError( 1, null, "The given new item is not an active item" );
				}
			else
				return setError( 1, null, "The given new item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given new item is undefined" );

		return CommonVariables.result;
		}

	protected byte addItemToDeactiveList( Item newItem )
		{
		Item searchItem = deactiveList_;
		Item previousSearchItem = null;

		if( newItem != null )
			{
			if( newItem.myList() == this )
				{
				if( newItem.isDeactiveItem() )
					{
					if( newItem.nextItem == null )
						{
						newItem.setDeactiveSentenceNr();

						// Sort item in list
						while( searchItem != null &&
						!newItem.isSorted( searchItem ) )
							{
							previousSearchItem = searchItem;
							searchItem = searchItem.nextItem;
							}

						if( searchItem == null ||
						searchItem.creationSentenceNr() != newItem.creationSentenceNr() ||	// Check on duplicates
						searchItem.itemNr() != newItem.itemNr() )								// for integrity
							{
							newItem.previousItem = previousSearchItem;

							if( previousSearchItem == null )	// First item in list
								{
								if( deactiveList_ != null )
									deactiveList_.previousItem = newItem;

								newItem.nextItem = deactiveList_;
								deactiveList_ = newItem;
								}
							else
								{
								if( searchItem != null )
									searchItem.previousItem = newItem;

								newItem.nextItem = previousSearchItem.nextItem;
								previousSearchItem.nextItem = newItem;
								}
							}
						else
							return setError( 1, null, "I found a deactive item with the same identification" );
						}
					else
						return setError( 1, null, "The given new item seems to be a part of a list" );
					}
				else
					return setError( 1, null, "The given new item is not a deactive item" );
				}
			else
				return setError( 1, null, "The given new item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given new item is undefined" );

		return CommonVariables.result;
		}

	protected byte addItemToArchiveList( Item newItem )
		{
		Item searchItem = archiveList_;
		Item previousSearchItem = null;

		if( newItem != null )
			{
			if( newItem.myList() == this )
				{
				if( newItem.isArchiveItem() )
					{
					if( newItem.nextItem == null )
						{
						newItem.setArchiveSentenceNr();

						// Sort item in list
						while( searchItem != null &&
						!newItem.isSorted( searchItem ) )
							{
							previousSearchItem = searchItem;
							searchItem = searchItem.nextItem;
							}

						if( searchItem == null ||
						searchItem.creationSentenceNr() != newItem.creationSentenceNr() ||	// Check on duplicates
						searchItem.itemNr() != newItem.itemNr() )								// for integrity
							{
							newItem.previousItem = previousSearchItem;

							if( previousSearchItem == null )	// First item in list
								{
								if( archiveList_ != null )
									archiveList_.previousItem = newItem;

								newItem.nextItem = archiveList_;
								archiveList_ = newItem;
								}
							else
								{
								if( searchItem != null )
									searchItem.previousItem = newItem;

								newItem.nextItem = previousSearchItem.nextItem;
								previousSearchItem.nextItem = newItem;
								}
							}
						else
							return setError( 1, null, "I found an archive item with the same identification" );
						}
					else
						return setError( 1, null, "The given new item seems to be a part of a list" );
					}
				else
					return setError( 1, null, "The given new item is not an archive item" );
				}
			else
				return setError( 1, null, "The given new item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given new item is undefined" );

		return CommonVariables.result;
		}

	protected byte activateDeactiveItem( Item deactiveItem )
		{
		if( deactiveItem != null )
			{
			if( !deactiveItem.hasActiveSentenceNr() ||
			deactiveItem.hasCurrentActiveSentenceNr() ||
			deactiveItem.hasCurrentDeactiveSentenceNr() )
				{
				if( removeItemFromDeactiveList( deactiveItem ) == Constants.RESULT_OK )
					{
					deactiveItem.setActiveStatus();

					if( addItemToActiveList( deactiveItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add an item to the active list" );
					}
				else
					addError( 1, null, "I failed to remove an item from the deactive list" );
				}
			else
				return setError( 1, null, "The active sentence number of the given deactive item is already assigned" );
			}
		else
			return setError( 1, null, "The given deactive item is undefined" );

		return CommonVariables.result;
		}

	protected byte deactivateActiveItem( Item activeItem )
		{
		if( activeItem != null )
			{
			if( !activeItem.hasDeactiveSentenceNr() ||
			activeItem.hasCurrentDeactiveSentenceNr() ||
			activeItem.hasCurrentActiveSentenceNr() )
				{
				if( removeItemFromActiveList( activeItem ) == Constants.RESULT_OK )
					{
					activeItem.setDeactiveStatus();

					if( addItemToDeactiveList( activeItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add an item to the deactive list" );
					}
				else
					addError( 1, null, "I failed to remove an item from the active list" );
				}
			else
				return setError( 1, null, "The deactive sentence number of the given active item is already assigned" );
			}
		else
			return setError( 1, null, "The given active item is undefined" );

		return CommonVariables.result;
		}

	protected byte archiveActiveItem( Item activeItem )
		{
		if( activeItem != null )
			{
			if( removeItemFromActiveList( activeItem ) == Constants.RESULT_OK )
				{
				activeItem.setArchiveStatus();
				activeItem.wasActiveBeforeArchiving = true;

				if( addItemToArchiveList( activeItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add an item to the archive list" );
				}
			else
				addError( 1, null, "I failed to remove an item from the active list" );
			}
		else
			return setError( 1, null, "The given active item is undefined" );

		return CommonVariables.result;
		}

	protected byte archiveDeactiveItem( Item deactiveItem )
		{
		if( deactiveItem != null )
			{
			if( removeItemFromDeactiveList( deactiveItem ) == Constants.RESULT_OK )
				{
				deactiveItem.setArchiveStatus();
				deactiveItem.wasActiveBeforeArchiving = false;

				if( addItemToArchiveList( deactiveItem ) != Constants.RESULT_OK )
					addError( 1, null, "I failed to add an item to the archive list" );
				}
			else
				addError( 1, null, "I failed to remove an item from the deactive list" );
			}
		else
			return setError( 1, null, "The given deactive item is undefined" );

		return CommonVariables.result;
		}

	protected byte activateArchiveItem( boolean isAvailableForRollback, Item archiveItem )
		{
		if( archiveItem != null )
			{
			if( isAvailableForRollback ||
			!archiveItem.hasActiveSentenceNr() ||
			archiveItem.hasCurrentActiveSentenceNr() )
				{
				if( removeItemFromArchiveList( archiveItem ) == Constants.RESULT_OK )
					{
					archiveItem.setActiveStatus();
					archiveItem.wasActiveBeforeArchiving = false;

					if( addItemToActiveList( archiveItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add an item to the active list" );
					}
				else
					addError( 1, null, "I failed to remove an item from the archive list" );
				}
			else
				return setError( 1, null, "The active sentence number of the given archive item is already assigned" );
			}
		else
			return setError( 1, null, "The given archive item is undefined" );

		return CommonVariables.result;
		}

	protected byte deactivateArchiveItem( boolean isAvailableForRollback, Item archiveItem )
		{
		if( archiveItem != null )
			{
			if( isAvailableForRollback ||
			!archiveItem.hasDeactiveSentenceNr() ||
			archiveItem.hasCurrentDeactiveSentenceNr() )
				{
				if( removeItemFromArchiveList( archiveItem ) == Constants.RESULT_OK )
					{
					archiveItem.setDeactiveStatus();
					archiveItem.wasActiveBeforeArchiving = false;

					if( addItemToDeactiveList( archiveItem ) != Constants.RESULT_OK )
						addError( 1, null, "I failed to add an item to the deactive list" );
					}
				else
					addError( 1, null, "I failed to remove an item from the archive list" );
				}
			else
				return setError( 1, null, "The deactive sentence number of the given archive item is already assigned" );
			}
		else
			return setError( 1, null, "The given archive item is undefined" );

		return CommonVariables.result;
		}

	protected byte deleteActiveItemsWithCurrentSentenceNr()
		{
		Item searchItem = activeList_;

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.hasCurrentCreationSentenceNr() )
				{
				if( deleteActiveItem( false, searchItem ) == Constants.RESULT_OK )
					searchItem = nextListItem_;
				else
					addError( 1, null, "I failed to delete an active item" );
				}
			else
				searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}

	protected byte deleteActiveItem( boolean isAvailableForRollback, Item activeItem )
		{
		if( removeItemFromActiveList( activeItem ) == Constants.RESULT_OK )
			{
			activeItem.setDeletedStatus();
			activeItem.setDeleteSentenceNr();

			if( addItemToDeletedList( isAvailableForRollback, activeItem ) != Constants.RESULT_OK )
				addError( 1, null, "I failed to add an item to the delete list" );
			}
		else
			addError( 1, null, "I failed to remove an item from the active list" );

		return CommonVariables.result;
		}

	protected byte deleteDeactiveItem( boolean isAvailableForRollback, Item deactiveItem )
		{
		if( removeItemFromDeactiveList( deactiveItem ) == Constants.RESULT_OK )
			{
			deactiveItem.setDeletedStatus();
			deactiveItem.setDeleteSentenceNr();

			if( addItemToDeletedList( isAvailableForRollback, deactiveItem ) != Constants.RESULT_OK )
				addError( 1, null, "I failed to add an item to the delete list" );
			}
		else
			addError( 1, null, "I failed to remove an item from the deactive list" );

		return CommonVariables.result;
		}

	protected byte deleteArchiveItem( boolean isAvailableForRollback, Item archiveItem )
		{
		if( removeItemFromArchiveList( archiveItem ) == Constants.RESULT_OK )
			{
			archiveItem.setDeletedStatus();
			archiveItem.setDeleteSentenceNr();

			if( addItemToDeletedList( isAvailableForRollback, archiveItem ) != Constants.RESULT_OK )
				addError( 1, null, "I failed to add an item to the delete list" );
			}
		else
			addError( 1, null, "I failed to remove an item from the archive list" );

		return CommonVariables.result;
		}

	protected byte removeFirstRangeOfDeletedItemsInList()
		{
		Item removeItem = deleteList_;
		Item previousRemoveItem = null;

		if( CommonVariables.nDeletedItems == 0 &&
		CommonVariables.removeSentenceNr == 0 &&
		CommonVariables.removeStartItemNr == 0 )
			{
			while( removeItem != null &&										// Skip items that must be kept for rollback
			removeItem.isAvailableForRollback )									// and items of current sentence (if wanted)
				{
				previousRemoveItem = removeItem;
				removeItem = removeItem.nextItem;
				}

			if( removeItem != null )											// Found items to remove
				{
				CommonVariables.removeSentenceNr = removeItem.creationSentenceNr();
				CommonVariables.removeStartItemNr = removeItem.itemNr();

				do	{
					if( previousRemoveItem == null )
						deleteList_ = deleteList_.nextItem;						// Disconnect deleted list from item
					else
						previousRemoveItem.nextItem = removeItem.nextItem;		// Disconnect deleted list from item

					if( checkForUsage( removeItem ) == Constants.RESULT_OK )
						{
						removeItem.nextItem = null;								// Disconnect item from deleted list
						removeItem = ( previousRemoveItem == null ? deleteList_ : previousRemoveItem.nextItem );
						CommonVariables.nDeletedItems++;
						}
					else
						addError( 1, null, "I failed to check an item for its usage" );
					}
				while( CommonVariables.result == Constants.RESULT_OK &&
				removeItem != null &&
				removeItem.creationSentenceNr() == CommonVariables.removeSentenceNr &&							// Same sentence number
				removeItem.itemNr() == CommonVariables.removeStartItemNr + CommonVariables.nDeletedItems );		// Ascending item number
				}
			}
		else
			return setError( 1, null, "There is already a range of deleted items" );

		return CommonVariables.result;
		}

	protected byte removeItemFromDeletedList( Item removeItem )
		{
		if( removeItem != null )
			{
			if( removeItem.myList() == this )
				{
				if( removeItem.isDeletedItem() )
					{
					if( removeItem.previousItem == null )					// First item in list
						{
						deleteList_ = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = null;
						}
					else
						{
						removeItem.previousItem.nextItem = removeItem.nextItem;

						if( removeItem.nextItem != null )
							removeItem.nextItem.previousItem = removeItem.previousItem;
						}

					nextListItem_ = removeItem.nextItem;	// Remember next item

					// Disconnect item from deleted list
					removeItem.previousItem = null;
					removeItem.nextItem = null;
					}
				else
					return setError( 1, null, "The given remove item is not a deleted item" );
				}
			else
				return setError( 1, null, "The given remove item doesn't belong to my list" );
			}
		else
			return setError( 1, null, "The given remove item is undefined" );

		return CommonVariables.result;
		}

	protected Item firstActiveItem()
		{
		return activeList_;
		}

	protected Item firstDeactiveItem()
		{
		return deactiveList_;
		}

	protected Item firstArchiveItem()
		{
		return archiveList_;
		}

	protected Item firstDeleteItem()
		{
		return deleteList_;
		}

	protected Item nextListItem()
		{
		return nextListItem_;
		}

	protected WordItem myWord()
		{
		return myWord_;
		}


	// Protected cleanup methods

	protected byte getCurrentItemNrInList()
		{
		if( listCleanup_ == null &&
		( listCleanup_ = new ListCleanup( this ) ) == null )
			return setError( 1, null, "I failed to create my list cleanup module" );

		listCleanup_.currentItemNr();
		return CommonVariables.result;
		}

	protected byte getHighestInUseSentenceNrInList( boolean includeDeletedItems, int highestSentenceNr )
		{
		int tempSentenceNr;

		if( listCleanup_ == null &&
		( listCleanup_ = new ListCleanup( this ) ) == null )
			return setError( 1, null, "I failed to create my list cleanup module" );

		if( ( tempSentenceNr = listCleanup_.highestInUseSentenceNrInList( includeDeletedItems, highestSentenceNr ) ) > CommonVariables.highestInUseSentenceNr )
			CommonVariables.highestInUseSentenceNr = tempSentenceNr;

		return CommonVariables.result;
		}

	protected byte undoCurrentSentenceInList()
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listCleanup_.undoCurrentSentence();

		return setError( 1, null, "I failed to create my list query" );
		}

	protected byte redoCurrentSentenceInList()
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listCleanup_.redoCurrentSentence();

		return setError( 1, null, "I failed to create my list query" );
		}

	protected byte rollbackDeletedRedoInfoInList()
		{
		if( listCleanup_ == null &&
		( listCleanup_ = new ListCleanup( this ) ) == null )
			return setError( 1, null, "I failed to create my list cleanup module" );

		listCleanup_.rollbackDeletedRedoInfo();
		return CommonVariables.result;
		}

	protected byte deleteRollbackInfoInList()
		{
		if( listCleanup_ == null &&
		( listCleanup_ = new ListCleanup( this ) ) == null )
			return setError( 1, null, "I failed to create my list cleanup module" );

		listCleanup_.deleteRollbackInfo();
		return CommonVariables.result;
		}

	protected byte deleteSentencesInList( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		if( listCleanup_ != null ||
		( listCleanup_ = new ListCleanup( this ) ) != null )
			return listCleanup_.deleteSentences( isAvailableForRollback, lowestSentenceNr );

		return setError( 1, null, "I failed to create my list cleanup module" );
		}

	protected byte decrementSentenceNrsInList( int startSentenceNr )
		{
		if( listCleanup_ != null ||
		( listCleanup_ = new ListCleanup( this ) ) != null )
			return listCleanup_.decrementSentenceNrs( startSentenceNr );

		return setError( 1, null, "I failed to create my list cleanup module" );
		}

	protected byte decrementItemNrRangeInList( int decrementSentenceNr, int startDecrementItemNr, int decrementOffset )
		{
		if( listCleanup_ != null ||
		( listCleanup_ = new ListCleanup( this ) ) != null )
			return listCleanup_.decrementItemNrRange( decrementSentenceNr, startDecrementItemNr, decrementOffset );

		return setError( 1, null, "I failed to create my list cleanup module" );
		}


	// Protected query methods

	protected void countQueryResultInList()
		{
		if( listQuery_ != null )
			listQuery_.countQueryResult();
		}

	protected void clearQuerySelectionsInList()
		{
		if( listQuery_ != null )
			listQuery_.clearQuerySelections();
		}

	protected byte compareStrings( String searchString, String sourceString )
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listQuery_.compareStrings( searchString, sourceString );

		return setError( 1, null, "I failed to create my list query" );
		}

	protected byte itemQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, boolean isReferenceQuery, int querySentenceNr, int queryItemNr )
		{
		if( listQuery_ == null &&
		( listQuery_ = new ListQuery( this ) ) == null )
			return setError( 1, null, "I failed to create my list query" );

		listQuery_.itemQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, isReferenceQuery, querySentenceNr, queryItemNr );
		return CommonVariables.result;
		}

	protected byte listQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, StringBuffer queryListStringBuffer )
		{
		boolean isListIncludedInQuery = includesThisList( queryListStringBuffer );

		if( isFirstInstruction ||
		!isListIncludedInQuery )
			{
			if( listQuery_ == null &&
			( listQuery_ = new ListQuery( this ) ) == null )
				return setError( 1, null, "I failed to create my list query" );

			listQuery_.listQuery( ( isFirstInstruction && isListIncludedInQuery ), selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems );
			}

		return CommonVariables.result;
		}

	protected byte wordTypeQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, short queryWordTypeNr )
		{
		if( listQuery_ == null &&
		( listQuery_ = new ListQuery( this ) ) == null )
			return setError( 1, null, "I failed to create my list query" );

		listQuery_.wordTypeQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryWordTypeNr );
		return CommonVariables.result;
		}

	protected byte parameterQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, int queryParameter )
		{
		if( listQuery_ == null &&
		( listQuery_ = new ListQuery( this ) ) == null )
			return setError( 1, null, "I failed to create my list query" );

		listQuery_.parameterQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryParameter );
		return CommonVariables.result;
		}

	protected byte wordQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems )
		{
		if( listQuery_ == null &&
		( listQuery_ = new ListQuery( this ) ) == null )
			return setError( 1, null, "I failed to create my list query" );

		listQuery_.wordQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems );
		return CommonVariables.result;
		}

	protected byte wordReferenceQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String wordReferenceNameString )
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listQuery_.wordReferenceQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, wordReferenceNameString );

		return setError( 1, null, "I failed to create my list query" );
		}

	protected byte stringQueryInList( boolean isFirstInstruction, boolean selectActiveItems, boolean selectDeactiveItems, boolean selectArchiveItems, boolean selectDeletedItems, String queryString )
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listQuery_.stringQuery( isFirstInstruction, selectActiveItems, selectDeactiveItems, selectArchiveItems, selectDeletedItems, queryString );

		return setError( 1, null, "I failed to create my list query" );
		}

	protected byte showQueryResultInList( boolean showOnlyWords, boolean showOnlyWordReferences, boolean showOnlyStrings, boolean returnQueryToPosition, short promptTypeNr, short queryWordTypeNr, int queryWidth )
		{
		if( listQuery_ != null ||
		( listQuery_ = new ListQuery( this ) ) != null )
			return listQuery_.showQueryResult( showOnlyWords, showOnlyWordReferences, showOnlyStrings, returnQueryToPosition, promptTypeNr, queryWordTypeNr, queryWidth );

		return setError( 1, null, "The list query module isn't created yet" );
		}
	};

/*************************************************************************
 *
 *	"But he rescues the poor from trouble
 *	and increases their families like flocks of sheep." (Psalm 107:41)
 *
 *************************************************************************/
