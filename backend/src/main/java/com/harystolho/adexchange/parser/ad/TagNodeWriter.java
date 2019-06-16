package com.harystolho.adexchange.parser.ad;

import java.util.List;

import org.apache.tomcat.util.security.Escape;
import org.springframework.stereotype.Service;

/**
 * Converts {@link TagNode} to HTML
 * 
 * @author Harystolho
 *
 */
@Service
public class TagNodeWriter {

	public String writeHTML(List<TagNode> tags) {
		StringBuilder sb = new StringBuilder();

		tags.forEach(tag -> appendTagTo(tag, sb));

		return sb.toString();
	}

	private void appendTagTo(TagNode tag, StringBuilder sb) {
		switch (tag.getTag()) {
		case "br":
			appendSingleTagTo(tag, sb);
			break;
		default:
			appendCommonTagTo(tag, sb);
			break;
		}
	}

	private void appendCommonTagTo(TagNode tag, StringBuilder sb) {
		sb.append("<" + tag.getTag() + ">");
		sb.append(Escape.htmlElementContent(tag.getContent()));
		sb.append("</" + tag.getTag() + ">");
	}
	
	private void appendSingleTagTo(TagNode tag, StringBuilder sb) {
		sb.append("<" + tag.getTag() + "/>");
	}

}
