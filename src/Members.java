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

/**
 *
 * @author Victor Apellaniz
 */
/**
 * Gets information from http://www.merlot.org/merlot/members.htm
 */
public class Members {
    private String _url;
    private String _code;
    private int _numberObjects;
    private int _numberPages;

    /**
     * Constructor
     */
    public Members()
    {
        _url="http://www.merlot.org/merlot/members.htm";
        _code=getCodeWebPage(_url);
        _numberObjects=extractNumberObjects();
        //System.out.println("_numberObjects=" + _numberObjects);
        _numberPages=extractNumberPages();
        //System.out.println("_numberPages=" + _numberPages);
    }
    /**
     * Users HTMLParser class to get the webpage code
     * @param url
     * @return webpage code
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * Gets the number of users objects in the web
     * @return number of users objects in the web
     */
    private int extractNumberObjects()
    {   //In this method, we extract from HTML code the number of LO's that
        //Merlot contains
        String NumberObjects= "0";
        String StringStart="shown of"; //String from where we will start to
        //look for
        String StringEnd="results"; //String where we will finish to look for

        try
        {//We try to position in the start string
            int StartPosition=_code.indexOf(StringStart);
            StartPosition=_code.indexOf(StringStart,StartPosition);
            if(StartPosition!=-1)
            {   //If it exist, we look for the end string
                 int EndPosition=_code.indexOf(StringEnd,StartPosition);
                 if(EndPosition!=-1)
                     //If end position exists too, we extract the substring
                     //between them, which will be the fact
                     //that we are looking for
                     NumberObjects=_code.substring(StartPosition+
                             StringStart.length()+1,EndPosition-1);
                     NumberObjects = NumberObjects.replaceAll(",", "");
            }
        }
        catch(Exception e)
        {
            System.err.print("Error extracting the number of objects from " +
                    "HTML code "+e.getMessage());
        }
        return Integer.valueOf(NumberObjects);
    }
    /**
     * Gets the number of pages in the members page
     * @return number of pages in the members page
     */
    private int extractNumberPages()
    {   //Method to extract the number of pages with LO's in Merlot
        String NumberPages= "0";
        String StartString="Results page";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {      //If the start string exists, we calculate the end position
                //and extract the substring
                 int EndPosition=StartPosition+24;
                 NumberPages=_code.substring
                         (StartPosition+StartString.length()+6,EndPosition);
                 NumberPages = NumberPages.replace(",", "");
            }
        }
        catch(Exception e)
            {
            System.err.print("Error extracting the number of pages from " +
                    "HTML code "+e.getMessage());
            }
        return Integer.valueOf(NumberPages);
    }
    /**
     * Gets the members' links on a specific page
     * @param pageNumber
     * @return a string array of links on that page
     */
    public String[] extractLinksInPage(int pageNumber)
     {   //Method to extract an array with identifiers from every LO in a page
        String pageUrl;
        if(pageNumber==1)
        {
            //If it's the first page, the URL is the initial
            pageUrl=_url;
        }
        else
        {
            pageUrl=_url + "?page=" + pageNumber;
        }
         String Code = getCodeWebPage(pageUrl);
         int NumberObjects=10;
         int Counter=0;
         String ID;
         String[] ArrayID=new String[NumberObjects];
         String StartString="";
         String EndString="";
         int StartPosition=0;
         int EndPosition=0;

         while(Counter<NumberObjects)
         {  //For every LO in the page
             try
             {  //We go to the start string
                 StartString="/merlot/viewMember.htm";
                 EndString="\" class=\"title";
                 StartPosition=Code.indexOf(StartString,EndPosition);
                  if(StartPosition!=-1)
                 {
                     //We go to a new start string after the previous
                     StartString="?id=";

                     StartPosition=Code.indexOf(StartString, StartPosition);

                     EndPosition=Code.indexOf(EndString, StartPosition);
                     //Extract the ID, located between the start position and
                     //the end position
                     ID=Code.substring(StartPosition+StartString.length(),
                             EndPosition);
                     ArrayID[Counter]=ID;
                 }
                 else
                 ArrayID[Counter]="";
                 Counter++;
             }
             catch(Exception e)
            {
                 System.err.print("Error on Materials.extractLinksInPage "+
                         e.getMessage());
            }
         }
         String stringFinal = "";
         for(int i=0;i<ArrayID.length;i++)
         {
             if (!ArrayID[i].equals(""))
             {
                 if(stringFinal.equals(""))
                 {
                     stringFinal = ArrayID[i] + ";";
                 }
                 else
                 {
                    stringFinal = stringFinal + ArrayID[i] + ";";
                 }
             }
         }
         String[] ArrayFinal = stringFinal.split(";");
         return ArrayFinal;
     }
    /**
     * @return number of pages
     */
    public int getNumberPages()
    {
        return _numberPages;
    }

    /**
     *
     * @return number of objects
     */
    public int getNumberObjects()
    {
        return _numberObjects;
    }

}
