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

import java.sql.Date;

/**
 * Gets information from http://www.merlot.org/merlot/viewMaterial.htm?id=...
 * @author Victor Apellaniz
 */
public class ViewMaterial {
    private String _url;
    private String _code;

    private String _organizationName;
    private String _authorName;
    private String _authorEmail;
    private int _authorID;
    private int _submitterID;
    private String _title;
    private String _materialType;
    private String _technicalFormat;
    private String _location;
    private Date _dateAdded;
    private Date _dateModified;
    private String _description;
    private String[] _primaryAudience;
    private String _technicalRequirements;
    private String _language;
    private String _materialVersion;
    private Boolean _copyright;
    private Boolean _sourceCodeAvailable;
    //this doesn't exist no more
    //private Boolean _section508Compliant;
    private Boolean _costInvolved;
    private String _creativeCommons;
    //this doesn't exist no more
    //private String _learningManagemetSystems;
    private Boolean _editorsChoice;
    private Boolean _merlotClassic;
    private String _peerReviews;
    private String _comments;
    private String _learningExercises;
    private double _starsReviews;
    private double _starsComments;
    private String _personalCollections;
    private int[] _categoriesIDs;
    private String[] _categoriesNames;
    private String  _mobileCompatibility;
    private Boolean _accessibilityInformationAvailable;
    
