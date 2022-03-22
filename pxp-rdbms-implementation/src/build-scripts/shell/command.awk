# This file contains all the awk commands
# Remove all empty lines
NF==0 {next}

# Remove all comment lines
/^[#]+/ {next}

# Check if this is Oracle and replace all ending line with end by end;
/end$/ { if ( a=="oracle" ) { 
            gsub(/end/,"end;"); 
        }
      }

# Check if this is Oracle and replace all line only containing ';' with /
/^;$/ { if ( a=="oracle" ) { 
            gsub(/;/,"/"); 
        }
      }

# Replace all characters __NL__ by a new line
/__NL__/ {
            gsub( /__NL__/, "\n");
        }

# Replace all double quotes with single quotes when they are not preceeding by json curly brace
# and not included in a date format
/"/ { if ( !match($0, /[{].*[}]/) && !match($0, /to_char[(].*[)]/) ) {
            gsub(/"/,"'");  
      }
    }

# Check if this is Oracle and replace () in all procedures/functions with empty parameters
/(procedure|function).*[(][)]/ {
        if ( a=="oracle" ) { 
            gsub(/[(][)]/,""); 
        }
}
# Print all lines to file
{print $0}
