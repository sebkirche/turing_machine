

class TMPreset {

    int initPos;
    String initChars;
    String program;
    String comment;

    public TMPreset(String s) {
        if(s.contains("Busy Beaver") || s.contains("B.B.")) {
            initPos = 15000;
            initChars = "";
            comment = "";
            if(s.contains("501"))
                program = "1,_  2,1,>\n1,1  3,_,<\n2,_  3,1,>\n2,1  4,1,>\n3,_  1,1,<\n3,1  2,_,>\n4,_  5,_,>\n4,1  H,1,>\n5,_  3,1,<\n5,1  1,1,>";
            else if(s.contains("1915"))
                program = "1,_  2,1,>\n1,1  3,1,<\n2,_  1,_,<\n2,1  4,_,<\n3,_  1,1,<\n3,1  H,1,<\n4,_  2,1,<\n4,1  5,1,>\n5,_  4,_,>\n5,1  2,_,>";
            else if(s.contains("4098"))
                program = "1,_  2,1,<\n1,1  3,1,>\n2,_  3,1,<\n2,1  2,1,<\n3,_  4,1,<\n3,1  5,_,>\n4,_  1,1,>\n4,1  4,1,>\n5,_  H,1,<\n5,1  1,_,>";
            else if(s.contains("6-State"))
                program = "1,_  2,1,<\n1,1  1,1,<\n2,_  3,1,>\n2,1  2,1,>\n3,_  6,_,>\n3,1  4,1,>\n4,_  1,1,<\n4,1  5,_,>\n5,_  1,_,<\n5,1  3,1,>\n6,_  5,1,<\n6,1  H,1,<";
            else
                initPos = -1;
        } else if(s.equals("Subtracter")) {
            initPos = 0;
            initChars = "111111-1111=";
            comment = "Subtracts numbers in unary.\nInput format: '<num>-<num>='";
            program = "1,_  1,_,>\n1,1  1,1,>\n1,-  1,-,>\n1,=  2,_,<\n2,1  3,=,<\n2,-  H,_,<\n3,1  3,1,<\n3,-  4,-,<\n4,_  4,_,<\n4,1  1,_,>";
        } else if(s.equals("Palindrome Detector")) {
            initPos = 0;
            initChars = " BABBBAABBBAB";
            comment = "Determines whether a given string of\nA's and B's is a palindrome.\nLeave one blank space before the string.";
            program = "1,_ 2,#,>\n2,A 3,_,>\n2,B 4,_,>\n2,_ 7,_,<\n3,A 3,A,>\n3,B 3,B,>\n3,_ 5,_,<\n4,A 4,A,>\n4,B 4,B,>\n4,_ 6,_,<\n5,A 11,_,<\n5,B 12,_,<\n5,_ 7,_,<\n6,A 12,_,<\n6,B 11,_,<\n6,_ 7,_,<\n7,_ 7,_,<\n7,# 8,_,>\n8,_ 9,Y,>\n9,_ 10,E,>\n10,_ H,S,>\n11,A 11,A,<\n11,B 11,B,<\n11,_ 2,_,>\n12,A 12,_,<\n12,B 12,_,<\n12,_ 12,_,<\n12,# 13,_,>\n13,_ 14,N,>\n14,_ H,O,>\n";
        } else if(s.equals("Bit Duplicator")) {
            initPos = 0;
            initChars = "11";
            comment = "Replicate a sequence of contiguous 1-bits\nand insert a 0 between the two sequences.\nSource: Wikipedia http://fr.wikipedia.org/wiki/Machine_de_Turing";
            program = "1,0 H,_, \n1,1 2,0,>\n2,1 2,1,>\n2,0 3,0,>\n2,_ 3,0,>\n3,1 3,1,>\n3,0 4,1,<\n3,_ 4,1,<\n4,1 4,1,<\n4,0 5,0,<\n5,1 5,1,<\n5,0 1,1,>";
        } else if(s.equals("<New>")) {
            initPos = 0;
            initChars = "";
            program = "";
        } else {
            initPos = -1;
        }
    }

    public static final String names[] = {
        "<New>", "Bit Duplicator", "Subtracter", "Palindrome Detector", "5-State B.B. (501)", "5-State B.B. (1915)", "5-State B.B. (4098)", "6-State Busy Beaver"
    };

}
