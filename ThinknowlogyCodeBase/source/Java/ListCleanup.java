/*
 *	Class:			ListCleanup
 *	Supports class:	List
 *	Purpose:		To cleanup unnecessary items in the lists
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

class ListCleanup
	{
	// Private constructible variables

	private List myList_;
	private String moduleNameString_;


	// Private methods

	private void currentItemNr( Item searchItem )
		{
		while( searchItem != null )
			{
			if( searchItem.hasCurrentCreationSentenceNr() &&
			searchItem.itemNr() > CommonVariables.currentItemNr )
				CommonVariables.currentItemNr = searchItem.itemNr();

			searchItem = searchItem.nextItem;
			}
		}

	private int highestInUseSentenceNrInList( int highestSentenceNr, Item searchItem )
		{
		int tempSentenceNr;
		int highestInUseSentenceNr = Constants.NO_SENTENCE_NR;

		while( searchItem != null &&
		highestInUseSentenceNr < highestSentenceNr )
			{
			tempSentenceNr = searchItem.activeSentenceNr();

			if( tempSentenceNr > highestInUseSentenceNr &&
			tempSentenceNr <= highestSentenceNr )
				highestInUseSentenceNr = tempSentenceNr;

			tempSentenceNr = searchItem.deactiveSentenceNr();

			if( tempSentenceNr > highestInUseSentenceNr &&
			tempSentenceNr <= highestSentenceNr )
				highestInUseSentenceNr = tempSentenceNr;

			tempSentenceNr = searchItem.archiveSentenceNr();

			if( tempSentenceNr > highestInUseSentenceNr &&
			tempSentenceNr <= highestSentenceNr )
				highestInUseSentenceNr = tempSentenceNr;

			searchItem = searchItem.nextItem;
			}

		return highestInUseSentenceNr;
		}

	private byte decrementSentenceNrs( int startSentenceNr, Item searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.activeSentenceNr() >= startSentenceNr )
				{
				if( searchItem.activeSentenceNr() > startSentenceNr )
					{
					if( searchItem.decrementActiveSentenceNr() != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the active sentence number of an item" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item with an active sentence number equal to the given start sentence number " + startSentenceNr );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			searchItem.deactiveSentenceNr() >= startSentenceNr )
				{
				if( searchItem.deactiveSentenceNr() > startSentenceNr )
					{
					if( searchItem.decrementDeactiveSentenceNr() != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the deactive sentence number of an item" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item with a deactive sentence number equal to the given start sentence number " + startSentenceNr );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			searchItem.originalSentenceNr() >= startSentenceNr )
				{
				if( searchItem.originalSentenceNr() > startSentenceNr )
					{
					if( searchItem.decrementOriginalSentenceNr() != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the original sentence number of an item" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item with an original sentence number equal to the given start sentence number " + startSentenceNr );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			searchItem.creationSentenceNr() >= startSentenceNr )
				{
				if( searchItem.creationSentenceNr() > startSentenceNr )
					{
					if( searchItem.decrementCreationSentenceNr() != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the creation sentence number of an item" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item with a creation sentence number equal to the given start sentence number " + startSentenceNr );
				}

			if( CommonVariables.result == Constants.RESULT_OK &&
			searchItem.archiveSentenceNr() >= startSentenceNr )
				{
				if( searchItem.archiveSentenceNr() > startSentenceNr )
					{
					if( searchItem.decrementArchiveSentenceNr() != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the archive sentence number of an item" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item with an archive sentence number equal to the given start sentence number " + startSentenceNr );
				}

			searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}

	private byte decrementItemNrRange( int decrementSentenceNr, int startDecrementItemNr, int decrementOffset, Item searchItem )
		{
		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.creationSentenceNr() == decrementSentenceNr &&
			searchItem.itemNr() >= startDecrementItemNr )
				{
				if( searchItem.itemNr() > startDecrementItemNr )
					{
					if( searchItem.decrementCreationItemNr( decrementOffset ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to decrement the creation item number of an item with a certain offset" );
					}
				else
					return myList_.setError( 1, moduleNameString_, "I found an item number equal to the given start item number" );
				}

			searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}


	// Constructor

	protected ListCleanup( List myList )
		{
		String errorString = null;

		myList_ = myList;
		moduleNameString_ = this.getClass().getName();

		if( myList_ == null )
			errorString = "The given my list is undefined";

		if( errorString != null )
			{
			if( myList_ != null )
				myList_.setSystemError( 1, moduleNameString_, errorString );
			else
				{
				CommonVariables.result = Constants.RESULT_SYSTEM_ERROR;
				Console.addError( "\nClass:" + moduleNameString_ + "\nMethod:\t" + Constants.PRESENTATION_ERROR_CONSTRUCTOR_METHOD_NAME + "\nError:\t\t" + errorString + ".\n" );
				}
			}
		}


	// Protected methods

	protected void currentItemNr()
		{
		currentItemNr( myList_.firstActiveItem() );
		currentItemNr( myList_.firstDeactiveItem() );
		currentItemNr( myList_.firstArchiveItem() );
		currentItemNr( myList_.firstDeleteItem() );
		}

	protected void deleteRollbackInfo()
		{
		Item searchItem = myList_.firstDeleteItem();

		while( searchItem != null )
			{
			if( searchItem.isAvailableForRollback &&
			searchItem.deleteSentenceNr() >= CommonVariables.currentSentenceNr )
				searchItem.isAvailableForRollback = false;

			searchItem = searchItem.nextItem;
			}
		}

	protected int highestInUseSentenceNrInList( boolean includeDeletedItems, int highestSentenceNr )
		{
		int tempSentenceNr;
		int highestInUseSentenceNr = highestInUseSentenceNrInList( highestSentenceNr, myList_.firstActiveItem() );

		if( ( tempSentenceNr = highestInUseSentenceNrInList( highestSentenceNr, myList_.firstDeactiveItem() ) ) > highestInUseSentenceNr )
			highestInUseSentenceNr = tempSentenceNr;

		if( ( tempSentenceNr = highestInUseSentenceNrInList( highestSentenceNr, myList_.firstArchiveItem() ) ) > highestInUseSentenceNr )
			highestInUseSentenceNr = tempSentenceNr;

		if( includeDeletedItems &&
		( tempSentenceNr = highestInUseSentenceNrInList( highestSentenceNr, myList_.firstDeleteItem() ) ) > highestInUseSentenceNr )
			highestInUseSentenceNr = tempSentenceNr;

		return highestInUseSentenceNr;
		}

	protected byte rollbackDeletedRedoInfo()
		{
		Item searchItem = myList_.firstDeleteItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.isAvailableForRollback &&
			searchItem.hasCurrentDeleteSentenceNr() )
				{
				searchItem.isAvailableForRollback = false;

				if( myList_.removeItemFromDeletedList( searchItem ) == Constants.RESULT_OK )
					{
					if( searchItem.hasDeactiveSentenceNr() )
						{
						if( searchItem.hasArchiveSentenceNr() )
							{
							searchItem.setArchiveStatus();

							if( myList_.addItemToArchiveList( searchItem ) == Constants.RESULT_OK )
								searchItem = myList_.nextListItem();
							else
								myList_.addError( 1, moduleNameString_, "I failed to add an item to the archive list" );
							}
						else
							{
							searchItem.setDeactiveStatus();

							if( myList_.addItemToDeactiveList( searchItem ) == Constants.RESULT_OK )
								searchItem = myList_.nextListItem();
							else
								myList_.addError( 1, moduleNameString_, "I failed to add an item to the deactive list" );
							}
						}
					else
						{
						searchItem.setActiveStatus();

						if( myList_.addItemToActiveList( searchItem ) == Constants.RESULT_OK )
							searchItem = myList_.nextListItem();
						else
							myList_.addError( 1, moduleNameString_, "I failed to add an item to the active list" );
						}
					}
				else
					myList_.addError( 1, moduleNameString_, "I failed to remove an item from the deleted list" );
				}
			else
				searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}

	protected byte decrementSentenceNrs( int startSentenceNr )
		{
		if( startSentenceNr > Constants.NO_SENTENCE_NR )
			{
			if( decrementSentenceNrs( startSentenceNr, myList_.firstActiveItem() ) == Constants.RESULT_OK )
				{
				if( decrementSentenceNrs( startSentenceNr, myList_.firstDeactiveItem() ) == Constants.RESULT_OK )
					{
					if( decrementSentenceNrs( startSentenceNr, myList_.firstArchiveItem() ) == Constants.RESULT_OK )
						decrementSentenceNrs( startSentenceNr, myList_.firstDeleteItem() );
					}
				}
			}
		else
			return myList_.setError( 1, moduleNameString_, "The given start sentence number is undefined" );

		return CommonVariables.result;
		}

	protected byte decrementItemNrRange( int decrementSentenceNr, int startDecrementItemNr, int decrementOffset )
		{
		if( decrementSentenceNr >= Constants.NO_SENTENCE_NR &&
		decrementSentenceNr < Constants.MAX_SENTENCE_NR )
			{
			if( startDecrementItemNr > Constants.NO_ITEM_NR )
				{
				if( decrementOffset > 0 )
					{
					if( decrementItemNrRange( decrementSentenceNr, startDecrementItemNr, decrementOffset, myList_.firstActiveItem() ) == Constants.RESULT_OK )
						{
						if( decrementItemNrRange( decrementSentenceNr, startDecrementItemNr, decrementOffset, myList_.firstDeactiveItem() ) == Constants.RESULT_OK )
							{
							if( decrementItemNrRange( decrementSentenceNr, startDecrementItemNr, decrementOffset, myList_.firstArchiveItem() ) == Constants.RESULT_OK )
								return decrementItemNrRange( decrementSentenceNr, startDecrementItemNr, decrementOffset, myList_.firstDeleteItem() );
							}
						}
					}
				else
					return myList_.setError( 1, moduleNameString_, "The given decrement offset is undefined" );
				}
			else
				return myList_.setError( 1, moduleNameString_, "The given start item number is undefined" );
			}
		else
			return myList_.setError( 1, moduleNameString_, "The given decrement sentence number is undefined" );

		return CommonVariables.result;
		}

	protected byte deleteSentences( boolean isAvailableForRollback, int lowestSentenceNr )
		{
		Item searchItem = myList_.firstActiveItem();

		if( lowestSentenceNr > Constants.NO_SENTENCE_NR )
			{
			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.activeSentenceNr() >= lowestSentenceNr )
					{
					if( searchItem.creationSentenceNr() >= lowestSentenceNr )
						{
						if( myList_.deleteActiveItem( isAvailableForRollback, searchItem ) != Constants.RESULT_OK )
							myList_.addError( 1, moduleNameString_, "I failed to delete an active item" );
						}
					else
						{
						if( myList_.deactivateActiveItem( searchItem ) != Constants.RESULT_OK )
							myList_.addError( 1, moduleNameString_, "I failed to deactivate an active item" );
						}

					searchItem = myList_.nextListItem();

					if( myList_.isAssignmentList() )
						CommonVariables.isAssignmentChanged = true;
					}
				else
					searchItem = searchItem.nextItem;
				}

			searchItem = myList_.firstDeactiveItem();

			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.deactiveSentenceNr() >= lowestSentenceNr )
					{
					if( searchItem.creationSentenceNr() >= lowestSentenceNr )
						{
						if( myList_.deleteDeactiveItem( isAvailableForRollback, searchItem ) != Constants.RESULT_OK )
							myList_.addError( 1, moduleNameString_, "I failed to archive a deactive item" );
						}
					else
						{
						if( myList_.activateDeactiveItem( searchItem ) != Constants.RESULT_OK )
							myList_.addError( 1, moduleNameString_, "I failed to activate a deactive item" );
						}

					searchItem = myList_.nextListItem();

					if( myList_.isAssignmentList() )
						CommonVariables.isAssignmentChanged = true;
					}
				else
					searchItem = searchItem.nextItem;
				}

			searchItem = myList_.firstArchiveItem();

			while( CommonVariables.result == Constants.RESULT_OK &&
			searchItem != null )
				{
				if( searchItem.archiveSentenceNr() >= lowestSentenceNr )
					{
					if( searchItem.creationSentenceNr() >= lowestSentenceNr )
						{
						if( myList_.deleteArchiveItem( isAvailableForRollback, searchItem ) != Constants.RESULT_OK )
							myList_.addError( 1, moduleNameString_, "I failed to delete an archive item" );
						}
					else
						{
						searchItem.clearArchiveSentenceNr();

						if( searchItem.wasActiveBeforeArchiving )
							{
							if( myList_.activateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
								myList_.addError( 1, moduleNameString_, "I failed to activate an archive item" );
							}
						else
							{
							if( myList_.deactivateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
								myList_.addError( 1, moduleNameString_, "I failed to deactivate an archive item" );
							}
						}

					searchItem = myList_.nextListItem();

					if( myList_.isAssignmentList() )
						CommonVariables.isAssignmentChanged = true;
					}
				else
					searchItem = searchItem.nextItem;
				}
			}
		else
			return myList_.setError( 1, moduleNameString_, "The given lowest sentence number is undefined" );

		return CommonVariables.result;
		}

	protected byte undoCurrentSentence()
		{
		Item searchItem = myList_.firstActiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.activeSentenceNr() >= CommonVariables.currentSentenceNr )
				{
				if( searchItem.hasDeactiveSentenceNr() &&
				searchItem.hasCurrentActiveSentenceNr() &&
				searchItem.deactiveSentenceNr() < searchItem.activeSentenceNr() )
					{
					if( myList_.deactivateActiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to deactivate an active item" );
					}
				else
					{
					if( myList_.archiveActiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to archive an active item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstDeactiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.deactiveSentenceNr() >= CommonVariables.currentSentenceNr )
				{
				if( searchItem.hasActiveSentenceNr() &&
				searchItem.hasCurrentDeactiveSentenceNr() &&
				searchItem.activeSentenceNr() < searchItem.deactiveSentenceNr() )
					{
					if( myList_.activateDeactiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to activate a deactive item" );
					}
				else
					{
					if( myList_.archiveDeactiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to archive a deactive item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstArchiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( ( searchItem.archiveSentenceNr() == CommonVariables.currentSentenceNr &&
			searchItem.creationSentenceNr() < CommonVariables.currentSentenceNr ) ||

			( searchItem.archiveSentenceNr() + 1 == CommonVariables.currentSentenceNr &&
			searchItem.creationSentenceNr() + 1 == CommonVariables.currentSentenceNr ) )
				{
				if( ( searchItem.wasActiveBeforeArchiving &&
				( searchItem.activeSentenceNr() >= searchItem.deactiveSentenceNr() ||
				searchItem.activeSentenceNr() != CommonVariables.currentSentenceNr ) ) ||

				( !searchItem.wasActiveBeforeArchiving &&
				searchItem.activeSentenceNr() < searchItem.deactiveSentenceNr() &&
				searchItem.hasCurrentDeactiveSentenceNr() ) )
					{
					if( myList_.activateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to activate an archive item" );
					}
				else
					{
					if( myList_.deactivateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to deactivate an archive item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}

	protected byte redoCurrentSentence()
		{
		Item searchItem = myList_.firstActiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.activeSentenceNr() < CommonVariables.currentSentenceNr &&

			( searchItem.hasCurrentDeactiveSentenceNr() ||
			searchItem.hasCurrentArchiveSentenceNr() ) )
				{
				if( searchItem.deactiveSentenceNr() > searchItem.archiveSentenceNr() )
					{
					if( myList_.deactivateActiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to deactivate an active item" );
					}
				else
					{
					if( myList_.archiveActiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to archive an active item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstDeactiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.deactiveSentenceNr() < CommonVariables.currentSentenceNr &&
			( searchItem.hasCurrentActiveSentenceNr() ||
			searchItem.hasCurrentArchiveSentenceNr() ) )
				{
				if( searchItem.deactiveSentenceNr() > searchItem.archiveSentenceNr() )
					{
					if( myList_.activateDeactiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to activate a deactive item" );
					}
				else
					{
					if( myList_.archiveDeactiveItem( searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to archive a deactive item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		searchItem = myList_.firstArchiveItem();

		while( CommonVariables.result == Constants.RESULT_OK &&
		searchItem != null )
			{
			if( searchItem.hasCurrentCreationSentenceNr() )
				{
				if( searchItem.wasActiveBeforeArchiving )
					{
					if( myList_.activateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to activate an archive item" );
					}
				else
					{
					if( myList_.deactivateArchiveItem( true, searchItem ) != Constants.RESULT_OK )
						myList_.addError( 1, moduleNameString_, "I failed to deactivate an archive item" );
					}

				searchItem = myList_.nextListItem();

				if( myList_.isAssignmentList() )
					CommonVariables.isAssignmentChanged = true;
				}
			else
				searchItem = searchItem.nextItem;
			}

		return CommonVariables.result;
		}
	};

/*************************************************************************
 *
 *	"The winds blows, and we are gone -
 *	as though we had never been here.
 *	But the love of the Lord remains forever
 *	with those who fear him." (Psalm 103:16-17)
 *
 *************************************************************************/
