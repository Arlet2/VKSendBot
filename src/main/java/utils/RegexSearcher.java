package utils;

import utils.exceptions.NotFoundByRegexException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSearcher {


    public String searchFirst(String regex, String input) throws NotFoundByRegexException {
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(input);
        matcher.find();
        try {
            return input.substring(matcher.start(), matcher.end());
        } catch (IllegalStateException e) {
            throw new NotFoundByRegexException(e);
        }
    }

    public int searchFoundsCount(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        int counter = 0;
        while (matcher.find()) counter++;
        return counter;
    }
}
