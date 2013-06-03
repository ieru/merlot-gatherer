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
     * Class for retrieving data from http://www.merlot.org/merlot/comments.htm?material=
     * We'll only need this class if ViewMaterial._commetns is <> "none"
     */
public class Comments {
    
    private String _url;
    private String _code;
    private int _numberComments;
    private int _numberPages;

    private int[] _CommentsIDs;

    /**
     * Class' constructor
     * @param MaterialID
     */
    public Comments (int MaterialID)
    {
        _url="http://www.merlot.org/merlot/comments.htm?material=" + MaterialID;
        _code=getCodeWebPage(_url);
        _numberComments=extractNumberComments();
        //System.out.println("_numberComments=" + _numberComments);
        _numberPages=extractNumberPages();
        //System.out.println("_numberPages=" + _numberPages);
        _CommentsIDs = extractTotalCommentsIDs();
        //for(int i=0;i<_CommentsIDs.length;i++)
        //{
        //    System.out.println("_CommentsIDs[" + i + "]=" + _CommentsIDs[i]);
        //}
    }
    /**
     * getCodeWebPage
     * @param MaterialID
     * @return String
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * extractNumberComments()
     * @return int
     */
    private int extractNumberComments()
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
                 {
                     //If end position exists too, we extract the substring
                     //between them, which will be the fact
                     //that we are looking for
                     NumberObjects=_code.substring(StartPosition+
                             StringStart.length()+1,EndPosition-1);
                     NumberObjects = NumberObjects.replaceAll(",", "");
                 }
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on Comments.extractNumberComments " +
                    "HTML code "+e.getMessage());
        }
        int number=Integer.valueOf(NumberObjects);
        if(number==0)
        {
            return 1;
        }
        else
        {
            return number;
        }
    }
    /**
     * extractNumberPages()
     * Method to extract the number of pages with LO's in Merlot
     * @return int
     */
    private int extractNumberPages()
    {   
        String NumberPages= "0";
        String StartString="Results page";
        String EndString="<!--";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {      //If the start string exists, we calculate the end position
                //and extract the substring
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 NumberPages=_code.substring
                         (StartPosition+StartString.length()+6,EndPosition);
                 NumberPages = NumberPages.replace(",", "");
            }
        }
        catch(Exception e)
            {
            System.err.print("Error on Comments.extractNumberPages " +
                    "HTML code "+e.getMessage());
            }
        return Integer.valueOf(NumberPages);
    }
    
    /**
     * extractCommentsIDsInPage()
     * Method to extract the Comments' IDs from a page
     * @args int pageNumber
     * @return int[]
     */
    private int[] extractCommentsIDsInPage(int pageNumber)
    {
        int[] arrayIDsInPage = null;
        String pageUrl;
        String code;
        String IDsInPage="";
        try
        {
            if(pageNumber==1)
            {
                //If it's the first page, the URL is the initial
                pageUrl=_url;
                code=_code;
            }
            else
            {
                pageUrl=_url + "&page=" + pageNumber;
                HTMLParser parser = new HTMLParser(pageUrl);
                code=parser.getSourceCode();
            }
            String firstString="/merlot/viewComment.htm";
            String secondString="?id=";
            String lastString="&backPage";
            int firstStringPosition=0;
            int secondStringPosition=0;
            int lastStringPosition=0;
            while (firstStringPosition!=-1)
            {
                firstStringPosition = code.indexOf(firstString,firstStringPosition);
                if (firstStringPosition!=-1)
                {
                    secondStringPosition=code.indexOf(secondString,firstStringPosition);
                    lastStringPosition=code.indexOf(lastString, secondStringPosition);
                    if (IDsInPage.equals(""))
                    {
                        IDsInPage=code.substring(secondStringPosition+secondString.length(), lastStringPosition);
                    }
                    else
                    {
                        IDsInPage=IDsInPage + ";" + code.substring(secondStringPosition+secondString.length(), lastStringPosition);
                    }
                    firstStringPosition=lastStringPosition;
                }
            }
            String[] temp = IDsInPage.split(";");
            arrayIDsInPage=new int[temp.length];
                for(int i=0;i<temp.length;i++)
                {
                    arrayIDsInPage[i]=Integer.parseInt(temp[i]);
                    //System.out.println(arrayIDsInPage[i]);
                }
        }
        catch(Exception e)
        {
            System.err.print("Error on Comments.extractCommentsIDsInPage "+e.getMessage());
        }
        return arrayIDsInPage;
    }
    /**
     * extractTotalCommentsIDs()
     * Method to extract all the Comments' IDs from the material
     * @return int[]
     */
    private int[] extractTotalCommentsIDs()
    {
        int numberPages = extractNumberPages();
        int countPages = 1;
        int[] arrayTemp = null;
        int[] result = null;
        while (countPages<=numberPages)
        {
            arrayTemp = extractCommentsIDsInPage(countPages);
            if (result==null)
            {
                result = arrayTemp;
            }
            else
            {
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
     * getCommentsIDs()
     * Method to return the Comments' IDs
     * @return int[]
     */
    public int[] getCommentsIDs()
    {
        return _CommentsIDs;
    }
}
