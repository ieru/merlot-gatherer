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
 * Gets info from http://www.merlot.org/merlot/viewPortfolio.htm?id=
 * @author Victor Apellaniz
 */
public class ViewPortfolio {
    private String _url;
    private String _code;

    private int[] _idsMaterials;

    /**
     * Constructor
     * @param idPortfolio
     */
    public ViewPortfolio(int idPortfolio)
    {
        _url="http://www.merlot.org/merlot/viewPortfolio.htm?id=" + idPortfolio;
        _code=getCodeWebPage(_url);

        if (materialsAssociated())
        {
            //System.out.println("Hay materiales Asociados");
            _idsMaterials=extractIdsMaterials();
            //for(int i=0;i<_idsMaterials.length;i++)
            //{
            //    System.out.println("_idsMaterials["+i+"]="+_idsMaterials[i]);
            //}
        }
        else
        {
            //No materials associated
            _idsMaterials = null;
        }
    }
    /**
     * Gets html code from url using HTMLParser
     * @param url
     * @return html code from url
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * gets if has materials associated or not
     * @return has materials associated or not
     */
    private Boolean materialsAssociated()
    {
        Boolean ma=false;
        String cad="There are no materials in this personal collection";
        if(!_code.contains(cad))
        {
            ma=true;
        }

        return ma;
    }
    /**
     * get array with materials' ids
     * @return an array with materials' ids
     */
    private int[] extractIdsMaterials()
    {
        String temp="";
        Boolean lastMaterial=false;
        String StartString="viewMaterial.htm?id=";
        String EndString="\">";
        int StartPosition=0;
        int EndPosition=0;
        while(!lastMaterial)
        {
            StartPosition=_code.indexOf(StartString,EndPosition);
            if(StartPosition!=-1)
            {
                EndPosition=_code.indexOf(EndString,StartPosition);
                String tempString=_code.substring(StartPosition+StartString.length(),EndPosition);
                //System.out.println("tempString="+tempString);
                if(!tempString.contains(("<")))
                {
                    temp=temp+";"+_code.substring(StartPosition+StartString.length(),EndPosition);
                }
                //System.out.println("Temp="+temp);
            }
            else
            {
                lastMaterial=true;
            }
        }
        temp=temp.substring(1,temp.length());
        //System.out.println("temp="+temp);
        String[] tempArray = temp.split(";");
        int[] returnArray = new int[tempArray.length];
        for(int i=0;i<tempArray.length;i++)
        {
            returnArray[i]=Integer.parseInt(tempArray[i]);
        }
        return returnArray;
    }

    /**
     *
     * @return array of materials' ids
     */
    public int[] getIdsMaterials()
    {
        return _idsMaterials;
    }
}
