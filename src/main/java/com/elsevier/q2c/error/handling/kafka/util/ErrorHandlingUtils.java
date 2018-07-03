package com.elsevier.q2c.error.handling.kafka.util;

import java.util.Optional;

public class ErrorHandlingUtils {
	
	public static String removeDotTSuffix(String s) {
		return Optional.ofNullable(s).filter(str -> str.length() != 0 && str.contains(".t"))
				.map(str -> str.substring(0, str.length() - 2)).orElse(s);
	}

}
