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

import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Gets information from http://www.merlot.org/merlot/reviews.htm?material=
 * @author Victor Apellaniz
 */
public class Reviews {

    private String _url;
    private String _code;
    private int _numberReviews;
    private int _numberPages;

    private int[] _ReviewsIDs;

    /**
     * Constructor
     * @param MaterialID
     */
    public Reviews (int MaterialID)
    {
        _url="http://www.merlot.org/merlot/reviews.htm?material=" + MaterialID;
        _code=getCodeWebPage(_url);
        if(unicoReview())
        {
            _numberReviews=1;
            _numberPages=1;
            try
            {
                HttpURLConnection con = (HttpURLConnection)(new URL( _url ).openConnection());
                con.setInstanceFollowRedirects( false );
                con.connect();
                //int responseCode = con.getResponseCode();
                String location = con.getHeaderField( "Location" );
                String id = location.substring(location.indexOf("id=")+3,location.length());
                //System.out.println(id);
                _ReviewsIDs = new int[1];
                _ReviewsIDs[0]=Integer.parseInt(id);
                //for (int i=0;i<_ReviewsIDs.length;i++)
                //    System.out.println("_ReviewsIDs[" + i + "]=" + _ReviewsIDs[i]);
                //System.out.println( location );
            }
            catch(Exception e)
            {
                System.err.print("Error on extracting url redirection on Reviews' constructor " +
                    "HTML code "+e.getMessage());
            }


        }
        else
        {
            _numberReviews=extractNumberReviews();
            _numberPages=extractNumberPages();
            _ReviewsIDs = extractTotalReviewsIDs();
            //for(int i=0;i<_ReviewsIDs.length;i++)
            //{
            //    System.out.println("_ReviewsIDs[" + i + "]=" + _ReviewsIDs[i]);
            //}
        }
    }
    /**
     * Uses HTMLParser class for getting the web code
     * @param url
     * @return String web code
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * Gets the number of reviews
     * @return number of reviews
     */
    private int extractNumberReviews()
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
            System.err.print("Error on Comments.extractNumberComments " +
                    "HTML code "+e.getMessage());
        }
        int number = Integer.valueOf(NumberObjects);
        if(number==0)
            return 1;
        else
            return number;
    }
    /**
     * Gets the number of pages
     * @return number of pages
     */
    private int extractNumberPages()
    {   //Method to extract the number of pages with LO's in Merlot
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
     * Gets the reviews IDs in a specific page
     * @param pageNumber
     * @return array of ReviewsIDs in pageNumber
     */
    private int[] extractReviewsIDsInPage(int pageNumber)
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
            String firstString="/merlot/viewCompositeReview.htm";
            String secondString="?id=";
            String lastString="\">";
            int firstStringPosition=0;
            int secondStringPosition=0;
            int lastStringPosition=0;
            while (firstStringPosition!=-1)
            {
                firstStringPosition = code.indexOf(firstString,firstStringPosition);
                //System.out.println("firstStringPosition=" + firstStringPosition);
                if (firstStringPosition!=-1)
                {
                    secondStringPosition=code.indexOf(secondString,firstStringPosition);
                    //System.out.println("secondStringPosition=" + secondStringPosition);
                    lastStringPosition=code.indexOf(lastString, secondStringPosition);
                    //System.out.println("lastStringPosition=" + lastStringPosition);
                    if (IDsInPage.equals(""))
                    {
                        IDsInPage=code.substring(secondStringPosition+secondString.length(), lastStringPosition);
                    }
                    else
                    {
                        IDsInPage=IDsInPage + ";" + code.substring(secondStringPosition+secondString.length(), lastStringPosition);
                    }
                    //System.out.println("IDsInPage=" + IDsInPage);
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
            System.err.print("Error on Comments.extractReviewsIDsInPage "+e.getMessage());
        }
        return arrayIDsInPage;
    }
    /**
     * Gets all the reviews IDs
     * @return an array with all the reviews IDs
     */
    private int[] extractTotalReviewsIDs()
    {
        int numberPages = extractNumberPages();
        int countPages = 1;
        int[] arrayTemp = null;
        int[] result = null;
        while (countPages<=numberPages)
        {
            //System.out.println("contador de páginas=" + countPages);
            arrayTemp = extractReviewsIDsInPage(countPages);
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
     * 
     * @return array of commentsIDs
     */
    public int[] getCommentsIDs()
    {
        return _ReviewsIDs;
    }
    /**
     * Checks if there's only 1 review
     * @return if it has only 1 review or not
     */
    private Boolean unicoReview()
    {   //Para saber si solo hay un review
        Boolean esUnicoReview=false;
        String SeekedString="<title>Peer Review</title>";
        //String EndString="<!--";
        try
        {
            int Position=_code.indexOf(SeekedString);
            //StartPosition=_code.indexOf(StartString,StartPosition);
            if(Position!=-1)
                esUnicoReview=true;
        }
        catch(Exception e)
            {
            System.err.print("Error on Comments.unicoReview " +
                    "HTML code "+e.getMessage());
            }
        return esUnicoReview;
    }
    /**
     * 
     * @return number of reviews
     */
    public int getNumberReviews()
    {
        return _numberReviews;
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
     * @return array with the reviews IDs
     */
    public int[] getReviewsIDs()
    {
        return _ReviewsIDs;
    }
}