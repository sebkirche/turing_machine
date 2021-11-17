
class TMAction {

    int currentState;
    char currentChar;
    int newState;
    char newChar;
    HeadDirection newDirection;

    public TMAction(int curState, char curChar, int newState, char newChar, HeadDirection newDir) {
        currentState = curState;
        currentChar = curChar;
        this.newState = newState;
        this.newChar = newChar;
        newDirection = newDir;
    }
}
