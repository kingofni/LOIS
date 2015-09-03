public class CSVReader {
    public boolean EOF {
        get
        {
            if( this.position < this.csvStringLength )
                return false;
            else
                return true;
        }
    }
           
    private string csvString;
    private integer csvStringLength;
    private integer position = 0;
    
    private static string COMMA = ',';
    private static string NL = '\n';
    private static string QUOTE = '"';
    private static string DOUBLE_QUOTE = '""';

    public CSVReader( string csvString )
    {
        this.csvString = csvString;
        this.csvStringLength = csvString.length();
    }
    
    private integer ReadToLine(integer position, List<string> values)
    {
        integer startPos = position;

        string currentValue;
        integer cAt, nlAt, foundAt;

        List<string> tmpValues = new List<string>();
        while (position < csvStringLength)
        {
            cAt = this.csvString.indexOf( COMMA, position );
            nlAt = this.csvString.indexOf( NL, position );
            foundAt = Math.min( cAt, nlAt );
            if( foundAt == -1 )
                    foundAt = Math.max( cAt, nlAt );


            if (foundAt == -1)
            {
                currentValue = this.csvString.substring(startPos);
                position = this.csvStringLength;
            }
            else
            {
                currentValue = this.csvString.substring(startPos, foundAt );
                position = foundAt + 1;
            }


            if( !currentValue.startsWith( QUOTE ) )
            {
                tmpValues.add( currentValue );

                if( foundAt == nlAt )
                    break;

                startPos = position;
            }
            else if( currentValue.endsWith( QUOTE ) && !currentValue.endsWith( DOUBLE_QUOTE ) )
            {
                if( currentValue.indexOf( DOUBLE_QUOTE ) == -1 )
                    tmpValues.add( currentValue.substring( 1, currentValue.length() - 1 ) );
                else
                    tmpValues.add( currentValue.substring( 1, currentValue.length() - 1 ).replace( DOUBLE_QUOTE, QUOTE ) );
                    
                if( foundAt == nlAt )
                    break;
                
                startPos = position;
            }
        }

        values.addAll( tmpValues );
        return position;
    }

    public List<string> ReadLine()
    {
        List<string> values = new List<string>();
        this.position = this.ReadToLine( this.position, values );
        return values;
    }

    public static boolean Parseandinsert( string csvString )
    {
        system.debug('***' + csvString );
         
        List<Lois_offender__c> lstOff= new List<Lois_offender__c>();
        CSVReader reader = new CSVReader(csvString);
        
        integer firstRow=0;
        while (!reader.EOF)
        {
            List<string> actualValues = reader.ReadLine();
            firstRow ++;
            if(firstRow >1){
               //system.debug('List size:' + actualValues.size());
                              
               system.debug('LoisId:' + actualValues[0]);
               system.debug('lname:' + actualValues[1]);
               system.debug('fname:' + actualValues[2]);
               system.debug('mname:' + actualValues[3]);
               system.debug('ssn:' + actualValues[4]);
               if(actualValues[5]!=null || actualValues[5]!='' ){
                   system.debug('dob:' + actualValues[5]);
                   system.debug('date.parse:'+ date.parse(actualValues[5]));
               }
               system.debug('gender:' + actualValues[6]);
               system.debug('race:' + actualValues[7]);
               
               Lois_offender__c off=new Lois_offender__c();
               if(actualValues[0]!=null)
                   off.Name=actualValues[0];
              
               if(actualValues[1]!=null)
                   off.Last_Name__c=actualValues[1];
                   
               if(actualValues[2]!=null)
                   off.First_Name__c=actualValues[2];
               
               if(actualValues[3]!=null)
                   off.Middle_Name__c=actualValues[3]; 

               if(actualValues[4]!=null)
                   off.Social_Security_Number__c=actualValues[4];
               system.debug('@@@ Social:' + actualValues[4]);

               
               if(actualValues[5]!=null || actualValues[5]!='' )
                   off.Date_of_Birth__c = date.parse(actualValues[5]);
                         
               if(actualValues[6]!=null)   
                   off.Gender__c= actualValues[6];
               
                if(actualValues[7]!=null)   
                   off.Race_Ethnicity__c= actualValues[7];
                      
               lstOff.Add(off);
            }
            
            system.debug('TotalRows:' + firstRow);
   
        }
        if(lstOff.size()>0){
            insert lstOff;
            return true;
          }
          else{
            return false;
          }

    }
 
    
    
    
}