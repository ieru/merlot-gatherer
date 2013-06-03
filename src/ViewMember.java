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

import java.util.Date;
/**
 * Gets info from http://www.merlot.org/merlot/viewMember.htm?id=
 * @author Victor Apellaniz
 */
public class ViewMember {
    private String _url;
    private String _code;

    private int _idMember;
    private String _name;
    private String _ribbon;
    private Boolean _author;
    private Boolean _vsb;
    private Boolean _peerReviewer;
    private String _merlotAward;
    private int _idCategory;
    private String _categoryName;
    private String _memberType;
    private Date _lastLogin;
    private Date _memberSince;
    private Boolean _hasPersonalCollections;

    /**
     * Constructor
     * @param id
     */
    public ViewMember(int id)
    {
        _url="http://www.merlot.org/merlot/viewMember.htm?id=" + id;
        _code=getCodeWebPage(_url);

        _idMember=id;
        //System.out.println("_idMember="+_idMember);
        _name=extractName();
        //System.out.println("_name=" + _name);
        _ribbon=extractRibbon();
        //System.out.println("_ribbon=" + _ribbon);
        _author=extractAuthor();
        //System.out.println("_author="+_author);
        _vsb=extractVsb();
        //System.out.println("_vsb=" + _vsb);
        _peerReviewer=extractPeerReviewer();
        //System.out.println("_peerReviewer=" + _peerReviewer);
        _merlotAward=extractMerlotAward();
        //System.out.println("_merlotAward=" + _merlotAward);
        _idCategory=extractIdCategory();
        //System.out.println("_idCategory=" + _idCategory);
        _categoryName=extractCategoryName();
        //System.out.println("_categoryName=" + _categoryName);
        _memberType=extractMemberType();
        //System.out.println("_memberType=" + _memberType);
        _lastLogin=extractLastLogin();
        //System.out.println("_lastLogin=" + _lastLogin);
        _memberSince=extractMemberSince();
        //System.out.println("_memberSince=" + _memberSince);
        _hasPersonalCollections=extractHasPersonalCollections();
        //System.out.println("_hasPersonalCollections="+_hasPersonalCollections);
    }
    /**
     * get code from url using HTMLParser class
     * @param url
     * @return code from url
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * gets member name
     * @return member name
     */
    private String extractName()
    {
        String author="";
        String StartString="style=\"display:inline\">";
        String EndString="</h3></div>";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 author=HTMLParser.deleteHTMLLabels
                         (_code,StartPosition, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractAuthor() "+
                    e.getMessage());
        }
        return HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(author));
    }
    /**
     * get ribbon
     * @return ribbon
     */
    private String extractRibbon()
    {
        String ribbon="None";
        String StartString="/merlot/images/ribbon-";
        String EndString=".gif";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 ribbon=HTMLParser.deleteHTMLLabels
                         (_code,StartPosition, EndPosition);
                 ribbon=HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(ribbon));
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractRibbon() "+
                    e.getMessage());
        }
        return ribbon;
    }
    /**
     * gets is vsb
     * @return is vsb or not
     */
    private Boolean extractVsb()
    {
        Boolean vsb=false;
        String cad="vsb.gif";
        if(_code.contains(cad))
        {
            vsb=true;
        }

        return vsb;
    }
    /**
     * gets is author
     * @return is author or not
     */
    private Boolean extractAuthor()
    {
        Boolean isAuthor=false;
        String cad="authoricon.gif";
        if(_code.contains(cad))
        {
            isAuthor=true;
        }

        return isAuthor;
    }
    /**
     * get is peer reviewer
     * @return is peer reviewer or not
     */
    private Boolean extractPeerReviewer()
    {
        Boolean peerReviewer=false;
        String cad="pr.gif";
        if(_code.contains(cad))
        {
            peerReviewer=true;
        }

        return peerReviewer;
    }
    /**
     * gets merlot award name
     * @return merlot award name
     */
    private String extractMerlotAward()
    {
        String award="None";
        String StartString="taste.merlot.org/MERLOTAwards/";
        String EndString="\">";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 String NewStartString = "alt=\"";
                 int NewStartPosition=_code.indexOf(NewStartString, StartPosition) + NewStartString.length();
                 int EndPosition=_code.indexOf(EndString,NewStartPosition);
                 award=HTMLParser.deleteHTMLLabels
                         (_code,NewStartPosition, EndPosition);
                 award=HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(award));
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractMerlotAward() "+
                    e.getMessage());
        }
        return award;
    }
    /**
     * get last login
     * @return last login
     */
    private Date extractLastLogin()
    {
        Date LastLogin=new Date(0);
        String LastLog="";
        String StartString="Last Login:";
        String EndString="</td></tr>";
        try{
            int StartPosition=_code.indexOf(StartString);

            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             LastLog=HTMLParser.deleteHTMLLabels
                     (_code,StartPosition+StartString.length(),EndPosition);

            }
            LastLogin=HTMLParser.convertDate(LastLog);
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractLastLogin "+e.getMessage());
        }

        return LastLogin;
    }
    /**
     * gets date member since
     * @return date member since
     */
    private Date extractMemberSince()
    {
        Date MemberSince=new Date(0);
        String MemberSin="";
        String StartString="Member Since:";
        String EndString="</td></tr>";
        try{
            int StartPosition=_code.indexOf(StartString);

            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             MemberSin=HTMLParser.deleteHTMLLabels
                     (_code,StartPosition+StartString.length(),EndPosition);

            }
            MemberSince=HTMLParser.convertDate(MemberSin);
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractMemberSince "+e.getMessage());
        }

        return MemberSince;
    }
    /**
     * get member type
     * @return member type
     */
    private String extractMemberType()
    {
        String memberType="";
        String StartString="Member Type:";
        String EndString="</td></tr>";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 memberType=HTMLParser.deleteHTMLLabels
                         (_code,StartPosition, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractMemberType() "+
                    e.getMessage());
        }
        return HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(memberType));
    }
    /**
     * get user's category name
     * @return user's category name
     */
    private String extractCategoryName()
    {
        String CategoryName="";
        String StartString="Primary Discipline:";
        String EndString="Secondary Disciplines:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 if(EndPosition==-1)
                 {
                     String NewEndString="Member Type:";
                     EndPosition=_code.indexOf(NewEndString,StartPosition);
                     if(EndPosition==-1)
                     {
                         NewEndString="Last Login:";
                         EndPosition=_code.indexOf(NewEndString,StartPosition);
                     }
                 }
                 CategoryName=HTMLParser.deleteHTMLLabels
                         (_code,StartPosition, EndPosition);
                 CategoryName=CategoryName.replace(" / ", "/");

            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractCategoryName() "+
                    e.getMessage());
        }
        return HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(CategoryName));
    }
    /**
     * get user's category id
     * @return user's category id
     */
    private int extractIdCategory()
    {
        int categoryId=0;
        String StartString="Primary Discipline:";
        String EndString="Secondary Disciplines:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 StartPosition = StartPosition + StartString.length();
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 if(EndPosition==-1)
                 {
                     String NewEndString="Member Type:";
                     EndPosition=_code.indexOf(NewEndString,StartPosition);
                     if(EndPosition==-1)
                     {
                         NewEndString="Last Login:";
                         EndPosition=_code.indexOf(NewEndString,StartPosition);
                     }
                 }
                 String temp = _code.substring(StartPosition, EndPosition);
                 StartString = "?category=";
                 StartPosition=temp.lastIndexOf(StartString) + StartString.length();
                 EndPosition=temp.indexOf("\">", StartPosition);
                 //System.out.println(temp.substring(StartPosition, EndPosition));
                 categoryId = Integer.parseInt(temp.substring(StartPosition, EndPosition));
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMember.extractIdCategory() "+
                    e.getMessage());
        }
        return categoryId;
    }
    /**
     * get if user has personal collections
     * @return user has personal collections or not
     */
    private Boolean extractHasPersonalCollections()
    {
        Boolean collections=false;
        String cad="Personal Collections";
        if(_code.contains(cad))
        {
            collections=true;
        }

        return collections;
    }

    /**
     *
     * @return member ID
     */
    public int getIdMember()
    {
        return _idMember;
    }
    /**
     *
     * @return is author or not
     */
    public Boolean getAuthor()
    {
        return _author;
    }
    /**
     *
     * @return member name
     */
    public String getName()
    {
        return _name;
    }
    /**
     *
     * @return ribbon
     */
    public String getRibbon()
    {
        return _ribbon;
    }
    /**
     *
     * @return is vsb or not
     */
    public Boolean getVsb()
    {
        return _vsb;
    }
    /**
     *
     * @return is peer reviewer or no
     */
    public Boolean getPeerReviewer()
    {
        return _peerReviewer;
    }
    /**
     *
     * @return merlot award name
     */
    public String getMerlotAward()
    {
        return _merlotAward;
    }
    /**
     *
     * @return user's category id
     */
    public int getIdCategory()
    {
        return _idCategory;
    }
    /**
     *
     * @return user's category name
     */
    public String getCategoryName()
    {
        return _categoryName;
    }
    /**
     *
     * @return member type
     */
    public String getMemberType()
    {
        return _memberType;
    }
    /**
     *
     * @return last login date
     */
    public Date getLastLogin()
    {
        return _lastLogin;
    }
    /**
     *
     * @return date member since
     */
    public Date getMemberSince()
    {
        return _memberSince;
    }
    /**
     *
     * @return user has personal collections or not
     */
    public Boolean hasPersonalCollections()
    {
        return _hasPersonalCollections;
    }

}
