package com.guhappy.util;


import java.util.ArrayList;

public class CSVParser {
    // default values
    private static char DELIMITER = ',';

    private static char QUOTE = '"';

    private static char ESCAPE_CHAR = '\\';

    public CSVParser() {
    }

    /**
     * Parses the <code>line</code> and produces an Array of objects
     * 
     * @param line
     * @return An Array of objects holding the parsed csv values
     */
    public Object[] parse(String line) {
        // using a StringBuffer since String objects are immutable and there
        // will be a lot of overhead to create new String objects for every
        // character we add
        StringBuffer buffer = new StringBuffer();

        // an ArrayList to store the parsed values
        ArrayList values = new ArrayList();

        // boolean that holds the state of whether we are inside a quote or not
        boolean insideQuote = false;

        int i = 0;
        while (i < line.length()) {
            // get a character
            char c = line.charAt(i);

            // check to see if the character is an escape
            if (c == ESCAPE_CHAR) {
                // if it is, then check to see if we are at the end of the line
                // if not, then get the next character right after the escape
                // character add it to our buffer and continue
                // if we are at the last character, simply add the escape
                // character to the buffer
                if (line.length() > (i + 1)) {
                    buffer.append(line.charAt(++i));
                } else {
                    buffer.append(line.charAt(i));
                }
            } else if (c == DELIMITER) {
                // if we found a delimiter, then check to see if we are within
                // quotes and if we are then continue without handling the
                // delimiter if we are not inside the quote, then we add it to
                // our <code>values</code> arraylist, create a new StringBuffer
                // and continue parsing
                if (!insideQuote) {
                	if (buffer.length()>0)
                    	values.add(buffer.toString().trim());
                    else 
                    	values.add("");
                    buffer = new StringBuffer();
                }
            } else if (c == QUOTE) {
                // if we found a quote, then negate the current state of the
                // insideQuote variable and continue
                insideQuote = !insideQuote;
            } else {
                // if all cases above fails, then its a character that we need
                // to add to buffer, so add it and continue
                buffer.append(c);
            }

            // increment processed character count
            i++;
        }

        // when we leave the loop above, the buffer might still have data in it
        // if so, then add it to our arraylist
        if (buffer.length() > 0) {
            values.add(buffer.toString().trim());
        }
        // for the last one is none
        if (buffer.length()==0){
        	values.add("");
        }

        // return an Array of objects
        return values.toArray();
    }
}