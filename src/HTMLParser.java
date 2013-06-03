/*
=============
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package merlotcrawler;

import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.Config;
import java.net.URL;
import java.net.URLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.List;
import java.util.Iterator;


/**
 *
 * @author Victor Apellaniz
 */
/**
 * This class helps to crawl a webpage
 * 
 */
public class HTMLParser {
    private String _address;

    /**
     * Class' constructor
     * @param address
     */
    public HTMLParser(String address)
    {
        _address=address;
        Config.LoggerProvider = LoggerProvider.DISABLED;
    }
    
    /**
     * It returns the webpage code, formatted
     * @return String code formatted
     */
    public String formatCode()
    {
        String code = getSource(_address).toString();
        code=deleteFragment(code,"<!--Start Top -->",
                         "<!--End Tab Setup -->");
        code=deleteFragment(code,"<!--Start Tabs -->",
                         "<!-- End Tabs -->");
        code=deleteFragment(code,"<!-- Start Footer -->",
                         "<!-- End Footer -->");
        code=deleteFragment(code,"<script","</script>");
        code=deleteFragment(code,"<SCRIPT","</SCRIPT>");
        code=deleteLeapsAndTabs(code);
        code=code.replaceAll("&nbsp;"," ");
        code=deleteElement(code,"<br>");
        code=deleteElement(code,"<BR>");
        code=deleteElement(code,"<nobr>");
        code=deleteElement(code,"</nobr>");
        code=deleteElement(code,"<strong>");
        code=deleteElement(code,"</strong>");
        code=deleteElement(code,"\\&amp;");
        code=deleteExtraSpaces(code);

        return code;
    }
    
    /**
     * Gets the code of the webpage, without formatting
     * @return string code not formatted
     */
    public String getSourceCode()
    {
        String code = getSource(_address).toString();
        return code;
    }
    
