
class MiscUtil {

    MiscUtil() {
    }

    public static boolean isLetterOrDigit(char c) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return s.indexOf(c) > -1;
    }

    public static String[] split(String s, String s1) {
        int i = -1;
        int j = 0;
        while(++i < s.length())
            if(s1.indexOf(s.charAt(i)) > -1)
                j++;
        int k = j + 1;
        String as[] = new String[k];
        i = -1;
        int i1 = 0;
        int j1 = 0;
        while(++i < s.length())
            if(s1.indexOf(s.charAt(i)) > -1) {
                if(i > j1)
                    as[i1++] = s.substring(j1, i);
                j1 = i + 1;
            }
        if(i > j1)
            as[i1++] = s.substring(j1, i);
        if(i1 < k) {
            int l = i1;
            String as1[] = new String[l];
            for(int k1 = 0; k1 < l; k1++)
                as1[k1] = as[k1];

            return as1;
        } else {
            return as;
        }
    }
}
