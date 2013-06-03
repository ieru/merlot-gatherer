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
 * Gets information from http://www.merlot.org/merlot/viewComment.htm?id=
 * @author Victor Apellaniz
 */
public class ViewComment {
    private String _url;
    private String _code;

    private int _idComment;
    private String _material;
    private int _rating;
    private String _classroomUse;
    private String _writtenBy;
    private int _authorID;
    private Date _dateAdded;
    private String _remarks;
    private String _technicalRemarks;

    /**
     * Constructor
     * @param id
     * @param material
     */
    public ViewComment(int id,String material)
    {
        _url="http://www.merlot.org/merlot/viewComment.htm?id=" + id;
        _code=getCodeWebPage(_url);

       _idComment=id;
       //System.out.println("_idComment="+_idComment);
       _material=material;
       //System.out.println("_material="+_material);
       _rating=extractRating("starApricot.gif");
       //System.out.println("_rating="+_rating);
       _classroomUse=extractClassroomUse();
       //System.out.println("_classroomUse="+_classroomUse);
       _writtenBy=extractAuthor();
       //System.out.println("_writtenBy="+_writtenBy);
       _authorID=extractAuthorID();
       //System.out.println("_authorID="+_authorID);
       _dateAdded=extractDateAdded();
       //System.out.println("_dateAdded="+_dateAdded);
       _remarks=extractRemarks();
       //System.out.println("_remarks="+_remarks);
       _technicalRemarks=extractTechnicalRemarks();
       //System.out.println("_technicalRemarks="+_technicalRemarks);
    }
    /**
     * Users HTMLParser class for getting the web code
     * @param url
     * @return web code from url
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * Gets the rating
     * @param StringStar
     * @return rating
     */
    private int extractRating(String StringStar) {
        int rating=0;
        String StrStar=StringStar;
        try
        {
            int StarPosition=_code.indexOf(StrStar);
            while(StarPosition!=-1)
            {
                rating++;
                StarPosition=_code.indexOf(StrStar, StarPosition+1);
            }
        }
        catch(Exception e)
        {
          System.err.print("Error extracting Comment's Rating "+e.getMessage());
        }
        return rating;
    }
    /**
     * Get classroom use
     * @return classroom use
     */
    private String extractClassroomUse() {
        String use="";
        String StartString="Classroom Use:";
        String EndString="Submitted by:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString,StartPosition);
            if(StartPosition!=-1 && EndPosition!=-1)
            {
             use=HTMLParser.deleteHTMLLabels
                     (_code, StartPosition+StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error extracting Comment's classroom use "+
                    e.getMessage());
        }
        return use;
    }
    /**
     * Get the author name
     * @return author name
     */
    private String extractAuthor() {
       String author="";
       String StartString="Submitted by:";
       String EndString="),";
       try
       {
           int StartPosition=_code.indexOf(StartString);
           int EndPosition=_code.indexOf(EndString,StartPosition);
           if(StartPosition!=-1 && EndPosition!=-1)
           {
             author=HTMLParser.deleteHTMLLabels
                     (_code, StartPosition+StartString.length(), EndPosition+1);
           }
       }
       catch(Exception e)
        {
          System.err.print("Error extracting Comment's Author "+e.getMessage());
        }
       return author;
    }
    /**
     * Gets the author ID
     * @return author ID
     */
    private int extractAuthorID() {
       String author="";
       String StartString="Submitted by:";
       String EndString="\">";
       int authorID=0;
       try
       {
           int StartPosition=_code.indexOf(StartString);
           int EndPosition=_code.indexOf(EndString,StartPosition);
           if(StartPosition!=-1 && EndPosition!=-1)
           {
             String NewStartString="viewMember.htm?id=";
             int NewStartPosition=_code.indexOf(NewStartString,StartPosition);
             author=HTMLParser.deleteHTMLLabels
                     (_code, NewStartPosition+NewStartString.length(), EndPosition);
             //System.out.println("authorParseado="+author);
             authorID=Integer.parseInt(author);
           }
       }
       catch(Exception e)
        {
          System.err.print("Error extracting Comment's Author "+e.getMessage());
        }
       return authorID;
    }
    /**
     * Gets the date added
     * @return date added
     */
    private Date extractDateAdded()
    {
        Date DateAdded=new Date(0);
        String DateAdd="";
        String StartString=_writtenBy.substring(_writtenBy.length()-3) + ", ";
        String EndString="Comment:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(EndPosition!=-1)
                {                    
                    DateAdd=HTMLParser.deleteHTMLLabels
                             (_code,StartPosition+StartString.length(), EndPosition);
                }
                else
                {
                    DateAdd=HTMLParser.deleteHTMLLabels(_code,
                            StartPosition+StartString.length(), StartPosition+StartString.length()+12);
                }
            }
            DateAdded=HTMLParser.convertDate(DateAdd);
        }
        catch(Exception e)
        {
            System.err.print("Error extracting Comment's Date Added "+
                    e.getMessage());
        }

        return DateAdded;
    }
    /**
     * Gets the remarks
     * @return remarks
     */
    private String extractRemarks() {
        String remarks="";
        String StartString="Comment:";
        String EndString="Technical Remarks:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString,StartPosition);

            if(StartPosition!=-1 && EndPosition!=-1)
            {
             remarks=HTMLParser.deleteHTMLLabels
                     (_code, StartPosition+StartString.length(), EndPosition);
            }
            else if(StartPosition!=-1 && EndPosition==-1)
            {
               remarks=HTMLParser.deleteHTMLLabels
                    (_code, StartPosition+StartString.length(), _code.length());
            }
        }
        catch(Exception e)
        {
         System.err.print("Error extracting Comment's Remarks "+e.getMessage());
        }
        return remarks;
    }
    /**
     * Gets the technical remarks
     * @return technical remarks
     */
    private String extractTechnicalRemarks() {
        String technicalRemarks="";
        String StartString="Technical Remarks:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.length();
            if(StartPosition!=-1)
            {
             technicalRemarks=HTMLParser.deleteHTMLLabels
                     (_code, StartPosition+StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error extracting Comment's technical remarks"+
                    e.getMessage());
        }
        return technicalRemarks;
    }

    /**
     * 
     * @return comment ID
     */
    public int getIdComment()
    {
        return _idComment;
    }
    /**
     *
     * @return material name
     */
    public String getMaterial()
    {
        return _material;
    }
    /**
     *
     * @return rating
     */
    public int getRating()
    {
        return _rating;
    }
    /**
     *
     * @return classroom use
     */
    public String getClassroomUse()
    {
        return _classroomUse;
    }
    /**
     *
     * @return written by
     */
    public String getWrittenBy()
    {
        return _writtenBy;
    }
    /**
     *
     * @return Date added
     */
    public Date getDateAdded()
    {
        return _dateAdded;
    }
    /**
     *
     * @return remarks
     */
    public String getRemarks()
    {
        return _remarks;
    }
    /**
     *
     * @return technical remarks
     */
    public String getTechnicalRemarks()
    {
        return _technicalRemarks;
    }
    /**
     *
     * @return author id
     */
    public int getAuthorID()
    {
        return _authorID;
    }

}