    /**
     * Constructor. Needs the material ID
     * @param MaterialID
     */
    public ViewMaterial(int MaterialID)
    {
        _url="http://www.merlot.org/merlot/viewMaterial.htm?id=" + MaterialID;
        //System.out.println(_url);
        _code=getCodeWebPage(_url);

        _organizationName=extractOrganizationName();
        //System.out.println("_organizationName="+_organizationName);
        _authorName=extractAuthorName();
        //System.out.println("_authorName="+_authorName);
        _authorEmail=extractAuthorEmail();
        //System.out.println("_authorEmail="+_authorEmail);
        _authorID=extractAuthorID();
        //System.out.println("_authorID="+_authorID);
        _submitterID=extractSubmitterID();
        //System.out.println("_submitterID="+_submitterID);
        _title=extractTitle();
        //System.out.println("_title="+_title);
        _materialType=extractMaterialType();
        //System.out.println("_materialType="+_materialType);
        _technicalFormat=extractTechnicalFormat();
        //System.out.println("_technicalFormat="+_technicalFormat);
        _location=extractLocation();
        //System.out.println("_location="+_location);
        _dateAdded=extractDateAdded();
        //System.out.println("_dateAdded="+_dateAdded);
        _dateModified=extractDateModified();
        //System.out.println("_dateModified="+_dateModified);
        _description=extractDescription();
        //System.out.println("_description=" + _description);
        _primaryAudience=extractPrimaryAudience();
        //for(int i=0;i<_primaryAudience.length;i++)
        //{
        //    System.out.println("_primaryAudience[" + i + "]=" + _primaryAudience[i]);
        //}
        _technicalRequirements=extractTechnicalRequirements();
        //System.out.println("_technicalRequirements="+_technicalRequirements);
        _language=extractLanguage();
        //System.out.println("_language="+_language);
        _materialVersion=extractMaterialVersion();
        //System.out.println("_materialVersion="+_materialVersion);
        _copyright=extractCopyright();
        //System.out.println("_copyright=" + _copyright);
        _sourceCodeAvailable=extractSourceCodeAvailable();
        //System.out.println("_sourceCodeAvailable=" + _sourceCodeAvailable);
        _costInvolved=extractCostInvolved();
        //System.out.println("_costInvolved=" + _costInvolved);
        _creativeCommons=extractCreativeCommons();
        //System.out.println("_creativeCommons=" + _creativeCommons);
        _editorsChoice=extractEditorsChoice();
        //System.out.println("_editorsChoice=" + _editorsChoice);
        _merlotClassic=extractMerlotClassic();
        //System.out.println("_merlotClassic=" + _merlotClassic);
        _peerReviews=extractPeerReviews();
        //System.out.println("_peerReviews=" + _peerReviews);
        _comments=extractComments();
        //System.out.println("_comments=" + _comments);
        _learningExercises=extractLearningExercises();
        //System.out.println("_learningExercises="+_learningExercises);
        _starsReviews=extractStarsReviews();
        //System.out.println("_starsReviews=" + _starsReviews);
        _starsComments=extractStarsComments();
        //System.out.println("_starsComments=" + _starsComments);
        _personalCollections=extractPersonalCollections();
        //System.out.println("_personalCollections=" + _personalCollections);
        _mobileCompatibility=extractMobileCompatibility();
        //System.out.println("_mobileCompatibility=" + _mobileCompatibility);
        _accessibilityInformationAvailable=extractAccessibilityInformationAvailable();
        //System.out.println("_accessibilityInformationAvailable=" + _accessibilityInformationAvailable);
        String[][] tempArray=extractCategories();
        //System.out.println("lenght tempArray="+tempArray.length);
        _categoriesNames=new String[(tempArray.length)-1];
        _categoriesIDs=new int[(tempArray.length)-1];
        for(int i=1;i<tempArray.length;i++)
        {
            _categoriesNames[i-1]=tempArray[i][0];
            _categoriesIDs[i-1]=Integer.parseInt(tempArray[i][1]);
            //System.out.println("tempArray["+i+"][1]="+tempArray[i][1]);
        }
        //System.out.println("categories lenght="+_categoriesNames.length);
        //for(int i=0;i<_categoriesNames.length;i++)
        //{
        //    System.out.println("_categoriesNames[" + i + "]=" + _categoriesNames[i]);
        //    System.out.println("_categoriesIDs[" + i + "]=" + _categoriesIDs[i]);
        //}
    }
    /**
     * Gets code from webpage using HTMLParser class
     * @param url
     * @return code from url
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * gets organization name
     * @return organization name
     */
    private String extractOrganizationName()
    {
        String Organization="";
        String StartString="<a href=\"mailto";
        String EndString="Submitter:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 Organization=HTMLParser.deleteHTMLLabels
                         (_code,StartPosition, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractOrganizationName() "+
                    e.getMessage());
        }
        return HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(Organization));
    }
    /**
     * gets author name
     * @return author name
     */
    private String extractAuthorName()
    {
        String Author="";
        String StartString="Author:";
        String EndString="<a href=\"mailto";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(EndPosition!=-1)
                {

                 Author=HTMLParser.deleteHTMLLabels(_code,
                         StartPosition+StartString.length(), EndPosition);

                }
                else
                {
                    EndString="Submitter:";
                    EndPosition=_code.indexOf(EndString,StartPosition);
                    Author=HTMLParser.deleteHTMLLabels(_code,
                            StartPosition+StartString.length(), EndPosition);
                }
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractAuthorName() "+e.getMessage());
        }

        return HTMLParser.deleteInvertedBars(HTMLParser.deleteTicks(Author));
    }
    /**
     * get author's email
     * @return author's email
     */
    private String extractAuthorEmail()
    {
        String Email= "";
        String StartString="mailto:";
        String EndString=">";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);

                 Email=_code.substring(StartPosition+StartString.length(),
                         EndPosition-1);

            }
        }
           catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractAuthorEmail "+e.getMessage());
        }
        return Email;
    }
    /**
     * get author ID
     * @return author ID
     */
    private int extractAuthorID()
    {
       int ID_Author=0;
       //String ID="";
       String StartString="Author:";
       String EndString="/merlot/viewMember.htm";
       String SubmitterString="Submitter:";

       try
       {
            int StartPosition=_code.indexOf(StartString);
            int SubmitterPosition=_code.indexOf(SubmitterString,StartPosition);
       if(StartPosition!=-1)
        {
            int EndPosition=_code.indexOf(EndString, StartPosition);
            if(EndPosition<SubmitterPosition)
            {

                    StartString="?id=";
                    //EndString="\">";
                    EndString="\" itemprop=\"author\">";
                    StartPosition=_code.indexOf(StartString, StartPosition);
                    EndPosition=_code.indexOf(EndString,StartPosition);
                    String auth=_code.substring
                            (StartPosition+StartString.length(),EndPosition);
                    //System.out.println("auth="+auth);
                    ID_Author=Integer.parseInt(auth);
            }
        }
       }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractAuthorID "+e.getMessage());
        }

       return ID_Author;
    }
    /**
     * get submitter ID
     * @return submitter ID
     */
    private int extractSubmitterID()
    {
        int SubmitterID=0;
        String Submitter="";
        String StartString="Submitter:";
        String EndString="\">";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                String NewStartString="?id=";
                int NewStartPosition=_code.indexOf(NewStartString,StartPosition);
                 int EndPosition=_code.indexOf(EndString,NewStartPosition);

                 Submitter=HTMLParser.deleteHTMLLabels(_code,NewStartPosition+
                         NewStartString.length(),EndPosition);

                 SubmitterID=Integer.parseInt(Submitter);
            }
        }
          catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractSubmitterID "+e.getMessage());
        }
        return SubmitterID;
    }
    /**
     * gets material title
     * @return material title
     */
    private String extractTitle()
    {   //Method that extracts the Title of the LO from the HTML code
        String Title= ""; //Default Value
        String StartString="\"materialtitle\"";   //Substring from which we
        //know we will find the data we need
        String EndString="<";   //Substring where we stop looking for
        try
        {
            int StartPosition=_code.indexOf(StartString);    //We look for the
            //position of the start string
            if(StartPosition!=-1)
            {
                //We've found the start string we need, now we have to find
                //the properly information
                String NewStartString = ">";
                int NewStartPosition=_code.indexOf(NewStartString,StartPosition);
                if(NewStartPosition!=-1)
                {
                    int EndPosition=_code.indexOf(EndString,NewStartPosition); //We look
                    //for the position of the end string
                    //from the position of start string
                    Title=_code.substring(NewStartPosition+NewStartString.length(),
                        EndPosition); //We extract the information needed
                }
            }

        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractTitle "+
                    e.getMessage());
        }

        return Title;
    }
    /**
     * get material type
     * @return material type
     */
    private String extractMaterialType()
    {
        String MaterialType= "";
        String StartString="Material Type:";
        String EndString="</a>";
        try
        {
            int StartPosition=_code.indexOf(StartString);

            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
                String NewStartString = "\">";
                int NewStartPosition =_code.indexOf(NewStartString,StartPosition);
                int EndPosition=_code.indexOf(EndString,NewStartPosition);
                if(EndPosition==-1)
                {
                    EndPosition=_code.indexOf(EndString,NewStartPosition);
                }
                MaterialType=HTMLParser.deleteHTMLLabels(_code,NewStartPosition+
                        NewStartString.length(),EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractMaterialType "+
                    e.getMessage());
        }
        return MaterialType;
    }
    /**
     * get technical format
     * @return technical format
     */
    private String extractTechnicalFormat()
    {
        String TechnicalFormat= "";
        String StartString="Technical Format:";
        String EndString="</td></tr>";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);

                 TechnicalFormat=HTMLParser.deleteHTMLLabels(_code,StartPosition+
                         StartString.length(),EndPosition);

            }
        }
          catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractTechnicalFormat "+e.getMessage());
        }
        return TechnicalFormat;
    }
    /**
     * get Location
     * @return location
     */
    private String extractLocation()
    {
        String Location= "";
        String StartString="Location:";
        String EndString="onclick=";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            StartString="title=";
            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             Location=_code.substring(StartPosition+7, EndPosition);
             Location=Location.replaceAll(" ", "");
             Location=Location.replaceAll("\"", "");
            }
        }
         catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractLocation "+e.getMessage());
        }
        return Location;
    }
    /**
     * get date added to merlot
     * @return date added to merlot
     */
    private Date extractDateAdded()
    {
        Date DateAdded=new Date(0);
        String DateAdd="";
        String StartString="Date Added to MERLOT:";
        String EndString="</td></tr>";
        try{
            int StartPosition=_code.indexOf(StartString);

            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             DateAdd=HTMLParser.deleteHTMLLabels
                     (_code,StartPosition+StartString.length(),EndPosition);

            }
            DateAdded=HTMLParser.convertDate(DateAdd);
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractDateAdded "+e.getMessage());
        }

        return DateAdded;
    }
    /**
     * get date modified
     * @return date modified
     */
    private Date extractDateModified()
    {
        Date DateModified=new Date(0);
        String DateMod="";
        String StartString="Date Modified in MERLOT:";
        String EndString="</td></tr>";
        try
        {
            int StartPosition=_code.indexOf(StartString);

            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             DateMod=HTMLParser.deleteHTMLLabels
                     (_code,StartPosition+StartString.length(),EndPosition);

            }
            DateModified=HTMLParser.convertDate(DateMod);
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractDateModified "+e.getMessage());
        }

        return DateModified;
    }
    /**
     * get description
     * @return description
     */
    private String extractDescription()
    {
        String Description="";
        String StartString="Description:";
        String EndString="Browse in Categories:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 Description=HTMLParser.deleteHTMLLabels(_code,
                         StartPosition+StartString.length(), EndPosition);
            }
        }
           catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractDescription "+e.getMessage());
        }
        return Description;
    }
    /**
     * get primary audiences
     * @return primary audiences
     */
    private String[] extractPrimaryAudience()
    {
        String[] arrayPrimaryAudience = {""};
        String PrimaryAudience= "";
        String StartString="Primary Audience:";
        String EndString="Learning ManagementSystem:";
        try
        {
            int StartPosition=_code.indexOf(StartString);

            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 if(EndPosition==-1)
                 {
                     EndString="Delivery Platforms:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                 if(EndPosition==-1)
                 {
                     EndString="Mobile Compatibility:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                 if(EndPosition==-1)
                 {
                     EndString="Technical Requirements:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                 if(EndPosition==-1)
                 {
                     EndString="Language:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                   if(EndPosition==-1)
                 {
                     EndString="Material Version:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                   if(EndPosition==-1)
                 {
                     EndString="Copyright:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }

                 PrimaryAudience=HTMLParser.deleteHTMLLabels(_code,StartPosition+
                         StartString.length(),EndPosition);
            }
            //We got the correct string, now we have to separate the different
            //audiences and insert them into an array
            //System.out.println("PrimaryAudience=" + PrimaryAudience);
            arrayPrimaryAudience = PrimaryAudience.split(",");
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractPrimaryAudience "+
                    e.getMessage());
        }
        return arrayPrimaryAudience;
    }
    /**
     * get technical requirements
     * @return technical requirements
     */
    private String extractTechnicalRequirements()
    {
        String TechnicalRequirements="";
        String StartString="Technical Requirements:";
        String EndString="Language:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(EndPosition==-1)
                {
                    EndString="Material Version:";
                    EndPosition=_code.indexOf(EndString,StartPosition);
                }
                if(EndPosition==-1)
                {
                    EndString="Copyright:";
                    EndPosition=_code.indexOf(EndString,StartPosition);
                }

                TechnicalRequirements=HTMLParser.deleteHTMLLabels(_code,
                        StartPosition+StartString.length()+1, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractTechnicalRequirements "+
                    e.getMessage());
        }
        return TechnicalRequirements;
    }
    /**
     * get language
     * @return language
     */
    private String extractLanguage()
    {
        String Language="";
        String StartString="Language:";
        String EndString="More information about this material:";
        //String EndString="Cost Involved:";
        int EndPosition=_code.indexOf(EndString);
        try
        {
            int StartPosition=_code.indexOf(StartString, EndPosition);
            EndString="Material Version:";
            if(StartPosition!=-1)
            {
                 EndPosition=_code.indexOf(EndString,StartPosition);
                 if(EndPosition==-1)
                 {
                     EndString="Cost Involved:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                 Language=HTMLParser.deleteHTMLLabels(_code,
                         StartPosition+StartString.length(),EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractLanguage "+e.getMessage());
        }
        return Language;
    }
    /**
     * get material version
     * @return material version
     */
    private String extractMaterialVersion()
    {
        String MaterialVersion="";
        String StartString="Material Version:";
        //String EndString="Copyright:";
        String EndString="Cost Involved:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                MaterialVersion=HTMLParser.deleteHTMLLabels(_code,
                        StartPosition+StartString.length()+1, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractMaterialVersion "+
                    e.getMessage());
        }
        return MaterialVersion;
    }
    /**
     * get copyright
     * @return copyright or not
     */
    private Boolean extractCopyright()
    {
        String Copyright="";
        String StartString="Copyright:";
        //String EndString="Source Code Available:";
        String EndString="Creative Commons:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 Copyright=HTMLParser.deleteHTMLLabels(_code,
                         StartPosition+StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractCopyright "+e.getMessage());
        }
        if (Copyright.equals("yes"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * get source code available
     * @return source code available or not
     */
    private Boolean extractSourceCodeAvailable()
    {
        String SourceCodeAvailable="";
        String StartString="Source Code Available";
        //String EndString="Section 508 compliant:";
        String EndString="Accessiblity Information Available:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                SourceCodeAvailable=HTMLParser.deleteHTMLLabels
                      (_code,StartPosition+StartString.length()+1, EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractSourceCodeAvailable "+
                    e.getMessage());
        }
         if (SourceCodeAvailable.equals("yes"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * get cost involved
     * @return cost involved or not
     */
    private Boolean extractCostInvolved()
    {
        String CostInvolved="";
        String StartString="Cost Involved:";
        //String EndString="Creative Commons:";
        String EndString="Source Code Available:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                CostInvolved=HTMLParser.deleteHTMLLabels(_code,StartPosition+
                        StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractCostInvolved "+e.getMessage());
        }
         if (CostInvolved.equals("yes"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * get accessibility information available
     * @return accessibility information available or not
     */
    private Boolean extractAccessibilityInformationAvailable()
    {
        String CostInvolved="";
        String StartString="Accessiblity Information Available:";
        String EndString="Copyright:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
                int EndPosition=_code.indexOf(EndString,StartPosition);
                CostInvolved=HTMLParser.deleteHTMLLabels(_code,StartPosition+
                        StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractAccesibilityInformationAvailable  "+e.getMessage());
        }
         if (CostInvolved.equals("yes"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * gets creative commons
     * @return creative commons
     */
    private String extractCreativeCommons()
    {
        String CreativeCommons="";
        String StartString="Creative Commons:";
        String EndString="About this material:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            if(StartPosition!=-1)
            {
              int EndPosition=_code.indexOf(EndString,StartPosition);
              CreativeCommons=HTMLParser.deleteHTMLLabels(_code,StartPosition+
                      StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractCreativeCommons "+
                    e.getMessage());
        }
        return CreativeCommons;
    }
    /**
     * gets Mobile compatibility
     * @return mobile compatibility
     */
    private String extractMobileCompatibility()
    {
        String MobileCompatibility= "";
        String StartString="Mobile Compatibility:";
        String EndString="Technical Requirements:";
        try
            {
            int StartPosition=_code.indexOf(StartString);

            StartPosition=_code.indexOf(StartString,StartPosition);
            if(StartPosition!=-1)
            {
                 int EndPosition=_code.indexOf(EndString,StartPosition);
                 if(EndPosition==-1)
                 {
                     EndString="Language:";
                     EndPosition=_code.indexOf(EndString,StartPosition);
                 }
                 MobileCompatibility=HTMLParser.deleteHTMLLabels(_code,
                         StartPosition+StartString.length(),EndPosition);
            }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractMobileCompatibility "+
                    e.getMessage());
        }
        return MobileCompatibility;
    }
    /**
     * get editors choice
     * @return is editors choice or not
     */
    private Boolean extractEditorsChoice()
    {
        Boolean edChoice=false;
        String cad="choice.gif";
        if(_code.contains(cad))
            edChoice=true;

        return edChoice;
    }
    /**
     * get merlot classic
     * @return is merlot classic or not
     */
    private Boolean extractMerlotClassic() {
        Boolean merlotClassic=false;
        String cad="classic.gif";
        if(_code.contains(cad))
            merlotClassic=true;

        return merlotClassic;
    }
    /**
     * get peer reviews
     * @return peer reviews
     */
    private String extractPeerReviews(){
        String peerReviews = "";
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Peer Reviews";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            peerReviews=CodeFragment.substring(NewStartPosition+1, NewEndPosition);
            peerReviews=peerReviews.replace("<span itemprop=\"reviewCount\">", "");
            peerReviews=peerReviews.replace("</span>", "");
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractPeerReviews "+e.getMessage());
        }
          return peerReviews;
    }
    /**
     * gets comments
     * @return comments
     */
    private String extractComments(){
        String comments = "";
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Comments";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            comments=CodeFragment.substring(NewStartPosition+1, NewEndPosition);
            comments=comments.replace("<span itemprop=\"reviewCount\">", "");
            comments=comments.replace("</span>", "");
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractComments "+e.getMessage());
        }
          return comments;
    }
    /**
     * get learning exercises
     * @return learning exercises
     */
    private String extractLearningExercises(){
        String learningExercises = "";
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Learning Exercises";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            learningExercises=CodeFragment.substring(NewStartPosition+1, NewEndPosition);
            learningExercises=learningExercises.replace("<span itemprop=\"reviewCount\">", "");
            learningExercises=learningExercises.replace("</span>", "");
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractComments "+e.getMessage());
        }
          return learningExercises;
    }
    /**
     * get stars (reviews)
     * @return stars (reviews)
     */
    private double extractStarsReviews(){
        double Stars = 0.0;
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Peer Reviews";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            String Str=CodeFragment.substring(NewEndPosition+1, NewEndPosition+4);
            if(Str.equals("avg"))
                {
                    String StrStar="starYellow.gif";
                    int StarPosition=CodeFragment.indexOf(StrStar);
                    while(StarPosition!=-1)
                    {
                        Stars++;
                        StarPosition=CodeFragment.indexOf(StrStar, StarPosition+1);
                    }
                   String StringHalfStar="halfStarYellow.gif";
                   String StringThreeQuartersStar="threeQuarterStarYellow.gif";
                   String StringQuarterStar="quarterStarYellow.gif";

                   int QuarterStarPosition=CodeFragment.indexOf(StringQuarterStar);
                   int HalfStarPosition=CodeFragment.indexOf(StringHalfStar);
                   int ThreeQuarterStarPosition=CodeFragment.indexOf
                           (StringThreeQuartersStar);
                   if(QuarterStarPosition!=-1)
                   {
                       Stars=Stars+0.25;
                   }
                   else if(HalfStarPosition!=-1)
                   {
                       Stars=Stars+0.5;
                   }
                   else if(ThreeQuarterStarPosition!=-1)
                   {
                        Stars=Stars+0.75;
                   }
                }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractStarsReviews "+e.getMessage());
        }
          return Stars;
    }
    /**
     * get stars (comments)
     * @return stars (comments)
     */
    private double extractStarsComments(){
        double Stars = 0.0;
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Comments";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            String Str=CodeFragment.substring(NewEndPosition+1, NewEndPosition+4);
            if(Str.equals("avg"))
                {
                    String StrStar="starApricot.gif";
                    int StarPosition=CodeFragment.indexOf(StrStar);
                    while(StarPosition!=-1)
                    {
                        Stars++;
                        StarPosition=CodeFragment.indexOf(StrStar, StarPosition+1);
                    }
                   String StringHalfStar="halfStarApricot.gif";
                   String StringThreeQuartersStar="threeQuarterStarApricot.gif";
                   String StringQuarterStar="quarterStarYellow.gif";

                   int QuarterStarPosition=CodeFragment.indexOf(StringQuarterStar);
                   int HalfStarPosition=CodeFragment.indexOf(StringHalfStar);
                   int ThreeQuarterStarPosition=CodeFragment.indexOf
                           (StringThreeQuartersStar);
                   if(QuarterStarPosition!=-1)
                   {
                       Stars=Stars+0.25;
                   }
                   else if(HalfStarPosition!=-1)
                   {
                       Stars=Stars+0.5;
                   }
                   else if(ThreeQuarterStarPosition!=-1)
                   {
                        Stars=Stars+0.75;
                   }
                }
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractStarsComments "+e.getMessage());
        }
          return Stars;
    }
    /**
     * get personal collections
     * @return personal collections
     */
    private String extractPersonalCollections(){
        String personalCollections = "";
        String StartString="About this material:";
        String EndString="Add your own:";
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString, StartPosition);
            String CodeFragment=_code.substring(StartPosition,EndPosition+
                    EndString.length());
            String NewStartString="Personal Collections";
            int NewStartPosition=CodeFragment.indexOf(NewStartString);
            NewStartPosition=CodeFragment.indexOf("(",NewStartPosition);
            int NewEndPosition=CodeFragment.indexOf(")",NewStartPosition);
            personalCollections=CodeFragment.substring(NewStartPosition+1, NewEndPosition);
        }
        catch(Exception e)
        {
            System.err.print("Error on ViewMaterial.extractComments "+e.getMessage());
        }
          return personalCollections;
    }
    /**
     * get categories
     * @return the categories in a "special" array
     * [0][0] Number of Categories
     * [1][0] Name of categorie
     * [2][0] Name of categorie
     * [3][0] Name of categorie
     * [X][0] Name of categorie
     * [1][1] Categorie's ID
     * [2][1] Categorie's ID
     * [3][1] Categorie's ID
     */
    private String[][] extractCategories()
    {   
        String[][] Categories = null;
        String StartString="-<span itemprop=\"educationalAlignment\"";
        String EndString="</a></span>";
        int NumCategories=0;
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.indexOf(EndString,StartPosition);
            while(StartPosition!=-1)
            {
                 //System.out.println(_code.substring(StartPosition, EndPosition));
                 NumCategories++;
                 EndPosition=_code.indexOf(EndString,StartPosition);
                 StartPosition=_code.indexOf(StartString, EndPosition);
            }
                 Categories=new String[NumCategories+1][2];
                 //We insert the number of categories in the [0][0] position
                 Categories[0][0]=String.valueOf(NumCategories);
                 StartPosition=_code.indexOf(StartString);
                 EndPosition=_code.indexOf(StartString,StartPosition+1);

                 int i=0;
                 while (i<NumCategories)
                     {
                     int posId=StartPosition;
                            if(i!=NumCategories-1)
                            {
                                Categories[i+1][0]=HTMLParser.deleteHTMLLabels
                                        (_code,StartPosition+1,EndPosition);
                                //System.out.println("1:"+HTMLParser.deleteHTMLLabels(_code,StartPosition+1,EndPosition));
                                StartPosition=EndPosition;
                                EndPosition=_code.indexOf(StartString,
                                        StartPosition+1);
                            }
                            else
                            {
                                EndString="More information about this material:";
                                EndPosition=_code.indexOf
                                        (EndString,StartPosition);
                                Categories[i+1][0]=HTMLParser.deleteHTMLLabels
                                        (_code, StartPosition+1,EndPosition);
                                //System.out.println("2:"+HTMLParser.deleteHTMLLabels(_code, StartPosition+1,EndPosition));
                            }
                            int posToken=Categories[i+1][0].lastIndexOf('/');
                            String strCat=Categories[i+1][0].substring
                                    (posToken+1);
                            //System.out.println("strCat="+strCat);
                            if(strCat.equals("Corporate")&&_code.contains
                                    ("Executive/Corporate"))
                                strCat="Executive/Corporate";
                            if(strCat.equals("OB"))
                                    strCat="Cross Cultural Management/OB";
                            if(strCat.equals("wellness"))
                                    strCat="Health/wellness";
                            if(strCat.equals("Career Technical Education"))
                                    strCat="Industrial Education/Career Technical Education";
                            String strEndId="\"><span itemprop=\"targetName\">" + strCat;
                            //System.out.println("strEndId="+strEndId);
                            int posEndId=_code.indexOf(strEndId,posId);
                            //System.out.println("posEndId="+posEndId);
                            //System.out.println(_code.substring(posId,posEndId));
                            if(posEndId!=-1)
                            {
                                String id= _code.substring(posEndId-6, posEndId);
                                //System.out.println("id="+id);
                                id=id.replaceAll("y","");
                                id=id.replaceAll("=","");
                                Categories[i+1][1]=id;
                            }
                            i++;
                        }
        }
        catch(Exception e)
        {
            System.err.print("Error extracting Categories "+e.getMessage());
        }
        return Categories;
    }

    /**
     *
     * @return organization name
     */
    public String getOrganizationName()
    {
        return _organizationName;
    }

    /**
     *
     * @return author name
     */
    public String getAuthorName()
    {
        return _authorName;
    }

    /**
     *
     * @return author email
     */
    public String getAuthorEmail()
    {
        return _authorEmail;
    }

    /**
     *
     * @return author ID
     */
    public int getAuthorID()
    {
        return _authorID;
    }

    /**
     *
     * @return submitter ID
     */
    public int getSubmitterID()
    {
        return _submitterID;
    }
    /**
     *
     * @return title
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     *
     * @return material type
     */
    public String getMaterialType()
    {
        return _materialType;
    }

    /**
     *
     * @return technical format
     */
    public String getTechnicalFormat()
    {
        return _technicalFormat;
    }

    /**
     *
     * @return location
     */
    public String getLocation()
    {
        return _location;
    }

    /**
     *
     * @return date added
     */
    public Date getDateAdded()
    {
        return _dateAdded;
    }

    /**
     *
     * @return date modified
     */
    public Date getDateModified()
    {
        return _dateModified;
    }

    /**
     *
     * @return description
     */
    public String getDescription()
    {
        return _description;
    }

    /**
     *
     * @return primary audiences (array)
     */
    public String[] getPrimaryAudience()
    {
        return _primaryAudience;
    }

    /**
     *
     * @return technical requirements
     */
    public String getTechnicalRequirements()
    {
        return _technicalRequirements;
    }

    /**
     *
     * @return language
     */
    public String getLanguage()
    {
        return _language;
    }

    /**
     *
     * @return material version
     */
    public String getMaterialVersion()
    {
        return _materialVersion;
    }

    /**
     *
     * @return copyright
     */
    public Boolean getCopyright()
    {
        return _copyright;
    }

    /**
     *
     * @return source code available
     */
    public Boolean getSourceCodeAvailable()
    {
        return _sourceCodeAvailable;
    }

    /**
     *
     * @return mobile compatibility
     */
    public String getMobileCompatibility()
    {
        return _mobileCompatibility;
    }

    /**
     *
     * @return cost involved
     */
    public Boolean getCostInvolved()
    {
        return _costInvolved;
    }

    /**
     *
     * @return creative commons
     */
    public String getCreativeCommons()
    {
        return _creativeCommons;
    }

    /**
     *
     * @return accessibility information available
     */
    public Boolean getAccessibilityInformationAvailable()
    {
        return _accessibilityInformationAvailable;
    }

    /**
     *
     * @return editor's choice
     */
    public Boolean getEditorsChoice()
    {
        return _editorsChoice;
    }

    /**
     *
     * @return merlot classic
     */
    public Boolean getMerlotClassic()
    {
        return _merlotClassic;
    }

    /**
     *
     * @return peer reviews
     */
    public String getPeerReviews()
    {
        return _peerReviews;
    }

    /**
     *
     * @return comments
     */
    public String getComments()
    {
        return _comments;
    }
    
    /**
     *
     * @return learning exercises
     */
    public String getLearningExercises()
    {
        return _learningExercises;
    }

    /**
     *
     * @return stars (reviews)
     */
    public double getStarsReviews()
    {
        return _starsReviews;
    }

    /**
     *
     * @return stars(comments)
     */
    public double getStarsComments()
    {
        return _starsComments;
    }

    /**
     *
     * @return personal collections
     */
    public String getPersonalCollections()
    {
        return _personalCollections;
    }

    /**
     *
     * @return categories IDs (array)
     */
    public int[] getCategoriesIDs()
    {
        return _categoriesIDs;
    }

    /**
     *
     * @return categories names(array)
     */
    public String[] getCategoriesNames()
    {
        return _categoriesNames;
    }
}