package controller;

import java.util.regex.Pattern;

/**
 *
 * @author Игорь
 */
public class LexAnalysator {

    static String patternCreate = "^\\s*CREATE\\s+GROUP\\s+([\\w]+)\\s+WHERE\\s+id\\s*=\\s*\"(.*)\"\\s*$";
    static String patternDrop = "^\\s*DROP\\s+GROUP\\s+([\\w]+)\\s*$";
    static String patternShowGroup = "^\\s*SHOW\\s+GROUP\\s+([\\w]+)\\s*$";
    static String patternSelectAll = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+WHERE\\s+likes\\s*>=\\s*([\\d]+)\\s*$";
    static String patternSelectIntervalPostByLikes = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+WHERE\\s+likes\\s*>=\\s*([\\d]+)\\s*$";
    static String patternSelectIntervalPostByLikesWithoutText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+BY\\s+likes\\s*$";
    static String patternSelectIntervalPostByCommentWithoutText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+BY\\s+comment\\s*$";
    static String patternSelectIntervalPostByLikesDivCommentWithoutText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+BY\\s+likes/comment\\s*$";
    static String patternSelectIntervalPostByCommentDivLikesWithoutText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+BY\\s+comment/likes\\s*$";
    static String patternSelectIntervalPostByText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+WHERE\\s+text\\s*=\\s*([\\w\\W]+)\\s*$";
    static String patternSelectIntervalPostByComment = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+WHERE\\s+comment\\s*>=\\s*([\\d]+)\\s*$";
    static String patternSelectIntervalPostByDependencyCommentDivLike = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+WHERE\\s+comment/likes\\s*>=\\s*([\\d]+.[\\d]+)\\s*$";
    static String patternSelectIntervalPostByDependencyLikeDivComment = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+WHERE\\s+likes/comment\\s*>=\\s*([\\d]+.[\\d]+)\\s*$";
    static String patternSelectIntervalPostByCommentContainsText = "^\\s*SELECT\\s+POST\\s+FROM\\s+([\\w]+)\\s+"
            + "START\\s+([\\d]+)\\s+TO\\s+([\\d]+|\\?)\\s+COMCONTAINS\\s+text\\s*=\\s*([\\w\\W]+)\\s*$";

    public static int getCode(String expr) {
        if (Pattern.matches(patternCreate, expr)) {
            return 1;
        } else if (Pattern.matches(patternDrop, expr)) {
            return 2;
        } else if (Pattern.matches(patternShowGroup, expr)) {
            return 3;
        } else if (Pattern.matches(patternSelectAll, expr)) {
            return 4;
        } else if (Pattern.matches(patternSelectIntervalPostByLikes, expr)) {
            return 5;
        } else if (Pattern.matches(patternSelectIntervalPostByText, expr)) {
            return 6;
        } else if (Pattern.matches(patternSelectIntervalPostByComment, expr)) {
            return 7;
        } else if (Pattern.matches(patternSelectIntervalPostByDependencyCommentDivLike, expr)) {
            return 8;
        } else if (Pattern.matches(patternSelectIntervalPostByDependencyLikeDivComment, expr)) {
            return 9;
        } else if (Pattern.matches(patternSelectIntervalPostByCommentContainsText, expr)) {
            return 10;
        } else if (Pattern.matches(patternSelectIntervalPostByLikesWithoutText, expr)) {
            return 11;
        } else if (Pattern.matches(patternSelectIntervalPostByCommentWithoutText, expr)) {
            return 12;
        } else if (Pattern.matches(patternSelectIntervalPostByLikesDivCommentWithoutText, expr)) {
            return 13;
        } else if (Pattern.matches(patternSelectIntervalPostByCommentDivLikesWithoutText, expr)) {
            return 14;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(getCode("SELECT POST FROM football START 0 TO ? BY comment/likes"));
    }

}
