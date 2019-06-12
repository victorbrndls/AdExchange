package com.harystolho.adexchange.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class StreamUtils {

	/**
	 * @param br
	 * @return the buffered reader content or <code>null</code>
	 */
	public static String readBufferedReader(BufferedReader br) {
		try {
			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			br.close();

			return sb.toString();
		} catch (IOException e) {
			return null;
		}
	}

}
