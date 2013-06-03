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
 * Gets info from http://www.merlot.org/merlot/portfolios.htm?userId=
 * @author Victor
 */
public class Portfolios {
    
    private String _url;
    private String _code;

    private int _numberObjects;
    private int _numberPages;
    private int[] _portfoliosIDs;

    /**
     * Constructor
     * @param idUser
     */
    public Portfolios(int idUser)
    {
        //Constructor
        _url="http://www.merlot.org/merlot/portfolios.htm?userId=" + idUser;
        _code=getCodeWebPage(_url);

        _numberObjects=extractNumberObjects();
        //System.out.println("_numberObjects="+_numberObjects);
        _numberPages=extractNumberPages();
        //System.out.println("_numberPages="+_numberPages);
        _portfoliosIDs=extractTotalIDs();
        //for(int i=0;i<_portfoliosIDs.length;i++)
        //{
        //    System.out.println("_portfoliosIDs["+i+"]="+_portfoliosIDs[i]);
        //}


    }
    /**
     * Users HTMLParser class to gets the code from webpage
     * @param url
     * @return code from the webpage
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * Gets the number of objects in the web
     * @return number of objects in the web
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
     * Gets the number of pages on the portfolios web
     * @return number of pages on the portfolios web
     */
    private int extractNumberPages()
    {   //Method to extract the number of pages with LO's in Merlot
        String NumberPages= "0";
        String StartString="Results page";
        String EndString="<!-- Show";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {      //If the start string exists, we calculate the end position
                //and extract the substring
                int EndPosition=_code.indexOf(EndString, StartPosition);
                NumberPages=_code.substring
                         (StartPosition+StartString.length(),EndPosition);
                NumberPages=NumberPages.substring(NumberPages.indexOf("of ")+3, NumberPages.length());
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
     * Extracts the materials' links on a portfolio's page
     * @param pageNumber
     * @return array of materials' links
     */
    private int[] extractLinksInPage(int pageNumber)
     {   //Method to extract an array with identifiers from every LO in a page
        String pageUrl;
        if(pageNumber==1)
        {
            //If it's the first page, the URL is the initial
            pageUrl=_url;
        }
        else
        {
            pageUrl=_url + "&page=" + pageNumber;
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
                 StartString="/merlot/viewPortfolio.htm";
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
                     if(ID.contains("<"))
                     {
                         //It's not a valid ID
                         //System.out.println("ID="+ID);
                         //ArrayID[Counter]="";
                         int NewStartPosition=ID.indexOf(StartString);
                         ID=ID.substring(NewStartPosition+StartString.length(),ID.length());
                     }
                     ArrayID[Counter]=ID;
                 }
                 else
                 ArrayID[Counter]="";
                 Counter++;
             }
             catch(Exception e)
            {
                 System.err.print("Error on Portfolios.extractLinksInPage "+
                         e.getMessage());
            }
         }
         String stringFinal = "";
         for(int i=0;i<ArrayID.length;i++)
         {
             //System.out.println("ArrayID["+i+"]="+ArrayID[i]);
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
         //String[] ArrayFinal = stringFinal.split(";");
         String[] temp = stringFinal.split(";");
         int[] ArrayFinal = new int[temp.length];
         for(int i=0;i<temp.length;i++)
         {
             //System.out.println("temp[i]="+temp[i]);
            ArrayFinal[i]=Integer.parseInt(temp[i]);
         }
         return ArrayFinal;
     }
    /**
     * Gets the total array of IDs in the portfolio
     * @return array with all the materials in the portfolio (in all pages)
     */
    private int[] extractTotalIDs()
    {
        int numberPages = extractNumberPages();
        int countPages = 1;
        int[] arrayTemp = null;
        int[] result = null;
        while (countPages<=numberPages)
        {
            arrayTemp = extractLinksInPage(countPages);
            if (result==null)
            {
                result = arrayTemp;
            }
            else
            {
                //System.out.println("No null");
                int[] arrayTemp2 = new int[arrayTemp.length+result.length];
                System.arraycopy(result, 0, arrayTemp2, 0, result.length);
                System.arraycopy(arrayTemp, 0, arrayTemp2, result.length, arrayTemp.length);
                result = arrayTemp2;
            }
            countPages++;
        }
        return result;
    }


    /**
     * 
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
    /**
     *
     * @return array with all the portfolios IDs
     */
    public int[] getPortfoliosIDs()
    {
        return _portfoliosIDs;
    }

}
