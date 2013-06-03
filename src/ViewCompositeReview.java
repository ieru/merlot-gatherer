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
 * Gets info from http://www.merlot.org/merlot/viewCompositeReview.htm?id=
 * @author Victor Apellaniz
 */
public class ViewCompositeReview {
    private String _url;
    private String _code;

    private int _idReview;
    private String _material;
    private String _overview;
    private String _learningGoals;
    private String _author;
    private Date _dateAdded;
    private String _targetStudentPopulation;
    private String _prerequisiteKnowledgeOrSkills;
    private String _typeOfMaterial;
    private String _recommendedUse;
    private String _technicalRequirements;
    private String[] _contentQuality;
    private String[] _efectiveness;
    private String[] _easeOfUse;
    private String _otherIssuesAndComments;
    private String _commentsFromTheAuthor;
    private int _peerReviewerID;

    /**
     * Constructor. We need the review ID and the material's name
     * @param id
     * @param material
     */
    public ViewCompositeReview(int id,String material)
    {
        _url="http://www.merlot.org/merlot/viewCompositeReview.htm?id=" + id;
        _code=getCodeWebPage(_url);
        //System.out.println(_code);

        _idReview=id;
        //System.out.println("_idReview=" + _idReview);
        _material=material;
        _overview=extractOverview();
        //System.out.println("_overview=" + _overview);
        _learningGoals=extractLearningGoals();
        _author=extractAuthor();
        _dateAdded=extractDateAdded();
        _targetStudentPopulation=extractTargetStudentPopulation();
        _prerequisiteKnowledgeOrSkills=extractPrerequisiteKnowledgeOrSkills();
        _typeOfMaterial=extractTypeOfMaterial();
        _recommendedUse=extractRecommendedUse();
        _technicalRequirements=extractTechnicalRequirements();
        _contentQuality=extractEvaluationAndObservationInfo
                ("Evaluation and Observation",
                "Potential Effectiveness as a Teaching Tool");
        _efectiveness=extractEvaluationAndObservationInfo
                ("Potential Effectiveness as a Teaching Tool",
                "Ease of Use for Both Students and Faculty");
        _easeOfUse=extractEvaluationAndObservationInfo
                ("Ease of Use for Both Students and Faculty",
                "Other Issues and Comments:");
        _otherIssuesAndComments=extractOtherInfo("Other Issues and Comments:");
        _commentsFromTheAuthor=extractOtherInfo("Comments from Author:");
        //System.out.println("_commentsFromTheAuthor=" + _commentsFromTheAuthor);
        _peerReviewerID=extractPeerReviewerID();
    }
    /**
     * Users HTMLParser class to get the page code
     * @param url
     * @return page code from url
     */
    private String getCodeWebPage(String url)
    {
        HTMLParser Parser = new HTMLParser(url);
        return Parser.formatCode();
    }
    /**
     * Gets the overview
     * @return overview
     */
    private String extractOverview()
        {
            String info="";
            String StartString="Overview:";
            String EndString="Learning Goals:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Target Student Population:";
                        EndPosition=_code.indexOf(EndString,StartPosition);

                        if(EndPosition==-1)
                        {
                            EndString="Prerequisite Knowledge or Skills:";
                            EndPosition=_code.indexOf(EndString,StartPosition);

                            if(EndPosition==-1)
                            {
                                EndString="Type of Material:";
                                EndPosition=_code.indexOf(EndString,StartPosition);

                                if(EndPosition==-1)
                                {
                                    EndString="Recommended Use:";
                                    EndPosition=_code.indexOf
                                            (EndString,StartPosition);

                                    if(EndPosition==-1)
                                    {
                                        EndString="Technical Requirements:";
                                        EndPosition=_code.indexOf
                                                (EndString,StartPosition);

                                        if(EndPosition==-1)
                                        {
                                            EndString="Evaluation and Observation";
                                            EndPosition=_code.indexOf
                                                    (EndString,StartPosition);

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels(_code, StartPosition+
                                 StartString.length(), EndPosition);
                        }
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's overview "+
                        e.getMessage());
            }
            return info;
        }
    /**
     * Gets Learning goals
     * @return Learning goals
     */
    private String extractLearningGoals()
        {
            String info="";
            String StartString="Learning Goals:";
            String EndString="Target Student Population:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Prerequisite Knowledge or Skills:";
                        EndPosition=_code.indexOf(EndString,StartPosition);

                        if(EndPosition==-1)
                        {
                            EndString="Type of Material:";
                            EndPosition=_code.indexOf(EndString,StartPosition);

                            if(EndPosition==-1)
                            {
                                EndString="Recommended Use:";
                                EndPosition=_code.indexOf(EndString,StartPosition);

                                if(EndPosition==-1)
                                {
                                    EndString="Technical Requirements:";
                                    EndPosition=_code.indexOf
                                            (EndString,StartPosition);

                                    if(EndPosition==-1)
                                    {
                                        EndString="Evaluation and Observation";
                                        EndPosition=_code.indexOf
                                                (EndString,StartPosition);

                                    }
                                }
                            }
                        }
                    }
                     if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels (_code, StartPosition+
                                 StartString.length(), EndPosition);
                        }
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's learning goals "+
                        e.getMessage());
            }
            return info;
        }
    /**
     * Gets author name
     * @return author name
     */
    private String extractAuthor()
       {
           String author="";
           String StartString="Reviewed:";
           String EndString="by";
           try
           {
               int StartPosition=_code.indexOf(StartString);
               int EndPosition=_code.indexOf(EndString,StartPosition)+
                       EndString.length()+1;
               if(StartPosition!=-1 && EndPosition!=-1)
               {
                 StartString=EndString;
                 EndString="Overview:";
                 StartPosition=EndPosition;
                 EndPosition=_code.indexOf(EndString,StartPosition);

                  if(StartPosition!=-1 && EndPosition!=-1)
                  {
                         author=HTMLParser.deleteHTMLLabels(_code, StartPosition,
                                 EndPosition);
                  }
               }
           }
        catch(Exception e)
        {
            System.err.print("Error extracting Review's Author "+e.getMessage());
        }
           return author;
     }
     /**
      * Gets the date added to merlot
      * @return date added to merlot
      */
     private Date extractDateAdded()
    {
        Date DateAdded=new Date(0);
        String DateAdd="";
        String StartString="Reviewed:";
        String EndString="by";
        try{
            int StartPosition=_code.indexOf(StartString);

            if(StartPosition!=-1)
            {
             int EndPosition=_code.indexOf(EndString,StartPosition);
             DateAdd=HTMLParser.deleteHTMLLabels
                     (_code,StartPosition+StartString.length(),
                     EndPosition-1);

            }
            DateAdded=HTMLParser.convertDate(DateAdd);
        }
        catch(Exception e)
        {
            System.err.print("Error extracting Review's Date Added "+
                    e.getMessage());
        }

        return DateAdded;
    }
     /**
      * Gets target student population
      * @return target student population
      */
    private String extractTargetStudentPopulation()
        {
            String info="";
            String StartString="Target Student Population:";
            String EndString="Prerequisite Knowledge or Skills:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Type of Material:";
                        EndPosition=_code.indexOf(EndString,StartPosition);

                        if(EndPosition==-1)
                        {
                            EndString="Recommended Use:";
                            EndPosition=_code.indexOf(EndString,StartPosition);

                            if(EndPosition==-1)
                            {
                                EndString="Technical Requirements:";
                                EndPosition=_code.indexOf(EndString,StartPosition);

                                if(EndPosition==-1)
                                {
                                    EndString="Evaluation and Observation";
                                    EndPosition=_code.indexOf
                                            (EndString,StartPosition);
                                    if(EndPosition!=-1)
                                    {
                                     info=HTMLParser.deleteHTMLLabels
                                             (_code, StartPosition+
                                             StartString.length(), EndPosition);
                                    }
                                }
                            }
                        }
                    }
                    if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels(_code, StartPosition+
                                 StartString.length(), EndPosition);
                        }
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's target student " +
                        "population "+e.getMessage());
            }
            return info;
        }
    /**
     * Gets prerequisite knowledge of skills
     * @return prerequisite knowledge of skills
     */
    private String extractPrerequisiteKnowledgeOrSkills()
        {
            String info="";
            String StartString="Prerequisite Knowledge or Skills:";
            String EndString="Type of Material:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Recommended Use:";
                        EndPosition=_code.indexOf(EndString,StartPosition);

                        if(EndPosition==-1)
                        {
                            EndString="Technical Requirements:";
                            EndPosition=_code.indexOf(EndString,StartPosition);

                            if(EndPosition==-1)
                            {
                                EndString="Evaluation and Observation";
                                EndPosition=_code.indexOf(EndString,StartPosition);

                            }
                        }
                    }
                    if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels(_code, StartPosition+
                                 StartString.length(), EndPosition);
                        }
        }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's prerequisite " +
                        "knowledge or skills "+e.getMessage());
            }
            return info;
        }
    /**
     * get the type of material
     * @return type of material
     */
    private String extractTypeOfMaterial()
        {
            String info="";
            String StartString="Type of Material:";
            String EndString="Recommended Uses:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Technical Requirements:";
                        EndPosition=_code.indexOf(EndString,StartPosition);

                        if(EndPosition==-1)
                        {
                            EndString="Evaluation and Observation";
                            EndPosition=_code.indexOf(EndString,StartPosition);

                        }
                    }
                    if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels(_code,
                               StartPosition+StartString.length(), EndPosition);
                        }
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's type of material "+
                        e.getMessage());
            }
            return info;
        }
    /**
     * Get recommended use
     * @return recommended use
     */
    private String extractRecommendedUse()
        {
            String info="";
            String StartString="Recommended Uses:";
            String EndString="Technical Requirements:";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1)
                {
                    if(EndPosition==-1)
                    {
                        EndString="Evaluation and Observation";
                        EndPosition=_code.indexOf(EndString,StartPosition);
                    }
                    if(EndPosition!=-1)
                        {
                         info=HTMLParser.deleteHTMLLabels(_code, StartPosition+
                                 StartString.length(), EndPosition);
                        }
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's recommended use "+
                        e.getMessage());
            }
            return info;
        }
    /**
     * get the technical remarks
     * @return technical remarks
     */
    private String extractTechnicalRequirements()
        {
            String info="";
            String StartString="Technical Requirements:";
            String EndString="Evaluation and Observation";
            try
            {
                int StartPosition=_code.indexOf(StartString);
                int EndPosition=_code.indexOf(EndString,StartPosition);
                if(StartPosition!=-1 && EndPosition!=-1)
                {
                 info=HTMLParser.deleteHTMLLabels
                       (_code, StartPosition+StartString.length(), EndPosition);
                }
            }
            catch(Exception e)
            {
                System.err.print("Error extracting Review's technical " +
                        "requirements "+e.getMessage());
            }
            return info;
        }
    /**
     * get evaluation and observation info
     * @param StartString
     * @param EndString
     * @return evaluation and observation info
     */
    private String[] extractEvaluationAndObservationInfo
               (String StartString, String EndString)
       {
           String[] EvAndOb=new String[3];
         try
         {
               int StartPosition=_code.indexOf(StartString)+StartString.length();
               int EndPosition=_code.indexOf(EndString,StartPosition);
               if(StartPosition!=-1 && EndPosition==-1)
                    EndPosition=_code.length();
               if(StartPosition!=-1 && EndPosition!=-1)
               {
                   String rating=String.valueOf
                           (extractRating(StartPosition, EndPosition));
                   String code=_code.substring(StartPosition, EndPosition);
                   String Strengths=extractStrengths(code);
                   String Concerns= extractConcerns(code);
                   EvAndOb[0]=rating;
                   EvAndOb[1]=Strengths;
                   EvAndOb[2]=Concerns;
               }
         }
         catch(Exception e)
            {
                System.err.print("Error extracting Review's evaluation and " +
                        "observation info "+e.getMessage());
            }
           return EvAndOb;
       }
    /**
     * gets rating
     * @param StartPosition
     * @param EndPosition
     * @return rating
     */
    private Double extractRating(int StartPosition,int EndPosition)
       {
           Double rating=0.0;
           String Star="reviewStar.gif";
           String ThreeQuarterStar="threeQuarterReviewStar.gif";
           String HalfStar="halfReviewStar.gif";
           String QuarterStar="quarterReviewStar.gif";
          try
          {
                if(StartPosition!=-1 && EndPosition!=-1)
                {
                 String codeFragment=_code.substring(StartPosition, EndPosition);
                 int StarPosition=codeFragment.indexOf(Star);
                 while(StarPosition!=-1)
                    {
                        rating++;
                        StarPosition=codeFragment.indexOf(Star, StarPosition+1);
                    }
                  int QuarterStarPosition=codeFragment.indexOf(QuarterStar);
                  int HalfStarPosition=codeFragment.indexOf(HalfStar);
                  int ThreeQuarterStarPosition=codeFragment.indexOf
                          (ThreeQuarterStar);
                   if(QuarterStarPosition!=-1)
                       {
                           rating=rating+0.25;
                       }
                       else if(HalfStarPosition!=-1)
                       {
                           rating=rating+0.5;
                       }
                       else if(ThreeQuarterStarPosition!=-1)
                       {
                            rating=rating+0.75;
                       }
                }
          }
          catch(Exception e)
            {
                System.err.print("Error extracting Review's rating "+
                        e.getMessage());
            }
           return rating;
       }
    /**
     * gets strengths
     * @param code
     * @return strengths
     */
    private String extractStrengths(String code)
    {
        String Strengths="";
        String StartString="Strengths:";
        String EndString="Concerns:";
        try
        {
            int StartPosition=code.indexOf(StartString);
            int EndPosition =code.indexOf(EndString, StartPosition);
            if(StartPosition!=-1 && EndPosition!=-1)
            {
                Strengths=HTMLParser.deleteHTMLLabels(code, StartPosition+
                        StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
            {
                System.err.print("Error extracting Review's strengths "+
                        e.getMessage());
            }
        return Strengths;
    }
    /**
     * get concerns
     * @param code
     * @return concerns
     */
    private String extractConcerns(String code)
    {
        String Concerns="";
        String StartString="Concerns:";
        try
        {
            int StartPosition=code.indexOf(StartString);
            int EndPosition=code.length();
            if(StartPosition!=-1 && EndPosition!=-1)
            {
                Concerns=HTMLParser.deleteHTMLLabels
                        (code, StartPosition+StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
            {
                System.err.print("Error extracting Review's concerns "+
                        e.getMessage());
            }
        return Concerns;
    }
    /**
     * get other info
     * @param info
     * @return other info
     */
    private String extractOtherInfo(String info)
    {
        String otherInfo="";
        String StartString=info;
        try
        {
            int StartPosition=_code.indexOf(StartString);
            int EndPosition=_code.length();
            if(StartPosition!=-1 && EndPosition!=-1)
            {
                otherInfo=HTMLParser.deleteHTMLLabels(_code, StartPosition+
                        StartString.length(), EndPosition);
            }
        }
        catch(Exception e)
            {
                System.err.print("Error extracting Review's other info "+
                        e.getMessage());
            }
        return otherInfo;

    }
    /**
     * gets Peer Reviewer ID
     * @return peer reviewer ID
     */
    private int extractPeerReviewerID()
       {
           int id=0;
           String StartString="/merlot/viewMember.htm";
           String EndString="\">";
           try
           {
               int StartPosition=_code.indexOf(StartString);
               if(StartPosition!=-1)
               {
                   StartString="?id=";
                   StartPosition=_code.indexOf(StartString,StartPosition);
                   if(StartPosition!=-1)
                   {
                       int EndPosition=_code.indexOf(EndString,StartPosition);
                       if(EndPosition!=-1)
                       {
                            String temp = _code.substring(StartPosition+StartString.length(),
                                EndPosition);
                            id=Integer.parseInt(temp);
                       }
                   }
               }
           }
        catch(Exception e)
        {
            System.err.print("Error extracting PeerReviewer's ID "+e.getMessage());
        }
           return id;
     }

    /**
     * 
     * @return id Review
     */
    public int getIdReview()
    {
        return _idReview;
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
     * @return overview
     */
    public String getOverview()
    {
        return _overview;
    }
    /**
     *
     * @return learning goals
     */
    public String getLearningGoals()
    {
        return _learningGoals;
    }
    /**
     *
     * @return author name
     */
    public String getAuthor()
    {
        return _author;
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
     * @return target student population
     */
    public String getTargetStudentPopulation()
    {
        return _targetStudentPopulation;
    }
    /**
     *
     * @return prerequisite knowledge of skills
     */
    public String getPrerequisiteKnowledgeOrSkills()
    {
        return _prerequisiteKnowledgeOrSkills;
    }
    /**
     *
     * @return type of material
     */
    public String getTypeOfMaterial()
    {
        return _typeOfMaterial;
    }
    /**
     *
     * @return recommended use
     */
    public String getRecommendedUse()
    {
        return _recommendedUse;
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
     * @return content quality
     */
    public String[] getContentQuality()
    {
        return _contentQuality;
    }
    /**
     *
     * @return effectiveness
     */
    public String[] getEfectiveness()
    {
        return _efectiveness;
    }
    /**
     *
     * @return ease of use
     */
    public String[] getEaseOfUse()
    {
        return _easeOfUse;
    }
    /**
     *
     * @return other issues and comments
     */
    public String getOtherIssuesAndComments()
    {
        return _otherIssuesAndComments;
    }
    /**
     *
     * @return comments from the author
     */
    public String getCommentsFromTheAuthor()
    {
        return _commentsFromTheAuthor;
    }
    /**
     *
     * @return peer reviewer ID
     */
    public int getPeerReviewerID()
    {
        return _peerReviewerID;
    }

}
