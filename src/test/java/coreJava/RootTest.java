package coreJava;

import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.http.annotation.Experimental;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class RootTest {

    private static final Object[] parametersForTestJunitAndMockito_ch4Q2_getNumberTesCase2() {
	//regex: [^\D]*\d\d\d+[^\D]*
	return new Object[] {
		new Object[] { "123", "123" },
		new Object[] { "1234", "1234" },
		new Object[] { "a123a", "123" },
		new Object[] { " 123 ", "123" },
		new Object[] { "\n123\n", "123" },
		new Object[] { "abc123", "123" },
		new Object[] { "\\t\\n123", "123" },
		new Object[] { "  123  ", "123" },
		new Object[] { "abc1234", "1234" },
		new Object[] { "abc12 123  ", "123" },
		new Object[] { "\\n233\\t", "233" },
		new Object[] { "\\n231 \\t", "231" },
		new Object[] { "    123   ", "123" },
		new Object[] { "    125 123 456 ", "125,123,456" },
		new Object[] { "cdefg 345 12bb23", "345" },
		new Object[] { "cdefg 345 12bbb33 678tt", "345,678" },
		new Object[] { "3454585*# 12bbb33 678tt", "3454585,678" },
		new Object[] { "56cdefg 345 12bbb33 678984   ", "345,678984" }, };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q2_getNumberTesCase2(String input,
	    String expected) {
	Assert.assertEquals(
		"Black Box partition testing for inputs that should generate an output String number",
		expected, Root.junitAndMockito_ch4Q2_getNumber(input));
    }
    
    private static final Object[] parametersForTestJunitAndMockito_ch4Q2_getNumberTesCase1() {
	// The partition: Character, special character, white space, empty
	// String (BV), 2 digits only, (2 digits + white space)*
	return new Object[] {
		new Object[] { "abc", "" },
		new Object[] { "\\t\\n", "" },
		new Object[] { "    ", "" },
		new Object[] { "abc12", "" },
		new Object[] { "abc12 12", "" },
		new Object[] { "\\n23\\t", "" },
		new Object[] { "\\n23 \\t", "" },
		new Object[] { "    12   ", "" },
		new Object[] { "    12 12 45 ", "" },
		new Object[] { "", "" }, };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q2_getNumberTesCase1(String input,
	    String expected) {
	Assert.assertEquals(
		"Black box partiton testing: The inputs should generate an empty String Output",
		expected, Root.junitAndMockito_ch4Q2_getNumber(input));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_validPaswords() {
	return new Object[] {
		new Object[] { "raminDutt_12345#" },
		new Object[] { "    raminDutt_12345#" },
		new Object[] { "    raminDutt_12345#         " },
		new Object[] { "    raminDutt   _ #12345" } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_validPaswords(String pwd) {
	Assert.assertEquals(
		"These passwords should be accepted by the validator.", true,
		Root.junitAndMockito_ch4Q1_pwdValidation(pwd));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_pwd_DoesNotContainMixtureLowerAndCapital() {
	return new Object[] {
		new Object[] { "ramin12345_#" },
		new Object[] { "RAMINDUTT12345_#" },
		new Object[] { "       4 59  #   _   12       " } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_pwd_DoesNotContainMixtureLowerAndCapital(
	    String pwd) {
	Assert.assertEquals(
		"The password does not contain any Mixture of lower and capital. It is expected to be rejected",
		false, Root.junitAndMockito_ch4Q1_pwdValidation(pwd));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_pwd_DoesNotContainHash() {
	return new Object[] {
		new Object[] { "aaaP12345aaaaa_" },
		new Object[] { "poffy _   G     12345" } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_pwd_DoesNotContainHash(String pwd) {
	Assert.assertEquals(
		"The password does not contain any hash character. It is expected to be rejected",
		false, Root.junitAndMockito_ch4Q1_pwdValidation(pwd));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_pwd_DoesNotContainInderscore() {
	return new Object[] {
		new Object[] { "aaa12345aaaaa#H" },
		new Object[] { "poffy   H  #    12345" } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_pwd_DoesNotContainInderscore(
	    String pwd) {
	Assert.assertEquals(
		"The password does not contain any inderscore. It is expected to be rejected",
		false, Root.junitAndMockito_ch4Q1_pwdValidation(pwd));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_pwdDigits_lessThan_minDigitsAllowed() {
	return new Object[] {
		new Object[] { "aaaaaaaaaaa_#A" },
		new Object[] { "   #     Z    _" },
		new Object[] { "aa#aaa12aa_aaaaaK" } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_pwdDigits_lessThan_minDigitsAllowed(
	    String pwd) {
	Assert.assertEquals(
		"The passwords is not meeting the required allowe digits. It is expected to be rejected.",
		false, Root.junitAndMockito_ch4Q1_pwdValidation(pwd));
    }

    private static final Object[] parametersForTestJunitAndMockito_ch4Q1_pwdLength_lessThan_minLength() {

	return new Object[] {
		"m123#A",
		"_12a#53J",
		"13_2a#K",
		"1a_25#U",
		"12  #3_P",
		new Object[] { " a12_  12Z" } };
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch4Q1_pwdLength_lessThan_minLength(
	    String pwd) {

	boolean actual = Root.junitAndMockito_ch4Q1_pwdValidation(pwd);
	Assert.assertEquals(
		"The passwords has length less than allowed. It is expected to be rejected.",
		false, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testJunitAndMockito_ch4Q1Test_pwdValidationShouldThrowExceptionForNullInputs() {

	boolean actual = Root.junitAndMockito_ch4Q1_pwdValidation(null);
	Assert.assertEquals(
		"The password is NULL. A null pointer exception is expected.",
		false, actual);
    }

    private static final Object[] getWords() {
	Object[] words = {
		new Object[] { "ramin", "nimar" },
		new Object[] { "poffy", "yffop" },
		new Object[] { "java", "avaj" },
		new Object[] { "San Fransisco", "ocsisnarF naS" },
		new Object[] { "San Jose", "esoJ naS" }, };
	return words;
    }

    // Excercise 3.11.2
    @Test
    @Parameters(method = "getWords")
    public void testJunitAndMockito_ch3Q2_Reversed(String word, String reversed) {
	Assert.assertEquals(Root.junitAndMockito_ch3Q2_reverse(word), reversed);
    }

    @Test(expected = NullPointerException.class)
    public void testJunitAndMockito_ch3Q2_nullWord() {
	Root.junitAndMockito_ch3Q2_reverse(null);
    }

    private static final Object[] parametersForTestJunitAndMockito_ch3Q3_PutAndGet() {
	return new Object[] {
		new Object[] { "ramin", 10 },
		new Object[] { "poffy", 15 },
		new Object[] { "java", 100 },
		new Object[] { null, null },
		new Object[] { null, 10 },
		new Object[] { "ramin", 10 },
		new Object[] { "ramin", 15 },
		new Object[] { null, 100 },
		new Object[] { null, 150 },
		new Object[] { null, 10 },
		new Object[] { null, 101 } };
    }

    public Map<String, Integer> map = null;

    @Before
    public void setUp() {
	map = new HashMap<>();
    }

    @Test
    @Parameters
    public void testJunitAndMockito_ch3Q3_PutAndGet(String key, Integer value) {
	map.put(key, value);
	Assert.assertEquals(value, map.get(key));
    }

    @Test
    public void testJunitAndMockito_ch3Q3_Clear() {
	Object[] objects = parametersForTestJunitAndMockito_ch3Q3_PutAndGet();
	for (Object object : objects) {
	    Object[] obj = (Object[]) object;
	    map.put((String) obj[0], (Integer) obj[1]);
	}

	Assert.assertNotEquals(0, map.size());
	map.clear();
	Assert.assertEquals(0, map.size());

    }

}