    /**
     * Receives the code and deletes the extra blank spaces
     * @param Code
     * @return code without extra spaces
     */
    private String deleteExtraSpaces(String Code)
    {
         try
         {
            int FirstSpacePosition=0;
            FirstSpacePosition=Code.indexOf(' ',FirstSpacePosition);

            while(FirstSpacePosition!=-1)
            {
                int LastSpacePosition=FirstSpacePosition;

                while(LastSpacePosition!=Code.length()-1 &&
                        Code.charAt(LastSpacePosition+1)==' ')
                {
                    LastSpacePosition++;
                }
                if (LastSpacePosition!=FirstSpacePosition)
                {
                    StringBuilder String=new StringBuilder(Code);
                    Code=String.replace(FirstSpacePosition,
                            LastSpacePosition+1, "").toString();
                }
                FirstSpacePosition=Code.indexOf(' ',(LastSpacePosition-
                        (LastSpacePosition-FirstSpacePosition))+1);
            }
         }
        catch(Exception e)
        {
            System.err.print("Error on HTMLParser.deleteExtraSpaces "+
                    e.getMessage());
        }
        return Code;
    }
    /**
     * Deletes a specific element from the web code
     * @param Code
     * @param Element
     * @return code without that specific element
     */
    private String deleteElement(String Code, String Element)
    {
          StringBuilder CodeFixed=new StringBuilder(Code);
        try
        {
            int StartPosition=CodeFixed.indexOf(Element);
            int EndPosition=StartPosition+Element.length();
            while(StartPosition!=-1 && EndPosition!=-1)
            {
                CodeFixed=CodeFixed.delete(StartPosition, EndPosition);
                StartPosition=CodeFixed.indexOf(Element, EndPosition);
                EndPosition=StartPosition+Element.length();
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on HTMLParser.deleteElement(" + Code + ", "
                    + Element + ")" + e.getMessage());
        }
           return CodeFixed.toString();
    }
    /**
     * Deletes a fragment od the code, between start and end string
     * @param Code
     * @param StartString
     * @param EndString
     * @return code without the fragment
     */
    private String deleteFragment
            (String Code,String StartString,String EndString)
    {
        StringBuilder CodeFixed=new StringBuilder(Code);
        try
        {
            int StartPosition=CodeFixed.indexOf(StartString);
            if (StartPosition!=-1)
            {
                int EndPosition=CodeFixed.indexOf(EndString, StartPosition)+
                        EndString.length();
                CodeFixed=CodeFixed.delete(StartPosition, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on HTMLParser.deleteFragment(Code,"
                    + StartString + "," + EndString + ")" + e.getMessage());
        }
         return CodeFixed.toString();
    }
    /**
     * Deletes leaps and labels from code
     * @param Code
     * @return code without leaps and labels
     */
    private String deleteLeapsAndTabs(String Code)
    {
        //This method deletes leaps and tabs from merlot page code
        try
        {
        Code=Code.replaceAll("\t", "");
        Code=Code.replaceAll("\n", "");
        }
        catch(Exception e)
        {
            System.err.print("Error on HTMLParser.deleteLeapsAndTabs"+e.getMessage());
        }

        return Code;
    }

    /**
     * Get a Source object of a webpage
     * @param location
     * @return Source object
     */
    public Source getSource(String location) {
        Source source = null;
        MicrosoftTagTypes.register(); //Recognition of Microsoft special Tags
        PHPTagTypes.register(); //Recognition of PHP special Tags
        MasonTagTypes.register(); //Recognition of Mason special Tags
        try {
            URL url = new URL(location);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(3600000); //20 secs timeout antes
            urlConnection.setReadTimeout(3600000); //20 secs timeout antes
            urlConnection.connect();
            source = new Source(urlConnection);
        } catch (ConnectException ce) {
            System.out.println("Connection error: " + ce.getMessage());
        } catch (FileNotFoundException fe) {
            System.out.println("File not found Exception: " + fe.getMessage());
        } catch (MalformedURLException ue) {
            System.out.println("Malformed Exception: " + ue.getMessage());
        } catch (UnknownHostException he) {
            System.out.println("Unknown Host Exception: " + he.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return source;
    }

    /**
     * Gets an array with all elements of a specific kind
     * @param element
     * @return String array of elements
     */
    public String[] getListAllElements(String element)
    {
        String[] arrayElements=null;
        List<Element> linkList = getSource(_address).getAllElements(element);
        int i = 0;
        for (Iterator mem = linkList.iterator(); mem.hasNext();) {
                Element memElement = (Element) mem.next();
                System.out.println(memElement.toString());
                arrayElements = new String[i+1];
                arrayElements[i] = memElement.toString();
                i++;
        }
        return arrayElements;
    }

    /**
     * Deletes the html labesl from the code
     * @param Code
     * @param StartPosition
     * @param EndPosition
     * @return code without html labels
     */
    public static String deleteHTMLLabels(String Code, int StartPosition,
            int EndPosition)
    {   //This method deletes HTML labels and their content between two
        //positions
       String Result="";
       try
       {
           StringBuilder StringFragment=new StringBuilder(Code.substring
                   (StartPosition, EndPosition));
           int StartPos=StringFragment.indexOf(("<"));
           int EndPos=StringFragment.indexOf(">", StartPos);
           while(StartPos!=-1)
           {
            StringFragment=StringFragment.replace(StartPos, EndPos+1, "");
            StartPos=StringFragment.indexOf(("<"));
            EndPos=StringFragment.indexOf(">", StartPos);
           }
           Result=StringFragment.toString();
       }
       catch(Exception e)
        {
            System.err.print("Error on HTMLParser.deleteHTMLLabels "+e.getMessage());
        }
       return Result;
    }

    /**
     * Delete ticks from the code
     * @param Code
     * @return code without ticks
     */
    public static String deleteTicks(String Code)
    {
        return Code.replaceAll("\'"," ");
    }

    /**
     * Delete inverted bars from the code
     * @param Code
     * @return code without inverted bars
     */
    public static String deleteInvertedBars(String Code)
    {

        return Code.replace('\\',' ');
    }

    /**
     * Converts a date from the merlot webpage to a regular java date
     * @param StringDate
     * @return date converted to java.util.Date
     */
    public static Date convertDate(String StringDate)
      {
          String Month="";
          int NumMonth=0;
          int Day=0;
          int Year=0;
          java.sql.Date Date=new java.sql.Date(0);
          try
          {
              int CommaPosition=StringDate.indexOf(",");
              Month=StringDate.substring(0, CommaPosition-3);
              Day=Integer.valueOf(StringDate.substring(CommaPosition-2,
                      CommaPosition));
              Year=Integer.valueOf(StringDate.substring(CommaPosition+2,
                      CommaPosition+6));

                 if(Month.equals("January")||Month.equals("enero")||Month.equals("Jan")||Month.equals("Ene"))
                      NumMonth=0;

                 else if(Month.equals("February")||Month.equals("febrero")||Month.equals("Feb"))
                      NumMonth=1;

                 else if(Month.equals("March")||Month.equals("marzo")||Month.equals("Mar"))
                      NumMonth=2;

                 else if(Month.equals("April")||Month.equals("abril")||Month.equals("Apr")||Month.equals("Abr"))
                      NumMonth=3;

                  else if(Month.equals("May")||Month.equals("mayo")||Month.equals("May"))
                          NumMonth=4;

                  else if(Month.equals("June")||Month.equals("junio")||Month.equals("Jun"))
                          NumMonth=5;

                  else if(Month.equals ("July")||Month.equals("julio")||Month.equals("Jul"))
                          NumMonth=6;

                  else if(Month.equals("August")||Month.equals("agosto")||Month.equals("Aug")||Month.equals("Ago"))
                          NumMonth=7;

                  else if(Month.equals("September")||Month.equals("septiembre")||Month.equals("Sep"))
                          NumMonth=8;

                  else if(Month.equals("October")||Month.equals("octubre")||Month.equals("Oct"))
                          NumMonth=9;

                  else if(Month.equals("November")||Month.equals("noviembre")||Month.equals("Nov"))
                          NumMonth=10;

                  else if(Month.equals("December")||Month.equals("diciembre")||Month.equals("Dec")||Month.equals("Dic"))
                          NumMonth=11;

              java.util.Calendar JavaDate = java.util.Calendar.getInstance();
              JavaDate.set(Year, NumMonth, Day);
              Date=new java.sql.Date(JavaDate.getTimeInMillis());
        }
        catch(Exception e)
        {
            System.err.print("Error on HTMLParser.convertDate("+StringDate+ ")" +e.getMessage());
        }

          return Date;
      }
}
