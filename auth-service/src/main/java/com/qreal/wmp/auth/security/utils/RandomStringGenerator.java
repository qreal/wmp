package com.qreal.wmp.auth.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/** Util class for password generation.*/
public class RandomStringGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RandomStringGenerator.class);

    private static  Random random = new Random();

    private RandomStringGenerator() {
    }

    /** Static method for generation of random strings. Based on jvm random.*/
    public static String generateString(int lengthOfString) {
        StringBuilder sb = new StringBuilder();
        String randAlph = "qwertyuiop[]asdfghjklzxcvbnm/QWERTYUIOP[]ASDFGHJKLZXCVBNM1234567890-!@#$%^&*()";

        for (int i = 0; i < lengthOfString; i++) {
            char letter = randAlph.charAt(random.nextInt(randAlph.length()));
            sb.append(letter);
        }
        logger.trace("New random string was created {}", sb.toString());
        return sb.toString();

    }
}
