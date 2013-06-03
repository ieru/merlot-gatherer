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
 * Class for get info from the general materials page http://www.merlot.org/merlot/materials.htm
 */
public class Materials {
    //Attributes
    //The URL of the Materials' page
    private String _url;
    //the code of the materials' page
    private String _code;
    private int _numberObjects;
    private int _numberPages;

    /**
     * Constructor. Gets the number of LOs and the number of pages from the web
     */
    public Materials()
    {
        _url="http://www.merlot.org/merlot/materials.htm";
        _code=getCodeWebPage(_url);
        _numberObjects=extractNumberObjects();
        //System.out.println("_numberObjects=" + _numberObjects);
        _numberPages=extractNumberPages();
        //System.out.println("_numberPages=" + _numberPages);
    }

    /**
     * Users the HTMLParser class to return the code of a webpage
     * @param url
     * @return a string with the webcode
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }

    /**
     * Extracts the number of Materials in total
     * @return the total number of materials in Merlot
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
     * Extracts the total number of pages
     * @return the number of pages on the principal web of materials
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
                 int EndPosition=StartPosition+23;
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
     * It receives the number of the page, and returns a
     * String[] with the Materials' IDs in that page
     * @param pageNumber
     * @return the array of IDs contained in page PageNumber
     */
    public int[] extractLinksInPage(int pageNumber)
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
                 StartString="/merlot/viewMaterial.htm";
                 EndString="\">";
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
         int[] ArrayFinal = null;
         String[] temp = stringFinal.split(";");
            ArrayFinal=new int[temp.length];
                for(int i=0;i<temp.length;i++)
                {
                    ArrayFinal[i]=Integer.parseInt(temp[i]);
                    //System.out.println(arrayIDsInPage[i]);
                }
         return ArrayFinal;
     }

    /**
     * @return the number of pages on the principal materials web
     */
    public int getNumberPages()
    {
        return _numberPages;
    }

    /**
     * The total number of merlot objects
     * @return
     */
    public int getNumberObjects()
    {
        return _numberObjects;
    }
}