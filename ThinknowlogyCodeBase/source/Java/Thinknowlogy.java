/*
 *	Class:		Thinknowlogy
 *	Purpose:	Main class of the Thinknowlogy knowledge technology
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

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

class Thinknowlogy
{
	// Private static methods

	private static void deriveCurrentPath()
	{
		int lastIndex;
		StringBuffer tempStringBuffer;
		String absolutePath = Thinknowlogy.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	
		if( absolutePath.endsWith( Constants.SOURCE_DIRECTORY_NAME_STRING ) )
			lastIndex = absolutePath.length() - Constants.SOURCE_DIRECTORY_NAME_STRING.length() - 1;
		else
		{
			if( absolutePath.endsWith( Constants.DEVELOPMENT_SOURCE_DIRECTORY_NAME_STRING ) )
			{
				lastIndex = absolutePath.length() - Constants.DEVELOPMENT_SOURCE_DIRECTORY_NAME_STRING.length() - 1;
				tempStringBuffer = new StringBuffer ( absolutePath.substring( 0, lastIndex + 1 ) + Constants.DEVELOPMENT_DIRECTORY_NAME_STRING );
				absolutePath = tempStringBuffer.toString(); 
				lastIndex += Constants.DEVELOPMENT_DIRECTORY_NAME_STRING.length();
			}
			else
			{
				if( ( lastIndex = ( absolutePath.lastIndexOf( Constants.SLASH_STRING ) ) ) < 0 )
					lastIndex = absolutePath.lastIndexOf( Constants.BACK_SLASH_STRING );
			}
		}

		// If no absolute path is given - add it
		CommonVariables.currentPathStringBuffer = new StringBuffer( lastIndex < 0 ? absolutePath 
			                                                                      : absolutePath.substring( 0, lastIndex + 1 ) );

		// Replace all '%20' by spaces
		while( ( lastIndex = CommonVariables.currentPathStringBuffer.indexOf( "%20" ) ) > 0 )
			CommonVariables.currentPathStringBuffer.replace( lastIndex, lastIndex + 3, " " );
	}

	private static void createAndShowGUI()
	{
		// Setup screen and frame size for console
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension( ( screenSize.width > Constants.CONSOLE_MAXIMUM_FRAME_WIDTH_AT_STARTUP ? Constants.CONSOLE_MAXIMUM_FRAME_WIDTH_AT_STARTUP 
			                                                                                                       : screenSize.width )
										   , ( screenSize.height - Constants.CONSOLE_TOP_BOTTOM_SPACE ) );

		// Create and set up the window.
		JFrame frame = new JFrame( Constants.PRODUCT_NAME + Constants.SPACE_STRING + Constants.VERSION_NAME );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		// Set the initial position of the frame on the screen
		frame.setBounds( ( ( screenSize.width - frameSize.width ) / 2 ),
						( ( screenSize.height - frameSize.height ) / 2 ),
						frameSize.width,
						frameSize.height );

		// Create and set up the content pane.
		Console console = new Console( frameSize );

		frame.add( console );
		frame.addComponentListener( console );
		frame.setVisible( true );
	}

	private static void runAdmin()
	{
		byte result = Constants.RESULT_OK;
		boolean isRestart = false;
		AdminItem admin;

		do{
			// Start the administrator
			Console.restart();
			admin = new AdminItem();

			if( admin != null )
			{
				// Interact with the administrator
				// until a restart or a serious error occurs
				do{
					result = admin.interact();
					isRestart = admin.isRestart();
				}
				while( result == Constants.RESULT_OK &&
				!isRestart );
			}
		}
		while( admin != null && isRestart );

		if( result != Constants.RESULT_OK )
		{
			Console.showStatus( "System error: The administrator failed to start." );
			Console.showError();	// Force error message
		}
	}


	// Public static methods

	public static void main(String [] args)
	{
		deriveCurrentPath();
		createAndShowGUI();
		runAdmin();
	}
};
