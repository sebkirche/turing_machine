/**
* HeadDirection
* @author sebastien
* Date: 28/11/12
*/
public enum HeadDirection {
    left('<'),
    right('>'),
    stay(' ');

    private char s;
    HeadDirection(char d){
        s = d;
    }

    char symbol(){
        return s;
    }

    public String toString(){
       return Character.toString(s);
    }
}
