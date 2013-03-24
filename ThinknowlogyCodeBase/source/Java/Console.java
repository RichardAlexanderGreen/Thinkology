/*
 *	Class:		Console
 *	Purpose:	To create the GUI and to process the events
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

class Console extends JPanel implements ActionListener, ComponentListener
	{
	// Private static constants
	private static final long serialVersionUID = -2264899695119545018L;

	// Private static variables
	private static boolean isConsoleReady_;
	private static boolean isSelectedAmbiguityBoston_;
	private static boolean isSelectedAmbiguityPresidents_;
	private static boolean isSelectedProgrammingConnect4_;
	private static boolean isSelectedProgrammingGreeting_;
	private static boolean isSelectedProgrammingTowerOfHanoi_;
	private static boolean isSelectedReasoningFamily_;

	private static short currentSubMenu_;
	private static short nSubMenuButtons_;
	private static short nProgrammingConnect4SubMenuButtons_;
	private static short nProgrammingTowerOfHanoiSubMenuButtons_;
	private static short nReasoningFamilyDefinitionSubMenuButtons_;
	private static short nReasoningFamilyConflictSubMenuButtons_;
	private static short nReasoningFamilyJustificationSubMenuButtons_;
	private static short nReasoningFamilyQuestionSubMenuButtons_;
	private static short nReasoningFamilyShowInfoSubMenuButtons_;
	private static short nHelpSubMenuButtons_;

	private static int currentFrameWidth_;
	private static int currentFrameHeight_;
	private static int currentPreferredOutputScrollPaneSize_;
	private static int preferredSubMenuHeight_;

	private static String inputString_;
	private static String errorHeaderString_;
	private static StringBuffer errorStringBuffer_;

	private static WordItem currentInterfaceLanguageWordItem_;

	// Private non-static components
	private JFileChooser fileChooser_;

	// Private static components - Output area
	private static JScrollPane outputScrollPane_;
	private static JTextArea outputArea_;

	// Private static components - Panels
	private static JPanel upperPanel_;
	private static JPanel mainMenuPanel_;
	private static JPanel subMenuPanel_;
	private static JPanel inputPanel_;

	// Private static components - Upper menu
	private static JButton clearYourMindButton_;
	private static JButton restartButton_;
	private static JButton undoButton_;
	private static JButton redoButton_;
	private static JButton selectExampleFile_;
	private static JLabel statusLabel_;
	private static JPasswordField passwordField_;
	private static JProgressBar progressBar_;

	// Private static components - Main menu
	private static JButton ambiguitySubMenuButton_;
	private static JButton readTheFileAmbiguityBostonButton_;
	private static JButton readTheFileAmbiguityPresidentsButton_;

	private static JButton programmingSubMenuButton_;
	private static JButton readTheFileProgrammingConnect4Button_;
	private static JButton readTheFileProgrammingGreetingButton_;
	private static JButton readTheFileProgrammingTowerOfHanoiButton_;

	private static JButton reasoningSubMenuButton_;
	private static JButton readTheFileReasoningFamilyButton_;
	private static JButton familyDefinitionsSubMenuButton_;
	private static JButton familyConflictsSubMenuButton_;
	private static JButton familyJustificationSubMenuButton_;
	private static JButton familyQuestionsSubMenuButton_;
	private static JButton familyShowInfoSubMenuButton_;

	private static JButton backButton_;
	private static JButton helpButton_;

	// Private static components - sub-menus
	private static JLabel initialRemarkLabel1_;
	private static JLabel initialRemarkLabel2_;
	private static JLabel initialRemarkLabel3_;
	private static JLabel initialRemarkLabel4_;
	private static JLabel initialRemarkLabel5_;

	private static JButton[] subMenuButtonArray_ = new JButton[Constants.CONSOLE_MAX_SUBMENU_BUTTONS];

	// Private static components - input area
	private static JTextField inputField_;


	// Private non-static methods

	private void resizeFrameComponents( ComponentEvent componentEvent )
		{
		Component component;

		if( componentEvent != null &&
		( component = componentEvent.getComponent() ) != null )
			resizeFrame( component.getWidth(), component.getHeight() );
		}


	// Private static methods

	private static void waitForConsoleToBeReady()
		{
		while( !isConsoleReady_ )
			{
			try {
				Thread.sleep( Constants.CONSOLE_SLEEP_TIME );
				}
			catch( InterruptedException ie )
				{
				ie.printStackTrace();
				}
			}
		}

	private static void setSubMenuButtonText()
		{
		if( currentInterfaceLanguageWordItem_ != null )
			{
			switch( currentSubMenu_ )
				{
				case Constants.CONSOLE_SUBMENU_PROGRAMMING_CONNECT4:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_A ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_B ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_C ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_D ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_E ) );
					subMenuButtonArray_[5].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_F ) );
					subMenuButtonArray_[6].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_MY_SET_IS_G ) );
					subMenuButtonArray_[7].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_SHOW_INFO_ABOUT_THE_SET ) );
					subMenuButtonArray_[8].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_THE_SOLVE_LEVEL_IS_LOW ) );
					subMenuButtonArray_[9].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CONNECT4_THE_SOLVE_LEVEL_IS_HIGH ) );

					nProgrammingConnect4SubMenuButtons_ = 10;
					break;

				case Constants.CONSOLE_SUBMENU_PROGRAMMING_TOWER_OF_HANOI:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_TOWER_OF_HANOI_EVEN_NUMBER_OF_DISCS ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_TOWER_OF_HANOI_ODD_NUMBER_OF_DISCS ) );

					nProgrammingTowerOfHanoiSubMenuButtons_ = 2;
					break;

				case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOHN_ANN ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_SON ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOE_IS_A_SON ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_DAUGHTER ) );

					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_SON_OF_JOHN_AND_ANN ) );
					subMenuButtonArray_[5].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOE_IS_A_SON_OF_JOHN_AND_ANN ) );
					subMenuButtonArray_[6].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_DAUGHTER_OF_JOHN_AND_ANN ) );

					subMenuButtonArray_[7].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_FATHER ) );
					subMenuButtonArray_[8].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_ANN_IS_A_MOTHER ) );

					subMenuButtonArray_[9].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_PARENT ) );
					subMenuButtonArray_[10].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_ANN_IS_A_PARENT ) );

					subMenuButtonArray_[11].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOHN_IS_A_MAN ) );
					subMenuButtonArray_[12].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_ANN_IS_A_WOMAN ) );
					subMenuButtonArray_[13].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_PAUL_IS_A_MAN ) );
					subMenuButtonArray_[14].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JOE_IS_A_MAN ) );
					subMenuButtonArray_[15].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_LAURA_IS_A_WOMAN ) );

					nReasoningFamilyDefinitionSubMenuButtons_ = 16;
					break;

				case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_JOHN_IS_A_WOMAN ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_JOHN_IS_THE_MOTHER_PETE ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_ANN_IS_A_SON ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_PAUL_IS_A_DAUGHTER ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_JOE_IS_A_MOTHER ) );

					nReasoningFamilyConflictSubMenuButtons_ = 5;
					break;

				case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_PARENTS ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_CHILDREN ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_JOHN ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_ANN ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_PAUL ) );
					subMenuButtonArray_[5].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_JOE ) );
					subMenuButtonArray_[6].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_THE_JUSTIFICATION_REPORT_FOR_LAURA ) );

					nReasoningFamilyJustificationSubMenuButtons_ = 7;
					break;

				case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_A_FATHER ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_A_MOTHER ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_THE_FATHER_OF_PAUL ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_JOHN_THE_MOTHER_OF_PAUL ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_MAN ) );
					subMenuButtonArray_[5].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_WOMAN ) );
					subMenuButtonArray_[6].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_MAN_OR_A_WOMAN ) );
					subMenuButtonArray_[7].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_SON ) );
					subMenuButtonArray_[8].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_DAUGHTER ) );
					subMenuButtonArray_[9].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_IS_PAUL_A_SON_OR_A_DAUGHTER ) );

					nReasoningFamilyQuestionSubMenuButtons_ = 10;
					break;

				case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_JOHN ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_ANN ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_PAUL ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_JOE ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_ABOUT_LAURA ) );

					nReasoningFamilyShowInfoSubMenuButtons_ = 5;
					break;

				case Constants.CONSOLE_SUBMENU_HELP:
					subMenuButtonArray_[0].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_INFO_ABOUT_THE_LISTS ) );
					subMenuButtonArray_[1].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_INFO_ABOUT_THE_WORD_TYPES ) );
					subMenuButtonArray_[2].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_THE_QUERY_COMMANDS ) );
					subMenuButtonArray_[3].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_THE_COPYRIGHT ) );
					subMenuButtonArray_[4].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_THE_GPLv2_LICENSE ) );
					subMenuButtonArray_[5].setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP_SHOW_THE_WARRANTY ) );

					nHelpSubMenuButtons_ = 6;
					break;
				}
			}
		}

	private static void setInterfaceLanguage()
		{
		if( CommonVariables.currentInterfaceLanguageWordItem != null &&
		CommonVariables.currentInterfaceLanguageWordItem != currentInterfaceLanguageWordItem_ )
			{
			currentInterfaceLanguageWordItem_ = CommonVariables.currentInterfaceLanguageWordItem;
			
			// Upper panel buttons
			clearYourMindButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_CLEAR_YOUR_MIND ) );
			restartButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_RESTART ) );
			undoButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_UNDO ) );
			redoButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_REDO ) );
			selectExampleFile_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_MORE_EXAMPLES ) );

			// Main menu buttons
			ambiguitySubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_AMBIGUITY_SUBMENU ) );
			readTheFileAmbiguityBostonButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_AMBIGUITY_BOSTON ) );
			readTheFileAmbiguityPresidentsButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_AMBIGUITY_PRESIDENTS ) );

			programmingSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_PROGRAMMING_SUBMENU ) );
			readTheFileProgrammingConnect4Button_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_CONNECT4 ) );
			readTheFileProgrammingGreetingButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_GREETING ) );
			readTheFileProgrammingTowerOfHanoiButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_PROGRAMMING_TOWER_OF_HANOI ) );

			reasoningSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_REASONING_SUBMENU ) );
			readTheFileReasoningFamilyButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_THE_FILE_REASONING_FAMILY ) );
			familyDefinitionsSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SUBMENU ) );
			familyConflictsSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_CONFLICT_SUBMENU ) );
			familyJustificationSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_JUSTIFICATION_REPORT_SUBMENU ) );
			familyQuestionsSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_QUESTION_SUBMENU ) );
			familyShowInfoSubMenuButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_FAMILY_SHOW_INFO_SUBMENU ) );

			backButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_BACK ) );
			helpButton_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP ) );

			// Initial remark labels
			initialRemarkLabel1_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_INITIAL_REMARK_LABEL1 ) );
			initialRemarkLabel2_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_INITIAL_REMARK_LABEL2 ) );
			initialRemarkLabel3_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_INITIAL_REMARK_LABEL3 ) );
			initialRemarkLabel4_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_INITIAL_REMARK_LABEL4 ) );
			initialRemarkLabel5_.setText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_INITIAL_REMARK_LABEL5 ) );

			setSubMenuButtonText();
			}
		}

	private static void enableMenus( boolean enabledNormalButtons, boolean enableSubMenuButtons )
		{
		// Upper menu
		clearYourMindButton_.setEnabled( enabledNormalButtons );
		restartButton_.setEnabled( enabledNormalButtons );
		undoButton_.setEnabled( enabledNormalButtons );
		redoButton_.setEnabled( enabledNormalButtons );
		selectExampleFile_.setEnabled( enabledNormalButtons );

		ambiguitySubMenuButton_.setEnabled( enabledNormalButtons );
		ambiguitySubMenuButton_.setVisible( currentSubMenu_ == Constants.CONSOLE_SUBMENU_INIT || currentSubMenu_ == Constants.CONSOLE_SUBMENU_HELP );

		programmingSubMenuButton_.setEnabled( enabledNormalButtons );
		programmingSubMenuButton_.setVisible( currentSubMenu_ == Constants.CONSOLE_SUBMENU_INIT || currentSubMenu_ == Constants.CONSOLE_SUBMENU_HELP );

		reasoningSubMenuButton_.setEnabled( enabledNormalButtons );
		reasoningSubMenuButton_.setVisible( currentSubMenu_ == Constants.CONSOLE_SUBMENU_INIT || currentSubMenu_ == Constants.CONSOLE_SUBMENU_HELP );

		// Main menu
		if( currentSubMenu_ == Constants.CONSOLE_SUBMENU_AMBIGUITY )
			{
			readTheFileAmbiguityBostonButton_.setEnabled( isSelectedAmbiguityBoston_ ? false : enabledNormalButtons );
			readTheFileAmbiguityBostonButton_.setVisible( true );

			readTheFileAmbiguityPresidentsButton_.setEnabled( isSelectedAmbiguityPresidents_ ? false : enabledNormalButtons );
			readTheFileAmbiguityPresidentsButton_.setVisible( true );
			}
		else
			{
			readTheFileAmbiguityBostonButton_.setVisible( false );
			readTheFileAmbiguityPresidentsButton_.setVisible( false );
			}

		if( currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING_CONNECT4 ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING_GREETING ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING_TOWER_OF_HANOI )
			{
			readTheFileProgrammingConnect4Button_.setEnabled( isSelectedProgrammingConnect4_ ? false : enabledNormalButtons );
			readTheFileProgrammingConnect4Button_.setVisible( true );

			readTheFileProgrammingGreetingButton_.setEnabled( isSelectedProgrammingGreeting_ ? false : enabledNormalButtons );
			readTheFileProgrammingGreetingButton_.setVisible( true );

			readTheFileProgrammingTowerOfHanoiButton_.setEnabled( isSelectedProgrammingTowerOfHanoi_ ? false : enabledNormalButtons );
			readTheFileProgrammingTowerOfHanoiButton_.setVisible( true );
			}
		else
			{
			readTheFileProgrammingConnect4Button_.setVisible( false );
			readTheFileProgrammingGreetingButton_.setVisible( false );
			readTheFileProgrammingTowerOfHanoiButton_.setVisible( false );
			}

		if( currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO )
			{
			readTheFileReasoningFamilyButton_.setEnabled( isSelectedReasoningFamily_ ? false : enabledNormalButtons );
			readTheFileReasoningFamilyButton_.setVisible( true );

			familyDefinitionsSubMenuButton_.setEnabled( isSelectedReasoningFamily_ && currentSubMenu_ != Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS ? enabledNormalButtons : false );
			familyDefinitionsSubMenuButton_.setVisible( isSelectedReasoningFamily_ );

			familyConflictsSubMenuButton_.setEnabled( isSelectedReasoningFamily_ && currentSubMenu_ != Constants.CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS ? enabledNormalButtons : false );
			familyConflictsSubMenuButton_.setVisible( isSelectedReasoningFamily_ );

			familyJustificationSubMenuButton_.setEnabled( isSelectedReasoningFamily_ && currentSubMenu_ != Constants.CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT ? enabledNormalButtons : false );
			familyJustificationSubMenuButton_.setVisible( isSelectedReasoningFamily_ );

			familyQuestionsSubMenuButton_.setEnabled( isSelectedReasoningFamily_ && currentSubMenu_ != Constants.CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS ? enabledNormalButtons : false );
			familyQuestionsSubMenuButton_.setVisible( isSelectedReasoningFamily_ );

			familyShowInfoSubMenuButton_.setEnabled( isSelectedReasoningFamily_ && currentSubMenu_ != Constants.CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO ? enabledNormalButtons : false );
			familyShowInfoSubMenuButton_.setVisible( isSelectedReasoningFamily_ );
			}
		else
			{
			readTheFileReasoningFamilyButton_.setVisible( false );

			familyDefinitionsSubMenuButton_.setVisible( false );
			familyConflictsSubMenuButton_.setVisible( false );
			familyJustificationSubMenuButton_.setVisible( false );
			familyQuestionsSubMenuButton_.setVisible( false );
			familyShowInfoSubMenuButton_.setVisible( false );
			}

		backButton_.setEnabled( enabledNormalButtons );
		backButton_.setVisible( currentSubMenu_ != Constants.CONSOLE_SUBMENU_INIT && currentSubMenu_ != Constants.CONSOLE_SUBMENU_HELP  );

		helpButton_.setEnabled( enabledNormalButtons );

		// Sub-menu
		if( currentSubMenu_ != Constants.CONSOLE_SUBMENU_INIT )
			{
			for( short index = 0; index < Constants.CONSOLE_MAX_SUBMENU_BUTTONS; index++ )
				subMenuButtonArray_[index].setEnabled( enableSubMenuButtons );
			}

		resizeFrame( currentFrameWidth_, currentFrameHeight_ );		// The calculation of the GUI components can go wrong on a slow systems.
		resizeFrame( currentFrameWidth_, currentFrameHeight_ );		// Another call should solve this problem.
		}

	private static void setUpperMenuVisible( boolean isVisible )
		{
		clearYourMindButton_.setVisible( isVisible );
		restartButton_.setVisible( isVisible );
		undoButton_.setVisible( isVisible );
		redoButton_.setVisible( isVisible );
		selectExampleFile_.setVisible( isVisible );
		}

	private static void setSubMenuVisible( boolean skipChangingCurrentSettingSubMenu, short subMenu )
		{
		boolean showInitButtons = false;

		if( !skipChangingCurrentSettingSubMenu )
			currentSubMenu_ = subMenu;

		setSubMenuButtonText();

		switch( subMenu )
			{
			case Constants.CONSOLE_SUBMENU_PROGRAMMING_CONNECT4:
				nSubMenuButtons_ = nProgrammingConnect4SubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_PROGRAMMING_TOWER_OF_HANOI:
				nSubMenuButtons_ = nProgrammingTowerOfHanoiSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS:
				nSubMenuButtons_ = nReasoningFamilyDefinitionSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS:
				nSubMenuButtons_ = nReasoningFamilyConflictSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT:
				nSubMenuButtons_ = nReasoningFamilyJustificationSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS:
				nSubMenuButtons_ = nReasoningFamilyQuestionSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO:
				nSubMenuButtons_ = nReasoningFamilyShowInfoSubMenuButtons_;
				break;

			case Constants.CONSOLE_SUBMENU_HELP:
				nSubMenuButtons_ = nHelpSubMenuButtons_;
				break;

			default:
				showInitButtons = true;
				nSubMenuButtons_ = 0;
			}

		initialRemarkLabel1_.setVisible( showInitButtons );
		initialRemarkLabel2_.setVisible( showInitButtons );
		initialRemarkLabel3_.setVisible( showInitButtons );
		initialRemarkLabel4_.setVisible( showInitButtons );
		initialRemarkLabel5_.setVisible( showInitButtons );

		for( short index = 0; index < Constants.CONSOLE_MAX_SUBMENU_BUTTONS; index++ )
			subMenuButtonArray_[index].setVisible( index < nSubMenuButtons_ );
		}

	private static void resizeFrame( int newFrameWidth, int newFrameHeight )
		{
		int preferredOutputScrollPaneSize;

		currentFrameWidth_ = newFrameWidth;
		currentFrameHeight_ = newFrameHeight;
		preferredSubMenuHeight_ = lastVisibleSubMenuButtonY();

		subMenuPanel_.setPreferredSize( new Dimension( 0, preferredSubMenuHeight_ ) );
		subMenuPanel_.revalidate();

		preferredOutputScrollPaneSize = ( newFrameHeight - ( preferredSubMenuHeight_ + 3 * Constants.CONSOLE_BUTTON_PANE_HEIGHT + 2 * 5 * Constants.CONSOLE_BORDER_SIZE ) );

		if( currentPreferredOutputScrollPaneSize_ != preferredOutputScrollPaneSize )
			{
			outputScrollPane_.setPreferredSize( new Dimension( 0, preferredOutputScrollPaneSize ) );
			outputScrollPane_.revalidate();

			currentPreferredOutputScrollPaneSize_ = preferredOutputScrollPaneSize;

			if( isConsoleReady_ )
				{
				// Update output area
				outputArea_.updateUI();

				// Go to end of text in output area
				outputArea_.setCaretPosition( outputArea_.getDocument().getLength() );
				}
			}
		}

	private static int lastVisibleSubMenuButtonY()
		{
		int y = 0;

		if( currentSubMenu_ == Constants.CONSOLE_SUBMENU_INIT ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_AMBIGUITY ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_PROGRAMMING_GREETING ||
		currentSubMenu_ == Constants.CONSOLE_SUBMENU_REASONING )
			y = initialRemarkLabel5_.getY();
		else
			{
			if( nSubMenuButtons_ > 0 )
				y = subMenuButtonArray_[nSubMenuButtons_ - 1].getY();
			}

		return Constants.CONSOLE_BUTTON_PANE_HEIGHT + y;
		}

	private static String getInputString()
		{
		inputString_ = null;

		do	{
			try {
				Thread.sleep( Constants.CONSOLE_SLEEP_TIME );
				}
			catch( InterruptedException ie )
				{
				ie.printStackTrace();
				}
			}
		while( inputString_ == null );

		return inputString_;
		}


	// Constructor

	public Console( Dimension frameSize )
		{
		super( new GridBagLayout() );

		// Create output area
		outputArea_ = new JTextArea();
		outputArea_.setEditable( false );
		outputArea_.setLineWrap( true );
		outputArea_.setWrapStyleWord( true );

		// Set a mono-spaced (= non-proportional) font for output area
		outputArea_.setFont( new Font( Constants.CONSOLE_MONO_SPACED_FONT, Font.PLAIN, 13 ) );

		// Create scroll pane with border for output area
		outputScrollPane_ = new JScrollPane( outputArea_ );
		outputScrollPane_.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		outputScrollPane_.setPreferredSize( new Dimension( 0, frameSize.getSize().height - ( 4 * Constants.CONSOLE_BUTTON_PANE_HEIGHT + 2 * 5 * Constants.CONSOLE_BORDER_SIZE ) ) );
		outputScrollPane_.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder( Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE ),
						outputScrollPane_.getBorder() ) );

		// Create upper panel with border
		upperPanel_ = new JPanel();
		GridBagLayout upperPaneGridbag = new GridBagLayout();
		upperPanel_.setLayout( upperPaneGridbag );
		upperPanel_.setPreferredSize( new Dimension( 0, Constants.CONSOLE_BUTTON_PANE_HEIGHT ) );
		upperPanel_.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder( Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE ),
						upperPanel_.getBorder() ) );

		// Create main menu with border
		mainMenuPanel_ = new JPanel();
		mainMenuPanel_.setPreferredSize( new Dimension( 0, Constants.CONSOLE_BUTTON_PANE_HEIGHT ) );
		mainMenuPanel_.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder( Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE ),
						mainMenuPanel_.getBorder() ) );

		// Create sub-menu panel with border
		subMenuPanel_ = new JPanel();
		subMenuPanel_.setPreferredSize( new Dimension( 0, Constants.CONSOLE_BUTTON_PANE_HEIGHT ) );
		subMenuPanel_.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder( Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE ),
						subMenuPanel_.getBorder() ) );

		// Create input panel with border
		inputPanel_ = new JPanel();
		GridBagLayout inputPaneGridbag = new GridBagLayout();
		inputPanel_.setLayout( inputPaneGridbag );
		inputPanel_.setPreferredSize( new Dimension( 0, Constants.CONSOLE_BUTTON_PANE_HEIGHT ) );
		inputPanel_.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder( Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE, Constants.CONSOLE_BORDER_SIZE ),
						inputPanel_.getBorder() ) );

		// Create the file chooser
		fileChooser_ = new JFileChooser( CommonVariables.currentPathStringBuffer + Constants.EXAMPLES_DIRECTORY_NAME_STRING );

		// Create the status label
		statusLabel_ = new JLabel();

		// Create progress bar
		progressBar_ = new JProgressBar();
		progressBar_.setVisible( false );
		progressBar_.setStringPainted( true );

		// Create upper panel buttons
		clearYourMindButton_ = new JButton();
		clearYourMindButton_.addActionListener( this );

		restartButton_ = new JButton();
		restartButton_.addActionListener( this );

		undoButton_ = new JButton();
		undoButton_.addActionListener( this );

		redoButton_ = new JButton();
		redoButton_.addActionListener( this );

		selectExampleFile_ = new JButton();
		selectExampleFile_.addActionListener( this );

		// Create the main menu buttons
		ambiguitySubMenuButton_ = new JButton();
		ambiguitySubMenuButton_.addActionListener( this );

		readTheFileAmbiguityBostonButton_ = new JButton();
		readTheFileAmbiguityBostonButton_.addActionListener( this );

		readTheFileAmbiguityPresidentsButton_ = new JButton();
		readTheFileAmbiguityPresidentsButton_.addActionListener( this );

		programmingSubMenuButton_ = new JButton();
		programmingSubMenuButton_.addActionListener( this );

		readTheFileProgrammingConnect4Button_ = new JButton();
		readTheFileProgrammingConnect4Button_.addActionListener( this );

		readTheFileProgrammingGreetingButton_ = new JButton();
		readTheFileProgrammingGreetingButton_.addActionListener( this );

		readTheFileProgrammingTowerOfHanoiButton_ = new JButton();
		readTheFileProgrammingTowerOfHanoiButton_.addActionListener( this );

		reasoningSubMenuButton_ = new JButton();
		reasoningSubMenuButton_.addActionListener( this );

		readTheFileReasoningFamilyButton_ = new JButton();
		readTheFileReasoningFamilyButton_.addActionListener( this );

		familyDefinitionsSubMenuButton_ = new JButton();
		familyDefinitionsSubMenuButton_.addActionListener( this );

		familyConflictsSubMenuButton_ = new JButton();
		familyConflictsSubMenuButton_.addActionListener( this );

		familyJustificationSubMenuButton_ = new JButton();
		familyJustificationSubMenuButton_.addActionListener( this );

		familyQuestionsSubMenuButton_ = new JButton();
		familyQuestionsSubMenuButton_.addActionListener( this );

		familyShowInfoSubMenuButton_ = new JButton();
		familyShowInfoSubMenuButton_.addActionListener( this );

		backButton_ = new JButton();
		backButton_.addActionListener( this );

		helpButton_ = new JButton();
		helpButton_.addActionListener( this );

		// Create initial remark labels
		initialRemarkLabel1_ = new JLabel();
		initialRemarkLabel2_ = new JLabel();
		initialRemarkLabel3_ = new JLabel();
		initialRemarkLabel4_ = new JLabel();
		initialRemarkLabel5_ = new JLabel();

		// Create buttons of sub-menu
		for( short index = 0; index < Constants.CONSOLE_MAX_SUBMENU_BUTTONS; index++ )
			subMenuButtonArray_[index] = new JButton();

		for( short index = 0; index < Constants.CONSOLE_MAX_SUBMENU_BUTTONS; index++ )
			subMenuButtonArray_[index].addActionListener( this );

		// Create input field
		inputField_ = new JTextField();
		inputField_.setFont( new Font( Constants.CONSOLE_MONO_SPACED_FONT, Font.PLAIN, 13 ) );	// Set a mono-spaced (= non-proportional) font for input field
		inputField_.addActionListener( this );

		// Create password field
		passwordField_ = new JPasswordField();
		passwordField_.setVisible( false );
		passwordField_.addActionListener( this );

		// Create grid bag constraints
		GridBagConstraints westOrientedConstraints = new GridBagConstraints();
		westOrientedConstraints.gridwidth = GridBagConstraints.WEST;
		westOrientedConstraints.fill = GridBagConstraints.BOTH;
		westOrientedConstraints.weightx = 1.0;
		westOrientedConstraints.weighty = 1.0;

		GridBagConstraints eastOrientedConstraints = new GridBagConstraints();
		eastOrientedConstraints.gridwidth = GridBagConstraints.EAST;
		eastOrientedConstraints.fill = GridBagConstraints.BOTH;
		eastOrientedConstraints.weightx = 1.0;
		eastOrientedConstraints.weighty = 1.0;

		GridBagConstraints remainderOrientedConstraints = new GridBagConstraints();
		remainderOrientedConstraints.gridwidth = GridBagConstraints.REMAINDER;
		remainderOrientedConstraints.fill = GridBagConstraints.BOTH;
		remainderOrientedConstraints.weightx = 1.0;
		remainderOrientedConstraints.weighty = 1.0;

		// Add fields to upper panel
		upperPanel_.add( statusLabel_, westOrientedConstraints );
		upperPanel_.add( progressBar_, eastOrientedConstraints );
		upperPanel_.add( clearYourMindButton_ );
		upperPanel_.add( restartButton_ );
		upperPanel_.add( undoButton_ );
		upperPanel_.add( redoButton_ );
		upperPanel_.add( selectExampleFile_ );

		// Add buttons to main menu panel
		mainMenuPanel_.add( ambiguitySubMenuButton_ );
		mainMenuPanel_.add( readTheFileAmbiguityBostonButton_ );
		mainMenuPanel_.add( readTheFileAmbiguityPresidentsButton_ );

		mainMenuPanel_.add( programmingSubMenuButton_ );
		mainMenuPanel_.add( readTheFileProgrammingConnect4Button_ );
		mainMenuPanel_.add( readTheFileProgrammingGreetingButton_ );
		mainMenuPanel_.add( readTheFileProgrammingTowerOfHanoiButton_ );

		mainMenuPanel_.add( reasoningSubMenuButton_ );
		mainMenuPanel_.add( readTheFileReasoningFamilyButton_ );
		mainMenuPanel_.add( familyDefinitionsSubMenuButton_ );
		mainMenuPanel_.add( familyConflictsSubMenuButton_ );
		mainMenuPanel_.add( familyJustificationSubMenuButton_ );
		mainMenuPanel_.add( familyQuestionsSubMenuButton_ );
		mainMenuPanel_.add( familyShowInfoSubMenuButton_ );

		mainMenuPanel_.add( backButton_ );
		mainMenuPanel_.add( helpButton_ );

		// Add initial button remark label to sub-menu panel
		subMenuPanel_.add( initialRemarkLabel1_ );
		subMenuPanel_.add( initialRemarkLabel2_ );
		subMenuPanel_.add( initialRemarkLabel3_ );
		subMenuPanel_.add( initialRemarkLabel4_ );
		subMenuPanel_.add( initialRemarkLabel5_ );

		// Add buttons to sub-menu panel
		for( short index = 0; index < Constants.CONSOLE_MAX_SUBMENU_BUTTONS; index++ )
			subMenuPanel_.add( subMenuButtonArray_[index] );

		// Add input field to input panel
		inputPanel_.add( inputField_, remainderOrientedConstraints );
		inputPanel_.add( passwordField_, remainderOrientedConstraints );

		// Add pane/panels to the frame
		add( outputScrollPane_, remainderOrientedConstraints );
		add( upperPanel_, remainderOrientedConstraints );
		add( mainMenuPanel_, remainderOrientedConstraints );
		add( subMenuPanel_, remainderOrientedConstraints );
		add( inputPanel_, remainderOrientedConstraints );
		}


	// Protected static methods

	protected static void restart()
		{
		// Initialize private static variables
		isSelectedAmbiguityBoston_ = false;
		isSelectedAmbiguityPresidents_ = false;
		isSelectedProgrammingConnect4_ = false;
		isSelectedProgrammingGreeting_ = false;
		isSelectedProgrammingTowerOfHanoi_ = false;
		isSelectedReasoningFamily_ = false;

		preferredSubMenuHeight_ = lastVisibleSubMenuButtonY();
		currentSubMenu_ = Constants.CONSOLE_SUBMENU_INIT;

		nProgrammingConnect4SubMenuButtons_ = 0;
		nProgrammingTowerOfHanoiSubMenuButtons_ = 0;
		nReasoningFamilyDefinitionSubMenuButtons_ = 0;
		nReasoningFamilyConflictSubMenuButtons_ = 0;
		nReasoningFamilyJustificationSubMenuButtons_ = 0;
		nReasoningFamilyQuestionSubMenuButtons_ = 0;
		nReasoningFamilyShowInfoSubMenuButtons_ = 0;
		nHelpSubMenuButtons_ = 0;

		errorHeaderString_ = null;
		errorStringBuffer_ = null;

		currentInterfaceLanguageWordItem_ = null;

		// Disable menus
		enableMenus( false, false );

		// Initialize sub-menus
		setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_INIT );

		// Clear screen
		outputArea_.setText( Constants.NEW_LINE_STRING );

		// Clear possible restart sentence in input field
		inputField_.setText( Constants.EMPTY_STRING );

		// Disable input field
		inputField_.setEnabled( false );
		}

	protected static void writeText( String textString )
		{
		Document document;

		waitForConsoleToBeReady();

		if( textString != null )
			{
				// Show text in output area
				outputArea_.append( textString );

				if( ( document = outputArea_.getDocument() ) != null )
					// Go to end of text in output area
					outputArea_.setCaretPosition( document.getLength() );
			}
		else
			{
			addError( "Class Console;\nMethod: writeText;\nError: The given text string is undefined." );
			showError();
			}
		}

	protected static void showStatus( String newStatusString )
		{
		if( statusLabel_ != null )
			statusLabel_.setText( newStatusString == null ? Constants.EMPTY_STRING : newStatusString );
		}

	protected static void startProgress( int startProgress, int maxProgress, String progressString )
		{
		progressBar_.setValue( startProgress );
		progressBar_.setMaximum( maxProgress );

		showStatus( progressString );
		setUpperMenuVisible( false );
		progressBar_.setVisible( true );
		}

	protected static void showProgress( int currentProgress )
		{
		progressBar_.setValue( currentProgress );
		}

	protected static void clearProgress()
		{
		progressBar_.setVisible( false );
		showStatus( null );
		}

protected static void showError()
		{
		if( errorStringBuffer_ != null )
			{
			JTextArea errorTextArea = new JTextArea( errorStringBuffer_.toString() );
			errorTextArea.setEditable( false );
			JScrollPane errorScrollPane = new JScrollPane( errorTextArea );
			errorScrollPane.setPreferredSize( new Dimension( Constants.CONSOLE_ERROR_PANE_WIDTH, Constants.CONSOLE_ERROR_PANE_HEIGHT ) );
			JOptionPane.showMessageDialog( null, errorScrollPane, ( errorHeaderString_ == null ? Constants.PRESENTATION_ERROR_INTERNAL_TITLE_STRING : Constants.PRESENTATION_ERROR_INTERNAL_TITLE_STRING + errorHeaderString_ ), JOptionPane.ERROR_MESSAGE );
			errorHeaderString_ = null;
			errorStringBuffer_ = null;
			}
		}

	protected static void addError( String newErrorString )
		{
		if( errorStringBuffer_ == null )
			errorStringBuffer_ = new StringBuffer( newErrorString );
		else
			errorStringBuffer_.append( newErrorString ); 
		}

	protected static void addError( String newHeaderString, String newErrorString )
		{
		errorHeaderString_ = new String( newHeaderString );
		addError( newErrorString ); 
		}

	protected static String getPassword()
		{
		if( errorStringBuffer_ != null )
			showError();

		// Prepare password field
		inputField_.setVisible( false );
		passwordField_.setVisible( true );
		passwordField_.requestFocus();
		passwordField_.setText( Constants.EMPTY_STRING );	// Clear previous password

		inputString_ = getInputString();

		// Hide password field again
		inputField_.setVisible( true );
		passwordField_.setVisible( false );

		return inputString_;
		}

	protected static String readLine( boolean clearInputField )
		{
		if( errorStringBuffer_ != null )
			showError();

		// Set the language of the interface
		setInterfaceLanguage();

		// Prepare input field
		inputField_.setEnabled( true );
		inputField_.requestFocus();

		if( clearInputField )
			inputField_.setText( Constants.EMPTY_STRING );
		else
			{
			// Set upper menu visible
			setUpperMenuVisible( true );

			// Enable menus again
			enableMenus( true, true );

			// Select all text in input field
			inputField_.selectAll();
			}

		inputString_ = getInputString();

		// Disable input field
		inputField_.setEnabled( false );

		return inputString_;
		}


	// Public non-static methods

	public void componentHidden( ComponentEvent componentEvent )
		{
		}

	public void componentMoved( ComponentEvent componentEvent )
		{
		resizeFrameComponents( componentEvent );
		}

	public void componentResized( ComponentEvent componentEvent )
		{
		resizeFrameComponents( componentEvent );
		}

	public void componentShown( ComponentEvent componentEvent )
		{
		isConsoleReady_ = true;
		}

	public void actionPerformed( ActionEvent actionEvent )
		{
		Object actionSource;
		String actionCommandString;

		if( currentInterfaceLanguageWordItem_ != null )
			{
			if( ( actionSource = actionEvent.getSource() ) != null )
				{
				if( ( actionCommandString = actionEvent.getActionCommand() ) != null )
					{
					// Disable menus during action
					enableMenus( false, false );
			
					if( actionSource == passwordField_ )
						inputString_ = String.valueOf( passwordField_.getPassword() );
					else
						{
						if( actionSource == inputField_ )
							inputString_ = inputField_.getText();
						else
							{
							if( actionSource == clearYourMindButton_ )
								{
								isSelectedAmbiguityBoston_ = false;
								isSelectedAmbiguityPresidents_ = false;
								isSelectedProgrammingConnect4_ = false;
								isSelectedProgrammingGreeting_ = false;
								isSelectedProgrammingTowerOfHanoi_ = false;
								isSelectedReasoningFamily_ = false;

								setSubMenuVisible( true, Constants.CONSOLE_SUBMENU_INIT );

								inputString_ = actionCommandString;
								}
							else
								{
								if( actionSource == restartButton_ )
									{
									inputString_ = actionCommandString;
									restart();
									}
								else
									{
									if( actionSource == selectExampleFile_ )
										{
										if( fileChooser_.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION &&
										fileChooser_.getSelectedFile() != null &&
										fileChooser_.getSelectedFile().getPath() != null )
											inputString_ = Presentation.convertDiacriticalText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_FILE_ACTION_START ) ) + fileChooser_.getSelectedFile().getPath() + Presentation.convertDiacriticalText( currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_READ_FILE_ACTION_END ) );
										else
											{
											// When canceled
											enableMenus( true, true );
		
											inputField_.setEnabled( true );
											inputField_.requestFocus();
											}
										}
									else
										{
										if( actionSource == ambiguitySubMenuButton_ )
											{
											setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_AMBIGUITY );
											enableMenus( true, true );
											}
										else
											{
											if( actionSource == readTheFileAmbiguityBostonButton_ )
												{
												isSelectedAmbiguityBoston_ = true;
												inputString_ = actionCommandString;
												}
											else
												{
												if( actionSource == readTheFileAmbiguityPresidentsButton_ )
													{
													isSelectedAmbiguityPresidents_ = true;
													inputString_ = actionCommandString;
													}
												else
													{
													if( actionSource == programmingSubMenuButton_ )
														{
														setSubMenuVisible( false, ( isSelectedProgrammingConnect4_ ? Constants.CONSOLE_SUBMENU_PROGRAMMING_CONNECT4 : Constants.CONSOLE_SUBMENU_PROGRAMMING ) );
														enableMenus( true, true );
														}
													else
														{
														if( actionSource == readTheFileProgrammingConnect4Button_ )
															{
															isSelectedProgrammingConnect4_ = true;
															setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_PROGRAMMING_CONNECT4 );
															inputString_ = actionCommandString;
															}
														else
															{
															if( actionSource == readTheFileProgrammingGreetingButton_ )
																{
																isSelectedProgrammingGreeting_ = true;
																setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_PROGRAMMING_GREETING );
																inputString_ = actionCommandString;
																}
															else
																{
																if( actionSource == readTheFileProgrammingTowerOfHanoiButton_ )
																	{
																	isSelectedProgrammingTowerOfHanoi_ = true;
																	setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_PROGRAMMING_TOWER_OF_HANOI );
																	inputString_ = actionCommandString;
																	}
																else
																	{
																	if( actionSource == reasoningSubMenuButton_ )
																		{
																		setSubMenuVisible( false, ( isSelectedReasoningFamily_ ? Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS : Constants.CONSOLE_SUBMENU_REASONING ) );
																		enableMenus( true, true );
																		}
																	else
																		{
																		if( actionSource == readTheFileReasoningFamilyButton_ )
																			{
																			isSelectedReasoningFamily_ = true;
																			setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS );
																			inputString_ = actionCommandString;
																			}
																		else
																			{
																			if( actionSource == familyDefinitionsSubMenuButton_ )
																				{
																				setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_DEFINITIONS );
																				enableMenus( true, true );
																				}
																			else
																				{
																				if( actionSource == familyConflictsSubMenuButton_ )
																					{
																					setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_CONFLICTS );
																					enableMenus( true, true );
																					}
																				else
																					{
																					if( actionSource == familyJustificationSubMenuButton_ )
																						{
																						setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_JUSTIFICATION_REPORT );
																						enableMenus( true, true );
																						}
																					else
																						{
																						if( actionSource == familyQuestionsSubMenuButton_ )
																							{
																							setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_QUESTIONS );
																							enableMenus( true, true );
																							}
																						else
																							{
																							if( actionSource == familyShowInfoSubMenuButton_ )
																								{
																								setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_REASONING_FAMILY_SHOW_INFO );
																								enableMenus( true, true );
																								}
																							else
																								{
																								if( actionSource == backButton_ )
																									{
																									setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_INIT );
																									enableMenus( true, true );
																									}
																								else
																									{
																									if( actionSource == helpButton_ )
																										{
																										setSubMenuVisible( false, Constants.CONSOLE_SUBMENU_HELP );
																										inputString_ = currentInterfaceLanguageWordItem_.interfaceString( Constants.INTERFACE_CONSOLE_HELP );
																										}
																									else
																										inputString_ = actionCommandString;
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}

						if( inputString_ != null )
							{
							if( actionSource != inputField_ )
								inputField_.setText( inputString_ );
			
							if( actionSource != restartButton_ )
								writeText( inputString_ + Constants.NEW_LINE_STRING );
							}
						}
					}
				else
					{
					addError( "Class Console;\nMethod: actionPerformed;\nError: The action command string is undefined." );
					showError();
					}
				}
			else
				{
				addError( "Class Console;\nMethod: actionPerformed;\nError: The action source is undefined." );
				showError();
				}
			}
		else
			{
			addError( "Class Console;\nMethod: actionPerformed;\nError: The current interface language word is undefined." );
			showError();
			}
		}
	};

/*************************************************************************
 *
 *	"I will love the Lord because he hears my voice
 *	and my prayer for mercy.
 *	Because he bends down to listen,
 *	I will pray as long as I breath!" (Psalm 116:1-2)
 *
 *************************************************************************/
